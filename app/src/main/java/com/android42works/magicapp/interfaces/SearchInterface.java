package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;
import com.android42works.magicapp.responses.SearchResponse;

import java.util.ArrayList;

public interface SearchInterface extends MvpView{

    void onSuccess(String message, ArrayList<SearchResponse.Dishes> dishList);

}
