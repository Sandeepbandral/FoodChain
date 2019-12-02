package com.android42works.magicapp.presenters;


import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.OrderDetailInterface;
import com.android42works.magicapp.responses.OrderDetailResponse;
import com.android42works.magicapp.retrofit.APIInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by apple on 04/10/18.
 */

public class OrderDetailPresenter extends BasePresenter<OrderDetailInterface> {

    private APIInterface apiInterface;

    public OrderDetailPresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void fetchOrderDetail(String orderId) {

        getCompositeDisposable().add(
                apiInterface
                        .getOrderDetail(orderId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableSingleObserver<OrderDetailResponse>() {

                                    @Override
                                    public void onSuccess(OrderDetailResponse orderStatusResponse) {
                                        getView().onSuccess(orderStatusResponse.getSuccess(), orderStatusResponse);
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
