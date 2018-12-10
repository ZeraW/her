package com.digitalsigma.hobrasul.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;


import com.digitalsigma.hobrasul.Activity.ImageViewActivity;
import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by ahmedabouelfadle on 22/03/16.
 */
public class GallaryRecyAdapter extends RecyclerView.Adapter<GallaryRecyAdapter.ViewHolder> {
    private ArrayList<String> imgUrl,idList;
    Context context;
    public String trackName="";

    ArrayList<String> downloadedImageName1 = new ArrayList<String>();

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imageView;
        public CardView cardView;

        // public TextView txtFooter;

        public ViewHolder(View v) {
            super(v);
            imageView= (ImageView) v.findViewById(R.id.gallary);
            cardView=(CardView)v.findViewById(R.id.cv_gallary);


            // txtFooter = (TextView) v.findViewById(R.id.secondLine);
        }
    }

    public void add(int position, String item) {
        imgUrl.add(position, item);
        //  speechTypeArray.add(position,speech);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = imgUrl.indexOf(item);
        imgUrl.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GallaryRecyAdapter(ArrayList<String> myDateArray,ArrayList<String> idlist,Context context1) {
        imgUrl = myDateArray;
        idList=idlist;

        context=context1;

        //speechTypeArray=mySpeechTypeArray;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GallaryRecyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_gallary, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        Animation anim = AnimationUtils.loadAnimation(context, R.anim.alpha);
        anim.reset();
        /*LinearLayout l=(LinearLayout) context.findViewById(R.id.main_activity_mo);
        l.clearAnimation();
        l.startAnimation(anim);*/

      /*  anim = AnimationUtils.loadAnimation(context, R.anim.translate);
        anim.reset();*/
        //ImageView iv = (ImageView) findViewById(R.id.splash);

        holder.cardView.clearAnimation();
        holder.cardView.startAnimation(anim);



        Picasso
                .with(context)
                .load(imgUrl.get(position))
                .fit()
                .centerCrop()
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constant.img_url=imgUrl.get(position);

                Constant.img_Id = position;
                Constant.gallaryList=imgUrl;
                Constant.gallaryIdList=idList;

                v.getContext().startActivity(new Intent(v.getContext(), ImageViewActivity.class));

            }
        });


        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                //"لكى تكون كول تون لهاتفك "+Constant.playListName.get(position)+"لقد اخترت "
                builder.setMessage("هل تريد هذه الصوره خلفيه لموبايلك");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
                        File file = new File(Environment.getExternalStorageDirectory(), SDCardRoot+"/SemsmShehabPhotos/");



                        if (file.exists()) {
                            //  Toast.makeText(DownLoadsActivity.this, "exist", Toast.LENGTH_SHORT).show();
                            File[] files = file.listFiles();

                            if (files.length > 0)
                            {

                                Log.d("Files", "Size: " + files.length);
                            //  Toast.makeText(DownLoadsActivity.this, ""+files[0], Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < files.length; i++) {


                               // Toast.makeText(context, ""+files[i], Toast.LENGTH_SHORT).show();
                                downloadedImageName1.add(files[i].getName());

                            }


                            if (downloadedImageName1.contains(idList.get(position))) {


                                File SDCardRoot1 = Environment.getExternalStorageDirectory(); // location where you want to store
                                File file1 = new File(Environment.getExternalStorageDirectory(), SDCardRoot + "/SemsmShehabPhotos/" +
                                        idList.get(position));

                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                Bitmap bitmap = BitmapFactory.decodeFile(file1.toString(), options);

                                WallpaperManager myWallpaperManager
                                        = WallpaperManager.getInstance(context);
                                try {
                                    // Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL("http://www.gms-sms.com:89/TarekElSheikhApp/Tarek%20Elshikh/aaa%20(1).jpg").getContent());
                                    myWallpaperManager.setBitmap(bitmap);
                                    Toast.makeText(context, "لقد تم وضع الصوره كخلفيه لموبايلك", Toast.LENGTH_SHORT).show();

                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }
                            else {
                                downloadong(imgUrl.get(position),idList.get(position),"SemsmShehabPhotos");

                            }

                            }
                            else {

                                Random r = new Random();
                                int i1 = r.nextInt(80 - 65) + 65;

                                downloadong(imgUrl.get(position),idList.get(position),"SemsmShehabPhotos");
//                                        Toast.makeText(context, "لقد اخترت ' " + Constant.playListName.get(Constant.postion) + "' علشان تبقى رنة موبايلك", Toast.LENGTH_SHORT).show();

                            }

                        }else {
                            Random r = new Random();
                            int i1 = r.nextInt(80 - 65) + 65;

                            downloadong(imgUrl.get(position),idList.get(position),"SemsmShehabPhotos");
                      }

                        dialog.dismiss();



                        //  System.exit(0);
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

                return false;
            }
        });




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
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return imgUrl.size();
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
                    .setDestinationInExternalPublicDir(String.valueOf(folder), name);

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
            File file = new File(Environment.getExternalStorageDirectory(), SDCardRoot+"/SemsmShehabPhotos/"+
                    trackName);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(file.toString(), options);

            WallpaperManager myWallpaperManager
                    = WallpaperManager.getInstance(context);
            try {
                Bitmap b=BitmapFactory.decodeResource(context.getResources(),R.drawable.about);
                // Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL("http://www.gms-sms.com:89/TarekElSheikhApp/Tarek%20Elshikh/aaa%20(1).jpg").getContent());
                myWallpaperManager.setBitmap(bitmap);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Toast.makeText(context, "لقد تم وضع الصوره كخلفيه لموبايلك", Toast.LENGTH_SHORT).show();


        }
    };

}