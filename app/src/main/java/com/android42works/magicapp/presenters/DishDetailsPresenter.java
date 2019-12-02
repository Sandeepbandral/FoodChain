package com.android42works.magicapp.presenters;

import android.util.Log;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.DishDetailsInterface;
import com.android42works.magicapp.interfaces.FavouriteInterface;
import com.android42works.magicapp.responses.DishDetailsResponse;
import com.android42works.magicapp.responses.FavouriteResponse;
import com.android42works.magicapp.responses.NewNutritionalResponse;
import com.android42works.magicapp.responses.NutritionalResponse;
import com.android42works.magicapp.responses.SuccessResponse;
import com.android42works.magicapp.retrofit.APIInterface;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class DishDetailsPresenter extends BasePresenter<DishDetailsInterface> {

    private APIInterface apiInterface;

    public DishDetailsPresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void getDishDetails(String userId, String dishId){

        Log.e("userId::"+userId,"::dishId::"+dishId);

        getCompositeDisposable().add(
            apiInterface
            .getDishDetails(dishId, userId, dishId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<DishDetailsResponse>() {

                    @Override
                    public void onSuccess(DishDetailsResponse dishDetailsResponse) {
                        getView().onSuccess(dishDetailsResponse.getMessage(), dishDetailsResponse);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getView().onError(getErrorMessage(throwable));
                    }

                }
            )
        );

    }

    public void getDishNutritions(String dishId)
    {
        getCompositeDisposable().add(
                apiInterface
                        .getDishNutritions(dishId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableSingleObserver<NutritionalResponse>() {

                                    @Override
                                    public void onSuccess(NutritionalResponse nutritionsResponse) {
                                        getView().onSuccessDishNutritions(nutritionsResponse, null);
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        getView().onError(getErrorMessage(throwable));
                                    }

                                }
                        )
        );
    }

    public void getNewDishNutritions(String dishId)
    {
        getCompositeDisposable().add(
                apiInterface
                        .getNewDishNutritions(dishId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableSingleObserver<NewNutritionalResponse>() {

                                    @Override
                                    public void onSuccess(NewNutritionalResponse newNutritionalResponse) {
                                        getView().onSuccessDishNutritions(null, newNutritionalResponse);
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
