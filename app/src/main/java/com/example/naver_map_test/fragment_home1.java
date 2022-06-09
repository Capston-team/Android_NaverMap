package com.example.naver_map_test;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class fragment_home1 extends Fragment implements OnMapReadyCallback {

    //지도 제어를 위한 mapView 변수
    // private MapView mapView;     // View를 사용하여 naver map을 출력했다면
    private static NaverMap naverMap;  // Fragment를 이용하여 naver map을 출력 했다면
    private static final String FRAGMENT1 = "FRAGMENT_HOME1";

    Retrofit retrofit;

    public static final String BASE_URL = "http://10.0.2.2:4000";

    Button conv;
    Button cafe;
    Button meal;
    Button oil;

    // 현재 위도 경도 받아야함
    double latitude;
    double longitude;

    // 지도상에 현재 위치를 받아오는 변수
    private FusedLocationSource locationSource;

    // Response된 데이터 목록을 저장하는 ArrayList
    private ArrayList<DataModel_response> dataModel_responses = new ArrayList<>();

    // 지도에 찍혀있는 마커들을 지우기 위한 Vector
    public Vector<Marker> activeMarkers;

    // 마커 색상을 저장한 배열
    String[] markerColor = {"RED", "ORANGE", "YELLOW", "GREEN", "BLUE", "INDIGO", "VIOLET"};

    String carrier;
    String rate;

    // Fragment가 생성되고 최초로 실행되는 함수
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Fragment onCreate", "fragment ENTER");

        FragmentManager fm = getChildFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        // onMapReady 함수를 인자로 callback함
        mapFragment.getMapAsync(this);

        // 현재 위치를 받아오는 함수
        int LOCATION_PERMISSION_REQUEST_CODE = 1000;
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        // 햄버거 메뉴
        setHasOptionsMenu(true);
    }

    // onDestroyView()가 불리고 다시 Fragment가 보여진다면 불려지는 화면
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home1, container, false);
        conv = v.findViewById(R.id.conv);
        cafe = v.findViewById(R.id.cafe);
        meal = v.findViewById(R.id.meal);
        oil = v.findViewById(R.id.oil);

        Intent form_intent = new Intent(getActivity(), carrier_form.class);
        form_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        // 처음 통신사, 등급 입력 창 호출
        startActivityResult.launch(form_intent);


        Log.e("Fragment onCreateView", "fragment ENTER");

        /* --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------  */
        // 현재 위치를 찾아주는 함수의 권한을 부여하기 위한 코드
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
            // 권한 설정 check Listener
            PermissionListener permissionListener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    Log.i("Current Location Permission", "Current Location Granted");
                }

                @Override
                public void onPermissionDenied(List<String> deniedPermissions) {
                    Toast.makeText(requireActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                }
            };

            TedPermission.create()
                    .setPermissionListener(permissionListener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    .check();
        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 10, locationListener);
        // GPS_PROVIDER는 정확도가 높지만 야외에서만 가능
        // 실내에서는 NETWORK_PROVIDER를 사용하여 WIFI 같은 네트워크를 이용해 위치를 추정한다.
        Location loc_Current = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.d("Location", "loc_Current :  " + loc_Current);


        // LocationListener가 성공적으로 위치를 가져올 경우
        if(loc_Current != null) {
            latitude = loc_Current.getLatitude();
            longitude = loc_Current.getLongitude();
            Log.d("Test", "GPS Location changed, Latitude: "+ latitude + ", Longitude: " +longitude);
        } else {   // 만약 LocationListener가 위도, 경도를 가져오지 못할경우
            Log.e("getLastknownLocation", "getLastknownLocation is null");
        }
        /* --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------  */


        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
//             Retrofit 시간 설정
//            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()  // Retrofit 연결, 통신 시간이 오래 걸리므로 지연시간 부여
//                    .connectTimeout(1, TimeUnit.SECONDS)   // call 할 경우 연결되는 시간
//                    .readTimeout(4, TimeUnit.SECONDS)   // 받은 데이터 읽는 역할
//                    .writeTimeout(1, TimeUnit.SECONDS)   // 보내는 역할
//                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
//                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return v;
    }

    public void onHandlerResult(String carrier, String rate) {
        Log.i("---","---");
        Log.w("//===========//","================================================");
        Log.i("","\n"+"["+ carrier +" >> onHandlerResult :: carrier 확인]");
        Log.i("","\n"+"["+ rate +" >> onHandlerResult :: rate 확인]");
        Log.w("//===========//","================================================");
        Log.i("---","---");

        conv.setOnClickListener(view -> {
           Send_request sendRequest = new Send_request(latitude, longitude, "CONV", carrier, rate);
           setMarkerWithLocation(sendRequest);
        });

        cafe.setOnClickListener(view -> {
            Send_request sendRequest = new Send_request(latitude, longitude, "CAFE", carrier, rate);
            setMarkerWithLocation(sendRequest);
        });

        meal.setOnClickListener(view -> {
            Send_request sendRequest = new Send_request(latitude, longitude, "MEAL", carrier, rate);
            setMarkerWithLocation(sendRequest);
        });


    }

    //     통신사, 등급 입력 창 결과
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
//                    Log.i("---","---");
//                    Log.w("//===========//","================================================");
//                    Log.i("","\n"+"["+String.valueOf(FRAGMENT1)+" >> registerForActivityResult() :: 인텐트 결과 확인]");
//                    Log.i("","\n"+"[result :: [전체 데이터] :: "+String.valueOf(result)+"]");
//                    Log.w("//===========//","================================================");
//                    Log.i("---","---");
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        // -----------------------------------------
                        // [인텐트 데이터 얻어온다]
                        Intent intent = result.getData();
                        // ----------------------------------------------------------------------------------
                        // ----------------------------------------------------------------------------------
                        // [setResult 에서 응답 받은 데이터 확인 실시]
                        assert intent != null;
                        carrier = intent.getStringExtra("carrier");
                        rate = intent.getStringExtra("rate");

                        Log.i("---","---");
                        Log.w("//===========//","================================================");
                        Log.i("","\n"+"["+String.valueOf(FRAGMENT1)+" >> registerForActivityResult() :: 인텐트 응답 데이터 확인]");
                        Log.i("","\n"+"[onActivityResult carrier : "+String.valueOf(carrier)+"]");
                        Log.i("","\n"+"[onActivityResult rate : "+String.valueOf(rate)+"]");
                        Log.w("//===========//","================================================");
                        Log.i("---","---");
                        // ----------------------------------------------------------------------------------
                        onHandlerResult(carrier, rate);
                    }
                }
            }
    );


    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        fragment_home1.naverMap = naverMap;
        Log.e("Fragment onMapReady", "MAP ENTER");
//        LatLng initialPosition = new LatLng(latitude, longitude);
//        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(initialPosition);
//        naverMap.moveCamera(cameraUpdate);

        naverMap.setCameraPosition(getCameraPosition(latitude, longitude));

        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true);

        naverMap.setLocationSource(locationSource);

        // 현재 위치 표시 설정
        setLocationMode(naverMap);

        // Map UI 설정 함수
        setMapUi(naverMap);

        // 오버레이 설정
        setOverlay(naverMap);
    }


    public CameraPosition getCameraPosition(double latitude, double longitude) {
        // 시작시 지도 위치 설정
        return new CameraPosition(
                new LatLng(latitude, longitude),
                15
        );
    }

    public void setOverlay(NaverMap naverMap) {
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
    }

    public void setLocationMode(@NonNull NaverMap naverMap) {
//        Log.e("setLocationMode", "setLocationMode ENTER");

        naverMap.addOnLocationChangeListener(location -> {
//            카메라 고정
//            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(location.getLatitude(), location.getLongitude()));
//            naverMap.moveCamera(cameraUpdate);
        });
    }

    public void setMapUi(@NonNull NaverMap naverMap) {
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(false);
        uiSettings.setScaleBarEnabled(false);
        uiSettings.setLocationButtonEnabled(true);
    }


    /* --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------  */
    // 마커에 관련된 코드
    public void setMarkerWithLocation(Send_request send_request) {
        try {
            Instant start = Instant.now();
            APIInterface apiInterface = retrofit.create(APIInterface.class);
            Call<List<DataModel_response>> call_request = apiInterface.call_request(send_request);

            call_request.clone().enqueue(new Callback<List<DataModel_response>>() {
                @Override
                public void onResponse(@NonNull Call<List<DataModel_response>> call, @NonNull Response<List<DataModel_response>> response) {
                    if (response.isSuccessful()) {

                        checkRemoveMarker();

                        dataModel_responses = (ArrayList<DataModel_response>) response.body();
                        int before_discount_Rank = 0;
                        Log.i("---","---");
                        Log.w("//===========//","================================================");
                        Log.i("","\n"+"["+dataModel_responses.size()+" >> dataModel_responses Size :: dataModel_response 결과값 확인]");
                        Log.w("//===========//","================================================");
                        Log.i("---","---");

                        for(int i = 0; i < dataModel_responses.size(); i++) {
                            // 현재 해당하는 매장의 위도, 경도
                            List<Double> latitude = dataModel_responses.get(i).getLatitude();
                            List<Double> longitude = dataModel_responses.get(i).getLongitude();

                            // i == 0일 경우 전에 있는 할인율은 가져올 수 없기 때문에 if문으로 확인
                            if(i != 0) {
                                before_discount_Rank =  dataModel_responses.get(i - 1).getDiscountRate();
                            }

                            if(before_discount_Rank == dataModel_responses.get(i).getDiscountRate() && i != 0) {
                                setMarker(latitude, longitude, markerColor[i - 1], send_request.category);
                            } else {
                                setMarker(latitude, longitude, markerColor[i], send_request.category);
                            }
                        }

                        dataModel_responses.clear();
                        Log.d("successful response", "onResponse 성공");
                        Instant finish = Instant.now();
                        long elapsedTime = Duration.between(start, finish).toMillis();
                        System.out.println("elapsedTime(ms) : " + elapsedTime);
                    } else {
                        Log.e("fail response", "onResponse 실패 : " + response.body());
                    }
                }
                @Override
                public void onFailure(@NonNull Call<List<DataModel_response>> call, @NonNull Throwable t) {
                    Log.e("fail response", "onFailure ->" + t.getMessage());
                }

            });
        } catch (Exception e) {
            Log.e("REST API ERROR", "Retrofit REST API ERROR : " + e);
        }
    }

    public void setMarker(List<Double> latitude, List<Double> longitude, String color, String category) {
        Vector<LatLng> markersPosition = new Vector<>();
        for (int i = 0; i < latitude.size(); i++) {
            markersPosition.add(new LatLng(latitude.get(i), longitude.get(i)));
        }

        for(LatLng markerPosition : markersPosition) {
            switch (color) {
                case "RED":
                    Marker red_marker = new Marker();
                    red_marker.setPosition(markerPosition);
                    red_marker.setMap(naverMap);
                    red_marker.setIcon(MarkerIcons.BLACK);
                    red_marker.setIconTintColor(Color.rgb(255, 0, 51));
                    // 마커 사이즈 설정
                    setMarkerSize(red_marker, 80, 100);
                    activeMarkers.add(red_marker);
                    break;
                case "ORANGE":
                    Marker orange_marker = new Marker();
                    orange_marker.setPosition(markerPosition);
                    orange_marker.setMap(naverMap);
                    orange_marker.setIcon(MarkerIcons.BLACK);
                    orange_marker.setIconTintColor(Color.rgb(255, 153, 0));
                    // 마커 사이즈 설정
                    setMarkerSize(orange_marker, 80, 100);
                    activeMarkers.add(orange_marker);
                    break;
                case "YELLOW":
                    Marker yellow_marker = new Marker();
                    yellow_marker.setPosition(markerPosition);
                    yellow_marker.setMap(naverMap);
                    yellow_marker.setIcon(MarkerIcons.BLACK);
                    yellow_marker.setIconTintColor(Color.YELLOW);
                    // 마커 사이즈 설정
                    setMarkerSize(yellow_marker, 80, 100);
                    activeMarkers.add(yellow_marker);
                    break;
                default:
                    break;
            }
        }
        // 아이콘 이미지 설정
    }

    public void setMarkerSize(@NonNull Marker marker, int width, int height) {
        marker.setWidth(width);
        marker.setHeight(height);
    }

    private void checkRemoveMarker() {
        if (activeMarkers != null) {
            for (Marker activeMarker : activeMarkers) {
                activeMarker.setMap(null);
            }
        }
        activeMarkers = new Vector<Marker>();
    }
    /* --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------  */

    // 현재 위치를 받아오는 함수
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(@NonNull Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Log.d("Test", "GPS Location changed, Latitude: "+ latitude + ", Longitude: " +longitude);
        }
    };


    /*
      현재 위치를 받아오기 위한 위치 권한 함수
    */
    @Override
    @SuppressWarnings("deprecation")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] granResults) {

        if (locationSource.onRequestPermissionsResult(requestCode, permissions, granResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, granResults);
    }
}