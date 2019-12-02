package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;

public interface LogoutInterface extends MvpView {

    void onLogoutClick();
    void onLogoutSuccess(String message);

}
