package com.android42works.magicapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Netwatcher extends BroadcastReceiver{

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    private NetwatcherListener netwatcherListener;

    public Netwatcher() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(netwatcherListener!=null) {

            if (isUserOnline(context)) {
                netwatcherListener.onNetworkConnectionChanged(true);
            } else {
                netwatcherListener.onNetworkConnectionChanged(false);
            }
        }

    }

    public Netwatcher(NetwatcherListener netwatcherListener) {
        this.netwatcherListener = netwatcherListener;
    }

    private boolean isUserOnline(Context context){

        boolean status = false;

        int conn = getConnectivityStatus(context);

        if (conn == TYPE_WIFI) {
            status = true;
            Log.e("Netwatcher", "WIFI Enabled.");
        } else if (conn == TYPE_MOBILE) {
            status = true;
            Log.e("Netwatcher", "WIFI Enabled.");
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = false;
            Log.e("Netwatcher", "Not connected to Internet");
        }

        return status;
    }

    private int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public interface NetwatcherListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

}
