package com.example.p3;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private final static int CREATE_NEW_CONTACT = 1;
    private final static int UPDATE_CONTACT = 2;
    private final static int UPDATE_CONTACT_FROM_RECORD = 20;
    private final static int DELETE_RESULT_CODE = 40;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private TabOneFragment tabOneFragment = new TabOneFragment();
    private TabTwoFragment tabTwoFragment = new TabTwoFragment();
    private TabThreeFragment tabThreeFragment = new TabThreeFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.main_navigation_view);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame,tabOneFragment).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_menu1: {
                        transaction.replace(R.id.main_frame, tabOneFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu2: {
                        transaction.replace(R.id.main_frame, tabTwoFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu3: {
                        transaction.replace(R.id.main_frame, tabThreeFragment).commitAllowingStateLoss();
                        break;
                    }
                }

                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CREATE_NEW_CONTACT){
            if(resultCode == RESULT_OK){
                tabOneFragment.getMyAdapter().getItems().add((TabOneRecyclerItem)data.getSerializableExtra("item"));
                Collections.sort(tabOneFragment.getMyAdapter().getItems());
                tabOneFragment.getMyAdapter().notifyDataSetChanged();
            }
        }else if(requestCode == UPDATE_CONTACT && resultCode == RESULT_OK && data != null){
            TabOneRecyclerItem update_item = (TabOneRecyclerItem)data.getSerializableExtra("updateditem");
            int pos = data.getIntExtra("position",1);
            tabOneFragment.getMyAdapter().getItems().get(pos).setName(update_item.getName());
            tabOneFragment.getMyAdapter().getItems().get(pos).setPhonenum(update_item.getPhonenum());
            tabOneFragment.getMyAdapter().notifyDataSetChanged();
        }else if(requestCode == UPDATE_CONTACT_FROM_RECORD && resultCode == RESULT_OK && data != null){
            TabOneRecyclerItem update_item = (TabOneRecyclerItem)data.getSerializableExtra("updateditem");
            int pos = data.getIntExtra("position",1);
            tabOneFragment.getMyAdapter().getItems().get(pos).setName(update_item.getName());
            tabOneFragment.getMyAdapter().getItems().get(pos).setPhonenum(update_item.getPhonenum());
            tabOneFragment.getMyAdapter().notifyDataSetChanged();
        }else if(requestCode == UPDATE_CONTACT_FROM_RECORD && resultCode == DELETE_RESULT_CODE && data != null){
            int pos = data.getIntExtra("position",1);
            tabOneFragment.getMyAdapter().getItems().remove(pos);
            tabOneFragment.getMyAdapter().notifyDataSetChanged();
        }
    }
}
