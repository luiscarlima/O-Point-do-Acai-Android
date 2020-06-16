package com.devup.opointdoacai.opointdoacai.Helper;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import com.devup.opointdoacai.opointdoacai.R;

public class NotificationHelper extends ContextWrapper {

    private static final String OPDA_CHANNEL_ID = "com.devup.opointdoacai.opointdoacai.OPDA";
    private static final String OPDA_CHANNEL_NAME = "O Point do Açaí";

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            createChannel();

        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {

        NotificationChannel opdaChannel = new NotificationChannel(OPDA_CHANNEL_ID,
                OPDA_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        opdaChannel.enableLights(false);
        opdaChannel.enableVibration(true);
        opdaChannel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(opdaChannel);
    }

    public NotificationManager getManager() {

        if (manager == null){

            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        }

        return manager;

    }

    @TargetApi(Build.VERSION_CODES.O)
    public android.app.Notification.Builder getOPDAChannelNotification(String title, String body, PendingIntent contentIntent, Uri soundUri){

        return new android.app.Notification.Builder(getApplicationContext(), OPDA_CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.logo_header_img)
                .setSound(soundUri)
                .setAutoCancel(false);

    }
}
