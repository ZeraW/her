package com.digitalsigma.hobrasul.Activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalsigma.hobrasul.Adapter.DownLoadRecyAdapter;
import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;
import com.digitalsigma.hobrasul.Service.MusicServiceSemsm;
import com.digitalsigma.hobrasul.Service.myPlayService;

import net.alhazmy13.catcho.library.Catcho;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by AhmedAbouElFadle on 12/4/2016.
 */
public class DownLoadsActivity extends AppCompatActivity {


    //service
    private MusicServiceSemsm musicSrv;
    private Intent playIntent;
    //binding
    private boolean musicBound = false;
    RingtoneManager mgr;
    // --Seekbar variables --
    private int seekMax;
    private int cPosItion;
    private static int songEnded = 0;
    boolean mBroadcastIsRegistered;

    int delete = 0;


    // Set up the notification ID
    private static final int NOTIFICATION_ID = 1;
    private boolean isPausedInCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;


    // --Set up constant ID for broadcast of seekbar position--
    public static final String BROADCAST_SEEKBAR = "com.smsemshehab.gmsproduction.sendseekbar";
    Intent intent;


    public static final String BROADCAST_Text = "com.smsemshehab.gmsproductiontextView";


    private Paint p = new Paint();

    private View view;

    private MediaPlayer player;
    Button btn_play;
    Button btn_next;
    Button btn_back;
    Button btn_CallTone;
    Button btn_Fav;

    TextView startTimeTxt;
    TextView songNametxt;
    TextView finalTimeTxt;
    private AlertDialog.Builder alertDialog;
    private double startTime = 0;
    private double finalTime = 0;

    SeekBar seekBar;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> downloadedSongUrl = new ArrayList<String>();
    ArrayList<String> downloadedSongName = new ArrayList<String>();

    String filePath;

    Typeface t1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Catcho.Builder(this)
                .activity(ContactActivity.class)
                .build();

        setContentView(R.layout.activity_downloads);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));


        inti();
        setLisner();

        t1 = Typeface.createFromAsset(getAssets(), "hacen.ttf");

        songNametxt.setTypeface(t1);
        startTimeTxt.setTypeface(t1);
        finalTimeTxt.setTypeface(t1);


        initDialog();


        intent = new Intent(BROADCAST_SEEKBAR);


        registerReceiver(broadcastReceiver, new IntentFilter(
                MusicServiceSemsm.BROADCAST_ACTION));
        mBroadcastIsRegistered = true;


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


        // mRecyclerView = (RecyclerView) findViewById(R.id.MusicListDownloaded);
        mRecyclerView.setHasFixedSize(true);
        initSwipe();


        File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
        File file = new File(Environment.getExternalStorageDirectory(), SDCardRoot + "/hobelrasulMp3");
        //  Toast.makeText(DownLoadsActivity.this, ""+Environment.getExternalStorageDirectory()+SDCardRoot+"MusicPro", Toast.LENGTH_SHORT).show();


        if (file.exists()) {
            //  Toast.makeText(DownLoadsActivity.this, "exist", Toast.LENGTH_SHORT).show();
            File[] files = file.listFiles();

            Log.d("Files", "Size: " + files.length);
            //  Toast.makeText(DownLoadsActivity.this, ""+files[0], Toast.LENGTH_SHORT).show();
            for (int i = 0; i < files.length; i++) {

                String[] items = files[i].getName().split(".mp3");

                for (String item : items) {
                    downloadedSongName.add(item);
                    //  Toast.makeText(DownLoadsActivity.this, "" + item, Toast.LENGTH_SHORT).show();
                }


                downloadedSongUrl.add(files[i].toString());
                //   downloadedSongName.add(files[i].getName());
                //  Toast.makeText(DownLoadsActivity.this, "url lem"+downloadedSongName.get(0), Toast.LENGTH_SHORT).show();

            }

        } else {
            Toast.makeText(DownLoadsActivity.this, "Not Exist" + Environment.getExternalStorageDirectory() + "/hobelrasulMp3", Toast.LENGTH_SHORT).show();
        }

        if (downloadedSongName.size() > 0)

        {
            findViewById(R.id.txtError).setVisibility(View.GONE);

            mLayoutManager = new LinearLayoutManager(DownLoadsActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new DownLoadRecyAdapter(downloadedSongName, downloadedSongUrl, DownLoadsActivity.this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            findViewById(R.id.txtError).setVisibility(View.VISIBLE);
        }


        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListenerDS(DownLoadsActivity.this
                ,mRecyclerView,new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                StartAnimations();
                Constant.pointer = 2;

                btn_play.setEnabled(true);
                btn_back.setEnabled(true);
                btn_next.setEnabled(true);

                btn_play.setBackground(getResources().getDrawable(R.drawable.stop_button_light_new));

                Constant.playListName = downloadedSongName;
                Constant.playListUrl = downloadedSongUrl;


                //     trackPlayingicon(position);

                Constant.pointer = 1;
                Constant.postion = position;

                if (musicSrv.isPlayingg()) {
                    musicSrv.pausePlayer();
                    //
                    // ms MusicServiceSemsm.player.pause();
                    //  musicSrv.playSong();
                    // playSong();
                    play();
                } else {

                    //  musicSrv.playSong();

                    play();
                    //playSong();
                }
                //  startActivity(new Intent(DownLoadsActivity.this,PlayerActivity.class));


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        intent = new Intent(BROADCAST_SEEKBAR);

        registerReceiver(broadcastReceiver, new IntentFilter(
                MusicServiceSemsm.BROADCAST_ACTION));
        mBroadcastIsRegistered = true;


        btn_CallTone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(DownLoadsActivity.this, "jsdksaj", Toast.LENGTH_SHORT).show();

                settingPermission();
                mgr = new RingtoneManager(DownLoadsActivity.this);
                mgr.setType(RingtoneManager.TYPE_RINGTONE);

                if (Constant.playListName.size() > 0) {

                    alertDialog = new AlertDialog.Builder(DownLoadsActivity.this);
                    alertDialog.setTitle("عاوز اغنية '" + downloadedSongName.get(Constant.postion) + "' تبقى رنة موبايلك");
                    // view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
                    // alertDialog.setView(view);
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
                            File file = new File(Environment.getExternalStorageDirectory(), SDCardRoot + "/hobelrasulMp3/" + downloadedSongName.get(Constant.postion));
                            // String path=Environment.getExternalStorageDirectory()++"/MusicPro";
                            // File file = new File(dir, "my_filename");
                            // Toast.makeText(DownLoadsActivity.this, "p"+file.toString(), Toast.LENGTH_SHORT).show();

                            // Toast.makeText(DownLoadsActivity.this, "name    "+downloadedSongUrl.get(Constant.postion), Toast.LENGTH_SHORT).show();

                            //  setRingtone(file.toString(),downloadedSongName.get(Constant.postion));
                            ringtone(downloadedSongUrl.get(Constant.postion) + "");


                        }
                    });
                    alertDialog.show();


                } else {
                    Snackbar snackbar = Snackbar
                            .make(v, "اختر النغمه التى تريد الاستماع اليها :( !!", Snackbar.LENGTH_LONG);

                    snackbar.show();

                }
            }
        });
    }

    public void settingPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 200);

            }
        }
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.downloadPlayerSheet);
        l.clearAnimation();
        l.startAnimation(anim);
        findViewById(R.id.downloadPlayerSheet).setVisibility(View.VISIBLE);

    }


    public void ringtone(String path) {
        // Create File object for the specified ring tone path
        File f = new File(path);


        ContentValues content = new ContentValues();
        content.put(MediaStore.MediaColumns.DATA, f.getAbsolutePath());
        content.put(MediaStore.MediaColumns.TITLE, downloadedSongName.get(Constant.postion));
        content.put(MediaStore.MediaColumns.SIZE, 215454);
        content.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
        //  content.put(MediaStore.Audio.Media.ARTIST, "Madonna");
        content.put(MediaStore.Audio.Media.DURATION, 230);
        content.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        content.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        content.put(MediaStore.Audio.Media.IS_ALARM, true);
        content.put(MediaStore.Audio.Media.IS_MUSIC, true);


        //Insert it into the database
        Log.i("TAG", "the absolute path of the file is :" +
                f.getAbsolutePath());
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(
                f.getAbsolutePath());


        getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + f.getAbsolutePath() + "\"",
                null);
        Uri newUri = getContentResolver().insert(uri, content);
        System.out.println("uri==" + uri);
        Log.i("TAG", "the ringtone uri is :" + newUri);
        RingtoneManager.setActualDefaultRingtoneUri(
                getApplicationContext(), RingtoneManager.TYPE_RINGTONE,
                newUri);

    }

    public void setLisner() {

        btn_Fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.playListName.size() > 0) {


                    // Toast.makeText(getActivity(), "pos"+searchnameResult.get(Constant.postion), Toast.LENGTH_SHORT).show();
                    // downloadong(Constant.playListUrl.get(Constant.postion),Constant.playListName.get(Constant.postion));
                } else {
                    Snackbar snackbar = Snackbar
                            .make(v, "اختر النغمه التى تريد نحميلها", Snackbar.LENGTH_LONG);

                    snackbar.show();

                }
            }
        });


        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.playListName.size() > 0) {


                    if (musicSrv.isPlayingg()) {
                        btn_play.setBackground(getResources().getDrawable(R.drawable.play_button_light_new));

                        musicSrv.pausePlayer();
                        //new myPlayService().pauseMedia();
                        // MusicServiceSemsm.player.pause();

                    } else {
                        btn_play.setBackground(getResources().getDrawable(R.drawable.stop_button_light_new));
                        musicSrv.go();
                        //  new myPlayService().playMedia();
                        // musicSrv.pausePlayer();
                        //  MusicServiceSemsm.player.start();
                    }

                } else {
                    Snackbar snackbar = Snackbar
                            .make(v, "اختر النغمه التى تريد الاستماع اليها :( !!", Snackbar.LENGTH_LONG);

                    snackbar.show();

                }


            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.playListName.size() > 0) {


                    if (Constant.postion > 0) {

                        //  lastPosition=Constant.postion;

                        Constant.postion--;
                        //  musicSrv.pausePlayer();

                        //  play();
                        musicSrv.pausePlayer();
                        play();
                      /*  new myPlayService().pauseMedia();
                        playAudio();*/


                    } else {
                        //  lastPosition=Constant.postion;
                        Constant.postion = Constant.playListUrl.size() - 1;
                       /* new myPlayService().pauseMedia();
                        playAudio();*/
                        musicSrv.pausePlayer();
                        play();

                    }
                } else {
                    Snackbar snackbar = Snackbar
                            .make(v, "اختر النغمه التى تريد الاستماع اليها :( !!", Snackbar.LENGTH_LONG);

                    snackbar.show();

                }
            }
        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.playListName.size() > 0) {

                    if (Constant.postion < (Constant.playListName.size() - 1)) {
                        //   lastPosition=Constant.postion;
                        Constant.postion++;
                        new myPlayService().pauseMedia();
                        /// playAudio();
                        // musicSrv.pausePlayer();

                        // player.pause();
                        play();
                        //  playSong();

                    } else {


                        ///      lastPosition=searchnameResult.size()-1;
                        Constant.postion = 0;
                        /*new myPlayService().pauseMedia();
                        playAudio();*/


                        musicSrv.pausePlayer();

                        // player.pause();
                        play();
                        //   playSong();


                    }
                } else {
                    Snackbar snackbar = Snackbar
                            .make(v, "اختر النغمه التى تريد الاستماع اليها :( !!", Snackbar.LENGTH_LONG);

                    snackbar.show();

                }
            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicServiceSemsm.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
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


    public void play() {
        //trackPlayingicon(Constant.postion);
        musicSrv.playSong();
        songNametxt.setText(Constant.playListName.get(Constant.postion));

        registerReceiver(broadcastReceiver, new IntentFilter(
                MusicServiceSemsm.BROADCAST_ACTION));


        mBroadcastIsRegistered = true;

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
        String cPos = serviceIntent.getStringExtra("cPostion");
        int seekProgress = Integer.parseInt(counter);
        seekMax = Integer.parseInt(mediamax);
        songEnded = Integer.parseInt(strSongEnded);
        seekBar.setMax(seekMax);
        seekBar.setProgress(seekProgress);


        finalTime = Double.valueOf(mediamax);
        startTime = Double.valueOf(counter);

        if (Constant.playListName.size() > 0) {

            songNametxt.setText(Constant.playListName.get(Constant.postion));
        }
        //  trackPlayingicon(Constant.postion);

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


    }


    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    // Toast.makeText(AlbumContentActivity.this, "download   "+position, Toast.LENGTH_SHORT).show();

                    alertDialog = new AlertDialog.Builder(DownLoadsActivity.this);
                    alertDialog.setTitle("هل تريد مسح هذه الاغنيه");
                    // view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
                    // alertDialog.setView(view);
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
                            File file = new File(Environment.getExternalStorageDirectory(), SDCardRoot + "/hobelrasulMp3/" + downloadedSongName.get(position));
                            // File file = new File(dir, "my_filename");
                            File file1 = new File(Environment.getExternalStorageDirectory(), SDCardRoot + "/hobelrasulMp3/" + downloadedSongName.get(position) + ".mp3");

                            boolean deleted = file.delete();
                            boolean deleted2 = file1.delete();
                            if (deleted || deleted2) {
                                mAdapter.notifyDataSetChanged();

                                delete = 1;

                                startActivity(new Intent(DownLoadsActivity.this, DownLoadsActivity.class));
                                Toast.makeText(DownLoadsActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                    alertDialog.show();

                    mAdapter.notifyDataSetChanged();
                } else {
                    alertDialog = new AlertDialog.Builder(DownLoadsActivity.this);
                    alertDialog.setTitle("هل تريد مسح هذه الاغنيه");
                    // view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
                    // alertDialog.setView(view);
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
                            File file = new File(Environment.getExternalStorageDirectory(), SDCardRoot + "/hobelrasulMp3/" + downloadedSongName.get(position));
                            // File file = new File(dir, "my_filename");
                            File file1 = new File(Environment.getExternalStorageDirectory(), SDCardRoot + "/hobelrasulMp3/" + downloadedSongName.get(position) + ".mp3");

                            boolean deleted = file.delete();
                            boolean deleted2 = file1.delete();
                            if (deleted || deleted2) {
                                mAdapter.notifyDataSetChanged();

                                delete = 1;

                                startActivity(new Intent(DownLoadsActivity.this, DownLoadsActivity.class));
                                Toast.makeText(DownLoadsActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                    alertDialog.show();

                    mAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.delete_icon);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.delete_icon);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (delete == 1) {
            delete = 0;
            finish();
        }

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

                                // MusicServiceSemsm.player.pause();
                                isPausedInCall = true;
                            }

                            break;
                        case TelephonyManager.CALL_STATE_IDLE:
                            // Phone idle. Start playing.
                            if (musicSrv != null) {
                                if (isPausedInCall) {
                                    isPausedInCall = false;
                                    // musicSrv.go();

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

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (MusicServiceSemsm.player.isPlaying()) {
            if (Constant.playListName.size() > 0) {

                findViewById(R.id.downloadPlayerSheet).setVisibility(View.VISIBLE);
                btn_play.setBackground(getResources().getDrawable(R.drawable.stop_button_light_new));
                songNametxt.setText(Constant.playListName.get(Constant.postion));
            }
        }
    }


    public void inti() {

        btn_CallTone = (Button) findViewById(R.id.callToneBtn);
        btn_Fav = (Button) findViewById(R.id.favbtn);

        startTimeTxt = (TextView) findViewById(R.id.startTimetxt);
        songNametxt = (TextView) findViewById(R.id.songNametxt);
        finalTimeTxt = (TextView) findViewById(R.id.endTimetxt);


        btn_play = (Button) findViewById(R.id.btnPlayerPlay);
        btn_back = (Button) findViewById(R.id.btnPlayerBack);
        btn_next = (Button) findViewById(R.id.btnPlayerNext);

        seekBar = (SeekBar) findViewById(R.id.seekBar2);
        mRecyclerView = (RecyclerView) findViewById(R.id.MusicListDownloaded);
    }


    private void initDialog() {

        // et_country = (EditText)view.findViewById(R.id.et_country);
    }

}

class RecyclerTouchListenerDS implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private DownLoadsActivity.ClickListener clickListener;

    public RecyclerTouchListenerDS(Context context, final RecyclerView recyclerView, final DownLoadsActivity.ClickListener clickListener) {
        this.clickListener = clickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                }
            }
        });
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}

