package com.android42works.magicapp.base;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONObject;

import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter<T extends MvpView> implements BasePresenterView<T> {

    private T mView;
    private CompositeDisposable compositeDisposable;

    /*
     * Attaching Interface to Base Presenter
     */
    @Override
    public void attachView(T view) {
        mView = view;
    }

    /*
     * Detaching Interface to Base Presenter
     */
    @Override
    public void detachView() {
        mView = null;
        disposeCompositeDisposable();
    }

    /*
     * Returns Interaface attached
     */
    public T getView(){
        return mView;
    }

    /*
     * Returns CompositeDisposable
     */
    public CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
    }

    /*
     * Dispose CompositeDisposable
     */
    private void disposeCompositeDisposable() {
        if (compositeDisposable != null){
            if (!compositeDisposable.isDisposed()) {
                compositeDisposable.dispose();
            }
        }
    }

    /* Checks for HttpException */
    public String getErrorMessage(Throwable throwable) {

        if (throwable instanceof HttpException) {
            try {
                HttpException exception = (HttpException) throwable;
                JSONObject jObjError = new JSONObject(exception.response().errorBody().string());
                return jObjError.getString("message");
            }catch (Exception e){
                return throwable.getMessage();
            }
        }else {
            return throwable.getMessage();
        }

    }

    /* CNo Internet Error Message */
    public String getInternetErrorMessage() {
        return "No internet connection. Please try again.";
    }


}
