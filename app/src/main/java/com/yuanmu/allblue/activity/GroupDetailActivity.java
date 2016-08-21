package com.yuanmu.allblue.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuanmu.allblue.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupDetailActivity extends AppCompatActivity {

    public static String CONTENT = "com.yuanmu.allblue.activity.GroupDetailActivity.CONTENT";
    public static String SEQ_ID = "com.yuanmu.allblue.SEQ_ID.GroupDetailActivity.SEQ_ID";
    public static String ALT = "com.yuanmu.allblue.ALT.GroupDetailActivity.ALT";
    public static String TITLE = "com.yuanmu.allblue.activity.GroupDetailActivity.TITLE";
    private int imgIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;


        final String title = getIntent().getStringExtra(TITLE);
        final String content = getIntent().getStringExtra(CONTENT);;
        List<String> seqs = getIntent().getStringArrayListExtra(SEQ_ID);
        final List<String> alts = getIntent().getStringArrayListExtra(ALT);

        HashMap<String,String> hashMap = new HashMap<>();
        for (int k = 0 ; k<seqs.size();k++){
            hashMap.put(seqs.get(k),alts.get(k));
        }

        List<String> texts = new ArrayList<>();
        List<String> seqsReal = new ArrayList<>();

        char[] chars = content.toCharArray();
        int i = 0;
        int j = 0;
        for(; i < chars.length ;){
            if(chars[i] == '<' && chars[i + 1] == '图' && chars[i+2] == '片'){
                String text;
                if(i == j){
                    text = "";
                    if(Character.isDigit(chars[i+3]) && Character.isDigit(chars[i+4]) && chars[i+5] == '>'){
                        seqsReal.add(String.valueOf(content.substring(i+3,i+5)));
                        i=i+6;
                        j=j+6;
                    }else if(Character.isDigit(chars[i+3])  && chars[i+4] =='>'){
                        char seq_id = chars[i+3];
                        seqsReal.add(String.valueOf(seq_id));
                        j = j + 5;
                        i = i+ 5;
                    }
                } else {
                    text = content.substring(j,i);
                    j = i;
                }
                texts.add(text);
            } else {
                i = i + 1;
                if(i == chars.length){
                    String text = content.substring(j,i);
                    texts.add(text);
                }
            }
        }

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textViewTitle = new TextView(this);
        textViewTitle.setTextSize(20);
        textViewTitle.setLineSpacing(0,(float)1.5);
        textViewTitle.setTextColor(getResources().getColor(R.color.colorTitle));
        int padding = (int)(8 * density);
        textViewTitle.setPadding(padding,padding,padding,0);
        textViewTitle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textViewTitle.setText(title);
        linearLayout.addView(textViewTitle);

        final ArrayList<String> imageUrls = new ArrayList<>();
        imgIndex = 0;
        for(String s : texts){
            if(s.length() == 0){
                final ImageView imageView = new ImageView(this);
                imageView.setId(imgIndex);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(300*density));
                params.setMargins(0,padding,0,0);
                imageView.setLayoutParams(params);
                String alt = hashMap.get(seqsReal.get(imgIndex));
                imageUrls.add(alt);
                linearLayout.addView(imageView);
                Glide.with(this).load(alt).placeholder(R.drawable.bcg_image_view).centerCrop().into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GroupDetailActivity.this,ImageDisplayActivity.class);
                        intent.putExtra(ImageDisplayActivity.IMAGE_INDEX,imageView.getId());
                        intent.putExtra(ImageDisplayActivity.PHOTOBEAN_LIST,imageUrls);
                        startActivity(intent);
                    }
                });
                imgIndex++;
            }else {
                TextView textView = new TextView(this);
                textView.setTextSize(14);
                textView.setLineSpacing(0,(float)1.5);
                textView.setTextColor(getResources().getColor(R.color.colorTitle));
                textView.setPadding(padding,padding,padding,0);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setText(s);
                linearLayout.addView(textView);
            }
        }
        scrollView.addView(linearLayout);
        findViewById(R.id.navigation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
