package com.android42works.magicapp.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android42works.magicapp.dialogs.MyProgressDialog;
import com.android42works.magicapp.interfaces.PermissionsInterface;
import com.android42works.magicapp.retrofit.APIClient;
import com.android42works.magicapp.retrofit.APIInterface;
import com.android42works.magicapp.stripe.StripeAPIClient;
import com.android42works.magicapp.stripe.StripeAPIInterface;
import com.android42works.magicapp.utils.NetworkDetector;
import com.android42works.magicapp.utils.SessionManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.Collection;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/* Created by JSP@nesar */

public abstract class BaseActivity extends AppCompatActivity {

    public Context context;
    private MyProgressDialog myProgressDialog;
    private int progressDialogCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(getLayoutView());
        context = getActivityContext();
        initView();
        initData();
        initListener();

    }


    /* Abstract Methods */

    protected abstract int getLayoutView();
    protected abstract Context getActivityContext();
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initListener();


    /* Pass context to Calligraphy */

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }


    /* Permissions */

    public void askPermissions(final Collection<String> permissions, final PermissionsInterface permissionsInterface){

        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            Dexter.withActivity((Activity)context)
                    .withPermissions(permissions)
                    .withListener(new MultiplePermissionsListener() {

                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {

                    boolean grantedStatus = report.areAllPermissionsGranted();
                    boolean permanentlyDenied = report.isAnyPermissionPermanentlyDenied();

                    if(permanentlyDenied){
                        permissionsInterface.onPermissionsDenied(true);
                    }else {
                        if (grantedStatus) {
                            permissionsInterface.onPermissionsGranted();
                        } else {
                            permissionsInterface.onPermissionsDenied(false);
                        }
                    }

                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    token.continuePermissionRequest();
                }

            }).check();

        }else {

            permissionsInterface.onPermissionsGranted();

        }

    }


    /* Permissions Dialog */

    public void openPermissionsScreen(final boolean isPermanentalyDenied,String message,
            final PermissionsInterface permissionsInterface,final Collection<String> permissions){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Information");
        alertDialog.setMessage(message);

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

                if(isPermanentalyDenied){   // Settings screen
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 0);

                }else {     // retry permissions
                    askPermissions(permissions, permissionsInterface);
                }

            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();

    }


    /* Public Methods */

    public SessionManager getSessionManager(){
        return new SessionManager(context);

    }

    public APIInterface getAPIInterface(){
        return APIClient.getClient(context).create(APIInterface.class);
    }

    public StripeAPIInterface getStripeAPIInterface(){
        return StripeAPIClient.getClient(context).create(StripeAPIInterface.class);
    }

    public boolean isInternetAvailable(){
        return new NetworkDetector(context).isInternetAvailable();
    }

    public void showToast(String message){

        if (message.trim().length() != 0) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }

    }

    public void showProgressDialog(){

        if(myProgressDialog==null){
            myProgressDialog = new MyProgressDialog(context);
        }

        if(!myProgressDialog.isDialogVisisble()){
            myProgressDialog.showDialog();
        }

        progressDialogCount++;

    }

    public void hideProgressDialog(){

        if(myProgressDialog!=null && myProgressDialog.isDialogVisisble()){
            progressDialogCount--;
            if(progressDialogCount<1) {
                myProgressDialog.hideDialog();
            }
        }

    }

    public void hideKeyboard(View view) {

        InputMethodManager inputMethodManager =(InputMethodManager)context.getSystemService(context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    public String getDeviceId(){

        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

    }

}
