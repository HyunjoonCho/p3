package com.example.p3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabOneUpdateActivity extends AppCompatActivity {

    private static final int GET_GALLERY_IMAGE = 2;
    private EditText nametxt;
    private EditText phonetxt;
    private ImageView profile;
    private FloatingActionButton camera_fab;
    private Bitmap profile_bitmap = null;
    private Uri profile_uri = null;
    private int position;
    Intent update_intent;

    TabOneRecyclerItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1_update_contact);

        nametxt = findViewById(R.id.tab1_update_name);
        phonetxt = findViewById(R.id.tab1_update_phonenum);
        profile = findViewById(R.id.tab1_update_profile);
        camera_fab = findViewById(R.id.tab1_update_add_profile);


        update_intent = getIntent();
        item = (TabOneRecyclerItem)update_intent.getSerializableExtra("updateitem");
        position = update_intent.getIntExtra("position",1);

        nametxt.setText(item.getName());
        phonetxt.setText(item.getPhone_number());

        if(!item.getProfile_pic().equals("no_profile")){
            profile.setImageBitmap(getBitmapFromString(item.getProfile_pic()));
            profile.setClickable(true);
            profile.setVisibility(View.VISIBLE);
            camera_fab.setClickable(false);
            camera_fab.hide();
        }else{
            profile.setClickable(false);
            profile.setVisibility(View.INVISIBLE);
            camera_fab.setClickable(true);
            camera_fab.show();
        }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);


        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.tab1_update_titlebar, null);

        actionBar.setCustomView(actionbar);

        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        findViewById(R.id.tab1_update_title_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.tab1_update_title_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setName(nametxt.getText().toString());
                item.setPhone_number(phonetxt.getText().toString());

                NetworkHelper.getApiService().putContract(item.getContactid(),item).enqueue(new Callback<TabOneRecyclerItem>() {
                    @Override
                    public void onResponse(Call<TabOneRecyclerItem> call, Response<TabOneRecyclerItem> response) {
                        Log.e("PUT",response.body().getName());
                        Intent intent = new Intent();
                        intent.putExtra("updateditem",item);
                        intent.putExtra("position",position);
                        setResult(RESULT_OK,intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<TabOneRecyclerItem> call, Throwable t) {
                        Log.e("error",t.getMessage());
                    }
                });
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            profile_uri = data.getData();
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

    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
