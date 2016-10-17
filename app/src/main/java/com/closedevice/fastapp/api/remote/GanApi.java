package com.closedevice.fastapp.api.remote;

import com.closedevice.fastapp.model.response.gan.GanImages;
import com.closedevice.fastapp.model.response.gan.GanItem;
import com.closedevice.fastapp.model.response.gan.GanResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


public interface GanApi {

    @GET("data/福利/{num}")
    Observable<GanResult<List<GanImages>>> getImages(@Path("num") int num);

    @GET("data/{catalog}/{num}/{page}")
    Observable<GanResult<List<GanItem>>> getArticles(@Path("catalog") String catalog, @Path("num") int num, @Path("page") int page);


}
