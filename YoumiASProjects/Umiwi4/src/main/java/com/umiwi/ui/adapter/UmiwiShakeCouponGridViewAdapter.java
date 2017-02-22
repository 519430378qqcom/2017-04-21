package com.umiwi.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;

/**
 * Shake coupon content gridview's adapter 
 * @author tjie00	
 * 2014/02/26 
 *
 */
public class UmiwiShakeCouponGridViewAdapter extends BaseAdapter {

	private ArrayList<String> shakeCouponBeanURLs;
	private Context context;
//	private DisplayMetrics metrics;
	
	public UmiwiShakeCouponGridViewAdapter(Context context,
			ArrayList<String> shakeCouponBeanURLs) {
		this.shakeCouponBeanURLs = shakeCouponBeanURLs;
		this.context = context;
		
//		Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
//		metrics = new DisplayMetrics();
//		display.getMetrics(metrics);
	}
	
	public void setData(ArrayList<String> shakeCouponBeanURLs) {
		this.shakeCouponBeanURLs = shakeCouponBeanURLs;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(shakeCouponBeanURLs != null && shakeCouponBeanURLs.size() > 0)
			return shakeCouponBeanURLs.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return shakeCouponBeanURLs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		/*if(view == null) {*/
			holder = new ViewHolder();
		//ViewHolder viewHolder = new ViewHolder();
			
			view = new ImageView(context);
			
			int width = parent.getWidth()/3-10;
			int height = width*2/3;
			GridView.LayoutParams layoutParam = new GridView.LayoutParams(width, height);
	        view.setLayoutParams(layoutParam);
	        
	        holder.imageView = (ImageView) view;
	        //viewHolder.imageView.setScaleType(ScaleType.FIT_XY);
	        //viewHolder.imageView.setScaleType(ScaleType.FIT_START);
	        
	        /*view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}*/
		
		final String shakeCouponBeanURL = shakeCouponBeanURLs.get(position);
		
		ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
		mImageLoader.loadImage(shakeCouponBeanURL, holder.imageView);
		
		return view;
	}
	
	private static class ViewHolder {
		ImageView imageView;
	}

}
