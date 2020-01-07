package com.example.p3;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.p3.TabThreeFragment.isregister;
import static com.example.p3.TabThreeFragment.register_position;


public class TabThreeRecyclerAdapter extends RecyclerView.Adapter<TabThreeRecyclerAdapter.ViewHolder> {
    private ArrayList<TabThreeItem> tData = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private View rootview;
    private String facebook_id;
    private Socket mSocket;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){ this.onItemClickListener = listener; }

    public ArrayList<TabThreeItem> gettData() {
        return tData;
    }

    public TabThreeRecyclerAdapter(View rootview, String facebook_id) {
        notify_adapter();
        this.rootview = rootview;
        this.facebook_id = facebook_id;
    }

    @Override
    public TabThreeRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.tab3_item,parent,false);
        TabThreeRecyclerAdapter.ViewHolder tvh = new TabThreeRecyclerAdapter.ViewHolder(view);

        return tvh;
    }

    @Override
    public void onBindViewHolder(TabThreeRecyclerAdapter.ViewHolder holder, int position){

        TabThreeItem item = tData.get(position);
        holder.title.setText(item.getDestination());
        holder.time.setText(item.getExpireAt().substring(11,16));
        holder.count.setText(item.getUserid().size()+"/"+item.getCount());
        holder.state.setText(item.getStatus());
        holder.departure.setText(item.getDeparture());
        if(item.getStatus().equals("모집중"))
            holder.state.setTextColor(Color.parseColor("#FF18AA00"));
        else if(item.getStatus().equals("임박"))
            holder.state.setTextColor(Color.parseColor("#FFD6A000"));
        else if(item.getStatus().equals("마감")) {
            holder.state.setTextColor(Color.parseColor("#FF000000"));
            //holder.itemView.setClickable(false);
        }
    }

    @Override
    public int getItemCount(){
        return tData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView time;
        TextView count;
        TextView state;
        TextView departure;

        ViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.tab3_item_title);
            time = itemView.findViewById(R.id.tab3_item_time);
            count = itemView.findViewById(R.id.tab3_item_count);
            state = itemView.findViewById(R.id.tab3_item_state);
            departure = itemView.findViewById(R.id.tab3_item_place);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v,getAdapterPosition());
                }
            });
        }
    }

    public void add(String userid, String gatheringname, String when, String place, String count){
        tData.add(new TabThreeItem(userid,gatheringname,when,place,count));
    }

    public void notify_adapter(){
        NetworkHelper.getApiService().getGatherings().enqueue(new Callback<List<TabThreeItem>>() {
            @Override
            public void onResponse(Call<List<TabThreeItem>> call, Response<List<TabThreeItem>> response) {
                if (response.body() != null) {
                    tData.clear();
                    List<TabThreeItem> response_list = response.body();
                    for (int i = 0; i < response_list.size(); i++)
                        tData.add(new TabThreeItem(response_list.get(i).getUserid(),
                                response_list.get(i).getGatheringid(),
                                response_list.get(i).getDestination(),
                                response_list.get(i).getExpireAt(),
                                response_list.get(i).getDeparture(),
                                response_list.get(i).getCount()));
                    if(tData.size()!= 0)
                        setpick(rootview);
                    notifyDataSetChanged();
                    rootview.invalidate();
                }
            }

            @Override
            public void onFailure(Call<List<TabThreeItem>> call, Throwable t) {
                Log.e("error",t.getMessage());
            }
        });
    }


    public void setpick(View rootview){

        for(int i = 0; i < tData.size(); i++) {
            for (int j = 0; j < tData.get(i).getUserid().size(); j++) {
                if (tData.get(i).getUserid().get(j).equals(facebook_id)) {
                    register_position = i;
                    isregister = true;
                    setpickbypos(rootview);
                    return;
                }
            }
        }

        if(isregister != true){
            int nearest_pos = 0;
            long time = -1;
            for(int i = 0; i < tData.size(); i++){
                long cal;
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date cur_time = dateFormat.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                    Date exptime_date = dateFormat.parse(convertDatetoString(tData.get(i).getExpireAt()));
                    cal = exptime_date.getTime() - cur_time.getTime();

                    if(time == -1) {
                        time = cal;
                        nearest_pos = i;
                    }else if(time > cal){
                        time = cal;
                        nearest_pos = i;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            register_position = nearest_pos;
            setpickbypos(rootview);
        }
    }

    public void setpickbypos(View rootview){
        TextView titletxt = rootview.findViewById(R.id.tab3_pick_title);
        TextView counttxt = rootview.findViewById(R.id.tab3_pick_count);
        TextView timetxt = rootview.findViewById(R.id.tab3_pick_time);
        TextView placetxt = rootview.findViewById(R.id.tab3_pick_place);
        TextView statetxt = rootview.findViewById(R.id.tab3_pick_state);

        String str = tData.get(register_position).getUserid().size()+"/"+tData.get(register_position).getCount();
        titletxt.setText(tData.get(register_position).getDestination());
        counttxt.setText(str);
        timetxt.setText(tData.get(register_position).getExpireAt().substring(11,16));
        placetxt.setText(tData.get(register_position).getDeparture());
        statetxt.setText(tData.get(register_position).getStatus());
    }

    public void removeid(int pos){
        tData.get(pos).getUserid().remove(facebook_id);
        if(tData.get(pos).getUserid().size() == 0){
            NetworkHelper.getApiService().deleteGatherings(tData.get(pos).getGatheringid()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.e("delete respone",response.body());
                    try {
                        mSocket = IO.socket("http://192.249.19.251:8780");
                        mSocket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                            mSocket.emit("clientMessage","HI");
                        }).on("serverMessage",(Object... objects) -> { //다른사람은 false로 하고 answer도 보내줘야함
                            JsonParser jsonParsers = new JsonParser();
                            JsonObject probleminfo = (JsonObject) jsonParsers.parse(objects[0] + "");
                            Log.d("probleminfo", probleminfo.toString());
                            tData.remove(pos);
                            rootview.invalidate();
                            notify_adapter();
                        });
                        mSocket.connect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("error",t.getMessage());
                }
            });
        }else{
            NetworkHelper.getApiService().putGatherings(tData.get(pos).getGatheringid(),tData.get(pos)).enqueue(new Callback<TabThreeItem>() {
                @Override
                public void onResponse(Call<TabThreeItem> call, Response<TabThreeItem> response) {
                    TabThreeItem item = response.body();
                    Log.e("put respone",item.getUserid().get(0));
                    try {
                        mSocket = IO.socket("http://192.249.19.251:8780");
                        mSocket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                            mSocket.emit("clientMessage","HI");
                        }).on("serverMessage",(Object... objects) -> { //다른사람은 false로 하고 answer도 보내줘야함
                            JsonParser jsonParsers = new JsonParser();
                            JsonObject probleminfo = (JsonObject) jsonParsers.parse(objects[0] + "");
                            Log.d("probleminfo", probleminfo.toString());
                            rootview.invalidate();
                            notify_adapter();
                        });
                        mSocket.connect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<TabThreeItem> call, Throwable t) {
                    Log.e("error",t.getMessage());
                }
            });
        }
    }

    public void addid(int pos){
        tData.get(pos).getUserid().add(facebook_id);
        NetworkHelper.getApiService().putGatherings(tData.get(pos).getGatheringid(),tData.get(pos)).enqueue(new Callback<TabThreeItem>() {
            @Override
            public void onResponse(Call<TabThreeItem> call, Response<TabThreeItem> response) {
                TabThreeItem item = response.body();
                Log.e("put respone",item.getUserid().get(0));
                try {
                    mSocket = IO.socket("http://192.249.19.251:8780");
                    mSocket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                        mSocket.emit("clientMessage","HI");
                    }).on("serverMessage",(Object... objects) -> { //다른사람은 false로 하고 answer도 보내줘야함
                        JsonParser jsonParsers = new JsonParser();
                        JsonObject probleminfo = (JsonObject) jsonParsers.parse(objects[0] + "");
                        Log.d("probleminfo", probleminfo.toString());
                        rootview.invalidate();
                        notify_adapter();
                    });
                    mSocket.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TabThreeItem> call, Throwable t) {
                Log.e("error",t.getMessage());
            }
        });
    }

    private String convertDatetoString(String date){
        String str = "";
        str += date.substring(0,4);
        str += date.substring(5,7);
        str += date.substring(8,10);
        str += date.substring(11,13);
        str += date.substring(14,16);
        str += date.substring(17,19);

        return str;
    }
}

