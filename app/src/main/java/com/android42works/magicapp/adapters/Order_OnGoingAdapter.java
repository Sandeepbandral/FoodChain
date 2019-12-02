package com.android42works.magicapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android42works.magicapp.R;
import com.android42works.magicapp.dialogs.RateDialog;
import com.android42works.magicapp.interfaces.OrderDetailsInterface;
import com.android42works.magicapp.models.OrdersModel;
import com.android42works.magicapp.responses.OrdersResponse;
import com.android42works.magicapp.utils.AppUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Order_OnGoingAdapter extends RecyclerView.Adapter<Order_OnGoingAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private OrderDetailsInterface myInterface;
    private ArrayList<OrdersResponse.Orders> ordersList;
    private DecimalFormat formater = new DecimalFormat("0.00");

    public Order_OnGoingAdapter(Context context, OrderDetailsInterface myInterface, ArrayList<OrdersResponse.Orders> ordersList) {
        this.context = context;
        this.myInterface = myInterface;
        this.ordersList = ordersList;
        this.mInflater = LayoutInflater.from(context);
    }

    public void updateList(ArrayList<OrdersResponse.Orders> ordersList) {
        this.ordersList = ordersList;
        notifyDataSetChanged();
    }

    @Override
    public Order_OnGoingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_order_ongoing, parent, false);
        return new Order_OnGoingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Order_OnGoingAdapter.ViewHolder holder, final int position) {

        holder.txt_order.setText("Order No. : " + ordersList.get(position).getOrder_number());
        holder.txt_hall.setText(ordersList.get(position).getRestaurant_name());

        if (ordersList.get(position).getIs_immediate().length() > 0) {
            if (ordersList.get(position).getIs_immediate().equals("1")) {
                holder.txt_time.setText("Immediate");
            } else {
                holder.txt_time.setText(AppUtils.convertDate(ordersList.get(position).getTiming()));
            }
        } else {
            holder.txt_time.setText(AppUtils.convertDate(ordersList.get(position).getTiming()));
        }

        String startTime = ordersList.get(position).getStart_time();
        String endTime = ordersList.get(position).getEnd_time();

        if(!AppUtils.isStringEmpty(startTime) && !AppUtils.isStringEmpty(endTime)){
            holder.txt_time.setText(startTime + " - " + endTime);
        }

        double tmp = Double.parseDouble(ordersList.get(position).getGrand_total());

        String price = formater.format(tmp);
        double value = Double.parseDouble(price);
        holder.txt_price.setText(context.getString(R.string.currencySymbol) + " " + String.format("%.2f", value));

        String items = "";

        if (null != ordersList.get(position).getItems()) {

            OrdersResponse.Items[] itemsArray = ordersList.get(position).getItems();
            for (int i = 0; i < itemsArray.length; i++) {
                if (i == 0) {
                    items = itemsArray[i].getItem_name();
                } else {
                    items = items + ", " + itemsArray[i].getItem_name();
                }
            }

        }

        holder.txt_name.setText(items);

        holder.txt_status.setTextColor(context.getResources().getColor(R.color.colorStatusGreen));

        String status = ordersList.get(position).getStatus();
        status = status.replace("_", " ");
        //status = status.substring(0, 1).toUpperCase() + status.substring(1);

        status = capitalize(status);

        holder.txt_status.setText(status);

        if (ordersList.get(position).getCancel_request().equalsIgnoreCase("1")) {
            holder.txtOrderStatus.setVisibility(View.VISIBLE);
        }else{
            holder.txtOrderStatus.setVisibility(View.GONE);
        }

        holder.ll_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onClick(position, "openDish");
            }
        });

        holder.btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onClick(position, "statusDish");
            }
        });

        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ordersList.get(position).getCancel_request().equalsIgnoreCase("0")) {
                    myInterface.onClick(position, "cancelDish");
                } else if (ordersList.get(position).getCancel_request().equalsIgnoreCase("1")) {
                    Toast.makeText(context, "Cancel request has already been sent to the admin. Please wait for approval.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_order, txt_hall, txt_name, txt_price, txt_time, txt_status, txtOrderStatus;
        private LinearLayout ll_parent;
        private Button btn_status, btn_cancel;

        ViewHolder(View itemView) {
            super(itemView);

            txt_order = itemView.findViewById(R.id.txt_order);
            txt_hall = itemView.findViewById(R.id.txt_hall);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_time = itemView.findViewById(R.id.txt_time);
            ll_parent = itemView.findViewById(R.id.ll_parent);
            btn_status = itemView.findViewById(R.id.btn_status);
            btn_cancel = itemView.findViewById(R.id.btn_cancel);
            txt_status = itemView.findViewById(R.id.txt_status);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);

        }

    }

}
