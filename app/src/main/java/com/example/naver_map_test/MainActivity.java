package com.example.naver_map_test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;

import java.util.Arrays;

import nl.joery.animatedbottombar.AnimatedBottomBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    FragmentManager fragmentManager;
    AnimatedBottomBar animatedBottomBar;

    private ActionBar actionBar;

    DrawerLayout drawerLayout;
    NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MainActivity onCreate", "ENTER");

        // 처음 통신사, 등급 입력 창 호출
        startActivityForResult(new Intent(MainActivity.this, carrier_form.class), 0);


        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        // 툴바 활성화
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_other);
        // 툴바에 적힐 제목
        actionBar.setTitle("");
        actionBar.setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);


        animatedBottomBar = findViewById(R.id.bottom_bar);

        if (savedInstanceState == null) {
            animatedBottomBar.selectTabById(R.id.home1, true);
            fragmentManager = getSupportFragmentManager();
            fragment_home1 homeFragment = new fragment_home1();
            fragmentManager.beginTransaction().replace(R.id.menu_frame_layout, homeFragment)
                    .commit();
        }

        animatedBottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int lastIndex, @Nullable AnimatedBottomBar.Tab lastTab, int newIndex, @NonNull AnimatedBottomBar.Tab newTab) {
                Fragment fragment = null;

                int id = newTab.getId();
                if (id == R.id.home1) {
                    System.out.println(id);
                    Log.d("bottom_bar", "Selected index: home1, title: home1");
                    fragment = new fragment_home1();
                } else if (id == R.id.home2) {
                    System.out.println(id);
                    Log.d("bottom_bar", "Selected index: home2, title: home2");
                    fragment = new fragment_home2();
                } else if (id == R.id.home3) {
                    System.out.println(id);
                    Log.d("bottom_bar", "Selected index: home3, title: home3");
                    fragment = new fragment_home3();
                }

                if (fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.menu_frame_layout, fragment)
                            .commit();
                } else {
                    Log.e(TAG, "Error in creatring Fragment");
                }

            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });
    }
    // 통신사, 등급 입력 창 결과
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "result ok!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "result cancle", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 입력 화면 구현? 확실하진 않음
    @Override
    protected void onStart() {
        super.onStart();
//        setContentView(R.layout.activity_carrier_form);
        Log.e("MainActivity onStart", "ENTER");
    }

    // 중지 되어있던 액티비티가 다시 재 실행 되는 시점에서 이곳 내부구문들을 실행
    @Override
    protected void onResume() {
        //다이얼로그 밖의 화면은 흐리게 만들어줌
        super.onResume();
        Log.e("MainActivity onResume", "ENTER");

    }

    // 중지 상태(홈 버튼을 눌러서 바깥으로 잠깐 빠져나갔을 때, 다른 액티비티가 활성화 되어있을 때) 일 때 이곳 내부 구문을 실행
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainActivity onPause", "ENTER");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MainActivity onStop", "ENTER");
    }

    // 화면이 파괴되어서 소멸했을 때의 시점 일 때 이곳 내부구문들을 실행
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MainActivity onDestroy", "ENTER");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}