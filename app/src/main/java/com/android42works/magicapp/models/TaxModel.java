package com.android42works.magicapp.models;

public class TaxModel {

    String name, amount;

    public TaxModel(String name, String amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }
}
