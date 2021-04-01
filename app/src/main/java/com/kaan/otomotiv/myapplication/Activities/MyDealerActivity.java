package com.kaan.otomotiv.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kaan.otomotiv.myapplication.R;

public class MyDealerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dealer);
    }

    public void applyTestDriving(View view)
    {
        Intent intent = new Intent(MyDealerActivity.this, RequestTestDriveActivity.class);
        startActivity(intent);
    }

    public void contactRequest(View view)
    {
        Intent intent = new Intent(MyDealerActivity.this, LinkWithDealerActivity.class);
        startActivity(intent);
    }

    public void applyInsurance(View view)
    {
        Intent intent = new Intent(MyDealerActivity.this, InsuranceRequestActivity.class);
        startActivity(intent);
    }


}
