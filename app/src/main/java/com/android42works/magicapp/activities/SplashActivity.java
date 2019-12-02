package com.android42works.magicapp.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.utils.Netwatcher;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.jsoup.Jsoup;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;

/* Created by JSP@nesar */

public class SplashActivity extends BaseActivity {

    private boolean isRunning = false;
    private boolean isUpdated = true;
    private Dialog dialog;

    @Override
    protected int getLayoutView() {
        return R.layout.act_splash;
    }

    @Override
    protected Context getActivityContext() {
        return SplashActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        isRunning = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    protected void initData() {

        // TODO initData

        if(isGooglePlayServicesAvailable(SplashActivity.this)){

            if(isInternetAvailable()){
                new CheckUpdateAsyncTask().execute();
            }else {
                showToast(getString(R.string.api_error_internet));
            }

        }else {
           showToast(getString(R.string.error_play_services));
        }


        Fabric.with(this, new Crashlytics());

        getSessionManager().setSelectedHall("");
        getSessionManager().setHallFilterSort("");
        getSessionManager().setFilterSort("");
        getSessionManager().setHallFilterDietary("");
        getSessionManager().setFilterDietary("");
        getSessionManager().setHallFilterCuisine("");
        getSessionManager().setFilterCuisine("");
        getSessionManager().setIsHallfilterActive(false);
        getSessionManager().setIsHomefilterActive(false);
        startDelayHandler();

       /* try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("Hash", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("Hash", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("Hash", "printHashKey()", e);
        }*/

    }

    @Override
    protected void initListener() {

        // TODO initListener

    }


    // TODO Activity Methods

    private void startDelayHandler(){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                // Check whether activity is terminated or not
                if (isRunning) {

                    if (isUpdated) {

                        if (getSessionManager().isUserLoggedIn() || getSessionManager().isUserSkippedLoggedIn()) {
                            startActivity(new Intent(context, HomeActivity.class));
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        } else {
                            startActivity(new Intent(context, LoginActivity.class));
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }

                        finish();
                    }
                }

            }
        }, 3000);

    }

    private boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    private class CheckUpdateAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String newVersion = "0.0";

            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=com.android42works.magicapp&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".hAyfc .htlgb")
                        .get(7)
                        .ownText();
                return newVersion;
            } catch (Exception e) {
                return newVersion;
            }

        }


        @Override
        protected void onPostExecute(String newVersion) {
            matchUpdate(newVersion);
        }

        @Override
        protected void onPreExecute() {     }

        @Override
        protected void onProgressUpdate(String... text) {     }

    }


    public void matchUpdate(String newVersion){

        try {

            if(newVersion.equalsIgnoreCase("0.0")){
                Log.e("updated version code", " error in getting version ");
                isUpdated = true;
            }else {
                PackageInfo packageInfo = null;
                try {
                    packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                int version_code = packageInfo.versionCode;
                String version_name = packageInfo.versionName;
                Log.e("updated version code", String.valueOf(version_code) + "  " + version_name);
                if (Float.parseFloat(version_name) < Float.parseFloat(newVersion)) {
                    isUpdated = false;
                }else{
                    isUpdated = true;
                }

            }

        } catch (Exception e) {
            isUpdated = true;
            Log.e("updated version code", " error in getting version ");
        }

        if(isUpdated) {

            // App is already updated

        }else {

            showUpdatePopup();

        }


    }

    private void showUpdatePopup(){

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_updateapp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button btn_update = dialog.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();

    }


}
