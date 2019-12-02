package com.android42works.magicapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.models.TaxModel;

import java.util.ArrayList;

public class TaxAdapter extends RecyclerView.Adapter<TaxAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<TaxModel> taxList;

    public TaxAdapter(Context context,ArrayList<TaxModel> taxList) {
        this.context = context;
        this.taxList = taxList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public TaxAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_tax, parent, false);
        return new TaxAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TaxAdapter.ViewHolder holder, final int position) {

        holder.txt_title.setText(taxList.get(position).getName());

        double value = Double.parseDouble(taxList.get(position).getAmount());
        holder.txt_amount.setText(context.getString(R.string.currencySymbol) + " "+String.format("%.2f", value));

    }

    @Override
    public int getItemCount() {
        return taxList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_title, txt_amount;

        ViewHolder(View itemView) {
            super(itemView);

            txt_title = itemView.findViewById(R.id.txt_title);
            txt_amount = itemView.findViewById(R.id.txt_amount);

        }

    }

}
