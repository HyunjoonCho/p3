package com.example.p3;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TabTwoFragment extends Fragment {

    RecyclerView mRecyclerView = null ;
    RecyclerAdapter mAdapter = null ;
    ArrayList<RecyclerItem> mList = new ArrayList();

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }



    public void getUriArray() {
        Uri uri_1 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media._ID};
        Cursor imageCursor = getActivity().getContentResolver().query(uri_1, projection, null, null, null);

        if(imageCursor.moveToFirst()){
            do {
                Uri photoUri = Uri.withAppendedPath(uri_1, imageCursor.getString(0));
                //photoUri = MediaStore.setRequireOriginal(photoUri);

                Bitmap bitmap = null;
                try{
                    //InputStream ims = getContentResolver().openInputStream(photoUri);
                    //bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeStream(ims),125,128);
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),photoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(photoUri.toString());
                }catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = 6;
                bitmap = rotateBitmap(bitmap, orientation);
                Drawable new_img = new BitmapDrawable(bitmap);
                addItem(new_img);

            }while (imageCursor.moveToNext());
        }

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main, container, false);

        mList.clear();



        //loading 화면 띄우기

        // 사진추가
        addItem(getResources().getDrawable(R.drawable.water));
        addItem(getResources().getDrawable(R.drawable.fall));
        //addItem(getResources().getDrawable(R.drawable.image1));
        //addItem(getResources().getDrawable(R.drawable.image2));
        //addItem(getResources().getDrawable(R.drawable.image3));
        //addItem(getResources().getDrawable(R.drawable.image4));
        //addItem(getResources().getDrawable(R.drawable.image5));
        //addItem(getResources().getDrawable(R.drawable.image6));
        //addItem(getResources().getDrawable(R.drawable.image7));
        addItem(getResources().getDrawable(R.drawable.image26));
        addItem(getResources().getDrawable(R.drawable.image27));
        addItem(getResources().getDrawable(R.drawable.image28));
        addItem(getResources().getDrawable(R.drawable.image8));
        addItem(getResources().getDrawable(R.drawable.image9));
        addItem(getResources().getDrawable(R.drawable.image10));
        addItem(getResources().getDrawable(R.drawable.image29));
        addItem(getResources().getDrawable(R.drawable.image30));
        addItem(getResources().getDrawable(R.drawable.image31));
        addItem(getResources().getDrawable(R.drawable.image19));
        addItem(getResources().getDrawable(R.drawable.image20));
        addItem(getResources().getDrawable(R.drawable.image23));
        addItem(getResources().getDrawable(R.drawable.image33));
        addItem(getResources().getDrawable(R.drawable.image13));
        addItem(getResources().getDrawable(R.drawable.image14));
        addItem(getResources().getDrawable(R.drawable.image15));
        addItem(getResources().getDrawable(R.drawable.image35));
        addItem(getResources().getDrawable(R.drawable.image36));
        addItem(getResources().getDrawable(R.drawable.image11));
        addItem(getResources().getDrawable(R.drawable.image12));
        addItem(getResources().getDrawable(R.drawable.image16));
        addItem(getResources().getDrawable(R.drawable.image18));
        addItem(getResources().getDrawable(R.drawable.image24));
        addItem(getResources().getDrawable(R.drawable.image25));
        getUriArray();


        mRecyclerView = rootView.findViewById(R.id.recycler1);
        mAdapter = new RecyclerAdapter(mList,getActivity());
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayoutManager.setItemPrefetchEnabled(true);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new MovieItemDecoration(getActivity()));

        return rootView;
    }

    public void addItem(Drawable icon){
        RecyclerItem item = new RecyclerItem();
        item.setIcon(icon);
        mList.add(item);
    }
}
