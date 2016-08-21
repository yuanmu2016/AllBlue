package com.yuanmu.allblue.fragment.online_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuanmu.allblue.R;
import com.yuanmu.allblue.util.Constants;
import com.yuanmu.allblue.view.GridSpacingItemDecoration;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class OnlinesDetailActivity extends AppCompatActivity {

    public static final String ARGS = "com.yuanmu.allblue.fragment.online_fragment.OnlinesDetailActivity.ARGS";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private String albumId;
    private List<PhotosPhotosBean.PhotosBean> photosBeanList;
    private OnlinesRecyclerViewAdapter adapter;
    private int start;
    private boolean isLoading;
    private  PhotosPhotosBeanService service;
    private  Observable<PhotosPhotosBean> observable;
    private Subscriber<PhotosPhotosBean> subscriber;

    private List<String> args;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_onlines);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.navigation);

        start = 0;
        isLoading = false;

        args = getIntent().getStringArrayListExtra(ARGS);

        albumId = args.get(args.size() - 1);






        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3,8,false));




        Glide.with(this).load(args.get(6)).placeholder(R.drawable.bcg_image_view)
                .centerCrop().into((ImageView)findViewById(R.id.image));


        Retrofit retrofit = Constants.RETROFIT;

        service = retrofit.create(PhotosPhotosBeanService.class);

        observable = service.getPhotosPhotosBean(albumId,start,Constants.COUNT_PER_DATA);

        subscriber = new Subscriber<PhotosPhotosBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(PhotosPhotosBean photosPhotosBean) {
                photosBeanList = photosPhotosBean.getPhotos();
                adapter = new OnlinesRecyclerViewAdapter(OnlinesDetailActivity.this,photosBeanList,args);
                recyclerView.setAdapter(adapter);
            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager manager = (GridLayoutManager)recyclerView.getLayoutManager();
                if(!isLoading && manager.findLastVisibleItemPosition() == recyclerView.getAdapter().getItemCount()-1){
                        isLoading = true;
                        start += Constants.COUNT_PER_DATA;
                        observable = service.getPhotosPhotosBean(albumId,start,Constants.COUNT_PER_DATA);
                        observable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<PhotosPhotosBean>() {
                                    @Override
                                    public void onCompleted() {
                                        isLoading = false;
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        isLoading = false;
                                    }

                                    @Override
                                    public void onNext(PhotosPhotosBean photosPhotosBean) {
                                        photosBeanList.addAll(photosPhotosBean.getPhotos());
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                    }
                }


        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(subscriber != null && subscriber.isUnsubscribed()){
            subscriber.unsubscribe();
        }
    }
}
