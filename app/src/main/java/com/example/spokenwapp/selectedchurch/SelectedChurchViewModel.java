package com.example.spokenwapp.selectedchurch;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.util.Util;
import com.example.spokenwapp.data.model.DacastAudioSermonEntity;
import com.example.spokenwapp.data.model.DacastSpecialSermonEntity;
import com.example.spokenwapp.data.model.VodDacastLogEntity;
import com.example.spokenwapp.data.repository.LocalVideoRepository;

import java.io.File;
import java.util.List;
import javax.inject.Inject;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class SelectedChurchViewModel extends AndroidViewModel {

    @Inject
    LocalVideoRepository localVideoRepository;

    // Video Sermons
    private CompositeDisposable disposable;
    private MutableLiveData<List<VodDacastLogEntity>> dacastVod = new MutableLiveData<>();
    private MutableLiveData<Boolean> dacastVodLoadError = new MutableLiveData<>();
    private MutableLiveData<Boolean> vodLoading = new MutableLiveData<>();

    // Audio sermons
    private CompositeDisposable disposableAudio;
    private MutableLiveData<List<DacastAudioSermonEntity>> dacastAudio = new MutableLiveData<>();
    private MutableLiveData<Boolean> dacastAudioLoadError = new MutableLiveData<>();
    private MutableLiveData<Boolean> audioLoading = new MutableLiveData<>();

    // Specials
    private CompositeDisposable disposableSpecials;
    private MutableLiveData<List<DacastSpecialSermonEntity>> dacastSpecials = new MutableLiveData<>();
    private MutableLiveData<Boolean> dacastSpecialsLoadError = new MutableLiveData<>();
    private MutableLiveData<Boolean> specialsLoading = new MutableLiveData<>();


    @Inject
    public SelectedChurchViewModel(LocalVideoRepository localVideoRepository, Application application) {
            super(application);
            this.localVideoRepository = localVideoRepository;
            disposable = new CompositeDisposable();
            disposableAudio = new CompositeDisposable();
            disposableSpecials = new CompositeDisposable();
            fetchDacastVodLogs();
            fetchDacastAudios();
            fetchDacastSpecial();

    }

    // Videos
    public LiveData<List<VodDacastLogEntity>> getVodRepos() {
        return dacastVod;
    }


    public LiveData<Boolean> getVodError() {
        return dacastVodLoadError;
    }


    public LiveData<Boolean> getVodLoading() {
        return vodLoading;
    }

    // Audios
    public LiveData<List<DacastAudioSermonEntity>> getAudioRepos() {
        return dacastAudio;
    }


    public LiveData<Boolean> getAudioError() {
        return dacastAudioLoadError;
    }


    public LiveData<Boolean> getAudioLoading() {
        return audioLoading;
    }

    // Specials
    public LiveData<List<DacastSpecialSermonEntity>> getSpecialsRepos() {
        return dacastSpecials;
    }


    public LiveData<Boolean> getSpecialsError() {
        return dacastSpecialsLoadError;
    }


    public LiveData<Boolean> getSpecialsLoading() {
        return audioLoading;
    }

    // Videos
    public void fetchDacastVodLogs() {
        vodLoading.setValue(true);
        disposable.add(getDacastVod()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<VodDacastLogEntity>>() {

                    @Override
                    public void onNext(List<VodDacastLogEntity> dacastLogEntities) {
                        if(dacastLogEntities != null) {
                            // Log.e("MVVM", dacastLogEntities.get(0).getDescription());
                            dacastVodLoadError.setValue(false);
                            dacastVod.setValue(dacastLogEntities);
                            vodLoading.setValue(false);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        dacastVodLoadError.setValue(true);
                        vodLoading.setValue(false);
                    }

                    @Override
                    public void onComplete() {

                    }

                }));

    }

    // Audios
    public void fetchDacastAudios() {
        audioLoading.setValue(true);
        disposableAudio.add(getDacastAudio()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<DacastAudioSermonEntity>>() {

                    @Override
                    public void onNext(List<DacastAudioSermonEntity> dacastVideoSermonEntities) {
                        if(dacastVideoSermonEntities != null) {
                            //Log.e("MVVM", dacastVideoSermonEntities.get(0).getDescription());
                            dacastAudioLoadError.setValue(false);
                            dacastAudio.setValue(dacastVideoSermonEntities);
                            audioLoading.setValue(false);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        dacastAudioLoadError.setValue(true);
                        audioLoading.setValue(false);
                    }

                    @Override
                    public void onComplete() {

                    }

                }));

    }

    // Specials
    public void fetchDacastSpecial() {
        specialsLoading.setValue(true);
        disposableSpecials.add(getDacastSpecials()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<DacastSpecialSermonEntity>>() {

                    @Override
                    public void onNext(List<DacastSpecialSermonEntity> dacastAudioSermonEntities) {
                        if(dacastAudioSermonEntities != null) {
                            // Log.e("MVVM", dacastLogEntities.get(0).getDescription());
                            dacastSpecialsLoadError.setValue(false);
                            dacastSpecials.setValue(dacastAudioSermonEntities);
                            specialsLoading.setValue(false);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        dacastSpecialsLoadError.setValue(true);
                        specialsLoading.setValue(false);
                    }

                    @Override
                    public void onComplete() {

                    }

                }));

    }


    // Video
    public Flowable<List<VodDacastLogEntity>> getDacastVod(){
        return new LocalVideoRepository(getApplication()).allDacastVodLogs;
    }

    // Audio
    public Flowable<List<DacastAudioSermonEntity>> getDacastAudio(){
        return new LocalVideoRepository(getApplication()).allDacastVideoSermons;
    }

    // Specials
    public Flowable<List<DacastSpecialSermonEntity>> getDacastSpecials(){
        return new LocalVideoRepository(getApplication()).allDacastAudioSermons;
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.clear();
            disposable = null;
        }

        if (disposableAudio != null) {
            disposableAudio.clear();
            disposableAudio = null;
        }

        if (disposableSpecials != null) {
            disposableSpecials.clear();
            disposableSpecials = null;
        }
    }
}
