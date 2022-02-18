package com.example.spokenwapp.localaudio;


import androidx.lifecycle.ViewModelProvider;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.spokenwapp.R;
import com.example.spokenwapp.adapters.LocalAudioListAdapter;
import com.example.spokenwapp.adapters.OnItemTouchListener;
import com.example.spokenwapp.adapters.RecyclerViewOnItemTouchListener;
import com.example.spokenwapp.adapters.SpokenMediaBrowserAdapter;
import com.example.spokenwapp.base.SpokenMainScreen;
import com.example.spokenwapp.base.ViewModelFactory;
import com.example.spokenwapp.services.SpokenMediaBrowserService;
import com.example.spokenwapp.utilities.SpokenSharedPreferences;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import dagger.android.AndroidInjection;
import dagger.android.support.DaggerFragment;


public class LocalAudioPageFragment extends DaggerFragment {

    LocalAudioPageViewModel localAudioPageViewModel;
    public static final String ARG_AUDIO = "Local Audio";
    private View root;
    @Inject
    ViewModelFactory viewModelFactory;
    private RecyclerView recyclerViewAudios;
    LocalAudioListAdapter localAudioListAdapter;
    SpokenMediaBrowserAdapter mediaBrowserAdapter;
    private MediaBrowserCompat mediaBrowser;
    // Create a MediaControllerCompat
    public static MediaControllerCompat mediaController = null;
    SpokenSharedPreferences spokenSharedPreferences;
    List<MediaBrowserCompat.MediaItem> mMediaItems;
    public static final String SEND_SPOKEN_ACTION = "com.example.spokenwapp.SPOKEN_ACTON";
    public static final String SEND_SPOKEN_ITEM = "com.example.spokenwapp.SPOKEN_ITEM";
    BroadcastReceiver spokenItemBroadcastReceiver;
    IntentFilter intentFilterSpokenItem;


    public static LocalAudioPageFragment newInstance() {
        return new LocalAudioPageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.local_audio_page_fragment, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        localAudioPageViewModel = new ViewModelProvider(this, viewModelFactory).get(LocalAudioPageViewModel.class);
        spokenSharedPreferences = new SpokenSharedPreferences(requireActivity());
        recyclerViewAudios = root.findViewById(R.id.localAudioList);

        //localAudioListAdapter = new LocalAudioListAdapter(localAudioPageViewModel, requireActivity(), getActivity());

        recyclerViewAudios.setHasFixedSize(true);
        RecyclerView.LayoutManager audioLayoutManager = new LinearLayoutManager(requireActivity());
        recyclerViewAudios.setLayoutManager(audioLayoutManager);
        mMediaItems = new ArrayList<>();

        //
        int[] ATTRS = new int[]{android.R.attr.listDivider};
        TypedArray a = requireActivity().obtainStyledAttributes(ATTRS);
        Drawable divider = a.getDrawable(0);
        int inset = getResources().getDimensionPixelSize(R.dimen.rec_margin);
        InsetDrawable insetDivider = new InsetDrawable(divider, inset, 0, inset, 0);
        a.recycle();

        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireActivity(),
                DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(insetDivider);
        recyclerViewAudios.addItemDecoration(itemDecoration);
        //recyclerViewAudios.setAdapter(localAudioListAdapter);

        //observableViewModelLocalAudio();

        // Create MediaBrowserServiceCompat
        mediaBrowser = new MediaBrowserCompat(requireActivity(),
                new ComponentName(requireActivity(), SpokenMediaBrowserService.class),
                connectionCallbacks,
                null); // optional Bundle
        mediaBrowser.connect();
        mediaController = MediaControllerCompat.getMediaController(requireActivity());


        recyclerViewAudios.addOnItemTouchListener(new RecyclerViewOnItemTouchListener(this
                .requireActivity().getApplicationContext(),
                recyclerViewAudios, new OnItemTouchListener() {
            @Override
            public void onClick(View view, int position) {
                setTrackColor(position);
                // Saved selected song data
                spokenSharedPreferences.saveMediaData(mediaBrowserAdapter.getId(position),
                        mediaBrowserAdapter.getSongPath(position),
                        mediaBrowserAdapter.getTitle(position));
                spokenSharedPreferences.saveDacastContentTitle(mediaBrowserAdapter.getTitle(position));
                spokenSharedPreferences.saveMediaMetadataPosition(position);

                //mediaController.getTransportControls().pause();
                //mediaController.getTransportControls().play();
                mediaController.getTransportControls().playFromMediaId(
                        mediaBrowserAdapter.getId(position), null);

                // Broadcast tittle to required activity
                Intent intent = new Intent();
                intent.setAction(SEND_SPOKEN_ACTION);
                intent.putExtra("My Title", mediaBrowserAdapter.getTitle(position));
                requireActivity().sendBroadcast(intent);

                // Register a Callback to stay in sync
                mediaController.registerCallback(controllerCallback);
                spokenSharedPreferences.saveTheoPlayerState("audio");
                //Navigation.findNavController(view).navigate(R.id.spokenLocalAudioSearchFragment);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // Broadcast receiver
        spokenItemBroadcastReceiver = new LocalAudioPageFragment.SpokenItemBroadcastReceiver();
        intentFilterSpokenItem = new IntentFilter(LocalAudioPageFragment.SEND_SPOKEN_ITEM);
        intentFilterSpokenItem.addAction(Intent.EXTRA_TEXT);

    }

    private void setTrackColor(int position) {
        mediaBrowserAdapter.setTrackPlaying(position);
        // Line below will `RecyclerView` to re-draw that position.. in other words, it will triggers a call to `onBindViewHolder`
        mediaBrowserAdapter.notifyItemChanged(position);

        // Reset the color of song previously playing..
        mediaBrowserAdapter.notifyItemChanged(spokenSharedPreferences.getMediaMetadataPosition());
    }


    private void observableViewModelLocalAudio() {
        localAudioPageViewModel.getLocalAudioRepos().observe(getViewLifecycleOwner(), repos -> {
            if (repos != null) recyclerViewAudios.setVisibility(View.VISIBLE);
            Toast.makeText(requireActivity(), "Audio Loaded", Toast.LENGTH_LONG).show();
        });

        localAudioPageViewModel.getLocalAudioError().observe(getViewLifecycleOwner(), isError -> {
            if (isError != null) {
                if (isError) {
                    Toast.makeText(requireActivity(), "Error Audio Loading ...", Toast.LENGTH_LONG).show();
                    recyclerViewAudios.setVisibility(View.INVISIBLE);
                }
            } else {

                recyclerViewAudios.setVisibility(View.VISIBLE);

            }
        });

        localAudioPageViewModel.getLocalAudioLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                Toast.makeText(requireActivity(), "Audios Loading ...", Toast.LENGTH_LONG).show();
                if (isLoading) {
                    recyclerViewAudios.setVisibility(View.INVISIBLE);

                }
            }
        });
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(@NonNull Activity activity) {
        AndroidInjection.inject(activity);
        super.onAttach(activity);

    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(spokenItemBroadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().registerReceiver(spokenItemBroadcastReceiver, intentFilterSpokenItem);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaController != null) {
            mediaController.unregisterCallback(controllerCallback);
        }
        if (mediaBrowser.isConnected()) {
            //mediaBrowser.unsubscribe(SpokenMediaBrowserService.MEDIA_ROOT_ID, mSubscriptionCallback);
            mediaBrowser.unsubscribe(SpokenMediaBrowserService.PACKAGE_ID, mSubscriptionCallback);
            mediaBrowser.disconnect();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaController != null) {
            mediaController.unregisterCallback(controllerCallback);
        }
        if (mediaBrowser.isConnected()) {
            //mediaBrowser.unsubscribe(SpokenMediaBrowserService.MEDIA_ROOT_ID, mSubscriptionCallback);
            mediaBrowser.unsubscribe(SpokenMediaBrowserService.PACKAGE_ID, mSubscriptionCallback);
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

                    Log.e("AUDIO BROWSER", "connected");
                    // Save the controller
                    MediaControllerCompat.setMediaController(requireActivity(), mediaController);
                    // Finish building the UI
                    mediaController = MediaControllerCompat.getMediaController(requireActivity());
                    // Display the initial state
                    MediaMetadataCompat metadata = mediaController.getMetadata();
                    PlaybackStateCompat pbState = mediaController.getPlaybackState();
                    // Register a Callback to stay in sync
                    mediaController.registerCallback(controllerCallback);
                    //mediaBrowser.unsubscribe(SpokenMediaBrowserService.MEDIA_ROOT_ID, mSubscriptionCallback);
                    //mediaBrowser.subscribe(SpokenMediaBrowserService.MEDIA_ROOT_ID, mSubscriptionCallback);
                    mediaBrowser.unsubscribe(SpokenMediaBrowserService.PACKAGE_ID, mSubscriptionCallback);
                    mediaBrowser.subscribe(SpokenMediaBrowserService.PACKAGE_ID, mSubscriptionCallback);
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

    MediaControllerCompat.Callback controllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    Log.e("onMetadataChanged", Objects.requireNonNull(metadata.getDescription()
                            .getDescription()).toString());
                    spokenSharedPreferences.saveDacastContentTitle(Objects.requireNonNull(metadata
                            .getDescription().getTitle()).toString());
                    ((SpokenMainScreen)requireActivity()).updateTitle(metadata.getDescription().getTitle().toString());
                    //mediaBrowserAdapter.setTrackPlaying(spokenSharedPreferences.getMediaMetadataPosition());

                }

                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat state) {
                    Log.e("onPlaybackStateChanged", state.getPlaybackState().toString());
                    //mediaBrowserAdapter.setTrackPlaying(spokenSharedPreferences.getMediaMetadataPosition());
                }
            };

    private MediaBrowserCompat.SubscriptionCallback mSubscriptionCallback = new MediaBrowserCompat.SubscriptionCallback() {
        @Override
        public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
            Log.e("onChildrenLoaded", "onChildrenLoaded: " + parentId);
            Log.e("ChildrenLoaded Size", "" + children.size());

            if (children.isEmpty()) {
                return;
            }

            mediaBrowserAdapter = new SpokenMediaBrowserAdapter(requireActivity(), children);
            recyclerViewAudios.setAdapter(mediaBrowserAdapter);
            mMediaItems = children;


            // Play the first item?
            // Probably should check firstItem.isPlayable()
            //MediaControllerCompat.getMediaController(requireActivity())
                    //.getTransportControls()
                    //.playFromMediaId(firstItem.getMediaId(), null);

        }


        @Override
        public void onError(@NonNull String id) {
            Toast.makeText(getActivity(), R.string.error_no_metadata, Toast.LENGTH_LONG).show();
        }


    };

    public class SpokenItemBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String extra = intent.getAction();
            assert extra != null;
            if (extra.equals(LocalAudioPageFragment.SEND_SPOKEN_ITEM)) {
                Log.e("BROAD TWO","BroadcastReceiverItem fired ...");
                int pos = intent.getIntExtra("Song Pos", 0);
                mediaBrowserAdapter.setTrackPlaying(spokenSharedPreferences.getMediaMetadataPosition());


            }
        }
    }


}
