package com.closedevice.fastapp.api.remote;

import com.closedevice.fastapp.model.response.wx.WXItem;
import com.closedevice.fastapp.model.response.wx.WXResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface WXApi {

    @GET("wxnew")
    Observable<WXResult<List<WXItem>>> getWXHot(@Query("key") String key, @Query("num") int num, @Query("page") int page);

    @GET("wxnew")
    Observable<WXResult<List<WXItem>>> getWXHotSearch(@Query("key") String key, @Query("num") int num, @Query("page") int page, @Query("word") String word);
}

