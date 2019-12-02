package com.android42works.magicapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.adapters.CardListingAdapter;
import com.android42works.magicapp.base.BaseActivity;
import com.android42works.magicapp.interfaces.CardListingInterface;
import com.android42works.magicapp.interfaces.ProfileInterface;
import com.android42works.magicapp.interfaces.SavedCardsInterface;
import com.android42works.magicapp.models.CardsListModel;
import com.android42works.magicapp.presenters.ProfilePresenter;
import com.android42works.magicapp.presenters.SavedCardsPresenter;
import com.android42works.magicapp.responses.StripeSavedCardsListResponse;
import com.android42works.magicapp.responses.UserDetailsResponse;
import com.android42works.magicapp.utils.AppUtils;

import java.util.ArrayList;

/* Created by JSP@nesar */

public class CardsListActivity extends BaseActivity implements SavedCardsInterface, CardListingInterface, ProfileInterface {

    private TextView txt_actionbar_title;
    private RecyclerView recycler_cards;
    private CardListingAdapter cardListingAdapter;
    private ArrayList<CardsListModel> savedCardsList = new ArrayList<>();
    private SavedCardsPresenter savedCardsPresenter;
    private ProfilePresenter profilePresenter;

    private String stripeCustomerId = "", defaultCardId = "", selectedCardId = "";
    private int selectedPosition = 0;

    @Override
    protected int getLayoutView() {
        return R.layout.act_cardlist;
    }

    @Override
    protected Context getActivityContext() {
        return CardsListActivity.this;
    }

    @Override
    protected void initView() {

        // TODO initView

        txt_actionbar_title = findViewById(R.id.txt_actionbar_title);
        recycler_cards = findViewById(R.id.recycler_cards);

    }

    @Override
    protected void initData() {

        // TODO initData

        context = CardsListActivity.this;

        txt_actionbar_title.setText("SAVED CARDS");

        selectedCardId = getIntent().getStringExtra("selectedCardId");

        stripeCustomerId = getSessionManager().getUserDetailsResponse().getData().getStripe_id();
        defaultCardId = getSessionManager().getUserDetailsResponse().getData().getDefault_card_id();

        if(AppUtils.isStringEmpty(selectedCardId)){
            selectedCardId = defaultCardId;
        }

        cardListingAdapter = new CardListingAdapter(context, this, savedCardsList, defaultCardId);
        recycler_cards.setLayoutManager(new LinearLayoutManager(context));
        recycler_cards.setAdapter(cardListingAdapter);

        savedCardsPresenter = new SavedCardsPresenter(getStripeAPIInterface());
        savedCardsPresenter.attachView(this);

        profilePresenter = new ProfilePresenter(getAPIInterface());
        profilePresenter.attachView(this);

    }

    @Override
    protected void initListener() {

        // TODO initListener

        findViewById(R.id.img_actionbar_backarrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        findViewById(R.id.btn_addnew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(savedCardsList.size()<5) {
                    startActivity(new Intent(context, AddNewCardActivity.class));
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }else {
                    showToast(getString(R.string.max_cards));
                }
            }
        });

    }

    // TODO Activity Methods


    @Override
    protected void onPostResume() {
        super.onPostResume();

        if(isInternetAvailable()) {
            showProgressDialog();
            savedCardsPresenter.getAllCards(stripeCustomerId);
        }else {
            showToast(getString(R.string.api_error_internet));
        }

    }

    @Override
    public void onBackPressed() {
        finish(); overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    protected void onDestroy() {
        try{ savedCardsPresenter.detachView(); } catch (Exception e) {}
        try{ profilePresenter.detachView(); } catch (Exception e){}
        super.onDestroy();
    }

    // TODO Interface Methods

    @Override
    public void onSuccess_CardsList(ArrayList<StripeSavedCardsListResponse.Data> savedCardsListResponse) {

        hideProgressDialog();

        savedCardsList = new ArrayList<>();

        for(int i=0; i<savedCardsListResponse.size(); i++){
            String id = savedCardsListResponse.get(i).getId();
            if(id.equalsIgnoreCase(selectedCardId)){
                savedCardsList.add(new CardsListModel(true, savedCardsListResponse.get(i)));
            }else {
                savedCardsList.add(new CardsListModel(false, savedCardsListResponse.get(i)));
            }
        }

        cardListingAdapter.updateList(savedCardsList, defaultCardId);

    }

    @Override
    public void onClick_SaveAsDefault(int position) {

        if(isInternetAvailable()) {

            showProgressDialog();
            profilePresenter.updateProfile(
                    getSessionManager().getUserId(),
                    getSessionManager().getUserDetailsResponse().getData().getName(),
                    getSessionManager().getUserDetailsResponse().getData().getPhone(),
                    getSessionManager().getUserDetailsResponse().getData().getStudent_id(),
                    getSessionManager().getUserDetailsResponse().getData().getStripe_id(),
                    savedCardsList.get(position).getData().getId()
            );

        }else {
            showToast(getString(R.string.api_error_internet));
        }


    }

    @Override
    public void onClick_SelectCard(int position) {

        selectedCardId = savedCardsList.get(position).getData().getId();

        resetCardsListSelection();

    }

    @Override
    public void onClick_DeleteCard(int position) {

        if(isInternetAvailable()) {

            selectedPosition = position;
            showProgressDialog();
            savedCardsPresenter.deleteCard(
                    getSessionManager().getUserDetailsResponse().getData().getStripe_id(),
                    savedCardsList.get(position).getData().getId()
            );

        }else {
            showToast(getString(R.string.api_error_internet));
        }

    }

    @Override
    public void onSuccess(String message, UserDetailsResponse userDetailsResponse) {

        hideProgressDialog();
        defaultCardId = userDetailsResponse.getData().getDefault_card_id();
        getSessionManager().setUserDetailsResponse(userDetailsResponse);
        cardListingAdapter.updateList(savedCardsList, defaultCardId);

        Intent intent = new Intent("reloadSavedCards");
        sendBroadcast(intent);

    }

    @Override
    public void onSuccess_DeleteCard(String cardId) {

        Intent intent = new Intent("reloadSavedCards");
        sendBroadcast(intent);

        boolean isDeletedCardIsDefaultCard = false, isDeletedCardIsSelectedCard = false;

        if(savedCardsList.get(selectedPosition).getData().getId().equals(defaultCardId)){
            isDeletedCardIsDefaultCard = true;
        }
        if(savedCardsList.get(selectedPosition).getData().getId().equals(selectedCardId)){
            isDeletedCardIsSelectedCard = true;
        }

        savedCardsList.remove(selectedPosition);
        cardListingAdapter.updateList(savedCardsList, defaultCardId);

        if(savedCardsList.size()>0) {

            if(isDeletedCardIsSelectedCard==true) {

                if(isDeletedCardIsDefaultCard==true) {
                    selectedCardId = savedCardsList.get(0).getData().getId();
                }else {
                    selectedCardId = defaultCardId;
                }

                resetCardsListSelection();

            }

            if(isDeletedCardIsDefaultCard==true) {

                if (isInternetAvailable()) {

                    showProgressDialog();
                    profilePresenter.updateProfile(
                            getSessionManager().getUserId(),
                            getSessionManager().getUserDetailsResponse().getData().getName(),
                            getSessionManager().getUserDetailsResponse().getData().getPhone(),
                            getSessionManager().getUserDetailsResponse().getData().getStudent_id(),
                            getSessionManager().getUserDetailsResponse().getData().getStripe_id(),
                            savedCardsList.get(0).getData().getId()
                    );

                } else {
                    showToast(getString(R.string.api_error_internet));
                }

            }

        }else {
            CartActivity.getInstance().showNewSelectedCard("");
        }

        hideProgressDialog();

    }

    @Override
    public void onError(String message) {
        hideProgressDialog();
        showToast(message);
    }


    private void resetCardsListSelection(){

        for(int i=0; i<savedCardsList.size(); i++){
            String id = savedCardsList.get(i).getData().getId();
            if(id.equalsIgnoreCase(selectedCardId)){
                savedCardsList.get(i).setSelected(true);
            }else {
                savedCardsList.get(i).setSelected(false);
            }
        }

        cardListingAdapter.updateList(savedCardsList, defaultCardId);
        CartActivity.getInstance().showNewSelectedCard(selectedCardId);

    }

}