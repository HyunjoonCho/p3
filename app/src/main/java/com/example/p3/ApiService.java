package com.example.p3;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/contacts/userid/{userid}")
    Call<List<TabOneRecyclerItem>> getContactsByUserid(@Path("userid") String userid);
    @GET("/images/userid/{userid}")
    Call<List<TabTwoRecyclerItem>> getImagesByUserid(@Path("userid") String userid);

    @GET("/contacts/contactid/{contactid}")
    Call<TabOneRecyclerItem> getContactByContactid(@Path("contactid") String contactid);
    @GET("/images/imageid/{imageid}")
    Call<TabTwoRecyclerItem> getImageByContactid(@Path("imageid") String imageid);

    @FormUrlEncoded
    @POST("/contacts")
    Call<TabOneRecyclerItem> postContract(@Field("userid") String userid, @Field("contactid") String contactid, @Field("name") String name, @Field("phone_number") String phone_number, @Field("profile_pic") String profile_pic);
    @FormUrlEncoded
    @POST("/images")
    Call<TabTwoRecyclerItem> postImage(@Field("userid") String userid, @Field("imageid") String imageid, @Field("image_name") String iamge_name, @Field("image") String image);


    /*@POST("/contacts")
    Call<TabOneRecyclerItem> postContract(@Body TabOneRecyclerItem contact);*/

    @PUT("/contacts/contactid/{contactid}")
    Call<TabOneRecyclerItem> putContract(@Path("contactid") String contactid, @Body TabOneRecyclerItem contact);
    @PUT("/images/imageid/{imageid}")
    Call<TabTwoRecyclerItem> putImage(@Path("imageid") String imageid, @Body TabTwoRecyclerItem contact);


    @DELETE("/contacts/contactid/{contactid}")
    Call<String> deleteContract(@Path("contactid") String contactid);

    @GET("/gatherings")
    Call<List<TabThreeItem>> getGatherings();

    @FormUrlEncoded
    @POST("/gatherings")
    Call<TabThreeItem> postGatherings(@Field("userid") String userid, @Field("gatheringid") String gatheringid, @Field("destination") String destination, @Field("expireAt") String expireAt,@Field("departure") String departure, @Field("count") String count);

    @POST("/gatherings")
    Call<TabThreeItem> postGatherings(@Body TabThreeItem tabThreeItem);

    @DELETE("/gatherings/gatheringid/{gatheringid}")
    Call<String> deleteGatherings(@Path("gatheringid") String gatheringid);

    @PUT("/gatherings/gatheringid/{gatheringid}")
    Call<TabThreeItem> putGatherings(@Path("gatheringid") String gatheringid, @Body TabThreeItem item);

    @DELETE("/images/imageid/{imageid}")
    Call<String> deleteImage(@Path("imageid") String imageid);
}
