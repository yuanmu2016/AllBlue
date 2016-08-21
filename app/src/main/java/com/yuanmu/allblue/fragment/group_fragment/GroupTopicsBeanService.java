package com.yuanmu.allblue.fragment.group_fragment;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface GroupTopicsBeanService {
    @GET("group/{id}/topics")
    Observable<GroupTopicsBean> getGroupTopicsBean(@Path("id") String id, @Query("start") int start, @Query("count") int count);
}
