package com.example.spokenwapp.online;

import androidx.lifecycle.ViewModelProviders;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.spokenwapp.R;
import com.example.spokenwapp.base.SpokenBaseApplication;
import com.example.spokenwapp.base.ViewModelFactory;
import dagger.android.support.DaggerFragment;
import com.example.spokenwapp.di.components.ApplicationComponent;
import com.example.spokenwapp.di.components.DaggerApplicationComponent;
import com.example.spokenwapp.di.modules.ApplicationModule;
import com.example.spokenwapp.di.modules.LocalVideoRepoModule;
import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.streamaxia.android.CameraPreview;
import com.streamaxia.android.StreamaxiaPublisher;
import com.streamaxia.player.StreamaxiaPlayer;
import com.streamaxia.player.listener.StreamaxiaPlayerState;
import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.configuration.WOWZMediaConfig;
import com.wowza.gocoder.sdk.api.devices.WOWZAudioDevice;
import com.wowza.gocoder.sdk.api.errors.WOWZError;
import com.wowza.gocoder.sdk.api.errors.WOWZStreamingError;
import com.wowza.gocoder.sdk.api.logging.WOWZLog;
import com.wowza.gocoder.sdk.api.player.WOWZPlayerConfig;
import com.wowza.gocoder.sdk.api.player.WOWZPlayerView;
import com.wowza.gocoder.sdk.api.status.WOWZPlayerStatus;
import com.wowza.gocoder.sdk.api.status.WOWZPlayerStatusCallback;

import java.util.Objects;

public class SpokenOnlineService extends DaggerFragment implements StreamaxiaPlayerState {

    private View root;
    ViewModelFactory viewModelFactory;
    private SpokenOnlineViewModel spokenOnlineViewModel;
    ApplicationComponent applicationComponent;
    SurfaceView surfaceView;
    ProgressBar progressBar;
    TextView debugText;
    TextView stateText;
    ImageView playBtn;
    ImageView smallBtn;
    ImageView muteBtn;
    EditText mEditText;
    AspectRatioFrameLayout aspectRatioFrameLayout;
    private Uri uri;
    private StreamaxiaPlayer streamaxiaPlayer;
    private int STREAM_TYPE = 0;
    Bundle bundle;

    Runnable hide = new Runnable() {
        @Override
        public void run() {
            playBtn.setVisibility(View.GONE);
        }
    };


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
        spokenOnlineViewModel = ViewModelProviders.of(this, viewModelFactory).get(SpokenOnlineViewModel.class);

        this.requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        this.requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        surfaceView = root.findViewById(R.id.surface_view);
        progressBar = root.findViewById(R.id.progress_bar);
        debugText = root.findViewById(R.id.debug_text_view);
        stateText = root.findViewById(R.id.player_state_view);
        playBtn = root.findViewById(R.id.play);
        smallBtn = root.findViewById(R.id.small);
        muteBtn = root.findViewById(R.id.mute);
        mEditText = root.findViewById(R.id.url_edit_text);
        aspectRatioFrameLayout = root.findViewById(R.id.video_frame);

        // Instantiate Streamaxia
        streamaxiaPlayer = new StreamaxiaPlayer();

        getExtras();
        //setting the initial tags of the player
        playBtn.setTag("play");
        muteBtn.setTag("mute");
        smallBtn.setTag("small");
        progressBar.setVisibility(View.GONE);
        initRTMPExoPlayer();
        mEditText.setText(uri.toString());

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayBtn();
                //setActivityFullScreen();
            }
        });

        muteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMute();
            }
        });

        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScreenSize();
            }
        });

    }

    private void getExtras() {
        bundle = getArguments();
        uri = Uri.parse(bundle.getString(SpokenChooseURLViewModel.URI));
        STREAM_TYPE = bundle.getInt(SpokenChooseURLViewModel.TYPE);
    }

    public void setPlayBtn() {
        playBtn.postDelayed(hide, 1000);
        if (playBtn.getTag().equals("play")) {
            streamaxiaPlayer.play(uri, STREAM_TYPE);
            surfaceView.setBackgroundColor(Color.TRANSPARENT);
            progressBar.setVisibility(View.GONE);
            playBtn.setTag("pause");
            playBtn.setImageResource(R.drawable.pause);
            setAspectRatioFrameLayoutOnClick();
        } else {

            streamaxiaPlayer.pause();

            progressBar.setVisibility(View.VISIBLE);
            playBtn.setTag("play");
            playBtn.setImageResource(R.drawable.play);
        }
    }

    public void setMute() {
        if (muteBtn.getTag().equals("mute")) {
            streamaxiaPlayer.setMute();
            muteBtn.setTag("muted");
            muteBtn.setImageResource(R.drawable.muted);
        } else {
            streamaxiaPlayer.setMute();
            muteBtn.setTag("mute");
            muteBtn.setImageResource(R.drawable.mute);
        }
    }

    public void setScreenSize() {
        if (smallBtn.getTag().equals("small")) {
            streamaxiaPlayer.setVideoSize(300, 300);
            smallBtn.setTag("big");
            smallBtn.setImageResource(R.drawable.big);
        } else {
            streamaxiaPlayer.setVideoSize(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            smallBtn.setTag("small");
            smallBtn.setImageResource(R.drawable.small);
        }
    }

    private void setAspectRatioFrameLayoutOnClick() {
        aspectRatioFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playBtn.setVisibility(View.VISIBLE);
                playBtn.postDelayed(hide, 1000);
            }
        });
    }

    private void initRTMPExoPlayer() {

        streamaxiaPlayer.initStreamaxiaPlayer(surfaceView, aspectRatioFrameLayout,
                stateText, this, requireActivity(), uri);
    }

    @Override
    public void stateENDED() {
        progressBar.setVisibility(View.GONE);
        playBtn.setImageResource(R.drawable.play);

    }

    @Override
    public void stateBUFFERING() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stateIDLE() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void statePREPARING() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stateREADY() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void stateUNKNOWN() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void setActivityFullScreen() {
        this.getActivity().setVisible(false);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        streamaxiaPlayer.stop();
    }

}
