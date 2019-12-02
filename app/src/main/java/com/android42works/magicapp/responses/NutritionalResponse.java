package com.android42works.magicapp.responses;

public class NutritionalResponse {

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

        private Nutrition nutrition;

        public String getCustom ()
        {
            if(null!=custom){ return custom; } else { return ""; }
        }

        public void setCustom (String custom)
        {
            this.custom = custom;
        }

        public Nutrition getNutrition() {
            return nutrition;
        }

        public void setNutrition(Nutrition nutrition) {
            this.nutrition = nutrition;
        }

    }

    public class Nutrition {


        private Calories calories;

        private CaloriesFromFat caloriesFromFat;

        private TotalFat totalFat;

        private SaturatedFat saturatedFat;

        private TransFat transFat;

        private Cholestrol cholestrol;

        private Sodium sodium;

        private Carbohydrate carbohydrate;

        private DietaryFiber dietaryFiber;

        private Sugars sugars;

        private Protein protein;

        private VitaminA vitaminA;

        private VitaminC vitaminC;

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

        public CaloriesFromFat getCaloriesFromFat ()
        {
            return caloriesFromFat;
        }

        public void setCaloriesFromFat (CaloriesFromFat caloriesFromFat)
        {
            this.caloriesFromFat = caloriesFromFat;
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

        public Calories getCalories() {
            return calories;
        }

        public void setCalories(Calories calories) {
            this.calories = calories;
        }

        public TotalFat getTotalFat() {
            return totalFat;
        }

        public void setTotalFat(TotalFat totalFat) {
            this.totalFat = totalFat;
        }

        public SaturatedFat getSaturatedFat() {
            return saturatedFat;
        }

        public void setSaturatedFat(SaturatedFat saturatedFat) {
            this.saturatedFat = saturatedFat;
        }

        public TransFat getTransFat() {
            return transFat;
        }

        public void setTransFat(TransFat transFat) {
            this.transFat = transFat;
        }

        public Cholestrol getCholestrol() {
            return cholestrol;
        }

        public void setCholestrol(Cholestrol cholestrol) {
            this.cholestrol = cholestrol;
        }

        public Sodium getSodium() {
            return sodium;
        }

        public void setSodium(Sodium sodium) {
            this.sodium = sodium;
        }

        public Carbohydrate getCarbohydrate() {
            return carbohydrate;
        }

        public void setCarbohydrate(Carbohydrate carbohydrate) {
            this.carbohydrate = carbohydrate;
        }

        public DietaryFiber getDietaryFiber() {
            return dietaryFiber;
        }

        public void setDietaryFiber(DietaryFiber dietaryFiber) {
            this.dietaryFiber = dietaryFiber;
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

        public VitaminA getVitaminA() {
            return vitaminA;
        }

        public void setVitaminA(VitaminA vitaminA) {
            this.vitaminA = vitaminA;
        }

        public VitaminC getVitaminC() {
            return vitaminC;
        }

        public void setVitaminC(VitaminC vitaminC) {
            this.vitaminC = vitaminC;
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

    public class CaloriesFromFat
    {
        private String amount;

        private String unit;

        public String getAmount ()
        {
            return amount;
        }

        public void setAmount (String amount)
        {
            this.amount = amount;
        }

        public String getUnit ()
        {
            return unit;
        }

        public void setUnit (String unit)
        {
            this.unit = unit;
        }

    }

    public class Calories {

        private String amount;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

    }

    public class TotalFat {
        private String dailyValFactor;

        private String amount;

        private String unit;

        private String dailyValue;

        public String getDailyValFactor() {
            return dailyValFactor;
        }

        public void setDailyValFactor(String dailyValFactor) {
            this.dailyValFactor = dailyValFactor;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getDailyValue() {
            return dailyValue;
        }

        public void setDailyValue(String dailyValue) {
            this.dailyValue = dailyValue;
        }

    }

    public class SaturatedFat {
        private String dailyValFactor;

        private String amount;

        private String dailyValue;

        public String getDailyValFactor() {
            return dailyValFactor;
        }

        public void setDailyValFactor(String dailyValFactor) {
            this.dailyValFactor = dailyValFactor;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getDailyValue() {
            return dailyValue;
        }

        public void setDailyValue(String dailyValue) {
            this.dailyValue = dailyValue;
        }

    }

    public class TransFat {
        private String amount;

        private String unit;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

    }

    public class Cholestrol {
        private String dailyValFactor;

        private String amount;

        private String unit;

        private String dailyValue;

        public String getDailyValFactor() {
            return dailyValFactor;
        }

        public void setDailyValFactor(String dailyValFactor) {
            this.dailyValFactor = dailyValFactor;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getDailyValue() {
            return dailyValue;
        }

        public void setDailyValue(String dailyValue) {
            this.dailyValue = dailyValue;
        }

    }

    public class Sodium {
        private String dailyValFactor;

        private String amount;

        private String unit;

        private String dailyValue;

        public String getDailyValFactor() {
            return dailyValFactor;
        }

        public void setDailyValFactor(String dailyValFactor) {
            this.dailyValFactor = dailyValFactor;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getDailyValue() {
            return dailyValue;
        }

        public void setDailyValue(String dailyValue) {
            this.dailyValue = dailyValue;
        }

    }

    public class Carbohydrate {
        private String dailyValFactor;

        private String amount;

        private String unit;

        private String dailyValue;

        public String getDailyValFactor() {
            return dailyValFactor;
        }

        public void setDailyValFactor(String dailyValFactor) {
            this.dailyValFactor = dailyValFactor;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getDailyValue() {
            return dailyValue;
        }

        public void setDailyValue(String dailyValue) {
            this.dailyValue = dailyValue;
        }

    }

    public class DietaryFiber {
        private String dailyValFactor;

        private String amount;

        private String unit;

        private String dailyValue;

        public String getDailyValFactor() {
            return dailyValFactor;
        }

        public void setDailyValFactor(String dailyValFactor) {
            this.dailyValFactor = dailyValFactor;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getDailyValue() {
            return dailyValue;
        }

        public void setDailyValue(String dailyValue) {
            this.dailyValue = dailyValue;
        }

    }

    public class Sugars {
        private String amount;

        private String unit;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

    }

    public class Protein {
        private String amount;

        private String unit;

        public String getDailyValue() {
            return dailyValue;
        }

        public void setDailyValue(String dailyValue) {
            this.dailyValue = dailyValue;
        }

        private String dailyValue;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

    }

    public class VitaminA {
        private String dailyValFactor;

        private String amount;

        private String unit;

        private String dailyValue;

        public String getDailyValFactor() {
            return dailyValFactor;
        }

        public void setDailyValFactor(String dailyValFactor) {
            this.dailyValFactor = dailyValFactor;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getDailyValue() {
            return dailyValue;
        }

        public void setDailyValue(String dailyValue) {
            this.dailyValue = dailyValue;
        }

    }

    public class VitaminC {
        private String dailyValFactor;

        private String amount;

        private String unit;

        private String dailyValue;

        public String getDailyValFactor() {
            return dailyValFactor;
        }

        public void setDailyValFactor(String dailyValFactor) {
            this.dailyValFactor = dailyValFactor;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getDailyValue() {
            return dailyValue;
        }

        public void setDailyValue(String dailyValue) {
            this.dailyValue = dailyValue;
        }

    }

    public class Calcium {
        private String dailyValFactor;

        private String amount;

        private String unit;

        private String dailyValue;

        public String getDailyValFactor() {
            return dailyValFactor;
        }

        public void setDailyValFactor(String dailyValFactor) {
            this.dailyValFactor = dailyValFactor;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getDailyValue() {
            return dailyValue;
        }

        public void setDailyValue(String dailyValue) {
            this.dailyValue = dailyValue;
        }

    }

    public class Iron {
        private String dailyValFactor;

        private String amount;

        private String unit;

        private String dailyValue;

        public String getDailyValFactor() {
            return dailyValFactor;
        }

        public void setDailyValFactor(String dailyValFactor) {
            this.dailyValFactor = dailyValFactor;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getDailyValue() {
            return dailyValue;
        }

        public void setDailyValue(String dailyValue) {
            this.dailyValue = dailyValue;
        }

    }

}
