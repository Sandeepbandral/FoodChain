package com.android42works.magicapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android42works.magicapp.fragments.Filter_Cuisines_Fragment;
import com.android42works.magicapp.fragments.Filter_DietaryFragment;
import com.android42works.magicapp.fragments.Filter_Sort_Fragment;

public class Filter_PagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 3;

    public Filter_PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Filter_Sort_Fragment();
            case 1:
                return new Filter_DietaryFragment();
            case 2:
                return new Filter_Cuisines_Fragment();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}