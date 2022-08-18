package com.example.naver_map_test;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.naver.maps.map.MapFragment;

import java.util.ArrayList;


public class fragment_home2 extends Fragment {
    private RecyclerView eventRecyclerView;
    private Event_RecyclerViewAdapter adapter;
    private final ArrayList<eventItem> items = new ArrayList<>();

    private boolean isLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home2, container, false);
        Log.e("fragment2", "fragment2 onCreateView");

        eventRecyclerView = v.findViewById(R.id.EventRecyclerView);

        populateData();
        initAdapter(eventRecyclerView);
        initScrollListener();

        return v;
    }

    private void populateData() {
        eventItem eventItem1 = new eventItem("http://static.hubzum.zumst.com/hubzum/2018/02/06/09/962ec338ca3b4153b037168ec92756ac.jpg");
        eventItem eventItem2 = new eventItem("https://t1.daumcdn.net/cfile/tistory/0138F14A517F77713A");
        eventItem eventItem3 = new eventItem("https://i.ytimg.com/vi/5-mWvUR7_P0/maxresdefault.jpg");
        eventItem eventItem4 = new eventItem("https://www.tworld.co.kr/uploads/poc/85489/9318/7BE1D65F7E9B428FBFA8460306F64678.jpg");
        eventItem eventItem5 = new eventItem("https://www.tworld.co.kr/uploads/poc/85489/9318/290A0B1886AE4967830AC97C497C4446.jpg");
        eventItem eventItem6 = new eventItem("https://www.tworld.co.kr/uploads/poc/85489/9318/2D2A1075E04147E3BDCB627127F78F3D.jpg");
        eventItem eventItem7 = new eventItem("https://www.tworld.co.kr/uploads/poc/16972/57643/6B9184A9548E4B70AF5E168493848977.jpg");
        eventItem eventItem8 = new eventItem("https://www.tworld.co.kr/uploads/poc/4555/2692/CEF7C002795349BAAEA233045C1FC40D.jpg");

        items.add(eventItem1);
        items.add(eventItem2);
        items.add(eventItem3);
        items.add(eventItem4);
        items.add(eventItem5);
        items.add(eventItem6);
        items.add(eventItem7);
        items.add(eventItem8);


    }

    private void initAdapter(RecyclerView eventRecyclerView) {
        Log.e("initAdapter", "initAdapter");
        adapter = new Event_RecyclerViewAdapter(items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        eventRecyclerView.setLayoutManager(layoutManager);
        eventRecyclerView.setAdapter(adapter);

    }

    private void initScrollListener() {
        Log.e("initScrollListener", "initScrollListener");
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
}