package com.android42works.magicapp.models;

public class FilterOptionsModel {

    String filterId, filterName;
    boolean isSelected;

    public FilterOptionsModel(String filterId, String filterName, boolean isSelected) {
        this.filterId = filterId;
        this.filterName = filterName;
        this.isSelected = isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getFilterId() {
        return filterId;
    }

    public String getFilterName() {
        return filterName;
    }

    public boolean isSelected() {
        return isSelected;
    }

}
