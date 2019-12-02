package com.android42works.magicapp.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.interfaces.FilterOptionsInterface;
import com.android42works.magicapp.models.FilterOptionsModel;

import java.util.ArrayList;

public class Filter_CuisineAdapter extends RecyclerView.Adapter<Filter_CuisineAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private FilterOptionsInterface filterOptionsInterface;
    private ArrayList<FilterOptionsModel> filterOptionsList;

    public Filter_CuisineAdapter(Context context, FilterOptionsInterface filterOptionsInterface, ArrayList<FilterOptionsModel> filterOptionsList) {

        this.context = context;
        this.filterOptionsList = filterOptionsList;
        this.filterOptionsInterface = filterOptionsInterface;
        this.mInflater = LayoutInflater.from(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_filter_cuisines, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txt_name.setText(filterOptionsList.get(position).getFilterName());

        final boolean isSelected = filterOptionsList.get(position).isSelected();

        if(isSelected){
            holder.txt_name.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.checkbox.setChecked(true);
        }else {
            holder.txt_name.setTextColor(context.getResources().getColor(R.color.colorGreyHeading));
            holder.checkbox.setChecked(false);
        }

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filterOptionsInterface.onOptionClicked(position, isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return filterOptionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_name;
        private AppCompatCheckBox checkbox;

        ViewHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            checkbox = itemView.findViewById(R.id.checkbox);
        }

    }

}
