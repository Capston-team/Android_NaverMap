package com.example.naver_map_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

public class BarcodeImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("");
        WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams();
        layoutParams.flags= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount= 0.5f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.activity_barcode_image);

        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("imagePath");

        ImageView imageBarcode = findViewById(R.id.imageBarcode);


        Bitmap bitmap_barcode = BitmapFactory.decodeFile(imagePath);
        imageBarcode.setImageBitmap(bitmap_barcode);



    }
}