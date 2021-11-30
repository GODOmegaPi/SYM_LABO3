package com.example.sym.labo3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NFCAuthActivity extends AppCompatActivity {

    private final String SECRET_USERNAME = "root";
    private final String SECRET_PASSWORD = "toor";

    private EditText username;
    private EditText password;
    private Button connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcauth);

        username = findViewById(R.id.nfcauth_username_text);
        password = findViewById(R.id.nfcauth_password_text);
        connect = findViewById(R.id.nfcauth_connect_btn);
        connect.setOnClickListener(view -> {
            if (checkLogin()) {
                Intent intent = new Intent(this, NFCActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Wrong username/password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkLogin() {
        return username.getText().toString().equals(SECRET_USERNAME)
                && password.getText().toString().equals(SECRET_PASSWORD);
    }
}