package com.umiwi.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 用于与sorllview 结合的listview
 * 
 * @author tangxiyong 2013-11-21下午6:21:59
 * 
 */
public class MyListView extends ListView {
	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setItemsCanFocus(boolean itemsCanFocus) {
		super.setItemsCanFocus(itemsCanFocus);
	}

	/**
	 * 设置不滚动
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}

}
