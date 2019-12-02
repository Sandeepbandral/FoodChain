package com.android42works.magicapp.models;

import com.android42works.magicapp.sectionedrecyclerview.Section;

import java.util.List;

public class FutureTimeParentModel implements Section<FutureTimeChildModel>, Comparable<FutureTimeParentModel> {

    List<FutureTimeChildModel> childList;
    public String sectionText;
    int index;

    public FutureTimeParentModel(String sectionText, List<FutureTimeChildModel> childList, int index) {
        this.childList = childList;
        this.sectionText = sectionText;
        this.index = index;
    }

    public void setChildList(List<FutureTimeChildModel> childList) {
        this.childList = childList;
    }

    @Override
    public List<FutureTimeChildModel> getChildItems() {
        return childList;
    }

    public String getSectionText() {
        return sectionText;
    }

    @Override
    public int compareTo(FutureTimeParentModel another) {
        if (this.index > another.index) {
            return -1;
        } else {
            return 1;
        }
    }
}

