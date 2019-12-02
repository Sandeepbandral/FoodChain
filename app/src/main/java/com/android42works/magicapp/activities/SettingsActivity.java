package com.android42works.magicapp.activities;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.responses.HomeDishesResponse;

/* Created by JSP@nesar */

public class SettingsActivity extends BaseActivity {

    private TextView txt_actionbar_title, txtEmail;
    private RelativeLayout rl_changepass, rl_divider_pass;

    private String strFullText = "Be its bug you found or an area you want us to serve,\nshoot us an email here and we will try our best.";
    private String strtextWithHere = "Be its bug you found or an area you want us to serve,\nshoot us an email here";
    private String strtextWithoutHere = "Be its bug you found or an area you want us to serve,\nshoot us an email ";

    @Override
    protected int getLayoutView() {
        return R.layout.act_settings;
    }

    @Override
    protected Context getActivityContext() {
        return SettingsActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);
        txtEmail = findViewById(R.id.txtEmail);
        rl_changepass = findViewById(R.id.rl_changepass);
        rl_divider_pass = findViewById(R.id.rl_divider_pass);

    }

    @Override
    protected void initData() {

        // TODO initData

        txt_actionbar_title.setText("SETTINGS");
        txtEmail.setMovementMethod(LinkMovementMethod.getInstance());
        txtEmail.setText(showSpannableText(strFullText), TextView.BufferType.SPANNABLE);

        if(getSessionManager().isFBLogin()){
            rl_changepass.setVisibility(View.GONE);
            rl_divider_pass.setVisibility(View.GONE);
        }else {
            rl_changepass.setVisibility(View.VISIBLE);
            rl_divider_pass.setVisibility(View.VISIBLE);
        }

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

        findViewById(R.id.rl_contact_support).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeDishesResponse.Halls[] hallsResponse = getSessionManager().getHallsResponse();
                if(null!=hallsResponse){
                    startActivity(new Intent(context, ContactUsActivity.class));
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }else {
                    if(!isInternetAvailable()){
                        showToast(getString(R.string.api_error_internet));
                    }else {
                        showToast(getString(R.string.api_error_data));
                    }
                }

            }
        });

        findViewById(R.id.rl_about_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AboutUsActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        rl_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ChangePassActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

    }

    private CharSequence showSpannableText(String strText) {

        SpannableStringBuilder ssb = new SpannableStringBuilder(strText);

        int idx1 = strtextWithoutHere.length();

        int idx2 = strtextWithHere.length();

        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                HomeDishesResponse.Halls[] hallsResponse = getSessionManager().getHallsResponse();
                if(null!=hallsResponse){
                    startActivity(new Intent(context, ContactUsActivity.class));
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }else {
                    if(!isInternetAvailable()){
                        showToast(getString(R.string.api_error_internet));
                    }else {
                        showToast(getString(R.string.api_error_data));
                    }
                }

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(context.getResources().getColor(R.color.colorPrimary));
                ds.setUnderlineText(false); // set to false to remove underline
            }
        }, idx1, idx2, 0);

        return ssb;
    }

    // TODO Activity Methods

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

}
