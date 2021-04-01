package com.kaan.otomotiv.myapplication.Classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaan.otomotiv.myapplication.Activities.LegalActivity;
import com.kaan.otomotiv.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class BuyFeedRecyclerAdapter extends RecyclerView.Adapter<BuyFeedRecyclerAdapter.PostHolder>
{
    private ArrayList<String> buyGoodsTitleList;
    private ArrayList<String> buyGoodsBodiesList;
    private ArrayList<String> buyGoodsPricesList;
    private ArrayList<String> buyGoodsModelsList;
    private ArrayList<String> buyGoodsImageList;




    public BuyFeedRecyclerAdapter(ArrayList<String> buyGoodsTitleList, ArrayList<String> buyGoodsBodiesList, ArrayList<String> buyGoodsPricesList, ArrayList<String> buyGoodsModelsList, ArrayList<String> buyGoodsImageList)
    {
        this.buyGoodsTitleList = buyGoodsTitleList;
        this.buyGoodsBodiesList = buyGoodsBodiesList;
        this.buyGoodsPricesList = buyGoodsPricesList;
        this.buyGoodsModelsList = buyGoodsModelsList;
        this.buyGoodsImageList = buyGoodsImageList;
    }



    @NonNull
    @Override
    public BuyFeedRecyclerAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row4,parent,false);
        return new BuyFeedRecyclerAdapter.PostHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull BuyFeedRecyclerAdapter.PostHolder holder, int position) {

        holder.buyGoodsTitleTextView.setText(buyGoodsTitleList.get(position));
        holder.buyGoodsBodyTextView.setText(buyGoodsBodiesList.get(position));
        holder.buyGoodsPriceTextView.setText(buyGoodsPricesList.get(position));
        holder.buyGoodsModelsTextView.setText(buyGoodsModelsList.get(position));

        Picasso.get().load(buyGoodsImageList.get(position)).into(holder.buyGoodsImageView);
    }

    @Override
    public int getItemCount() {
        return buyGoodsTitleList.size();
    }


    class PostHolder extends RecyclerView.ViewHolder {

        ImageView buyGoodsImageView;
        TextView buyGoodsTitleTextView;
        TextView buyGoodsBodyTextView;
        TextView buyGoodsPriceTextView;
        TextView buyGoodsModelsTextView;

        Button deleteButton;

        public PostHolder(@NonNull final View itemView) {
            super(itemView);

            buyGoodsImageView = itemView.findViewById(R.id.photo_recycler_buy);
            buyGoodsTitleTextView = itemView.findViewById(R.id.title_recycler_buy);
            buyGoodsBodyTextView = itemView.findViewById(R.id.body_recycler_buy);
            buyGoodsPriceTextView = itemView.findViewById(R.id.price_recycler_buy);
            buyGoodsModelsTextView = itemView.findViewById(R.id.models_recycler_buy);

            /// to add to basket
            deleteButton = itemView.findViewById(R.id.delete_btn);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    deleteButton.setVisibility(View.INVISIBLE);
                    deleteButton.setClickable(false);
                    // begin

                    FirebaseFirestore db;

                    db = FirebaseFirestore.getInstance();
                    db.collection("BASKETS").addSnapshotListener(new EventListener<QuerySnapshot>()
                    {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                            if (e !=null)
                            {

                            }

                            for (DocumentChange documentChange : documentSnapshots.getDocumentChanges())
                            {
                                try
                                {
                                    String willDelete = documentChange.getDocument().getData().get("bought_goods_title").toString();
                                    //System.out.println("silinecek başlığı bulduk:     "+ buyGoodsTitleList.get(getAdapterPosition()));

                                    if (willDelete.equals(buyGoodsTitleList.get(getAdapterPosition())))
                                    {
                                        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                        //System.out.println("silinecek şey databasede  " + documentChange.getDocument().getId().toString());
                                        db2.collection("BASKETS").document(documentChange.getDocument().getId())
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        System.out.println("sildik seçileni");

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        System.out.println("silemedik");
                                                    }
                                                });
                                    }
                                }
                                catch (NullPointerException e1)
                                {
                                    System.out.println(e1.getLocalizedMessage());
                                }
                                catch (ArrayIndexOutOfBoundsException e2)
                                {
                                    System.out.println(e2.getLocalizedMessage());
                                }

                            }
                        }
                    });

                    /// end
                }
            });

        }
    }
}
