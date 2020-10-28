package com.e.diteyb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
EditText edtUsername, edtEmail, edtPassword;
Button btnRegister;
TextView btnRegisterLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnRegister = findViewById(R.id.btn_register);
        edtUsername = findViewById(R.id.edt_register_nome);
        edtEmail = findViewById(R.id.edt_register_email);
        edtPassword = findViewById(R.id.edt_register_password);
        btnRegisterLogin = findViewById(R.id.btn_register_login);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=edtUsername.getText().toString(), email=edtEmail.getText().toString(),
                        password=edtPassword.getText().toString();
                VolleySingleton.getInstance().requestRegister(username,email,password);
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnRegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(registerLogin);
            }
        });
    }

}