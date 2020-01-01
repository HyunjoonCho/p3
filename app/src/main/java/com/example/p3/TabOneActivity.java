package com.example.p3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupMenu;

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
    private final static int UPDATE_CONTACT = 2;
    private final static int UPDATE_CONTACT_FROM_RECORD = 20;
    private final static int DELETE_RESULT_CODE = 40;

    TabOneRecyclerAdapter myAdapter;
    ArrayList<TabOneRecyclerItem> items = new ArrayList<>();
    CardView cv;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.tab1_main);

        //jsonParsing(getJsonString());
        getContacts();

        RecyclerView myRecycler = findViewById(R.id.tab1_recyclerview);
        CustomLayoutManager myLayoutmgr = new CustomLayoutManager(this);

        myAdapter = new TabOneRecyclerAdapter(items);

        myAdapter.setOnItemClickListener(new TabOneRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), TabOneRecordActivity.class);
                intent.putExtra("recorditem",items.get(position));
                intent.putExtra("position",position);
                startActivityForResult(intent, UPDATE_CONTACT);
            }
        });

        myAdapter.setOnListItemLongSelectedListener(new TabOneRecyclerAdapter.OnListItemLongSelectedInterface() {
            @Override
            public void onItemLongSelected(View v, final int position) {
                builder = new AlertDialog.Builder(TabOneActivity.this);       //Builder을 먼저 생성하여 옵션을 설정합니다.
                builder.setTitle("삭제");                                                                //타이틀을 지정합니다.
                builder.setMessage("확인을 누르시면 정보가 삭제됩니다.");

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {        //확인 버튼을 생성하고 클릭시 동작을 구현합니다.
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        myAdapter.getItems().remove(position);
                        myAdapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {       //취소 버튼을 생성하고 클릭시 동작을 구현합니다.
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //donothing
                    }
                });
                builder.create().show();
            }
        });

        myRecycler.setLayoutManager(myLayoutmgr);
        myRecycler.setAdapter(myAdapter);

        cv = findViewById(R.id.tab1_cardview);

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TabOneSearchActivity.class);
                intent.putExtra("items",items);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(TabOneActivity.this,cv,"custom_transition3");
                startActivityForResult(intent,UPDATE_CONTACT_FROM_RECORD,options.toBundle());
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
                Collections.sort(items);
                myAdapter.notifyDataSetChanged();
            }
        }else if(requestCode == UPDATE_CONTACT && resultCode == RESULT_OK && data != null){
            TabOneRecyclerItem update_item = (TabOneRecyclerItem)data.getSerializableExtra("updateditem");
            int pos = data.getIntExtra("position",1);
            myAdapter.getItems().get(pos).setName(update_item.getName());
            myAdapter.getItems().get(pos).setPhonenum(update_item.getPhonenum());
            myAdapter.notifyDataSetChanged();
        }else if(requestCode == UPDATE_CONTACT_FROM_RECORD && resultCode == RESULT_OK && data != null){
            TabOneRecyclerItem update_item = (TabOneRecyclerItem)data.getSerializableExtra("updateditem");
            int pos = data.getIntExtra("position",1);
            myAdapter.getItems().get(pos).setName(update_item.getName());
            myAdapter.getItems().get(pos).setPhonenum(update_item.getPhonenum());
            myAdapter.notifyDataSetChanged();
        }else if(requestCode == UPDATE_CONTACT_FROM_RECORD && resultCode == DELETE_RESULT_CODE && data != null){
            int pos = data.getIntExtra("position",1);
            myAdapter.getItems().remove(pos);
            myAdapter.notifyDataSetChanged();
        }
    }
}
