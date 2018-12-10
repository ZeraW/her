package com.digitalsigma.hobrasul.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;

import net.alhazmy13.catcho.library.Catcho;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AhmedAbouElFadle on 12/21/2016.
 */
public class SplashScreenActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 10000;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    String id;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;




    Button skipBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Catcho.Builder(this)
                .activity(ContactActivity.class)
                .build();


        setContentView(R.layout.activity_splash_screen);
        checkAndRequestPermissions();


        skipBtn= (Button) findViewById(R.id.skipBtn);


        checkAndRequestPermissions();

        Thread thread=new Thread(){

            @Override
            public void run() {

                try {
                    sleep(2000);
                    startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();






        // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("name,link,email,gender,birthday"));
        //  LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("public_profile"));








        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                finish();
            }
        });


    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    private  boolean checkAndRequestPermissions() {
        int permissionWriteContact = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CONTACTS);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionWriteContact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()])
                    ,REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }


    public void userRegister(final String name, final String email, final String gender, final String profileId
            , final String phone)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.registeration,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Toast.makeText(SplashScreenActivity.this, "res"+response, Toast.LENGTH_SHORT).show();

                        Log.d("fadle",response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SplashScreenActivity.this,error.toString(),Toast.LENGTH_LONG).show();
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


}
