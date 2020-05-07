package com.example.spokenwapp.di.modules;

import com.example.spokenwapp.services.LoadLocalVideoService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class LoadVideoServiceModule {

    @ContributesAndroidInjector
    protected abstract LoadLocalVideoService provideLoadVideoService();
}
