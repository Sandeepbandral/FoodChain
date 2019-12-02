package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;
import com.android42works.magicapp.responses.Notifications;
import com.android42works.magicapp.responses.NotificationsResponse;

import java.util.ArrayList;

public interface NotificationsInterface extends MvpView{

    void onSuccess(String message, ArrayList<Notifications> notificationsList);
    void onSuccess_HideAll(String message);

}
