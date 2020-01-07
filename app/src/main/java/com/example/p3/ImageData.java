package com.example.p3;

public class ImageData {
    String userid;
    String imageid;
    String image;
    String imgName;
    String imgSize;

    ImageData() {}
    ImageData(String userid, String imageid, String imgPath, String imgName) {
        this.userid = userid;
        this.imageid = imageid;
        this.image = imgPath;
        this.imgName = imgName;
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

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgSize() {
        return imgSize;
    }

    public void setImgSize(String imgSize) {
        this.imgSize = imgSize;
    }
}
