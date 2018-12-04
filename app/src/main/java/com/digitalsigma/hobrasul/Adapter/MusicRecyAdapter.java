package com.digitalsigma.hobrasul.Adapter;


import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


//import com.koushikdutta.ion.Ion;
import com.digitalsigma.hobrasul.Activity.DialActivity;
import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;
import com.digitalsigma.hobrasul.Service.MusicServiceSemsm;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ahmedabouelfadle on 22/03/16.
 */
public class MusicRecyAdapter extends RecyclerView.Adapter<MusicRecyAdapter.ViewHolder> {
    private ArrayList<String> trackNameList,trackImgList;
    Context context;
    MediaPlayer mediaPlayer;
    int pos;
    public String trackName="";
    private MusicServiceSemsm musicSrv;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    ArrayList<String> downloadedSongUrl1=new ArrayList<String>();
    ArrayList<String> downloadedSongName1=new ArrayList<String>();
    ArrayList<String> TrackNameList = new ArrayList<String>();
    ArrayList<String> TrackUrlList = new ArrayList<String>();
    ArrayList<String> TrackIdList = new ArrayList<String>();
    ArrayList<String> TrackVodList = new ArrayList<String>();
    ArrayList<String> TrackOrList = new ArrayList<String>();
    ArrayList<String> TrackEtList = new ArrayList<String>();
    ArrayList<String> TrackImgList = new ArrayList<String>();
    ArrayList<String> TrackAlbumIdList = new ArrayList<String>();
    ArrayList<String> TrackRingtoneList = new ArrayList<String>();

    ArrayList<String> IdList = new ArrayList<String>();
    ArrayList<String> Artistname = new ArrayList<String>();

    String checkRadio;
    Typeface t1;


    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView username;
        public TextView newsTxt;
        public ImageView imageView;
       public ProgressBar progressBar;

        public ImageView imageViewGif;
        public ImageButton downloadBtn;
        public Button callToneBtn;


        public LinearLayout animRow;




        //  public TextView vod;



        // public TextView txtFooter;

        public ViewHolder(View v) {
            super(v);
            username = (TextView) v.findViewById(R.id.musicName);
            newsTxt = (TextView) v.findViewById(R.id.newsTxt);
            imageView= (ImageView) v.findViewById(R.id.imageView3);
           // downloadBtn= (ImageButton) v.findViewById(R.id.downloadAction);

            callToneBtn= (Button) v.findViewById(R.id.toneAction);
          //  imageViewGif= (ImageView) v.findViewById(R.id.gif_tracks);

            animRow= (LinearLayout) v.findViewById(R.id.tracksListRowAnim);


            // imageViewGif.setTag("gif");
            progressBar= (ProgressBar) v.findViewById(R.id.TrackLoadingDownload);










            // txtFooter = (TextView) v.findViewById(R.id.secondLine);
        }
    }

    public void add(int position, String item) {
        trackNameList.add(position, item);
        //  speechTypeArray.add(position,speech);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = trackNameList.indexOf(item);
        trackNameList.remove(position);
        notifyItemRemoved(position);
    }

    public void change(int position)
    {


      // notifyItemChanged(position,"http://musicpro.890m.com/musicpro/img/DSCF3266.JPG");

    }

    public MusicRecyAdapter()
    {}
    // Provide a suitable constructor (depends on the kind of dataset)
    public MusicRecyAdapter(ArrayList<String> name
            , ArrayList<String> trackurl, ArrayList<String> vod,
                            ArrayList<String> or, ArrayList<String> et, ArrayList<String> albumid,
                             ArrayList<String> ingtonetrackurl,ArrayList<String> idList
            ,Context co) {
        trackNameList = name;
        TrackAlbumIdList=albumid;
        TrackEtList=et;
        TrackNameList=name;
        TrackOrList=or;
        TrackVodList=vod;
        TrackUrlList=trackurl;
        TrackRingtoneList=ingtonetrackurl;
        IdList=idList;
        context=co;
        //speechTypeArray=mySpeechTypeArray;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MusicRecyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_tracks, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

      /*  Picasso
                .with(context)
                .load(songUrlList.get(position))
                .into(holder.imageView);*/



     ///   Ion.with(holder.imageViewGif).load("http://www.animatedimages.org/data/media/1377/animated-music-note-image-0028.gif");



        Animation anim = AnimationUtils.loadAnimation(context, R.anim.alpha);
        anim.reset();
        /*LinearLayout l=(LinearLayout) context.findViewById(R.id.main_activity_mo);
        l.clearAnimation();
        l.startAnimation(anim);*/

      /*  anim = AnimationUtils.loadAnimation(context, R.anim.translate);
        anim.reset();*/
        //ImageView iv = (ImageView) findViewById(R.id.splash);

        holder.animRow.clearAnimation();
        holder.animRow.startAnimation(anim);

        t1=Typeface.createFromAsset(context.getAssets(),"hacen.ttf");

        sharedPreferences=context.getSharedPreferences("playing",Context.MODE_PRIVATE);
        String played=sharedPreferences.getString("track"+IdList.get(position),"");
       // Toast.makeText(context, "test  "+played, Toast.LENGTH_SHORT).show();

        if (played.equals("played"))
        {

            holder.username.setText(trackNameList.get(position));

            holder.newsTxt.setVisibility(View.GONE);
            holder.newsTxt.setTypeface(t1);

        }
        else {
            holder.username.setText(trackNameList.get(position));
            holder.newsTxt.setTypeface(t1);
            holder.newsTxt.setVisibility(View.VISIBLE);

        }



        t1=Typeface.createFromAsset(context.getAssets(),"hacen.ttf");
        holder.username.setTypeface(t1);

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



/*

                Constant.pointer=1;
                Constant.songName=trackNameList.get(position);
                Constant.songUrl=TrackUrlList.get(position);

                Constant.playListName=trackNameList;
                Constant.playListUrl=TrackUrlList;
                Constant.RingToneUrl=TrackRingtoneList;

                Constant.postion=position;


                Constant.EtisCallToneList=TrackEtList;
                Constant.vodCallToneList=TrackVodList;
                Constant.orangCallToneList=TrackOrList;

                holder.username.setText(Constant.playListName.get(position));


                musicSrv.playSong();
*/














             /*   LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(v.getContext());
                View parentView=inflater.inflate(R.layout.player_sheet_dialog,null);
                bottomSheetDialog.setContentView(parentView);
                BottomSheetBehavior bottomSheetBehavior=BottomSheetBehavior.from((View)parentView.getParent());
                bottomSheetBehavior.setPeekHeight((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,v.getContext().getResources().getDisplayMetrics()));
                bottomSheetBehavior.setHideable(false);
                bottomSheetDialog.show();
*/


               /* Toast.makeText(v.getContext(), "user id"+userIdList.get(position), Toast.LENGTH_SHORT).show();
                v.getContext().startActivity(new Intent(v.getContext(),PlayerActivity.class));*/


            }
        });

  /*      holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(v.getContext(), "download", Toast.LENGTH_SHORT).show();


                Toast.makeText(v.getContext(), "download", Toast.LENGTH_SHORT).show();
                File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
                File directory = new File(SDCardRoot, "/MusicPro/"); //create directory to keep your downloaded file

                Toast.makeText(v.getContext(), SDCardRoot+"/MusicPro/", Toast.LENGTH_SHORT).show();
                if (!directory.exists())
                {
                    directory.mkdir();
                }


                DownloadManager.Request req=new DownloadManager.Request(Uri.parse(Constant.songUrl));

                req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle(Constant.songName)
                        .setDescription("Song is Being Downloaded......")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(String.valueOf(directory),Constant.songName);

                DownloadManager manager= (DownloadManager) v.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(req);
            }
        });*/

        holder.callToneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(v.getContext());
                View parentView=inflater.inflate(R.layout.ringtone_calltone_action_sheet,null);
                bottomSheetDialog.setContentView(parentView);
                BottomSheetBehavior bottomSheetBehavior=BottomSheetBehavior.from((View)parentView.getParent());
                bottomSheetBehavior.setPeekHeight((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,v.getContext().getResources().getDisplayMetrics()));
                bottomSheetDialog.show();


               final Button ringtone= (Button) parentView.findViewById(R.id.btn_ringTone);

                final Button calltone= (Button) parentView.findViewById(R.id.btn_callTone);

                ringtone.setTypeface(t1);
                calltone.setTypeface(t1);


                ringtone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        settingPermission();
                        pos = holder.getAdapterPosition();

                        if (!(pos > 0) || TrackRingtoneList.size() == 0 )
                        {
                            Snackbar snackbar = Snackbar
                                    .make(v, "قم بتشغيل الاغنيه ", Snackbar.LENGTH_LONG);

                            snackbar.show();
                        }
                        else if (pos==0 || TrackRingtoneList.size() == 0)
                        {
                            Snackbar snackbar = Snackbar
                                    .make(v, "قم بتشغيل الاغنيه ", Snackbar.LENGTH_LONG);

                            snackbar.show();
                        }
                        else {

                            if (TrackRingtoneList.get(pos).equals("") || TrackRingtoneList.get(pos).equals(null)
                                    || TrackRingtoneList.get(pos).equals("null")) {
                                Snackbar snackbar = Snackbar
                                        .make(v, "لايوجد رابط ", Snackbar.LENGTH_LONG);

                                snackbar.show();
                            } else {
                                if (isStoragePermissionGranted() ) {
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

                                        }

                                        if (downloadedSongName1.contains(TrackNameList.get(pos)+".mp3")) {

                                            File SDCardRoot1 = Environment.getExternalStorageDirectory(); // location where you want to store
                                            File file1 = new File(Environment.getExternalStorageDirectory(), SDCardRoot + "/SemsmShehabRingTone/" +
                                                    TrackNameList.get(pos)+".mp3");


                                            ringtone(file1.toString() + "", pos);

                                            Toast.makeText(context, "لقد اخترت ' " + trackNameList.get(pos) + "' علشان تبقى رنة موبايلك", Toast.LENGTH_SHORT).show();


                                        }
                                        else {

                                            downloadong(TrackRingtoneList.get(holder.getAdapterPosition()), TrackNameList.get(holder.getAdapterPosition()),
                                                    "SemsmShehabRingTone");
//                                        Toast.makeText(context, "لقد اخترت ' " + Constant.playListName.get(Constant.postion) + "' علشان تبقى رنة موبايلك", Toast.LENGTH_SHORT).show();


                                        }

                                    } else {

                                        downloadong(TrackRingtoneList.get(holder.getAdapterPosition()), TrackNameList.get(holder.getAdapterPosition()),
                                                "SemsmShehabRingTone");
                                        //  Toast.makeText(context, "Not Exist", Toast.LENGTH_SHORT).show();

                                    }

                         /* else {

                                downloadong(TrackRingtoneList.get(holder.getAdapterPosition()), TrackNameList.get(holder.getAdapterPosition()),
                                        "ElMuledRingTone");
                            }*/

                                }
                            }
                        }
                        bottomSheetDialog.hide();




                    }
                });



                calltone.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        pos=holder.getAdapterPosition();

                        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
                        String number = tm.getNetworkOperatorName();
                        if (number.equals("") || number.equals(null))
                        {
                            Toast.makeText(context, "لايوجد بالهاتف شريحه", Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar
                                    .make(v, "لايوجد بالهاتف شريحه ", Snackbar.LENGTH_LONG);

                            snackbar.show();
                        }
                        else {
                            calltoneChecker(holder.getAdapterPosition(),v);
                            bottomSheetDialog.hide();

                        }



                    }
                });

            }
        });


    }




    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return trackNameList.size();
    }


    public void calltoneChecker(int position,View toneViewSnackBar)
    {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String number = tm.getNetworkOperatorName();
        Log.d("sim type",number);
        Toast.makeText(context, ""+number, Toast.LENGTH_SHORT).show();

        if (number.toLowerCase().contains("vodaf"))
        {

            if (!(TrackVodList.get(position).equals("") || TrackVodList.get(position).equals(null)
                    || TrackVodList.get(position).equals("null")) ) {
                // if (!TrackVodList.get(position).equals("")) {
                //Constant.ussd=Constant.vodCallToneList.get(position);
                Constant.ussd=TrackVodList.get(position);
                // Toast.makeText(context, "vodafone code find" +TrackVodList.get(position), Toast.LENGTH_SHORT).show();

                confirmDailog(position,"vod");


            }
            else {
                Constant.ussd="";
                Toast.makeText(context, "لا يوجد كول تون فى هذه الشبكه", Toast.LENGTH_SHORT).show();

                Snackbar snackbar = Snackbar
                        .make(toneViewSnackBar, "لا يوجد كول تون فى هذه الشبكه :( !!", Snackbar.LENGTH_LONG);



                snackbar.show();
            }




            //   Toast.makeText(TracksActiviy.this, "call   true", Toast.LENGTH_SHORT).show();
        }
        else if (number.toLowerCase().contains("etis"))
        {

            if (!(TrackEtList.get(position).equals("")||
                    TrackEtList.get(position).equals(null)||
                    TrackEtList.get(position).equals("null"))) {


                // if (!TrackEtList.get(position).equals("")) {
                //  Constant.ussd=Constant.EtisCallToneList.get(position);

                Constant.ussd=TrackEtList.get(position);

                confirmDailog(position,"et");


                //   Toast.makeText(TracksActiviy.this, "elislate code find" +Constant.EtisCallToneList.get(position), Toast.LENGTH_SHORT).show();
            }
            else {
                Constant.ussd="";
                Toast.makeText(context, "لا يوجد كول تون فى هذه الشبكه", Toast.LENGTH_SHORT).show();

                Snackbar snackbar = Snackbar
                        .make(toneViewSnackBar, "لا يوجد كول تون هنا :( !!", Snackbar.LENGTH_LONG);


                snackbar.show();            }



        }
        else if (number.toLowerCase().contains("oran"))
        {

            if (!(TrackOrList.get(position).equals("")
                    || TrackOrList.get(position).equals(null)
                    || TrackOrList.get(position).equals("null"))) {
                //  if (!TrackOrList.get(position).equals("")) {

                // Constant.ussd=Constant.orangCallToneList.get(position);
                Constant.ussd=TrackOrList.get(position);

                confirmDailog(position,"or");





                // Toast.makeText(MainActivity.this, "orange code find" +Constant.orangCallToneList.get(position), Toast.LENGTH_SHORT).show();
            }
            else {
                Constant.ussd="";
                Toast.makeText(context, "لا يوجد كول تون فى هذه الشبكه", Toast.LENGTH_SHORT).show();

                // Toast.makeText(MainActivity.this, "noooo", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar
                        .make(toneViewSnackBar, "لا يوجد كول تون فى هذه الشبكه :( !!", Snackbar.LENGTH_LONG);


                snackbar.show();
            }






        }else if (number.toLowerCase().contains("mobin"))
        {
            if (!(TrackOrList.get(position).equals("")
                    || TrackOrList.get(position).equals(null)
                    || TrackOrList.get(position).equals("null"))) {
                //      if (!TrackOrList.get(position).equals("")) {

                // Constant.ussd=Constant.orangCallToneList.get(position);
                Constant.ussd=TrackOrList.get(position);


                Constant.networkName="or";

                confirmDailog(position,"or");




                // Toast.makeText(MainActivity.this, "orange code find" +Constant.orangCallToneList.get(position), Toast.LENGTH_SHORT).show();
            }
            else {
                Constant.ussd="";
                Toast.makeText(context, "لا يوجد كول تون فى هذه الشبكه", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar
                        .make(toneViewSnackBar, "لا يوجد كول تون فى هذه الشبكه :( !!", Snackbar.LENGTH_LONG);


                snackbar.show();
            }


        }
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED ) {
                return true;
            } else {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }


    public void downloadong(final String url ,String name,String folderName) {


        if (isStoragePermissionGranted()) {
            File folder = new File(Environment.getExternalStorageDirectory()+ File.separator + folderName);

            Toast.makeText(context, "download", Toast.LENGTH_SHORT).show();
            if(!folder.exists()){
                folder.mkdir();
            }
            DownloadManager.Request req = new DownloadManager.Request(Uri.parse(url));

            req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                    | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(name)
                    .setDescription("Song is Being Downloaded......")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(String.valueOf(folder), name+".mp3");

            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(req);
        }




        if (!folderName.equals("SemsmShehab")) {
            trackName=name;
            context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }



    }
    BroadcastReceiver onComplete=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
            File file = new File(Environment.getExternalStorageDirectory(), SDCardRoot+"/SemsmShehabRingTone/"+
                    trackName+".mp3");

            ringtone(file.toString()+"",pos);
            Toast.makeText(context, "تم تحميل اغنية "+trackNameList.get(pos)+" علشان تبقى رنة موبايلك", Toast.LENGTH_SHORT).show();

        }
    };



    public void settingPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(context.getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
                //startActivityForResult(intent, 200);

            }
        }
    }


    public void confirmDailog(int position, final String network)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //"لكى تكون كول تون لهاتفك "+Constant.playListName.get(position)+"لقد اخترت "
        builder.setMessage("لقد اخترت  "+TrackNameList.get(position)+" علشان تكون كول تون لموبيلك");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Constant.networkName=network;

                context.startActivity(new Intent(context,DialActivity.class));
                // Toast.makeText(TracksActiviy.this, ""+Constant.ussd, Toast.LENGTH_SHORT).show();

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





    public void ringtone(String path,int positon)
    {
        // Create File object for the specified ring tone path
        File f=new File(path);




        ContentValues content = new ContentValues();
        content.put(MediaStore.MediaColumns.DATA,f.getAbsolutePath());
        content.put(MediaStore.MediaColumns.TITLE, TrackNameList.get(positon));
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




        context.getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + f.getAbsolutePath() + "\"",
                null);
        Uri newUri = context.getContentResolver().insert(uri, content);
        System.out.println("uri=="+uri);
        Log.i("TAG","the ringtone uri is :"+newUri);
  /*      if (isRingTonePermissionGranted()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.System.canWrite(context)) {*/
        // Do stuff here

      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(context)) {
                Intent intentt = new     Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intentt.setData(Uri.parse("package:" + context.getPackageName()));
                intentt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentt);
            }
        }*/
        RingtoneManager.setActualDefaultRingtoneUri(
                context.getApplicationContext(), RingtoneManager.TYPE_RINGTONE,
                newUri);
        /*        }
                else {
                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
*/



        //Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();

       /* }else {

            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
        }*/

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



}