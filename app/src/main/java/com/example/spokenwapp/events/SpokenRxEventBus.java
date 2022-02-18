package com.example.spokenwapp.events;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class SpokenRxEventBus {

    public SpokenRxEventBus() {
    }

    private PublishSubject<Object> spokenBus = PublishSubject.create();

    public void setSpokenBus(Object o){
        spokenBus.onNext(o);
    }

    public Observable<Object> toObjectObservable(){
        return spokenBus;
    }

    public boolean hasObservers(){
        return spokenBus.hasObservers();
    }
}
