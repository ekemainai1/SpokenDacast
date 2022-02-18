package com.example.spokenwapp.di.modules;

import com.example.spokenwapp.services.LoadLocalVideoService;
import com.example.spokenwapp.services.SpokenAnalyticsWorkManager;
import com.example.spokenwapp.services.SpokenDacastIntentService;
import com.example.spokenwapp.services.SpokenDacastJobScheduler;
import com.example.spokenwapp.services.SpokenDacastVodIntentService;
import com.example.spokenwapp.services.SpokenDacastVodWorkManager;
import com.example.spokenwapp.services.SpokenDacastWorkManager;
import com.example.spokenwapp.services.SpokenMediaBrowserService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class LoadVideoServiceModule {

    @ContributesAndroidInjector
    protected abstract LoadLocalVideoService provideLoadVideoService();

    @ContributesAndroidInjector
    protected abstract SpokenDacastJobScheduler provideSpokenDacastJobScheduler();

    @ContributesAndroidInjector
    abstract SpokenDacastIntentService provideSpokenDacastIntentService();

    @ContributesAndroidInjector
    abstract SpokenDacastWorkManager provideSpokenDacastWorkManager();

    @ContributesAndroidInjector
    abstract SpokenDacastVodIntentService provideSpokenDacastVodIntentService();

    @ContributesAndroidInjector
    abstract SpokenDacastVodWorkManager provideSpokenDacastVodWorkManager();

    @ContributesAndroidInjector
    abstract SpokenAnalyticsWorkManager provideSpokenAnalyticsWorkManager();

    @ContributesAndroidInjector
    abstract SpokenMediaBrowserService provideSpokenMediaBrowserService();
}
