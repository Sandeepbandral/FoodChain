package com.android42works.magicapp.presenters;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.HallMenuInterface;
import com.android42works.magicapp.interfaces.SuccessInterface;
import com.android42works.magicapp.responses.ForgotPassResponse;
import com.android42works.magicapp.responses.HallMenuResponse;
import com.android42works.magicapp.retrofit.APIInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class HallMenuPresenter extends BasePresenter<HallMenuInterface> {

    private APIInterface apiInterface;

    public HallMenuPresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void getHallMenuData(String userId, String hallId, String dietary, String cuisine, String sortMentod, String pageNo, String perPageCount){

        getCompositeDisposable().add(
            apiInterface
            .getHallMenu(userId, hallId, "", "", dietary, cuisine, sortMentod, pageNo, perPageCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<HallMenuResponse>() {

                    @Override
                    public void onSuccess(HallMenuResponse hallMenuResponse) {
                        getView().onSuccess(hallMenuResponse.getMessage(), hallMenuResponse);
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
