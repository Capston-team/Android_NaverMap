package com.example.naver_map_test;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

public class fragment_home3 extends Fragment {

    Button btnAdd;

    ImageView imageViewResult;

    String num; //생성할 바코드 숫자

    public fragment_home3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home3, container, false);

        btnAdd=v.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(v.getContext(), PopupSelectActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        imageViewResult = v.findViewById(R.id.imageViewResult);

        // 이미지 저장 미완성
//        imageViewResult.setOnClickListener(view -> {
//            Bitmap bitmap = Bitmap.createBitmap(200, 400, Bitmap.Config.ARGB_8888);
//
//            File root = new File(Environment.getExternalStorageDirectory(), "barcode");
//
//            if (!root.exists()) {
//                root.mkdirs();
//            }
//
//            File mypath = new File(root,"barcode.jpg");
//
//            FileOutputStream fos = null;
//
//            try {
//                fos = new FileOutputStream(mypath);
//                // Use the compress method on the BitMap object to write image to the OutputStream
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            try {
//                fos.flush();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                fos.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                num=data.getStringExtra("num");
                try {
                    //받아온 바코드 숫자로 이미지 생성
                    String productId = num;
                    Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
                    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                    Writer codeWriter;
                    codeWriter = new Code128Writer();
                    BitMatrix byteMatrix = codeWriter.encode(productId, BarcodeFormat.CODE_128,400, 200, hintMap);
                    int width = byteMatrix.getWidth();
                    int height = byteMatrix.getHeight();
                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    imageViewResult.setImageBitmap(bitmap);



                } catch (Exception e) {

                }

            }


        }

    }
//    public static void saveImage(Bitmap bitmapImage) {
//
//        File root = new File(Environment.getExternalStorageDirectory(), "barcode");
//
//        if (!root.exists()) {
//            root.mkdirs();
//        }
//
//        File mypath = new File(root,"barcode.jpg");
//
//
//        FileOutputStream fos = null;
//
//        try {
//            fos = new FileOutputStream(mypath);
//            // Use the compress method on the BitMap object to write image to the OutputStream
//            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}