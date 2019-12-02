package com.android42works.magicapp.responses;

public class NotificationsResponse {


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
        private Notifications[] notifications;

        public Notifications[] getNotifications ()
        {
            return notifications;
        }

        public void setNotifications (Notifications[] notifications)
        {
            this.notifications = notifications;
        }

    }

}
