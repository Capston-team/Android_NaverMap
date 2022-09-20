package com.example.naver_map_test;

import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        List<ArrayList<String>> branch = new ArrayList<ArrayList<String>>();
        List<Integer> discount = new ArrayList<Integer>();
        List<String> branchName = new ArrayList<String>();
        List<ArrayList<Integer>> distance = new ArrayList<ArrayList<Integer>>();
        mstoreItems = new ArrayList<>();


        if(bundle!=null){
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
                                + branch.get(i).get(j), "최대 " + discount.get(i).toString() + "% 할인",
                                "약 " + (distance.get(i).get(j)).toString() + "m"));
                    } catch(Exception e) {
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

        /* initiate adapter */
        CustomAdapter mRecyclerAdapter = new CustomAdapter(getActivity());

        /* initiate recyclerview */
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mRecyclerAdapter.setStoreList(mstoreItems);


        return view;
    }
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return new BottomSheetDialog(requireContext(),R.style.MyTransparentBottomSheetDialogTheme);
//    }
}
