package com.example.p3;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;

import java.security.cert.PKIXRevocationChecker;
import java.util.ArrayList;

public class SubActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //super.oncreate는 이미 appcompatactivity에 있는 oncreate기능을 받아오고 거기에 추가하는 느낌.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subactivity);

        ImageView imageView = findViewById(R.id.imageView);
        Intent intent = getIntent();
        Bitmap bitmap = intent.getParcelableExtra("image");

        imageView.setImageBitmap(bitmap);
    }
}