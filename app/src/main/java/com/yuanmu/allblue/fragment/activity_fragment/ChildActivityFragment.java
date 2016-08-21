package com.yuanmu.allblue.fragment.activity_fragment;

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

import com.yuanmu.allblue.activity.GroupActivity;
import com.yuanmu.allblue.R;
import com.yuanmu.allblue.util.CacheExecutive;
import com.yuanmu.allblue.util.Constants;
import com.yuanmu.allblue.util.NetworkUtil;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class ChildActivityFragment extends Fragment {

    private SwipeRefreshLayout refreshLayout;
    private ListView listView;
    private String cateName;

    private Observable<ActivityEventBean> observable ;
    private Subscriber<ActivityEventBean> subscriber;

    private ActivityEventService service;


    private int start;
    private boolean isRefreshing;
    private boolean isLoading;

    private ChildActivityFragmentAdapter adapter;
    private List<ActivityEventBean.EventsBean>  eventBeanList;

    private String preferencesKey;

    private CacheExecutive<ActivityEventBean.EventsBean> cacheExecutive;





    public static Fragment newInstance(String category,String preferencesKey){
        Bundle args = new Bundle();
        args.putString(Constants.CATEGORY_NAME,category);
        args.putString(GroupActivity.PREFERENCES,preferencesKey);
        Fragment fragment = new ChildActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cateName = getArguments().getString(Constants.CATEGORY_NAME);
        preferencesKey = getArguments().getString(GroupActivity.PREFERENCES);
        cacheExecutive = new CacheExecutive<>(getActivity(),cateName,preferencesKey);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_activity,container,false);
        listView = (ListView) view.findViewById(R.id.list_view);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        start = 0;
        isRefreshing = false;
        isLoading = false;


        cacheExecutive.openCache();



        Retrofit retrofit = Constants.RETROFIT;

        service = retrofit.create(ActivityEventService.class);

        if(NetworkUtil.isNetworkAvailable(getActivity())){
            observable = service.getEventList("shenzhen",cateName,start,Constants.COUNT_PER_DATA);
        }else {
            observable = Observable.just(cacheExecutive.readBeanList())
                    .subscribeOn(Schedulers.io()).flatMap(new Func1<List<ActivityEventBean.EventsBean>, Observable<ActivityEventBean>>() {
                        @Override
                        public Observable<ActivityEventBean> call(List<ActivityEventBean.EventsBean> eventsBeen) {
                            ActivityEventBean activityEventBean = new ActivityEventBean();
                            activityEventBean.setEvents(eventsBeen);
                            return Observable.just(activityEventBean);
                        }
                    }).observeOn(Schedulers.io());
        }


        subscriber = new Subscriber<ActivityEventBean>() {
            @Override
            public void onCompleted() {
                Log.e("activityBeanHolder","complete");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("activityBeanHolder","error");
            }

            @Override
            public void onNext(ActivityEventBean activityEventBean) {

                eventBeanList = activityEventBean.getEvents();
                adapter = new ChildActivityFragmentAdapter(getActivity(),eventBeanList);
                listView.setAdapter(adapter);

            }
        };
        observable.subscribeOn(Schedulers.io())
                .map(new Func1<ActivityEventBean, ActivityEventBean>() {

                    @Override
                    public ActivityEventBean call(ActivityEventBean activityEventBean) {
                        if(NetworkUtil.isNetworkAvailable(getActivity())){
                            cacheExecutive.deleteCache();
                            cacheExecutive.openCache();
                            cacheExecutive.writeBeanList(activityEventBean.getEvents(),start);
                        }
                        return activityEventBean;
                    }
                })
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isRefreshing && NetworkUtil.isNetworkAvailable(getActivity())){
                    subscriber = new Subscriber<ActivityEventBean>() {

                        @Override
                        public void onStart() {
                            super.onStart();
                        }

                        @Override
                        public void onCompleted() {
                            isRefreshing = false;
                            refreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            isRefreshing = false;
                            refreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onNext(ActivityEventBean activityEventBean) {
                            eventBeanList = activityEventBean.getEvents();
                            adapter = new ChildActivityFragmentAdapter(getActivity(),eventBeanList);
                            listView.setAdapter(adapter);
                        }
                    };


                    start = 0;

                    service.getEventList("shenzhen",cateName,start,Constants.COUNT_PER_DATA)
                            .subscribeOn(Schedulers.io())
                            .doOnSubscribe(new Action0() {
                                @Override
                                public void call() {
                                    isRefreshing = true;
                                    refreshLayout.setRefreshing(true);
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(new Func1<ActivityEventBean, ActivityEventBean>() {

                                @Override
                                public ActivityEventBean call(ActivityEventBean activityEventBean) {
                                    if (NetworkUtil.isNetworkAvailable(getActivity())) {
                                        cacheExecutive.deleteCache();
                                        cacheExecutive.openCache();
                                        cacheExecutive.writeBeanList(activityEventBean.getEvents(), start);
                                    }
                                    return activityEventBean;

                                }
                            })
                            .observeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(subscriber);
                }

                if(!isRefreshing && !NetworkUtil.isNetworkAvailable(getActivity())){
                    refreshLayout.setRefreshing(false);
                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount - 1  && !isLoading) {
                    isLoading = true;
                    start += Constants.COUNT_PER_DATA;

                    if(NetworkUtil.isNetworkAvailable(getActivity())){
                        observable = service.getEventList("shenzhen",cateName,start,Constants.COUNT_PER_DATA);
                    }else {
                        observable = Observable.just(cacheExecutive.readBeanList())
                                .subscribeOn(Schedulers.io()).flatMap(new Func1<List<ActivityEventBean.EventsBean>, Observable<ActivityEventBean>>() {
                                    @Override
                                    public Observable<ActivityEventBean> call(List<ActivityEventBean.EventsBean> eventsBeen) {
                                        ActivityEventBean activityEventBean = new ActivityEventBean();
                                        activityEventBean.setEvents(eventsBeen);
                                        return Observable.just(activityEventBean);
                                    }
                                }).observeOn(Schedulers.io());
                    }


                    subscriber = new Subscriber<ActivityEventBean>() {
                        @Override
                        public void onCompleted() {
                            isLoading = false;
                        }

                        @Override
                        public void onError(Throwable e) {
                            isLoading = false;
                        }

                        @Override
                        public void onNext(ActivityEventBean activityEventBean) {
                            eventBeanList.addAll(activityEventBean.getEvents());
                            adapter.notifyDataSetChanged();
                        }
                    };

                    observable.subscribeOn(Schedulers.io())
                            .map(new Func1<ActivityEventBean, ActivityEventBean>() {
                                @Override
                                public ActivityEventBean call(ActivityEventBean activityEventBean) {
                                    if(NetworkUtil.isNetworkAvailable(getActivity())){
                                        cacheExecutive.writeBeanList(activityEventBean.getEvents(),start);
                                    }
                                    return activityEventBean;
                                }
                            })
                            .observeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(subscriber);

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
    }
}
