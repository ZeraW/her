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
import android.widget.Toast;


import com.digitalsigma.hobrasul.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ahmedabouelfadle on 22/03/16.
 */
public class NewsRecyAdapter extends RecyclerView.Adapter<NewsRecyAdapter.ViewHolder> {
    private ArrayList<String> usernameList,userIdList,newsTilte;
    Context context;
    Typeface t1;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView username;
        public TextView title;
        public ImageView imageView;
        public LinearLayout newsAnim;

        // public TextView txtFooter;

        public ViewHolder(View v) {
            super(v);
            username = (TextView) v.findViewById(R.id.newsDocmentation);
            title = (TextView) v.findViewById(R.id.newsDocmentationTitle);

            imageView= (ImageView) v.findViewById(R.id.imageNews);
            newsAnim= (LinearLayout) v.findViewById(R.id.newsAnim);


            // txtFooter = (TextView) v.findViewById(R.id.secondLine);
        }
    }

    public void add(int position, String item) {
        usernameList.add(position, item);
        //  speechTypeArray.add(position,speech);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = usernameList.indexOf(item);
        usernameList.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewsRecyAdapter(ArrayList<String> myDateArray,ArrayList<String> userIdList1,ArrayList<String> newsTitle1,Context context2) {
        usernameList = myDateArray;
        userIdList = userIdList1;
        newsTilte=newsTitle1;
        context=context2;
        //speechTypeArray=mySpeechTypeArray;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewsRecyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_article, parent, false);
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

        /*anim = AnimationUtils.loadAnimation(context, R.anim.translate);
        anim.reset();*/
        //ImageView iv = (ImageView) findViewById(R.id.splash);

        holder.newsAnim.clearAnimation();
        holder.newsAnim.startAnimation(anim);




        final String name = usernameList.get(position);
        holder.title.setText(newsTilte.get(position));
        holder.username.setText(usernameList.get(position));
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "user id"+userIdList.get(position), Toast.LENGTH_SHORT).show();


            }
        });

        Picasso
                .with(context)
                .load(userIdList.get(position))
//                .fit() // will explain later
                .into(holder.imageView);

        t1=Typeface.createFromAsset(context.getAssets(),"hacen.ttf");
        holder.username.setTypeface(t1);

        holder.title.setTypeface(t1);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return usernameList.size();
    }

}