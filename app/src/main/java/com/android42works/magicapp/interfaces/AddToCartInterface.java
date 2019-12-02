package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;

public interface AddToCartInterface extends MvpView{

    void onSuccess_AddToCart(String message, String cartCount, String cartPrice);

    void onError_AddToCart(String message, boolean isDishFromDifferentHall, boolean isDishFromDifferentMeal, String hallName, String mealName );

}
