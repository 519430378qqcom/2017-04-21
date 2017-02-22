package com.umiwi.ui.fragment.course;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.util.ListViewPositionUtils;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.CourseListAdapter;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.model.CourseListModel;
import com.umiwi.ui.parsers.UmiwiListParser;
import com.umiwi.ui.parsers.UmiwiListResult;

/**
 * @author tangxiyong
 * @version 2015-5-7 下午5:02:21
 */
public class CourseListNoToolbarFragment extends BaseConstantFragment {
	/** 无需拼&p=1 这个参数 */
	public static final String KEY_URL = "key.url";

	private String url;

	public static CourseListNoToolbarFragment newInstance() {
		CourseListNoToolbarFragment courseListFragment = new CourseListNoToolbarFragment();
		return courseListFragment;
	}

	public static CourseListNoToolbarFragment newInstance(String url) {
		CourseListNoToolbarFragment courseListFragment = new CourseListNoToolbarFragment();
		Bundle bundle = new Bundle();
		bundle.putString(KEY_URL, url);
		courseListFragment.setArguments(bundle);
		return courseListFragment;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_URL, url);
	}

	@Override
	public void onActivityCreated(AppCompatActivity a) {
		super.onActivityCreated(a);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			url = savedInstanceState.getString(KEY_URL);
		} else {
			Bundle bundle = getArguments();
			if (bundle != null) {
				url = bundle.getString(KEY_URL);
			}
		}
	}
	
	private ListView mListView;
	private CourseListAdapter mAdapter;
	private ArrayList<CourseListModel> mList;

	private LoadingFooter mLoadingFooter;
	private ListViewScrollLoader mScrollLoader;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_frame_notoolbar_listview_layout, null);

		mList = new ArrayList<CourseListModel>();
		mListView = (ListView) view.findViewById(R.id.listView);
		mAdapter = new CourseListAdapter(getActivity(), mList);

		mLoadingFooter = new LoadingFooter(getActivity());// 加载更多的view
		mListView.addFooterView(mLoadingFooter.getView());

		mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);// 初始化加载更多监听

		mListView.setAdapter(mAdapter);

		mListView.setOnScrollListener(mScrollLoader);// 添加加载更多到listveiw

		mListView.setOnItemClickListener(mTimeLineOnClickListener);

		mScrollLoader.onLoadFirstPage();// 初始化接口，加载第一页

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

	private OnItemClickListener mTimeLineOnClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mListView.clearChoices();
			// listview item的点击容错处理
			CourseListModel item = mList.get(ListViewPositionUtils.indexInDataSource(position, mListView));
			if (ListViewPositionUtils.isPositionCanClick(item, position, mListView, mList)) {
				Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
				intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, item.detailurl);
				startActivity(intent);
			}
		}
	};

	private Listener<UmiwiListResult<CourseListModel>> listener = new Listener<UmiwiListResult<CourseListModel>>() {

		@Override
		public void onResult(AbstractRequest<UmiwiListResult<CourseListModel>> request,
				UmiwiListResult<CourseListModel> t) {// 下面的格式按顺序写，必写
			if (t == null) {// 主要用于防止服务器数据出错
				mScrollLoader.showLoadErrorView("未知错误,请重试");
				return;
			}
			if (t.isLoadsEnd()) {// 判断是否是最后一页
				mScrollLoader.setEnd(true);
			}

			if (t.isEmptyData()) {// 当前列表没有数据
				mScrollLoader.showContentView("你还没有关注");
				return;
			}
			mScrollLoader.setPage(t.getCurrentPage());// 用于分页请求
			mScrollLoader.setloading(false);//

			// 数据加载
			ArrayList<CourseListModel> charts = t.getItems();
			mList.addAll(charts);

			if (mAdapter == null) {
				mAdapter = new CourseListAdapter(getActivity(), mList);
				mListView.setAdapter(mAdapter);// 解析成功 播放列表
			} else {
				mAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void onError(AbstractRequest<UmiwiListResult<CourseListModel>> requet,
				int statusCode, String body) {
			mScrollLoader.showLoadErrorView();
		}

	};
	
	
	private void onClickFooterFinishView() {
		mLoadingFooter.getView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
	}

	@Override
	public void onLoadData(int page) {
		if (TextUtils.isEmpty(url)) {
			mScrollLoader.showContentView("链接错误,请重新打开");
			onClickFooterFinishView();
			return;
		}
		GetRequest<UmiwiListResult<CourseListModel>> request = new GetRequest<UmiwiListResult<CourseListModel>>(
				url + "&p=" + page, UmiwiListParser.class,
				CourseListModel.class, listener);
		request.go();
	}

	

	@Override
	public void onLoadData() {
	}

	@Override
	public void customScrollInChange() {
	}

}
