package com.example.sym.labo3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button btnQRCode;
    private Button btnIbeacon;
    private Button btnNfc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnQRCode = findViewById(R.id.main_btn_qr_code);
        btnIbeacon = findViewById(R.id.main_btn_ibeacon);
        btnNfc = findViewById(R.id.main_btn_nfc);

        btnQRCode.setOnClickListener(view -> {
            Intent intent = new Intent(this, QRCodeActivity.class);
            startActivity(intent);
        });

        btnIbeacon.setOnClickListener(view -> {
            Intent intent = new Intent(this, IBeaconActivity.class);
            startActivity(intent);
        });

        btnNfc.setOnClickListener(view -> {
            Intent intent = new Intent(this, NFCActivity.class);
            startActivity(intent);
        });
    }
}