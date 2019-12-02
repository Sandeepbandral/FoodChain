package com.android42works.magicapp.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.adapters.SpinnerArrayAdapter;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.interfaces.SuccessInterface;
import com.android42works.magicapp.presenters.ContactSupportPresenter;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.android42works.magicapp.utils.AppUtils;

import java.util.ArrayList;

/* Created by JSP@nesar */

public class ContactUsActivity extends BaseActivity implements SuccessInterface {

    private TextView txt_actionbar_title, txt_email;
    private Spinner spinner_hall;
    private EditText edt_name, edt_subject, edt_message;

    private ArrayList<HomeDishesResponse.Halls> hallsList = new ArrayList<>();
    private String selectedHallId = "";

    private ContactSupportPresenter contactSupportPresenter;

    @Override
    protected int getLayoutView() {
        return R.layout.act_contactus;
    }

    @Override
    protected Context getActivityContext() {
        return ContactUsActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);
        edt_name = findViewById(R.id.edt_name);
        txt_email = findViewById(R.id.txt_email);
        edt_subject = findViewById(R.id.edt_subject);
        edt_message = findViewById(R.id.edt_message);
        spinner_hall = findViewById(R.id.spinner_hall);

    }

    @Override
    protected void initData() {

        // TODO initData

        txt_actionbar_title.setText("CONTACT US");

        edt_name.setText(getSessionManager().getUserName());
        txt_email.setText(getSessionManager().getUserEmail());

        ArrayList<String> halls = new ArrayList<>();
        halls.add("Hall*");
        hallsList.add(null);

        HomeDishesResponse.Halls[] hallsResponse = getSessionManager().getHallsResponse();
        for (int i = 0; i < hallsResponse.length; i++) {
            hallsList.add(hallsResponse[i]);
            halls.add(hallsResponse[i].getName());
        }

        new SpinnerArrayAdapter(context, spinner_hall, halls, R.layout.spinner_item_contact_us, true,
                R.color.colorGreyHeading, R.color.colorGreyDishDeliveryTime).setAdapter();

        contactSupportPresenter = new ContactSupportPresenter(getAPIInterface());
        contactSupportPresenter.attachView(this);

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

        findViewById(R.id.img_spinner_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_hall.performClick();
            }
        });

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

        spinner_hall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    selectedHallId = "";
                } else {
                    selectedHallId = hallsList.get(position).getId();
                }

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
            contactSupportPresenter.detachView();
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    private void checkData() {

        if (AppUtils.isEditTextEmpty(edt_name)) {
            showToast(getString(R.string.emptyName));
            return;
        }

        if (AppUtils.isStringEmpty(selectedHallId)) {
            showToast(getString(R.string.emptyHall));
            return;
        }

        if (AppUtils.isEditTextEmpty(edt_subject)) {
            showToast(getString(R.string.emptySubject));
            return;
        }

        if (AppUtils.isEditTextEmpty(edt_message)) {
            showToast(getString(R.string.emptyMessage));
            return;
        }

        if (isInternetAvailable()) {

            showProgressDialog();
            contactSupportPresenter.contactSupport(
                    "",
                    edt_name.getText().toString(),
                    selectedHallId,
                    txt_email.getText().toString(),
                    edt_subject.getText().toString(),
                    edt_message.getText().toString()
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
