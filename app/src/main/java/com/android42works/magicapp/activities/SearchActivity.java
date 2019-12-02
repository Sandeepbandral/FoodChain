package com.android42works.magicapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.adapters.SearchAdapter;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.interfaces.AddRemoveFavouriteInterface;
import com.android42works.magicapp.interfaces.DishOptionsInterface;
import com.android42works.magicapp.interfaces.SearchInterface;
import com.android42works.magicapp.interfaces.UnavailableDishInterface;
import com.android42works.magicapp.presenters.AddRemoveFavouritePresenter;
import com.android42works.magicapp.presenters.SearchPresenter;
import com.android42works.magicapp.responses.SearchResponse;
import com.android42works.magicapp.utils.AppUtils;
import com.android42works.magicapp.utils.KeyboardUtils;
import com.android42works.magicapp.utils.Netwatcher;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/* Created by JSP@nesar */

public class SearchActivity extends BaseActivity implements SearchInterface, DishOptionsInterface, AddRemoveFavouriteInterface, Netwatcher.NetwatcherListener, UnavailableDishInterface {

    private TextView txt_actionbar_title, txt_nodata_title, txt_nodata_desc, txt_hall;
    private EditText edt_actionbar_search;
    private ImageView img_actionbar_cross, img_actionbar_search, img_nodata;
    private AVLoadingIndicatorView progress_search;
    private RelativeLayout rl_actionbar_search, rl_filter;
    private Handler timerHandler;
    private Runnable timerRunnable;
    private View view_nodata;
    private SearchPresenter searchPresenter;
    private AddRemoveFavouritePresenter addRemoveFavouritePresenter;
    private int pageNo = 1;
    private String perPageCount = "";
    private RecyclerView recycler_dishes;
    private SearchAdapter searchAdapter;
    private boolean hasMoreData = true, showHallName = false;
    private LinearLayoutManager manager;
    private String hallName = "", hallId = "", baseCategoryId = "", subCategoryId = "", filterMode = "";
    private ArrayList<SearchResponse.Dishes> dishList = new ArrayList<>();
    private SwipyRefreshLayout swipyrefreshlayout;

    /* No Internet View */

    private View view_nointernet;
    private Netwatcher netwatcher;

    /* Cart View */

    private View view_cart;
    private TextView txt_cart_count, txt_cart_price;
    private RelativeLayout rl_parent_cart;

    @Override
    protected int getLayoutView() {
        return R.layout.act_search;
    }

    @Override
    protected Context getActivityContext() {
        return SearchActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);
        edt_actionbar_search = findViewById(R.id.edt_actionbar_search);
        img_actionbar_cross = findViewById(R.id.img_actionbar_cross);
        img_actionbar_search = findViewById(R.id.img_actionbar_search);
        rl_actionbar_search = findViewById(R.id.rl_actionbar_search);
        progress_search = findViewById(R.id.progress_search);
        view_nodata = findViewById(R.id.view_nodata);
        recycler_dishes = findViewById(R.id.recycler_dishes);
        img_nodata = findViewById(R.id.img_nodata);
        txt_nodata_title = findViewById(R.id.txt_nodata_title);
        txt_nodata_desc = findViewById(R.id.txt_nodata_desc);
        txt_hall = findViewById(R.id.txt_hall);
        rl_filter = findViewById(R.id.rl_filter);
        swipyrefreshlayout = findViewById(R.id.swipyrefreshlayout);
        view_nointernet = findViewById(R.id.view_nointernet);

        view_cart = findViewById(R.id.view_cart);
        rl_parent_cart = findViewById(R.id.rl_parent_cart);
        txt_cart_count = findViewById(R.id.txt_cart_count);
        txt_cart_price = findViewById(R.id.txt_cart_price);

    }

    @Override
    protected void initData() {

        // TODO initData

        context = SearchActivity.this;

        netwatcher = new Netwatcher(this);
        registerReceiver(netwatcher, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        edt_actionbar_search.setHint("Type here to search");

        showHallName = getIntent().getBooleanExtra("showHallName", false);
        filterMode = getIntent().getStringExtra("filterMode");
        hallName = getIntent().getStringExtra("hallName");
        hallId = getIntent().getStringExtra("hallId");

        baseCategoryId = getIntent().getStringExtra("baseCategoryId");
        subCategoryId = getIntent().getStringExtra("subCategoryId");

        if(baseCategoryId==null) { baseCategoryId = ""; }
        if(subCategoryId==null) { subCategoryId = ""; }

        perPageCount = getString(R.string.perPageCount);

        img_nodata.setImageResource(R.drawable.ic_nodata_search);
        txt_nodata_title.setText("No Matches");
        txt_nodata_desc.setText("Try broadening your search");

        txt_actionbar_title.setVisibility(View.GONE);
        rl_actionbar_search.setVisibility(View.VISIBLE);

        manager = new LinearLayoutManager(context);
        recycler_dishes.setLayoutManager(manager);
        searchAdapter = new SearchAdapter(context, this, dishList, showHallName, this);
        recycler_dishes.setAdapter(searchAdapter);

        addRemoveFavouritePresenter = new AddRemoveFavouritePresenter(getAPIInterface());
        addRemoveFavouritePresenter.attachView(this);

        searchPresenter = new SearchPresenter(getAPIInterface());
        searchPresenter.attachView(this);

        txt_hall.setText("Selected Hall: " + hallName);

        if(filterMode.equalsIgnoreCase("home") || filterMode.equalsIgnoreCase("hall")){
            rl_filter.setVisibility(View.VISIBLE);

        }

        startSearchWithDelay();

    }

    @Override
    protected void initListener() {

        // TODO initListener

        findViewById(R.id.btn_tryagain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetAvailable()) {
                    view_nointernet.setVisibility(View.GONE);
                    pageNo = 1;
                    searchDishes();
                }else {
                    showToast(getString(R.string.api_error_internet));
                }
            }
        });

        rl_parent_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CartActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        findViewById(R.id.img_actionbar_backarrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(edt_actionbar_search);
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        findViewById(R.id.img_actionbar_cross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_actionbar_search.setText("");
            }
        });

        findViewById(R.id.txt_clear_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dishList = new ArrayList<>();
                searchAdapter.updateList(dishList, showHallName);
                rl_filter.setVisibility(View.GONE);
                if (filterMode.equalsIgnoreCase("home")) {
                    hallId = "";
                    showHallName = true;
                }
                filterMode = "";
                startSearchWithDelay();
            }
        });

        edt_actionbar_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (AppUtils.isStringEmpty(s.toString())) {

                    img_actionbar_cross.setVisibility(View.GONE);
                    dishList = new ArrayList<>();
                    searchAdapter.updateList(dishList, showHallName);
                    startSearchWithDelay();

                } else {
                    img_actionbar_cross.setVisibility(View.VISIBLE);
                    pageNo = 1;
                    startSearchWithDelay();
                }
            }
        });

        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {

                if (isVisible) {
                    view_cart.setVisibility(View.GONE);
                } else {
                    checkAndSetCartCount();
                }
            }
        });

        swipyrefreshlayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP) {

                    if (isInternetAvailable()) {
                        pageNo = 1;
                        searchDishes();
                    } else {
                        swipyrefreshlayout.setRefreshing(false);
                        showToast(getString(R.string.api_error_internet));
                    }

                } else {

                    if (hasMoreData) {

                        if (isInternetAvailable()) {
                            pageNo++;
                            searchDishes();
                        } else {
                            swipyrefreshlayout.setRefreshing(false);
                            showToast(getString(R.string.api_error_internet));
                        }

                    } else {
                        swipyrefreshlayout.setRefreshing(false);
                    }

                }

            }
        });

    }


    // TODO Activity Methods

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        checkAndSetCartCount();
    }

    @Override
    protected void onDestroy() {
        try{ unregisterReceiver(netwatcher); }catch (Exception e){}
        try {
            searchPresenter.detachView();
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if(isConnected && view_nointernet.getVisibility()==View.VISIBLE){
            view_nointernet.setVisibility(View.GONE);
            pageNo = 1;
            searchDishes();
        }

    }

    private void startSearchWithDelay() {

        if (timerHandler != null) {
            timerHandler.removeCallbacks(timerRunnable);
        }

        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                searchDishes();
                timerHandler.removeCallbacks(timerRunnable);
            }
        };

        timerHandler.postDelayed(timerRunnable, 700);

    }

    private void searchDishes() {

        String strSearch = edt_actionbar_search.getText().toString();

        if (isInternetAvailable()) {

            if(pageNo==1) {
                dishList = new ArrayList<>();
                searchAdapter.updateList(dishList, showHallName);
            }

            img_actionbar_search.setVisibility(View.GONE);
            progress_search.setVisibility(View.VISIBLE);

            if (filterMode.trim().length()!=0) {

                String strSortMethod = "", strCuisines = "", strDietary = "";

                if(filterMode.equalsIgnoreCase("home") || filterMode.equalsIgnoreCase("seeall")) {
                    strSortMethod = getSessionManager().getFilterSort();
                    strCuisines = getSessionManager().getFilterCuisine();
                    strDietary = getSessionManager().getFilterDietary();
                }else{
                    strSortMethod = getSessionManager().getHallFilterSort();
                    strCuisines = getSessionManager().getHallFilterCuisine();
                    strDietary = getSessionManager().getHallFilterDietary();
                }

                searchPresenter.searchDish(
                        getSessionManager().getUserId(),
                        hallId,
                        baseCategoryId,
                        subCategoryId,
                        strSearch,
                        strDietary,
                        strCuisines,
                        strSortMethod,
                        String.valueOf(pageNo),
                        perPageCount
                );

            } else {

                searchPresenter.searchDish(
                        getSessionManager().getUserId(),
                        hallId,
                        baseCategoryId,
                        subCategoryId,
                        strSearch,
                        "",
                        "",
                        "",
                        String.valueOf(pageNo),
                        perPageCount
                );

            }

        } else {
            view_nointernet.setVisibility(View.VISIBLE);
        }

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


    // TODO Interface Methods

    @Override
    public void onSuccess(String message, ArrayList<SearchResponse.Dishes> newDishList) {

        swipyrefreshlayout.setRefreshing(false);

        img_actionbar_search.setVisibility(View.VISIBLE);
        progress_search.setVisibility(View.GONE);

        dishList.addAll(newDishList);

        searchAdapter.updateList(dishList, showHallName);

        if (dishList.size() == 0) {
            recycler_dishes.setVisibility(View.GONE);
            view_nodata.setVisibility(View.VISIBLE);
        } else {
            recycler_dishes.setVisibility(View.VISIBLE);
            view_nodata.setVisibility(View.GONE);
        }

        if (newDishList.size() < Integer.parseInt(perPageCount)) {
            hasMoreData = false;
        } else {
            hasMoreData = true;
        }

    }

    @Override
    public void onClick_Dish(String dishId) {

        startActivity(new Intent(context, DishDetailsActivity.class).putExtra("dishId", dishId));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }

    @Override
    public void onClick_Fav(int position, String dishId, String isFavourite) {

        if (getSessionManager().isUserSkippedLoggedIn()) {
            startActivity(new Intent(context, SkipLoginActivity.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        } else {

            if (isInternetAvailable()) {
                dishList.get(position).setFavorited(isFavourite);
                searchAdapter.updateList(dishList, showHallName);

                Intent intent = new Intent("reloadOnlyDishesData");
                intent.putExtra("dishId", dishId);
                intent.putExtra("isFavourite", isFavourite);
                context.sendBroadcast(intent);

                addRemoveFavouritePresenter.addRemoveFavourite(getSessionManager().getUserId(), dishId);
            } else {
                showToast(getString(R.string.api_error_internet));
            }

        }

    }

    @Override
    public void onSuccess_Favourite(String message) {
        // Do Nothing
    }

    @Override
    public void onError(String message) {
        img_actionbar_search.setVisibility(View.VISIBLE);
        progress_search.setVisibility(View.GONE);
        showToast(message);
    }

    @Override
    public void openDish_UnavailableDish(String dishId) {

        startActivity(new Intent(context, DishDetailsActivity.class).putExtra("dishId", dishId));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }
}
