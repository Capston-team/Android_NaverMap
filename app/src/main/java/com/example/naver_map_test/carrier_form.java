package com.example.naver_map_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class carrier_form extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "carrier_form";


    Button confirm;

    String carrier;
    String rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrier_form);

        confirm = (Button) findViewById(R.id.confirm);

        String[] types = new String[]{"SKT", "KT", "LG"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                types
        );

        String[] skt_rate = new String[]{"VIP", "GOLD", "SILVER"};
        ArrayAdapter<String> skt_adapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                skt_rate
        );
        String[] kt_rate = new String[]{"VVIP","VIP" ,"GOLD", "일반"};
        ArrayAdapter<String> kt_adapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                kt_rate
        );
        String[] lg_rate = new String[]{"VVIP","VIP" ,"DIAMOND"};
        ArrayAdapter<String> lg_adapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                lg_rate
        );


        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.filled_exposed);
        autoCompleteTextView.setAdapter(adapter);

        AutoCompleteTextView rate_autoCompleteTextView = findViewById(R.id.filled_exposed2);

        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            carrier = autoCompleteTextView.getText().toString();
            if(autoCompleteTextView.getText().toString().equals("SKT")) {
                rate_autoCompleteTextView.setAdapter(skt_adapter);
                rate_autoCompleteTextView.setOnItemClickListener((adapterView1, view1, i1, l1) -> rate = rate_autoCompleteTextView.getText().toString());
            } else if(autoCompleteTextView.getText().toString().equals("KT")) {
                rate_autoCompleteTextView.setAdapter(kt_adapter);
                rate_autoCompleteTextView.setOnItemClickListener((adapterView12, view12, i12, l12) -> rate = rate_autoCompleteTextView.getText().toString());
            } else {
                rate_autoCompleteTextView.setAdapter(lg_adapter);
                rate_autoCompleteTextView.setOnItemClickListener((adapterView13, view13, i13, l13) -> rate = rate_autoCompleteTextView.getText().toString());
            }
        });


        confirm.setOnClickListener(view -> {
            if(carrier != null && rate != null) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("carrier", carrier);
                intent.putExtra("rate", rate);


                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(carrier_form.this, "통신사와 등급을 입력해야 합니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Log.i("---","---");
            Log.w("//===========//","================================================");
            Log.i("","\n"+"["+String.valueOf(ACTIVITY_NAME)+" >> onKeyDown() :: 백버튼 터치시 뒤로 가기 이벤트 발생]");
            Log.w("//===========//","================================================");
            Log.i("---","---");
        }
        return true;
    }

}