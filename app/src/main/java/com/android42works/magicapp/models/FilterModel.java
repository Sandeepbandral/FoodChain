package com.android42works.magicapp.models;

import java.util.ArrayList;

public class FilterModel {

    String id, name;
    boolean isExpanded;
    ArrayList<FilterOptionsModel> optionsList;

    public FilterModel(String id, String name, boolean isExpanded, ArrayList<FilterOptionsModel> optionsList) {
        this.id = id;
        this.name = name;
        this.isExpanded = isExpanded;
        this.optionsList = optionsList;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public ArrayList<FilterOptionsModel> getOptionsList() {
        return optionsList;
    }

}
