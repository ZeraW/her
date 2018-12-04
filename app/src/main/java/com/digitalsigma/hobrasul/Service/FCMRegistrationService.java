package com.digitalsigma.hobrasul.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.digitalsigma.hobrasul.Other.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahmed on 8/8/2017.
 */

public class FCMRegistrationService extends IntentService {
    SharedPreferences preferences;

    public FCMRegistrationService() {
        super("FCM");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // get Default Shard Preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // get token from Firebase
        String token = FirebaseInstanceId.getInstance().getToken();

        // check if intent is null or not if it isn't null we will ger refreshed value and
        // if its true we will override token_sent value to false and apply
        if (intent.getExtras() != null) {
            boolean refreshed = intent.getExtras().getBoolean("refreshed");
            if (refreshed) preferences.edit().putBoolean("token_sent", false).apply();
        }

        // if token_sent value is false then use method sendTokenToServer to send token to server
        if (!preferences.getBoolean("token_sent", false))
            sendTokenToServer(token);

    }


    // method use volley to send token to server and stop the service when done or error happened
    private void sendTokenToServer(final String token) {
        Log.e("Registration Service","here");
        String ADD_TOKEN_URL = Constant.TOKEN_REGISTER;
        StringRequest request = new StringRequest(Request.Method.POST, ADD_TOKEN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String responseCode = response;
                if (responseCode.equals("success")) {
                    preferences.edit().putBoolean("token_sent", true).apply();
                    Log.e("Registration Service", "Response : Send Token Success");
                    stopSelf();

                } else {
                    preferences.edit().putBoolean("token_sent", false).apply();
                    Log.e("Registration Service", "Response : Send Token Failed");
                    stopSelf();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                preferences.edit().putBoolean("token_sent", false).apply();
                Log.e("Registration Service", "Error :Send Token Failed");
                stopSelf();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;

            }
        };

        Volley.newRequestQueue(this).add(request);

    }

}