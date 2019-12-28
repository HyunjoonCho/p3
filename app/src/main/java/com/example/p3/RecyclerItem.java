package com.example.p3;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerItem{
    private Drawable iconDrawable;

    public void setIcon(Drawable icon){
        iconDrawable = icon;
    }
    public Drawable getIcon(){
        return this.iconDrawable;
    }
}
