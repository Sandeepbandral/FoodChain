package com.android42works.magicapp.retrofit;


import android.content.Context;

import com.android42works.magicapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

   /* final static String LIVE_URL = "http://blusharkdev.com/magic/api/v1/";
    final static String SANDBOX_URL = "http://blusharkdev.com/magic/api/v1/";*/

    final static String MAIN_URL = "http://54.80.245.200/api/v1/";
    final static String INSTANCE_URL = "http://52.73.198.19/api/v1/";

    private static int REQUEST_TIMEOUT = 60;
    private static OkHttpClient okHttpClient;

    public static Retrofit getClient(Context context) {

        Gson gson = new GsonBuilder().setLenient().create();

        if (okHttpClient == null)
            initOkHttp(context);

        if (retrofit == null) {

            String BaseURL = "";
            String useLiveLinks = context.getString(R.string.use_base_links);
            if(useLiveLinks.equalsIgnoreCase("true")){
                BaseURL = MAIN_URL;
            }else {
                BaseURL = INSTANCE_URL;
            }

            retrofit = new Retrofit.Builder()
                    .baseUrl(BaseURL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    private static void initOkHttp(final Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(interceptor);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json");

                // Adding Authorization Key
                requestBuilder.addHeader("X-custom-header", "7890abcdefghijkl");

                Request request = requestBuilder.build();
                return chain.proceed(request);

            }
        });

        okHttpClient = httpClient.build();
    }

}
