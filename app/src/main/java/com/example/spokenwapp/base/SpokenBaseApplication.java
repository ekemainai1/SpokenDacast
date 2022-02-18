package com.example.spokenwapp.base;
import android.app.Application;
import android.util.Log;
import com.example.spokenwapp.di.components.ApplicationComponent;
import com.example.spokenwapp.di.components.DaggerApplicationComponent;
import com.example.spokenwapp.di.modules.ApplicationModule;
import com.example.spokenwapp.di.modules.LocalVideoRepoModule;
import com.example.spokenwapp.events.SpokenEvents;
import com.example.spokenwapp.events.SpokenRxEventBus;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class SpokenBaseApplication extends DaggerApplication {

    private static ApplicationComponent applicationComponent;
    private  Application application;
    private SpokenRxEventBus spokenRxEventBus;

    public static ApplicationComponent getApplicationComponent(){
        return applicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = getApplicationComponent();
        spokenRxEventBus = new SpokenRxEventBus();
        Log.e("Before event", ""+ System.currentTimeMillis());
        new Thread(){
            @Override
            public void run(){
                //SystemClock.sleep(100);
                spokenRxEventBus.setSpokenBus(new SpokenEvents.Message("Song Title",
                        0, 200)
                );
            }
        }.start();
        Log.e("After event", ""+System.currentTimeMillis());
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

    public SpokenRxEventBus spokenRxEventBus(){
        return spokenRxEventBus;
    }


}
