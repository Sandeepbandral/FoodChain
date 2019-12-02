package com.android42works.magicapp.presenters;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.FavouriteInterface;
import com.android42works.magicapp.interfaces.SuccessInterface;
import com.android42works.magicapp.responses.FavouriteResponse;
import com.android42works.magicapp.responses.ForgotPassResponse;
import com.android42works.magicapp.responses.SuccessResponse;
import com.android42works.magicapp.retrofit.APIInterface;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class FavouritePresenter extends BasePresenter<FavouriteInterface> {

    private APIInterface apiInterface;

    public FavouritePresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void getAllFavourites(String userId, String pageNo, String perPageCount){

        getCompositeDisposable().add(
            apiInterface
            .getFavourites(userId, pageNo, perPageCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<FavouriteResponse>() {

                    @Override
                    public void onSuccess(FavouriteResponse favouriteResponse) {

                        ArrayList<FavouriteResponse.Dishes> dishesList = new ArrayList<>();

                        if(favouriteResponse.getData()!=null){

                            if(favouriteResponse.getData().getDishes()!=null){

                                FavouriteResponse.Dishes[] dishesArray = favouriteResponse.getData().getDishes();

                                for(int i=0; i<dishesArray.length; i++){
                                    dishesList.add(dishesArray[i]);
                                }

                            }

                        }

                        getView().onSuccess_getList(favouriteResponse.getMessage(), dishesList);

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
