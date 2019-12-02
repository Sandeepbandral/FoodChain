package com.android42works.magicapp.presenters;

import android.util.Log;

import com.android42works.magicapp.base.BasePresenter;
import com.android42works.magicapp.interfaces.SearchInterface;
import com.android42works.magicapp.responses.SearchResponse;
import com.android42works.magicapp.retrofit.APIInterface;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class  SearchPresenter extends BasePresenter<SearchInterface> {

    private APIInterface apiInterface;

    public SearchPresenter(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void searchDish(String userId, String hallId, String baseCategoryId, String subCategoryId, String query, String dietary,
                           String cuisine, String sort, String pageNo, String perPageCount){

        getCompositeDisposable().add(
            apiInterface
             .searchDish(userId, hallId, baseCategoryId, subCategoryId, query, dietary, cuisine, sort, pageNo, perPageCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                new DisposableSingleObserver<SearchResponse>() {

                    @Override
                    public void onSuccess(SearchResponse searchResponse) {

                        ArrayList<SearchResponse.Dishes> dishList = new ArrayList<>();

                        if(null!=searchResponse.getData()){

                            if(null!=searchResponse.getData().getDishes()){

                                SearchResponse.Dishes[] dishes = searchResponse.getData().getDishes();

                                for(int i=0; i<dishes.length; i++){
                                    dishList.add(dishes[i]);
                                }

                            }

                        }

                        getView().onSuccess(searchResponse.getMessage(), dishList);

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
