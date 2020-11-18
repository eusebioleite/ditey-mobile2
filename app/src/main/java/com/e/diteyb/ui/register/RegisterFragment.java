package com.e.diteyb.ui.register;

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

public class RegisterFragment extends Fragment {
    EditText edtUsername, edtEmail, edtPassword;
    Button btnRegister;
    NavController navController;
    TextView btnRegisterLogin;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        btnRegister = root.findViewById(R.id.btn_register_frag);
        edtUsername = root.findViewById(R.id.edt_register_name_frag);
        edtEmail = root.findViewById(R.id.edt_register_email_frag);
        edtPassword = root.findViewById(R.id.edt_register_password_frag);
        btnRegisterLogin = root.findViewById(R.id.btn_register_login_frag);
        navController = Navigation.findNavController(MainActivity.MAIN_ACTIVITY_INSTANCE, R.id.nav_host_fragment);
        RegisterViewModel registerViewModel =
                new ViewModelProvider(MainActivity.MAIN_ACTIVITY_INSTANCE).get(RegisterViewModel.class);
        registerViewModel.getRegisterState().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    new AlertDialog.Builder(MainActivity.MAIN_ACTIVITY_INSTANCE)
                            .setTitle("Erro ao criar conta")
                            .setMessage("Por favor verifique se inseriu os dados corretos")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create().show();
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=edtUsername.getText().toString(), email=edtEmail.getText().toString(),
                        password=edtPassword.getText().toString();
                VolleySingleton.getInstance().requestRegister(username,email,password);
                navController.navigate(R.id.action_nav_register_to_nav_login);
            }
        });

        btnRegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_register_to_nav_login);
            }
        });
        return root;
    }
}
