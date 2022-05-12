package com.example.naver_map_test;

import android.Manifest;
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

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
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
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class fragment_home1 extends Fragment implements OnMapReadyCallback {

    //지도 제어를 위한 mapView 변수
    private MapView mapView;     // View를 사용하여 naver map을 출력했다면
    private static NaverMap naverMap;  // Fragment를 이용하여 naver map을 출력 했다면

    public static final String BASE_URL = "http://10.0.2.2:4000";

    Button conv;
    Button cafe;
    Button pizza;
    Button oil;

    // 임시로 넣어준 좌표
    Send_request send_request = new Send_request(36.833654477157914, 127.13431388502335);

    // 지도상에 현재 위치를 받아오는 변수
    private FusedLocationSource locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home1, container, false);
        conv = v.findViewById(R.id.conv);
        cafe = v.findViewById(R.id.cafe);
        pizza = v.findViewById(R.id.pizza);
        oil = v.findViewById(R.id.oil);


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

        double latitude = 36.833654477157914;
        double longitude = 127.13431388502335;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<DataModel_response> call = apiInterface.getData(latitude, longitude);

        Timer timer = new Timer("Timer");
        long delay = 30000L;
        long period = 10000L;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                call.clone().enqueue(new Callback<DataModel_response>() {
                    @Override
                    public void onResponse(@NonNull Call<DataModel_response> call, @NonNull Response<DataModel_response> response) {
                        if(response.isSuccessful()) {
                            DataModel_response result = response.body();
                            System.out.println("Response 성공!");
                            assert result != null;
                            System.out.println("Branch: " + Arrays.toString(result.Branch));
                            System.out.println("Location: " +Arrays.toString(result.Location));
                            System.out.println("Latitude: " +Arrays.toString(result.Latitude));
                            System.out.println("Longitude: " +Arrays.toString(result.Longitude));


                            Log.d("successful response", "onResponse 성공, 결과: " + result.toString());
                        } else {
                            Log.d("fail response", "onResponse 실패 : " + response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<DataModel_response> call, Throwable t) {
                        Log.d("fail response", "onFailure " + t.getMessage());
                    }
                });

            }
        };
        System.out.println(LocalDateTime.now() + "Scheduling....");
        timer.scheduleAtFixedRate(task, delay, period);

        conv.setOnClickListener(view -> {

        });

        cafe.setOnClickListener(view -> {

        });

        pizza.setOnClickListener(view -> {

        });
        oil.setOnClickListener(view -> {

        });

//        return inflater.inflate(R.layout.fragment_home1, container, false);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.map_search) {
            Toast.makeText(getContext(), "아직 구현되지 않은 기능입니다.", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        fragment_home1.naverMap = naverMap;

        naverMap.setCameraPosition(getCameraPosition(naverMap, 36.833654477157914, 127.13431388502335));

        setMarker(37.5670135, 126.9783740);
        setMarker(36.69051516, 126.577804);
        setMarker(36.82970416, 127.1839876);
        setMarker(35.56321329, 129.3332209);
        setMarker(36.833654477157914, 127.13431388502335);

        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true);

//        naverMap.setLocationSource(locationSource);

        ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS,LOCATION_PERMISSION_REQUEST_CODE);

        // 현재 위치 표시 설정
        setLocationMode(naverMap);

        // Map UI 설정 함수
        setMapUi(naverMap);

        // 오버레이 설정
//        setOverlay(naverMap);
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

//        try {
//            coordinate_timer coordinateTimer = new coordinate_timer();
//            // 데몬 스레드로 실행함으로 써 프로그램이 종료되면 타이머도 종료됨
//            Timer scheduler = new Timer(true);
//            coordinateTimer.run(naverMap);
//            long delay = 3000L;
//            long period = 1000L;
//            scheduler.scheduleAtFixedRate(coordinateTimer, delay, period);
//
//        } catch(Exception e) {
//            System.out.println("setLocation Error: " + e);
//
//        }
        naverMap.addOnLocationChangeListener(location -> {
            System.out.println("addOnLocationChangeListener" + ": " +location.getLatitude() + ", " + location.getLongitude());

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
        if(locationSource.onRequestPermissionsResult(requestCode, permissions, granResults)) {
            if(!locationSource.isActivated()) {  // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                return;
            } else {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, granResults);
    }

//    private final ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
//            new ActivityResultContracts.RequestPermission(),
//            new ActivityResultCallback<Boolean>() {
//                @Override
//                public void onActivityResult(Boolean result) {
//                    if(!result) {
//                        naverMap.setLocationTrackingMode(LocationTrackingMode.None);
//                        Log.e("naverMap TrackingMode None", "onActivityResult: PERMISSION DENIED");
//                    } else {
//                        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
//                        Log.e("naverMap TrackingMode Follow", "onActivityResult: PERMISSION GRANTED");
//                    }
//                }
//            }
//
//    );
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }
}