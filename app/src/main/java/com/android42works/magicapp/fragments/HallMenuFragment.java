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
import com.android42works.magicapp.activities.DishDetailsActivity;
import com.android42works.magicapp.activities.HallMenuActivity;
import com.android42works.magicapp.activities.SkipLoginActivity;
import com.android42works.magicapp.adapters.HallMenu_ChildAdapter;
import com.android42works.magicapp.base.BaseFragment;
import com.android42works.magicapp.interfaces.AddRemoveFavouriteInterface;
import com.android42works.magicapp.interfaces.FavouriteInterface;
import com.android42works.magicapp.interfaces.HallMenu_ChildInterface;
import com.android42works.magicapp.interfaces.UnavailableDishInterface;
import com.android42works.magicapp.presenters.AddRemoveFavouritePresenter;
import com.android42works.magicapp.presenters.FavouritePresenter;
import com.android42works.magicapp.responses.FavouriteResponse;
import com.android42works.magicapp.responses.HallMenuResponse;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.android42works.magicapp.utils.AppUtils;

import java.util.ArrayList;

/* Created by JSP@nesar */

public class HallMenuFragment extends BaseFragment implements HallMenu_ChildInterface, AddRemoveFavouriteInterface, UnavailableDishInterface {

    private RecyclerView mRecyclerView;
    private HallMenu_ChildAdapter hallMenuChildAdapter;
    private ArrayList<HallMenuResponse.Dishes> dishList = new ArrayList<>();

    private BroadcastReceiver broadcast_reciever;
    private IntentFilter intentFilter;

    private AddRemoveFavouritePresenter addRemoveFavouritePresenter;

    private String baseCategoryId = "";

    public HallMenuFragment() {}

    @Override
    protected int getLayoutView() {
        return R.layout.frag_hallmenu;
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

        addRemoveFavouritePresenter = new AddRemoveFavouritePresenter(getAPIInterface());
        addRemoveFavouritePresenter.attachView(this);

        intentFilter = new IntentFilter();
        intentFilter.addAction("loadHallMenuSubFragments");
        intentFilter.addAction("reloadOnlyDishesData");
        intentFilter.addAction("unregisterHallMenuSubFragments");

        if(broadcast_reciever==null) {

            broadcast_reciever = new BroadcastReceiver() {

                @Override
                public void onReceive(Context arg0, Intent intent) {

                    String action = intent.getAction();

                    if (action.equals("loadHallMenuSubFragments")) {
                        LoadAllFragments();
                    }

                    if (action.equals("reloadOnlyDishesData")) {
                        String dishId = intent.getStringExtra("dishId");
                        String isFavourite = intent.getStringExtra("isFavourite");
                        if(hallMenuChildAdapter!=null){
                            hallMenuChildAdapter.updateList(dishId, isFavourite);
                        }
                    }

                    if (action.equals("unregisterHallMenuSubFragments")) {
                        try{ addRemoveFavouritePresenter.detachView(); }catch (Exception e){}
                        try{ context.unregisterReceiver(broadcast_reciever); }catch (Exception e){}
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


    // TODO Public Methods

    private void LoadAllFragments(){

        HallMenuResponse hallMenuResponse = getSessionManager().getHallMenuResponse();

        if(null!=hallMenuResponse.getData()) {

            if (null != hallMenuResponse.getData().getCategories()) {

                HallMenuResponse.Categories[] categories = hallMenuResponse.getData().getCategories();

                for(int i=0; i<categories.length; i++){

                    String id = categories[i].getId();

                    if(id.equalsIgnoreCase(baseCategoryId)){

                        if(null!=categories[i].getDishes()){

                            HallMenuResponse.Dishes[] dishes = categories[i].getDishes();

                            dishList = new ArrayList<>();
                            for(int j=0; j<dishes.length; j++){
                              dishList.add(dishes[j]);
                            }

                            checkAndArrangeAvailableDishes();

                            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                            hallMenuChildAdapter = new HallMenu_ChildAdapter(context, this, dishList, this);
                            mRecyclerView.setAdapter(hallMenuChildAdapter);

                        }

                    }

                }

            }

        }

    }

    private void checkAndArrangeAvailableDishes(){

        HomeDishesResponse.Halls[] halls = getSessionManager().getHallsResponse();

        ArrayList<HallMenuResponse.Dishes> AvailableDishList = new ArrayList<>();
        ArrayList<HallMenuResponse.Dishes> UnavailableDishList = new ArrayList<>();

        for(int j=0; j<dishList.size(); j++){

            String status = dishList.get(j).getStatus();

            if(status.equals("1")){         // Available

                boolean isAnyMealIdActive = AppUtils.isAnyMealIdActive(HallMenuActivity.getInstance().hallId, dishList.get(j).getMeal_ids(), halls);

                if(isAnyMealIdActive){      // Meal timings are available

                    AvailableDishList.add(dishList.get(j));

                }else {                     // Meal timings are closed

                    dishList.get(j).setStatus("");
                    UnavailableDishList.add(dishList.get(j));

                }

            }else {                         // UnAvailable

                dishList.get(j).setStatus("");
                UnavailableDishList.add(dishList.get(j));

            }

        }

        dishList = new ArrayList<>();
        dishList.addAll(AvailableDishList);
        dishList.addAll(UnavailableDishList);

    }

    // TODO Interface Methods

    @Override
    public void onClick_dish(String dishId) {

        startActivity(new Intent(context, DishDetailsActivity.class).putExtra("dishId", dishId));
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }

    @Override
    public void onClick_fav(String dishId, String isFavourite) {

        if(getSessionManager().isUserSkippedLoggedIn()){
            startActivity(new Intent(context, SkipLoginActivity.class));
            context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }else {

            if(isInternetAvailable()) {

                Intent intent = new Intent("reloadOnlyDishesData");
                intent.putExtra("dishId", dishId);
                intent.putExtra("isFavourite", isFavourite);
                context.sendBroadcast(intent);

                addRemoveFavouritePresenter.addRemoveFavourite( getSessionManager().getUserId(), dishId );
            }else {
                showToast(getString(R.string.api_error_internet));
            }

        }

    }

    @Override
    public void onError(String message) {
        showToast(message);
    }

    @Override
    public void onSuccess_Favourite(String message) {
        // Do Nothing
    }

    @Override
    public void openDish_UnavailableDish(String dishId) {

        startActivity(new Intent(context, DishDetailsActivity.class).putExtra("dishId", dishId));
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }
}

