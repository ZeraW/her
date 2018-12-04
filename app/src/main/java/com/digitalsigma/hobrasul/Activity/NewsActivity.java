package com.digitalsigma.hobrasul.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.digitalsigma.hobrasul.Adapter.MySingleton;
import com.digitalsigma.hobrasul.Adapter.NewsRecyAdapter;
import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;
import com.digitalsigma.hobrasul.localData.Database_Handler;

import net.alhazmy13.catcho.library.Catcho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by user2 on 1/2/2017.
 */

public class NewsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> newsDocment = new ArrayList<String>();
    ArrayList<String> newsURLImg = new ArrayList<String>();
    ArrayList<String> newsTitle = new ArrayList<String>();

    SharedPreferences newsUpdate;
    SharedPreferences.Editor editor;


    Database_Handler dbHelper;
    SQLiteDatabase db;


    Button btn_retry;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Catcho.Builder(this)
                .activity(ContactActivity.class)
                .build();


        setContentView(R.layout.activity_news);

        getSupportActionBar().setTitle("ادعية");

        findViewById(R.id.txtError).setVisibility(View.VISIBLE);


        init();
        connection();
        mRecyclerView.setHasFixedSize(true);


        //progressActivity.showLoading();


        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListenerNewsAc(NewsActivity.this,
                mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {


                Constant.newsNotificationDocmention = newsDocment.get(position);
                Constant.newsNotificationDocmentionTilte = newsTitle.get(position);
                Constant.newsNotificationDocmentionImgUrl = newsURLImg.get(position);

                startActivity(new Intent(NewsActivity.this, NewsContentActivity.class));


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }


    public void connection() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            //  Toast.makeText(getActivity(), "here", Toast.LENGTH_SHORT).show();
            //  progressActivity.showLoading();

            findViewById(R.id.loadingPrograss).setVisibility(View.VISIBLE);
            allNews();


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
                        allNews();

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
         /*   Snackbar snackbar = Snackbar
                    .make(getView(), "No internet connection!", Snackbar.LENGTH_LONG);


            snackbar.show();*/


        }
    }

    public void allNews() {

       /* final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();*/
        StringRequest req = new StringRequest(Request.Method.GET, Constant.NEWS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray dataArray = object.getJSONArray("data");
                    for (int a = 0; a < dataArray.length(); a++) {
                        JSONObject news = dataArray.getJSONObject(a);

                        String newsDesc = news.getString("ar_body");
                        //Toast.makeText(getActivity(), " "+albumName, Toast.LENGTH_SHORT).show();
                        newsDocment.add(newsDesc);


                        String imgURL = Constant.BASE_URL + news.getString("image");
                        newsURLImg.add(imgURL);

                        String Title = news.getString("ar_title");
                        newsTitle.add(Title);


                    }
//                            Toast.makeText(getActivity(), "url  "+newsDocment.size(), Toast.LENGTH_SHORT).show();
                    findViewById(R.id.loadingPrograss).setVisibility(View.GONE);
                    if (newsDocment.size() > 0) {
                        findViewById(R.id.txtError).setVisibility(View.GONE);

                    }
                    // findViewById(R.id.txtError).setVisibility(View.VISIBLE);

                    Collections.reverse(newsDocment);
                    Collections.reverse(newsURLImg);
                    Collections.reverse(newsTitle);


                    mLayoutManager = new LinearLayoutManager(NewsActivity.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);

                    // specify an adapter (see also next example)
                    mAdapter = new NewsRecyAdapter(newsDocment, newsURLImg, newsTitle, NewsActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                    // mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


                } catch (JSONException e) {
                    // e.printStackTrace();


                    //Toast.makeText(getActivity(), "erre", Toast.LENGTH_SHORT).show();
                    // allNews();

                            /*Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();*/
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                // allNews();
              /*
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();*/
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(req);
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.NewsList);
        btn_retry = (Button) findViewById(R.id.retry_news);
        // progressActivity = (ProgressActivity) findViewById(R.id.progress);

    }


}


class RecyclerTouchListenerNewsAc implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private NewsActivity.ClickListener clickListener;

    public RecyclerTouchListenerNewsAc(Context context, final RecyclerView recyclerView, final NewsActivity.ClickListener clickListener) {
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
