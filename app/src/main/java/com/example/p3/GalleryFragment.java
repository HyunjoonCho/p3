package com.example.p3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryFragment extends Fragment implements MainActivity.onBackPressedListener {

    private GalleryFragment p2me = this;

    private View view;
    private RelativeLayout bigView;
    private ImageView bigImg;
    private Button backButton;
    private Button infoButton;
    private Button shareButton;
    private TextView infoText;
    private LinearLayout buttonLayout;
    private String facebook_id;
    private ArrayList<ImageData> image_list = new ArrayList<>();

    public GalleryFragment(String facebook_id){this.facebook_id = facebook_id;}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;

        bigView = view.findViewById(R.id.bigView);
        bigImg = view.findViewById(R.id.imgView);
        buttonLayout = view.findViewById(R.id.ButtonLayout);
        backButton = view.findViewById(R.id.Back);
        infoButton = view.findViewById(R.id.Info);
        shareButton = view.findViewById(R.id.Share);
        infoText = view.findViewById(R.id.Infotext);

        bigImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonLayout.getVisibility() == View.VISIBLE)
                    buttonLayout.setVisibility(View.GONE);
                else
                    buttonLayout.setVisibility(View.VISIBLE);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoText.setVisibility(View.VISIBLE);
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoText.setVisibility(View.GONE);
                bigView.setVisibility(View.GONE);

                ((MainActivity)requireContext()).setOnBackPressedListener(null);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                Uri imgUri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName().concat(".provider"), new File(bigImg.getContentDescription().toString()));
                intent.putExtra(Intent.EXTRA_STREAM, imgUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(intent, "사진 공유하기"));
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                setRecyclerView();
            }
            else {
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS}, 1001);
            }
        }
        else {
            setRecyclerView();
        }
    }


    private void setRecyclerView() {
        getPathOfAllImages();

        RecyclerView rcView = view.findViewById(R.id.recyclerView);
        rcView.setItemViewCacheSize(20);
        rcView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        GalleryAdapter adapter = new GalleryAdapter(image_list);
        adapter.setOnImgClickListener(new GalleryAdapter.OnImgClickListener() {
            @Override
            public void onImgClick(ImageData image) {
                bigImg.setImageBitmap(getBitmapFromString(image.getImage()));
                //bigImg.setContentDescription(image.imgPath);
                buttonLayout.setVisibility(View.VISIBLE);
                bigView.setVisibility(View.VISIBLE);
                ((MainActivity)requireContext()).setOnBackPressedListener(p2me);
            }
        });
        rcView.setAdapter(adapter);
    }

    private ArrayList<ImageData> getPathOfAllImagesFromPhone() {

        ArrayList<ImageData> result = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};

        Cursor imgCursor = getContext().getContentResolver().query(uri, projection, null, null, null);

        while (imgCursor.moveToNext()) {
            String absPath = imgCursor.getString(1);
            String imgName = imgCursor.getString(2);
            String imgSize = imgCursor.getString(3);
            if (!TextUtils.isEmpty(absPath)) {
                result.add(new ImageData(facebook_id, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),absPath, imgName));
            }
        }

        return result;
    }

    private void getPathOfAllImages() {

        NetworkHelper.getApiService().getImagesByUserid(facebook_id).enqueue(new Callback<List<ImageData>>() {
            @Override
            public void onResponse(Call<List<ImageData>> call, Response<List<ImageData>> response) {
                List<ImageData> imageData = response.body();
                for(int i = 0; i < imageData.size(); i++){
                    image_list.add(new ImageData(facebook_id, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),imageData.get(i).getImage(), imageData.get(i).imgName));
                }
            }

            @Override
            public void onFailure(Call<List<ImageData>> call, Throwable t) {
                Log.e("error",t.getMessage());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setRecyclerView();
            }
            else {
                Toast.makeText(requireContext(), "권한이 없습니다", Toast.LENGTH_LONG).show();
                ((MainActivity)requireContext()).finish();
            }
        }
    }

    @Override
    public void onBack() {
        if (infoText.getVisibility() == View.VISIBLE) {
            infoText.setVisibility(View.GONE);
        }
        else if (bigView.getVisibility() == View.VISIBLE) {
            bigView.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.GONE);
            ((MainActivity)requireContext()).setOnBackPressedListener(null);
        }
    }

    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
