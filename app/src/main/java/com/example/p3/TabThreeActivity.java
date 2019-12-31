package com.example.p3;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class TabThreeActivity extends AppCompatActivity {

    Button lockbtn;
    Button unlockbtn;
    Intent service_intent = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab3_main);

        lockbtn = findViewById(R.id.tab3_lockbtn);
        unlockbtn = findViewById(R.id.tab3_unlockbtn);

        lockbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service_intent == null) {
                    service_intent = new Intent(getApplicationContext(), TabThreeScreenService.class);
                    if (Build.VERSION.SDK_INT >= 26)
                        startForegroundService(service_intent);
                    else
                        startService(service_intent);
                }
            }
        });

        unlockbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service_intent != null) {
                    service_intent.putExtra("turnoff", 2);
                    stopService(service_intent);
                }
            }
        });
    }
}
