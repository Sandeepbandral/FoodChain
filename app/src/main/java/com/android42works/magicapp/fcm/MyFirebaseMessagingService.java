package com.android42works.magicapp.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android42works.magicapp.R;
import com.android42works.magicapp.activities.HomeActivity;
import com.android42works.magicapp.activities.OrderDetailsActivity;
import com.android42works.magicapp.utils.SessionManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

/* Created by JSP@nesar */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(String token) {

        Log.d(TAG, "FCM Token: " + token);

        SessionManager sessionManager = new SessionManager(this);
        sessionManager.setFcmtoken(token);

    }



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        String id = "0";
        String pushType = "ORDER";

        try {

            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);
            id = object.get("order_id").toString();
            pushType = object.get("push_type").toString();

            if(pushType.equalsIgnoreCase("ORDER")){
                Intent intent = new Intent("refreshOrderScreenStatus");
                sendBroadcast(intent);
            }

        } catch (Exception e) {

        }

        try {
            HomeActivity.getInstance().fetchNotiCount();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendNotification(title, body, id, pushType);

    }

    private void sendNotification(String title, String messageBody, String id, String pushType) {

        int notifyId = 0;

        try{
            notifyId = Integer.parseInt(id);
        } catch (Exception e){ }

        Intent intent = null;
        NotificationCompat.Builder notificationBuilder = null;

        if(notifyId!=0){
            intent = new Intent(this, OrderDetailsActivity.class);
            intent.putExtra("orderId", id);
        }else {
            intent = new Intent(this, HomeActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("forceOpenNotificationsTab", true);
        }


        String channelId = getString(R.string.FCMChannel);

        if (intent == null) {

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setContentTitle(title)
                            .setContentText(messageBody)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setColor(this.getResources().getColor(R.color.colorPrimary));
                notificationBuilder.setSmallIcon(R.mipmap.aap_icon_trans);
                // builder.setLargeIcon(bitmapicon);
            } else {
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
            }

        } else {

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, notifyId,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setContentTitle(title)
                            .setContentText(messageBody)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setColor(this.getResources().getColor(R.color.colorPrimary));
                notificationBuilder.setSmallIcon(R.drawable.ic_small_notify);
            } else {
                notificationBuilder.setSmallIcon(R.drawable.ic_small_notify);
            }

        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(channelId,
                    title, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);

        }



        notificationManager.notify(notifyId, notificationBuilder.build());

    }
}
