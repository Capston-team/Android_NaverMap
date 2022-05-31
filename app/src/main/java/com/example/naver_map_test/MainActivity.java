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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.util.InetAddressUtils;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import nl.joery.animatedbottombar.AnimatedBottomBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String ACTIVITY_NAME = "MainActivity";

    FragmentManager fragmentManager;
    AnimatedBottomBar animatedBottomBar;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    Fragment fragment = null;

    String carrier;
    String rate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MainActivity onCreate", "ENTER");

        Intent form_intent = new Intent(MainActivity.this, carrier_form.class);
        form_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        // 처음 통신사, 등급 입력 창 호출
        startActivityResult.launch(form_intent);


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
            fragment_home1 homeFragment = new fragment_home1();
            fragmentManager.beginTransaction().replace(R.id.menu_frame_layout, homeFragment)
                    .commit();
        }

        animatedBottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int lastIndex, @Nullable AnimatedBottomBar.Tab lastTab, int newIndex, @NonNull AnimatedBottomBar.Tab newTab) {

                int id = newTab.getId();
                if (id == R.id.home1) {
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

        try {
            String address = getIpAddress();


        } catch (SocketException e) {
            e.printStackTrace();
        }

    }
    // 통신사, 등급 입력 창 결과
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.i("---","---");
                    Log.w("//===========//","================================================");
                    Log.i("","\n"+"["+String.valueOf(ACTIVITY_NAME)+" >> registerForActivityResult() :: 인텐트 결과 확인]");
                    Log.i("","\n"+"[result :: [전체 데이터] :: "+String.valueOf(result)+"]");
                    Log.w("//===========//","================================================");
                    Log.i("---","---");
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        // -----------------------------------------
                        // [인텐트 데이터 얻어온다]
                        Intent intent = result.getData();
                        // -----------------------------------------
                        // -----------------------------------------
                        // [setResult 에서 응답 받은 데이터 확인 실시]
                        assert intent != null;
                        carrier = intent.getStringExtra("carrier");
                        rate = intent.getStringExtra("rate");

                        Log.i("---","---");
                        Log.w("//===========//","================================================");
                        Log.i("","\n"+"["+String.valueOf(ACTIVITY_NAME)+" >> registerForActivityResult() :: 인텐트 응답 데이터 확인]");
                        Log.i("","\n"+"[onActivityResult carrier : "+String.valueOf(carrier)+"]");
                        Log.i("","\n"+"[onActivityResult rate : "+String.valueOf(rate)+"]");
                        Log.w("//===========//","================================================");
                        Log.i("---","---");
                        // -----------------------------------------

//                        Intent fragment_intent = new Intent();
//                        fragment_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    }
                }
            }
    );


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
    protected void onStart() {
        super.onStart();
//        setContentView(R.layout.activity_carrier_form);
        Log.e("MainActivity onStart", "ENTER");
    }


    @Override
    protected void onResume() {
        //다이얼로그 밖의 화면은 흐리게 만들어줌
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