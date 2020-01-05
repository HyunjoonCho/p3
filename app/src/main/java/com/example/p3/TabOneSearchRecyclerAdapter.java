package com.example.p3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class TabOneSearchRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Serializable {

    ArrayList<TabOneRecyclerItem> items;
    private OnItemsearchClickListener mListener = null;
    private OnListItemLongSelectedInterface mLongListener = null;
    private int profile_color_num = 8;

    public interface OnItemsearchClickListener{
        void OnItemClick(View v, int position);
    }

    public interface OnListItemLongSelectedInterface {
        void onItemLongSelected(View v, int position);
    }

    public void setOnItemClickListener(OnItemsearchClickListener listener){ this.mListener = listener; }
    public void setOnListItemLongSelectedListener(OnListItemLongSelectedInterface longSelectedListener) { this.mLongListener = longSelectedListener; }

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
        if(!items.get(position).getProfile_pic().equals("no_profile"))
            tab1viewholder.circleImageView.setImageBitmap(getBitmapFromString(items.get(position).getProfile_pic()));
        else
            tab1viewholder.circleImageView.setImageResource(return_profile_36(items.get(position).getDefault_profile_color()));

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

    public int random_profile_color(){
        return new Random().nextInt(profile_color_num);
    }

    public int return_profile_36(int num){
        switch (num){
            case 0:
                return R.drawable.person_icon_blue_36;
            case 1:
                return R.drawable.person_icon_grape_36;
            case 2:
                return R.drawable.person_icon_green_36;
            case 3:
                return R.drawable.person_icon_orange_36;
            case 4:
                return R.drawable.person_icon_pink_36;
            case 5:
                return R.drawable.person_icon_red_36;
            case 6:
                return R.drawable.person_icon_sky_36;
            case 7:
                return R.drawable.person_icon_yellow_36;
            default:
                return R.drawable.person_icon_blue_36;
        }
    }

    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
