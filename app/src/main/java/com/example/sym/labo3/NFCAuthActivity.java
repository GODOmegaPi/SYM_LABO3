package com.example.sym.labo3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class NFCAuthActivity extends AppCompatActivity {

    private final String SECRET_USERNAME = "root";
    private final String SECRET_PASSWORD = "toor";
    private boolean goodNFCTag = false;

    private final String TAG = this.getClass().getSimpleName();
    private static final String MIME_TEXT_PLAIN = "text/plain";

    private EditText username;
    private EditText password;
    private Button connect;

    private NfcAdapter mNfcAdapter;

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
                Toast.makeText(this, "Wrong username/password/NFCTag", Toast.LENGTH_SHORT).show();
            }
        });

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void setupForegroundDispatch() {
        if(mNfcAdapter == null)
            return;

        final Intent intent = new Intent(this.getApplicationContext(),this.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent =
                PendingIntent.getActivity(this.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // On souhaite être notifié uniquement pour les TAG au format NDEF
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);

        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            Log.e(TAG, "MalformedMimeTypeException", e);
        }

        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techList);
    }

    private void stopForegroundDispatch() {
        if(mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask(results -> {
                    if(results.equals(AuthLevel.NFC_MESSAGES)) {
                        goodNFCTag = true;
                    }
                }).execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask(results -> {
                        if(results.equals(AuthLevel.NFC_MESSAGES)) {
                            goodNFCTag = true;
                        }
                    }).execute(tag);
                    break;
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopForegroundDispatch();
    }

    private boolean checkLogin() {
        return username.getText().toString().equals(SECRET_USERNAME)
                && password.getText().toString().equals(SECRET_PASSWORD)
                && goodNFCTag;
    }
}