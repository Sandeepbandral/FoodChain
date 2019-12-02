package com.android42works.magicapp.presenters;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.LogoutInterface;
import com.android42works.magicapp.responses.SuccessResponse;
import com.android42works.magicapp.retrofit.APIInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LogoutPresenter extends BasePresenter<LogoutInterface> {

    private APIInterface apiInterface;

    public LogoutPresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void logoutUser(String userId, String deviceId){

        getCompositeDisposable().add(
            apiInterface
            .logoutUser(userId, deviceId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<SuccessResponse>() {

                    @Override
                    public void onSuccess(SuccessResponse successResponse) {
                        getView().onLogoutSuccess(successResponse.getMessage());
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
