package com.balancerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btPickerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btPickerBtn = findViewById(R.id.connectBtn);
        this.btPickerBtn.setOnClickListener(this::showBTPicker);
    }

    public void showBTPicker(View view) {
        Toast.makeText(getApplicationContext(), "Clicked the button", Toast.LENGTH_SHORT).show();
        Intent intent =new Intent(MainActivity.this, BluetoothPicker.class);
        startActivity(intent);
    }
}