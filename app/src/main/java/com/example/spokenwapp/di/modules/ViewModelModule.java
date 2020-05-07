package com.example.spokenwapp.di.modules;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.spokenwapp.online.SpokenChooseURLViewModel;
import com.example.spokenwapp.online.SpokenOnlineService;
import com.example.spokenwapp.online.SpokenOnlineViewModel;
import com.example.spokenwapp.selectedchurch.SelectedChurchViewModel;
import com.example.spokenwapp.base.ViewModelFactory;
import com.example.spokenwapp.base.ViewModelKey;
import com.example.spokenwapp.church.ChurchPageViewModel;
import com.example.spokenwapp.localaudio.LocalAudioPageViewModel;
import com.example.spokenwapp.localvideos.LocalVideoPageViewModel;
import com.example.spokenwapp.ui.artwork.ArtWorkViewModel;
import com.example.spokenwapp.ui.equalizer.EqualizerViewModel;
import com.example.spokenwapp.ui.pager.SpokenViewPagerViewModel;
import com.example.spokenwapp.ui.scan.ScanAppViewModel;


import javax.inject.Singleton;

import dagger.Binds;
import dagger.BindsInstance;
import dagger.Module;
import dagger.multibindings.IntoMap;


@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(ArtWorkViewModel.class)
    abstract ViewModel bindArtWorkViewModel(@BindsInstance ArtWorkViewModel artWorkViewModel);

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(ScanAppViewModel.class)
    abstract ViewModel bindScanViewModel(@BindsInstance ScanAppViewModel detailsViewModel);

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(EqualizerViewModel.class)
    abstract ViewModel bindEqualizerViewModel(@BindsInstance EqualizerViewModel equalizerViewModel);

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(SpokenViewPagerViewModel.class)
    abstract ViewModel bindSpokenViewPagerViewModel(@BindsInstance SpokenViewPagerViewModel spokenViewPagerViewModel);

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(ChurchPageViewModel.class)
    abstract ViewModel bindChurchPageViewModel(@BindsInstance ChurchPageViewModel churchPageViewModel);

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(LocalAudioPageViewModel.class)
    abstract ViewModel bindLocalAudioPageViewModel(@BindsInstance LocalAudioPageViewModel localAudioPageViewModel);

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(LocalVideoPageViewModel.class)
    abstract ViewModel bindLocalVideoViewModel(@BindsInstance LocalAudioPageViewModel localAudioPageViewModel);

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(SelectedChurchViewModel.class)
    abstract ViewModel bindSelectedChurchViewModel(@BindsInstance SelectedChurchViewModel selectedChurchViewModel);

    @Binds
    @Singleton
    abstract ViewModelProvider.Factory bindViewModelFactory(@BindsInstance ViewModelFactory factory);

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(SpokenOnlineViewModel.class)
    abstract ViewModel bindSpokenOnlineViewModel(@BindsInstance SpokenOnlineViewModel spokenOnlineViewModel);

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(SpokenChooseURLViewModel.class)
    abstract ViewModel bindSpokenChooseURLViewModel(@BindsInstance SpokenChooseURLViewModel spokenChooseURLViewModel);
}
