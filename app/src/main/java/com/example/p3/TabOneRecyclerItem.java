package com.example.p3;

import android.media.Image;

public class TabOneRecyclerItem implements Comparable<TabOneRecyclerItem>{
    private String name;
    private String phonenum;

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

    @Override
    public int compareTo(TabOneRecyclerItem o) {
        return name.compareTo(o.getName());
    }
}
