package com.android42works.magicapp.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.activities.HomeActivity;
import com.android42works.magicapp.activities.SkipLoginActivity;
import com.android42works.magicapp.activities.SkipRegisterActivity;
import com.android42works.magicapp.adapters.NotificationsAdapter;
import com.android42works.magicapp.base.BaseFragment;
import com.android42works.magicapp.interfaces.NotificationsInterface;
import com.android42works.magicapp.models.OrdersModel;
import com.android42works.magicapp.presenters.NotificationsPresenter;
import com.android42works.magicapp.responses.Notifications;
import com.android42works.magicapp.responses.NotificationsResponse;
import com.android42works.magicapp.utils.Netwatcher;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;

/* Created by JSP@nesar */

public class NotificationsFragment extends BaseFragment implements NotificationsInterface, Netwatcher.NetwatcherListener {

    private RecyclerView recycler_notifications;
    private NestedScrollView nestedScrollView;
    private View view_nodata, view_nointernet;
    private NotificationsPresenter notificationsPresenter;
    private NotificationsAdapter notificationsAdapter;
    private ArrayList<Notifications> notificationsList = new ArrayList<>();

    private BroadcastReceiver broadcast_reciever;
    private IntentFilter intentFilter;

    private int pageNo = 1;
    private String perPageCount = "";
    private TextView txt_login_message;
    private SwipyRefreshLayout swipyrefreshlayout;
    private RelativeLayout rlLogin;
    private Netwatcher netwatcher;

    public static NotificationsFragment instance;

    public static NotificationsFragment getInstance() {
        return instance;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.frag_notifications;
    }

    @Override
    protected Activity getActivityContext() {
        return getActivity();
    }

    @Override
    protected void initView(View v) {

        // TODO initView

        rlLogin = v.findViewById(R.id.rlLogin);
        recycler_notifications = v.findViewById(R.id.recycler_notifications);
        nestedScrollView = v.findViewById(R.id.nestedScrollView);
        view_nodata = v.findViewById(R.id.view_nodata);
        swipyrefreshlayout = v.findViewById(R.id.swipyrefreshlayout);
        txt_login_message = v.findViewById(R.id.txt_login_message);
        view_nointernet = v.findViewById(R.id.view_nointernet);
    }

    @Override
    protected void initData(View v) {

        // TODO initData0

        instance = this;

        netwatcher = new Netwatcher(this);

        intentFilter = new IntentFilter();
        intentFilter.addAction("checkLoginStatus");

        if (broadcast_reciever == null) {

            broadcast_reciever = new BroadcastReceiver() {

                @Override
                public void onReceive(Context arg0, Intent intent) {

                    String action = intent.getAction();

                    if (action.equals("checkLoginStatus")) {
                        checkLoginAndData();
                    }

                }

            };

        }

        context.registerReceiver(netwatcher, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        context.registerReceiver(broadcast_reciever, intentFilter);

        perPageCount = getString(R.string.perPageCount);

        txt_login_message.setText(getString(R.string.login_notifications));

        notificationsPresenter = new NotificationsPresenter(getAPIInterface());
        notificationsPresenter.attachView(this);

        recycler_notifications.setLayoutManager(new LinearLayoutManager(context));
        notificationsAdapter = new NotificationsAdapter(context, notificationsList);
        recycler_notifications.setAdapter(notificationsAdapter);

        checkLoginAndData();

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
                    notificationsPresenter.getNotifications(getSessionManager().getUserId(), String.valueOf(pageNo), perPageCount);
                }else {
                    showToast(getString(R.string.api_error_internet));
                }
            }
        });

        swipyrefreshlayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (isInternetAvailable()) {

                    notificationsList.clear();

                    notificationsPresenter.getNotifications(getSessionManager().getUserId(), String.valueOf(pageNo), perPageCount);
                } else {
                    swipyrefreshlayout.setRefreshing(false);
                    showToast(getString(R.string.api_error_internet));
                }
            }
        });

        v.findViewById(R.id.txtLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context, SkipLoginActivity.class));
                context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        v.findViewById(R.id.txt_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context, SkipRegisterActivity.class));
                context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void hideAllNotifications(){

        if (isInternetAvailable()) {
            showProgressDialog();
            notificationsPresenter.hideAllReadNotifications(getSessionManager().getUserId());
            view_nointernet.setVisibility(View.GONE);
        } else {
            view_nointernet.setVisibility(View.VISIBLE);
        }

    }

    // TODO Interface Methods

    private void checkLoginAndData(){

        if (getSessionManager().isUserSkippedLoggedIn()) {
            rlLogin.setVisibility(View.VISIBLE);
        } else {
            rlLogin.setVisibility(View.GONE);

            if (isInternetAvailable()) {
                showProgressDialog();
                notificationsPresenter.getNotifications(getSessionManager().getUserId(), String.valueOf(pageNo), perPageCount);
                view_nointernet.setVisibility(View.GONE);
            } else {
                view_nointernet.setVisibility(View.VISIBLE);
            }

        }

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if(isConnected && view_nointernet.getVisibility()==View.VISIBLE){
            view_nointernet.setVisibility(View.GONE);
            showProgressDialog();
            notificationsPresenter.getNotifications(getSessionManager().getUserId(), String.valueOf(pageNo), perPageCount);
        }

    }

    @Override
    public void onDestroyView() {
        try{ context.unregisterReceiver(netwatcher); }catch (Exception e){}
        try{ context.unregisterReceiver(broadcast_reciever); }catch (Exception e){}
        try{ notificationsPresenter.detachView(); } catch (Exception e) {}
        super.onDestroyView();
    }

    @Override
    public void onSuccess(String message, ArrayList<Notifications> notificationsList) {

        hideProgressDialog();

        swipyrefreshlayout.setRefreshing(false);

        if (notificationsList.size() == 0) {

            Log.e("onSuccess00::", "::" + notificationsList.size());

            HomeActivity.getInstance().setVisiBilityOfClearNotifyDialog(false);
            view_nodata.setVisibility(View.VISIBLE);
            swipyrefreshlayout.setVisibility(View.GONE);

        } else {

            Log.e("onSuccess11::", "::" + notificationsList.size());

            HomeActivity.getInstance().setVisiBilityOfClearNotifyDialog(true);
            this.notificationsList = notificationsList;
            notificationsAdapter.updateList(notificationsList);
            swipyrefreshlayout.setVisibility(View.VISIBLE);
            view_nodata.setVisibility(View.GONE);
        }

    }

    @Override
    public void onSuccess_HideAll(String message) {

        hideProgressDialog();

        notificationsAdapter.updateList(new ArrayList<Notifications>());
        view_nodata.setVisibility(View.VISIBLE);
        swipyrefreshlayout.setVisibility(View.GONE);
        HomeActivity.getInstance().setVisiBilityOfClearNotifyDialog(false);

    }

    @Override
    public void onError(String message) {

        Log.e("onError", "onError" + message);

        swipyrefreshlayout.setRefreshing(false);
        hideProgressDialog();
        showToast(message);

        swipyrefreshlayout.setVisibility(View.GONE);
        view_nodata.setVisibility(View.VISIBLE);
    }


}

