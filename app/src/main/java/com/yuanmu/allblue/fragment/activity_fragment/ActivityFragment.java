package com.yuanmu.allblue.fragment.activity_fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuanmu.allblue.R;

import java.util.Arrays;
import java.util.List;


public class ActivityFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<String> activityCatCn = Arrays.asList("音乐","运动","聚会","旅行","电影");
    private List<String> activityCatEn = Arrays.asList("music","sports","party","travel","film");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_activity,container,false);
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);
        return v;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ChildActivityFragment.newInstance(activityCatEn.get(position),activityCatEn.get(position));
            }

            @Override
            public int getCount() {
                return activityCatEn.size();
            }
        };
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < activityCatCn.size(); i++) {
            tabLayout.getTabAt(i).setText(activityCatCn.get(i));
        }
    }


}
