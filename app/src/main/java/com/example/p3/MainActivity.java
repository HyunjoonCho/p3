package com.example.p3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView = null ;
    RecyclerAdapter mAdapter = null ;
    ArrayList<RecyclerItem> mList = new ArrayList<RecyclerItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        addItem(getDrawable(R.drawable.water));
        addItem(getDrawable(R.drawable.fall));
        addItem(getDrawable(R.drawable.image1));
        addItem(getDrawable(R.drawable.image2));
        addItem(getDrawable(R.drawable.image3));


        mRecyclerView = findViewById(R.id.recycler1);
        mAdapter = new RecyclerAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.addItemDecoration(new MovieItemDecoration(this));
        mAdapter.notifyDataSetChanged();
    }

    public void addItem(Drawable icon){
        RecyclerItem item = new RecyclerItem();
        item.setIcon(icon);
        mList.add(item);
    }
}
