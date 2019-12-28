package com.example.p3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import java.util.ArrayList;


public class Gallery extends AppCompatActivity {
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
        addItem(getDrawable(R.drawable.image4));
        addItem(getDrawable(R.drawable.image5));
        addItem(getDrawable(R.drawable.image6));
        addItem(getDrawable(R.drawable.image7));
        addItem(getDrawable(R.drawable.image8));
        addItem(getDrawable(R.drawable.image9));
        addItem(getDrawable(R.drawable.image10));
        addItem(getDrawable(R.drawable.image11));
        addItem(getDrawable(R.drawable.image12));
        addItem(getDrawable(R.drawable.image13));
        addItem(getDrawable(R.drawable.image14));
        addItem(getDrawable(R.drawable.image15));
        addItem(getDrawable(R.drawable.image16));
        addItem(getDrawable(R.drawable.image18));
        addItem(getDrawable(R.drawable.image19));
        addItem(getDrawable(R.drawable.image20));
        addItem(getDrawable(R.drawable.image23));
        addItem(getDrawable(R.drawable.image24));
        addItem(getDrawable(R.drawable.image25));


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
