package com.example.spokenwapp.players;

import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spokenwapp.R;
import com.example.spokenwapp.adapters.SpokenMediaBrowserAdapter;
import com.example.spokenwapp.base.SpokenMainScreen;
import com.example.spokenwapp.services.SpokenMediaBrowserService;
import com.example.spokenwapp.utilities.SpokenSharedPreferences;

import java.util.List;
import java.util.Objects;

public class SpokenMediaBrowser {

    private MediaBrowserCompat mediaBrowser;
    // Create a MediaControllerCompat
    public static MediaControllerCompat mediaController = null;
    SpokenSharedPreferences spokenSharedPreferences;

    public void createMediaBrowser(AppCompatActivity context){
        // Create MediaBrowserServiceCompat
        mediaBrowser = new MediaBrowserCompat(context ,
                new ComponentName(context, SpokenMediaBrowserService.class),
                createMediaBrowserConnectionCallback(context),
                null); // optional Bundle
    }

    public MediaBrowserCompat.ConnectionCallback createMediaBrowserConnectionCallback(AppCompatActivity context){
        final MediaBrowserCompat.ConnectionCallback connectionCallbacks =
                new MediaBrowserCompat.ConnectionCallback() {
                    @Override
                    public void onConnected() {
                        // Get the token for the MediaSession
                        MediaSessionCompat.Token token = mediaBrowser.getSessionToken();
                        mediaController = new MediaControllerCompat(context, // Context
                                token);

                        Log.e("AUDIO BROWSER", "connected");
                        // Save the controller
                        MediaControllerCompat.setMediaController(context, mediaController);
                        // Finish building the UI
                        mediaController = MediaControllerCompat.getMediaController(context);
                        // Display the initial state
                        MediaMetadataCompat metadata = mediaController.getMetadata();
                        PlaybackStateCompat pbState = mediaController.getPlaybackState();
                        // Register a Callback to stay in sync
                        mediaController.registerCallback(createControllerCallback(context));
                        //mediaBrowser.unsubscribe(SpokenMediaBrowserService.MEDIA_ROOT_ID, mSubscriptionCallback);
                        //mediaBrowser.subscribe(SpokenMediaBrowserService.MEDIA_ROOT_ID, mSubscriptionCallback);
                        mediaBrowser.unsubscribe(SpokenMediaBrowserService.PACKAGE_ID, createSubscriptionCallback(context));
                        mediaBrowser.subscribe(SpokenMediaBrowserService.PACKAGE_ID, createSubscriptionCallback(context));
                        //mSubscriptionCallback.onChildrenLoaded(SpokenMediaBrowserService.PACKAGE_ID, mMediaItems);

                    }

                    @Override
                    public void onConnectionSuspended() {
                        // The Service has crashed. Disable transport controls until it automatically reconnects
                    }

                    @Override
                    public void onConnectionFailed() {
                        // The Service has refused our connection

                    }
                };
        return connectionCallbacks;
    }

    public  MediaControllerCompat.Callback createControllerCallback(Context context){
        MediaControllerCompat.Callback controllerCallback =
                new MediaControllerCompat.Callback() {
                    @Override
                    public void onMetadataChanged(MediaMetadataCompat metadata) {
                        Log.e("onMetadataChanged", Objects.requireNonNull(metadata.getDescription()
                                .getDescription()).toString());
                        spokenSharedPreferences.saveDacastContentTitle(Objects.requireNonNull(metadata
                                .getDescription().getTitle()).toString());
                        ((SpokenMainScreen)context).updateTitle(metadata.getDescription().getTitle().toString());
                        //mediaBrowserAdapter.setTrackPlaying(spokenSharedPreferences.getMediaMetadataPosition());

                    }

                    @Override
                    public void onPlaybackStateChanged(PlaybackStateCompat state) {
                        Log.e("onPlaybackStateChanged", state.getPlaybackState().toString());
                        //mediaBrowserAdapter.setTrackPlaying(spokenSharedPreferences.getMediaMetadataPosition());
                    }
                };
        return controllerCallback;
    }

    public MediaBrowserCompat.SubscriptionCallback createSubscriptionCallback(Context context){
        MediaBrowserCompat.SubscriptionCallback mSubscriptionCallback = new MediaBrowserCompat.SubscriptionCallback() {
            @Override
            public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
                Log.e("onChildrenLoaded", "onChildrenLoaded: " + parentId);
                Log.e("ChildrenLoaded Size", "" + children.size());

                if (children.isEmpty()) {
                    return;
                }


                // Play the first item?
                // Probably should check firstItem.isPlayable()
                //MediaControllerCompat.getMediaController(requireActivity())
                //.getTransportControls()
                //.playFromMediaId(firstItem.getMediaId(), null);

            }


            @Override
            public void onError(@NonNull String id) {
                Toast.makeText(context, R.string.error_no_metadata, Toast.LENGTH_LONG).show();
            }


        };

        return mSubscriptionCallback;
    }

    public void connectMediaBrowser(AppCompatActivity compatActivity){
        createMediaBrowser(compatActivity);
    }




}
