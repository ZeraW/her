package com.digitalsigma.hobrasul.Fragment;

import android.app.DialogFragment;
import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalsigma.hobrasul.Activity.DialActivity;
import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;

import java.io.File;

/**
 * Created by AhmedAbouElFadle on 12/23/2016.
 */
public class RingFragmentDialog extends DialogFragment {
    RadioButton ringtone,calltone;
    Button okBtn;
    TextView timeinfoTxt,placeInfoTxt,partyTitleInfo;
    String path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_party_details, container, false);
        getDialog().setTitle(Constant.partyTitle);

     //
        //   partyTitleInfo= (TextView) rootView.findViewById(R.id.PartyTitle);
        placeInfoTxt= (TextView) rootView.findViewById(R.id.PartyplaceInfo);
        timeinfoTxt= (TextView) rootView.findViewById(R.id.PartytimeInfo);

okBtn= (Button) rootView.findViewById(R.id.ok);

       // partyTitleInfo.setText(Constant.partyTitle);
        placeInfoTxt.setText(Constant.partyPlace);
        timeinfoTxt.setText(Constant.partyTime);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            dismiss();
            }
        });


/*
        ringtone= (RadioButton) rootView.findViewById(R.id.ringToneRadioButton);
*/
        /*calltone= (RadioButton) rootView.findViewById(R.id.callToneradioButton);*/


   /*     Button confirm = (Button) rootView.findViewById(R.id.confirmRingType);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ringtone.isChecked())
                {
                 //   downloadong(Constant.playListUrl.get(Constant.postion),Constant.playListName.get(Constant.postion));


                    File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
                    File file = new File(Environment.getExternalStorageDirectory(), SDCardRoot+"/MusicPro");
                    if (file.exists()){


                         // Toast.makeText(getActivity(), ""+file.toString(), Toast.LENGTH_SHORT).show();

                       ringtone(file.toString(),Constant.playListName.get(Constant.postion));

                     *//*   //  Toast.makeText(DownLoadsActivity.this, "exist", Toast.LENGTH_SHORT).show();
                        File[] files = file.listFiles();

                        Log.d("Files", "Size: "+ files.length);
                        //  Toast.makeText(DownLoadsActivity.this, ""+files[0], Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < files.length; i++)
                        {
                            if (files[i].toString().contains(Constant.playListName.get(Constant.postion)))
                            {
                                path=files[i].toString();
                            }
                              Toast.makeText(getActivity(), "url "+path+Constant.playListName.get(Constant.postion), Toast.LENGTH_SHORT).show();

                        }*//*

                    }else {
                        Toast.makeText(getActivity(), "Not Exist" +Environment.getExternalStorageDirectory()+"/MusicPro" , Toast.LENGTH_SHORT).show();
                    }





                    Toast.makeText(getActivity(), "ring tone", Toast.LENGTH_SHORT).show();
                }
                else
                {
                calltoneChecker(Constant.postion);
                }
            }
        });*/

        return rootView;
    }


public void ringtone(String path,String songeName)
{
    File k = new File(path, songeName);

    ContentValues values = new ContentValues();
    values.put(MediaStore.MediaColumns.DATA, k.getAbsolutePath());
    values.put(MediaStore.MediaColumns.TITLE, songeName);
    values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
    values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
    values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
    values.put(MediaStore.Audio.Media.IS_ALARM, false);
    values.put(MediaStore.Audio.Media.IS_MUSIC, false);

    //Insert it into the database
    Uri uri = MediaStore.Audio.Media.getContentUriForPath(k.getAbsolutePath());
    Uri newUri = getActivity().getContentResolver().insert(uri, values);

    RingtoneManager.setActualDefaultRingtoneUri(
            getActivity(),
            RingtoneManager.TYPE_RINGTONE,
            newUri
    );
}



    public void downloadong(final String url ,String name) {
      /*  thread = new Thread() {
            @Override
            public void run() {*/
        Toast.makeText(getActivity(), "download", Toast.LENGTH_SHORT).show();
        File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
        File directory = new File(SDCardRoot, "/MusicProRingTone/"); //create directory to keep your downloaded file

        Toast.makeText(getActivity(), SDCardRoot + "/MusicPro/", Toast.LENGTH_SHORT).show();
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

        DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(req);
/*
            }
        };*/

    }






    public void calltoneChecker(int position)
    {
        TelephonyManager tm = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String number = tm.getNetworkOperatorName();
        //  Toast.makeText(getActivity(), "tel "+number, Toast.LENGTH_SHORT).show();

        if (number.toLowerCase().contains("vodaf"))
        {
            if (!Constant.vodCallToneList.get(position).equals("")) {
                Constant.ussd=Constant.vodCallToneList.get(position);
                Toast.makeText(getActivity(), "vodafone code find" +Constant.vodCallToneList.get(position), Toast.LENGTH_SHORT).show();


                               /* alertDialog = new AlertDialog.Builder(getActivity());
                                alertDialog.setTitle("Are you sure to make this your calltone");*/
                // view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
                // alertDialog.setView(view);

                Constant.networkName="vod";

                startActivity(new Intent(getActivity(),DialActivity.class));
                Toast.makeText(getActivity(), ""+Constant.ussd, Toast.LENGTH_SHORT).show();

                /*alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Constant.networkName="vod";

                        startActivity(new Intent(getActivity(),DialActivity.class));
                        Toast.makeText(getActivity(), ""+Constant.ussd, Toast.LENGTH_SHORT).show();



                    }
                });
                alertDialog.show();*/
            }
            else {
                Constant.ussd="";
                // Toast.makeText(getActivity(), "noooo", Toast.LENGTH_SHORT).show();

                Snackbar snackbar = Snackbar
                        .make(getView(), "No Calltone here Sorry :( !!", Snackbar.LENGTH_LONG);


                snackbar.show();
            }


          /*  if (Constant.ussd.equals("") ||Constant.ussd.equals(null))
            {


            }
            else {





            }*/



            //  Toast.makeText(getActivity(), "call   true", Toast.LENGTH_SHORT).show();
        }
        else if (number.toLowerCase().contains("etis"))
        {

            if (!Constant.orangCallToneList.get(position).equals("")) {
                Constant.ussd=Constant.orangCallToneList.get(position);



                            /*    alertDialog = new AlertDialog.Builder(getActivity());
                                alertDialog.setTitle("Are you sure to make this your calltone");*/
                // view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
                // alertDialog.setView(view);

                Constant.networkName="et";
                startActivity(new Intent(getActivity(),DialActivity.class));
                Toast.makeText(getActivity(), ""+Constant.ussd, Toast.LENGTH_SHORT).show();

               /* alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Constant.networkName="et";
                        startActivity(new Intent(getActivity(),DialActivity.class));
                        Toast.makeText(getActivity(), ""+Constant.ussd, Toast.LENGTH_SHORT).show();



                    }
                });
                alertDialog.show();
*/
                Toast.makeText(getActivity(), "elislate code find" +Constant.orangCallToneList.get(position), Toast.LENGTH_SHORT).show();
            }
            else {
                Constant.ussd="";
                Snackbar snackbar = Snackbar
                        .make(getView(), "No Calltone here Sorry :( !!", Snackbar.LENGTH_LONG);


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

            if (!Constant.orangCallToneList.get(position).equals("")) {

                Constant.ussd=Constant.orangCallToneList.get(position);


                Constant.networkName="or";
                startActivity(new Intent(getActivity(),DialActivity.class));
                Toast.makeText(getActivity(), ""+Constant.ussd, Toast.LENGTH_SHORT).show();
/*
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
                alertDialog.show();*/


                Toast.makeText(getActivity(), "orange code find" +Constant.orangCallToneList.get(position), Toast.LENGTH_SHORT).show();
            }
            else {
                Constant.ussd="";
                Toast.makeText(getActivity(), "noooo", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar
                        .make(getView(), "No Calltone here Sorry :( !!", Snackbar.LENGTH_LONG);


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
            if (!Constant.orangCallToneList.get(position).equals("")) {

                Constant.ussd=Constant.orangCallToneList.get(position);

                Constant.networkName="or";
                startActivity(new Intent(getActivity(),DialActivity.class));
                Toast.makeText(getActivity(), ""+Constant.ussd, Toast.LENGTH_SHORT).show();

               /* alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Are you sure to make this your calltone");
                // view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
                // alertDialog.setView(view);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Constant.networkName="or";
                        startActivity(new Intent(getActivity(),DialActivity.class));
                        Toast.makeText(getActivity(), ""+Constant.ussd, Toast.LENGTH_SHORT).show();



                    }
                });
                alertDialog.show();*/






                Toast.makeText(getActivity(), "orange code find" +Constant.orangCallToneList.get(position), Toast.LENGTH_SHORT).show();
            }
            else {
                Constant.ussd="";
                Toast.makeText(getActivity(), "noooo", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar
                        .make(getView(), "No Calltone here Sorry :( !!", Snackbar.LENGTH_LONG);


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
}
