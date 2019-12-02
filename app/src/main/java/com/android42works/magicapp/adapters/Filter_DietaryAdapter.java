package com.android42works.magicapp.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
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
import com.android42works.magicapp.models.FilterModel;
import com.android42works.magicapp.models.FilterOptionsModel;

import java.util.ArrayList;

public class Filter_DietaryAdapter extends RecyclerView.Adapter<Filter_DietaryAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private FilterOptionsInterface filterOptionsInterface;
    private ArrayList<FilterModel> filtersList;

    public Filter_DietaryAdapter(Context context, FilterOptionsInterface filterOptionsInterface, ArrayList<FilterModel> filtersList) {

        this.context = context;
        this.filtersList = filtersList;
        this.filterOptionsInterface = filterOptionsInterface;
        this.mInflater = LayoutInflater.from(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_filter_dietary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txt_name.setText(filtersList.get(position).getName());

        if(filtersList.get(position).isExpanded()){
            holder.txt_name.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.img_arrow.setRotation(180);
            holder.recycler_options.setVisibility(View.VISIBLE);
        }else {
            holder.txt_name.setTextColor(context.getResources().getColor(R.color.colorGreyHeading));
            holder.img_arrow.setRotation(0);
            holder.recycler_options.setVisibility(View.GONE);
        }

        if(null!=filtersList.get(position).getOptionsList()){

            ArrayList<FilterOptionsModel> filtersOptionsList = filtersList.get(position).getOptionsList();

            if(filtersOptionsList.size()!=0){

                holder.recycler_options.setLayoutManager(new LinearLayoutManager(context));
                Filter_DietaryChildAdapter adapter = new Filter_DietaryChildAdapter(context, filterOptionsInterface, filtersOptionsList);
                holder.recycler_options.setAdapter(adapter);

            }

        }

        holder.rl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterOptionsInterface.onOptionClicked(position, !filtersList.get(position).isExpanded());
            }
        });

    }

    @Override
    public int getItemCount() {
        return filtersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_name;
        private ImageView img_arrow;
        private RecyclerView recycler_options;
        private RelativeLayout rl_parent;

        ViewHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            img_arrow = itemView.findViewById(R.id.img_arrow);
            recycler_options = itemView.findViewById(R.id.recycler_options);
            rl_parent = itemView.findViewById(R.id.rl_parent);
        }

    }

}
