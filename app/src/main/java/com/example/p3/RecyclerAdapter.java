package com.example.p3;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.content.ComponentName;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<RecyclerItem> mData = null;
    public Context mcontext;

    RecyclerAdapter(ArrayList<RecyclerItem> list, Context context){
        this.mcontext = context;
        mData = list;
    }


    @NonNull //null을 허용하지 않는 경우
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        //inflater는 xml로 정의된 view를 실제 객체화 시키는 용도
        View view = inflater.inflate(R.layout.recycler_item, parent, false) ;
        RecyclerAdapter.ViewHolder vh = new RecyclerAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        // 이줄 근처에서 연동
        RecyclerItem item = mData.get(position) ;

        holder.icon.setImageDrawable(item.getIcon()) ;
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon ;

        ViewHolder(View itemView) {
            super(itemView) ;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(mcontext, SubActivity.class);
                        Drawable draw_1 = mData.get(pos).getIcon();
                        Bitmap bitmap = ((BitmapDrawable)draw_1).getBitmap();
                        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap,350,350,true);
                        intent.putExtra("image",bitmap1);

                        mcontext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        notifyItemChanged(pos);
                    }

                }

            });
            icon = itemView.findViewById(R.id.icon) ;
        }
    }
}



