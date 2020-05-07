package com.example.spokenwapp.online;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.spokenwapp.R;
import com.example.spokenwapp.base.SpokenBaseApplication;
import com.example.spokenwapp.di.components.ApplicationComponent;
import com.example.spokenwapp.di.components.DaggerApplicationComponent;
import com.example.spokenwapp.di.modules.ApplicationModule;
import com.example.spokenwapp.di.modules.LocalVideoRepoModule;
import com.streamaxia.android.StreamaxiaPublisher;
import com.streamaxia.player.StreamaxiaPlayer;

import dagger.android.support.DaggerFragment;
import dagger.multibindings.IntoMap;

public class SpokenChooseURLFragment extends DaggerFragment {

    private View root;
    private SpokenChooseURLViewModel spokenChooseURLViewModel;
    ApplicationComponent applicationComponent;
    RelativeLayout rtmpDemoBtn;
    RelativeLayout hlsDemoBtn;
    RelativeLayout dashDemoBtn;

    public static SpokenChooseURLFragment newInstance() {
        return new SpokenChooseURLFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.spoken_choose_u_r_l_fragment, container, false);
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
        spokenChooseURLViewModel = ViewModelProviders.of(this).get(SpokenChooseURLViewModel.class);

        rtmpDemoBtn = root.findViewById(R.id.rtmp_demo);
        hlsDemoBtn = root.findViewById(R.id.hls_demo);
        dashDemoBtn = root.findViewById(R.id.dash_demo);

        rtmpDemoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spokenChooseURLViewModel.setRtmpDemoBtn(v);
            }
        });

        hlsDemoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spokenChooseURLViewModel.setHlsDemoBtn(v);
            }
        });

        dashDemoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spokenChooseURLViewModel.setDashDemoBtn(v);
            }
        });

    }

    public void sendIntentPatch(int type, View view) {
        spokenChooseURLViewModel.sendIntent(type, view);
        }


}
