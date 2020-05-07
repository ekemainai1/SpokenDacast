package com.example.spokenwapp.di.modules;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class ContextModule {

    @Singleton
    @Binds
    abstract Context provideContext(Application application);
}
