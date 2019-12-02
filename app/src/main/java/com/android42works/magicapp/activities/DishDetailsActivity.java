package com.android42works.magicapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.adapters.DishCustParentAdapter;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.cart.Addons;
import com.android42works.magicapp.cart.Items;
import com.android42works.magicapp.cart.Options;
import com.android42works.magicapp.dialogs.FutureTimingsDialog;
import com.android42works.magicapp.interfaces.AddToCartInterface;
import com.android42works.magicapp.interfaces.DishDetailsInterface;
import com.android42works.magicapp.models.DishCustChildModel;
import com.android42works.magicapp.models.DishCustParentModel;
import com.android42works.magicapp.presenters.AddToCartPresenter;
import com.android42works.magicapp.presenters.DishDetailsPresenter;
import com.android42works.magicapp.responses.DishDetailsResponse;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.android42works.magicapp.responses.NewNutritionalResponse;
import com.android42works.magicapp.responses.NutritionalResponse;
import com.android42works.magicapp.utils.AppUtils;
import com.android42works.magicapp.utils.Netwatcher;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/* Created by JSP@nesar */

public class DishDetailsActivity extends BaseActivity implements DishDetailsInterface, AddToCartInterface, Netwatcher.NetwatcherListener {

    private TextView txt_actionbar_title, txt_name, txt_calorie, txt_allergie, txt_cost, txt_quantity, txt_customisations,
            add_to_cart_txt, total_price_txt;
    private ImageView img_veg_nonveg, img_dish;
    private DishDetailsPresenter dishDetailsPresenter;
    private AddToCartPresenter addToCartPresenter;
    private RecyclerView recycler_customization;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private ArrayList<DishCustParentModel> customizationList = new ArrayList<>();
    private DishCustParentAdapter dishCustParentAdapter;
    private NestedScrollView nestedScrollView;
    private RelativeLayout rl_description, rl_customization;
    private LinearLayout ll_customization_sub;
    private String[] mealIdArray, mealNameArray;
    private String dishId = "", mealIds = "", mealNames = "", dishName = "", hallId = "",itemId = "", dishPrice = "", hallName = "",
            pickUpDate = "", pickUpTime = "", todayDate = "", hallOpeningTime = "00:00 AM", hallClosingTime = "11:59 PM";
    private int quantity = 1, setDayFlag = 0;;
    private DishDetailsResponse dishDetailsResponse;
    private RelativeLayout btn_addtocart;
    private boolean isDishOpenedFromCart = false;
    private float totalItemsPrice, getAddOnPrice;
    private View view_nointernet;
    private Netwatcher netwatcher;
    private boolean isHallClosed = true, isDishAvailable = false, isMealTimingClosed = true;
    private TextView txt_serving_size, txt_serving_per_container, txt_cal, txt_cal_from_fat, txt_total_fat_val, txt_total_fat_per, txt_saturted_fat_val, txt_saturted_fat_per,
            txt_trans_fat_val, txt_cholesterol_val, txt_cholesterol_per, txt_sodium_val, txt_sodium_per, txt_carbohydrate_val,
            txt_carbohydrate_per, txt_diatery_fat_val, txt_diatery_per, txt_total_sugars_val, txt_protein_val, txt_protein_per, txt_vitamin_a_val, txt_vitamin_a_per, txt_vitamin_c_val,
            txt_vitamin_c_per, txt_calcium_val, txt_calcium_per, txt_iron_val, txt_iron_per,
            txt_dish_desc, txt_dish_desc_val;
    private int isNutritionalShow = 0;
    private LinearLayout llNutritional;
    private ImageView imgArrow;
    private boolean isNewDish = false;

    /* For Update */

    private Addons[] updateCartAddons;
    private Options[] updateCartOptions;

    @Override
    protected int getLayoutView() {
        return R.layout.act_dish_details;
    }

    @Override
    protected Context getActivityContext() {
        return DishDetailsActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);
        txt_name = findViewById(R.id.txt_name);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        txt_calorie = findViewById(R.id.txt_calorie);
        txt_allergie = findViewById(R.id.txt_allergie);
        txt_cost = findViewById(R.id.txt_cost);
        txt_quantity = findViewById(R.id.txt_quantity);
        img_veg_nonveg = findViewById(R.id.img_veg_nonveg);
        img_dish = findViewById(R.id.img_dish);
        btn_addtocart = findViewById(R.id.btn_addtocart);
        recycler_customization = findViewById(R.id.recycler_customization);
        rl_description = findViewById(R.id.rl_description);
        rl_customization = findViewById(R.id.rl_customization);
        ll_customization_sub = findViewById(R.id.ll_customization_sub);
        txt_customisations = findViewById(R.id.txt_customisations);
        add_to_cart_txt = findViewById(R.id.add_to_cart_txt);
        total_price_txt = findViewById(R.id.total_price_txt);
        txt_dish_desc = findViewById(R.id.txt_dish_desc);
        txt_dish_desc_val = findViewById(R.id.txt_dish_desc_val);

        txt_cal = findViewById(R.id.txt_cal);
        txt_cal_from_fat = findViewById(R.id.txt_cal_from_fat);
        view_nointernet = findViewById(R.id.view_nointernet);

        txt_total_fat_val = findViewById(R.id.txt_total_fat_val);
        txt_total_fat_per = findViewById(R.id.txt_total_fat_per);

        txt_saturted_fat_val = findViewById(R.id.txt_saturted_fat_val);
        txt_saturted_fat_per = findViewById(R.id.txt_saturted_fat_per);

        txt_trans_fat_val = findViewById(R.id.txt_trans_fat_val);

        txt_cholesterol_val = findViewById(R.id.txt_cholesterol_val);
        txt_cholesterol_per = findViewById(R.id.txt_cholesterol_per);

        txt_sodium_val = findViewById(R.id.txt_sodium_val);
        txt_sodium_per = findViewById(R.id.txt_sodium_per);

        txt_carbohydrate_val = findViewById(R.id.txt_carbohydrate_val);
        txt_carbohydrate_per = findViewById(R.id.txt_carbohydrate_per);

        txt_diatery_fat_val = findViewById(R.id.txt_diatery_fat_val);
        txt_diatery_per = findViewById(R.id.txt_diatery_per);

        txt_total_sugars_val = findViewById(R.id.txt_total_sugars_val);

        txt_protein_val = findViewById(R.id.txt_protein_val);
        txt_protein_per = findViewById(R.id.txt_protein_per);

        txt_vitamin_a_val = findViewById(R.id.txt_vitamin_a_val);
        txt_vitamin_a_per = findViewById(R.id.txt_vitamin_a_per);

        txt_vitamin_c_val = findViewById(R.id.txt_vitamin_c_val);
        txt_vitamin_c_per = findViewById(R.id.txt_vitamin_c_per);

        txt_calcium_val = findViewById(R.id.txt_calcium_val);
        txt_calcium_per = findViewById(R.id.txt_calcium_per);

        txt_iron_val = findViewById(R.id.txt_iron_val);
        txt_iron_per = findViewById(R.id.txt_iron_per);

        llNutritional = findViewById(R.id.llNutritional);

        imgArrow = findViewById(R.id.imgArrow);

        txt_serving_size = findViewById(R.id.txt_serving_size);
        txt_serving_per_container = findViewById(R.id.txt_serving_per_container);

    }

    @Override
    protected void initData() {

        // TODO initData

        context = DishDetailsActivity.this;

        netwatcher = new Netwatcher(this);

        getIntentValues();

        if (isDishOpenedFromCart) {
            add_to_cart_txt.setText("Update Cart");

        }

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        recycler_customization.setLayoutManager(new LinearLayoutManager(context));

        addToCartPresenter = new AddToCartPresenter(getAPIInterface());
        addToCartPresenter.attachView(this);

        dishDetailsPresenter = new DishDetailsPresenter(getAPIInterface());
        dishDetailsPresenter.attachView(this);

        if (isInternetAvailable()) {
            showProgressDialog();
            dishDetailsPresenter.getDishDetails(getSessionManager().getUserId(), dishId);
        } else {
            view_nointernet.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void initListener() {

        // TODO initListener

        findViewById(R.id.btn_tryagain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetAvailable()) {
                    view_nointernet.setVisibility(View.GONE);
                    showProgressDialog();
                    dishDetailsPresenter.getDishDetails(getSessionManager().getUserId(), dishId);
                }else {
                    showToast(getString(R.string.api_error_internet));
                }

            }
        });

        findViewById(R.id.img_actionbar_backarrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        findViewById(R.id.img_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    txt_quantity.setText(String.valueOf(quantity));
                    float price = Float.parseFloat(dishPrice);
                    totalItemsPrice -= price;

                    totalItemsPrice = (totalItemsPrice - getAddOnPrice);

                    double value = totalItemsPrice;
                    total_price_txt.setText("$ " + String.format("%.2f", value));

                }
            }
        });

        findViewById(R.id.img_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                txt_quantity.setText(String.valueOf(quantity));
                float price = Float.parseFloat(dishPrice);

                totalItemsPrice += price;

                totalItemsPrice = (totalItemsPrice + getAddOnPrice);

                double value = totalItemsPrice;
                total_price_txt.setText("$ " + String.format("%.2f", value));

            }
        });

        rl_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNutritionalShow == 0) {

                    isNutritionalShow = 1;
                    imgArrow.setRotation(180);
                    llNutritional.setVisibility(View.VISIBLE);

                } else {

                    isNutritionalShow = 0;
                    imgArrow.setRotation(0);
                    llNutritional.setVisibility(View.GONE);
                }
            }
        });

        btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isHallClosed) {

                    new FutureTimingsDialog(context, hallId, hallName, mealIds, mealNames, dishName,true).showDialog();

                } else {

                    if(!isDishAvailable) {

                        showToast("Dish is currently unavailable.");

                    }else {

                        if (isMealTimingClosed) {

                            new FutureTimingsDialog(context, hallId, hallName, mealIds, mealNames, dishName,false).showDialog();

                        } else {

                            if (getSessionManager().isUserSkippedLoggedIn()) {

                                ArrayList<Items> itemsList = getSessionManager().getLocalCartItems();
                                if (itemsList != null && itemsList.size() == 0)
                                    getSessionManager().setLocalcarthallId("");

                                String previous_hallName = getSessionManager().getLocalcartHallName();
                                String previous_hallId = getSessionManager().getLocalCartHallId();

                                /*Check if hall_id is same when item added to cart*/

                                if (AppUtils.isStringEmpty(previous_hallId)) {
                                    saveToLocalCart();
                                } else {
                                    if (previous_hallId.equals(hallId)) {
                                        saveToLocalCart();
                                    } else {
                                        openConfirmDialog(hallName, previous_hallName, true);
                                    }
                                }

                            } else {

                                if (isInternetAvailable()) {

                                    String items = getSelectedCustomizations();

                                    showProgressDialog();
                                    addToCartPresenter.addToCart(getSessionManager().getUserId(), hallId, "", items, "0");

                                } else {
                                    showToast(getString(R.string.api_error_internet));
                                }

                            }

                        }

                    }

                }

            }

        });

    }


    // TODO Activity Methods

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void onPause() {
        try{ context.unregisterReceiver(netwatcher); }catch (Exception e){}
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        context.registerReceiver(netwatcher, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if(isConnected && view_nointernet.getVisibility()==View.VISIBLE){
            view_nointernet.setVisibility(View.GONE);
            showProgressDialog();
            dishDetailsPresenter.getDishDetails(getSessionManager().getUserId(), dishId);
        }

    }

    @Override
    protected void onDestroy() {
        try {
            addToCartPresenter.detachView();
        } catch (Exception e) {
        }
        try {
            dishDetailsPresenter.detachView();
        } catch (Exception e) {
        }
        super.onDestroy();
    }


    // TODO Public Methods

    private String checkForDoubleDigits(int value) {

        String temp = String.valueOf(value);
        if (temp.length() == 1) {
            temp = "0" + temp;
        }

        return temp;
    }

    private void getIntentValues() {

        Gson gson = new Gson();

        dishId = getIntent().getStringExtra("dishId");

        isDishOpenedFromCart = getIntent().getBooleanExtra("isDishOpenedFromCart", false);

        if (isDishOpenedFromCart) {

            if (null != getIntent().getStringExtra("itemId")) {
                itemId = getIntent().getStringExtra("itemId");
            } else {
                itemId = "";
            }

            try {
                quantity = Integer.parseInt(getIntent().getStringExtra("quantity"));
            } catch (Exception e) {
            }
            txt_quantity.setText(String.valueOf(quantity));

            try {
                Type type = new TypeToken<Addons[]>() {
                }.getType();
                updateCartAddons = gson.fromJson(getIntent().getStringExtra("addOns"), type);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Type type = new TypeToken<Options[]>() {
                }.getType();
                updateCartOptions = gson.fromJson(getIntent().getStringExtra("options"), type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private boolean isUpdateCartAddOnsChecked(String id_ToCheck) {

        boolean isIdPresent = false;

        if (null != updateCartAddons) {

            for (int i = 0; i < updateCartAddons.length; i++) {

                String id = updateCartAddons[i].getAddon_id();
                if (id.equals(id_ToCheck)) {
                    isIdPresent = true;
                }

            }
        }

        return isIdPresent;

    }

    private boolean isUpdateCartOptionsChecked(String id_ToCheck) {

        boolean isIdPresent = false;

        if (null != updateCartOptions) {

            for (int i = 0; i < updateCartOptions.length; i++) {

                String id = updateCartOptions[i].getOption_id();
                if (id.equals(id_ToCheck)) {
                    isIdPresent = true;
                }

            }
        }

        return isIdPresent;

    }

    private String getSelectedCustomizations() {

        JsonArray jsonArray = new JsonArray();

        JsonObject dishObject = new JsonObject();

        if (isDishOpenedFromCart) {
            dishObject.addProperty("item_id", itemId);
        }

        dishObject.addProperty("id", dishId);
        dishObject.addProperty("qty", quantity);

        JsonArray addOnsArray = new JsonArray();
        JsonArray optionsArray = new JsonArray();

        for (int i = 0; i < customizationList.size(); i++) {

            String parentId = customizationList.get(i).getId();

            ArrayList<DishCustChildModel> optionsList = customizationList.get(i).getOptionList();

            for (int j = 0; j < optionsList.size(); j++) {

                String childId = optionsList.get(j).getId();
                boolean status = optionsList.get(j).getSelected();

                if (status) {

                    if (parentId.equals("*")) {         // AddOns

                        JsonObject addOnsObject = new JsonObject();
                        addOnsObject.addProperty("id", childId);
                        addOnsArray.add(addOnsObject);

                    } else {                     // Options
                        JsonObject optionsObject = new JsonObject();
                        optionsObject.addProperty("option_id", childId);
                        optionsObject.addProperty("ingredient_id", parentId);
                        optionsArray.add(optionsObject);

                    }

                }

            }

        }

        dishObject.add("addons", addOnsArray);
        dishObject.add("options", optionsArray);

        jsonArray.add(dishObject);


        return jsonArray.toString();
    }

    private void extractAndSetDishInfo(DishDetailsResponse dishDetailsResponse) {

        if (null != dishDetailsResponse.getData()) {

            if (null != dishDetailsResponse.getData().getDish()) {

                this.dishDetailsResponse = dishDetailsResponse;

                // Dish Name

                dishName = dishDetailsResponse.getData().getDish().getName();
                txt_actionbar_title.setText(dishName);
                txt_name.setText(dishName);


                // Dish Cost
                double value = Double.parseDouble(dishDetailsResponse.getData().getDish().getPrice());
                txt_cost.setText(getString(R.string.currencySymbol) + " " + String.format("%.2f", value));

                dishPrice = dishDetailsResponse.getData().getDish().getPrice();
                value = Double.parseDouble(dishPrice);
                total_price_txt.setText("$ " + String.format("%.2f", value));
                totalItemsPrice = Float.parseFloat(dishPrice);

                // If Dish open from the cart show total cost as per the quantity and add ons
                if (isDishOpenedFromCart) {

                    totalItemsPrice = 0;
                    dishPrice = dishDetailsResponse.getData().getDish().getPrice();
                    totalItemsPrice = Float.parseFloat(dishPrice);
                    totalItemsPrice = quantity * totalItemsPrice;

                    for (Addons addOn : updateCartAddons) {

                        totalItemsPrice += (Float.parseFloat(addOn.getPrice()) * quantity);
                        getAddOnPrice = getAddOnPrice + (Float.parseFloat(addOn.getPrice()));

                    }

                    value = totalItemsPrice;
                    total_price_txt.setText("$ " + String.format("%.2f", value));
                    //total_price_txt.setText(getString(R.string.currencySymbol) + " " + totalItemsPrice);

                }

                if (dishDetailsResponse.getData().getDish().getIngredients().length() > 0) {
                    txt_dish_desc_val.setVisibility(View.VISIBLE);
                    txt_dish_desc.setVisibility(View.VISIBLE);
                    txt_dish_desc_val.setText(dishDetailsResponse.getData().getDish().getIngredients());
                } else {
                    txt_dish_desc_val.setVisibility(View.GONE);
                    txt_dish_desc.setVisibility(View.GONE);
                }

            }

            // Hall Id
            hallId = dishDetailsResponse.getData().getDish().getHall_id();
            hallName = dishDetailsResponse.getData().getDish().getHall_name();
            mealIdArray = dishDetailsResponse.getData().getDish().getMeal_ids();
            mealNameArray = dishDetailsResponse.getData().getDish().getMeal_names();

            // Dish Calorie
            String strHtml1 = "<b>Calories: </b>" + dishDetailsResponse.getData().getDish().getCalories();
            txt_calorie.setText(Html.fromHtml(strHtml1));

            // Dish Allergies
            String strAttributes = "N/A";
            String strHtml = "<b>Allergens: </b>";

            if (dishDetailsResponse.getData().getDish().getAttributes() != null) {

                DishDetailsResponse.Attributes[] attributesArray = dishDetailsResponse.getData().getDish().getAttributes();

                if (attributesArray.length != 0) {

                    for(int i=0; i<attributesArray.length; i++){

                        String optionName = attributesArray[i].getName();
                        if(optionName.equalsIgnoreCase("Allergens")){

                            if (attributesArray[i].getValues() != null) {

                                strAttributes = "";

                                String[] strArrayattributes = attributesArray[i].getValues();

                                for (int j = 0; j < strArrayattributes.length; j++) {

                                    if (j != 0) {
                                        strAttributes = strAttributes + ", " + strArrayattributes[j];
                                    } else {
                                        strAttributes = strArrayattributes[j];
                                    }

                                }

                            }

                        }

                    }

                }

            }

            txt_allergie.setText(Html.fromHtml(strHtml + strAttributes));

            // Dish Veg/NonVeg
            if (dishDetailsResponse.getData().getDish().getVeg().equalsIgnoreCase("veg")) {
                img_veg_nonveg.setImageResource(R.drawable.ic_veg);
            } else if (dishDetailsResponse.getData().getDish().getVeg().equalsIgnoreCase("non-veg")) {
                img_veg_nonveg.setImageResource(R.drawable.ic_nonveg);
            } else if (dishDetailsResponse.getData().getDish().getVeg().equalsIgnoreCase("contain egg")) {
                img_veg_nonveg.setImageResource(R.drawable.ic_egg);
            }

            // Dish Image
            String imageURL = dishDetailsResponse.getData().getDish().getImage();
            if (imageURL.trim().length() != 0) {

                imageLoader.displayImage(imageURL, img_dish, options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        img_dish.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        img_dish.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        img_dish.setVisibility(View.GONE);
                    }
                });

            } else {

                img_dish.setVisibility(View.GONE);

            }

            try {
                HomeDishesResponse.Halls[] hallsResponse = getSessionManager().getHallsResponse();
                isHallClosed = AppUtils.isHallCurrentlyClosed(hallId, hallsResponse);
            }catch (Exception e){}

            if(isHallClosed==false) {

                try {
                    HomeDishesResponse.Halls[] hallsResponse = getSessionManager().getHallsResponse();

                    String tempIds = "", tempNames = "";

                    for(int i=0; i<mealIdArray.length; i++){

                        if(mealIdArray[i].trim().length()!=0) {

                            boolean temp = AppUtils.isMealTimeCurrentlyClosed(hallId, mealIdArray[i], hallsResponse);
                            if (temp == false) {
                                isMealTimingClosed = false;
                                if (mealIds.trim().length() == 0) {
                                    mealIds = mealIdArray[i];
                                    mealNames = mealNameArray[i];
                                } else {
                                    mealIds = mealIds + "," + mealIdArray[i];
                                    mealNames = mealNames + "," + mealNameArray[i];
                                }
                            }

                            if (tempIds.trim().length() == 0) {
                                tempIds = mealIdArray[i];
                                tempNames = mealNameArray[i];
                            } else {
                                tempIds = tempIds + "," + mealIdArray[i];
                                tempNames = tempNames + "," + mealNameArray[i];
                            }

                        }
                    }

                    if(isMealTimingClosed){
                        mealIds = tempIds;
                        mealNames = tempNames;
                    }

                } catch (Exception e) {
                }

            }

            try {
                String dishStatus = dishDetailsResponse.getData().getDish().getStatus();
                if (dishStatus.equalsIgnoreCase("1")) {
                    isDishAvailable = true;
                }else {
                    isDishAvailable = false;
                }
            }catch (Exception e){}


            if (isHallClosed || isDishAvailable==false || isMealTimingClosed) {
                btn_addtocart.setVisibility(View.VISIBLE);
                btn_addtocart.setBackgroundResource(R.drawable.bg_grey_round_complete);
            } else {
                btn_addtocart.setVisibility(View.VISIBLE);
                btn_addtocart.setBackgroundResource(R.drawable.bg_primary_round_complete);
            }


        }

    }

    private void extractAndSetCustomizationOptions(DishDetailsResponse dishDetailsResponse) {

        customizationList = new ArrayList<>();

        if (null != dishDetailsResponse.getData()) {

            if (null != dishDetailsResponse.getData().getDish()) {

                customizationList = new ArrayList<>();

                // For Customizations

                if (null != dishDetailsResponse.getData().getDish().getCustomizable_options()) {

                    DishDetailsResponse.Customizable_options[] customizableOptions = dishDetailsResponse.getData().getDish().getCustomizable_options();

                    for (int i = 0; i < customizableOptions.length; i++) {

                        ArrayList<DishCustChildModel> optionsList = new ArrayList<>();
                        if (null != customizableOptions[i].getOptions()) {

                            DishDetailsResponse.Options[] options = customizableOptions[i].getOptions();

                            for (int j = 0; j < options.length; j++) {

                                boolean isSelected = false;

                                if (isDishOpenedFromCart) {
                                    isSelected = isUpdateCartOptionsChecked(options[j].getId());
                                }

                                optionsList.add(
                                        new DishCustChildModel(
                                                options[j].getId(),
                                                options[j].getName(),
                                                "",
                                                isSelected
                                        )
                                );

                            }

                        }

                        customizationList.add(
                                new DishCustParentModel(
                                        customizableOptions[i].getId(),
                                        customizableOptions[i].getIngredient(),
                                        customizableOptions[i].getMax_allowed_options(),
                                        optionsList
                                )
                        );

                    }

                }

                if (customizationList.size() != 0) {
                    rl_customization.setVisibility(View.VISIBLE);
                    ll_customization_sub.setVisibility(View.VISIBLE);
                }

                // For AddOns

                if (null != dishDetailsResponse.getData().getDish().getAddons()) {

                    DishDetailsResponse.Addons[] addons = dishDetailsResponse.getData().getDish().getAddons();

                    ArrayList<DishCustChildModel> optionsList = new ArrayList<>();

                    for (int i = 0; i < addons.length; i++) {

                        boolean isSelected = false;

                        if (isDishOpenedFromCart) {
                            isSelected = isUpdateCartAddOnsChecked(addons[i].getId());
                        }

                        optionsList.add(
                                new DishCustChildModel(
                                        addons[i].getId(),
                                        addons[i].getAddon(),
                                        addons[i].getPrice(),
                                        isSelected
                                )
                        );

                    }

                    customizationList.add(
                            new DishCustParentModel(
                                    "*",
                                    "ADD ONS",
                                    "*",
                                    optionsList
                            )
                    );

                    if (customizationList.size() != 0) {
                        ll_customization_sub.setVisibility(View.VISIBLE);
                    }

                }


            }

        }

        dishCustParentAdapter = new DishCustParentAdapter(context, this, customizationList, isDishOpenedFromCart);
        recycler_customization.setAdapter(dishCustParentAdapter);

    }

    private void clearOptions() {

        quantity = 1;
        txt_quantity.setText(String.valueOf(quantity));

        for (int i = 0; i < customizationList.size(); i++) {
            ArrayList<DishCustChildModel> optionsList = customizationList.get(i).getOptionList();
            for (int j = 0; j < optionsList.size(); j++) {
                customizationList.get(i).getOptionList().get(j).setSelected(false);
            }
        }

        dishCustParentAdapter = new DishCustParentAdapter(context, this, customizationList, isDishOpenedFromCart);
        recycler_customization.setAdapter(dishCustParentAdapter);

    }


    // TODO Interface Methods

    @Override
    public void onSuccess_AddToCart(String message, String cartCount, String cartPrice) {

        hideProgressDialog();

        getSessionManager().setCartCount(cartCount);
        getSessionManager().setCartPrice(cartPrice);

        if (isDishOpenedFromCart) {

            showToast("Cart value is updated");

            Intent intent = new Intent("reloadCart");
            context.sendBroadcast(intent);

            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

        } else {

//            clearOptions();

            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }

    }

    @Override
    public void onSuccess(String message, DishDetailsResponse dishDetailsResponse) {

        hideProgressDialog();
        extractAndSetDishInfo(dishDetailsResponse);
        extractAndSetCustomizationOptions(dishDetailsResponse);
        nestedScrollView.setVisibility(View.VISIBLE);

        String strIsNewDish = dishDetailsResponse.getData().getDish().getCustom();

        showProgressDialog();

        if(strIsNewDish.equalsIgnoreCase("true")){
            isNewDish = true;
            dishDetailsPresenter.getNewDishNutritions(dishId);
        }else {
            isNewDish = false;
            dishDetailsPresenter.getDishNutritions(dishId);
        }

    }

    @Override
    public void onSelect_Option(int parentPosition, int childPosition, boolean isChecked, String addOnPrice) {

        if (customizationList.get(parentPosition).getMaxoptions().equals("1")) {

            ArrayList<DishCustChildModel> optionsList = customizationList.get(parentPosition).getOptionList();
            for (int i = 0; i < optionsList.size(); i++) {
                customizationList.get(parentPosition).getOptionList().get(i).setSelected(false);
            }

            //customizationList.get(parentPosition).setOptionList(optionsList);

        }
        if (addOnPrice != null & !"".equals(addOnPrice)) {
            if (isChecked) {

                totalItemsPrice += (Float.parseFloat(addOnPrice) * quantity);
                getAddOnPrice += Float.parseFloat(addOnPrice);

            } else {

                totalItemsPrice -= (Float.parseFloat(addOnPrice) * quantity);
                getAddOnPrice -= Float.parseFloat(addOnPrice);

            }
        }

        double value = totalItemsPrice;
        total_price_txt.setText("$ " + String.format("%.2f", value));

        customizationList.get(parentPosition).getOptionList().get(childPosition).setSelected(isChecked);

        dishCustParentAdapter.updateList(customizationList);

    }

    @Override
    public void onSuccessDishNutritions(NutritionalResponse nutritionalResponse, NewNutritionalResponse newNutritionalResponse) {

        hideProgressDialog();

        if(isNewDish){
            ShowNewDishNutritions(newNutritionalResponse);
        }else {
            ShowDishNutritions(nutritionalResponse);
        }

    }

    @Override
    public void onError_AddToCart(String message, boolean isDishFromDifferentHall, boolean isDishFromDifferentMeal, String cartDishHallName, String cartDishMealName) {

        hideProgressDialog();
        if (isDishFromDifferentHall) {
            openConfirmDialog(hallName, cartDishHallName, true);
        } else if (isDishFromDifferentMeal) {
            openConfirmDialog(hallName, cartDishHallName, false);
        }else {
            showToast(message);
        }

    }

    @Override
    public void onError(String message) {
        hideProgressDialog();
        showToast(message);
    }

    public void openConfirmDialog(String hallName, String cartDishHallName, boolean showHallData) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_discardcart);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        TextView txt_message = dialog.findViewById(R.id.txt_message);
        if(showHallData) {
            txt_message.setText("Your cart already contains an item from \'" + cartDishHallName + "\'. Would you like to clear the cart and add this item from \'" + hallName + "\' instead?");
        }else {
            txt_message.setText("Some of the dish(s) in your cart are currently unavailable. Please clear the cart to proceed.");
        }

        ImageView img_exit = dialog.findViewById(R.id.img_exit);
        img_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button btn_yes = dialog.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                String items = getSelectedCustomizations();

                if (getSessionManager().isUserSkippedLoggedIn()) {
                    ArrayList<Items> itemsList = new ArrayList<>();
                    getSessionManager().setLocalCartItems(itemsList);
                    saveToLocalCart();
                } else {
                    addToCartPresenter.addToCart(getSessionManager().getUserId(), hallId, mealIds, items, "1");
                }

            }
        });

        Button btn_no = dialog.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    // TODO Local Cart Methods

    private void saveToLocalCart() {

        Items items = new Items();
        items.setId(AppUtils.getTimeStamp());
        items.setDish_id(dishId);
        items.setPrice(dishDetailsResponse.getData().getDish().getPrice());
        items.setVeg(dishDetailsResponse.getData().getDish().getVeg());
        items.setItem_name(dishDetailsResponse.getData().getDish().getName());
        items.setStatus("1");
        items.setMeal_ids(mealIdArray);
        items.setQty(String.valueOf(quantity));

        ArrayList<Addons> addonsList = new ArrayList<>();
        ArrayList<Options> optionsList = new ArrayList<>();

        for (int i = 0; i < customizationList.size(); i++) {

            String parentId = customizationList.get(i).getId();

            ArrayList<DishCustChildModel> optionsArray = customizationList.get(i).getOptionList();

            for (int j = 0; j < optionsArray.size(); j++) {

                boolean status = optionsArray.get(j).getSelected();

                if (status) {

                    if (parentId.equals("*")) {         // AddOns
                        Addons addons = new Addons();
                        addons.setId(AppUtils.getTimeStamp());
                        addons.setAddon_id(optionsArray.get(j).getId());
                        addons.setAddon(optionsArray.get(j).getName());
                        addons.setPrice(optionsArray.get(j).getPrice());
                        addonsList.add(addons);
                    } else {                          // Options
                        Options options = new Options();
                        options.setId(AppUtils.getTimeStamp());
                        options.setIngredient_id(optionsArray.get(j).getId());
                        options.setIngredient("");
                        options.setOption_id(optionsArray.get(j).getId());
                        options.setOption(optionsArray.get(j).getName());
                        optionsList.add(options);
                    }

                }

            }

        }

        Addons[] addons = new Addons[addonsList.size()];
        int intAddons[] = new int[addonsList.size()];

        for (int i = 0; i < addonsList.size(); i++) {
            addons[i] = addonsList.get(i);
            intAddons[i] = Integer.parseInt(addonsList.get(i).getAddon_id());
        }

        items.setAddons(addons);

        Options[] options = new Options[optionsList.size()];
        int intOptions[] = new int[optionsList.size()];

        for (int i = 0; i < optionsList.size(); i++) {
            options[i] = optionsList.get(i);
            intOptions[i] = Integer.parseInt(optionsList.get(i).getOption_id());
        }

        items.setOptions(options);

        if (isDishOpenedFromCart) {                                                         /* Update Cart */

            getSessionManager().setLocalcarthallId(hallId);
            getSessionManager().setLocalcartHallName(hallName);

            ArrayList<Items> itemsList = getSessionManager().getLocalCartItems();

            /* Now check for duplicate or update quantity logic */

            int currentItemIdPosition = 0, anotherSameDishPosition = 0;
            boolean isCustomizationEqual = false;

            for (int i = 0; i < itemsList.size(); i++) {

                String id = itemsList.get(i).getId();
                String dish_Id = itemsList.get(i).getDish_id();
                if (dish_Id.equals(dishId) && !id.equals(itemId)) {

                    boolean isAddOnsEqual = false;
                    Addons[] add1 = itemsList.get(i).getAddons();
                    int intAdd1[] = new int[add1.length];
                    for (int x = 0; x < add1.length; x++) {
                        intAdd1[x] = Integer.parseInt(add1[x].getAddon_id());
                    }

                    if (Arrays.equals(intAddons, intAdd1)) {
                        isAddOnsEqual = true;
                    }

                    boolean isOptionsEqual = false;
                    Options[] opt1 = itemsList.get(i).getOptions();
                    int intOpt1[] = new int[opt1.length];
                    for (int x = 0; x < opt1.length; x++) {
                        intOpt1[x] = Integer.parseInt(opt1[x].getOption_id());
                    }


                    if (Arrays.equals(intOptions, intOpt1)) {
                        isOptionsEqual = true;
                    }

                    if (isAddOnsEqual && isOptionsEqual) {
                        isCustomizationEqual = true;
                        anotherSameDishPosition = i;
                    }

                }

                if (id.equals(itemId)) {
                    currentItemIdPosition = i;
                }

            }

            if (isCustomizationEqual) {

                itemsList.get(anotherSameDishPosition).setAddons(addons);
                itemsList.get(anotherSameDishPosition).setOptions(options);

                quantity = quantity + Integer.parseInt(itemsList.get(anotherSameDishPosition).getQty());

                itemsList.get(anotherSameDishPosition).setQty(String.valueOf(quantity));

                itemsList.remove(currentItemIdPosition);

            } else {

                itemsList.get(currentItemIdPosition).setAddons(addons);
                itemsList.get(currentItemIdPosition).setOptions(options);
                itemsList.get(currentItemIdPosition).setQty(String.valueOf(quantity));

            }

            getSessionManager().setLocalCartItems(itemsList);

            Intent intent = new Intent("reloadCart");
            context.sendBroadcast(intent);

            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

        } else {                                                                    /* Adding New Item To Cart */

            ArrayList<Items> itemsList = getSessionManager().getLocalCartItems();

            if (null == itemsList || itemsList.size() == 0) {

                /* Add dish data directly*/

                getSessionManager().setLocalcarthallId(hallId);
                getSessionManager().setLocalcartHallName(hallName);

                itemsList = new ArrayList<>();
                itemsList.add(items);

                getSessionManager().setLocalCartItems(itemsList);
                calculateCartCountAndPrice(itemsList);

                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

            } else {

                /* Now check for duplicate or update quantity logic */

                itemsList = getSessionManager().getLocalCartItems();

                /* Now check for duplicate or update quantity logic */

                int currentItemIdPosition = 0, anotherSameDishPosition = 0;
                boolean isCustomizationEqual = false, isSameDishAvailable = false;

                for (int i = 0; i < itemsList.size(); i++) {

                    String dish_Id = itemsList.get(i).getDish_id();

                    if (dish_Id.equals(dishId)) {

                        isSameDishAvailable = true;

                        boolean isAddOnsEqual = false;
                        Addons[] add1 = itemsList.get(i).getAddons();
                        int intAdd1[] = new int[add1.length];
                        for (int x = 0; x < add1.length; x++) {
                            intAdd1[x] = Integer.parseInt(add1[x].getAddon_id());
                        }

                        if (Arrays.equals(intAddons, intAdd1)) {
                            isAddOnsEqual = true;
                        }

                        boolean isOptionsEqual = false;
                        Options[] opt1 = itemsList.get(i).getOptions();
                        int intOpt1[] = new int[opt1.length];
                        for (int x = 0; x < opt1.length; x++) {
                            intOpt1[x] = Integer.parseInt(opt1[x].getOption_id());
                        }

                        if (Arrays.equals(intOptions, intOpt1)) {
                            isOptionsEqual = true;
                        }

                        if (isAddOnsEqual && isOptionsEqual) {
                            isCustomizationEqual = true;
                            anotherSameDishPosition = i;
                        }

                    }

                    if (dish_Id.equals(dishId)) {
                        currentItemIdPosition = i;
                    }

                }

                if(isSameDishAvailable){

                    if (isCustomizationEqual) {

                        itemsList.get(anotherSameDishPosition).setAddons(addons);
                        itemsList.get(anotherSameDishPosition).setOptions(options);
                        quantity = quantity + Integer.parseInt(itemsList.get(anotherSameDishPosition).getQty());
                        itemsList.get(anotherSameDishPosition).setQty(String.valueOf(quantity));

                    } else {

                        itemsList.add(currentItemIdPosition, items);
                        itemsList.get(currentItemIdPosition).setAddons(addons);
                        itemsList.get(currentItemIdPosition).setOptions(options);
                        itemsList.get(currentItemIdPosition).setQty(String.valueOf(quantity));

                    }

                    calculateCartCountAndPrice(itemsList);
                    getSessionManager().setLocalcarthallId(hallId);
                    getSessionManager().setLocalcartHallName(hallName);
                    getSessionManager().setLocalCartItems(itemsList);
                    Intent intent = new Intent("reloadCart");
                    context.sendBroadcast(intent);

                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                }else {

                    getSessionManager().setLocalcarthallId(hallId);
                    getSessionManager().setLocalcartHallName(hallName);

                    itemsList.add(currentItemIdPosition, items);
                    itemsList.get(currentItemIdPosition).setAddons(addons);
                    itemsList.get(currentItemIdPosition).setOptions(options);
                    itemsList.get(currentItemIdPosition).setQty(String.valueOf(quantity));

                    calculateCartCountAndPrice(itemsList);
                    getSessionManager().setLocalCartItems(itemsList);
                    Intent intent = new Intent("reloadCart");
                    context.sendBroadcast(intent);

                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                }



            }

        }

    }

    private void calculateCartCountAndPrice(ArrayList<Items> itemsList){

        int totalItemsInCart = 0;
        double totalCartPrice = 0.00;

        for (int ss = 0; ss < itemsList.size(); ss++) {

            int itemQuantity = Integer.parseInt(itemsList.get(ss).getQty());

            totalItemsInCart += itemQuantity;

            String strItemPrice = itemsList.get(ss).getPrice();
            double dblItemPrice = Double.parseDouble(strItemPrice);
            totalCartPrice += (dblItemPrice*itemQuantity);

            double singleQuantityAllAddonsPrice = 0.00;

            if(null!= itemsList.get(ss).getAddons()){

                Addons addons[] = itemsList.get(ss).getAddons();

                for(int i=0; i<addons.length; i++){

                    String strAddonsPrice = addons[i].getPrice();
                    double dblAddonsPrice = Double.parseDouble(strAddonsPrice);
                    singleQuantityAllAddonsPrice += dblAddonsPrice;

                }

            }

            double totalQuantityAllAddonsPrice = itemQuantity * singleQuantityAllAddonsPrice;
            totalCartPrice += totalQuantityAllAddonsPrice;

        }

        getSessionManager().setCartCount(String.valueOf(totalItemsInCart));
        getSessionManager().setCartPrice(String.valueOf(totalCartPrice));

    }

    private void ShowDishNutritions(NutritionalResponse nutritionalResponse) {

        try {

            if (nutritionalResponse.getData().getNutrition().getSuggested_serving_size() != null) {
                txt_serving_size.setText("Serving Size (" + AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutrition().getSuggested_serving_size().getValue()) + nutritionalResponse.getData().getNutrition().getSuggested_serving_size().getType() + ")");
            } else {
                txt_serving_size.setText("Serving Size (N/A)");
            }

            if (nutritionalResponse.getData().getNutrition().getServing_per_container() != null) {
                txt_serving_per_container.setText("Serving Per Container About " + AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutrition().getServing_per_container()));
            } else {
                txt_serving_per_container.setText("Serving Per Container N/A");
            }

            if (nutritionalResponse.getData().getNutrition().getCalories() != null) {
                txt_cal.setText(nutritionalResponse.getData().getNutrition().getCalories().getAmount());
            } else {
                txt_cal.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutrition().getCaloriesFromFat() != null) {
                txt_cal_from_fat.setText("Calories from Fat " + AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutrition().getCaloriesFromFat().getAmount()));
            } else {
                txt_cal_from_fat.setText("Calories from Fat N/A");
            }

            if (nutritionalResponse.getData().getNutrition().getTotalFat() != null) {
                txt_total_fat_val.setText(nutritionalResponse.getData().getNutrition().getTotalFat().getAmount() + nutritionalResponse.getData().getNutrition().getTotalFat().getUnit());
                txt_total_fat_per.setText(nutritionalResponse.getData().getNutrition().getTotalFat().getDailyValue());
            } else {
                txt_total_fat_val.setText("N/A");
                txt_total_fat_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutrition().getSaturatedFat() != null) {
                txt_saturted_fat_val.setText(nutritionalResponse.getData().getNutrition().getSaturatedFat().getAmount() + "g");
                txt_saturted_fat_per.setText(nutritionalResponse.getData().getNutrition().getSaturatedFat().getDailyValue());
            } else {
                txt_saturted_fat_val.setText("N/A");
                txt_saturted_fat_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutrition().getTransFat() != null) {
                txt_trans_fat_val.setText(nutritionalResponse.getData().getNutrition().getTransFat().getAmount() + nutritionalResponse.getData().getNutrition().getTransFat().getUnit());
            } else {
                txt_trans_fat_val.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutrition().getCholestrol() != null) {
                txt_cholesterol_val.setText(nutritionalResponse.getData().getNutrition().getCholestrol().getAmount() + nutritionalResponse.getData().getNutrition().getCholestrol().getUnit());
                txt_cholesterol_per.setText(nutritionalResponse.getData().getNutrition().getCholestrol().getDailyValue());
            } else {
                txt_cholesterol_val.setText("N/A");
                txt_cholesterol_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutrition().getSodium() != null) {
                txt_sodium_val.setText(nutritionalResponse.getData().getNutrition().getSodium().getAmount() + nutritionalResponse.getData().getNutrition().getSodium().getUnit());
                txt_sodium_per.setText(nutritionalResponse.getData().getNutrition().getSodium().getDailyValue());
            } else {
                txt_sodium_val.setText("N/A");
                txt_sodium_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutrition().getCarbohydrate() != null) {
                txt_carbohydrate_val.setText(nutritionalResponse.getData().getNutrition().getCarbohydrate().getAmount() + nutritionalResponse.getData().getNutrition().getCarbohydrate().getUnit());
                txt_carbohydrate_per.setText(nutritionalResponse.getData().getNutrition().getCarbohydrate().getDailyValue());
            } else {
                txt_carbohydrate_val.setText("N/A");
                txt_carbohydrate_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutrition().getDietaryFiber() != null) {
                txt_diatery_fat_val.setText(nutritionalResponse.getData().getNutrition().getDietaryFiber().getAmount() + nutritionalResponse.getData().getNutrition().getDietaryFiber().getUnit());
                txt_diatery_per.setText(nutritionalResponse.getData().getNutrition().getDietaryFiber().getDailyValue());
            } else {
                txt_diatery_fat_val.setText("N/A");
                txt_diatery_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutrition().getSugars() != null) {
                txt_total_sugars_val.setText(nutritionalResponse.getData().getNutrition().getSugars().getAmount() + nutritionalResponse.getData().getNutrition().getSugars().getUnit());
            } else {
                txt_total_sugars_val.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutrition().getProtein() != null) {
                txt_protein_val.setText(nutritionalResponse.getData().getNutrition().getProtein().getAmount() + nutritionalResponse.getData().getNutrition().getProtein().getUnit());
//                txt_protein_per.setText(nutritionalResponse.getData().getNutrition().getProtein().getDailyValue());
            } else {
                txt_protein_val.setText("N/A");
                txt_protein_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutrition().getVitaminA() != null) {
                txt_vitamin_a_per.setText(nutritionalResponse.getData().getNutrition().getVitaminA().getDailyValue());
            } else {
                txt_vitamin_a_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutrition().getVitaminC() != null) {
                txt_vitamin_c_per.setText(nutritionalResponse.getData().getNutrition().getVitaminC().getDailyValue());
            } else {
                txt_vitamin_c_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutrition().getCalcium() != null) {
                txt_calcium_per.setText(nutritionalResponse.getData().getNutrition().getCalcium().getDailyValue());
            } else {
                txt_calcium_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutrition().getIron() != null) {
                txt_iron_per.setText(nutritionalResponse.getData().getNutrition().getIron().getDailyValue());
            } else {
                txt_iron_per.setText("N/A");
            }

            rl_description.setVisibility(View.VISIBLE);

        }catch (Exception e) {
        }

    }

    private void ShowNewDishNutritions(NewNutritionalResponse nutritionalResponse) {

        try {

            if (nutritionalResponse.getData().getNutritions().getSuggested_serving_size() != null) {
                txt_serving_size.setText("Serving Size (" + AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getSuggested_serving_size().getValue()) + nutritionalResponse.getData().getNutritions().getSuggested_serving_size().getType() + ")");
            } else {
                txt_serving_size.setText("Serving Size (N/A)");
            }

            if (nutritionalResponse.getData().getNutritions().getServing_per_container() != null) {
                txt_serving_per_container.setText("Serving Per Container About " + AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getServing_per_container()));
            } else {
                txt_serving_per_container.setText("Serving Per Container N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getCalories() != null) {
                txt_cal.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getCalories()));
            } else {
                txt_cal.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getCalories_from_fat() != null) {
                txt_cal_from_fat.setText("Calories from Fat " + AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getCalories_from_fat()));
            } else {
                txt_cal_from_fat.setText("Calories from Fat N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getTotal_fat() != null) {
                txt_total_fat_val.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getTotal_fat().getValue()) + nutritionalResponse.getData().getNutritions().getTotal_fat().getType());
            } else {
                txt_total_fat_val.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getTotal_fat_perc() != null) {
                txt_total_fat_per.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getTotal_fat_perc().getValue()) + nutritionalResponse.getData().getNutritions().getTotal_fat_perc().getType());
            } else {
                txt_total_fat_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getSaturated_fat() != null) {
                txt_saturted_fat_val.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getSaturated_fat().getValue()) + nutritionalResponse.getData().getNutritions().getSaturated_fat().getType());
            } else {
                txt_saturted_fat_val.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getSaturated_fat_perc() != null) {
                txt_saturted_fat_per.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getSaturated_fat_perc().getValue()) + nutritionalResponse.getData().getNutritions().getSaturated_fat_perc().getType());
            } else {
                txt_saturted_fat_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getTrans_fat() != null) {
                txt_trans_fat_val.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getTrans_fat().getValue()) + nutritionalResponse.getData().getNutritions().getTrans_fat().getType());
            } else {
                txt_trans_fat_val.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getCholestrol() != null) {
                txt_cholesterol_val.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getCholestrol().getValue()) + nutritionalResponse.getData().getNutritions().getCholestrol().getType());
            } else {
                txt_cholesterol_val.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getCholestrol_perc() != null) {
                txt_cholesterol_per.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getCholestrol_perc().getValue()) + nutritionalResponse.getData().getNutritions().getCholestrol_perc().getType());
            } else {
                txt_cholesterol_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getSodium() != null) {
                txt_sodium_val.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getSodium().getValue()) + nutritionalResponse.getData().getNutritions().getSodium().getType());
            } else {
                txt_sodium_val.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getSodium_perc() != null) {
                txt_sodium_per.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getSodium_perc().getValue()) + nutritionalResponse.getData().getNutritions().getSodium_perc().getType());
            } else {
                txt_sodium_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getTotal_carb() != null) {
                txt_carbohydrate_val.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getTotal_carb().getValue()) + nutritionalResponse.getData().getNutritions().getTotal_carb().getType());
            } else {
                txt_carbohydrate_val.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getTotal_carb_perc() != null) {
                txt_carbohydrate_per.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getTotal_carb_perc().getValue()) + nutritionalResponse.getData().getNutritions().getTotal_carb_perc().getType());
            } else {
                txt_carbohydrate_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getDietary_fiber() != null) {
                txt_diatery_fat_val.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getDietary_fiber().getValue()) + nutritionalResponse.getData().getNutritions().getDietary_fiber().getType());
            } else {
                txt_diatery_fat_val.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getDietary_fiber_perc() != null) {
                txt_diatery_per.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getDietary_fiber_perc().getValue()) + nutritionalResponse.getData().getNutritions().getDietary_fiber_perc().getType());
            } else {
                txt_diatery_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getSugars() != null) {
                txt_total_sugars_val.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getSugars().getValue()) + nutritionalResponse.getData().getNutritions().getSugars().getType());
            } else {
                txt_total_sugars_val.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getProtein() != null) {
                txt_protein_val.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getProtein().getValue()) + nutritionalResponse.getData().getNutritions().getProtein().getType());
            } else {
                txt_protein_val.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getProtein_perc() != null) {
                txt_protein_per.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getProtein_perc().getValue()) + nutritionalResponse.getData().getNutritions().getProtein_perc().getType());
            } else {
                txt_protein_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getVitamin_a() != null) {
                txt_vitamin_a_per.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getVitamin_a().getValue()) + nutritionalResponse.getData().getNutritions().getVitamin_a().getType());
            } else {
                txt_vitamin_a_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getVitamin_c() != null) {
                txt_vitamin_c_per.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getVitamin_c().getValue()) + nutritionalResponse.getData().getNutritions().getVitamin_c().getType());
            } else {
                txt_vitamin_c_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getCalcium() != null) {
                txt_calcium_per.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getCalcium().getValue()) + nutritionalResponse.getData().getNutritions().getCalcium().getType());
            } else {
                txt_calcium_per.setText("N/A");
            }

            if (nutritionalResponse.getData().getNutritions().getIron() != null) {
                txt_iron_per.setText(AppUtils.roundOffDecimalToInt(nutritionalResponse.getData().getNutritions().getIron().getValue()) + nutritionalResponse.getData().getNutritions().getIron().getType());
            } else {
                txt_iron_per.setText("N/A");
            }

            rl_description.setVisibility(View.VISIBLE);

        }catch (Exception e) {
        }


    }


}
