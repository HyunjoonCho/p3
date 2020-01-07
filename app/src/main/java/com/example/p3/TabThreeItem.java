package com.example.p3;

import java.io.Serializable;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TabThreeItem implements Serializable {
    private List<String> userid = new ArrayList<>(); //개설자 아이디
    private String gatheringid;
    private String destination;
    private String expireAt;
    private String departure;
    private String status;
    private String count;

    public TabThreeItem(){
    }

    public TabThreeItem(List<String> userid, String gatheringid, String destination, String expireAt, String departure, String count) {
        for(int i = 0; i < userid.size(); i++)
            this.userid.add(userid.get(i));
        this.gatheringid = gatheringid;
        this.destination = destination;
        this.expireAt = expireAt;
        this.departure = departure;
        this.status = calstate(convertDatetoString(expireAt));
        this.count = count;
    }

    public TabThreeItem(String userid, String destination, String expireAt, String departure, String count) {
        this.userid.add(userid);
        this.gatheringid = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.destination = destination;
        this.expireAt = expireAt;
        this.departure = departure;
        this.status = calstate(convertDatetoString(expireAt));
        this.count = count;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<String> getUserid() {
        return userid;
    }

    public void setUserid(List<String> userid) {
        this.userid = userid;
    }

    public String getGatheringid() {
        return gatheringid;
    }

    public void setGatheringid(String gatheringid) {
        this.gatheringid = gatheringid;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(String expireAt) {
        this.expireAt = expireAt;
    }

    public String getStatus() {
        return this.status = calstate(convertDatetoString(expireAt));
    }

    public void updateStatus() {
        this.status = calstate(convertDatetoString(expireAt));
    }

    private String calstate(String exptime){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date exptime_date = dateFormat.parse(exptime);
            Date cur_date = dateFormat.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

            if(exptime_date.getTime() - cur_date.getTime() > 0 && exptime_date.getTime() - cur_date.getTime() < 3600000){
                return "임박";
            }else if(exptime_date.getTime() - cur_date.getTime() > 0){
                return "모집중";
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "마감";
    }

    private String convertDatetoString(String date){
        String str = "";
        str += date.substring(0,4);
        str += date.substring(5,7);
        str += date.substring(8,10);
        str += date.substring(11,13);
        str += date.substring(14,16);
        str += date.substring(17,19);

        return str;
    }
}
