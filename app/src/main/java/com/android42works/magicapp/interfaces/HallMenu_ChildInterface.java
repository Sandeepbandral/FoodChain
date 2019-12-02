package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;

public interface HallMenu_ChildInterface extends MvpView{

    void onClick_dish(String dishId);
    void onClick_fav(String dishId, String isFavourite);

}
