package com.e.diteyb.ui.login;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.e.diteyb.MainActivity;
import com.e.diteyb.R;
import com.e.diteyb.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;

public class LoginFragment extends Fragment {
Button btnLogin;
TextView loginRegister;
    NavController navController;
EditText edtEmail, edtPassword;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_login, container, false);
        btnLogin = root.findViewById(R.id.btn_login_frag);
        edtEmail = root.findViewById(R.id.edt_register_name_frag);
        edtPassword = root.findViewById(R.id.edt_register_password_frag);
        loginRegister = root.findViewById(R.id.btn_login_register_frag);
        final Snackbar snackbar = Snackbar.make(
                MainActivity.getInstance().drawer,"Fazendo login por favor aguarde...",Snackbar.LENGTH_SHORT);
        final LoginViewModel loginViewModel =
                new ViewModelProvider(MainActivity.MAIN_ACTIVITY_INSTANCE).get(LoginViewModel.class);
        navController = Navigation.findNavController(MainActivity.MAIN_ACTIVITY_INSTANCE, R.id.nav_host_fragment);

        loginViewModel.getLoginState().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    new AlertDialog.Builder(MainActivity.MAIN_ACTIVITY_INSTANCE)
                            .setTitle("Erro ao fazer login")
                            .setMessage("Email ou senha incorretos.")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create().show();
                    btnLogin.setEnabled(true);
                    loginViewModel.isWrong(false);
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String email = edtEmail.getText().toString(),
                            password = edtPassword.getText().toString();
                    VolleySingleton.getInstance().requestLogin(email, password);
                    snackbar.show();
                    btnLogin.setEnabled(false);
            }
        });





        loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_login_to_nav_register);
            }
        });
        return root;
    }

}
