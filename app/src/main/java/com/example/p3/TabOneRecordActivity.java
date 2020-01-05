package com.example.p3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class TabOneRecordActivity extends AppCompatActivity {

    private TextView record_name;
    private TextView phonenum;
    private CircleImageView profile_img;
    public TabOneRecyclerItem item;
    private final static int UPDATE_CONTACT = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1_record_contact);

        record_name = findViewById(R.id.tab1_record_name);
        profile_img = findViewById(R.id.tab1_record_profile);
        phonenum = findViewById(R.id.tab1_record_phonenum);

        Intent intent = getIntent();
        item = (TabOneRecyclerItem)intent.getSerializableExtra("recorditem");
        final int pos = intent.getIntExtra("position",1);

        if(!item.getProfile_pic().equals("no_profile"))
            profile_img.setImageBitmap(getBitmapFromString(item.getProfile_pic()));
        else
            profile_img.setImageResource(return_profile_180(item.getDefault_profile_color()));
        record_name.setText(item.getName());
        phonenum.setText(item.getPhone_number());

        findViewById(R.id.tab1_record_backbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.tab1_record_updatebtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TabOneUpdateActivity.class);
                intent.putExtra("updateitem",item);
                intent.putExtra("position",pos);
                startActivityForResult(intent,UPDATE_CONTACT);
            }
        });


        findViewById(R.id.tab1_record_msg_img1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //문자메시지
            }
        });

        findViewById(R.id.tab1_record_msg_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //문자메시지
            }
        });

        findViewById(R.id.tab1_record_call_img1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.getPhone_number()));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == UPDATE_CONTACT && resultCode == RESULT_OK && data != null) {
            TabOneRecyclerItem update_item = (TabOneRecyclerItem)data.getSerializableExtra("updateditem");
            item.setName(update_item.getName());
            item.setPhone_number(update_item.getPhone_number());
            record_name.setText(update_item.getName());
            phonenum.setText(update_item.getPhone_number());
            setResult(RESULT_OK, data);
        }
    }

    public int return_profile_180(int num){
        switch (num){
            case 0:
                return R.drawable.person_icon_blue_180;
            case 1:
                return R.drawable.person_icon_grape_180;
            case 2:
                return R.drawable.person_icon_green_180;
            case 3:
                return R.drawable.person_icon_orange_180;
            case 4:
                return R.drawable.person_icon_pink_180;
            case 5:
                return R.drawable.person_icon_red_180;
            case 6:
                return R.drawable.person_icon_sky_180;
            case 7:
                return R.drawable.person_icon_yellow_180;
            default:
                return R.drawable.person_icon_blue_180;
        }
    }

    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
