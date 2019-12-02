package com.android42works.magicapp.activities;

import android.content.Context;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.interfaces.SuccessInterface;
import com.android42works.magicapp.presenters.ChangePassPresenter;
import com.android42works.magicapp.utils.AppUtils;

/* Created by JSP@nesar */

public class ChangePassActivity extends BaseActivity implements SuccessInterface {

    private TextView txt_actionbar_title;
    private EditText edt_current, edt_newpass, edt_confirmpass;
    private ImageView imgHideCurrent, imgHideNew, imgHideConfirm;
    private ImageView imgShowCurrent, imgShowNew, imgShowConfirm;
    private int startCurrent = 0, startNew = 0, startConfirm = 0;
    private int endCurrent = 0, endNew = 0, endConfirm = 0;
    private ChangePassPresenter changePassPresenter;

    @Override
    protected int getLayoutView() {
        return R.layout.act_changepass;
    }

    @Override
    protected Context getActivityContext() {
        return ChangePassActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);

        edt_current = findViewById(R.id.edt_current);
        edt_newpass = findViewById(R.id.edt_newpass);
        edt_confirmpass = findViewById(R.id.edt_confirmpass);

        imgHideCurrent = findViewById(R.id.imgHideCurrent);
        imgHideNew = findViewById(R.id.imgHideNew);
        imgHideConfirm = findViewById(R.id.imgHideConfirm);
        imgShowCurrent = findViewById(R.id.imgShowCurrent);
        imgShowNew = findViewById(R.id.imgShowNew);
        imgShowConfirm = findViewById(R.id.imgShowConfirm);

    }

    @Override
    protected void initData() {

        // TODO initData

        context = ChangePassActivity.this;

        txt_actionbar_title.setText("CHANGE PASSWORD");

        changePassPresenter = new ChangePassPresenter(getAPIInterface());
        changePassPresenter.attachView(this);

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

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

        imgHideCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCurrent = edt_current.getSelectionStart();
                endCurrent = edt_current.getSelectionEnd();
                edt_current.setTransformationMethod(null);
                edt_current.setSelection(startCurrent, endCurrent);

                imgShowCurrent.setVisibility(View.VISIBLE);
                imgHideCurrent.setVisibility(View.GONE);
            }
        });
        imgShowCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCurrent = edt_current.getSelectionStart();
                endCurrent = edt_current.getSelectionEnd();
                edt_current.setTransformationMethod(new PasswordTransformationMethod());
                edt_current.setSelection(startCurrent, endCurrent);

                imgShowCurrent.setVisibility(View.GONE);
                imgHideCurrent.setVisibility(View.VISIBLE);
            }
        });


        imgHideNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNew = edt_newpass.getSelectionStart();
                endNew = edt_newpass.getSelectionEnd();
                edt_newpass.setTransformationMethod(null);
                edt_newpass.setSelection(startNew, endNew);

                imgShowNew.setVisibility(View.VISIBLE);
                imgHideNew.setVisibility(View.GONE);
            }
        });
        imgShowNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNew = edt_newpass.getSelectionStart();
                endNew = edt_newpass.getSelectionEnd();
                edt_newpass.setTransformationMethod(new PasswordTransformationMethod());
                edt_newpass.setSelection(startNew, endNew);

                imgShowNew.setVisibility(View.GONE);
                imgHideNew.setVisibility(View.VISIBLE);
            }
        });


        imgHideConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConfirm = edt_confirmpass.getSelectionStart();
                endConfirm = edt_confirmpass.getSelectionEnd();
                edt_confirmpass.setTransformationMethod(null);
                edt_confirmpass.setSelection(startConfirm, endConfirm);

                imgShowConfirm.setVisibility(View.VISIBLE);
                imgHideConfirm.setVisibility(View.GONE);
            }
        });
        imgShowConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConfirm = edt_confirmpass.getSelectionStart();
                endConfirm = edt_confirmpass.getSelectionEnd();
                edt_confirmpass.setTransformationMethod(new PasswordTransformationMethod());
                edt_confirmpass.setSelection(startConfirm, endConfirm);

                imgShowConfirm.setVisibility(View.GONE);
                imgHideConfirm.setVisibility(View.VISIBLE);
            }
        });
    }

    // TODO Activity Methods

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    protected void onDestroy() {
        try {
            changePassPresenter.detachView();
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    private void checkData() {

        hideKeyboard(edt_confirmpass);

        if (AppUtils.isEditTextEmpty(edt_current)) {
            showToast(getString(R.string.emptyPassword));
            return;
        }

        if (edt_current.getText().toString().trim().length() < 8) {
            showToast(getString(R.string.invalidPassword));
            return;
        }

        if (AppUtils.isEditTextEmpty(edt_newpass)) {
            showToast(getString(R.string.emptyNewPassword));
            return;
        }

        if (edt_newpass.getText().toString().trim().length() < 8) {
            showToast(getString(R.string.invalidPassword));
            return;
        }

        if (!edt_newpass.getText().toString().equals(edt_confirmpass.getText().toString())) {
            showToast(getString(R.string.confirmPassNotMatched));
            return;
        }

        if (isInternetAvailable()) {
            showProgressDialog();
            changePassPresenter.changePass(
                    getSessionManager().getUserId(),
                    edt_current.getText().toString(),
                    edt_newpass.getText().toString()
            );
        } else {
            showToast(getString(R.string.api_error_internet));
        }

    }

    // TODO Interface Methods

    @Override
    public void onSuccess(String message) {
        hideProgressDialog();
        showToast(message);
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void onError(String message) {
        hideProgressDialog();
        showToast(message);
    }

}
