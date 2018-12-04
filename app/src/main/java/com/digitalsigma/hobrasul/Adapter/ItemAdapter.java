package com.digitalsigma.hobrasul.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digitalsigma.hobrasul.R;

import java.util.ArrayList;

/**
 * Created by ahmedabouelfadle on 30/11/15.
 */
public class ItemAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<String> items;
   // ImageView storedImage;
    Context context;
    TextView storedName;


    public ItemAdapter(Activity activity, ArrayList<String> items) {
        this.activity = activity;
        this.items = items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (v == null)
            v = inflater.inflate(R.layout.row_list_search, null);
        // if (imageLoader == null)


        //storedImage = (ImageView) v.findViewById(R.id.storedImage);
        storedName = (TextView) v.findViewById(R.id.searchResultname);


      //  Items m = items.get(position);

      // storedImage.setImageBitmap(m.getImage());


        storedName.setText(items.get(position));

        return v;
    }
}