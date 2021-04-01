package com.kaan.otomotiv.myapplication.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaan.otomotiv.myapplication.Classes.NewsFeedRecyclerAdapter;
import com.kaan.otomotiv.myapplication.Classes.WarningsFeedRecyclerAdapter;
import com.kaan.otomotiv.myapplication.R;

import java.util.ArrayList;
import java.util.Map;


public class WarningsActivity extends AppCompatActivity
{
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    ArrayList<String> warningsTitleFromFB;
    ArrayList<String> warningsBodyFromFB;
    ArrayList<String> warningsPhotoFromFB;

    WarningsFeedRecyclerAdapter warningsFeedRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warnings);

        warningsTitleFromFB = new ArrayList<>();
        warningsBodyFromFB= new ArrayList<>();
        warningsPhotoFromFB = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getDataFromFirestore();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewForWarnings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        warningsFeedRecyclerAdapter = new WarningsFeedRecyclerAdapter(warningsTitleFromFB, warningsBodyFromFB, warningsPhotoFromFB);
        recyclerView.setAdapter(warningsFeedRecyclerAdapter);
    }

    public void getDataFromFirestore()    {
        CollectionReference collectionReference = firebaseFirestore.collection("WARNINGS");

        collectionReference.orderBy("warnings_date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Toast.makeText(WarningsActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null) {

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {

                        Map<String,Object> data = snapshot.getData();

                        //Casting
                        String title = (String) data.get("warnings_title");
                        String body = (String) data.get("warnings_body");
                        String downloadUrl = (String) data.get("warnings_downloadUrl");

                        warningsTitleFromFB.add(title);
                        warningsBodyFromFB.add(body);
                        warningsPhotoFromFB.add(downloadUrl);

                        warningsFeedRecyclerAdapter.notifyDataSetChanged();

                    }


                }

            }
        });


    }

}