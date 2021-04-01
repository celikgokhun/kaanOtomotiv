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

public class NewsFeedRecyclerAdapter extends RecyclerView.Adapter<NewsFeedRecyclerAdapter.PostHolder> {

    private ArrayList<String> newsTitleList;
    private ArrayList<String> newsBodiesList;
    private ArrayList<String> newsImageList;

    public NewsFeedRecyclerAdapter(ArrayList<String> newsTitleList, ArrayList<String> newsBodiesList, ArrayList<String> newsImageList)
    {
        this.newsTitleList = newsTitleList;
        this.newsBodiesList = newsBodiesList;
        this.newsImageList = newsImageList;
    }



    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.newsTitleTextView.setText(newsTitleList.get(position));
        holder.newsBodyTextView.setText(newsBodiesList.get(position));
        Picasso.get().load(newsImageList.get(position)).into(holder.newsImageView);
    }

    @Override
    public int getItemCount() {
        return newsTitleList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder {

        ImageView newsImageView;
        TextView newsTitleTextView;
        TextView newsBodyTextView;


        public PostHolder(@NonNull View itemView) {
            super(itemView);

            newsImageView = itemView.findViewById(R.id.photo_recycler_news);
            newsTitleTextView = itemView.findViewById(R.id.title_recycler_news);
            newsBodyTextView = itemView.findViewById(R.id.body_recycler_news);


        }
    }
}
