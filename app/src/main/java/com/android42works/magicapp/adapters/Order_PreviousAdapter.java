package com.android42works.magicapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.interfaces.OrderDetailsInterface;
import com.android42works.magicapp.responses.OrdersResponse;
import com.android42works.magicapp.utils.AppUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Order_PreviousAdapter extends RecyclerView.Adapter<Order_PreviousAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private OrderDetailsInterface myInterface;
    private ArrayList<OrdersResponse.Orders> ordersList;
    private DecimalFormat formater = new DecimalFormat("0.00");

    public Order_PreviousAdapter(Context context, OrderDetailsInterface myInterface, ArrayList<OrdersResponse.Orders> ordersList) {
        this.context = context;
        this.myInterface = myInterface;
        this.ordersList = ordersList;
        this.mInflater = LayoutInflater.from(context);
    }

    public void updateList(ArrayList<OrdersResponse.Orders> ordersList){
        this.ordersList = ordersList;
        notifyDataSetChanged();
    }

    @Override
    public Order_PreviousAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_order_delivered, parent, false);
        return new Order_PreviousAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Order_PreviousAdapter.ViewHolder holder, final int position) {

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
        holder.txt_price.setText(context.getString(R.string.currencySymbol) + " "+String.format("%.2f", value));

        String items = "";

        if(null!=ordersList.get(position).getItems()){

            OrdersResponse.Items[] itemsArray = ordersList.get(position).getItems();
            for(int i=0; i<itemsArray.length; i++){
                if(i==0){
                    items = itemsArray[i].getItem_name();
                }else {
                    items = items + ", " + itemsArray[i].getItem_name();
                }
            }

        }

        holder.txt_name.setText(items);

        String status = ordersList.get(position).getStatus();
        status = status.replace("_", " ");

        status = capitalize(status);

        Log.e("status???","status???"+status);

        //status = status.substring(0,1).toUpperCase() + status.substring(1);

        if(status.trim().contains("Cancelled")){

            holder.img_tick.setImageResource(R.drawable.ic_cross_withorangecircle);
            holder.img_rate.setVisibility(View.GONE);
            holder.btn_rate.setVisibility(View.GONE);
            holder.txt_status.setText(status);
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.colorStatusRed));

        }else {

            holder.img_tick.setImageResource(R.drawable.ic_tick_orangecircle);
            holder.txt_status.setText(status);
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.colorStatusGreen));

            if(ordersList.get(position).getThumb().toString().trim().length()!=0){

                if(ordersList.get(position).getThumb().toString().equalsIgnoreCase("up")){
                    holder.img_rate.setVisibility(View.VISIBLE);
                    holder.img_rate.setImageResource(R.drawable.ic_rateup_primary);
                    holder.btn_rate.setVisibility(View.GONE);
                }

                if(ordersList.get(position).getThumb().toString().equalsIgnoreCase("down")){
                    holder.img_rate.setVisibility(View.VISIBLE);
                    holder.img_rate.setImageResource(R.drawable.ic_ratedown_primary);
                    holder.btn_rate.setVisibility(View.GONE);
                }

            }else {

                holder.img_rate.setVisibility(View.GONE);
                holder.btn_rate.setVisibility(View.VISIBLE);

            }

        }

        holder.ll_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onClick(position, "openDish");
            }
        });

        holder.btn_reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onClick(position, "reorderDish");
            }
        });

        holder.btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onClick(position, "rateDish");
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

        private TextView txt_order, txt_hall, txt_name, txt_price, txt_time, txt_status;
        private LinearLayout ll_parent;
        private ImageView img_rate, img_tick;
        private Button btn_reorder, btn_rate;

        ViewHolder(View itemView) {
            super(itemView);

            img_rate = itemView.findViewById(R.id.img_rate);
            txt_order = itemView.findViewById(R.id.txt_order);
            txt_hall = itemView.findViewById(R.id.txt_hall);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_time = itemView.findViewById(R.id.txt_time);
            ll_parent = itemView.findViewById(R.id.ll_parent);
            btn_reorder = itemView.findViewById(R.id.btn_reorder);
            btn_rate = itemView.findViewById(R.id.btn_rate);
            txt_status = itemView.findViewById(R.id.txt_status);
            img_tick = itemView.findViewById(R.id.img_tick);

        }

    }

}
