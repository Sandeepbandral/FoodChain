package com.android42works.magicapp.models;

public class FutureTimeChildModel {

    String timingRange, openTime, closeTime;

    public FutureTimeChildModel(String timingRange, String openTime, String closeTime) {
        this.timingRange = timingRange;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public String getTimingRange() {
        return timingRange;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

}
