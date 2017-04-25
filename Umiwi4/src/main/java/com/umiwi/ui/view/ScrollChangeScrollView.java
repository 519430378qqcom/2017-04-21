package com.umiwi.ui.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;



/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class ScrollChangeScrollView extends ScrollView {
    public interface ScrollViewListener {
        void onScrollChanged(ScrollChangeScrollView scrollView, int x, int y,
                             int oldx, int oldy);
    }

    private ScrollViewListener scrollViewListener = null;

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    public ScrollChangeScrollView(Context context) {
        super(context);
    }

    public ScrollChangeScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ScrollChangeScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScrollChangeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }
}
