package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;
import com.android42works.magicapp.responses.HallMenuResponse;

public interface HallMenuInterface extends MvpView{

    void onSuccess(String message, HallMenuResponse hallMenuResponse);

}
