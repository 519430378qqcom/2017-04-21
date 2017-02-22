package com.umiwi.ui.fragment.course;

import java.util.ArrayList;

import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;
import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView.OnItemClickListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.ChatsListAdapter;
import com.umiwi.ui.beans.ChartsListBean;
import com.umiwi.ui.beans.ChartsListBean.RecentChangeRequestData;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;

/**
 * 
 * @author tangxiyong
 * @version 2015-6-4 下午4:14:17
 * 榜单
 */
public class ChartsListFragment extends BaseConstantFragment {
	public static final String KEY_URL = "key.url";
	public static final String KEY_ACTION_TITLE = "key.actiontitle";
	
	private String url;
	private String actionTitle;

	private LoadingFooter mLoadingFooter;
	private View footerView;
	private PinnedHeaderListView recentchangeListView;
	private ChatsListAdapter mListAdapter;
	private ChartsListBean.RecentChangeWrapper recentChangeWrapper;
	private ListViewScrollLoader mScrollLoader;
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_URL, url);
		outState.putString(KEY_ACTION_TITLE, KEY_ACTION_TITLE);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			url = savedInstanceState.getString(KEY_URL);
			actionTitle = savedInstanceState.getString(KEY_ACTION_TITLE);
		} else {
			Bundle bundle = getArguments();
			if (bundle != null) {
				url = bundle.getString(KEY_URL);
				actionTitle = bundle.getString(KEY_ACTION_TITLE);
			}
		}
	}

	@SuppressLint("InflateParams") 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_frame_pinnedheaderlistview_layout, null);
		
		mActionBarToolbar = (Toolbar) contentView.findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(mActionBarToolbar);
		if (TextUtils.isEmpty(actionTitle)) {
			mActionBarToolbar.setVisibility(View.GONE);
		} else {
			setSupportActionBarAndToolbarTitle(mActionBarToolbar, actionTitle);
			mActionBarToolbar.setVisibility(View.VISIBLE);
		}

		recentchangeListView = (PinnedHeaderListView) contentView.findViewById(R.id.listView);

		mLoadingFooter = new LoadingFooter(getActivity());
		footerView = mLoadingFooter.getView();
		footerView.setVisibility(View.GONE);

		recentchangeListView.addFooterView(footerView);
		mListAdapter = new ChatsListAdapter(getActivity());

		recentchangeListView.setAdapter(mListAdapter);


		mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);
		recentchangeListView.setOnScrollListener(mScrollLoader);
		
		mScrollLoader.onLoadFirstPage();
		
		recentchangeListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int section, int position, long id) {
				ArrayList<ChartsListBean> recentChanges = null;
				if (recentChangeWrapper != null) {
					switch (section) {
					case 0:
						recentChanges = recentChangeWrapper.getDay();
						break;
					case 1:
						recentChanges = recentChangeWrapper.getWeek();
						break;
					case 2:
						recentChanges = recentChangeWrapper.getMonth();
						break;
					default:
						break;
					}

					if (recentChanges != null
							&& recentChanges.size() > position) {
						ChartsListBean recentChange = recentChanges.get(position);
						Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
						intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
						intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, recentChange.getDetailurl());
						startActivity(intent);
					}
				}

			}

			@Override
			public void onSectionClick(AdapterView<?> adapterView, View view,
					int section, long id) {

			}
		});
		return contentView;
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

	private Listener<RecentChangeRequestData> listener = new Listener<ChartsListBean.RecentChangeRequestData>() {

		@Override
		public void onResult(AbstractRequest<RecentChangeRequestData> request,
				RecentChangeRequestData t) {
			
			if (t != null) {

				ChartsListBean.RecentChangeWrapper newRecentChangeWrapper = t.getRecord();
				if (recentChangeWrapper == null) {
					recentChangeWrapper = newRecentChangeWrapper;
				} else {
					recentChangeWrapper.setWrapper(newRecentChangeWrapper);
				}

				mListAdapter.setDatas(recentChangeWrapper);

				mLoadingFooter.setState(LoadingFooter.State.TheEnd);
			
			}
		}

		@Override
		public void onError(AbstractRequest<RecentChangeRequestData> requet,
				int statusCode, String body) {
			mLoadingFooter.setState(LoadingFooter.State.Error);
			mLoadingFooter.getView().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					mScrollLoader.onLoadErrorPage();
				}
			});

		}
	};

	public void onLoadData() {

		if (TextUtils.isEmpty(url)) {
			url = UmiwiAPI.RANK_LIST;
		}
		
		GetRequest<ChartsListBean.RecentChangeRequestData> request = new GetRequest<ChartsListBean.RecentChangeRequestData>(
				url, GsonParser.class,
				ChartsListBean.RecentChangeRequestData.class, listener);
		HttpDispatcher.getInstance().go(request);
	}
}
