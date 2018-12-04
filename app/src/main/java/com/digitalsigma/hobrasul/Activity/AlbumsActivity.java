package com.digitalsigma.hobrasul.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import com.digitalsigma.hobrasul.Adapter.AlbumRecyAdapter;
import com.digitalsigma.hobrasul.Adapter.MySingleton;
import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;
import com.digitalsigma.hobrasul.Service.MusicServiceSemsm;

import net.alhazmy13.catcho.library.Catcho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user2 on 2/5/2017.
 */

public class AlbumsActivity extends AppCompatActivity {


    ArrayList<String> AlbumNameList = new ArrayList<String>();
    ArrayList<String> ArtistIdList = new ArrayList<String>();
    ArrayList<String> AlbumIdList = new ArrayList<String>();
    ArrayList<String> AlbumImgList = new ArrayList<String>();


    ArrayList<String> gallaryAlbumIdList = new ArrayList<String>();
    ArrayList<String> gallaryAlbumImgList = new ArrayList<String>();


    private RecyclerView mRecyclerView,mRecyclerAlbumView;
    private RecyclerView.Adapter mAdapter,mAlbumAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //service
    Intent serviceIntent;

    //service
    private MusicServiceSemsm musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;



    Button btn_retry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Catcho.Builder(this)
                .activity(ContactActivity.class)
                .build();
        setContentView(R.layout.activity_albums);
        getSupportActionBar().setTitle("الالبومات");
        init();
        setLisner();
        connectionCheck();

    }
    public void init()
    {
        btn_retry= (Button) findViewById(R.id.retry);
        mRecyclerView = (RecyclerView) findViewById(R.id.albumList);
        //mRecyclerAlbumView = (RecyclerView) findViewById(R.id.albumImgList);


    }


    public void setLisner()
    {

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListenerAlbums(AlbumsActivity.this,
                mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Constant.album_id=AlbumIdList.get(position);

                Constant.albumName=AlbumNameList.get(position);


               startActivity(new Intent(AlbumsActivity.this,AlbumTracksActivity.class));


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }


    public void connectionCheck()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null  && info.isConnected())
        {


            findViewById(R.id.loadingPrograss).setVisibility(View.VISIBLE);
            // getTracks(Constant.artist_id);

            getAlbums();



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

                        // getTracks(Constant.artist_id);

                        getAlbums();
                        btn_retry.setVisibility(View.INVISIBLE);

                    }else {

                        Snackbar snackbar = Snackbar
                                .make(v, "No internet connection!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
            });


        }
    }



    public void getAlbums()
    {


        StringRequest req = new StringRequest(Request.Method.GET, Constant.all_albums, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray dataArray = object.getJSONArray("data");
                    for (int a = 0; a < dataArray.length(); a++) {

                        JSONObject tracks = dataArray.getJSONObject(a);

                                String AlbumName = tracks.getString("album_name");
                                String albumImg = Constant.BASE_URL + tracks.getString("img_url");
                                String albumId = tracks.getString("id");



                                //Log.d("response url",songUrl);

                                AlbumIdList.add(albumId);
                                AlbumNameList.add(AlbumName);
                                AlbumImgList.add(albumImg);





                            }
                            findViewById(R.id.loadingPrograss).setVisibility(View.GONE);



                            mLayoutManager = new LinearLayoutManager(AlbumsActivity.this);
                          //  mRecyclerView.setLayoutManager(mLayoutManager);
                              mRecyclerView.setLayoutManager(new GridLayoutManager(AlbumsActivity.this, 2));


                            // specify an adapter (see also next example)
                            mAdapter = new AlbumRecyAdapter(AlbumNameList,AlbumImgList,AlbumsActivity.this);
                            mRecyclerView.setAdapter(mAdapter);



                        } catch (JSONException e) {
                         /*   e.printStackTrace();
                            Toast.makeText(TracksActivity.this,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();*/
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
              /*  Toast.makeText(TracksActivity.this,
                        error.getMessage(), Toast.LENGTH_SHORT).show();*/
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(req);

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}
class RecyclerTouchListenerAlbums implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private AlbumsActivity.ClickListener clickListener;

    public RecyclerTouchListenerAlbums(Context context, final RecyclerView recyclerView,
                                       final AlbumsActivity.ClickListener clickListener) {
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