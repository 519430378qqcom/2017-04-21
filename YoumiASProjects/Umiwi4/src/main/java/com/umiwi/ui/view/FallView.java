package com.umiwi.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class FallView extends View {
	
	private class Particle{
		float x;
		float y;
		public void compute(){
			
		}
	}
	
	public FallView(Context context) {
		super(context);
	}

	public FallView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	
}
