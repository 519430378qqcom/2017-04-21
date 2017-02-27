package com.umiwi.ui.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.umiwi.ui.beans.LecturerBean.LecturerBeanWapper;
import com.umiwi.ui.fragment.LecturerListFragment;
import com.umiwi.ui.fragment.LecturerListFragment.LastnameHandler;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.ExpertFragment;


public class MyLetterListView extends View {
		private LastnameHandler lastnameHandler;
	
	OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	
	ArrayList<String> b;
	
	boolean showBkg = false;
	int choose = -1;
	Paint paint = new Paint();
	

	ArrayList<LecturerBeanWapper> lecturerWapper;
	
	public MyLetterListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyLetterListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyLetterListView(Context context) {
		super(context);
	}
	
	public void setData(ArrayList<LecturerBeanWapper> lecturerWapper) {
		this.lecturerWapper = lecturerWapper;
		parseLetters(lecturerWapper);
	}
	
	private void parseLetters(ArrayList<LecturerBeanWapper> lecturerWappers) {
		if(lecturerWappers != null) {
			this.b = new ArrayList<String>(lecturerWappers.size()); 
			
			for(int i=0; i<lecturerWappers.size(); i++) {
				LecturerBeanWapper lecturerWapper = lecturerWappers.get(i);
				String lastName = lecturerWapper.getLastName();
				b.add(lastName);
			}
		}
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(showBkg){
			/*Color color = getContext().getResources().getColor(R.color.umiwi_gray_e);
		    canvas.drawColor(Color.parseColor("#40000000"));*/
			//canvas.drawColor(getContext().getResources().getColor(R.color.umiwi_gray_e));
		}
		
	    int height = getHeight();
	    int width = getWidth();
	    
	    if(b != null && b.size() > 0) {
		    int singleHeight = height / b.size();
		    for(int i=0; i<b.size(); i++){
		       paint.setColor(Color.GRAY);
		       paint.setTextSize(25.0f);
		       paint.setTypeface(Typeface.DEFAULT_BOLD);
		       paint.setAntiAlias(true);
		       if(i == choose){
		    	   paint.setColor(Color.parseColor("#3399ff"));
		    	   paint.setFakeBoldText(true);
		       }
		       float xPos = width/2  - paint.measureText(b.get(i))/2;
		       float yPos = singleHeight * i + singleHeight;
		       canvas.drawText(b.get(i), xPos, yPos, paint);
		       paint.reset();
		    }
	    }
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
	    final float y = event.getY();
	    final int oldChoose = choose;
	    final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
	    final int c = (int) (y/getHeight()*b.size());
	    
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				showBkg = true;
				if(listener != null){
					if(b != null && c >= 0 && c < b.size()){
						listener.onTouchingLetterChanged(b.get(c), c+"");
						
						if(lastnameHandler != null) {
							Message msgShowLastname = lastnameHandler.obtainMessage(LecturerListFragment.POP_SHOW);
							msgShowLastname.obj = b.get(c);
							msgShowLastname.arg1 = (int) event.getRawY();
							/*Bundle data = new Bundle();
							data.putFloat("distanceY", event.getY());
							msgShowLastname.setData(data); */ 
							lastnameHandler.sendMessage(msgShowLastname);
						}
						
						choose = c;
						invalidate();
					}
				}
				
				break;
			case MotionEvent.ACTION_MOVE:
				if(b != null && oldChoose != c && listener != null){
					if(c >= 0 && c< b.size()){
						listener.onTouchingLetterChanged(b.get(c), c+"");
						
						choose = c;
						
						if(lastnameHandler != null) {
							Message msgLastnameMove = lastnameHandler.obtainMessage(LecturerListFragment.POP_MOVE);
							msgLastnameMove.obj = b.get(c);  
							msgLastnameMove.arg1 = (int) event.getY();
							lastnameHandler.sendMessage(msgLastnameMove);
						}
						
						invalidate();
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				showBkg = false;
				Message msgDismissLastname = lastnameHandler.obtainMessage(LecturerListFragment.POP_DISMISS);
				lastnameHandler.sendMessage(msgDismissLastname);
				choose = -1;
				invalidate();
				break;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public void setHandler(ExpertFragment.LastnameHandler mHandler) {

	}

	public interface OnTouchingLetterChangedListener{
		void onTouchingLetterChanged(String s, String order);
	}

	
	public void setHandler(LastnameHandler mHandler) {
		this.lastnameHandler = mHandler;
	}
	
}
