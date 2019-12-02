package com.android42works.magicapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.adapters.FutureSectionRecyclerAdapter;
import com.android42works.magicapp.models.FutureTimeParentModel;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.android42works.magicapp.utils.AppUtils;
import com.android42works.magicapp.utils.SessionManager;

import java.util.ArrayList;

public class FutureTimingsDialog {

    private Dialog dialog;

    public FutureTimingsDialog(
            Context context, String hallId,
            String hallName, String mealIds,
            String mealNames, String dishName,
            boolean showHallTimings) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_future_timings);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        /* initView */

        ImageView img_exit = dialog.findViewById(R.id.img_exit);
        TextView txt_hall_name = dialog.findViewById(R.id.txt_hall_name);
        RecyclerView mRecyclerView = dialog.findViewById(R.id.mRecyclerView);

        /* initData */

        if(showHallTimings){
            txt_hall_name.setText(hallName);
        }else {
            txt_hall_name.setText(dishName);
        }


        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setHasFixedSize(true);

        ArrayList<FutureTimeParentModel> futureTimings = new ArrayList<>();

        SessionManager sessionManager = new SessionManager(context);
        if(null!=sessionManager.getHallsResponse()){
            HomeDishesResponse.Halls[] halls = sessionManager.getHallsResponse();

            if(showHallTimings){
                futureTimings = AppUtils.getHallFutureTimings(hallId, halls);
            }else {
                futureTimings = AppUtils.getMealFutureTimings(hallId, mealIds, mealNames, halls);
            }

        }

        FutureSectionRecyclerAdapter adapterSectionRecycler = new FutureSectionRecyclerAdapter(context, futureTimings);
        mRecyclerView.setAdapter(adapterSectionRecycler);


        /* initListeners */

        img_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

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
