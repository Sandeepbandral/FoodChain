package com.android42works.magicapp.responses;

public class TimingsResponse {

    private String message;

    private Data data;

    private String success;

    public String getMessage() {
        if (message != null) {
            return message;
        } else {
            return "";
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getSuccess() {
        if (success != null) {
            return success;
        } else {
            return "";
        }
    }

    public void setSuccess(String success) {
        this.success = success;
    }


    /* Data */

    public class Data {

        private Halls[] halls;

        public Halls[] getHalls() {
            return halls;
        }

        public void setHalls(Halls[] halls) {
            this.halls = halls;
        }

    }



    /* Meal Timings */

    public class Meal_timing
    {
        private String id;

        private Timing[] timing;

        private String name;

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public Timing[] getTiming ()
        {
            return timing;
        }

        public void setTiming (Timing[] timing)
        {
            this.timing = timing;
        }

        public String getName ()
        {
            return name;
        }

        public void setName (String name)
        {
            this.name = name;
        }

    }



    /* Halls */

    public class Halls {

        private String id;

        private String name;

        private String logo;

        private Timing[] timing;

        private Meal_timing[] meal_timing;

        public Meal_timing[] getMeal_timings ()
        {
            return meal_timing;
        }

        public void setMeal_timings (Meal_timing[] meal_timing)
        {
            this.meal_timing = meal_timing;
        }

        public Timing[] getTiming ()
        {
            return timing;
        }

        public void setTiming (Timing[] timing)
        {
            this.timing = timing;
        }

        public String getId() {
            if (id != null) {
                return id;
            } else {
                return "";
            }
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            if (name != null) {
                return name;
            } else {
                return "";
            }
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            if (logo != null) {
                return logo;
            } else {
                return "";
            }
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

    }

}
