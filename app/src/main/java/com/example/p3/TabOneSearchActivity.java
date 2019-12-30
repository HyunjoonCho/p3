package com.example.p3;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class TabOneSearchActivity extends AppCompatActivity {

    EditText editText;
    ImageButton closebtn;
    ImageButton backbtn;

    ArrayList<TabOneRecyclerItem> searchitems = new ArrayList<>();
    TabOneSearchRecyclerAdapter myAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1_contacts_search);

        RecyclerView myRecycler = findViewById(R.id.tab1_search_recyclerview);
        backbtn = findViewById(R.id.tab1_search_back);
        closebtn = findViewById(R.id.tab1_search_close);
        editText = findViewById(R.id.tab1_search_edittxt);


        CustomLayoutManager myLayoutmgr = new CustomLayoutManager(this);
        myAdapter = new TabOneSearchRecyclerAdapter(searchitems);
        myRecycler.setLayoutManager(myLayoutmgr);
        myRecycler.setAdapter(myAdapter);

        closebtn.setVisibility(View.INVISIBLE);
        closebtn.setEnabled(false);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edit_check();
                search(editText.getText().toString());
            }
        });

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                edit_check();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    public void search(String charText) {

        UnicodeHandler uh = new UnicodeHandler();
        ArrayList<Character> search_name = uh.splitHangeulToConsonant(charText);
        ArrayList<Character> person_name;

        ArrayList<TabOneRecyclerItem> items;
        Intent intent = getIntent();
        items = (ArrayList<TabOneRecyclerItem>) intent.getSerializableExtra("items");

        searchitems.clear();

        if (charText.length() == 0) {
            myAdapter.notifyDataSetChanged();
            return;
        }

        for(int i = 0;i < items.size(); i++)
        {
            person_name = uh.splitHangeulToConsonant(items.get(i).getName());
            if (comparelist(search_name,person_name))
            {
                // 검색된 데이터를 리스트에 추가한다.
                searchitems.add(items.get(i));
            }
        }
        myAdapter.notifyDataSetChanged();
    }


    private void edit_check() {
        if(editText.getText().toString().trim().length() == 0){
            closebtn.setVisibility(View.INVISIBLE);
            closebtn.setEnabled(false);
        }else{
            closebtn.setVisibility(View.VISIBLE);
            closebtn.setEnabled(true);
        }
    }

    public boolean comparelist(ArrayList<Character> search, ArrayList<Character> person){
        int search_len = search.size();
        int person_len = person.size();
        int index = 0;
        int i;

        if(search_len > person_len)
            return false;

        while(person_len - search_len >= index ){
            for (i = 0; i < search_len; i++) {
                if (!person.get(index + i).toString().equals(search.get(i).toString()))
                    break;
            }

            if(i == search_len)
                return true;

            index++;
        }
        return false;
    }
}
