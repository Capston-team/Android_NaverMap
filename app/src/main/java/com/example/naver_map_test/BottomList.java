package com.example.naver_map_test;

import android.app.Dialog;
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

public class BottomList extends BottomSheetDialogFragment {
    private ArrayList<StoreItem> mfriendItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mfriendItems = new ArrayList<>();
        for(int i=1;i<=10;i++){
            mfriendItems.add(new StoreItem(R.drawable.marker_cafe,i+"번째 가게",i+"% 할인"));
        }


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


        mRecyclerAdapter.setStoreList(mfriendItems);


        return view;
    }
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return new BottomSheetDialog(requireContext(),R.style.MyTransparentBottomSheetDialogTheme);
//    }
}
