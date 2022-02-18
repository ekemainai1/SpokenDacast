package com.example.spokenwapp.services;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.spokenwapp.base.SpokenBaseApplication;
import com.example.spokenwapp.dacast.DacastObject;
import com.example.spokenwapp.dacastanalytics.DacastChannelAnalytics;
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

public class SpokenAnalyticsWorkManager extends Worker {

    @Inject
    RepositoryService repositoryService;
    @Inject
    LocalVideoRepository localVideoRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Inject
    SpokenNetworkUtils spokenNetworkUtils;

    @Inject
    Application application;
    boolean network;
    List<DacastChannelAnalytics> dacastChannelAnalytics;

    ApplicationComponent applicationComponent = DaggerApplicationComponent.builder()
            .application(new SpokenBaseApplication())
            .applicationModule(new ApplicationModule())
            .localVideoRepoModule(new LocalVideoRepoModule())
            .build();

    public SpokenAnalyticsWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        dacastChannelAnalytics = new ArrayList<>();
        repositoryService = new RepositoryService(applicationComponent.getRetrofit());
        localVideoRepository = new LocalVideoRepository(application);
        spokenNetworkUtils = new SpokenNetworkUtils();
        network = spokenNetworkUtils.isNetworkOnline(getApplicationContext());
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "Fetching Data from Remote host");

        try {
            if (network) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    //Toast.makeText(getApplicationContext(),
                            //"Android Version not supported", Toast.LENGTH_LONG).show();
                    dacastAnalyticsBackgroundTaskScheduler();
                } else {
                    dacastAnalyticsBackgroundTaskScheduler();
                }


            } else {
                Toast.makeText(getApplicationContext(),
                        "Network is not available!", Toast.LENGTH_LONG).show();
            }
            return Result.success();
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "Error fetching data", e);
        }
        return Result.failure();
    }


    @Override
    public void onStopped() {
        super.onStopped();
        Log.i(TAG, "OnStopped called for this ANALYTICS worker");
    }

    // Background task for Dacast live streaming
    public void dacastAnalyticsBackgroundTaskScheduler() {

        compositeDisposable.add(dacastAnalyticsObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(Schedulers.newThread())
                .subscribeWith(new DisposableObserver<List<DacastChannelAnalytics>>() {
                                   @Override public void onComplete() {
                                       Log.d(TAG, "onComplete()");
                                   }

                                   @Override public void onError(Throwable e) {
                                       Log.e(TAG, "onError()", e);
                                   }

                                   @Override public void onNext(List<DacastChannelAnalytics>
                                                                        dacastChannelAnalyticsList) {
                                       Log.d(TAG, "onNext(" + dacastChannelAnalyticsList+ ")");

                                   }
                               }
                )
        );
    }

    // Dacast logs observables
    Observable<List<DacastChannelAnalytics>> dacastAnalyticsObservable() {
        repositoryService = new RepositoryService(applicationComponent.getRetrofit());
        dacastChannelAnalytics = repositoryService.invokeDacastAnalyticsList(localVideoRepository);
        // Log.e("Dacast List", dacastChannelAnalytics.get(0).getVisitors().toString());
        return Observable.just(dacastChannelAnalytics);
    }

}
