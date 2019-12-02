package com.android42works.magicapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.adapters.OrdersPagerAdapter;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.interfaces.SuccessInterface;
import com.android42works.magicapp.presenters.ReorderPresenter;

/* Created by JSP@nesar */

public class OrderHistoryActivity extends BaseActivity implements SuccessInterface{

    private TextView txt_actionbar_title;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ReorderPresenter reorderPresenter;

    public static OrderHistoryActivity instance;

    public static OrderHistoryActivity getInstance() {
        return instance;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.act_order_history;
    }

    @Override
    protected Context getActivityContext() {
        return OrderHistoryActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);
        mTabLayout = findViewById(R.id.mTabLayout);
        mViewPager = findViewById(R.id.mViewPager);

    }

    @Override
    protected void initData() {

        // TODO initData

        instance = this;

        txt_actionbar_title.setText("ORDER HISTORY");

        OrdersPagerAdapter adapterViewPager = new OrdersPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapterViewPager);

        mTabLayout.addTab(mTabLayout.newTab().setText("Ongoing Orders"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Previous Orders"));

        mViewPager.setOffscreenPageLimit(2);

        reorderPresenter = new ReorderPresenter(getAPIInterface());
        reorderPresenter.attachView(this);


    }

    @Override
    protected void initListener() {

        // TODO initListener

        findViewById(R.id.img_actionbar_backarrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
        });

    }

    // TODO Activity Methods


    @Override
    protected void onPostResume() {
        super.onPostResume();
        Intent intent1 = new Intent("loadDeliveredOrdersData");
        context.sendBroadcast(intent1);
        Intent intent2 = new Intent("loadOnGoingOrdersData");
        context.sendBroadcast(intent2);
    }

    @Override
    public void onBackPressed() {
        finish(); overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void reorder(String orderId){

        if(isInternetAvailable()) {
            showProgressDialog();
            reorderPresenter.reorder(orderId);
        }else {
            showToast(getString(R.string.api_error_internet));
        }

    }

    // TODO Interface Methods

    @Override
    public void onSuccess(String message) {
        hideProgressDialog();
        startActivity(new Intent(context, CartActivity.class));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    public void onError(String message) {
        hideProgressDialog();
        showToast(message);
    }
}
