package com.kaan.otomotiv.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaan.otomotiv.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UserSignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    EditText emailText;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // userdata
    EditText nameText;
    EditText surnameText;
    EditText phoneNumberText;
    EditText addressText;
    /// car data
    EditText carPlateText;
    EditText carYearText;

    String model;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        emailText = findViewById(R.id.user_email_edit_text);

        //user date
        nameText = findViewById(R.id.user_name_edit_text);
        surnameText = findViewById(R.id.user_surname_edit_text);
        phoneNumberText = findViewById(R.id.user_phone_edit_text);
        addressText = findViewById(R.id.user_address_edit_text);

        // car data
        carPlateText = findViewById(R.id.car_plate_edit_text);
        carYearText = findViewById(R.id.car_year_edit_text);

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Lütfen Araç Modelini Seçiniz");
        categories.add("Model A");
        categories.add("Model B");
        categories.add("Model C");
        categories.add("Model D");
        categories.add("Model E");
        categories.add("Model F");
        categories.add("Model G");
        categories.add("Model H");
        categories.add("Model I");
        categories.add("Model J");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
    }


    public void saveDatabase(View view)
    {
        // user data
        String userEmail = emailText.getText().toString().toLowerCase();
        String name = nameText.getText().toString().toLowerCase();
        String surname = surnameText.getText().toString().toLowerCase();
        final String phoneNumber = phoneNumberText.getText().toString();
        String address = addressText.getText().toString().toLowerCase();

        //car data
        String carPlate = carPlateText.getText().toString().toLowerCase();
        String carYear = carYearText.getText().toString().toLowerCase();

        if (isOnline() && !userEmail.isEmpty() && !name.isEmpty() && !surname.isEmpty() && !phoneNumber.isEmpty() && !address.isEmpty() && !carPlate.isEmpty() && !model.equals("Lütfen Araç Modelini Seçiniz") && !carYear.isEmpty())
        {
            if(userEmail.contains("@"))
            {
                if (userEmail.contains(".com"))
                {
                    try
                    {
                        HashMap<String, Object> userData = new HashMap<>();

                        //user data
                        userData.put("user_email",userEmail);
                        userData.put("user_name",name);
                        userData.put("user_surname",surname);
                        userData.put("user_phone_number",phoneNumber);
                        userData.put("user_address",address);
                        //car data
                        userData.put("car_plate",carPlate);
                        userData.put("car_brand",model);
                        userData.put("car_year",carYear);


                        db.collection("USERS").add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                        {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(),"Kullanıcı kaydedildi"+ documentReference.getId(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UserSignUpActivity.this, VerificationCodeActivity.class);
                                intent.putExtra("phonenumber",phoneNumber);
                                startActivity(intent);

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
                    Toast.makeText(getApplicationContext(),"E-mail adresiniz '.com' içermiyor. Geçerli bir mail adresi girin.", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"E-mail adresiniz '@' karakteri içermiyor. Geçerli bir mail adresi girin.", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Lütfen internet bağlantınızı kontrol edin ve bilgilerinizi eksiksiz doldurun !", Toast.LENGTH_LONG).show();
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        model = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), "Lütfen bir model seçiniz !", Toast.LENGTH_LONG).show();

    }

    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
