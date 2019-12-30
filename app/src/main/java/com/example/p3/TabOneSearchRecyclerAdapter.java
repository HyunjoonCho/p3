package com.example.p3;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TabOneSearchRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<TabOneRecyclerItem> items;
    private OnItemsearchClickListener mListener = null;

    public interface OnItemsearchClickListener{
        void OnItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemsearchClickListener listener){
        this.mListener = listener;
    }

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


    public class TabOneRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        CircleImageView circleImageView;

        public TabOneRecyclerViewHolder(View itemView){
            super(itemView);
            tv_name = itemView.findViewById(R.id.tab1_item_name_write);
            circleImageView = itemView.findViewById(R.id.tab1_item_profile);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION)
                        if(mListener != null)
                            mListener.OnItemClick(v,pos);
                }
            });
        }
    }
}
