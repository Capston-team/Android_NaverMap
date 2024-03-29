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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.util.InetAddressUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.CompassView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class fragment_home1 extends Fragment implements OnMapReadyCallback, Overlay.OnClickListener   {

    String myTel;
    private static NaverMap naverMap;  // Fragment를 이용하여 naver map을 출력 했다면
    private static final String FRAGMENT1 = "FRAGMENT_HOME1";

    Retrofit retrofit;

    //    public static final String BASE_URL = "http://10.0.2.2:4000";
    public static final String BASE_URL = "https://where-we-go.herokuapp.com/capstone/store-address/";

    Button conv;
    Button cafe;
    Button meal;

    LinearLayout btnList;
    CompassView compassView;

    ItemViewModel viewModel;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    final BottomList bottomSheetFragment = new BottomList();
    Bundle resData = new Bundle();
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
    String[] markerColor = {"RED", "ORANGE", "GREEN", "BLUE", "INDIGO", "VIOLET"};

    String carrier;
    String rate;
    String current_carrier;

    LocationManager lm;

    int selcategory;
    // Fragment가 생성되고 최초로 실행되는 함수
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("Fragment onCreate", "fragment ENTER");

        selcategory=0;
        // 현재 위치를 받아오는 함수
        int LOCATION_PERMISSION_REQUEST_CODE = 1000;
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        // 햄버거 메뉴
        setHasOptionsMenu(true);

        lm = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);


        int COARSE_PERMISSION = ContextCompat.checkSelfPermission(requireContext().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        int FINE_PERMISSION = ContextCompat.checkSelfPermission(requireContext().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
//        int CAMERA_PERMISSION = ContextCompat.checkSelfPermission(requireContext().getApplicationContext(), Manifest.permission.CAMERA);

        // 만약 3개의 권한 중 하나를 허용하지 않았다면 requestCode를 1000으로 할당
        if (COARSE_PERMISSION == PackageManager.PERMISSION_DENIED
                || FINE_PERMISSION == PackageManager.PERMISSION_DENIED) {
//                || CAMERA_PERMISSION == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]
                            {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
                    1000);
            Log.d("권한 결과", "아직 허용 안됨");
        } else {
            Log.d("권한 결과", "허용 됨");
            try {
                setLocation();
            } catch (Exception e) {
                Log.e("Unknown Error", "LocationManager Error");
            }
        }
    }

    public void updateCameraPosition(NaverMap naverMap, double latitude, double longitude) {
        if(naverMap!=null){
            LatLng initialPosition = new LatLng(latitude, longitude);
            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(initialPosition);
            naverMap.moveCamera(cameraUpdate);
            naverMap.setCameraPosition(getCameraPosition(latitude, longitude));
        }
    }
    // onDestroyView()가 불리고 다시 Fragment가 보여진다면 불려지는 화면
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Fragment onCreateView", "fragment1 onCreateView");
        FragmentManager fm = getChildFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);

        View v = inflater.inflate(R.layout.fragment_home1, container, false);



        btnList = v.findViewById((R.id.btnList));
        conv = v.findViewById(R.id.conv);
        cafe = v.findViewById(R.id.cafe);
        meal = v.findViewById(R.id.meal);
        compassView = v.findViewById(R.id.Compass);

        btnList.setVisibility(View.GONE);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        // onMapReady 함수를 인자로 callback함
        mapFragment.getMapAsync(this);

        btnList.setOnClickListener(view -> {
            bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
        });

        carrier = PreferenceUtil.getCarrierPreferences(requireActivity().getApplicationContext(), "carrier");
        rate = PreferenceUtil.getRatePreferences(requireActivity().getApplicationContext(), "rate");

        if(carrier != null && rate != null) {
            onHandlerResult();
        }
//        else {
//            Intent form_intent = new Intent(getActivity(), carrier_form.class);
//            form_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            // 처음 통신사, 등급 입력 창 호출
//            startActivityResult.launch(form_intent);
//        }

        if (retrofit == null) {
            try {
                String address = getIpAddress();
                Log.e("address", address);

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
//             Retrofit 시간 설정
//            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()  // Retrofit 연결, 통신 시간이 오래 걸리므로 지연시간 부여
//                    .connectTimeout(60, TimeUnit.SECONDS)   // call 할 경우 연결되는 시간
//                    .readTimeout(60, TimeUnit.SECONDS)   // 받은 데이터 읽는 역할
//                    .writeTimeout(60, TimeUnit.SECONDS)   // 보내는 역할
//                    .build();

                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
//                    .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
            } catch (SocketException e) {
                e.printStackTrace();
                Log.e("Socket Address Error", "서버가 닫혀 있습니다.");
            }
        }
        current_carrier = PreferenceUtil.getCarrierPreferences(requireContext(), "carrier");
        Log.e("Fragment onCreateView", "fragment ENTER");
        myTel = PreferenceUtil.getCarrierPreferences(requireContext(), "carrier");

        return v;
    }



    public void onHandlerResult() {
        conv.setOnClickListener(view -> {
            String carrier = PreferenceUtil.getCarrierPreferences(requireContext(), "carrier");
            String rate = PreferenceUtil.getRatePreferences(requireContext(), "rate");


            Log.d("onHandlerResult", "GPS Location changed, Latitude: "+ latitude + ", Longitude: " +longitude);

            selcategory=1;
            conv.setBackgroundResource(R.drawable.round_button_signiture);
            conv.setTextColor(Color.parseColor("#FFFFFF"));
            cafe.setBackgroundResource(R.drawable.round_button);
            cafe.setTextColor(Color.parseColor("#000000"));
            meal.setBackgroundResource(R.drawable.round_button);
            meal.setTextColor(Color.parseColor("#000000"));

            setMarkerWithLocation(latitude, longitude, "CONV", carrier, rate);
        });
        cafe.setOnClickListener(view -> {
            String carrier = PreferenceUtil.getCarrierPreferences(requireContext(), "carrier");
            String rate = PreferenceUtil.getRatePreferences(requireContext(), "rate");

            Log.d("onHandlerResult", "GPS Location changed, Latitude: "+ latitude + ", Longitude: " +longitude);

            selcategory=2;
            cafe.setBackgroundResource(R.drawable.round_button_signiture);
            cafe.setTextColor(Color.parseColor("#FFFFFF"));
            conv.setBackgroundResource(R.drawable.round_button);
            conv.setTextColor(Color.parseColor("#000000"));
            meal.setBackgroundResource(R.drawable.round_button);
            meal.setTextColor(Color.parseColor("#000000"));

            setMarkerWithLocation(latitude, longitude, "CAFE", carrier, rate);
        });
        meal.setOnClickListener(view -> {
            String carrier = PreferenceUtil.getCarrierPreferences(requireContext(), "carrier");
            String rate = PreferenceUtil.getRatePreferences(requireContext(), "rate");

            Log.d("onHandlerResult", "GPS Location changed, Latitude: "+ latitude + ", Longitude: " +longitude);
            selcategory=3;
            meal.setBackgroundResource(R.drawable.round_button_signiture);
            meal.setTextColor(Color.parseColor("#FFFFFF"));
            cafe.setBackgroundResource(R.drawable.round_button);
            cafe.setTextColor(Color.parseColor("#000000"));
            conv.setBackgroundResource(R.drawable.round_button);
            conv.setTextColor(Color.parseColor("#000000"));

            setMarkerWithLocation(latitude, longitude, "MEAL", carrier, rate);
        });
    }


    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        fragment_home1.naverMap = naverMap;
        Log.e("Fragment onMapReady", "MAP ENTER");

        // 지도의 레이아웃 설정
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true);

        updateCameraPosition(naverMap, latitude, longitude);

        // 위치 추적기능 설정
        naverMap.setLocationSource(locationSource);
        // 현재 위치 표시 설정
        setLocationMode(naverMap);

        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        // Map UI 설정 함수
        setMapUi(naverMap);

        // 오버레이 설정
        setOverlay(naverMap);


    }

    public void updateCamerePosition(NaverMap naverMap, double latitude, double longitude) {
        if(naverMap != null) {
            LatLng initialPosition = new LatLng(latitude, longitude);
            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(initialPosition);
            naverMap.moveCamera(cameraUpdate);
            naverMap.setCameraPosition(getCameraPosition(latitude, longitude));
        }
    }

    // 초기 지도 위도, 경도 초기화 함수
    public CameraPosition getCameraPosition(double latitude, double longitude) {
        // 시작시 지도 위치 설정
        return new CameraPosition(
                new LatLng(latitude, longitude),
                16
        );
    }

    // 지도 오버레이 설정
    public void setOverlay(NaverMap naverMap) {
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
    }

    // 이동 시 위도, 경도 설정 함수
    public void setLocationMode(@NonNull NaverMap naverMap) {
        naverMap.addOnLocationChangeListener(location -> {
//            카메라 고정
//            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(location.getLatitude(), location.getLongitude()));
//            naverMap.moveCamera(cameraUpdate);
        });
    }

    // 지도 UI 설정 함수
    public void setMapUi(@NonNull NaverMap naverMap) {
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(false);
        uiSettings.setScaleBarEnabled(false);
        uiSettings.setLocationButtonEnabled(true);
        uiSettings.setCompassEnabled(true);

        compassView.setMap(naverMap);
    }

    //거리 구하기
    public static int distanceInmeterByHaversine(double x1, double y1, double x2, double y2) {
        int distance;
        double radius = 6371; // 지구 반지름(km)
        double toRadian = Math.PI / 180;

        double deltaLatitude = Math.abs(x1 - x2) * toRadian;
        double deltaLongitude = Math.abs(y1 - y2) * toRadian;

        double sinDeltaLat = Math.sin(deltaLatitude / 2);
        double sinDeltaLng = Math.sin(deltaLongitude / 2);
        double squareRoot = Math.sqrt(
                sinDeltaLat * sinDeltaLat +
                        Math.cos(x1 * toRadian) * Math.cos(x2 * toRadian) * sinDeltaLng * sinDeltaLng);

        distance = (int)(2000 * radius * Math.asin(squareRoot));

        if(distance>500) distance =500;
        return distance;
    }

    /* --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------  */
    // retrofit을 통해 설정한 객체를 통한 마커 설정 함수
    public void setMarkerWithLocation(double latitude, double longitude, String category, String carrier, String rate) {
        try {
            Instant start = Instant.now();

            APIInterface apiInterface = retrofit.create(APIInterface.class);
            Call<List<DataModel_response>> call_request = apiInterface.call_request(latitude, longitude, category, carrier, rate);
            call_request.clone().enqueue(new Callback<List<DataModel_response>>() {
                @Override
                public void onResponse(@NonNull Call<List<DataModel_response>> call, @NonNull Response<List<DataModel_response>> response) {
                    if (response.isSuccessful()) {
                        if(response.code() == 200) {

                            checkRemoveMarker();

                            dataModel_responses = (ArrayList<DataModel_response>) response.body();

                            System.out.println("dataModel_response : " + dataModel_responses);

                            int before_discount_Rank = 0;
                            Log.i("---","---");
                            Log.w("//===========//","================================================");
                            Log.i("","\n"+"["+dataModel_responses.size()+" >> dataModel_responses Size :: dataModel_response 결과값 확인]");
                            Log.w("//===========//","================================================");
                            Log.i("---","---");

                            if(dataModel_responses.size() == 0) {
                                resData.putInt("size", 0);
                            }

                            for(int i = 0; i < dataModel_responses.size(); i++) {
                                ArrayList<Integer> dist = new ArrayList<Integer>();
                                // 현재 해당하는 매장의 위도, 경도
                                List<Double> _latitude = dataModel_responses.get(i).getLatitude();
                                List<Double> _longitude = dataModel_responses.get(i).getLongitude();

                                for (int j = 0; j < _latitude.size(); j++) {
                                    dist.add(distanceInmeterByHaversine(_latitude.get(j), _longitude.get(j), latitude, longitude));
                                }


                                Log.d("create bottom", "x" + dataModel_responses.size());
                                resData.putIntegerArrayList("distance" + i, dist);
                                resData.putInt("size", dataModel_responses.size());
                                resData.putStringArrayList("branch" + i, dataModel_responses.get(i).getBranch());
                                resData.putInt("discount" + i, dataModel_responses.get(i).getDiscountRate());
                                resData.putString("branchName" + i, dataModel_responses.get(i).getBranchName());

                                // i == 0일 경우 전에 있는 할인율은 가져올 수 없기 때문에 if문으로 확인
                                if (i != 0) {
                                    before_discount_Rank = dataModel_responses.get(i - 1).getDiscountRate();
                                }

                                if (before_discount_Rank == dataModel_responses.get(i).getDiscountRate() && i != 0) {
                                    setMarker(_latitude, _longitude, markerColor[i - 1], category, dataModel_responses.get(i).getBranchName(), dataModel_responses.get(i).getBranch());
                                } else {
                                    setMarker(_latitude, _longitude, markerColor[i], category, dataModel_responses.get(i).getBranchName(), dataModel_responses.get(i).getBranch());
                                }



                                //-----------
                            }
                            List<ArrayList<String>> branch = new ArrayList<ArrayList<String>>();
                            List<Integer> discount = new ArrayList<Integer>();
                            List<String> branchName = new ArrayList<String>();
                            List<ArrayList<Integer>> distance = new ArrayList<ArrayList<Integer>>();

                            ArrayList<StoreItem> changedStoreItems = new ArrayList<>();
                            if (resData != null) {
                                for (int i = 0; i < resData.getInt("size"); i++) {
                                    branch.add(resData.getStringArrayList("branch" + i));
                                    discount.add(resData.getInt("discount" + i));
                                    branchName.add(resData.getString("branchName" + i));
                                    distance.add(resData.getIntegerArrayList("distance" + i));

                                    for (int j = 0; j < branch.get(i).size(); j++) {
                                        try {
                                            int img = setBranchImage(branchName.get(i));
                                            changedStoreItems.add(new StoreItem(img, branchName.get(i) + " "
                                                    + branch.get(i).get(j), discount.get(i),
                                                    distance.get(i).get(j)));
                                        } catch (Exception e) {
                                            Log.e("BottomList Error", e.toString());
                                        }
                                    }
                                }
                            }
                            //-----------
                            viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
                            viewModel.changeSelectedItem(changedStoreItems);

                            bottomSheetFragment.setArguments(resData);

                            btnList.setVisibility(View.VISIBLE);
                            dataModel_responses.clear();
                            Log.d("successful response", "onResponse 성공");
                            Instant finish = Instant.now();
                            long elapsedTime = Duration.between(start, finish).toMillis();
                            System.out.println("setMarkerWithLocation Time(Ms) : " + elapsedTime);

                        } else {
                            Log.e("fragment_home1 onResponse status Code", String.valueOf(response.code()));
                        }
                    } else {
                        Log.e("fragment_home1 fail response", "onResponse 실패 : " + response.body());
                    }
                }
                @Override
                public void onFailure(@NonNull Call<List<DataModel_response>> call, @NonNull Throwable t) {
                    Log.e("fragment_home1 fail response", "onFailure ->" + t.getMessage());
                }

            });
        } catch (Exception e) {
            Log.e("MAP REST API ERROR", "MAP Retrofit REST API ERROR : " + e);
        }
    }
    public Integer setBranchImage(String br){
        int img=0;
        if(br.equals("CU")) img = R.drawable.cu;
        else if(br.equals("GS25")) img = R.drawable.gs25;
        else if(br.equals("SEVEN")) img = R.drawable.seven;
        else if(br.equals("PB")) img = R.drawable.pb;
        else if(br.equals("BASKIN31")) img = R.drawable.br;
        else img=R.drawable.marker_conv;

        return img;
    }
    // 색상에 따른 마커 설정 함수
    public void setMarker(@NonNull List<Double> latitude, List<Double> longitude, String color, String category, String branchName, ArrayList<String> branch) {
        // HashMap 생각 해볼것
        // <Key, Value> <markersInfo(List), markersPosition(List)>


        Vector<LatLng> markersPosition = new Vector<>();
        Vector<String> markersInfo = new Vector<>();

        int index =0;
        for (int i = 0; i < latitude.size(); i++) {
            markersPosition.add(new LatLng(latitude.get(i), longitude.get(i)));
            markersInfo.add(branchName +" "+branch.get(i));
        }
        Log.d("branchandName", branch + " @@ " + branchName);
        for(LatLng markerPosition : markersPosition) {
            Marker marker = new Marker();
            marker.setPosition(markerPosition);
            marker.setMap(naverMap);
            setMarkerSize(marker, 90, 120);
            marker.setTag(markersInfo.get(index));
            Log.d("category", category+"");
            switch(category){
                case "CONV":
                    marker.setIcon(OverlayImage.fromResource(R.drawable.conv_mark_icon));
                    break;
                case "CAFE":
                    marker.setIcon(OverlayImage.fromResource(R.drawable.coffee_mark_icon));
                    break;
                case "MEAL":
                    marker.setIcon(OverlayImage.fromResource(R.drawable.meal_mark_icon));
                    break;
                default:
                    break;
            }
            switch (color) {
                case "RED":
                    marker.setIconTintColor(Color.rgb(255, 0, 51));
                    break;
                case "ORANGE":
                    marker.setIconTintColor(Color.rgb(255, 153, 0));
                    break;
                case "GREEN":
                    marker.setIconTintColor(Color.rgb(162, 239, 68));
                    break;
                case "BLUE":
                    marker.setIconTintColor(Color.rgb(25, 50, 255));
                default:
                    break;
            }
            marker.setOnClickListener(this);
            activeMarkers.add(marker);
            index++;
        }
        // 아이콘 이미지 설정
    }
    @Override
    public boolean onClick(@NonNull Overlay overlay) {
        if (overlay instanceof Marker) {
            if(((Marker) overlay).getCaptionText().equals("")){
                ((Marker) overlay).setCaptionText(Objects.requireNonNull(overlay.getTag()).toString());
                setMarkerSize((Marker) overlay, 120, 160);
            }
            else{
                ((Marker) overlay).setCaptionText("");
                setMarkerSize((Marker) overlay, 90, 120);
            }
            return true;
        }
        return false;
    }
    // 마커 사이즈 설정 함수
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
        activeMarkers = new Vector<>();
    }

    /* --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------  */

    // 현재 위치를 받아오는 함수
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {


            if (ActivityCompat.checkSelfPermission(requireContext().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Log.d("requestLocationUpdates locationListener", "GPS Location changed, Latitude: "+ latitude + ", Longitude: " +longitude);


//                 GPS_PROVIDER는 정확도가 높지만 야외에서만 가능
//                 실내에서는 NETWORK_PROVIDER를 사용하여 WIFI 같은 네트워크를 이용해 위치를 추정한다.
            Location loc_Current = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(loc_Current == null) {
                loc_Current = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            Log.d("Location", "loc_Current :  " + loc_Current);
            // LocationListener가 성공적으로 위치를 가져올 경우
            if(loc_Current != null) {
                latitude = loc_Current.getLatitude();
                longitude = loc_Current.getLongitude();
                //updateCamerePosition(naverMap, latitude, longitude);
                Log.d("onRequestPermissionsResult", "GPS Location changed, Latitude: "+ latitude + ", Longitude: " +longitude);
            } else {   // 만약 LocationListener가 위도, 경도를 가져오지 못할경우
                Log.e("getLastknownLocation", "getLastknownLocation is null");
            }
            switch (selcategory){
                case 1:
                    setMarkerWithLocation(latitude,longitude, "CONV", carrier,rate);
                    Log.v("onLocationChanged", latitude+"/"+longitude+"/"+carrier+"/"+rate);
                    break;
                case 2:
                    setMarkerWithLocation(latitude,longitude, "CAFE", carrier,rate);
                    Log.v("onLocationChanged", "CAFE");
                    break;
                case 3:
                    setMarkerWithLocation(latitude,longitude, "MEAL", carrier,rate);
                    Log.v("onLocationChanged", "MEAL");
                    break;
            }

            //longitude = location.getLongitude();
            //latitude = location.getLatitude();
            // updateCameraPosition(naverMap, latitude ,longitude);

            Log.d("onLocationChanged", "GPS Location changed, Latitude: "+ latitude + ", Longitude: " +longitude);

        }
    };

    /* --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------  */

    /*
        권한 설정함수
        ACCESS_COARSE_LOCATION
        ACCESS_FINE_LOCATIONocationChanged: GPS Location
    */
    @Override
    @SuppressWarnings("deprecation")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {

        if (requestCode == 1000) {
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {  //grandResult == DENIED
                    check_result = false;
                    break;
                }
            }
            if (check_result) {
                // 허용했을 경우
                setLocation();
            } else {
                // 종료코드
                Toast.makeText(requireContext().getApplicationContext(), "권한 설정을 허용해야 위치를 가져올 수 있습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        // 위치 권한 설정이 안되어 있다면 오버레이를 표시하지 않음.
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grandResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grandResults);
    }


    public String getIpAddress() throws SocketException {
        for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
            NetworkInterface intf = en.nextElement();


            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                InetAddress inetAddress = enumIpAddr.nextElement();

                if(inetAddress.isLoopbackAddress()) {
                    Log.e("IPAddress", intf.getDisplayName() + "(loopback) | " + inetAddress.getHostAddress());
                } else {
                    Log.e("IPAddress", intf.getDisplayName() + " | " + inetAddress.getHostAddress());
                }
                if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
                    return "http://" + inetAddress.getHostAddress();
                }
            }
        }
        return null;
    }
    public void setLocation(){

        if (ActivityCompat.checkSelfPermission(requireContext().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Log.d("requestLocationUpdates locationListener", "GPS Location changed, Latitude: "+ latitude + ", Longitude: " +longitude);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 50, locationListener);

//                 GPS_PROVIDER는 정확도가 높지만 야외에서만 가능
//                 실내에서는 NETWORK_PROVIDER를 사용하여 WIFI 같은 네트워크를 이용해 위치를 추정한다.
        Location loc_Current = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(loc_Current == null) {
            loc_Current = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        Log.d("Location", "loc_Current :  " + loc_Current);
        // LocationListener가 성공적으로 위치를 가져올 경우
        if(loc_Current != null) {
            latitude = loc_Current.getLatitude();
            longitude = loc_Current.getLongitude();
            //updateCamerePosition(naverMap, latitude, longitude);
            Log.d("onRequestPermissionsResult", "GPS Location changed, Latitude: "+ latitude + ", Longitude: " +longitude);
        } else {   // 만약 LocationListener가 위도, 경도를 가져오지 못할경우
            Log.e("getLastknownLocation", "getLastknownLocation is null");
        }
    }
    /* --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------  */
    @Override
    public void onPause() {
        super.onPause();
        Log.e("Fragment onPause", "ENTER");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("Fragment onStop", "ENTER");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("Fragment onDestroyView", "ENTER");
    }
    @Override
    public void onResume() {
        super.onResume();
        if(!myTel.equals(PreferenceUtil.getCarrierPreferences(requireContext(), "carrier"))){
            checkRemoveMarker();
            selcategory=0;
            meal.setBackgroundResource(R.drawable.round_button);
            meal.setTextColor(Color.parseColor("#000000"));
            cafe.setBackgroundResource(R.drawable.round_button);
            cafe.setTextColor(Color.parseColor("#000000"));
            conv.setBackgroundResource(R.drawable.round_button);
            conv.setTextColor(Color.parseColor("#000000"));
            btnList.setVisibility(View.GONE);
            myTel = PreferenceUtil.getCarrierPreferences(requireContext(), "carrier");

        }
    }
}