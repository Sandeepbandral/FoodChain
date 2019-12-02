package com.android42works.magicapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android42works.magicapp.R;
import com.android42works.magicapp.adapters.Filter_PagerAdapter;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.fragments.Filter_Cuisines_Fragment;
import com.android42works.magicapp.fragments.Filter_DietaryFragment;
import com.android42works.magicapp.fragments.Filter_Sort_Fragment;
import com.android42works.magicapp.responses.HallMenuResponse;
import com.android42works.magicapp.responses.HomeDishesResponse;

/* Created by JSP@nesar */

public class FilterActivity extends BaseActivity {

    private ViewPager viewPager_filter;
    private TabLayout tablayout_filter;

    private String hallName = "", hallId = "";
    private boolean isHomeFilter = false;

    public static FilterActivity instance;

    public static FilterActivity getInstance() {
        return instance;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.act_filter;
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void initView() {

        // TODO initView

        tablayout_filter = findViewById(R.id.tablayout_filter);
        viewPager_filter = findViewById(R.id.viewPager_filter);

    }

    @Override
    protected void initData() {

        // TODO initData

        instance = this;

        isHomeFilter = getIntent().getBooleanExtra("isHomeFilter", true);
        hallId = getIntent().getStringExtra("hallId");
        hallName = getIntent().getStringExtra("hallName");

        isFilterDataAvailable();

        setFilterValues();

    }

    @Override
    protected void initListener() {

        // TODO initListener

        viewPager_filter.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout_filter));
        tablayout_filter.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager_filter.setCurrentItem(tab.getPosition());
            }
        });

        findViewById(R.id.txt_filter_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("resetFilter");
                context.sendBroadcast(intent);

                Filter_Sort_Fragment.getInstance().resetFilter();
                Filter_Cuisines_Fragment.getInstance().resetFilter();

                if(isHomeFilter){
                    getSessionManager().setIsHomefilterActive(false);
                    HomeActivity.getInstance().loadAllHomeData();
                }else {
                    getSessionManager().setIsHallfilterActive(false);
                    HallMenuActivity.getInstance().loadHallData();
                }

            }
        });

        findViewById(R.id.img_filter_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); overridePendingTransition(0, 0);
            }
        });

        findViewById(R.id.rl_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); overridePendingTransition(0, 0);
            }
        });

        findViewById(R.id.btn_filter_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Filter_Sort_Fragment.getInstance().saveValues();
                Filter_Cuisines_Fragment.getInstance().saveValues();
                Filter_DietaryFragment.getInstance().saveValues();

                Intent intent2 = new Intent("unregisterFilter");
                context.sendBroadcast(intent2);

                if(isHomeFilter) {

                    getSessionManager().setIsHomefilterActive(true);

                    if(getSessionManager().getFilterSort().trim().length()==0
                            &&
                       getSessionManager().getFilterDietary().trim().length()==0
                            &&
                       getSessionManager().getFilterCuisine().trim().length()==0)
                    {
                        getSessionManager().setIsHomefilterActive(false);

                    }

                    HomeActivity.getInstance().loadAllHomeData();
                    finish();
                    overridePendingTransition(0, 0);

                }else {

                    getSessionManager().setIsHallfilterActive(true);

                    if(getSessionManager().getHallFilterSort().trim().length()==0
                            &&
                        getSessionManager().getHallFilterDietary().trim().length()==0
                            &&
                        getSessionManager().getHallFilterCuisine().trim().length()==0)
                    {
                        getSessionManager().setIsHallfilterActive(false);

                    }

                    HallMenuActivity.getInstance().loadHallData();
                    finish();
                    overridePendingTransition(0, 0);

                }

            }
        });

    }

    // TODO Activity Methods

    public boolean isHomeFilter(){
        return isHomeFilter;
    }

    private void isFilterDataAvailable(){

        if(isHomeFilter){

            HomeDishesResponse.Filters homeFilterResponse = getSessionManager().getFilterResponse();

            if(null!=homeFilterResponse) {

                setFilterValues();

            }else {

                if(!isInternetAvailable()){
                    showToast(getString(R.string.api_error_internet));
                }else {
                    showToast(getString(R.string.api_error_data));
                }

                finish();
                overridePendingTransition(0, 0);

            }

        }else {

            HallMenuResponse.Filters hallFilterResponse = getSessionManager().getHallFilterResponse();

            if(null!=hallFilterResponse) {

                setFilterValues();

            }else {

                if(!isInternetAvailable()){
                    showToast(getString(R.string.api_error_internet));
                }else {
                    showToast(getString(R.string.api_error_data));
                }

                finish();
                overridePendingTransition(0, 0);

            }

        }

    }

    private void setFilterValues() {

        tablayout_filter.removeAllTabs();
        viewPager_filter.removeAllViews();

        Filter_PagerAdapter adapterViewPager = new Filter_PagerAdapter(((AppCompatActivity) context).getSupportFragmentManager());
        viewPager_filter.setAdapter(adapterViewPager);

        tablayout_filter.addTab(tablayout_filter.newTab().setText("   Sort   "));
        tablayout_filter.addTab(tablayout_filter.newTab().setText("   Dietary   "));
        tablayout_filter.addTab(tablayout_filter.newTab().setText("   Cuisines   "));

        if (tablayout_filter.getTabCount() < 2) {
            tablayout_filter.setTabGravity(TabLayout.GRAVITY_CENTER);
        } else if (tablayout_filter.getTabCount() > 1 && tablayout_filter.getTabCount() < 5) {
            tablayout_filter.setTabGravity(TabLayout.GRAVITY_FILL);
        } else {
            tablayout_filter.setTabGravity(TabLayout.GRAVITY_CENTER);
            tablayout_filter.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

        viewPager_filter.setOffscreenPageLimit(3);

    }

    public String getHallId(){
        return hallId;
    }

    @Override
    public void onBackPressed() {
        finish(); overridePendingTransition(0, 0);
    }

}
