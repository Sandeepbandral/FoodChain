package com.android42works.magicapp.responses;

public class SearchResponse {

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
        private Dishes[] dishes;

        public Dishes[] getDishes ()
        {
            return dishes;
        }

        public void setDishes (Dishes[] dishes)
        {
            this.dishes = dishes;
        }

    }


    /* Dishes */

    public class Dishes
    {
        private String preparation_time;

        private String id;

        private String veg;

        private String price;

        private String favorited;

        private String description;

        private String name;

        private String calories;

        private String hall_name;

        private String image;

        private Attributes[] attributes;

        private String best_seller;

        private String status;

        private String isImageLoaded;

        public String getIsImageLoaded() {
            if(isImageLoaded!=null){ return isImageLoaded; } else { return "false"; }
        }

        public void setIsImageLoaded(String isImageLoaded) {
            this.isImageLoaded = isImageLoaded;
        }

        public String getStatus() {
            if(status!=null){ return status; } else { return ""; }
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getHall_name() {
            if(hall_name!=null){ return hall_name; } else { return ""; }
        }

        public void setHall_name(String hall_name) {
            this.hall_name = hall_name;
        }

        public String getPreparation_time ()
        {
            if(preparation_time!=null){ return preparation_time; } else { return ""; }
        }

        public void setPreparation_time (String preparation_time)
        {
            this.preparation_time = preparation_time;
        }

        public String getId ()
        {
            if(id!=null){ return id; } else { return ""; }
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getVeg ()
        {
            if(veg!=null){ return veg; } else { return ""; }
        }

        public void setVeg (String veg)
        {
            this.veg = veg;
        }

        public String getPrice ()
        {
            if(price!=null){ return price; } else { return ""; }
        }

        public void setPrice (String price)
        {
            this.price = price;
        }

        public String getFavorited ()
        {
            if(favorited!=null){ return favorited; } else { return ""; }
        }

        public void setFavorited (String favorited)
        {
            this.favorited = favorited;
        }

        public String getDescription ()
        {
            if(description!=null){ return description; } else { return ""; }
        }

        public void setDescription (String description)
        {
            this.description = description;
        }

        public String getName ()
        {
            if(name!=null){ return name; } else { return ""; }
        }

        public void setName (String name)
        {
            this.name = name;
        }

        public String getCalories ()
        {
            if(calories!=null){ return calories; } else { return ""; }
        }

        public void setCalories (String calories)
        {
            this.calories = calories;
        }

        public String getImage ()
        {
            if(image!=null){ return image; } else { return ""; }
        }

        public void setImage (String image)
        {
            this.image = image;
        }

        public Attributes[] getAttributes ()
        {
            return attributes;
        }

        public void setAttributes (Attributes[] attributes)
        {
            this.attributes = attributes;
        }

        public String getBest_seller ()
        {
            if(best_seller!=null){ return best_seller; } else { return ""; }
        }

        public void setBest_seller (String best_seller)
        {
            this.best_seller = best_seller;
        }

    }


    /* Attributes */

    public class Attributes
    {
        private String id;

        private String[] values;

        private String name;

        public String getId ()
        {
            if(id!=null){ return id; } else { return ""; }
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String[] getValues ()
        {
            return values;
        }

        public void setValues (String[] values)
        {
            this.values = values;
        }

        public String getName ()
        {
            if(name!=null){ return name; } else { return ""; }
        }

        public void setName (String name)
        {
            this.name = name;
        }

    }


}
