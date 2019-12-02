package com.android42works.magicapp.responses;

public class LocalCartTaxResponse {

    private String message;

    private Data data;

    private String success;

    public String getMessage ()
    {
        return message;
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
        return success;
    }

    public void setSuccess (String success)
    {
        this.success = success;
    }


    /* Data */
    public class Data
    {
        private Coupon_data coupon_data;

        private Tax_data[] tax_data;

        public Coupon_data getCoupon_data ()
        {
            return coupon_data;
        }

        public void setCoupon_data (Coupon_data coupon_data)
        {
            this.coupon_data = coupon_data;
        }

        public Tax_data[] getTax_data ()
        {
            return tax_data;
        }

        public void setTax_data (Tax_data[] tax_data)
        {
            this.tax_data = tax_data;
        }

    }

    /* Coupon Data*/

    public class Coupon_data
    {
        private String coupon_code;

        private String grand_total;

        private String discount;

        private String status;

        private String errors;

        public String getCoupon_code ()
        {
            if(null!=coupon_code){ return coupon_code; }else { return ""; }
        }

        public void setCoupon_code (String coupon_code)
        {
            this.coupon_code = coupon_code;
        }

        public String getGrand_total ()
        {
            if(null!=grand_total){ return grand_total; }else { return ""; }
        }

        public void setGrand_total (String grand_total)
        {
            this.grand_total = grand_total;
        }

        public String getDiscount ()
        {
            if(null!=discount){ return discount; }else { return ""; }
        }

        public void setDiscount (String discount)
        {
            this.discount = discount;
        }

        public String getStatus() {
            if(null!=status){ return status; }else { return ""; }
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getErrors() {
            if(null!=errors){ return errors; }else { return ""; }
        }

        public void setErrors(String errors) {
            this.errors = errors;
        }
    }

    /* Tax Data*/

    public class Tax_data
    {

        private String title;

        private String percentage;

        public String getTitle ()
        {
            return title;
        }

        public void setTitle (String title)
        {
            this.title = title;
        }

        public String getPercentage ()
        {
            return percentage;
        }

        public void setPercentage (String percentage)
        {
            this.percentage = percentage;
        }

    }

}
