package com.android42works.magicapp.fragments;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android42works.magicapp.R;
import com.android42works.magicapp.activities.FilterActivity;
import com.android42works.magicapp.adapters.Filter_CuisineAdapter;
import com.android42works.magicapp.base.BaseFragment;
import com.android42works.magicapp.interfaces.FilterOptionsInterface;
import com.android42works.magicapp.models.FilterOptionsModel;
import com.android42works.magicapp.responses.HallMenuResponse;
import com.android42works.magicapp.responses.HomeDishesResponse;

import java.util.ArrayList;

/* Created by JSP@nesar */

public class Filter_Cuisines_Fragment extends BaseFragment implements FilterOptionsInterface {

    private RecyclerView mRecyclerView;
    private Filter_CuisineAdapter filterCuisineAdapter;
    private ArrayList<FilterOptionsModel> filterOptionsList;

    public static Filter_Cuisines_Fragment instance;

    public static Filter_Cuisines_Fragment getInstance() {
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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        getFilterValues();

    }

    @Override
    protected void initListener(View v) {

        // TODO initListener

    }

    // TODO Activity Methods

    public void resetFilter(){

        if(FilterActivity.getInstance().isHomeFilter()) {
            getSessionManager().setFilterCuisine("");
        }else {
            getSessionManager().setHallFilterCuisine("");
        }

        getFilterValues();

    }

    private void getFilterValues(){

        filterOptionsList = new ArrayList<>();

        if(FilterActivity.getInstance().isHomeFilter()) {

            HomeDishesResponse.Filters filterResponse = getSessionManager().getFilterResponse();

            if (null != filterResponse) {

                if (null != filterResponse.getSort()) {

                    HomeDishesResponse.Cuisines[] cuisines = filterResponse.getCuisines();
                    for (int i = 0; i < cuisines.length; i++) {
                        filterOptionsList.add(new FilterOptionsModel(cuisines[i].getId(), cuisines[i].getName(), false));
                    }

                }

            }

        }else {

            HallMenuResponse.Filters filterResponse = getSessionManager().getHallFilterResponse();

            if (null != filterResponse) {

                if (null != filterResponse.getSort()) {

                    HallMenuResponse.Cuisines[] cuisines = filterResponse.getCuisines();
                    for (int i = 0; i < cuisines.length; i++) {
                        filterOptionsList.add(new FilterOptionsModel(cuisines[i].getId(), cuisines[i].getName(), false));
                    }

                }

            }

        }

        checkForAnySavedValue();

        filterCuisineAdapter = new Filter_CuisineAdapter(context, this, filterOptionsList);
        mRecyclerView.setAdapter(filterCuisineAdapter);

    }

    public void checkForAnySavedValue(){

        String savedCuisines = "";

        if(FilterActivity.getInstance().isHomeFilter()) {
            savedCuisines = getSessionManager().getFilterCuisine();
        }else {
            savedCuisines = getSessionManager().getHallFilterCuisine();
        }

        if(savedCuisines.trim().length()!=0){

            String[] cuisinesList = savedCuisines.split(",");

            for(int x=0; x<cuisinesList.length; x++){

                String savedCuisineId = cuisinesList[x];

                for(int i=0; i<filterOptionsList.size(); i++){

                    String cuisineId = filterOptionsList.get(i).getFilterId();
                    if(cuisineId.equalsIgnoreCase(savedCuisineId)){
                        filterOptionsList.get(i).setSelected(true);
                    }

                }

            }



        }


    }

    // TODO Interface Methods

    @Override
    public void onOptionClicked(int position, boolean setSelected) {

        filterOptionsList.get(position).setSelected(setSelected);

    }

    public void saveValues(){

        String strCuisines = "";

        for(int i=0; i<filterOptionsList.size(); i++){
            if(filterOptionsList.get(i).isSelected()){
                if(strCuisines.trim().length()==0){
                    strCuisines = filterOptionsList.get(i).getFilterId();
                }else {
                    strCuisines = strCuisines + "," + filterOptionsList.get(i).getFilterId();
                }
            }
        }

        if(FilterActivity.getInstance().isHomeFilter()) {
            getSessionManager().setFilterCuisine(strCuisines);
        }else {
            getSessionManager().setHallFilterCuisine(strCuisines);
        }


    }

}

