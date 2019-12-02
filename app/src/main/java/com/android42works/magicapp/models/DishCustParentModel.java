package com.android42works.magicapp.models;

import java.util.ArrayList;

public class DishCustParentModel {

    String id, name, maxoptions;

    ArrayList<DishCustChildModel> optionList;

    public DishCustParentModel(String id, String name, String maxoptions, ArrayList<DishCustChildModel> optionList) {
        this.id = id;
        this.name = name;
        this.maxoptions = maxoptions;
        this.optionList = optionList;
    }

    public void setOptionList(ArrayList<DishCustChildModel> optionList) {
        this.optionList = optionList;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMaxoptions() {
        return maxoptions;
    }

    public ArrayList<DishCustChildModel> getOptionList() {
        return optionList;
    }
}
