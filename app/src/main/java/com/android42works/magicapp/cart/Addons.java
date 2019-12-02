package com.android42works.magicapp.cart;

public class Addons
{
    private String id;

    private String price;

    private String addon_id;

    private String addon;

    public String getId ()
    {
        if(id!=null){ return id; } else { return ""; }
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getPrice ()
    {
        if(price!=null){ return price; } else { return "0"; }
    }

    public void setPrice (String price)
    {
        this.price = price;
    }

    public String getAddon_id ()
    {
        if(addon_id!=null){ return addon_id; } else { return ""; }
    }

    public void setAddon_id (String addon_id)
    {
        this.addon_id = addon_id;
    }

    public String getAddon ()
    {
        if(addon!=null){ return addon; } else { return ""; }
    }

    public void setAddon (String addon)
    {
        this.addon = addon;
    }

}
