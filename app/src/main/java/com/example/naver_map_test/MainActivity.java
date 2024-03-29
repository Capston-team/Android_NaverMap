package com.example.naver_map_test;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.util.InetAddressUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import nl.joery.animatedbottombar.AnimatedBottomBar;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String ACTIVITY_NAME = "MainActivity";

    FragmentManager fragmentManager;
    AnimatedBottomBar animatedBottomBar;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    private Fragment MapFrag, EventFrag, BarcodeFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        //getWindow().setAttributes(layoutParams);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        // 툴바 활성화
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_other);
        // 툴바에 적힐 제목
        actionBar.setTitle("");
        actionBar.setHomeButtonEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(this);

        animatedBottomBar = findViewById(R.id.bottom_bar);

        String carrier = PreferenceUtil.getCarrierPreferences(this.getApplicationContext(), "carrier");
        String rate = PreferenceUtil.getRatePreferences(this.getApplicationContext(), "rate");

        if(carrier == null && rate == null) {
            Intent form_intent = new Intent(this, carrier_form.class);
            form_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            // 처음 통신사, 등급 입력 창 호출
            startActivityForResult(form_intent, 1);
        }
        else{
            animatedBottomBar.selectTabById(R.id.home1, true);
            fragmentManager = getSupportFragmentManager();
            MapFrag = new fragment_home1();
            fragmentManager.beginTransaction().add(R.id.menu_frame_layout, MapFrag,"MapFrag")
                    .addToBackStack(null)
                    .commit();
        }
        Log.e("MainActivity onCreate", "ENTER");

        animatedBottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int lastIndex, @Nullable AnimatedBottomBar.Tab lastTab, int newIndex, @NonNull AnimatedBottomBar.Tab newTab) {

                int id = newTab.getId();
                if(id == R.id.home1) {
                    if(MapFrag == null){
                        MapFrag = new fragment_home1();
                        fragmentManager.beginTransaction().add(R.id.menu_frame_layout, MapFrag, "MapFrag").addToBackStack(null).commit();
                    }
                    if(MapFrag != null) fragmentManager.beginTransaction().show(MapFrag).commit();
                    if(EventFrag != null) fragmentManager.beginTransaction().hide(EventFrag).commit();
                    if(BarcodeFrag != null) fragmentManager.beginTransaction().hide(BarcodeFrag).commit();

                } else if (id == R.id.home2) {
                    if(EventFrag == null){
                        EventFrag = new fragment_home2();
                        fragmentManager.beginTransaction().add(R.id.menu_frame_layout, EventFrag, "EventFrag").addToBackStack(null).commit();
                    }
                    if(MapFrag != null) fragmentManager.beginTransaction().hide(MapFrag).commit();
                    if(EventFrag != null) fragmentManager.beginTransaction().show(EventFrag).commit();
                    if(BarcodeFrag != null) fragmentManager.beginTransaction().hide(BarcodeFrag).commit();

                } else if (id == R.id.home3) {
                    if(BarcodeFrag == null){
                        BarcodeFrag = new fragment_home3();
                        fragmentManager.beginTransaction().add(R.id.menu_frame_layout, BarcodeFrag, "BarcodeFrag").addToBackStack(null).commit();
                    }
                    if(MapFrag != null) fragmentManager.beginTransaction().hide(MapFrag).commit();
                    if(EventFrag != null) fragmentManager.beginTransaction().hide(EventFrag).commit();
                    if(BarcodeFrag != null) fragmentManager.beginTransaction().show(BarcodeFrag).commit();

                } else {
                    Log.e("처리되지 않은 Fragment 접근", "처리되지않은 Fragment 접근입니다.");
                }
            }
            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                animatedBottomBar.selectTabById(R.id.home1, true);
                fragmentManager = getSupportFragmentManager();
                MapFrag = new fragment_home1();
                fragmentManager.beginTransaction().add(R.id.menu_frame_layout, MapFrag,"MapFrag")
                        .addToBackStack(null)
                        .commit();
            }
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.config) {
            // 이제 여기에 Intent 설정
            Intent UserSettings = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(UserSettings);
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.e("BackPressed", "backPressed");
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finish();
            super.onBackPressed();
        }
    }
}