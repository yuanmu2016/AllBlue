package com.yuanmu.allblue.fragment.activity_fragment;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ActivityEventService {
    @GET("event/list")
    Observable<ActivityEventBean> getEventList(@Query("loc") String loc, @Query("type") String type,
                                                         @Query("start") int start, @Query("count") int count);
}
