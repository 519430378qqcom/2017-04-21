package com.umiwi.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class GameVideoView extends VideoView {

	public GameVideoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public GameVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = (int) ( 9.0f / 16 * width); 
	}

}
