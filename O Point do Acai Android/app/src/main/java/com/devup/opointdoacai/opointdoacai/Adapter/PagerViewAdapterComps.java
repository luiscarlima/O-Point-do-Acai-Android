package com.devup.opointdoacai.opointdoacai.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.devup.opointdoacai.opointdoacai.FragmentCoberturas;
import com.devup.opointdoacai.opointdoacai.FragmentComps;
import com.devup.opointdoacai.opointdoacai.FragmentFrutas;
import com.devup.opointdoacai.opointdoacai.FragmentLimitedEdition;
import com.devup.opointdoacai.opointdoacai.FragmentMousses;

public class PagerViewAdapterComps extends FragmentPagerAdapter {
    public PagerViewAdapterComps(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                FragmentMousses moussesFragment = new FragmentMousses();
                return moussesFragment;

            case 1:
                FragmentFrutas frutasFragment = new FragmentFrutas();
                return frutasFragment;

            case 2:
                FragmentComps compsFragment = new FragmentComps();
                return compsFragment;

            case 3:
                FragmentCoberturas coberturasFragment = new FragmentCoberturas();
                return coberturasFragment;

            case 4:
                FragmentLimitedEdition limitedEditionFragment = new FragmentLimitedEdition();
                return limitedEditionFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
