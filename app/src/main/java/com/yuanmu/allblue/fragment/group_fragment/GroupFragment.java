package com.yuanmu.allblue.fragment.group_fragment;

import android.app.Activity;
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

public class GroupFragment extends Fragment {


    private GroupFragmentAdapter groupFragmentAdapter;
    private List<GroupTopicsBean.TopicsBean> topicsBeanList;
    private int start;
    private boolean isLoading;
    private boolean isRefreshing;


    private CacheExecutive<GroupTopicsBean.TopicsBean> cacheExecutive;


    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;


    private GroupTopicsBeanService service;
    private Observable<GroupTopicsBean> observable;
    private Subscriber<GroupTopicsBean> subscriber;
    private String groupId;
    private String cacheDirName;
    private Activity activity;

    private  String preferencesKey;




    public static Fragment newInstance(String groupId,String cacheDirName,String preferencesKey){
        Bundle args = new Bundle();
        args.putString(GroupActivity.GROUP_ID,groupId);
        args.putString(GroupActivity.CACHEDIR_NAME,cacheDirName);
        args.putString(GroupActivity.PREFERENCES,preferencesKey);
        Fragment fragment = new GroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        groupId = getArguments().getString(GroupActivity.GROUP_ID);
        cacheDirName = getArguments().getString(GroupActivity.CACHEDIR_NAME);
        preferencesKey = getArguments().getString(GroupActivity.PREFERENCES);
        cacheExecutive = new CacheExecutive<>(activity,cacheDirName,preferencesKey);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group,container,false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

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


        Retrofit retrofit = Constants.RETROFIT;

        service = retrofit.create(GroupTopicsBeanService.class);

        if(NetworkUtil.isNetworkAvailable(activity)){
            observable = service.getGroupTopicsBean(groupId,start,Constants.COUNT_PER_DATA);
        }else {

            observable = Observable.just(cacheExecutive.readBeanList())
                    .subscribeOn(Schedulers.io())
                    .flatMap(new Func1<List<GroupTopicsBean.TopicsBean>, Observable<GroupTopicsBean>>() {
                        @Override
                        public Observable<GroupTopicsBean> call(List<GroupTopicsBean.TopicsBean> topicsBeen) {
                            GroupTopicsBean groupTopicsBean = new GroupTopicsBean();
                            groupTopicsBean.setTopics(topicsBeen);
                            return Observable.just(groupTopicsBean);
                        }
                    }).observeOn(Schedulers.io());
        }

        subscriber = new Subscriber<GroupTopicsBean>() {
            @Override
            public void onCompleted() {
                Log.e("GroupItemBean", "Get GroupItemBean complete");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("GroupItemBean", "Get GroupItemBean error");
            }

            @Override
            public void onNext(GroupTopicsBean groupTopicsBean) {
                topicsBeanList = groupTopicsBean.getTopics();
                groupFragmentAdapter = new GroupFragmentAdapter(activity, topicsBeanList);
                listView.setAdapter(groupFragmentAdapter);
            }
        };

        observable.subscribeOn(Schedulers.io())
                .map(new Func1<GroupTopicsBean, GroupTopicsBean>() {
                    @Override
                    public GroupTopicsBean call(GroupTopicsBean groupTopicsBean){
                        if(NetworkUtil.isNetworkAvailable(activity)){
                            cacheExecutive.deleteCache();
                            cacheExecutive.openCache();
                            cacheExecutive.writeBeanList(groupTopicsBean.getTopics(),start);
                        }
                        return groupTopicsBean;
                    }
                })
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isRefreshing  && NetworkUtil.isNetworkAvailable(activity)){

                    subscriber = new Subscriber<GroupTopicsBean>() {

                        @Override
                        public void onCompleted() {
                            isRefreshing = false;
                            swipeRefreshLayout.setRefreshing(false);
                            Log.e("refresh", "refresh GroupItemBean complete");
                        }

                        @Override
                        public void onError(Throwable e) {
                            isRefreshing = false;
                            swipeRefreshLayout.setRefreshing(false);
                            Log.e("refresh", "refresh GroupItemBean error");
                        }

                        @Override
                        public void onNext(GroupTopicsBean groupTopicsBean) {
                            topicsBeanList = groupTopicsBean.getTopics();
                            groupFragmentAdapter = new GroupFragmentAdapter(activity, topicsBeanList);
                            listView.setAdapter(groupFragmentAdapter);
                        }
                    };

                    start = 0;

                    service.getGroupTopicsBean(groupId,start,Constants.COUNT_PER_DATA)
                            .subscribeOn(Schedulers.io())
                            .doOnSubscribe(new Action0() {
                                @Override
                                public void call() {
                                    isRefreshing = true;
                                    swipeRefreshLayout.setRefreshing(true);
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(new Func1<GroupTopicsBean,GroupTopicsBean>() {
                                @Override
                                public GroupTopicsBean call(GroupTopicsBean groupTopicsBean) {
                                    cacheExecutive.deleteCache();
                                    cacheExecutive.openCache();
                                    cacheExecutive.writeBeanList(groupTopicsBean.getTopics(),start);

                                    return groupTopicsBean;
                                }
                            })
                            .observeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(subscriber);
                }

                if(!isRefreshing && !NetworkUtil.isNetworkAvailable(activity)){
                    swipeRefreshLayout.setRefreshing(false);
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
                    if(NetworkUtil.isNetworkAvailable(activity)){
                        observable = service.getGroupTopicsBean(groupId,start,Constants.COUNT_PER_DATA);
                    }else {
                        observable = Observable.just(cacheExecutive.readBeanList())
                                .subscribeOn(Schedulers.io()).flatMap(new Func1<List<GroupTopicsBean.TopicsBean>, Observable<GroupTopicsBean>>() {
                                    @Override
                                    public Observable<GroupTopicsBean> call(List<GroupTopicsBean.TopicsBean> topicsBeen) {
                                        GroupTopicsBean groupTopicsBean = new GroupTopicsBean();
                                        groupTopicsBean.setTopics(topicsBeen);
                                        return Observable.just(groupTopicsBean);
                                    }
                                }).observeOn(Schedulers.io());
                    }

                    subscriber = new Subscriber<GroupTopicsBean>() {
                        @Override
                        public void onCompleted() {
                            Log.e("LoadMore", "LoadMore completed");
                            isLoading = false;
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("LoadMore", "LoadMore error");
                            isLoading = false;
                        }

                        @Override
                        public void onNext(GroupTopicsBean groupTopicsBean) {
                            topicsBeanList.addAll(groupTopicsBean.getTopics());
                            groupFragmentAdapter.notifyDataSetChanged();
                        }
                    };

                    observable.subscribeOn(Schedulers.io())
                            .map(new Func1<GroupTopicsBean, GroupTopicsBean>() {
                                @Override
                                public GroupTopicsBean call(GroupTopicsBean groupTopicsBean){
                                    if(NetworkUtil.isNetworkAvailable(activity)){
                                        cacheExecutive.writeBeanList(groupTopicsBean.getTopics(),start);
                                    }
                                    return groupTopicsBean;
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
    public void onPause() {
        super.onPause();
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
