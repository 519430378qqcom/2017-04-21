package com.umiwi.ui.adapter;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import cn.youmi.framework.main.ContextProvider;
import cn.youmi.framework.util.ImageLoader;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.MineShakeCouponBean;
import com.umiwi.ui.main.UmiwiApplication;

// 我摇到的adapter
public class MineShakeCouponAdapter extends BaseAdapter {

	private ArrayList<MineShakeCouponBean> coupons;

	public void setCoupons(ArrayList<MineShakeCouponBean> coupons){
		this.coupons = coupons;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return coupons == null ? 0 : coupons.size();
	}

	@Override
	public Object getItem(int position) {
		
		if(coupons != null && coupons.size() > 0) {
			return coupons.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Holder holder = null;
		if(view == null){
			view = LayoutInflater.from(UmiwiApplication.getInstance()).inflate(R.layout.image_layout, null, false);
			holder = new Holder(view);
		}else{
			holder = (Holder) view.getTag();
		}
		MineShakeCouponBean coupon = (MineShakeCouponBean) getItem(position);
		ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
		mImageLoader.loadImage(coupon.getImage(), holder.imageView);
		
		return view;
	}

	private class Holder {
		public ImageView imageView;
		
		public Holder(View view) {
			imageView = (ImageView) view.findViewById(R.id.image_sc);
			view.setTag(this);
		}
	}
}
