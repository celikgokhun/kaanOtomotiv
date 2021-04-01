package com.kaan.otomotiv.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kaan.otomotiv.myapplication.R;

public class AdminLoginActivity extends AppCompatActivity {

    EditText adminMailEditText;
    EditText adminPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminMailEditText = findViewById(R.id.admin_email_edit_text);
        adminPasswordEditText = findViewById(R.id.admin_pass_edit_text);

    }

    public void signInAdmin(View view)
    {
        /// bunu da silmemiz lazım
        //startActivity(new Intent(AdminLoginActivity.this, PanelAdminActivity.class));


        String adminMail = adminMailEditText.getText().toString();
        String adminPass = adminPasswordEditText.getText().toString();

        if (isOnline())
        {
            if (!adminMail.isEmpty() && !adminPass.isEmpty())
            {
                if (adminMail.contains("@"))
                {
                    if (adminMail.contains(".com"))
                    {
                        if (adminMail.equals("admin@admin.com") && adminPass.equals("admin"))
                        {
                            startActivity(new Intent(AdminLoginActivity.this, PanelAdminActivity.class));
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Hatalı e-posta adresi veya hatalı şifre.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Mail adresiniz  hatalı [.com] yok", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Mail adresiniz  hatalı [@] yok", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Lütfen tüm alanları doldurun !", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Lütfen internet bağlantınız olduğuna emin olun !", Toast.LENGTH_SHORT).show();
        }



    }

    public boolean isOnline()    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

}
