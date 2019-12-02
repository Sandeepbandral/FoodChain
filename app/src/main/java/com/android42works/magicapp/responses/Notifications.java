package com.android42works.magicapp.responses;

public class Notifications
{

    private String message;

    private String id;

    private String title;

    private Data data;

    private String updated_at;

    private String notification_type;

    private String hall_id;

    private String student_id;

    private String created_at;

    private String is_read;

    public String getMessage ()
    {
        if(message!=null){ return message; } else { return ""; }
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getId ()
    {
        if(id!=null){ return id; } else { return ""; }
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getTitle ()
    {
        if(title!=null){ return title; } else { return ""; }
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getUpdated_at ()
    {
        if(updated_at!=null){ return updated_at; } else { return ""; }
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getNotification_type ()
    {
        if(notification_type!=null){ return notification_type; } else { return ""; }
    }

    public Data getData ()
    {
        return data;
    }

    public void setData (Data data)
    {
        this.data = data;
    }

    public void setNotification_type (String notification_type)
    {
        this.notification_type = notification_type;
    }

    public String getHall_id ()
    {
        if(hall_id!=null){ return hall_id; } else { return ""; }
    }

    public void setHall_id (String hall_id)
    {
        this.hall_id = hall_id;
    }

    public String getStudent_id ()
    {
        if(student_id!=null){ return student_id; } else { return ""; }
    }

    public void setStudent_id (String student_id)
    {
        this.student_id = student_id;
    }

    public String getCreated_at ()
    {
        if(created_at!=null){ return created_at; } else { return ""; }
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getIs_read ()
    {
        if(is_read!=null){ return is_read; } else { return ""; }
    }

    public void setIs_read (String is_read)
    {
        this.is_read = is_read;
    }





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

