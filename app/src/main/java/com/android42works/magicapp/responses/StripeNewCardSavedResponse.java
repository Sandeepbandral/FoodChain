package com.android42works.magicapp.responses;

public class StripeNewCardSavedResponse {

    private String id;

    public String getId ()
    {
        if(null!=id){ return id; } else { return ""; }
    }

    public void setId (String id)
    {
        this.id = id;
    }

}
