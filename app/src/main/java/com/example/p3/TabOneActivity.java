package com.example.p3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;


public class TabOneActivity extends AppCompatActivity {

    private final static int CREATE_NEW_CONTACT = 1;

    TabOneRecyclerAdapter myAdapter;
    ArrayList<TabOneRecyclerItem> items = new ArrayList<>();
    CardView cv;

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

        myAdapter = new TabOneRecyclerAdapter(items);
        myRecycler.setLayoutManager(myLayoutmgr);
        myRecycler.setAdapter(myAdapter);

        cv = findViewById(R.id.tab1_cardview);

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TabOneSearchActivity.class);
                intent.putExtra("items",items);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(TabOneActivity.this,cv,"custom_transition3");
                startActivity(intent, options.toBundle());
            }
        });

        FloatingActionButton fabbtn = findViewById(R.id.tab1_fab);
        fabbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TabOneActivity.this, TabOneCreateActivity.class);
                startActivityForResult(intent, CREATE_NEW_CONTACT);
            }
        });
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
                //연락처 포토 가져오기
                //item.setProfile(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.baseline_person_black_36dp));
                items.add(item);
                Collections.sort(items);

            }
        }
        cursor.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CREATE_NEW_CONTACT){
            if(resultCode == RESULT_OK){
                items.add((TabOneRecyclerItem)data.getSerializableExtra("item"));
                myAdapter.notifyDataSetChanged();
            }
        }
    }
}
