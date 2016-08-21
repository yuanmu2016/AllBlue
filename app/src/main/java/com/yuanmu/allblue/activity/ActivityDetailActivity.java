package com.yuanmu.allblue.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yuanmu.allblue.R;

import java.util.List;


public class ActivityDetailActivity extends AppCompatActivity {

    public static final String ARGS = "com.yuanmu.allblue.activity.ActivityDetailActivity.ARGS";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_activity);

        final List<String> list = getIntent().getStringArrayListExtra(ARGS);

        ImageView imageView = (ImageView) findViewById(R.id.background);
        Picasso.with(this).load(list.get(list.size()-2)).placeholder(R.drawable.bcg_image_view).into(imageView);
       // Glide.with(this).load(list.get(list.size() - 1)).placeholder(R.drawable.bcg_image_view).into(imageView);


        TextView textViewTitle = (TextView) findViewById(R.id.text_title);
        textViewTitle.setText(list.get(0));
        TextView textViewTime = (TextView) findViewById(R.id.text_time);
        textViewTime.setText("时间："+list.get(1));
        TextView textViewPlace = (TextView) findViewById(R.id.text_place);
        textViewPlace.setText("地点："+list.get(2));
        TextView textViewPrice = (TextView) findViewById(R.id.text_price);
        textViewPrice.setText("费用："+list.get(3));
        TextView textViewContent = (TextView) findViewById(R.id.text_content);
        textViewContent.setText(list.get(4));


        View view = findViewById(R.id.layout_map);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityDetailActivity.this,MapActivity.class);
                intent.putExtra(MapActivity.LATLNG,list.get(list.size()-1));
                intent.putExtra(MapActivity.TITLE,list.get(2));
                startActivity(intent);
            }
        });


    }
}
