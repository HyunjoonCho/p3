package com.example.p3;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TabThreePopupRecyclerAdapter extends RecyclerView.Adapter<TabThreePopupRecyclerAdapter.ViewHolder>{

    private ArrayList<String> nameList = new ArrayList<>();


    @Override
    public TabThreePopupRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.tab3_popup_item,parent,false);
        TabThreePopupRecyclerAdapter.ViewHolder tvh = new TabThreePopupRecyclerAdapter.ViewHolder(view);

        return tvh;
    }

    @Override
    public void onBindViewHolder(TabThreePopupRecyclerAdapter.ViewHolder holder, int position){
        holder.popup_item_name.setText(nameList.get(position));
        if(position != 0)
            holder.popup_item_role.setText("참가자");
    }

    @Override
    public int getItemCount(){
        return nameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView popup_item_name;
        TextView popup_item_role;

        ViewHolder(View itemView){
            super(itemView);
            popup_item_name = itemView.findViewById(R.id.tab3_popup_item_name);
            popup_item_role = itemView.findViewById(R.id.tab3_popup_item_role);
        }
    }

    public void add(String name){
        nameList.add(name);
    }
}
