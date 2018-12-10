package com.digitalsigma.hobrasul.Activity;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digitalsigma.hobrasul.Adapter.MusicRecyAdapter;
import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;
import com.digitalsigma.hobrasul.Service.MusicServiceSemsm;

import net.alhazmy13.catcho.library.Catcho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by user2 on 1/2/2017.
 */

public class AlbumTracksActivity extends AppCompatActivity {


    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    String id;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    View toneViewSnackBar;

    ProgressBar progressBar;


    boolean exit = false;
    Intent serviceIntent;

    private boolean isOnline;
    private boolean boolMusicPlaying = false;


    public static int b = 0;

    // Progress dialogue and broadcast receiver variables
    boolean mBufferBroadcastIsRegistered;
    private ProgressDialog pdBuff = null;

    // Set up the notification ID
    private static final int NOTIFICATION_ID = 1;
    private boolean isPausedInCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;

    double c = 0;
    int lastPosition = 0;
    AlertDialog.Builder alertDialog;

    // --Seekbar variables --
    private int seekMax;
    private int cPosItion;
    private static int songEnded = 0;
    boolean mBroadcastIsRegistered;


    // --Set up constant ID for broadcast of seekbar position--
    public static final String BROADCAST_SEEKBAR = "com.smsemshehab.gmsproduction.sendseekbar";
    Intent intent;
    public static final String BROADCAST_Text = "com.smsemshehab.gmsproduction.textView";
    Intent textintent;
    public static final String BROADCAST_ACTION = "com.smsemshehab.gmsproduction.seekprogress";
    // Set up broadcast identifier and intent
    public static final String BROADCAST_BUFFER = "com.smsemshehab.gmsproduction.broadcastbuffer";
    Button btn_play;
    Button btn_next;
    Button btn_back;
    Button btn_CallTone;
    Button btn_Fav;
    private double startTime = 0;
    private double finalTime = 0;
    TextView startTimeTxt;
    public TextView songNametxt;
    TextView finalTimeTxt;
    SeekBar seekBar;

    ArrayList<String> searchnameResult = new ArrayList<String>();
    ArrayList<String> searchurlResult = new ArrayList<String>();
    ArrayList<String> ringToneList = new ArrayList<String>();
    ArrayList<String> albumIdList = new ArrayList<String>();
    ArrayList<String> idList = new ArrayList<String>();

    ArrayList<String> searchVodToneCodeResult = new ArrayList<String>();
    ArrayList<String> searchEtisCodeToneResult = new ArrayList<String>();
    ArrayList<String> searchOranCodeToneResult = new ArrayList<String>();


    ArrayList<String> downloadedSongUrl = new ArrayList<String>();
    ArrayList<String> downloadedSongName = new ArrayList<String>();


    ArrayList<String> downloadedSongUrl1 = new ArrayList<String>();
    ArrayList<String> downloadedSongName1 = new ArrayList<String>();


    public static MediaPlayer player;


    private Paint p = new Paint();
    private int ref = 0;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Button btn_retry;

    //service
    private MusicServiceSemsm musicSrv;
    private Intent playIntent;
    //binding
    private boolean musicBound = false;

    private CoordinatorLayout coordinatorLayout;

    Typeface t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Catcho.Builder(this)
                .activity(ContactActivity.class)
                .build();
        setContentView(R.layout.album_fragment);

        getSupportActionBar().setTitle(Constant.albumName);

        progressBar = (ProgressBar) findViewById(R.id.TrackLoadingDownload);


        try {

            inti();
            connection();
            setLisner();
            t1 = Typeface.createFromAsset(getAssets(), "hacen.ttf");

            songNametxt.setTypeface(t1);
            startTimeTxt.setTypeface(t1);
            finalTimeTxt.setTypeface(t1);
            //    serviceIntent = new Intent(getActivity(), myPlayService.class);
            intent = new Intent(BROADCAST_SEEKBAR);


            registerReceiver(broadcastReceiver, new IntentFilter(
                    MusicServiceSemsm.BROADCAST_ACTION));
            mBroadcastIsRegistered = true;


        } catch (Exception e) {
            e.printStackTrace();
      /*      Toast.makeText(TracksActivity.this,
                    e.getClass().getName() + " " + e.getMessage(),
                    Toast.LENGTH_LONG).show();*/
        }


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        checkAndRequestPermissions();


    }





    public void btnFav(View v) {


        double s = 5.0;
        // if (Double.valueOf(getAvailableInternalMemorySize()) < s) {
        if (Constant.playListName.size() > 0) {


            boolean check = isExternalStorageWritable();
            if (check) {
                File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
                File file = new File(Environment.getExternalStorageDirectory(), SDCardRoot + "/SemsmShehab");

                if (file.exists()) {
                    //  Toast.makeText(DownLoadsActivity.this, "exist", Toast.LENGTH_SHORT).show();
                    File[] files = file.listFiles();

                    Log.d("Files", "Size: " + files.length);
                    //  Toast.makeText(DownLoadsActivity.this, ""+files[0], Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < files.length; i++) {


                        downloadedSongUrl.add(files[i].toString());
                        downloadedSongName.add(files[i].getName());
                        //  Toast.makeText(DownLoadsActivity.this, "url lem"+downloadedSongName.get(0), Toast.LENGTH_SHORT).show();

                    }

                    if (downloadedSongName.contains(Constant.playListName.get(Constant.postion) + ".mp3")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(AlbumTracksActivity.this);

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

                    } else {


                        AlertDialog.Builder builder = new AlertDialog.Builder(AlbumTracksActivity.this);

                        //"لكى تكون كول تون لهاتفك "+Constant.playListName.get(position)+"لقد اخترت "
                        builder.setMessage("هل تريد تحميل اغنية ' " + Constant.playListName.get(Constant.postion) + " '");

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                downloadong(Constant.playListUrl.get(Constant.postion), Constant.playListName.get(Constant.postion), "SemsmShehab");
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


                } else {
                    downloadong(Constant.playListUrl.get(Constant.postion), Constant.playListName.get(Constant.postion), "SemsmShehab");
                }


            } else {
                //    Toast.makeText(TracksActivity.this, "no space", Toast.LENGTH_SHORT).show();

                downloadong(Constant.playListUrl.get(Constant.postion), Constant.playListName.get(Constant.postion), "SemsmShehab");

            }
        } else {
            Snackbar snackbar = Snackbar
                    .make(v, "اختر النغمه التى تريد نحميلها", Snackbar.LENGTH_LONG);

            snackbar.show();

        }
    }


    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int settingPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS);

        int wakeLockPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK);
        int readPhoneStatePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int callphonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (settingPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_SETTINGS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (wakeLockPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WAKE_LOCK);
        }
        if (readPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (callphonePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void settingPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 200);

            }
        }
    }


    public void connection() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            //progressActivity.showLoading();
            //  Toast.makeText(getActivity(), "here", Toast.LENGTH_SHORT).show();

            findViewById(R.id.loadingPrograss).setVisibility(View.VISIBLE);
            getTracks(Constant.album_id);
            // allTracks();


        } else {

            btn_retry.setVisibility(View.VISIBLE);
            btn_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConnectivityManager cm1 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo info1 = cm1.getActiveNetworkInfo();

                    if (info1 != null && info1.isConnected()) {


                        getTracks(Constant.album_id);

                        // allTracks();
                        btn_retry.setVisibility(View.INVISIBLE);

                    } else {

                        Snackbar snackbar = Snackbar
                                .make(v, "No internet connection!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
            });


        }

    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.playerSheetView);
        l.clearAnimation();
        l.startAnimation(anim);
        findViewById(R.id.playerSheetView).setVisibility(View.VISIBLE);

    /*    anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        //ImageView iv = (ImageView) findViewById(R.id.splash);

        .clearAnimation();
        holder.youtubeAnim.startAnimation(anim);*/


    }

    public void getTracks(final String id) {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.all_albums + "/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject dataobject = object.getJSONObject("data");
                    JSONArray dataArray = dataobject.getJSONArray("tracks");
                    for (int a = 0; a < dataArray.length(); a++) {

                        JSONObject tracks = dataArray.getJSONObject(a);
                        String albumId = tracks.getString("album_id");

                        String trackName = tracks.getString("title");
                        String trackUrl = Constant.BASE_URL + tracks.getString("audio");
                        String ringToneUrl = Constant.BASE_URL + tracks.getString("ringtone");

                        String vod = tracks.getString("vodafone");
                        String ora = tracks.getString("orange");
                        String etis = tracks.getString("etisalat");

                        String id = tracks.getString("id");


                        idList.add(id);

                        ringToneList.add(ringToneUrl);
                        searchnameResult.add(trackName);
                        searchurlResult.add(trackUrl);
                        searchVodToneCodeResult.add(vod);
                        searchOranCodeToneResult.add(ora);
                        searchEtisCodeToneResult.add(etis);
                        albumIdList.add(albumId);


                    }

                    findViewById(R.id.loadingPrograss).setVisibility(View.GONE);

                    Collections.reverse(searchurlResult);
                    Collections.reverse(searchnameResult);
                    Collections.reverse(searchVodToneCodeResult);
                    Collections.reverse(searchOranCodeToneResult);
                    Collections.reverse(searchEtisCodeToneResult);
                    Collections.reverse(ringToneList);
                    Collections.reverse(albumIdList);


                    Collections.reverse(idList);
                    mLayoutManager = new LinearLayoutManager(AlbumTracksActivity.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    //  mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    //  mAdapter = new TracksRecyAdapter(TrackNameList,TrackImgList,TracksActiviy.this);

                    mAdapter = new MusicRecyAdapter(searchnameResult, searchurlResult, searchVodToneCodeResult, searchOranCodeToneResult,
                            searchEtisCodeToneResult, albumIdList, ringToneList, idList, AlbumTracksActivity.this);
                    mRecyclerView.setAdapter(mAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(SearchActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        // loginUser();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("album_id", id);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    // --- Stop service (and music) ---


    public void play() {

        songNametxt.setText(Constant.playListName.get(Constant.postion));

        //trackPlayingicon(Constant.postion);
        musicSrv.playSong();


        // -- Register receiver for seekbar--


        //customSimpleNotification(getActivity(),searchnameResult.get(Constant.postion),Constant.album_name);
        registerReceiver(broadcastReceiver, new IntentFilter(
                MusicServiceSemsm.BROADCAST_ACTION));
        mBroadcastIsRegistered = true;
    /*    getActivity().registerReceiver(broadcastReceiverSongName, new IntentFilter(
                MusicServiceSemsm.BROADCAST_Text));*/


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

        // songNametxt.setText(Constant.playListName.get(Constant.postion));
    /*    if (Constant.playListName.size() > 0)
        {
            for (int i=0;i< Constant.playListName.size() ;i++)
            {
                MusicRecyAdapter.ViewHolder holder =
                        (MusicRecyAdapter.ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(i);
                 holder.username.setTextColor(getResources().getColor(R.color.cardview_light_background));
                //  holder.imageViewGif.setImageDrawable(getResources().getDrawable(R.drawable.vod));
               // holder.imageViewGif.setVisibility(View.GONE);
            }
            MusicRecyAdapter.ViewHolder holder =
                    (MusicRecyAdapter.ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(Constant.postion);
            holder.username.setTextColor(getResources().getColor(R.color.colorAccent));


        }*/

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

        if (startTime == finalTime) {
            //   songNametxt.setText(searchnameResult.get(Integer.valueOf(Constant.postion+1)));

        }


        if (songEnded == 1) {
            // buttonPlayStop.setBackgroundResource(R.drawable.playbuttonsm);
            // Toast.makeText(getActivity(), "cc", Toast.LENGTH_SHORT).show();

        }
    }


    // Handle progress dialogue for buffering...
    private void showPD(Intent bufferIntent) {
        String bufferValue = bufferIntent.getStringExtra("buffering");
        int bufferIntValue = Integer.parseInt(bufferValue);

        // When the broadcasted "buffering" value is 1, show "Buffering"
        // progress dialogue.
        // When the broadcasted "buffering" value is 0, dismiss the progress
        // dialogue.

        switch (bufferIntValue) {
            case 0:
                // Log.v(TAG, "BufferIntValue=0 RemoveBufferDialogue");
                // txtBuffer.setText("");
              /*  if (pdBuff != null) {
                   // pdBuff.dismiss();
                    getActivity().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                }*/

                btn_play.setEnabled(true);
                findViewById(R.id.loadingPrograss).setVisibility(View.GONE);
                songNametxt.setText(Constant.playListName.get(Constant.postion));
              /*  songNametxt.setText(Constant.playListName.get(Constant.postion));
                StartAnimations();
*/
                //  findViewById(R.id.playerSheetView).setVisibility(View.VISIBLE);

                break;

            case 1:
                BufferDialogue();
                // findViewById(R.id.playerSheetView).setVisibility(View.GONE);
                btn_play.setEnabled(false);

                break;

            // Listen for "2" to reset the button to a play button
            case 2:
                // buttonPlayStop.setBackgroundResource(R.drawable.playbuttonsm);
                break;

        }
    }

    // Progress dialogue...
    private void BufferDialogue() {

        findViewById(R.id.loadingPrograss).setVisibility(View.VISIBLE);

        /*pdBuff = ProgressDialog.show(getActivity(), "Buffering...",
                "Acquiring song...", true);*/
    }

    // Set up broadcast receiver
    private BroadcastReceiver broadcastBufferReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent bufferIntent) {
            showPD(bufferIntent);
        }
    };


    public void confirmDailog(int position, final String network) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //"لكى تكون كول تون لهاتفك "+Constant.playListName.get(position)+"لقد اخترت "
        builder.setMessage("لقد اخترت " + Constant.playListName.get(position) + " علشان تكون كول تون لموبيلك");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Constant.networkName = network;

                startActivity(new Intent(AlbumTracksActivity.this, DialActivity.class));
                //   Toast.makeText(TracksActivity.this, ""+Constant.ussd, Toast.LENGTH_SHORT).show();

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


    public void calltoneChecker(int position) {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String number = tm.getNetworkOperatorName();
        Log.d("sim type", number);
        //   Toast.makeText(TracksActivity.this, ""+number, Toast.LENGTH_SHORT).show();

        if (number.toLowerCase().contains("vodaf")) {
            if (!(Constant.vodCallToneList.get(position).equals("")
                    || Constant.vodCallToneList.get(position).equals("null"))) {
                Constant.ussd = Constant.vodCallToneList.get(position);
                //  Toast.makeText(MainActivity.this, "vodafone code find" +Constant.vodCallToneList.get(position), Toast.LENGTH_SHORT).show();

                confirmDailog(position, "vod");


            } else {
                Constant.ussd = "";
                // Toast.makeText(getActivity(), "noooo", Toast.LENGTH_SHORT).show();

                Snackbar snackbar = Snackbar
                        .make(toneViewSnackBar, "لا يوجد كول تون فى هذه الشبكه :( !!", Snackbar.LENGTH_LONG);


                snackbar.show();
            }


            //    Toast.makeText(TracksActivity.this, "call   true", Toast.LENGTH_SHORT).show();
        } else if (number.toLowerCase().contains("etis")) {

            if (!(Constant.EtisCallToneList.get(position).equals("")
                    || Constant.EtisCallToneList.get(position).equals("null"))) {
                Constant.ussd = Constant.EtisCallToneList.get(position
                );

                confirmDailog(position, "et");


                // Toast.makeText(TracksActivity.this, "elislate code find" +Constant.EtisCallToneList.get(position), Toast.LENGTH_SHORT).show();
            } else {
                Constant.ussd = "";
                Snackbar snackbar = Snackbar
                        .make(toneViewSnackBar, "لا يوجد كول تون فى هذه الشبكه :( !!", Snackbar.LENGTH_LONG);


                snackbar.show();
            }


        } else if (number.toLowerCase().contains("oran")) {

            if (!(Constant.orangCallToneList.get(position).equals("")
                    || Constant.orangCallToneList.get(position).equals("null"))) {

                Constant.ussd = Constant.orangCallToneList.get(position);

                confirmDailog(position, "or");


                // Toast.makeText(MainActivity.this, "orange code find" +Constant.orangCallToneList.get(position), Toast.LENGTH_SHORT).show();
            } else {
                Constant.ussd = "";
                // Toast.makeText(MainActivity.this, "noooo", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar
                        .make(toneViewSnackBar, "لا يوجد كول تون فى هذه الشبكه :( !!", Snackbar.LENGTH_LONG);


                snackbar.show();
            }


        } else if (number.toLowerCase().contains("mobin")) {
            if (!(Constant.orangCallToneList.get(position).equals("")
                    || Constant.orangCallToneList.get(position).equals("null"))) {

                Constant.ussd = Constant.orangCallToneList.get(position);

                Constant.networkName = "or";

                confirmDailog(position, "or");


                // Toast.makeText(MainActivity.this, "orange code find" +Constant.orangCallToneList.get(position), Toast.LENGTH_SHORT).show();
            } else {
                Constant.ussd = "";
                // Toast.makeText(MainActivity.this, "noooo", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar
                        .make(toneViewSnackBar, "لا يوجد كول تون فى هذه الشبكه :( !!", Snackbar.LENGTH_LONG);


                snackbar.show();
            }


        }
    }


    public void downloadong(final String url, String name, String folderName) {
      /*  thread = new Thread() {
            @Override
            public void run() {*/
        //Toast.makeText(getActivity(), "download", Toast.LENGTH_SHORT).show();
        File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
        File directory = new File(SDCardRoot, "/" + folderName + "/"); //create directory to keep your downloaded file

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
                .setDestinationInExternalPublicDir(String.valueOf(directory), name + ".mp3");

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(req);
        if (!folderName.equals("SemsmShehab")) {
            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }


    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
            File file = new File(Environment.getExternalStorageDirectory(), SDCardRoot + "/SemsmShehabRingTone/" +
                    Constant.playListName.get(Constant.postion) + ".mp3");

            ringtone(file.toString() + "");
            //    Toast.makeText(TracksActivity.this, "finish", Toast.LENGTH_SHORT).show();

        }
    };


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    public void setLisner() {


        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListenerAlbumTracks(AlbumTracksActivity.this,
                mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                btn_play.setBackground(getResources().getDrawable(R.drawable.stop_button_light_new));
                //findViewById(R.id.playerSheetView).setVisibility(View.GONE);

                // StartAnimations();


                MusicRecyAdapter.ViewHolder holder =
                        (MusicRecyAdapter.ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
                holder.username.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        btn_play.setBackground(getResources().getDrawable(R.drawable.stop_button_light_new));
                        //findViewById(R.id.playerSheetView).setVisibility(View.GONE);

                        // StartAnimations();

                        b = 1;


                        Constant.pointer = 1;
                        Constant.songName = searchnameResult.get(position);
                        Constant.songUrl = searchurlResult.get(position);

                        Constant.playListName = searchnameResult;
                        Constant.playListUrl = searchurlResult;
                        Constant.RingToneUrl = ringToneList;

                        Constant.postion = position;


                        Constant.EtisCallToneList = searchEtisCodeToneResult;
                        Constant.vodCallToneList = searchVodToneCodeResult;
                        Constant.orangCallToneList = searchOranCodeToneResult;

                        songNametxt.setText(Constant.playListName.get(position));

                        //songNametxt.setText(Constant.playListName.get(Constant.postion));
                        StartAnimations();

                        sharedPreferences = getSharedPreferences("playing", Context.MODE_PRIVATE);
                        String played = sharedPreferences.getString("track" + idList.get(position), "");
                        editor = sharedPreferences.edit();
                        editor.putString("track" + idList.get(position), "played");
                        editor.commit();


                        play();
                    }
                });







/*
                b=1;


                Constant.pointer=1;
                Constant.songName=searchnameResult.get(position);
                Constant.songUrl=searchurlResult.get(position);

                Constant.playListName=searchnameResult;
                Constant.playListUrl=searchurlResult;
                Constant.RingToneUrl=ringToneList;

                Constant.postion=position;


                Constant.EtisCallToneList=searchEtisCodeToneResult;
                Constant.vodCallToneList=searchVodToneCodeResult;
                Constant.orangCallToneList=searchOranCodeToneResult;

                songNametxt.setText(Constant.playListName.get(position));

                //songNametxt.setText(Constant.playListName.get(Constant.postion));
                StartAnimations();



                play();*/


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


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


        btn_Fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                btnFav(v);
               /* if (AccessToken.getCurrentAccessToken() != null) {


                    // }
                } else {


                    AlertDialog.Builder builder = new AlertDialog.Builder(AlbumTracksActivity.this);

                    //"لكى تكون كول تون لهاتفك "+Constant.playListName.get(position)+"لقد اخترت "
                    builder.setMessage("يجب عليك تسجيل الدخول لتحميل لاغنيه");

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            fbLogin(v);

                            // Toast.makeText(AllTracksActivity.this, "in", Toast.LENGTH_SHORT).show();

                            // startActivity(new Intent(AllTracksActivity.this,SplashScreenActivity.class));

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


                }*/


                // }
            }
        });


        btn_CallTone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                settingPermission();
                toneViewSnackBar = v;

                //   calltoneChecker(Constant.postion);
                //   callTone(Constant.postion);
                if (Constant.playListName.size() > 0) {


                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AlbumTracksActivity.this);
                    View parentView = getLayoutInflater().inflate(R.layout.ringtone_calltone_action_sheet, null);
                    bottomSheetDialog.setContentView(parentView);
                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) parentView.getParent());
                    bottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics()));
                    bottomSheetDialog.show();

                    final Button btn_ringtone = (Button) parentView.findViewById(R.id.btn_ringTone);
                    final Button btn_calltone = (Button) parentView.findViewById(R.id.btn_callTone);


                    btn_calltone.setTypeface(t1);
                    btn_ringtone.setTypeface(t1);


                    btn_ringtone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int pos = Constant.postion;

                            if (Constant.RingToneUrl.get(pos).equals("") || Constant.RingToneUrl.get(pos).equals(null)) {
                                Snackbar snackbar = Snackbar
                                        .make(v, "لايوجد رابط ", Snackbar.LENGTH_LONG);

                                snackbar.show();
                            } else {
                                File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
                                File file = new File(Environment.getExternalStorageDirectory(), SDCardRoot + "/SemsmShehabRingTone");

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
                                    if (downloadedSongName1.contains(Constant.playListName.get(Constant.postion) + ".mp3")) {

                                        File SDCardRoot1 = Environment.getExternalStorageDirectory(); // location where you want to store
                                        File file1 = new File(Environment.getExternalStorageDirectory(), SDCardRoot + "/SemsmShehabRingTone/" +
                                                Constant.playListName.get(Constant.postion) + ".mp3");

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
                                                "SemsmShehabRingTone");

                                    }

                                } else {
                                    downloadong(Constant.RingToneUrl.get(pos), Constant.playListName.get(pos),
                                            "SemsmShehabRingTone");
                                }


                            }
                            bottomSheetDialog.hide();

                        }
                    });

                    btn_calltone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                            String number = tm.getNetworkOperatorName();
                            if (number.equals("") || number.equals(null) || number.equals("null")) {
                                Toast.makeText(AlbumTracksActivity.this, "لايوجد بالهاتف شريحه", Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar
                                        .make(v, "لايوجد بالهاتف شريحه ", Snackbar.LENGTH_LONG);

                                snackbar.show();
                            } else {
                                calltoneChecker(Constant.postion);
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


        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.playListName.size() > 0) {


                    if (musicSrv.isPlayingg()) {

                        // btn_play.requestLayout();
                        btn_play.setBackground(getResources().getDrawable(R.drawable.play_button_light_new));
                        //   btn_play.invalidate();

                        musicSrv.pausePlayer();

                        //  stopService(playIntent);

                        //   musicSrv.setShuffle();

                        //   AlbumFragment.b =1;
                        //new myPlayService().pauseMedia();
                        // MusicServiceSemsm.player.pause();

                    } else {
                        //  btn_play.requestLayout();
                        btn_play.setBackground(getResources().getDrawable(R.drawable.stop_button_light_new));
                        // btn_play.invalidate();

                        musicSrv.go();


                        //  AlbumFragment.b =0;
                        //  new myPlayService().playMedia();
                        // musicSrv.pausePlayer();
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

                        sharedPreferences = getSharedPreferences("playing", Context.MODE_PRIVATE);
                        String played = sharedPreferences.getString("track" + idList.get(Constant.postion), "");
                        editor = sharedPreferences.edit();
                        editor.putString("track" + idList.get(Constant.postion), "played");
                        editor.commit();
                        //  musicSrv.pausePlayer();

                        //  play();
                        // new myPlayService().pauseMedia();
                        //  playAudio();
                        musicSrv.pausePlayer();
                        play();


                    } else {
                        //  lastPosition=Constant.postion;
                        Constant.postion = Constant.playListUrl.size() - 1;
                        // new myPlayService().pauseMedia();
                        // playAudio();
                        musicSrv.pausePlayer();
                        play();

                        sharedPreferences = getSharedPreferences("playing", Context.MODE_PRIVATE);
                        String played = sharedPreferences.getString("track" + idList.get(Constant.postion), "");
                        editor = sharedPreferences.edit();
                        editor.putString("track" + idList.get(Constant.postion), "played");
                        editor.commit();

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
                        //  new myPlayService().pauseMedia();
                        /// playAudio();
                        musicSrv.pausePlayer();

                        // player.pause();
                        play();
                        //  playSong();
                        sharedPreferences = getSharedPreferences("playing", Context.MODE_PRIVATE);
                        String played = sharedPreferences.getString("track" + idList.get(Constant.postion), "");
                        editor = sharedPreferences.edit();
                        editor.putString("track" + idList.get(Constant.postion), "played");
                        editor.commit();
                    } else {


                        ///      lastPosition=searchnameResult.size()-1;
                        Constant.postion = 0;
                        // new myPlayService().pauseMedia();
                        // playAudio();


                        musicSrv.pausePlayer();

                        // player.pause();
                        play();
                        //   playSong();
                        sharedPreferences = getSharedPreferences("playing", Context.MODE_PRIVATE);
                        String played = sharedPreferences.getString("track" + idList.get(Constant.postion), "");
                        editor = sharedPreferences.edit();
                        editor.putString("track" + idList.get(Constant.postion), "played");
                        editor.commit();

                    }
                } else {
                    Snackbar snackbar = Snackbar
                            .make(v, "اختر النغمه التى تريد الاستماع اليها :( !!", Snackbar.LENGTH_LONG);

                    snackbar.show();

                }
            }
        });


///////// on touch lisner


        //-----------------------------------------------------------------------


 /*       btn_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {




                if(event.getAction() == MotionEvent.ACTION_DOWN) {

                    btn_back.setBackground(getResources().getDrawable(R.drawable.btn_prev_light));


                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    btn_back.setBackground(getResources().getDrawable(R.drawable.btn_prev));
                }

                return false;
            }
        });

        btn_next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {




                if(event.getAction() == MotionEvent.ACTION_DOWN) {

                    btn_next.setBackground(getResources().getDrawable(R.drawable.btn_next_light));


                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    btn_next.setBackground(getResources().getDrawable(R.drawable.btn_next));
                }

                return false;
            }
        });


        btn_CallTone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {




                if(event.getAction() == MotionEvent.ACTION_DOWN) {

                    btn_CallTone.setBackground(getResources().getDrawable(R.drawable.btn_ringtone_light));


                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    btn_CallTone.setBackground(getResources().getDrawable(R.drawable.btn_ringtone_light));
                }

                return false;
            }
        });



        btn_play.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {




                if(event.getAction() == MotionEvent.ACTION_DOWN) {

                    if (MusicServiceSemsm.player != null && MusicServiceSemsm.player.isPlaying())
                    {
                        btn_play.setBackground(getResources().getDrawable(R.drawable.btn_stop_light));

                    }
                    else {
                        btn_play.setBackground(getResources().getDrawable(R.drawable.btn_play_light));

                    }



                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    if (MusicServiceSemsm.player != null && MusicServiceSemsm.player.isPlaying())
                    {
                        btn_play.setBackground(getResources().getDrawable(R.drawable.stop_icon));

                    }
                    else {
                        btn_play.setBackground(getResources().getDrawable(R.drawable.play_button));

                    }                }

                return false;
            }
        });


*/


    }


    public void ringtone(String path) {
        // Create File object for the specified ring tone path
        File f = new File(path);


        ContentValues content = new ContentValues();
        content.put(MediaStore.MediaColumns.DATA, f.getAbsolutePath());
        content.put(MediaStore.MediaColumns.TITLE, Constant.playListName.get(Constant.postion));
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


    public void inti() {
        // profileImage= (ImageView) findViewById(R.id.profileImageView);


        startTimeTxt = (TextView) findViewById(R.id.startTimetxt);
        songNametxt = (TextView) findViewById(R.id.songNametxt);
        finalTimeTxt = (TextView) findViewById(R.id.endTimetxt);


        btn_play = (Button) findViewById(R.id.btnPlayerPlay);
        btn_back = (Button) findViewById(R.id.btnPlayerBack);
        btn_next = (Button) findViewById(R.id.btnPlayerNext);

        btn_CallTone = (Button) findViewById(R.id.callToneBtn);
        btn_Fav = (Button) findViewById(R.id.favbtn);

        seekBar = (SeekBar) findViewById(R.id.seekBar2);


        btn_retry = (Button) findViewById(R.id.retry);
        //  progressActivity = (ProgressActivity) findViewById(R.id.progress);
        mRecyclerView = (RecyclerView) findViewById(R.id.MusicListRV);
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
    public void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicServiceSemsm.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }

    }


    @Override
    public void onPause() {
        super.onPause();



       /* if (mBufferBroadcastIsRegistered) {
            unregisterReceiver(broadcastBufferReceiver);
            mBufferBroadcastIsRegistered = false;
        }*/
    /*    if (mBroadcastIsRegistered) {
            unregisterReceiver(broadcastReceiver);
            mBroadcastIsRegistered = false;
        }
*/

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
                                    //musicSrv.go();
                                    musicSrv.pausePlayer();
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


  /*      if (MusicServiceSemsm.player != null) {
            if (MusicServiceSemsm.player.isPlaying()) {


                telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                //   Log.v(TAG, "Starting listener");
                phoneStateListener = new PhoneStateListener() {
                    @Override
                    public void onCallStateChanged(int state, String incomingNumber) {
                        // String stateString = "N/A";
                        // Log.v(TAG, "Starting CallStateChange");
                        switch (state) {
                            case TelephonyManager.CALL_STATE_OFFHOOK:
                            case TelephonyManager.CALL_STATE_RINGING:
                                if (MusicServiceSemsm.player != null) {
                                    MusicServiceSemsm.player.pause();
                                    isPausedInCall = true;
                                }

                                break;
                            case TelephonyManager.CALL_STATE_IDLE:
                                // Phone idle. Start playing.
                                if (MusicServiceSemsm.player != null) {
                                    if (isPausedInCall) {
                                        isPausedInCall = false;
                                        MusicServiceSemsm.player.start();
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
        }*/

    }


    @Override
    public void onResume() {
        super.onResume();


        // Register broadcast receiver
        if (!mBufferBroadcastIsRegistered) {
            registerReceiver(broadcastBufferReceiver, new IntentFilter(
                    MusicServiceSemsm.BROADCAST_BUFFER));
            mBufferBroadcastIsRegistered = true;
        }
        if (MusicServiceSemsm.player.isPlaying()) {

            findViewById(R.id.playerSheetView).setVisibility(View.VISIBLE);
            btn_play.setBackground(getResources().getDrawable(R.drawable.stop_button_light_new));
            songNametxt.setText(Constant.playListName.get(Constant.postion));


        }


    }



    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
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
        if (id == R.id.search_action) {

            startActivity(new Intent(AlbumTracksActivity.this, SearchActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}


class RecyclerTouchListenerAlbumTracks implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private AlbumTracksActivity.ClickListener clickListener;

    public RecyclerTouchListenerAlbumTracks(Context context, final RecyclerView recyclerView,
                                            final AlbumTracksActivity.ClickListener clickListener) {
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



