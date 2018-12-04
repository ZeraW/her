package com.digitalsigma.hobrasul.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


//import com.koushikdutta.ion.Ion;
import com.digitalsigma.hobrasul.R;

import java.util.ArrayList;

/**
 * Created by ahmedabouelfadle on 22/03/16.
 */
public class DownLoadRecyAdapter extends RecyclerView.Adapter<DownLoadRecyAdapter.ViewHolder> {
    private ArrayList<String> songNamePathList, songUrlList;
    Context context;
    Typeface t1;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView songNametxt;
        public LinearLayout animRow;

        public ImageView gif;

        // public TextView txtFooter;

        public ViewHolder(View v) {
            super(v);
            songNametxt = (TextView) v.findViewById(R.id.downloadTxt);
            animRow = (LinearLayout) v.findViewById(R.id.downloadTracksListRowAnim);

            //gif= (ImageView) v.findViewById(R.id.gif);

            // songImg= (ImageView) v.findViewById(R.id.videoImg);

            // txtFooter = (TextView) v.findViewById(R.id.secondLine);
        }
    }

    public void add(int position, String item) {
        songNamePathList.add(position, item);
        //  speechTypeArray.add(position,speech);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = songNamePathList.indexOf(item);
        songNamePathList.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DownLoadRecyAdapter(ArrayList<String> name, ArrayList<String> songPath, Context context1) {
        songNamePathList = name;
        songUrlList = songPath;
        context = context1;

        //speechTypeArray=mySpeechTypeArray;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DownLoadRecyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_download, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //Ion.with(holder.gif).load("http://www.animatedimages.org/data/media/1377/animated-music-note-image-0028.gif");
       /* Picasso
                .with(context)
                .load(videoImgPathList.get(position))
                .into(holder.videoImg);*/

  /*      holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        Animation anim = AnimationUtils.loadAnimation(context, R.anim.alpha);
        anim.reset();
        /*LinearLayout l=(LinearLayout) context.findViewById(R.id.main_activity_mo);
        l.clearAnimation();
        l.startAnimation(anim);*/

      /*  anim = AnimationUtils.loadAnimation(context, R.anim.translate);
        anim.reset();*/
        //ImageView iv = (ImageView) findViewById(R.id.splash);

        holder.animRow.clearAnimation();
        holder.animRow.startAnimation(anim);

        holder.songNametxt.setText(songNamePathList.get(position));

        t1 = Typeface.createFromAsset(context.getAssets(), "hacen.ttf");
        holder.songNametxt.setTypeface(t1);

        t1 = Typeface.createFromAsset(context.getAssets(), "hacen.ttf");
        holder.songNametxt.setTypeface(t1);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return songNamePathList.size();
    }

}