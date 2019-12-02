package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;
import com.android42works.magicapp.responses.StripeSavedCardsListResponse;

import java.util.ArrayList;

public interface CardListingInterface extends MvpView{

    void onClick_SaveAsDefault(int position);
    void onClick_SelectCard(int position);
    void onClick_DeleteCard(int position);

}
