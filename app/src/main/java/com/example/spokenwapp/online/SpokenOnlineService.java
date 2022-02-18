package com.example.spokenwapp.online;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.example.spokenwapp.R;
import com.example.spokenwapp.base.SpokenBaseApplication;
import com.example.spokenwapp.base.SpokenMainScreen;
import com.example.spokenwapp.base.ViewModelFactory;
import dagger.android.support.DaggerFragment;

import com.example.spokenwapp.dacast.DacastPlayer;
import com.example.spokenwapp.di.components.ApplicationComponent;
import com.example.spokenwapp.di.components.DaggerApplicationComponent;
import com.example.spokenwapp.di.modules.ApplicationModule;
import com.example.spokenwapp.di.modules.LocalVideoRepoModule;
import com.example.spokenwapp.utilities.SpokenSharedPreferences;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.theoplayer.android.api.THEOplayerConfig;
import com.theoplayer.android.api.THEOplayerSettings;
import com.theoplayer.android.api.THEOplayerView;
import com.theoplayer.android.api.event.EventListener;
import com.theoplayer.android.api.event.player.PauseEvent;
import com.theoplayer.android.api.event.player.PlayEvent;
import com.theoplayer.android.api.event.player.PlayerEventTypes;
import com.theoplayer.android.api.event.player.TimeUpdateEvent;
import com.theoplayer.android.api.player.Player;

import javax.inject.Inject;


public class SpokenOnlineService extends DaggerFragment {

    private View root;
    @Inject
    ViewModelFactory viewModelFactory;
    SpokenOnlineViewModel spokenOnlineViewModel;
    ApplicationComponent applicationComponent;
    boolean isFullScreen = false;
    BottomNavigationView navBar;
    CollapsingToolbarLayout layout;
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    private static final String TAG = SpokenOnlineService.class.getName();
    SpokenSharedPreferences spokenSharedPreferences;
    DacastPlayer player;

    public static SpokenOnlineService newInstance() {
        return new SpokenOnlineService();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.spoken_online_fragment, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        applicationComponent = DaggerApplicationComponent.builder()
                .application(new SpokenBaseApplication())
                .applicationModule(new ApplicationModule())
                .localVideoRepoModule(new LocalVideoRepoModule())
                .build();
        applicationComponent.inject(this);
        spokenOnlineViewModel = new ViewModelProvider(this, viewModelFactory).get(SpokenOnlineViewModel.class);

        this.requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        this.requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        navBar = ((SpokenMainScreen) requireActivity()).findViewById(R.id.bottom_nav);
        appBarLayout = ((SpokenMainScreen) requireActivity()).findViewById(R.id.mainScreenAppBar);
        toolbar = ((SpokenMainScreen) requireActivity()).findViewById(R.id.toolbar);

        //
        if(isAdded()) {
            ((SpokenMainScreen) requireActivity()).changePlayerVisibility();
        }

        // Get corresponding content Id from sharedPreferences
        spokenSharedPreferences = new SpokenSharedPreferences(this.requireActivity());
        String contentId = spokenSharedPreferences.getSysIPAddress();

        player = new DacastPlayer(this.requireActivity(), contentId //contentId
                /*, "https://cdn.theoplayer.com/demos/preroll.xml"*/);
        ConstraintLayout layout = root.findViewById(R.id.mainLayout);
        layout.setBackgroundColor(Color.TRANSPARENT);
        player.getView().setLayoutParams(
                new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.MATCH_PARENT
                )
        );
        layout.addView(player.getView());
        player.getView().setBackgroundColor(Color.TRANSPARENT);

        if (player.getTHEOplayer().getPlayer().isPaused()) {
            player.getTHEOplayer().getPlayer().play();
        } else {
            player.getTHEOplayer().getPlayer().pause();
        }

        // Call play event listeners
        playerEventListeners();

       // player.getTHEOplayer().getSettings().setFullScreenOrientationCoupled(true);

    }

    public void playerEventListeners(){
        player.getTHEOplayer().getPlayer().addEventListener(PlayerEventTypes.PLAY,
                new EventListener<PlayEvent>() {
            @Override
            public void handleEvent(PlayEvent playEvent) {

            }
        });

        player.getTHEOplayer().getPlayer().addEventListener(PlayerEventTypes.PAUSE,
                new EventListener<PauseEvent>() {
            @Override
            public void handleEvent(PauseEvent pauseEvent) {

            }
        });

        player.getTHEOplayer().getPlayer().addEventListener(PlayerEventTypes.TIMEUPDATE,
                new EventListener<TimeUpdateEvent>() {
            @Override
            public void handleEvent(TimeUpdateEvent timeUpdateEvent) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        player.getTHEOplayer().onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
       player.getTHEOplayer().onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        player.getTHEOplayer().onDestroy();
    }

}
