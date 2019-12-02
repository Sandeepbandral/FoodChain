package com.android42works.magicapp.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android42works.magicapp.R;
import com.android42works.magicapp.activities.OrderDetailsActivity;
import com.android42works.magicapp.activities.OrderHistoryActivity;
import com.android42works.magicapp.adapters.Order_PreviousAdapter;
import com.android42works.magicapp.base.BaseFragment;
import com.android42works.magicapp.dialogs.RateDialog;
import com.android42works.magicapp.interfaces.OrderDetailsInterface;
import com.android42works.magicapp.interfaces.RateOrderInterface;
import com.android42works.magicapp.presenters.OrdersPresenter;
import com.android42works.magicapp.presenters.RatePresenter;
import com.android42works.magicapp.responses.OrdersResponse;
import com.android42works.magicapp.utils.Netwatcher;
import com.google.gson.Gson;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;

/* Created by JSP@nesar */

public class Orders_PreviousFragment extends BaseFragment implements OrderDetailsInterface, RateOrderInterface, Netwatcher.NetwatcherListener {

    private RecyclerView mRecyclerView;
    private Order_PreviousAdapter adapter;
    private OrdersPresenter presenter;
    private RatePresenter ratePresenter;
    private ArrayList<OrdersResponse.Orders> ordersList = new ArrayList<>();
    private int selectedRatePosition = 0, pageNo = 1, perPageCount = 1;
    private String selectedRateThumbVote = "";
    private Netwatcher netwatcher;

    private BroadcastReceiver broadcast_reciever;
    private IntentFilter intentFilter;

    private SwipyRefreshLayout swipyrefreshlayout;

    private boolean hasMoreData = true;
    private View view_nodata, view_nointernet;

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

        perPageCount = Integer.parseInt(getString(R.string.perPageCount));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new Order_PreviousAdapter(context, this, ordersList);
        mRecyclerView.setAdapter(adapter);

        ratePresenter = new RatePresenter(getAPIInterface());
        ratePresenter.attachView(this);

        presenter = new OrdersPresenter(getAPIInterface());
        presenter.attachView(this);

        intentFilter = new IntentFilter();
        intentFilter.addAction("loadDeliveredOrdersData");

        if (broadcast_reciever == null) {

            broadcast_reciever = new BroadcastReceiver() {

                @Override
                public void onReceive(Context arg0, Intent intent) {

                    String action = intent.getAction();

                    if (action.equals("loadDeliveredOrdersData")) {
                        loadDeliveredOrdersData();
                    }

                }

            };

        }

        context.registerReceiver(broadcast_reciever, intentFilter);

        loadDeliveredOrdersData();

    }

    @Override
    protected void initListener(View v) {

        // TODO initListener

        v.findViewById(R.id.btn_tryagain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetAvailable()) {
                    view_nointernet.setVisibility(View.GONE);
                    loadDeliveredOrdersData();
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
                                "previous",
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
                                    "previous",
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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if(isConnected && view_nointernet.getVisibility()==View.VISIBLE){
            view_nointernet.setVisibility(View.GONE);
            loadDeliveredOrdersData();
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
            ratePresenter.detachView();
        } catch (Exception e) {
        }
        try {
            presenter.detachView();
        } catch (Exception e) {
        }
        super.onDestroyView();
    }

    private void loadDeliveredOrdersData() {

        if (isInternetAvailable()) {
            pageNo = 1;
            showProgressDialog();
            presenter.getOrders(
                    getSessionManager().getUserId(),
                    "previous",
                    String.valueOf(pageNo),
                    String.valueOf(perPageCount)
            );
        } else {
            view_nointernet.setVisibility(View.VISIBLE);
        }

    }

    // TODO Interface Methods

    @Override
    public void onClick(int position, String type) {

        selectedRatePosition = position;

        switch (type) {

            case "openDish":

                OrdersResponse.Orders orders = ordersList.get(position);

                Gson gson = new Gson();
                String json = gson.toJson(orders);

                String value = "delivered";
                String status = ordersList.get(position).getStatus();
                if (status.contains("cancelled")) {
                    value = "cancelled";
                } else {
                    value = "delivered";
                }

                startActivity(new Intent(context, OrderDetailsActivity.class)
                        .putExtra("orderId", ordersList.get(position).getId())
                );

                context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                break;

            case "reorderDish":

                ((OrderHistoryActivity) context).reorder(ordersList.get(position).getId());

                break;

            case "rateDish":


                String items = "";

                if (null != ordersList.get(selectedRatePosition).getItems()) {

                    OrdersResponse.Items[] itemsArray = ordersList.get(position).getItems();
                    for (int i = 0; i < itemsArray.length; i++) {
                        if (i == 0) {
                            items = itemsArray[i].getItem_name();
                        } else {
                            items = items + ", " + itemsArray[i].getItem_name();
                        }
                    }

                }

                new RateDialog(context, this, ordersList.get(selectedRatePosition).getOrder_number(), items).show();

                break;

        }

    }


    @Override
    public void onSuccess_RateOrder(String message) {

        hideProgressDialog();
        showToast(message);
        ordersList.get(selectedRatePosition).setThumb(selectedRateThumbVote);
        adapter.updateList(ordersList);

    }

    @Override
    public void onRateApply(String thumbVote, String area, String notes) {

        selectedRateThumbVote = thumbVote;

        if (isInternetAvailable()) {
            showProgressDialog();
            ratePresenter.rateOrder(
                    ordersList.get(selectedRatePosition).getId(),
                    thumbVote,
                    area,
                    notes
            );
        } else {
            showToast(getString(R.string.api_error_internet));
        }

    }

    @Override
    public void onSuccess(String message, ArrayList<OrdersResponse.Orders> newOrdersList) {

        hideProgressDialog();
        swipyrefreshlayout.setRefreshing(false);

        if (newOrdersList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            view_nodata.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            view_nodata.setVisibility(View.VISIBLE);
        }

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

    }

    @Override
    public void onError(String message) {
        swipyrefreshlayout.setRefreshing(false);
        hideProgressDialog();
        showToast(message);
    }


}

