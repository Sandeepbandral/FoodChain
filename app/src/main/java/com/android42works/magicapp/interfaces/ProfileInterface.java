package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.android42works.magicapp.responses.UserDetailsResponse;

public interface ProfileInterface extends MvpView{

    void onSuccess(String message, UserDetailsResponse userDetailsResponse);

}
