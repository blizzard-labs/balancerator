package com.balancerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothPicker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_picker);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.BLUETOOTH_CONNECT)) {
                Toast.makeText(getApplicationContext(), "Should show rationale", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 10);
            return;
        }
        continueOnCreate();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults )
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean btPermissionGranted = false;
        for (int i = 0; i < permissions.length; i++) {
            if (Manifest.permission.BLUETOOTH_CONNECT.equals(permissions[i]) && (grantResults[i] == PackageManager.PERMISSION_GRANTED)) {
                btPermissionGranted = true;
                break;
            }
        }

        if (!btPermissionGranted) {
            Toast.makeText(getApplicationContext(), "Permission was not granted", Toast.LENGTH_SHORT).show();
        }
        continueOnCreate();
    }

    @SuppressLint("MissingPermission")
    private void continueOnCreate() {
        BluetoothManager bm = (BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bm.getAdapter();

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        final ArrayList<String> list = new ArrayList<>();
        for(BluetoothDevice bt : pairedDevices)
            list.add(bt.getName());

        Toast.makeText(getApplicationContext(),"Showing Paired Devices",
                Toast.LENGTH_SHORT).show();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this,android.R.layout.simple_list_item_1, list);

        ListView picker = findViewById(R.id.btPicker);
        picker.setAdapter(adapter);

        picker.setOnItemClickListener((adapterView, view, i, l) -> {
            Toast.makeText(getApplicationContext(), "Selected : " + list.get(i), Toast.LENGTH_SHORT).show();


        });

        View btn = findViewById(R.id.btButton);
        btn.setOnClickListener((view) -> {
            Toast.makeText(getApplicationContext(), "Selected : " , Toast.LENGTH_SHORT).show();
        });

    }
}