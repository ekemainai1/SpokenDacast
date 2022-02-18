package com.example.spokenwapp.localaudio;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava2.PagingRx;

import com.example.spokenwapp.data.database.SpokenAudioPagingDataSource;
import com.example.spokenwapp.data.model.LocalAudioEntity;
import com.example.spokenwapp.data.repository.LocalVideoRepository;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class LocalAudioPageViewModel extends AndroidViewModel {

    @Inject
    LocalVideoRepository localVideoRepository;
    private CompositeDisposable disposable;
    private MutableLiveData<List<LocalAudioEntity>> localAudioData = new MutableLiveData<>();
    private MutableLiveData<Boolean> localAudioError = new MutableLiveData<>();
    private MutableLiveData<Boolean> localAudioLoading = new MutableLiveData<>();

    @Inject
    public LocalAudioPageViewModel(LocalVideoRepository localVideoRepository, Application application) {
        super(application);
        this.localVideoRepository = localVideoRepository;
        disposable = new CompositeDisposable();
        fetchLocalAudio();
    }

    public LiveData<List<LocalAudioEntity>> getLocalAudioRepos() {
        return localAudioData;
    }


    public LiveData<Boolean> getLocalAudioError() {
        return localAudioError;
    }


    public LiveData<Boolean> getLocalAudioLoading() {
        return localAudioLoading;
    }

    public Flowable<List<LocalAudioEntity>> getAudios(){
        return new LocalVideoRepository(getApplication()).allLocalAudio;
    }

    public long insertAudios(LocalAudioEntity localAudioEntity){
        return localVideoRepository.insertAllLocalAudios(localAudioEntity);
    }

    public void deleteAudios(){
        localVideoRepository.deleteAllAudio();
    }

    public void fetchLocalAudio() {
        localAudioLoading.setValue(true);
        disposable.add(getAudios()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<LocalAudioEntity>>() {

                    @Override
                    public void onNext(List<LocalAudioEntity> localAudioEntities) {
                        if(localAudioEntities != null) {
                            Collections.sort(localAudioEntities, new Comparator<LocalAudioEntity>() {
                                @Override
                                public int compare(LocalAudioEntity localAudioEntity, LocalAudioEntity textSort) {
                                    return localAudioEntity.getLocalAudioTitle().compareTo(textSort.getLocalAudioTitle());
                                }
                            });

                            // Log.e("MVVM", dacastLogEntities.get(0).getDescription());
                            localAudioError.setValue(false);
                            localAudioData.setValue(localAudioEntities);
                            localAudioLoading.setValue(false);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        localAudioError.setValue(true);
                        localAudioLoading.setValue(false);
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
