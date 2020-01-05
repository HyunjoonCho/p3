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

    @GET("/contacts/contactid/{contactid}")
    Call<TabOneRecyclerItem> getContactByContactid(@Path("contactid") String contactid);

    @FormUrlEncoded
    @POST("/contacts")
    Call<TabOneRecyclerItem> postContract(@Field("userid") String userid, @Field("contactid") String contactid, @Field("name") String name, @Field("phone_number") String phone_number, @Field("profile_pic") String profile_pic);


    /*@POST("/contacts")
    Call<TabOneRecyclerItem> postContract(@Body TabOneRecyclerItem contact);*/

    @PUT("/contacts/contactid/{contactid}")
    Call<TabOneRecyclerItem> putContract(@Path("contactid") String contactid, @Body TabOneRecyclerItem contact);

    @DELETE("/contacts/contactid/{contactid}")
    Call<String> deleteContract(@Path("contactid") String contactid);
}
