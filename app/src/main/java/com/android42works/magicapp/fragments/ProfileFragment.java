package com.android42works.magicapp.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.activities.EditProfileActivity;
import com.android42works.magicapp.activities.LoginActivity;
import com.android42works.magicapp.activities.OrderHistoryActivity;
import com.android42works.magicapp.activities.FavouritesActivity;
import com.android42works.magicapp.activities.SettingsActivity;
import com.android42works.magicapp.activities.SkipLoginActivity;
import com.android42works.magicapp.activities.SkipRegisterActivity;
import com.android42works.magicapp.base.BaseFragment;
import com.android42works.magicapp.dialogs.LogoutDialog;
import com.android42works.magicapp.interfaces.LogoutInterface;
import com.android42works.magicapp.presenters.LogoutPresenter;


/* Created by JSP@nesar */

public class ProfileFragment extends BaseFragment implements LogoutInterface {

    private LogoutInterface logoutInterface;
    private LogoutPresenter logoutPresenter;
    private LinearLayout ll_withonlylogin;
    private RelativeLayout llLogin;
    private TextView txt_login_logout, txtLogin;
    private ScrollView scrollView;

    private BroadcastReceiver broadcast_reciever;
    private IntentFilter intentFilter;

    @Override
    protected int getLayoutView() {
        return R.layout.frag_profile;
    }

    @Override
    protected Activity getActivityContext() {
        return getActivity();
    }

    @Override
    protected void initView(View v) {

        // TODO initView

        ll_withonlylogin = v.findViewById(R.id.ll_withonlylogin);
        llLogin = v.findViewById(R.id.llLogin);
        txt_login_logout = v.findViewById(R.id.txt_login_logout);
        txtLogin = v.findViewById(R.id.txtLogin);
        scrollView = v.findViewById(R.id.scrollView);

    }

    @Override
    protected void initData(View v) {

        // TODO initData

        logoutInterface = this;
        logoutPresenter = new LogoutPresenter(getAPIInterface());
        logoutPresenter.attachView(this);

        checkLoginStatus();

        intentFilter = new IntentFilter();
        intentFilter.addAction("checkLoginStatus");

        if (broadcast_reciever == null) {

            broadcast_reciever = new BroadcastReceiver() {

                @Override
                public void onReceive(Context arg0, Intent intent) {

                    String action = intent.getAction();

                    if (action.equals("checkLoginStatus")) {
                        checkLoginStatus();
                    }

                }

            };

        }

        context.registerReceiver(broadcast_reciever, intentFilter);

    }

    @Override
    protected void initListener(View v) {

        // TODO initListener

        v.findViewById(R.id.rl_personal_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, EditProfileActivity.class));
                context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        v.findViewById(R.id.rl_favourite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, FavouritesActivity.class));
                context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        v.findViewById(R.id.rl_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isInternetAvailable()){
                    startActivity(new Intent(context, SettingsActivity.class));
                    context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }else {
                    showToast(getString(R.string.api_error_internet));
                }

            }
        });

        v.findViewById(R.id.rl_order_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, OrderHistoryActivity.class));
                context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        v.findViewById(R.id.txtLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getSessionManager().isUserSkippedLoggedIn()) {
                    startActivity(new Intent(context, SkipLoginActivity.class));
                    context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } /*else {
                    new LogoutDialog(context, logoutInterface).show();
                }*/

            }
        });

        v.findViewById(R.id.ll_login_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogoutDialog(context, logoutInterface).show();
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

    // TODO Fragment Methods

    @Override
    public void onDestroyView() {
        try{ context.unregisterReceiver(broadcast_reciever); }catch (Exception e){}
        try{ logoutPresenter.detachView(); } catch (Exception e){}
        super.onDestroyView();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void checkLoginStatus() {

        if (getSessionManager().isUserSkippedLoggedIn()) {
            llLogin.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            //txt_login_logout.setText("LOGIN");
        } else {
            scrollView.setVisibility(View.VISIBLE);
            llLogin.setVisibility(View.GONE);
            //txt_login_logout.setText("LOGOUT");
        }

    }


    // TODO Interface Methods

    @Override
    public void onLogoutClick() {
        showProgressDialog();
        logoutPresenter.logoutUser(getSessionManager().getUserId(), getDeviceId());
    }

    @Override
    public void onLogoutSuccess(String message) {
        hideProgressDialog();
        showToast(message);
        getSessionManager().clearAllData();
        context.finishAffinity();
        startActivity(new Intent(context, LoginActivity.class));
        context.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void onError(String message) {
        hideProgressDialog();
        showToast(message);
    }

}

