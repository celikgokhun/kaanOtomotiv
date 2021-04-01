package com.kaan.otomotiv.myapplication.Activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaan.otomotiv.myapplication.Classes.CampFeedRecyclerAdapter;
import com.kaan.otomotiv.myapplication.Classes.WarningsFeedRecyclerAdapter;
import com.kaan.otomotiv.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccessoriesActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    ArrayList<String> goodsTitleFromFB;
    ArrayList<String> goodsBodyFromFB;
    ArrayList<String> goodsPriceFromFB;
    ArrayList<String> goodsModelsFromFB;
    ArrayList<String> goodsPhotoFromFB;

    ArrayList<String> selected;

    RecyclerView recyclerView;

    CampFeedRecyclerAdapter campFeedRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessories);

        goodsTitleFromFB = new ArrayList<>();
        goodsBodyFromFB = new ArrayList<>();
        goodsPriceFromFB = new ArrayList<>();
        goodsModelsFromFB = new ArrayList<>();
        goodsPhotoFromFB = new ArrayList<>();


        selected =  new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getDataFromFirestore();

        recyclerView = findViewById(R.id.recyclerViewForCamp);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        campFeedRecyclerAdapter = new CampFeedRecyclerAdapter(goodsTitleFromFB, goodsBodyFromFB, goodsPriceFromFB, goodsModelsFromFB, goodsPhotoFromFB);
        recyclerView.setAdapter(campFeedRecyclerAdapter);

    }

    public void getDataFromFirestore()
    {
        final String userPhoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();
        System.out.println(userPhoneNumber);

        FirebaseFirestore firebaseFirestore1 = FirebaseFirestore.getInstance();
        firebaseFirestore1.collection("USERS").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e !=null) {}

                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges())
                {
                    try
                    {
                        String phoneNumberFromDb = documentChange.getDocument().getData().get("user_phone_number").toString();
                        //System.out.println(phoneNumberFromDb);

                        if (userPhoneNumber.equals(phoneNumberFromDb))
                        {

                            final String userCarModel = documentChange.getDocument().getData().get("car_brand").toString();
                            System.out.println("kullanıcının arabasi:    "+ userCarModel);


                            CollectionReference collectionReference = firebaseFirestore.collection("GOODS");
                            collectionReference.orderBy("goods_date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                    if (e != null) {
                                        Toast.makeText(AccessoriesActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                    }

                                    if (queryDocumentSnapshots != null) {

                                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments())
                                        {
                                            Map<String,Object> data = snapshot.getData();

                                            //Casting
                                            String title = (String) data.get("goods_title");
                                            String body = (String) data.get("goods_body");
                                            String price = (String) data.get("goods_price");
                                            String model = (String) data.get("goods_models");
                                            String downloadUrl = (String) data.get("goods_downloadUrl");

                                            try
                                            {
                                                if (model.contains(userCarModel))
                                                {
                                                    goodsTitleFromFB.add(title);
                                                    goodsBodyFromFB.add(body);
                                                    goodsPriceFromFB.add(price);
                                                    goodsModelsFromFB.add(model);
                                                    goodsPhotoFromFB.add(downloadUrl);

                                                    campFeedRecyclerAdapter.notifyDataSetChanged();
                                                }
                                            }
                                            catch (NullPointerException npe)
                                            {
                                                System.out.println(npe.getLocalizedMessage());
                                            }


                                        }
                                    }
                                }
                            });

                        }
                    }
                    catch (NullPointerException e1)
                    {
                        System.out.println(e1.getLocalizedMessage());
                    }
                }
            }
        });
    }
}
