package com.example.naver_map_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        Intent intent=getIntent();
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        String distance = intent.getStringExtra("distance");
        int imageId = intent.getIntExtra("profile", 0);

        ImageView shopProfile;
        TextView shopTitle;
        TextView rate;
        TextView dt;

        shopProfile = findViewById(R.id.shopProfile);
        shopTitle = (TextView)findViewById(R.id.shopTitle);
        rate = (TextView)findViewById(R.id.rate);
        dt = (TextView)findViewById(R.id.dt);

        shopProfile.setImageResource(imageId);
        shopTitle.setText(title);
        rate.setText(message);
        dt.setText(distance);


    }
}