package com.android42works.magicapp.responses;

public class UserDetailsResponse {

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
        private String id;

        private String role;

        private String phone;

        private String email;

        private String name;

        private String student_id;

        private String stripe_id;

        private String default_card_id;

        private String country;

        private String city;

        public String getRole() {
            if (role != null) {
                return role;
            } else {
                return "";
            }
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getId() {
            if(id!=null){ return id; } else { return ""; }
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone ()
        {
            if(phone!=null){ return phone; } else { return ""; }
        }

        public void setPhone (String phone)
        {
            this.phone = phone;
        }

        public String getEmail ()
        {
            if(email!=null){ return email; } else { return ""; }
        }

        public void setEmail (String email)
        {
            this.email = email;
        }

        public String getName ()
        {
            if(name!=null){ return name; } else { return ""; }
        }

        public void setName (String name)
        {
            this.name = name;
        }

        public String getStripe_id() {
            if(stripe_id!=null){ return stripe_id; } else { return ""; }
        }

        public void setStripe_id(String stripe_id) {
            this.stripe_id = stripe_id;
        }

        public String getDefault_card_id() {
            if(default_card_id!=null){ return default_card_id; } else { return ""; }
        }

        public void setDefault_card_id(String default_card_id) {
            this.default_card_id = default_card_id;
        }

        public String getStudent_id ()
        {
            if(student_id!=null){ return student_id; } else { return ""; }
        }

        public void setStudent_id (String student_id)
        {
            this.student_id = student_id;
        }

        public String getCountry ()
        {
            if(country!=null){ return country; } else { return ""; }
        }

        public void setCountry (String country)
        {
            this.country = country;
        }

        public String getCity ()
        {
            if(city!=null){ return city; } else { return ""; }
        }

        public void setCity (String city)
        {
            this.city = city;
        }

    }

}
