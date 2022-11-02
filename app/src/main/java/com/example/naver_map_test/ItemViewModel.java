package com.example.naver_map_test;

import android.content.ClipData;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ItemViewModel extends ViewModel{

    private final MutableLiveData<ArrayList<StoreItem>> selectedItem = new MutableLiveData<>();
    public void selectItem(ArrayList<StoreItem> item) {
        selectedItem.setValue(item);
    }
    public LiveData<ArrayList<StoreItem>> getSelectedItem() {
        return selectedItem;
    }
    public void changeSelectedItem(ArrayList<StoreItem> item){
        selectedItem.setValue(item);
    }

}