package com.umiwi.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import cn.youmi.framework.util.DimensionUtil;

public class AlignViewGroup extends LinearLayout {

	public AlignViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AlignViewGroup(Context context) {
		super(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int width = r - l;
		int g = 12;// 20
		if (!isInEditMode()) {
			g = DimensionUtil.dip2px(1);// 8
		}
		width -= g * 3;
		getChildAt(0).layout(l + 8 * g, 0, g + l + width / 2, b - t - g);
		getChildAt(1).layout(l + 2 * g + width / 2, 0, r - 8 * g, b - t - g);
		// getChildAt(0).layout(l + g, 0, g + l + width / 2, b - t - g);
		// getChildAt(1).layout(l + 2 * g + width / 2, 0, r - g, b - t - g);

	}

//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		int width = MeasureSpec.getSize(widthMeasureSpec);
//		int height = (int) (width * 3.2 / 8);
//		setMeasuredDimension(width, height);
//	}

}
