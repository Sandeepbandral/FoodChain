package com.android42works.magicapp.presenters;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.SuccessInterface;
import com.android42works.magicapp.responses.ForgotPassResponse;
import com.android42works.magicapp.retrofit.APIInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ForgotPassPresenter extends BasePresenter<SuccessInterface> {

    private APIInterface apiInterface;

    public ForgotPassPresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void forgotPass(String email){

        getCompositeDisposable().add(
            apiInterface
            .forgotPass(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<ForgotPassResponse>() {

                    @Override
                    public void onSuccess(ForgotPassResponse forgotPassResponse) {
                        getView().onSuccess(forgotPassResponse.getMessage());
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
