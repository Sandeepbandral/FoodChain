package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;

public interface SuccessWithTypeInterface extends MvpView{

    void onSuccess(String type, String message);

}
