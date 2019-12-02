package com.android42works.magicapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.adapters.FavouritesAdapter;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.interfaces.AddRemoveFavouriteInterface;
import com.android42works.magicapp.interfaces.DishOptionsInterface;
import com.android42works.magicapp.interfaces.FavouriteInterface;
import com.android42works.magicapp.presenters.AddRemoveFavouritePresenter;
import com.android42works.magicapp.presenters.FavouritePresenter;
import com.android42works.magicapp.responses.FavouriteResponse;
import com.android42works.magicapp.utils.AppUtils;
import com.android42works.magicapp.utils.Netwatcher;

import java.util.ArrayList;

/* Created by JSP@nesar */

public class FavouritesActivity extends BaseActivity implements DishOptionsInterface,
        FavouriteInterface, AddRemoveFavouriteInterface, Netwatcher.NetwatcherListener {

    private TextView txt_actionbar_title;
    private RecyclerView recycler_orders;
    private FavouritesAdapter favouritesAdapter;
    private ArrayList<FavouriteResponse.Dishes> dishesList = new ArrayList<>();
    private FavouritePresenter favouritePresenter;
    private AddRemoveFavouritePresenter addRemoveFavouritePresenter;
    private Netwatcher netwatcher;
    private int pageNo = 1;
    private String perPageCount = "";
    private View view_nodata, view_nointernet;

    /* Cart View */

    private View view_cart;
    private TextView txt_cart_count, txt_cart_price;
    private RelativeLayout rl_parent_cart;

    @Override
    protected int getLayoutView() {
        return R.layout.act_favourite;
    }

    @Override
    protected Context getActivityContext() {
        return FavouritesActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);
        recycler_orders = findViewById(R.id.recycler_orders);
        view_nodata = findViewById(R.id.view_nodata);
        view_nointernet = findViewById(R.id.view_nointernet);

        view_cart = findViewById(R.id.view_cart);
        rl_parent_cart = findViewById(R.id.rl_parent_cart);
        txt_cart_count = findViewById(R.id.txt_cart_count);
        txt_cart_price = findViewById(R.id.txt_cart_price);

    }

    @Override
    protected void initData() {

        // TODO initData

        netwatcher = new Netwatcher(this);

        txt_actionbar_title.setText("FAVORITE DISHES");

        registerReceiver(netwatcher, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        perPageCount = getString(R.string.perPageCount);

        favouritesAdapter = new FavouritesAdapter(context, this, dishesList);
        recycler_orders.setLayoutManager(new LinearLayoutManager(context));
        recycler_orders.setAdapter(favouritesAdapter);

        addRemoveFavouritePresenter = new AddRemoveFavouritePresenter(getAPIInterface());
        addRemoveFavouritePresenter.attachView(this);

        favouritePresenter = new FavouritePresenter(getAPIInterface());
        favouritePresenter.attachView(this);

        if (isInternetAvailable()) {
            showProgressDialog();
            favouritePresenter.getAllFavourites(getSessionManager().getUserId(), String.valueOf(pageNo), perPageCount);
        } else {
            view_nointernet.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void initListener() {

        // TODO initListener

        rl_parent_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CartActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        findViewById(R.id.btn_tryagain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetAvailable()) {
                    view_nointernet.setVisibility(View.GONE);
                    showProgressDialog();
                    favouritePresenter.getAllFavourites(getSessionManager().getUserId(), String.valueOf(pageNo), perPageCount);
                }else {
                    showToast(getString(R.string.api_error_internet));
                }
            }
        });

        findViewById(R.id.img_actionbar_backarrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

    }

    // TODO Activity Methods


    @Override
    protected void onPostResume() {
        super.onPostResume();
        checkAndSetCartCount();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    protected void onDestroy() {
        try{ unregisterReceiver(netwatcher); }catch (Exception e){}
        try{ favouritePresenter.detachView(); } catch (Exception e){}
        super.onDestroy();
    }

    public void checkAndSetCartCount() {

        try {

            int cartCount = Integer.parseInt(getSessionManager().getCartCount());
            if (cartCount != 0) {

                view_cart.setVisibility(View.VISIBLE);

                txt_cart_count.setVisibility(View.VISIBLE);
                txt_cart_count.setText(cartCount + "");

                String strCartPrice = getSessionManager().getCartPrice();
                double value = Double.parseDouble(strCartPrice);
                txt_cart_price.setText(getString(R.string.currencySymbol) + " " + String.format("%.2f", value));

            } else {

                view_cart.setVisibility(View.GONE);

            }

        } catch (Exception e) {

        }

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if(isConnected && view_nointernet.getVisibility()==View.VISIBLE){
            view_nointernet.setVisibility(View.GONE);
            showProgressDialog();
            favouritePresenter.getAllFavourites(getSessionManager().getUserId(), String.valueOf(pageNo), perPageCount);
        }

    }


    // TODO Interface Methods

    @Override
    public void onSuccess_getList(String message, ArrayList<FavouriteResponse.Dishes> dishesList) {
        hideProgressDialog();

        if (dishesList.size() > 0) {
            this.dishesList = dishesList;
            favouritesAdapter.updateList(true, dishesList);

            view_nodata.setVisibility(View.GONE);
            recycler_orders.setVisibility(View.VISIBLE);
        } else {
            view_nodata.setVisibility(View.VISIBLE);
            recycler_orders.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick_Dish(String dishId) {
        startActivity(new Intent(context, DishDetailsActivity.class).putExtra("dishId", dishId));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    public void onClick_Fav(int position, String dishId, String isFavourite) {

        if (isInternetAvailable()) {
            dishesList.remove(position);
            favouritesAdapter.updateList(false, dishesList);

            Intent intent = new Intent("reloadOnlyDishesData");
            intent.putExtra("dishId", dishId);
            intent.putExtra("isFavourite", isFavourite);
            context.sendBroadcast(intent);

            addRemoveFavouritePresenter.addRemoveFavourite(getSessionManager().getUserId(), dishId);

        } else {
            showToast(getString(R.string.api_error_internet));
        }

        if (dishesList.size() > 0) {
            view_nodata.setVisibility(View.GONE);
            recycler_orders.setVisibility(View.VISIBLE);
        } else {
            view_nodata.setVisibility(View.VISIBLE);
            recycler_orders.setVisibility(View.GONE);
        }

    }

    @Override
    public void onSuccess_Favourite(String message) {
        // Do Nothing


    }

    @Override
    public void onError(String message) {
        hideProgressDialog();
        showToast(message);

        view_nodata.setVisibility(View.VISIBLE);
        recycler_orders.setVisibility(View.GONE);
    }
}
