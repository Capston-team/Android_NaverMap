package com.example.naver_map_test;

import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomList extends BottomSheetDialogFragment {
    private ArrayList<StoreItem> mstoreItems;
    private ArrayList<StoreItem> sortedItems;
    int min = 999;
    int max=0;
    int minIndex, maxIndex;
    Spinner spinnerSort;
    String[] sortSel = {"할인율 순", "거리 순"};
    CustomAdapter mRecyclerAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("create bottom", "xxxxxxxxxxxxx");

        Bundle bundle = getArguments();
        List<ArrayList<String>> branch = new ArrayList<ArrayList<String>>();
        List<Integer> discount = new ArrayList<Integer>();
        List<String> branchName = new ArrayList<String>();
        List<ArrayList<Integer>> distance = new ArrayList<ArrayList<Integer>>();
        mstoreItems = new ArrayList<>();
        sortedItems = new ArrayList<>();


        if (bundle != null) {
            for (int i = 0; i < bundle.getInt("size"); i++) {
                branch.add(bundle.getStringArrayList("branch" + i));
                discount.add(bundle.getInt("discount" + i));
                branchName.add(bundle.getString("branchName" + i));
                distance.add(bundle.getIntegerArrayList("distance" + i));

                for (int j = 0; j < branch.get(i).size(); j++) {
                    try {
                        int img = setBranchImage(branchName.get(i));
//                        int img = R.drawable.cu;
                        mstoreItems.add(new StoreItem(img, branchName.get(i) + " "
                                + branch.get(i).get(j), discount.get(i),
                                distance.get(i).get(j)));
                    } catch (Exception e) {
                        Log.e("BottomList Error", e.toString());
                    }
                }
            }
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
        spinnerSort=view.findViewById(R.id.spinnerSort);

        /* initiate adapter */
        mRecyclerAdapter = new CustomAdapter(getActivity());

        /* initiate recyclerview */
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mRecyclerAdapter.setStoreList(mstoreItems);

        ArrayAdapter<String> adapterSort = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, sortSel
        );
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Adaper를 view에 연결
        for(StoreItem item : mstoreItems){
            Log.d("mstoreItems1", item.getDistance()+"");
        }
        spinnerSort.setAdapter(adapterSort);

        ArrayList<String> dist = new ArrayList<String>();
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortedItems = new ArrayList<>();
                int t=mstoreItems.size();
                if(i==1){
                    for(int k=0; k<t; k++){
                        minIndex=0;

                        min=mstoreItems.get(0).getDistance();
                        max=mstoreItems.get(0).getdiscount();
                        for(int j =0; j<mstoreItems.size(); j++) {
                            if (min > mstoreItems.get(j).getDistance()) {
                                min = mstoreItems.get(j).getDistance();
                                minIndex=j;
                            }
                            else if(min==mstoreItems.get(j).getDistance()){
                                if(max<mstoreItems.get(j).getdiscount()){
                                    max=mstoreItems.get(j).getdiscount();
                                    minIndex=j;
                                }
                            }

                        }
                        sortedItems.add(new StoreItem(mstoreItems.get(minIndex).getResourceId(), mstoreItems.get(minIndex).getTitle(),
                                mstoreItems.get(minIndex).getdiscount(), mstoreItems.get(minIndex).getDistance()));
                        mstoreItems.remove(minIndex);
                    }
                }
                else if(i==0){


                    for(int k=0; k<t; k++){
                        maxIndex=0;
                        min=mstoreItems.get(0).getDistance();
                        max=mstoreItems.get(0).getdiscount();
                        for(int j =0; j<mstoreItems.size(); j++) {

                            if (max < mstoreItems.get(j).getdiscount()) {
                                max = mstoreItems.get(j).getdiscount();
                                maxIndex=j;
                            }
                            else if(max==mstoreItems.get(j).getdiscount()){
                                if(min>mstoreItems.get(j).getDistance()){
                                    min=mstoreItems.get(j).getDistance();
                                    maxIndex=j;
                                }
                            }
                        }
                        Log.d("index", maxIndex+"@"+k);
                        sortedItems.add(new StoreItem(mstoreItems.get(maxIndex).getResourceId(), mstoreItems.get(maxIndex).getTitle(),
                                mstoreItems.get(maxIndex).getdiscount(), mstoreItems.get(maxIndex).getDistance()));
                        mstoreItems.remove(maxIndex);

                        for(StoreItem item : sortedItems){
                            Log.d("mstoreItemsSort", item.getDistance()+"");
                        }
                        Log.d("Sortsize", mstoreItems.size()+"@"+k);
                    }
                }
                else{}
                for(StoreItem item : sortedItems){
                    mstoreItems.add(new StoreItem(item.getResourceId(),item.getTitle(),item.getdiscount(),item.getDistance()));
                }

                Log.d("Sortfinalsize", mstoreItems.size()+"");
                mRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        return view;
    }
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return new BottomSheetDialog(requireContext(),R.style.MyTransparentBottomSheetDialogTheme);
//    }
}
