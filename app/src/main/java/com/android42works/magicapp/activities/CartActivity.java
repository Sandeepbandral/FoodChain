package com.android42works.magicapp.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.android42works.magicapp.R;
import com.android42works.magicapp.adapters.CartAdapter;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.cart.Addons;
import com.android42works.magicapp.cart.Cart;
import com.android42works.magicapp.cart.CartResponse;
import com.android42works.magicapp.cart.Items;
import com.android42works.magicapp.cart.Options;
import com.android42works.magicapp.dialogs.TaxesDialog;
import com.android42works.magicapp.dialogs.FutureTimingsDialog;
import com.android42works.magicapp.fragments.HomeFragment;
import com.android42works.magicapp.interfaces.AddToCartInterface;
import com.android42works.magicapp.interfaces.CartInterface;
import com.android42works.magicapp.interfaces.SavedCardsInterface;
import com.android42works.magicapp.presenters.CartPresenter;
import com.android42works.magicapp.presenters.SavedCardsPresenter;
import com.android42works.magicapp.responses.CouponResponse;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.android42works.magicapp.responses.LocalCartTaxResponse;
import com.android42works.magicapp.responses.StripeSavedCardsListResponse;
import com.android42works.magicapp.responses.TimingsResponse;
import com.android42works.magicapp.utils.AppUtils;
import com.android42works.magicapp.utils.Netwatcher;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/* Created by JSP@nesar */

public class CartActivity extends BaseActivity implements CartInterface, SavedCardsInterface, AddToCartInterface, Netwatcher.NetwatcherListener {

    private TextView txt_actionbar_title;
    private RecyclerView recycler_cart;
    private CartAdapter cartAdapter;
    private ArrayList<Items> cartList = new ArrayList<>();
    private RelativeLayout rl_coupon_details, rl_discount, rl_card;
    private ImageView img_dropdown_coupon, img_card, img_taxinfo;
    private View view_nodata, view_nointernet;
    private EditText edt_coupon, edt_instructions;
    private Handler timerHandler;
    private Runnable timerRunnable;
    private Button btn_place, btn_applycoupon;
    private TextView txt_change, txt_cardno, txt_total, txt_discount, txt_taxes, txt_topay;
    private NestedScrollView nestedScrollView;
    private boolean isCouponViewExpanded = true;
    private String hallId = "", stripeCustomerId = "", pickUpMethod = "immediate", pickUpDate = "",
            pickUpTime = "", hallOpeningTime = "00:00 AM", hallClosingTime = "11:59 PM", todayDate = "";
    private ArrayList<StripeSavedCardsListResponse.Data> savedCardsList = new ArrayList<>();
    private Netwatcher netwatcher;
    private DecimalFormat formater = new DecimalFormat("0.00");
    private BroadcastReceiver broadcast_reciever;
    private IntentFilter intentFilter;
    private SavedCardsPresenter savedCardsPresenter;
    private CartPresenter cartPresenter;
    private String strTaxInfo = "", totaTax = "", hallName = "";
    private boolean isHallClosed = true, showCouponMessage = false;
    public static CartActivity instance;
    private float totalCartItemsCost = 0f;
    private String selectedCardId = "", mealIds = "", activeMealIds = "";

    private int setDayFlag = 0;

    private Handler timerHandler_PlaceOrder;
    private Runnable timerRunnable_PlaceOrder;
    private float progressvalue = 0f;

    private int tpMinHour = 00, tpMinMinute = 00, tpMaxHour = 23, tpMaxMinute = 59, totalItemsInCart = 0;

    /* Unused Variables */

//    private RadioGroup mRadioGroup;
//    private LinearLayout ll_pickup;
//    private TextView txt_pickup_date, txt_pickup_time;


    public static CartActivity getInstance() {
        return instance;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.act_cart;
    }

    @Override
    protected Activity getActivityContext() {
        return this;
    }

    @Override
    protected void initView() {

        // TODO initView

        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);
        txt_cardno = findViewById(R.id.txt_cardno);
        img_card = findViewById(R.id.img_card);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        recycler_cart = findViewById(R.id.recycler_cart);
        img_dropdown_coupon = findViewById(R.id.img_dropdown_coupon);
        img_taxinfo = findViewById(R.id.img_taxinfo);
        rl_coupon_details = findViewById(R.id.rl_coupon_details);
        txt_change = findViewById(R.id.txt_change);
        edt_coupon = findViewById(R.id.edt_coupon);
        view_nodata = findViewById(R.id.view_nodata);
        btn_place = findViewById(R.id.btn_place);
        rl_discount = findViewById(R.id.rl_discount);
        txt_total = findViewById(R.id.txt_total);
        txt_discount = findViewById(R.id.txt_discount);
        txt_taxes = findViewById(R.id.txt_taxes);
        txt_topay = findViewById(R.id.txt_topay);
        rl_card = findViewById(R.id.rl_card);
        btn_applycoupon = findViewById(R.id.btn_applycoupon);
        edt_instructions = findViewById(R.id.edt_instructions);
        view_nointernet = findViewById(R.id.view_nointernet);

//        mRadioGroup = findViewById(R.id.mRadioGroup);
//        ll_pickup = findViewById(R.id.ll_pickup);
//        txt_pickup_date = findViewById(R.id.txt_pickup_date);
//        txt_pickup_time = findViewById(R.id.txt_pickup_time);

    }

    @Override
    protected void initData() {

        // TODO initData

        instance = this;

        txt_actionbar_title.setText("CART");

        netwatcher = new Netwatcher(this);

        edt_instructions.setText(getSessionManager().getCartInstructions());

        cartAdapter = new CartAdapter(context, this, cartList);
        recycler_cart.setLayoutManager(new LinearLayoutManager(context));
        recycler_cart.setAdapter(cartAdapter);

        cartPresenter = new CartPresenter(getAPIInterface());
        cartPresenter.attachView(this);

        intentFilter = new IntentFilter();
        intentFilter.addAction("reloadCart");
        intentFilter.addAction("reloadSavedCards");
        intentFilter.addAction("checkLoginStatus");

        if (broadcast_reciever == null) {

            broadcast_reciever = new BroadcastReceiver() {

                @Override
                public void onReceive(Context arg0, Intent intent) {

                    String action = intent.getAction();

                    Log.e("action???", "action???" + action);

                    if (action.equals("reloadCart")) {
                        if (getSessionManager().isUserSkippedLoggedIn()) {
                            loadLocalCartData();
                        } else {
                            loadData();
                        }
                    }

                    if (action.equals("reloadSavedCards")) {
                        loadSavedCards();
                    }

                    if (action.equals("checkLoginStatus")) {

                        /*Add item from local cart*/
                        // addItemsFromLocalCart();

                        loadSavedCards();
                        loadData();
                    }

                }

            };

        }

        context.registerReceiver(netwatcher, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        context.registerReceiver(broadcast_reciever, intentFilter);

        savedCardsPresenter = new SavedCardsPresenter(getStripeAPIInterface());
        savedCardsPresenter.attachView(this);

        if (isInternetAvailable()) {

            if (getSessionManager().isUserSkippedLoggedIn()) {
                loadLocalCartData();
            } else {
                loadData();
                loadSavedCards();
            }

        } else {
            view_nointernet.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void initListener() {

        // TODO initListener

        findViewById(R.id.img_actionbar_backarrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        findViewById(R.id.btn_tryagain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetAvailable()) {
                    if (getSessionManager().isUserSkippedLoggedIn()) {
                        loadLocalCartData();
                    } else {
                        loadData();
                        loadSavedCards();
                    }
                }else {
                    showToast(getString(R.string.api_error_internet));
                }
            }
        });

        edt_instructions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getSessionManager().setCartInstructions(s.toString());
            }
        });

        btn_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getSessionManager().isUserSkippedLoggedIn()) {

                    startActivity(new Intent(context, SkipLoginActivity.class));
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                } else {

                    if (savedCardsList.size() == 0) {

                        showToast("Please add payment method to continue with order");

                    }else {

                        if (txt_change.getText().toString().equalsIgnoreCase("change")) {

                            if (isHallClosed) {

                                new FutureTimingsDialog(context, hallId, hallName, "", "", "", true).showDialog();

                            } else {

                                if (isAllDishesAvailable()) {

                                    if (isInternetAvailable()) {
                                        cartPresenter.getLatestTimings(AppUtils.getTimeZone());
                                        placeOrderDialog();

                                    } else {
                                        showToast(getString(R.string.api_error_internet));
                                    }

                                } else {

                                    showToast("Some of the dish(s) in your cart are currently unavailable. Please remove the unavailable dish(s) to proceed.");

                                }

                            }

                        }

                    }

                }

            }
        });

        txt_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideProgressDialog();

                if (txt_change.getText().toString().equalsIgnoreCase("change")) {

                    startActivity(new Intent(context, CardsListActivity.class).putExtra("selectedCardId", selectedCardId));
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                } else {

                    startActivity(new Intent(context, AddNewCardActivity.class)
                            .putExtra("createCustomer", true)
                            .putExtra("setAsDefault", true)
                    );
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                }


            }
        });

        findViewById(R.id.img_taxinfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TaxesDialog(context, strTaxInfo, totaTax).show();
            }
        });

        btn_applycoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_applycoupon.getText().toString().equalsIgnoreCase("Apply")) {
                    showCouponMessage = true;
                    applyCoupon();
                } else {
                    removeCoupon();
                }
            }
        });

        findViewById(R.id.rl_coupon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCouponViewExpanded) {
                    isCouponViewExpanded = false;
                    rl_coupon_details.setVisibility(View.GONE);
                    img_dropdown_coupon.setRotation(0);
                } else {
                    isCouponViewExpanded = true;
                    rl_coupon_details.setVisibility(View.VISIBLE);
                    img_dropdown_coupon.setRotation(180);
                }
            }
        });

//        findViewById(R.id.txt_changedate).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Calendar minPickerDate = Calendar.getInstance();
//                Calendar maxPickerDate = Calendar.getInstance();
//                maxPickerDate.add(Calendar.DAY_OF_YEAR, 14);
//
//                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//                                txt_pickup_date.setText(AppUtils.getMonthString(monthOfYear) + " " + AppUtils.checkForDoubleDigits(dayOfMonth) + ", " + year);
//                                pickUpDate = year + "-" + AppUtils.checkForDoubleDigits(monthOfYear + 1) + "-" + AppUtils.checkForDoubleDigits(dayOfMonth);
//
//                                setDayFlag = 0;
//
//                                getOpeningHoursOfHall(AppUtils.checkForDoubleDigits(dayOfMonth) + "-" + AppUtils.checkForDoubleDigits(monthOfYear + 1) + "-" + year);
//
//                                checkForValidTime(true);
//                            }
//                        },
//                        minPickerDate.get(Calendar.YEAR),
//                        minPickerDate.get(Calendar.MONTH),
//                        minPickerDate.get(Calendar.DAY_OF_MONTH)
//                );
//
//                datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
//                datePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
//                datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
//
//                datePickerDialog.setMinDate(minPickerDate);
//                datePickerDialog.setMaxDate(maxPickerDate);
//
//            }
//        });

//        findViewById(R.id.txt_changetime).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (isHallClosed) {
//
//                    showToast(hallName + " is closed for selected date/time.");
//
//                } else {
//
//                    checkForValidTime(true);
//
//                    TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
//                            new TimePickerDialog.OnTimeSetListener() {
//                                @Override
//                                public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
//
//                                    String strTime = AppUtils.change24hrsTo12HrsFormat(AppUtils.checkForDoubleDigits(hourOfDay) + ":" + AppUtils.checkForDoubleDigits(minute));
//                                    txt_pickup_time.setText(strTime);
//                                    pickUpTime = AppUtils.checkForDoubleDigits(hourOfDay) + ":" + AppUtils.checkForDoubleDigits(minute) + ":00";
//                                }
//                            },
//                            tpMinHour,
//                            tpMinMinute,
//                            false
//                    );
//                    timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
//                    timePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
//
//                    timePickerDialog.setMinTime(tpMinHour, tpMinMinute, 00);
//                    timePickerDialog.setMaxTime(tpMaxHour, tpMaxMinute, 00);
//                    timePickerDialog.show(getFragmentManager(), "TimePickerDialog");
//
//                }
//
//            }
//        });

//        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                AppCompatRadioButton checkedRadioButton = (AppCompatRadioButton) group.findViewById(checkedId);
//                boolean isChecked = checkedRadioButton.isChecked();
//
//                if (checkedId == R.id.rdbtn_immediate && isChecked) {
//
//                    ll_pickup.setVisibility(View.GONE);
//                    pickUpMethod = "immediate";
//                    pickUpDate = "";
//                    pickUpTime = "";
//
//                    checkForValidTime(false);
//
//                } else {
//
//                    setDayFlag = 1;
//
//                    ll_pickup.setVisibility(View.VISIBLE);
//                    pickUpMethod = "later";
//                    checkForValidTime(false);
//
//                }
//            }
//        });

    }


    // TODO Fragment Methods

    @Override
    protected void onDestroy() {
        try { context.unregisterReceiver(netwatcher); }catch (Exception e){}
        try { context.unregisterReceiver(broadcast_reciever); }catch (Exception e){}
        try { savedCardsPresenter.detachView(); } catch (Exception e) {}
        super.onDestroy();
    }

    private void calculateLocalCartItemsTax() {

        if (isInternetAvailable()) {
            showProgressDialog();
            String coupon = "";
            if (!"".equals(getSessionManager().getLocalCartCouponName())) {
                coupon = getSessionManager().getLocalCartCouponName();
            } else {
                coupon = edt_coupon.getText().toString();
            }

            cartPresenter.getLocalCartItemsTax(getSessionManager().getLocalCartHallId(), String.valueOf(totalCartItemsCost), coupon);
        } else {
            showToast(getString(R.string.api_error_internet));
        }

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if(isConnected && view_nointernet.getVisibility()==View.VISIBLE){

            view_nointernet.setVisibility(View.GONE);

            if (getSessionManager().isUserSkippedLoggedIn()) {
                loadLocalCartData();
            } else {
                loadData();
                loadSavedCards();
            }

        }

    }

    private void updateDishQuantityWithDelay() {

        if (timerHandler != null) {
            timerHandler.removeCallbacks(timerRunnable);
        }

        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                updateCart();
                timerHandler.removeCallbacks(timerRunnable);
            }
        };

        timerHandler.postDelayed(timerRunnable, 500);

    }

    private void updateCart() {

        JsonArray itemsArray = new JsonArray();

        for (int i = 0; i < cartList.size(); i++) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", cartList.get(i).getId());
            jsonObject.addProperty("qty", cartList.get(i).getQty());
            itemsArray.add(jsonObject);
        }

        if (isInternetAvailable()) {

            showProgressDialog();
            cartPresenter.updateCartDishQuantity(getSessionManager().getUserId(), itemsArray);

        } else {
            showToast(getString(R.string.api_error_internet));
        }


    }

    private void loadSavedCards() {

        if (isInternetAvailable()) {

            stripeCustomerId = getSessionManager().getUserDetailsResponse().getData().getStripe_id();

            if (!AppUtils.isStringEmpty(stripeCustomerId)) {

                try {
                    showProgressDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                savedCardsPresenter.getAllCards(stripeCustomerId);
            } else {
                rl_card.setVisibility(View.VISIBLE);
            }

            view_nointernet.setVisibility(View.GONE);

        } else {
            view_nointernet.setVisibility(View.VISIBLE);
        }

    }

    private void applyCoupon() {

        if (AppUtils.isEditTextEmpty(edt_coupon)) {
            if(showCouponMessage) {
                showToast(getString(R.string.emptyCouponCode));
            }
            return;
        }

        if(showCouponMessage || (!showCouponMessage && !btn_applycoupon.getText().toString().equalsIgnoreCase("Apply"))) {


            if (getSessionManager().isUserSkippedLoggedIn()) {

                calculateLocalCartItemsTax();

            } else {

                if (isInternetAvailable()) {
                    showProgressDialog();
                    cartPresenter.applyCoupon(getSessionManager().getUserId(), hallId, edt_coupon.getText().toString());
                } else {
                    showToast(getString(R.string.api_error_internet));
                }

            }
        }

    }

    private void removeCoupon() {

        if (getSessionManager().isUserSkippedLoggedIn()) {

            /* Save local cart coupon name */
            getSessionManager().setLocalCartCouponName("");
            edt_coupon.setText("");

            calculateLocalCartItemsTax();

        } else {
            if (isInternetAvailable()) {
                showProgressDialog();
                cartPresenter.removeCoupon(getSessionManager().getUserId());
            } else {
                showToast(getString(R.string.api_error_internet));
            }
        }

    }

    private void loadData() {

        if (isInternetAvailable()) {

            try {
                showProgressDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }

            cartPresenter.getCartItems(getSessionManager().getUserId());
            view_nointernet.setVisibility(View.GONE);

        } else {
            view_nointernet.setVisibility(View.VISIBLE);
        }

    }

    private void loadLocalCartData() {

        ArrayList<Items> itemsList = getSessionManager().getLocalCartItems();
        if (null != itemsList) {
            cartList = itemsList;
        } else {
            getSessionManager().setLocalcarthallId("");
            cartList = new ArrayList<>();
        }

        hallId = getSessionManager().getLocalCartHallId();
        totalItemsInCart = 0;

        /* Calculate total no of items in the cart */
        if (cartList != null & cartList.size() > 0) {
            for (int ss = 0; ss < cartList.size(); ss++) {
                totalItemsInCart += Integer.parseInt(itemsList.get(ss).getQty());
            }
        }

        getSessionManager().setCartCount("" + totalItemsInCart);
        checkAndSetDishAvailability();
        cartAdapter.updateList(cartList);

        if (cartList.size() == 0) {
            nestedScrollView.setVisibility(View.GONE);
            view_nodata.setVisibility(View.VISIBLE);
        } else {
            nestedScrollView.setVisibility(View.VISIBLE);
            view_nodata.setVisibility(View.GONE);
        }


    }

    private void checkAndSetDishAvailability(){

        for (int i = 0; i < cartList.size(); i++) {
            if(cartList.get(i).getStatus().equals("1")){
                boolean isAnyMealIdActive = AppUtils.isAnyMealIdActive(hallId, cartList.get(i).getMeal_ids(), getSessionManager().getHallsResponse());
                if(!isAnyMealIdActive){
                    cartList.get(i).setStatus("");
                }
            }
        }

    }

    private boolean isAllDishesAvailable(){

        boolean status = true;
        for(int i=0; i<cartList.size(); i++) {

            if (!cartList.get(i).getStatus().equalsIgnoreCase("1")) {
                status = false;
            }
        }

        return status;

    }

    private void checkAndSetPlaceOrderButtonStatus(boolean showMessage){

        checkAndSetDishAvailability();
        cartAdapter.updateList(cartList);

        btn_place.setBackgroundResource(R.drawable.bg_primary_round_complete);

        HomeDishesResponse.Halls[] halls = getSessionManager().getHallsResponse();
        isHallClosed = AppUtils.isHallCurrentlyClosed(hallId, halls);

        if (getSessionManager().isUserSkippedLoggedIn()) {
            btn_place.setText("Login to Place Order");
        } else {
            btn_place.setText("Place Order");
        }

        if (!isAllDishesAvailable()) {

            btn_place.setVisibility(View.VISIBLE);
            btn_place.setBackgroundResource(R.drawable.bg_grey_round_complete);
            if(showMessage) {
                showToast("Some of the dish(s) in your cart are currently unavailable. Please remove the unavailable dish(s) to proceed.");
            }

        }

        if (isHallClosed) {

            btn_place.setVisibility(View.VISIBLE);
            btn_place.setText("Hall is closed");
            btn_place.setBackgroundResource(R.drawable.bg_grey_round_complete);
            if(showMessage) {
                showToast("Hall is closed.");
            }

        }

    }

    private void placeOrderDialog(){

        progressvalue = 0f;

        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_placeorder);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView txt_cancel_order  = dialog.findViewById(R.id.txt_cancel_order);
        final RoundCornerProgressBar progressbar  = dialog.findViewById(R.id.progressbar);

        timerHandler_PlaceOrder = new Handler();
        timerRunnable_PlaceOrder = new Runnable() {
            @Override
            public void run() {
                if(progressvalue == 5000){
                    dialog.dismiss();
                    timerHandler_PlaceOrder.removeCallbacks(timerRunnable_PlaceOrder);

                    checkAndSetPlaceOrderButtonStatus(true);

                    if(!isHallClosed && isAllDishesAvailable()){
                        placeOrderFinally();
                    }

                }else {
                    progressvalue += 50f;
                    progressbar.setProgress(progressvalue);
                    timerHandler_PlaceOrder.postDelayed(timerRunnable_PlaceOrder, 50);
                }
            }
        };

        timerHandler_PlaceOrder.postDelayed(timerRunnable_PlaceOrder, 00);

        txt_cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerHandler_PlaceOrder.removeCallbacks(timerRunnable_PlaceOrder);
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void placeOrderFinally(){

        if (isInternetAvailable()) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("stripe_customer_id", getSessionManager().getUserDetailsResponse().getData().getStripe_id());
            jsonObject.addProperty("stripe_card_id", selectedCardId);

            String strPickUp = "immediate";
            if (pickUpMethod.equalsIgnoreCase("immediate")) {
                strPickUp = "immediate";
            } else {
                strPickUp = pickUpDate + " " + pickUpTime;
                strPickUp = AppUtils.changeTimeFormat(strPickUp);
            }

            showProgressDialog();
            cartPresenter.placeOrder(
                    getSessionManager().getUserId(),
                    hallId,
                    edt_instructions.getText().toString(),
                    "Stripe",
                    jsonObject.toString(),
                    strPickUp
            );

        } else {
            showToast(getString(R.string.api_error_internet));
        }

    }

    public void showNewSelectedCard(String selectedCardId){

        this.selectedCardId = selectedCardId;

        for (int i = 0; i < savedCardsList.size(); i++) {

            String cardId = savedCardsList.get(i).getId();
            if (cardId.equals(selectedCardId)) {
                txt_cardno.setText("xxxx-xxxx-xxxx-" + savedCardsList.get(i).getLast4());
                String cardBrand = savedCardsList.get(i).getBrand();
                img_card.setImageResource(AppUtils.getCardImage(cardBrand));
                rl_card.setVisibility(View.VISIBLE);
                btn_place.setVisibility(View.VISIBLE);
            }

        }

    }


    // TODO Interface Methods

    @Override
    public void onClick_Dish(String type, int position, String dishId) {

        switch (type) {

            case "increaseQuantity":

                int quantity1 = Integer.parseInt(cartList.get(position).getQty());
                quantity1++;
                cartList.get(position).setQty(String.valueOf(quantity1));
                cartAdapter.updateList(cartList);

                if (getSessionManager().isUserSkippedLoggedIn()) {
                    getSessionManager().setLocalCartItems(cartList);
                    /* Calculate total no of items in the cart */
                    totalCartItemCount();
                } else {
                    updateDishQuantityWithDelay();
                }

                break;

            case "decreaseQuantity":

                int quantity2 = Integer.parseInt(cartList.get(position).getQty());
                if (quantity2 > 1) {
                    quantity2--;
                }
                cartList.get(position).setQty(String.valueOf(quantity2));
                cartAdapter.updateList(cartList);

                if (getSessionManager().isUserSkippedLoggedIn()) {
                    getSessionManager().setLocalCartItems(cartList);
                    /* Calculate total no of items in the cart */
                    totalCartItemCount();
                } else {
                    updateDishQuantityWithDelay();
                }

                break;

            case "openDish":

                Gson gson = new Gson();

                Addons[] addons = cartList.get(position).getAddons();
                Options[] options = cartList.get(position).getOptions();

                startActivity(new Intent(context, DishDetailsActivity.class)
                        .putExtra("itemId", cartList.get(position).getId())
                        .putExtra("dishId", dishId)
                        .putExtra("addOns", gson.toJson(addons))
                        .putExtra("options", gson.toJson(options))
                        .putExtra("quantity", cartList.get(position).getQty())
                        .putExtra("isDishOpenedFromCart", true)
                );
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                break;

            case "removeDish":

                if (getSessionManager().isUserSkippedLoggedIn()) {

                    cartList.remove(position);
                    getSessionManager().setLocalCartItems(cartList);
                    cartAdapter.updateList(cartList);

                    totalCartItemCount();

                    if (cartList.size() == 0) {
                        getSessionManager().setCartInstructions("");
                        getSessionManager().setLocalCartCouponName("");
                        nestedScrollView.setVisibility(View.GONE);
                        view_nodata.setVisibility(View.VISIBLE);
                    } else {
                        nestedScrollView.setVisibility(View.VISIBLE);
                        view_nodata.setVisibility(View.GONE);
                    }

                } else {

                    if (isInternetAvailable()) {
                        showProgressDialog();
                        cartPresenter.removeFromCart(getSessionManager().getUserId(), dishId);
                    } else {
                        showToast(getString(R.string.api_error_internet));
                    }
                }

                break;
        }

    }

    private void totalCartItemCount() {
        totalItemsInCart = 0;
        /* Calculate total no of items in the cart */
        for (int ss = 0; ss < cartList.size(); ss++) {
            totalItemsInCart += Integer.parseInt(cartList.get(ss).getQty());

        }
        getSessionManager().setCartCount(String.valueOf(totalItemsInCart));

    }

    @Override
    public void onSuccess_CartItems(String message, CartResponse viewCartResponse) {

        showCouponMessage = false;
        applyCoupon();

        hideProgressDialog();

        cartList = new ArrayList<>();

        if (null != viewCartResponse.getData()) {

            if (null != viewCartResponse.getData().getCart()) {

                nestedScrollView.setVisibility(View.VISIBLE);

                mealIds = viewCartResponse.getData().getCart().getMeal_id();

                strTaxInfo = viewCartResponse.getData().getCart().getTax_description();
                totaTax = viewCartResponse.getData().getCart().getTax();

                String strTotal = viewCartResponse.getData().getCart().getCart_total();
                double tmp1 = Double.parseDouble(strTotal);
                strTotal = formater.format(tmp1);

                double value = Double.parseDouble(strTotal);
                String strCartPrice = String.format("%.2f", value);
                txt_total.setText(context.getString(R.string.currencySymbol) + " " + strCartPrice);
                getSessionManager().setCartPrice(strCartPrice);

                String strTax = viewCartResponse.getData().getCart().getTax();
                double tmp2 = Double.parseDouble(strTax);
                strTax = formater.format(tmp2);

                value = Double.parseDouble(strTax);

               /* if (value == 0) {
                    img_taxinfo.setVisibility(View.INVISIBLE);
                } else {
                    img_taxinfo.setVisibility(View.VISIBLE);
                }*/

                txt_taxes.setText("" + (context.getString(R.string.currencySymbol) + " " + String.format("%.2f", value)));

                String strToPay = viewCartResponse.getData().getCart().getGrand_total();
                double tmp3 = Double.parseDouble(strToPay);
                strToPay = formater.format(tmp3);

                value = Double.parseDouble(strToPay);
                txt_topay.setText(context.getString(R.string.currencySymbol) + " " + String.format("%.2f", value));
                //txt_topay.setText(context.getString(R.string.currencySymbol) + strToPay);

                if (viewCartResponse.getData().getCart().getCoupon_code().trim().length() != 0) {
                    String strDiscount = viewCartResponse.getData().getCart().getDiscount();
                    double tmp4 = Double.parseDouble(strDiscount);
                    strDiscount = formater.format(tmp4);
                    rl_discount.setVisibility(View.VISIBLE);

                    value = Double.parseDouble(strDiscount);
                    txt_discount.setText("-" + context.getString(R.string.currencySymbol) + " " + String.format("%.2f", value));
                    isCouponViewExpanded = true;
                    rl_coupon_details.setVisibility(View.VISIBLE);
                    img_dropdown_coupon.setRotation(180);
                    btn_applycoupon.setText("Remove");
                    edt_coupon.setEnabled(false);
                    edt_coupon.setText(viewCartResponse.getData().getCart().getCoupon_code());
                } else {
                   /* isCouponViewExpanded = false;
                    img_dropdown_coupon.setRotation(0);
                    btn_applycoupon.setText("Apply");
                    rl_coupon_details.setVisibility(View.GONE);*/
                    edt_coupon.setEnabled(true);
                    rl_discount.setVisibility(View.GONE);
                }

                Cart cart = viewCartResponse.getData().getCart();

                Log.e("getHall_id??","getHall_id??"+cart.getHall_id());

                hallId = cart.getHall_id();
                hallName = cart.getHall_name();

                if (null != cart.getItems()) {

                    Items[] items = viewCartResponse.getData().getCart().getItems();

                    if (items.length != 0) {

                        for (int i = 0; i < items.length; i++) {
                            cartList.add(items[i]);
                        }
                    }
                }

            }

        }

        getSessionManager().setCartCount("" + viewCartResponse.getData().getCart_count());
        checkAndSetDishAvailability();
        cartAdapter.updateList(cartList);

        if (cartList.size() == 0) {
            nestedScrollView.setVisibility(View.GONE);
            view_nodata.setVisibility(View.VISIBLE);
        } else {
            nestedScrollView.setVisibility(View.VISIBLE);
            view_nodata.setVisibility(View.GONE);
            checkAndSetPlaceOrderButtonStatus(false);
        }

    }

    @Override
    public void onSuccess_Timings(String message, TimingsResponse response) {

        if(null!= response.getData()){
            if(null!= response.getData().getHalls()){
                getSessionManager().setHallsTimingsResponse(response.getData().getHalls());
                HomeFragment.getInstance().checkAndSetHallCloseStatus();
                Intent intent = new Intent("checkMealAvailability");
                context.sendBroadcast(intent);
            }
        }

    }

    @Override
    public void onSuccess_ApplyCoupon(String message, CouponResponse couponResponse) {

        if(showCouponMessage){
            showToast(message);
        }

        hideProgressDialog();

        rl_discount.setVisibility(View.VISIBLE);
        btn_applycoupon.setText("Remove");

        edt_coupon.setEnabled(false);

        strTaxInfo = couponResponse.getData().getTax_description();
        totaTax = couponResponse.getData().getTax();

        String strTotal = couponResponse.getData().getCart_total();
        double tmp1 = Double.parseDouble(strTotal);
        strTotal = formater.format(tmp1);

        double value = Double.parseDouble(strTotal);
        txt_total.setText(context.getString(R.string.currencySymbol) + " " + String.format("%.2f", value));
        //txt_total.setText(context.getString(R.string.currencySymbol) + strTotal);

        String strTax = couponResponse.getData().getTax();
        double tmp2 = Double.parseDouble(strTax);
        strTax = formater.format(tmp2);

        value = Double.parseDouble(strTax);

        if (value == 0) {
            img_taxinfo.setVisibility(View.INVISIBLE);
        } else {
            img_taxinfo.setVisibility(View.VISIBLE);
        }

        txt_taxes.setText("" + (context.getString(R.string.currencySymbol) + " " + String.format("%.2f", value)));
        //txt_taxes.setText(context.getString(R.string.currencySymbol) + strTax);

        String strToPay = couponResponse.getData().getGrand_total();
        double tmp3 = Double.parseDouble(strToPay);
        strToPay = formater.format(tmp3);

        value = Double.parseDouble(strToPay);
        txt_topay.setText(context.getString(R.string.currencySymbol) + " " + String.format("%.2f", value));
        //txt_topay.setText(context.getString(R.string.currencySymbol) + strToPay);

        String strDiscount = couponResponse.getData().getDiscount();
        double tmp4 = Double.parseDouble(strDiscount);
        strDiscount = formater.format(tmp4);

        value = Double.parseDouble(strDiscount);
        txt_discount.setText("-" + context.getString(R.string.currencySymbol) + " " + String.format("%.2f", value));

    }

    @Override
    public void onSuccess_PlaceOrder(String message, String orderId) {

        hideProgressDialog();

        startActivity(new Intent(context, OrderSuccessActivity.class).putExtra("orderId", orderId));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

        finish();

        getSessionManager().setCartInstructions("");
        getSessionManager().setCartCount("0");
        getSessionManager().setCartPrice("0");

    }

    @Override
    public void onSuccess_RemoveCoupon(String message, CouponResponse couponResponse) {

        hideProgressDialog();
        btn_applycoupon.setText("Apply");
        edt_coupon.setText("");
        rl_discount.setVisibility(View.GONE);

        edt_coupon.setEnabled(true);

        strTaxInfo = couponResponse.getData().getTax_description();
        totaTax = couponResponse.getData().getTax();

        String strTotal = couponResponse.getData().getCart_total();
        double tmp1 = Double.parseDouble(strTotal);
        strTotal = formater.format(tmp1);

        double value = Double.parseDouble(strTotal);
        txt_total.setText(context.getString(R.string.currencySymbol) + " " + String.format("%.2f", value));
        //txt_total.setText(context.getString(R.string.currencySymbol) + strTotal);

        String strTax = couponResponse.getData().getTax();
        double tmp2 = Double.parseDouble(strTax);
        strTax = formater.format(tmp2);

        value = Double.parseDouble(strTax);

        if (value == 0) {
            img_taxinfo.setVisibility(View.INVISIBLE);
        } else {
            img_taxinfo.setVisibility(View.VISIBLE);
        }

        txt_taxes.setText("" + (context.getString(R.string.currencySymbol) + " " + String.format("%.2f", value)));
        //txt_taxes.setText(context.getString(R.string.currencySymbol) + strTax);

        String strToPay = couponResponse.getData().getGrand_total();
        double tmp3 = Double.parseDouble(strToPay);
        strToPay = formater.format(tmp3);

        value = Double.parseDouble(strToPay);
        txt_topay.setText(context.getString(R.string.currencySymbol) + " " + String.format("%.2f", value));
        //txt_topay.setText(context.getString(R.string.currencySymbol) + strToPay);

        txt_discount.setText("");

    }

    @Override
    public void onSuccess_LocalCartTax(String message, LocalCartTaxResponse viewLocalCartResponse) {

        hideProgressDialog();

        float totlCartTax = 0f;

        double value = totalCartItemsCost;
        String strCartPrice = String.format("%.2f", value);
        txt_total.setText(context.getString(R.string.currencySymbol) + " " + strCartPrice);
        getSessionManager().setCartPrice(strCartPrice);

        btn_place.setVisibility(View.VISIBLE);

        if (getSessionManager().isUserSkippedLoggedIn()) {
            btn_place.setText("Login to Place Order");
        } else {
            btn_place.setText("Place Order");
        }

        if (null != viewLocalCartResponse.getData()) {

            /* Coupon data *//*

            String couponName = getSessionManager().getLocalCartCouponName();

            if (couponName.trim().length() != 0) {

                if (couponName.trim().length() == 0) {
                    btn_applycoupon.setText("Apply");
                    edt_coupon.setEnabled(true);
                } else {
                    btn_applycoupon.setText("Remove");
                    edt_coupon.setEnabled(false);
                    edt_coupon.setText(getSessionManager().getLocalCartCouponName());
                }

            }*/

            /* Tax data */

            if (null != viewLocalCartResponse.getData().getTax_data()) {

                nestedScrollView.setVisibility(View.VISIBLE);

                LocalCartTaxResponse.Tax_data[] totalLocalCartTax = viewLocalCartResponse.getData().getTax_data();

                JsonArray taxArray = new JsonArray();

                for (int i = 0; i < totalLocalCartTax.length; i++) {

                    String taxTitle = totalLocalCartTax[i].getTitle();
                    String taxValue = totalLocalCartTax[i].getPercentage();
                    totlCartTax += Float.parseFloat(taxValue);

                    JsonObject taxObject = new JsonObject();
                    taxObject.addProperty("title", taxTitle);
                    taxObject.addProperty("percentage", taxValue);
                    taxArray.add(taxObject);
                }

                if (totalLocalCartTax.length != 0) {
                    strTaxInfo = taxArray.toString();
                } else {
                    strTaxInfo = "";
                }

                totaTax = String.format("%.2f", totlCartTax);

                float totalPay = totalCartItemsCost + totlCartTax;

                value = totlCartTax;

                if (value == 0) {
                    img_taxinfo.setVisibility(View.INVISIBLE);
                } else {
                    img_taxinfo.setVisibility(View.VISIBLE);
                }

                txt_taxes.setText("" + (context.getString(R.string.currencySymbol) + " " + String.format("%.2f", value)));

                value = totalPay;
                txt_topay.setText(context.getString(R.string.currencySymbol) + " " + String.format("%.2f", value));

                /* Coupon Code */



                if(null!=viewLocalCartResponse.getData().getCoupon_data()) {

                    LocalCartTaxResponse.Coupon_data discount = viewLocalCartResponse.getData().getCoupon_data();

                    String status = discount.getStatus();

                    if(status.equalsIgnoreCase("true")){

                        rl_discount.setVisibility(View.VISIBLE);
                        btn_applycoupon.setText("Remove");

                        String discountPrice = discount.getDiscount();
                        String couponCodeName = discount.getCoupon_code();
                        String amountAfterDiscount = discount.getGrand_total();

                        edt_coupon.setText(couponCodeName);
                        edt_coupon.setEnabled(false);
                        getSessionManager().setLocalCartCouponName(couponCodeName);
                        edt_coupon.setText(couponCodeName);

                        value = Double.parseDouble(discountPrice);
                        txt_discount.setText("-" + context.getString(R.string.currencySymbol) + " " + String.format("%.2f", value));

                        value = Double.parseDouble(amountAfterDiscount);
                        double temp = Double.parseDouble(totaTax);

                        txt_topay.setText(context.getString(R.string.currencySymbol) + " " + String.format("%.2f", (value+temp)));

                        if(edt_coupon.getText().toString().trim().length()!=0) {
                            showToast(discount.getErrors());
                        }

                    }else {

                        rl_discount.setVisibility(View.GONE);
                        edt_coupon.setEnabled(true);
                        btn_applycoupon.setText("Apply");
                        if(edt_coupon.getText().toString().trim().length()!=0) {
                            showToast(discount.getErrors());
                            edt_coupon.setText("");
                            getSessionManager().setLocalCartCouponName("");
                        }

                    }


                }


                if (cartList.size() == 0) {
                    nestedScrollView.setVisibility(View.GONE);
                    view_nodata.setVisibility(View.VISIBLE);
                } else {
                    nestedScrollView.setVisibility(View.VISIBLE);
                    view_nodata.setVisibility(View.GONE);
                }

            }
        }

    }

    @Override
    public void onError_applyCoupon(String message) {
        edt_coupon.setText("");
        hideProgressDialog();
        if(showCouponMessage){
            showToast(message);
        }
    }

    @Override
    public void onError_placeOrder(String message) {
        hideProgressDialog();
        showToast(message);
        loadData();
    }

    @Override
    public void onSuccess_CardsList(ArrayList<StripeSavedCardsListResponse.Data> savedCardsList) {

        this.savedCardsList = savedCardsList;

        hideProgressDialog();

        String defaultCardId = getSessionManager().getUserDetailsResponse().getData().getDefault_card_id();

        if(AppUtils.isStringEmpty(selectedCardId)){
            selectedCardId = defaultCardId;
        }

        boolean isDefaultCardPositionFound = false;

        btn_place.setVisibility(View.VISIBLE);

        for (int i = 0; i < savedCardsList.size(); i++) {

            String cardId = savedCardsList.get(i).getId();
            if (cardId.equals(selectedCardId)) {
                txt_cardno.setText("xxxx-xxxx-xxxx-" + savedCardsList.get(i).getLast4());
                String cardBrand = savedCardsList.get(i).getBrand();
                img_card.setImageResource(AppUtils.getCardImage(cardBrand));
                isDefaultCardPositionFound = true;
                rl_card.setVisibility(View.VISIBLE);
                btn_place.setVisibility(View.VISIBLE);
            }

        }

        // if default card is not present but cardlist > 0
        // then setting first card as default.
        if (savedCardsList.size() > 0 && isDefaultCardPositionFound == false) {
            txt_cardno.setText("xxxx-xxxx-xxxx-" + savedCardsList.get(0).getLast4());
            String cardBrand = savedCardsList.get(0).getBrand();
            img_card.setImageResource(AppUtils.getCardImage(cardBrand));
            rl_card.setVisibility(View.VISIBLE);
            btn_place.setVisibility(View.VISIBLE);
            selectedCardId = savedCardsList.get(0).getId();
        }

        if (savedCardsList.size() == 0) {
            txt_cardno.setText("xxxx-xxxx-xxxx-xxxx");
            img_card.setImageResource(R.drawable.ic_card_default);
            txt_change.setText("ADD CARD");
            rl_card.setVisibility(View.VISIBLE);
            btn_place.setVisibility(View.VISIBLE);
        } else {
            txt_change.setText("CHANGE");
            rl_card.setVisibility(View.VISIBLE);
            btn_place.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onSuccess_DeleteCard(String cardId) {
        // Not Used Here
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void onError(String message) {
        hideProgressDialog();
        showToast(message);
    }

    @Override
    public void setTotalCostOfCartItems(float cartcost) {

        totalCartItemsCost = cartcost;

        if (getSessionManager().isUserSkippedLoggedIn()) {
            calculateLocalCartItemsTax();
        }

    }

    @Override
    public void onSuccess_AddToCart(String message, String cartCount, String cartPrice) {

    }

    @Override
    public void onError_AddToCart(String message, boolean isDishFromDifferentHall, boolean isDishFromDifferentMeal, String hallName, String mealName) {

    }

    // TODO Un Used Methods

//    private void checkForValidTime(boolean isSelectedDate) {
//
//        Date dateCurrentTime = null;
//
//        Calendar now = Calendar.getInstance();
//
//        if (!isSelectedDate) {
//
//            txt_pickup_date.setText(AppUtils.getMonthString(now.get(Calendar.MONTH)) + " " + AppUtils.checkForDoubleDigits(now.get(Calendar.DAY_OF_MONTH)) + ", " + now.get(Calendar.YEAR));
//
//            pickUpDate = now.get(Calendar.YEAR) + "-" + AppUtils.checkForDoubleDigits(now.get(Calendar.MONTH) + 1) + "-" + AppUtils.checkForDoubleDigits(now.get(Calendar.DAY_OF_MONTH));
//            todayDate = now.get(Calendar.YEAR) + "-" + AppUtils.checkForDoubleDigits(now.get(Calendar.MONTH) + 1) + "-" + AppUtils.checkForDoubleDigits(now.get(Calendar.DAY_OF_MONTH));
//
//            getOpeningHoursOfHall(AppUtils.checkForDoubleDigits(now.get(Calendar.DAY_OF_MONTH)) + "-" + AppUtils.checkForDoubleDigits(now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR));
//
//            dateCurrentTime = AppUtils.getTimeFrom24HoursString(AppUtils.checkForDoubleDigits(now.get(Calendar.HOUR_OF_DAY)) + ":" + AppUtils.checkForDoubleDigits(now.get(Calendar.MINUTE)));
//            String strTime = AppUtils.change24hrsTo12HrsFormat(AppUtils.checkForDoubleDigits(now.get(Calendar.HOUR_OF_DAY)) + ":" + AppUtils.checkForDoubleDigits(now.get(Calendar.MINUTE)));
//            txt_pickup_time.setText(strTime);
//
//        } else {
//
//            if (todayDate.equals(pickUpDate)) {
//                dateCurrentTime = AppUtils.getTimeFrom24HoursString(AppUtils.checkForDoubleDigits(now.get(Calendar.HOUR_OF_DAY)) + ":" + AppUtils.checkForDoubleDigits(now.get(Calendar.MINUTE)));
//            } else {
//                dateCurrentTime = AppUtils.getTimeFrom12HoursString(hallOpeningTime);
//            }
//
//        }
//
//        Date dateHallOpeningTime = AppUtils.getTimeFrom12HoursString(hallOpeningTime);
//        Date dateHallClosingTime = AppUtils.getTimeFrom12HoursString(hallClosingTime);
//
//        /* Comparing values */
//
//        if (setDayFlag == 1) {
//
//            if (dateCurrentTime.equals(dateHallOpeningTime) || (dateCurrentTime.after(dateHallOpeningTime)
//                    && dateCurrentTime.before(dateHallClosingTime))) {
//
//                isHallClosed = false;
//
//                txt_pickup_time.setText(AppUtils.getTime(dateCurrentTime));
//                pickUpTime = AppUtils.getTime(dateCurrentTime);
//
//                String str1 = AppUtils.getTimeFromDate(dateCurrentTime);
//                tpMinHour = Integer.parseInt(str1.split(":")[0]);
//                tpMinMinute = Integer.parseInt(str1.split(":")[1]);
//
//                String str2 = AppUtils.getTimeFromDate(dateHallClosingTime);
//                tpMaxHour = Integer.parseInt(str2.split(":")[0]);
//                tpMaxMinute = Integer.parseInt(str2.split(":")[1]);
//
//                btn_place.setVisibility(View.VISIBLE);
//                if(getSessionManager().isUserSkippedLoggedIn()){
//                    btn_place.setText("Login to Place Order");
//                }else {
//                    btn_place.setText("Place Order");
//                }
//
//                btn_place.setBackgroundResource(R.drawable.bg_primary_round_complete);
//
//            }
//
//            if (dateCurrentTime.before(dateHallOpeningTime)) {
//
//                isHallClosed = false;
//
//                txt_pickup_time.setText(AppUtils.getTime(dateCurrentTime));
//                pickUpTime = AppUtils.getTime(dateCurrentTime);
//
//                String str1 = AppUtils.getTimeFromDate(dateHallOpeningTime);
//                tpMinHour = Integer.parseInt(str1.split(":")[0]);
//                tpMinMinute = Integer.parseInt(str1.split(":")[1]);
//
//                String str2 = AppUtils.getTimeFromDate(dateHallClosingTime);
//                tpMaxHour = Integer.parseInt(str2.split(":")[0]);
//                tpMaxMinute = Integer.parseInt(str2.split(":")[1]);
//
//                btn_place.setVisibility(View.VISIBLE);
//                if(getSessionManager().isUserSkippedLoggedIn()){
//                    btn_place.setText("Login to Place Order");
//                }else {
//                    btn_place.setText("Place Order");
//                }
//                btn_place.setBackgroundResource(R.drawable.bg_primary_round_complete);
//
//            }
//
//            if (dateCurrentTime.after(dateHallClosingTime) || dateCurrentTime.equals(dateHallClosingTime)) {
//
//                isHallClosed = true;
//
//                if(!btn_place.getText().toString().equalsIgnoreCase("Hall is closed")) {
//                    showToast(hallName + " is closed for selected date/time.");
//                }
//                //showToast(getString(R.string.hall_closed_error));
//
//
//                if (!isSelectedDate) {
//
//                } else {
//                    /*pickUpDate = "";
//                    pickUpTime = "";
//                    txt_pickup_date.setText("");
//                    txt_pickup_time.setText("");*/
//                }
//
//                btn_place.setVisibility(View.VISIBLE);
//                btn_place.setText("Hall is closed");
//                btn_place.setBackgroundResource(R.drawable.bg_grey_round_complete);
//
//            }
//        } else {
//
//            isHallClosed = true;
//
//            if(!btn_place.getText().toString().equalsIgnoreCase("Hall is closed")) {
//                showToast(hallName + " is closed for selected date/time.");
//            }
//            //showToast(getString(R.string.hall_closed_error));
//
//
//            if (!isSelectedDate) {
//
//            } else {
//                /*pickUpDate = "";
//                pickUpTime = "";
//                txt_pickup_date.setText("");
//                txt_pickup_time.setText("");*/
//            }
//
//            btn_place.setVisibility(View.VISIBLE);
//            btn_place.setText("Hall is closed");
//            btn_place.setBackgroundResource(R.drawable.bg_grey_round_complete);
//        }
//    }

//    private void getOpeningHoursOfHall(String selectedDate) {
//
//        try {
//
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.ENGLISH);
//            Date myDate = sdf.parse(selectedDate);
//            sdf.applyPattern("EEE");
//            String selectedDay = sdf.format(myDate);
//
//            HomeDishesResponse.Halls[] halls = getSessionManager().getHallsResponse();
//
//            Log.e("halls.length??", "halls.length??" + halls.length);
//
//            for (int i = 0; i < halls.length; i++) {
//
//                String id = halls[i].getId();
//
//                Log.e("id::" + id, "::hallId::" + hallId);
//
//                if (id.equalsIgnoreCase(hallId)) {
//
//                    HomeDishesResponse.Timing[] timings = halls[i].getTiming();
//
//                    for (int j = 0; j < timings.length; j++) {
//
//                        String strDay = timings[j].getDay();
//
//                        if (strDay.equalsIgnoreCase(selectedDay)) {
////                            hallOpeningTime = timings[j].getStart_time();
////                            hallClosingTime = timings[j].getEnd_time();
//
//                            setDayFlag = 1;
//                        }
//
//                        hallName = halls[i].getName();
//                    }
//
//                }
//            }
//
//        } catch (Exception e) {
//
//        }
//
//    }

}

