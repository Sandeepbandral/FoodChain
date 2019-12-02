package com.android42works.magicapp.presenters;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.LoginAndRegisterInterface;
import com.android42works.magicapp.responses.UserDetailsResponse;
import com.android42works.magicapp.retrofit.APIInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginOrRegisterPresenter extends BasePresenter<LoginAndRegisterInterface> {

    private APIInterface apiInterface;

    public LoginOrRegisterPresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void loginUser(String email, String password, String deviceId, String deviceToken){

        getCompositeDisposable().add(
            apiInterface
            .loginUser(email, password, deviceId, "Android", deviceToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<UserDetailsResponse>() {

                    @Override
                    public void onSuccess(UserDetailsResponse userDetailsResponse) {
                        if(userDetailsResponse.getData()!=null) {
                            getView().onSuccess_LoginOrRegister(userDetailsResponse.getMessage(), userDetailsResponse, userDetailsResponse.getData().getName(), false);
                        }else {
                            getView().onError("API Response Error.");
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


    public void registerUser(String email, String studentId, String stripeId, String password, String confirmPassword,
                         String deviceId, String deviceToken){

        getCompositeDisposable().add(
            apiInterface
            .registerUser(email, studentId, stripeId, password, confirmPassword, deviceId, "Android", deviceToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<UserDetailsResponse>() {

                    @Override
                    public void onSuccess(UserDetailsResponse userDetailsResponse) {
                        if(userDetailsResponse.getData()!=null) {
                            getView().onSuccess_LoginOrRegister(userDetailsResponse.getMessage(), userDetailsResponse, userDetailsResponse.getData().getName(), false);
                        }else {
                            getView().onError("API Response Error.");
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

    public void registerSocialUser(String name, String email, String facebookId, String deviceId, String deviceToken, String deviceType, String profilePic){

        getCompositeDisposable().add(apiInterface
                        .registerFacebookUser(name, email, deviceId, deviceType, deviceToken, facebookId, profilePic)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableSingleObserver<UserDetailsResponse>() {
                                    @Override
                                    public void onSuccess(UserDetailsResponse userDetailsResponse) {
                                        if(userDetailsResponse.getData()!=null) {
                                            getView().onSuccess_LoginOrRegister(userDetailsResponse.getMessage(), userDetailsResponse, userDetailsResponse.getData().getName(), true);
                                        }else {
                                            getView().onError("API Response Error.");
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
