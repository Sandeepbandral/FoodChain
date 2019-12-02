package com.android42works.magicapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.android42works.magicapp.R;

public class MyProgressDialog {

    private Context context;
    private Dialog dialog;

    public MyProgressDialog(Context context) {

        this.context = context;

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

    }

    public void showDialog(){

        if(dialog!=null){
            if(!dialog.isShowing()) {
                dialog.show();
            }

        }

    }

    public boolean isDialogVisisble(){
        return dialog.isShowing();
    }

    public void hideDialog(){

        if(dialog!=null){
            dialog.dismiss();
        }

    }


}
