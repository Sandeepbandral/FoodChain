package com.android42works.magicapp.interfaces;

import com.android42works.magicapp.base.MvpView;

public interface TermsInterface extends MvpView{

    void onSuccess(String message, String title, String htmlData);

}
