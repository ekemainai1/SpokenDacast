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


public class SpokenDacastWorkManager extends Worker {

    @Inject
    RepositoryService repositoryService;
    @Inject
    LocalVideoRepository localVideoRepository;
    private CompositeDisposable disposables = new CompositeDisposable();
    @Inject
    SpokenNetworkUtils spokenNetworkUtils;
    SpokenDacastNotification spokenDacastNotification;
    SpokenSharedPreferences spokenSharedPreferences;
    @Inject
    Application application;
    boolean network;
    public static int notificationIdLive = 400;
    public static String CHANNEL_ID_LIVE = "DACAST LIVE";
    public static String DACAST_LIVE_NOTE = "Service Alert";
    DacastObject dacastObject;
    List<DacastChannelAnalytics> dacastChannelAnalytics;

    ApplicationComponent applicationComponent = DaggerApplicationComponent.builder()
            .application(new SpokenBaseApplication())
            .applicationModule(new ApplicationModule())
            .localVideoRepoModule(new LocalVideoRepoModule())
            .build();

    public SpokenDacastWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        dacastObject = new DacastObject();
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
                    dacastLiveBackgroundTaskScheduler();
                    scheduleShowNotificationDacastLive();
                } else {
                    dacastLiveBackgroundTaskScheduler();
                    scheduleShowNotificationDacastLive();
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
        Log.i(TAG, "OnStopped called for this LIVE worker");
    }

    // Background task for Dacast live streaming
    public void dacastLiveBackgroundTaskScheduler() {

        disposables.add(dacastLiveObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.computation())
                // Be notified on the main thread
                .observeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<List<DacastObject>>() {
                                   @Override public void onComplete() {
                                       Log.d(TAG, "onComplete()");
                                   }

                                   @Override public void onError(Throwable e) {
                                       Log.e(TAG, "onError()", e);
                                   }

                                   @Override public void onNext(List<DacastObject> dacastLogEntities) {
                                       Log.d(TAG, "onNext(" + dacastLogEntities+ ")");

                                   }
                               }
                )
        );
    }

    // Dacast logs observables
    Observable<List<DacastObject>> dacastLiveObservable () {
        List<DacastObject> dacastModels = new ArrayList<>();
        repositoryService = new RepositoryService(applicationComponent.getRetrofit());
        dacastObject = repositoryService.invokeDacastChannelsList(localVideoRepository,
                getApplicationContext());
        dacastModels.add(dacastObject);
        // Log.e("Dacast List", dacastModel.getTotalCount());
        return Observable.just(dacastModels);
    }

    // Schedule notification for Dacast live streaming
    public void scheduleShowNotificationDacastLive() {
        spokenDacastNotification = new SpokenDacastNotification();
        spokenSharedPreferences = new SpokenSharedPreferences(this.getApplicationContext());
        String state = spokenSharedPreferences.getStreamPlay();


            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                if (!spokenSharedPreferences.getLogVodTitle().equals("") || spokenSharedPreferences.getChurchOnline())
                    spokenDacastNotification.sendNotification(application,
                            DACAST_LIVE_NOTE,
                            spokenSharedPreferences.getLogTitle(),
                            spokenSharedPreferences.getLogDate(),
                            spokenSharedPreferences.getLogtime(),
                            notificationIdLive);
                }
             else {
                if (!spokenSharedPreferences.getLogVodTitle().equals("") || spokenSharedPreferences.getChurchOnline()) {
                    spokenDacastNotification.sendNotificationAPI(this.getApplicationContext(),
                            DACAST_LIVE_NOTE,
                            spokenSharedPreferences.getLogTitle(),
                            spokenSharedPreferences.getLogDate(),
                            spokenSharedPreferences.getLogtime(),
                            CHANNEL_ID_LIVE,
                            notificationIdLive);
                }
            }
        }


}
