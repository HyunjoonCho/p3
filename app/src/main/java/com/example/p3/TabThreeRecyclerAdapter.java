package com.example.p3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TabThreeRecyclerAdapter extends RecyclerView.Adapter<TabThreeRecyclerAdapter.ViewHolder> {
    private ArrayList<TabThreeItem> tData;

    public TabThreeRecyclerAdapter(ArrayList<TabThreeItem> list){
        this.tData = list;
    }

    @Override
    public TabThreeRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.tab3_item,parent,false);
        TabThreeRecyclerAdapter.ViewHolder tvh = new TabThreeRecyclerAdapter.ViewHolder(view);

        return tvh;
    }

    @Override
    public void onBindViewHolder(TabThreeRecyclerAdapter.ViewHolder holder, int position){

        TabThreeItem item = tData.get(position);
        holder.tab3_text.setText(item.getWarning());
        holder.tab3_switch.setChecked(item.getIcon());
    }

    @Override
    public int getItemCount(){
        return tData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        Switch tab3_switch;
        TextView tab3_text;

        ViewHolder(View itemView){
            super(itemView);

            tab3_switch = itemView.findViewById(R.id.tab3_switch);
            tab3_text = itemView.findViewById(R.id.tab3_text);

        }
    }
}
