package com.example.p3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;


import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabThreeAddActivity extends AppCompatActivity {

    private Socket mSocket;
    private AlertDialog.Builder builder;
    private String facebook_id;
    private EditText dest;
    private EditText depart;
    private EditText hour;
    private EditText minute;
    private EditText count;
    private final static String TAG = "tab3addactivity";
    private boolean isregister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab3_add_gathering);
        alertbuilderset();

        isregister = getIntent().getBooleanExtra("isregistered",false);
        facebook_id = getIntent().getStringExtra("id");

        dest = findViewById(R.id.tab3_gathering_destination);
        depart = findViewById(R.id.tab3_gathering_departure);
        hour = findViewById(R.id.tab3_gathering_hour);
        minute = findViewById(R.id.tab3_gathering_minute);
        count = findViewById(R.id.tab3_gathering_count);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);


        final LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.tab3_add_title, null);

        actionBar.setCustomView(actionbar);

        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        findViewById(R.id.tab3_create_title_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dest.getText().toString().trim().length() == 0 || depart.getText().toString().trim().length() == 0 || hour.getText().toString().trim().length() == 0 || minute.getText().toString().trim().length() == 0 || count.getText().toString().trim().length() == 0)
                    finish();
                else
                    builder.create().show();
            }
        });

        findViewById(R.id.tab3_create_title_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isregister){
                    Toast.makeText(getApplicationContext(),"잡혀있는 약속을 먼저 취소하세요.",Toast.LENGTH_SHORT);
                }else {
                    if (dest.getText().toString().trim().length() == 0 || depart.getText().toString().trim().length() == 0 || hour.getText().toString().trim().length() == 0 || minute.getText().toString().trim().length() == 0 || count.getText().toString().trim().length() == 0)
                        Toast.makeText(getApplicationContext(), "정보를 입력하세요", Toast.LENGTH_SHORT).show();
                    else {
                        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                        time = time.substring(0, 8) + hour.getText().toString() + minute.getText().toString() + time.substring(12, 14);
                        final TabThreeItem item = new TabThreeItem(facebook_id, dest.getText().toString(), makeformatdate(time), depart.getText().toString(), count.getText().toString());
                        NetworkHelper.getApiService().postGatherings(item).enqueue(new Callback<TabThreeItem>() {

                            @Override
                            public void onResponse(Call<TabThreeItem> call, Response<TabThreeItem> response) {
                                Log.e("response of post",response.body().getUserid().get(0));

                                try {
                                    mSocket = IO.socket("http://192.249.19.251:8780");
                                    mSocket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                                        mSocket.emit("clientMessage","HI");
                                    }).on("serverMessage",(Object... objects) -> { //다른사람은 false로 하고 answer도 보내줘야함
                                        JsonParser jsonParsers = new JsonParser();
                                        JsonObject probleminfo = (JsonObject) jsonParsers.parse(objects[0] + "");
                                        Log.d("probleminfo", probleminfo.toString());
                                    });
                                    mSocket.connect();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                
                                setResult(RESULT_OK,new Intent());
                                finish();
                            }

                            @Override
                            public void onFailure(Call<TabThreeItem> call, Throwable t) {
                                Log.e("error",t.getMessage());
                            }
                        });
                    }
                }
            }
        });
        return true;
    }

    private void alertbuilderset(){
        builder = new AlertDialog.Builder(TabThreeAddActivity.this);       //Builder을 먼저 생성하여 옵션을 설정합니다.
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

    private String makeformatdate(String time){
        StringBuffer stringBuffer = new StringBuffer(time);
        stringBuffer.insert(4,"-");
        stringBuffer.insert(7,"-");
        stringBuffer.insert(10,"T");
        stringBuffer.insert(13,":");
        stringBuffer.insert(16,":");
        stringBuffer.append(".000Z");

        return stringBuffer.toString();
    }
}
