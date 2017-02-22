package com.umiwi.ui.fragment.pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.UmiwiPayOrderCouponBeans;

import java.util.ArrayList;
import java.util.HashMap;

import cn.youmi.framework.util.SingletonFactory;

/**
 * 优惠券列表
 * @author tangxiyong
 * 2014-3-19上午11:19:08
 *
 */
public class PayCouponDialog {
	private ListView mListView;
	private static ArrayList<UmiwiPayOrderCouponBeans> mList;
	private UmiwiPaymentCouponAdapter mListAdapter;
	
	private HashMap<Integer, Integer> map;
	
	private String couponid ="";
	
	private String chooseCouponId;

	public interface OnCouponDialogChooseListener {
		void onCoupongDialogCallback(String name);
	}

	private OnCouponDialogChooseListener couponListener;
	
	public void setCouponChooseListener(OnCouponDialogChooseListener l) {
		this.couponListener = l;
	}
	
	public static PayCouponDialog getInstance() {
		return SingletonFactory.getInstance(PayCouponDialog.class);
	}
	
	public void showDialog(Activity context, ArrayList<UmiwiPayOrderCouponBeans> coupon, String chooseCouponId) {
		this.chooseCouponId = chooseCouponId;
		showDialog(context, coupon);
	}
	
	@SuppressLint("UseSparseArrays") 
	public void showDialog(Activity context, ArrayList<UmiwiPayOrderCouponBeans> coupon) {
		mList = coupon;
		
		map = new HashMap<Integer, Integer>();
		
		int id = 0;
		for (int i = 0; i < coupon.size(); i++) {
			if (coupon.get(i).getId().equals(chooseCouponId)) {
				id = i;
			}
		}
		
		ViewHolder holder = new ViewHolder(R.layout.fragment_frame_notoolbar_listview_layout);
		
		DialogPlus dialogPlus = new DialogPlus.Builder(context)
			.setContentHolder(holder)
			.setGravity(Gravity.BOTTOM)
			.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogPlus dialog) {
					chooseCouponId = "";
					if (couponListener != null) {
						if (null == couponid || "".equals(couponid)) {
							couponListener.onCoupongDialogCallback(mList.get(0).getId());
						}else {
							couponListener.onCoupongDialogCallback(couponid);
						}
					}
				}
			})
			.create();
		
		View view = dialogPlus.getHolderView();
		
		mListView = (ListView) view.findViewById(R.id.listView);
		
		mListAdapter = new UmiwiPaymentCouponAdapter(context, mList);
		mListView.setAdapter(mListAdapter);
		mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//		mListView.setSelected(true);
//		mListView.setSelection(id);
//		mListView.setItemChecked(id, true);
		
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UmiwiPayOrderCouponBeans mListBeans = (UmiwiPayOrderCouponBeans) mListAdapter.getItem(position - mListView.getHeaderViewsCount());
				Holder holder = new Holder(view);
				holder.coupon.setChecked(true);
				map.clear();
				map.put(position, 100);
				mListAdapter.notifyDataSetChanged();
				couponid = mListBeans.getId();
			}});
		map.clear();
		map.put(id, 100);
		mListAdapter.notifyDataSetChanged();
		
		dialogPlus.show();
	}

	
	public class UmiwiPaymentCouponAdapter extends BaseAdapter {

		private LayoutInflater mLayoutInflater;

		private ArrayList<UmiwiPayOrderCouponBeans> mList;
		
		private Holder holder;

		public UmiwiPaymentCouponAdapter(Context context,
				ArrayList<UmiwiPayOrderCouponBeans> mList) {
			super();
			mLayoutInflater = ((Activity) context).getLayoutInflater();
			this.mList = mList;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint({ "InflateParams", "ViewHolder" }) @Override
		public View getView(final int position, View view, ViewGroup parent) {
			view = mLayoutInflater.inflate(R.layout.item_pay_coupon, null);
			holder = getHolder(view);

			final UmiwiPayOrderCouponBeans listBeans = mList.get(position);
			if (mList != null && mList.size() > 0) {
				if (listBeans.getName() != null) {
					holder.coupon.setText(listBeans.getName());
				}
			} else {
				throw new IllegalStateException(view.getClass().getName()
						+ "data is null or to is null");
			}
			
			holder.coupon.setChecked(map.get(position) == null ? false : true);
			return view;
		}

		private Holder getHolder(final View view) {
			Holder holder = (Holder) view.getTag();
			if (holder == null) {
				holder = new Holder(view);
				view.setTag(holder);
			}
			return holder;
		}

		
	}
	class Holder {
		public RadioButton coupon;
		public Holder(View view) {
			coupon = (RadioButton) view.findViewById(R.id.tv_payment_coupon_i);
		}
	}
}
