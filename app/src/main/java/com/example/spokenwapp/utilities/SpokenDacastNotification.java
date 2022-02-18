package com.example.spokenwapp.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.media.session.MediaButtonReceiver;
import com.example.spokenwapp.R;
import com.example.spokenwapp.base.SplashScreen;
import com.example.spokenwapp.base.SpokenMainScreen;
import com.example.spokenwapp.localaudio.LocalAudioPageFragment;

import javax.inject.Inject;
import static android.content.Context.NOTIFICATION_SERVICE;

public class SpokenDacastNotification {

    NotificationManagerCompat notificationManagerCompat;
    @Inject
    public SpokenDacastNotification() {
    }


    public void sendNotification(Context applicationContext, String type, String title, String date, String time, int id) {
        Uri sound = Uri.parse("android.resource://" + applicationContext.getApplicationContext()
                .getPackageName() + "/" + R.raw.ancientwords);
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(applicationContext, SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(applicationContext, (int) System.currentTimeMillis(),
                intent,  PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(applicationContext)
                .setSmallIcon(R.drawable.endtimeeagle)
                .setColor(Color.argb(20,200,78,100))
                .setLargeIcon(BitmapFactory.decodeResource(applicationContext.getResources(), R.drawable.branham))
                .setContentTitle(type)
                .setStyle(new Notification.InboxStyle()
                        .addLine("Title: "+title)
                        .addLine("Date: "+date)
                        .addLine("Time: "+time))
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setSound(sound, AudioManager.STREAM_NOTIFICATION)
                .setAutoCancel(true);


        //This is what will will issue the notification i.e.notification will be visible
        NotificationManager notificationManagerCompat = (NotificationManager) applicationContext
                .getSystemService(NOTIFICATION_SERVICE);
        assert notificationManagerCompat != null;
        notificationManagerCompat.notify(id, builder.build());
        //Toast.makeText(applicationContext, "NOTIFICATION", Toast.LENGTH_SHORT).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotificationAPI(Context context, String type, String title, String date, String time,
                                    String channel, int id) {

        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.ancientwords);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel)
                .setSmallIcon(R.drawable.endtimeeagle)
                .setColor(Color.argb(20,200,78,100))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.branham))
                .setContentTitle(type)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("Title: "+title)
                        .addLine("Date: "+date)
                        .addLine("Time: "+time))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent(context.getApplicationContext()))
                .setAutoCancel(true)
                .setSound(sound, AudioManager.STREAM_NOTIFICATION)
                .setChannelId(channel);
        //This is what will will issue the notification i.e.notification will be visible
        createNotificationChannel(context, channel);
        NotificationManager notificationManagerCompat = (NotificationManager) context
                .getSystemService(NOTIFICATION_SERVICE);
        assert notificationManagerCompat != null;
        notificationManagerCompat.notify(id, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel(Context context, String link) {
        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.ancientwords);
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(link, name, importance);
            channel.setDescription(description);
            AudioAttributes att = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
            channel.setSound(sound, att);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
    }


    public void createNotificationChannelForMusic(Context context, String link) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = context.getString(R.string.channel_name_music);
        String description = context.getString(R.string.channel_description_music);
        int importance = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_LOW;
        }
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(link, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(
                    new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }

        AudioAttributes att = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager = context.getSystemService(NotificationManager.class);
        }
        assert notificationManager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
    }

    public PendingIntent pendingIntent(Context context){
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context.getApplicationContext(), SpokenMainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }


    public PendingIntent pendingIntentMedia(Context context){
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context.getApplicationContext(), SpokenMainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context.getApplicationContext());
        stackBuilder.addNextIntentWithParentStack(intent);
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // Local media playback notification for audio files
    public NotificationCompat.Builder sendLocalMediaPlaybackNotification(Context context,
           MediaSessionCompat mediaSessionCompat, MediaSessionCompat.QueueItem metadataCompat){
        // Get the session's metadata
        MediaControllerCompat controller = mediaSessionCompat.getController();
        MediaDescriptionCompat description = null;
        description = metadataCompat.getDescription();

        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.branham);
        //bitmap = Bitmap.createScaledBitmap(bitmap, 150, 100, false);
        NotificationCompat.Builder builder;
        String channelId = "MediaChannelId";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannelForMusic(context, channelId);
            builder = new NotificationCompat.Builder(context, channelId);
        }else {
            builder = new NotificationCompat.Builder(context);
        }
        assert description != null;
        builder
                // Add the metadata for the currently playing track
                .setContentTitle(description.getTitle())
                .setContentText(description.getSubtitle())
                .setSubText(description.getDescription())
                .setLargeIcon(bitmap)
                // Enable launching the player by clicking the notification
                .setContentIntent(pendingIntentMedia(context))
                // Stop the service when the notification is swiped away
                .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                        PlaybackStateCompat.ACTION_STOP))
                // Make the transport controls visible on the lock screen
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setUsesChronometer(true)
                // Add an app icon and set its accent color
                // Be careful about the color
                .setSmallIcon(R.drawable.endtimeeagle)
                .setColor(Color.argb(20,200,78,100))

                // Add a pause button
                .addAction(new NotificationCompat.Action(
                        R.drawable.ic_baseline_skip_previous_24, "Previous",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)))
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSessionCompat.getSessionToken())
                        .setShowActionsInCompactView(0,1,2 /* #1: pause button */)

                .setShowCancelButton(true)
                        .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                                PlaybackStateCompat.ACTION_STOP)));
                // Dismiss Notification
            updatePlayPauseAction(builder,context, mediaSessionCompat);
            builder.addAction(new NotificationCompat.Action(
                R.drawable.ic_baseline_skip_next_24, "Next",
                MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT)));

        return builder;
    }

    private NotificationCompat.Action noteButton(Context context, int icon, String tittle) {
        return new NotificationCompat.Action(
               icon, tittle,
                MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));
    }

    private void updatePlayPauseAction(NotificationCompat.Builder builder, Context context,
                                       MediaSessionCompat mediaSessionCompat) {
        SpokenLogHelper.d("TAG", "updatePlayPauseAction");

        PlaybackStateCompat playbackStateCompat = mediaSessionCompat.getController().getPlaybackState();

        String playPauseLabel = "";
        int playPauseIcon;
        if (playbackStateCompat.getState() == PlaybackStateCompat.STATE_PLAYING) {
            playPauseLabel = context.getString(R.string.pause);
            playPauseIcon = R.drawable.ic_baseline_pause_circle_outline_24;
        } else {
            playPauseLabel = context.getString(R.string.play_pause);
            playPauseIcon = R.drawable.ic_play_circle_outline_black_24dp;
        }
        builder.addAction(noteButton(context, playPauseIcon, playPauseLabel));

    }

}
