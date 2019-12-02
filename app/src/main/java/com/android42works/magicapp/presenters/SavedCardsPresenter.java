package com.android42works.magicapp.presenters;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.SavedCardsInterface;
import com.android42works.magicapp.responses.StripeDeleteCardResponse;
import com.android42works.magicapp.responses.StripeNewCustomerResponse;
import com.android42works.magicapp.responses.StripeSavedCardsListResponse;
import com.android42works.magicapp.stripe.StripeAPIInterface;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SavedCardsPresenter extends BasePresenter<SavedCardsInterface> {

    private StripeAPIInterface stripeApiInterface;

    public SavedCardsPresenter(StripeAPIInterface stripeApiInterface) {
        this.stripeApiInterface = stripeApiInterface;
    }

    public void getAllCards(String stripeCustomerId) {

        getCompositeDisposable().add(
                stripeApiInterface
                        .getSavedCards(stripeCustomerId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableSingleObserver<StripeSavedCardsListResponse>() {

                                    @Override
                                    public void onSuccess(StripeSavedCardsListResponse response) {

                                        ArrayList<StripeSavedCardsListResponse.Data> savedCardsList = new ArrayList<>();

                                        if (null != response.getData()) {

                                            StripeSavedCardsListResponse.Data[] data = response.getData();

                                            for (int i = 0; i < data.length; i++) {
                                                savedCardsList.add(data[i]);
                                            }
                                        }

                                        try {
                                            getView().onSuccess_CardsList(savedCardsList);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {

                                        try {
                                            getView().onError(getErrorMessage(throwable));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                        )
        );

    }

    public void deleteCard(String stripeCustomerId, String cardId) {

        getCompositeDisposable().add(
                stripeApiInterface
                        .deleteCard(stripeCustomerId, cardId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableSingleObserver<StripeDeleteCardResponse>() {

                                    @Override
                                    public void onSuccess(StripeDeleteCardResponse response) {
                                        getView().onSuccess_DeleteCard(response.getId());
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        getView().onError(getErrorMessage(throwable));
                                    }

                                }
                        )
        );

    }

}
