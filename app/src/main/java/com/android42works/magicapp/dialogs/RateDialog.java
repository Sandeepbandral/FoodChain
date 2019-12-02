package com.android42works.magicapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android42works.magicapp.R;
import com.android42works.magicapp.interfaces.LogoutInterface;
import com.android42works.magicapp.interfaces.OrderDetailsInterface;
import com.android42works.magicapp.interfaces.RateOrderInterface;

public class RateDialog {

    private Context context;
    private RateOrderInterface myInterface;
    private TextView txt_quality, txt_time, txt_taste, txt_packaging, txt_others;
    private boolean isQualityChecked = false, isTimeChecked = false, isTasteChecked = false, isPackagingChecked = false, isOthersChecked = false;

    /* Step 1 */

    private String userVote = "", orderId, items;

    /* Step 2 */

    public RateDialog(Context context, RateOrderInterface myInterface, String orderId, String items) {
        this.context = context;
        this.orderId = orderId;
        this.items = items;
        this.myInterface = myInterface;
    }

    public void show(){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rate);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final LinearLayout ll_step1 = dialog.findViewById(R.id.ll_step1);
        final LinearLayout ll_step2 = dialog.findViewById(R.id.ll_step2);

        final EditText edt_addnote = dialog.findViewById(R.id.edt_addnote);

        /* Step 1 */

        final TextView txt_orderno = dialog.findViewById(R.id.txt_orderno);
        final TextView txt_items = dialog.findViewById(R.id.txt_items);
        final ImageView img_rateup = dialog.findViewById(R.id.img_rateup);
        final ImageView img_ratedown = dialog.findViewById(R.id.img_ratedown);
        final RelativeLayout rl_rateup = dialog.findViewById(R.id.rl_rateup);
        final RelativeLayout rl_ratedown = dialog.findViewById(R.id.rl_ratedown);

        txt_orderno.setText("Order No. #" + orderId);
        txt_items.setText(items);

        rl_rateup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userVote = "up";
                img_rateup.setImageResource(R.drawable.ic_rateup_primary);
                img_ratedown.setImageResource(R.drawable.ic_ratedown_grey);
                rl_rateup.setBackgroundResource(R.drawable.bg_circle_white_border_green);
                rl_ratedown.setBackgroundResource(R.drawable.bg_circle_white_border_grey);
            }
        });

        rl_ratedown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userVote = "down";
                img_rateup.setImageResource(R.drawable.ic_rateup_grey);
                img_ratedown.setImageResource(R.drawable.ic_ratedown_primary);
                rl_rateup.setBackgroundResource(R.drawable.bg_circle_white_border_grey);
                rl_ratedown.setBackgroundResource(R.drawable.bg_circle_white_border_primary);
            }
        });

        Button btn_next = dialog.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userVote.trim().length()!=0){
                    ll_step1.setVisibility(View.GONE);
                    ll_step2.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(context, "Please select your vote", Toast.LENGTH_SHORT).show();
                }

            }
        });

        /* Step 2 */

        txt_quality = dialog.findViewById(R.id.txt_quality);
        txt_time = dialog.findViewById(R.id.txt_time);
        txt_taste = dialog.findViewById(R.id.txt_taste);
        txt_packaging = dialog.findViewById(R.id.txt_packaging);
        txt_others = dialog.findViewById(R.id.txt_others);

        txt_quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isQualityChecked){
                    txt_quality.setTextColor(context.getResources().getColor(R.color.colorGreyHeading));
                    txt_quality.setBackgroundResource(R.drawable.bg_white_border_grey_heading);
                }else {
                    txt_quality.setTextColor(context.getResources().getColor(R.color.colorWhite));
                    txt_quality.setBackgroundResource(R.color.colorGreenRateTag);
                }

                isQualityChecked = !isQualityChecked;

            }
        });

        txt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isTimeChecked){
                    txt_time.setTextColor(context.getResources().getColor(R.color.colorGreyHeading));
                    txt_time.setBackgroundResource(R.drawable.bg_white_border_grey_heading);
                }else {
                    txt_time.setTextColor(context.getResources().getColor(R.color.colorWhite));
                    txt_time.setBackgroundResource(R.color.colorGreenRateTag);
                }

                isTimeChecked = !isTimeChecked;

            }
        });

        txt_taste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isTasteChecked){
                    txt_taste.setTextColor(context.getResources().getColor(R.color.colorGreyHeading));
                    txt_taste.setBackgroundResource(R.drawable.bg_white_border_grey_heading);
                }else {
                    txt_taste.setTextColor(context.getResources().getColor(R.color.colorWhite));
                    txt_taste.setBackgroundResource(R.color.colorGreenRateTag);
                }

                isTasteChecked = !isTasteChecked;

            }
        });

        txt_packaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isPackagingChecked){
                    txt_packaging.setTextColor(context.getResources().getColor(R.color.colorGreyHeading));
                    txt_packaging.setBackgroundResource(R.drawable.bg_white_border_grey_heading);
                }else {
                    txt_packaging.setTextColor(context.getResources().getColor(R.color.colorWhite));
                    txt_packaging.setBackgroundResource(R.color.colorGreenRateTag);
                }

                isPackagingChecked = !isPackagingChecked;

            }
        });

        txt_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isOthersChecked){
                    txt_others.setTextColor(context.getResources().getColor(R.color.colorGreyHeading));
                    txt_others.setBackgroundResource(R.drawable.bg_white_border_grey_heading);
                }else {
                    txt_others.setTextColor(context.getResources().getColor(R.color.colorWhite));
                    txt_others.setBackgroundResource(R.color.colorGreenRateTag);
                }

                isOthersChecked = !isOthersChecked;

            }
        });

        Button btn_submit = dialog.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myInterface.onRateApply(userVote, getUserFeedbackArea(), edt_addnote.getText().toString());
                dialog.dismiss();

            }
        });

        dialog.show();

    }

    private String getUserFeedbackArea(){

        String userFeedbackArea = "";

        if(isQualityChecked) {
            if(userFeedbackArea.trim().length()==0){ userFeedbackArea = "Quality"; } else { userFeedbackArea = userFeedbackArea + "," + "Quality"; }
        }

        if(isTimeChecked) {
            if(userFeedbackArea.trim().length()==0){ userFeedbackArea = "Time"; } else { userFeedbackArea = userFeedbackArea + "," + "Time"; }
        }

        if(isTasteChecked) {
            if(userFeedbackArea.trim().length()==0){ userFeedbackArea = "Taste"; } else { userFeedbackArea = userFeedbackArea + "," + "Taste"; }
        }

        if(isPackagingChecked) {
            if(userFeedbackArea.trim().length()==0){ userFeedbackArea = "Packaging"; } else { userFeedbackArea = userFeedbackArea + "," + "Packaging"; }
        }

        if(isOthersChecked) {
            if(userFeedbackArea.trim().length()==0){ userFeedbackArea = "Others"; } else { userFeedbackArea = userFeedbackArea + "," + "Others"; }
        }

        return userFeedbackArea;

    }

}
