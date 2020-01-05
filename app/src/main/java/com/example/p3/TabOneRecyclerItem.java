package com.example.p3;

import android.graphics.Bitmap;

import java.io.Serializable;

public class TabOneRecyclerItem implements Comparable<TabOneRecyclerItem>, Serializable {
    private String userid;
    private String contactid;
    private String name;
    private String phone_number;
    private String profile_pic;
    private String timestamps;
    private int default_profile_color = 0;

    public TabOneRecyclerItem(){
    }

    public TabOneRecyclerItem(String userid, String contactid, String name, String phone_number, String profile_pic){
        this.userid = userid;
        this.contactid = contactid;
        this.name = name;
        this.phone_number = phone_number;
        this.profile_pic = profile_pic;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getContactid() {
        return contactid;
    }

    public void setContactid(String contactid) {
        this.contactid = contactid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public int getDefault_profile_color() {
        return default_profile_color;
    }

    public void setDefault_profile_color(int default_profile_color) {
        this.default_profile_color = default_profile_color;
    }

    @Override
    public int compareTo(TabOneRecyclerItem o) {
        return name.compareTo(o.getName());
    }
}
