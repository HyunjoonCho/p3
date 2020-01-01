package com.example.p3;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TabThreeLockScreenActivity extends AppCompatActivity {

    EditText editText;
    ArrayList<TabThreeItem> mList;
    ArrayList<TabThreeItem> selected_list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //setTheme(android.R.style.Theme_NoTitleBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab3_lockscreen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        //mList = (ArrayList<TabThreeItem>) getIntent().getSerializableExtra("mList");
        SharedPreferences sp = getSharedPreferences("TabThreeItemList", MODE_PRIVATE);
        String strContact = sp.getString("TabThreeItemList", "");
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<ArrayList<TabThreeItem>>() {}.getType();
        mList = gson.fromJson(strContact, listType);

        selected_list = getTrueItem(mList);


        TextView textView = findViewById(R.id.quiz);
        editText = findViewById(R.id.answer);
        final int r = (int)(Math.random()*selected_list.size());
        textView.setText(selected_list.get(r).getWarning());
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String answer_text = editText.getText().toString();
                //Toast.makeText(getApplicationContext(),answer_text + "\n"+quizes[r],Toast.LENGTH_SHORT).show();
                if(actionId == EditorInfo.IME_ACTION_DONE&& answer_text.equals(selected_list.get(r).getWarning()) ){
                    Toast.makeText(getApplicationContext(),"정답!",Toast.LENGTH_SHORT).show();
                    finish();
                    return true;
                }else{
                    Toast.makeText(getApplicationContext(),"다시 입력하세요.",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    public ArrayList<TabThreeItem> getTrueItem(ArrayList<TabThreeItem> original_list){
        ArrayList<TabThreeItem> selected_list = new ArrayList<>();
        int i;

        for(i = 0; i < original_list.size(); i++)
            if(original_list.get(i).getIcon())
                selected_list.add(original_list.get(i));
        return selected_list;
    }
}
