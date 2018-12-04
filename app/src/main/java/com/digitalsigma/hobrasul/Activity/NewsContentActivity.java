package com.digitalsigma.hobrasul.Activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;
import com.squareup.picasso.Picasso;

import net.alhazmy13.catcho.library.Catcho;


/**
 * Created by AhmedAbouElFadle on 12/6/2016.
 */
public class NewsContentActivity extends AppCompatActivity {


    TextView docmentaion;
    ImageView newsImg;
    Typeface t1;
    ScrollView scroll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Catcho.Builder(this)
                .activity(ContactActivity.class)
                .build();
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_newscontent);


        scroll = (ScrollView) findViewById(R.id.scrol);
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });

    /*    Drawable navIcon = .getNavigationIcon();
        navIcon.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);*/

        // getSupportActionBar().setTitle("news");

        t1 = Typeface.createFromAsset(this.getAssets(), "hacen.ttf");

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_profile_layout);
        collapsingToolbar.setTitle(Constant.newsNotificationDocmentionTilte);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbar.setCollapsedTitleTypeface(t1);

        setTitle(Constant.newsNotificationDocmentionTilte);


      /*  Typeface font = Typer.set(NewsContentActivity.this).getFont(Font.ROBOTO_CONDENSED_REGULAR);
        collapsingToolbar.setExpandedTitleTypeface(font);*/


        docmentaion = (TextView) findViewById(R.id.documentaion);
        //  docmentaionTitle= (TextView) findViewById(R.id.documentaionTilte);

        newsImg = (ImageView) findViewById(R.id.news_img);

        // docmentaionTitle.setText(Constant.newsNotificationDocmentionTilte);
        docmentaion.setText(Constant.newsNotificationDocmention);


        docmentaion.setTypeface(t1);


        // Toast.makeText(NewsContentActivity.this, "url"+Constant.newsNotificationDocmentionImgUrl, Toast.LENGTH_SHORT).show();


        Picasso
                .with(this)
                .load(Constant.newsNotificationDocmentionImgUrl)
                .into(newsImg);


    }


}
