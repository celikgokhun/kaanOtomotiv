package com.kaan.otomotiv.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaan.otomotiv.myapplication.R;

import java.util.HashMap;

public class LinkWithDealerActivity extends AppCompatActivity
{

    RadioButton finance;
    RadioButton rentCar;
    RadioButton customerRelation;
    RadioButton filo;
    RadioButton insurance;

    EditText phone;
    EditText nameEditText;
    EditText surnameEditText;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String contactTopic;
    @Override
    protected void onCreate(Bundle savedInstanceState)    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_with_dealer);

        finance = findViewById(R.id.radioFinance);
        rentCar = findViewById(R.id.radioRent);
        customerRelation = findViewById(R.id.radioCustomerRelations);
        filo = findViewById(R.id.radioFilo);
        insurance = findViewById(R.id.radioInsurance);


        nameEditText = findViewById(R.id.contact_name_edit_text);
        surnameEditText = findViewById(R.id.contact_surname_edit_text);
        phone = findViewById(R.id.contact_phone_edit_text);

    }

    public void requestContactFromDealer(View view)    {
        String contactNumber = phone.getText().toString();
        String contactName = nameEditText.getText().toString();
        String contactSurname = surnameEditText.getText().toString();

        if (finance.isChecked())
        {
            contactTopic = finance.getText().toString();
            sendMail(contactName,contactSurname,contactNumber,contactTopic);
        }
        else if (rentCar.isChecked())
        {
            contactTopic = rentCar.getText().toString();
            sendMail(contactName,contactSurname,contactNumber,contactTopic);
        }
        else if (customerRelation.isChecked())
        {
            contactTopic = customerRelation.getText().toString();
            sendMail(contactName,contactSurname,contactNumber,contactTopic);
        }
        else if (filo.isChecked())
        {
            contactTopic = filo.getText().toString();
            sendMail(contactName,contactSurname,contactNumber,contactTopic);
        }
        else if (insurance.isChecked())
        {
            contactTopic = insurance.getText().toString();
            sendMail(contactName,contactSurname,contactNumber,contactTopic);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Lütfen bir konu seçiniz.",Toast.LENGTH_LONG).show();
        }
    }

    private void sendMail(String ad, String soyad,String telNo, String type)
    {
        if (!ad.isEmpty() && !soyad.isEmpty() && !telNo.isEmpty() && !type.isEmpty())
        {
            if (isOnline())
            {
                try
                {
                    HashMap<String, Object> userData = new HashMap<>();

                    userData.put("link_requester_name",ad);
                    userData.put("link_requester_surname",soyad);
                    userData.put("link_requester_number",telNo);
                    userData.put("link_topic",type);


                    db.collection("LINK_REQUEST").add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(),"İletişim talebiniz alındı."+ documentReference.getId(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LinkWithDealerActivity.this, MainActivity.class));


                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {

                                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                }
                            });
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Lütfen internet bağlatınızın olduğundan emin olun.",Toast.LENGTH_LONG).show();
            }

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Lütfen tüm alanları doldurun !", Toast.LENGTH_LONG).show();
        }


    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

}
