package com.umiwi.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class VerticallyRepeatedBackgroundLinearLayout extends LinearLayout {
    private int width;
    private RectF mRect;

    public VerticallyRepeatedBackgroundLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public VerticallyRepeatedBackgroundLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VerticallyRepeatedBackgroundLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setWillNotDraw(false);
        mRect = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable bg = getBackground();
        int h = bg.getMinimumHeight();
        int drawWidth = width;
        int drawHeight = h;
        int bottom = 0;
        while (getHeight() < bottom) {
            mRect.set(0, bottom, drawWidth, bottom + drawHeight);
            bg.setBounds(0, bottom, drawWidth, bottom + drawHeight);
            bg.draw(canvas);
            bottom += drawHeight;
        }
    }
}