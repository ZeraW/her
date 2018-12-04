package com.digitalsigma.hobrasul.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.digitalsigma.hobrasul.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ahmedabouelfadle on 22/03/16.
 */
public class AlbumRecyAdapter extends RecyclerView.Adapter<AlbumRecyAdapter.ViewHolder> {
    private ArrayList<String> albumNameList, ImageUrlList;
    Context context;

    Typeface t1;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView username;
        public ImageView imageView;
        public ImageView imageViewGif;

        // public TextView txtFooter;

        public ViewHolder(View v) {
            super(v);
            username = (TextView) v.findViewById(R.id.AlbumNameList);
            imageView = (ImageView) v.findViewById(R.id.imageView2);
            //  imageView= (ImageView) v.findViewById(R.id.gif);


            // txtFooter = (TextView) v.findViewById(R.id.secondLine);
        }
    }

    public void add(int position, String item) {
        albumNameList.add(position, item);
        //  speechTypeArray.add(position,speech);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = albumNameList.indexOf(item);
        albumNameList.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AlbumRecyAdapter(ArrayList<String> myDateArray, ArrayList<String> img, Context context1) {
        albumNameList = myDateArray;
        ImageUrlList = img;
        context = context1;

        //speechTypeArray=mySpeechTypeArray;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AlbumRecyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_album, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        Picasso
                .with(context)
                .load(ImageUrlList.get(position))
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final String name = albumNameList.get(position);
        holder.username.setText(albumNameList.get(position));
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(v.getContext(), "user id"+userIdList.get(position), Toast.LENGTH_SHORT).show();


            }
        });


        t1 = Typeface.createFromAsset(context.getAssets(), "hacen.ttf");
        holder.username.setTypeface(t1);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return albumNameList.size();
    }

}