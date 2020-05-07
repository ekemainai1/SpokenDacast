package com.example.spokenwapp.base;
import android.content.Context;

import com.example.spokenwapp.di.components.ApplicationComponent;
import com.example.spokenwapp.di.components
        .DaggerApplicationComponent;
import com.example.spokenwapp.di.modules.ApplicationModule;
import com.example.spokenwapp.di.modules.LocalVideoRepoModule;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class SpokenBaseApplication extends DaggerApplication{

    private static ApplicationComponent applicationComponent;
    public static ApplicationComponent getApplicationComponent(){
        return applicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = getApplicationComponent();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        ApplicationComponent component = DaggerApplicationComponent.builder()
                .application(this)
                .applicationModule(new ApplicationModule())
                .localVideoRepoModule(new LocalVideoRepoModule())
                .build();
        component.inject(this);
        return component;

    }

}
