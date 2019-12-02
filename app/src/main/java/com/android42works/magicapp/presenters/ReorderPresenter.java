package com.android42works.magicapp.presenters;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.RateOrderInterface;
import com.android42works.magicapp.interfaces.SuccessInterface;
import com.android42works.magicapp.responses.SuccessResponse;
import com.android42works.magicapp.retrofit.APIInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ReorderPresenter extends BasePresenter<SuccessInterface> {

    private APIInterface apiInterface;

    public ReorderPresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void reorder(String orderId){

        getCompositeDisposable().add(
            apiInterface
            .reorder(orderId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<SuccessResponse>() {

                    @Override
                    public void onSuccess(SuccessResponse response) {
                        getView().onSuccess(response.getMessage());
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
