package com.example.p3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class TabThreePopupActivity extends Activity {

    private boolean isregistered;
    private Button register_btn;
    private int position;
    private ArrayList<String> namelist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab3_popup);

        register_btn = findViewById(R.id.tab3_popup_accept);
        isregistered = getIntent().getBooleanExtra("isregistered", false);
        position = getIntent().getIntExtra("position",0);
        namelist = getIntent().getStringArrayListExtra("namelist");

        if(isregistered)
            register_btn.setText("취소");
        else
            register_btn.setText("참가");

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //소켓
                Intent intent = new Intent();
                intent.putExtra("position",position);
                if(isregistered){
                    intent.putExtra("isregistered",false);
                }else{
                    intent.putExtra("isregistered",true);
                }
                Log.e("SET","intent넘김");
                setResult(RESULT_OK,intent);
                finish();
            }
        });


        RecyclerView myRecycler = findViewById(R.id.tab3_popup_recycler);
        TabThreePopupRecyclerAdapter myAdapter = new TabThreePopupRecyclerAdapter();
        myRecycler.setLayoutManager(new LinearLayoutManager(this));
        myRecycler.setAdapter(myAdapter);

        //소켓으로 방정보 불러와 이름 리스트 어뎁터로 넘기고 notify
        for(int i = 0; i < namelist.size(); i++)
            myAdapter.add(namelist.get(i));
        myAdapter.notifyDataSetChanged();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}
