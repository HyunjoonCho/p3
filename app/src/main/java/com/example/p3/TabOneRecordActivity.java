package com.example.p3;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

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

        if(item.getProfile() != null)
            profile_img.setImageBitmap(BitmapFactory.decodeByteArray(item.getProfile(),0,item.getProfile().length));
        else
            profile_img.setImageResource(R.drawable.baseline_person_black_36dp);
        record_name.setText(item.getName());
        phonenum.setText(item.getPhonenum());

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
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.getPhonenum()));
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
            item.setPhonenum(update_item.getPhonenum());
            record_name.setText(update_item.getName());
            phonenum.setText(update_item.getPhonenum());
            setResult(RESULT_OK, data);
        }
    }
}
