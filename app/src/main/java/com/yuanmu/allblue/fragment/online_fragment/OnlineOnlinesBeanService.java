package com.yuanmu.allblue.fragment.online_fragment;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface OnlineOnlinesBeanService {

    @GET("onlines")
    Observable<OnlineOnlinesBean> getOnlineOnlinesBean(@Query("cate") String cate, @Query("start") int start, @Query("count") int count);
}
