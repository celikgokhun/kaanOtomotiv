package com.kaan.otomotiv.myapplication.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaan.otomotiv.myapplication.Classes.BuyFeedRecyclerAdapter;
import com.kaan.otomotiv.myapplication.Classes.CampFeedRecyclerAdapter;
import com.kaan.otomotiv.myapplication.R;

import java.util.ArrayList;
import java.util.Map;

public class BasketActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    ArrayList<String> buyGoodsTitleFromFB;
    ArrayList<String> buyGoodsBodyFromFB;
    ArrayList<String> buyGoodsPriceFromFB;
    ArrayList<String> buyGoodsModelsFromFB;
    ArrayList<String> buyGoodsPhotoFromFB;

    ArrayList<String> selected;

    RecyclerView recyclerView;

    BuyFeedRecyclerAdapter buyFeedRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        buyGoodsTitleFromFB = new ArrayList<>();
        buyGoodsBodyFromFB = new ArrayList<>();
        buyGoodsPriceFromFB = new ArrayList<>();
        buyGoodsModelsFromFB = new ArrayList<>();
        buyGoodsPhotoFromFB = new ArrayList<>();


        selected =  new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getDataFromFirestore();

        recyclerView = findViewById(R.id.recyclerViewForBuy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        buyFeedRecyclerAdapter = new BuyFeedRecyclerAdapter(buyGoodsTitleFromFB, buyGoodsBodyFromFB, buyGoodsPriceFromFB, buyGoodsModelsFromFB, buyGoodsPhotoFromFB);
        recyclerView.setAdapter(buyFeedRecyclerAdapter);
    }


    public void refreshBasket(View view)
    {
        //startActivity(new Intent(BasketActivity.this, BasketActivity.class));
        System.out.println("tekrar başlatıldım.");
    }

    public void getDataFromFirestore()    {

        CollectionReference collectionReference = firebaseFirestore.collection("BASKETS");
        collectionReference.orderBy("bought_goods_date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Toast.makeText(BasketActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null) {

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {

                        Map<String,Object> data = snapshot.getData();

                        //Casting
                        String title = (String) data.get("bought_goods_title");
                        String body = (String) data.get("bought_goods_body");
                        String price = (String) data.get("bought_goods_price");
                        String model = (String) data.get("bought_goods_models");
                        String downloadUrl = (String) data.get("bought_goods_downloadUrl");

                        buyGoodsTitleFromFB.add(title);
                        buyGoodsBodyFromFB.add(body);
                        buyGoodsPriceFromFB.add(price);
                        buyGoodsModelsFromFB.add(model);
                        buyGoodsPhotoFromFB.add(downloadUrl);

                        buyFeedRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }
}
