package com.example.news;

import android.content.Context;
import com.squareup.picasso.Picasso;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewsRVAdapter extends RecyclerView.Adapter<NewsRVAdapter.ViewHolder> {

    private ArrayList<Articles> articlesArrayList;
    private ArrayList<ArticlesDbModel> arrArticles;
    private ArrayList<ArticlesDbModel> arr;
    private Context context;
    private Articles articles;
    private ArticlesDbModel articlesDbModel;
    private NewsRVAdapter.ViewHolder holder;

    public NewsRVAdapter(ArrayList<Articles> articlesArrayList, MainActivity context) {
        this.articlesArrayList = articlesArrayList;
        this.context = context;
        ArticlesDatabase db1=new ArticlesDatabase(context);
         arr=db1.fetch();
    }

    public NewsRVAdapter(MainActivity context, ArrayList<ArticlesDbModel> arrArticles) {
        this.arrArticles = arrArticles;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_rv_item, parent, false);
        return new NewsRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsRVAdapter.ViewHolder holder, int position) {
         this.holder=holder;
        if (articlesArrayList != null && !articlesArrayList.isEmpty() && position < articlesArrayList.size()) {
            articles = articlesArrayList.get(position);
            bindArticleData(holder, articles);
        } else if (arrArticles != null && !arrArticles.isEmpty() && position < arrArticles.size()) {
            articlesDbModel = arrArticles.get(position);
            bindDbArticleData(holder, articlesDbModel);
        }
    }

    private void bindArticleData(ViewHolder holder, Articles articles) {
        ArticlesDatabase db = new ArticlesDatabase(context);
        holder.source.setText(articles.getSource().getName());
        holder.subTileTV.setText(articles.getDescription());
        holder.titleTV.setText(articles.getTitle());
        Picasso.get().load(articles.getUrlToImage()).into(holder.newsIV);
        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, NewsDetailActivity.class);
            i.putExtra("title", articles.getTitle());
            i.putExtra("content", articles.getContent());
            i.putExtra("desc", articles.getDescription());
            i.putExtra("image", articles.getUrlToImage());
            i.putExtra("url", articles.getUrl());
            context.startActivity(i);
        });

        checkSavedState();


        holder.saveBtn.setOnClickListener(v -> {
            holder.saveBtn.setVisibility(View.GONE);
            holder.savedBtn.setVisibility(View.VISIBLE);
            db.insert(articles.getSource().getName(), articles.getTitle(), articles.getDescription(), articles.getContent(), articles.getUrlToImage(), articles.getUrl());
        });

        holder.savedBtn.setOnClickListener(v -> {
            holder.savedBtn.setVisibility(View.GONE);
            holder.saveBtn.setVisibility(View.VISIBLE);
            db.delete(articles.getUrl());
        });

        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ishare=new Intent(Intent.ACTION_SEND);
                ishare.setType("text/plain");
                ishare.putExtra(Intent.EXTRA_TEXT,articles.getDescription()+"\n"+articles.getUrl());
                context.startActivity(ishare);
            }
        });
    }

    private void checkSavedState(){
        if (articlesArrayList!=null && arr.size()!=0){
            for (int i=0;i<arr.size();i++){
                if(articles.getUrl().equals(arr.get(i).newsUrl)){
                    holder.saveBtn.setVisibility(View.GONE);
                    holder.savedBtn.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    private void bindDbArticleData(ViewHolder holder, ArticlesDbModel articlesDbModel) {
      ArticlesDatabase db=new ArticlesDatabase(context);
        holder.saveBtn.setVisibility(View.GONE);
        holder.savedBtn.setVisibility(View.VISIBLE);

        holder.source.setText(articlesDbModel.source);
        holder.titleTV.setText(articlesDbModel.title);
        holder.subTileTV.setText(articlesDbModel.subtitle);
        Picasso.get().load(articlesDbModel.imgUrl).into(holder.newsIV);

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, NewsDetailActivity.class);
            i.putExtra("title", articlesDbModel.title);
            i.putExtra("content", articlesDbModel.content);
            i.putExtra("desc", articlesDbModel.subtitle);
            i.putExtra("image", articlesDbModel.imgUrl);
            i.putExtra("url", articlesDbModel.newsUrl);
            context.startActivity(i);
        });

        holder.savedBtn.setOnClickListener(v -> {
            holder.savedBtn.setVisibility(View.GONE);
            holder.saveBtn.setVisibility(View.VISIBLE);
            db.delete(articlesDbModel.newsUrl);
            arrArticles.remove(holder.getAdapterPosition());
            notifyDataSetChanged();
            notifyItemRemoved(holder.getAdapterPosition());
            notifyItemRangeRemoved(holder.getAdapterPosition(),arrArticles.size());
        });
        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ishare=new Intent(Intent.ACTION_SEND);
                ishare.setType("text/plain");
                ishare.putExtra(Intent.EXTRA_TEXT,articlesDbModel.subtitle+"\n"+articlesDbModel.newsUrl);
                context.startActivity(ishare);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (articlesArrayList != null && !articlesArrayList.isEmpty()) {
            return articlesArrayList.size();
        } else if (arrArticles != null && !arrArticles.isEmpty()) {
            return arrArticles.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTV, subTileTV, source;
        private ImageView newsIV, saveBtn, savedBtn,shareBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.idTVNewsHeading);
            subTileTV = itemView.findViewById(R.id.idTVSubTitle);
            newsIV = itemView.findViewById(R.id.idIVNews);
            source = itemView.findViewById(R.id.source);
            saveBtn = itemView.findViewById(R.id.saveBtn);
            savedBtn = itemView.findViewById(R.id.savedBtn);
            shareBtn=itemView.findViewById(R.id.shareBtn);
        }
    }
}
