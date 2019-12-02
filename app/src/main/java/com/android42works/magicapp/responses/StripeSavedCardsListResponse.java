package com.android42works.magicapp.responses;

public class StripeSavedCardsListResponse {

    private Data[] data;

    private String has_more;

    private String object;

    private String url;

    public Data[] getData ()
    {
        return data;
    }

    public void setData (Data[] data)
    {
        this.data = data;
    }

    public String getHas_more ()
    {
        if(null!=has_more){ return has_more; } else { return ""; }
    }

    public void setHas_more (String has_more)
    {
        this.has_more = has_more;
    }

    public String getObject ()
    {
        if(null!=object){ return object; } else { return ""; }
    }

    public void setObject (String object)
    {
        this.object = object;
    }

    public String getUrl ()
    {
        if(null!=url){ return url; } else { return ""; }
    }

    public void setUrl (String url)
    {
        this.url = url;
    }


    /* Data */

    public class Data
    {
        private String address_zip_check;

        private String exp_year;

        private String exp_month;

        private String funding;

        private String cvc_check;

        private String address_country;

        private String object;

        private String tokenization_method;

        private String customer;

        private String address_city;

        private String address_zip;

        private String address_state;

        private String country;

        private String id;

        private String fingerprint;

        private String address_line1;

        private String address_line2;

        private String name;

        private String address_line1_check;

        private String brand;

        private String last4;

        private String dynamic_last4;

        public String getAddress_zip_check ()
        {
            if(null!=address_zip_check){ return address_zip_check; } else { return ""; }
        }

        public void setAddress_zip_check (String address_zip_check)
        {
            this.address_zip_check = address_zip_check;
        }

        public String getExp_year ()
        {
            if(null!=exp_year){ return exp_year; } else { return ""; }
        }

        public void setExp_year (String exp_year)
        {
            this.exp_year = exp_year;
        }

        public String getExp_month ()
        {
            if(null!=exp_month){ return exp_month; } else { return ""; }
        }

        public void setExp_month (String exp_month)
        {
            this.exp_month = exp_month;
        }

        public String getFunding ()
        {
            if(null!=funding){ return funding; } else { return ""; }
        }

        public void setFunding (String funding)
        {
            this.funding = funding;
        }

        public String getCvc_check ()
        {
            if(null!=cvc_check){ return cvc_check; } else { return ""; }
        }

        public void setCvc_check (String cvc_check)
        {
            this.cvc_check = cvc_check;
        }

        public String getAddress_country ()
        {
            if(null!=address_country){ return address_country; } else { return ""; }
        }

        public void setAddress_country (String address_country)
        {
            this.address_country = address_country;
        }

        public String getObject ()
        {
            if(null!=object){ return object; } else { return ""; }
        }

        public void setObject (String object)
        {
            this.object = object;
        }

        public String getTokenization_method ()
        {
            if(null!=tokenization_method){ return tokenization_method; } else { return ""; }
        }

        public void setTokenization_method (String tokenization_method)
        {
            this.tokenization_method = tokenization_method;
        }

        public String getCustomer ()
        {
            if(null!=customer){ return customer; } else { return ""; }
        }

        public void setCustomer (String customer)
        {
            this.customer = customer;
        }

        public String getAddress_city ()
        {
            if(null!=address_city){ return address_city; } else { return ""; }
        }

        public void setAddress_city (String address_city)
        {
            this.address_city = address_city;
        }

        public String getAddress_zip ()
        {
            if(null!=address_zip){ return address_zip; } else { return ""; }
        }

        public void setAddress_zip (String address_zip)
        {
            this.address_zip = address_zip;
        }

        public String getAddress_state ()
        {
            if(null!=address_state){ return address_state; } else { return ""; }
        }

        public void setAddress_state (String address_state)
        {
            this.address_state = address_state;
        }

        public String getCountry ()
        {
            if(null!=country){ return country; } else { return ""; }
        }

        public void setCountry (String country)
        {
            this.country = country;
        }

        public String getId ()
        {
            if(null!=id){ return id; } else { return ""; }
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getFingerprint ()
        {
            if(null!=fingerprint){ return fingerprint; } else { return ""; }
        }

        public void setFingerprint (String fingerprint)
        {
            this.fingerprint = fingerprint;
        }

        public String getAddress_line1 ()
        {
            if(null!=address_line1){ return address_line1; } else { return ""; }
        }

        public void setAddress_line1 (String address_line1)
        {
            this.address_line1 = address_line1;
        }

        public String getAddress_line2 ()
        {
            if(null!=address_line2){ return address_line2; } else { return ""; }
        }

        public void setAddress_line2 (String address_line2)
        {
            this.address_line2 = address_line2;
        }

        public String getName ()
        {
            if(null!=name){ return name; } else { return ""; }
        }

        public void setName (String name)
        {
            this.name = name;
        }

        public String getAddress_line1_check ()
        {
            if(null!=address_line1_check){ return address_line1_check; } else { return ""; }
        }

        public void setAddress_line1_check (String address_line1_check)
        {
            this.address_line1_check = address_line1_check;
        }

        public String getBrand ()
        {
            if(null!=brand){ return brand; } else { return ""; }
        }

        public void setBrand (String brand)
        {
            this.brand = brand;
        }

        public String getLast4 ()
        {
            if(null!=last4){ return last4; } else { return ""; }
        }

        public void setLast4 (String last4)
        {
            this.last4 = last4;
        }

        public String getDynamic_last4 ()
        {
            if(null!=dynamic_last4){ return dynamic_last4; } else { return ""; }
        }

        public void setDynamic_last4 (String dynamic_last4)
        {
            this.dynamic_last4 = dynamic_last4;
        }

    }

}
