package com.android42works.magicapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.cart.Items;
import com.android42works.magicapp.dialogs.ClearNotificationsDialog;
import com.android42works.magicapp.fragments.FullMenuFragment;
import com.android42works.magicapp.fragments.HomeFragment;
import com.android42works.magicapp.fragments.Home_SubFragment;
import com.android42works.magicapp.fragments.NotificationsFragment;
import com.android42works.magicapp.fragments.ProfileFragment;
import com.android42works.magicapp.interfaces.HomeInterface;
import com.android42works.magicapp.presenters.HomePresenter;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.android42works.magicapp.responses.SearchHomeDishesResponse;
import com.android42works.magicapp.responses.UserDetailsResponse;
import com.android42works.magicapp.utils.AppUtils;
import com.android42works.magicapp.utils.KeyboardUtils;
import com.android42works.magicapp.utils.Netwatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/* Created by JSP@nesar */

public class HomeActivity extends BaseActivity implements HomeInterface, Netwatcher.NetwatcherListener{

    /* Action Bar */

    private TextView txt_actionbar_title;
    private ImageView img_actionbar_logo, img_actionbar_filter, img_actionbar_trash;
    private RelativeLayout rl_actionbar_search;

    /* Main View */

    private LinearLayout ll_bottom_navigation;
    private ImageView img_bottom_home, img_bottom_notifications, img_bottom_profile, img_bottom_fullmenu;
    private TextView txt_bottom_home, txt_bottom_notifications, txt_bottom_profile, txt_bottom_fullmenu, txtNotiCount;
    private FragmentManager fragmentManager;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    private HomePresenter homePresenter;
    private boolean isFragmentPopedOut = false ;
    private String currentFragmentTag = "home";
    private final String TAG_HOME_FRAGMENT = "home";
    private final String TAG_FULLMENU_FRAGMENT = "fullmenu";
    private final String TAG_NOTIFICATIONS_FRAGMENT = "notifications";
    private final String TAG_PROFILE_FRAGMENT = "profile";
    private String anyInstance = "", notificationCount = "";
    private Netwatcher netwatcher;

    /* Cart View */

    private View view_cart;
    private TextView txt_cart_count, txt_cart_price;
    private RelativeLayout rl_parent_cart;

    public static HomeActivity instance;
    public static HomeActivity getInstance() {
        return instance;
    }

    /* Main Code */

    @Override
    protected int getLayoutView() {
        return R.layout.act_home;
    }

    @Override
    protected Context getActivityContext() {
        return HomeActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

        img_actionbar_logo = findViewById(R.id.img_actionbar_logo);
        ll_bottom_navigation = findViewById(R.id.ll_bottom_navigation);
        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);
        rl_actionbar_search = findViewById(R.id.rl_actionbar_search);
        img_actionbar_filter = findViewById(R.id.img_actionbar_filter);
        img_bottom_home = findViewById(R.id.img_bottom_home);
        img_bottom_notifications = findViewById(R.id.img_bottom_notifications);
        img_bottom_profile = findViewById(R.id.img_bottom_profile);
        txt_bottom_home = findViewById(R.id.txt_bottom_home);
        txt_bottom_notifications = findViewById(R.id.txt_bottom_notifications);
        txt_bottom_profile = findViewById(R.id.txt_bottom_profile);
        img_bottom_fullmenu = findViewById(R.id.img_bottom_fullmenu);
        txt_bottom_fullmenu = findViewById(R.id.txt_bottom_fullmenu);
        txtNotiCount = findViewById(R.id.txtNotiCount);
        img_actionbar_trash = findViewById(R.id.img_actionbar_trash);

        view_cart = findViewById(R.id.view_cart);
        rl_parent_cart = findViewById(R.id.rl_parent_cart);
        txt_cart_count = findViewById(R.id.txt_cart_count);
        txt_cart_price = findViewById(R.id.txt_cart_price);

    }

    @Override
    public void initData() {

        // TODO initData

        instance = this;

        netwatcher = new Netwatcher(this);
        registerReceiver(netwatcher, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        fragmentManager = getSupportFragmentManager();

        loadFragment(new HomeFragment(), TAG_HOME_FRAGMENT);

        boolean forceOpenNotificationsTab = getIntent().getBooleanExtra("forceOpenNotificationsTab", false);
        if(forceOpenNotificationsTab){
            loadFragment(new NotificationsFragment(), TAG_NOTIFICATIONS_FRAGMENT);
        }

        homePresenter = new HomePresenter(getAPIInterface());
        homePresenter.attachView(this);

        getSessionManager().setSelectedHall("");

    }

    @Override
    protected void initListener() {

        // TODO initListener

        rl_parent_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CartActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        findViewById(R.id.ll_bottom_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new HomeFragment(), TAG_HOME_FRAGMENT);
            }
        });

        findViewById(R.id.ll_bottom_fullmenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new FullMenuFragment(), TAG_FULLMENU_FRAGMENT);
            }
        });

        findViewById(R.id.ll_bottom_notifications).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNotiCount.setVisibility(View.GONE);
                loadFragment(new NotificationsFragment(), TAG_NOTIFICATIONS_FRAGMENT);
            }
        });

        findViewById(R.id.ll_bottom_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ProfileFragment(), TAG_PROFILE_FRAGMENT);
            }
        });

        img_actionbar_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ClearNotificationsDialog(context).show();
            }
        });

        findViewById(R.id.rl_actionbar_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hallId = "", hallName = "", filterMode = "";
                boolean showHallName = true;

                if(getSessionManager().isHomeFilterActive()){
                    String hallInfo = getSessionManager().getSelectedHall();
                    hallId = hallInfo.split(",")[0];
                    hallName = hallInfo.split(",")[1];
                    filterMode = "home";
                    showHallName = false;
                }

                startActivity(new Intent(context, SearchActivity.class)
                        .putExtra("showHallName", showHallName)
                        .putExtra("filterMode", filterMode)
                        .putExtra("hallName", hallName)
                        .putExtra("hallId", hallId)
                );
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        img_actionbar_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String hallInfo = getSessionManager().getSelectedHall();
                    String hallId = hallInfo.split(",")[0];
                    String hallName = hallInfo.split(",")[1];

                    startActivity(new Intent(context, FilterActivity.class)
                            .putExtra("isHomeFilter", true)
                            .putExtra("hallName", hallName)
                            .putExtra("hallId", hallId)
                    );
                    overridePendingTransition(0, 0);

                }catch (Exception e){
                    showToast(getString(R.string.api_error_data));
                }

            }
        });

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                int count = fragmentManager.getBackStackEntryCount();

                if (count > 0 && isFragmentPopedOut == true) {

                    isFragmentPopedOut = false;

                    try {

                        FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(count - 1);
                        currentFragmentTag = backEntry.getName();

                        switch (currentFragmentTag) {

                            case TAG_HOME_FRAGMENT:
                                loadFragment(new HomeFragment(), TAG_HOME_FRAGMENT);
                                break;

                            case TAG_FULLMENU_FRAGMENT:
                                loadFragment(new FullMenuFragment(), TAG_FULLMENU_FRAGMENT);
                                break;

                            case TAG_NOTIFICATIONS_FRAGMENT:
                                loadFragment(new NotificationsFragment(), TAG_NOTIFICATIONS_FRAGMENT);
                                break;

                            case TAG_PROFILE_FRAGMENT:
                                loadFragment(new ProfileFragment(), TAG_PROFILE_FRAGMENT);
                                break;

                        }

                    } catch (Exception e) {
                    }

                }

            }
        });

        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {

                if (isVisible) {
                    ll_bottom_navigation.setVisibility(View.GONE);
                } else {
                    ll_bottom_navigation.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    // TODO Activity Methods

    /* Instance methods */

    public void loadAllHomeData(){

        if (isInternetAvailable()) {

            if (!getSessionManager().isUserSkippedLoggedIn()) {
                showProgressDialog();
                homePresenter.getUserDetails(getSessionManager().getUserId());
            }

            String hallInfo = getSessionManager().getSelectedHall();
            String hallId = hallInfo.split(",")[0];

            if (!getSessionManager().isUserSkippedLoggedIn()) {
                fetchNotiCount();
            }

            loadNewHallData(hallId, true);

        }

    }

    public void loadHomeFragment() {
        loadFragment(new HomeFragment(), TAG_HOME_FRAGMENT);
    }

    public void loadHomeData() {

        Log.e("loadHomeData", "loadHomeData");

        anyInstance = "home";

        checkLocalCartList();
    }

    public void checkAndSetCartCount() {

        if(isInternetAvailable()) {

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

        }else {

            view_cart.setVisibility(View.GONE);

        }

    }

    public void setVisiBilityOfClearNotifyDialog(boolean status){

        if(status) {
            img_actionbar_trash.setVisibility(View.VISIBLE);
        }else {
            img_actionbar_trash.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        checkAndSetCartCount();

        if (anyInstance.equalsIgnoreCase("home")) {
            HomeFragment.isSpinnerjustStarted = true;
            loadNewHallData("", true);
            anyInstance = "";
        }

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if(isConnected){
            loadAllHomeData();
        }

    }

    @Override
    protected void onDestroy() {
        try { homePresenter.detachView(); } catch (Exception e) {}
        try{ context.unregisterReceiver(netwatcher); }catch (Exception e){}
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        if (fragmentManager.getBackStackEntryCount() == 1) {

            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {

                Intent intent = new Intent("unregisterFilterBroadcast");
                context.sendBroadcast(intent);

                finish();

            } else {
                showToast("Tap back button in order to exit");
            }

            mBackPressed = System.currentTimeMillis();

        } else {

            isFragmentPopedOut = true;
            fragmentManager.popBackStack();

        }

    }

    private void loadFragment(Fragment fragment, String fragmentTag) {


        currentFragmentTag = fragmentTag;
        boolean isAlreadyPresentInStack = false;

        List<Fragment> frags = getSupportFragmentManager().getFragments();
        for (Fragment f : frags) {
            try {
                if (f.getTag().equalsIgnoreCase(fragmentTag)) {
                    isAlreadyPresentInStack = true;
                }
            } catch (Exception e) {
            }
        }

        if (isAlreadyPresentInStack) {

            fragmentManager.popBackStack(fragmentTag, 0);

            if(fragmentTag.equalsIgnoreCase(TAG_FULLMENU_FRAGMENT)){
                Intent intent1 = new Intent("reloadFullMenuData");
                sendBroadcast(intent1);
            }

        } else {

            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out)
                    .add(R.id.frame_container, fragment, fragmentTag)
                    .addToBackStack(fragmentTag)
                    .commit();
        }


            /* Setting Bottom Ui */

        img_bottom_home.setImageResource(R.drawable.ic_home_grey);
        img_bottom_notifications.setImageResource(R.drawable.ic_bell_grey);
        img_bottom_fullmenu.setImageResource(R.drawable.ic_fullmenu_grey);
        img_bottom_profile.setImageResource(R.drawable.ic_profile_grey);
        txt_bottom_home.setTextColor(getResources().getColor(R.color.colorGreyBottomNavigation));
        txt_bottom_fullmenu.setTextColor(getResources().getColor(R.color.colorGreyBottomNavigation));
        txt_bottom_notifications.setTextColor(getResources().getColor(R.color.colorGreyBottomNavigation));
        txt_bottom_profile.setTextColor(getResources().getColor(R.color.colorGreyBottomNavigation));

        switch (currentFragmentTag) {

            case TAG_HOME_FRAGMENT:
                txt_bottom_home.setTextColor(getResources().getColor(R.color.colorPrimary));
                img_bottom_home.setImageResource(R.drawable.ic_home_primary);
                txt_actionbar_title.setText("HOME");
                break;

            case TAG_FULLMENU_FRAGMENT:
                txt_bottom_fullmenu.setTextColor(getResources().getColor(R.color.colorPrimary));
                img_bottom_fullmenu.setImageResource(R.drawable.ic_fullmenu_primary);
                txt_actionbar_title.setText("FULL MENU");
                break;

            case TAG_NOTIFICATIONS_FRAGMENT:
                txt_bottom_notifications.setTextColor(getResources().getColor(R.color.colorPrimary));
                img_bottom_notifications.setImageResource(R.drawable.ic_bell_primary);
                txt_actionbar_title.setText("NOTIFICATIONS");
                break;

            case TAG_PROFILE_FRAGMENT:
                txt_bottom_profile.setTextColor(getResources().getColor(R.color.colorPrimary));
                img_bottom_profile.setImageResource(R.drawable.ic_profile_primary);
                txt_actionbar_title.setText("PROFILE");
                break;

        }

        refreshActionbarOptions();


    }

    private void refreshActionbarOptions() {

        if (currentFragmentTag.equals(TAG_HOME_FRAGMENT)) {
            img_actionbar_logo.setVisibility(View.VISIBLE);
            rl_actionbar_search.setVisibility(View.VISIBLE);
            img_actionbar_filter.setVisibility(View.VISIBLE);
            txt_actionbar_title.setVisibility(View.GONE);
            img_actionbar_trash.setVisibility(View.GONE);
        } else {
            img_actionbar_logo.setVisibility(View.GONE);
            rl_actionbar_search.setVisibility(View.GONE);
            img_actionbar_filter.setVisibility(View.GONE);
            txt_actionbar_title.setVisibility(View.VISIBLE);
            img_actionbar_trash.setVisibility(View.GONE);
        }

        if (currentFragmentTag.equals(TAG_NOTIFICATIONS_FRAGMENT)) {
            img_actionbar_trash.setVisibility(View.VISIBLE);
        }else {
            img_actionbar_trash.setVisibility(View.GONE);
        }

    }

    public void loadNewHallData(String hallId, boolean showDialog) {

        if (isInternetAvailable()) {

            if (showDialog) {
                showProgressDialog();
            }

            String dietaryList = "", cuisineList = "", sortMethod = "";


            if(getSessionManager().isHomeFilterActive()){

                dietaryList = getSessionManager().getFilterDietary();
                cuisineList = getSessionManager().getFilterCuisine();
                sortMethod = getSessionManager().getFilterSort();

                homePresenter.getSearchHall1Dishes(
                        getSessionManager().getUserId(),
                        hallId,
                        "",
                        "",
                        dietaryList,
                        cuisineList,
                        sortMethod
                );

            }else {

                homePresenter.getHall1Dishes(
                        getSessionManager().getUserId(),
                        hallId,
                        AppUtils.getTimeZone(),
                        dietaryList,
                        cuisineList,
                        sortMethod
                );

            }

        } else {
            showToast(getString(R.string.api_error_internet));
        }

    }


    // TODO Interface Methods

    @Override
    public void onSuccess_GetDetails(String message, UserDetailsResponse userDetailsResponse) {
        hideProgressDialog();
        getSessionManager().setUserDetailsResponse(userDetailsResponse);
    }

    @Override
    public void onSuccess_AllHallMenu(String message, HomeDishesResponse homeDishesResponse) {

        hideProgressDialog();

        if (!getSessionManager().isUserSkippedLoggedIn()) {
            getSessionManager().setCartCount(homeDishesResponse.getData().getCart_count());
            getSessionManager().setCartPrice(homeDishesResponse.getData().getCart_total());
            checkAndSetCartCount();
        }

        // Halls Data
        if (null != homeDishesResponse.getData().getHalls()) {
            HomeDishesResponse.Halls[] halls = homeDishesResponse.getData().getHalls();
            halls = AppUtils.checkAndSetTimingIssue(halls);
            getSessionManager().setHallsResponse(halls);
        }

        // Banners Data
        if (null != homeDishesResponse.getData().getBanners()) {
            HomeDishesResponse.Banners[] banners = homeDishesResponse.getData().getBanners();
            getSessionManager().setBannersResponse(banners);
        }

        // Categories Data
        if (null != homeDishesResponse.getData().getCategories()) {
            HomeDishesResponse.Categories[] categories = homeDishesResponse.getData().getCategories();
            getSessionManager().setCategoriesResponse(categories);
        }

        // Filters Data
        if (null != homeDishesResponse.getData().getFilters()) {
            getSessionManager().setFilterResponse(homeDishesResponse.getData().getFilters());
        }

        HomeFragment.isSpinnerjustStarted = true;

        HomeFragment.getInstance().loadTabsAndFragments();

        Intent intent1 = new Intent("reloadFullMenuData");
        sendBroadcast(intent1);

    }

    @Override
    public void onSuccess_SearchHomeDishes(SearchHomeDishesResponse searchHomeDishesResponse) {

        hideProgressDialog();

        if(null != searchHomeDishesResponse.getData()) {

            // Banners Data
            if (null != searchHomeDishesResponse.getData().getBanners()) {
                getSessionManager().setSearchBannersResponse(searchHomeDishesResponse.getData().getBanners());
            }

            // Categories Data
            if (null != searchHomeDishesResponse.getData().getCategories()) {
                getSessionManager().setSearchCategoriesResponse(searchHomeDishesResponse.getData().getCategories());
            }

            // Filters Data
            if (null != searchHomeDishesResponse.getData().getFilters()) {
                getSessionManager().setSearchFilterResponse(searchHomeDishesResponse.getData().getFilters());
            }

            HomeFragment.isSpinnerjustStarted = true;

            HomeFragment.getInstance().loadTabsAndFragments();

            Intent intent1 = new Intent("reloadFullMenuData");
            sendBroadcast(intent1);

        }

    }

    @Override
    public void onError(String message) {
        hideProgressDialog();
        try{ Home_SubFragment.getInstance().swipyrefreshlayout.setRefreshing(false); }catch (Exception e){}
        showToast(message);
    }

    private void checkLocalCartList() {

        if (getSessionManager().getLocalCartItems() != null) {
            ArrayList<Items> itemsList = getSessionManager().getLocalCartItems();

            if (itemsList.size() > 0) {

                JSONArray jsonArray = new JSONArray();
                JSONArray addOnArray, optionsArray;


                for (int i = 0; i < itemsList.size(); i++) {


                    JSONObject jsonObject = new JSONObject();

                    try {
                        //jsonObject.put("item_id(optional)", itemsList.get(i).getDish_id());
                        jsonObject.put("id", itemsList.get(i).getDish_id());
                        jsonObject.put("qty", itemsList.get(i).getQty());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (itemsList.get(i).getOptions().length > 0) {

                        optionsArray = new JSONArray();
                        JSONObject optionObject;


                        for (int j = 0; j < itemsList.get(i).getOptions().length; j++) {

                            optionObject = new JSONObject();

                            try {
                                optionObject.put("option_id", itemsList.get(i).getOptions()[j].getOption_id());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                optionObject.put("ingredient_id", itemsList.get(i).getOptions()[j].getIngredient_id());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                optionsArray.put(j, optionObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            if (optionsArray.length() > 0) {

                                jsonObject.put("options", optionsArray);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        optionsArray = new JSONArray();

                        try {

                            jsonObject.put("options", optionsArray);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    if (itemsList.get(i).getAddons().length > 0) {
                        addOnArray = new JSONArray();
                        JSONObject addOnObject = null;

                        for (int j = 0; j < itemsList.get(i).getAddons().length; j++) {
                            addOnObject = new JSONObject();

                            try {
                                addOnObject.put("id", itemsList.get(i).getAddons()[j].getAddon_id());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                addOnArray.put(j, addOnObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            if (addOnArray.length() > 0) {

                                jsonObject.put("addons", addOnArray);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        addOnArray = new JSONArray();

                        try {
                            jsonObject.put("addons", addOnArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        jsonArray.put(i, jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Log.e("jsonArray}}", "jsonArray}}" + jsonArray.toString());

                if (jsonArray.length() > 0) {
                    saveLocalcartToServer(jsonArray);
                }
            }
        }
    }

    private void saveLocalcartToServer(JSONArray jsonArray) {

        homePresenter.saveLocalcartToServer(getSessionManager().getUserId(),
                getSessionManager().getLocalCartHallId(), "", jsonArray.toString());

    }

    @Override
    public void onSuccess_LocalCartSave(String message) {

        Log.e("LocalCartSave::", "LocalCartSave::" + message);

        getSessionManager().setLocalCartItems(null);
    }

    public void fetchNotiCount() {

        if (isInternetAvailable()) {
            homePresenter.getNotiCount(getSessionManager().getUserId());
        }

    }

    @Override
    public void onSuccess_NotiCount(String notiCount) {

        notificationCount = notiCount;

        Log.e("notificationCount:::", "notificationCount:::" + notificationCount);

        if (!notificationCount.equalsIgnoreCase("0")) {
            txtNotiCount.setVisibility(View.VISIBLE);
            txtNotiCount.setText(notificationCount);
        }
    }
}
