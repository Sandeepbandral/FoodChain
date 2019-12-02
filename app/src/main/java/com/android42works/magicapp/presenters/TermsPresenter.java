package com.android42works.magicapp.presenters;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.SuccessInterface;
import com.android42works.magicapp.interfaces.TermsInterface;
import com.android42works.magicapp.responses.SuccessResponse;
import com.android42works.magicapp.responses.TermsResponse;
import com.android42works.magicapp.retrofit.APIInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class TermsPresenter extends BasePresenter<TermsInterface> {

    private APIInterface apiInterface;

    public TermsPresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void getPageHtml(String pageType){

        getCompositeDisposable().add(
            apiInterface
            .getInfoPage(pageType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<TermsResponse>() {

                    @Override
                    public void onSuccess(TermsResponse termsResponse) {

                        getView().onSuccess(
                                termsResponse.getMessage(),
                                termsResponse.getData().getPage().getTitle(),
                                termsResponse.getData().getPage().getHtml_data()
                                );
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
