package com.android42works.magicapp.fragments;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android42works.magicapp.R;
import com.android42works.magicapp.activities.FilterActivity;
import com.android42works.magicapp.adapters.Filter_SortAdapter;
import com.android42works.magicapp.base.BaseFragment;
import com.android42works.magicapp.interfaces.FilterOptionsInterface;
import com.android42works.magicapp.models.FilterOptionsModel;
import com.android42works.magicapp.responses.HallMenuResponse;
import com.android42works.magicapp.responses.HomeDishesResponse;

import java.util.ArrayList;

/* Created by JSP@nesar */

public class Filter_Sort_Fragment extends BaseFragment implements FilterOptionsInterface {

    private RecyclerView mRecyclerView;
    private Filter_SortAdapter filterSortAdapter;
    private ArrayList<FilterOptionsModel> filterOptionsList;
    private String selectedSortingMethod = "";

    public static Filter_Sort_Fragment instance;

    public static Filter_Sort_Fragment getInstance() {
        return instance;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.frag_filter_options;
    }

    @Override
    protected Activity getActivityContext() {
        return getActivity();
    }

    @Override
    protected void initView(View v) {

        // TODO initView

        mRecyclerView = v.findViewById(R.id.mRecyclerView);

    }

    @Override
    protected void initData(View v) {

        // TODO initData

        instance = this;

        getFilterValues();

    }

    @Override
    protected void initListener(View v) {

        // TODO initListener

    }

    // TODO Activity Methods

    public void resetFilter(){
        selectedSortingMethod = "";
        saveValues();
        getFilterValues();
    }

    public void saveValues(){

        if(FilterActivity.getInstance().isHomeFilter()) {
            getSessionManager().setFilterSort(selectedSortingMethod);
        }else {
            getSessionManager().setHallFilterSort(selectedSortingMethod);
        }

    }

    private void getFilterValues(){

        selectedSortingMethod = "";

        filterOptionsList = new ArrayList<>();

        if(FilterActivity.getInstance().isHomeFilter()) {

            HomeDishesResponse.Filters filterResponse = getSessionManager().getFilterResponse();

            if (null != filterResponse) {

                if (null != filterResponse.getSort()) {

                    String[] sort = filterResponse.getSort();
                    for (int i = 0; i < sort.length; i++) {
                        filterOptionsList.add(new FilterOptionsModel("", sort[i], false));
                    }

                }

            }

        }else {

            HallMenuResponse.Filters filterResponse = getSessionManager().getHallFilterResponse();

            if (null != filterResponse) {

                if (null != filterResponse.getSort()) {

                    String[] sort = filterResponse.getSort();
                    for (int i = 0; i < sort.length; i++) {
                        filterOptionsList.add(new FilterOptionsModel("", sort[i], false));
                    }

                }

            }

        }

        checkForAnySavedValue();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        filterSortAdapter = new Filter_SortAdapter(context, this, filterOptionsList);
        mRecyclerView.setAdapter(filterSortAdapter);

    }

    public void checkForAnySavedValue(){

        String savedSortType = "";

        if(FilterActivity.getInstance().isHomeFilter()) {
            savedSortType = getSessionManager().getFilterSort();
        }else {
            savedSortType = getSessionManager().getHallFilterSort();
        }

        if(savedSortType.trim().length()!=0){

            for(int i=0; i<filterOptionsList.size(); i++){

                String sortType = filterOptionsList.get(i).getFilterName();
                if(sortType.equalsIgnoreCase(savedSortType)){
                    filterOptionsList.get(i).setSelected(true);
                    selectedSortingMethod = savedSortType;
                }

            }

        }


    }



// TODO Interface Methods

    @Override
    public void onOptionClicked(int position, boolean setSelected) {

        for(int i=0; i<filterOptionsList.size(); i++){
            filterOptionsList.get(i).setSelected(false);
        }

        filterOptionsList.get(position).setSelected(setSelected);
        filterSortAdapter.updateList(filterOptionsList);

        if(setSelected){
            selectedSortingMethod = filterOptionsList.get(position).getFilterName();
        }else {
            selectedSortingMethod = "";
        }


    }

}

