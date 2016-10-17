package com.closedevice.fastapp.api.remote;

import com.closedevice.fastapp.model.response.fir.Version;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface FirApi {

    @GET("/apps/latest/{id}")
    Observable<Version> getVersion(@Path("id") String id, @Query("api_token") String token);


}
