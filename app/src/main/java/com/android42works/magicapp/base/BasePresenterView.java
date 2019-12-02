package com.android42works.magicapp.base;

public interface BasePresenterView<T extends MvpView> {

    void attachView(T view);
    void detachView();

}
