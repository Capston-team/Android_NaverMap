package com.example.naver_map_test;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class PopupSelectActivity extends AppCompatActivity {

    String barcodeName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("바코드를 등록하세요.");
        WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams();
        layoutParams.flags= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount= 0.5f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.activity_popup_select);

        final Button btn1 = findViewById(R.id.btn1);
        final Button btn2 = findViewById(R.id.btn2);
        final EditText EditTextBarcodeName = findViewById(R.id.barcodeName);

        EditTextBarcodeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                barcodeName=EditTextBarcodeName.getText().toString();
            }
        });


        //바코드 스캔
        btn1.setOnClickListener(view -> {
            Intent intent = new Intent(PopupSelectActivity.this, ScanActivity.class);
            startActivityForResult(intent, 2);
        });
        //바코드 숫자로 등록
        btn2.setOnClickListener(view -> {
            Intent intent = new Intent(PopupSelectActivity.this, PopupBarcodeActivity.class);
            startActivityForResult(intent, 3);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 || requestCode == 3) {
            if (resultCode == RESULT_OK) {
                String barcodeNum = data.getStringExtra("barcodeNum");
                Intent intent=new Intent();
                intent.putExtra("num", barcodeNum);
                Log.d("barcodenumselect", barcodeNum);
                intent.putExtra("barcodeName", barcodeName);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    //바깥 영역 클릭 방지 와 백 버튼 차단
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
//            return false;
//        }
//        return true;
//    }

}