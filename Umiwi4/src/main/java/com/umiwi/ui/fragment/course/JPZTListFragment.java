package com.umiwi.ui.fragment.course;

import java.util.ArrayList;

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
import android.widget.ListView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ListViewPositionUtils;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.UmiwiJPZTListAdapter;
import com.umiwi.ui.beans.UmiwiJPZTBeans;
import com.umiwi.ui.beans.UmiwiJPZTBeans.UmiwiJPZTListRequestData;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;

/**
 * 精品专题 列表页
 * 
 * @author tangxiyong 2013-12-9下午2:50:27
 * 
 */
public class JPZTListFragment extends BaseConstantFragment {
	
	/** 无需拼&p=1 这个参数 */
	public static final String KEY_URL = "key.url";
	public static final String KEY_ACTION_TITLE = "key.actiontitle";

	private String url;
	private String actionTitle;
	
	private UmiwiJPZTListAdapter mAdapter;
	private ListView mListView;
	private LoadingFooter mLoadingFooter;
	private ListViewScrollLoader mScrollLoader;

	private ArrayList<UmiwiJPZTBeans> mList;

	private Listener<UmiwiJPZTListRequestData> listener = new Listener<UmiwiJPZTBeans.UmiwiJPZTListRequestData>() {

		@Override
		public void onResult(AbstractRequest<UmiwiJPZTListRequestData> request,
				UmiwiJPZTListRequestData t) {
			if (t != null) {
				if (t.getCurr_page() == t.getPages()) {
					mScrollLoader.setEnd(true);
				}
				ArrayList<UmiwiJPZTBeans> charts = t.getRecord();
				mList.addAll(charts);
				mScrollLoader.setPage(t.getCurr_page());
				mScrollLoader.setloading(false);
				if (mAdapter == null) {
					mAdapter = new UmiwiJPZTListAdapter(getActivity(), mList);
					mListView.setAdapter(mAdapter);
				} else {
					mAdapter.notifyDataSetChanged();
				}
			}
		}

		@Override
		public void onError(AbstractRequest<UmiwiJPZTListRequestData> requet,
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

	@SuppressLint("InflateParams") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_frame_listview_layout, null);
		
		mActionBarToolbar = (Toolbar) contentView.findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(mActionBarToolbar);
		if (TextUtils.isEmpty(actionTitle)) {
			mActionBarToolbar.setVisibility(View.GONE);
		} else {
			setSupportActionBarAndToolbarTitle(mActionBarToolbar, actionTitle);
			mActionBarToolbar.setVisibility(View.VISIBLE);
		}
		
		mList = new ArrayList<UmiwiJPZTBeans>();
		mListView = (ListView) contentView.findViewById(R.id.listView);
		mAdapter = new UmiwiJPZTListAdapter(getActivity(), mList);

		mLoadingFooter = new LoadingFooter(getActivity());
		mListView.addFooterView(mLoadingFooter.getView());
		mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);
		
		mListView.setAdapter(mAdapter);
		mScrollLoader.onLoadFirstPage();

		mListView.setOnScrollListener(mScrollLoader);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				mListView.clearChoices();
				UmiwiJPZTBeans item = mList.get(ListViewPositionUtils.indexInDataSource(position, mListView));
				if (ListViewPositionUtils.isPositionCanClick(item, position, mListView, mList)) {
					Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
					intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, JPZTDetailFragment.class);
					intent.putExtra(JPZTDetailFragment.KEY_URL, item.getDetailurl());
					startActivity(intent);
				}
				MobclickAgent.onEvent(getActivity(), "首页VI", "专题");
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

	public void onLoadData(final int page) {

		if (TextUtils.isEmpty(url)) {
			url = String.format(UmiwiAPI.HOT_LIST_JPZT, page);
		} else {
			url = url + "?&pagenum=5&p=" + page;
		}
		GetRequest<UmiwiJPZTBeans.UmiwiJPZTListRequestData> request = new GetRequest<UmiwiJPZTBeans.UmiwiJPZTListRequestData>(
				url, GsonParser.class,
				UmiwiJPZTBeans.UmiwiJPZTListRequestData.class, listener);
		HttpDispatcher.getInstance().go(request);
	}

}
