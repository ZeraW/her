package com.digitalsigma.hobrasul.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.digitalsigma.hobrasul.R;

import net.alhazmy13.catcho.library.Catcho;

/**
 * Created by user2 on 1/29/2017.
 */

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Catcho.Builder(this)
                .activity(ContactActivity.class)
                .build();
        setContentView(R.layout.about_layout);
    }
}
