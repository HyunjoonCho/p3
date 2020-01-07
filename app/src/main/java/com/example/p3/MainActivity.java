package com.example.p3;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private long backPressedTime;
    private onBackPressedListener mOnBackPressedListener;

    String[] permission_list = {
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE
    };

    private final static int CREATE_NEW_CONTACT = 1;
    private final static int UPDATE_CONTACT = 2;
    private final static int UPDATE_CONTACT_FROM_RECORD = 20;
    private final static int DELETE_RESULT_CODE = 40;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private TabOneFragment tabOneFragment;
    private GalleryFragment tabTwoFragment;
    private TabThreeFragment tabThreeFragment = new TabThreeFragment();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabOneFragment = new TabOneFragment(getIntent().getStringExtra("id"));
        tabTwoFragment = new GalleryFragment(getIntent().getStringExtra("id"));

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
            tabOneFragment.getMyAdapter().getItems().get(pos).setPhone_number(update_item.getPhone_number());
            tabOneFragment.getMyAdapter().notifyDataSetChanged();
        }else if(requestCode == UPDATE_CONTACT_FROM_RECORD && resultCode == RESULT_OK && data != null){
            TabOneRecyclerItem update_item = (TabOneRecyclerItem)data.getSerializableExtra("updateditem");
            int pos = data.getIntExtra("position",1);
            tabOneFragment.getMyAdapter().getItems().get(pos).setName(update_item.getName());
            tabOneFragment.getMyAdapter().getItems().get(pos).setPhone_number(update_item.getPhone_number());
            tabOneFragment.getMyAdapter().notifyDataSetChanged();
        }else if(requestCode == UPDATE_CONTACT_FROM_RECORD && resultCode == DELETE_RESULT_CODE && data != null){
            int pos = data.getIntExtra("position",1);
            tabOneFragment.getMyAdapter().getItems().remove(pos);
            tabOneFragment.getMyAdapter().notifyDataSetChanged();
        }else if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                tabThreeFragment.getmAdapter().gettData().add((TabThreeItem)data.getSerializableExtra("item"));
                tabThreeFragment.getmAdapter().notifyDataSetChanged();
                tabThreeFragment.getmAdapter().save_list(getApplicationContext());
            }
        }
    }

    public interface onBackPressedListener {
        void onBack();
    }

    public void setOnBackPressedListener(@Nullable onBackPressedListener mListener) {
        mOnBackPressedListener = mListener;
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressedListener == null) {
            if (backPressedTime == (long)0) {
                Toast.makeText(this, "Exit when back pressed once more", Toast.LENGTH_SHORT).show();
                backPressedTime = System.currentTimeMillis();
            }
            else {
                long seconds = System.currentTimeMillis() - backPressedTime;

                if (seconds > (long) 2000 ) {
                    Toast.makeText(this, "Exit when back pressed once more", Toast.LENGTH_SHORT).show();
                    backPressedTime = System.currentTimeMillis();
                }
                else {
                    LoginManager.getInstance().logOut();
                    super.onBackPressed();
                }
            }

        }
        else {
            mOnBackPressedListener.onBack();
        }
    }
}
