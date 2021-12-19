package com.example.sym.labo3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.List;


/**
 * @author  Eric Bousbaa & Ilias Goujgali & Guillaume Laubscher
 * Adaptater permettant d'afficher les items de types beacons dans une viewlist
 */
public class BeaconAdapter extends BaseAdapter {
    Context context;
    List<Beacon> beacons;
    private static LayoutInflater inflater = null;

    public BeaconAdapter(Context context, List<Beacon> beacons) {
        this.context = context;
        this.beacons = beacons;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return beacons.size();
    }

    @Override
    public Beacon getItem(int i) {
        return beacons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if(vi == null) {
            vi = inflater.inflate(R.layout.beacon, null);
        }

        ImageView imageView = vi.findViewById(R.id.beacon_image);
        imageView.setImageDrawable(vi.getResources().getDrawable(R.drawable.beacon));

        TextView bluetoothAddress = vi.findViewById(R.id.beacon_bluetooth_address);
        bluetoothAddress.setText(beacons.get(i).getBluetoothAddress());

        TextView uuid = vi.findViewById(R.id.beacon_uuid);
        uuid.setText(String.format("UUID: %s", beacons.get(i).getId1().toString()));

        TextView majorMinor = vi.findViewById(R.id.beacon_major_minor);
        majorMinor.setText(String.format("Majeur: %d Mineur: %d", beacons.get(i).getId2().toInt(), beacons.get(i).getId3().toInt()));

        TextView rssi = vi.findViewById(R.id.beacon_rssi);
        rssi.setText(String.format("RSSI\n%d", beacons.get(i).getRssi()));

        return vi;
    }
}
