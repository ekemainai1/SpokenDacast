package com.example.spokenwapp.ui.equalizer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class EqualizerViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    @Inject
    public EqualizerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is equalizer fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}