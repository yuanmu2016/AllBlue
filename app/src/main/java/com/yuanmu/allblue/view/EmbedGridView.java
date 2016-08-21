package com.yuanmu.allblue.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;


public class EmbedGridView extends GridView {
    public EmbedGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmbedGridView(Context context) {
        super(context);
    }

    public EmbedGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}