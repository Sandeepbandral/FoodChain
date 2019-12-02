package com.android42works.magicapp.presenters;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.SuccessInterface;
import com.android42works.magicapp.responses.SuccessResponse;
import com.android42works.magicapp.retrofit.APIInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ChangePassPresenter extends BasePresenter<SuccessInterface> {

    private APIInterface apiInterface;

    public ChangePassPresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void changePass(String userId, String currentPass, String newPass){

        getCompositeDisposable().add(
            apiInterface
            .changePass(userId, currentPass, newPass, newPass)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<SuccessResponse>() {

                    @Override
                    public void onSuccess(SuccessResponse successResponse) {
                        getView().onSuccess(successResponse.getMessage());
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
