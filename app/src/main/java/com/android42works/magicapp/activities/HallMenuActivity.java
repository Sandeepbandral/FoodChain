package com.android42works.magicapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.adapters.ViewPagerDynamicAdapter;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.dialogs.FutureTimingsDialog;
import com.android42works.magicapp.fragments.HallMenuFragment;
import com.android42works.magicapp.interfaces.HallMenuInterface;
import com.android42works.magicapp.presenters.HallMenuPresenter;
import com.android42works.magicapp.responses.HallMenuResponse;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.android42works.magicapp.utils.AppUtils;

import java.util.ArrayList;

/* Created by JSP@nesar */

public class HallMenuActivity extends BaseActivity implements HallMenuInterface {

    private TextView txt_actionbar_title, txt_hall_status, txt_reset_filter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ArrayList<Fragment> fragmentsList = new ArrayList<android.support.v4.app.Fragment>();
    private ViewPagerDynamicAdapter dynamicPagerAdapter;
    private HallMenuPresenter hallMenuPresenter;
    private ImageView img_actionbar_search2, img_actionbar_filter;
    private RelativeLayout rl_reset, rl_no_dish, rl_tab;
    public String hallId = "";
    private String perPageCount = "", hallName = "";
    private int pageNo = 1, selectedPosition = 0;
    private boolean isPageLoadedFirstTime = true;

    /* Cart View */

    private View view_cart;
    private TextView txt_cart_count, txt_cart_price;
    private RelativeLayout rl_parent_cart;

    public static HallMenuActivity instance;
    public static HallMenuActivity getInstance() {
        return instance;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.act_hall_menu;
    }

    @Override
    protected Context getActivityContext() {
        return HallMenuActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);
        mViewPager = findViewById(R.id.mViewPager);
        mTabLayout = findViewById(R.id.mTabLayout);
        img_actionbar_search2 = findViewById(R.id.img_actionbar_search2);
        img_actionbar_filter = findViewById(R.id.img_actionbar_filter);
        txt_hall_status = findViewById(R.id.txt_hall_status);
        txt_reset_filter = findViewById(R.id.txt_reset_filter);
        rl_reset = findViewById(R.id.rl_reset);

        view_cart = findViewById(R.id.view_cart);
        rl_parent_cart = findViewById(R.id.rl_parent_cart);
        txt_cart_count = findViewById(R.id.txt_cart_count);
        txt_cart_price = findViewById(R.id.txt_cart_price);
        rl_no_dish = findViewById(R.id.rl_no_dish);
        rl_tab = findViewById(R.id.rl_tab);

    }

    @Override
    protected void initData() {

        // TODO initData
        instance = this;
        context = HallMenuActivity.this;
        hallId = getIntent().getStringExtra("hallId");
        hallName = getIntent().getStringExtra("hallName");
        txt_actionbar_title.setText(hallName);

        img_actionbar_search2.setVisibility(View.VISIBLE);
        img_actionbar_filter.setVisibility(View.VISIBLE);

        perPageCount = getString(R.string.perPageCount);

        hallMenuPresenter = new HallMenuPresenter(getAPIInterface());
        hallMenuPresenter.attachView(this);

        loadHallData();

    }

    @Override
    protected void initListener() {

        // TODO initListener

        txt_reset_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSessionManager().setIsHallfilterActive(false);
                getSessionManager().setHallFilterSort("");
                getSessionManager().setHallFilterDietary("");
                getSessionManager().setHallFilterCuisine("");
                loadHallData();

                rl_reset.setVisibility(View.GONE);

            }
        });

        rl_parent_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CartActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        txt_hall_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new FutureTimingsDialog(context, hallId, hallName, "", "", "", true).showDialog();

            }
        });

        findViewById(R.id.img_actionbar_backarrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        img_actionbar_search2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String filterMode = "";

                if(getSessionManager().isHallFilterActive()){
                    filterMode = "hall";
                }

                startActivity(new Intent(context, SearchActivity.class)
                        .putExtra("filterMode", filterMode)
                        .putExtra("hallId", hallId)
                        .putExtra("hallName", hallName)
                );
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });

        img_actionbar_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context, FilterActivity.class)
                        .putExtra("isHomeFilter", false)
                        .putExtra("hallName", hallName)
                        .putExtra("hallId", hallId)
                );
                overridePendingTransition(0, 0);

               /* Intent intent = new Intent("resetFilter");
                context.sendBroadcast(intent);
                try{ Filter_DietaryFragment.getInstance().selectFirstFragment(); }catch (Exception e){}
                setFilterValues();*/

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
                if (!isPageLoadedFirstTime) {
                    selectedPosition = tab.getPosition();
                } else {
                    isPageLoadedFirstTime = !isPageLoadedFirstTime;
                }
                mViewPager.setCurrentItem(tab.getPosition());
            }
        });

    }

    // TODO Activity Methods


    @Override
    protected void onPostResume() {
        super.onPostResume();

        isPageLoadedFirstTime = true;

        checkAndSetCartCount();

        checkHallAvailabilityStatus();

    }

    public void loadHallData(){

        if (isInternetAvailable()) {

            showProgressDialog();

            if(getSessionManager().isHallFilterActive()){

                String dietaryList = getSessionManager().getHallFilterDietary();
                String cuisineList = getSessionManager().getHallFilterCuisine();
                String sortMethod = getSessionManager().getHallFilterSort();

                hallMenuPresenter.getHallMenuData(
                        getSessionManager().getUserId(),
                        hallId,
                        dietaryList,
                        cuisineList,
                        sortMethod,
                        "",
                        ""
                );

            }else {

                hallMenuPresenter.getHallMenuData(
                        getSessionManager().getUserId(),
                        hallId,
                        "",
                        "",
                        "",
                        "",
                        ""
                );

            }

        } else {
            showToast(getString(R.string.api_error_internet));
        }

    }

    public void checkAndSetCartCount() {

        try {

            int cartCount = Integer.parseInt(getSessionManager().getCartCount());
            if (cartCount != 0) {

                view_cart.setVisibility(View.VISIBLE);

                txt_cart_count.setVisibility(View.VISIBLE);
                txt_cart_count.setText(cartCount + "");

                String strCartPrice = getSessionManager().getCartPrice();
                double value = Double.parseDouble(strCartPrice);
                txt_cart_price.setText(getString(R.string.currencySymbol) + " " + String.format("%.2f", value));

            } else {

                view_cart.setVisibility(View.GONE);

            }

        } catch (Exception e) {

        }

    }

    public void checkHallAvailabilityStatus(){

        HomeDishesResponse.Halls[] hallsResponse = getSessionManager().getHallsResponse();

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

    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent("unregisterHallMenuSubFragments");
        context.sendBroadcast(intent);
        try {
            hallMenuPresenter.detachView();
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    // TODO Interface Methods

    @Override
    public void onSuccess(String message, HallMenuResponse hallMenuResponse) {

        hideProgressDialog();
        getSessionManager().setHallMenuResponse(hallMenuResponse);
        getSessionManager().setEmptyHallFilterResponse();

        if(getSessionManager().isHallFilterActive()){
            rl_reset.setVisibility(View.VISIBLE);
        }else {
            rl_reset.setVisibility(View.GONE);
        }

        mTabLayout.removeAllTabs();
        mViewPager.removeAllViews();

        boolean isDishAvailable = false;

        if (null != hallMenuResponse.getData()) {

            if (null != hallMenuResponse.getData().getFilters()) {
                getSessionManager().setHallFilterResponse(hallMenuResponse.getData().getFilters());
            }

            if (null != hallMenuResponse.getData().getCategories()) {

                HallMenuResponse.Categories[] categories = hallMenuResponse.getData().getCategories();

                for (int i = 0; i < categories.length; i++) {

                    if (categories[i].getDishes().length > 0) {
                        mTabLayout.addTab(mTabLayout.newTab().setText("   " + categories[i].getName() + "   "));

                        Bundle b = new Bundle();
                        b.putString("categoryId", categories[i].getId());
                        fragmentsList.add(Fragment.instantiate(context, HallMenuFragment.class.getName(), b));
                    }
                }

                dynamicPagerAdapter = new ViewPagerDynamicAdapter(context, getSupportFragmentManager(), fragmentsList);
                mViewPager.setAdapter(dynamicPagerAdapter);

                mViewPager.setOffscreenPageLimit(categories.length);

                if (mTabLayout.getTabCount() < 2) {
                    mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
                } else if (mTabLayout.getTabCount() > 1 && mTabLayout.getTabCount() < 5) {
                    mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                } else {
                    mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
                    mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                }

                if (categories.length > 0) {
                    mViewPager.setCurrentItem(selectedPosition);
                }

            }

            if(null != hallMenuResponse.getData().getCategories()){

                if(null != hallMenuResponse.getData().getCategories()){

                    HallMenuResponse.Categories[] categories = hallMenuResponse.getData().getCategories();

                    for(int i=0; i<categories.length; i++){

                        if(null != categories[i].getDishes()){

                            HallMenuResponse.Dishes[] dishes = categories[i].getDishes();

                            if(dishes.length>0){
                                isDishAvailable = true;
                            }

                        }

                    }

                }


            }

        }



        if(isDishAvailable){

            rl_tab.setVisibility(View.VISIBLE);
            rl_no_dish.setVisibility(View.GONE);

            Intent intent = new Intent("loadHallMenuSubFragments");
            context.sendBroadcast(intent);

        }else {
            rl_no_dish.setVisibility(View.VISIBLE);
            rl_tab.setVisibility(View.GONE);
        }

    }

    @Override
    public void onError(String message) {
        hideProgressDialog();
        showToast(message);
    }

}
