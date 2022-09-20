package com.example.naver_map_test;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class BarcodeItem {

    String brandName;
    String barcodePath;

    public BarcodeItem(String brandName, String barcodePath) {
        this.brandName=brandName;
        this.barcodePath=barcodePath;
    }
    public String getBrandName() {
        return brandName;
    }
    public String getBarcodePath() {
        return barcodePath;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    public void setBarcodePath(String barcodePath) {
        this.barcodePath = barcodePath;
    }
}
