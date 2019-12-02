package com.android42works.magicapp.activities;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.interfaces.TermsInterface;
import com.android42works.magicapp.presenters.TermsPresenter;
import com.android42works.magicapp.utils.Netwatcher;

/* Created by JSP@nesar */

public class TermsOrPrivacyPolicyActivity extends BaseActivity implements TermsInterface, Netwatcher.NetwatcherListener {

    private TextView txt_actionbar_title;
    private WebView mWebview;
    private TermsPresenter termsPresenter;
    private View view_nointernet;
    private Netwatcher netwatcher;

    private String pageType = "";

    @Override
    protected int getLayoutView() {
        return R.layout.act_terms_or_privacy_policy;
    }

    @Override
    protected Context getActivityContext() {
        return TermsOrPrivacyPolicyActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);
        mWebview = findViewById(R.id.mWebview);
        view_nointernet = findViewById(R.id.view_nointernet);

    }

    @Override
    protected void initData() {

        // TODO initData

        netwatcher = new Netwatcher(this);

        registerReceiver(netwatcher, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        termsPresenter = new TermsPresenter(getAPIInterface());
        termsPresenter.attachView(this);

        mWebview.getSettings().setJavaScriptEnabled(true);

        pageType = getIntent().getStringExtra("pageType");

        if (isInternetAvailable()) {
            showProgressDialog();
            termsPresenter.getPageHtml(pageType);
        } else {
            view_nointernet.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void initListener() {

        // TODO initListener

        findViewById(R.id.btn_tryagain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetAvailable()) {
                    showProgressDialog();
                    termsPresenter.getPageHtml(pageType);
                }else {
                    showToast(getString(R.string.api_error_internet));
                }
            }
        });

        findViewById(R.id.img_actionbar_backarrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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
        try{ unregisterReceiver(netwatcher); }catch (Exception e){}
        try {
            termsPresenter.detachView();
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if(isConnected && view_nointernet.getVisibility()==View.VISIBLE){
            view_nointernet.setVisibility(View.GONE);
            showProgressDialog();
            termsPresenter.getPageHtml(pageType);
        }

    }

    // TODO Interface Methods

    @Override
    public void onSuccess(String message, String title, String htmlData) {
        hideProgressDialog();

        if (title.equalsIgnoreCase("TERMS")) {
            txt_actionbar_title.setText("TERMS AND CONDITIONS");
        } else {
            txt_actionbar_title.setText(title);
        }
        mWebview.setVisibility(View.VISIBLE);
        view_nointernet.setVisibility(View.GONE);
        mWebview.loadData(htmlData, "text/html", "UTF-8");
    }

    @Override
    public void onError(String message) {
        hideProgressDialog();
        showToast(message);
    }
}
