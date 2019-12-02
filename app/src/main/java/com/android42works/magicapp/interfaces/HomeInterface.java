package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.android42works.magicapp.responses.SearchHomeDishesResponse;
import com.android42works.magicapp.responses.UserDetailsResponse;

public interface HomeInterface extends MvpView {

    void onSuccess_GetDetails(String message, UserDetailsResponse userDetailsResponse);

    void onSuccess_AllHallMenu(String message, HomeDishesResponse homeDishesResponse);

    void onSuccess_SearchHomeDishes(SearchHomeDishesResponse searchHomeDishesResponse);

    void onSuccess_LocalCartSave(String message);

    void onSuccess_NotiCount(String notiCount);

}
