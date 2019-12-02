package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.android42works.magicapp.responses.UserDetailsResponse;

public interface HomeDataInterface extends MvpView {

    void onSuccess_AllHallMenu(String message, HomeDishesResponse homeDishesResponse);

}
