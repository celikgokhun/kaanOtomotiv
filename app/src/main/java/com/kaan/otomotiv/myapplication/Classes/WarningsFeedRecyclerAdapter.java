package com.kaan.otomotiv.myapplication.Classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaan.otomotiv.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WarningsFeedRecyclerAdapter extends RecyclerView.Adapter<WarningsFeedRecyclerAdapter.PostHolder>
{
    private ArrayList<String> warningsTitleList;
    private ArrayList<String> warningsBodiesList;
    private ArrayList<String> warningsImageList;

    public WarningsFeedRecyclerAdapter(ArrayList<String> warningsTitleList, ArrayList<String> warningsBodiesList, ArrayList<String> warningsImageList)
    {
        this.warningsTitleList = warningsTitleList;
        this.warningsBodiesList = warningsBodiesList;
        this.warningsImageList = warningsImageList;
    }

    @NonNull
    @Override
    public WarningsFeedRecyclerAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row2,parent,false);
        return new WarningsFeedRecyclerAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WarningsFeedRecyclerAdapter.PostHolder holder, int position) {

        holder.warningsTitleTextView.setText(warningsTitleList.get(position));
        holder.warningsBodyTextView.setText(warningsBodiesList.get(position));
        Picasso.get().load(warningsImageList.get(position)).into(holder.warningsImageView);
    }

    @Override
    public int getItemCount() {
        return warningsTitleList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder {

        ImageView warningsImageView;
        TextView warningsTitleTextView;
        TextView warningsBodyTextView;


        public PostHolder(@NonNull View itemView) {
            super(itemView);

            warningsImageView = itemView.findViewById(R.id.photo_recycler_warnings);
            warningsTitleTextView = itemView.findViewById(R.id.title_recycler_warnings);
            warningsBodyTextView = itemView.findViewById(R.id.body_recycler_warnings);


        }
    }
}
