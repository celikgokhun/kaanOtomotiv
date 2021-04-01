package com.kaan.otomotiv.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaan.otomotiv.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class RequestTestDriveActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText requesterMailText;
    EditText requesterNameText;
    EditText requesterSurnameText;
    EditText requesterPhoneText;

    DatePicker datePicker;

    RadioButton nineRad;
    RadioButton tenRad;
    RadioButton elevenRad;
    RadioButton thirteenRad;
    RadioButton fourteenRad;
    RadioButton fifteenRad;
    RadioButton sixteenRad;

    RadioGroup radioGroup;

    Button appbtn;



    FirebaseFirestore db = FirebaseFirestore.getInstance();


    String testDriveModel;

    String timeFromDB;
    String time;
    String systemTime;
    String systemTimeAndDateTotal;


    int timer = 0;
    int timer2 = 0;
    boolean thereIsAconflict = false;


    final Calendar[] user = {new GregorianCalendar()};

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_test_drive);

        requesterMailText = findViewById(R.id.requester_email_edit_text);
        requesterNameText = findViewById(R.id.requester_name_edit_text);
        requesterSurnameText = findViewById(R.id.requester_surname_edit_text);
        requesterPhoneText = findViewById(R.id.requester_phone_edit_text);

        datePicker =  findViewById(R.id.date_picker1);

        nineRad = findViewById(R.id.nine_ten_btn1);
        tenRad = findViewById(R.id.ten_eleven_btn1);
        elevenRad = findViewById(R.id.eleven_twelve_btn1);
        thirteenRad = findViewById(R.id.thirteen_fourteen_btn1);
        fourteenRad = findViewById(R.id.fourteen_fifteen_btn1);
        fifteenRad = findViewById(R.id.fifteen_sixteen_btn1);
        sixteenRad = findViewById(R.id.sixteen_seventeen_btn1);
        appbtn = findViewById(R.id.request_btn);


        radioGroup = findViewById(R.id.rdforapp1);

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

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


        getSystemTime();
    }

    public void updatePicker(View view)    {
        Calendar calendar = new GregorianCalendar(
                datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMDDYYYY");

        String userSelectedDate = simpleDateFormat.format(calendar.getTime());
        //System.out.println("güncelleme tarihi : " +userSelectedDate);

        setLayoutAccTime(userSelectedDate);
    }

    public void setLayoutAccTime(String fromUserDate)    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HHMM");
        systemTime = format.format(calendar.getTime());



        SimpleDateFormat realformat = new SimpleDateFormat("HHMM,MMDDYYYY");

        systemTimeAndDateTotal = realformat.format(calendar.getTime());
        systemTimeAndDateTotal = systemTimeAndDateTotal.replace(",","");

        System.out.println(systemTime);
        String composedTime = systemTime+fromUserDate;


        System.out.println("kullanıcıdan gelen zaman:    "+composedTime);
        System.out.println("sistam saati:     "+ systemTimeAndDateTotal);

        /*
        db = FirebaseFirestore.getInstance();
        db.collection("APPOINTMENT").addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) { }
                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                    try
                    {
                        timeFromDB = documentChange.getDocument().getData().get("date+hour").toString();
                        System.out.println("databaseden "+timeFromDB);
                    }
                    catch (NullPointerException e1) {
                        System.out.println(e1.getLocalizedMessage());
                    }
                }
            }
        });

         */

        if (!composedTime.equals(systemTimeAndDateTotal))
        {
            System.out.println("gün farklı");


            nineRad.setVisibility(View.VISIBLE);
            nineRad.setBackgroundColor(Color.GREEN);
            nineRad.setClickable(true);

            tenRad.setVisibility(View.VISIBLE);
            tenRad.setBackgroundColor(Color.GREEN);
            tenRad.setClickable(true);

            elevenRad.setVisibility(View.VISIBLE);
            elevenRad.setBackgroundColor(Color.GREEN);
            elevenRad.setClickable(true);

            thirteenRad.setVisibility(View.VISIBLE);
            thirteenRad.setBackgroundColor(Color.GREEN);
            thirteenRad.setClickable(true);

            fourteenRad.setVisibility(View.VISIBLE);
            fourteenRad.setBackgroundColor(Color.GREEN);
            fourteenRad.setClickable(true);

            fifteenRad.setVisibility(View.VISIBLE);
            fifteenRad.setBackgroundColor(Color.GREEN);
            fifteenRad.setClickable(true);

            sixteenRad.setVisibility(View.VISIBLE);
            sixteenRad.setBackgroundColor(Color.GREEN);
            sixteenRad.setClickable(true);

            appbtn.setVisibility(View.VISIBLE);
        }

        else {
            long sysTime = Long.parseLong(systemTime);
            System.out.println("gün aynı");

            if (sysTime >= 0900f)            {
                nineRad.setVisibility(View.VISIBLE);
                nineRad.setBackgroundColor(Color.GRAY);
                nineRad.setClickable(false);

                tenRad.setVisibility(View.VISIBLE);
                elevenRad.setVisibility(View.VISIBLE);
                thirteenRad.setVisibility(View.VISIBLE);
                fourteenRad.setVisibility(View.VISIBLE);
                fifteenRad.setVisibility(View.VISIBLE);
                sixteenRad.setVisibility(View.VISIBLE);

                appbtn.setVisibility(View.VISIBLE);
            }

            if (sysTime >= 1000f)            {
                nineRad.setVisibility(View.VISIBLE);
                nineRad.setBackgroundColor(Color.GRAY);
                nineRad.setClickable(false);

                tenRad.setVisibility(View.VISIBLE);
                tenRad.setBackgroundColor(Color.GRAY);
                tenRad.setClickable(false);

                elevenRad.setVisibility(View.VISIBLE);
                thirteenRad.setVisibility(View.VISIBLE);
                fourteenRad.setVisibility(View.VISIBLE);
                fifteenRad.setVisibility(View.VISIBLE);
                sixteenRad.setVisibility(View.VISIBLE);

                appbtn.setVisibility(View.VISIBLE);
            }

            if (sysTime >= 1100f)            {
                nineRad.setVisibility(View.VISIBLE);
                nineRad.setBackgroundColor(Color.GRAY);
                nineRad.setClickable(false);

                tenRad.setVisibility(View.VISIBLE);
                tenRad.setBackgroundColor(Color.GRAY);
                tenRad.setClickable(false);

                elevenRad.setVisibility(View.VISIBLE);
                elevenRad.setBackgroundColor(Color.GRAY);
                elevenRad.setClickable(false);

                thirteenRad.setVisibility(View.VISIBLE);
                fourteenRad.setVisibility(View.VISIBLE);
                fifteenRad.setVisibility(View.VISIBLE);
                sixteenRad.setVisibility(View.VISIBLE);

                appbtn.setVisibility(View.VISIBLE);
            }

            if (sysTime >= 1300f)            {
                nineRad.setVisibility(View.VISIBLE);
                nineRad.setBackgroundColor(Color.GRAY);
                nineRad.setClickable(false);

                tenRad.setVisibility(View.VISIBLE);
                tenRad.setBackgroundColor(Color.GRAY);
                tenRad.setClickable(false);

                elevenRad.setVisibility(View.VISIBLE);
                elevenRad.setBackgroundColor(Color.GRAY);
                elevenRad.setClickable(false);

                thirteenRad.setVisibility(View.VISIBLE);
                thirteenRad.setBackgroundColor(Color.GRAY);
                thirteenRad.setClickable(false);

                fourteenRad.setVisibility(View.VISIBLE);
                fifteenRad.setVisibility(View.VISIBLE);
                sixteenRad.setVisibility(View.VISIBLE);

                appbtn.setVisibility(View.VISIBLE);
            }

            if (sysTime >= 1400f)            {
                nineRad.setVisibility(View.VISIBLE);
                nineRad.setBackgroundColor(Color.GRAY);
                nineRad.setClickable(false);

                tenRad.setVisibility(View.VISIBLE);
                tenRad.setBackgroundColor(Color.GRAY);
                tenRad.setClickable(false);

                elevenRad.setVisibility(View.VISIBLE);
                elevenRad.setBackgroundColor(Color.GRAY);
                elevenRad.setClickable(false);

                thirteenRad.setVisibility(View.VISIBLE);
                thirteenRad.setBackgroundColor(Color.GRAY);
                thirteenRad.setClickable(false);

                fourteenRad.setVisibility(View.VISIBLE);
                fourteenRad.setBackgroundColor(Color.GRAY);
                fourteenRad.setClickable(false);

                fifteenRad.setVisibility(View.VISIBLE);
                sixteenRad.setVisibility(View.VISIBLE);

                appbtn.setVisibility(View.VISIBLE);
            }

            if (sysTime >= 1500f)            {
                nineRad.setVisibility(View.VISIBLE);
                nineRad.setBackgroundColor(Color.GRAY);
                nineRad.setClickable(false);

                tenRad.setVisibility(View.VISIBLE);
                tenRad.setBackgroundColor(Color.GRAY);
                tenRad.setClickable(false);

                elevenRad.setVisibility(View.VISIBLE);
                elevenRad.setBackgroundColor(Color.GRAY);
                elevenRad.setClickable(false);

                thirteenRad.setVisibility(View.VISIBLE);
                thirteenRad.setBackgroundColor(Color.GRAY);
                thirteenRad.setClickable(false);

                fourteenRad.setVisibility(View.VISIBLE);
                fourteenRad.setBackgroundColor(Color.GRAY);
                fourteenRad.setClickable(false);

                fifteenRad.setVisibility(View.VISIBLE);
                fifteenRad.setBackgroundColor(Color.GRAY);
                fifteenRad.setClickable(false);

                sixteenRad.setVisibility(View.VISIBLE);

                appbtn.setVisibility(View.VISIBLE);
            }

            if (sysTime >= 1600f)            {
                nineRad.setVisibility(View.VISIBLE);
                nineRad.setBackgroundColor(Color.GRAY);
                nineRad.setClickable(false);

                tenRad.setVisibility(View.VISIBLE);
                tenRad.setBackgroundColor(Color.GRAY);
                tenRad.setClickable(false);

                elevenRad.setVisibility(View.VISIBLE);
                elevenRad.setBackgroundColor(Color.GRAY);
                elevenRad.setClickable(false);

                thirteenRad.setVisibility(View.VISIBLE);
                thirteenRad.setBackgroundColor(Color.GRAY);
                thirteenRad.setClickable(false);

                fourteenRad.setVisibility(View.VISIBLE);
                fourteenRad.setBackgroundColor(Color.GRAY);
                fourteenRad.setClickable(false);

                fifteenRad.setVisibility(View.VISIBLE);
                fifteenRad.setBackgroundColor(Color.GRAY);
                fifteenRad.setClickable(false);

                sixteenRad.setVisibility(View.VISIBLE);
                sixteenRad.setBackgroundColor(Color.GRAY);
                sixteenRad.setClickable(false);

            }
        }
    }

    public void requestTestDriveFromDealer(View view)    {
        getTimeDate();
        final String email = requesterMailText.getText().toString().toLowerCase();
        final String name = requesterNameText.getText().toString().toLowerCase();
        final String surname = requesterSurnameText.getText().toString().toLowerCase();
        final String phone = requesterPhoneText.getText().toString().toLowerCase();

        Calendar now = new GregorianCalendar();
        //System.out.println("Karşılaştırma"+ user[0].compareTo(now));
        final int comparison = user[0].compareTo(now);

        db = FirebaseFirestore.getInstance();
        db.collection("TEST_DRIVE_REQUEST").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e !=null)
                {

                }

                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges())
                {
                    try
                    {
                        timeFromDB = documentChange.getDocument().getData().get("test_drive_time").toString();
                        if (time.equals(timeFromDB) && timer==0)
                        {
                            thereIsAconflict = true;
                            timer = timer+1;
                            timer2 = timer2 +1;
                        }
                    }
                    catch (NullPointerException e1)
                    {
                        System.out.println("Sorun Burda---------------------------------------------------------------------------------------------------------------------------------");
                        System.out.println(e1.getLocalizedMessage());
                    }
                }

                if (!email.isEmpty() && !name.isEmpty() && !surname.isEmpty() && !phone.isEmpty() && !testDriveModel.isEmpty() && !testDriveModel.equals("Lütfen Araç Modelini Seçiniz"))
                {
                    if (email.contains("@"))
                    {
                        if (email.contains(".com"))
                        {
                            if (isOnline())
                            {
                                if (!thereIsAconflict && timer<=1)
                                {

                                    System.out.println(" yazmaya geldik bro");

                                    writeDatabase(email, name,surname,phone,testDriveModel,time);
                                    //System.out.println(timer);
                                }
                                else
                                {
                                    if (thereIsAconflict && timer2 <2)
                                    {

                                        Toast.makeText(getApplicationContext(),"Seçtiğiniz zaman veya gün doludur, farklı tarih ve saat seçerek tekrar deneyiniz. ",Toast.LENGTH_SHORT).show();
                                        System.out.println("bura geldim");
                                        recreate();
                                        //startActivity(new Intent(SetAppointmentActivity.this, MainActivity.class));
                                    }
                                    else
                                    {
                                        System.out.println("zıpladım");
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"İnternet bağlantınız olduğuna emin olun.",Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Geçerli bir email adresi giriniz. '.com' içermiyor",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Geçerli bir mail adresi giriniz. '@' içermiyor",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Tüm alanları doldurmanınız gerekir.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void writeDatabase (String email, String name, String surname, String phone, String model, String datehour)       {
        System.out.println("yazdık");
        timer2++;
        try
        {
            HashMap<String, Object> appointmentData = new HashMap<>();

            appointmentData.put("test_drive_car_model",model);
            appointmentData.put("test_drive_email",email);
            appointmentData.put("test_drive_requester_name",name);
            appointmentData.put("test_drive_phone",phone);
            appointmentData.put("test_drive_requester_surname",surname);
            appointmentData.put("test_drive_time",datehour);


            db.collection("TEST_DRIVE_REQUEST").add(appointmentData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getApplicationContext(),"Test Sürüşü Talebiniz Tarafımıza ulaştırıldı !"+ documentReference.getId(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RequestTestDriveActivity.this, MyDealerActivity.class);
                    startActivity(intent);
                    timer2++;
                    //startActivity(new Intent(RequestTestDriveActivity.this, MainActivity.class));
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        testDriveModel = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + testDriveModel, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), "Lütfen bir model seçiniz !", Toast.LENGTH_LONG).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Calendar getSystemTime()
    {
        DatePicker datePicker =  findViewById(R.id.date_picker1);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        Calendar systemCalender = new GregorianCalendar(
                datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth());

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:MM");

        systemTime = simpleDateFormat1.format(Calendar.getInstance().getTime());

        System.out.println("sistem saati : " +systemTime);
        return systemCalender;
    }

    public Calendar getTimeDate()
    {
        Calendar calendar = new GregorianCalendar(
                datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD:MM:YYYY:ZZZZ");

        if (nineRad.isChecked())
        {
            time = "Saat: "+nineRad.getText()+"  Tarih:  "+ simpleDateFormat.format(calendar.getTime());
        }
        if (tenRad.isChecked())
        {
            time = "Saat: "+tenRad.getText()+"  Tarih:  "+ simpleDateFormat.format(calendar.getTime());
        }
        if (elevenRad.isChecked())
        {
            time = "Saat: "+elevenRad.getText()+"  Tarih:  "+ simpleDateFormat.format(calendar.getTime());
        }
        if (thirteenRad.isChecked())
        {
            time = "Saat: "+thirteenRad.getText()+"  Tarih:  "+ simpleDateFormat.format(calendar.getTime());
        }
        if (fourteenRad.isChecked())
        {
            time = "Saat: "+fourteenRad.getText()+"  Tarih:  "+ simpleDateFormat.format(calendar.getTime());
        }
        if (fifteenRad.isChecked())
        {
            time = "Saat: "+fifteenRad.getText()+"  Tarih:  "+ simpleDateFormat.format(calendar.getTime());
        }
        if (sixteenRad.isChecked())
        {
            time = "Saat: "+sixteenRad.getText()+"  Tarih:  "+ simpleDateFormat.format(calendar.getTime());
        }

        System.out.println("kullanıcının girdiği zaman :----------------------------------------------"+time);

        user[0] = calendar;
        return user[0];

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
