package com.android42works.magicapp.models;

import com.android42works.magicapp.cart.Items;

public class CartModel {

    boolean isUpdating;
    Items items;

    public CartModel(boolean isUpdating, Items items) {
        this.isUpdating = isUpdating;
        this.items = items;
    }

    public boolean isUpdating() {
        return isUpdating;
    }

    public Items getItems() {
        return items;
    }

}
