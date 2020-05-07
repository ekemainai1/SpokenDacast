package com.example.spokenwapp.di.components;


import android.app.Application;

import com.example.spokenwapp.online.SpokenChooseURLFragment;
import com.example.spokenwapp.online.SpokenOnlineService;
import com.example.spokenwapp.selectedchurch.SelectedChurchFragment;
import com.example.spokenwapp.base.SplashScreen;
import com.example.spokenwapp.base.SpokenBaseApplication;
import com.example.spokenwapp.base.SpokenMainScreen;
import com.example.spokenwapp.base.ViewModelFactory;
import com.example.spokenwapp.church.ChurchPageFragment;
import com.example.spokenwapp.di.modules.ActivityBindingModule;
import com.example.spokenwapp.di.modules.ApplicationModule;
import com.example.spokenwapp.di.modules.ContextModule;
import com.example.spokenwapp.di.modules.LoadVideoServiceModule;
import com.example.spokenwapp.di.modules.LocalVideoRepoModule;
import com.example.spokenwapp.localaudio.LocalAudioPageFragment;
import com.example.spokenwapp.localvideos.LocalVideoPageFragment;
import com.example.spokenwapp.services.LoadLocalVideoService;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(modules = {ContextModule.class,
        ApplicationModule.class,
        AndroidSupportInjectionModule.class,
        ActivityBindingModule.class,
        LoadVideoServiceModule.class,
        LocalVideoRepoModule.class})

public interface ApplicationComponent extends AndroidInjector<DaggerApplication>{

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        Builder applicationModule(ApplicationModule applicationModule);
        Builder localVideoRepoModule(LocalVideoRepoModule localVideoRepoModule);
        ApplicationComponent build();
    }

    void inject(SpokenBaseApplication application);
    void inject(SplashScreen splashScreen);
    void inject(SpokenMainScreen spokenMainScreen);
    void inject(ChurchPageFragment churchPageFragment);
    void inject(LocalVideoPageFragment localVideoPageFragment);
    void inject(LocalAudioPageFragment localAudioPageFragment);
    void inject(LoadLocalVideoService loadLocalVideoService);
    void inject(SelectedChurchFragment selectedChurchFragment);
    void inject(SpokenOnlineService spokenOnlineService);
    void inject(SpokenChooseURLFragment spokenChooseURLFragment);
    void inject(ViewModelFactory viewModelFactory);


}
