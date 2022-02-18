package com.example.spokenwapp.di.modules;


import com.example.spokenwapp.base.SplashScreen;
import com.example.spokenwapp.base.SpokenMainScreen;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = {MainFragmentBindingModule.class})
    abstract SpokenMainScreen bindSpokenMainScreen();

    @ContributesAndroidInjector
    abstract SplashScreen bindSplashScreen();
}
