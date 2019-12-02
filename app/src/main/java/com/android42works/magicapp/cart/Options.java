package com.android42works.magicapp.cart;

public class Options
{
    private String id;

    private String ingredient_id;

    private String ingredient;

    private String option_id;

    private String option;

    public String getId ()
    {
        if(id!=null){ return id; } else { return ""; }
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getIngredient_id ()
    {
        if(ingredient_id!=null){ return ingredient_id; } else { return ""; }
    }

    public void setIngredient_id (String ingredient_id)
    {
        this.ingredient_id = ingredient_id;
    }

    public String getIngredient ()
    {
        if(ingredient!=null){ return ingredient; } else { return ""; }
    }

    public void setIngredient (String ingredient)
    {
        this.ingredient = ingredient;
    }

    public String getOption_id ()
    {
        if(option_id!=null){ return option_id; } else { return ""; }
    }

    public void setOption_id (String option_id)
    {
        this.option_id = option_id;
    }

    public String getOption ()
    {
        if(option!=null){ return option; } else { return ""; }
    }

    public void setOption (String option)
    {
        this.option = option;
    }

}
