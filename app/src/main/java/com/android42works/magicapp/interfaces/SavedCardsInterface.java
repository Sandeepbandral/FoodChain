package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;
import com.android42works.magicapp.responses.StripeSavedCardsListResponse;

import java.util.ArrayList;

public interface SavedCardsInterface extends MvpView{

    void onSuccess_CardsList(ArrayList<StripeSavedCardsListResponse.Data> savedCardsList);
    void onSuccess_DeleteCard(String cardId);

}
