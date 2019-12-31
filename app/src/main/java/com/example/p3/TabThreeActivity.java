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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab3_main);

        lockbtn = findViewById(R.id.tab3_lockbtn);
        unlockbtn = findViewById(R.id.tab3_unlockbtn);

        lockbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TabThreeScreenService.class);
                if (Build.VERSION.SDK_INT >= 26)
                    startForegroundService(intent);
                else
                    startService(intent);
            }
        });

        unlockbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TabThreeScreenService.class);
                stopService(intent);
            }
        });
    }
}
