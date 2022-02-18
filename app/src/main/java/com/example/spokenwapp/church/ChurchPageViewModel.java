package com.example.spokenwapp.church;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.spokenwapp.data.model.DacastChannelAnalyticsEntity;
import com.example.spokenwapp.data.model.DacastLogEntity;
import com.example.spokenwapp.data.repository.LocalVideoRepository;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class ChurchPageViewModel extends AndroidViewModel {

    @Inject
    LocalVideoRepository localVideoRepository;
    private CompositeDisposable disposable;
    private MutableLiveData<List<DacastChannelAnalyticsEntity>> dacast = new MutableLiveData<>();
    private MutableLiveData<Boolean> dacastLoadError = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    List<DacastLogEntity> dacastLogEntitiesLogs;

    @Inject
    public ChurchPageViewModel(LocalVideoRepository localVideoRepository,
                               Application application) {
        super(application);
        this.localVideoRepository = localVideoRepository;
        disposable = new CompositeDisposable();
        dacastLogEntitiesLogs = new ArrayList<>();
        fetchDacastLogs();

    }

    public LiveData<List<DacastChannelAnalyticsEntity>> getRepos() {
        return dacast;
    }


    public LiveData<Boolean> getError() {
        return dacastLoadError;
    }


    public LiveData<Boolean> getLoading() {
        return loading;
    }


    public void fetchDacastLogs() {
        loading.setValue(true);
        disposable.add(getDacast()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<DacastChannelAnalyticsEntity>>() {

                    @Override
                    public void onNext(List<DacastChannelAnalyticsEntity> dacastLogEntities) {
                        if(dacastLogEntities != null) {
                            Log.e("MVVM ANALYTICS", String.valueOf(dacastLogEntities.size()));
                            dacastLoadError.setValue(false);
                            dacast.setValue(dacastLogEntities);
                            loading.setValue(false);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        dacastLoadError.setValue(true);
                        loading.setValue(false);
                    }

                    @Override
                    public void onComplete() {

                    }

                })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(disposable != null) {
            disposable.clear();
            disposable = null;
        }
    }

    public Flowable<List<DacastChannelAnalyticsEntity>> getDacast(){
        return new LocalVideoRepository(getApplication()).allDacastChannelAnalytics.cache();
    }

}
