/*
package com.gmsproduction.tarekelsheikh.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


*/
/*import com.gmsproduction.tarekelsheikh.Fragment.AlbumFragment;
import com.gmsproduction.tarekelsheikh.Fragment.MusicFragment;
import com.gmsproduction.tarekelsheikh.Fragment.NewsFragment;
import com.gmsproduction.tarekelsheikh.Fragment.PartyFragment;*//*



public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:

                PartyFragment tab4 = new PartyFragment();
                return tab4;

            case 1:

            NewsFragment tab3 = new NewsFragment();
            return tab3;
            case 2:


            MusicFragment tab2 = new MusicFragment();
            return tab2;

            case 3:
                AlbumFragment tab1 = new AlbumFragment();
                return tab1;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}*/
