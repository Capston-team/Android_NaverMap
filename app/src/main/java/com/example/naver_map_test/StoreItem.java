package com.example.naver_map_test;

public class StoreItem implements Comparable<StoreItem>{
    int resourceId;
    String title;
    int discount;
    int distance;

    @Override
    public int compareTo(StoreItem storeItem) {
        if ( this.getDistance() < storeItem.getDistance()){
            return -1;
        }
        return 0;
    }


    public StoreItem(int resourceId,String title, int discount, int distance) {
        this.title = title;
        this.discount= discount;
        this.distance=distance;
        this.resourceId=resourceId;
    }
    public int getResourceId() {
        return resourceId;
    }
    public String getTitle() {
        return title;
    }
    public int getdiscount() {
        return discount;
    }
    public int getDistance() {
        return distance;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setdiscount(int discount) {
        this.discount = discount;
    }
    public void setDistance(int distance) {
        this.distance = distance;
    }
}
