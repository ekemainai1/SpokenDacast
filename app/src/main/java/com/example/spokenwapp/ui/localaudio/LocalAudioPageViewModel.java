package com.example.spokenwapp.ui.localaudio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocalAudioPageViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public LocalAudioPageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is local audio fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
