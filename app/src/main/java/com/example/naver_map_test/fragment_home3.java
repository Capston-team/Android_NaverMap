package com.example.naver_map_test;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class fragment_home3 extends Fragment {

    ImageButton btnAdd;
    String num; //생성할 바코드 숫자
    ArrayList<BarcodeItem> mbarcodeItems= new ArrayList<>();;
    BarcodeRecyclerAdapter mRecyclerAdapter;

    public fragment_home3() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home3, container, false);

        RecyclerView mRecyclerView = v.findViewById(R.id.barcodeRecycler);

        /* initiate adapter */
        mRecyclerAdapter = new BarcodeRecyclerAdapter(getActivity());

        /* initiate recyclerview */
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        int i=1;

        mRecyclerAdapter.setBarcodeList(mbarcodeItems);
        Log.d("wow",Integer.toString(mbarcodeItems.size()));

//        imageViewResult = v.findViewById(R.id.imageViewResult);

        btnAdd=v.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(v.getContext(), PopupSelectActivity.class);
                startActivityForResult(intent, 1);

            }
        });
        File file_barcode_image = getContext().getFilesDir();
        String barcode_path = file_barcode_image.getPath();
        String[] file_list = getContext().fileList();

        Log.d("wow7", ""+file_list.length);
        for(int k=0; k<file_list.length; k++){
            if(file_list[k].contains("barcode")){
                mbarcodeItems.add(new BarcodeItem("skt"+k,barcode_path+"/"+file_list[k]));
            }
        }

        //File file = new File(barcode_path + "barcode1.png");
        //ContentResolver contentResolver = v.getContext().getContentResolver();
        //contentResolver.delete(barcode_path + "barcode1.png", null, null);
        //Log.d("bool_test", bool+"");
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        File file_barcode_image = getContext().getFilesDir();
        String barcode_path = file_barcode_image.getPath();
        String[] file_list = getContext().fileList();
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                try {
                    if(data != null) {
                        num=data.getStringExtra("num");
                    }
                    String productId = num;
                    Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
                    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                    Writer codeWriter;
                    codeWriter = new Code128Writer();
                    BitMatrix byteMatrix = codeWriter.encode(productId, BarcodeFormat.CODE_128,
                            400, 200, hintMap);
                    int width = byteMatrix.getWidth();
                    int height = byteMatrix.getHeight();
                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                        }
                    }

                    //바코드 이미지 순서대로 저장
                    int k;
                    for(k=0; k<file_list.length; k++){
                        if(Arrays.asList(file_list).contains("barcode"+k+".png")){
                            continue;
                        }
                        else{
                            String filepath = barcode_path + "/barcode"+k+".png";
                            Log.d("wow_k",""+k);
                            saveBitmapAsFile(bitmap, filepath);
                            break;
                        }
                    }
                    mbarcodeItems.add(new BarcodeItem("skt"+k,barcode_path+"/barcode"+k+".png"));
                    mRecyclerAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Log.e("fragment_home3", "onActivityResult Callback Error");
                }
            }
        }
    }
    private void saveBitmapAsFile(Bitmap bitmap, String filepath) {
        File file = new File(filepath);
        OutputStream os = null;

        try {
            file.createNewFile();
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
            Log.d("wow5","저장성공");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("wow5",e.toString());
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("wow2",Integer.toString(mbarcodeItems.size()));
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d("wow3",Integer.toString(mbarcodeItems.size()));
    }
}