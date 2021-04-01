package com.kaan.otomotiv.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.kaan.otomotiv.myapplication.Classes.BuyFeedRecyclerAdapter;
import com.kaan.otomotiv.myapplication.R;

public class LegalActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal);
        mAuth = FirebaseAuth.getInstance();

        //// bunu silmeyi unutma

        //startActivity(new Intent(LegalActivity.this, AccessoriesActivity.class));

    }

    public void flyToMainActivity(View view)
    {
        try
        {
            if (mAuth.getCurrentUser().getPhoneNumber() != null)
            {
                System.out.println(mAuth.getCurrentUser().getPhoneNumber());
                startActivity(new Intent(LegalActivity.this, MainActivity.class));
            }
            else
            {
                startActivity(new Intent(LegalActivity.this, UserSignUpActivity.class));
            }
        }
        catch (NullPointerException npe )
        {
            System.out.println(npe.getLocalizedMessage());
        }


    }

    public void jumpToAdminLogin(View viev)
    {
        startActivity(new Intent(LegalActivity.this, AdminLoginActivity.class));
    }

    public void flyToUserRegister(View view)
    {
        startActivity(new Intent(LegalActivity.this, UserSignUpActivity.class));
    }

    public void closeApp(View view)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.finishAffinity();
        } else{
            this.finish();
            System.exit(0);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setContentView(R.layout.activity_legal);
    }


}
