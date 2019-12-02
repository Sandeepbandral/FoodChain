package com.android42works.magicapp.retrofit;

import com.android42works.magicapp.cart.CartResponse;
import com.android42works.magicapp.responses.AddToCartResponse;
import com.android42works.magicapp.responses.CouponResponse;
import com.android42works.magicapp.responses.DishDetailsResponse;
import com.android42works.magicapp.responses.FavouriteResponse;
import com.android42works.magicapp.responses.ForgotPassResponse;
import com.android42works.magicapp.responses.HallMenuResponse;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.android42works.magicapp.responses.LocalCartTaxResponse;
import com.android42works.magicapp.responses.NewNutritionalResponse;
import com.android42works.magicapp.responses.NotificationCountResponse;
import com.android42works.magicapp.responses.NotificationsResponse;
import com.android42works.magicapp.responses.NutritionalResponse;
import com.android42works.magicapp.responses.OrderDetailResponse;
import com.android42works.magicapp.responses.OrderSuccessResponse;
import com.android42works.magicapp.responses.OrdersResponse;
import com.android42works.magicapp.responses.PlaceOrderResponse;
import com.android42works.magicapp.responses.SearchHomeDishesResponse;
import com.android42works.magicapp.responses.SearchResponse;
import com.android42works.magicapp.responses.SuccessResponse;
import com.android42works.magicapp.responses.TermsResponse;
import com.android42works.magicapp.responses.TimingsResponse;
import com.android42works.magicapp.responses.UserDetailsResponse;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @FormUrlEncoded
    @POST("user/login")
    Single<UserDetailsResponse> loginUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("device_id") String deviceId,
            @Field("device_type") String deviceType,
            @Field("device_token") String deviceToken
    );

    @FormUrlEncoded
    @POST("user/changePassword")
    Single<SuccessResponse> changePass(
            @Field("id") String userId,
            @Field("oldpassord") String currentPass,
            @Field("password") String newPass,
            @Field("password_confirmation") String confirmPassword
    );

    @GET("user/detail/{Id}")
    Single<UserDetailsResponse> getUserDetails(
            @Path("Id") String userId
    );

    @FormUrlEncoded
    @POST("user/update/{Id}")
    Single<UserDetailsResponse> updateProfile(
            @Path("Id") String userId,
            @Field("id") String againUserId,
            @Field("name") String userName,
            @Field("phone") String userPhone,
            @Field("student_id") String studentId,
            @Field("stripe_id") String stripeId,
            @Field("default_card_id") String defaultCardId

    );

    @FormUrlEncoded
    @POST("order/rate")
    Single<SuccessResponse> rateOrder(
            @Field("order_id") String orderId,
            @Field("thumb") String thumb,
            @Field("area") String area,
            @Field("notes") String notes

    );

    @FormUrlEncoded
    @POST("order/reorder")
    Single<SuccessResponse> reorder(
            @Field("order_id") String orderId

    );

    @FormUrlEncoded
    @POST("user")
    Single<UserDetailsResponse> registerUser(
            @Field("email") String email,
            @Field("student_id") String studentId,
            @Field("stripe_id") String stripeId,
            @Field("password") String password,
            @Field("password_confirmation") String confirmPassword,
            @Field("device_id") String deviceId,
            @Field("device_type") String deviceType,
            @Field("device_token") String deviceToken
    );

    @FormUrlEncoded
    @POST("user/socialLogin")
    Single<UserDetailsResponse> registerFacebookUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("device_id") String deviceId,
            @Field("device_type") String deviceType,
            @Field("device_token") String deviceToken,
            @Field("social_token") String socialToken,
            @Field("social_profile_pic") String profilePic

    );

    @FormUrlEncoded
    @POST("user/{Id}/logout")
    Single<SuccessResponse> logoutUser(
            @Path("Id") String userId,
            @Field("device_id") String deviceId
    );

    @FormUrlEncoded
    @POST("contact")
    Single<SuccessResponse> contactSupport(
            @Field("order_id") String order_id,
            @Field("name") String name,
            @Field("hall_id") String hallId,
            @Field("email") String email,
            @Field("subject") String subject,
            @Field("message") String message
    );

    @FormUrlEncoded
    @POST("user/forgetPassword")
    Single<ForgotPassResponse> forgotPass(
            @Field("email") String email
    );

    @GET("home")
    Single<HomeDishesResponse> getAllHallDishes(
            @Query("user_id") String user_id,
            @Query("hall_id") String hall_id,
            @Query("timezone_name") String timezone,
            @Query("q") String query,
            @Query("dietary") String dietary,
            @Query("cuisine") String cuisine,
            @Query("sort") String sort
    );

    @GET("filter")
    Single<SearchHomeDishesResponse> searchHomeDishes(
            @Query("user_id") String userId,
            @Query("hall_id") String hallId,
            @Query("meal_id") String baseCategoryId,
            @Query("category_id") String subCategoryId,
            @Query("q") String query,
            @Query("dietary") String dietary,
            @Query("cuisine") String cuisine,
            @Query("sort") String sort,
            @Query("page") String pageNo,
            @Query("limit") String perPageCount
    );

    @GET("pages")
    Single<TermsResponse> getInfoPage(
            @Query("slug") String pageType
    );

    @GET("dishes/favorites/list")
    Single<FavouriteResponse> getFavourites(
            @Query("user_id") String userId,
            @Query("page") String pageNo,
            @Query("limit") String perPageCount
    );

    @GET("menu")
    Single<HallMenuResponse> getHallMenu(
            @Query("user_id") String userId,
            @Query("hall_id") String hallId,
            @Query("for") String hallFor,
            @Query("q") String search,
            @Query("dietary") String dietary,
            @Query("cuisine") String cuisine,
            @Query("sort") String sort,
            @Query("page") String pageNo,
            @Query("limit") String perPageCount
    );

    @GET("dishes/view/{Id}")
    Single<DishDetailsResponse> getDishDetails(
            @Path("Id") String dish_id,
            @Query("user_id") String userId,
            @Query("dish_id") String dishId
    );

    @FormUrlEncoded
    @POST("dishes/favorite")
    Single<SuccessResponse> addRemoveFavourite(
            @Field("user_id") String userId,
            @Field("dish_id") String dish_id
    );

    @FormUrlEncoded
    @POST("cart/add")
    Single<AddToCartResponse> addToCart(
            @Field("user_id") String userId,
            @Field("hall_id") String hallId,
            @Field("meal_id") String mealId,
            @Field("items") String items,
            @Field("override") String override
    );

    @FormUrlEncoded
    @POST("cart/remove")
    Single<CartResponse> removeFromCart(
            @Field("user_id") String userId,
            @Field("items") String items
    );

    @FormUrlEncoded
    @POST("cart/updateqty")
    Single<CartResponse> updateCartDishQuantity(
            @Field("user_id") String userId,
            @Field("items") String items
    );

    @GET("cart/view")
    Single<CartResponse> getCart(
            @Query("user_id") String userId
    );

    @GET("timings")
    Single<TimingsResponse> getLatestTimings(
            @Query("timezone_name") String timezone
    );

    @GET("cart/total")
    Single<LocalCartTaxResponse> getLocalCartItemsTax(
            @Query("hall_id") String hallId,
            @Query("cart_total") String cartTotal,
            @Query("coupon") String coupon
    );

    @GET("order/detail")
    Single<OrderSuccessResponse> getOrderStatus(
            @Query("order_id") String orderId
    );

    @FormUrlEncoded
    @POST("cart/apply_coupon")
    Single<CouponResponse> applyCoupon(
            @Field("user_id") String userId,
            @Field("hall_id") String hallId,
            @Field("coupon_code") String couponCode
    );

    @FormUrlEncoded
    @POST("cart/remove_coupon")
    Single<CouponResponse> removeCoupon(
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("notification/list/{Id}")
    Single<NotificationsResponse> getNotifications(
            @Path("Id") String userId,
            @Field("user_id") String againUserId,
            @Field("page") String pageNo,
            @Field("limit") String perPageCount
    );

    @GET("api/notification/status/{Id}/{Status}")
    Single<SuccessResponse> hideAllReadNotifications(
            @Path("Id") String userId,
            @Path("Status") String status
    );

    @GET("search")
    Single<SearchResponse> searchDish(
            @Query("user_id") String userId,
            @Query("hall_id") String hallId,
            @Query("meal_id") String baseCategoryId,
            @Query("category_id") String subCategoryId,
            @Query("q") String query,
            @Query("dietary") String dietary,
            @Query("cuisine") String cuisine,
            @Query("sort") String sort,
            @Query("page") String pageNo,
            @Query("limit") String perPageCount
    );

    @GET("orders/list")
    Single<OrdersResponse> myOrders(
            @Query("user_id") String userId,
            @Query("type") String type,
            @Query("page") String pageNo,
            @Query("limit") String perPageCount
    );

    @FormUrlEncoded
    @POST("order/create")
    Single<PlaceOrderResponse> placeOrder(
            @Field("user_id") String userId,
            @Field("order_id") String orderId,
            @Field("hall_id") String hallId,
            @Field("instructions") String instructions,
            @Field("payment_method") String paymentMethod,
            @Field("payment_details") String paymentDetails,
            @Field("pickup") String pickup
    );

    @GET("api/notification/unread/count")
    Single<NotificationCountResponse> notiCount(
            @Query("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("cart/add")
    Single<SuccessResponse> saveLocalcart(
            @Field("user_id") String userId,
            @Field("hall_id") String hallId,
            @Field("meal_id") String mealId,
            @Field("items") String items,
            @Field("override") String isOverride
    );

    @GET("dishes/nutritionalinfo/{dish_id}")
    Single<NutritionalResponse> getDishNutritions(
            @Path("dish_id") String dish_id
    );

    @GET("dishes/nutritionalinfo/{dish_id}")
    Single<NewNutritionalResponse> getNewDishNutritions(
            @Path("dish_id") String dish_id
    );

    @GET("order/detail")
    Single<OrderDetailResponse> getOrderDetail(
            @Query("order_id") String orderId
    );
}