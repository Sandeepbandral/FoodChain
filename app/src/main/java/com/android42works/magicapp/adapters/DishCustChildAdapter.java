package com.android42works.magicapp.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.activities.HomeActivity;
import com.android42works.magicapp.interfaces.DishDetailsInterface;
import com.android42works.magicapp.models.DishCustChildModel;

import java.util.ArrayList;

public class DishCustChildAdapter extends RecyclerView.Adapter<DishCustChildAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private DishDetailsInterface myInterface;
    private ArrayList<DishCustChildModel> optionsList;
    private int maxSelection, parentPosition;
    private boolean isSingleSelection, isMaxSelected;
    private String strMax = "";

    public DishCustChildAdapter(Context context, DishDetailsInterface myInterface,
                                ArrayList<DishCustChildModel> optionsList, int parentPosition, int maxSelection,
                                boolean isSingleSelection, boolean isMaxSelected, String _strMax) {
        this.context = context;
        this.myInterface = myInterface;
        this.optionsList = optionsList;
        this.parentPosition = parentPosition;
        this.maxSelection = maxSelection;
        this.isSingleSelection = isSingleSelection;
        this.isMaxSelected = isMaxSelected;
        this.mInflater = LayoutInflater.from(context);
        this.strMax = _strMax;
    }

    public void updateList(ArrayList<DishCustChildModel> optionsList, boolean isMaxSelected) {
        this.optionsList = optionsList;
        this.isMaxSelected = isMaxSelected;
        notifyDataSetChanged();
    }

    @Override
    public DishCustChildAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_dish_cust_child, parent, false);
        return new DishCustChildAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DishCustChildAdapter.ViewHolder holder, final int position) {

//        if (isSingleSelection && !strMax.equalsIgnoreCase("*")) {

        if (isSingleSelection && !strMax.equalsIgnoreCase("*")) {

            holder.rdbtn_option.setVisibility(View.VISIBLE);
            holder.chkbxk_option.setVisibility(View.GONE);
            holder.rdbtn_option.setText(optionsList.get(position).getName());
            holder.rdbtn_option.setChecked(optionsList.get(position).getSelected());


        } else {

            holder.rdbtn_option.setVisibility(View.GONE);
            holder.chkbxk_option.setVisibility(View.VISIBLE);
            if (!"".equals(optionsList.get(position).getPrice())) {
                holder.chkbxk_option.setText(optionsList.get(position).getName());

                double value = Double.parseDouble(optionsList.get(position).getPrice());
                holder.addon_price_txt.setText("$ " + String.format("%.2f", value));
            } else {
                holder.chkbxk_option.setText(optionsList.get(position).getName());

            }
            holder.chkbxk_option.setChecked(optionsList.get(position).getSelected());

            if (isMaxSelected && (optionsList.get(position).getSelected() == false)) {
                holder.chkbxk_option.setEnabled(false);
            } else {
                holder.chkbxk_option.setEnabled(true);
            }

        }

        holder.rdbtn_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.rdbtn_option.isChecked()) {

                    myInterface.onSelect_Option(parentPosition, position, true, optionsList.get(position).getPrice());
                } else {

                    //optionsList.get(position).setSelected(false);
                    myInterface.onSelect_Option(parentPosition, position, false, optionsList.get(position).getPrice());
                }
            }
        });

        /*holder.rdbtn_option.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.e("1111","1111");
                myInterface.onSelect_Option(parentPosition, position, isChecked, optionsList.get(position).getPrice());
            }
        });*/

        holder.chkbxk_option.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                myInterface.onSelect_Option(parentPosition, position, isChecked, optionsList.get(position).getPrice());

            }
        });

    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatRadioButton rdbtn_option;
        private AppCompatCheckBox chkbxk_option;
        private TextView addon_price_txt;

        ViewHolder(View itemView) {
            super(itemView);

            rdbtn_option = itemView.findViewById(R.id.rdbtn_option);
            chkbxk_option = itemView.findViewById(R.id.chkbxk_option);
            addon_price_txt = itemView.findViewById(R.id.addon_price_txt);

        }

    }

}
