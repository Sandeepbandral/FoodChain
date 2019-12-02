package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;
import com.android42works.magicapp.responses.OrderSuccessResponse;

public interface OrderSuccessInterface extends MvpView{

    void onSuccess(String message, OrderSuccessResponse response);

}
