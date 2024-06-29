package com.example.news;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import com.squareup.picasso.Picasso;


    public class NewsDetailActivity extends AppCompatActivity {
        String title, desc, content, imageURL, url;
        private TextView titleTV, subDescTv, contentTv;
        private ImageView newsIv;
        private Button readnewsBtn;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_news_detail);

            title = getIntent().getStringExtra("title");
            desc = getIntent().getStringExtra("desc");
            imageURL = getIntent().getStringExtra("image");
            url = getIntent().getStringExtra("url");
            content = getIntent().getStringExtra("content");

            titleTV = findViewById(R.id.idTVTitle);
            subDescTv = findViewById(R.id.idTVSubDesc);
            contentTv = findViewById(R.id.idTVContent);
            newsIv = findViewById(R.id.idIVNews);
            readnewsBtn = findViewById(R.id.idBtnReadNews);

            titleTV.setText(title);
            subDescTv.setText(desc);
            contentTv.setText(content);
            Picasso.get().load(imageURL).into(newsIv);

            readnewsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });


        }
    }