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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.view.PagerAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.digitalsigma.hobrasul.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by user2 on 1/10/2017.
 */

public class GallarySwipeAdapter extends PagerAdapter {
    private ArrayList<String> imageResources =new ArrayList<String>();
    private ArrayList<String> idList =new ArrayList<String>();

    private Context ctx;
    private LayoutInflater layoutInflater;
    public String trackName="";

    ArrayList<String> downloadedImageName1 = new ArrayList<String>();
    public GallarySwipeAdapter(Context c,ArrayList<String> imageList,ArrayList<String> id) {
        ctx=c;
        idList=id;
        imageResources=imageList;
    }

    @Override
    public int getCount() {

        return imageResources.size();
    }



    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater= (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=layoutInflater.inflate(R.layout.image_custom_swip,container,false);
        ImageView imageView=(ImageView) itemView.findViewById(R.id.imgView);
        Picasso
                .with(ctx)
                .load(imageResources.get(position))
                .into(imageView);



        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {






                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

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
                                            = WallpaperManager.getInstance(getApplicationContext());
                                    try {
                                        // Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL("http://www.gms-sms.com:89/TarekElSheikhApp/Tarek%20Elshikh/aaa%20(1).jpg").getContent());
                                        myWallpaperManager.setBitmap(bitmap);
                                        Toast.makeText(ctx, "لقد تم وضع الصوره كخلفيه لموبايلك", Toast.LENGTH_SHORT).show();

                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                }
                                else {
                                    downloadong(imageResources.get(position),idList.get(position),"SemsmShehabPhotos");

                                }

                            }
                            else {

                                Random r = new Random();
                                int i1 = r.nextInt(80 - 65) + 65;

                                downloadong(imageResources.get(position),idList.get(position),"SemsmShehabPhotos");
//                                        Toast.makeText(context, "لقد اخترت ' " + Constant.playListName.get(Constant.postion) + "' علشان تبقى رنة موبايلك", Toast.LENGTH_SHORT).show();


                            }

                        }else {
                            Random r = new Random();
                            int i1 = r.nextInt(80 - 65) + 65;

                            downloadong(imageResources.get(position),idList.get(position),"SemsmShehabPhotos");
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


        // imageView.setImageResource(imageResources.get(position));

        container.addView(itemView);
        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return  (view==object);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ctx.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED ) {
                return true;
            } else {
                ActivityCompat.requestPermissions((Activity) ctx,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
    // Return the size of your dataset (invoked by the layout manager)



    public void downloadong(final String url ,String name,String folderName) {


        if (isStoragePermissionGranted()) {
            File folder = new File(Environment.getExternalStorageDirectory()+ File.separator + folderName);

            Toast.makeText(ctx, "download", Toast.LENGTH_SHORT).show();
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

            DownloadManager manager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(req);
        }




        if (!folderName.equals("SemsmShehab")) {
            trackName=name;
            ctx.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
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
