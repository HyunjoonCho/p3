package com.example.p3;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class TabOneFragment extends Fragment {

    private final static int CREATE_NEW_CONTACT = 1;
    private final static int UPDATE_CONTACT = 2;
    private final static int UPDATE_CONTACT_FROM_RECORD = 20;
    private final static int DELETE_RESULT_CODE = 40;


    TabOneRecyclerAdapter myAdapter;
    ArrayList<TabOneRecyclerItem> items = new ArrayList<>();
    CardView cv;
    private AlertDialog.Builder builder;

    public TabOneRecyclerAdapter getMyAdapter() {
        return myAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_main, container, false);

        getContacts();

        RecyclerView myRecycler =  rootView.findViewById(R.id.tab1_recyclerview);
        LinearLayoutManager myLayoutmgr = new LinearLayoutManager(getActivity());

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

        FloatingActionButton fabbtn = rootView.findViewById(R.id.tab1_fab);
        fabbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TabOneCreateActivity.class);
                getActivity().startActivityForResult(intent, CREATE_NEW_CONTACT);
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private String getJsonString()
    {
        String json = "";

        try {
            InputStream is = getActivity().getAssets().open("phone.json");
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


    public void getContacts(){
        items.clear();
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

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
}
