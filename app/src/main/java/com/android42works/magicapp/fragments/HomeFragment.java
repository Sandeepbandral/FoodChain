package com.android42works.magicapp.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.activities.HomeActivity;
import com.android42works.magicapp.adapters.SpinnerArrayAdapter;
import com.android42works.magicapp.adapters.ViewPagerDynamicAdapter;
import com.android42works.magicapp.base.BaseFragment;
import com.android42works.magicapp.dialogs.FutureTimingsDialog;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.android42works.magicapp.utils.AppUtils;
import com.android42works.magicapp.utils.Netwatcher;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;

/* Created by JSP@nesar */

public class HomeFragment extends BaseFragment implements Netwatcher.NetwatcherListener{

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Spinner spinner_hall;
    private LinearLayout ll_halls;
    private RelativeLayout rl_spinner;
    public static boolean isSpinnerjustStarted = true;
    private ViewPagerDynamicAdapter dynamicPagerAdapter;
    private ArrayList<Fragment> fragmentsList = new ArrayList<android.support.v4.app.Fragment>();
    private ArrayList<HomeDishesResponse.Halls> hallsList = new ArrayList<>();
    private View view_nointernet;
    private int spinnerPosition = 0;
    private Netwatcher netwatcher;
    private TextView txt_hall_status;
    public static HomeFragment instance;

    public static HomeFragment getInstance() {
        return instance;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.frag_home;
    }

    @Override
    protected Activity getActivityContext() {
        return getActivity();
    }

    @Override
    protected void initView(View v) {

        // TODO initView

        ll_halls = v.findViewById(R.id.ll_halls);
        mViewPager = v.findViewById(R.id.mViewPager);
        mTabLayout = v.findViewById(R.id.mTabLayout);
        spinner_hall = v.findViewById(R.id.spinner_hall);
        view_nointernet = v.findViewById(R.id.view_nointernet);
        txt_hall_status = v.findViewById(R.id.txt_hall_status);
        rl_spinner = v.findViewById(R.id.rl_spinner);

    }

    @Override
    protected void initData(View v) {

        // TODO initData

        instance = this;

        netwatcher = new Netwatcher(this);
        context.registerReceiver(netwatcher, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        if(!isInternetAvailable()) {
            view_nointernet.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void initListener(View v) {

        // TODO initListener

        v.findViewById(R.id.btn_tryagain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetAvailable()) {
                    hideNoInternetView();
                }else {
                    showToast(getString(R.string.api_error_internet));
                }
            }
        });

        rl_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_hall.performClick();
            }
        });

        txt_hall_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String hallInfo = getSessionManager().getSelectedHall();
                    String hallId = hallInfo.split(",")[0];
                    String hallName = hallInfo.split(",")[1];

                    new FutureTimingsDialog(context, hallId, hallName, "", "", "", true).showDialog();

                }catch (Exception e){}

            }
        });

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
        });

        spinner_hall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(isInternetAvailable()) {

                    spinnerPosition = position;

                    getSessionManager().setSelectedHall(hallsList.get(position).getId() + "," + hallsList.get(position).getName());

                    if (isSpinnerjustStarted == false) {
                        HomeActivity.getInstance().loadNewHallData(hallsList.get(position).getId(), true);
                        isSpinnerjustStarted = true;
                    } else {
                        isSpinnerjustStarted = false;
                    }

                }else {
                    showToast(getString(R.string.api_error_internet));
                    spinner_hall.setSelection(spinnerPosition);
                }

            }
        });

    }

    // TODO Fragment Methods

    public void showNoInternetView(){
        view_nointernet.setVisibility(View.VISIBLE);
    }

    public void hideNoInternetView(){
        view_nointernet.setVisibility(View.GONE);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if(isConnected && view_nointernet.getVisibility()==View.VISIBLE){
            view_nointernet.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDestroyView() {
        try{ context.unregisterReceiver(netwatcher); }catch (Exception e){}
        Intent intent = new Intent("terminateFragment");
        context.sendBroadcast(intent);
        super.onDestroyView();
    }

    public void loadTabsAndFragments(){

        hideNoInternetView();

        hallsList = new ArrayList<>();
        mTabLayout.setVisibility(View.GONE);
        ll_halls.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        ArrayList<String> halls = new ArrayList<>();

        String selectedHallId = getSessionManager().getSelectedHall();
        selectedHallId = selectedHallId.split(",")[0];
        int spinnerPosition = 0;

        HomeDishesResponse.Halls[] hallsResponse = getSessionManager().getHallsResponse();

        for(int i=0; i<hallsResponse.length; i++){
            hallsList.add(hallsResponse[i]);
            halls.add(hallsResponse[i].getName());
            if(selectedHallId.equals(hallsList.get(i).getId())){
                spinnerPosition = i;
            }
        }

        if(hallsList.size()!=0){
            ll_halls.setVisibility(View.VISIBLE);
            new SpinnerArrayAdapter(context, spinner_hall, halls, R.layout.spinneritem_hall, false, R.color.colorPrimary, R.color.colorPrimary).setAdapter();
            spinner_hall.setSelection(spinnerPosition);

            getSessionManager().setSelectedHall(hallsList.get(spinnerPosition).getId() + "," + hallsList.get(spinnerPosition).getName());


            if(hallsList.size()==1){
                spinner_hall.setEnabled(false);
                rl_spinner.setVisibility(View.GONE);
            }else {
                spinner_hall.setEnabled(true);
                rl_spinner.setVisibility(View.VISIBLE);
            }

        }

        if(null!=getSessionManager().getCategoriesResponse()){

            HomeDishesResponse.Categories[] categoryArray = getSessionManager().getCategoriesResponse();

            mTabLayout.removeAllTabs();
            fragmentsList = new ArrayList<>();

            for(int i=0; i<categoryArray.length; i++){

                // Tabs Data
                String categoryName = categoryArray[i].getName();

                mTabLayout.addTab(mTabLayout.newTab().setText("   " + categoryName + "   "));

                // Fragments Lists Data
                String categoryId = categoryArray[i].getId();
                Bundle b = new Bundle();
                b.putString("categoryId", categoryId);
                fragmentsList.add(Fragment.instantiate(context,Home_SubFragment.class.getName(),b));

            }

            if(mTabLayout.getTabCount()!=0){
                mTabLayout.setVisibility(View.VISIBLE);
            }

            if(fragmentsList.size()!=0){
                mViewPager.setVisibility(View.VISIBLE);
            }

            if(mTabLayout.getTabCount()<2){
                mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
            }else if(mTabLayout.getTabCount()>1 && mTabLayout.getTabCount()<5){
                mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            }else {
                mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
                mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            }

            dynamicPagerAdapter = new ViewPagerDynamicAdapter(context, ((AppCompatActivity)context).getSupportFragmentManager(), fragmentsList);
            mViewPager.setAdapter(dynamicPagerAdapter);

            mViewPager.setOffscreenPageLimit(mTabLayout.getTabCount());

            checkAndSetHallCloseStatus();

        }

        Intent intent = new Intent("loadHomeSubFragments");
        context.sendBroadcast(intent);

    }

    public void checkAndSetHallCloseStatus(){

        HomeDishesResponse.Halls[] hallsResponse = getSessionManager().getHallsResponse();

        String hallInfo = getSessionManager().getSelectedHall();
        String hallId = hallInfo.split(",")[0];

        if(hallId.trim().length()==0){
            if(hallsResponse.length!=0){
                hallId = hallsResponse[0].getId();
            }
        }

        boolean isHallClosed = AppUtils.isHallCurrentlyClosed(hallId, hallsResponse);
        if(isHallClosed){
            txt_hall_status.setVisibility(View.VISIBLE);
            String getNextHallOpeningDateTime = AppUtils.getNextHallOpeningDateTime(hallId, hallsResponse);
            if(getNextHallOpeningDateTime.trim().length()==0){
                txt_hall_status.setText("Hall is closed.");
            }else {
                txt_hall_status.setText("Hall is closed. Opens at " + getNextHallOpeningDateTime);
            }
        }else {
            txt_hall_status.setVisibility(View.GONE);
        }

    }

    public void changeTabColor(String baseCategoryId){

        HomeDishesResponse.Categories[] categoryArray = getSessionManager().getCategoriesResponse();

        for(int i=0; i<categoryArray.length; i++){

            String categoryId = categoryArray[i].getId();
            if(baseCategoryId.equalsIgnoreCase(categoryId)){

                String categoryName = categoryArray[i].getName();

                mTabLayout.getTabAt(i).setText(Html.fromHtml("<font color = \"#FF0000\"> &nbsp &nbsp "+ categoryName + " &nbsp &nbsp </font>"));

            }

        }

    }

}

