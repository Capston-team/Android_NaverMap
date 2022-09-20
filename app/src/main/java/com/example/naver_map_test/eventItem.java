package com.example.naver_map_test;


/**
 * 해당 이벤트의 url
 * 나중에 추가로 이벤트에 텍스트가 들어올 수 있음
 */
public class eventItem {

    private String url;

    public eventItem(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
