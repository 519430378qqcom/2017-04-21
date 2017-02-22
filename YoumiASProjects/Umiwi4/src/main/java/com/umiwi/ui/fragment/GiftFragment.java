package com.umiwi.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.beans.GiftBeans;
import com.umiwi.ui.beans.GiftBeans.GiftRequestData;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.CommonHelper;
import com.umiwi.ui.util.LoginUtil;
import com.umiwi.ui.util.ManifestUtils;

import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.ImageLoader;

/**
 * 优惠大礼包
 * 
 * @author tangxiyong
 * @version 2014年9月16日 下午2:03:52
 */
public class GiftFragment extends BaseFragment {
	
	private ImageView giftBackground;
	private ImageView giftCommit;
	private ImageView giftBox;
	private String giftUrl;

	@SuppressLint("InflateParams") 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_gift_show_layout, null);
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "大礼包");
		
		mImageLoader = new ImageLoader(getActivity());
		getGiftData();
		giftBackground = (ImageView) view.findViewById(R.id.gift_background_imageview);
		giftBox = (ImageView) view.findViewById(R.id.gift_show_box_imageview);
		giftCommit = (ImageView) view.findViewById(R.id.gift_commit_imageview);
		
		LayoutParams para = giftCommit.getLayoutParams();
		para.width = DimensionUtil.getScreenWidth(getActivity());
		para.height = (para.width * 153) / 800;
		giftCommit.setLayoutParams(para);
		
		giftCommit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (YoumiRoomUserManager.getInstance().isLogin()) {
					Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
					i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, GiftListFragment.class);
					i.putExtra(GiftListFragment.EXTRA_CATEGORY, giftUrl);
					startActivity(i);
				} else {
					LoginUtil.getInstance().showLoginView(getActivity());
				}
				
			}
		});
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
	
	private void getGiftData() {
		String ad_str;
		try {
			ad_str = String
					.format(UmiwiAPI.APP_GIFT,
							CommonHelper.getVersionName(),
							CommonHelper.getMacMD5(),
							ManifestUtils.getChannelString(getActivity()));
			GetRequest<GiftBeans.GiftRequestData> request = new GetRequest<GiftBeans.GiftRequestData>(
					ad_str, GsonParser.class, GiftBeans.GiftRequestData.class,
					giftListener);
			request.go();
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Listener<GiftBeans.GiftRequestData> giftListener = new Listener<GiftBeans.GiftRequestData>() {

		@Override
		public void onResult(AbstractRequest<GiftRequestData> request,
				GiftRequestData t) {
			giftUrl = t.getGiftUrl();
			mImageLoader.loadImage(t.getImagebackground(), giftBackground);
			mImageLoader.loadImage(t.getImagebox(), giftBox);
			mImageLoader.loadImage(t.getImagecommit(), giftCommit);
		}

		@Override
		public void onError(AbstractRequest<GiftRequestData> requet,
				int statusCode, String body) {

		}
	};
	private ImageLoader mImageLoader;
	
}
