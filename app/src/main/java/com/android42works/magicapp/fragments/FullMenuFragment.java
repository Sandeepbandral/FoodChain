package com.android42works.magicapp.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android42works.magicapp.R;
import com.android42works.magicapp.activities.HallMenuActivity;
import com.android42works.magicapp.activities.HomeActivity;
import com.android42works.magicapp.adapters.FullMenuAdapter;
import com.android42works.magicapp.base.BaseFragment;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.android42works.magicapp.utils.Netwatcher;
import com.android42works.magicapp.utils.RecyclerItemClickListener;

import java.util.ArrayList;

/* Created by JSP@nesar */

public class FullMenuFragment extends BaseFragment implements Netwatcher.NetwatcherListener{

    private RecyclerView mRecyclerView;
    private FullMenuAdapter adapter;
    private ArrayList<HomeDishesResponse.Halls> hallList = new ArrayList<>();
    private View view_nointernet;
    private BroadcastReceiver broadcast_reciever;
    private IntentFilter intentFilter;
    private Netwatcher netwatcher;

    @Override
    protected int getLayoutView() {
        return R.layout.frag_fullmenu;
    }

    @Override
    protected Activity getActivityContext() {
        return getActivity();
    }

    @Override
    protected void initView(View v) {

        // TODO initView

        mRecyclerView = v.findViewById(R.id.mRecyclerView);
        view_nointernet = v.findViewById(R.id.view_nointernet);

        if(!isInternetAvailable()) {
            view_nointernet.setVisibility(View.VISIBLE);
        }

        v.findViewById(R.id.btn_tryagain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetAvailable()) {
                    view_nointernet.setVisibility(View.GONE);
                    ((HomeActivity) context).loadAllHomeData();
                }else {
                    showToast(getString(R.string.api_error_internet));
                }
            }
        });

    }

    @Override
    protected void initData(View v) {

        // TODO initData

        netwatcher = new Netwatcher(this);

        intentFilter = new IntentFilter();
        intentFilter.addAction("reloadFullMenuData");

        if (broadcast_reciever == null) {

            broadcast_reciever = new BroadcastReceiver() {

                @Override
                public void onReceive(Context arg0, Intent intent) {

                    String action = intent.getAction();

                    if (action.equals("reloadFullMenuData")) {
                        loadMenuData();
                    }

                }

            };

        }

        context.registerReceiver(netwatcher, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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
                    loadMenuData();
                }else {
                    showToast(getString(R.string.api_error_internet));
                }
            }
        });

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        startActivity(new Intent(context, HallMenuActivity.class)
                                .putExtra("hallName", hallList.get(position).getName())
                                .putExtra("hallId", hallList.get(position).getId())
                        );
                        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                    }
                })
        );

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isInternetAvailable()) {
            view_nointernet.setVisibility(View.VISIBLE);
        }else {
            loadMenuData();
        }
    }

    @Override
    public void onDestroyView() {
        try{ context.unregisterReceiver(netwatcher); }catch (Exception e){}
        try{ context.unregisterReceiver(broadcast_reciever); }catch (Exception e){}
        super.onDestroyView();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if(isConnected && view_nointernet.getVisibility()==View.VISIBLE){
            view_nointernet.setVisibility(View.GONE);
        }

    }

    private void loadMenuData(){

        hallList = new ArrayList<>();

        HomeDishesResponse.Halls[] hallsResponse = getSessionManager().getHallsResponse();
        if(null!=hallsResponse) {

            for (int i = 0; i < hallsResponse.length; i++) {
                hallList.add(hallsResponse[i]);
            }

            mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            adapter = new FullMenuAdapter(context, hallList, hallsResponse);
            mRecyclerView.setAdapter(adapter);

        }

    }


}

