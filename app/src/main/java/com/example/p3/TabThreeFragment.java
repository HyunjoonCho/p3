package com.example.p3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TabThreeFragment extends Fragment {

    class SwitchListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                Intent intent = new Intent(getActivity(), TabThreeScreenService.class);
                getActivity().startService(intent);
            } else {
                Intent intent = new Intent(getActivity(), TabThreeScreenService.class);
                getActivity().stopService(intent);
            }
        }
    }



    RecyclerView mRecyclerView = null;
    TabThreeRecyclerAdapter mAdapter = null;
    ArrayList<TabThreeItem> mList = new ArrayList();
    public Switch main_switch;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_main, container, false);
        //setTheme(android.R.style.Theme_NoTitleBar);

        addItem(false,"전여친한테 전화하지마!");
        addItem(false,"전남친한테 전화하지마!");
        addItem(true,"Hello");

        mRecyclerView = rootView.findViewById(R.id.tab3_recycler);
        mAdapter = new TabThreeRecyclerAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        main_switch = rootView.findViewById(R.id.tab3_main_switch);
        main_switch.setOnCheckedChangeListener(new SwitchListener());
        mAdapter.notifyDataSetChanged();
        return rootView;
    }

    public void addItem(Boolean b2, String text){
        TabThreeItem item = new TabThreeItem();
        item.setIcon(b2);
        item.setWarning(text);

        mList.add(item);
    }
}
