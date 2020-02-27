package com.example.spokenwapp.localvideos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocalVideoPageViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public LocalVideoPageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is local video fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
