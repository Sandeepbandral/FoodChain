package com.android42works.magicapp.presenters;

import android.util.Log;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.ProfileInterface;
import com.android42works.magicapp.responses.UserDetailsResponse;
import com.android42works.magicapp.retrofit.APIInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ProfilePresenter extends BasePresenter<ProfileInterface> {

    private APIInterface apiInterface;

    public ProfilePresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void updateProfile(String userId, String name, String phone, String studentId, String stripeId, String defaultCardId){

        getCompositeDisposable().add(
            apiInterface
            .updateProfile(userId, userId, name, phone, studentId, stripeId, defaultCardId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<UserDetailsResponse>() {

                    @Override
                    public void onSuccess(UserDetailsResponse userDetailsResponse) {
                        getView().onSuccess(userDetailsResponse.getMessage(), userDetailsResponse);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getView().onError(getErrorMessage(throwable));
                        Log.e("updateProfile", throwable.getMessage());
                    }

                }
            )
        );

    }

}
