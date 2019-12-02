package com.android42works.magicapp.responses;

public class DishDetailsResponse {

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
        private Dish dish;

        public Dish getDish ()
        {
            return dish;
        }

        public void setDish (Dish dish)
        {
            this.dish = dish;
        }

    }



    /* Dish */

    public class Dish
    {
        private String preparation_time;

        private Addons[] addons;

        private String image;

        private String custom;

        private String status;

        private Customizable_options[] customizable_options;

        private String best_seller;

        private String id;

        private String price;

        private String veg;

        private String hall_id;

        private String[] meal_ids;

        private String[] meal_names;

        private String hall_name;

        private String favorited;

        private String description;

        private String ingredients;

        private String name;

        private String calories;

        private Attributes[] attributes;

        public String[] getMeal_ids ()
        {
            return meal_ids;
        }

        public void setMeal_ids (String[] meal_ids)
        {
            this.meal_ids = meal_ids;
        }

        public String[] getMeal_names ()
        {
            return meal_names;
        }

        public void setMeal_names (String[] meal_names)
        {
            this.meal_names = meal_names;
        }

        public String getStatus() {
            if(status!=null){ return status; } else { return ""; }
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIngredients() {
            if(ingredients!=null){ return ingredients; } else { return ""; }
        }

        public void setIngredients(String ingredients) {
            this.ingredients = ingredients;
        }

        public String getCustom() {
            if(custom!=null){ return custom; } else { return ""; }
        }

        public void setCustom(String custom) {
            this.custom = custom;
        }

        public String getPreparation_time ()
        {
            if(preparation_time!=null){ return preparation_time; } else { return ""; }
        }

        public void setPreparation_time (String preparation_time)
        {
            this.preparation_time = preparation_time;
        }

        public Addons[] getAddons ()
        {
            return addons;
        }

        public void setAddons (Addons[] addons)
        {
            this.addons = addons;
        }

        public String getImage ()
        {
            if(image!=null){ return image; } else { return ""; }
        }

        public void setImage (String image)
        {
            this.image = image;
        }

        public Customizable_options[] getCustomizable_options ()
        {
            return customizable_options;
        }

        public void setCustomizable_options (Customizable_options[] customizable_options)
        {
            this.customizable_options = customizable_options;
        }

        public String getBest_seller ()
        {
            if(best_seller!=null){ return best_seller; } else { return ""; }
        }

        public void setBest_seller (String best_seller)
        {
            this.best_seller = best_seller;
        }

        public String getId ()
        {
            if(id!=null){ return id; } else { return ""; }
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getPrice ()
        {
            if(price!=null){ return price; } else { return ""; }
        }

        public void setPrice (String price)
        {
            this.price = price;
        }

        public String getVeg ()
        {
            if(veg!=null){ return veg; } else { return ""; }
        }

        public void setVeg (String veg)
        {
            this.veg = veg;
        }

        public String getHall_id ()
        {
            if(hall_id!=null){ return hall_id; } else { return ""; }
        }

        public void setHall_id (String hall_id)
        {
            this.hall_id = hall_id;
        }


        public String getHall_name ()
        {
            if(hall_name!=null){ return hall_name; } else { return ""; }
        }

        public void setHall_name (String hall_name)
        {
            this.hall_name = hall_name;
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

        public Attributes[] getAttributes ()
        {
            return attributes;
        }

        public void setAttributes (Attributes[] attributes)
        {
            this.attributes = attributes;
        }

    }



    /* Addons */

    public class Addons
    {
        private String id;

        private String price;

        private String addon;

        public String getId ()
        {
            if(id!=null){ return id; } else { return ""; }
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getPrice ()
        {
            if(price!=null){ return price; } else { return "0"; }
        }

        public void setPrice (String price)
        {
            this.price = price;
        }

        public String getAddon ()
        {
            if(addon!=null){ return addon; } else { return ""; }
        }

        public void setAddon (String addon)
        {
            this.addon = addon;
        }

    }



    /* Customizable_options */

    public class Customizable_options
    {
        private String id;

        private String max_allowed_options;

        private String ingredient;

        private Options[] options;

        public String getId ()
        {
            if(id!=null){ return id; } else { return ""; }
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getMax_allowed_options ()
        {
            if(max_allowed_options!=null){ return max_allowed_options; } else { return ""; }
        }

        public void setMax_allowed_options (String max_allowed_options)
        {
            this.max_allowed_options = max_allowed_options;
        }

        public String getIngredient ()
        {
            if(ingredient!=null){ return ingredient; } else { return ""; }
        }

        public void setIngredient (String ingredient)
        {
            this.ingredient = ingredient;
        }

        public Options[] getOptions ()
        {
            return options;
        }

        public void setOptions (Options[] options)
        {
            this.options = options;
        }

    }



    /* Options */

    public class Options
    {
        private String id;

        private String name;

        public String getId ()
        {
            if(id!=null){ return id; } else { return ""; }
        }

        public void setId (String id)
        {
            this.id = id;
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
