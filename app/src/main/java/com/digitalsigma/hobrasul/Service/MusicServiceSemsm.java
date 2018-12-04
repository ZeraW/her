package com.digitalsigma.hobrasul.Service;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;


import com.digitalsigma.hobrasul.Activity.DownLoadsActivity;
import com.digitalsigma.hobrasul.Activity.MainActivity;
import com.digitalsigma.hobrasul.Activity.PlayerActivity;
import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;
import com.digitalsigma.hobrasul.Activity.TracksActivity;

import java.util.ArrayList;
import java.util.Random;

/*
 * This is demo code to accompany the Mobiletuts+ series:
 * Android SDK: Creating a Music Player
 *
 * Sue Smith - February 2014
 */

public class MusicServiceSemsm extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,MediaPlayer.OnSeekCompleteListener ,MediaPlayer.OnBufferingUpdateListener{


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    int lastpos=0;

    //Notification

    public static NotificationManager nm;

    public static final String NOTIFY_PREVIOUS = "com.smsemshehab.gmsproduction.previous";
    public static final String NOTIFY_DELETE = "com.smsemshehab.gmsproduction.delete";
    public static final String NOTIFY_PAUSE = "com.smsemshehab.gmsproduction.pause";
    public static final String NOTIFY_PLAY = "com.smsemshehab.gmsproduction.play";
    public static final String NOTIFY_NEXT = "com.smsemshehab.gmsproduction.next";


    // Set up the notification ID
    private static final int NOTIFICATION_ID = 1;
    private boolean isPausedInCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;



    // ---Variables for seekbar processing---
    String sntSeekPos;
    int intSeekPos;
    int mediaPosition;
    int cPostion;
    int mediaMax;
    //Intent intent;
    private final Handler handler = new Handler();
    private static int songEnded;
    public static final String BROADCAST_ACTION = "com.gmsproduction.tarekelsheikh.seekprogress";


    public static final String BROADCAST_Text = "com.gmsproduction.tarekelsheikh.textView";

    // Set up broadcast identifier and intent
    public static final String BROADCAST_BUFFER = "com.gmsproduction.tarekelsheikh.broadcastbuffer";

    // Set up broadcast identifier and intent

    Intent bufferIntent;

    Intent textIntent;
    Intent seekIntent;


    Button btn_play;
    Button btn_next;
    Button btn_back;

    TextView startTimeTxt;
    TextView songNametxt;
    TextView finalTimeTxt;

    SeekBar seekBar;


    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();;




    //media player
    public static MediaPlayer player;
    //song list
    private ArrayList<String> songs;
    //current position
    private int songPosn;
    //binder
    private final IBinder musicBind = new MusicBinder();
    //title of current song
    private String songTitle="";
    //notification id
    private static final int NOTIFY_ID=1;
    //shuffle flag and random
    private boolean shuffle=false;
    private Random rand;

    public void onCreate(){
        //create the service
        super.onCreate();


        // ---Set up intent for seekbar broadcast ---
        seekIntent = new Intent(BROADCAST_ACTION);

        bufferIntent = new Intent(BROADCAST_BUFFER);

      //  textIntent=new Intent(BROADCAST_Text);

        //initialize position
        songPosn=0;
        //random
        rand=new Random();
        //create player
        player = new MediaPlayer();
        //initialize
        initMusicPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // ---Set up receiver for seekbar change ---
        /*registerReceiver(broadcastReceiver, new IntentFilter(
                AlbumFragment.BROADCAST_SEEKBAR));*/

        registerReceiver(broadcastReceiver, new IntentFilter(
                MainActivity.BROADCAST_SEEKBAR));

        registerReceiver(broadcastReceiver, new IntentFilter(
                DownLoadsActivity.BROADCAST_SEEKBAR));

        registerReceiver(broadcastReceiver, new IntentFilter(
                PlayerActivity.BROADCAST_SEEKBAR));


        registerReceiver(broadcastReceiver, new IntentFilter(
                TracksActivity.BROADCAST_SEEKBAR));



   /*     // ---Set up receiver for seekbar change ---
        registerReceiver(broadcastReceiver, new IntentFilter(
                AlbumFragment.BROADCAST_Text));*/



       //





        if (player != null &&player.isPlaying()) {


            telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            //   Log.v(TAG, "Starting listener");
            phoneStateListener = new PhoneStateListener() {
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    // String stateString = "N/A";
                    // Log.v(TAG, "Starting CallStateChange");
                    switch (state) {
                        case TelephonyManager.CALL_STATE_OFFHOOK:
                        case TelephonyManager.CALL_STATE_RINGING:
                            if (player != null) {
                                player.pause();
                                isPausedInCall = true;
                            }

                            break;
                        case TelephonyManager.CALL_STATE_IDLE:
                            // Phone idle. Start playing.
                            if (player != null) {
                                if (isPausedInCall) {
                                    isPausedInCall = false;
                                   // player.start();
                                    player.pause();
                                }

                            }
                            break;
                    }

                }
            };

            // Register the listener with the telephony manager
            telephonyManager.listen(phoneStateListener,
                    PhoneStateListener.LISTEN_CALL_STATE);

        }


       // return super.onStartCommand(intent, flags, startId);
return START_STICKY;

    }



    //    Notification


    public  void customSimpleNotification(Context context,String songname,String albumname) {
        Intent notificationIntent = null;

        notificationIntent = new Intent(getApplicationContext(), TracksActivity.class);

      /*  if (Constant.pointer == 1) {
            notificationIntent = new Intent(getApplicationContext(), TracksActivity.class);


        } else if (Constant.pointer == 2) {
            notificationIntent = new Intent(getApplicationContext(), DownLoadsActivity.class);


        } else if (Constant.pointer == 0)
        {
            notificationIntent = new Intent(getApplicationContext(), PlayerActivity.class);

        }*/
        
        

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews simpleView = new RemoteViews(context.getPackageName(), R.layout.player_custom_notification);

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon)
                .setAutoCancel(false)
                .setContentTitle("Custom Big View").setContentIntent(contentIntent).build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.contentView = simpleView;
        notification.contentView.setTextViewText(R.id.textSongName, songname);
        notification.contentView.setTextViewText(R.id.textAlbumName, albumname);
        setListeners(simpleView,context);



        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        nm.notify(7, notification);

    }



    private static void setListeners(RemoteViews view, Context context) {
        Intent previous = new Intent(NOTIFY_PREVIOUS);
        Intent delete = new Intent(NOTIFY_DELETE);
        Intent pause = new Intent(NOTIFY_PAUSE);
        Intent next = new Intent(NOTIFY_NEXT);
        Intent play = new Intent(NOTIFY_PLAY);

       /* PendingIntent pPrevious = PendingIntent.getBroadcast(context, 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPrevious, pPrevious);*/

        PendingIntent pDelete = PendingIntent.getBroadcast(context, 0, delete, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnDelete, pDelete);


        PendingIntent pPause = PendingIntent.getBroadcast(context, 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPause, pPause);

        PendingIntent pNext = PendingIntent.getBroadcast(context, 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnNext, pNext);

        PendingIntent pPlay = PendingIntent.getBroadcast(context, 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPlay, pPlay);



    }




    // --Receive seekbar position if it has been changed by the user in the


    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //set listeners
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.setOnSeekCompleteListener(this);
        player.reset();

    }

    public void setUpSongNameText()
    {
        handler.removeCallbacks(sendUpdatesToUIText);
        handler.postDelayed(sendUpdatesToUIText, 1000); // 1 second
    }

    private Runnable sendUpdatesToUIText = new Runnable() {
        public void run() {
            // // Log.d(TAG, "entered sendUpdatesToUI");

            LogMediaName();
            //  LogMediaName();
            handler.postDelayed(this, 1000); // 2 seconds

        }
    };



    // ---Send seekbar info to activity----
    private void setupHandler() {
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            // // Log.d(TAG, "entered sendUpdatesToUI");

            LogMediaPosition();
          //  LogMediaName();

            handler.postDelayed(this, 1000); // 2 seconds

        }
    };


    public boolean isPlayingg()
    {

        if ( player != null && player.isPlaying() )
            return true;
        else
            return false;
    }

    private void LogMediaPosition() {
        // // Log.d(TAG, "entered LogMediaPosition");
        if (player.isPlaying()) {
            mediaPosition = player.getCurrentPosition();
            cPostion=Constant.postion;
            // if (mediaPosition < 1) {
            // Toast.makeText(this, "Buffering...", Toast.LENGTH_SHORT).show();
            // }
            mediaMax = player.getDuration();
            //seekIntent.putExtra("time", new Date().toLocaleString());
            seekIntent.putExtra("counter", String.valueOf(mediaPosition));
            seekIntent.putExtra("cPosition", String.valueOf(mediaPosition));
            seekIntent.putExtra("mediamax", String.valueOf(mediaMax));
            seekIntent.putExtra("song_ended", String.valueOf(songEnded));
            sendBroadcast(seekIntent);
        }
    }

      /// textview Song name



    private void LogMediaName() {
        // // Log.d(TAG, "entered LogMediaPosition");
        if (player.isPlaying()) {
           // mediaPosition = player.getCurrentPosition();
            cPostion=Constant.postion;
            // if (mediaPosition < 1) {
            // Toast.makeText(this, "Buffering...", Toast.LENGTH_SHORT).show();
            // }
           // mediaMax = player.getDuration();
            //seekIntent.putExtra("time", new Date().toLocaleString());
           // seekIntent.putExtra("counter", String.valueOf(mediaPosition));
            textIntent.putExtra("cPosition", String.valueOf(Constant.playListName.get(Constant.postion)));
          //  seekIntent.putExtra("mediamax", String.valueOf(mediaMax));
          //  seekIntent.putExtra("song_ended", String.valueOf(songEnded));
            sendBroadcast(textIntent);
        }
    }









    // --Receive seekbar position if it has been changed by the user in the
    // activity
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateSeekPos(intent);
        }
    };

    // Update seek position from Activity
    public void updateSeekPos(Intent intent) {
        int seekPos = intent.getIntExtra("seekpos", 0);
        if (player.isPlaying()) {
            handler.removeCallbacks(sendUpdatesToUI);
            player.seekTo(seekPos);
            setupHandler();
        }

    }



    //pass song list
    public void setList(ArrayList<String> theSongs){
        songs=theSongs;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

        /*songEnded=1;*/

    }

    // started.
    private void sendBufferingBroadcast() {
        // Log.v(TAG, "BufferStartedSent");
        bufferIntent.putExtra("buffering", "1");
        sendBroadcast(bufferIntent);
    }

    // Send a message to Activity that audio is prepared and ready to start
    // playing.
    private void sendBufferCompleteBroadcast() {
        // Log.v(TAG, "BufferCompleteSent");
        bufferIntent.putExtra("buffering", "0");
        sendBroadcast(bufferIntent);
    }

    // Send a message to Activity to reset the play button.
    private void resetButtonPlayStopBroadcast() {
        // Log.v(TAG, "BufferCompleteSent");
        bufferIntent.putExtra("buffering", "2");
        sendBroadcast(bufferIntent);
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    //binder
    public class MusicBinder extends Binder {
        public MusicServiceSemsm getService() {
            return MusicServiceSemsm.this;
        }
    }

    //activity will bind to service
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    //release resources when unbind
    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    //play a song
    public void playSong(){





        player.reset();

        try{
            if (Constant.playListUrl.size()>0) {


                if (Constant.pointer == 1) {
                    player.setDataSource(Constant.playListUrl.get(Constant.postion));

                } else {

                    player.setDataSource(Constant.playListUrl.get(Constant.postion));

                }
            }
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        sendBufferingBroadcast();
        player.prepareAsync();



        // --- Set up seekbar handler ---
        setupHandler();


        songEnded=0;

    }

    //set the song
    public void setSong(int songIndex){
        songPosn=songIndex;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //check if playback has reached the end of a track
    /*    if(Constant.postion){
            mp.reset();
             Constant.postion++;

           playSong();
        }
*/

      //  setUpSongNameText();
        songEnded=1;

        if (Constant.playListUrl.size()>0) {

            if (Constant.postion + 1 >= Constant.playListName.size()) {
                mp.pause();
                //lastPosition=Constant.playListName.size()-1;
                Constant.postion = 0;
                //newPos=0;

                //  new AlbumFragment().songNametxt.setText(Constant.playListName.get(Constant.postion));

                playSong();

                sharedPreferences = getSharedPreferences("playing", Context.MODE_PRIVATE);
                String played = sharedPreferences.getString("track" + Constant.idList.get(Constant.postion), "");
                editor = sharedPreferences.edit();
                editor.putString("track" + Constant.idList.get(Constant.postion), "played");
                editor.commit();

            } else {
                mp.pause();
                Constant.postion++;


                sharedPreferences = getSharedPreferences("playing", Context.MODE_PRIVATE);
                String played = sharedPreferences.getString("track" + Constant.idList.get(Constant.postion), "");
                editor = sharedPreferences.edit();
                editor.putString("track" + Constant.idList.get(Constant.postion), "played");
                editor.commit();

                playSong();

            }
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.v("MUSIC PLAYER", "Playback Error");
        mp.reset();
    /*    switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Toast.makeText(this,
                        "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra,
                        Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Toast.makeText(this, "MEDIA ERROR SERVER DIED " + extra,
                        Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Toast.makeText(this, "MEDIA ERROR UNKNOWN " + extra,
                        Toast.LENGTH_SHORT).show();
                break;
        }*/
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        sendBufferCompleteBroadcast();



        //start playback
        mp.start();


  /*      if (Constant.pointer == 1)
        {
            customSimpleNotification(this,Constant.playListName.get(Constant.postion),Constant.playListName.get(Constant.postion));}
        else {
            customSimpleNotification(this,Constant.songName,Constant.songName);
        }*/
        if (Constant.playListUrl.size()>0) {

            customSimpleNotification(this, Constant.playListName.get(Constant.postion), Constant.playListName.get(Constant.postion));

        }


       // customSimpleNotification(this,Constant.playListName.get(Constant.postion),Constant.playListName.get(Constant.postion));

    }

    //playback methods
    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }

    //skip to previous track
    public void playPrev(){
        songPosn--;
        if(songPosn<0) songPosn=songs.size()-1;
        playSong();
    }

    //skip to next
    public void playNext(){

        int index= Constant.postion++;
        Constant.postion=index;
        //  Constant.songUrl=Constant.playListUrl.get(index);
        if(index >=Constant.playListUrl.size()) {
            Constant.postion = 0;
        }

        playSong();
    }


       /* if(shuffle){
          *//*  int newSong = songPosn;
            while(newSong==songPosn){
                newSong=rand.nextInt(songs.size());
            }
            songPosn=newSong;*//*
        }
        else{
           int index= Constant.postion++;
            Constant.songUrl=Constant.playListUrl.get(index);
            if(index >=Constant.playListUrl.size())

                Constant.postion=0;
        }
        playSong();*/
    // }








    @Override
    public void onDestroy() {
        stopForeground(true);

      /*  if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
        }*/

   /*     if (player != null) {
            if (player.isPlaying()) {

                nm.cancelAll();
                nm.cancel(1);
            }

        }*/
      /*  NotificationManager notifManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();*/
/*
        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();*/



        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();

        if (phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener,
                    PhoneStateListener.LISTEN_NONE);
        }


        // Stop the seekbar handler from sending updates to UI
        handler.removeCallbacks(sendUpdatesToUI);



        // Unregister seekbar receiver
        unregisterReceiver(broadcastReceiver);
    }

    //toggle shuffle
    public void setShuffle(){
        if(shuffle) shuffle=false;
        else shuffle=true;
    }

/*    public void inti(){


        startTimeTxt = (TextView) findViewById(R.id.startTimetxt);
        songNametxt = (TextView) findViewById(R.id.songNametxt);
        finalTimeTxt = (TextView) findViewById(R.id.endTimetxt);


        btn_play= (Button) findViewById(R.id.btnPlayerPlay);
        btn_back= (Button) findViewById(R.id.btnPlayerBack);
        btn_next= (Button) findViewById(R.id.btnPlayerNext);

        seekBar= (SeekBar) findViewById(R.id.seekBar2);
    }*/




}
/*

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.music.test.musicpro.Activity.AlbumContentActivity;
import com.music.test.musicpro.Fragment.AlbumFragment;
import com.music.test.musicpro.Other.Constant;
import com.music.test.musicpro.R;

import java.io.IOException;
import java.util.ArrayList;

public class MusicServiceSemsm extends Service  implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<String> songs;
    //current position
    private int songPosn;



    public void onCreate(){
        //create the service
        //create the service
        super.onCreate();
//initialize position
        songPosn=0;
//create player
        player = new MediaPlayer();


        initMusicPlayer();
    }


    public void setSong(int songIndex){
        songPosn=songIndex;
    }

    public void initMusicPlayer(){
        //set player properties

        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }



    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

    }
    public void setList(ArrayList<String> theSongs){
        songs=theSongs;
    }


    public void playSong(){
        //play a song

//        player.reset();
        String trackUri= Constant.songUrl;

        try{
            player.setDataSource(trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

        player.prepareAsync();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }


    public class MusicBinder extends Binder {

        private final IBinder musicBind = new MusicBinder();

        public MusicServiceSemsm getService() {
            return MusicServiceSemsm.this;
        }


        public IBinder onBind(Intent intent) {
            return musicBind;
        }



    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }
}*/
