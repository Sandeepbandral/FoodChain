package com.android42works.magicapp.presenters;

import android.util.Log;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.NotificationsInterface;
import com.android42works.magicapp.responses.Notifications;
import com.android42works.magicapp.responses.NotificationsResponse;
import com.android42works.magicapp.responses.SuccessResponse;
import com.android42works.magicapp.retrofit.APIInterface;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class NotificationsPresenter extends BasePresenter<NotificationsInterface> {

    private APIInterface apiInterface;

    public NotificationsPresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void getNotifications(String userId, String pageNo, String perPageCount){

        Log.e("userId:::","::"+userId);

        getCompositeDisposable().add(
            apiInterface
            .getNotifications(userId, userId, pageNo, perPageCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<NotificationsResponse>() {

                    @Override
                    public void onSuccess(NotificationsResponse notificationsResponse) {

                        ArrayList<Notifications> notificationsList = new ArrayList<>();

                        if(null!=notificationsResponse.getData()){

                            if(null!=notificationsResponse.getData().getNotifications()){

                                Notifications[] notifications = notificationsResponse.getData().getNotifications();

                                for(int i=0; i<notifications.length; i++){
                                    notificationsList.add(notifications[i]);
                                }

                            }

                        }

                        getView().onSuccess(notificationsResponse.getMessage(), notificationsList);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getView().onError(getErrorMessage(throwable));
                    }

                }
            )
        );

    }

    public void hideAllReadNotifications(String userId){

        getCompositeDisposable().add(
            apiInterface
            .hideAllReadNotifications(userId, "0")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<SuccessResponse>() {

                    @Override
                    public void onSuccess(SuccessResponse response) {
                        getView().onSuccess_HideAll(response.getMessage());
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
