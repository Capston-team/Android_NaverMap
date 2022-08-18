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
import android.view.WindowManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends AppCompatActivity {
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
        getWindow().setAttributes(layoutParams);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
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
                        Log.i("---","---");
                        Log.w("//===========//","================================================");
                        Log.i("","\n"+"["+String.valueOf(ACTIVITY_NAME)+" >>  fragment 호출 시점() ::]");
                        Log.w("//===========//","================================================");
                        Log.i("---","---");
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
                } else { //
                    Log.e("처리되지 않은 Fragment 접근", "처리되지않은 Fragment 접근입니다.");
                }
            }
            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });

        try {
            String address = getIpAddress();

        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    // 실제 device IPv4 주소 가져오는 함수
    public String getIpAddress() throws SocketException {
        for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
            NetworkInterface intf = en.nextElement();


            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                InetAddress inetAddress = enumIpAddr.nextElement();

                if(inetAddress.isLoopbackAddress()) {
                    Log.i("IPAddress", intf.getDisplayName() + "(loopback) | " + inetAddress.getHostAddress());
                } else {
                    Log.i("IPAddress", intf.getDisplayName() + " | " + inetAddress.getHostAddress());
                }
                if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
                    return inetAddress.getHostAddress();
                }
            }
        }
        return null;
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


    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MainActivity onStart", "ENTER");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MainActivity onResume", "ENTER");

    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MainActivity onDestroy", "ENTER");
    }

}