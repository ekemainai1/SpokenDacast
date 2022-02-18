package com.example.spokenwapp.players;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;

import java.io.IOException;

import javax.inject.Inject;

public class SpokenMediaPlayer  {
   Context context;
   MediaPlayer mediaPlayer;


    public SpokenMediaPlayer() {
    }

    // Initialize MediaPlayer object
    public void initializeMediaPlayer(Context context, MediaPlayer mediaPlayer,
                                      Uri uri) throws IOException {

        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        mediaPlayer.setDataSource(context, uri);
        mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
    }

    public void releaseMediaPlayer(MediaPlayer mediaPlayer){
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void startMediaPlayer(MediaPlayer mediaPlayer){
        mediaPlayer.start();
    }
}
