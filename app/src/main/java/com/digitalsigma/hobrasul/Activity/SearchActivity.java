package com.digitalsigma.hobrasul.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.digitalsigma.hobrasul.Adapter.SearchRecyAdapter;
import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;
import com.digitalsigma.hobrasul.Service.MusicServiceSemsm;

import net.alhazmy13.catcho.library.Catcho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by AhmedAbouElFadle on 7/26/2016.
 */
public class SearchActivity extends AppCompatActivity {

    //service
    private MusicServiceSemsm musicSrv;
    private Intent playIntent;
    //binding

    private boolean musicBound = false;

    SearchView searchView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<String> searchNameResult2 = new ArrayList<String>();
    ArrayList<String> searchUrlResult2 = new ArrayList<String>();
    ArrayList<String> searchVodResult2 = new ArrayList<String>();
    ArrayList<String> searchOrResult2 = new ArrayList<String>();
    ArrayList<String> searchEtResult2 = new ArrayList<String>();
    ArrayList<String> ringToneList2 = new ArrayList<String>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Catcho.Builder(this)
                .activity(ContactActivity.class)
                .build();


        setContentView(R.layout.activity_search);

        findViewById(R.id.txtSearch).setVisibility(View.VISIBLE);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        LayoutInflater mInflater = LayoutInflater.from(SearchActivity.this);
        View mCustomView = mInflater.inflate(R.layout.search_tab, null);
        // mCustomView.setBackgroundColor(getResources().getColor(R.color.cardview_light_background));
        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        getSupportActionBar().setTitle("Search");


        mRecyclerView = (RecyclerView) findViewById(R.id.search_RV);

        searchView = (SearchView) findViewById(R.id.usersSearch);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                getUsersResult(newText);

                findViewById(R.id.searchPrograss).setVisibility(View.VISIBLE);


                return false;
            }
        });


        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListenerSe(this, mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {


                Constant.pointer = 0;
                Constant.songName = searchNameResult2.get(position);
                Constant.songUrl = searchUrlResult2.get(position);

                Constant.postion = position;
                Constant.playListUrl = searchUrlResult2;
                Constant.playListName = searchNameResult2;
                Constant.ringtoneUrl = ringToneList2.get(position);

                Constant.RingToneUrl = ringToneList2;

                Constant.serachCalltoneEt = searchEtResult2.get(position);
                Constant.serachCalltoneOran = searchOrResult2.get(position);
                Constant.serachCalltoneVod = searchVodResult2.get(position);

                //Toast.makeText(SearchActivity.this, "ussd"+ Constant.serachCalltoneVod, Toast.LENGTH_SHORT).show();


                play();


                // Toast.makeText(SearchActivity.this, ""+Constant.songUrl, Toast.LENGTH_SHORT).show();

                startActivity(new Intent(SearchActivity.this, PlayerActivity.class));


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    public void play() {
        //trackPlayingicon(Constant.postion);
        musicSrv.playSong();
        //   songNametxt.setText(Constant.playListName.get(Constant.postion));

        /*finalTime=MusicServiceSemsm.player.getDuration();
        startTime=musicSrv.getPosn();

        finalTimeTxt.setText(String.format("%d.%d ",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)))
        );

        startTimeTxt.setText(String.format("%d.%d ",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime)))
        );*/


        // -- Register receiver for seekbar--


    /*    //customSimpleNotification(getActivity(),searchnameResult.get(Constant.postion),Constant.album_name);
       registerReceiver(broadcastReceiver, new IntentFilter(
                MusicServiceSemsm.BROADCAST_ACTION));

    *//*    getActivity().registerReceiver(broadcastReceiverSongName, new IntentFilter(
                MusicServiceSemsm.BROADCAST_Text));*//*



        mBroadcastIsRegistered = true;*/

    }


    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicServiceSemsm.MusicBinder binder = (MusicServiceSemsm.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(Constant.playListUrl);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicServiceSemsm.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    public void getUsersResult(final String trackNAme) {


        final ArrayList<String> searchNameResult = new ArrayList<String>();
        final ArrayList<String> searchUrlResult = new ArrayList<String>();
        final ArrayList<String> searchVodResult = new ArrayList<String>();
        final ArrayList<String> searchOrResult = new ArrayList<String>();
        final ArrayList<String> searchEtResult = new ArrayList<String>();
        final ArrayList<String> ringToneList = new ArrayList<String>();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.all_tracks+"?q="+trackNAme, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray dataArray = object.getJSONArray("data");
                    for (int a = 0; a < dataArray.length(); a++) {

                        JSONObject tracks = dataArray.getJSONObject(a);


                        String album_id = tracks.getString("album_id");

                        String trackName = tracks.getString("title");
                        String trackUrl = Constant.BASE_URL + tracks.getString("audio");
                        String ringToneUrl = Constant.BASE_URL + tracks.getString("ringtone");

                        String songCallToneCodeVodafone = tracks.getString("vodafone");
                        String songCallToneCodeOrange = tracks.getString("orange");
                        String songCallToneCodeEtislate = tracks.getString("etisalat");

                        String id = tracks.getString("id");

                               /* searchUrlResult2.add(trackUrl);
                                searchNameResult2.add(trackName);*/

                                searchNameResult.add(trackName);
                                searchUrlResult.add(trackUrl);
                                searchOrResult.add(songCallToneCodeOrange);
                                searchVodResult.add(songCallToneCodeVodafone);
                                searchEtResult.add(songCallToneCodeEtislate);
                                ringToneList.add(ringToneUrl);


                                //    Toast.makeText(SearchActivity.this, "user >>>>"+ringToneUrl, Toast.LENGTH_SHORT).show();

                            }
                            findViewById(R.id.searchPrograss).setVisibility(View.GONE);

                            mLayoutManager = new LinearLayoutManager(SearchActivity.this);
                            mRecyclerView.setLayoutManager(mLayoutManager);

                            searchNameResult2 = searchNameResult;
                            searchUrlResult2 = searchUrlResult;

                            searchVodResult2 = searchVodResult;
                            searchEtResult2 = searchEtResult;
                            searchOrResult2 = searchOrResult;
                            ringToneList2 = ringToneList;


                            if (searchNameResult.size() > 0) {
                                findViewById(R.id.txtSearch).setVisibility(View.GONE);

                            }

                            // specify an adapter (see also next example)
                            mAdapter = new SearchRecyAdapter(searchNameResult, searchUrlResult, SearchActivity.this);
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(SearchActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        // loginUser();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put(Constant.Search_key, trackNAme);
               /* params.put(KEY_PASSWORD,password);
                params.put(KEY_EMAIL, email);*/
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

}


class RecyclerTouchListenerSe implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private SearchActivity.ClickListener clickListener;

    public RecyclerTouchListenerSe(Context context, final RecyclerView recyclerView, final SearchActivity.ClickListener clickListener) {
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

