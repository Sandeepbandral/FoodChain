package com.android42works.magicapp.responses;

public class ForgotPassResponse {

    private String message;

    private String[] data;

    private String success;

    public String getMessage ()
    {
        if(message!=null){ return message; } else { return ""; }
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String[] getData ()
    {
        if(data!=null){ return data; } else { return new String[0]; }
    }

    public void setData (String[] data)
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

}
