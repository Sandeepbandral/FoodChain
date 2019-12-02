package com.android42works.magicapp.responses;

public class StripeDeleteCardResponse {

    private String id;

    private String deleted;

    public String getId ()
    {
        if(null!=id){ return id; } else { return ""; }
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getDeleted() {
        if(null!=deleted){ return deleted; } else { return ""; }
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
