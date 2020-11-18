package com.e.diteyb.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<Boolean> wrongPassword;

    public RegisterViewModel() {
        wrongPassword = new MutableLiveData<>();
        wrongPassword.setValue(false);
    }

    public void isWrong(boolean b) {
        wrongPassword.setValue(b);
    }

    public LiveData<Boolean> getRegisterState() {
        return wrongPassword;
    }
}
