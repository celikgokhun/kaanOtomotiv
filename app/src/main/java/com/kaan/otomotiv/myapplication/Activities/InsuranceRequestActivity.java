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

public class InsuranceRequestActivity extends AppCompatActivity {

    EditText insuranceNameText;
    EditText insuranceSurnameText;
    EditText insuranceTcText;
    EditText insurancePhoneText;
    RadioButton deprem;
    RadioButton saglik;
    RadioButton hirsizlik;
    RadioButton kasko;
    RadioButton trafik;

    String insName;
    String insSurname;
    String insTc;
    String phone;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    String insuranceType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_request);

        insuranceNameText = findViewById(R.id.insurance_name_edit_text);
        insuranceSurnameText = findViewById(R.id.insurance_surname_edit_text);
        insuranceTcText = findViewById(R.id.insurance_tc_edit_text);
        insurancePhoneText = findViewById(R.id.insurance_number);

        deprem = findViewById(R.id.depram_radio_btn);
        saglik = findViewById(R.id.saglik_radio_btn);
        hirsizlik = findViewById(R.id.hirsizlik_radio_btn);
        kasko = findViewById(R.id.kasko_radio_btn);
        trafik =findViewById(R.id.trafik_radio_btn);



    }


    public void requestInsuranceFromDealer(View view)
    {
        insName = insuranceNameText.getText().toString();
        insSurname = insuranceSurnameText.getText().toString();
        insTc = insuranceTcText.getText().toString();
        phone = insurancePhoneText.getText().toString();


        if (deprem.isChecked())
        {
            insuranceType = deprem.getText().toString();
            sendMail(insName,insSurname,insTc,insuranceType);
        }
        else if (saglik.isChecked())
        {
            insuranceType = saglik.getText().toString();
            sendMail(insName,insSurname,insTc,insuranceType);
        }
        else if (hirsizlik.isChecked())
        {
            insuranceType = hirsizlik.getText().toString();
            sendMail(insName,insSurname,insTc,insuranceType);
        }
        else if (kasko.isChecked())
        {
            insuranceType = kasko.getText().toString();
            sendMail(insName,insSurname,insTc,insuranceType);
        }
        else if (trafik.isChecked())
        {
            insuranceType = trafik.getText().toString();
            sendMail(insName,insSurname,insTc,insuranceType);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Lütfen bir sigorta tipi seçiniz.",Toast.LENGTH_LONG).show();
        }

    }

    private void sendMail(String name, String surname, String tc, String type)
    {
        if (!name.isEmpty() && !surname.isEmpty() && !type.isEmpty() && !tc.isEmpty() && !phone.isEmpty() )
        if (isOnline())
        {
            HashMap<String, Object> userData = new HashMap<>();

            userData.put("insurance_requester_name",name);
            userData.put("insurance_requester_surname",surname);
            userData.put("insurance_requester_tc",tc);
            userData.put("insurance_topic_address",type);


            db.collection("INSURANCE_REQUEST").add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getApplicationContext(),"Sigorta talebiniz alındı."+ documentReference.getId(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(InsuranceRequestActivity.this, MainActivity.class));


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
        else
        {
            Toast.makeText(getApplicationContext(),"Lütfen internet bağlatınızın olduğundan emin olun.",Toast.LENGTH_LONG).show();
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
