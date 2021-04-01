package com.kaan.otomotiv.myapplication.Classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaan.otomotiv.myapplication.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;


public class CampFeedRecyclerAdapter extends RecyclerView.Adapter<CampFeedRecyclerAdapter.PostHolder>
{
    private ArrayList<String> goodsTitleList;
    private ArrayList<String> goodsBodiesList;
    private ArrayList<String> goodsPricesList;
    private ArrayList<String> goodsModelsList;
    private ArrayList<String> goodsImageList;




    public CampFeedRecyclerAdapter(ArrayList<String> goodsTitleList, ArrayList<String> goodsBodiesList, ArrayList<String> goodsPricesList, ArrayList<String> goodsModelsList, ArrayList<String> goodsImageList)
    {
        this.goodsTitleList = goodsTitleList;
        this.goodsBodiesList = goodsBodiesList;
        this.goodsPricesList = goodsPricesList;
        this.goodsModelsList = goodsModelsList;
        this.goodsImageList = goodsImageList;
    }



    @NonNull
    @Override
    public CampFeedRecyclerAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row3,parent,false);
        return new CampFeedRecyclerAdapter.PostHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CampFeedRecyclerAdapter.PostHolder holder, int position) {

        holder.goodsTitleTextView.setText(goodsTitleList.get(position));
        holder.goodsBodyTextView.setText(goodsBodiesList.get(position));
        holder.goodsPriceTextView.setText(goodsPricesList.get(position));
        holder.goodsModelsTextView.setText(goodsModelsList.get(position));


        Picasso.get().load(goodsImageList.get(position)).into(holder.goodsImageView);


    }

    @Override
    public int getItemCount() {
        return goodsTitleList.size();
    }


    class PostHolder extends RecyclerView.ViewHolder {

        ImageView goodsImageView;
        TextView goodsTitleTextView;
        TextView goodsBodyTextView;
        TextView goodsPriceTextView;
        TextView goodsModelsTextView;

        Button buyButton;

        public PostHolder(@NonNull final View itemView) {
            super(itemView);

            goodsImageView = itemView.findViewById(R.id.photo_recycler_camp);
            goodsTitleTextView = itemView.findViewById(R.id.title_recycler_camp);
            goodsBodyTextView = itemView.findViewById(R.id.body_recycler_camp);
            goodsPriceTextView = itemView.findViewById(R.id.price_recycler_camp);
            goodsModelsTextView = itemView.findViewById(R.id.models_recycler_camp);

            /// to add to basket
            buyButton = itemView.findViewById(R.id.buy_btn);
            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    buyButton.setVisibility(View.GONE);
                    buyButton.setClickable(false);
                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                    //System.out.println("phtonun download urli"+ goodsImageList.get(getAdapterPosition()));

                    String downloadUrl = goodsImageList.get(getAdapterPosition());
                    String goodsTitle = goodsTitleTextView.getText().toString();
                    String goodsBody = goodsBodyTextView.getText().toString();
                    String goodsPrice = goodsPriceTextView.getText().toString();
                    String goodsModels = goodsModelsTextView.getText().toString();

                    HashMap<String, Object> postData = new HashMap<>();

                    postData.put("bought_goods_downloadUrl",downloadUrl);
                    postData.put("bought_goods_title",goodsTitle);
                    postData.put("bought_goods_body",goodsBody);
                    postData.put("bought_goods_price",goodsPrice);
                    postData.put("bought_goods_models",goodsModels);
                    postData.put("bought_goods_date", FieldValue.serverTimestamp());

                    firebaseFirestore.collection("BASKETS").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            });

        }
    }

}
