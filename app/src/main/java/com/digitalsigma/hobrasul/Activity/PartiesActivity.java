package com.digitalsigma.hobrasul.Activity;

import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.digitalsigma.hobrasul.Adapter.MySingleton;
import com.digitalsigma.hobrasul.Adapter.partyRecyAdapter;
import com.digitalsigma.hobrasul.Fragment.RingFragmentDialog;
import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;

import net.alhazmy13.catcho.library.Catcho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by user2 on 1/2/2017.
 */

public class PartiesActivity extends AppCompatActivity {


    Button btn_retry;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    ArrayList<String> partyPlaceList = new ArrayList<String>();
    ArrayList<String> partyTimeList = new ArrayList<String>();

    ArrayList<String> partyList = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Catcho.Builder(this)
                .activity(ContactActivity.class)
                .build();



        setContentView(R.layout.activity_party);

getSupportActionBar().setTitle("مواعيد أخر الحفلات");

        init();
        connection();
        mRecyclerView.setHasFixedSize(true);

        findViewById(R.id.txtError).setVisibility(View.VISIBLE);









        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListenerParties(PartiesActivity.this,
                mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {



                Constant.partyPlace=partyPlaceList.get(position);
                Constant.partyTime=partyTimeList.get(position);
                Constant.partyTitle=partyList.get(position);
                //   Toast.makeText(getActivity(), "dialog", Toast.LENGTH_SHORT).show();

                FragmentManager fm = getFragmentManager();
                RingFragmentDialog dialogFragment = new RingFragmentDialog();
                dialogFragment.show(fm, "ring");





            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));












    }


    public void connection()
    {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null  && info.isConnected())
        {
            //progressActivity.showLoading();
            //  Toast.makeText(PartiesActivity.this, "here", Toast.LENGTH_SHORT).show();
            findViewById(R.id.loadingPrograss).setVisibility(View.VISIBLE);

            allPartiest();


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
                        // progressActivity.showLoading();
                        //Toast.makeText(PartiesActivity.this, "Retry", Toast.LENGTH_SHORT).show();
                        allPartiest();

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
        /*    Snackbar snackbar = Snackbar
                    .make(getView(), "No internet connection!", Snackbar.LENGTH_LONG);


            snackbar.show();*/


        }

    }



    public void allPartiest()
    {
      /*  final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("loading...");
        progressDialog.show();*/

        JsonArrayRequest req = new JsonArrayRequest(Constant.ALL_PARTIES,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Log.d(TAG, response.toString());
                        // progressDialog.dismiss();
                       // progressActivity.showContent();
                        try {
                            // Parsing json array response
                            // loop through each json object

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject albums = (JSONObject) response
                                        .get(i);

                                String place=albums.getString("place");
                                //  Toast.makeText(getActivity(), " "+albumName, Toast.LENGTH_SHORT).show();
                                partyPlaceList.add(place);


                                String time=albums.getString("time");
                                partyTimeList.add(time);

                                String party=albums.getString("party");
                                partyList.add(party);

                                // Toast.makeText(getActivity(), "part"+party, Toast.LENGTH_SHORT).show();





                            }
                            findViewById(R.id.loadingPrograss).setVisibility(View.GONE);
                            if (partyList.size()>0)
                            {
                                findViewById(R.id.txtError).setVisibility(View.GONE);

                            }


                            Collections.reverse(partyList);
                            Collections.reverse(partyPlaceList);
                            Collections.reverse(partyTimeList);


                            mLayoutManager = new LinearLayoutManager(PartiesActivity.this);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            //  mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));


                            // specify an adapter (see also next example)
                            mAdapter = new partyRecyAdapter(partyList,partyPlaceList,partyTimeList,PartiesActivity.this);
                            mRecyclerView.setAdapter(mAdapter);
                            // mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


                        } catch (JSONException e) {
                      /*      e.printStackTrace();
                            Toast.makeText(PartiesActivity.this,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();*/
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
          /*      Toast.makeText(PartiesActivity.this,
                        error.getMessage(), Toast.LENGTH_SHORT).show();*/
            }
        });

        MySingleton.getInstance(PartiesActivity.this).addToRequestQueue(req);
    }


    public void init()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.partyList);
        btn_retry= (Button) findViewById(R.id.retry_party);
      //  progressActivity = (ProgressActivity)findViewById(R.id.progress);
    }
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}
class RecyclerTouchListenerParties implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private PartiesActivity.ClickListener clickListener;

    public RecyclerTouchListenerParties(Context context, final RecyclerView recyclerView, final PartiesActivity.ClickListener clickListener) {
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