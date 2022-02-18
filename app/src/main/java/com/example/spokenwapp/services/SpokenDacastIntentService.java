package com.example.spokenwapp.services;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.work.Configuration;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.spokenwapp.base.SpokenBaseApplication;
import com.example.spokenwapp.dacast.DacastObject;
import com.example.spokenwapp.dacastvod.DacastVODObject;
import com.example.spokenwapp.data.repository.LocalVideoRepository;
import com.example.spokenwapp.data.repository.RepositoryService;
import com.example.spokenwapp.di.components.ApplicationComponent;
import com.example.spokenwapp.di.components.DaggerApplicationComponent;
import com.example.spokenwapp.di.modules.ApplicationModule;
import com.example.spokenwapp.di.modules.LocalVideoRepoModule;
import com.example.spokenwapp.utilities.SpokenDacastNotification;
import com.example.spokenwapp.utilities.SpokenNetworkUtils;
import com.example.spokenwapp.utilities.SpokenSharedPreferences;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DaggerIntentService;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SpokenDacastIntentService extends DaggerIntentService {


    public SpokenDacastIntentService() {
        super("SpokenDacastIntentService");
    }

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();

    }

    protected void onHandleIntent(@Nullable Intent intent) {

    }



}
