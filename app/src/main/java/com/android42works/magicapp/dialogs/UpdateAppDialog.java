package com.android42works.magicapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.fragments.NotificationsFragment;

public class UpdateAppDialog {

    private Context context;

    public UpdateAppDialog(Context context) {
        this.context = context;
    }

    public void show(){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_updateapp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageView img_exit = dialog.findViewById(R.id.img_exit);
        img_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button btn_update = dialog.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.example.android"));
                context.startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();

    }

}
