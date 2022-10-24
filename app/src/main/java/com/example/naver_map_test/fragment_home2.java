package com.example.naver_map_test;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.naver.maps.map.MapFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 */
public class fragment_home2 extends Fragment {
    private RecyclerView eventRecyclerView;
    private Event_RecyclerViewAdapter adapter;
    private final ArrayList<eventItem> items = new ArrayList<>();

    private boolean isLoading = false;
    ProgressBar progressBar;

    String myTel;

    private int totalRetries = 3;
    private int retryCount = 0;
    private static final String TAG = fragment_home2.class.getSimpleName();


    Retrofit retrofit;
    public static final String BASE_URL = "https://where-we-go.herokuapp.com/capstone/event/";


    private eventDataModel_response dataModel_response = null;

    ArrayList<String> eventTitle = new ArrayList<>();
    ArrayList<String> eventDate = new ArrayList<>();
    ArrayList<String> eventImg = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("fragment2", "fragment2 onCreate");

        if(retrofit == null) {

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
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home2, container, false);
        Log.e(TAG, "fragment2 onCreateView");

        progressBar = new ProgressBar(requireContext());
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar.setCancelable(false);
        progressBar.show();

        String carrier = PreferenceUtil.getCarrierPreferences(requireContext(), "carrier");

        eventRecyclerView = v.findViewById(R.id.EventRecyclerView);
        initAdapter(eventRecyclerView);
        initScrollListener();

        getRetrofitResult(retrofit, progressBar, carrier);
        myTel = PreferenceUtil.getCarrierPreferences(requireContext(), "carrier");
        return v;
    }


    private void removeData(@NonNull ArrayList<eventItem> items) {
        for (int i = 0; i < items.size(); i++) {
            items.clear();
        }
    }

    private void populateData(@NonNull ArrayList<String> title, ArrayList<String> date, ArrayList<String> img) {
        for(int i = 0; i < title.size(); i++) {
            eventItem eventItem = new eventItem(title.get(i), date.get(i), img.get(i));
            items.add(eventItem);
        }
    }

    private void initAdapter(@NonNull RecyclerView eventRecyclerView) {
        Log.v("initAdapter", "initAdapter");
        adapter = new Event_RecyclerViewAdapter(items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        eventRecyclerView.setLayoutManager(layoutManager);
        eventRecyclerView.setAdapter(adapter);

    }

    private void initScrollListener() {
        Log.v("initScrollListener", "initScrollListener");
        eventRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if(!isLoading) {
                    if(layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == items.size() - 1) {
                        // 리스트 마지막
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        items.add(null);
        adapter.notifyItemInserted(items.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            items.remove(items.size() - 1);
            int scrollPosition = items.size();
            adapter.notifyItemRemoved(scrollPosition);
            int currentSize = scrollPosition;
            int nextLimit = currentSize + 10;

            // 이벤트 화면에서 맨 아래쪽으로 내려올 시 ArrayList의 items를 add 해줘야함
            while (currentSize - 1 < nextLimit) {
                currentSize++;
            }

            adapter.notifyDataSetChanged();
            isLoading = false;
        }, 2000);
    }

    public void getRetrofitResult(Retrofit retrofit, ProgressBar progressBar, String carrier) {
        try {
            APIInterface apiInterface = retrofit.create(APIInterface.class);
            Call<eventDataModel_response> event_call_request = apiInterface.event_request(carrier);
            event_call_request.clone().enqueue(new Callback<eventDataModel_response>() {
                @Override
                public void onResponse(@NonNull Call<eventDataModel_response> call, @NonNull Response<eventDataModel_response> response) {
                    if(response.isSuccessful()) {
                        if(response.code() == 200) {

                            dataModel_response = response.body();

                            assert dataModel_response != null;
                            eventTitle = (ArrayList<String>) dataModel_response.getTitle();
                            eventDate = (ArrayList<String>) dataModel_response.getDate();
                            eventImg = (ArrayList<String>) dataModel_response.getImg();

                            populateData(eventTitle, eventDate, eventImg);

                            assert dataModel_response != null;
                            System.out.println("eventTitle : " + eventTitle);
                            System.out.println("eventTitle.size() : " + eventTitle.size());
                            System.out.println("eventDate : " + eventDate);
                            System.out.println("eventDate.size() : " + eventDate.size());

                            adapter.notifyDataSetChanged();
                            progressBar.dismiss();
                        } else {
                            Log.e("fragment_home2 onResponse status Code", String.valueOf(response.code()));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<eventDataModel_response> call, Throwable t) {

                    Log.e(TAG, "onFailure -> " + t.getMessage());
                    if(retryCount++ < totalRetries) {
                        Log.v(TAG, "Retrying API Call - (" + retryCount + " / " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        // 네트워크 에러, retrofit 에러로 데이터 조회가 안될 경우 recycler view가 아닌 다른 viwe를 띄워줘야 함.

                    }
                }
            });
        } catch(Exception e) {
            Log.e("EVENT REST API ERROR", "EVENT Retrofit REST API ERROR : " + e);
        }
        Log.e("fragment2", "getRetrofitResult");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("fragment2", "onResume");
        System.out.println("onResume myTel : " + myTel);
        System.out.println("getCarrierPreferences : " + PreferenceUtil.getCarrierPreferences(getContext().getApplicationContext(), "carrier"));
        if(!myTel.equals(PreferenceUtil.getCarrierPreferences(getContext().getApplicationContext(), "carrier"))){
            progressBar.show();
            items.clear();
            myTel = PreferenceUtil.getCarrierPreferences(requireContext(), "carrier");
            getRetrofitResult(retrofit, progressBar, PreferenceUtil.getCarrierPreferences(getContext().getApplicationContext(), "carrier"));

        }
    }
}
