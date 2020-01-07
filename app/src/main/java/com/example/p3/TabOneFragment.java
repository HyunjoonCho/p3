package com.example.p3;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.JsonWriter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabOneFragment extends Fragment {

    private final static int CREATE_NEW_CONTACT = 1;
    private final static int UPDATE_CONTACT = 2;
    private final static int UPDATE_CONTACT_FROM_RECORD = 20;
    private final static int DELETE_RESULT_CODE = 40;


    TabOneRecyclerAdapter myAdapter;
    ArrayList<TabOneRecyclerItem> items = new ArrayList<>();
    CardView cv;
    private AlertDialog.Builder builder;
    private boolean isdrawed = false;
    private String facebook_id;
    private int profile_color_num = 8;


    public TabOneFragment(String facebook_id){this.facebook_id = facebook_id; }
    public TabOneRecyclerAdapter getMyAdapter() {
        return myAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_main, container, false);

        getContactsFromDB();

        TextView contactsnum = rootView.findViewById(R.id.tab1_drawer_contactsnum);
        contactsnum.setText(Integer.toString(items.size()));

        RecyclerView myRecycler =  rootView.findViewById(R.id.tab1_recyclerview);
        final LinearLayoutManager myLayoutmgr = new LinearLayoutManager(getActivity());

        myAdapter = new TabOneRecyclerAdapter(items);

        myAdapter.setOnItemClickListener(new TabOneRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(), TabOneRecordActivity.class);
                intent.putExtra("recorditem",items.get(position));
                intent.putExtra("position",position);
                getActivity().startActivityForResult(intent, UPDATE_CONTACT);
            }
        });

        myAdapter.setOnListItemLongSelectedListener(new TabOneRecyclerAdapter.OnListItemLongSelectedInterface() {
            @Override
            public void onItemLongSelected(View v, final int position) {
                builder = new AlertDialog.Builder(getActivity());       //Builder을 먼저 생성하여 옵션을 설정합니다.
                builder.setTitle("삭제");                                                                //타이틀을 지정합니다.
                builder.setMessage("확인을 누르시면 정보가 삭제됩니다.");

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {        //확인 버튼을 생성하고 클릭시 동작을 구현합니다.
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        NetworkHelper.getApiService().deleteContract(myAdapter.getItems().get(position).getContactid()).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.e("DELETE",response.body());
                                myAdapter.getItems().remove(position);
                                myAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.e("error",t.getMessage());
                            }
                        });
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

        myRecycler.setHasFixedSize(true);
        myRecycler.setLayoutManager(myLayoutmgr);
        myRecycler.setAdapter(myAdapter);

        cv = rootView.findViewById(R.id.tab1_cardview);

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TabOneSearchActivity.class);
                intent.putExtra("items",items);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),cv,"custom_transition3");
                getActivity().startActivityForResult(intent,UPDATE_CONTACT_FROM_RECORD,options.toBundle());
            }
        });


        ImageButton select_option_btn = rootView.findViewById(R.id.tab1_title_img2);
        select_option_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(),v);

                getActivity().getMenuInflater().inflate(R.menu.tab1_main_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.tab1_pop_select_one:
                                break;
                            case R.id.tab1_pop_select_all:
                                break;
                            case R.id.tab1_pop_setting:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        FloatingActionButton fabbtn = rootView.findViewById(R.id.tab1_fab);
        fabbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TabOneCreateActivity.class);
                intent.putExtra("id",facebook_id);
                getActivity().startActivityForResult(intent, CREATE_NEW_CONTACT);
            }
        });

        final DrawerLayout drawerLayout = rootView.findViewById(R.id.tab1_drawerlayout);
        final View drawerView = rootView.findViewById(R.id.tab1_drawer_item_layout);

        rootView.findViewById(R.id.tab1_drawer_contactslayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(drawerView);
            }
        });

        rootView.findViewById(R.id.tab1_drawer_settinglayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //미구현
            }
        });

        ImageButton drawerbtn = rootView.findViewById(R.id.tab1_title_drawerbtn);
        drawerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        return rootView;
    }


    public void getContactsFromDB(){
        items.clear();
        NetworkHelper.getApiService().getContactsByUserid(facebook_id).enqueue(new Callback<List<TabOneRecyclerItem>>() {
            @Override
            public void onResponse(Call<List<TabOneRecyclerItem>> call, Response<List<TabOneRecyclerItem>> response) {
                try{
                    List<TabOneRecyclerItem> contact = response.body();
                    if(contact != null && contact.size() != 0) {
                        for (int i = 0; i < contact.size(); i++) {
                            TabOneRecyclerItem item = new TabOneRecyclerItem(contact.get(i).getUserid(),contact.get(i).getContactid(),contact.get(i).getName(),contact.get(i).getPhone_number(),contact.get(i).getProfile_pic());
                            item.setDefault_profile_color(random_profile_color());
                            items.add(item);
                            Log.e("name : ",contact.get(i).getName());
                        }
                        Collections.sort(items);
                        myAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<TabOneRecyclerItem>> call, Throwable t) {
                Log.v("error",t.getMessage());
            }
        });
    }

    public void getContactsFromPhone(){
        items.clear();
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                final TabOneRecyclerItem item = new TabOneRecyclerItem(facebook_id,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
                        "no_profile");
                item.setDefault_profile_color(random_profile_color());
                //연락처 포토 가져오기
                //item.setProfile(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.baseline_person_black_36dp));
                items.add(item);
            }
            Collections.sort(items);
        }
        cursor.close();
    }

    public int random_profile_color(){
        return new Random().nextInt(profile_color_num);
    }

    /*public void checkPermission(){
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = getActivity().checkCallingOrSelfPermission(permission);

            if(chk == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list,0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0)
        {
            for(int i=0; i<grantResults.length; i++)
            {
                if(grantResults[i]!= PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(),"앱권한설정하세요", Toast.LENGTH_LONG).show();
                }
            }
        }
    }*/

    public void makejson(ArrayList<TabOneRecyclerItem> list){
        JSONObject object = new JSONObject();
        try{
            JSONArray jArray = new JSONArray();//배열이 필요할때
            for (int i = 0; i < list.size(); i++)//배열
            {
                TabOneRecyclerItem item = list.get(i);
                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                sObject.put("userid", item.getUserid());
                sObject.put("contactid", item.getContactid());
                sObject.put("name", item.getName());
                sObject.put("phone_number", item.getPhone_number());
                sObject.put("profile_pic", item.getProfile_pic());
                jArray.put(sObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
