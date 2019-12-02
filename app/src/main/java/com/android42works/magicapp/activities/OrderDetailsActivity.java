package com.android42works.magicapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android42works.magicapp.R;
import com.android42works.magicapp.adapters.OrderDetailsAdapter;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.dialogs.RateDialog;
import com.android42works.magicapp.dialogs.TaxesDialog;
import com.android42works.magicapp.interfaces.OrderDetailInterface;
import com.android42works.magicapp.interfaces.RateOrderInterface;
import com.android42works.magicapp.interfaces.SuccessInterface;
import com.android42works.magicapp.presenters.ContactSupportPresenter;
import com.android42works.magicapp.presenters.OrderDetailPresenter;
import com.android42works.magicapp.presenters.RatePresenter;
import com.android42works.magicapp.presenters.ReorderPresenter;
import com.android42works.magicapp.responses.OrderDetailResponse;
import com.android42works.magicapp.responses.OrdersResponse;
import com.android42works.magicapp.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Created by JSP@nesar */

public class OrderDetailsActivity extends BaseActivity implements RateOrderInterface, SuccessInterface, OrderDetailInterface {

    private TextView txt_actionbar_title, txt_order, txt_hall, txt_date_time, txt_total, txtOrderReason,
            txt_discount, txt_taxes, txt_topay, txt_cardno, txt_status, txtOrderStatus;
    private ImageView img_rate, img_card, img_tick;
    private RelativeLayout rl_discount;
    private LinearLayout ll_delivered_cancelled, ll_ongoing, llOrderReason;
    private RecyclerView recycler_dishes;
    private Button btn_rate;
    private DecimalFormat formater = new DecimalFormat("0.00");
    private Gson gson = new Gson();
    private String orderType = "", strTaxInfo = "", totaTax = "", selectedRateThumbVote = "", strOrderData = "", orderId = "";

    private int isCancel = 0;

    private LinearLayout llOrderStatus;

    private OrderDetailResponse.Data orderData;

    private RatePresenter ratePresenter;
    private RateOrderInterface myInterface;
    private ReorderPresenter reorderPresenter;
    private ContactSupportPresenter contactSupportPresenter;
    private OrderDetailPresenter orderDetailPresenter;

    @Override
    protected int getLayoutView() {
        return R.layout.act_order_details;
    }

    @Override
    protected Context getActivityContext() {
        return OrderDetailsActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);
        txt_order = findViewById(R.id.txt_order);
        txt_hall = findViewById(R.id.txt_hall);
        txt_date_time = findViewById(R.id.txt_date_time);
        txt_total = findViewById(R.id.txt_total);
        txt_discount = findViewById(R.id.txt_discount);
        txt_taxes = findViewById(R.id.txt_taxes);
        txt_topay = findViewById(R.id.txt_topay);
        img_rate = findViewById(R.id.img_rate);
        rl_discount = findViewById(R.id.rl_discount);
        ll_ongoing = findViewById(R.id.ll_ongoing);
        ll_delivered_cancelled = findViewById(R.id.ll_delivered_cancelled);
        btn_rate = findViewById(R.id.btn_rate);
        recycler_dishes = findViewById(R.id.recycler_dishes);
        img_card = findViewById(R.id.img_card);
        txt_cardno = findViewById(R.id.txt_cardno);
        txt_status = findViewById(R.id.txt_status);
        txtOrderStatus = findViewById(R.id.txtOrderStatus);
        img_tick = findViewById(R.id.img_tick);
        llOrderStatus = findViewById(R.id.llOrderStatus);
        txtOrderReason = findViewById(R.id.txtOrderReason);
        llOrderReason = findViewById(R.id.llOrderReason);

    }

    @Override
    protected void initData() {

        // TODO initData

        context = OrderDetailsActivity.this;
        myInterface = this;

        txt_actionbar_title.setText("ORDER HISTORY");

        if (getIntent().getStringExtra("orderId") != null) {
            orderId = getIntent().getStringExtra("orderId");
        }

        ratePresenter = new RatePresenter(getAPIInterface());
        ratePresenter.attachView(this);

        contactSupportPresenter = new ContactSupportPresenter(getAPIInterface());
        contactSupportPresenter.attachView(this);

        orderDetailPresenter = new OrderDetailPresenter(getAPIInterface());
        orderDetailPresenter.attachView(this);

        reorderPresenter = new ReorderPresenter(getAPIInterface());
        reorderPresenter.attachView(this);

        if (isInternetAvailable()) {

            showProgressDialog();

            orderDetailPresenter.fetchOrderDetail(orderId);

        } else {
            showToast(getString(R.string.api_error_internet));
        }


    }

    private String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
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

        findViewById(R.id.img_taxinfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TaxesDialog(context, strTaxInfo, totaTax).show();
            }
        });

        findViewById(R.id.btn_status).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context, OrderSuccessActivity.class)
                        .putExtra("orderId", orderData.getId())
                        .putExtra("orderNumber", orderData.getOrder_number())
                );

                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (orderData.getCancel_request().equalsIgnoreCase("0")) {
                    isCancel = 1;
                    sendCancelOrderRequest(orderData.getId(), orderData.getHall_id());
                } else if (orderData.getCancel_request().equalsIgnoreCase("1")) {
                    showToast("Cancel request has already been sent to the admin. Please wait for approval.");
                }

            }
        });

        findViewById(R.id.btn_reorder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetAvailable()) {
                    showProgressDialog();
                    reorderPresenter.reorder(orderData.getId());
                } else {
                    showToast(getString(R.string.api_error_internet));
                }

            }
        });

        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String items = "";

                OrderDetailResponse.Items[] itemsArray = orderData.getItems();
                for (int i = 0; i < itemsArray.length; i++) {
                    if (i == 0) {
                        items = itemsArray[i].getItem_name();
                    } else {
                        items = items + ", " + itemsArray[i].getItem_name();
                    }
                }

                new RateDialog(context, myInterface, orderData.getId(), items).show();

            }
        });

    }

    // TODO Activity Methods

    @Override
    protected void onDestroy() {

        try { ratePresenter.detachView(); } catch (Exception e) {}
        try { contactSupportPresenter.detachView(); } catch (Exception e) {}
        try { orderDetailPresenter.detachView(); } catch (Exception e) {}
        try { reorderPresenter.detachView(); } catch (Exception e) {}

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    // TODO Interface Methods

    @Override
    public void onSuccess_RateOrder(String message) {

        Intent intent = new Intent("loadDeliveredOrdersData");
        context.sendBroadcast(intent);

        hideProgressDialog();
        showToast(message);
        img_rate.setVisibility(View.VISIBLE);
        btn_rate.setVisibility(View.GONE);

    }

    @Override
    public void onRateApply(String thumbVote, String area, String notes) {

        selectedRateThumbVote = thumbVote;

        if (isInternetAvailable()) {
            showProgressDialog();
            ratePresenter.rateOrder(
                    orderData.getId(),
                    thumbVote,
                    area,
                    notes
            );
        } else {
            showToast(getString(R.string.api_error_internet));
        }

    }

    @Override
    public void onSuccess(String message) {
        hideProgressDialog();

        if (isCancel == 1) {
            showToast("Order status has been sent to Hall. Please wait for the confirmation from the Hall's end.");
            txtOrderStatus.setText("Order pending for cancellation");
            txtOrderStatus.setTextColor(getResources().getColor(R.color.colorStatusRed));
            llOrderStatus.setVisibility(View.VISIBLE);
        } else {
            OrderHistoryActivity.getInstance().finish();
            startActivity(new Intent(context, CartActivity.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            finish();
        }

    }

    @Override
    public void onError(String message) {
        hideProgressDialog();
        showToast(message);
    }

    private void sendCancelOrderRequest(String orderId, String hallId) {

        if (isInternetAvailable()) {

            showProgressDialog();
            contactSupportPresenter.contactSupport(
                    orderId,
                    "order",
                    hallId,
                    "userEmail",
                    "subject",
                    "order_cancel"
            );

        } else {
            showToast(getString(R.string.api_error_internet));
        }
    }

    @Override
    public void onSuccess(String message, OrderDetailResponse orderResponse) {

        hideProgressDialog();

        if(null!=orderResponse.getData()){

            orderData = orderResponse.getData();

            orderType = orderData.getStatus();

            txt_order.setText("Order No. : " + orderData.getOrder_number());
            txt_hall.setText(orderData.getRestaurant_name());

            if (orderData.getIs_immediate().length() > 0) {
                if (orderData.getIs_immediate().equals("1")) {
                    txt_date_time.setText("Immediate");
                } else {
                    txt_date_time.setText(AppUtils.convertDate(orderData.getTiming()));
                }
            } else {
                txt_date_time.setText(AppUtils.convertDate(orderData.getTiming()));
            }

            String startTime = orderData.getStart_time();
            String endTime = orderData.getEnd_time();

            if(!AppUtils.isStringEmpty(startTime) && !AppUtils.isStringEmpty(endTime)){
                txt_date_time.setText(startTime + " - " + endTime);
            }

            double tmp1 = Double.parseDouble(orderData.getOrder_total());
            txt_total.setText(context.getString(R.string.currencySymbol) + " " + String.format("%.2f", tmp1));

            strTaxInfo = orderData.getTax_description();

            double tmp2 = Double.parseDouble(orderData.getTax());
            totaTax = formater.format(tmp2);

            txt_taxes.setText(context.getString(R.string.currencySymbol) + " " + String.format("%.2f", tmp2));

            double tmp3 = Double.parseDouble(orderData.getGrand_total());
            txt_topay.setText(context.getString(R.string.currencySymbol) + " " + String.format("%.2f", tmp3));

            if (orderData.getCoupon_code().trim().length() != 0) {
                rl_discount.setVisibility(View.VISIBLE);

                double tmp4 = Double.parseDouble(orderData.getDiscount());
                txt_discount.setText("-" + context.getString(R.string.currencySymbol) + " " + String.format("%.2f", tmp4));
            }

            String reason = orderData.getReason();
            if(reason.trim().length()!=0){
                llOrderReason.setVisibility(View.VISIBLE);
                txtOrderReason.setText(reason);
            }else {
                llOrderReason.setVisibility(View.GONE);
                txtOrderReason.setText("");
            }

            String status = orderData.getStatus();
            status = status.replace("_", " ");
            //status = status.substring(0, 1).toUpperCase() + status.substring(1);

            status = capitalize(status);

            txt_status.setText(status);

            if (status.trim().contains("Cancelled")) {

                llOrderStatus.setVisibility(View.VISIBLE);

                txt_status.setTextColor(context.getResources().getColor(R.color.colorStatusRed));

                if (orderData.getCancel_request().equalsIgnoreCase("0")) {
                    txtOrderStatus.setText("Unfortunately, Hall has declined your order. Your refund will be initiated within 7-10 working days.");
                } else if (orderData.getCancel_request().equalsIgnoreCase("1")) {
                    txtOrderStatus.setText("Order has been cancelled successfully. Your refund will be initiated withinh 7-10 working days.");
                } else if (orderData.getCancel_request().equalsIgnoreCase("2")) {
                    txtOrderStatus.setText("The order has not been picked up, hence the order is cancelled.");
                }

                txtOrderStatus.setTextColor(getResources().getColor(R.color.colorGreyHeading));

            } else {

                if (orderData.getCancel_request().equalsIgnoreCase("1")) {
                    txtOrderStatus.setText("Order pending for cancellation");
                    txtOrderStatus.setTextColor(getResources().getColor(R.color.colorStatusRed));
                    llOrderStatus.setVisibility(View.VISIBLE);
                }else{
                    llOrderStatus.setVisibility(View.GONE);
                }

                txt_status.setTextColor(context.getResources().getColor(R.color.colorStatusGreen));
            }

            ArrayList<OrderDetailResponse.Items> itemsList = new ArrayList<>();

            if (null != orderData.getItems()) {

                OrderDetailResponse.Items[] items = orderData.getItems();
                for (int i = 0; i < items.length; i++) {
                    itemsList.add(items[i]);
                }

            }

            recycler_dishes.setLayoutManager(new LinearLayoutManager(context));
            OrderDetailsAdapter adapter = new OrderDetailsAdapter(context, itemsList);
            recycler_dishes.setAdapter(adapter);

            if (null != orderData.getCard()) {
                txt_cardno.setText("xxxx-xxxx-xxxx-" + orderData.getCard().getLast_four_digit());
                img_card.setImageResource(AppUtils.getCardImage(orderData.getCard().getBrand()));
            }

            if (orderType.contains("deliver")) {

                ll_delivered_cancelled.setVisibility(View.VISIBLE);
                img_tick.setVisibility(View.VISIBLE);

                if (orderData.getThumb().toString().trim().length() != 0) {

                    btn_rate.setVisibility(View.GONE);

                    if (orderData.getThumb().toString().equalsIgnoreCase("up")) {
                        img_rate.setVisibility(View.VISIBLE);
                        img_rate.setImageResource(R.drawable.ic_rateup_primary);
                    }

                    if (orderData.getThumb().toString().equalsIgnoreCase("down")) {
                        img_rate.setVisibility(View.VISIBLE);
                        img_rate.setImageResource(R.drawable.ic_ratedown_primary);
                    }

                }

            }else if (orderType.contains("cancel")) {

                img_tick.setVisibility(View.VISIBLE);
                img_tick.setImageResource(R.drawable.ic_cross_withorangecircle);

                ll_delivered_cancelled.setVisibility(View.VISIBLE);
                btn_rate.setVisibility(View.GONE);

            }else {

                ll_ongoing.setVisibility(View.VISIBLE);

            }

        }else {
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            showToast(getString(R.string.api_error_data));
        }

    }
}
