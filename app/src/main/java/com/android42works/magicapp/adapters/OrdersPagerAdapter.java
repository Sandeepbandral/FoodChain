package com.android42works.magicapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android42works.magicapp.fragments.Orders_PreviousFragment;
import com.android42works.magicapp.fragments.Orders_OngoingFragment;

public class OrdersPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 2;

    public OrdersPagerAdapter(FragmentManager fragmentManager) {
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
                return new Orders_OngoingFragment();
            case 1:
                return new Orders_PreviousFragment();
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