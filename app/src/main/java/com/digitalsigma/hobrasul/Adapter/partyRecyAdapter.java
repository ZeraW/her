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


import com.digitalsigma.hobrasul.R;

import java.util.ArrayList;

/**
 * Created by ahmedabouelfadle on 22/03/16.
 */
public class partyRecyAdapter extends RecyclerView.Adapter<partyRecyAdapter.ViewHolder> {
    private ArrayList<String> partyList,partyPlaceList,partyTimeList;
    Context context;
    Typeface t1;
    int size=0;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView place;
        public TextView time;
        public TextView party;
        public ImageView imageView;
        public LinearLayout partyAnim;


        // public TextView txtFooter;

        public ViewHolder(View v) {
            super(v);
           /* place = (TextView) v.findViewById(R.id.newPartPlace);

            time = (TextView) v.findViewById(R.id.newPartyTime);*/
            party = (TextView) v.findViewById(R.id.newParty);
            imageView= (ImageView) v.findViewById(R.id.partyImg);
            partyAnim= (LinearLayout) v.findViewById(R.id.partyAnim);


            // txtFooter = (TextView) v.findViewById(R.id.secondLine);
        }
    }

    public void add(int position, String item) {
        partyList.add(position, item);
        //  speechTypeArray.add(position,speech);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = partyList.indexOf(item);
        partyList.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public partyRecyAdapter(ArrayList<String> party,ArrayList<String> place,ArrayList<String> time,Context context1) {
        partyList = party;
        partyPlaceList = place;
        partyTimeList = time;
        context=context1;

        //speechTypeArray=mySpeechTypeArray;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public partyRecyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_party, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element




        Animation anim = AnimationUtils.loadAnimation(context, R.anim.alpha);
        anim.reset();
        /*LinearLayout l=(LinearLayout) context.findViewById(R.id.main_activity_mo);
        l.clearAnimation();
        l.startAnimation(anim);*/

       /* anim = AnimationUtils.loadAnimation(context, R.anim.translate);
        anim.reset();*/
        //ImageView iv = (ImageView) findViewById(R.id.splash);

        holder.partyAnim.clearAnimation();
        holder.partyAnim.startAnimation(anim);


     /*  Picasso
                .with(context)
                .load(R.drawable.him)
                .into(holder.imageView);*/

  /*      holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


        holder.party.setText(partyList.get(position));

        t1=Typeface.createFromAsset(context.getAssets(),"hacen.ttf");
        holder.party.setTypeface(t1);
    /*    holder.place.setText(partyPlaceList.get(position));
        holder.time.setText(partyTimeList.get(position));*/



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return partyPlaceList.size();
    }

}