package com.android42works.magicapp.responses;

public class TermsResponse {

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
        private Page page;

        public Page getPage ()
        {
            return page;
        }

        public void setPage (Page page)
        {
            this.page = page;
        }

    }


    /* Pages*/

    public class Page
    {
        private String html_data;

        private String id;

        private String title;

        private String slug;

        public String getHtml_data ()
        {
            if(html_data!=null){ return html_data; } else { return ""; }
        }

        public void setHtml_data (String html_data)
        {
            this.html_data = html_data;
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

        public String getSlug ()
        {
            if(slug!=null){ return slug; } else { return ""; }
        }

        public void setSlug (String slug)
        {
            this.slug = slug;
        }

    }


}
