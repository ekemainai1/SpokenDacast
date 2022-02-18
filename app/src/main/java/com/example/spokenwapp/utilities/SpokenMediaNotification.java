package com.example.spokenwapp.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.AsyncTask;
import android.os.Build;
import android.os.RemoteException;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.LruCache;
import android.util.SparseArray;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.media.session.MediaButtonReceiver;

import com.example.spokenwapp.R;
import com.example.spokenwapp.data.model.User;
import com.example.spokenwapp.services.SpokenMediaBrowserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class SpokenMediaNotification<userName, userEmail> extends BroadcastReceiver {

    private static final String TAG = "SpokenMediaNotification";
    private static final int NOTIFICATION_ID = 412;
    public static final String ACTION_PAUSE = "com.example.spokenwapp.pause";
    public static final String ACTION_PLAY = "com.example.spokenwapp.play";
    public static final String ACTION_PREV = "com.example.spokenwapp.prev";
    public static final String ACTION_NEXT = "com.example.spokenwapp.next";
    private static final int MAX_ALBUM_ART_CACHE_SIZE = 1024*1024;
    private final SpokenMediaBrowserService mService;
    private MediaSessionCompat.Token mSessionToken;
    private MediaControllerCompat mController;
    private MediaControllerCompat.TransportControls mTransportControls;
    private final SparseArray<PendingIntent> mIntents = new SparseArray<PendingIntent>();
    private final LruCache<String, Bitmap> mAlbumArtCache;
    private PlaybackStateCompat mPlaybackState;
    private MediaMetadataCompat mMetadata;
    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManagerCompat mNotificationManager;
    private NotificationCompat.Action mPlayPauseAction;
    private String mCurrentAlbumArt;
    private int mNotificationColor;
    private boolean mStarted = false;
    private CompositeDisposable disposable;
    Context context;
    private static final String channelId = "local_music_channelId";

    public SpokenMediaNotification(SpokenMediaBrowserService service) throws RemoteException {
        mService = service;
        updateSessionToken();
        // simple album art cache that holds no more than
        // MAX_ALBUM_ART_CACHE_SIZE bytes:
        mAlbumArtCache = new LruCache<String, Bitmap>(MAX_ALBUM_ART_CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };

        disposable = new CompositeDisposable();
        mNotificationColor = getNotificationColor();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mNotificationManager = mService.getSystemService(NotificationManagerCompat.class);
        }//else {
         //NotificationManager   mNotificationManager = (NotificationManager)mService.getSystemService(Context.NOTIFICATION_SERVICE);
        //}

        String pkg = mService.getPackageName();
        mIntents.put(R.drawable.ic_baseline_pause_circle_outline_24, PendingIntent.getBroadcast(mService, 100,
                new Intent(ACTION_PAUSE).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT));
        mIntents.put(R.drawable.ic_play_circle_outline_black_24dp, PendingIntent.getBroadcast(mService, 100,
                new Intent(ACTION_PLAY).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT));
        mIntents.put(R.drawable.ic_baseline_skip_previous_24, PendingIntent.getBroadcast(mService, 100,
                new Intent(ACTION_PREV).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT));
        mIntents.put(R.drawable.ic_baseline_skip_next_24, PendingIntent.getBroadcast(mService, 100,
                new Intent(ACTION_NEXT).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT));
    }
    protected int getNotificationColor() {
        int notificationColor = 0;
        String packageName = mService.getPackageName();
        try {
            Context packageContext = mService.createPackageContext(packageName, 0);
            ApplicationInfo applicationInfo =
                    mService.getPackageManager().getApplicationInfo(packageName, 0);
            packageContext.setTheme(applicationInfo.theme);
            Resources.Theme theme = packageContext.getTheme();
            TypedArray ta = theme.obtainStyledAttributes(
                    new int[] {android.R.attr.colorPrimary});
            notificationColor = ta.getColor(0, Color.DKGRAY);
            ta.recycle();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return notificationColor;
    }
    /**
     * Posts the notification and starts tracking the session to keep it
     * updated. The notification will automatically be removed if the session is
     * destroyed before {@link #stopNotification} is called.
     */
    public void startNotification() {
        if (!mStarted) {
            mController.registerCallback(mCb);
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_NEXT);
            filter.addAction(ACTION_PAUSE);
            filter.addAction(ACTION_PLAY);
            filter.addAction(ACTION_PREV);
            mService.registerReceiver(this, filter);
            mMetadata = mController.getMetadata();
            mPlaybackState = mController.getPlaybackState();
            mStarted = true;
            // The notification must be updated after setting started to true
            updateNotificationMetadata();
        }
    }
    /**
     * Removes the notification and stops tracking the session. If the session
     * was destroyed this has no effect.
     */
    public void stopNotification() {
        mStarted = false;
        mController.unregisterCallback(mCb);
        try {
            mService.unregisterReceiver(this);
        } catch (IllegalArgumentException ex) {
            // ignore if the receiver is not registered.
        }
        mService.stopForeground(true);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        SpokenLogHelper.d(TAG, "Received intent with action " + action);
        if (ACTION_PAUSE.equals(action)) {
            mTransportControls.pause();
        } else if (ACTION_PLAY.equals(action)) {
            mTransportControls.play();
        } else if (ACTION_NEXT.equals(action)) {
            mTransportControls.skipToNext();
        } else if (ACTION_PREV.equals(action)) {
            mTransportControls.skipToPrevious();
        }
    }
    /**
     * Update the state based on a change on the session token. Called either when
     * we are running for the first time or when the media session owner has destroyed the session
     * (see {@link android.media.session.MediaController.Callback#onSessionDestroyed()})
     */
    private void updateSessionToken() throws RemoteException {
        MediaSessionCompat.Token freshToken = mService.getSessionToken();
        if (mSessionToken == null || !mSessionToken.equals(freshToken)) {
            if (mController != null) {
                mController.unregisterCallback(mCb);
            }
            mSessionToken = freshToken;
            assert mSessionToken != null;
            mController = new MediaControllerCompat(mService, mSessionToken);
            mTransportControls = mController.getTransportControls();
            if (mStarted) {
                mController.registerCallback(mCb);
            }
        }
    }

    private final MediaControllerCompat.Callback mCb = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            mPlaybackState = state;
            SpokenLogHelper.d(TAG, "Received new playback state", state);
            updateNotificationPlaybackState();
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            mMetadata = metadata;
            SpokenLogHelper.d(TAG, "Received new metadata ", metadata);
            updateNotificationMetadata();
        }

        @Override
        public void onSessionDestroyed() {
            super.onSessionDestroyed();
            SpokenLogHelper.d(TAG, "Session was destroyed, resetting to the new session token");
            try {
                updateSessionToken();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private void updateNotificationMetadata() {
        SpokenLogHelper.d(TAG, "updateNotificationMetadata. mMetadata=" + mMetadata);
        if (mMetadata == null || mPlaybackState == null) {
            return;
        }
        updatePlayPauseAction();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannelForMusic(context,channelId);
        }
        mNotificationBuilder = new NotificationCompat.Builder(mService, channelId);
        int playPauseActionIndex = 0;
        // If skip to previous action is enabled
        if ((mPlaybackState.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) != 0) {
            mNotificationBuilder
                    .addAction(R.drawable.ic_baseline_skip_previous_24,
                            mService.getString(R.string.label_previous),
                            mIntents.get(R.drawable.ic_baseline_skip_previous_24));
            playPauseActionIndex = 1;
        }
        mNotificationBuilder.addAction(mPlayPauseAction);
        // If skip to next action is enabled
        if ((mPlaybackState.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_NEXT) != 0) {
            mNotificationBuilder.addAction(R.drawable.ic_baseline_skip_next_24,
                    mService.getString(R.string.label_next),
                    mIntents.get(R.drawable.ic_baseline_skip_next_24));
        }
        MediaDescriptionCompat description = mMetadata.getDescription();
        String fetchArtUrl = null;
        Bitmap art = description.getIconBitmap();
        if (art == null && description.getIconUri() != null) {
            // This sample assumes the iconUri will be a valid URL formatted String, but
            // it can actually be any valid Android Uri formatted String.
            // async fetch the album art icon
            String artUrl = description.getIconUri().toString();
            art = mAlbumArtCache.get(artUrl);
            if (art == null) {
                fetchArtUrl = artUrl;
                // use a placeholder art while the remote art is being downloaded
                art = BitmapFactory.decodeResource(mService.getResources(), R.drawable.branham);
            }
        }
        mNotificationBuilder
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(playPauseActionIndex)  // only show play/pause in compact view
                        .setMediaSession(mSessionToken)
                        .setShowCancelButton(true))
                .setColor(mNotificationColor)
                .setSmallIcon(R.drawable.endtimeeagle)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .setUsesChronometer(true)
                .setContentTitle(description.getTitle())
                .setContentText(description.getSubtitle())
                .setLargeIcon(art);
        updateNotificationPlaybackState();
        mService.startForeground(NOTIFICATION_ID, mNotificationBuilder.build());
        if (fetchArtUrl != null) {
            fetchBitmapFromURLAsync(fetchArtUrl);
        }
    }

    private void updatePlayPauseAction() {
        SpokenLogHelper.d(TAG, "updatePlayPauseAction");
        String playPauseLabel = "";
        int playPauseIcon;
        if (mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            playPauseLabel = mService.getString(R.string.pause);
            playPauseIcon = R.drawable.ic_baseline_pause_circle_outline_24;
        } else {
            playPauseLabel = mService.getString(R.string.play_pause);
            playPauseIcon = R.drawable.ic_play_circle_outline_black_24dp;
        }
        if (mPlayPauseAction == null) {
            mPlayPauseAction = new NotificationCompat.Action(playPauseIcon, playPauseLabel,
                    mIntents.get(playPauseIcon));
        } else {
            mPlayPauseAction.icon = playPauseIcon;
            mPlayPauseAction.title = playPauseLabel;
            mPlayPauseAction.actionIntent = mIntents.get(playPauseIcon);
        }
    }

    private void updateNotificationPlaybackState() {
        SpokenLogHelper.d(TAG, "updateNotificationPlaybackState. mPlaybackState=" + mPlaybackState);
        if (mPlaybackState == null || !mStarted) {
            SpokenLogHelper.d(TAG, "updateNotificationPlaybackState. cancelling notification!");
            mService.stopForeground(true);
            return;
        }
        if (mNotificationBuilder == null) {
            SpokenLogHelper.d(TAG, "updateNotificationPlaybackState. there is no notificationBuilder. Ignoring request to update state!");
            return;
        }
        if (mPlaybackState.getPosition() >= 0) {
            SpokenLogHelper.d(TAG, "updateNotificationPlaybackState. updating playback position to ",
                    (System.currentTimeMillis() - mPlaybackState.getPosition()) / 1000, " seconds");
            mNotificationBuilder
                    .setWhen(System.currentTimeMillis() - mPlaybackState.getPosition())
                    .setShowWhen(true)
                    .setUsesChronometer(true);
            mNotificationBuilder.setShowWhen(true);
        } else {
            SpokenLogHelper.d(TAG, "updateNotificationPlaybackState. hiding playback position");
            mNotificationBuilder
                    .setWhen(0)
                    .setShowWhen(false)
                    .setUsesChronometer(false);
        }
        updatePlayPauseAction();
        mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
    }

    public void fetchBitmapFromURLAsync(final String source) {
        SpokenLogHelper.d(TAG, "getBitmapFromURLAsync: starting AsyncTask to fetch ", source);

        Flowable<Bitmap> bitmap = null;
        try {

            mAlbumArtCache.put(source, bitmap.blockingSingle());
        } catch (Throwable e) {
            SpokenLogHelper.e(TAG, e, "getBitmapFromURLAsync: " + source);
        }

        assert bitmap != null;
        disposable.add(bitmap
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Bitmap>() {

                    @Override
                    public void onNext(Bitmap bitmap1) {
                        if (bitmap1 != null && mMetadata != null &&
                                mNotificationBuilder != null && mMetadata.getDescription() != null &&
                                !source.equals(mMetadata.getDescription().getIconUri())) {
                            // If the media is still the same, update the notification:
                            SpokenLogHelper.d(TAG, "getBitmapFromURLAsync: set bitmap to ", source);
                            mNotificationBuilder.setLargeIcon(bitmap1);
                            mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());

                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                }));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannelForMusic(Context context, String link) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = context.getString(R.string.channel_name_music);
        String description = context.getString(R.string.channel_description_music);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(link, name, importance);
        channel.setDescription(description);
        AudioAttributes att = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManagerCompat notificationManager = context.getSystemService(NotificationManagerCompat.class);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }

    protected void onCleared() {
        if (disposable != null) {
            disposable.clear();
            disposable = null;
        }
    }
}
