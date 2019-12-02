package com.android42works.magicapp.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.base.BaseActivity;

/**
 * Created by apple on 10/10/18.
 */

public class AboutUsActivity extends BaseActivity {

    private TextView txt_actionbar_title;

    @Override
    protected int getLayoutView() {
        return R.layout.layout_about_us;
    }

    @Override
    protected Context getActivityContext() {
        return AboutUsActivity.this;
    }

    @Override
    protected void initView() {
        // TODO initView

        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);
    }

    @Override
    protected void initData() {
        // TODO initData

        txt_actionbar_title.setText("ABOUT");
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

        findViewById(R.id.rl_rate_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.rl_terms_conditions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, TermsOrPrivacyPolicyActivity.class)
                        .putExtra("pageType", "terms")
                );
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        findViewById(R.id.rl_privacy_policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context, TermsOrPrivacyPolicyActivity.class)
                        .putExtra("pageType", "privacy-policy")
                );
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

}
