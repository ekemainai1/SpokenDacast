package com.example.spokenwapp.services;

import android.util.Log;
import com.example.spokenwapp.base.SpokenBaseApplication;
import com.example.spokenwapp.dacast.DacastObject;
import com.example.spokenwapp.data.repository.LocalVideoRepository;
import com.example.spokenwapp.data.repository.RepositoryService;
import com.example.spokenwapp.di.components.ApplicationComponent;
import com.example.spokenwapp.di.components.DaggerApplicationComponent;
import com.example.spokenwapp.di.modules.ApplicationModule;
import com.example.spokenwapp.di.modules.LocalVideoRepoModule;
import com.firebase.jobdispatcher.JobService;
import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class SpokenDacastJobScheduler extends JobService {

    @Inject
    RepositoryService repositoryService;
    @Inject
    LocalVideoRepository localVideoRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    DacastObject dacastModels;


    ApplicationComponent applicationComponent = DaggerApplicationComponent.builder()
            .application(new SpokenBaseApplication())
            .applicationModule(new ApplicationModule())
            .localVideoRepoModule(new LocalVideoRepoModule())
            .build();


    public void backgroundTaskSchedulerSync() {

            disposables.add(dacastListObservableSync()
                    // Run on a background thread
                    .subscribeOn(Schedulers.computation())
                    // Be notified on the main thread
                    .observeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<DacastObject>() {
                        @Override public void onComplete() {
                            Log.d(TAG, "onComplete()");
                        }

                        @Override public void onError(Throwable e) {
                            Log.e(TAG, "onError()", e);
                        }

                        @Override public void onNext(DacastObject dacastObjects) {
                            Log.d(TAG, "onNext(" + dacastObjects + ")");

                        }
                    }
                    )
            );
    }

    Observable<DacastObject> dacastListObservableSync () {
        repositoryService = new RepositoryService(applicationComponent.getRetrofit());
        dacastModels = repositoryService.invokeDacastChannelsList(localVideoRepository,
                getApplicationContext());
        Log.e("Dacast List", dacastModels.getTotalCount());
        return Observable.just(dacastModels);

    }

    @Override
    public boolean onStartJob(com.firebase.jobdispatcher.JobParameters job) {
        applicationComponent.inject(this);
        // Retrieve streaming web server info
        backgroundTaskSchedulerSync();
        return true;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        disposables.clear();
        return false;
    }
}
