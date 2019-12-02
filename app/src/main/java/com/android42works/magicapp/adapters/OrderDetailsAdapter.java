package com.android42works.magicapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.models.TaxModel;
import com.android42works.magicapp.responses.OrderDetailResponse;
import com.android42works.magicapp.responses.OrdersResponse;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<OrderDetailResponse.Items> itemsList;
    private DecimalFormat formater = new DecimalFormat("0.00");

    public OrderDetailsAdapter(Context context, ArrayList<OrderDetailResponse.Items> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_order_detail, parent, false);
        return new OrderDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrderDetailsAdapter.ViewHolder holder, final int position) {

        String strHtml = "<b>" + itemsList.get(position).getItem_name() + "</b> " +  "&nbsp&nbsp<font color=#00704a>(x" +itemsList.get(position).getQty() + ")</font)";

        holder.txt_name.setText(Html.fromHtml(strHtml));

        double tmp = Double.parseDouble(itemsList.get(position).getPrice());

        String strHtmlCost = "<b>" + context.getString(R.string.currencySymbol) + " "+ String.format("%.2f", tmp) + "</b>";

        holder.txt_cost.setText(Html.fromHtml(strHtmlCost));

        if (itemsList.get(position).getVeg().equalsIgnoreCase("veg")) {
            holder.img_veg_nonveg.setImageResource(R.drawable.ic_veg);
        } else if (itemsList.get(position).getVeg().equalsIgnoreCase("non-veg")) {
            holder.img_veg_nonveg.setImageResource(R.drawable.ic_nonveg);
        } else if (itemsList.get(position).getVeg().equalsIgnoreCase("contain egg")) {
            holder.img_veg_nonveg.setImageResource(R.drawable.ic_egg);
        }

        if(position==itemsList.size()-1){
            holder.rl_divider.setVisibility(View.GONE);
        }else {
            holder.rl_divider.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_name, txt_cost;
        private ImageView img_veg_nonveg;
        private RelativeLayout rl_divider;

        ViewHolder(View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            txt_cost = itemView.findViewById(R.id.txt_cost);
            img_veg_nonveg = itemView.findViewById(R.id.img_veg_nonveg);
            rl_divider = itemView.findViewById(R.id.rl_divider);

        }

    }

}
