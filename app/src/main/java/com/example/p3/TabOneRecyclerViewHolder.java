package com.example.p3;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TabOneRecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView tv_name;

    public TabOneRecyclerViewHolder(View itemView){
        super(itemView);
        tv_name = itemView.findViewById(R.id.tab1_item_name_write);
    }
}
