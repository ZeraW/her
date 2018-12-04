package com.digitalsigma.hobrasul.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.digitalsigma.hobrasul.Activity.NewsContentActivity;
import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;

import java.util.Date;

/**
 * Created by Quzal on 8/27/2016.
 */
public class PartiesReciverNotify extends FirebaseMessagingService {


    String time,place,newsImg;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        showNotification(remoteMessage.getData().get("time"),remoteMessage.getData().get("place"));
        time=remoteMessage.getData().get("time");
        place=remoteMessage.getData().get("place");
     //   newsImg=remoteMessage.getData().get("img");


       /* Constant.newsNotificationDocmention=time;
        Constant.newsNotificationDocmentionTilte=place;
        Constant.newsNotificationDocmentionImgUrl=remoteMessage.getData().get("img");*/

    }

    private void showNotification(String time,String place) {
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

  /*      Constant.newsNotificationDocmention=message;
        Constant.newsNotificationDocmentionTilte=title;*/
        Constant.newsNotificationDocmentionImgUrl=newsImg;


        Intent i = new Intent(this,NewsContentActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.semem);


        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("news")
                .setContentText(time)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.semem)
                .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission
                .setPriority(NotificationCompat.PRIORITY_HIGH);

               // .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(m,builder.build());
    }









 /*   @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this);
        // set notification properties
        notificationBuilder.setContentTitle("FCM Notification"); //title of notification
        notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher); // change notification icon
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());


    }*/
}
