package com.example.tuananh.weatherforecast.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class MyNotifyReceiver extends BroadcastReceiver {
    private String notifyState = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        String checkState = intent.getStringExtra("Notify State");
        Intent service = new Intent(context, MyNotifyService.class);

        if (checkState == null) {
            notifyState = "isSet";
            if (isNetworkConnected(context)) {
                context.startService(service);
            }
        } else if (checkState.equalsIgnoreCase("On")) {
            notifyState = "On";
            if (isNetworkConnected(context)) {
                context.startService(service);
            }
        } else if (checkState.equalsIgnoreCase("Off")) {
            notifyState = "Off";
            context.stopService(service);
        }
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
