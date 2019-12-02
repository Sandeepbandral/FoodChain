package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;

public interface RateOrderInterface extends MvpView{

    void onSuccess_RateOrder(String message);
    void onRateApply(String thumbVote, String area, String notes);

}
