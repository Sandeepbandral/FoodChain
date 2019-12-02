package com.android42works.magicapp.presenters;

import android.util.Log;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.AddNewCardInterface;
import com.android42works.magicapp.interfaces.RateOrderInterface;
import com.android42works.magicapp.interfaces.SuccessInterface;
import com.android42works.magicapp.responses.StripeNewCardSavedResponse;
import com.android42works.magicapp.responses.StripeNewCustomerResponse;
import com.android42works.magicapp.responses.StripeNewTokenResponse;
import com.android42works.magicapp.responses.SuccessResponse;
import com.android42works.magicapp.retrofit.APIInterface;
import com.android42works.magicapp.stripe.StripeAPIInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class RatePresenter extends BasePresenter<RateOrderInterface> {

    private APIInterface apiInterface;

    public RatePresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void rateOrder(String orderId, String thumbVote, String area, String notes){

        getCompositeDisposable().add(
            apiInterface
            .rateOrder(orderId, thumbVote, area, notes)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<SuccessResponse>() {

                    @Override
                    public void onSuccess(SuccessResponse response) {
                        getView().onSuccess_RateOrder(response.getMessage());
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
