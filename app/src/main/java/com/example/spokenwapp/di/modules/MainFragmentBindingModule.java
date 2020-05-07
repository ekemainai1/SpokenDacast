package com.example.spokenwapp.di.modules;

import com.example.spokenwapp.online.SpokenChooseURLFragment;
import com.example.spokenwapp.online.SpokenOnlineService;
import com.example.spokenwapp.selectedchurch.SelectedChurchFragment;
import com.example.spokenwapp.church.ChurchPageFragment;
import com.example.spokenwapp.localaudio.LocalAudioPageFragment;
import com.example.spokenwapp.localvideos.LocalVideoPageFragment;
import com.example.spokenwapp.ui.artwork.ArtWorkFragment;
import com.example.spokenwapp.ui.equalizer.EqualizerFragment;
import com.example.spokenwapp.ui.pager.SpokenViewPager;
import com.example.spokenwapp.ui.scan.ScanAppFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
abstract class MainFragmentBindingModule {

    @ContributesAndroidInjector
    abstract ChurchPageFragment provideChurchPageFragment();

    @ContributesAndroidInjector
    abstract LocalAudioPageFragment provideLocalAudioPageFragment();

    @ContributesAndroidInjector
    abstract LocalVideoPageFragment provideLocalVideoPageFragment() ;

    @ContributesAndroidInjector
    abstract ArtWorkFragment provideArtWorkFragment();

    @ContributesAndroidInjector
    abstract EqualizerFragment provideEqualizerFragment();

    @ContributesAndroidInjector
    abstract SpokenViewPager provideSpokenViewPagerFragment();

    @ContributesAndroidInjector
    abstract ScanAppFragment provideScanAppFragment();

    @ContributesAndroidInjector
    abstract SelectedChurchFragment provideSelectedChurchFragment();

    @ContributesAndroidInjector
    abstract SpokenChooseURLFragment provideSpokenChooseURLFragment();

    @ContributesAndroidInjector
    abstract SpokenOnlineService provideSpokenOnlineServiceFragment();
}
