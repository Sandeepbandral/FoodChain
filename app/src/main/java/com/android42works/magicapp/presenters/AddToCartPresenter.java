package com.android42works.magicapp.presenters;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.AddToCartInterface;
import com.android42works.magicapp.responses.AddToCartResponse;
import com.android42works.magicapp.responses.SuccessResponse;
import com.android42works.magicapp.retrofit.APIInterface;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class AddToCartPresenter extends BasePresenter<AddToCartInterface> {

    private APIInterface apiInterface;

    public AddToCartPresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void addToCart(String userId, String hallId, String mealId, String items, String override){

        getCompositeDisposable().add(
            apiInterface
            .addToCart(userId, hallId, mealId, items, override)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<AddToCartResponse>() {

                    @Override
                    public void onSuccess(AddToCartResponse response) {
                        getView().onSuccess_AddToCart(
                                response.getMessage(),
                                response.getData().getCart_count(),
                                response.getData().getCart_total()
                        );
                    }

                    @Override
                    public void onError(Throwable throwable) {

                        // for checking different hall

                        boolean isDishFromDifferentHall = false, isDishFromDifferentMeal = false;

                        String HallName = "other hall.", mealName = "";

                        if (throwable instanceof HttpException) {
                            try {
                                HttpException exception = (HttpException) throwable;
                                JSONObject jObjError = new JSONObject(exception.response().errorBody().string());
                                JSONObject dataObject = jObjError.getJSONObject("data");
                                JSONObject dataObject2 = dataObject.getJSONObject("data");
                                HallName = dataObject2.getString("restaurant_name");
                                String diff = dataObject.getString("diff");
                                if(diff.equalsIgnoreCase("hall")){
                                    isDishFromDifferentHall = true;
                                }

                            }catch (Exception e){

                            }
                        }

                        getView().onError_AddToCart(getErrorMessage(throwable), isDishFromDifferentHall, isDishFromDifferentMeal, HallName, mealName);
                    }

                }
            )
        );

    }

}
