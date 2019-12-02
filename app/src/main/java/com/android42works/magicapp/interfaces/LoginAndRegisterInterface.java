package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;
import com.android42works.magicapp.responses.UserDetailsResponse;

public interface LoginAndRegisterInterface extends MvpView{

    void onSuccess_LoginOrRegister(String message, UserDetailsResponse userDetailsResponse, String name, boolean isFBLogin);

}
