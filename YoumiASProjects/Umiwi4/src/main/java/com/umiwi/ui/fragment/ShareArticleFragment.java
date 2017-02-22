package com.umiwi.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.share.ShareUtils;
import cn.youmi.share.ShareUtils.ShareModle;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.ShareArticleBeans;
import com.umiwi.ui.beans.ShareArticleBeans.ShareArticleBeansRequestData;

public class ShareArticleFragment extends BaseFragment {
	
	public static final String KEY_URL = "key.url";

	private ImageView shareImage;
	private ProgressBar mProgressBar;
	private TextView shareButton;
	private ImageLoader mImageLoader;

	private String detailUrl;
	private String shareTpye;
	private ShareArticleBeansRequestData shareData;

	@SuppressLint("InflateParams") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView =  inflater.inflate(R.layout.share_article_layout, null);
		mActionBarToolbar = (Toolbar) rootView.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "");
		
		detailUrl = getActivity().getIntent().getStringExtra(KEY_URL);
		
		mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
		shareImage = (ImageView) rootView.findViewById(R.id.share_image);
		shareButton = (TextView) rootView.findViewById(R.id.share_button);
		
		LayoutParams para = shareImage.getLayoutParams();
		para.width = DimensionUtil.getScreenWidth(getActivity());
		para.height = DimensionUtil.getScreenHeight(getActivity());
		shareImage.setLayoutParams(para);
		mImageLoader = new ImageLoader(getActivity());
		
		shareButton.setOnClickListener(new ShareClickListener());
		
		loadData();
		
		
		return rootView;
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

	private class ShareClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (shareTpye.equals("sinaweibo")) {
				ShareUtils.Share(getActivity(), getString(R.string.app_name),
						shareData.getSharecontent(),
						shareData.getSharecontent(), shareData.getShareurl(),
						shareData.getShareimage(), ShareModle.SinaWeibo);
			} else if (shareTpye.equals("weixin")) {
				ShareUtils.Share(getActivity(), getString(R.string.app_name),
						shareData.getSharecontent(),
						shareData.getSharecontent(), shareData.getShareurl(),
						shareData.getShareimage(), ShareModle.WechatMoments);
			} else if (shareTpye.equals("qq")) {
				ShareUtils.Share(getActivity(), getString(R.string.app_name),
						shareData.getSharecontent(),
						shareData.getSharecontent(), shareData.getShareurl(),
						shareData.getShareimage(), ShareModle.QQ);
			} else {
				ShareUtils.Share(getActivity(), getString(R.string.app_name),
						shareData.getSharecontent(),
						shareData.getSharecontent(), shareData.getShareurl(),
						shareData.getShareimage(), ShareModle.WechatMoments);
			}
		}
	}

	private void loadData() {
		GetRequest<ShareArticleBeansRequestData> request = new GetRequest<ShareArticleBeansRequestData>(
				detailUrl, GsonParser.class, ShareArticleBeansRequestData.class,
				shareListener);
		request.go();
	}
	
	private Listener<ShareArticleBeans.ShareArticleBeansRequestData> shareListener = new Listener<ShareArticleBeans.ShareArticleBeansRequestData>() {

		@Override
		public void onResult(
				AbstractRequest<ShareArticleBeansRequestData> request,
				ShareArticleBeansRequestData t) {
			if (t != null) {
				mImageLoader.loadImage(t.getShareimage(), shareImage);
				shareTpye = t.getSharetype();
//				shareContent = t.getSharecontent();
				shareData = t;
				if (!TextUtils.isEmpty(t.getSharebutton())) {
					shareButton.setText(t.getSharebutton());
				}
			}
			mProgressBar.setVisibility(View.GONE);
		}

		@Override
		public void onError(
				AbstractRequest<ShareArticleBeansRequestData> requet,
				int statusCode, String body) {
			// TODO Auto-generated method stub
			mProgressBar.setVisibility(View.GONE);
		}
	};

}
