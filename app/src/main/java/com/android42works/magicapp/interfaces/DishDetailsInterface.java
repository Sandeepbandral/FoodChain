package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;
import com.android42works.magicapp.responses.DishDetailsResponse;
import com.android42works.magicapp.responses.NewNutritionalResponse;
import com.android42works.magicapp.responses.NutritionalResponse;

public interface DishDetailsInterface extends MvpView{

    void onSuccess(String message, DishDetailsResponse dishDetailsResponse);
    void onSelect_Option(int parentPosition, int childPosition, boolean isChecked, String price);
    void onSuccessDishNutritions(NutritionalResponse nutritionalResponse, NewNutritionalResponse newNutritionalResponse);

}
