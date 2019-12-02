package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;
import com.android42works.magicapp.responses.FavouriteResponse;

import java.util.ArrayList;

public interface FavouriteInterface extends MvpView{

    void onSuccess_getList(String message, ArrayList<FavouriteResponse.Dishes> dishesList);

}
