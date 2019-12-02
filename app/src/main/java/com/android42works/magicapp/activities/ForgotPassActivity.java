package com.android42works.magicapp.activities;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.interfaces.SuccessInterface;
import com.android42works.magicapp.presenters.ForgotPassPresenter;
import com.android42works.magicapp.utils.AppUtils;

/* Created by JSP@nesar */

public class ForgotPassActivity extends BaseActivity implements SuccessInterface{

    private EditText edt_email;
    private ForgotPassPresenter forgotPassPresenter;

    @Override
    protected int getLayoutView() {
        return R.layout.act_forgotpass;
    }

    @Override
    protected Context getActivityContext() {
        return ForgotPassActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

        edt_email = findViewById(R.id.edt_email);

    }

    @Override
    protected void initData() {

        // TODO initData

        forgotPassPresenter = new ForgotPassPresenter(getAPIInterface());
        forgotPassPresenter.attachView(this);

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

        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

    }

    // TODO Activity Methods >>>


    @Override
    protected void onDestroy() {
        forgotPassPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish(); overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    private void checkData() {

        hideKeyboard(edt_email);

        if (AppUtils.isEditTextEmpty(edt_email)) {
            showToast(getString(R.string.emptyEmail));
            return;
        }

        if (!AppUtils.isValidEmail(edt_email)) {
            showToast(getString(R.string.invalidEmail));
            return;
        }

        if(isInternetAvailable()) {
            showProgressDialog();
            forgotPassPresenter.forgotPass(edt_email.getText().toString());
        }else {
            showToast(getString(R.string.api_error_internet));
        }

    }

    // TODO Interface Methods

    @Override
    public void onSuccess(String message) {
        hideProgressDialog();
        showToast(message);
        finish(); overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void onError(String message) {
        hideProgressDialog();
        showToast(message);
    }

}
