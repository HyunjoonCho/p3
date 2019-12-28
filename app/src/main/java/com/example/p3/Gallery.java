package com.example.p3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import androidx.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;



public class Gallery extends AppCompatActivity {
    //리사이클러뷰의 구성요소 adapter, layoutmanager, viewholder
    RecyclerView mRecyclerView = null ;
    RecyclerAdapter mAdapter = null ;
    ArrayList<RecyclerItem> mList = new ArrayList<RecyclerItem>();


    public ArrayList<Bitmap> getUriArray(){
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.SIZE};
        Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();

        if(imageCursor.moveToFirst()){
            do {
                Uri photoUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageCursor.getString(0));
                //photoUri = MediaStore.setRequireOriginal(photoUri);

                Bitmap bitmap = null;
                try{
                    InputStream ims = getContentResolver().openInputStream(photoUri);
                    bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeStream(ims),125,128);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Drawable new_img = new BitmapDrawable(bitmap);
                addItem(new_img);

            }while (imageCursor.moveToNext());
        }
        return bitmapArrayList;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //super.oncreate는 이미 appcompatactivity에 있는 oncreate기능을 받아오고 거기에 추가하는 느낌.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);


        // 사진추가
        addItem(getDrawable(R.drawable.water));
        addItem(getDrawable(R.drawable.fall));
        addItem(getDrawable(R.drawable.image1));
        addItem(getDrawable(R.drawable.image2));
        addItem(getDrawable(R.drawable.image3));
        addItem(getDrawable(R.drawable.image4));
        addItem(getDrawable(R.drawable.image5));
        addItem(getDrawable(R.drawable.image6));
        addItem(getDrawable(R.drawable.image7));
        addItem(getDrawable(R.drawable.image8));
        addItem(getDrawable(R.drawable.image9));
        addItem(getDrawable(R.drawable.image10));
        addItem(getDrawable(R.drawable.image11));
        addItem(getDrawable(R.drawable.image12));
        addItem(getDrawable(R.drawable.image13));
        addItem(getDrawable(R.drawable.image14));
        addItem(getDrawable(R.drawable.image15));
        addItem(getDrawable(R.drawable.image16));
        addItem(getDrawable(R.drawable.image18));
        addItem(getDrawable(R.drawable.image19));
        addItem(getDrawable(R.drawable.image20));
        addItem(getDrawable(R.drawable.image23));
        addItem(getDrawable(R.drawable.image24));
        addItem(getDrawable(R.drawable.image25));

        getUriArray();

        mRecyclerView = findViewById(R.id.recycler1);
        mAdapter = new RecyclerAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.addItemDecoration(new MovieItemDecoration(this));
        mAdapter.notifyDataSetChanged();

        }



    public void addItem(Drawable icon){
        RecyclerItem item = new RecyclerItem();
        item.setIcon(icon);
        mList.add(item);
    }
}

