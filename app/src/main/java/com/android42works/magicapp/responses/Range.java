package com.android42works.magicapp.responses;

public class Range {

    private String end_time;

    private String start_time;

    public String getEnd_time ()
    {
        if (end_time != null) {
            return end_time;
        } else {
            return "";
        }
    }

    public void setEnd_time (String end_time)
    {
        this.end_time = end_time;
    }

    public String getStart_time ()
    {
        if (start_time != null) {
            return start_time;
        } else {
            return "";
        }
    }

    public void setStart_time (String start_time)
    {
        this.start_time = start_time;
    }
}
