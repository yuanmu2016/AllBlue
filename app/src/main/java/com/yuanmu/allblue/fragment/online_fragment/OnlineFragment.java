package com.yuanmu.allblue.fragment.online_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.yuanmu.allblue.R;
import com.yuanmu.allblue.util.CacheExecutive;
import com.yuanmu.allblue.util.Constants;
import com.yuanmu.allblue.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class OnlineFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;

    private int start;
    private boolean isLoading;
    private boolean isRefreshing;


    private  OnlineOnlinesBeanService service;

    private Observable<OnlineOnlinesBean> observable;
    private Subscriber<PhotosPhotosBean> subscriber;

    private Observable<List<OnlinesBeanWithPhotos>> observableList;
    private Subscriber<List<OnlinesBeanWithPhotos>> subscriberList;


    
    private OnlinesFragmentAdapter adapter;

    private List<PhotosPhotosBean> photosPhotosBeanList = new ArrayList<>();
    private List<OnlineOnlinesBean.OnlinesBean> onlinesBeanList = new ArrayList<>();

    private List<OnlinesBeanWithPhotos> onlinesBeanWithPhotosList = new ArrayList<>();

    private CacheExecutive<OnlinesBeanWithPhotos> cacheExecutive;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cacheExecutive = new CacheExecutive<>(getActivity(),"onlines","onlines");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_online,container,false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        listView = (ListView) view.findViewById(R.id.list_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        start = 0;
        isLoading = false;
        isRefreshing = false;


        cacheExecutive.openCache();


        final Retrofit retrofit = Constants.RETROFIT;

        service = retrofit.create(OnlineOnlinesBeanService.class);



        if(NetworkUtil.isNetworkAvailable(getActivity())){
            observable = service.getOnlineOnlinesBean("latest",start,Constants.COUNT_PER_DATA);
            subscriber = new Subscriber<PhotosPhotosBean>() {
                @Override
                public void onCompleted() {
                    for(int i = 0; i < onlinesBeanList.size(); i++){
                        OnlinesBeanWithPhotos onlinesBeanWithPhotos = new OnlinesBeanWithPhotos();
                        onlinesBeanWithPhotos.setOnlinesBean(onlinesBeanList.get(i));
                        onlinesBeanWithPhotos.setPhotosPhotosBean(photosPhotosBeanList.get(i));
                        onlinesBeanWithPhotosList.add(onlinesBeanWithPhotos);
                    }
                    cacheExecutive.deleteCache();
                    cacheExecutive.openCache();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            cacheExecutive.writeBeanList(onlinesBeanWithPhotosList,start);
                        }
                    }).start();


                    adapter =  new OnlinesFragmentAdapter(getActivity(),onlinesBeanWithPhotosList);
                    listView.setAdapter(adapter);
                    Log.e("OnlinesBeanWithPhotoses", "Get OnlinesBeanWithPhotoses complete");
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("OnlinesBeanWithPhotoses", "Get OnlinesBeanWithPhotoses error");
                }

                @Override
                public void onNext(PhotosPhotosBean photosPhotosBean) {
                    photosPhotosBeanList.add(photosPhotosBean);
                }
            };

            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .flatMap(new Func1<OnlineOnlinesBean,Observable<OnlineOnlinesBean.OnlinesBean>>() {
                        @Override
                        public Observable<OnlineOnlinesBean.OnlinesBean> call(OnlineOnlinesBean onlineOnlinesBean){

                            onlinesBeanList = onlineOnlinesBean.getOnlines();
                            return Observable.from(onlineOnlinesBean.getOnlines());
                        }
                    })
                    .observeOn(Schedulers.io())
                    .flatMap(new Func1<OnlineOnlinesBean.OnlinesBean, Observable<PhotosPhotosBean>>() {
                        @Override
                        public Observable<PhotosPhotosBean> call(OnlineOnlinesBean.OnlinesBean onlinesBean) {

                            PhotosPhotosBeanService photoService = retrofit.create(PhotosPhotosBeanService.class);

                            return photoService.getPhotosPhotosBean(onlinesBean.getAlbum_id(),start,Constants.COUNT_PER_DATA);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);

        }else {


            observableList = Observable.just(cacheExecutive.readBeanList());
            subscriberList = new Subscriber<List<OnlinesBeanWithPhotos>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<OnlinesBeanWithPhotos> onlinesBeanWithPhotoses) {
                    onlinesBeanWithPhotosList = onlinesBeanWithPhotoses;
                    adapter = new OnlinesFragmentAdapter(getActivity(),onlinesBeanWithPhotosList);
                    listView.setAdapter(adapter);
                }
            };

            observableList.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriberList);


        }





        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isRefreshing  && NetworkUtil.isNetworkAvailable(getActivity())){
                    subscriber = new Subscriber<PhotosPhotosBean>() {
                        @Override
                        public void onCompleted() {
                            onlinesBeanWithPhotosList.clear();
                            for(int i = 0; i < onlinesBeanList.size(); i++){
                                OnlinesBeanWithPhotos onlineBeanWithPhotos = new OnlinesBeanWithPhotos();
                                onlineBeanWithPhotos.setOnlinesBean(onlinesBeanList.get(i));
                                onlineBeanWithPhotos.setPhotosPhotosBean(photosPhotosBeanList.get(i));
                                onlinesBeanWithPhotosList.add(onlineBeanWithPhotos);
                            }

                            cacheExecutive.deleteCache();
                            cacheExecutive.openCache();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    cacheExecutive.writeBeanList(onlinesBeanWithPhotosList,start);
                                }
                            }).start();

                            adapter =  new OnlinesFragmentAdapter(getActivity(),onlinesBeanWithPhotosList);
                            listView.setAdapter(adapter);

                            isRefreshing = false;
                            swipeRefreshLayout.setRefreshing(false);
                            Log.e("refresh", "Get OnlinesBeanHolder complete");
                        }

                        @Override
                        public void onError(Throwable e) {
                            isRefreshing = false;
                            swipeRefreshLayout.setRefreshing(false);
                            Log.e("refresh", "Get OnlinesBeanHolder error");
                        }

                        @Override
                        public void onNext(PhotosPhotosBean photosPhotosBean) {
                            photosPhotosBeanList.add(photosPhotosBean);
                        }
                    };



                    start = 0;
                    service.getOnlineOnlinesBean("latest",start,Constants.COUNT_PER_DATA)
                            .subscribeOn(Schedulers.io())
                            .doOnSubscribe(new Action0() {
                                @Override
                                public void call() {
                                    isRefreshing = true;
                                    swipeRefreshLayout.setRefreshing(true);
                                    photosPhotosBeanList.clear();
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .observeOn(Schedulers.io())
                            .flatMap(new Func1<OnlineOnlinesBean,Observable<OnlineOnlinesBean.OnlinesBean>>() {
                                @Override
                                public Observable<OnlineOnlinesBean.OnlinesBean> call(OnlineOnlinesBean onlineOnlinesBean) {

                                    onlinesBeanList = onlineOnlinesBean.getOnlines();
                                    return Observable.from(onlinesBeanList);
                                }
                            })
                            .observeOn(Schedulers.io())
                            .flatMap(new Func1<OnlineOnlinesBean.OnlinesBean, Observable<PhotosPhotosBean>>() {
                                @Override
                                public Observable<PhotosPhotosBean> call(OnlineOnlinesBean.OnlinesBean onlinesBean) {

                                    PhotosPhotosBeanService  photoService = retrofit.create(PhotosPhotosBeanService.class);
                                    Observable<PhotosPhotosBean> observablePhotos = photoService.getPhotosPhotosBean(onlinesBean.getAlbum_id(),start,Constants.COUNT_PER_DATA);
                                    return observablePhotos;
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(subscriber);

                }

                if(!isRefreshing && !NetworkUtil.isNetworkAvailable(getActivity())){
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });




        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, final int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, final int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if(!isLoading && firstVisibleItem + visibleItemCount == totalItemCount - 1){
                        isLoading = true;
                        start += Constants.COUNT_PER_DATA;


                        if(NetworkUtil.isNetworkAvailable(getActivity())){
                            observable =service.getOnlineOnlinesBean("latest",start,Constants.COUNT_PER_DATA);
                            subscriber = new Subscriber<PhotosPhotosBean>() {
                                @Override
                                public void onCompleted() {
                                    for(int i = start; i < onlinesBeanList.size(); i++){
                                        OnlinesBeanWithPhotos onlineBeanWithPhotos = new OnlinesBeanWithPhotos();
                                        onlineBeanWithPhotos.setOnlinesBean(onlinesBeanList.get(i));
                                        onlineBeanWithPhotos.setPhotosPhotosBean(photosPhotosBeanList.get(i));
                                        onlinesBeanWithPhotosList.add(onlineBeanWithPhotos);
                                    }

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            cacheExecutive.writeBeanList(onlinesBeanWithPhotosList.subList(start,onlinesBeanWithPhotosList.size()),start);

                                        }
                                    }).start();

                                    adapter.notifyDataSetChanged();
                                    Log.e("LoadMore", "LoadMore completed");
                                    isLoading = false;
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e("LoadMore", "LoadMore error");
                                    isLoading = false;
                                }

                                @Override
                                public void onNext(PhotosPhotosBean photosPhotosBean) {
                                    photosPhotosBeanList.add(photosPhotosBean);
                                }
                            };

                            observable.subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.io())
                                    .flatMap(new Func1<OnlineOnlinesBean,Observable<OnlineOnlinesBean.OnlinesBean>>() {
                                        @Override
                                        public Observable<OnlineOnlinesBean.OnlinesBean> call(OnlineOnlinesBean onlineOnlinesBean){
                                            onlinesBeanList.addAll(onlineOnlinesBean.getOnlines());
                                            return Observable.from(onlineOnlinesBean.getOnlines());
                                        }
                                    })
                                    .observeOn(Schedulers.io())
                                    .flatMap(new Func1<OnlineOnlinesBean.OnlinesBean, Observable<PhotosPhotosBean>>() {
                                        @Override
                                        public Observable<PhotosPhotosBean> call(OnlineOnlinesBean.OnlinesBean onlinesBean) {
                                            Observable<PhotosPhotosBean> observablePhotos;
                                            PhotosPhotosBeanService photoService = retrofit.create(PhotosPhotosBeanService.class);
                                            observablePhotos = photoService.getPhotosPhotosBean(onlinesBean.getAlbum_id(),start,Constants.COUNT_PER_DATA);
                                            return observablePhotos;
                                        }
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(subscriber);

                        }else {
                            observableList = Observable.just(cacheExecutive.readBeanList());
                            subscriberList = new Subscriber<List<OnlinesBeanWithPhotos>>() {
                                @Override
                                public void onCompleted() {
                                    isLoading = false;

                                }

                                @Override
                                public void onError(Throwable e) {
                                    isLoading = false;
                                }

                                @Override
                                public void onNext(List<OnlinesBeanWithPhotos> onlinesBeanWithPhotoses) {

                                    onlinesBeanWithPhotosList.addAll(onlinesBeanWithPhotoses);
                                    adapter.notifyDataSetChanged();
                                }
                            };

                            observableList.subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(subscriberList);

                        }


                    }
                }

        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        cacheExecutive.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cacheExecutive.closeCache();
        if(subscriber != null && subscriber.isUnsubscribed()){
            subscriber.unsubscribe();
        }

        if(subscriberList != null && subscriberList.isUnsubscribed()){
            subscriberList.unsubscribe();
        }
    }


}
