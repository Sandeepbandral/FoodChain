package com.android42works.magicapp.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.interfaces.DishDetailsInterface;
import com.android42works.magicapp.models.DishCustChildModel;
import com.android42works.magicapp.models.DishCustParentModel;

import java.util.ArrayList;

public class DishCustParentAdapter extends RecyclerView.Adapter<DishCustParentAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private DishDetailsInterface myInterface;
    private ArrayList<DishCustParentModel> customizationList;
    private ArrayList<DishCustChildAdapter> adaptersList = new ArrayList<>();
    private boolean isDishOpenedFromCart;

    public DishCustParentAdapter(Context context, DishDetailsInterface myInterface,
                                 ArrayList<DishCustParentModel> customizationList, boolean _isDishOpenedFromCart) {
        this.context = context;
        this.myInterface = myInterface;
        this.customizationList = customizationList;
        this.mInflater = LayoutInflater.from(context);
        this.isDishOpenedFromCart = _isDishOpenedFromCart;

    }

    public void updateList(ArrayList<DishCustParentModel> customizationList) {
        this.customizationList = customizationList;

        notifyDataSetChanged();
    }

    @Override
    public DishCustParentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_dish_cust_parent, parent, false);
        return new DishCustParentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DishCustParentAdapter.ViewHolder holder, final int position) {

        Log.e("customizationList::", "::" + customizationList.size());

        if (position == customizationList.size() - 1) {
            holder.bottomLine.setVisibility(View.GONE);
        } else {
            holder.bottomLine.setVisibility(View.VISIBLE);
        }

        int maxSelection = 1;
        String strHtml = "";
        String strMax = customizationList.get(position).getMaxoptions();

        if (strMax.equalsIgnoreCase("*")) {
            strHtml = "<b>" + customizationList.get(position).getName() + "</b>";
        } else {
            try {
                maxSelection = Integer.parseInt(strMax);
            } catch (Exception e) {
            }
            strHtml = "<b>" + customizationList.get(position).getName() + "</b> (Choose " + maxSelection + ")";
        }

        holder.txt_name.setText(Html.fromHtml(strHtml));

        DishCustChildAdapter adapter = null;

        if (null != customizationList.get(position).getOptionList()) {

            ArrayList<DishCustChildModel> optionsList = customizationList.get(position).getOptionList();

            if (strMax.equalsIgnoreCase("*")) {
                maxSelection = optionsList.size();
            }

            if (optionsList.size() != 0) {

                boolean isMaxSelected = false, isSingleSelection = true;
                int count = 0;

                for (int i = 0; i < optionsList.size(); i++) {
                    if (optionsList.get(i).getSelected()) {
                        count++;
                    }
                }


                if (count == maxSelection) {
                    isMaxSelected = true;
                } else {
                    isMaxSelected = false;
                }

                if (maxSelection == 1) {
                    isSingleSelection = true;
                } else {
                    isSingleSelection = false;
                }

                if (position > adaptersList.size() - 1) {

                    for (int i = 0; i < optionsList.size(); i++) {
                        //    Log.e("getSelected::", "getSelected::" + optionsList.get(i).getSelected());
                    }

                    if (isSingleSelection == true && !strMax.equalsIgnoreCase("*")) {

                        if (isDishOpenedFromCart == false) {
                            optionsList.get(0).setSelected(true);
                        }
                    }


                    adapter = new DishCustChildAdapter(context, myInterface, optionsList, position, maxSelection, isSingleSelection,
                            isMaxSelected, strMax);
                    holder.recycler_cust.setLayoutManager(new LinearLayoutManager(context));
                    holder.recycler_cust.setAdapter(adapter);
                    adaptersList.add(position, adapter);

                } else {

                    if (null != adaptersList.get(position)) {
                        adaptersList.get(position).updateList(optionsList, isMaxSelected);
                    }

                }

            } else {

                adaptersList.add(position, adapter);

            }
        }

    }

    @Override
    public int getItemCount() {
        return customizationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_name;
        private RecyclerView recycler_cust;
        private RelativeLayout bottomLine;

        ViewHolder(View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            recycler_cust = itemView.findViewById(R.id.recycler_cust);
            bottomLine = itemView.findViewById(R.id.bottomLine);
        }

    }

}
