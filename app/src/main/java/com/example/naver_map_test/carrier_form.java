package com.example.naver_map_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class carrier_form extends AppCompatActivity {

    TextView txt;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrier_form);

        confirm = (Button) findViewById(R.id.confirm);

        String[] types = new String[]{"A", "b", "c"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                types
        );

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.filled_exposed);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(carrier_form.this, autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });

    }
}