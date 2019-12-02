package com.android42works.magicapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.android42works.magicapp.R;
import com.android42works.magicapp.activities.FilterActivity;
import com.android42works.magicapp.adapters.ViewPagerDynamicAdapter;
import com.android42works.magicapp.base.BaseFragment;
import com.android42works.magicapp.models.FilterOptionsModel;
import com.android42works.magicapp.responses.HallMenuResponse;
import com.android42works.magicapp.responses.HomeDishesResponse;

import java.util.ArrayList;

/* Created by JSP@nesar */

public class Filter_DietaryFragment extends BaseFragment {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ViewPagerDynamicAdapter dynamicPagerAdapter;
    private ArrayList<String> dietaryList = new ArrayList<>();


    public static Filter_DietaryFragment instance;

    private ArrayList<Fragment> fragmentsList = new ArrayList<android.support.v4.app.Fragment>();

    public static Filter_DietaryFragment getInstance() {
        return instance;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.frag_filter_dietary;
    }

    @Override
    protected Activity getActivityContext() {
        return getActivity();
    }

    @Override
    protected void initView(View v) {

        // TODO initView

        mViewPager = v.findViewById(R.id.mViewPager);
        mTabLayout = v.findViewById(R.id.mTabLayout);

    }

    @Override
    protected void initData(View v) {

        // TODO initData

        instance = this;
        loadDietaryTabsData();

    }

    @Override
    protected void initListener(View v) {

        // TODO initListener

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
        });

    }

    // TODO Activity Methods

    private void loadDietaryTabsData(){

        mTabLayout.removeAllTabs();
        mViewPager.removeAllViews();

        fragmentsList = new ArrayList<android.support.v4.app.Fragment>();

        if(FilterActivity.getInstance().isHomeFilter()) {

            HomeDishesResponse.Filters filterResponse = getSessionManager().getFilterResponse();

            if (null != filterResponse) {

                if (null != filterResponse.getDietary()) {

                    HomeDishesResponse.Dietary[] dietaries = filterResponse.getDietary();

                    for (int i = 0; i < dietaries.length; i++) {

                        if (null != dietaries[i].getOptions()) {

                            if (dietaries[i].getOptions().length > 0) {
                                mTabLayout.addTab(mTabLayout.newTab().setText("   " + dietaries[i].getName() + "   "));

                                Bundle b = new Bundle();
                                b.putString("categoryId", dietaries[i].getId());
                                fragmentsList.add(Fragment.instantiate(context, Filter_Dietary_SubFragment.class.getName(), b));
                            }

                        }

                    }

                }

            }

        }else {

            HallMenuResponse.Filters filterResponse = getSessionManager().getHallFilterResponse();

            if (null != filterResponse) {

                if (null != filterResponse.getDietary()) {

                    HallMenuResponse.Dietary[] dietaries = filterResponse.getDietary();

                    for (int i = 0; i < dietaries.length; i++) {

                        if (null != dietaries[i].getOptions()) {

                            if (dietaries[i].getOptions().length > 0) {
                                mTabLayout.addTab(mTabLayout.newTab().setText("   " + dietaries[i].getName() + "   "));

                                Bundle b = new Bundle();
                                b.putString("categoryId", dietaries[i].getId());
                                fragmentsList.add(Fragment.instantiate(context, Filter_Dietary_SubFragment.class.getName(), b));
                            }

                        }

                    }

                }

            }

        }

        dynamicPagerAdapter = new ViewPagerDynamicAdapter(context, getChildFragmentManager(), fragmentsList);
        mViewPager.setAdapter(dynamicPagerAdapter);

        mViewPager.setOffscreenPageLimit(fragmentsList.size());

        if (mTabLayout.getTabCount() < 2) {
            mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        } else if (mTabLayout.getTabCount() > 1 && mTabLayout.getTabCount() < 5) {
            mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        } else {
            mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }


    }

    public void saveValues(){

        String strDietary = "";

        for(int j=0; j<dietaryList.size(); j++){

            if(j==0){
                strDietary = dietaryList.get(j);
            }else {
                strDietary = strDietary + "," + dietaryList.get(j);
            }

        }

        if(FilterActivity.getInstance().isHomeFilter()) {
            getSessionManager().setFilterDietary(strDietary);
        }else {
            getSessionManager().setHallFilterDietary(strDietary);
        }

    }

    public void addDietaryId(String Id){
        dietaryList.add(Id);
    }

    public void removeDietaryId(String Id){

        for(int j=0; j<dietaryList.size(); j++){

            String tempId = dietaryList.get(j);
            if(tempId.equalsIgnoreCase(Id)){
                dietaryList.remove(j);
                return;
            }

        }

    }

}

