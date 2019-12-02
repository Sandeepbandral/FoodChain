package com.android42works.magicapp.responses;

public class OrderSuccessResponse {

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
        private String status;

        private String start_time;

        private String end_time;

        public String getStatus ()
        {
            if(status!=null){ return status; } else { return ""; }
        }

        public void setStatus (String status)
        {
            this.status = status;
        }

        public String getStart_time() {
            if(start_time!=null){ return start_time; } else { return ""; }
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            if(end_time!=null){ return end_time; } else { return ""; }
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }
    }


}
