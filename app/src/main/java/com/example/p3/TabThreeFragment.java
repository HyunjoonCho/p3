package com.example.p3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TabThreeFragment extends Fragment {

    Intent service_intent = null;
    RecyclerView mRecyclerView = null;
    TabThreeRecyclerAdapter mAdapter = null;
    ArrayList<TabThreeItem> mList = new ArrayList();
    public Switch main_switch;
    private boolean is_main_switch_on = false;
    private final static int REQUEST_TEST = 100;

    class SwitchListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(is_main_switch_on)
                service_intent = new Intent(getActivity(), TabThreeScreenService.class);
            main_swtich_save(isChecked);
            is_main_switch_on = isChecked;

            if (isChecked) {
                if(service_intent == null) {
                    service_intent = new Intent(getActivity(), TabThreeScreenService.class);
                    service_intent.putExtra("turnoff", 1);
                    if (Build.VERSION.SDK_INT >= 26)
                        getActivity().startForegroundService(service_intent);
                    else
                        getActivity().startService(service_intent);
                }
            } else {
                if(service_intent != null) {
                    service_intent.putExtra("turnoff", 2);
                    getActivity().stopService(service_intent);
                    service_intent = null;
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_main, container, false);
        //setTheme(android.R.style.Theme_NoTitleBar);

        main_switch = rootView.findViewById(R.id.tab3_main_switch);
        is_main_switch_on = main_switch_load();
        main_switch.setChecked(is_main_switch_on);

        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<ArrayList<TabThreeItem>>() {}.getType();
        SharedPreferences sp = getActivity().getSharedPreferences("TabThreeItemList", MODE_PRIVATE);
        String mList_json = sp.getString("TabThreeItemList", "");



        if(!mList_json.equals(""))
            mList = gson.fromJson(mList_json, listType);
        else {
            addItem(false, "전여친한테 전화하지마!");
            addItem(false, "Stay...");
            addItem(false, "이불킥 또 하게?");

            mList_json = gson.toJson(mList,listType);
            sp = getActivity().getSharedPreferences("TabThreeItemList",MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("TabThreeItemList", mList_json);
            editor.commit();
        }

        mRecyclerView = rootView.findViewById(R.id.tab3_recycler);
        mAdapter = new TabThreeRecyclerAdapter(mList);

        mAdapter.setOnItemClickListener(new TabThreeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mAdapter.gettData().get(position).setIcon(!mAdapter.gettData().get(position).getIcon());
                mAdapter.notifyDataSetChanged();
                if(mAdapter.isAlloff() && is_main_switch_on){
                    is_main_switch_on = false;
                    main_switch.setChecked(false);
                    main_swtich_save(false);
                }
                Gson gson = new GsonBuilder().create();
                Type listType = new TypeToken<ArrayList<TabThreeItem>>() {}.getType();
                String mList_json = gson.toJson(mAdapter.gettData(),listType);
                SharedPreferences sp = getActivity().getSharedPreferences("TabThreeItemList",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("TabThreeItemList", mList_json);
                editor.commit();
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.notifyDataSetChanged();

        main_switch.setOnCheckedChangeListener(new SwitchListener());

        FloatingActionButton fabbtn = rootView.findViewById(R.id.tab3_fab);
        fabbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TabThreeCreateActivity.class);
                getActivity().startActivityForResult(intent, REQUEST_TEST);
            }
        });

        return rootView;
    }

    public void addItem(Boolean b2, String text){
        TabThreeItem item = new TabThreeItem();
        item.setIcon(b2);
        item.setWarning(text);

        mList.add(item);
    }

    public void main_swtich_save(boolean ischecked){
        SharedPreferences sharePref = getActivity().getSharedPreferences("TabThreeMainSwitch",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePref.edit();
        editor.putBoolean("mainswitch",ischecked);
        editor.commit();
    }

    public boolean main_switch_load(){
        SharedPreferences sharePref = getActivity().getSharedPreferences("TabThreeMainSwitch",MODE_PRIVATE);
        return sharePref.getBoolean("mainswitch",false);
    }

    public TabThreeRecyclerAdapter getmAdapter() {
        return mAdapter;
    }
}
