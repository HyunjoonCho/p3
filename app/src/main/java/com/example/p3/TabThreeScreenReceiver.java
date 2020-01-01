package com.example.p3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

public class TabThreeScreenReceiver extends BroadcastReceiver {

    /*public boolean isRegistered = false;

    public Intent register(Context context, IntentFilter filter) {
        try {
            return !isRegistered
                    ? context.registerReceiver(this, filter)
                    : null;
        } finally {
            isRegistered = true;
        }
    }

    public boolean unregister(Context context) {
        // additional work match on context before unregister
        // eg store weak ref in register then compare in unregister
        // if match same instance
        return isRegistered
                && unregisterInternal(context);
    }

    private boolean unregisterInternal(Context context) {
        context.unregisterReceiver(this);
        isRegistered = false;
        return true;
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
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(i);
        }
    }
}
