package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;
import com.android42works.magicapp.responses.FavouriteResponse;

import java.util.ArrayList;

public interface DishOptionsInterface extends MvpView{

    void onClick_Dish(String dishId);
    void onClick_Fav(int position, String dishId, String isFavourite);

}
