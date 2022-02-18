package com.example.spokenwapp.search;

import android.app.Application;
import android.support.v4.media.MediaMetadataCompat;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.spokenwapp.adapters.LocalAudioListSearchAdapter;
import com.example.spokenwapp.data.model.LocalAudioEntity;
import com.example.spokenwapp.data.repository.LocalVideoRepository;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class SpokenLocalAudioSearchViewModel extends AndroidViewModel {

    public static final String TAG = SpokenLocalAudioSearchViewModel.class.getSimpleName();

    @Inject
    LocalVideoRepository localVideoRepository;
    PublishSubject<String> publishSubject;
    private CompositeDisposable disposable;


    @Inject
    public SpokenLocalAudioSearchViewModel(@NonNull Application application) {
        super(application);
        localVideoRepository = new LocalVideoRepository(getApplication());
        publishSubject = PublishSubject.create();
        disposable = new CompositeDisposable();
    }

    public void getAudioListForSearch(LocalAudioListSearchAdapter listSearchAdapter,
                                      List<LocalAudioEntity> audioEntities) {

        disposable.add(getAudiosForSearch()
                .subscribeOn(Schedulers.io())
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
                            for (int j = 0; j < localAudioEntities.size(); j++) {
                                Log.e("SEARCH SORT", localAudioEntities.get(j).getLocalAudioTitle());
                            }
                            audioEntities.clear();
                            audioEntities.addAll(localAudioEntities);
                            listSearchAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG,"FETCH SEARCH DATA: "+"completed");
                    }

                }));

    }

    public DisposableObserver<TextViewTextChangeEvent> performAudioListSearch(
            LocalAudioListSearchAdapter listSearchAdapter){

        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                Log.e(TAG,"Search query: " + textViewTextChangeEvent.component2());
                publishSubject.onNext(textViewTextChangeEvent.component2().toString());
                listSearchAdapter.getFilter().filter(textViewTextChangeEvent.component2().toString());

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };

    }

    public void executeAudioListSearch(EditText editText, LocalAudioListSearchAdapter listSearchAdapter){
        disposable.add((Disposable) RxTextView.textChangeEvents(editText)
                    .skipInitialValue()
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .distinctUntilChanged()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(performAudioListSearch(listSearchAdapter)));

        disposable.add(performAudioListSearch(listSearchAdapter));

    }

    public Flowable<List<LocalAudioEntity>> getAudiosForSearch(){
        return new LocalVideoRepository(getApplication()).allLocalAudio;
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