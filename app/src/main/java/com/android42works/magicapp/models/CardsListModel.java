package com.android42works.magicapp.models;

import com.android42works.magicapp.responses.StripeSavedCardsListResponse;

public class CardsListModel {

    boolean isSelected;
    StripeSavedCardsListResponse.Data data;

    public CardsListModel(boolean isSelected, StripeSavedCardsListResponse.Data data) {
        this.isSelected = isSelected;
        this.data = data;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public StripeSavedCardsListResponse.Data getData() {
        return data;
    }
}
