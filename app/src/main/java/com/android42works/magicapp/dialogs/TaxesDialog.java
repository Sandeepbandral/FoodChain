package com.android42works.magicapp.dialogs;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.adapters.TaxAdapter;
import com.android42works.magicapp.models.TaxModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TaxesDialog {

    private Context context;
    private String strTaxInfo, strTotal;

    ArrayList<TaxModel> taxList;

    public TaxesDialog(Context context, String strTaxInfo, String strTotal) {
        this.context = context;
        this.strTaxInfo = strTaxInfo;
        this.strTotal = strTotal;
    }

    public void show(){

        try {

            taxList = new ArrayList<>();

            DecimalFormat formater = new DecimalFormat("0.00");

            try {

                JSONArray jsonArray = new JSONArray(strTaxInfo);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString("title");
                    String amount = jsonObject.getString("percentage");


                    double tmp4 = Double.parseDouble(amount);
                    amount = formater.format(tmp4);

                    taxList.add(new TaxModel(title, amount));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            final BottomSheetDialog dialog = new BottomSheetDialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_tax);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            double tmp = Double.parseDouble(strTotal);
            strTotal = formater.format(tmp);

            TextView txt_total = dialog.findViewById(R.id.txt_total);
            txt_total.setText(context.getString(R.string.currencySymbol) + " "+ String.format("%.2f", tmp));

            ImageView img_exit = dialog.findViewById(R.id.img_exit);
            img_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            RecyclerView recycler_taxes = dialog.findViewById(R.id.recycler_taxes);
            recycler_taxes.setLayoutManager(new LinearLayoutManager(context));
            TaxAdapter adapter = new TaxAdapter(context, taxList);
            recycler_taxes.setAdapter(adapter);

            dialog.show();

        }catch (Exception e){

        }

    }

}
