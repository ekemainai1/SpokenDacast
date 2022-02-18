package com.example.spokenwapp.players;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.example.spokenwapp.R;
import com.example.spokenwapp.adapters.LocalAudioListAdapter;
import com.example.spokenwapp.adapters.LocalVideoListAdapter;
import com.example.spokenwapp.base.SpokenMainScreen;
import com.example.spokenwapp.dacast.DacastPlayer;
import com.example.spokenwapp.services.SpokenMediaBrowserService;
import com.example.spokenwapp.utilities.SpokenSharedPreferences;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.theoplayer.android.api.event.EventListener;
import com.theoplayer.android.api.event.player.PauseEvent;
import com.theoplayer.android.api.event.player.PlayEvent;
import com.theoplayer.android.api.event.player.PlayerEventTypes;
import com.theoplayer.android.api.event.player.TimeUpdateEvent;

import java.util.List;
import java.util.Objects;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SpokenOnlinePlayerFragment extends Fragment {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    View root;


    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            int flags = View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

            Activity activity = getActivity();
            if (activity != null
                    && activity.getWindow() != null) {
                activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            }
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }

        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */


    ImageView fullscreenButton;
    public MediaControllerCompat mediaController = null;
    SpokenSharedPreferences spokenSharedPreferences;
    List<MediaBrowserCompat.MediaItem> mMediaItems;
    private MediaBrowserCompat mediaBrowser;
    LocalVideoListAdapter localVideoListAdapter;
    PlayerView playerView;
    SpokenExoplayer spokenExoplayer;
    boolean fullscreen = false;
    ImageView backBtn;
    static boolean showBackButton = true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_spoken_online_player, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spokenSharedPreferences = new SpokenSharedPreferences(requireActivity());
        playerView = view.findViewById(R.id.video_view);
        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);


        if(isAdded()) {
            ((SpokenMainScreen) requireActivity()).changePlayerVisibility();
        }
        // Create SpokenExoplayer Instance
        spokenExoplayer = new SpokenExoplayer(requireActivity());

        // Create MediaBrowserServiceCompat
        mediaBrowser = new MediaBrowserCompat(requireActivity(),
                new ComponentName(requireActivity(), SpokenMediaBrowserService.class),
                connectionCallbacks,
                null); // optional Bundle
        mediaBrowser.connect();
        mediaController = MediaControllerCompat.getMediaController(requireActivity());
        // Set up the user interaction to manually show or hide the system UI.
        playerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Register a Callback to stay in sync
        mediaController.registerCallback(controllerCallback);
        Uri uri = Uri.parse(String.valueOf(spokenSharedPreferences.getSongMediaUri()));

        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullscreen) {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(requireActivity(),
                            R.drawable.exo_icon_fullscreen_enter));

                    requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                    if(getSupportActionBar() != null){
                        getSupportActionBar().show();
                    }

                    requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = (int) ( 450 * requireActivity().getResources().getDisplayMetrics().density);
                    playerView.setLayoutParams(params);
                    backBtn.setVisibility(View.VISIBLE);

                    fullscreen = false;
                }else{
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(requireActivity(),
                            R.drawable.exo_icon_fullscreen_exit));

                    requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                    if(getSupportActionBar() != null){
                        getSupportActionBar().hide();
                    }

                    requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    backBtn.setVisibility(View.INVISIBLE);

                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    playerView.setLayoutParams(params);

                    fullscreen = true;
                }
            }
        });

        backBtn = view.findViewById(R.id.back_btn);
        backBtn.setVisibility(showBackButton ? View.VISIBLE : View.GONE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        spokenExoplayer.hideSystemUi(playerView);
        if ((Util.SDK_INT < 24)) {
            spokenExoplayer.initializePlayer(playerView, Objects.requireNonNull(mediaController
                    .getMetadata().getDescription().getMediaUri()).getPath());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            spokenExoplayer.releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaController != null) {
            mediaController.unregisterCallback(controllerCallback);
        }
        if(mediaBrowser.isConnected()) {
            mediaBrowser.unsubscribe(SpokenMediaBrowserService.PLAYABLE_VIDEO_ROOT, mSubscriptionCallback);
            mediaBrowser.disconnect();
        }
    }


    @Nullable
    private ActionBar getSupportActionBar() {
        ActionBar actionBar = null;
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            actionBar = activity.getSupportActionBar();
        }
        return actionBar;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            spokenExoplayer.initializePlayer(playerView, String.valueOf(spokenSharedPreferences.getSongMediaUri()));
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            spokenExoplayer.releasePlayer();
        }

        if(mediaController != null) {
            mediaController.unregisterCallback(controllerCallback);
        }
        if(mediaBrowser.isConnected()) {
            mediaBrowser.unsubscribe(SpokenMediaBrowserService.PLAYABLE_VIDEO_ROOT, mSubscriptionCallback);
            mediaBrowser.disconnect();
        }
    }


    private final MediaBrowserCompat.ConnectionCallback connectionCallbacks =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    // Get the token for the MediaSession
                    MediaSessionCompat.Token token = mediaBrowser.getSessionToken();
                    mediaController = new MediaControllerCompat(requireActivity(), // Context
                            token);
                    mediaBrowser.unsubscribe(SpokenMediaBrowserService.PLAYABLE_VIDEO_ROOT, mSubscriptionCallback);
                    mediaBrowser.subscribe(SpokenMediaBrowserService.PLAYABLE_VIDEO_ROOT, mSubscriptionCallback);

                    Log.e("VIDEO BROWSER", "connected");
                    // Save the controller
                    MediaControllerCompat.setMediaController(requireActivity(), mediaController);
                    // Finish building the UI
                    mediaController = MediaControllerCompat.getMediaController(requireActivity());
                    // Display the initial state
                    MediaMetadataCompat metadata = mediaController.getMetadata();
                    PlaybackStateCompat pbState = mediaController.getPlaybackState();
                    // Register a Callback to stay in sync
                    mediaController.registerCallback(controllerCallback);

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

    MediaControllerCompat.Callback controllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {

                }

                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat state) {

                }
            };

    private MediaBrowserCompat.SubscriptionCallback mSubscriptionCallback = new MediaBrowserCompat.SubscriptionCallback() {

        @Override
        public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
            Log.i("LoadMediaItems", "onChildrenLoaded");
            if (mMediaItems != null) {
                mMediaItems.clear();
                mMediaItems = null;
            }
            mMediaItems = children;
            if (localVideoListAdapter == null) {
                //mRecyclerView.setAdapter(mAdapter);
            } else {
                //mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onError(String id) {
            Log.i("MediaItemsSubscript", "SubscriptionCallback onError");
        }
    };




}