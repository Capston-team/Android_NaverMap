package com.example.naver_map_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class PopupBarcodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams();
        layoutParams.flags= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount= 0.5f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.activity_popup_barcode);

        final EditText etBarcodeNum = findViewById(R.id.etBarcodeNum);
        final Button btnGenerate = findViewById(R.id.btnGenerate);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num=etBarcodeNum.getText().toString();
                Intent intent=new Intent();
                intent.putExtra("barcodeNum", num);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
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