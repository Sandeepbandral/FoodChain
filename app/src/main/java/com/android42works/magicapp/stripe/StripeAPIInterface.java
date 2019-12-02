package com.android42works.magicapp.stripe;

import com.android42works.magicapp.responses.NutritionalResponse;
import com.android42works.magicapp.responses.StripeDeleteCardResponse;
import com.android42works.magicapp.responses.StripeNewCardSavedResponse;
import com.android42works.magicapp.responses.StripeNewCustomerResponse;
import com.android42works.magicapp.responses.StripeNewTokenResponse;
import com.android42works.magicapp.responses.StripeSavedCardsListResponse;

import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface StripeAPIInterface {

    @FormUrlEncoded
    @POST("customers")
    Single<StripeNewCustomerResponse> createCustomer(
            @Field("description") String email,
            @Field("source") String source
    );

    @FormUrlEncoded
    @POST("tokens")
    Single<StripeNewTokenResponse> createCardToken(
            @Field("card[number]") String cardNumber,
            @Field("card[exp_month]") String cardMonth,
            @Field("card[exp_year]") String cardYear,
            @Field("card[cvc]") String cardCVC
    );

    @FormUrlEncoded
    @POST("customers/{Id}/sources")
    Single<StripeNewCardSavedResponse> saveNewCard(
            @Path("Id") String stripeCustomerId,
            @Field("source") String cardToken
    );

    @GET("customers/{Id}/sources?object=card&limit=5")
    Single<StripeSavedCardsListResponse> getSavedCards(
            @Path("Id") String stripeCustomerId
    );

    @DELETE("customers/{Id}/sources/{CardId}")
    Single<StripeDeleteCardResponse> deleteCard(
            @Path("Id") String stripeCustomerId,
            @Path("CardId") String cardId
    );


}