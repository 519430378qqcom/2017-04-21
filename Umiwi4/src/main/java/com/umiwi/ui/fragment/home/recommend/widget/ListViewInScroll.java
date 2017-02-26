package com.umiwi.ui.fragment.home.recommend.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Gpsi on 2017/2/26.
 */

public class ListViewInScroll extends ListView {

    public ListViewInScroll(Context context) {
        super(context);
    }

    public ListViewInScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}