package com.android42works.magicapp.responses;

public class NewNutritionalResponse {

    private String message;

    private Data data;

    private String success;

    public String getMessage() {
        return message;
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
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public class Data {

        private String custom;

        private Nutritions nutritions;


        public String getCustom ()
        {
            if(null!=custom){ return custom; } else { return ""; }
        }

        public void setCustom (String custom)
        {
            this.custom = custom;
        }

        public Nutritions getNutritions() {
            return nutritions;
        }

        public void setNutritions(Nutritions nutritions) {
            this.nutritions = nutritions;
        }

    }

    public class Suggested_serving_size
    {
        private String value;

        private String type;

        public String getValue ()
        {
            if(null!=value){ return value; } else { return ""; }
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            if(null!=type){ return type; } else { return ""; }
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Nutritions {

        private String calories;

        private String calories_from_fat;

        private Total_fat total_fat;

        private Total_fat_perc total_fat_perc;

        private Saturated_fat saturated_fat;

        private Saturated_fat_perc saturated_fat_perc;

        private Trans_fat trans_fat;

        private Cholestrol cholestrol;

        private Cholestrol_perc cholestrol_perc;

        private Sodium sodium;

        private Sodium_perc sodium_perc;

        private Total_carb total_carb;

        private Total_carb_perc total_carb_perc;

        private Dietary_fiber dietary_fiber;

        private Dietary_fiber_perc dietary_fiber_perc;

        private Sugars sugars;

        private Protein protein;

        private Protein_perc protein_perc;

        private Vitamin_a vitamin_a;

        private Vitamin_c vitamin_c;

        private Calcium calcium;

        private Iron iron;

        private Suggested_serving_size suggested_serving_size;

        private String serving_per_container;

        public Suggested_serving_size getSuggested_serving_size ()
        {
            return suggested_serving_size;
        }

        public void setSuggested_serving_size (Suggested_serving_size suggested_serving_size)
        {
            this.suggested_serving_size = suggested_serving_size;
        }

        public String getServing_per_container ()
        {
            if(null!=serving_per_container){ return serving_per_container; } else { return ""; }
        }

        public void setServing_per_container (String serving_per_container) {
            this.serving_per_container = serving_per_container;
        }


        public String getCalories() {
            return calories;
        }

        public void setCalories(String calories) {
            this.calories = calories;
        }

        public String getCalories_from_fat() {
            return calories_from_fat;
        }

        public void setCalories_from_fat(String calories_from_fat) {
            this.calories_from_fat = calories_from_fat;
        }

        public Total_fat getTotal_fat() {
            return total_fat;
        }

        public void setTotal_fat(Total_fat total_fat) {
            this.total_fat = total_fat;
        }

        public Total_fat_perc getTotal_fat_perc() {
            return total_fat_perc;
        }

        public void setTotal_fat_perc(Total_fat_perc total_fat_perc) {
            this.total_fat_perc = total_fat_perc;
        }

        public Saturated_fat getSaturated_fat() {
            return saturated_fat;
        }

        public void setSaturated_fat(Saturated_fat saturated_fat) {
            this.saturated_fat = saturated_fat;
        }

        public Saturated_fat_perc getSaturated_fat_perc() {
            return saturated_fat_perc;
        }

        public void setSaturated_fat_perc(Saturated_fat_perc saturated_fat_perc) {
            this.saturated_fat_perc = saturated_fat_perc;
        }

        public Trans_fat getTrans_fat() {
            return trans_fat;
        }

        public void setTrans_fat(Trans_fat trans_fat) {
            this.trans_fat = trans_fat;
        }

        public Cholestrol getCholestrol() {
            return cholestrol;
        }

        public void setCholestrol(Cholestrol cholestrol) {
            this.cholestrol = cholestrol;
        }

        public Cholestrol_perc getCholestrol_perc() {
            return cholestrol_perc;
        }

        public void setCholestrol_perc(Cholestrol_perc cholestrol_perc) {
            this.cholestrol_perc = cholestrol_perc;
        }

        public Sodium getSodium() {
            return sodium;
        }

        public void setSodium(Sodium sodium) {
            this.sodium = sodium;
        }

        public Sodium_perc getSodium_perc() {
            return sodium_perc;
        }

        public void setSodium_perc(Sodium_perc sodium_perc) {
            this.sodium_perc = sodium_perc;
        }

        public Total_carb getTotal_carb() {
            return total_carb;
        }

        public void setTotal_carb(Total_carb total_carb) {
            this.total_carb = total_carb;
        }

        public Total_carb_perc getTotal_carb_perc() {
            return total_carb_perc;
        }

        public void setTotal_carb_perc(Total_carb_perc total_carb_perc) {
            this.total_carb_perc = total_carb_perc;
        }

        public Dietary_fiber getDietary_fiber() {
            return dietary_fiber;
        }

        public void setDietary_fiber(Dietary_fiber dietary_fiber) {
            this.dietary_fiber = dietary_fiber;
        }

        public Dietary_fiber_perc getDietary_fiber_perc() {
            return dietary_fiber_perc;
        }

        public void setDietary_fiber_perc(Dietary_fiber_perc dietary_fiber_perc) {
            this.dietary_fiber_perc = dietary_fiber_perc;
        }

        public Sugars getSugars() {
            return sugars;
        }

        public void setSugars(Sugars sugars) {
            this.sugars = sugars;
        }

        public Protein getProtein() {
            return protein;
        }

        public void setProtein(Protein protein) {
            this.protein = protein;
        }

        public Protein_perc getProtein_perc() {
            return protein_perc;
        }

        public void setProtein_perc(Protein_perc protein_perc) {
            this.protein_perc = protein_perc;
        }

        public Vitamin_a getVitamin_a() {
            return vitamin_a;
        }

        public void setVitamin_a(Vitamin_a vitamin_a) {
            this.vitamin_a = vitamin_a;
        }

        public Vitamin_c getVitamin_c() {
            return vitamin_c;
        }

        public void setVitamin_c(Vitamin_c vitamin_c) {
            this.vitamin_c = vitamin_c;
        }

        public Calcium getCalcium() {
            return calcium;
        }

        public void setCalcium(Calcium calcium) {
            this.calcium = calcium;
        }

        public Iron getIron() {
            return iron;
        }

        public void setIron(Iron iron) {
            this.iron = iron;
        }

    }

    public class Total_fat
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Total_fat_perc
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Saturated_fat
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Saturated_fat_perc
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Trans_fat
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Cholestrol
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Cholestrol_perc
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Sodium
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Sodium_perc
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }


    public class Total_carb
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Total_carb_perc
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Dietary_fiber
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Dietary_fiber_perc
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Sugars
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Protein
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Protein_perc
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Vitamin_a
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Vitamin_c
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Calcium
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

    public class Iron
    {
        private String value;

        private String type;

        public String getValue ()
        {
            return value;
        }

        public void setValue (String value)
        {
            this.value = value;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

    }

}
