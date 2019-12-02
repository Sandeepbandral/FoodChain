package com.android42works.magicapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerDynamicAdapter extends FragmentPagerAdapter {

    public static int pos = 0;

    private ArrayList<Fragment> fragmentsList;
    private Context context;

    public ViewPagerDynamicAdapter(Context c, FragmentManager fragmentManager, ArrayList<Fragment> fragmentsList) {
        super(fragmentManager);
        this.fragmentsList = fragmentsList;
        this.context = c;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        setPos(position);
        return "";
    }

    public static int getPos() {
        return pos;
    }

    public void add(Class<Fragment> c, Bundle b) {
        fragmentsList.add(Fragment.instantiate(context,c.getName(),b));
    }

    public static void setPos(int pos) {
        ViewPagerDynamicAdapter.pos = pos;
    }

}
