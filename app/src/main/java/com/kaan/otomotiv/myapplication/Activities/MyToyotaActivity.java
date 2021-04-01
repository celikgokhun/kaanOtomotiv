package com.kaan.otomotiv.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kaan.otomotiv.myapplication.R;

public class MyToyotaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_toyota);
    }

    public void showWarnings (View view)
    {
        Intent intent = new Intent(MyToyotaActivity.this, WarningsActivity.class);
        startActivity(intent);
    }

    public void showAccessories (View view)
    {
        Intent intent = new Intent(MyToyotaActivity.this, AccessoriesActivity.class);
        startActivity(intent);
    }
}
