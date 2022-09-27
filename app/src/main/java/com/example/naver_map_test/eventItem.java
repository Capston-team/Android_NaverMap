package com.example.naver_map_test;


/**
 * 해당 이벤트의 url
 * 나중에 추가로 이벤트에 텍스트가 들어올 수 있음
 */
public class eventItem {

    private String title;
    private String date;
    private String img;

    public eventItem(String title, String date, String img) {
        this.title = title;
        this.date = date;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
