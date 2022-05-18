package com.example.naver_map_test;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class fragment_home1 extends Fragment implements OnMapReadyCallback {

    //지도 제어를 위한 mapView 변수
    // private MapView mapView;     // View를 사용하여 naver map을 출력했다면
    private static NaverMap naverMap;  // Fragment를 이용하여 naver map을 출력 했다면

    Retrofit retrofit;

    public static final String BASE_URL = "http://10.0.2.2:4000";

    Button conv;
    Button cafe;
    Button meal;
    Button oil;

    // 현재 위도 경도 받아야함
    private double latitude;
    private  double longitude;

    // 지도상에 현재 위치를 받아오는 변수
    private FusedLocationSource locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    // onDestroyView()가 불리고 다시 Fragment가 보여진다면 불려지는 화면

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Fragment onCreateView", "fragment ENTER");
        View v = inflater.inflate(R.layout.fragment_home1, container, false);
        conv = v.findViewById(R.id.conv);
        cafe = v.findViewById(R.id.cafe);
        meal = v.findViewById(R.id.meal);
        oil = v.findViewById(R.id.oil);



        if(retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(5, TimeUnit.SECONDS)   // call 할 경우 연결되는 시간
                    .readTimeout(5, TimeUnit.SECONDS)   // 받은 데이터 읽는 역할
                    .writeTimeout(1, TimeUnit.SECONDS)   // 보내는 역할
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

        }


        // 편의점 버튼이 클릭되면 마커가 찍힌다. 색상별로
        conv.setOnClickListener(view -> {

            Send_request body_send_request = new Send_request(latitude, longitude, "CONV", "KT", "GOLD");

            try {

                APIInterface apiInterface = retrofit.create(APIInterface.class);
                Call<DataModel_response> call_request = apiInterface.call_request(body_send_request);


                call_request.clone().enqueue(new Callback<DataModel_response>() {
                    @Override
                    public void onResponse(@NonNull Call<DataModel_response> call, @NonNull Response<DataModel_response> response) {
                        if(response.isSuccessful()) {


                            Log.d("successful response", "onResponse 성공, 결과: ");
                        } else {
                            Log.e("fail response", "onResponse 실패 : " + response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DataModel_response> call, @NonNull Throwable t) {
                        Log.e("fail response", "onFailure ->" + t.getMessage());
                    }
                });
            } catch(Exception e) {
                Log.e("REST API ERROR", "Retrofit REST API ERROR : " + e);
            }

        });

        cafe.setOnClickListener(view -> {

            double latitude = 36.833654477157914;
            double longitude = 127.13431388502335;

            Send_request body_send_request = new Send_request(latitude, longitude, "CONV", "KT", "GOLD");

        });

        meal.setOnClickListener(view -> {

        });
        oil.setOnClickListener(view -> {

        });

//        return inflater.inflate(R.layout.fragment_home1, container, false);
        return v;
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        fragment_home1.naverMap = naverMap;
        Log.e("Fragment onMapReady", "MAP ENTER");

//        naverMap.setCameraPosition(getCameraPosition(naverMap, 36.833654477157914, 127.13431388502335));

        setMarker(36.69051516, 126.577804);
        setMarker(36.82970416, 127.1839876);
        setMarker(35.56321329, 129.3332209);
        setMarker(36.833654477157914, 127.13431388502335);

        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true);

        naverMap.setLocationSource(locationSource);

        // 현재 위치 표시 설정
        setLocationMode(naverMap);

        // Map UI 설정 함수
        setMapUi(naverMap);

        // 오버레이 설정
        setOverlay(naverMap);

    }

    public CameraPosition getCameraPosition(NaverMap naverMap, double latitude, double longitude) {
        // 시작시 지도 위치 설정
        return new CameraPosition(
                new LatLng(latitude, longitude),
                18
        );
    }

    public void setOverlay(NaverMap naverMap) {
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
    }

    public void setLocationMode(@NonNull NaverMap naverMap) {
        Log.e("setLocationMode", "setLocationMode ENTER");

        naverMap.addOnLocationChangeListener(location -> {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(location.getLatitude(), location.getLongitude()));
            naverMap.moveCamera(cameraUpdate);

            System.out.println("setLocationMode : " + latitude + " + " + longitude);
        });
    }

    public void setMapUi(@NonNull NaverMap naverMap) {
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(false);
        uiSettings.setScaleBarEnabled(false);
        uiSettings.setLocationButtonEnabled(true);

    }

    public void setMarker(double latitude, double longitude) {
        Marker marker = new Marker();
        marker.setPosition(new LatLng(latitude, longitude));
        // 아이콘 이미지 설정
//        marker.setIcon(OverlayImage.fromResource(R.drawable.ic_launcher_foreground));

        // 마커 사이즈 설정
        setMarkerSize(marker, 80, 100);
        marker.setMap(naverMap);
    }
    public void setMarkerSize(@NonNull Marker marker, int width, int height) {
        marker.setWidth(width);
        marker.setHeight(height);
    }

    /*
        현재 위치를 받아오기 위한 위치 권한 함수
     */
    @Override
    @SuppressWarnings("deprecation")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] granResults) {
//        Activty에서 권한요청
//        if(locationSource.onRequestPermissionsResult(requestCode, permissions, granResults)) {
//            if(!locationSource.isActivated()) {  // 권한 거부됨
//                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
//            } else {
//                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
//            }
//            return;
//        }

        if(locationSource.onRequestPermissionsResult(requestCode, permissions, granResults)) {
            if(!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, granResults);
    }

    // Fragment가 생성되고 최초로 실행되는 함수
    // 다시 불리지는 않음
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Fragment onCreate", "fragment ENTER");
        FragmentManager fm = getChildFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);

        if(mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        // onMapReady 함수를 인자로 callback함
        mapFragment.getMapAsync(this);

        // 현재 위치를 받아오는 함수
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        // 햄버거 메뉴
        setHasOptionsMenu(true);
    }
}