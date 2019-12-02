package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;
import com.android42works.magicapp.responses.OrdersResponse;

import java.util.ArrayList;

public interface OrderDetailsInterface extends MvpView{

    void onClick(int position, String type);
    void onSuccess(String message, ArrayList<OrdersResponse.Orders> ordersList);

}
