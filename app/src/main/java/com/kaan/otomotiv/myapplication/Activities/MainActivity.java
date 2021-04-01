package com.kaan.otomotiv.myapplication.Activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaan.otomotiv.myapplication.Classes.NewsFeedRecyclerAdapter;
import com.kaan.otomotiv.myapplication.R;
import com.kaan.otomotiv.myapplication.Services.LocationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    private BroadcastReceiver broadcastReceiver;
    public final static int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_PHONE_CALL = 1;
    public String longitude;
    public String latitude;

    ImageButton callTowTruck;

    View dialogView;
    AlertDialog alertDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    ArrayList<String> newsTitleFromFB;
    ArrayList<String> newsBodyFromFB;
    ArrayList<String> newsPhotoFromFB;

    NewsFeedRecyclerAdapter newsFeedRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callTowTruck = findViewById(R.id.request_tow_truck_btn);
        requestLocationPermissions();
        startLocation();

        dialogView = View.inflate(MainActivity.this, R.layout.road_assistant, null);
        alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        newsTitleFromFB = new ArrayList<>();
        newsBodyFromFB= new ArrayList<>();
        newsPhotoFromFB = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getDataFromFirestore();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewForNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsFeedRecyclerAdapter = new NewsFeedRecyclerAdapter(newsTitleFromFB, newsBodyFromFB, newsPhotoFromFB);
        recyclerView.setAdapter(newsFeedRecyclerAdapter);

    }

    public void getDataFromFirestore()
    {
        CollectionReference collectionReference = firebaseFirestore.collection("NEWS");

        collectionReference.orderBy("news_date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null) {

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {

                        Map<String,Object> data = snapshot.getData();

                        //Casting
                        String title = (String) data.get("news_title");
                        String body = (String) data.get("news_body");
                        String downloadUrl = (String) data.get("news_downloadUrl");

                        newsTitleFromFB.add(title);
                        newsBodyFromFB.add(body);
                        newsPhotoFromFB.add(downloadUrl);

                        newsFeedRecyclerAdapter.notifyDataSetChanged();

                    }


                }

            }
        });


    }



    public void goBasketActivity(View view) {
        startActivity(new Intent(MainActivity.this, BasketActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    /// longitude and latitude is here
                    longitude = intent.getExtras().get("LONGITUDE").toString();
                    latitude = intent.getExtras().get("LATITUDE").toString();

                    //Toast.makeText(getApplicationContext(),"Longitude: "+intent.getExtras().get("LONGITUDE").toString()+"\n"+"Latitude: "+ intent.getExtras().get("LATITUDE"),Toast.LENGTH_SHORT).show();
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }


    public void openCallTowTruck()    {
        ///// her tuş için yazılacak
        dialogView.findViewById(R.id.ara_button).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view)
            {
                requestCallPermission();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "05383946580"));
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                alertDialog.dismiss();
            }});


        dialogView.findViewById(R.id.paylas_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                sendMail();
                alertDialog.dismiss();
            }});


        alertDialog.setView(dialogView);
        alertDialog.show();


        dialogView.findViewById(R.id.vazgec_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                alertDialog.cancel();
                alertDialog.dismiss();
            }});

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void requestLocationPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) & ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)  )
        {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Permission needed").setMessage("This permission needed because of this and that").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
                }
            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            })
                    .create().show();
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    private void requestCallPermission()    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
        }
        else
        {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "05383946580"));
            startActivity(intent);
        }
    }

    public void startLocation(){
        startService(new Intent(MainActivity.this, LocationService.class));
    }

    public String createAdress(String longitudeCame, String latitudeCame) {
        double longitude1 = Double.parseDouble(longitudeCame);
        double latitude1 = Double.parseDouble(latitudeCame);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try
        {
            addresses = geocoder.getFromLocation(latitude1, longitude1, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            //Toast.makeText(getApplicationContext(),addresses.get(0).toString(),Toast.LENGTH_SHORT).show();
            return addresses.get(0).toString();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
        return "Adres bilginiz yok";
    }

    private void sendMail()    {
        Intent intent = new Intent (Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"satis@kaanotomotiv.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Yol Yardım Talebi");
        intent.putExtra(Intent.EXTRA_TEXT, createAdress(longitude,latitude)+" Long: "+longitude+" Lang: "+ latitude);
        intent.setPackage("com.google.android.gm");
        if (intent.resolveActivity(getPackageManager())!=null)
        {
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this,"Gmail App is not installed",Toast.LENGTH_SHORT).show();
        }
    }

    public void showMyToyotaFeatures(View view) {
        Intent intent = new Intent(this, MyToyotaActivity.class);
        startActivity(intent);
    }

    public void goMyDealer(View view) {
        Intent intent = new Intent(this, MyDealerActivity.class);
        startActivity(intent);
    }

    public void setAppointment(View view) {
        Intent intent = new Intent(this, SetAppointmentActivity.class);
        startActivity(intent);
    }

    public void requestRoadAssistant(View view)       {
        callTowTruck.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if (longitude.isEmpty() || latitude.isEmpty() || isOnline())
                    {
                        createAdress(longitude, latitude);
                        openCallTowTruck();


                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Lütfen internet bağlantınızın olduğundan ve konumunuzun açık olduğundan emin olun. ", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Konum bilgisi aranıyor.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
