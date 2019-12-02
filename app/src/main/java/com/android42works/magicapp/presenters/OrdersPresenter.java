package com.android42works.magicapp.presenters;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.OrderDetailsInterface;
import com.android42works.magicapp.interfaces.TermsInterface;
import com.android42works.magicapp.responses.OrdersResponse;
import com.android42works.magicapp.responses.TermsResponse;
import com.android42works.magicapp.retrofit.APIInterface;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class OrdersPresenter extends BasePresenter<OrderDetailsInterface> {

    private APIInterface apiInterface;

    public OrdersPresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void getOrders(String userId, String type, String pageNo, String perPageCount){

        getCompositeDisposable().add(
            apiInterface
            .myOrders(userId, type, pageNo, perPageCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<OrdersResponse>() {

                    @Override
                    public void onSuccess(OrdersResponse ordersResponse) {

                        ArrayList<OrdersResponse.Orders> ordersList = new ArrayList<>();

                        if(null!=ordersResponse.getData()){
                            if(null!=ordersResponse.getData().getOrders()){
                                OrdersResponse.Orders[] orders = ordersResponse.getData().getOrders();
                                for(int i=0; i<orders.length; i++){
                                    ordersList.add(orders[i]);
                                }
                            }

                        }

                        getView().onSuccess(ordersResponse.getMessage(), ordersList);
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
