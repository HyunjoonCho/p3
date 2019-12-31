package com.example.p3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class TabThreeActivity extends AppCompatActivity {

    class SwitchListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                Intent intent = new Intent(getApplicationContext(), TabThreeScreenService.class);
                if (Build.VERSION.SDK_INT >= 26)
                    startForegroundService(intent);
                else
                    startService(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), TabThreeScreenService.class);
                stopService(intent);
            }

        }
    }
    RecyclerView mRecyclerView = null;
    TabThreeRecyclerAdapter mAdapter = null;
    ArrayList<TabThreeItem> mList = new ArrayList();
    public Switch main_switch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab3_main);
        //setTheme(android.R.style.Theme_NoTitleBar);

        addItem(false,"전여친한테 전화하지마!");
        addItem(false,"전남친한테 전화하지마!");

        mRecyclerView = findViewById(R.id.tab3_recycler);
        mAdapter = new TabThreeRecyclerAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        main_switch = findViewById(R.id.tab3_main_switch);
        main_switch.setOnCheckedChangeListener(new SwitchListener());

        mAdapter.notifyDataSetChanged();
    }
    public void addItem(Boolean b2, String text){
        TabThreeItem item = new TabThreeItem();
        item.setIcon(b2);
        item.setWarning(text);

        mList.add(item);
    }
}
