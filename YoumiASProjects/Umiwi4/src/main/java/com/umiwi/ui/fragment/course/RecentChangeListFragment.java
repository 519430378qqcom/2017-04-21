package com.umiwi.ui.fragment.course;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.RecentUpdatesListAdapter;
import com.umiwi.ui.beans.RecentChange;
import com.umiwi.ui.beans.RecentChange.RecentChangeRequestData;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;
import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;
import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView.OnItemClickListener;

/**
 * 最近更新列表页面
 * 
 * @author tjie00
 * @since v4.3
 * @version v4.3 2014\04\15
 * 
 */
public class RecentChangeListFragment extends BaseConstantFragment {

	private String recentchangeStr;
	private LoadingFooter mLoadingFooter;
	private View footerView;
	private PinnedHeaderListView recentchangeListView;
	private RecentUpdatesListAdapter mListAdapter;
	private RecentChange.RecentChangeWrapper recentChangeWrapper;
	private ListViewScrollLoader mScrollLoader;

	public static RecentChangeListFragment newInstance() {
		RecentChangeListFragment f = new RecentChangeListFragment();
		return f;
	}

	@SuppressLint("InflateParams") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_frame_pinnedheaderlistview_layout, null);
		mActionBarToolbar = (Toolbar) contentView.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "最近更新");
		recentchangeListView = (PinnedHeaderListView) contentView.findViewById(R.id.listView);

		mLoadingFooter = new LoadingFooter(getActivity());
		footerView = mLoadingFooter.getView();
		footerView.setVisibility(View.GONE);

		recentchangeListView.addFooterView(footerView);
		mListAdapter = new RecentUpdatesListAdapter(getActivity());

		recentchangeListView.setAdapter(mListAdapter);


		mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);
		recentchangeListView.setOnScrollListener(mScrollLoader);
		
		mScrollLoader.onLoadFirstPage();
		
		recentchangeListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int section, int position, long id) {
				ArrayList<RecentChange> recentChanges = null;
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
					case 3:
						recentChanges = recentChangeWrapper.getMore();
						break;
					default:
						break;
					}

					if (recentChanges != null
							&& recentChanges.size() > position) {
						RecentChange recentChange = recentChanges.get(position);
						Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
						intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
						intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, recentChange.getDetailurl());
						startActivity(intent);
						MobclickAgent.onEvent(getActivity(), "首页VI", "最新");
					}
				}

			}

			@Override
			public void onSectionClick(AdapterView<?> adapterView, View view,
					int section, long id) {

			}
		});
//		recentchangeListView.smoothScrollToPosition(0);
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

	private Listener<RecentChangeRequestData> listener = new Listener<RecentChange.RecentChangeRequestData>() {

		@Override
		public void onResult(AbstractRequest<RecentChangeRequestData> request,
				RecentChangeRequestData t) {
			
			if (t != null) {
				if (t.getCurr_page() == t.getPages()) {
					mScrollLoader.setEnd(true);
				}

				RecentChange.RecentChangeWrapper newRecentChangeWrapper = t.getRecord();
				if (recentChangeWrapper == null) {
					recentChangeWrapper = newRecentChangeWrapper;
				} else {
					recentChangeWrapper.setWrapper(newRecentChangeWrapper);
				}

				mListAdapter.setDatas(recentChangeWrapper);

				mScrollLoader.setPage(t.getCurr_page());
				mScrollLoader.setloading(false);
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

	public void onLoadData(final int page) {

		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyymmdd");
		String dateStr = format.format(date);
		recentchangeStr = String.format(UmiwiAPI.RECENTCHANGE_LIST, page,
				dateStr);

		GetRequest<RecentChange.RecentChangeRequestData> request = new GetRequest<RecentChange.RecentChangeRequestData>(
				recentchangeStr, GsonParser.class,
				RecentChange.RecentChangeRequestData.class, listener);
		HttpDispatcher.getInstance().go(request);
	}
}
