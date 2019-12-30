package com.example.p3;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

public class TabOneSearchRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<TabOneRecyclerItem> items;

    public TabOneSearchRecyclerAdapter(ArrayList<TabOneRecyclerItem> items){
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab1_recyclerview_item,parent,false);
        return new TabOneRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TabOneRecyclerViewHolder tab1viewholder = (TabOneRecyclerViewHolder)holder;
        tab1viewholder.tv_name.setText(items.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}