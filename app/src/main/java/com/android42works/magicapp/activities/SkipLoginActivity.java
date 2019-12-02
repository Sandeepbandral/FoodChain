package com.android42works.magicapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android42works.magicapp.R;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.interfaces.LoginAndRegisterInterface;
import com.android42works.magicapp.presenters.LoginOrRegisterPresenter;
import com.android42works.magicapp.responses.UserDetailsResponse;
import com.android42works.magicapp.utils.AppUtils;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;

/* Created by JSP@nesar */

public class SkipLoginActivity extends BaseActivity implements LoginAndRegisterInterface {

    private EditText edt_email, edt_pass;
    private LoginOrRegisterPresenter loginOrRegisterPresenter;
    private RelativeLayout rl_skip;
    private View actionbar;
    public static SkipLoginActivity instance;

    private CallbackManager mFacebookCallbackManager;
    private LoginManager mLoginManager;
    private AccessToken accessToken;
    private AccessTokenTracker mAccessTokenTracker;
    private String fbUserEmail, fbUserName, fbId, fbProfilePic;

    // get SkipLoginActivity instance
    public static SkipLoginActivity getInstance() {
        return instance;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.act_login;
    }

    @Override
    protected Context getActivityContext() {
        return SkipLoginActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

        instance = this;

        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pass);
        rl_skip = findViewById(R.id.rl_skip);
        actionbar = findViewById(R.id.actionbar);

    }

    @Override
    protected void initData() {

        // TODO initData

        rl_skip.setVisibility(View.GONE);
        actionbar.setVisibility(View.VISIBLE);

        loginOrRegisterPresenter = new LoginOrRegisterPresenter(getAPIInterface());
        loginOrRegisterPresenter.attachView(this);

    }

    @Override
    protected void initListener() {

        // TODO initListener

        findViewById(R.id.txt_forgotpass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ForgotPassActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

        findViewById(R.id.btn_fb_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetAvailable()) {
                    setupFacebookStuff();
                    handleFacebookLogin();
                } else {
                    showToast(getString(R.string.api_error_internet));
                }
            }
        });

        findViewById(R.id.txt_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SkipRegisterActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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

    // TODO Activity Methods >>>

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    private void checkData() {

        hideKeyboard(edt_pass);

        if (AppUtils.isEditTextEmpty(edt_email)) {
            showToast(getString(R.string.emptyEmail));
            return;
        }

        if (!AppUtils.isValidEmail(edt_email)) {
            showToast(getString(R.string.invalidEmail));
            return;
        }

        if (AppUtils.isEditTextEmpty(edt_pass)) {
            showToast(getString(R.string.emptyStudentId));
            return;
        }

        if (edt_pass.getText().toString().trim().length() < 8) {
            showToast(getString(R.string.invalidPassword));
            return;
        }


        if (isInternetAvailable()) {
            showProgressDialog();
            loginOrRegisterPresenter.loginUser(
                    edt_email.getText().toString(),
                    edt_pass.getText().toString(),
                    getDeviceId(),
                    getSessionManager().getFcmtoken()
            );
        } else {
            showToast(getString(R.string.api_error_internet));
        }

    }

    private void setupFacebookStuff() {

        // This should normally be on your application class
        FacebookSdk.sdkInitialize(getApplicationContext());

        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                //updateFacebookButtonUI();
            }
        };

        mLoginManager = LoginManager.getInstance();
        mFacebookCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //updateFacebookButtonUI();

                accessToken = loginResult.getAccessToken();
                getUserProfile(accessToken);
            }

            @Override
            public void onCancel() {

                Log.e("error", "");
                finish();
                showToast("Try again");

            }

            @Override
            public void onError(FacebookException error) {
                Log.e("error", "");
                finish();
                showToast("Try again");

            }
        });
    }

    private void handleFacebookLogin() {
        if (AccessToken.getCurrentAccessToken() != null) {
            mLoginManager.logOut();

            mAccessTokenTracker.startTracking();
            mLoginManager.logInWithReadPermissions(SkipLoginActivity.this, Arrays.asList("email"));

        } else {
            mAccessTokenTracker.startTracking();
            mLoginManager.logInWithReadPermissions(SkipLoginActivity.this, Arrays.asList("email"));
        }

    }

    private void getUserProfile(AccessToken currentAccessToken) {

        GraphRequest request = GraphRequest.newMeRequest(currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject json, GraphResponse response) {
                Log.e("fbResponse::", "::" + response.toString());
                if (response.getError() != null) {
                    // handle error
                    Log.e("error", "error");
                } else {
                    Log.e("success", "success");
                    try {
                        if (json.has("email")) {
                            fbUserEmail = json.getString("email");
                        } else {
                            fbUserEmail = "";
                        }
                        if (json.has("name")) {
                            fbUserName = json.getString("name");
                        } else {
                            fbUserName = "";
                        }
                        if (json.has("id")) {
                            fbId = json.getString("id");
                        } else {
                            fbUserName = "";
                        }

                        if (json.has("social_profile_pic")) {
                            fbProfilePic = json.getString("profile_url");
                        } else {
                            fbProfilePic = "";
                        }

                        if (fbUserEmail.equals("")) {

                        } else {
                            if (isInternetAvailable()) {
                                showProgressDialog();
                                loginOrRegisterPresenter.registerSocialUser(fbUserName, fbUserEmail, fbId, getDeviceId(), getSessionManager().getFcmtoken(), "Android", fbProfilePic);
                            }
                            else{
                                showToast(getString(R.string.api_error_internet));

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,birthday");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess_LoginOrRegister(String message, UserDetailsResponse userDetailsResponse, String name, boolean isFBLogin) {

        try {
            hideProgressDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userDetailsResponse.getData().getRole().equalsIgnoreCase("ROLE_RESTAURANT")) {
            showToast("Login details don't match with our database. Please try logging with other credentials.");
            return;
        }

        hideProgressDialog();
        getSessionManager().setUserLoginStatus(true);
        getSessionManager().setIsUserSkippedin(false);
        getSessionManager().setIsFBLogin(isFBLogin);
        getSessionManager().setUserDetailsResponse(userDetailsResponse);
        getSessionManager().setUserId(userDetailsResponse.getData().getId());
        getSessionManager().setUserEmail(userDetailsResponse.getData().getEmail());
        getSessionManager().setUserName(name);

        HomeActivity.getInstance().loadHomeData();

        Intent intent = new Intent("checkLoginStatus");
        context.sendBroadcast(intent);

        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }

    @Override
    public void onError(String message) {
        hideProgressDialog();
        showToast(message);
    }

}
