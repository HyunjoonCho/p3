package com.example.p3;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TabThreeRecyclerAdapter extends RecyclerView.Adapter<TabThreeRecyclerAdapter.ViewHolder> {
    private ArrayList<TabThreeItem> tData;
    private OnItemClickListener onItemClickListener;

    public TabThreeRecyclerAdapter(ArrayList<TabThreeItem> list){ this.tData = list; }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){ this.onItemClickListener = listener; }

    public ArrayList<TabThreeItem> gettData() {
        return tData;
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

        final int pos = position;
        TabThreeItem item = tData.get(position);
        holder.tab3_text.setText(item.getWarning());
        holder.tab3_switch.setChecked(item.getIcon());
        holder.tab3_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,pos);
            }
        });
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

    public boolean isAlloff(){
        int i;
        for(i = 0; i < tData.size(); i++)
            if(tData.get(i).getIcon())
                return false;
        return true;
    }

    public void save_list(Context mContext){
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<ArrayList<TabThreeItem>>() {}.getType();
        String mList_json = gson.toJson(tData,listType);
        SharedPreferences sp = mContext.getSharedPreferences("TabThreeItemList",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("TabThreeItemList", mList_json);
        editor.commit();
    }
}
