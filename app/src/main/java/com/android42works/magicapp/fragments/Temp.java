package com.android42works.magicapp.fragments;

import android.app.Activity;
import android.view.View;

import com.android42works.magicapp.R;
import com.android42works.magicapp.base.BaseFragment;

/* Created by JSP@nesar */

public class Temp extends BaseFragment {

    @Override
    protected int getLayoutView() {
        return 0;
    }

    @Override
    protected Activity getActivityContext() {
        return getActivity();
    }

    @Override
    protected void initView(View v) {

        // TODO initView

    }

    @Override
    protected void initData(View v) {

        // TODO initData

    }

    @Override
    protected void initListener(View v) {

        // TODO initListener

    }

    @Override
    public void onDestroyView() {
        /* Detach presenter here if used */
        super.onDestroyView();
    }
}

