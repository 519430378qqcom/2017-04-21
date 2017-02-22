package com.umiwi.ui.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.ImageLoader;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.GiftListAdapter;
import com.umiwi.ui.beans.GiftListBeans;
import com.umiwi.ui.beans.GiftListBeans.GiftListRequestData;
import com.umiwi.ui.fragment.course.CourseListFragment;
import com.umiwi.ui.fragment.mine.MyCouponFragment;
import com.umiwi.ui.main.UmiwiAPI;

/**
 * 优惠大礼包 领后列表
 * 
 * @author tangxiyong
 * @version 2014年9月16日 下午2:03:52
 */
public class GiftListFragment extends BaseFragment {

	public static final String EXTRA_CATEGORY = "CHARTS_POSITION";
	private String giftUrl;

	private ImageView giftListBackground;
	private ImageView giftListHeader;

	private ArrayList<GiftListBeans> mList;
	private GiftListAdapter mAdapter;
	private ListView mListView;

	public static GiftListFragment newInstance(String url) {
		GiftListFragment f = new GiftListFragment();
		Bundle b = new Bundle();
		b.putString(EXTRA_CATEGORY, url);
		f.setArguments(b);
		return f;
	}

	@SuppressLint("InflateParams") 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_gift_list_layout, null);
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "大礼包");
		
		mImageLoader = new ImageLoader(getActivity());

		giftListBackground = (ImageView) view.findViewById(R.id.gift_list_background_imageview);
		giftListHeader = (ImageView) view.findViewById(R.id.gift_list_header_imageview);
		
		LayoutParams para = giftListHeader.getLayoutParams();
		para.width = DimensionUtil.getScreenWidth(getActivity());
		para.height = (para.width * 382) / 800;
		giftListHeader.setLayoutParams(para);
		
//		Bundle b = getArguments();
		giftUrl = getActivity().getIntent().getStringExtra(EXTRA_CATEGORY);

		mList = new ArrayList<GiftListBeans>();
		mListView = (ListView) view.findViewById(R.id.listView);
		mAdapter = new GiftListAdapter(getActivity(), mList);
		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = null;
				GiftListBeans mListBeans = mList.get(position - mListView.getFooterViewsCount());
            	if ("coupon".equals(mListBeans.getType())) {
            		intent  = new Intent(getActivity(), UmiwiContainerActivity.class);
            		intent .putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, MyCouponFragment.class);
				} else if ("album".equals(mListBeans.getType())) {
					intent = new Intent(getActivity(), UmiwiContainerActivity.class);
					intent.putExtra(CourseListFragment.KEY_URL, UmiwiAPI.VIDEO_VIP_MYCOURSE);
					intent.putExtra(CourseListFragment.KEY_ACTION_TITLE, "我的课程");
					intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseListFragment.class);
				} else {

				}
            	startActivity(intent);
			}
		});

		getGiftListData(giftUrl);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(fragmentName);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(fragmentName);
	}

	private void getGiftListData(String url) {
		GetRequest<GiftListBeans.GiftListRequestData> request = new GetRequest<GiftListBeans.GiftListRequestData>(
				url, GsonParser.class, GiftListBeans.GiftListRequestData.class,
				giftListener);
		request.go();
	}

	private Listener<GiftListBeans.GiftListRequestData> giftListener = new Listener<GiftListBeans.GiftListRequestData>() {

		@Override
		public void onResult(AbstractRequest<GiftListRequestData> request,
				GiftListRequestData t) {
			mImageLoader.loadImage(t.getImagelistbackground(), giftListBackground);
			mImageLoader.loadImage(t.getImageheader(), giftListHeader);
			
			ArrayList<GiftListBeans> charts = t.getGiftlist();
			mList.addAll(charts);
			if (mAdapter == null) {
				mAdapter = new GiftListAdapter(getActivity(), mList);
				mListView.setAdapter(mAdapter);
			} else {
				mAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void onError(AbstractRequest<GiftListRequestData> requet,
				int statusCode, String body) {
			// TODO Auto-generated method stub

		}
	};
	private ImageLoader mImageLoader;

}
