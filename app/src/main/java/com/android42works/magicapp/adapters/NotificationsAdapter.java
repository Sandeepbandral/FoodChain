package com.android42works.magicapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.activities.OrderDetailsActivity;
import com.android42works.magicapp.responses.Notifications;
import com.android42works.magicapp.responses.NotificationsResponse;
import com.android42works.magicapp.utils.AppUtils;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Notifications> notificationsList;

    public NotificationsAdapter(Context context, ArrayList<Notifications> notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
        this.mInflater = LayoutInflater.from(context);
    }

    public void updateList(ArrayList<Notifications> notificationsList) {
        this.notificationsList = notificationsList;
        notifyDataSetChanged();
    }

    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_notifications, parent, false);
        return new NotificationsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotificationsAdapter.ViewHolder holder, final int position) {

        String orderId = "";

        if(null!=notificationsList.get(position).getData()){
            orderId = notificationsList.get(position).getData().getOrder_id();
        }else {
            orderId = "";
        }

        if(orderId.trim().length()==0){
            holder.txt_order.setText(notificationsList.get(position).getTitle());
        }else {
            holder.txt_order.setText("Order No. " + notificationsList.get(position).getTitle());
        }


        holder.txt_title.setText(notificationsList.get(position).getMessage());
        holder.txt_datentime.setText(AppUtils.convertDate(notificationsList.get(position).getCreated_at()));

        if (position == notificationsList.size() - 1) {
            holder.rl_divider.setVisibility(View.GONE);
        } else {
            holder.rl_divider.setVisibility(View.VISIBLE);
        }

        holder.llDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String orderId1 = "";

                if(null!=notificationsList.get(position).getData()){
                    orderId1 = notificationsList.get(position).getData().getOrder_id();
                }else {
                    orderId1 = "";
                }

                if(orderId1.trim().length()!=0){
                    Intent intent = new Intent(context, OrderDetailsActivity.class);
                    intent.putExtra("orderId", orderId1);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_order, txt_title, txt_datentime;
        private ImageView img_watch;
        private RelativeLayout rl_divider, rl_timer;
        private LinearLayout llDetail;

        ViewHolder(View itemView) {
            super(itemView);

            txt_order = itemView.findViewById(R.id.txt_order);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_datentime = itemView.findViewById(R.id.txt_datentime);
            img_watch = itemView.findViewById(R.id.img_watch);
            rl_divider = itemView.findViewById(R.id.rl_divider);
            rl_timer = itemView.findViewById(R.id.rl_timer);
            llDetail = itemView.findViewById(R.id.llDetail);

        }

    }

}
