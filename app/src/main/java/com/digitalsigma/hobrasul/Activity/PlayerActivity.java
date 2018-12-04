package com.digitalsigma.hobrasul.Activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;
import com.digitalsigma.hobrasul.Service.MusicServiceSemsm;

import net.alhazmy13.catcho.library.Catcho;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by AhmedAbouElFadle on 11/29/2016.
 */
public class PlayerActivity extends Activity {

    ArrayList<String> downloadedSongUrl=new ArrayList<String>();
    ArrayList<String> downloadedSongName=new ArrayList<String>();
    ArrayList<String> downloadedSongUrl1=new ArrayList<String>();
    ArrayList<String> downloadedSongName1=new ArrayList<String>();

    // Set up the notification ID
    private static final int NOTIFICATION_ID = 1;
    private boolean isPausedInCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;


    AlertDialog.Builder alertDialog;


    // --Seekbar variables --
    private int seekMax;
    private int cPosItion;
    private static int songEnded = 0;
    boolean mBroadcastIsRegistered;


    // --Set up constant ID for broadcast of seekbar position--
    public static final String BROADCAST_SEEKBAR = "com.gmsproduction.tarekelsheikh.sendseekbar";
    Intent intent;






    public Button btn_play;
    Button btn_next;
    Button btn_back;
    Button btn_CallTone;
    Button btn_Fav;
    Button btn_backPress;

    TextView startTimeTxt;
    TextView songNametxt;
    TextView finalTimeTxt;

    SeekBar seekBar;




    //service
    private MusicServiceSemsm musicSrv;
    private Intent playIntent;
    //binding
    private boolean musicBound=false;



    String songName,songUrl;
    Runnable runnable;


    private Button b1,b2_pause,b3_play,b4;
    private ImageView iv;

    private double startTime = 0;
    private double finalTime = 0;

    private Handler myHandler = new Handler();;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1,tx2,tx3_songName;

    public static int oneTimeOnly = 0;
    Typeface t1;

private MediaPlayer mediaPlayer;

    View toneViewSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Catcho.Builder(this)
                .activity(ContactActivity.class)
                .build();


        setContentView(R.layout.activity_player);

        inti();
        setLisner();


        t1= Typeface.createFromAsset(getAssets(),"hacen.ttf");

        songNametxt.setTypeface(t1);
        startTimeTxt.setTypeface(t1);
        finalTimeTxt.setTypeface(t1);


        btn_play.setBackground(getResources().getDrawable(R.drawable.stop_button_light_new));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int seekPos = seekBar.getProgress();
                    intent.putExtra("seekpos", seekPos);
                    sendBroadcast(intent);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




        intent = new Intent(BROADCAST_SEEKBAR);



        registerReceiver(broadcastReceiver, new IntentFilter(
                MusicServiceSemsm.BROADCAST_ACTION));





        mBroadcastIsRegistered = true;

       //play();



    }

    public void settingPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 200);

            }
        }
    }


    // -- Broadcast Receiver to update position of seekbar from service --
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent serviceIntent) {
            updateUI(serviceIntent);
            //    updateUIText(serviceIntent);
        }
    };

    private void updateUI(Intent serviceIntent) {
        String counter = serviceIntent.getStringExtra("counter");
        String mediamax = serviceIntent.getStringExtra("mediamax");
        String strSongEnded = serviceIntent.getStringExtra("song_ended");
        String cPos=serviceIntent.getStringExtra("cPostion");
        int seekProgress = Integer.parseInt(counter);
        seekMax = Integer.parseInt(mediamax);
        songEnded = Integer.parseInt(strSongEnded);
        seekBar.setMax(seekMax);
        seekBar.setProgress(seekProgress);


        finalTime=Double.valueOf(mediamax);
        startTime=Double.valueOf(counter);


        songNametxt.setText(Constant.songName);














        if (Constant.playListName.size()>0) {
            songNametxt.setText(Constant.playListName.get(Constant.postion));

            if (!(musicSrv == null)) {

                if (musicSrv.isPlayingg()) {

                    btn_play.setEnabled(true);
                    btn_back.setEnabled(true);
                    btn_next.setEnabled(true);
                    btn_CallTone.setEnabled(true);
                    btn_Fav.setEnabled(true);


                    btn_play.setBackground(getResources().getDrawable(R.drawable.stop_button_light_new));


                    // btn_play.setText("Pause");
                } else {
                    btn_play.setBackground(getResources().getDrawable(R.drawable.play_button_light_new));

                    //  btn_play.setText("Play");


                }
            }
        }














        finalTimeTxt.setText(String.format("%d:%d ",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)))
        );

        startTimeTxt.setText(String.format("%d:%d ",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime)))
        );

        //Toast.makeText(getActivity(), "cc"+cPos, Toast.LENGTH_SHORT).show();

        if (startTime == finalTime)
        {
            // songNametxt.setText(Cona.get(Integer.valueOf(Constant.postion+1)));

        }


        if (songEnded == 1) {
            // buttonPlayStop.setBackgroundResource(R.drawable.playbuttonsm);
            // Toast.makeText(getActivity(), "cc", Toast.LENGTH_SHORT).show();

        }
    }

 /*   @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();
    }*/

    public void play()
    {
        //trackPlayingicon(Constant.postion);
        musicSrv.playSong();
        songNametxt.setText(Constant.playListName.get(Constant.postion));



        //  customSimpleNotification(getActivity(),searchnameResult.get(Constant.postion),Constant.album_name);
        registerReceiver(broadcastReceiver, new IntentFilter(
                MusicServiceSemsm.BROADCAST_ACTION));

        mBroadcastIsRegistered = true;

    }

    @Override
    public void onPause() {
        super.onPause();

        if (musicSrv != null && MusicServiceSemsm.player != null && MusicServiceSemsm.player.isPlaying()) {

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
                            if (musicSrv != null) {
                                musicSrv.pausePlayer();

                                //  MusicServiceSemsm.player.pause();
                                isPausedInCall = true;
                            }

                            break;
                        case TelephonyManager.CALL_STATE_IDLE:
                            // Phone idle. Start playing.
                            if (musicSrv != null) {
                                if (isPausedInCall) {
                                    isPausedInCall = false;

                                  //  musicSrv.go();
                                    // MusicServiceSemsm.player.start();
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
    }




    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicServiceSemsm.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    public void inti(){
        // profileImage= (ImageView) findViewById(R.id.profileImageView);


        startTimeTxt = (TextView) findViewById(R.id.first);
        songNametxt = (TextView) findViewById(R.id.name);
        finalTimeTxt = (TextView) findViewById(R.id.finaltime);


        btn_play= (Button) findViewById(R.id.btn_play_pause);
        btn_back= (Button) findViewById(R.id.btnPlay_back);
        btn_next= (Button) findViewById(R.id.btn_next_player);
        btn_backPress= (Button) findViewById(R.id.playerbackbtn);

        btn_CallTone= (Button) findViewById(R.id.btn_call_tone_player);
        btn_Fav= (Button) findViewById(R.id.btn_fav_player);

        seekBar= (SeekBar) findViewById(R.id.seekBar);


       // progressActivity = (ProgressActivity) findViewById(R.id.progress);
    }


    private void shareTrackUrl(String trackUrl,String trackName) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, trackName);
        share.putExtra(Intent.EXTRA_TEXT, trackUrl);
        startActivity(Intent.createChooser(share, "Share to..."));
    }

    public void setLisner()
    {

        btn_Fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
                File file = new File(Environment.getExternalStorageDirectory(), SDCardRoot+"/TarekElSheikh");

                if (file.exists()){
                    //  Toast.makeText(DownLoadsActivity.this, "exist", Toast.LENGTH_SHORT).show();
                    File[] files = file.listFiles();

                    Log.d("Files", "Size: "+ files.length);
                    //  Toast.makeText(DownLoadsActivity.this, ""+files[0], Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < files.length; i++)
                    {


                        downloadedSongUrl.add(files[i].toString());
                        downloadedSongName.add(files[i].getName());
                        //  Toast.makeText(DownLoadsActivity.this, "url lem"+downloadedSongName.get(0), Toast.LENGTH_SHORT).show();

                    }

                }else {
                    Toast.makeText(PlayerActivity.this, "Not Exist" +Environment.getExternalStorageDirectory()+"/TarekElSheikh" , Toast.LENGTH_SHORT).show();
                }

                if (downloadedSongName.contains(Constant.playListName.get(Constant.postion)))
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(PlayerActivity.this);

                    //"لكى تكون كول تون لهاتفك "+Constant.playListName.get(position)+"لقد اخترت "
                    builder.setMessage("الاغنيه موجود فى التحميلات");

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {


                            dialog.dismiss();
                        }
                    });

                           /* builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // Do nothing
                                    dialog.dismiss();
                                }
                            });*/

                    AlertDialog alert = builder.create();
                    alert.show();

                }
                else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(PlayerActivity.this);

                    //"لكى تكون كول تون لهاتفك "+Constant.playListName.get(position)+"لقد اخترت "
                    builder.setMessage("هل تريد تحميل اغنية ' "+Constant.playListName.get(Constant.postion)+" '");

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            downloadong1(Constant.playListUrl.get(Constant.postion), Constant.playListName.get(Constant.postion));
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                    // Toast.makeText(getActivity(), "pos"+searchnameResult.get(Constant.postion), Toast.LENGTH_SHORT).show();
                }
            }
        });




        btn_CallTone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                settingPermission();
                toneViewSnackbar=v;
               // toneViewSnackBar=v;

                //   calltoneChecker(Constant.postion);
                //   callTone(Constant.postion);
                if (Constant.playListName.size()>0) {


                    final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(PlayerActivity.this);
                    View parentView=getLayoutInflater().inflate(R.layout.ringtone_calltone_action_sheet,null);
                    bottomSheetDialog.setContentView(parentView);
                    BottomSheetBehavior bottomSheetBehavior=BottomSheetBehavior.from((View)parentView.getParent());
                    bottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,getResources().getDisplayMetrics()));
                    bottomSheetDialog.show();

                    final Button btn_ringtone= (Button) parentView.findViewById(R.id.btn_ringTone);
                    final Button btn_calltone= (Button) parentView.findViewById(R.id.btn_callTone);

                    btn_ringtone.setTypeface(t1);
                    btn_calltone.setTypeface(t1);

                    btn_ringtone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int pos = Constant.postion;


                            if (Constant.RingToneUrl.size()>0)
                            {
                            if (Constant.RingToneUrl.get(pos).equals("") || Constant.RingToneUrl.get(pos).equals(null)) {
                                Snackbar snackbar = Snackbar
                                        .make(v, "لايوجد رابط ", Snackbar.LENGTH_LONG);

                                snackbar.show();
                            } else {
                                File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
                                File file = new File(Environment.getExternalStorageDirectory(), SDCardRoot + "/TarekElSheikhRingTone");

                                if (file.exists()) {
                                    //  Toast.makeText(DownLoadsActivity.this, "exist", Toast.LENGTH_SHORT).show();
                                    File[] files = file.listFiles();

                                    Log.d("Files", "Size: " + files.length);
                                    //  Toast.makeText(DownLoadsActivity.this, ""+files[0], Toast.LENGTH_SHORT).show();
                                    for (int i = 0; i < files.length; i++) {


                                        downloadedSongUrl1.add(files[i].toString());
                                        downloadedSongName1.add(files[i].getName());
                                        //  Toast.makeText(DownLoadsActivity.this, "url lem"+downloadedSongName.get(0), Toast.LENGTH_SHORT).show();

                                    }

                                    if (downloadedSongName1.contains(Constant.playListName.get(Constant.postion))) {

                                        File SDCardRoot1 = Environment.getExternalStorageDirectory(); // location where you want to store
                                        File file1 = new File(Environment.getExternalStorageDirectory(), SDCardRoot + "/TarekElSheikhRingTone/" +
                                                Constant.playListName.get(Constant.postion));

                                        ringtone(file1.toString() + "");
                                        //   Toast.makeText(TracksActivity.this, "aa", Toast.LENGTH_SHORT).show();

                                /*    AlertDialog.Builder builder = new AlertDialog.Builder(TracksActivity.this);

                                    //"لكى تكون كول تون لهاتفك "+Constant.playListName.get(position)+"لقد اخترت "
                                    builder.setMessage("النشيد موجود فى التحميلات");

                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {


                                            dialog.dismiss();
                                        }
                                    });

                           *//* builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // Do nothing
                                    dialog.dismiss();
                                }
                            });*//*

                                    AlertDialog alert = builder.create();
                                    alert.show();*/

                                    } else {

                                        downloadong(Constant.RingToneUrl.get(pos), Constant.playListName.get(pos),
                                                "TarekElSheikhRingTone");
                                    }










                                    Toast.makeText(PlayerActivity.this, "لقد اخترت ' " + Constant.playListName.get(Constant.postion) + "' علشان تبقى رنة موبايلك", Toast.LENGTH_SHORT).show();


                                } else {
                                    downloadong(Constant.RingToneUrl.get(pos), Constant.playListName.get(pos),
                                            "TarekElSheikhRingTone");                                }


                            }


                        }
                            else {
                                Toast.makeText(PlayerActivity.this, "انتظر رابط التحميل قريبا ", Toast.LENGTH_SHORT).show();
                            }
                            bottomSheetDialog.hide();

                        }
                    });

                    btn_calltone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                            String number = tm.getNetworkOperatorName();
                            if (number.equals("") || number.equals(null)|| number.equals("null"))
                            {
                                Snackbar snackbar = Snackbar
                                        .make(v, "لايوجد بالهاتف شريحه ", Snackbar.LENGTH_LONG);

                                snackbar.show();
                            }
                            else {
                                calltoneChecker();
                                bottomSheetDialog.hide();

                            }



                        }
                    });



                   /* final RadioGroup radioTones= (RadioGroup) parentView.findViewById(R.id.radioGroupTones);

                    final RadioButton radioCalltone= (RadioButton) parentView.findViewById(R.id.radioCalltone);
                    final RadioButton radioRingtone= (RadioButton) parentView.findViewById(R.id.radioRingTone);
                    final Button confirm= (Button) parentView.findViewById(R.id.confirmToneType);



                    radioCalltone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            radioTones.check(R.id.radioCalltone);

                        }
                    });


                    radioRingtone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            radioTones.check(R.id.radioRingTone);

                        }
                    });



                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (radioRingtone.isChecked())
                            {
                                int pos=Constant.postion;

                                downloadong(Constant.playListUrl.get(pos),Constant.playListName.get(pos),
                                        "MusicProRingTone");
                                bottomSheetDialog.hide();




                            }else if (radioCalltone.isChecked())
                            {

                                TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                                String number = tm.getNetworkOperatorName();
                                if (number.equals("") || number.equals(null))
                                {
                                    Snackbar snackbar = Snackbar
                                            .make(v, "لايوجد بالهاتف شريحه ", Snackbar.LENGTH_LONG);

                                    snackbar.show();
                                }
                                else {
                                    calltoneChecker(Constant.postion);
                                    bottomSheetDialog.hide();

                                }

                            }
                        }
                    });

*/




                   /* FragmentManager fm = getFragmentManager();
                    RingFragmentDialog dialogFragment = new RingFragmentDialog();
                    dialogFragment.show(fm, "ring");*/
                } else {
                    Snackbar snackbar = Snackbar
                            .make(v, "اختر النغمه التى تريد الاستماع اليها :( !!", Snackbar.LENGTH_LONG);

                    snackbar.show();

                }
            }
        });


        btn_backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (musicSrv.isPlayingg())
                {
                    btn_play.setBackground(getResources().getDrawable(R.drawable.play_button_light_new));

                    musicSrv.pausePlayer();
                  //  MusicServiceSemsm.player.pause();

                }
                else
                {
                    btn_play.setBackground(getResources().getDrawable(R.drawable.stop_button_light_new));
                    // musicSrv.pausePlayer();
                    musicSrv.go();
                  //  MusicServiceSemsm.player.start();
                }

             /*   if (btn_play.getBackground(getResources().getDrawable(R.drawable.stop_icon)))
                {

                   // btn_play.setText("Play");

                    btn_play.setBackground(getResources().getDrawable(R.drawable.play_button));
                    // musicSrv.pausePlayer();
                    MusicServiceSemsm.player.pause();


                }
                else {

                   // btn_play.setText("Pause");
                    btn_play.setBackground(getResources().getDrawable(R.drawable.stop_icon));

                    MusicServiceSemsm.player.start();


                }
*/
            }
        });






        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (Constant.postion > 0)
                {

                    //  lastPosition=Constant.postion;

                    Constant.postion--;
                    musicSrv.pausePlayer();

                    play();





                }
                else {
                    //  lastPosition=Constant.postion;
                    Constant.postion=Constant.playListUrl.size()-1;
                    musicSrv.pausePlayer();
                    play();

                }
            }
        });






        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Constant.postion<(Constant.playListName.size()-1))
                {
                    //   lastPosition=Constant.postion;
                    Constant.postion++;
                    musicSrv.pausePlayer();

                    // player.pause();
                    play();
                    //  playSong();

                }
                else {


                    ///      lastPosition=searchnameResult.size()-1;
                    Constant.postion=0;
                    musicSrv.pausePlayer();

                    // player.pause();
                    play();
                    //   playSong();


                }
            }
        });







    }


    public void confirmDailog(final String network)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //"لكى تكون كول تون لهاتفك "+Constant.playListName.get(position)+"لقد اخترت "
        builder.setMessage("لقد اخترت '"+Constant.songName+"' علشان تكون كول تون لموبيلك");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Constant.networkName=network;

                startActivity(new Intent(PlayerActivity.this,DialActivity.class));
             //   Toast.makeText(PlayerActivity.this, ""+Constant.ussd, Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void calltoneChecker()
    {
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String number = tm.getNetworkOperatorName();
   //     Toast.makeText(PlayerActivity.this, "tel "+number, Toast.LENGTH_SHORT).show();

        if (number.toLowerCase().contains("vodaf"))
        {
            if (!(Constant.serachCalltoneVod.equals("") || Constant.serachCalltoneVod.equals("null"))) {
                Constant.ussd=Constant.serachCalltoneVod;
           //     Toast.makeText(PlayerActivity.this, "vodafone code find" +Constant.ussd, Toast.LENGTH_SHORT).show();


                confirmDailog("vod");


     /*           alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Are you sure to make this your calltone");
                // view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
                // alertDialog.setView(view);

                Constant.networkName="vod";



                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Constant.networkName="vod";
                        startActivity(new Intent(PlayerActivity.this,DialActivity.class));
                        Toast.makeText(PlayerActivity.this, ""+Constant.ussd, Toast.LENGTH_SHORT).show();



                    }
                });
                alertDialog.show();*/
            }
            else {
                Constant.ussd="";
             //    Toast.makeText(PlayerActivity.this, "no code here", Toast.LENGTH_SHORT).show();

                Snackbar snackbar = Snackbar
                        .make(toneViewSnackbar, "لا يوجد كول تون فى هذه الشبكه :( !!", Snackbar.LENGTH_LONG);


                snackbar.show();
            }


          /*  if (Constant.ussd.equals("") ||Constant.ussd.equals(null))
            {


            }
            else {





            }*/



           // Toast.makeText(PlayerActivity.this, "call   true", Toast.LENGTH_SHORT).show();
        }
        else if (number.toLowerCase().contains("etis"))
        {

            if (!(Constant.serachCalltoneEt.equals("")||Constant.serachCalltoneEt.equals("null"))) {
                Constant.ussd=Constant.serachCalltoneEt;



                confirmDailog("et");



     /*           alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Are you sure to make this your calltone");
                // view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
                // alertDialog.setView(view);

                Constant.networkName="et";


                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Constant.networkName="et";
                        startActivity(new Intent(PlayerActivity.this,DialActivity.class));
                        Toast.makeText(PlayerActivity.this, ""+Constant.ussd, Toast.LENGTH_SHORT).show();



                    }
                });
                alertDialog.show();*/
             //   Toast.makeText(PlayerActivity.this, "elislate code find" +Constant.ussd, Toast.LENGTH_SHORT).show();
            }
            else {
                Constant.ussd="";
              //  Toast.makeText(PlayerActivity.this, "no call tone code here", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar
                        .make(toneViewSnackbar, "لا يوجد كول تون فى هذه الشبكه :( !!", Snackbar.LENGTH_LONG);


                snackbar.show();            }


/*
            if (Constant.ussd.equals("") ||Constant.ussd.equals(null))
            {

              //  Toast.makeText(getActivity(), "no call tone code here", Toast.LENGTH_SHORT).show();

                Snackbar snackbar = Snackbar
                        .make(getView(), "No Calltone here Sorry :( !!", Snackbar.LENGTH_LONG);


                snackbar.show();

            }
            else {

                alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Are you sure to make this your calltone");
                // view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
                // alertDialog.setView(view);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(),DialActivity.class));
                        Toast.makeText(getActivity(), ""+Constant.ussd, Toast.LENGTH_SHORT).show();



                    }
                });
                alertDialog.show();


            }*/

        }
        else if (number.toLowerCase().contains("oran"))
        {

            if (!(Constant.serachCalltoneOran.equals("") || Constant.serachCalltoneOran.equals("null"))) {

                Constant.ussd=Constant.serachCalltoneOran;

confirmDailog("or");

                /*alertDialog = new AlertDialog.Builder(PlayerActivity.this);
                alertDialog.setTitle("Are you sure to make this your calltone");
                // view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
                // alertDialog.setView(view);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Constant.networkName="or";
                        startActivity(new Intent(PlayerActivity.this,DialActivity.class));
                        Toast.makeText(PlayerActivity.this, ""+Constant.ussd, Toast.LENGTH_SHORT).show();



                    }
                });
                alertDialog.show();*/


//                Toast.makeText(PlayerActivity.this, "orange code find" +Constant.ussd, Toast.LENGTH_SHORT).show();
            }
            else {
                Constant.ussd="";
              //  Toast.makeText(PlayerActivity.this, "no calltone here", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar
                        .make(toneViewSnackbar, "لا يوجد كول تون فى هذه الشبكه :( !!", Snackbar.LENGTH_LONG);


                snackbar.show();
            }
/*

            if (Constant.ussd.equals("") ||Constant.ussd.equals(null))
            {

                Snackbar snackbar = Snackbar
                        .make(getView(), "No Calltone here Sorry :( !!", Snackbar.LENGTH_LONG);


                snackbar.show();
            }
            else {


                alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Are you sure to make this your calltone");
                // view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
                // alertDialog.setView(view);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(),DialActivity.class));
                        Toast.makeText(getActivity(), ""+Constant.ussd, Toast.LENGTH_SHORT).show();



                    }
                });
                alertDialog.show();
*/



            //    }





        }else if (number.toLowerCase().contains("mobin"))
        {
            if (!(Constant.serachCalltoneOran.equals("") || Constant.serachCalltoneOran.equals(""))) {

                Constant.ussd=Constant.serachCalltoneOran;


                confirmDailog("or");
/*
                alertDialog = new AlertDialog.Builder(PlayerActivity.this);
                alertDialog.setTitle("Are you sure to make this your calltone");
                // view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
                // alertDialog.setView(view);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Constant.networkName="or";
                        startActivity(new Intent(PlayerActivity.this,DialActivity.class));
                        Toast.makeText(PlayerActivity.this, ""+Constant.ussd, Toast.LENGTH_SHORT).show();



                    }
                });
                alertDialog.show();*/






          //      Toast.makeText(PlayerActivity.this, "orange code find" +Constant.ussd, Toast.LENGTH_SHORT).show();
            }
            else {
                Constant.ussd="";
             //   Toast.makeText(PlayerActivity.this, "No Calltone code here", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar
                        .make(toneViewSnackbar, "لا يوجد كول تون فى هذه الشبكه :( !!", Snackbar.LENGTH_LONG);


                snackbar.show();
            }

            /*if (Constant.ussd.equals("") ||Constant.ussd.equals(null))
            {

                Snackbar snackbar = Snackbar
                        .make(getView(), "No Calltone here Sorry :( !!", Snackbar.LENGTH_LONG);


                snackbar.show();
            }
            else {


                alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Are you sure to make this your calltone");
                // view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
                // alertDialog.setView(view);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(),DialActivity.class));
                        Toast.makeText(getActivity(), ""+Constant.ussd, Toast.LENGTH_SHORT).show();



                    }
                });
                alertDialog.show();



            }

*/
        }
    }





    public void downloadong(final String url ,String name,String folderName) {
      /*  thread = new Thread() {
            @Override
            public void run() {*/
        //Toast.makeText(getActivity(), "download", Toast.LENGTH_SHORT).show();
        File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
        File directory = new File(SDCardRoot, "/"+folderName+"/"); //create directory to keep your downloaded file

        // Toast.makeText(getActivity(), SDCardRoot + "/MusicPro/", Toast.LENGTH_SHORT).show();
        if (!directory.exists()) {
            directory.mkdir();
        }


        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(url));

        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(Constant.playListName.get(Constant.postion))
                .setDescription("Song is Being Downloaded......")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(String.valueOf(directory), name);

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(req);
        if (!folderName.equals("TarekElSheikh")) {
            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
/*
            }
        };*/

    }
    BroadcastReceiver onComplete=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
            File file = new File(Environment.getExternalStorageDirectory(), SDCardRoot+"/TarekElSheikhRingTone/"+
                    Constant.playListName.get(Constant.postion));

            ringtone(file.toString()+"");
           // Toast.makeText(PlayerActivity.this, "finish", Toast.LENGTH_SHORT).show();

        }
    };
    public void ringtone(String path)
    {
        // Create File object for the specified ring tone path
        File f=new File(path);




        ContentValues content = new ContentValues();
        content.put(MediaStore.MediaColumns.DATA,f.getAbsolutePath());
        content.put(MediaStore.MediaColumns.TITLE, Constant.songName);
        content.put(MediaStore.MediaColumns.SIZE, 215454);
        content.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
        //  content.put(MediaStore.Audio.Media.ARTIST, "Madonna");
        content.put(MediaStore.Audio.Media.DURATION, 230);
        content.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        content.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        content.put(MediaStore.Audio.Media.IS_ALARM, true);
        content.put(MediaStore.Audio.Media.IS_MUSIC, true);


 /*       String Ringtonepath= "content://media/internal/audio/media/297";
        Uri Ringtone1 = Uri.parse(path);*/
        //Insert it into the database
        Log.i("TAG", "the absolute path of the file is :"+
                f.getAbsolutePath());
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(
                f.getAbsolutePath());




        getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + f.getAbsolutePath() + "\"",
                null);
        Uri newUri = getContentResolver().insert(uri, content);
        System.out.println("uri=="+uri);
        Log.i("TAG","the ringtone uri is :"+newUri);
        RingtoneManager.setActualDefaultRingtoneUri(
                getApplicationContext(), RingtoneManager.TYPE_RINGTONE,
                newUri);


/*
       // sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,Uri.parse("file://"+f.toString())));

        ContentResolver contentResolver=getContentResolver();

        //Cursor cursor=contentResolver.query(f.getAbsolutePath(),)

        // Insert the ring tone to the content provider
        ContentValues value=new ContentValues();
        Long current=System.currentTimeMillis();

        value.put(MediaStore.MediaColumns.DATA, f.getAbsolutePath());
        value.put(MediaStore.MediaColumns.TITLE, downloadedSongName.get(Constant.postion));
        value.put(MediaStore.MediaColumns.SIZE, f.length());
        value.put(MediaStore.Audio.Media.DATE_ADDED,(int) (current / 1000));

        value.put(MediaStore.MediaColumns.MIME_TYPE,"audio*//*");
        value.put(MediaStore.Audio.Media.ARTIST, "artist");
        value.put(MediaStore.Audio.Media.DURATION, 500);
        value.put(MediaStore.Audio.Media.IS_ALARM, false);
        value.put(MediaStore.Audio.Media.IS_MUSIC, false);
        value.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        value.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        ContentResolver cr=getContentResolver();
        Uri url= MediaStore.Audio.Media.getContentUriForPath(f.getAbsolutePath());
*//*        getContentResolver().delete(
                url,
                MediaStore.MediaColumns.DATA + "=\""
                        + f.getAbsolutePath() + "\"", null);*//*
        Uri newUri = contentResolver.insert(url, content);

        if (Uri.EMPTY.equals(newUri))
        {
            Toast.makeText(DownLoadsActivity.this, "emppppty", Toast.LENGTH_SHORT).show();
        }else {
//            String nn =newUri.toString();
          //  Toast.makeText(DownLoadsActivity.this, "not em"+ nn, Toast.LENGTH_SHORT).show();


        }

        //Uri addedUri=cr.insert(url, value);
       // Uri aa=getContentResolver().insert(url,value);
        // Set default ring tone

      //  f=addedUri;

        Log.d("uri ring tone   ",url.toString());
        Log.d("uri value   ",value.toString());
       // Log.d("uri value   ",aa.toString());


      //  Settings.System.putString(cr,Settings.System.RINGTONE,url.toString());

      //  Uri newUri=Uri.parse(path);

        RingtoneManager.setActualDefaultRingtoneUri(DownLoadsActivity.this, RingtoneManager.TYPE_RINGTONE,newUri);*/
    }



    public void downloadong1(final String url ,String name) {
      /*  thread = new Thread() {
            @Override
            public void run() {*/
        //Toast.makeText(getActivity(), "download", Toast.LENGTH_SHORT).show();
        File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
        File directory = new File(SDCardRoot, "/TarekElSheikh/"); //create directory to keep your downloaded file

        // Toast.makeText(getActivity(), SDCardRoot + "/MusicPro/", Toast.LENGTH_SHORT).show();
        if (!directory.exists()) {
            directory.mkdir();
        }


        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(url));

        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(Constant.playListName.get(Constant.postion))
                .setDescription("Song is Being Downloaded......")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(String.valueOf(directory), name);

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(req);
/*
            }
        };*/

    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicServiceSemsm.MusicBinder binder = (MusicServiceSemsm.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(Constant.playListUrl);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.player_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.download_action) {

       //     Toast.makeText(PlayerActivity.this, "download", Toast.LENGTH_SHORT).show();
            File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
            File directory = new File(SDCardRoot, "/MusicPro/"); //create directory to keep your downloaded file

       //     Toast.makeText(PlayerActivity.this, SDCardRoot+"/MusicPro/", Toast.LENGTH_SHORT).show();
            if (!directory.exists())
            {
                directory.mkdir();
            }


            DownloadManager.Request req=new DownloadManager.Request(Uri.parse(Constant.songUrl));

            req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                    | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(songName)
                    .setDescription("Song is Being Downloaded......")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(String.valueOf(directory),songName);

            DownloadManager manager= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(req);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }






}
