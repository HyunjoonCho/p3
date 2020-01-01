package com.example.p3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.ArrayList;

public class TabThreeScreenReceiver extends BroadcastReceiver {

    /*private ArrayList<TabThreeItem> mList;

    public void setmList(ArrayList<TabThreeItem> mList) {
        this.mList = mList;
    }*/

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent in = new Intent(context, TabThreeScreenService.class);
            context.startForegroundService(in);
        } else {
            Intent in = new Intent(context, TabThreeScreenService.class);
            context.startService(in);
        }

        if (intent != null && intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Intent i = new Intent(context, TabThreeLockScreenActivity.class);
            //i.putExtra("mList",mList);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(i);
        }
    }
}
