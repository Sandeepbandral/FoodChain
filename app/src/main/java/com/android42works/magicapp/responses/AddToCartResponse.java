package com.android42works.magicapp.responses;

public class AddToCartResponse {

    private String message;

    private String success;

    private Data data;

    public String getMessage ()
    {
        if(message!=null){ return message; } else { return ""; }
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getSuccess ()
    {
        if(success!=null){ return success; } else { return ""; }
    }

    public void setSuccess (String success)
    {
        this.success = success;
    }

    public Data getData ()
    {
        return data;
    }

    public void setData (Data data)
    {
        this.data = data;
    }

    /* Data */

    public class Data
    {
        private String cart_count;

        private String cart_total;

        public String getCart_count ()
        {
            if(cart_count!=null){ return cart_count; } else { return "0"; }
        }

        public void setCart_count (String cart_count)
        {
            this.cart_count = cart_count;
        }

        public String getCart_total() {
            if(cart_total!=null){ return cart_total; } else { return "0.00"; }
        }

        public void setCart_total(String cart_total) {
            this.cart_total = cart_total;
        }
    }


}
