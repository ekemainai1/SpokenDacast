package com.example.spokenwapp.utilities;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import static android.media.AudioManager.AUDIOFOCUS_REQUEST_GRANTED;

public class SpokenLocalAudioFocus {

    private static final String TAG = "BEST PRACTICES";
    public AudioManager audioManager;
    public AudioFocusRequest audioFocusRequest;
    public AudioAttributes playbackAttributes;
    final Object focusLock = new Object();
    private Handler handler = new Handler();

    public Runnable delayedStop(MediaSessionCompat mediaSessionCompat) {
        return new Runnable() {
            @Override
            public void run() {
                mediaSessionCompat.getController().getTransportControls().stop();
            }
        };
    }

    public AudioManager.OnAudioFocusChangeListener getAudioFocusListener(Context context,
                                                       MediaSessionCompat mediaSessionCompat, MediaPlayer mediaPlayer) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        return new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        Log.i(TAG, "AUDIOFOCUS_GAIN");
                        if(!mediaPlayer.isPlaying()) {
                            mediaSessionCompat.getController().getTransportControls().play();
                        }else {
                            mediaPlayer.setVolume(1.0f, 1.0f);
                        }
                        break;
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                        Log.i(TAG, "AUDIOFOCUS_GAIN_TRANSIENT");
                        mediaSessionCompat.getController().getTransportControls().play();
                        break;
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                        Log.i(TAG, "AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK");
                        mediaPlayer.setVolume(0.2f,0.2f);
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        Log.e(TAG, "AUDIOFOCUS_LOSS");
                        //pause();
                        mediaSessionCompat.getController().getTransportControls().stop();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                        //pause();
                        if(mediaPlayer.isPlaying()) {
                            mediaSessionCompat.getController().getTransportControls().pause();
                        }
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                        mediaPlayer.setVolume(0.2f,0.2f);
                        break;
                    case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                        Log.e(TAG, "AUDIOFOCUS_REQUEST_FAILED");
                        mediaSessionCompat.getController().getTransportControls().stop();
                        break;
                    default:
                        //
                }
            }
        };
    }


    public void requestAudioFocusService(Context context, MediaSessionCompat mediaSessionCompat,
                                         MediaPlayer mediaPlayer){
        playbackAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(playbackAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setWillPauseWhenDucked(true)
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener(getAudioFocusListener(context, mediaSessionCompat,
                            mediaPlayer))
                    .build();
            assert audioManager != null;
            int result = audioManager.requestAudioFocus(audioFocusRequest);
            synchronized(focusLock) {
                if (result == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
                    mediaSessionCompat.getController().getTransportControls().stop();
                } else if (result == AUDIOFOCUS_REQUEST_GRANTED) {
                    // play
                    mediaSessionCompat.getController().getTransportControls().play();
                } else if (result == AudioManager.AUDIOFOCUS_REQUEST_DELAYED) {
                    mediaSessionCompat.getController().getTransportControls().play();
                }
            }
        }
    }

    public void abandonAudioFocusService(Context context, MediaSessionCompat mediaSessionCompat,
                                         MediaPlayer mediaPlayer){
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int result = audioManager.abandonAudioFocusRequest(audioFocusRequest);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                audioManager.abandonAudioFocusRequest(audioFocusRequest);
            }
        }else {
            int result = audioManager.abandonAudioFocus(getAudioFocusListener(context,
                    mediaSessionCompat, mediaPlayer));
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                audioManager.abandonAudioFocus(getAudioFocusListener(context,
                        mediaSessionCompat, mediaPlayer));
            }
        }

    }


}
