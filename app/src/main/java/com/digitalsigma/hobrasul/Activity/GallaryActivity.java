package com.digitalsigma.hobrasul.Activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.digitalsigma.hobrasul.Adapter.GallaryRecyAdapter;
import com.digitalsigma.hobrasul.Adapter.MySingleton;
import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;

import net.alhazmy13.catcho.library.Catcho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by AhmedAbouElFadle on 12/13/2016.
 */
public class GallaryActivity extends AppCompatActivity {
    Button btn_retry;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> gallaryList = new ArrayList<String>();
    ArrayList<String> gallaryIdList = new ArrayList<String>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Catcho.Builder(this)
                .activity(ContactActivity.class)
                .build();

        setContentView(R.layout.activity_gallary);

        getSupportActionBar().setTitle("الصور");

        btn_retry = (Button) findViewById(R.id.retry_news);

        mRecyclerView = (RecyclerView) findViewById(R.id.gallaryList);
        mRecyclerView.setHasFixedSize(true);


        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            //  Toast.makeText(getActivity(), "here", Toast.LENGTH_SHORT).show();
            //  progressActivity.showLoading();

            findViewById(R.id.loadingPrograss).setVisibility(View.VISIBLE);
            allGallary();


        } else {

            btn_retry.setVisibility(View.VISIBLE);
            btn_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConnectivityManager cm1 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo info1 = cm1.getActiveNetworkInfo();

                    if (info1 != null && info1.isConnected()) {
                        // progressActivity.showLoading();
                        // Toast.makeText(getActivity(), "Retry", Toast.LENGTH_SHORT).show();
                        findViewById(R.id.loadingPrograss).setVisibility(View.VISIBLE);

                        allGallary();

                        btn_retry.setVisibility(View.INVISIBLE);
                       /* btn_retry.setClickable(false);
                        btn_retry.setEnabled(false);*/

                    } else {

                        Snackbar snackbar = Snackbar
                                .make(v, "No internet connection!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
            });

        }
        //findViewById(R.id.loadingPrograss).setVisibility(View.VISIBLE);


    }


    public void allGallary() {

        StringRequest req = new StringRequest(Request.Method.GET, Constant.ALL_GALLARY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray dataArray = object.getJSONArray("data");
                    for (int a = 0; a < dataArray.length(); a++) {
                        JSONObject albums = dataArray.getJSONObject(a);
                        String Img = Constant.BASE_URL + albums.getString("img");
                        String id = albums.getString("id");
                        gallaryList.add(Img);
                        gallaryIdList.add(id);
                    }

                    findViewById(R.id.loadingPrograss).setVisibility(View.GONE);

                    mLayoutManager = new LinearLayoutManager(GallaryActivity.this);
                    // mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(GallaryActivity.this, 1));


                    // specify an adapter (see also next example)
                    mAdapter = new GallaryRecyAdapter(gallaryList, gallaryIdList, GallaryActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                    // mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


                } catch (JSONException e) {
                    e.printStackTrace();
                           /* Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();*/


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              /*  Toast.makeText(getActivity(), "err" +
                        "", Toast.LENGTH_SHORT).show();*/
                //  progressActivity.showEmpty();
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                /*Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();*/
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(req);
    }
}
