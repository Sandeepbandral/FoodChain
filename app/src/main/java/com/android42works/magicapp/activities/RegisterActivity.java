package com.android42works.magicapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.interfaces.LoginAndRegisterInterface;
import com.android42works.magicapp.presenters.LoginOrRegisterPresenter;
import com.android42works.magicapp.responses.UserDetailsResponse;
import com.android42works.magicapp.utils.AppUtils;

/* Created by JSP@nesar */

public class RegisterActivity extends BaseActivity implements LoginAndRegisterInterface{

    private TextView txt_actionbar_title, txt_terms;
    private EditText edt_email, edt_student, edt_pass1, edt_pass2;
    private AppCompatCheckBox chk_terms;
    private LoginOrRegisterPresenter loginOrRegisterPresenter;

    @Override
    protected int getLayoutView() {
        return R.layout.act_register;
    }

    @Override
    protected Context getActivityContext() {
        return RegisterActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);
        edt_email = findViewById(R.id.edt_email);
        edt_student = findViewById(R.id.edt_student);
        edt_pass1 = findViewById(R.id.edt_pass1);
        edt_pass2 = findViewById(R.id.edt_pass2);
        chk_terms = findViewById(R.id.chk_terms);
        txt_terms = findViewById(R.id.txt_terms);

    }

    @Override
    protected void initData() {

        // TODO initData

        txt_actionbar_title.setText("");
        txt_terms.setLinkTextColor(getResources().getColor(R.color.colorWhite));

        ClickableSpan termsAndConditions = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, TermsOrPrivacyPolicyActivity.class)
                        .putExtra("pageType", "terms")
                );
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }

        };
        AppUtils.makeLinks(txt_terms, new String[] { "Terms and Conditions." }, new ClickableSpan[] { termsAndConditions });

        loginOrRegisterPresenter = new LoginOrRegisterPresenter(getAPIInterface());
        loginOrRegisterPresenter.attachView(this);

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

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

    }

    // TODO Activity Methods


    @Override
    protected void onDestroy() {
        try{ loginOrRegisterPresenter.detachView(); } catch (Exception e){}
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    private void checkData(){

        hideKeyboard(edt_pass2);

        if(AppUtils.isEditTextEmpty(edt_email)){
            showToast(getString(R.string.emptyEmail));
            return;
        }

        if(!AppUtils.isValidEmail(edt_email)){
            showToast(getString(R.string.invalidEmail));
            return;
        }

        /*if(AppUtils.isEditTextEmpty(edt_student)){
            showToast(getString(R.string.emptyStudentId));
            return;
        }*/

        if(AppUtils.isEditTextEmpty(edt_pass1)){
            showToast(getString(R.string.emptyPassword));
            return;
        }

        if(edt_pass1.getText().toString().trim().length()<8){
            showToast(getString(R.string.invalidPassword));
            return;
        }

        if(!edt_pass1.getText().toString().equals(edt_pass2.getText().toString())){
            showToast(getString(R.string.createPassNotMatched));
            return;
        }

        if(!chk_terms.isChecked()){
            showToast(getString(R.string.termsNotSelected));
            return;
        }

        if(isInternetAvailable()) {
            showProgressDialog();
            loginOrRegisterPresenter.registerUser(
                    edt_email.getText().toString(),
                    edt_student.getText().toString(),
                    "",
                    edt_pass1.getText().toString(),
                    edt_pass2.getText().toString(),
                    getDeviceId(),
                    getSessionManager().getFcmtoken()
            );
        }else {
            showToast(getString(R.string.api_error_internet));
        }

    }

    @Override
    public void onSuccess_LoginOrRegister(String message, UserDetailsResponse userDetailsResponse, String userName, boolean isFBLogin) {

        hideProgressDialog();

        showToast("Thanks for registering with Magic. Verification link has been sent to your registered email address. Please verify your account.");

        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }

    @Override
    public void onError(String message) {
        hideProgressDialog();
        showToast(message);
    }

}
