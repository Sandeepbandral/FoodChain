package com.android42works.magicapp.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.interfaces.FilterOptionsInterface;
import com.android42works.magicapp.models.FilterOptionsModel;

import java.util.ArrayList;

public class Filter_SortAdapter extends RecyclerView.Adapter<Filter_SortAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private FilterOptionsInterface filterOptionsInterface;
    private ArrayList<FilterOptionsModel> filterOptionsList;

    public Filter_SortAdapter(Context context, FilterOptionsInterface filterOptionsInterface, ArrayList<FilterOptionsModel> filterOptionsList) {
        this.context = context;
        this.filterOptionsList = filterOptionsList;
        this.filterOptionsInterface = filterOptionsInterface;
        this.mInflater = LayoutInflater.from(context);
    }

    public void updateList(ArrayList<FilterOptionsModel> filterOptionsList){
        this.filterOptionsList = filterOptionsList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_filter_sort, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txt_name.setText(filterOptionsList.get(position).getFilterName());

        final boolean isSelected = filterOptionsList.get(position).isSelected();

        if(isSelected){
            holder.txt_name.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.img_check.setVisibility(View.VISIBLE);
        }else {
            holder.txt_name.setTextColor(context.getResources().getColor(R.color.colorGreyHeading));
            holder.img_check.setVisibility(View.GONE);
        }

        holder.rl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterOptionsInterface.onOptionClicked(position, !isSelected);
            }
        });

    }

    @Override
    public int getItemCount() {
        return filterOptionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_name;
        private ImageView img_check;
        private RelativeLayout rl_parent;

        ViewHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            img_check = itemView.findViewById(R.id.img_check);
            rl_parent = itemView.findViewById(R.id.rl_parent);
        }

    }

}
