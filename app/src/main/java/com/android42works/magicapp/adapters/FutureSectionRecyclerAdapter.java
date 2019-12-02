package com.android42works.magicapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.models.FutureTimeChildModel;
import com.android42works.magicapp.models.FutureTimeParentModel;
import com.android42works.magicapp.sectionedrecyclerview.SectionRecyclerViewAdapter;

import java.util.List;

/**
 * Created by apple on 11/7/16.
 */

public class FutureSectionRecyclerAdapter extends SectionRecyclerViewAdapter<FutureTimeParentModel, FutureTimeChildModel, FutureSectionRecyclerAdapter.SectionViewHolder, FutureSectionRecyclerAdapter. ChildViewHolder> {

    Context context;

    public FutureSectionRecyclerAdapter(Context context, List<FutureTimeParentModel> sectionHeaderItemList) {
        super(context, sectionHeaderItemList);
        this.context = context;
    }

    @Override
    public SectionViewHolder onCreateSectionViewHolder(ViewGroup sectionViewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listitem_future_section, sectionViewGroup, false);
        return new SectionViewHolder(view);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listitem_future_child, childViewGroup, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindSectionViewHolder(SectionViewHolder sectionViewHolder, int sectionPosition, FutureTimeParentModel sectionHeader) {
        sectionViewHolder.name.setText(sectionHeader.sectionText);
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder childViewHolder, int sectionPosition, int childPosition, FutureTimeChildModel child) {
        childViewHolder.name.setText(child.getTimingRange());
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        public SectionViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.sectionHeader);
        }
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ChildViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.sectionChild);
        }
    }

}
