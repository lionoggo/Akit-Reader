package com.closedevice.fastapp.api.remote;


import com.closedevice.fastapp.AppConstant;
import com.closedevice.fastapp.api.ClientFactory;
import com.closedevice.fastapp.api.convert.gan.GanGsonConverterFactory;
import com.closedevice.fastapp.api.convert.wx.WXGsonConverterFactory;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public enum ApiFactory {
    INSTANCE;

    private static WXApi sWXApi;
    private static GanApi sGanApi;
    private static FirApi sFirApi;

    ApiFactory() {
    }

    public static FirApi getFirApi() {
        if (sFirApi == null) {
            ApiFactory.sFirApi = createApi(AppConstant.API_FIR_URL, FirApi.class, GsonConverterFactory.create());
        }
        return sFirApi;
    }

    public static WXApi getWXApi() {
        if (sWXApi == null) {
            ApiFactory.sWXApi = createApi(AppConstant.API_WX_URL, WXApi.class, WXGsonConverterFactory.create());

        }
        return sWXApi;
    }

    public static GanApi getGanApi() {
        if (sGanApi == null) {
            ApiFactory.sGanApi = createApi(AppConstant.API_GAN_URL, GanApi.class, GanGsonConverterFactory.create());
        }
        return sGanApi;
    }

    private static <T> T createApi(String baseUrl, Class<T> t, Converter.Factory factory) {
        Retrofit.Builder mBuilder = new Retrofit.Builder()
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl);


        return mBuilder.client(ClientFactory.INSTANCE.getHttpClient()).build().create(t);
    }


}
