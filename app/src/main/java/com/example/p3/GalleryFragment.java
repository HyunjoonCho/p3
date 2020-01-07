package com.example.p3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment implements MainActivity.onBackPressedListener {

    private GalleryFragment p2me = this;

    private View view;
    private RelativeLayout bigView;
    private ImageView bigImg;
    private Button backButton;
    private Button infoButton;
    private Button deleteButton;
    private FloatingActionButton addButton;
    private TextView infoText;
    private LinearLayout buttonLayout;
    private String facebook_id;
    private String picked_imageid = null;
    private static final int PICK_FROM_ALBUM = 1;
    ArrayList<TabTwoRecyclerItem> items = new ArrayList<>();


    public GalleryFragment(String facebook_id) {
        this.facebook_id = facebook_id;
    }

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
        deleteButton = view.findViewById(R.id.Delete);
        infoText = view.findViewById(R.id.Infotext);
        addButton = view.findViewById(R.id.imgAddButton);

        if(items.isEmpty()){
            getImagesFromDB();
        } else {
            setRecyclerView();
        }

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
                addButton.setVisibility(View.VISIBLE);

                ((MainActivity) requireContext()).setOnBackPressedListener(null);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());       //Builder을 먼저 생성하여 옵션을 설정합니다.
                builder.setTitle("삭제");                                                                //타이틀을 지정합니다.
                builder.setMessage("확인을 누르시면 이미지가 삭제됩니다.");

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {        //확인 버튼을 생성하고 클릭시 동작을 구현합니다.
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        NetworkHelper.getApiService().deleteImage(picked_imageid).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.d("deleted", response.body());
                                int idx = get_index(picked_imageid);
                                if (idx != -1) {
                                    items.remove(idx);
                                }
                                infoText.setVisibility(View.GONE);
                                bigView.setVisibility(View.GONE);
                                addButton.setVisibility(View.VISIBLE);
                                setRecyclerView();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.e("deleted", t.getMessage());
                            }
                        });
                    }

                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {       //취소 버튼을 생성하고 클릭시 동작을 구현합니다.
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //donothing
                    }
                });
                builder.create().show();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlbum();
            }
        });
    }

    private int get_index(String imageid) {
        for(int i=0; i<items.size(); i++){
            if(items.get(i).getImageid().equals(imageid)){
                return i;
            }
        }
        return -1;
    }

    private void getAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult", "CALL");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM) {
            if (resultCode == RESULT_OK) {
                Uri photoUri = data.getData();
                Cursor cursor = null;

                try {
                    String[] proj = {MediaStore.Images.Media.DATA};

                    assert photoUri != null;
                    cursor = getContext().getContentResolver().query(photoUri,proj, null, null);

                    assert cursor != null;

                    final TabTwoRecyclerItem item = new TabTwoRecyclerItem();
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    cursor.moveToFirst();
                    String path = cursor.getString(column_index);
                    File file = new File(path);

                    item.setUserid(facebook_id);
                    item.setImageid(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    item.setImage_name( path.substring(path.lastIndexOf("/") + 1));
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    String bitmapStr = getStringFromBitmap(BitmapFactory.decodeFile(file.getAbsolutePath(), options));
                    item.setImage(bitmapStr);

                    items.add(item);
                    Log.d("added", item.getImage_name());

                    NetworkHelper.getApiService().postImage(item.getUserid(), item.getImageid(), item.getImage_name(), item.getImage()).enqueue(new Callback<TabTwoRecyclerItem>() {
                        @Override
                        public void onResponse(Call<TabTwoRecyclerItem> call, Response<TabTwoRecyclerItem> response) {
                            Log.d("posted", response.body().getImage_name());
                            setRecyclerView();
                        }

                        @Override
                        public void onFailure(Call<TabTwoRecyclerItem> call, Throwable t) {
                            Log.e("posted", t.getMessage());
                        }
                    });
                } finally{
                    if(cursor != null){
                        cursor.close();
                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), "사진 선택 취소", Toast.LENGTH_SHORT).show();
        }
    }


    public void getImagesFromDB() {
        items.clear();
        NetworkHelper.getApiService().getImagesByUserid(facebook_id).enqueue(new Callback<List<TabTwoRecyclerItem>>() {
            @Override
            public void onResponse(Call<List<TabTwoRecyclerItem>> call, Response<List<TabTwoRecyclerItem>> response) {
                try {
                    List<TabTwoRecyclerItem> images = response.body();
                    if (images != null && images.size() != 0) {
                        for (int i = 0; i < images.size(); i++) {
                            TabTwoRecyclerItem item = new TabTwoRecyclerItem(images.get(i).getUserid(), images.get(i).getImageid(), images.get(i).getImage_name(), images.get(i).getImage());
                            items.add(item);
                            Log.d("name : ", images.get(i).getImage_name());
                        }
                        setRecyclerView();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<TabTwoRecyclerItem>> call, Throwable t) {
                Log.v("error", t.getMessage());
            }
        });
    }


    private void setRecyclerView() {
        RecyclerView rcView = view.findViewById(R.id.recyclerView);
        rcView.setItemViewCacheSize(20);
        rcView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        GalleryAdapter adapter = new GalleryAdapter(items);
        ((MainActivity) requireContext()).setOnBackPressedListener(null);

        adapter.setOnImgClickListener(new GalleryAdapter.OnImgClickListener() {
            @Override
            public void onImgClick(TabTwoRecyclerItem image) {
                picked_imageid = image.getImageid();
                bigImg.setImageBitmap(getBitmapFromString(image.getImage())); // String to bitmap
                //bigImg.setContentDescription(image.imgStr);
                infoText.setText(new String().concat("Image Name: ")
                        .concat(image.getImage_name()));
                buttonLayout.setVisibility(View.VISIBLE);
                bigView.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.GONE);
                ((MainActivity) requireContext()).setOnBackPressedListener(p2me);
            }
        });
        rcView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setRecyclerView();
            } else {
                Toast.makeText(requireContext(), "권한이 없습니다", Toast.LENGTH_LONG).show();
                ((MainActivity) requireContext()).finish();
            }
        }
    }

    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    private String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onBack() {
        if (infoText.getVisibility() == View.VISIBLE) {
            infoText.setVisibility(View.GONE);
        } else if (bigView.getVisibility() == View.VISIBLE) {
            bigView.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.GONE);
            addButton.setVisibility(View.VISIBLE);
            ((MainActivity) requireContext()).setOnBackPressedListener(null);
        }
    }
}
