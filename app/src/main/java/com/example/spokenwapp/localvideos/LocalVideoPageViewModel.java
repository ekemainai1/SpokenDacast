package com.example.spokenwapp.localvideos;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.spokenwapp.data.model.DacastLogEntity;
import com.example.spokenwapp.data.model.LocalVideoEntity;
import com.example.spokenwapp.data.repository.LocalVideoRepository;
import java.util.List;
import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;


public class LocalVideoPageViewModel extends AndroidViewModel {

    @Inject
    LocalVideoRepository localVideoRepository;
    private CompositeDisposable disposable;
    private MutableLiveData<List<LocalVideoEntity>> localVideoData = new MutableLiveData<>();
    private MutableLiveData<Boolean> localVideoLoadError = new MutableLiveData<>();
    private MutableLiveData<Boolean> localVideoLoading = new MutableLiveData<>();

    @Inject
    public LocalVideoPageViewModel(LocalVideoRepository localVideoRepository,
                               Application application) {
        super(application);
        this.localVideoRepository = localVideoRepository;
        disposable = new CompositeDisposable();
        fetchLocalVideos();
    }

    public LiveData<List<LocalVideoEntity>> getLocalVideoRepos() {
        return localVideoData;
    }

    public LiveData<Boolean> getLocalVideoError() {
        return localVideoLoadError;
    }


    public LiveData<Boolean> getLoading() {
        return localVideoLoading;
    }


    public Flowable<List<LocalVideoEntity>> getVideos(){
        return new LocalVideoRepository(getApplication()).allLocalVideos.cache();
    }

    public long insertVideos(LocalVideoEntity localVideoEntity){
        return localVideoRepository.insertAllLocalVideos(localVideoEntity);
    }

    public void deleteVideos(){
        localVideoRepository.deleteAllVideos();
    }

    public void fetchLocalVideos() {
        localVideoLoading.setValue(true);
        disposable.add(getVideos()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<LocalVideoEntity>>() {

                    @Override
                    public void onNext(List<LocalVideoEntity> localVideoEntities) {
                        if(localVideoEntities != null) {
                            // Log.e("MVVM", dacastLogEntities.get(0).getDescription());
                            localVideoLoadError.setValue(false);
                            localVideoData.setValue(localVideoEntities);
                            localVideoLoading.setValue(false);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        localVideoLoadError.setValue(true);
                        localVideoLoading.setValue(false);
                    }

                    @Override
                    public void onComplete() {

                    }

                }));

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.clear();
            disposable = null;
        }
    }

}
