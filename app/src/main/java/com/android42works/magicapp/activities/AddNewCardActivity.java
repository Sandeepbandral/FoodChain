package com.android42works.magicapp.activities;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.interfaces.AddNewCardInterface;
import com.android42works.magicapp.interfaces.ProfileInterface;
import com.android42works.magicapp.presenters.AddCardPresenter;
import com.android42works.magicapp.presenters.ProfilePresenter;
import com.android42works.magicapp.responses.UserDetailsResponse;
import com.android42works.magicapp.utils.AppUtils;
import com.stripe.android.model.Card;

/* Created by JSP@nesar */

public class AddNewCardActivity extends BaseActivity implements AddNewCardInterface, ProfileInterface {

    private TextView txt_actionbar_title;
    private EditText edt_name, edt_cardno, edt_cardexpiry, edt_cardcvv;
    private ProfilePresenter profilePresenter;
    private AddCardPresenter addCardPresenter;
    private String customerStripeId = "";
    private ImageView img_card;
    private boolean createCustomer = false, setAsDefault = false;

    @Override
    protected int getLayoutView() {
        return R.layout.act_addnewcard;
    }

    @Override
    protected Context getActivityContext() {
        return AddNewCardActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);
        edt_name = findViewById(R.id.edt_name);
        edt_cardno = findViewById(R.id.edt_cardno);
        edt_cardexpiry = findViewById(R.id.edt_cardexpiry);
        edt_cardcvv = findViewById(R.id.edt_cardcvv);
        img_card = findViewById(R.id.img_card);

    }

    @Override
    protected void initData() {

        // TODO initData

        txt_actionbar_title.setText("ADD NEW CARD");

        addCardPresenter = new AddCardPresenter(getStripeAPIInterface());
        addCardPresenter.attachView(this);

        profilePresenter = new ProfilePresenter(getAPIInterface());
        profilePresenter.attachView(this);

        createCustomer = getIntent().getBooleanExtra("createCustomer", false);
        setAsDefault = getIntent().getBooleanExtra("setAsDefault", false);

        customerStripeId = getSessionManager().getUserDetailsResponse().getData().getStripe_id();

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

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

        edt_cardexpiry.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    edt_cardexpiry.setText("");
                }
                return false;
            }
        });

        edt_cardno.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    edt_cardno.setText("");
                }
                return false;
            }
        });

        edt_cardexpiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==2){

                    int month = Integer.parseInt(s.toString());
                    if(month>12){
                        edt_cardexpiry.setText("");
                        showToast(getString(R.string.invalidCardExpiry));
                    }else {
                        edt_cardexpiry.setText(edt_cardexpiry.getText().toString() + "/");
                        edt_cardexpiry.setSelection(edt_cardexpiry.getText().length());
                    }


                }

            }
        });

        edt_cardno.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()!=0){

                    if(s.toString().trim().length()==4 || s.toString().trim().length()==9 || s.toString().trim().length()==14){
                        edt_cardno.setText(s + "-");
                        edt_cardno.setSelection(edt_cardno.getText().length());
                    }

                    String cardBrand = AppUtils.getCardType(s.toString());
                    img_card.setImageResource(AppUtils.getCardImage(cardBrand));

                }else {
                    img_card.setImageResource(R.drawable.ic_card_default);
                }

            }
        });

    }


    // TODO Activity Methods

    @Override
    public void onBackPressed() {
        finish(); overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    protected void onDestroy() {
        try{ addCardPresenter.detachView(); } catch (Exception e){}
        try{ profilePresenter.detachView(); } catch (Exception e){}
        super.onDestroy();
    }

    private void checkData(){

        hideKeyboard(edt_cardcvv);

        if(AppUtils.isEditTextEmpty(edt_name)){
            showToast(getString(R.string.emptyName));
            return;
        }

        if(AppUtils.isEditTextEmpty(edt_cardno)){
            showToast(getString(R.string.emptyCardNo));
            return;
        }

        if(AppUtils.isEditTextEmpty(edt_cardexpiry)){
            showToast(getString(R.string.emptyCardExpiry));
            return;
        }

        if(edt_cardexpiry.getText().toString().trim().length()!=5){
            showToast(getString(R.string.invalidCardExpiry));
            return;
        }

        if(AppUtils.isEditTextEmpty(edt_cardcvv)){
            showToast(getString(R.string.emptyCardCVV));
            return;
        }

        if(edt_cardcvv.getText().toString().trim().length()<3){
            showToast(getString(R.string.invalidCardCVV));
            return;
        }

        String expiry[] = edt_cardexpiry.getText().toString().split("/");
        String expMonth = expiry[0];
        String expYear = "20" + expiry[1];

        Card card = new Card(
                edt_cardno.getText().toString(),
                Integer.parseInt(expMonth),
                Integer.parseInt(expYear),
                edt_cardcvv.getText().toString()
        );

        if (!card.validateCard()) {
            showToast(getString(R.string.invalidCardDetails));
            return;
        }

        if(isInternetAvailable()) {

            showProgressDialog();

            if(createCustomer){

                addCardPresenter.createCustomer(
                        getSessionManager().getUserDetailsResponse().getData().getEmail()
                );

            }else {

                addCardPresenter.createCardToken(
                        edt_cardno.getText().toString(),
                        expMonth,
                        expYear,
                        edt_cardcvv.getText().toString()
                );

            }

        }else {
            showToast(getString(R.string.api_error_internet));
        }

    }


    // TODO Interface Methods

    @Override
    public void onSuccess_StripeID(String stripeId) {

        customerStripeId = stripeId;

        UserDetailsResponse userDetailsResponse = getSessionManager().getUserDetailsResponse();
        userDetailsResponse.getData().setStripe_id(stripeId);
        getSessionManager().setUserDetailsResponse(userDetailsResponse);

        if(isInternetAvailable()) {

            String expiry[] = edt_cardexpiry.getText().toString().split("/");
            String expMonth = expiry[0];
            String expYear = "20" + expiry[1];

            addCardPresenter.createCardToken(
                    edt_cardno.getText().toString(),
                    expMonth,
                    expYear,
                    edt_cardcvv.getText().toString()
            );

            profilePresenter.updateProfile(
                    getSessionManager().getUserId(),
                    getSessionManager().getUserDetailsResponse().getData().getName(),
                    getSessionManager().getUserDetailsResponse().getData().getPhone(),
                    getSessionManager().getUserDetailsResponse().getData().getStudent_id(),
                    stripeId,
                    getSessionManager().getUserDetailsResponse().getData().getDefault_card_id()
            );

        }else {
            showToast(getString(R.string.api_error_internet));
        }

    }

    @Override
    public void onSuccess_TokenID(String tokenId) {

        if(isInternetAvailable()) {
            addCardPresenter.saveCard(
                    customerStripeId,
                    tokenId
            );
        }else {
            showToast(getString(R.string.api_error_internet));
        }

    }

    @Override
    public void onSuccess_CardSave(String cardId) {

        UserDetailsResponse userDetailsResponse = getSessionManager().getUserDetailsResponse();
        userDetailsResponse.getData().setDefault_card_id(cardId);
        getSessionManager().setUserDetailsResponse(userDetailsResponse);

        Intent intent = new Intent("reloadSavedCards");
        sendBroadcast(intent);

        if(createCustomer) {

            if (isInternetAvailable()) {

                profilePresenter.updateProfile(
                        getSessionManager().getUserId(),
                        getSessionManager().getUserDetailsResponse().getData().getName(),
                        getSessionManager().getUserDetailsResponse().getData().getPhone(),
                        getSessionManager().getUserDetailsResponse().getData().getStudent_id(),
                        customerStripeId,
                        cardId
                );

            } else {
                showToast(getString(R.string.api_error_internet));
            }

        }

        hideProgressDialog();
        showToast("Card saved successfully");
        finish(); overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);


    }

    @Override
    public void onSuccess(String message, UserDetailsResponse userDetailsResponse) {
        getSessionManager().setUserDetailsResponse(userDetailsResponse);
    }

    @Override
    public void onError(String message) {
        hideProgressDialog();
        showToast(message);
    }
}
