package com.digitalsigma.hobrasul.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.digitalsigma.hobrasul.Activity.NewsContentActivity;
import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * Created by Quzal on 8/27/2016.
 */
public class MessageService extends FirebaseMessagingService {

    Bitmap  mIcon_val;

 String newsDoc,newsTitle,newsImg;
    String role="";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        role =remoteMessage.getData().get("role");
        if (role.equals("link"))
        {
            browserNotify(remoteMessage.getData().get("url"));

        }else if(role.equals("notifyLink"))
        {

            showNotificationLink(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"),remoteMessage.getData().get("url"),
                    remoteMessage.getData().get("img"));        }
        else if (role.equals("notifyYoutube")){

            showNotificationYoutube(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"),remoteMessage.getData().get("youtube_url"));

        }
        else if (role.equals("news")){


            showNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("title"));
            newsDoc = remoteMessage.getData().get("message");
            newsTitle = remoteMessage.getData().get("title");
            newsImg = remoteMessage.getData().get("img");


            Constant.newsNotificationDocmention = newsDoc;
            Constant.newsNotificationDocmentionTilte = newsTitle;
            Constant.newsNotificationDocmentionImgUrl = remoteMessage.getData().get("img");


        }

    }


    private void showNotificationYoutube(String title,String message,String url) {
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

/*
        Constant.newsNotificationDocmention=message;
        Constant.newsNotificationDocmentionTilte=title;
        Constant.newsNotificationDocmentionImgUrl=newsImg;
*/
        /*if(!url.startsWith("www.")&& !url.startsWith("http://")){
            url = "www."+url;
        }
        if(!url.startsWith("http://")){
            url = "http://"+url;
        }*/

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.youtube_logo);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setLargeIcon(bMap)
                .setSmallIcon(R.drawable.youtube_logo)
                .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission
                .setPriority(NotificationCompat.PRIORITY_HIGH)

                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(m,builder.build());
    }



    private void showNotificationLink(String title, String message, String url, String img) {
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

/*
        Constant.newsNotificationDocmention=message;
        Constant.newsNotificationDocmentionTilte=title;
        Constant.newsNotificationDocmentionImgUrl=newsImg;


*/

       /* if(!url.startsWith("www.")&& !url.startsWith("http://")){
            url = "www."+url;
        }
        if(!url.startsWith("http://")){
            url = "http://"+url;
        }*/


        try {
            URL newurl = new URL(img);
            mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

/*
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.youtube_logo);
*/

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setLargeIcon(mIcon_val)
                .setSmallIcon(R.drawable.logo)
                .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission
                .setPriority(NotificationCompat.PRIORITY_HIGH)

                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(m,builder.build());
    }







    public void browserNotify(String url)
    {
      /*  if(!url.startsWith("www.")&& !url.startsWith("http://")){
            url = "www."+url;
        }
        if(!url.startsWith("http://")){
            url = "http://"+url;
        }*/
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void showNotification(String message,String title) {
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        Constant.newsNotificationDocmention=message;
        Constant.newsNotificationDocmentionTilte=title;
        Constant.newsNotificationDocmentionImgUrl=newsImg;

      //  Bitmap bitmap=new Bitmap().getre

        Intent i = new Intent(this,NewsContentActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);


        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setLargeIcon(bMap)
                .setSmallIcon(R.drawable.logo)
                .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission
                .setPriority(NotificationCompat.PRIORITY_HIGH)

                .setContentIntent(pendingIntent);

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
