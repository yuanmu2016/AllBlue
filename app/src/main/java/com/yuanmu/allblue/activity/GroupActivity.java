package com.yuanmu.allblue.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yuanmu.allblue.R;
import com.yuanmu.allblue.fragment.group_fragment.GroupFragment;


public class GroupActivity extends AppCompatActivity {

    public static final String GROUP_ID = "com.yuanmu.allblue.activity.GroupActivity.GROUP_ID";
    public static final String CACHEDIR_NAME = "com.yuanmu.allblue.activity.GroupActivity.CACHEDIR_NAME";
    public static final String PREFERENCES = "com.yuanmu.allblue.activity.GroupActivity.PREFERENCES";

    public static void newInstance(Context context,String groupId,String cacheDirName,String preferencesKeyOfFragment){
        Intent intent = new Intent(context,GroupActivity.class);
        intent.putExtra(GROUP_ID,groupId);
        intent.putExtra(CACHEDIR_NAME,cacheDirName);
        intent.putExtra(PREFERENCES,preferencesKeyOfFragment);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        String groupId = getIntent().getStringExtra(GROUP_ID);


        TextView textViewTitle = (TextView) findViewById(R.id.text_title);
        String cacheDirName = getIntent().getStringExtra(CACHEDIR_NAME);
        textViewTitle.setText(cacheDirName);

        String preferencesKeyOfFragment = getIntent().getStringExtra(PREFERENCES);

        FragmentManager fragmentManager =getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragment = GroupFragment.newInstance(groupId,cacheDirName,preferencesKeyOfFragment);
            fragmentManager.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }

    }
}
