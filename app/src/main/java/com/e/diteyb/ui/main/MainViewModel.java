package com.e.diteyb.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private MutableLiveData<String> edtContentText, edtTitleText;

    public MainViewModel() {
        edtContentText = new MutableLiveData<>();
        edtTitleText = new MutableLiveData<>();

    }

    public void setEdtContentText(String s) {
        edtContentText.setValue(s);
    }

    public void setEdtTitle(String s) {
        edtTitleText.setValue(s);
    }

    public LiveData<String> getContentText() {
        return edtContentText;
    }

    public LiveData<String> getEdtTitleText() {
        return edtTitleText;
    }

}
