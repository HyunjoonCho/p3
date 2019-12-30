package com.example.p3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.WindowManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;



public class Gallery extends AppCompatActivity {
    //리사이클러뷰의 구성요소 adapter, layoutmanager, viewholder
    RecyclerView mRecyclerView = null ;
    RecyclerAdapter mAdapter = null ;
    ArrayList<RecyclerItem> mList = new ArrayList<RecyclerItem>();

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



    public void getUriArray(){
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.SIZE};
        Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);


        if(imageCursor.moveToFirst()){
            do {
                Uri photoUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageCursor.getString(0));
                //photoUri = MediaStore.setRequireOriginal(photoUri);

                Bitmap bitmap = null;
                try{
                    //InputStream ims = getContentResolver().openInputStream(photoUri);
                    //bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeStream(ims),125,128);
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),photoUri);
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //super.oncreate는 이미 appcompatactivity에 있는 oncreate기능을 받아오고 거기에 추가하는 느낌.
        super.onCreate(savedInstanceState);
        //상태바 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.content_main);
        //타이틀바 없애기
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // 사진추가
        addItem(getDrawable(R.drawable.water));
        addItem(getDrawable(R.drawable.fall));
        //addItem(getDrawable(R.drawable.image1));
        //addItem(getDrawable(R.drawable.image2));
        //addItem(getDrawable(R.drawable.image3));
        //addItem(getDrawable(R.drawable.image4));
        //addItem(getDrawable(R.drawable.image5));
        //addItem(getDrawable(R.drawable.image6));
        //addItem(getDrawable(R.drawable.image7));
        addItem(getDrawable(R.drawable.image26));
        addItem(getDrawable(R.drawable.image27));
        addItem(getDrawable(R.drawable.image28));
        addItem(getDrawable(R.drawable.image29));
        addItem(getDrawable(R.drawable.image30));
        addItem(getDrawable(R.drawable.image31));
        addItem(getDrawable(R.drawable.image33));
        addItem(getDrawable(R.drawable.image35));
        addItem(getDrawable(R.drawable.image36));
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
        mAdapter = new RecyclerAdapter(mList,getApplicationContext());
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

