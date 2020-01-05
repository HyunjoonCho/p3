package com.example.p3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabOneSearchActivity extends AppCompatActivity {

    EditText editText;
    ImageButton closebtn;
    ImageButton backbtn;
    Intent mainintent;
    ArrayList<TabOneRecyclerItem> searchitems = new ArrayList<>();
    ArrayList<Integer> position_list = new ArrayList<>();
    TabOneSearchRecyclerAdapter myAdapter;
    private AlertDialog.Builder builder;
    private final static int UPDATE_CONTACT = 31;
    private final static int DELETE_RESULT_CODE = 40;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1_contacts_search);

        RecyclerView myRecycler = findViewById(R.id.tab1_search_recyclerview);
        backbtn = findViewById(R.id.tab1_search_back);
        closebtn = findViewById(R.id.tab1_search_close);
        editText = findViewById(R.id.tab1_search_edittxt);

        mainintent = getIntent();

        CustomLayoutManager myLayoutmgr = new CustomLayoutManager(this);
        myAdapter = new TabOneSearchRecyclerAdapter(searchitems);
        myAdapter.setOnItemClickListener(new TabOneSearchRecyclerAdapter.OnItemsearchClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), TabOneRecordActivity.class);
                intent.putExtra("recorditem",searchitems.get(position));
                intent.putExtra("position",position);
                startActivityForResult(intent,UPDATE_CONTACT);
            }
        });

        myAdapter.setOnListItemLongSelectedListener(new TabOneSearchRecyclerAdapter.OnListItemLongSelectedInterface() {
            @Override
            public void onItemLongSelected(View v, final int position) {
                builder = new AlertDialog.Builder(TabOneSearchActivity.this);       //Builder을 먼저 생성하여 옵션을 설정합니다.
                builder.setTitle("삭제");                                                                //타이틀을 지정합니다.
                builder.setMessage("확인을 누르시면 정보가 삭제됩니다.");

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {        //확인 버튼을 생성하고 클릭시 동작을 구현합니다.
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        NetworkHelper.getApiService().deleteContract(myAdapter.getItems().get(position).getContactid()).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.e("DELETE",response.body());
                                myAdapter.getItems().remove(position);
                                myAdapter.notifyDataSetChanged();
                                Intent delete_intent = new Intent();
                                delete_intent.putExtra("position",position_list.get(position));
                                setResult(DELETE_RESULT_CODE,delete_intent);
                                position_list.remove(position);
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.e("error",t.getMessage());
                            }
                        });
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {       //취소 버튼을 생성하고 클릭시 동작을 구현합니다.
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //donothing
                    }
                });
                builder.create().show();
            }
        });

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
        items = (ArrayList<TabOneRecyclerItem>) mainintent.getSerializableExtra("items");

        searchitems.clear();
        position_list.clear();

        if (charText.length() == 0) {
            myAdapter.notifyDataSetChanged();
            return;
        }

        for(int i = 0;i < items.size(); i++)
        {
            person_name = uh.splitHangeulToConsonant(items.get(i).getName());
            if (comparelist(search_name,person_name))
            {
                searchitems.add(items.get(i));
                position_list.add(i);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPDATE_CONTACT && resultCode == RESULT_OK && data != null){
            TabOneRecyclerItem update_item = (TabOneRecyclerItem)data.getSerializableExtra("updateditem");
            int pos = data.getIntExtra("position",1);
            myAdapter.getItems().get(pos).setName(update_item.getName());
            myAdapter.getItems().get(pos).setPhone_number(update_item.getPhone_number());
            myAdapter.notifyDataSetChanged();

            Intent mainforintent = new Intent();
            mainforintent.putExtra("updateditem",update_item);
            mainforintent.putExtra("position",position_list.get(pos));
            setResult(RESULT_OK,mainforintent);
        }
    }
}
