package com.android42works.magicapp.cart;

public class Items
{
    private String id;

    private String dish_id;

    private String status;

    private String price;

    private String veg;

    private String item_name;

    private Addons[] addons;

    private String qty;

    private String[] meal_ids;

    private Options[] options;

    public String[] getMeal_ids() {
        if(meal_ids!=null){ return meal_ids; } else { return new String[0]; }
    }

    public void setMeal_ids(String[] meal_ids) {
        this.meal_ids = meal_ids;
    }

    public String getStatus() {
        if(status!=null){ return status; } else { return ""; }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId ()
    {
        if(id!=null){ return id; } else { return ""; }
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getDish_id ()
    {
        if(dish_id!=null){ return dish_id; } else { return ""; }
    }

    public void setDish_id (String dish_id)
    {
        this.dish_id = dish_id;
    }

    public String getPrice ()
    {
        if(price!=null){ return price; } else { return "0"; }
    }

    public void setPrice (String price)
    {
        this.price = price;
    }

    public String getVeg ()
    {
        if(veg!=null){ return veg; } else { return ""; }
    }

    public void setVeg (String veg)
    {
        this.veg = veg;
    }

    public String getItem_name ()
    {
        if(item_name!=null){ return item_name; } else { return ""; }
    }

    public void setItem_name (String item_name)
    {
        this.item_name = item_name;
    }

    public Addons[] getAddons ()
    {
        return addons;
    }

    public void setAddons (Addons[] addons)
    {
        this.addons = addons;
    }

    public String getQty ()
    {
        if(qty!=null){ return qty; } else { return ""; }
    }

    public void setQty (String qty)
    {
        this.qty = qty;
    }

    public Options[] getOptions ()
    {
        return options;
    }

    public void setOptions (Options[] options)
    {
        this.options = options;
    }

}