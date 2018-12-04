package com.digitalsigma.hobrasul.Adapter;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.Service.MusicServiceSemsm;

import static android.content.Context.NOTIFICATION_SERVICE;

public class PlayerNotificationBroadcast extends BroadcastReceiver {
    public static final String NOTIFY_PREVIOUS = "com.smsemshehab.gmsproduction.previous";
    public static final String NOTIFY_DELETE = "com.smsemshehab.gmsproduction.delete";
    public static final String NOTIFY_PAUSE = "com.smsemshehab.gmsproduction.pause";
    public static final String NOTIFY_PLAY = "com.smsemshehab.gmsproduction.play";
    public static final String NOTIFY_NEXT = "com.smsemshehab.gmsproduction.next";

    MusicServiceSemsm musicServiceSemsm;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Constant.playListUrl.size() > 0) {

            if (intent.getAction().equals(NOTIFY_PLAY)) {
                //   Toast.makeText(context, "NOTIFY_PLAY", Toast.LENGTH_LONG).show();
                if (Constant.playListUrl.get(Constant.postion).equals("") || Constant.playListUrl.get(Constant.postion).equals(null)) {

                } else {
                    //  if (MusicServiceSemsm.player != null) {

                    // musicServiceSemsm.go();
                    if (MusicServiceSemsm.player != null) {
                        MusicServiceSemsm.player.start();
                        Constant.NOTIFY_PLAYER = "pause";
                    }
                    //  }
                }

            } else if (intent.getAction().equals(NOTIFY_PAUSE)) {
                if (MusicServiceSemsm.player != null && MusicServiceSemsm.player.isPlaying()) {
                    // musicServiceSemsm.pausePlayer();
                    MusicServiceSemsm.player.pause();
                    //  Toast.makeText(context, "NOTIFY_PAUSE", Toast.LENGTH_LONG).show();
                } else {


                }
            } else if (intent.getAction().equals(NOTIFY_NEXT)) {
                Toast.makeText(context, "NOTIFY_NEXT", Toast.LENGTH_LONG).show();
                Constant.postion++;

           /* new AlbumContentActivity().playSong();*/

            } else if (intent.getAction().equals(NOTIFY_DELETE)) {


                if (MusicServiceSemsm.player != null && MusicServiceSemsm.player.isPlaying()) {
                    // musicServiceSemsm.pausePlayer();
                    MusicServiceSemsm.player.pause();
                    NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                    manager.cancelAll();
                    //  Toast.makeText(context, "NOTIFY_PAUSE", Toast.LENGTH_LONG).show();
                } else if (MusicServiceSemsm.player != null) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                    manager.cancelAll();

                }


        /*    Toast.makeText(context, "NOTIFY_DELETE", Toast.LENGTH_LONG).show();

            MusicServiceSemsm.nm.cancelAll();
            MusicServiceSemsm.nm.cancel(1);
            musicServiceSemsm.pausePlayer();
           *//* MusicServiceSemsm.player.pause();
            MusicServiceSemsm.player.stop();
            MusicServiceSemsm.player.reset();*//*
            System.exit(0);*/


            } else if (intent.getAction().equals(NOTIFY_PREVIOUS)) {
                Toast.makeText(context, "NOTIFY_PREVIOUS", Toast.LENGTH_LONG).show();
            }
        }

    }
}