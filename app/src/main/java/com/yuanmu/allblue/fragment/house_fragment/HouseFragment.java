package com.yuanmu.allblue.fragment.house_fragment;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.yuanmu.allblue.activity.GroupActivity;
import com.yuanmu.allblue.R;
import com.yuanmu.allblue.view.EmbedGridView;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class HouseFragment extends Fragment {

    private ConvenientBanner<Integer> convenientBanner;
    private GridView gridView;
    private LinearLayout layoutBed;
    private LinearLayout layoutAlone;
    private LinearLayout layoutGay;
    private LinearLayout layoutRoommate;

     @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house, container, false);
        convenientBanner = (ConvenientBanner<Integer>) view.findViewById(R.id.convenient_banner);
        gridView = (EmbedGridView) view.findViewById(R.id.embed_grid_view);
        layoutBed = (LinearLayout) view.findViewById(R.id.layout_bed);
        layoutAlone = (LinearLayout) view.findViewById(R.id.layout_alone);
        layoutGay = (LinearLayout) view.findViewById(R.id.layout_gay);
        layoutRoommate = (LinearLayout) view.findViewById(R.id.layout_roommate);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Resources resources = getResources();

        final List<String> districs = Arrays.asList(resources.getString(R.string.futian),
                                 resources.getString(R.string.nanshan),
                                 resources.getString(R.string.luohu),
                                 resources.getString(R.string.baoan),
                                 resources.getString(R.string.yantian),
                                 resources.getString(R.string.longhua),
                                 resources.getString(R.string.longgang),
                                 resources.getString(R.string.shenzhen));


        final List<String> groupIds = Arrays.asList("futianzufang","nanshanzufang","luohuzufang","baoanzufang","yantianzufang","longhuazufang","longgangzufang","106955");

        final List<Integer> imagRes = Arrays.asList(R.drawable.futian_icon,R.drawable.nanshan_icon,R.drawable.luohu_icon,
                R.drawable.baoan_icon,R.drawable.yantian_icon,R.drawable.longhua_icon,R.drawable.longgang_icon,R.drawable.more);


        List<Integer>  localImages = Arrays.asList(R.drawable.futian, R.drawable.nanshan, R.drawable.luohu, R.drawable.baoan,
                R.drawable.yantian, R.drawable.longhua, R.drawable.longgang);


        convenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new LocalImageHolderView();
            }
        }, localImages).setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .startTurning(3000);

        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                GroupActivity.newInstance(getActivity(),groupIds.get(position),districs.get(position),groupIds.get(position));
            }
        });


        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return districs.size();
            }

            @Override
            public Object getItem(int position) {
                return districs.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.grid_item_house,parent,false);
                CircleImageView imageView = (CircleImageView) convertView.findViewById(R.id.image_grid_item);
                imageView.setBorderColor(Color.argb(20,55,55,55));
                imageView.setBorderWidth(1);
                imageView.setImageResource(imagRes.get(position));
                TextView textView = (TextView) convertView.findViewById(R.id.text_grid_item);
                textView.setText(districs.get(position));

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GroupActivity.newInstance(getActivity(),groupIds.get(position),districs.get(position),groupIds.get(position));
                    }
                });

                return convertView;
            }
        });



        layoutBed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupActivity.newInstance(getActivity(),"472004","床位","472004");
            }
        });

        layoutAlone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupActivity.newInstance(getActivity(),"537027","整租","537027");
            }
        });


        layoutGay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupActivity.newInstance(getActivity(),"425971","同志","425971");
            }
        });

        layoutRoommate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupActivity.newInstance(getActivity(),"528184","求室友","528184");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        convenientBanner.startTurning(3000);
    }


    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
    }




    private class LocalImageHolderView implements Holder<Integer> {

        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, Integer data) {
            imageView.setImageResource(data);
        }
    }

}
