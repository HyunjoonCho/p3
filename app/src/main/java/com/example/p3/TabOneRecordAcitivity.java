package com.example.p3;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class TabOneRecordAcitivity extends AppCompatActivity {

    private TextView record_name;
    private CircleImageView profile_img;
    public TabOneRecyclerItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1_record_contact);

        record_name = findViewById(R.id.tab1_record_name);
        profile_img = findViewById(R.id.tab1_record_profile);

        Intent intent = getIntent();
        item = (TabOneRecyclerItem)intent.getSerializableExtra("recorditem");

        if(item.getProfile() != null)
            profile_img.setImageBitmap(BitmapFactory.decodeByteArray(item.getProfile(),0,item.getProfile().length));
        else
            profile_img.setImageResource(R.drawable.baseline_person_black_36dp);
        record_name.setText(item.getName());

        findViewById(R.id.tab1_record_backbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.tab1_record_updatebtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        findViewById(R.id.tab1_record_msg_img1).setOnClickListener(new View.OnClickListener() {
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
}
