package com.example.spokenwapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media.MediaBrowserServiceCompat;
import androidx.media.session.MediaButtonReceiver;
import com.example.spokenwapp.R;
import com.example.spokenwapp.base.SpokenBaseApplication;
import com.example.spokenwapp.data.model.SpokenLocalMusicProvider;
import com.example.spokenwapp.events.SpokenEvents;
import com.example.spokenwapp.players.SpokenMediaPlayer;
import com.example.spokenwapp.utilities.SpokenDacastNotification;
import com.example.spokenwapp.utilities.SpokenLocalAudioFocus;
import com.example.spokenwapp.utilities.SpokenLogHelper;
import com.example.spokenwapp.utilities.SpokenSharedPreferences;
import com.google.android.exoplayer2.SimpleExoPlayer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.example.spokenwapp.localaudio.LocalAudioPageFragment.SEND_SPOKEN_ACTION;
import static com.example.spokenwapp.localaudio.LocalAudioPageFragment.SEND_SPOKEN_ITEM;


public class SpokenMediaBrowserService extends MediaBrowserServiceCompat implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String ANDROID_AUTO_PACKAGE_NAME = "com.google.android.projection.gearhead";
    public static final String PACKAGE_ID = "com.example.spokenwapp";
    private static final String TAG = "SMediaBrowserService";
    public static final String MEDIA_ROOT_ID = "media_root_id";
    public static final String BROWSEABLE_ROOT = "root";
    public static final String PLAYABLE_VIDEO_ROOT = "root_video_id";
    public static final String CURRENT_MEDIA_POSITION = "current_media_position";
    public static final int id = 1500;
    public static int sessionId;
    // Action to thumbs up a media item
    private static final String CUSTOM_ACTION_THUMBS_UP = "thumbs_up";
    MediaSessionCompat mediaSessionCompat;
    public static MediaPlayer mediaPlayer;
    SpokenMediaPlayer spokenMediaPlayer;
    private Uri uri;
    SpokenDacastNotification myPlayerNotification;
    SpokenSharedPreferences spokenSharedPreferences;
    private String songMediaId;
    private List<MediaMetadataCompat> mPlayingQueue;
    private List<MediaSessionCompat.QueueItem> queueItemList;
    private int mCurrentIndexOnQueue;
    private int mState = PlaybackStateCompat.STATE_NONE;
    // Music catalog manager
    private SpokenLocalMusicProvider mMusicProvider;
    private AudioManager mAudioManager;
    private AudioAttributes playbackAttributes;
    private int currentSongPosition;
    Handler mHandler;
    public String playType;
    SpokenLocalAudioFocus spokenLocalAudioFocus;


    @Override
    public void onCreate() {
        super.onCreate();

        SpokenLogHelper.d(TAG, "onCreate");
        spokenSharedPreferences = new SpokenSharedPreferences(this);
        mediaPlayer = new MediaPlayer();
        spokenMediaPlayer = new SpokenMediaPlayer();
        myPlayerNotification = new SpokenDacastNotification();
        mMusicProvider = new SpokenLocalMusicProvider(getApplication());
        mediaPlayer = new MediaPlayer();
        queueItemList = new ArrayList<>();
        spokenLocalAudioFocus = new SpokenLocalAudioFocus();

        /*
        Insert new mediaSession pattern
         */
        mPlayingQueue = new ArrayList<>();

        // Initialize audioManager
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        playbackAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        playType = spokenSharedPreferences.getStreamPlay();
        // Initialize and cache media data
        mMusicProvider.retrieveMedia(new SpokenLocalMusicProvider.Callback() {
            @Override
            public void onMusicCatalogReady(boolean success) {
                mState = success ? PlaybackStateCompat.STATE_STOPPED : PlaybackStateCompat.STATE_ERROR;

            }
        }, getApplicationContext());

        mMusicProvider.getVideoListForPlayBack(new SpokenLocalMusicProvider.Callback() {
            @Override
            public void onMusicCatalogReady(boolean success) {
                mState = success ? PlaybackStateCompat.STATE_STOPPED : PlaybackStateCompat.STATE_ERROR;

            }
        }, getApplicationContext());


        // Register shared preference
        SpokenSharedPreferences.registerSpokenSharedPreferences(this, this);

        // Initialize mediaSession
        initMediaSession();
        // Update playback state
        updatePlaybackState(null);
        // Initialize MediaPlayer Object
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        );
        mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        mHandler = new Handler();

        // Get audio session Id
        sessionId = mediaPlayer.getAudioSessionId();

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        /*Intent intentBrowserService = new Intent(getApplicationContext(), SpokenMediaBrowserService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplication().startForegroundService(intentBrowserService);
        }else {
            getApplication().startService(intentBrowserService);
        }*/
        MediaButtonReceiver.handleIntent(mediaSessionCompat, intent);
        //registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
        SpokenSharedPreferences.registerSpokenSharedPreferences(this,this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName,
                                 int clientUid, @Nullable Bundle rootHints) {
        // (Optional) Control the level of access for the specified package name.
        // You'll need to write your own logic to do this.
        switch (clientPackageName) {// Returns a root ID that clients can use with onLoadChildren() to retrieve
        // the content hierarchy.
            case ANDROID_AUTO_PACKAGE_NAME:
                return new BrowserRoot(BROWSEABLE_ROOT, null);
            case PLAYABLE_VIDEO_ROOT:
                return new BrowserRoot(PLAYABLE_VIDEO_ROOT, null);
            case PACKAGE_ID:
                return new BrowserRoot(PACKAGE_ID, null);
            default:

                return new BrowserRoot(MEDIA_ROOT_ID, null);
        }
    }

    @Override
    public void onLoadChildren(@NonNull String parentId,
                               @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

        if (!mMusicProvider.isInitialized()) {
            // Use result.detach to allow calling result.sendResult from another thread:
            result.detach();
            mMusicProvider.retrieveMedia(new SpokenLocalMusicProvider.Callback() {
                @Override
                public void onMusicCatalogReady(boolean success) {
                    if (success) {
                        loadChildrenAudioItems(parentId, result);
                    } else {
                        updatePlaybackState(getString(R.string.error_no_metadata));
                        result.sendResult(new ArrayList<MediaBrowserCompat.MediaItem>());
                    }
                }
            }, getApplicationContext());
        } else {
            // If our music catalog is already loaded/cached, load them into result immediately
            loadChildrenAudioItems(parentId, result);
        }

        if (!mMusicProvider.isInitialized()) {
            // Use result.detach to allow calling result.sendResult from another thread:
            mMusicProvider.getVideoListForPlayBack(new SpokenLocalMusicProvider.Callback() {
                @Override
                public void onMusicCatalogReady(boolean success) {
                    if (success) {
                        loadChildrenVideoItems(parentId, result);
                    } else {
                        updatePlaybackState(getString(R.string.error_no_metadata));
                        result.sendResult(new ArrayList<MediaBrowserCompat.MediaItem>());
                    }
                }
            }, getApplicationContext());
        }
    }


    public void loadChildrenAudioItems(final String parentId, final Result<List<MediaBrowserCompat.MediaItem>> result){
        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
        List<MediaBrowserCompat.MediaItem> items = new ArrayList<>();
        List<MediaBrowserCompat.MediaItem> itemMedia = new ArrayList<>();
        // Check if this is the root menu:
        if (PACKAGE_ID.equals(parentId)) {
            Log.e("CALL ONE", PACKAGE_ID);
            if(playType.equals("audio")) {
                mPlayingQueue = mMusicProvider.getAllMusicById();
                queueItemList = mMusicProvider.convertToQueue(mPlayingQueue);
                mediaSessionCompat.setQueue(queueItemList);
                mediaSessionCompat.setQueueTitle(getResources().getString(R.string.browse_genres));
            }
            // build the MediaItem objects for the top level,
            // and put them in the mediaItems list
            mediaItems = mMusicProvider.getAllSongsById();

            result.detach();
            if(mediaItems != null){
                result.sendResult(mediaItems);
            }
        } else if(ANDROID_AUTO_PACKAGE_NAME.equals(parentId)){
            Log.e("CALL TWO", ANDROID_AUTO_PACKAGE_NAME);
            // examine the passed parentMediaId to see which submenu we're at,
            // and put the children of that menu in the mediaItems list
            items = mMusicProvider.getPlayableMediaItemByGenre(parentId);
            result.detach();
            if(items != null){
                result.sendResult(items);
            }
        } else {

            if(playType.equals("audio")) {
                mPlayingQueue = mMusicProvider.getAllMusicById();
                queueItemList = mMusicProvider.convertToQueue(mPlayingQueue);
                mediaSessionCompat.setQueue(queueItemList);
                mediaSessionCompat.setQueueTitle(getResources().getString(R.string.browse_genres));
            }

            Log.e("CALL THREE", MEDIA_ROOT_ID);
            itemMedia = mMusicProvider.getAllSongsById();
            result.sendResult(itemMedia);
        }
    }

    public void loadChildrenVideoItems(final String parentId, final Result<List<MediaBrowserCompat.MediaItem>> result){

        List<MediaBrowserCompat.MediaItem> itemsVideos = new ArrayList<>();

        if(PLAYABLE_VIDEO_ROOT.equals(parentId)){
            if(playType.equals("video")) {
                mPlayingQueue = mMusicProvider.getAllVideos();
            }
            itemsVideos = mMusicProvider.getAllVideosById();
            if(itemsVideos != null){
                result.sendResult(itemsVideos);
            }
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if( mediaPlayer != null ) {
            stopMedia(null);
            PreferenceManager.getDefaultSharedPreferences( this ).edit().putInt( CURRENT_MEDIA_POSITION,
                    0 ).apply();
        }

        //unregisterReceiver(myNoisyAudioStreamReceiver);
        SpokenSharedPreferences.unregisterSpokenSharedPreferences(this, this);
        // Service is being killed, so make sure we release our resources

        // In particular, always release the MediaSession to clean up resources
        // and notify associated MediaController(s).
        mediaSessionCompat.release();
    }

    MediaSessionCompat.Callback mediaSessionCallback = new
            MediaSessionCompat.Callback() {

                @Override
                public void onPlay() {
                    mState = PlaybackStateCompat.STATE_PLAYING;
                    Log.e("PLAY IS CALLED", "onPlay");
                    Log.e("MUSIC SIZE", String.valueOf(queueItemList.size()));
                    Log.e("FILE", spokenSharedPreferences.getSongMediaUri());
                    mediaSessionCompat.setQueue(queueItemList);
                    mediaSessionCompat.setQueueTitle(getString(R.string.random_queue_title));
                    if (queueItemList == null || queueItemList.isEmpty()) {
                        // start playing from the beginning of the queue
                        mCurrentIndexOnQueue = 0;
                    }

                    if(mediaPlayer.isPlaying()){
                        pauseMedia();
                        mState = PlaybackStateCompat.STATE_PAUSED;
                    }else {
                        mediaPlayer.seekTo(currentSongPosition);
                        mediaPlayer.start();
                    }

                    if (queueItemList != null && !queueItemList.isEmpty()) {
                        if (mState == PlaybackStateCompat.STATE_STOPPED) {

                            updateMetadata();
                            // start the player (custom call)
                            playMedia(mCurrentIndexOnQueue, queueItemList.get(mCurrentIndexOnQueue)
                                    .getDescription().getMediaId());
                            mState = PlaybackStateCompat.STATE_PLAYING;
                            updatePlaybackState(null);
                            NotificationCompat.Builder builder =
                                    myPlayerNotification.sendLocalMediaPlaybackNotification(getApplicationContext(),
                                            mediaSessionCompat,
                                            queueItemList.get(mCurrentIndexOnQueue));
                            startForeground(id, builder.build());

                        }
                    }

                    sendMessageBroadCast(String.valueOf(queueItemList.get(mCurrentIndexOnQueue).getDescription().getTitle()));
                    spokenSharedPreferences.saveMediaMetadataPosition(mCurrentIndexOnQueue);
                    sendItemBroadCast(mCurrentIndexOnQueue);
                }

                @Override
                public void onStop() {
                    // stop the player (custom call)
                    stopMedia(null);
                    //unregisterReceiver(myNoisyAudioStreamReceiver);
                    // Stop the service
                    stopSelf();
                    // Set the session inactive  (and update metadata and state)
                    mediaSessionCompat.setActive(false);

                    // Take the service out of the foreground
                    stopForeground(false);
                }

                @Override
                public void onPause() {
                    Log.e("PAUSE IS CALLED", "onPause");
                    pauseMedia();
                    mState = PlaybackStateCompat.STATE_PAUSED;
                }

                @Override
                public void onSkipToQueueItem(long queueId) {
                    SpokenLogHelper.d(TAG, "OnSkipToQueueItem:" + queueId);
                    if (queueItemList != null && !queueItemList.isEmpty()) {
                        // set the current index on queue from the music Id:
                        mCurrentIndexOnQueue = mMusicProvider.getMusicIndexOnQueue(queueItemList, String.valueOf(queueId));
                        // play the music
                        playMedia(mCurrentIndexOnQueue, queueItemList.get(mCurrentIndexOnQueue)
                                .getDescription().getMediaId());
                    }
                }


                @Override
                public void onPlayFromMediaId(String mediaId, Bundle extras) {
                    super.onPlayFromMediaId(mediaId, extras);

                    queueItemList = mMusicProvider.convertToQueue(mPlayingQueue);
                    mediaSessionCompat.setQueue(queueItemList);
                    mediaSessionCompat.setQueueTitle(getResources().getString(R.string.browse_by_genre_subtitle));
                    if(queueItemList.isEmpty()){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SpokenMediaBrowserService.this,
                                        "Catalog loading...", Toast.LENGTH_LONG).show();
                            }
                        });
                    }else {
                        // Set the session active  (and update metadata and state)
                        Log.e("MUSIC SIZE", String.valueOf(queueItemList.size()));
                        if (mediaPlayer != null) {
                            if (mediaPlayer.isPlaying()) {
                                relaxResources(true);
                            }
                        }

                        mCurrentIndexOnQueue = mMusicProvider.getMusicIndexOnQueue(queueItemList, mediaId);
                        mState = PlaybackStateCompat.STATE_PLAYING;
                        updateMetadata();
                        playMedia(mCurrentIndexOnQueue,
                                queueItemList.get(mCurrentIndexOnQueue).getDescription().getMediaId());
                        updatePlaybackState(null);

                        NotificationCompat.Builder builder =
                                myPlayerNotification.sendLocalMediaPlaybackNotification(getApplicationContext(),
                                        mediaSessionCompat,
                                        queueItemList.get(mCurrentIndexOnQueue));
                        startForeground(id, builder.build());
                    }
                    spokenSharedPreferences.saveMediaMetadataPosition(mCurrentIndexOnQueue);
                    //sendItemBroadCast(mCurrentIndexOnQueue);

                }

                @Override
                public void onSkipToNext() {
                    SpokenLogHelper.d(TAG, "skipToNext");

                    if(mediaPlayer.isPlaying()){
                        relaxResources(true);
                    }

                    mCurrentIndexOnQueue++;
                    if (queueItemList != null && mCurrentIndexOnQueue >= queueItemList.size()) {
                        mCurrentIndexOnQueue = 0;
                    }
                    mState = PlaybackStateCompat.STATE_PLAYING;
                    updateMetadata();
                    playMedia(mCurrentIndexOnQueue, queueItemList
                            .get(mCurrentIndexOnQueue).getDescription().getMediaId());
                    updatePlaybackState(null);
                    NotificationCompat.Builder builder =
                            myPlayerNotification.sendLocalMediaPlaybackNotification(getApplicationContext(),
                                    mediaSessionCompat,
                                   queueItemList.get(mCurrentIndexOnQueue));
                    startForeground(id, builder.build());
                    sendMessageBroadCast(String.valueOf(mPlayingQueue.get(mCurrentIndexOnQueue).getDescription().getTitle()));
                    spokenSharedPreferences.saveMediaMetadataPosition(mCurrentIndexOnQueue);
                    sendItemBroadCast(mCurrentIndexOnQueue);
                }

                @Override
                public void onSkipToPrevious() {
                    SpokenLogHelper.d(TAG, "skipToPrevious");
                    if(mediaPlayer.isPlaying()){
                        relaxResources(true);
                    }

                    Log.e("ONE PT", String.valueOf(mCurrentIndexOnQueue));
                    mCurrentIndexOnQueue--;
                    Log.e("ONE PT", String.valueOf(mCurrentIndexOnQueue));
                    if (queueItemList != null && mCurrentIndexOnQueue < 0) {
                        // This sample's behavior: skipping to previous when in first song restarts the
                        // first song.
                        mCurrentIndexOnQueue = 0;
                    }

                    mState = PlaybackStateCompat.STATE_PLAYING;
                    updateMetadata();

                    playMedia(mCurrentIndexOnQueue, queueItemList.get(mCurrentIndexOnQueue)
                            .getDescription().getMediaId());
                    updatePlaybackState(null);
                    NotificationCompat.Builder builder =
                            myPlayerNotification.sendLocalMediaPlaybackNotification(getApplicationContext(),
                                    mediaSessionCompat,
                                    queueItemList.get(mCurrentIndexOnQueue));
                    startForeground(id, builder.build());
                    sendMessageBroadCast(String.valueOf(queueItemList.get(mCurrentIndexOnQueue).getDescription().getTitle()));
                    spokenSharedPreferences.saveMediaMetadataPosition(mCurrentIndexOnQueue);
                    sendItemBroadCast(mCurrentIndexOnQueue);
                }


                @Override
                public void onCustomAction(String action, Bundle extras) {
                    if (CUSTOM_ACTION_THUMBS_UP.equals(action)) {
                        SpokenLogHelper.i(TAG, "onCustomAction: favorite for current track");
                        MediaMetadataCompat track = getCurrentPlayingMusic();
                        if (track != null) {
                            String mediaId = track.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
                            Log.e("Favorite Track", mediaId);
                        }
                        updatePlaybackState(null);
                    } else {
                        SpokenLogHelper.e(TAG, "Unsupported action: ", action);
                    }
                }
    };

    /*
     * Called when media player is done playing current song.
     * @see android.media.MediaPlayer.OnCompletionListener
     */
    @Override
    public void onCompletion(MediaPlayer player) {
        SpokenLogHelper.d(TAG, "onCompletion from MediaPlayer");
        // The media player finished playing the current song, so we go ahead
        // and start the next.

        mCurrentIndexOnQueue++;
        if (queueItemList != null && !queueItemList.isEmpty()) {
            // In this sample, we restart the playing queue when it gets to the end:
            if (mCurrentIndexOnQueue < (mPlayingQueue.size() - 1)) {
                //mCurrentIndexOnQueue++;
                if (mCurrentIndexOnQueue >= queueItemList.size()) {
                    mCurrentIndexOnQueue = 0;
                }

                updateMetadata();
                // start the player (custom call)
                playMedia(mCurrentIndexOnQueue, queueItemList.get(mCurrentIndexOnQueue)
                        .getDescription().getMediaId());
                mState = PlaybackStateCompat.STATE_PLAYING;
                updatePlaybackState(null);
                NotificationCompat.Builder builder =
                        myPlayerNotification.sendLocalMediaPlaybackNotification(getApplicationContext(),
                                mediaSessionCompat,
                                queueItemList.get(mCurrentIndexOnQueue));
                startForeground(id, builder.build());

            } else {
                // play first song
                mCurrentIndexOnQueue = 0;
                mState = PlaybackStateCompat.STATE_PLAYING;
                updateMetadata();
                // start the player (custom call)
                playMedia(mCurrentIndexOnQueue, queueItemList.get(mCurrentIndexOnQueue)
                        .getDescription().getMediaId());
                updatePlaybackState(null);

                NotificationCompat.Builder builder =
                        myPlayerNotification.sendLocalMediaPlaybackNotification(getApplicationContext(),
                                mediaSessionCompat,
                                queueItemList.get(mCurrentIndexOnQueue));
                startForeground(id, builder.build());


            }

            spokenSharedPreferences.saveMediaMetadataPosition(mCurrentIndexOnQueue);

        } else {
            // If there is nothing to play, we stop and release the resources:
           stopMedia(null);
        }
        sendMessageBroadCast(String.valueOf(mPlayingQueue.get(mCurrentIndexOnQueue).getDescription().getTitle()));
        spokenSharedPreferences.saveMediaMetadataPosition(mCurrentIndexOnQueue);
        sendItemBroadCast(mCurrentIndexOnQueue);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();

    }

    /**
     * Called when there's an error playing media. When this happens, the media
     * player goes to the Error state. We warn the user about the error and
     * reset the media player.
     *
     * @see android.media.MediaPlayer.OnErrorListener
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        SpokenLogHelper.e(TAG, "Media player error: what=" + what + ", extra=" + extra);
        stopMedia("MediaPlayer error " + what + " (" + extra + ")");

        mCurrentIndexOnQueue++;
        if (queueItemList != null && mCurrentIndexOnQueue >= queueItemList.size()) {
            mCurrentIndexOnQueue = 0;
        }
        mState = PlaybackStateCompat.STATE_PLAYING;
        updateMetadata();
        playMedia(mCurrentIndexOnQueue, queueItemList
                .get(mCurrentIndexOnQueue).getDescription().getMediaId());
        updatePlaybackState(null);
        NotificationCompat.Builder builder =
                myPlayerNotification.sendLocalMediaPlaybackNotification(getApplicationContext(),
                        mediaSessionCompat,
                        queueItemList.get(mCurrentIndexOnQueue));
        startForeground(id, builder.build());
        sendMessageBroadCast(String.valueOf(queueItemList.get(mCurrentIndexOnQueue).getDescription().getTitle()));
        return true; // true indicates we handled the error
    }


    private void initMediaSession() {
        // Create a MediaSessionCompat
        mediaSessionCompat = new MediaSessionCompat(this, "SpokenMediaBrowserService");
        // Set the session active  (and update metadata and state)
        mediaSessionCompat.setActive(true);
        // Enable callbacks from MediaButtons and TransportControls
        mediaSessionCompat.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS |
                        MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS);
       updatePlaybackState(null);
        // MySessionCallback() has methods that handle callbacks from a media controller
        mediaSessionCompat.setCallback(mediaSessionCallback);
        // Set the session's token so that client activities can communicate with it.
        setSessionToken(mediaSessionCompat.getSessionToken());
    }


    private void initMediaMetaData( String mediaId) {
        MediaMetadataCompat track = mPlayingQueue.get(mCurrentIndexOnQueue);
        Bitmap bitmap = null;

            if(!(TextUtils.isEmpty(String.valueOf(track.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID))) &&
                    (queueItemList.get(mCurrentIndexOnQueue).getDescription().getMediaId()).equalsIgnoreCase(mediaId))){
                MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
                try {
                    bitmap = retrieveAudioFrameFromAudio(track.getString(MediaMetadataCompat.METADATA_KEY_ART_URI));
                } catch (Throwable throwable) {
                throwable.printStackTrace();
                }

                if( !TextUtils.isEmpty( track.getString(MediaMetadataCompat.METADATA_KEY_TITLE)))
                    builder.putText( MediaMetadataCompat.METADATA_KEY_TITLE, track.getString(MediaMetadataCompat.METADATA_KEY_TITLE));

                if( !TextUtils.isEmpty(track.getString(MediaMetadataCompat.METADATA_KEY_ARTIST)))
                    builder.putText(MediaMetadataCompat.METADATA_KEY_ARTIST, track.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));

                if( !TextUtils.isEmpty(track.getString(MediaMetadataCompat.METADATA_KEY_GENRE)))
                    builder.putText(MediaMetadataCompat.METADATA_KEY_GENRE, track.getString(MediaMetadataCompat.METADATA_KEY_GENRE));

                if( !TextUtils.isEmpty(track.getString(MediaMetadataCompat.METADATA_KEY_ALBUM)))
                    builder.putText(MediaMetadataCompat.METADATA_KEY_ALBUM,track.getString(MediaMetadataCompat.METADATA_KEY_ALBUM));

                if( !TextUtils.isEmpty(track.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI)))
                    builder.putText(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, track.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI));
                if(bitmap != null) {
                    if (!TextUtils.isEmpty(track.getString(MediaMetadataCompat.METADATA_KEY_ART)))
                        builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ART, bitmap);
                }else {
                    if (!TextUtils.isEmpty(track.getString(MediaMetadataCompat.METADATA_KEY_ART)))
                        builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ART,
                                BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.branham));
                }
                if( !TextUtils.isEmpty(track.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)))
                    builder.putText( MediaMetadataCompat.METADATA_KEY_MEDIA_URI, track.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI));

                mediaSessionCompat.setMetadata( builder.build() );

        }
    }

    public static Bitmap retrieveAudioFrameFromAudio(String audioPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(String.valueOf(Uri.fromFile(new File(audioPath))), new HashMap<String, String>());
            bitmap = mediaMetadataRetriever.getFrameAtTime();
            if(bitmap == null){
                bitmap = BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.branham);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retrieveAudioFrameFromAudio(String audioPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    private void playMedia(int position, String songMediaId) {
        // Request audio focus before play commences
        //spokenLocalAudioFocus.requestAudioFocusService(getApplicationContext(),
                //mediaSessionCompat, mediaPlayer);
        if(mState == PlaybackStateCompat.STATE_PAUSED && mediaPlayer != null){
            mediaPlayer.seekTo(PreferenceManager.getDefaultSharedPreferences( this )
                    .getInt( CURRENT_MEDIA_POSITION,
                    mediaPlayer.getCurrentPosition() ));
        }else {
            if (!mediaSessionCompat.isActive()) {
                mediaSessionCompat.setActive(true);
            }

            if (mediaPlayer != null) {
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            uri = Uri.fromFile(new File(Objects.requireNonNull(mPlayingQueue.get(position)
                    .getDescription().getMediaUri()).toString()));

            mediaPlayer = new MediaPlayer();    // Initialize MediaPlayer Object
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            );

            try {
                mediaPlayer.setDataSource(this, uri);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.setOnErrorListener(this);
                mediaPlayer.setOnCompletionListener(this);
                updatePlaybackState(null);
                updateMetadata();
            } catch (IOException | IllegalArgumentException e) {
                e.printStackTrace();
                updatePlaybackState(e.getMessage());
            }
            mState = PlaybackStateCompat.STATE_PLAYING;
        }

    }

    private void stopMedia(String withError) {
        SpokenLogHelper.d(TAG, "handleStopRequest: mState=" + mState + " error=", withError);
        mState = PlaybackStateCompat.STATE_STOPPED;
        // Abandon audio focus
        //spokenLocalAudioFocus.abandonAudioFocusService(getApplicationContext(),
                //mediaSessionCompat, mediaPlayer);
        // let go of all resources...
        mediaPlayer.stop();
        relaxResources(true);

        updatePlaybackState(withError);
        stopForeground(true);
        // service is no longer necessary. Will be started again if needed.
        stopSelf();
    }

    private void pauseMedia() {
        SpokenLogHelper.d(TAG, "handlePauseRequest: mState=" + mState);
        if (mState == PlaybackStateCompat.STATE_PLAYING) {
            // Pause media player and cancel the 'foreground service' state.
            mState = PlaybackStateCompat.STATE_PAUSED;
            if (mediaPlayer.isPlaying()) {
                PreferenceManager.getDefaultSharedPreferences( this ).edit().putInt( CURRENT_MEDIA_POSITION,
                        mediaPlayer.getCurrentPosition()).apply();
                mediaPlayer.pause();
                currentSongPosition = mediaPlayer.getCurrentPosition();
                // Abandon audio focus
                //spokenLocalAudioFocus.abandonAudioFocusService(getApplicationContext(),
                        //mediaSessionCompat, mediaPlayer);
            }

        }
        updatePlaybackState(null);
    }

    private void updateMetadata() {
        MediaMetadataCompat queueItem = mPlayingQueue.get(mCurrentIndexOnQueue);
        String mediaId = queueItem.getDescription().getMediaId();
        MediaMetadataCompat track = mMusicProvider.getMusic(mediaId);
        String trackId = track.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
        if (!mediaId.equals(trackId)) {
            throw new IllegalStateException("track ID (" + trackId + ") " +
                    "should match mediaId (" + mediaId + ")");
        }
        SpokenLogHelper.d(TAG, "Updating metadata for MusicID= " + mediaId);
        initMediaMetaData(queueItem.getDescription().getMediaId());
    }


    /**
     * Releases resources used by the service for playback. This includes the
     * "foreground service" status, the wake locks and possibly the MediaPlayer.
     *
     * @param releaseMediaPlayer Indicates whether the Media Player should also
     *            be released or not
     */
    private void relaxResources(boolean releaseMediaPlayer) {
        SpokenLogHelper.d(TAG, "relaxResources. releaseMediaPlayer=" + releaseMediaPlayer);
        // stop being a foreground service
        stopForeground(true);
        // stop and release the Media Player, if it's available
        if (releaseMediaPlayer && mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("URI")) {
            uri = Uri.fromFile(new File(spokenSharedPreferences.getSongMediaUri()));
            Log.e("SHARED CALL TWO", Objects.requireNonNull(uri.getPath()));
        }

        if(key.equals("MEDIAID")){
            songMediaId = spokenSharedPreferences.getSongMediaId();
            Log.e("SHARED CALL ONE",songMediaId);
        }

        if(key.equals("MEDIAMETADATA")){
            int mCurrentIndex = spokenSharedPreferences.getMediaMetadataPosition();
            Log.e("SHARED CALL THREE", String.valueOf(mCurrentIndex));
        }
    }

    private MediaMetadataCompat getCurrentPlayingMusic() {
        if (mCurrentIndexOnQueue < mPlayingQueue.size()) {
            MediaMetadataCompat item = mPlayingQueue.get(mCurrentIndexOnQueue);
            if (item != null) {
                SpokenLogHelper.d(TAG, "getCurrentPlayingMusic for musicId=",
                        item.getDescription().getMediaId());
                return item;
            }
        }
        return null;
    }

    private void setCustomAction(PlaybackStateCompat.Builder stateBuilder) {
        MediaMetadataCompat currentMusic = getCurrentPlayingMusic();
        if (currentMusic != null) {
            // Set appropriate "Favorite" icon on Custom action:
            String mediaId = currentMusic.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
            int favoriteIcon = android.R.drawable.star_off;

            SpokenLogHelper.d(TAG, "updatePlaybackState, setting Favorite custom action of music ",
                    mediaId, " current favorite=", mediaId);
            stateBuilder.addCustomAction(CUSTOM_ACTION_THUMBS_UP, getString(R.string.favorite),
                    favoriteIcon);
        }
    }

    private long getAvailableActions() {
        long actions = PlaybackStateCompat.ACTION_PLAY |
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID |
                PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH |
                PlaybackStateCompat.ACTION_SEEK_TO;
        if (mPlayingQueue == null || mPlayingQueue.isEmpty()) {
            return actions;
        }
        if (mState == PlaybackStateCompat.STATE_PLAYING) {
            actions |= PlaybackStateCompat.ACTION_PAUSE;
        }
        if (mCurrentIndexOnQueue > 0) {
            actions |= PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;
        }
        if (mCurrentIndexOnQueue < mPlayingQueue.size() - 1) {
            actions |= PlaybackStateCompat.ACTION_SKIP_TO_NEXT;
        }
        return actions;
    }


    /**
     * Update the current media player state, optionally showing an error message.
     *
     * @param error if not null, error message to present to the user.
     *
     */
    private void updatePlaybackState(String error) {
        SpokenLogHelper.d(TAG, "updatePlaybackState, setting session playback state to " + mState);
        long position = PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN;
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            position = mediaPlayer.getCurrentPosition();
        }
        PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(getAvailableActions());
        setCustomAction(stateBuilder);
        // If there is an error message, send it to the playback state:
        if (error != null) {
            // Error states are really only supposed to be used for errors that cause playback to
            // stop unexpectedly and persist until the user takes action to fix it.
            stateBuilder.setErrorMessage(error);
            mState = PlaybackStateCompat.STATE_ERROR;
        }
        stateBuilder.setState(mState, position, 1.0f, SystemClock.elapsedRealtime());
        mediaSessionCompat.setPlaybackState(stateBuilder.build());
        if (mState == PlaybackStateCompat.STATE_PLAYING || mState == PlaybackStateCompat.STATE_PAUSED) {
            NotificationCompat.Builder builder =
                    myPlayerNotification.sendLocalMediaPlaybackNotification(getApplicationContext(),
                    mediaSessionCompat,
                    queueItemList.get(mCurrentIndexOnQueue));
            startForeground(id, builder.build());
        }
    }

    public void sendMessageBroadCast(String title){
        // Broadcast tittle to required activity
        Intent intent = new Intent();
        intent.setAction(SEND_SPOKEN_ACTION);
        intent.putExtra("My Title", title);
        getApplication().sendBroadcast(intent);
    }

    public void sendItemBroadCast(int pos){
        // Broadcast pos to required activity
        Intent intent = new Intent();
        intent.setAction(SEND_SPOKEN_ITEM);
        intent.putExtra("Song Pos", pos);
        getApplication().sendBroadcast(intent);
    }

    public void sendMessageEventBus(String title, long currentDuration, long totalDuration){
        ((SpokenBaseApplication)getApplication()).spokenRxEventBus()
                .setSpokenBus(new SpokenEvents.Message(title, currentDuration, totalDuration));
    }

}
