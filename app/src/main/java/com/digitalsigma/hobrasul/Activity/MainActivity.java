package com.digitalsigma.hobrasul.Activity;

import android.Manifest;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
//import com.firebase.client.Firebase;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digitalsigma.hobrasul.Activity.Videos.Download.DownloadVideos;
import com.digitalsigma.hobrasul.Activity.Videos.PlayVideo.VideoPlayerActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.widget.LikeView;
import com.digitalsigma.hobrasul.Adapter.CustomTypefaceSpan;
import com.digitalsigma.hobrasul.Service.FCMRegistrationService;
import com.digitalsigma.hobrasul.Service.MusicServiceSemsm;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
//import com.music.test.musicpro.Adapter.NotificationListener;
import com.digitalsigma.hobrasul.Adapter.RoundImage;
import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import net.alhazmy13.catcho.library.Catcho;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;


    ProgressDialog progressDialog;


    boolean mBufferBroadcastIsRegistered;
    boolean exit=false;

    Intent serviceIntent;

    Handler handler;

    private boolean isOnline;
    private boolean boolMusicPlaying = false;

    // Set up the notification ID
    private static final int NOTIFICATION_ID = 1;
    private boolean isPausedInCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;

    View toneViewSnackBar;




    // --Set up constant ID for broadcast of seekbar position--
    public static final String BROADCAST_SEEKBAR = "com.gmsproduction.tarekelsheikh.sendseekbar";
    Intent intent;






   public Button btn_play;
    Button btn_next;
    Button btn_back;
    Button btn_CallTone;
    Button btn_Fav;

    TextView startTimeTxt;
    TextView songNametxt;
    TextView finalTimeTxt;

    SeekBar seekBar;

    LinearLayout custom;
    ImageView drawer;


    //service
    private MusicServiceSemsm musicSrv;
    private Intent playIntent;
    //binding
    private boolean musicBound=false;


    private RecyclerView mRecyclerView;




    SharedPreferences facebookInfo;




    private double startTime = 0;
    private double finalTime = 0;
    Button  btn_tracks,btn_video,btn_party,btn_news,btn_downloads,btn_gallary,btn_center;


    LikeView likeView;
    InterstitialAd interstitial;
    ImageView imageView;
    RoundImage roundImage;
    CallbackManager callbackManager;
    LoginButton loginButton;
    String id;

    View header;
    CircleImageView circleImageView;

    TextView UserNameTxt;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    MenuItem nav_login;
    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Catcho.Builder(this)
                .activity(ContactActivity.class)
                .build();



        setContentView(R.layout.activity_main);



        init();
         setMainLisner();
        StartAnimations();

        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);




        checkAndRequestPermissions();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().getThemedContext();





        if(Build.VERSION.SDK_INT <= 19) {
            custom= (LinearLayout) findViewById(R.id.custom);
            custom.setVisibility(View.VISIBLE);
          /*  getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            customNav = LayoutInflater.from(this).inflate(
                    R.layout.custom_actionbar, null);
            getSupportActionBar().setCustomView(customNav);*/

        }

        startService(new Intent(this,FCMRegistrationService.class));

      /*  FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();*/
        try {


            intent = new Intent(BROADCAST_SEEKBAR);

        } catch (Exception e) {
            e.printStackTrace();
        }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setItemIconTintList(null);
        navigationView.setBackground(getResources().getDrawable(R.drawable.bg_main));
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
        //navigationView.getHeaderView(R.id.title).setBackgroundColor(Color.WHITE);


        Menu menu = navigationView.getMenu();

        MenuItem tools= menu.findItem(R.id.title);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
        tools.setTitle(s);
        navigationView.setNavigationItemSelectedListener(this);


        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }



        header = navigationView.getHeaderView(0);

        circleImageView= (CircleImageView)header.findViewById(R.id.logo_image_nav);
        UserNameTxt=(TextView)header.findViewById(R.id.userNameTxt);



        // get menu from navigationView
        menu = navigationView.getMenu();
        // find MenuItem you want to change
        nav_login = menu.findItem(R.id.nav_login);




        if(AccessToken.getCurrentAccessToken()!=null)
        {
            Log.v("User is login","YES");
            // Toast.makeText(MainActivity.this, "in", Toast.LENGTH_SHORT).show();
            sharedPreferences = getSharedPreferences("info",Context.MODE_PRIVATE);
            String Img=sharedPreferences.getString("imgUrl","url");
            String Name=sharedPreferences.getString("name","name");
            Picasso
                    .with(MainActivity.this)
                    .load(Img)

                    .into(circleImageView);

            UserNameTxt.setText(Name);
            nav_login.setTitle("Logout");




        }
        else
        {
            Log.v("User is not login","OK");
            //   Toast.makeText(MainActivity.this, "out", Toast.LENGTH_SHORT).show();
            nav_login.setTitle("Login");
        }


      //  MobileAds.initialize(getApplicationContext(),"ca-app-pub-7056789173094146~2313294112");
        AdView adView = (AdView) findViewById(R.id.adView);
       // adView.setAdUnitId("ca-app-pub-7056789173094146~2313294112");
      //  AdRequest adRequest = new AdRequest.Builder() .setRequestAgent("android_studio:ad_template").build();
        AdRequest adRequest = new AdRequest.Builder().build();
     //  adRequest.isTestDevice(t);

        adView.loadAd(adRequest);






    }


    private void applyFontToMenuItem(MenuItem mi) {
       // Typeface font = Typeface.createFromAsset(getAssets(), "ds_digi_b.TTF");
        Typeface font=Typeface.createFromAsset(getAssets(),"hacen.ttf");

        SpannableString mNewTitle = new SpannableString(mi.getTitle());

        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

/*    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }*/

    private  boolean checkAndRequestPermissions() {
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
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
      /*  LinearLayout l=(LinearLayout) findViewById(R.id.main_activity_mo);
        l.clearAnimation();
        l.startAnimation(anim);*/

       /* anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();*/
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

  /*      anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        btn_center.clearAnimation();
        btn_center.startAnimation(anim);*/




    }



    @Override
    public void onPause() {
        super.onPause();

      /*  if (mBufferBroadcastIsRegistered) {
            unregisterReceiver(broadcastBufferReceiver);
            mBufferBroadcastIsRegistered = false;
        }*/

       // if (musicSrv != null) {
           /* if (mBroadcastIsRegistered = true) {
                unregisterReceiver(broadcastReceiver);
                mBroadcastIsRegistered = false;
            }*/
      //  }


       // if (MusicServiceSemsm.player != null) {
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
                                        musicSrv.go();
                                     //   MusicServiceSemsm.player.start();
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


       // }
    }



    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            //"لكى تكون كول تون لهاتفك "+Constant.playListName.get(position)+"لقد اخترت "
            builder.setMessage("هل تريد اغلاق التطبيق");

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    AdRequest adRequest = new AdRequest.Builder().build();

                    // Prepare the Interstitial Ad
                    interstitial = new InterstitialAd(MainActivity.this);
// Insert the Ad Unit ID
                    interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));

                    interstitial.loadAd(adRequest);


                    interstitial.setAdListener(new AdListener() {
                        public void onAdLoaded() {
                            // Call displayInterstitial() function
                            displayInterstitial();
                        }
                    });





                    dialog.dismiss();

                    finish();

                    //  System.exit(0);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    AdRequest adRequest = new AdRequest.Builder().build();
                    // Prepare the Interstitial Ad
                    interstitial = new InterstitialAd(MainActivity.this);
// Insert the Ad Unit ID
                    interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
                    interstitial.loadAd(adRequest);
                    interstitial.setAdListener(new AdListener() {
                        public void onAdLoaded() {
                            // Call displayInterstitial() function
                            displayInterstitial();
                        }
                    });


                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();

            //super.onBackPressed();
        }





    }


    @Override
    protected void onStart() {
        super.onStart();
      //  startService(serviceIntent);

        if(playIntent==null){
            playIntent = new Intent(this, MusicServiceSemsm.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }




    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }


    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return formatSize(availableBlocks * blockSize);
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
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv=null;
        super.onDestroy();





        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();

        AdRequest adRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(MainActivity.this);
// Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));

        interstitial.loadAd(adRequest);


        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                displayInterstitial();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(AccessToken.getCurrentAccessToken()!=null)
        {
            Log.v("User is login","YES");
            // Toast.makeText(MainActivity.this, "in", Toast.LENGTH_SHORT).show();
            sharedPreferences = getSharedPreferences("info",Context.MODE_PRIVATE);
            String Img=sharedPreferences.getString("imgUrl","url");
            String Name=sharedPreferences.getString("name","name");
            Picasso
                    .with(MainActivity.this)
                    .load(Img)
                    .into(circleImageView);

            UserNameTxt.setText(Name);
            nav_login.setTitle("Logout");




        }
        else
        {
            Log.v("User is not login","OK");
            //   Toast.makeText(MainActivity.this, "out", Toast.LENGTH_SHORT).show();
            nav_login.setTitle("Login");
        }
    }
    public void userRegister(final String name, final String email, final String gender, final String profileId, final String phone)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.registeration,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("fadle",response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(SearchActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        // loginUser();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",name);
                params.put("email",email);
                params.put("id",profileId);
                params.put("gender",gender);
                params.put("phone_no",phone);
               /* params.put(KEY_PASSWORD,password);
                params.put(KEY_EMAIL, email);*/
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Log out");
        progressDialog.show();

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/", null,
                HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

                startActivity(new Intent(MainActivity.this,SplashScreenActivity.class));
                finish();

                progressDialog.dismiss();

            }
        }).executeAsync();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // get menu from navigationView
        Menu menu = navigationView.getMenu();
        // find MenuItem you want to change
        MenuItem nav_login = menu.findItem(R.id.nav_login);

        nav_login.setTitle("Login");
        sharedPreferences = getSharedPreferences("info",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }

    public void fbLogin(View view)
    {
        LoginManager.getInstance().logInWithReadPermissions(
                MainActivity.this,
                Arrays.asList("public_profile,email"));
        // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("name,link,email,gender,birthday"));
        //  LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>()
                {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        // App code

                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                        // get menu from navigationView
                        Menu menu = navigationView.getMenu();
                        // find MenuItem you want to change
                        MenuItem nav_login = menu.findItem(R.id.nav_login);

                        nav_login.setTitle("Logout");


                        Toast.makeText(MainActivity.this, "Welcome login", Toast.LENGTH_SHORT).show();

                        final AccessToken accessToken = loginResult.getAccessToken();





                        GraphRequest request = GraphRequest.newMeRequest(
                                accessToken,
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {


                                        id=object.optString("id").toString();
                                        String name=object.optString("name").toString();
                                        String email=object.optString("email").toString();
                                        String gender=object.optString("gender").toString();

                                     /*   Toast.makeText(MainActivity.this, "name  "
                                                +name+" email"+email+" id"+
                                                id, Toast.LENGTH_SHORT).show();*/

                                        String profileImg="https://graph.facebook.com/"+id+"/picture?width=200&height=150";


                                        //   Toast.makeText(MainActivity.this, "name"+email, Toast.LENGTH_SHORT).show();
                                        sharedPreferences = getSharedPreferences("info",Context.MODE_PRIVATE);
                                        editor = sharedPreferences.edit();
                                        editor.clear();

                                        editor.putString("email",email);
                                        editor.putString("name",name);
                                        editor.putString("id",object.optString("id").toString());
                                        editor.putString("gender",gender);
                                        editor.putString("login",object.optString("login").toString());
                                        editor.putString("imgUrl",profileImg);
                                        editor.commit();

                                        phoneNumber();
                                        String phone=phoneNumber();

                                        userRegister(name,email,gender,id,phone);



                                        Picasso
                                                .with(MainActivity.this)
                                                .load(profileImg)
                                                .into(circleImageView);

                                        UserNameTxt.setText(name);




                              /*  Toast.makeText(SplashScreenActivity.this, ""+object.optString("email").toString(), Toast.LENGTH_SHORT).show();
                                // Application code
                                Toast.makeText(SplashScreenActivity.this, ""+object.optString("id").toString(), Toast.LENGTH_SHORT).show();

                                Log.d("id",object.optString("id").toString());
                                Toast.makeText(SplashScreenActivity.this, ""+object.optString("name").toString(), Toast.LENGTH_SHORT).show();

                                Toast.makeText(SplashScreenActivity.this, ""+object.optString("gender").toString(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(SplashScreenActivity.this, ""+object.optString("birthday").toString(), Toast.LENGTH_SHORT).show();
*/
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,link,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();


                    }

                    @Override
                    public void onCancel()
                    {
                        Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT).show();

                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception)
                    {

                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                        // App code
                    }
                });
    }


    public String phoneNumber()
    {
        String main_data[] = {"data1", "is_primary", "data3", "data2", "data1",
                "is_primary", "photo_uri", "mimetype"};
        Object object = getContentResolver().
                query(Uri.withAppendedPath(android.provider.ContactsContract.Profile.CONTENT_URI, "data"),
                        main_data, "mimetype=?",
                        new String[]{"vnd.android.cursor.item/phone_v2"},
                        "is_primary DESC");
        String s1="";
        if (object != null) {
            do {
                if (!((Cursor) (object)).moveToNext())
                    break;
                // This is the phoneNumber
                s1 =s1+ ((Cursor) (object)).getString(4);
            } while (true);
            ((Cursor) (object)).close();
        }
        return s1;
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Home) {
            // Handle the camera action
        } /*else if (id == R.id.Music) {

        }*/
        else if (id == R.id.Gallary_list) {
            startActivity(new Intent(MainActivity.this,GallaryActivity.class));
           // Toast.makeText(MainActivity.this, "gallary", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.Search) {
            startActivity(new Intent(MainActivity.this,SearchActivity.class));
        } else if (id == R.id.Downloads) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("هل تريد تحميلات");
            builder.setPositiveButton("مقاطع الفيديو", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(MainActivity.this, VideoPlayerActivity.class);
                    i.putExtra("vid","2");
                    i.putExtra("title","تحميلات الفيديو");

                    startActivity(i);
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("اللفات الصوتية", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(MainActivity.this,DownLoadsActivity.class);
                    i.putExtra("title","تحميلات الصوت");
                    startActivity(i);
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
        else if (id == R.id.About) {
            startActivity(new Intent(MainActivity.this,AboutActivity.class));
        }
        else if (id == R.id.nav_login)
        {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            // get menu from navigationView
            Menu menu = navigationView.getMenu();
            // find MenuItem you want to change
            MenuItem nav_login = menu.findItem(R.id.nav_login);
            //   nav_login.setTitle("Logout");
            if(AccessToken.getCurrentAccessToken()!=null)
            {
                // set new title to the MenuItem
                //nav_login.setTitle("Login");

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                //"لكى تكون كول تون لهاتفك "+Constant.playListName.get(position)+"لقد اخترت "
                builder.setMessage("هل تريد تسجيل الخروج");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(MainActivity.this, "logout", Toast.LENGTH_SHORT).show();

                        disconnectFromFacebook();
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
            }
            else {
                Toast.makeText(MainActivity.this, "login", Toast.LENGTH_SHORT).show();
                // loginFacebook();
                fbLogin(findViewById(R.id.nav_login));
                sharedPreferences = getSharedPreferences("info",Context.MODE_PRIVATE);
                String Img=sharedPreferences.getString("imgUrl","url");
                String Name=sharedPreferences.getString("name","name");
            }
        }

        else if (id == R.id.nav_facebook) {

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/885739378144732"));
                startActivity(intent);
            } catch(Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/m.sultanlovers")));
            }
         //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/AlshykhYasynAlthamy/?fref=ts")));

        } else if (id == R.id.nav_soundcloud) {

            String idd = "721786940"; //Userid
          //  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("soundcloud://users:" + idd));

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("soundcloud://users:85891519"));
                startActivity(intent);
            } catch(Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://soundcloud.com/user-85891519")));
            }


          //  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://soundcloud.com/user-721786940"));

           // startActivity(intent);

          //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCR9DlQqNGqfp0jwql6q2r1A")));


        }
        else if (id == R.id.nav_youtube) {

            Intent i = new Intent(MainActivity.this, VideoPlayerActivity.class);
            i.putExtra("vid","1");
            i.putExtra("title","فديوهات");
            startActivity(i);

            // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/playlist?list=PLO_vsaTqh2ZuzfofnOyPlzdHLATlNmTcD")));

            //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCR9DlQqNGqfp0jwql6q2r1A")));


        }
        else if (id == R.id.nav_youtubeChannel) {



           // startActivity(new Intent(MainActivity.this,VideosActivity.class));

            // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/playlist?list=PLO_vsaTqh2ZuzfofnOyPlzdHLATlNmTcD")));

              startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/playlist?list=PLO_vsaTqh2Zsk5wWlcoMCKK2frojOLCet")));


        }



    //    https://www.youtube.com/playlist?list=PLO_vsaTqh2ZuzfofnOyPlzdHLATlNmTcD
        else if (id == R.id.nav_exit)
        {




            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            //"لكى تكون كول تون لهاتفك "+Constant.playListName.get(position)+"لقد اخترت "
            builder.setMessage("هل تريد اغلاق التطبيق");

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    AdRequest adRequest = new AdRequest.Builder().build();
                    // Prepare the Interstitial Ad
                    interstitial = new InterstitialAd(MainActivity.this);
                   // Insert the Ad Unit ID
                    interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
                    interstitial.loadAd(adRequest);
                    interstitial.setAdListener(new AdListener() {
                        public void onAdLoaded() {
                            // Call displayInterstitial() function
                            displayInterstitial();
                        }
                    });


                    dialog.dismiss();

                    /*System.gc();
                    System.exit(0);*/

                    finish();

                    //  System.exit(0);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    AdRequest adRequest = new AdRequest.Builder().build();
                    // Prepare the Interstitial Ad
                    interstitial = new InterstitialAd(MainActivity.this);
                     // Insert the Ad Unit ID
                    interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
                    interstitial.loadAd(adRequest);
                    interstitial.setAdListener(new AdListener() {
                        public void onAdLoaded() {
                            // Call displayInterstitial() function
                            displayInterstitial();
                        }
                    });


                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();




        }
        else if (id == R.id.nav_twitter) {

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("twitter://user?screen_name=semsemshehab1"));
                startActivity(intent);

            }catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://twitter.com/#!/semsemshehab1")));
            }

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    public void init()
    {
        drawer= (ImageView) findViewById(R.id.drawer);
        btn_party = (Button) findViewById(R.id.btn_anashid);
        btn_news= (Button) findViewById(R.id.btn_news);
        btn_video= (Button) findViewById(R.id.btn_videos);
        btn_tracks= (Button) findViewById(R.id.btn_tracks);
        btn_downloads= (Button) findViewById(R.id.btn_downloads);
        btn_gallary= (Button) findViewById(R.id.btn_gallary);
        btn_center= (Button) findViewById(R.id.center);



    }


    public void setMainLisner()
    {
        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);


            }
        });

        btn_tracks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, TracksActivity.class);
                i.putExtra("Page","1");
                i.putExtra("title","تواشيح ومدح");
                startActivity(i);
                //startActivity(new Intent(MainActivity.this, TracksActivity.class));

            }
        });

        btn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, VideoPlayerActivity.class);
                i.putExtra("vid","1");
                i.putExtra("title","فديوهات");

                startActivity(i);


            }
        });

        btn_party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TracksActivity.class);
                i.putExtra("Page","2");
                i.putExtra("title","أناشيد");
                startActivity(i);


            }
        });

        btn_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewsActivity.class));

            }
        });

        btn_downloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("هل تريد تحميلات");
                builder.setPositiveButton("مقاطع الفيديو", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MainActivity.this, VideoPlayerActivity.class);
                        i.putExtra("vid","2");
                        i.putExtra("title","تحميلات الفيديو");
                        startActivity(i);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("اللفات الصوتية", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MainActivity.this,DownLoadsActivity.class);
                        i.putExtra("title","تحميلات الصوت");
                        startActivity(i);
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }



        });

        btn_gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GallaryActivity.class));

            }
        });

        btn_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CvActiviy.class));

            }
        });

    }





}
