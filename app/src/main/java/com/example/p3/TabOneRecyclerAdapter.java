package com.example.p3;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TabOneRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FastScrollRecyclerView.SectionedAdapter, Serializable {
    ArrayList<TabOneRecyclerItem> items;
    private OnItemClickListener mListener = null;
    private OnListItemLongSelectedInterface mLongListener = null;

    public interface OnItemClickListener {
        void OnItemClick(View v, int position);
    }

    public interface OnListItemLongSelectedInterface {
        void onItemLongSelected(View v, int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
    public void setOnListItemLongSelectedListener(OnListItemLongSelectedInterface longSelectedListener) { this.mLongListener = longSelectedListener; }

    public TabOneRecyclerAdapter(ArrayList<TabOneRecyclerItem> items){
        this.items = items;
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return items.get(position).getName().substring(0,1);
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
        if(items.get(position).getProfile() != null) {
            tab1viewholder.circleImageView.setImageBitmap(BitmapFactory.decodeByteArray(items.get(position).getProfile(),0,items.get(position).getProfile().length));
        }else
            tab1viewholder.circleImageView.setImageResource(R.drawable.baseline_person_black_36dp);
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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION)
                            if(mLongListener != null)
                                mLongListener.onItemLongSelected(v, pos);
                    return false;
                }
            });
        }
    }

    public ArrayList<TabOneRecyclerItem> getItems() {
        return items;
    }
}
