package com.android42works.magicapp.responses;

public class OrdersResponse {

    private String message;

    private Data data;

    private String success;

    public String getMessage ()
    {
        if(message!=null){ return message; } else { return ""; }
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public Data getData ()
    {
        return data;
    }

    public void setData (Data data)
    {
        this.data = data;
    }

    public String getSuccess ()
    {
        if(success!=null){ return success; } else { return ""; }
    }

    public void setSuccess (String success)
    {
        this.success = success;
    }


   /* Data */

    public class Data
    {
        private Orders[] orders;

        public Orders[] getOrders ()
        {
            return orders;
        }

        public void setOrders (Orders[] orders)
        {
            this.orders = orders;
        }
    }

    /* Orders */

    public class Orders
    {
        private String order_number;

        private String order_total;

        private String area_rating;

        private String instructions;

        private String status;

        private String customer_phone;

        private String customer_name;

        private String hall_id;

        private String cancel_request;

        private String coupon_code;

        private String discount;

        private String id;

        private Card card;

        private String tax;

        private String start_time;

        private String end_time;

        private Items[] items;

        private String tax_description;

        private String is_immediate;

        private String notes;

        private String timing;

        private String thumb;

        private String grand_total;

        private String restaurant_name;

        public String getStart_time() {
            if(start_time!=null){ return start_time; } else { return "";}
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            if(end_time!=null){ return end_time; } else { return "";}
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getCancel_request() {
            return cancel_request;
        }

        public void setCancel_request(String cancel_request) {
            this.cancel_request = cancel_request;
        }

        public String getHall_id() {
            return hall_id;
        }

        public void setHall_id(String hall_id) {
            this.hall_id = hall_id;
        }

        public Card getCard ()
        {
            return card;
        }

        public void setCard (Card card)
        {
            this.card = card;
        }

        public String getOrder_number ()
        {
            if(order_number!=null){ return order_number; } else { return ""; }
        }

        public void setOrder_number (String order_number)
        {
            this.order_number = order_number;
        }

        public String getOrder_total ()
        {
            if(order_total!=null){ return order_total; } else { return "0"; }
        }

        public void setOrder_total (String order_total)
        {
            this.order_total = order_total;
        }

        public String getArea_rating ()
        {
            if(area_rating!=null){ return area_rating; } else { return ""; }
        }

        public void setArea_rating (String area_rating)
        {
            this.area_rating = area_rating;
        }

        public String getInstructions ()
        {
            if(instructions!=null){ return instructions; } else { return ""; }
        }

        public void setInstructions (String instructions)
        {
            this.instructions = instructions;
        }

        public String getStatus ()
        {
            if(status!=null){ return status; } else { return ""; }
        }

        public void setStatus (String status)
        {
            this.status = status;
        }

        public String getCustomer_phone ()
        {
            if(customer_phone!=null){ return customer_phone; } else { return ""; }
        }

        public void setCustomer_phone (String customer_phone)
        {
            this.customer_phone = customer_phone;
        }

        public String getCustomer_name ()
        {
            if(customer_name!=null){ return customer_name; } else { return ""; }
        }

        public void setCustomer_name (String customer_name)
        {
            this.customer_name = customer_name;
        }

        public String getCoupon_code ()
        {
            if(coupon_code!=null){ return coupon_code; } else { return ""; }
        }

        public void setCoupon_code (String coupon_code)
        {
            this.coupon_code = coupon_code;
        }

        public String getDiscount ()
        {
            if(discount!=null){ return discount; } else { return "0"; }
        }

        public void setDiscount (String discount)
        {
            this.discount = discount;
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
            if(tax!=null){ return tax; } else { return "0"; }
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

        public String getTax_description ()
        {
            if(tax_description!=null){ return tax_description; } else { return ""; }
        }

        public void setTax_description (String tax_description)
        {
            this.tax_description = tax_description;
        }

        public String getIs_immediate ()
        {
            if(is_immediate!=null){ return is_immediate; } else { return ""; }
        }

        public void setIs_immediate (String is_immediate)
        {
            this.is_immediate = is_immediate;
        }

        public String getNotes ()
        {
            if(notes!=null){ return notes; } else { return ""; }
        }

        public void setNotes (String notes)
        {
            this.notes = notes;
        }

        public String getTiming ()
        {
            if(timing!=null){ return timing; } else { return ""; }
        }

        public void setTiming (String timing)
        {
            this.timing = timing;
        }

        public String getThumb ()
        {
            if(thumb!=null){ return thumb; } else { return ""; }
        }

        public void setThumb (String thumb)
        {
            this.thumb = thumb;
        }

        public String getGrand_total ()
        {
            if(grand_total!=null){ return grand_total; } else { return "0"; }
        }

        public void setGrand_total (String grand_total)
        {
            this.grand_total = grand_total;
        }

        public String getRestaurant_name ()
        {
            if(restaurant_name!=null){ return restaurant_name; } else { return ""; }
        }

        public void setRestaurant_name (String restaurant_name)
        {
            this.restaurant_name = restaurant_name;
        }

    }


    /* Card */

    public class Card
    {
        private String brand;

        private String last_four_digit;

        public String getBrand ()
        {
            if(brand!=null){ return brand; } else { return ""; }
        }

        public void setBrand (String brand)
        {
            this.brand = brand;
        }

        public String getLast_four_digit ()
        {
            if(last_four_digit!=null){ return last_four_digit; } else { return ""; }
        }

        public void setLast_four_digit (String last_four_digit)
        {
            this.last_four_digit = last_four_digit;
        }

    }


    /* Items */

    public static class Items
    {
        private String id;

        private String dish_id;

        private String price;

        private String veg;

        private String item_name;

        private Addons[] addons;

        private String qty;

        private Custom_options[] custom_options;

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
            if(price!=null){ return price; } else { return ""; }
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

        public Custom_options[] getCustom_options ()
        {
            return custom_options;
        }

        public void setCustom_options (Custom_options[] custom_options)
        {
            this.custom_options = custom_options;
        }

    }


    /* Add Ons */

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
            if(price!=null){ return price; } else { return ""; }
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


    /* Custom Options */

    public class Custom_options
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

}
