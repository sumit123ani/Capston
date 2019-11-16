package com.example.capston;

import android.widget.VideoView;

public class VideoList {

    String url1, url2;

    public VideoList(String url1, String url2)
    {
        this.url1 = url1;
        this.url2 = url2;
    }

    public String getUrl1() {
        return url1;
    }

    public String getUrl2() {
        return url2;
    }
}
