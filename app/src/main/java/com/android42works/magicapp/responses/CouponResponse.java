package com.android42works.magicapp.responses;

public class CouponResponse {

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

        private String tax;

        private String tax_description;

        private String cart_total;

        private String coupon_code;

        private String grand_total;

        private String coupon_value;

        private String discount;

        public String getTax ()
        {
            if(tax!=null){ return tax; } else { return "0"; }
        }

        public void setTax (String tax)
        {
            this.tax = tax;
        }

        public String getTax_description ()
        {
            if(tax_description!=null){ return tax_description; } else { return ""; }
        }

        public void setTax_description (String tax_description)
        {
            this.tax_description = tax_description;
        }

        public String getCart_total ()
        {
            if(cart_total!=null){ return cart_total; } else { return "0"; }
        }

        public void setCart_total (String cart_total)
        {
            this.cart_total = cart_total;
        }

        public String getCoupon_code ()
        {
            if(coupon_code!=null){ return coupon_code; } else { return ""; }
        }

        public void setCoupon_code (String coupon_code)
        {
            this.coupon_code = coupon_code;
        }

        public String getGrand_total ()
        {
            if(grand_total!=null){ return grand_total; } else { return "0"; }
        }

        public void setGrand_total (String grand_total)
        {
            this.grand_total = grand_total;
        }

        public String getCoupon_value ()
        {
            if(coupon_value!=null){ return coupon_value; } else { return "0"; }
        }

        public void setCoupon_value (String coupon_value)
        {
            this.coupon_value = coupon_value;
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

}
