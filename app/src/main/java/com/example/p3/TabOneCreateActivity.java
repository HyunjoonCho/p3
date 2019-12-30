package com.example.p3;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class TabOneCreateActivity extends AppCompatActivity {

    EditText nametxt;
    EditText phonetxt;
    ImageView profile;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1_create_contact);
        alertbuilderset();

        profile = findViewById(R.id.tab1_create_profile);
        nametxt = findViewById(R.id.tab1_create_name);
        phonetxt = findViewById(R.id.tab1_create_phonenum);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.


        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.tab1_create_titlebar, null);

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        findViewById(R.id.tab1_create_title_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nametxt.getText().toString().trim().length() == 0 || phonetxt.getText().toString().trim().length() == 0)
                    finish();
                else
                    builder.create().show();
            }
        });

        findViewById(R.id.tab1_create_title_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nametxt.getText().toString().trim().length() == 0 || phonetxt.getText().toString().trim().length() == 0)
                    Toast.makeText(getApplicationContext(),"정보를 입력하세요",Toast.LENGTH_SHORT).show();
                else{
                    TabOneRecyclerItem item = new TabOneRecyclerItem();

                    item.setName(nametxt.getText().toString());
                    item.setPhonenum(phonetxt.getText().toString());
                    //카메라로 클릭한 후 uri 받아오기
                    //item.setProfile();
                    Intent intent = new Intent();
                    intent.putExtra("item",item);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
        return true;
    }

    private void alertbuilderset(){
        builder = new AlertDialog.Builder(TabOneCreateActivity.this);       //Builder을 먼저 생성하여 옵션을 설정합니다.
        builder.setTitle("저장하지않은 데이터");                                                                //타이틀을 지정합니다.
        builder.setMessage("확인을 누르시면 저장하지 않고 종료합니다.");

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {        //확인 버튼을 생성하고 클릭시 동작을 구현합니다.
            @Override

            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {       //취소 버튼을 생성하고 클릭시 동작을 구현합니다.
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //donothing
            }
        });
    }
}
