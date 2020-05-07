package com.example.spokenwapp.di.modules;

import android.app.Application;

import com.example.spokenwapp.base.LocalVideoScope;
import com.example.spokenwapp.data.database.AppLocalDatabase;
import com.example.spokenwapp.data.model.LocalAudioDao;
import com.example.spokenwapp.data.model.LocalVideoDao;
import com.example.spokenwapp.data.repository.LocalVideoRepository;

import javax.inject.Named;
import dagger.Module;
import dagger.Provides;
import io.reactivex.rxjava3.disposables.CompositeDisposable;


@Module
public class LocalVideoRepoModule {

    @LocalVideoScope
    @Provides
    LocalVideoDao provideLocalLocalVideoDao(AppLocalDatabase appLocalDatabase){
        return appLocalDatabase.localVideoDao();
    }

    @LocalVideoScope
    @Provides
    LocalVideoRepository provideLocalVideoRepository(Application application){
        return new LocalVideoRepository(application);
    }

    @LocalVideoScope
    @Provides @Named("activity")
    CompositeDisposable getCompositeDisposable(){
        return new CompositeDisposable();
    }

    @LocalVideoScope
    @Provides @Named("lv")
    CompositeDisposable getLVCompositeDisposable(){
        return new CompositeDisposable();
    }

    @LocalVideoScope
    @Provides
    LocalAudioDao provideLocalLocalAudioDao(AppLocalDatabase appLocalDatabase){
        return appLocalDatabase.localAudioDao();
    }


}
