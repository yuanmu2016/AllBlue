package com.yuanmu.allblue.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.bumptech.glide.Glide;
import com.yuanmu.allblue.R;
import com.yuanmu.allblue.util.BitmapUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class ImageDisplayActivity extends AppCompatActivity {
    private List<String> imageUrls;
    private int index;
    private int currentIndex;
    private Map<Integer, View> hashMapViews;
    public static final String PHOTOBEAN_LIST = "com.yuanmu.allblue.activity.ImageDisplayActivity.PHOTOBEAN_LIST";
    public static final String IMAGE_INDEX = "com.yuanmu.allblue.imagedisplayactivity.IMAGE_INDEX";
    private boolean once;
    private TextView textViewCustomToastContent;
    private Toast  toastCustom;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        once = false;

        Intent intent = getIntent();
        imageUrls = intent.getStringArrayListExtra(PHOTOBEAN_LIST);
        index = intent.getIntExtra(IMAGE_INDEX, 0);


        textViewCustomToastContent = (TextView)getLayoutInflater().inflate(R.layout.custom_toast_content, null);
        toastCustom = new Toast(this);
        toastCustom.setDuration(Toast.LENGTH_LONG);
        toastCustom.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,200);


        hashMapViews = new HashMap<>();

        final TextView textViewIndex = (TextView) findViewById(R.id.text_index);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        registerForContextMenu(viewPager);

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imageUrls.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = LayoutInflater.from(ImageDisplayActivity.this).inflate(R.layout.view_pager_item, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.image_view_view_pager);
                Glide.with(ImageDisplayActivity.this).load(imageUrls.get(position)).placeholder(R.drawable.bcg_view_pager)
                        .fitCenter().into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                container.addView(view);
                //imageViews.add(position,view);//为什么错误？
                hashMapViews.put(position, view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(hashMapViews.get(position));
            }
        });

        viewPager.setCurrentItem(index);
        viewPager.setPageTransformer(true, new RotateUpTransformer());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (!once) {
                    textViewIndex.setText((position + 1) + "/" + imageUrls.size());
                    once = true;
                    currentIndex = position;
                }
            }

            @Override
            public void onPageSelected(int position) {
                textViewIndex.setText((position + 1) + "/" + imageUrls.size());
                currentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 0, Menu.NONE, "保存图片");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                Observable.just(imageUrls.get(currentIndex))
                        .subscribeOn(Schedulers.io())
                        .map(new Func1<String, String>() {
                            @Override
                            public String call(String s) {
                                return BitmapUtil.saveImageToGallery(ImageDisplayActivity.this, s,"allblue");
                            }
                        })
                        .observeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                if (s == null) {
                                    s = "保存图片时出错";
                                } else {
                                    s = "图片已经保存";
                                }
                                textViewCustomToastContent.setText(s);
                                toastCustom.setView(textViewCustomToastContent);
                                toastCustom.show();
                            }
                        });
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }


}
