package com.devup.opointdoacai.opointdoacai.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.devup.opointdoacai.opointdoacai.Common.Common;
import com.devup.opointdoacai.opointdoacai.Helper.NotificationHelper;
import com.devup.opointdoacai.opointdoacai.OrderStatus;
import com.devup.opointdoacai.opointdoacai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessaging extends FirebaseMessagingService{

    FirebaseAuth mAuth;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String click_action = remoteMessage.getNotification().getClickAction();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            sendNotificationAPI26(remoteMessage);
        }else{

            sendNotification(remoteMessage);

        }


    }

    private void sendNotificationAPI26(RemoteMessage remoteMessage) {

        mAuth = FirebaseAuth.getInstance();

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        String title = notification.getTitle();
        String content = notification.getBody();

        Intent intent = new Intent(this, OrderStatus.class);
        intent.putExtra(Common.PHONE_TEXT, mAuth.getCurrentUser().getPhoneNumber());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationHelper helper = new NotificationHelper(this);
        Notification.Builder builder = helper.getOPDAChannelNotification(title, content, pendingIntent, defaultSoundUri);

        helper.getManager().notify(new Random().nextInt(), builder.build());

    }

    private void sendNotification(RemoteMessage remoteMessage) {

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Intent intent = new Intent(this, OrderStatus.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo_header_img)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        noti.notify(0, builder.build());
    }
}
