package com.example.naver_map_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class carrier_form extends AppCompatActivity {

    Spinner spinner_tel, spinner_rank;
    Button confirm;

    String[] telecom = {"SKT", "KT", "LG"};
    List<String> rank = new ArrayList<String>();

    String selTel, selRank;

    int tel_i, rank_i; //저장할 통신사 등급 인덱스
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setTitle("통신사와 등급을 선택하세요.");
        WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams();
        layoutParams.flags= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount= 0.5f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.activity_carrier_form);

        // view 찾는 코드
        confirm = findViewById(R.id.confirm);
        spinner_tel = findViewById(R.id.spinner_tel);
        spinner_rank = findViewById(R.id.spinner_rank);

        // 통신사 Adapter
        ArrayAdapter<String> adapterTel = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, telecom
        );
        adapterTel.setDropDownViewResource(android.R.layout.simple_spinner_item);
        // Adaper를 view에 연결
        spinner_tel.setAdapter(adapterTel);

        // 등급 Adapter
        ArrayAdapter<String> adapterRank = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, rank
        );
        adapterRank.setDropDownViewResource(android.R.layout.simple_spinner_item);

        // 통신사 Spinner 리스너
        spinner_tel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                selTel=telecom[index];
                tel_i=index;

                Log.e("index", String.valueOf(index));
                if(!rank.isEmpty()) {
                    rank.clear();
                }

                switch (index){
                    // SKT
                    case 0:
                        rank.add("VIP");
                        rank.add("GOLD");
                        rank.add("SILVER");
                        break;
                    // KT
                    case 1:
                        rank.add("VVIP");
                        rank.add("VIP");
                        rank.add("GOLD");
                        rank.add("일반");
                        break;

                    // LG
                    case 2:
                        rank.add("VVIP");
                        rank.add("VIP");
                        rank.add("DIAMOND");
                        break;
                }
                spinner_rank.setAdapter(adapterRank);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // 등급 Spinner 리스너
        spinner_rank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selRank=rank.get(i);
                rank_i =i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        confirm.setOnClickListener(view -> {
            PreferenceUtil.setCarrierPreferences(getApplicationContext(), "carrier", telecom[tel_i]);
            PreferenceUtil.setRatePreferences(getApplicationContext(), "rate", rank.get(rank_i));

//            if(selTel.equals("-- 통신사 입력 --") || selRank.equals("-- 등급 입력 --")){
//                Toast.makeText(carrier_form.this, "통신사 및 등급을 선택하세요.", Toast.LENGTH_LONG).show();
//                return;
//            }

            Intent intent = new Intent();
            intent.putExtra("carrier", telecom[tel_i]);
            intent.putExtra("rate", rank.get(rank_i));
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}
