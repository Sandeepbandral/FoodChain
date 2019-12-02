package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;
import com.android42works.magicapp.responses.StripeSavedCardsListResponse;

import java.util.ArrayList;

public interface AddNewCardInterface extends MvpView{

    void onSuccess_StripeID(String stripeId);
    void onSuccess_TokenID(String tokenId);
    void onSuccess_CardSave(String cardId);

}
