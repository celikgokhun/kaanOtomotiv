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
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.ToIntBiFunction;

public class SetAppointmentActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText carPlateText;
    EditText carKilometerText;
    EditText maintenanceKilometerText;


    Button appbtn;
    DatePicker datePicker;

    RadioButton nineRad;
    RadioButton tenRad;
    RadioButton elevenRad;
    RadioButton thirteenRad;
    RadioButton fourteenRad;
    RadioButton fifteenRad;
    RadioButton sixteenRad;

    RadioGroup radioGroup;

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
        setContentView(R.layout.activity_set_appointment);

        carPlateText = findViewById(R.id.car_plate_edit_text_appointment);
        carKilometerText = findViewById(R.id.car_kilometer_edit_text);
        maintenanceKilometerText = findViewById(R.id.maintenance_type_edit_text);

        appbtn = findViewById(R.id.app_btn);

        datePicker =  findViewById(R.id.date_picker);

        nineRad = findViewById(R.id.nine_ten_btn);
        tenRad = findViewById(R.id.ten_eleven_btn);
        elevenRad = findViewById(R.id.eleven_twelve_btn);
        thirteenRad = findViewById(R.id.thirteen_fourteen_btn);
        fourteenRad = findViewById(R.id.fourteen_fifteen_btn);
        fifteenRad = findViewById(R.id.fifteen_sixteen_btn);
        sixteenRad = findViewById(R.id.sixteen_seventeen_btn);

        radioGroup = findViewById(R.id.rdforapp);

        getSystemTime();
    }

    /*
    public void updatePicker (View view)
    {
        Calendar calendar2 = new GregorianCalendar(
                datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth());

        SimpleDateFormat formatUser = new SimpleDateFormat("MMDDYYYY");
        updatedPicker = formatUser.format(calendar2.getTime());
        System.out.println("pickerdan gelen zaman" + updatedPicker);
    }

     */

    public void updatePicker(View view)    {
        Calendar calendar = new GregorianCalendar(
                datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMDDYYYY");

        String userSelectedDate = simpleDateFormat.format(calendar.getTime());
        //System.out.println("g??ncelleme tarihi : " +userSelectedDate);

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


        System.out.println("kullan??c??dan gelen zaman:    "+composedTime);
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
            System.out.println("g??n farkl??");


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
            System.out.println("g??n ayn??");

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

    public void sendMaintenanceMail(View view)
    {
        getTimeDate();
        final String carPlate = carPlateText.getText().toString().toLowerCase();
        final String carKilometer = carKilometerText.getText().toString().toLowerCase();
        final String type = maintenanceKilometerText.getText().toString().toLowerCase();

        Calendar now = new GregorianCalendar();
        //System.out.println("Kar????la??t??rma"+ user[0].compareTo(now));
        final int comparison = user[0].compareTo(now);

        db = FirebaseFirestore.getInstance();
        db.collection("APPOINTMENT").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e !=null)
                {

                }

                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges())
                {
                    try
                    {
                        timeFromDB = documentChange.getDocument().getData().get("date+hour").toString();
                        if (time.equals(timeFromDB) && timer==0)
                        {
                            thereIsAconflict = true;
                            timer = timer+1;
                            timer2 = timer2 +1;

                        }
                    }
                    catch (NullPointerException e1)
                    {
                        System.out.println(e1.getLocalizedMessage());
                    }
                }

                if (!carPlate.isEmpty() && !carKilometer.isEmpty() && !type.isEmpty())
                {
                    if (isOnline())
                    {
                        if (comparison<8)
                        {
                            if (!thereIsAconflict && timer<=1)
                            {
                                writeDatabase(carPlate, carKilometer,type,time);
                            }
                            else
                             {
                                 if (thereIsAconflict && timer2 <2)
                                 {

                                     Toast.makeText(getApplicationContext(),"Se??ti??iniz zaman veya g??n doludur, farkl?? tarih ve saat se??erek tekrar deneyiniz. ",Toast.LENGTH_SHORT).show();
                                     System.out.println("bura geldim");
                                     //startActivity(new Intent(SetAppointmentActivity.this, MainActivity.class));

                                 }
                                 else
                                 {
                                     //timer2 = timer2+1;
                                     System.out.println("z??plad??m");
                                     //recreate();
                                 }


                                 //
                                 //startActivity(new Intent(SetAppointmentActivity.this, MainActivity.class));

                             }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"L??tfen ge??erli randevu zaman?? se??iniz !", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                        {
                        Toast.makeText(getApplicationContext(),"??nternet ba??lant??n??z oldu??una emin olun.",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"T??m alanlar?? doldurman??n??z gerekir.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public Calendar getTimeDate()    {
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

        System.out.println("kullan??c??n??n girdi??i zaman :----------------------------------------------"+time);

        user[0] = calendar;
        return user[0];
    }

    public void writeDatabase (String carPlate, String carKilometer, String type, String datehour)           {
        System.out.println("yazd??k");
        timer2++;
        try
        {
            HashMap<String, Object> appointmentData = new HashMap<>();

            appointmentData.put("carPlate",carPlate);
            appointmentData.put("carKilometer",carKilometer);
            appointmentData.put("bakimTipi",type);
            appointmentData.put("date+hour",datehour);


            db.collection("APPOINTMENT").add(appointmentData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getApplicationContext(),"Randevu Olu??turuldu!"+ documentReference.getId(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SetAppointmentActivity.this, MainActivity.class);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Calendar getSystemTime()    {

        DatePicker datePicker =  findViewById(R.id.date_picker);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        Calendar systemCalender = new GregorianCalendar(
                datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth());


        return systemCalender;
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
