package com.example.spokenwapp.ui.scan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class ScanAppViewModel extends ViewModel {

    private MutableLiveData<String> mText;


    @Inject
    public ScanAppViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is scan fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}