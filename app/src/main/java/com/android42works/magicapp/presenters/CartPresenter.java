package com.android42works.magicapp.presenters;

import android.util.Log;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.cart.CartResponse;
import com.android42works.magicapp.interfaces.CartInterface;
import com.android42works.magicapp.responses.CouponResponse;
import com.android42works.magicapp.responses.LocalCartTaxResponse;
import com.android42works.magicapp.responses.PlaceOrderResponse;
import com.android42works.magicapp.responses.TimingsResponse;
import com.android42works.magicapp.retrofit.APIInterface;
import com.google.gson.JsonArray;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CartPresenter extends BasePresenter<CartInterface> {

    private APIInterface apiInterface;

    public CartPresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void getCartItems(String userId) {

        getCompositeDisposable().add(
                apiInterface
                .getCart(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableSingleObserver<CartResponse>() {

                            @Override
                            public void onSuccess(CartResponse cartResponse) {
                                getView().onSuccess_CartItems(cartResponse.getMessage(), cartResponse);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                getView().onError(getErrorMessage(throwable));
                            }

                        }
                )
        );

    }

    public void getLatestTimings(String timezone) {

        getCompositeDisposable().add(
                apiInterface
                .getLatestTimings(timezone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableSingleObserver<TimingsResponse>() {

                            @Override
                            public void onSuccess(TimingsResponse response) {
                                getView().onSuccess_Timings(response.getMessage(), response);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                getView().onError(getErrorMessage(throwable));
                            }

                        }
                )
        );

    }

    public void removeFromCart(String userId, String dishIds) {

        JsonArray itemsArray = new JsonArray();

        String[] dishArray = dishIds.split(",");
        for(int i=0; i<dishArray.length; i++){
            itemsArray.add(dishArray[i]);
        }

        getCompositeDisposable().add(
                apiInterface
                        .removeFromCart(userId, itemsArray.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableSingleObserver<CartResponse>() {

                                    @Override
                                    public void onSuccess(CartResponse cartResponse) {
                                        getView().onSuccess_CartItems(cartResponse.getMessage(), cartResponse);
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        getView().onError(getErrorMessage(throwable));
                                    }

                                }
                        )
        );

    }

    public void updateCartDishQuantity(String userId, JsonArray itemsArray) {

        getCompositeDisposable().add(
                apiInterface
                        .updateCartDishQuantity(userId, itemsArray.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableSingleObserver<CartResponse>() {

                                    @Override
                                    public void onSuccess(CartResponse cartResponse) {
                                        getView().onSuccess_CartItems(cartResponse.getMessage(), cartResponse);
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        getView().onError(getErrorMessage(throwable));
                                    }

                                }
                        )
        );

    }

    public void applyCoupon(String userId, String hallId, String couponCode) {

        getCompositeDisposable().add(
                apiInterface
                        .applyCoupon(userId, hallId, couponCode)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableSingleObserver<CouponResponse>() {

                                    @Override
                                    public void onSuccess(CouponResponse couponResponse) {
                                        getView().onSuccess_ApplyCoupon(couponResponse.getMessage(), couponResponse);
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        getView().onError_applyCoupon(getErrorMessage(throwable));
                                    }

                                }
                        )
        );

    }

    public void removeCoupon(String userId) {

        getCompositeDisposable().add(
                apiInterface
                        .removeCoupon(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableSingleObserver<CouponResponse>() {

                                    @Override
                                    public void onSuccess(CouponResponse couponResponse) {
                                        getView().onSuccess_RemoveCoupon(couponResponse.getMessage(), couponResponse);
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        getView().onError(getErrorMessage(throwable));
                                    }

                                }
                        )
        );

    }

    public void placeOrder(String userId, String hallId, String instructions, String paymentMethod,
                           String paymentDetails, String pickUp) {

        Log.e("pickUp???", "pickUp???" + pickUp);

        getCompositeDisposable().add(
                apiInterface
                        .placeOrder(userId, "", hallId, instructions, paymentMethod, paymentDetails, pickUp)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableSingleObserver<PlaceOrderResponse>() {

                                    @Override
                                    public void onSuccess(PlaceOrderResponse placeOrderResponse) {
                                        getView().onSuccess_PlaceOrder(placeOrderResponse.getMessage(), placeOrderResponse.getData().getOrder_id());
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        getView().onError_placeOrder(getErrorMessage(throwable));
                                    }

                                }
                        )
        );

    }

    public void getLocalCartItemsTax(String localCartHallId, String totalItemsCost, String couponApplied) {

        getCompositeDisposable().add(
                apiInterface
                        .getLocalCartItemsTax(localCartHallId, totalItemsCost, couponApplied)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableSingleObserver<LocalCartTaxResponse>() {

                                    @Override
                                    public void onSuccess(LocalCartTaxResponse cartResponse) {
                                        getView().onSuccess_LocalCartTax(cartResponse.getMessage(), cartResponse);
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
