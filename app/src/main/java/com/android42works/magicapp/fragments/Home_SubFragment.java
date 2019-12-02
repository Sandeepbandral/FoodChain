package com.android42works.magicapp.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.activities.DishDetailsActivity;
import com.android42works.magicapp.activities.HomeActivity;
import com.android42works.magicapp.activities.SearchActivity;
import com.android42works.magicapp.activities.SkipLoginActivity;
import com.android42works.magicapp.adapters.Home_SubPagerAdapter;
import com.android42works.magicapp.adapters.Home_Sub_DishAdapter;
import com.android42works.magicapp.base.BaseFragment;

import com.android42works.magicapp.interfaces.AddRemoveFavouriteInterface;
import com.android42works.magicapp.interfaces.Home_ChildInterface;
import com.android42works.magicapp.presenters.AddRemoveFavouritePresenter;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.android42works.magicapp.utils.AppUtils;
import com.android42works.magicapp.utils.CustomViewPager;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;

/* Created by JSP@nesar */

public class Home_SubFragment extends BaseFragment implements Home_ChildInterface, AddRemoveFavouriteInterface {

    private CustomViewPager mViewPager;
    private TextView txt_offer, txt_offer_details, txt_no_dish, txt_reset_filter, txt_not_active;
    private LinearLayout ll_indicator_slider, ll_categories;
    private Home_SubPagerAdapter pagerAdapter;
    private RelativeLayout rl_banner, rl_no_dish, rl_reset, rl_not_active;
    private NestedScrollView nestedScrollView;
    private ArrayList<Home_Sub_DishAdapter> adaptersList = new ArrayList<>();
    private Home_ChildInterface home_childInterface;
    private Handler handler;
    private Runnable runnable;
    private int sliderPosition = 0;
    private int dotsCount;
    private String baseCategoryId = "", categoryName = "";
    private ImageView[] dots;
    private ArrayList<HomeDishesResponse.Sub_categories> subCategoryList = new ArrayList<>();
    private ArrayList<HomeDishesResponse.Banners> bannersList = new ArrayList<>();
    private BroadcastReceiver broadcast_reciever;
    private IntentFilter intentFilter;
    private AddRemoveFavouritePresenter addRemoveFavouritePresenter;
    public SwipyRefreshLayout swipyrefreshlayout;
    public static Home_SubFragment instance;
    public static Home_SubFragment getInstance() {
        return instance;
    }
    private boolean isMealClosed = true;

    @Override
    protected int getLayoutView() {
        return R.layout.frag_home_submodules;
    }

    @Override
    protected Activity getActivityContext() {
        return getActivity();
    }

    @Override
    protected void initView(View v) {

        // TODO initView

        mViewPager = v.findViewById(R.id.mViewPager);
        txt_offer = v.findViewById(R.id.txt_offer);
        txt_offer_details = v.findViewById(R.id.txt_offer_details);
        ll_indicator_slider = v.findViewById(R.id.ll_indicator_slider);
        rl_banner = v.findViewById(R.id.rl_banner);
        swipyrefreshlayout = v.findViewById(R.id.swipyrefreshlayout);
        rl_no_dish = v.findViewById(R.id.rl_no_dish);
        txt_no_dish = v.findViewById(R.id.txt_no_dish);
        nestedScrollView = v.findViewById(R.id.nestedScrollView);
        txt_reset_filter = v.findViewById(R.id.txt_reset_filter);
        rl_reset = v.findViewById(R.id.rl_reset);
        txt_not_active = v.findViewById(R.id.txt_not_active);
        rl_not_active = v.findViewById(R.id.rl_not_active);
        ll_categories = v.findViewById(R.id.ll_categories);

    }

    @Override
    protected void initData(View v) {

        // TODO initData

        instance = this;
        home_childInterface = this;
        checkResetOptionAvailability();

        baseCategoryId = getArguments().getString("categoryId");

        addRemoveFavouritePresenter = new AddRemoveFavouritePresenter(getAPIInterface());
        addRemoveFavouritePresenter.attachView(this);

        intentFilter = new IntentFilter();
        intentFilter.addAction("loadHomeSubFragments");
        intentFilter.addAction("reloadOnlyDishesData");
        intentFilter.addAction("checkMealAvailability");
        intentFilter.addAction("terminateFragment");

        if (broadcast_reciever == null) {

            broadcast_reciever = new BroadcastReceiver() {

                @Override
                public void onReceive(Context arg0, Intent intent) {

                    String action = intent.getAction();

                    Log.e("action??", "action??" + action);

                    if (action.equals("loadHomeSubFragments")) {
                        swipyrefreshlayout.setRefreshing(false);
                        checkResetOptionAvailability();
                        loadHallLocalData();
                    }

                    if (action.equals("reloadOnlyDishesData")) {

                        String dishId = intent.getStringExtra("dishId");
                        String isFavourite = intent.getStringExtra("isFavourite");
                        for(int i=0; i<adaptersList.size(); i++){
                            adaptersList.get(i).updateList(dishId, isFavourite);
                        }

                    }

                    if (action.equals("checkMealAvailability")) {
                        checkMealAvailability();
                    }

                    if (action.equals("terminateFragment")) {
                        terminateFragment();
                    }

                }

            };

        }

        context.registerReceiver(broadcast_reciever, intentFilter);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initListener(View v) {

        // TODO initListener

        txt_reset_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSessionManager().setIsHomefilterActive(false);
                getSessionManager().setFilterSort("");
                getSessionManager().setFilterDietary("");
                getSessionManager().setFilterCuisine("");
                HomeActivity.getInstance().loadAllHomeData();

                rl_reset.setVisibility(View.GONE);

            }
        });

        swipyrefreshlayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if(isInternetAvailable()) {
                    String hallInfo = getSessionManager().getSelectedHall();
                    String hallId = hallInfo.split(",")[0];
                    ((HomeActivity) context).loadNewHallData(hallId, false);
                    HomeFragment.isSpinnerjustStarted = true;
                }else {
                    swipyrefreshlayout.setRefreshing(false);
                    HomeFragment.getInstance().showNoInternetView();
                }

            }
        });

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (handler != null) {
                    handler.removeCallbacks(runnable);
                }

                if (bannersList != null && bannersList.size() > 1) {
                    startSliding();
                }

                return false;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
            @Override
            public void onPageSelected(int position) {

                sliderPosition = position;
                int itemPosition = position % bannersList.size();

                txt_offer.setText(bannersList.get(itemPosition).getTitle());
                txt_offer_details.setText(bannersList.get(itemPosition).getDescription());

                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.indicater_unselected));
                }

                try {
                    dots[itemPosition].setImageDrawable(getResources().getDrawable(R.drawable.indicater_selected));
                } catch (Exception e) {
                }

            }
        });

    }


    // TODO Public Methods

    public void checkResetOptionAvailability(){

        if(getSessionManager().isHomeFilterActive()){
            rl_reset.setVisibility(View.VISIBLE);
        }else {
            rl_reset.setVisibility(View.GONE);
        }

    }

    private void terminateFragment() {

        if (handler != null) {
            handler.removeCallbacks(runnable);
        }

        try {
            addRemoveFavouritePresenter.detachView();
        } catch (Exception e) {
        }
        try {
            context.unregisterReceiver(broadcast_reciever);
        } catch (Exception e) {
        }

    }

    private void loadHallLocalData() {

        /* Initialising Data */

        bannersList = new ArrayList<>();
        subCategoryList = new ArrayList<>();

        if (pagerAdapter != null) {

            mViewPager.removeAllViews();
            ll_indicator_slider.removeAllViews();

            txt_offer.setText("");
            txt_offer_details.setText("");

            rl_banner.setVisibility(View.GONE);

            if (handler != null) {
                handler.removeCallbacks(runnable);
            }

        }


        // Loading Banner Images

        rl_banner.setVisibility(View.GONE);

        if (null!= getSessionManager().getBannerResponse()) {

            HomeDishesResponse.Banners[] bannerArray = getSessionManager().getBannerResponse();

            for (int i = 0; i < bannerArray.length; i++) {
                bannersList.add(bannerArray[i]);
            }

            if (bannersList.size() != 0) {
                txt_offer.setText(bannersList.get(0).getTitle());
                txt_offer_details.setText(bannersList.get(0).getDescription());
                rl_banner.setVisibility(View.VISIBLE);
                pagerAdapter = new Home_SubPagerAdapter(context, bannersList);
                mViewPager.setAdapter(pagerAdapter);
                dotsCount = bannersList.size();
                setUiPageViewController();
            }

            if (bannersList != null && bannersList.size() > 1) {
                startSliding();
            }

        }

        // Loading RecyclerView Data

        if (null!=getSessionManager().getCategoriesResponse()) {

            HomeDishesResponse.Categories[] categoryArray = getSessionManager().getCategoriesResponse();

            HomeDishesResponse.Sub_categories[] subCategoryArray = null;

            for (int i = 0; i < categoryArray.length; i++) {
                String categoryId = categoryArray[i].getId();
                if (categoryId.equalsIgnoreCase(baseCategoryId)) {
                    categoryName = categoryArray[i].getName();
                    subCategoryArray = categoryArray[i].getSub_categories();
                }
            }

            if (subCategoryArray != null) {

                for (int i = 0; i < subCategoryArray.length; i++) {

                    if(null!=subCategoryArray[i].getDishes()){

                        HomeDishesResponse.Dishes[] dishes = subCategoryArray[i].getDishes();

                        if(dishes.length!=0){
                            subCategoryList.add(subCategoryArray[i]);
                        }

                    }

                }

            }

        }


        createCategoriesList();

        String hallInfo = getSessionManager().getSelectedHall();
        String hallId = hallInfo.split(",")[0];

        isMealClosed = AppUtils.isMealTimeCurrentlyClosed(hallId, baseCategoryId, getSessionManager().getHallsResponse());

        rl_no_dish.setVisibility(View.GONE);
        rl_not_active.setVisibility(View.GONE);
        ll_categories.setVisibility(View.VISIBLE);

        if(!isDishesAvailable()){
            txt_no_dish.setText("No dish is available under '" + categoryName + "' category");
            rl_no_dish.setVisibility(View.VISIBLE);
            ll_categories.setVisibility(View.GONE);
            HomeFragment.getInstance().changeTabColor(baseCategoryId);
        }

        if(isMealClosed){
            txt_not_active.setText("Hall is not accepting order for \'" + categoryName + "\' at this time.");
            rl_not_active.setVisibility(View.VISIBLE);
            ll_categories.setVisibility(View.GONE);
            HomeFragment.getInstance().changeTabColor(baseCategoryId);
        }

        checkMealAvailability();

    }

    public void checkMealAvailability(){

        String hallInfo = getSessionManager().getSelectedHall();
        String hallId = hallInfo.split(",")[0];

        isMealClosed = AppUtils.isMealTimeCurrentlyClosed(hallId, baseCategoryId, getSessionManager().getHallsResponse());

        if(isMealClosed){
            txt_not_active.setText("Hall is not accepting order for \'" + categoryName + "\' at this time.");
            rl_not_active.setVisibility(View.VISIBLE);

        }

    }

    private boolean isDishesAvailable(){

        boolean status = false;

        for (int i = 0; i < subCategoryList.size(); i++) {

            if(null != subCategoryList.get(i).getDishes()){

                HomeDishesResponse.Dishes[] dishesResponse = subCategoryList.get(i).getDishes();
                if(dishesResponse.length!=0){
                    status = true;
                }

            }

        }

        return status;
    }

    private void startSliding() {

        try {

            handler = new Handler();
            runnable = new Runnable() {
                public void run() {
                    if (sliderPosition == Integer.MAX_VALUE - 10) {
                        sliderPosition = 0;
                    } else {
                        sliderPosition = sliderPosition + 1;
                    }
                    mViewPager.setCurrentItem(sliderPosition, true);
                    handler.postDelayed(this, 3000);
                }
            };

            handler.postDelayed(runnable, 3000);

        } catch (Exception e) {
        }

    }

    private void setUiPageViewController() {

        ll_indicator_slider.removeAllViews();

        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(context);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.indicater_unselected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            ll_indicator_slider.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.indicater_selected));

        if (dotsCount < 2) {
            ll_indicator_slider.setVisibility(View.GONE);
        } else {
            ll_indicator_slider.setVisibility(View.VISIBLE);
        }

    }

    private void createCategoriesList(){

        ll_categories.removeAllViews();

        for (int i = 0; i < subCategoryList.size(); i++) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View layout = inflater.inflate(R.layout.listitem_home_sub_dish_category, null, false);

            TextView txt_name = layout.findViewById(R.id.txt_name);
            txt_name.setText(subCategoryList.get(i).getName());

            ArrayList<HomeDishesResponse.Dishes> dishesList = new ArrayList<>();
            HomeDishesResponse.Dishes[] dishesArray = subCategoryList.get(i).getDishes();
            for (int j = 0; j < dishesArray.length; j++) {
                dishesList.add(dishesArray[j]);
            }

            RecyclerView recycler_dish = layout.findViewById(R.id.recycler_dish);
            recycler_dish.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            Home_Sub_DishAdapter adapter = new Home_Sub_DishAdapter(context, home_childInterface, dishesList, i);
            recycler_dish.setAdapter(adapter);

            adaptersList.add(adapter);

            ll_categories.addView(layout);

        }


    }


    // TODO Interface Methods

    @Override
    public void onDishClick(String dishId) {

        startActivity(new Intent(context, DishDetailsActivity.class).putExtra("dishId", dishId));
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }

    @Override
    public void onSeeAllClick(int parentposition) {

        String subCategoryId = subCategoryList.get(parentposition).getId();

        String hallInfo = getSessionManager().getSelectedHall();
        String hallId = hallInfo.split(",")[0];
        String hallName = hallInfo.split(",")[1];

        String filterMode = "";

        if(getSessionManager().isHomeFilterActive()){
            filterMode = "seeall";
        }

        startActivity(new Intent(context, SearchActivity.class)
                .putExtra("filterMode", filterMode)
                .putExtra("hallId", hallId)
                .putExtra("hallName", hallName)
                .putExtra("baseCategoryId", baseCategoryId)
                .putExtra("subCategoryId", subCategoryId)
        );
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }

    @Override
    public void onDishFavouriteClick(String dishId, String isFavourite) {

        if (getSessionManager().isUserSkippedLoggedIn()) {
            startActivity(new Intent(context, SkipLoginActivity.class));
            context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        } else {
            if (isInternetAvailable()) {

                Intent intent = new Intent("reloadOnlyDishesData");
                intent.putExtra("dishId", dishId);
                intent.putExtra("isFavourite", isFavourite);
                context.sendBroadcast(intent);

                addRemoveFavouritePresenter.addRemoveFavourite(getSessionManager().getUserId(), dishId);
            } else {
                showToast(getString(R.string.api_error_internet));
            }
        }

    }

    @Override
    public void onSuccess_Favourite(String message) {
        // Do Nothing
    }

    @Override
    public void onError(String message) {
        hideProgressDialog();
        showToast(message);
    }

}

