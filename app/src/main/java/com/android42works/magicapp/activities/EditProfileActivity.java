package com.android42works.magicapp.activities;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.interfaces.ProfileInterface;
import com.android42works.magicapp.interfaces.SuccessInterface;
import com.android42works.magicapp.presenters.ProfilePresenter;
import com.android42works.magicapp.responses.UserDetailsResponse;
import com.android42works.magicapp.utils.AppUtils;

/* Created by JSP@nesar */

public class EditProfileActivity extends BaseActivity implements ProfileInterface {

    private TextView txt_actionbar_title, txt_email;
    private EditText edt_name, edt_mobile, edt_student;
    private ProfilePresenter profilePresenter;

    @Override
    protected int getLayoutView() {
        return R.layout.act_edit_profile;
    }

    @Override
    protected Context getActivityContext() {
        return EditProfileActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);
        edt_name = findViewById(R.id.edt_name);
        edt_mobile = findViewById(R.id.edt_mobile);
        txt_email = findViewById(R.id.txt_email);
        edt_student = findViewById(R.id.edt_student);

    }

    @Override
    protected void initData() {

        // TODO initData

        txt_actionbar_title.setText("PERSONAL INFORMATION");

        profilePresenter = new ProfilePresenter(getAPIInterface());
        profilePresenter.attachView(this);

        loadUserData();

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

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

    }

    // TODO Activity Methods


    @Override
    protected void onDestroy() {
        try{ profilePresenter.detachView(); } catch (Exception e){}
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish(); overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    private void loadUserData(){

        UserDetailsResponse userDetailsResponse = getSessionManager().getUserDetailsResponse();

        if(null!=userDetailsResponse.getData()){

            edt_name.setText(userDetailsResponse.getData().getName());
            edt_mobile.setText(userDetailsResponse.getData().getPhone());
            edt_student.setText(userDetailsResponse.getData().getStudent_id());
            txt_email.setText(userDetailsResponse.getData().getEmail());

        }

    }

    private void checkData(){

        hideKeyboard(edt_student);

        if(AppUtils.isEditTextEmpty(edt_name)){
            showToast(getString(R.string.emptyName));
            return;
        }

        if(AppUtils.isEditTextEmpty(edt_mobile)){
            showToast(getString(R.string.emptyMobile));
            return;
        }

        if(edt_mobile.getText().toString().trim().length()!=10){
            showToast(getString(R.string.invalidMobile));
            return;
        }

//        if(AppUtils.isEditTextEmpty(edt_student)){
//            showToast(getString(R.string.emptyStudentId));
//            return;
//        }

        if(isInternetAvailable()) {

            showProgressDialog();
            profilePresenter.updateProfile(
                    getSessionManager().getUserId(),
                    edt_name.getText().toString(),
                    edt_mobile.getText().toString(),
                    edt_student.getText().toString(),
                    getSessionManager().getUserDetailsResponse().getData().getStripe_id(),
                    getSessionManager().getUserDetailsResponse().getData().getDefault_card_id()
            );

        }else {
            showToast(getString(R.string.api_error_internet));
        }

    }

    // TODO Interface Methods

    @Override
    public void onSuccess(String message, UserDetailsResponse userDetailsResponse) {

        getSessionManager().setUserDetailsResponse(userDetailsResponse);
        showToast(message);
        finish(); overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }

    @Override
    public void onError(String message) {
        showToast(message);
    }
}
