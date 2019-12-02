package com.android42works.magicapp.presenters;

import android.util.Log;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.AddNewCardInterface;
import com.android42works.magicapp.interfaces.SavedCardsInterface;
import com.android42works.magicapp.responses.StripeNewCardSavedResponse;
import com.android42works.magicapp.responses.StripeNewCustomerResponse;
import com.android42works.magicapp.responses.StripeNewTokenResponse;
import com.android42works.magicapp.stripe.StripeAPIInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class AddCardPresenter extends BasePresenter<AddNewCardInterface> {

    private StripeAPIInterface stripeApiInterface;

    public AddCardPresenter(StripeAPIInterface stripeApiInterface) {
        this.stripeApiInterface = stripeApiInterface;
    }

    public void createCustomer(String email){

        getCompositeDisposable().add(
            stripeApiInterface
            .createCustomer("Customer for " + email, "tok_visa")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<StripeNewCustomerResponse>() {

                    @Override
                    public void onSuccess(StripeNewCustomerResponse response) {
                        getView().onSuccess_StripeID(response.getId());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getView().onError(getErrorMessage(throwable));
                        Log.e("createCustomer", throwable.getMessage());
                    }

                }
            )
        );

    }

    public void createCardToken(String cardNumber, String cardMonth, String cardYear, String cardCVC){

        getCompositeDisposable().add(
            stripeApiInterface
            .createCardToken(cardNumber, cardMonth, cardYear, cardCVC)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<StripeNewTokenResponse>() {

                    @Override
                    public void onSuccess(StripeNewTokenResponse response) {
                        getView().onSuccess_TokenID(response.getId());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getView().onError(getErrorMessage(throwable));
                        Log.e("createCardToken", throwable.getMessage());
                    }

                }
            )
        );

    }

    public void saveCard(String stripeCustomerId, String tokenId){

        getCompositeDisposable().add(
            stripeApiInterface
            .saveNewCard(stripeCustomerId, tokenId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<StripeNewCardSavedResponse>() {

                    @Override
                    public void onSuccess(StripeNewCardSavedResponse response) {
                        getView().onSuccess_CardSave(response.getId());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getView().onError(getErrorMessage(throwable));
                        Log.e("saveCard", throwable.getMessage());
                    }

                }
            )
        );

    }

}
