package com.example.news;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private RecyclerView newsRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<ArticlesDbModel> arrArticles;
    private ArticlesDatabase db;
    private NewsRVAdapter newsRVAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    ImageView saveBtn,savedBtn,shareBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = LayoutInflater.from(this).inflate(R.layout.news_rv_item,null,false);
        saveBtn=view.findViewById(R.id.saveBtn);
        savedBtn=view.findViewById(R.id.savedBtn);
        shareBtn=view.findViewById(R.id.shareBtn);

        db=new ArticlesDatabase(this);
        newsRV=findViewById(R.id.idRVNews);
        loadingPB=findViewById(R.id.idPBLoading);
        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.navigationView);
        toolbar=findViewById(R.id.idToolbar);


        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(MainActivity.this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();

                if (id==R.id.All) {
                    getNews("All");
                }
                else if (id==R.id.Business) {
                    getNews("business");
                } else if (id==R.id.Entertainment) {
                    getNews("entertainment");
                } else if (id==R.id.General) {
                    getNews("general");
                } else if (id==R.id.Health) {
                    getNews("health");
                } else if (id==R.id.Science) {
                    getNews("science");
                } else if (id==R.id.Sports) {
                    getNews("sports");
                } else if (id==R.id.Technology) {
                    getNews("technology");
                }
                else if(id==R.id.saved){
                    getSavedNews();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        articlesArrayList=new ArrayList<>();
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        getNews("All");
    }

    private void getSavedNews(){
        arrArticles=db.fetch();
        savedBtn.setVisibility(View.VISIBLE);
        saveBtn.setVisibility(View.GONE);
        newsRVAdapter=new NewsRVAdapter(MainActivity.this,arrArticles);
        newsRV.setAdapter(newsRVAdapter);
        newsRVAdapter.notifyDataSetChanged();
    }


    private void getNews(String category){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL="https://newsapi.org/v2/top-headlines?country=in&category="+category+"&apikey=";
        String url="https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apikey=";
        String BASE_URL="https://newsapi.org/";

        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI=retrofit.create(RetrofitAPI.class);
        Call<NewsModal> call;
        if(category.equals("All")){
            call=retrofitAPI.getAllNews(url);
        }else{
            call=retrofitAPI.getNewsByCategory(categoryURL);
        }
        call.enqueue(new Callback<NewsModal>() {
            @Override
            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
                NewsModal newsModal=response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles= newsModal.getArticles();
                for (int i=0;i<articles.size();i++){
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),articles.get(i).getUrl(),
                            articles.get(i).getContent(),articles.get(i).getSource()));

                }
                newsRVAdapter=new NewsRVAdapter(articlesArrayList,MainActivity.this);
                newsRV.setAdapter(newsRVAdapter);
                newsRVAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<NewsModal> call, Throwable t) {
                Toast.makeText(MainActivity.this,"check internet connection",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}