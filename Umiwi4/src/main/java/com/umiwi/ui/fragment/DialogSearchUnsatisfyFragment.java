package com.umiwi.ui.fragment;

import java.lang.reflect.Field;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.PostRequest;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.beans.FeedbackResultBean;
import com.umiwi.ui.http.parsers.SearchFeedbackParser;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.util.CommonHelper;

/**
 * 搜索不满意
 * 
 * @author tjie00 2014-06-14
 * 
 */
public class DialogSearchUnsatisfyFragment extends DialogFragment {

	private ImageView ivCancel;
	private TextView tvUnsatisfy;
	private View view;
	public static String keyWord; 
	public FragmentActivity parent;
	public static final String TAG = "DialogSearchUnsatisfyFragment";

	/*public static void show(FragmentActivity activity, String keyword) {
		new DialogSearchUnsatisfyFragment().show(activity.getSupportFragmentManager(), TAG);
		keyWord = keyword;
	}*/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("DialogSearchUnsatisfyFragment");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("DialogSearchUnsatisfyFragment");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = LayoutInflater.from(getActivity()).inflate(
				R.layout.search_unsatisfy_fragment, null);
		
		searchFeedbackFragment = getSearchFeedbackFragment();
		
		ivCancel = (ImageView) view.findViewById(R.id.iv_cancel);
		
		tvUnsatisfy = (TextView) view.findViewById(R.id.tv_unsatisfy);
		
		ivCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				commitFeedback(keyWord);
				dismiss();
			}
		});
		
		tvUnsatisfy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
				i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, SearchFeedbackFragment.class);
				i.putExtra(SearchFeedbackFragment.EXTRA_CATEGORY, keyWord);
				startActivity(i);
				dismiss();
			}
		});
		
		return view;
	}
	
	public SearchFeedbackFragment getSearchFeedbackFragment() {
		SearchFeedbackFragment fragment = ((SearchFeedbackFragment) getChildFragmentManager()
				.findFragmentByTag(SearchFeedbackFragment.class.getName()));
		if (fragment == null) {
			fragment = new SearchFeedbackFragment();
			fragment.setArguments(new Bundle());
		}
		return fragment;
	}

	private void commitFeedback(String keyword) {
		PostRequest<FeedbackResultBean.FeedbackResultRequestData> request = new PostRequest<FeedbackResultBean.FeedbackResultRequestData>(UmiwiAPI.FEEDBACK_URL, SearchFeedbackParser.class, listener);
		request.addParam("content", "搜索反馈内容为空");
		request.addParam("option", "搜索："+keyword);
		request.addParam("app", "android");
		request.addParam("system", Build.VERSION.RELEASE);
		request.addParam("deviceid", CommonHelper.getMacMD5());//TODO
		request.addParam("model", Build.MODEL);
		try {
			request.addParam("version", CommonHelper.getVersionName());
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		request.go();
	}
	
	private Listener<FeedbackResultBean.FeedbackResultRequestData> listener = new Listener<FeedbackResultBean.FeedbackResultRequestData>() {
		@Override
		public void onResult(AbstractRequest<FeedbackResultBean.FeedbackResultRequestData> request, FeedbackResultBean.FeedbackResultRequestData t) {
		}

		@Override
		public void onError(AbstractRequest<FeedbackResultBean.FeedbackResultRequestData> requet, int statusCode,
				String body) {
		}
	};
	private SearchFeedbackFragment searchFeedbackFragment;

	public void setSearchKeyWord(String autoTv_hot_word) {
		keyWord = autoTv_hot_word;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		
		try {  
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");  
            childFragmentManager.setAccessible(true);  
            childFragmentManager.set(this, null);  
  
        } catch (NoSuchFieldException e) {  
            throw new RuntimeException(e);  
        } catch (IllegalAccessException e) {  
            throw new RuntimeException(e);  
        }  
	}
	
	
	
}
