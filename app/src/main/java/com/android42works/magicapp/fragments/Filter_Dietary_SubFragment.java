package com.android42works.magicapp.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android42works.magicapp.R;
import com.android42works.magicapp.activities.FilterActivity;
import com.android42works.magicapp.adapters.Filter_DietaryChildAdapter;
import com.android42works.magicapp.base.BaseFragment;
import com.android42works.magicapp.interfaces.FilterOptionsInterface;
import com.android42works.magicapp.models.FilterOptionsModel;
import com.android42works.magicapp.responses.HallMenuResponse;
import com.android42works.magicapp.responses.HomeDishesResponse;

import java.util.ArrayList;

/* Created by JSP@nesar */

public class Filter_Dietary_SubFragment extends BaseFragment implements FilterOptionsInterface {

    private RecyclerView mRecyclerView;
    private ArrayList<FilterOptionsModel> filtersOptionsList = new ArrayList<>();

    private BroadcastReceiver broadcast_reciever;
    private IntentFilter intentFilter;

    private String baseCategoryId = "";

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

        baseCategoryId = getArguments().getString("categoryId");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        getFilterValues();

        intentFilter = new IntentFilter();
        intentFilter.addAction("resetFilter");
        intentFilter.addAction("unregisterFilter");

        if(broadcast_reciever==null) {

            broadcast_reciever = new BroadcastReceiver() {

                @Override
                public void onReceive(Context arg0, Intent intent) {

                    String action = intent.getAction();

                    if (action.equals("resetFilter")) {

                        if(FilterActivity.getInstance().isHomeFilter()) {
                            getSessionManager().setFilterDietary("");
                        }else {
                            getSessionManager().setHallFilterDietary("");
                        }

                        getFilterValues();
                    }

                    if (action.equals("unregisterFilter")) {
                        try { context.unregisterReceiver(broadcast_reciever); }catch (Exception e){}
                    }

                }

            };

        }

        context.registerReceiver(broadcast_reciever, intentFilter);

    }

    @Override
    protected void initListener(View v) {

        // TODO initListener

    }

    // TODO Activity Methods

    private void getFilterValues(){

        filtersOptionsList = new ArrayList<>();

        if(FilterActivity.getInstance().isHomeFilter()) {

            HomeDishesResponse.Filters filterResponse = getSessionManager().getFilterResponse();

            if (null != filterResponse) {

                if (null != filterResponse.getDietary()) {

                    HomeDishesResponse.Dietary[] dietaries = filterResponse.getDietary();

                    for (int i = 0; i < dietaries.length; i++) {

                        String tempId = dietaries[i].getId();
                        if (tempId.equalsIgnoreCase(baseCategoryId)) {

                            if (null != dietaries[i].getOptions()) {

                                HomeDishesResponse.Options[] optionsList = dietaries[i].getOptions();

                                if (optionsList.length != 0) {

                                    for (int j = 0; j < optionsList.length; j++) {

                                        filtersOptionsList.add(
                                                new FilterOptionsModel(
                                                        optionsList[j].getId(),
                                                        optionsList[j].getName(),
                                                        false
                                                )
                                        );

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }else {

            HallMenuResponse.Filters filterResponse = getSessionManager().getHallFilterResponse();

            if (null != filterResponse) {

                if (null != filterResponse.getDietary()) {

                    HallMenuResponse.Dietary[] dietaries = filterResponse.getDietary();

                    for (int i = 0; i < dietaries.length; i++) {

                        String tempId = dietaries[i].getId();
                        if (tempId.equalsIgnoreCase(baseCategoryId)) {

                            if (null != dietaries[i].getOptions()) {

                                HallMenuResponse.Options[] optionsList = dietaries[i].getOptions();

                                if (optionsList.length != 0) {

                                    for (int j = 0; j < optionsList.length; j++) {

                                        filtersOptionsList.add(
                                                new FilterOptionsModel(
                                                        optionsList[j].getId(),
                                                        optionsList[j].getName(),
                                                        false
                                                )
                                        );

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

        checkForAnySavedValue();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        Filter_DietaryChildAdapter adapter = new Filter_DietaryChildAdapter(context, this, filtersOptionsList);
        mRecyclerView.setAdapter(adapter);

    }

    public void checkForAnySavedValue(){

        String savedDietary = "";

        if(FilterActivity.getInstance().isHomeFilter()) {
            savedDietary = getSessionManager().getFilterDietary();
        }else {
            savedDietary = getSessionManager().getHallFilterDietary();
        }


        if(savedDietary.trim().length()!=0){

            String[] dietaryList = savedDietary.split(",");

            for(int x=0; x<dietaryList.length; x++){

                String savedDietaryId = dietaryList[x];

                for(int i=0; i<filtersOptionsList.size(); i++){

                    String dietaryId = filtersOptionsList.get(i).getFilterId();
                    if(dietaryId.equalsIgnoreCase(savedDietaryId)){
                        filtersOptionsList.get(i).setSelected(true);
                        Filter_DietaryFragment.getInstance().addDietaryId(filtersOptionsList.get(i).getFilterId());
                    }

                }

            }

        }

    }

    @Override
    public void onOptionClicked(int position, boolean setSelected) {

        filtersOptionsList.get(position).setSelected(setSelected);

        if(setSelected){
            Filter_DietaryFragment.getInstance().addDietaryId(filtersOptionsList.get(position).getFilterId());
        }else {
            Filter_DietaryFragment.getInstance().removeDietaryId(filtersOptionsList.get(position).getFilterId());
        }

    }


}

