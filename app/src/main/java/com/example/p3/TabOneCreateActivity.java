package com.example.p3;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TabOneCreateActivity extends AppCompatActivity {

    private static final int GET_GALLERY_IMAGE = 2;
    private EditText nametxt;
    private EditText phonetxt;
    private ImageView profile;
    private AlertDialog.Builder builder;
    private FloatingActionButton camera_fab;
    private Bitmap profile_bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1_create_contact);
        alertbuilderset();

        profile = findViewById(R.id.tab1_create_profile);
        nametxt = findViewById(R.id.tab1_create_name);
        phonetxt = findViewById(R.id.tab1_create_phonenum);
        camera_fab = findViewById(R.id.tab1_create_add_profile);

        camera_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri profile_uri = data.getData();
            try {
                profile_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), profile_uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            profile.setImageURI(profile_uri);
            profile.setVisibility(View.VISIBLE);
            profile.setClickable(true);
            camera_fab.hide();
            camera_fab.setClickable(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);


        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.tab1_create_titlebar, null);

        actionBar.setCustomView(actionbar);

        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        findViewById(R.id.tab1_create_title_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nametxt.getText().toString().trim().length() == 0 || phonetxt.getText().toString().trim().length() == 0)
                    finish();
                else
                    builder.create().show();
            }
        });

        findViewById(R.id.tab1_create_title_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nametxt.getText().toString().trim().length() == 0 || phonetxt.getText().toString().trim().length() == 0)
                    Toast.makeText(getApplicationContext(),"정보를 입력하세요",Toast.LENGTH_SHORT).show();
                else{
                    TabOneRecyclerItem item = new TabOneRecyclerItem();

                    item.setName(nametxt.getText().toString());
                    item.setPhonenum(phonetxt.getText().toString());
                    if(profile_bitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        profile_bitmap.compress(Bitmap.CompressFormat.PNG,50,stream);
                        profile_bitmap.createScaledBitmap(profile_bitmap,36,36,false);
                        item.setProfile(stream.toByteArray());
                    }else
                        item.setProfile(null);

                    Intent intent = new Intent();
                    intent.putExtra("item",item);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
        return true;
    }

    private void alertbuilderset(){
        builder = new AlertDialog.Builder(TabOneCreateActivity.this);       //Builder을 먼저 생성하여 옵션을 설정합니다.
        builder.setTitle("저장하지않은 데이터");                                                                //타이틀을 지정합니다.
        builder.setMessage("확인을 누르시면 저장하지 않고 종료합니다.");

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {        //확인 버튼을 생성하고 클릭시 동작을 구현합니다.
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {       //취소 버튼을 생성하고 클릭시 동작을 구현합니다.
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //donothing
            }
        });
    }


    public Bitmap readImageWithSampling(String imagePath, int targetWidth, int targetHeight) {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);

        int photoWidth  = bmOptions.outWidth;
        int photoHeight = bmOptions.outHeight;

        if (targetHeight <= 0) {
            targetHeight = (targetWidth * photoHeight) / photoWidth;
        }

        // Determine how much to scale down the image
        int scaleFactor = 1;
        if (photoWidth > targetWidth) {
            scaleFactor = Math.min(photoWidth / targetWidth, photoHeight / targetHeight);
        }

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(imagePath, bmOptions);
    }
}
