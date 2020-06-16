package com.devup.opointdoacai.opointdoacai.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.devup.opointdoacai.opointdoacai.JuiceDoubleFragment;
import com.devup.opointdoacai.opointdoacai.JuiceMilkFragment;
import com.devup.opointdoacai.opointdoacai.JuiceWaterFragment;

public class PagerViewAdapter extends FragmentPagerAdapter {
    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                JuiceMilkFragment milkFragment = new JuiceMilkFragment();
                return milkFragment;

            case 1:
                JuiceWaterFragment waterFragment = new JuiceWaterFragment();
                return waterFragment;

            case 2:
                JuiceDoubleFragment doubleFragment = new JuiceDoubleFragment();
                return doubleFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
