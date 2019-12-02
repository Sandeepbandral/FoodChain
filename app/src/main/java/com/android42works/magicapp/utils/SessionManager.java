package com.android42works.magicapp.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.android42works.magicapp.cart.Items;
import com.android42works.magicapp.responses.HallMenuResponse;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.android42works.magicapp.responses.SearchHomeDishesResponse;
import com.android42works.magicapp.responses.TimingsResponse;
import com.android42works.magicapp.responses.UserDetailsResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/* Created by JSP@nesar */

public class SessionManager {

    SharedPreferences pref, tokenPref;
    SharedPreferences.Editor editor, tokenEditor;
    Context _context;
    Gson gson = new Gson();

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "MagicAppSession";
    private static final String TOKEN_PREF_NAME = "TokenSession";

    /* Users */
    private static final String IS_FB_LOGIN = "IsFBLogin";
    private static final String IS_USER_SKIPPEDIN = "IsUserSkippedLoggedIn";
    private static final String IS_LOGGEDIN = "IsLoggedIn";
    private static final String USERID = "UserId";
    private static final String USEREMAIL = "UserEmail";
    private static final String USERNAME = "UserName";

    /* FCM */
    private static final String FCMTOKEN = "FCMToken";

    /* Others */
    private static final String USERDETAILSRESPONSE = "UserDetailsResponse";
    private static final String DISHESRESPONSE = "DishesResponse";
    private static final String BANNERSRESPONSE = "BannersResponse";
    private static final String HALLSRESPONSE = "HallsResponse";
    private static final String HALLMENURESPONSE = "HallMenuResponse";
    private static final String CARTCOUNT = "CartCount";
    private static final String CARTPRICE = "CartPrice";
    private static final String CARTINSTRUCTIONS = "CartInstructions";

    /* Filter */
    private static final String FILTERRESPONSE = "FilterResponse";
    private static final String HALLFILTERRESPONSE = "HallFilterResponse";
    private static final String IS_HOMEFILTER_ACTIVE = "IsHomeFilterActive";
    private static final String IS_HALLFILTER_ACTIVE = "IsHallFilter_Active";
    private static final String SELECTEDHALL = "SelectedHall";

    private static final String FILTER_DIETARY = "FilterDietary";
    private static final String FILTER_CUISINE = "FilterCuisine";
    private static final String FILTER_SORT = "FilterSort";

    private static final String HALL_FILTER_DIETARY = "HallFilterDietary";
    private static final String HALL_FILTER_CUISINE = "HallFilterCuisine";
    private static final String HALL_FILTER_SORT = "HallFilterSort";

    /* Local Cart */
    private static final String LOCALCARTHALLID = "localCartHallId";
    private static final String LOCALCARTITEMS = "localCartItems";
    private static final String LOCALCARTCOUPONCODE = "localCouponCode";
    private static final String LOCALCARTNAME = "localCartName";

    private String localCartCouponName;
    private String localcartHallName;

    public SessionManager(Context context) {

        this._context = context;

        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        tokenPref = _context.getSharedPreferences(TOKEN_PREF_NAME, PRIVATE_MODE);

        editor = pref.edit();
        tokenEditor = tokenPref.edit();

    }

    public void clearAllData() {
        editor.clear().commit();
    }


    //>>>>>>>>>>>>


    public void setIsUserSkippedin(boolean skipLoginStatus) {
        editor.putBoolean(IS_USER_SKIPPEDIN, skipLoginStatus);
        editor.commit();
    }

    public boolean isUserSkippedLoggedIn() {
        return pref.getBoolean(IS_USER_SKIPPEDIN, false);
    }


    //>>>>>>>>>>>>


    public void setIsFBLogin(boolean fbLogin) {
        editor.putBoolean(IS_FB_LOGIN, fbLogin);
        editor.commit();
    }

    public boolean isFBLogin() {
        return pref.getBoolean(IS_FB_LOGIN, false);
    }


    //>>>>>>>>>>>>


    public void setUserLoginStatus(boolean loginStatus) {
        editor.putBoolean(IS_LOGGEDIN, loginStatus);
        editor.commit();
    }

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_LOGGEDIN, false);
    }


    //>>>>>>>>>>>>


    public void setIsHallfilterActive(boolean isHallFilter) {
        editor.putBoolean(IS_HALLFILTER_ACTIVE, isHallFilter);
        editor.commit();
    }

    public boolean isHallFilterActive() {
        return pref.getBoolean(IS_HALLFILTER_ACTIVE, false);
    }

    //>>>>>>>>>>>>


    public void setIsHomefilterActive(boolean isHomeFilter) {
        editor.putBoolean(IS_HOMEFILTER_ACTIVE, isHomeFilter);
        editor.commit();
    }

    public boolean isHomeFilterActive() {
        return pref.getBoolean(IS_HOMEFILTER_ACTIVE, false);
    }

    //>>>>>>>>>>>>


    public void setCartCount(String count) {
        editor.putString(CARTCOUNT, count);
        editor.commit();
    }

    public String getCartCount() {
        return pref.getString(CARTCOUNT, "0");
    }


    //>>>>>>>>>>>>


    public void setCartPrice(String price) {
        editor.putString(CARTPRICE, price);
        editor.commit();
    }

    public String getCartPrice() {
        return pref.getString(CARTPRICE, "0");
    }


    //>>>>>>>>>>>>


    public void setCartInstructions(String instructions) {
        editor.putString(CARTINSTRUCTIONS, instructions);
        editor.commit();
    }

    public String getCartInstructions() {
        return pref.getString(CARTINSTRUCTIONS, "");
    }


    //>>>>>>>>>>>>


    public void setUserId(String userId) {
        editor.putString(USERID, userId);
        editor.commit();
    }

    public String getUserId() {
        return pref.getString(USERID, "");
    }


    //>>>>>>>>>>>>


    public void setFcmtoken(String fcmtoken) {
        tokenEditor.putString(FCMTOKEN, fcmtoken);
        tokenEditor.commit();
    }

    public String getFcmtoken() {
        return tokenPref.getString(FCMTOKEN, "NotAvailable");
    }


    //>>>>>>>>>>>>


    public void setSelectedHall(String hallId) {
        editor.putString(SELECTEDHALL, hallId);
        editor.commit();
    }

    public String getSelectedHall() {
        return pref.getString(SELECTEDHALL, "");
    }


    //>>>>>>>>>>>>


    public void setLocalcarthallId(String hallId) {
        editor.putString(LOCALCARTHALLID, hallId);
        editor.commit();
    }

    public String getLocalCartHallId() {
        return pref.getString(LOCALCARTHALLID, "");
    }


    //>>>>>>>>>>>>


    public void setLocalCartItems(ArrayList<Items> items) {
        String json = gson.toJson(items);
        editor.putString(LOCALCARTITEMS, json);
        editor.commit();
    }

    public ArrayList<Items> getLocalCartItems() {
        Type type = new TypeToken<ArrayList<Items>>() {
        }.getType();
        ArrayList<Items> items = gson.fromJson(pref.getString(LOCALCARTITEMS, null), type);
        return items;
    }


    //>>>>>>>>>>>>


    public void setUserDetailsResponse(UserDetailsResponse userDetailsResponse) {
        String json = gson.toJson(userDetailsResponse);
        editor.putString(USERDETAILSRESPONSE, json);
        editor.commit();
    }

    public UserDetailsResponse getUserDetailsResponse() {
        Type type = new TypeToken<UserDetailsResponse>() {
        }.getType();
        UserDetailsResponse userDetailsResponse = gson.fromJson(pref.getString(USERDETAILSRESPONSE, ""), type);
        return userDetailsResponse;
    }

    public void setUserEmail(String userEmail) {
        editor.putString(USEREMAIL, userEmail);
        editor.commit();
    }

    public String getUserEmail() {
        return pref.getString(USEREMAIL, "");
    }


    public void setUserName(String userName) {
        editor.putString(USERNAME, userName);
        editor.commit();
    }

    public String getUserName() {
        return pref.getString(USERNAME, "");
    }


    //>>>>>>>>>>>>


    public void setEmptyCategoriesResponse() {
        editor.putString(DISHESRESPONSE, "");
        editor.commit();
    }

    public void setSearchCategoriesResponse(SearchHomeDishesResponse.Categories[] categoriesResponse) {
        String json = gson.toJson(categoriesResponse);
        editor.putString(DISHESRESPONSE, json);
        editor.commit();
    }

    public void setCategoriesResponse(HomeDishesResponse.Categories[] categoriesResponse) {
        String json = gson.toJson(categoriesResponse);
        editor.putString(DISHESRESPONSE, json);
        editor.commit();
    }

    public HomeDishesResponse.Categories[] getCategoriesResponse() {
        Type type = new TypeToken<HomeDishesResponse.Categories[]>() {
        }.getType();
        HomeDishesResponse.Categories[] categoriesResponse = gson.fromJson(pref.getString(DISHESRESPONSE, ""), type);
        return categoriesResponse;
    }


    //>>>>>>>>>>>>


    public void setEmptyBannersResponse() {
        editor.putString(BANNERSRESPONSE, "");
        editor.commit();
    }

    public void setSearchBannersResponse(SearchHomeDishesResponse.Banners[] bannersResponse) {
        String json = gson.toJson(bannersResponse);
        editor.putString(BANNERSRESPONSE, json);
        editor.commit();
    }

    public void setBannersResponse(HomeDishesResponse.Banners[] bannersResponse) {
        String json = gson.toJson(bannersResponse);
        editor.putString(BANNERSRESPONSE, json);
        editor.commit();
    }

    public HomeDishesResponse.Banners[] getBannerResponse() {
        Type type = new TypeToken<HomeDishesResponse.Banners[]>() {
        }.getType();
        HomeDishesResponse.Banners[] bannersResponse = gson.fromJson(pref.getString(BANNERSRESPONSE, ""), type);
        return bannersResponse;
    }


    //>>>>>>>>>>>>


    public void setEmptyHallsResponse() {
        editor.putString(HALLSRESPONSE, "");
        editor.commit();
    }


    public void setHallsTimingsResponse(TimingsResponse.Halls[] hallsResponse) {
        String json = gson.toJson(hallsResponse);
        editor.putString(HALLSRESPONSE, json);
        editor.commit();
    }


    public void setHallsResponse(HomeDishesResponse.Halls[] hallsResponse) {
        String json = gson.toJson(hallsResponse);
        editor.putString(HALLSRESPONSE, json);
        editor.commit();
    }

    public HomeDishesResponse.Halls[] getHallsResponse() {
        Type type = new TypeToken<HomeDishesResponse.Halls[]>() {
        }.getType();
        HomeDishesResponse.Halls[] hallsResponse = gson.fromJson(pref.getString(HALLSRESPONSE, null), type);
        return hallsResponse;
    }


    //>>>>>>>>>>>>


    public void setEmptyFilterResponse() {
        editor.putString(FILTERRESPONSE, "");
        editor.commit();
    }

    public void setSearchFilterResponse(SearchHomeDishesResponse.Filters filterResponse) {
        String json = gson.toJson(filterResponse);
        editor.putString(FILTERRESPONSE, json);
        editor.commit();
    }

    public void setFilterResponse(HomeDishesResponse.Filters filterResponse) {
        String json = gson.toJson(filterResponse);
        editor.putString(FILTERRESPONSE, json);
        editor.commit();
    }

    public HomeDishesResponse.Filters getFilterResponse() {
        Type type = new TypeToken<HomeDishesResponse.Filters>() {
        }.getType();
        HomeDishesResponse.Filters filterResponse = gson.fromJson(pref.getString(FILTERRESPONSE, null), type);
        return filterResponse;
    }


    //>>>>>>>>>>>>


    public void setEmptyHallFilterResponse() {
        editor.putString(HALLFILTERRESPONSE, "");
        editor.commit();
    }

    public void setHallFilterResponse(HallMenuResponse.Filters filterResponse) {
        String json = gson.toJson(filterResponse);
        editor.putString(HALLFILTERRESPONSE, json);
        editor.commit();
    }

    public HallMenuResponse.Filters getHallFilterResponse() {
        Type type = new TypeToken<HallMenuResponse.Filters>() {
        }.getType();
        HallMenuResponse.Filters filterResponse = gson.fromJson(pref.getString(HALLFILTERRESPONSE, null), type);
        return filterResponse;
    }


    //>>>>>>>>>>>>


    public void setHallMenuResponse(HallMenuResponse hallMenuResponse) {
        String json = gson.toJson(hallMenuResponse);
        editor.putString(HALLMENURESPONSE, json);
        editor.commit();
    }

    public HallMenuResponse getHallMenuResponse() {
        Type type = new TypeToken<HallMenuResponse>() {
        }.getType();
        HallMenuResponse hallMenuResponse = gson.fromJson(pref.getString(HALLMENURESPONSE, ""), type);
        return hallMenuResponse;
    }


    //>>>>>>>>>>>>


    public void setFilterDietary(String filterDietary) {
        editor.putString(FILTER_DIETARY, filterDietary);
        editor.commit();
    }

    public String getFilterDietary() {
        return pref.getString(FILTER_DIETARY, "");
    }


    //>>>>>>>>>>>>


    public void setFilterCuisine(String filterCuisine) {
        editor.putString(FILTER_CUISINE, filterCuisine);
        editor.commit();
    }

    public String getFilterCuisine() {
        return pref.getString(FILTER_CUISINE, "");
    }


    //>>>>>>>>>>>>


    public void setFilterSort(String filterSort) {
        editor.putString(FILTER_SORT, filterSort);
        editor.commit();
    }

    public String getFilterSort() {
        return pref.getString(FILTER_SORT, "");
    }


    //>>>>>>>>>>>>


    public void setHallFilterDietary(String filterDietary) {
        editor.putString(HALL_FILTER_DIETARY, filterDietary);
        editor.commit();
    }

    public String getHallFilterDietary() {
        return pref.getString(HALL_FILTER_DIETARY, "");
    }


    //>>>>>>>>>>>>


    public void setHallFilterCuisine(String filterCuisine) {
        editor.putString(HALL_FILTER_CUISINE, filterCuisine);
        editor.commit();
    }

    public String getHallFilterCuisine() {
        return pref.getString(HALL_FILTER_CUISINE, "");
    }


    //>>>>>>>>>>>>


    public void setHallFilterSort(String filterSort) {
        editor.putString(HALL_FILTER_SORT, filterSort);
        editor.commit();
    }

    public String getHallFilterSort() {
        return pref.getString(HALL_FILTER_SORT, "");
    }


    //>>>>>>>>>>>>


    public void setLocalCartCouponName(String localCartCouponName) {
        editor.putString(LOCALCARTCOUPONCODE, localCartCouponName);
        editor.commit();
    }

    public String getLocalCartCouponName() {
        return pref.getString(LOCALCARTCOUPONCODE, "");
    }


    //>>>>>>>>>>>>


    public void setLocalcartHallName(String localcartHallName) {
        editor.putString(LOCALCARTNAME, localcartHallName);
        editor.commit();
    }

    public String getLocalcartHallName() {
        return pref.getString(LOCALCARTNAME, "");
    }


}
