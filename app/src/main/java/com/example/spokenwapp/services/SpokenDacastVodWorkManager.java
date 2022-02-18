package com.example.spokenwapp.services;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.spokenwapp.base.SpokenBaseApplication;
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
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SpokenDacastVodWorkManager extends Worker {
    @Inject
    RepositoryService repositoryService;
    @Inject
    LocalVideoRepository localVideoRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    List<DacastVODObject> dacastVODObjects;
    @Inject
    SpokenNetworkUtils spokenNetworkUtils;
    SpokenDacastNotification spokenDacastNotification;
    SpokenSharedPreferences spokenSharedPreferences;
    @Inject
    Application application;
    boolean network;
    public static int notificationIdVod = 500;
    public static String CHANNEL_ID_VOD = "DACAST VOD";
    public static String DACAST_LIVE_VOD = "Content Alert";

    ApplicationComponent applicationComponent = DaggerApplicationComponent.builder()
            .application(new SpokenBaseApplication())
            .applicationModule(new ApplicationModule())
            .localVideoRepoModule(new LocalVideoRepoModule())
            .build();

    public SpokenDacastVodWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        dacastVODObjects = new ArrayList<DacastVODObject>();
        repositoryService = new RepositoryService(applicationComponent.getRetrofit());
        spokenNetworkUtils = new SpokenNetworkUtils();
        localVideoRepository = new LocalVideoRepository(application);
        network = spokenNetworkUtils.isNetworkOnline(getApplicationContext());
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "Fetching VOD Data from Remote host");

        try {
        if(network) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

                        //Toast.makeText(getApplicationContext(),
                                //"Android Version not supported", Toast.LENGTH_LONG).show();
                dacastVodBackgroundTaskScheduler();
                scheduleShowNotificationDacastVod();

            }else {
                dacastVodBackgroundTaskScheduler();
                scheduleShowNotificationDacastVod();
            }
        }else {

                    Toast.makeText(getApplicationContext(),
                            "Network is not available!", Toast.LENGTH_LONG).show();
        }

            return Result.success();
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "Error Dacast VOD fetching data", e);
        }
        return Result.retry();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.i(TAG, "OnStopped called for this VOD worker");
    }

    // Background task for Dacast live streaming
    public void dacastVodBackgroundTaskScheduler() {

        disposables.add(dacastVodObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.computation())
                // Be notified on the main thread
                .observeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<List<DacastVODObject>>() {
                                   @Override public void onComplete() {
                                       Log.d(TAG, "onComplete()");
                                   }

                                   @Override public void onError(Throwable e) {
                                       Log.e(TAG, "onError()", e);
                                   }

                                   @Override public void onNext(List<DacastVODObject> dacastObjects) {
                                       Log.d(TAG, "onNext(" + dacastObjects + ")");

                                   }
                               }
                )
        );
    }

    // Dacast logs observables
    Observable<List<DacastVODObject>> dacastVodObservable () {
        repositoryService = new RepositoryService(applicationComponent.getRetrofit());
        DacastVODObject dacastModel = repositoryService
                .invokeDacastVoDList(localVideoRepository, getApplicationContext());
        dacastVODObjects.add(dacastModel);
        //Log.e("Dacast List", dacastModel.getTotalCount());
        return Observable.just(dacastVODObjects);

    }

    // Schedule notification for Dacast live streaming
    public void scheduleShowNotificationDacastVod() {
        spokenDacastNotification = new SpokenDacastNotification();
        spokenSharedPreferences = new SpokenSharedPreferences(getApplicationContext());
        int state = spokenSharedPreferences.getVODOnlineServiceState();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            if (!spokenSharedPreferences.getLogVodTitle().equals("") || spokenSharedPreferences.getVODOnlineServiceState()>2)
                spokenDacastNotification.sendNotification(application,
                        DACAST_LIVE_VOD,
                        spokenSharedPreferences.getLogVodTitle(),
                        spokenSharedPreferences.getLogVodDate(),
                        spokenSharedPreferences.getLogVodTime(),
                        notificationIdVod);

        } else {
            if (!spokenSharedPreferences.getLogVodTitle().equals("") || spokenSharedPreferences.getVODOnlineServiceState()>2) {
                spokenDacastNotification.sendNotificationAPI(getApplicationContext(),
                        DACAST_LIVE_VOD,
                        spokenSharedPreferences.getLogVodTitle(),
                        spokenSharedPreferences.getLogVodDate(),
                        spokenSharedPreferences.getLogVodTime(),
                        CHANNEL_ID_VOD,
                        notificationIdVod);
            }
        }

    }


}
