package com.balancerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        findViewById(R.id.disconnectButton).setOnClickListener(this::disconnect);
        findViewById(R.id.trendsBtn).setOnClickListener(this::insights);
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