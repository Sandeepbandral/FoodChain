package com.android42works.magicapp.presenters;

import android.util.Log;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.HomeInterface;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.android42works.magicapp.responses.NotificationCountResponse;
import com.android42works.magicapp.responses.SearchHomeDishesResponse;
import com.android42works.magicapp.responses.SuccessResponse;
import com.android42works.magicapp.responses.UserDetailsResponse;
import com.android42works.magicapp.retrofit.APIInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter extends BasePresenter<HomeInterface> {

    private APIInterface apiInterface;

    public HomePresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void getUserDetails(String userId) {

        getCompositeDisposable().add(
                apiInterface
                        .getUserDetails(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableSingleObserver<UserDetailsResponse>() {

                                    @Override
                                    public void onSuccess(UserDetailsResponse userDetailsResponse) {
                                        getView().onSuccess_GetDetails(userDetailsResponse.getMessage(), userDetailsResponse);
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        getView().onError(getErrorMessage(throwable));
                                    }

                                }
                        )
        );

    }

    public void getHall1Dishes(String userId, String hallId, String timezone, String dietaryList, String cuisineList, String sortMethod) {

        getCompositeDisposable().add(
                apiInterface
                        .getAllHallDishes(userId, hallId, timezone, "", dietaryList, cuisineList, sortMethod)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableSingleObserver<HomeDishesResponse>() {

                                    @Override
                                    public void onSuccess(HomeDishesResponse homeDishesResponse) {
                                        getView().onSuccess_AllHallMenu(homeDishesResponse.getMessage(), homeDishesResponse);
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        getView().onError(getErrorMessage(throwable));
                                    }

                                }
                        )
        );

    }

    public void getSearchHall1Dishes(String userId, String hallId, String baseCategoryId, String subCategoryId, String dietaryList, String cuisineList, String sortMethod) {

        getCompositeDisposable().add(
                apiInterface
                        .searchHomeDishes(userId, hallId, baseCategoryId, subCategoryId, "", dietaryList, cuisineList, sortMethod, "", "")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableSingleObserver<SearchHomeDishesResponse>() {

                                    @Override
                                    public void onSuccess(SearchHomeDishesResponse searchHomeDishesResponse) {
                                        getView().onSuccess_SearchHomeDishes(searchHomeDishesResponse);
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        getView().onError(getErrorMessage(throwable));
                                    }

                                }
                        )
        );

    }

    public void saveLocalcartToServer(String userId, String hallId, String mealId, String items) {

        getCompositeDisposable().add(
                apiInterface
                        .saveLocalcart(userId, hallId, mealId, items, "1")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableSingleObserver<SuccessResponse>() {

                                    @Override
                                    public void onSuccess(SuccessResponse successResponse) {
                                        getView().onSuccess_LocalCartSave(successResponse.getMessage());
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        getView().onError(getErrorMessage(throwable));
                                    }

                                }
                        )
        );
    }

    public void getNotiCount(String userId) {

        getCompositeDisposable().add(
                apiInterface
                        .notiCount(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableSingleObserver<NotificationCountResponse>() {

                                    @Override
                                    public void onSuccess(NotificationCountResponse successResponse) {

                                        Log.e("onSuccess11>>","onSuccess11>>");

                                        getView().onSuccess_NotiCount(successResponse.getData().getNotification_count());
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {

                                        Log.e("onSuccess22>>","onSuccess22>>"+throwable.toString());

                                        getView().onError(getErrorMessage(throwable));
                                    }

                                }
                        )
        );
    }

}
