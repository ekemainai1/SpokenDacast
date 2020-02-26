package com.example.spokenwapp.ui.church;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChurchPageViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ChurchPageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is church fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
