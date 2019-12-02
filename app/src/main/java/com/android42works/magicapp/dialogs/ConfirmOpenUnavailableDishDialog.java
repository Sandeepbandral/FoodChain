package com.android42works.magicapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.interfaces.LogoutInterface;
import com.android42works.magicapp.interfaces.UnavailableDishInterface;

public class ConfirmOpenUnavailableDishDialog {

    private Context context;
    private UnavailableDishInterface myInterface;
    private String dishId;

    public ConfirmOpenUnavailableDishDialog(Context context, UnavailableDishInterface myInterface, String dishId) {
        this.context = context;
        this.myInterface = myInterface;
        this.dishId = dishId;
    }

    public void show(){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirmopen);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

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
                myInterface.openDish_UnavailableDish(dishId);
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
