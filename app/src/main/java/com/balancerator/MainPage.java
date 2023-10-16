package com.balancerator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.balancerator.bluetooth.ConnectionThread;
import com.balancerator.bluetooth.Messages;

import java.util.stream.Collectors;

public class MainPage extends AppCompatActivity {

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        findViewById(R.id.disconnectButton).setOnClickListener(this::disconnect);
        findViewById(R.id.trendsBtn).setOnClickListener(this::insights);
        String name = getIntent().getStringExtra("btDevice");
        if (BluetoothAdapter.getDefaultAdapter().getBondedDevices() == null
                || BluetoothAdapter.getDefaultAdapter().getBondedDevices().size() < 1) {
            Toast.makeText(getApplicationContext(), "No devices found", Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(MainPage.this, MainActivity.class);
            startActivity(intent);
            return;
        }

        BluetoothDevice bd = BluetoothAdapter.getDefaultAdapter().getBondedDevices().stream().filter(i -> i.getName().equals(name)).collect(Collectors.<BluetoothDevice>toList()).get(0);

        Handler h = messageHandler();
        ConnectionThread ct = new ConnectionThread(bd, h);
        h.post(ct);
    }

    private Handler messageHandler() {
        return new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case Messages.CONNECTING_STATUS:
                        switch(msg.arg1){
                            case 1:
                            case -1:
                                break;
                        }
                        break;

                    case Messages.MESSAGE_READ:
                        String arduinoMsg = msg.obj.toString(); // Read message from Arduino
                        switch (arduinoMsg.toLowerCase()){
                            case "led is turned on":
                            case "led is turned off":
                                break;
                        }
                        break;
                }
            }
        };
    }

    private void disconnect(View view) {
        Toast.makeText(getApplicationContext(), "Disconnected from device.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainPage.this, MainActivity.class));
    }

    private void insights(View view) {
        Toast.makeText(getApplicationContext(), "Showing Insights", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainPage.this, Trends.class));
    }
}