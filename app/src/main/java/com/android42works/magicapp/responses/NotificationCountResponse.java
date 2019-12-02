package com.android42works.magicapp.responses;

/**
 * Created by apple on 09/10/18.
 */

public class NotificationCountResponse {

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

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", data = "+data+", success = "+success+"]";
    }

    public class Data
    {
        private String notification_count;

        public String getNotification_count ()
        {
            return notification_count;
        }

        public void setNotification_count (String notification_count)
        {
            this.notification_count = notification_count;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [notification_count = "+notification_count+"]";
        }
    }

}
