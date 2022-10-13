package com.example.naver_map_test;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;

public class ProgressBar extends Dialog {

    public ProgressBar(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);


        android.widget.ProgressBar progressBar = findViewById(R.id.spin_kit);
        Sprite FadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(FadingCircle);

    }
}
