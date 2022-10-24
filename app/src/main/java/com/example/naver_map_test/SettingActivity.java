package com.example.naver_map_test;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {
    TextView textMyTelRank;
    TextView textTel;
    TextView textRank;
    Spinner spinnerTel;
    Spinner spinnerRank;
    Button btnConfirm;
    Button btnBack;

    String selTel, selRank;
    int tel_i, rank_i; //저장할 통신사 등급 인덱스

    String[] telecom = {"SKT", "KT", "LG"};
    List<String> rank = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        textTel=findViewById(R.id.textTel);
        textRank=findViewById(R.id.textRank);
        spinnerTel=findViewById(R.id.spinnerTel);
        spinnerRank=findViewById(R.id.spinnerRank);
        btnConfirm=findViewById(R.id.btnConfirm);
        textMyTelRank=findViewById(R.id.currentTel);
        btnBack=findViewById(R.id.btnBack);

        ArrayAdapter<String> adapterTel = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, telecom
        );
        adapterTel.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerTel.setAdapter(adapterTel);

        ArrayAdapter<String> adapterRank = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, rank
        );
        adapterRank.setDropDownViewResource(android.R.layout.simple_spinner_item);


        String carrier = PreferenceUtil.getCarrierPreferences(getApplicationContext(), "carrier");
        String rate = PreferenceUtil.getRatePreferences(getApplicationContext(), "rate");
        textMyTelRank.setText("내 통신사  :  ["+carrier+"]     내 등급  :  ["+rate+"]");
        Log.d("preference", carrier+"  rate : " + rate);

        // 통신사 Spinner 리스너
        spinnerTel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                spinnerRank.setAdapter(adapterRank);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // 등급 Spinner 리스너
        spinnerRank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selRank=rank.get(i);
                rank_i =i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Toolbar settingsToolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(settingsToolbar);
        ActionBar actionBar = getSupportActionBar();


        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_settings_back);
            actionBar.setTitle("환경설정");
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String changeRank = spinnerRank.getSelectedItem().toString();
                String changeTel = spinnerTel.getSelectedItem().toString();
                PreferenceUtil.setCarrierPreferences(getApplicationContext(), "carrier", changeTel);
                PreferenceUtil.setRatePreferences(getApplicationContext(), "rate", changeRank);

                Toast.makeText(getApplicationContext(),"변경이 완료되었습니다.", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}