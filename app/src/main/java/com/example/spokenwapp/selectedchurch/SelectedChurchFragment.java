package com.example.spokenwapp.selectedchurch;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spokenwapp.R;
import com.example.spokenwapp.base.SpokenBaseApplication;
import com.example.spokenwapp.base.ViewModelFactory;
import com.example.spokenwapp.di.components.ApplicationComponent;
import com.example.spokenwapp.di.components.DaggerApplicationComponent;
import com.example.spokenwapp.di.modules.ApplicationModule;
import com.example.spokenwapp.di.modules.LocalVideoRepoModule;

import dagger.android.support.DaggerFragment;

public class SelectedChurchFragment extends DaggerFragment {

    ViewModelFactory viewModelFactory;
    ApplicationComponent applicationComponent;
    SelectedChurchViewModel selectedChurchViewModel;

    public static SelectedChurchFragment newInstance() {
        return new SelectedChurchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.selected_church_fragment, container, false);
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
        selectedChurchViewModel = ViewModelProviders.of(this, viewModelFactory).get(SelectedChurchViewModel.class);

    }

}
