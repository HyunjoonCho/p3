package com.example.p3;

import android.graphics.Bitmap;

import java.io.Serializable;

public class TabOneRecyclerItem implements Comparable<TabOneRecyclerItem>, Serializable {
    private String name;
    private String phonenum;
    private byte[] profile;

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

    public byte[] getProfile() {
        return profile;
    }

    public void setProfile(byte[] profile) {
        this.profile = profile;
    }

    @Override
    public int compareTo(TabOneRecyclerItem o) {
        return name.compareTo(o.getName());
    }
}
