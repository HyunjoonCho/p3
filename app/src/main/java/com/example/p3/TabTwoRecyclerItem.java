package com.example.p3;

import java.io.Serializable;

public class TabTwoRecyclerItem implements Comparable<TabTwoRecyclerItem>, Serializable {
    private String userid;
    private String imageid;
    private String image_name;
    private String image;
    private String timestamps;


    public TabTwoRecyclerItem(){
    }

    public TabTwoRecyclerItem(String userid, String imageid, String image_name, String image){
        this.userid = userid;
        this.imageid = imageid;
        this.image_name = image_name;
        this.image = image;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) { this.imageid = imageid; }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int compareTo(TabTwoRecyclerItem o) { return image_name.compareTo(o.getImage_name()); }
}
