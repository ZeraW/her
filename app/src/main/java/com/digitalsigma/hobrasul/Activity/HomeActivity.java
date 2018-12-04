package com.digitalsigma.hobrasul.Activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.widget.LikeView;
import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;
import com.digitalsigma.hobrasul.Service.MusicServiceSemsm;

import net.alhazmy13.catcho.library.Catcho;

/**
 * Created by user2 on 1/2/2017.
 */

public class HomeActivity extends Activity {
    //service
    private MusicServiceSemsm musicSrv;
    private Intent playIntent;
    boolean mBroadcastIsRegistered;

    //binding
    private boolean musicBound=false;

    // Set up the notification ID
    private static final int NOTIFICATION_ID = 1;
    private boolean isPausedInCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;
    Button  btn_tracks,btn_video,btn_party,btn_news,btn_downloads,btn_gallary,btn_center;

    LikeView likeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Catcho.Builder(this)
                .activity(ContactActivity.class)
                .build();


        setContentView(R.layout.activity_main_home);

        init();
        setLisner();
        StartAnimations();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

       /* likeView = (LikeView) findViewById(R.id.like_view);
        likeView.setObjectIdAndType(
                "https://www.facebook.com/korasport.official/?hc_ref=ADS&fref=nf&ft[tn]=kC&ft[qid]=6371698170242448246&ft[mf_story_key]=-3041165822730467794&ft[ei]=AI%404da870e70d4505f90cc5e511b3923e76&ft[top_level_post_id]=1113356942101127&ft[fbfeed_location]=1&ft[insertion_position]=1&__md__=1",
                LikeView.ObjectType.PAGE);*/
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // LikeView.handleOnActivityResult(this, requestCode, resultCode, data);
       // Log.i(TAG, "OnActivityResult...");
    }



    public void init()
    {
        btn_news= (Button) findViewById(R.id.btn_news);
        btn_video= (Button) findViewById(R.id.btn_videos);
        btn_tracks= (Button) findViewById(R.id.btn_tracks);
        btn_party= (Button) findViewById(R.id.btn_party);
        btn_downloads= (Button) findViewById(R.id.btn_downloads);
        btn_gallary= (Button) findViewById(R.id.btn_gallary);
        btn_center= (Button) findViewById(R.id.center);




    }


    public void setLisner()
    {
        btn_tracks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HomeActivity.this, TracksActivity.class);
                i.putExtra("Page","1");
                startActivity(i);

            }
        });

        btn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, VideosActivity.class));


            }
        });

        btn_party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, PartiesActivity.class));


            }
        });

        btn_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, NewsActivity.class));

            }
        });

        btn_downloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, DownLoadsActivity.class));

            }
        });

        btn_gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, GallaryActivity.class));

            }
        });

        btn_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CvActiviy.class));

            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicServiceSemsm.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
       /* if (MusicServiceSemsm.player!= null)
        {
            if (MusicServiceSemsm.player.isPlaying())
            {
                MusicServiceSemsm.player.pause();
            }
        }*/

    }

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicServiceSemsm.MusicBinder binder = (MusicServiceSemsm.MusicBinder) service;
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
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv=null;

        super.onDestroy();

    }



    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout l=(RelativeLayout) findViewById(R.id.main_home_re);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        //ImageView iv = (ImageView) findViewById(R.id.splash);

        btn_gallary.clearAnimation();
        btn_gallary.startAnimation(anim);

        btn_news.clearAnimation();
        btn_news.startAnimation(anim);

        btn_tracks.clearAnimation();
        btn_tracks.startAnimation(anim);

        btn_downloads.clearAnimation();
        btn_downloads.startAnimation(anim);

        btn_party.clearAnimation();
        btn_party.startAnimation(anim);

        btn_video.clearAnimation();
        btn_video.startAnimation(anim);





    }




    @Override
    protected void onPause() {
        super.onPause();
        //&& MusicServiceSemsm.player != null && MusicServiceSemsm.player.isPlaying()
        if (musicSrv != null ) {


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

                                // MusicServiceSemsm.player.pause();
                                isPausedInCall = true;
                            }

                            break;
                        case TelephonyManager.CALL_STATE_IDLE:
                            // Phone idle. Start playing.
                            if (musicSrv != null) {
                                if (isPausedInCall) {
                                    isPausedInCall = false;
                                    musicSrv.go();

                                    //MusicServiceSemsm.player.start();
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
}
