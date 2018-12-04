package com.digitalsigma.hobrasul.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.digitalsigma.hobrasul.Adapter.GallarySwipeAdapter;
import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.Other.DepthPageTransformer;
import com.digitalsigma.hobrasul.R;

import net.alhazmy13.catcho.library.Catcho;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by AhmedAbouElFadle on 12/18/2016.
 */
public class ImageViewActivity extends AppCompatActivity {

    ImageView imageView;
    ViewPager viewPager;
    GallarySwipeAdapter customSwip;
    ArrayList<String> downloadedImageName1 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Catcho.Builder(this)
                .activity(ContactActivity.class)
                .build();


        setContentView(R.layout.image_view_activity);
        getSupportActionBar().setTitle("الصور");
        viewPager = (ViewPager) findViewById(R.id.ImageViewPager);
        viewPager.setPageTransformer(true, new DepthPageTransformer());

        customSwip = new GallarySwipeAdapter(ImageViewActivity.this, Constant.gallaryList, Constant.gallaryIdList);
        viewPager.setAdapter(customSwip);
        viewPager.setCurrentItem(Constant.img_Id);
        imageView = (ImageView) findViewById(R.id.imgView);


       /* Picasso
                .with(this)
                .load(Constant.img_url)
                .into(imageView);*/
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();

    }


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
            wallpaper();
            return true;
        }
        if (id == R.id.download_action) {


            int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);


            // Toast.makeText(ImageViewActivity.this, "download", Toast.LENGTH_SHORT).show();
            File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
            File directory = new File(SDCardRoot, "/MusicPro/"); //create directory to keep your downloaded file

            //     Toast.makeText(ImageViewActivity.this, SDCardRoot+"/MusicProImage/", Toast.LENGTH_SHORT).show();
            if (!directory.exists()) {
                directory.mkdir();
            }


            DownloadManager.Request req = new DownloadManager.Request(Uri.parse(Constant.img_url));

            req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                    | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(String.valueOf(m))
                    .setDescription("Image is Being Downloaded......")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(String.valueOf(directory), String.valueOf(m));

            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(req);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void wallpaper() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //"لكى تكون كول تون لهاتفك "+Constant.playListName.get(position)+"لقد اخترت "
        builder.setMessage("هل تريد هذه الصوره خلفيه لموبايلك");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
                File file = new File(Environment.getExternalStorageDirectory(), SDCardRoot + "/SemsmShehabPhotos/");


                if (file.exists()) {
                    //  Toast.makeText(DownLoadsActivity.this, "exist", Toast.LENGTH_SHORT).show();
                    File[] files = file.listFiles();

                    if (files.length > 0) {

                        Log.d("Files", "Size: " + files.length);
                        //  Toast.makeText(DownLoadsActivity.this, ""+files[0], Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < files.length; i++) {


                            // Toast.makeText(context, ""+files[i], Toast.LENGTH_SHORT).show();
                            downloadedImageName1.add(files[i].getName());

                        }


                        if (downloadedImageName1.contains(Constant.gallaryIdList.get(Constant.img_Id))) {


                            File SDCardRoot1 = Environment.getExternalStorageDirectory(); // location where you want to store
                            File file1 = new File(Environment.getExternalStorageDirectory(), SDCardRoot + "/SemsmShehabPhotos/" +
                                    Constant.gallaryIdList.get(Constant.img_Id));

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                            Bitmap bitmap = BitmapFactory.decodeFile(file1.toString(), options);

                            WallpaperManager myWallpaperManager
                                    = WallpaperManager.getInstance(getApplicationContext());
                            try {
                                // Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL("http://www.gms-sms.com:89/TarekElSheikhApp/Tarek%20Elshikh/aaa%20(1).jpg").getContent());
                                myWallpaperManager.setBitmap(bitmap);
                                Toast.makeText(getApplicationContext(), "لقد تم وضع الصوره كخلفيه لموبايلك", Toast.LENGTH_SHORT).show();

                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } else {
                            downloadong(Constant.gallaryList.get(Constant.img_Id), Constant.gallaryIdList.get(Constant.img_Id), "SemsmShehabPhotos");

                        }

                    } else {

                        Random r = new Random();
                        int i1 = r.nextInt(80 - 65) + 65;

                        downloadong(Constant.gallaryList.get(Constant.img_Id), Constant.gallaryIdList.get(Constant.img_Id), "SemsmShehabPhotos");
//                                        Toast.makeText(context, "لقد اخترت ' " + Constant.playListName.get(Constant.postion) + "' علشان تبقى رنة موبايلك", Toast.LENGTH_SHORT).show();


                    }

                } else {
                    Random r = new Random();
                    int i1 = r.nextInt(80 - 65) + 65;

                    downloadong(Constant.gallaryList.get(Constant.img_Id), Constant.gallaryIdList.get(Constant.img_Id), "SemsmShehabPhotos");
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


    }

    public void downloadong(final String url ,String name,String folderName) {


        if (isStoragePermissionGranted()) {
            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + folderName);

            Toast.makeText(getApplicationContext(), "download", Toast.LENGTH_SHORT).show();
            if (!folder.exists()) {
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

            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(req);
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED ) {
                return true;
            } else {
                ActivityCompat.requestPermissions((Activity) this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }


}
