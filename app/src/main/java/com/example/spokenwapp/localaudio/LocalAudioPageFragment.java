package com.example.spokenwapp.localaudio;


import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.spokenwapp.R;
import com.example.spokenwapp.adapters.LocalAudioListAdapter;
import com.example.spokenwapp.adapters.LocalVideoListAdapter;
import com.example.spokenwapp.base.SpokenBaseApplication;
import com.example.spokenwapp.base.ViewModelFactory;
import com.example.spokenwapp.data.model.LocalAudioEntity;
import com.example.spokenwapp.data.model.LocalVideoEntity;
import com.example.spokenwapp.di.components.ApplicationComponent;
import com.example.spokenwapp.di.components.DaggerApplicationComponent;
import com.example.spokenwapp.di.modules.ApplicationModule;
import com.example.spokenwapp.di.modules.LocalVideoRepoModule;

import java.util.List;

import dagger.android.support.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class LocalAudioPageFragment extends DaggerFragment {

    LocalAudioPageViewModel localAudioPageViewModel;
    public static final String ARG_AUDIO = "Local Audio";
    private View root;
    ApplicationComponent applicationComponent;
    ViewModelFactory viewModelFactory;
    private RecyclerView recyclerViewAudios;
    CompositeDisposable compositeDisposable;
    LocalAudioListAdapter localAudioListAdapter;


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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        applicationComponent = DaggerApplicationComponent.builder()
                .application(new SpokenBaseApplication())
                .applicationModule(new ApplicationModule())
                .localVideoRepoModule(new LocalVideoRepoModule())
                .build();
        applicationComponent.inject(this);
        localAudioPageViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(LocalAudioPageViewModel.class);

        recyclerViewAudios = root.findViewById(R.id.localAudioList);
        RecyclerView.LayoutManager videosLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerViewAudios.setLayoutManager(videosLayoutManager);
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(localAudioPageViewModel.getAudios(this.getActivity().getApplication())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<LocalAudioEntity>>() {
                    @Override
                    public void accept(List<LocalAudioEntity> localAudioEntities) throws Exception {
                        if(localAudioEntities != null) {
                            localAudioListAdapter = new LocalAudioListAdapter(localAudioEntities,this );
                            recyclerViewAudios.setAdapter(localAudioListAdapter);
                            Log.e("RetrieveVideos", ""+localAudioEntities.size());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("LocalVideoPageFragment", "exception getting videos");
                    }
                })
        );

    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);

    }



}
