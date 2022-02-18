package com.example.spokenwapp.ui.artwork;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class ArtWorkViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    @Inject
    public ArtWorkViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is artwork fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}