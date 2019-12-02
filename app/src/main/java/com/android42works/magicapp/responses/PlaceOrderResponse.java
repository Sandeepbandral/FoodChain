package com.android42works.magicapp.responses;

public class PlaceOrderResponse {

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

        private String order_id;

        public String getOrder_id ()
        {
            if(order_id!=null){ return order_id; } else { return ""; }
        }

        public void setOrder_id (String order_id)
        {
            this.order_id = order_id;
        }

    }

}
