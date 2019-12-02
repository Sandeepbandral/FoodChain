package com.android42works.magicapp.models;

public class DishCustChildModel {

    String id, name, price;

    Boolean isSelected;

    public DishCustChildModel(String id, String name, String price, Boolean isSelected) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isSelected = isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public Boolean getSelected() {
        return isSelected;
    }

}
