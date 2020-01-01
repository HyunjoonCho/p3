package com.example.p3;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class TabTwoFragment extends Fragment {

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
        Cursor imageCursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);


        if(imageCursor.moveToFirst()){
            do {
                Uri photoUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageCursor.getString(0));
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
        mRecyclerView.setAdapter(mAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayoutManager.setItemPrefetchEnabled(true);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new MovieItemDecoration(getActivity()));
        //mAdapter.notifyDataSetChanged();

        return rootView;
    }

    public void addItem(Drawable icon){
        RecyclerItem item = new RecyclerItem();
        item.setIcon(icon);
        mList.add(item);
    }
}
