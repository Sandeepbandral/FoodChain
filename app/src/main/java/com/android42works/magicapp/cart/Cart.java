package com.android42works.magicapp.cart;

public class Cart
{
    private String id;

    private String tax;

    private String meal_id;

    private Items[] items;

    private String cart_total;

    private String tax_description;

    private String hall_id;

    private String hall_name;

    private String grand_total;

    private String default_card_id;

    private String coupon_code;

    private String discount;

    public String getMeal_id() {
        if(meal_id!=null){ return meal_id; } else { return "";}
    }

    public void setMeal_id(String meal_id) {
        this.meal_id = meal_id;
    }

    public String getTax_description() {
        if(tax_description!=null){ return tax_description; } else { return "";}
    }

    public void setTax_description(String tax_description) {
        this.tax_description = tax_description;
    }

    public String getCoupon_code() {
        if(coupon_code!=null){ return coupon_code; } else { return ""; }
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getHall_id() {
        if(hall_id!=null){ return hall_id; } else { return ""; }
    }

    public void setHall_id(String hall_id) {
        this.hall_id = hall_id;
    }

    public String getDefault_card_id() {
        if(default_card_id!=null){ return default_card_id; } else { return ""; }
    }

    public void setDefault_card_id(String default_card_id) {
        this.default_card_id = default_card_id;
    }

    public String getHall_name() {
        if(hall_name!=null){ return hall_name; } else { return ""; }
    }

    public void setHall_name(String hall_name) {
        this.hall_name = hall_name;
    }

    public String getId ()
    {
        if(id!=null){ return id; } else { return ""; }
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getTax ()
    {
        if(null!=tax){ return tax; } else { return "0"; }
    }

    public void setTax (String tax)
    {
        this.tax = tax;
    }

    public Items[] getItems ()
    {
        return items;
    }

    public void setItems (Items[] items)
    {
        this.items = items;
    }

    public String getCart_total ()
    {
        if(null!=cart_total){ return cart_total; } else { return "0"; }
    }

    public void setCart_total (String cart_total)
    {
        this.cart_total = cart_total;
    }

    public String getGrand_total ()
    {
        if(grand_total!=null){ return grand_total; } else { return "0"; }
    }

    public void setGrand_total (String grand_total)
    {
        this.grand_total = grand_total;
    }

    public String getDiscount ()
    {
        if(discount!=null){ return discount; } else { return "0"; }
    }

    public void setDiscount (String discount)
    {
        this.discount = discount;
    }

}
