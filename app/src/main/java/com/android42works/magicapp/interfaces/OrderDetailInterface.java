package com.android42works.magicapp.interfaces;


import com.android42works.magicapp.base.MvpView;
import com.android42works.magicapp.responses.OrderDetailResponse;

/**
 * Created by apple on 04/10/18.
 */

public interface OrderDetailInterface extends MvpView {

    void onSuccess(String message, OrderDetailResponse orderList);
}
