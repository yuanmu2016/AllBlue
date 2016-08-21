package com.yuanmu.allblue.activity;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuanmu.allblue.R;
import com.yuanmu.allblue.fragment.activity_fragment.ActivityFragment;
import com.yuanmu.allblue.fragment.group_fragment.GroupFragment;
import com.yuanmu.allblue.fragment.house_fragment.HouseFragment;
import com.yuanmu.allblue.fragment.online_fragment.OnlineFragment;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textViewTitle;

    private List<ImageView> imageViews;
    private List<TextView> textViews;
    private List<Fragment> fragments;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private int currentChosenItem = 0;
    private int passedChosenItem = -1;


    private String[] fragmentTags = new String[]{"house","activity","work","online"};
    private int[] drawableResChosen = new int[]{R.drawable.house_chosen,R.drawable.avtivity_chosen,R.drawable.work_chosen,R.drawable.online_chosen};
    private int[] drawableResUnchosen = new int[]{R.drawable.house_unchosen,R.drawable.activity_unchosen,R.drawable.work_unchosen,R.drawable.online_unchosen};
    private String[] titles;


    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTitle = (TextView) findViewById(R.id.text_title);

        resources = getResources();

        titles = new String[]{resources.getString(R.string.house),
                              resources.getString(R.string.activity),
                              resources.getString(R.string.job),
                              resources.getString(R.string.online)};


        findViewById(R.id.layout_item0).setOnClickListener(this);
        findViewById(R.id.layout_item1).setOnClickListener(this);
        findViewById(R.id.layotu_item2).setOnClickListener(this);
        findViewById(R.id.layout_item3).setOnClickListener(this);





        ImageView imageViewItem0 = (ImageView) findViewById(R.id.imageview_item0);
        ImageView imageViewItem1 = (ImageView) findViewById(R.id.imageview_item1);
        ImageView imageViewItem2 = (ImageView) findViewById(R.id.imageview_item2);
        ImageView imageViewItem3 = (ImageView) findViewById(R.id.imageview_item3);
        imageViews = Arrays.asList(imageViewItem0,imageViewItem1,imageViewItem2,imageViewItem3);

        TextView textViewItem0  = (TextView) findViewById(R.id.textview_item0);
        TextView textViewItem1 = (TextView) findViewById(R.id.textview_item1);
        TextView textViewItem2 = (TextView) findViewById(R.id.textview_item2);
        TextView textViewItem3 = (TextView) findViewById(R.id.textview_item3);
        textViews = Arrays.asList(textViewItem0,textViewItem1,textViewItem2,textViewItem3);


        Fragment houseFragment = new HouseFragment();
        Fragment activityFragment = new ActivityFragment();
        Fragment jobFragment = GroupFragment.newInstance("168726","招聘","168726");
        Fragment onlineFragment = new OnlineFragment();
        fragments = Arrays.asList(houseFragment,activityFragment,jobFragment,onlineFragment);


        textViewTitle.setText(resources.getString(R.string.house));

        imageViewItem0.setImageResource(drawableResChosen[0]);
        for (int i = 1; i < 4; i++) {
            imageViews.get(i).setImageResource(drawableResUnchosen[i]);
        }

        textViewItem0.setTextColor(resources.getColor(R.color.colorPrimary));
        for (int i = 1; i < 4; i++) {
            textViews.get(i).setTextColor(resources.getColor(R.color.colorTitle));
        }




        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        houseFragment = fragmentManager.findFragmentById(R.id.fragment_container);


        if(houseFragment == null){
            fragmentTransaction.add(R.id.fragment_container,fragments.get(currentChosenItem),"house").commit();
        }


    }

    @Override
    public void onClick(View v) {

        fragmentTransaction = fragmentManager.beginTransaction();
        passedChosenItem = currentChosenItem;
        currentChosenItem = Integer.valueOf((String)v.getTag());


        if(passedChosenItem != currentChosenItem){

            textViewTitle.setText(titles[currentChosenItem]);
            imageViews.get(passedChosenItem).setImageResource(drawableResUnchosen[passedChosenItem]);
            imageViews.get(currentChosenItem).setImageResource(drawableResChosen[currentChosenItem]);
            textViews.get(passedChosenItem).setTextColor(resources.getColor(R.color.colorTitle));
            textViews.get(currentChosenItem).setTextColor(resources.getColor(R.color.colorPrimary));
            if(fragmentManager.findFragmentByTag(fragmentTags[passedChosenItem]) != null){
                fragmentTransaction.hide(fragments.get(passedChosenItem));
            }
            if (fragmentManager.findFragmentByTag(fragmentTags[currentChosenItem]) == null) {
                fragmentTransaction.add(R.id.fragment_container, fragments.get(currentChosenItem), fragmentTags[currentChosenItem]);
            }
            if (fragments.get(currentChosenItem).isHidden()) {
                fragmentTransaction.show(fragments.get(currentChosenItem));
            }
        }

        fragmentTransaction.commit();
    }


}
