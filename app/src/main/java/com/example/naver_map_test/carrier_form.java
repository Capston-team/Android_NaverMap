package com.example.naver_map_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class carrier_form extends AppCompatActivity {

    Spinner spinner_tel, spinner_rank;
    Button confirm;

    String[] telecom = {"-- 통신사 입력 --","SKT", "KT", "LG"};
    String[] rank = new String[5];

    String selTel, selRank;

    int tel_i, rank_i; //저장할 통신사 등급 인덱스
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams();
        layoutParams.flags= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount= 0.5f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.activity_carrier_form);

        confirm = findViewById(R.id.confirm);

        spinner_tel = findViewById(R.id.spinner_tel);
        spinner_rank = findViewById(R.id.spinner_rank);



        ArrayAdapter<String> adapterTel = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, telecom
        );
        adapterTel.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_tel.setAdapter(adapterTel);

        ArrayAdapter<String> adapterRank = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, rank
        );
        adapterRank.setDropDownViewResource(android.R.layout.simple_spinner_item);


        spinner_tel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0) spinner_rank.setAdapter(adapterRank);
                rank[0] = "-- 등급 입력 --";
                selTel=telecom[i];

                tel_i=i;

                switch (i){
                    case 1:
                        rank[1]="VIP";
                        rank[2]="GOLD";
                        rank[3]="SILVER";
                        rank[4]="-";
                        break;
                    case 2:
                        rank[1]="VVIP";
                        rank[2]="VIP";
                        rank[3]="GOLD";
                        rank[4]="일반";
                        break;
                    case 3:
                        rank[1]="VVIP";
                        rank[2]="VIP";
                        rank[3]="다이아몬드";
                        rank[4]="-";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner_rank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selRank=rank[i];
                rank_i =i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceUtil.setCarrierPreferences(getApplicationContext(), "carrier", telecom[tel_i]);
                PreferenceUtil.setRatePreferences(getApplicationContext(), "rate", rank[rank_i]);
                if(selTel=="-- 통신사 입력 --" || selRank == "-- 등급 입력 --"){
                    Toast.makeText(carrier_form.this, "통신사 및 등급을 선택하세요.", Toast.LENGTH_LONG).show();
                    return;
                }


                Intent intent=new Intent();
                intent.putExtra("carrier", telecom[tel_i]);
                intent.putExtra("rate", rank[rank_i]);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //



    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}
