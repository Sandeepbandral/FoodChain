package com.android42works.magicapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.interfaces.OrderSuccessInterface;
import com.android42works.magicapp.presenters.OrderSuccessPresenter;
import com.android42works.magicapp.responses.OrderSuccessResponse;
import com.android42works.magicapp.utils.AppUtils;

/* Created by JSP@nesar */

public class OrderSuccessActivity extends BaseActivity implements OrderSuccessInterface {

    private TextView txt_actionbar_title, txt_orderid, txt_deliveryTime;
    private String orderId = "", orderNumber = "";
    private RelativeLayout rl_tick_waiting, rl_tick_confirmed, rl_tick_preparing, rl_tick_prepared, rl_tick_delivered;
    private ImageView img_tick_waiting, img_tick_confirmed, img_tick_preparing, img_tick_prepared, img_tick_delivered;
    private OrderSuccessPresenter presenter;
    private BroadcastReceiver broadcast_reciever;
    private IntentFilter intentFilter;
    private LinearLayout ll_deliveryTime;

    @Override
    protected int getLayoutView() {
        return R.layout.act_order_success;
    }

    @Override
    protected Context getActivityContext() {
        return OrderSuccessActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);
        txt_orderid = findViewById(R.id.txt_orderid);
        rl_tick_waiting = findViewById(R.id.rl_tick_waiting);
        rl_tick_confirmed = findViewById(R.id.rl_tick_confirmed);
        rl_tick_preparing = findViewById(R.id.rl_tick_preparing);
        rl_tick_prepared = findViewById(R.id.rl_tick_prepared);
        rl_tick_delivered = findViewById(R.id.rl_tick_delivered);
        img_tick_waiting = findViewById(R.id.img_tick_waiting);
        img_tick_confirmed = findViewById(R.id.img_tick_confirmed);
        img_tick_preparing = findViewById(R.id.img_tick_preparing);
        img_tick_prepared = findViewById(R.id.img_tick_prepared);
        img_tick_delivered = findViewById(R.id.img_tick_delivered);
        txt_deliveryTime = findViewById(R.id.txt_deliveryTime);
        ll_deliveryTime = findViewById(R.id.ll_deliveryTime);

    }

    @Override
    protected void initData() {

        // TODO initData

        context = OrderSuccessActivity.this;

        orderId = getIntent().getStringExtra("orderId");

        if(getIntent().getStringExtra("orderNumber") != null)
        {
            orderNumber = getIntent().getStringExtra("orderNumber");
        }

        if(orderNumber.length() == 0)
        {
            orderNumber = orderId;
        }

        txt_actionbar_title.setText("ORDER STATUS");

        txt_orderid.setText(Html.fromHtml("Your order <b>#" + orderNumber + "</b> has been placed. Please check the progress below."));

        presenter = new OrderSuccessPresenter(getAPIInterface());
        presenter.attachView(this);

        intentFilter = new IntentFilter();
        intentFilter.addAction("refreshOrderScreenStatus");

        if (broadcast_reciever == null) {

            broadcast_reciever = new BroadcastReceiver() {

                @Override
                public void onReceive(Context arg0, Intent intent) {

                    String action = intent.getAction();

                    if (action.equals("refreshOrderScreenStatus")) {
                        if (isInternetAvailable()) {
                            showProgressDialog();
                            presenter.getStatus(orderId);
                        } else {
                            showToast(getString(R.string.api_error_internet));
                        }
                    }
                }
            };
        }

        registerReceiver(broadcast_reciever, intentFilter);

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

    }

    // TODO Activity Methods


    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (isInternetAvailable()) {
            showProgressDialog();
            presenter.getStatus(orderId);
        } else {
            showToast(getString(R.string.api_error_internet));
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    protected void onDestroy() {
        try{ unregisterReceiver(broadcast_reciever); } catch (Exception e){}
        try {
            presenter.detachView();
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    @Override
    public void onSuccess(String message, OrderSuccessResponse response) {

        String status = response.getData().getStatus();
        String startTime = response.getData().getStart_time();
        String endTime = response.getData().getEnd_time();

        if(!AppUtils.isStringEmpty(startTime) && !AppUtils.isStringEmpty(endTime)){
            ll_deliveryTime.setVisibility(View.VISIBLE);
            txt_deliveryTime.setText(startTime + " - " + endTime);
        }else {
            ll_deliveryTime.setVisibility(View.GONE);
            txt_deliveryTime.setText("");
        }

        hideProgressDialog();

        if (status.equalsIgnoreCase("waiting_for_confirmation")) {
            rl_tick_waiting.setBackgroundResource(R.drawable.bg_circle_primary);
            img_tick_waiting.setVisibility(View.VISIBLE);
            rl_tick_confirmed.setBackgroundResource(R.drawable.bg_circle_grey);
            img_tick_confirmed.setVisibility(View.GONE);
            rl_tick_preparing.setBackgroundResource(R.drawable.bg_circle_grey);
            img_tick_preparing.setVisibility(View.GONE);
            rl_tick_prepared.setBackgroundResource(R.drawable.bg_circle_grey);
            img_tick_prepared.setVisibility(View.GONE);
            rl_tick_delivered.setBackgroundResource(R.drawable.bg_circle_grey);
            img_tick_delivered.setVisibility(View.GONE);
        }

        if (status.equalsIgnoreCase("order_confirmed")) {
            rl_tick_waiting.setBackgroundResource(R.drawable.bg_circle_primary);
            img_tick_waiting.setVisibility(View.VISIBLE);
            rl_tick_confirmed.setBackgroundResource(R.drawable.bg_circle_primary);
            img_tick_confirmed.setVisibility(View.VISIBLE);
            rl_tick_preparing.setBackgroundResource(R.drawable.bg_circle_grey);
            img_tick_preparing.setVisibility(View.GONE);
            rl_tick_prepared.setBackgroundResource(R.drawable.bg_circle_grey);
            img_tick_prepared.setVisibility(View.GONE);
            rl_tick_delivered.setBackgroundResource(R.drawable.bg_circle_grey);
            img_tick_delivered.setVisibility(View.GONE);
        }

        if (status.equalsIgnoreCase("order_preparing")) {
            rl_tick_waiting.setBackgroundResource(R.drawable.bg_circle_primary);
            img_tick_waiting.setVisibility(View.VISIBLE);
            rl_tick_confirmed.setBackgroundResource(R.drawable.bg_circle_primary);
            img_tick_confirmed.setVisibility(View.VISIBLE);
            rl_tick_preparing.setBackgroundResource(R.drawable.bg_circle_primary);
            img_tick_preparing.setVisibility(View.VISIBLE);
            rl_tick_prepared.setBackgroundResource(R.drawable.bg_circle_grey);
            img_tick_prepared.setVisibility(View.GONE);
            rl_tick_delivered.setBackgroundResource(R.drawable.bg_circle_grey);
            img_tick_delivered.setVisibility(View.GONE);
        }

        if (status.equalsIgnoreCase("order_prepared")) {
            rl_tick_waiting.setBackgroundResource(R.drawable.bg_circle_primary);
            img_tick_waiting.setVisibility(View.VISIBLE);
            rl_tick_confirmed.setBackgroundResource(R.drawable.bg_circle_primary);
            img_tick_confirmed.setVisibility(View.VISIBLE);
            rl_tick_preparing.setBackgroundResource(R.drawable.bg_circle_primary);
            img_tick_preparing.setVisibility(View.VISIBLE);
            rl_tick_prepared.setBackgroundResource(R.drawable.bg_circle_primary);
            img_tick_prepared.setVisibility(View.VISIBLE);
            rl_tick_delivered.setBackgroundResource(R.drawable.bg_circle_grey);
            img_tick_delivered.setVisibility(View.GONE);
        }

        if (status.equalsIgnoreCase("order_delivered")) {
            rl_tick_waiting.setBackgroundResource(R.drawable.bg_circle_primary);
            img_tick_waiting.setVisibility(View.VISIBLE);
            rl_tick_confirmed.setBackgroundResource(R.drawable.bg_circle_primary);
            img_tick_confirmed.setVisibility(View.VISIBLE);
            rl_tick_preparing.setBackgroundResource(R.drawable.bg_circle_primary);
            img_tick_preparing.setVisibility(View.VISIBLE);
            rl_tick_prepared.setBackgroundResource(R.drawable.bg_circle_primary);
            img_tick_prepared.setVisibility(View.VISIBLE);
            rl_tick_delivered.setBackgroundResource(R.drawable.bg_circle_primary);
            img_tick_delivered.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onError(String message) {

        Log.e("onError::", "onError::" + message);
        hideProgressDialog();
        showToast(message);
    }
}
