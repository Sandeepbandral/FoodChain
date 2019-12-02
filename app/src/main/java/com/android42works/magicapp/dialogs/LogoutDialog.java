package com.android42works.magicapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.interfaces.LogoutInterface;

public class LogoutDialog {

    private Context context;
    private LogoutInterface myInterface;

    public LogoutDialog(Context context, LogoutInterface myInterface) {
        this.context = context;
        this.myInterface = myInterface;
    }

    public void show(){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        ImageView img_exit = dialog.findViewById(R.id.img_exit);
        img_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button btn_yes = dialog.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                myInterface.onLogoutClick();
            }
        });

        Button btn_no = dialog.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

}
