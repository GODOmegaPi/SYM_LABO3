package com.example.sym.labo3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NFCActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private static final String MIME_TEXT_PLAIN = "text/plain";

    private NfcAdapter mNfcAdapter;
    private AuthLevel authLevel;

    private Button maxSecurity;
    private Button medSecurity;
    private Button minSecurity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        authLevel = new AuthLevel();

        maxSecurity = findViewById(R.id.nfc_maximum_security_btn);
        maxSecurity.setOnClickListener(view -> {
            checkAuth(AuthLevel.Level.MAXIMUM);
        });

        medSecurity = findViewById(R.id.nfc_medium_security_btn);
        medSecurity.setOnClickListener(view -> {
            checkAuth(AuthLevel.Level.MEDIUM);
        });

        minSecurity = findViewById(R.id.nfc_minimum_security_btn);
        minSecurity.setOnClickListener(view -> {
            checkAuth(AuthLevel.Level.MINIMUM);
        });

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        handleIntent(getIntent());
    }

    private void checkAuth(AuthLevel.Level level) {
        Toast.makeText(this,
                authLevel.isAuthLevelHighEnough(level) ? "Access permitted" : "Access denied",
                Toast.LENGTH_SHORT
        ).show();
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

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);
                authLevel.resetAuthLevelValue();

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    authLevel.resetAuthLevelValue();
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
        //filters[0].addCategory(Intent.CATEGORY_DEFAULT);
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
}