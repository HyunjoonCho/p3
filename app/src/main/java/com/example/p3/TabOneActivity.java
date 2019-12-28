package com.example.p3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class TabOneActivity extends AppCompatActivity {

    ArrayList<TabOneRecyclerItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.tab1_layout);

        //jsonParsing(getJsonString());
        getContacts();

        RecyclerView myRecycler = findViewById(R.id.tab1_recyclerview);
        CustomLayoutManager myLayoutmgr = new CustomLayoutManager(this);

        TabOneRecyclerAdapter myAdapter = new TabOneRecyclerAdapter(items);
        myRecycler.setLayoutManager(myLayoutmgr);
        myRecycler.setAdapter(myAdapter);
    }

    private String getJsonString()
    {
        String json = "";

        try {
            InputStream is = getAssets().open("phone.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    private void jsonParsing(String json)
    {
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray itemArray = jsonObject.getJSONArray("TabOneRecyclerItem");

            for(int i=0; i<itemArray.length(); i++)
            {
                JSONObject itemObject = itemArray.getJSONObject(i);

                TabOneRecyclerItem item = new TabOneRecyclerItem();

                item.setName(itemObject.getString("name"));
                item.setPhonenum(itemObject.getString("phonenum"));
                items.add(item);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();
    }


    public void getContacts(){

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        if(cursor != null){
            while (cursor.moveToNext()){
                TabOneRecyclerItem item = new TabOneRecyclerItem();
                item.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                item.setPhonenum(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                items.add(item);
                Collections.sort(items);

            }
        }
        cursor.close();
    }

    public static String cutString(String str, int len) {

        byte[] by = str.getBytes();
        int count = 0;
        try  {
            for(int i = 0; i < len; i++) {

                if((by[i] & 0x80) == 0x80) count++; // 핵심 코드

            }

            if((by[len - 1] & 0x80) == 0x80 && (count % 2) == 1) len--; // 핵심코드

            return new String(by, 0, len);

        }
        catch(java.lang.ArrayIndexOutOfBoundsException e)
        {
            System.out.println(e);
            return "";
        }

    }
}
