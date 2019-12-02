package com.android42works.magicapp.presenters;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.AddRemoveFavouriteInterface;
import com.android42works.magicapp.interfaces.FavouriteInterface;
import com.android42works.magicapp.interfaces.SuccessInterface;
import com.android42works.magicapp.responses.FavouriteResponse;
import com.android42works.magicapp.responses.SuccessResponse;
import com.android42works.magicapp.retrofit.APIInterface;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class AddRemoveFavouritePresenter extends BasePresenter<AddRemoveFavouriteInterface> {

    private APIInterface apiInterface;

    public AddRemoveFavouritePresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void addRemoveFavourite(String userId, String dishId){

        getCompositeDisposable().add(
            apiInterface
            .addRemoveFavourite(userId, dishId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<SuccessResponse>() {

                    @Override
                    public void onSuccess(SuccessResponse successResponse) {
                        if(null!=successResponse) {
                            getView().onSuccess_Favourite(successResponse.getMessage());
                        }
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
