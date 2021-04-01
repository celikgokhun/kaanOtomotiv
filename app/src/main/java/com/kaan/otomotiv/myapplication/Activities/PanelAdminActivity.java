package com.kaan.otomotiv.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kaan.otomotiv.myapplication.R;

public class PanelAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_admin);
    }

    public void jumpToNewsEditActivity(View view)
    {
        startActivity(new Intent(PanelAdminActivity.this, NewsEditActivity.class));
    }

    public void jumpToCampEditActivity(View view)
    {
        startActivity(new Intent(PanelAdminActivity.this, CampEditActivity.class));
    }

    public void jumpToWarningEditActivity(View view)
    {
        startActivity(new Intent(PanelAdminActivity.this, WarningEditActivity.class));
    }
}
