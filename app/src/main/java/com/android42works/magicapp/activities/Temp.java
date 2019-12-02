package com.android42works.magicapp.activities;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.base.BaseActivity;

/* Created by JSP@nesar */

public class Temp extends BaseActivity {

    private TextView txt_actionbar_title;

    @Override
    protected int getLayoutView() {
        return R.layout.act_temp;
    }  /* Set layout here */

    @Override
    protected Context getActivityContext() {
        return null;
    }  /* Set context here */

    @Override
    protected void initView() {

        // TODO initView

        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);

    }

    @Override
    protected void initData() {

        // TODO initData

        context = Temp.this;

        txt_actionbar_title.setText("");


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

    }

    // TODO Activity Methods

    @Override
    public void onBackPressed() {
        finish(); overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    protected void onDestroy() {
        /* Detach presenter here if used */
        super.onDestroy();
    }
}
