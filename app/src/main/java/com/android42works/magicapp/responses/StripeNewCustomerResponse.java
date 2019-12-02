package com.android42works.magicapp.responses;

public class StripeNewCustomerResponse {

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
