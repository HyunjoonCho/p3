package com.example.p3;

import android.graphics.Bitmap;

import java.io.Serializable;

public class TabOneRecyclerItem implements Comparable<TabOneRecyclerItem>, Serializable {
    private String name;
    private String phonenum;
    private String image_uri;
    private int changed = 0;

    public TabOneRecyclerItem(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.changed = 1;
        this.image_uri = image_uri;
    }

    public int getChanged() {
        return changed;
    }

    @Override
    public int compareTo(TabOneRecyclerItem o) {
        return name.compareTo(o.getName());
    }
}
