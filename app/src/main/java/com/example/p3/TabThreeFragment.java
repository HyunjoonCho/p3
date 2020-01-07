package com.example.p3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.scrollablelayout.ScrollableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class TabThreeFragment extends Fragment {

    private TabThreeRecyclerAdapter myAdapter;
    private String facebook_id;
    public static boolean isregister = false;
    public static int register_position;
    private final static int CREATE_CODE = 500;
    private final static int JOIN_CODE = 501;
    private ConstraintLayout pick_layout;
    private Socket mSocket;
    private final static String TAG = "tab3addactivity";

    public TabThreeFragment(String facebook_id){ this.facebook_id = facebook_id; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_main, container, false);

        try {
            mSocket = IO.socket("http://192.249.19.251:8780");
            mSocket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                mSocket.emit("clientMessage","HI");
            }).on("serverMessage",(Object... objects) -> { //다른사람은 false로 하고 answer도 보내줘야함
                JsonParser jsonParsers = new JsonParser();
                JsonObject probleminfo = (JsonObject) jsonParsers.parse(objects[0] + "");
                Log.d("probleminfo", probleminfo.toString());
                getView().invalidate();
                myAdapter.notify_adapter();
            });
            mSocket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }


        ScrollableLayout scrollableLayout = rootView.findViewById(R.id.tab3_main_nsv);
        RecyclerView myRecycler =  rootView.findViewById(R.id.tab3_main_recycler);
        pick_layout = rootView.findViewById(R.id.tab3_pick_layout);

        scrollableLayout.getHelper().setCurrentScrollableContainer(myRecycler);

        myAdapter = new TabThreeRecyclerAdapter(rootView,facebook_id);

        myAdapter.setOnItemClickListener(new TabThreeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity().getApplicationContext(), TabThreePopupActivity.class);
                intent.putExtra("isregistered",isregister);
                intent.putExtra("position",position);
                intent.putExtra("namelist",(ArrayList)myAdapter.gettData().get(position).getUserid());
                getActivity().startActivityForResult(intent,JOIN_CODE);
            }
        });

        pick_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myAdapter.gettData().size() != 0) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), TabThreePopupActivity.class);
                    intent.putExtra("isregistered", isregister);
                    intent.putExtra("position", register_position);
                    intent.putExtra("namelist", (ArrayList) myAdapter.gettData().get(register_position).getUserid());
                    getActivity().startActivityForResult(intent, JOIN_CODE);
                }
            }
        });

        myRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecycler.setAdapter(myAdapter);

        myAdapter.notifyDataSetChanged();


        rootView.findViewById(R.id.tab3_title_img2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(),TabThreeAddActivity.class);
                i.putExtra("id",facebook_id);
                i.putExtra("isregistered",isregister);
                getActivity().startActivityForResult(i,CREATE_CODE);
            }
        });

        return rootView;
    }

    public void setRegister_position(int register_position) {
        this.register_position = register_position;
        this.isregister = true;
    }

    public void setIsregister(boolean isregister) {
        this.isregister = isregister;
    }

    public TabThreeRecyclerAdapter getMyAdapter() {
        return myAdapter;
    }

}
