package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;
import com.android42works.magicapp.cart.CartResponse;
import com.android42works.magicapp.responses.CouponResponse;
import com.android42works.magicapp.responses.LocalCartTaxResponse;
import com.android42works.magicapp.responses.TimingsResponse;

public interface CartInterface extends MvpView{

    void onClick_Dish(String type, int position, String dishId);

    void setTotalCostOfCartItems(float cartcost);

    void onSuccess_CartItems(String message, CartResponse cartResponse);

    void onSuccess_Timings(String message, TimingsResponse response);

    void onSuccess_ApplyCoupon(String message, CouponResponse couponResponse);

    void onSuccess_PlaceOrder(String message, String orderId);

    void onSuccess_RemoveCoupon(String message, CouponResponse couponResponse);

    void onSuccess_LocalCartTax(String message, LocalCartTaxResponse cartResponse);

    void onError_applyCoupon(String message);

    void onError_placeOrder(String message);
}
