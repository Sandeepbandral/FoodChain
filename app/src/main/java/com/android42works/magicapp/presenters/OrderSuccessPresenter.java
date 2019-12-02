package com.android42works.magicapp.presenters;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.OrderSuccessInterface;
import com.android42works.magicapp.interfaces.SuccessInterface;
import com.android42works.magicapp.responses.OrderSuccessResponse;
import com.android42works.magicapp.responses.SuccessResponse;
import com.android42works.magicapp.retrofit.APIInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class OrderSuccessPresenter extends BasePresenter<OrderSuccessInterface> {

    private APIInterface apiInterface;

    public OrderSuccessPresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void getStatus(String orderId){

        getCompositeDisposable().add(
            apiInterface
            .getOrderStatus(orderId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<OrderSuccessResponse>() {

                    @Override
                    public void onSuccess(OrderSuccessResponse response) {
                        getView().onSuccess(response.getMessage(), response);
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
