package com.yuanmu.allblue.fragment.online_fragment;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface PhotosPhotosBeanService {
    @GET("album/{id}/photos")
    Observable<PhotosPhotosBean> getPhotosPhotosBean(@Path("id") String id, @Query("start") int start,@Query("count") int count);
}
