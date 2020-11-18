package com.e.diteyb.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<Boolean> wrongPassword;

    public LoginViewModel() {
        wrongPassword = new MutableLiveData<>();
        wrongPassword.setValue(false);
    }

    public void isWrong(boolean b) {
        wrongPassword.setValue(b);
    }

    public LiveData<Boolean> getLoginState() {
        return wrongPassword;
    }

}
