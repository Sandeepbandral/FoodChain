package com.android42works.magicapp.responses;

public class HallMenuResponse {

    private String message;

    private Data data;

    private String success;

    public String getMessage ()
    {
        if(message!=null){ return message; } else  { return ""; }
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
        if(success!=null){ return success; } else  { return ""; }
    }

    public void setSuccess (String success)
    {
        this.success = success;
    }


    /* Data */

    public class Data
    {

        private Filters filters;

        private Categories[] categories;

        public Filters getFilters() {
            return filters;
        }

        public void setFilters(Filters filters) {
            this.filters = filters;
        }

        public Categories[] getCategories ()
        {
            return categories;
        }

        public void setCategories (Categories[] categories)
        {
            this.categories = categories;
        }

    }

    /* Filters */

    public class Filters {

        private Cuisines[] cuisines;

        private Dietary[] dietary;

        private String[] sort;

        public Cuisines[] getCuisines() {
            return cuisines;
        }

        public void setCuisines(Cuisines[] cuisines) {
            this.cuisines = cuisines;
        }

        public Dietary[] getDietary() {
            return dietary;
        }

        public void setDietary(Dietary[] dietary) {
            this.dietary = dietary;
        }

        public String[] getSort() {
            if (sort != null) {
                return sort;
            } else {
                return (new String[0]);
            }
        }

        public void setSort(String[] sort) {
            this.sort = sort;
        }

    }


    /* Dietary */

    public class Dietary {
        private String id;

        private String name;

        private Options[] options;

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

        public Options[] getOptions() {
            return options;
        }

        public void setOptions(Options[] options) {
            this.options = options;
        }

    }


    /* Options */

    public class Options {
        private String id;

        private String name;

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

    }


    /* Cuisines */

    public class Cuisines {
        private String id;

        private String name;

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

    }


    /* Categories */

    public class Categories
    {
        private String id;

        private String name;

        private Dishes[] dishes;

        public String getId ()
        {
            if(id!=null){ return id; } else  { return ""; }
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getName ()
        {
            if(name!=null){ return name; } else  { return ""; }
        }

        public void setName (String name)
        {
            this.name = name;
        }

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

        private String image;

        private String[] meal_ids;

        private Attributes[] attributes;

        private String best_seller;

        private String status;

        private String isImageLoaded;

        public String[] getMeal_ids() {
            if(meal_ids!=null){ return meal_ids; } else  { return new String[0]; }
        }

        public void setMeal_ids(String[] meal_ids) {
            this.meal_ids = meal_ids;
        }

        public String getIsImageLoaded() {
            if(isImageLoaded!=null){ return isImageLoaded; } else  { return "false"; }
        }

        public void setIsImageLoaded(String isImageLoaded) {
            this.isImageLoaded = isImageLoaded;
        }

        public String getStatus() {
            if(status!=null){ return status; } else  { return ""; }
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPreparation_time ()
        {
            if(preparation_time!=null){ return preparation_time; } else  { return ""; }
        }

        public void setPreparation_time (String preparation_time)
        {
            this.preparation_time = preparation_time;
        }

        public String getId ()
        {
            if(id!=null){ return id; } else  { return ""; }
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getVeg ()
        {
            if(veg!=null){ return veg; } else  { return ""; }
        }

        public void setVeg (String veg)
        {
            this.veg = veg;
        }

        public String getPrice ()
        {
            if(price!=null){ return price; } else  { return ""; }
        }

        public void setPrice (String price)
        {
            this.price = price;
        }

        public String getFavorited ()
        {
            if(favorited!=null){ return favorited; } else  { return ""; }
        }

        public void setFavorited (String favorited)
        {
            this.favorited = favorited;
        }

        public String getDescription ()
        {
            if(description!=null){ return description; } else  { return ""; }
        }

        public void setDescription (String description)
        {
            this.description = description;
        }

        public String getName ()
        {
            if(name!=null){ return name; } else  { return ""; }
        }

        public void setName (String name)
        {
            this.name = name;
        }

        public String getCalories ()
        {
            if(calories!=null){ return calories; } else  { return ""; }
        }

        public void setCalories (String calories)
        {
            this.calories = calories;
        }

        public String getImage ()
        {
            if(image!=null){ return image; } else  { return ""; }
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
            if(best_seller!=null){ return best_seller; } else  { return ""; }
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
            if(id!=null){ return id; } else  { return ""; }
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
            if(name!=null){ return name; } else  { return ""; }
        }

        public void setName (String name)
        {
            this.name = name;
        }

    }

}
