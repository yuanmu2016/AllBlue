package com.yuanmu.allblue.util;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constants{

    public static final int COUNT_PER_DATA = 20;
    public static final String BASE_URL = "https://api.douban.com/v2/";
    public static final Retrofit RETROFIT = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build();
    public static final String CATEGORY_NAME = "com.yuanmu.allblue.CATEGORY_NAME";
}
