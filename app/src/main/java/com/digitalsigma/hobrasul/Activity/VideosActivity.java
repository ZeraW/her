package com.digitalsigma.hobrasul.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.digitalsigma.hobrasul.Adapter.MySingleton;
import com.digitalsigma.hobrasul.Adapter.YoutubeRecyAdapter;
import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;
import com.digitalsigma.hobrasul.Service.MusicServiceSemsm;

import net.alhazmy13.catcho.library.Catcho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user2 on 1/2/2017.
 */

public class VideosActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public static ArrayList<String> videoIdList=new ArrayList<String>();
    ArrayList<String> videoImgList=new ArrayList<String>();
    ArrayList<String> videoNameList=new ArrayList<String>();

    Button btn_retry;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Catcho.Builder(this)
                .activity(ContactActivity.class)
                .build();


        setContentView(R.layout.activity_tracks);

        getSupportActionBar().setTitle("الكليبات");


        findViewById(R.id.loadingPrograss).setVisibility(View.VISIBLE);

        init();

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new GridLayoutManager(VideosActivity.this,2);
        //   mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);



        ConnectivityManager cm = (ConnectivityManager)  getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null  && info.isConnected())
        {
           // Toast.makeText(getActivity(), "here", Toast.LENGTH_SHORT).show();
           // progressActivity.showLoading();
//            findViewById(R.id.loadingPrograss).setVisibility(View.VISIBLE);

            allChannelVideo();


        }
        else {

            btn_retry.setVisibility(View.VISIBLE);
            btn_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConnectivityManager cm1 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo info1 = cm1.getActiveNetworkInfo();

                    if (info1 != null  && info1.isConnected())
                    {
                      //  progressActivity.showLoading();
                       // Toast.makeText(getActivity(), "Retry", Toast.LENGTH_SHORT).show();
                        allChannelVideo();

                        btn_retry.setVisibility(View.INVISIBLE);
                       /* btn_retry.setClickable(false);
                        btn_retry.setEnabled(false);*/

                    }else {

                        Snackbar snackbar = Snackbar
                                .make(v, "No internet connection!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
            });
           /* Snackbar snackbar = Snackbar
                    .make(getView(), "No internet connection!", Snackbar.LENGTH_LONG);


            snackbar.show();*/


        }



        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListenerVideo(VideosActivity.this,
                mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {


                if (videoIdList.size()>0) {
                    Constant.video_id = videoIdList.get(position);
                    //  startActivity(new Intent(VideosActivity.this, YoutubePlayerActivity.class));
                    if (MusicServiceSemsm.player != null) {
                        if (MusicServiceSemsm.player.isPlaying()) {
                            MusicServiceSemsm.player.pause();
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com" +
                                    "/watch?v=" + videoIdList.get(position))));
                        } else {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com" +
                                    "/watch?v=" + videoIdList.get(position))));

                        }


                    } else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com" +
                                "/watch?v=" + videoIdList.get(position))));
                    }
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));




    }



    public void init()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.MusicList);
        btn_retry= (Button)findViewById(R.id.retry_video);
      //  progressActivity = (ProgressActivity) getActivity().findViewById(R.id.progress);

    }

    public void allChannelVideo()
    {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constant.ALL_VIDEOS, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d("youtube",response.toString());

                findViewById(R.id.loadingPrograss).setVisibility(View.GONE);


                try {




                    JSONArray items = response.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {

                        JSONObject object = items.getJSONObject(i);


                        JSONObject snippet=object.getJSONObject("snippet");
                        String title=snippet.getString("title");


                        JSONObject resourceId=snippet.getJSONObject("resourceId");
                        String vId=resourceId.getString("videoId");



                        JSONObject thumbnails=snippet.getJSONObject("thumbnails");
                        JSONObject defaultImg=thumbnails.getJSONObject("default");
                        String videoImg=defaultImg.getString("url");

                        dataList(title,videoImg,vId);


                    }







                } catch (JSONException e) {
                    //e.printStackTrace();
                }
                //   Toast.makeText(getActivity(), "Musiccccc"+videoNameList.size(), Toast.LENGTH_SHORT).show();


            }
            }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
              //  VolleyLog.d(TAG, "Error: " + error.getMessage());
               /* Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();*/

            }
        });

        MySingleton.getInstance(this).addToRequestQueue(jsonObjReq);

    }




    public void dataList(String title,String img,String id)
    {
        videoIdList.add(id);
        videoNameList.add(title);
        videoImgList.add(img);

       // Toast.makeText(VideosActivity.this, "size"+videoNameList.size(), Toast.LENGTH_SHORT).show();
 /*   for (int i=0; i<videoImgList.size(); i++)
    {
        if ( (i % 2 !=0) && i!=5 && i!=9 && i!=11) {
            videoImgList.remove(i);
            videoNameList.remove(i);
            videoIdList.remove(i);
        }
    }*/


        mAdapter = new YoutubeRecyAdapter(videoNameList,videoImgList,VideosActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        //Toast.makeText(getActivity(), "No,"+ videoImgList.size(), Toast.LENGTH_SHORT).show();
    }



    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}
class RecyclerTouchListenerVideo implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private VideosActivity.ClickListener clickListener;

    public RecyclerTouchListenerVideo(Context context, final RecyclerView recyclerView, final VideosActivity.ClickListener clickListener) {
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

