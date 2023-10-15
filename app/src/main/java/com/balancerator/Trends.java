package com.balancerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Trends extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends);

        findViewById(R.id.returnBtn).setOnClickListener(view -> startActivity(new Intent(Trends.this, MainPage.class)));
    }
}