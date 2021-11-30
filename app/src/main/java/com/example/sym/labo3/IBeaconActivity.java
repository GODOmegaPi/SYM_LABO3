package com.example.sym.labo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IBeaconActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 200;

    private RecyclerView ibeaconsList;
    private Collection<String> beaconsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ibeacon);

        // Ask for permission if necessary
        if (!Permissions.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Permissions.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSION_REQUEST_CODE);
        }

        ibeaconsList = findViewById(R.id.ibeacon_beacons_list);
        beaconsList = new ArrayList<>();

        BeaconManager bm = BeaconManager.getInstanceForApplication(this);
         bm.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        Region r = new Region("all-beacons-region", null, null, null);
        bm.getRegionViewModel(r).getRangedBeacons().observe(this, monitoringObserver);
        bm.startRangingBeacons(r);
    }

    Observer monitoringObserver = (Observer<Collection<Beacon>>) beacons -> {
        Log.d("beacon", "Ranged: " + beacons.size() + " beacons");
        for (Beacon beacon : beacons) {
            beaconsList.add(beacon.getBluetoothAddress() + "\n"
                    + beacon.getParserIdentifier() + "\n"
                    + beacon.getRssi());
            Log.d(" ", "$beacon about " + beacon.getDistance() + " meters away");
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}