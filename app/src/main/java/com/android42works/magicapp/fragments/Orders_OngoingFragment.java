package com.android42works.magicapp.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android42works.magicapp.R;
import com.android42works.magicapp.activities.HomeActivity;
import com.android42works.magicapp.activities.OrderDetailsActivity;
import com.android42works.magicapp.activities.OrderSuccessActivity;
import com.android42works.magicapp.adapters.Order_OnGoingAdapter;
import com.android42works.magicapp.base.BaseFragment;
import com.android42works.magicapp.interfaces.OrderDetailsInterface;
import com.android42works.magicapp.interfaces.SuccessInterface;
import com.android42works.magicapp.presenters.ContactSupportPresenter;
import com.android42works.magicapp.presenters.OrdersPresenter;
import com.android42works.magicapp.responses.OrdersResponse;
import com.android42works.magicapp.utils.Netwatcher;
import com.google.gson.Gson;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;

/* Created by JSP@nesar */

public class Orders_OngoingFragment extends BaseFragment implements OrderDetailsInterface, SuccessInterface, Netwatcher.NetwatcherListener {

    private RecyclerView mRecyclerView;
    private Order_OnGoingAdapter adapter;
    private OrdersPresenter presenter;
    private ArrayList<OrdersResponse.Orders> ordersList = new ArrayList<>();
    private SwipyRefreshLayout swipyrefreshlayout;
    private Netwatcher netwatcher;

    private int pageNo = 1, perPageCount = 1;
    private boolean hasMoreData = true;
    private View view_nodata, view_nointernet;
    private int orderPos = -1;

    private BroadcastReceiver broadcast_reciever;
    private IntentFilter intentFilter;

    private ContactSupportPresenter contactSupportPresenter;

    @Override
    protected int getLayoutView() {
        return R.layout.frag_order_sub;
    }

    @Override
    protected Activity getActivityContext() {
        return getActivity();
    }

    @Override
    protected void initView(View v) {

        // TODO initView

        mRecyclerView = v.findViewById(R.id.mRecyclerView);
        swipyrefreshlayout = v.findViewById(R.id.swipyrefreshlayout);
        view_nodata = v.findViewById(R.id.view_nodata);
        view_nointernet = v.findViewById(R.id.view_nointernet);
    }

    @Override
    protected void initData(View v) {

        // TODO initData

        netwatcher = new Netwatcher(this);
        context.registerReceiver(netwatcher, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        contactSupportPresenter = new ContactSupportPresenter(getAPIInterface());
        contactSupportPresenter.attachView(this);

        perPageCount = Integer.parseInt(getString(R.string.perPageCount));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new Order_OnGoingAdapter(context, this, ordersList);
        mRecyclerView.setAdapter(adapter);

        presenter = new OrdersPresenter(getAPIInterface());
        presenter.attachView(this);

        if (isInternetAvailable()) {
            showProgressDialog();
            presenter.getOrders(
                    getSessionManager().getUserId(),
                    "ongoing",
                    String.valueOf(pageNo),
                    String.valueOf(perPageCount)
            );
        } else {
            view_nointernet.setVisibility(View.VISIBLE);
        }

        intentFilter = new IntentFilter();
        intentFilter.addAction("loadOnGoingOrdersData");

        if (broadcast_reciever == null) {

            broadcast_reciever = new BroadcastReceiver() {

                @Override
                public void onReceive(Context arg0, Intent intent) {

                    String action = intent.getAction();

                    if (action.equals("loadOnGoingOrdersData")) {
                        loadOnGoingOrdersData();
                    }

                }

            };

        }

        context.registerReceiver(broadcast_reciever, intentFilter);

    }

    @Override
    protected void initListener(View v) {

        // TODO initListener

        v.findViewById(R.id.btn_tryagain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetAvailable()) {
                    view_nointernet.setVisibility(View.GONE);
                    showProgressDialog();
                    presenter.getOrders(
                            getSessionManager().getUserId(),
                            "ongoing",
                            String.valueOf(pageNo),
                            String.valueOf(perPageCount)
                    );
                }else {
                    showToast(getString(R.string.api_error_internet));
                }
            }
        });

        swipyrefreshlayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP) {

                    if (isInternetAvailable()) {
                        pageNo = 1;
                        presenter.getOrders(
                                getSessionManager().getUserId(),
                                "ongoing",
                                String.valueOf(pageNo),
                                String.valueOf(perPageCount)
                        );
                    } else {
                        swipyrefreshlayout.setRefreshing(false);
                        showToast(getString(R.string.api_error_internet));
                    }

                } else {

                    if (hasMoreData) {

                        if (isInternetAvailable()) {
                            pageNo++;
                            presenter.getOrders(
                                    getSessionManager().getUserId(),
                                    "ongoing",
                                    String.valueOf(pageNo),
                                    String.valueOf(perPageCount)
                            );
                        } else {
                            swipyrefreshlayout.setRefreshing(false);
                            showToast(getString(R.string.api_error_internet));
                        }

                    } else {
                        swipyrefreshlayout.setRefreshing(false);
                    }

                }

            }
        });

    }


    // TODO Fragment Methods

    private void loadOnGoingOrdersData(){

        if (isInternetAvailable()) {
            pageNo = 1;
            presenter.getOrders(
                    getSessionManager().getUserId(),
                    "ongoing",
                    String.valueOf(pageNo),
                    String.valueOf(perPageCount)
            );
        } else {
            view_nointernet.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if(isConnected && view_nointernet.getVisibility()==View.VISIBLE){
            view_nointernet.setVisibility(View.GONE);
            showProgressDialog();
            presenter.getOrders(
                    getSessionManager().getUserId(),
                    "ongoing",
                    String.valueOf(pageNo),
                    String.valueOf(perPageCount)
            );
        }

    }

    @Override
    public void onDestroyView() {
        try{ context.unregisterReceiver(netwatcher); }catch (Exception e){}
        try {
            context.unregisterReceiver(broadcast_reciever);
        } catch (Exception e) {
        }
        try {
            presenter.detachView();
        } catch (Exception e) {
        }
        super.onDestroyView();
    }

    // TODO Interface Methods

    @Override
    public void onClick(int position, String type) {

        orderPos = position;

        switch (type) {

            case "openDish":

                OrdersResponse.Orders orders = ordersList.get(position);

                Gson gson = new Gson();
                String json = gson.toJson(orders);

                startActivity(new Intent(context, OrderDetailsActivity.class)
                        .putExtra("orderId", ordersList.get(position).getId())
                );

                context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                break;

            case "statusDish":

                startActivity(new Intent(context, OrderSuccessActivity.class)
                        .putExtra("orderId", ordersList.get(position).getId())
                        .putExtra("orderNumber", ordersList.get(position).getOrder_number())
                );

                context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                break;

            case "cancelDish":

                /*if (ordersList.get(position).getCancel_request().equals("1")) {
                    showToast("Cancel request has already been sent to the admin. Please wait for approval.");
                    return;
                }
                else if(ordersList.get(position).getCancel_request().equals("0"))
                {*/

                sendCancelOrderRequest(ordersList.get(position).getId(), ordersList.get(position).getHall_id());
                // return;
                //  }

                break;

        }

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
    public void onSuccess(String message, ArrayList<OrdersResponse.Orders> newOrdersList) {

        hideProgressDialog();
        swipyrefreshlayout.setRefreshing(false);

        Log.e("newOrdersList::", "::" + newOrdersList.size());

        if (newOrdersList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            view_nodata.setVisibility(View.GONE);

            if (pageNo == 1) {
                ordersList = newOrdersList;
            } else {
                ordersList.addAll(newOrdersList);
            }

            adapter.updateList(ordersList);

            if (newOrdersList.size() < perPageCount) {
                hasMoreData = false;
            } else {
                hasMoreData = true;
            }

        } else {
            mRecyclerView.setVisibility(View.GONE);
            view_nodata.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onError(String message) {
        hideProgressDialog();
        swipyrefreshlayout.setRefreshing(false);
        showToast(message);
    }

    @Override
    public void onSuccess(String message) {

        hideProgressDialog();
        ordersList.get(orderPos).setCancel_request("1");
        showToast("Order status has been sent to Hall. Please wait for the confirmation from the Hall's end.");
        adapter.updateList(ordersList);

    }
}

