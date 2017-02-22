package com.umiwi.ui.fragment.mine;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.MineNoteListAdapter;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.model.CourseListModel;
import com.umiwi.ui.parsers.UmiwiListParser;
import com.umiwi.ui.parsers.UmiwiListResult;

/**
 * 我的笔记
 * 
 * @author tangxiong
 * @version 2014年6月13日 下午7:05:30
 */
public class MyNoteFragment extends BaseConstantFragment {

	private MineNoteListAdapter mAdapter;

	private ListView mListView;

	private ListViewScrollLoader mScrollLoader;
	private LoadingFooter mLoadingFooter;

	private ArrayList<CourseListModel> mList;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_frame_listview_layout, null);
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "我的笔记");
		mList = new ArrayList<CourseListModel>();

		mListView = (ListView) view.findViewById(R.id.listView);
		mAdapter = new MineNoteListAdapter(getActivity(), mList);
		mLoadingFooter = new LoadingFooter(getActivity());
		mListView.addFooterView(mLoadingFooter.getView());

		mListView.setAdapter(mAdapter);
		mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);

		mListView.setOnScrollListener(mScrollLoader);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CourseListModel mListBeans = (CourseListModel) mAdapter
						.getItem(position - mListView.getHeaderViewsCount());

				UmiwiContainerActivity ca = (UmiwiContainerActivity) getActivity();
				MyNoteDetailFragment noteDetailFragment = new MyNoteDetailFragment();
				noteDetailFragment.noteList = mListBeans;
				ca.getNavigationController().pushFragment(noteDetailFragment);
			}
		});

		mScrollLoader.onLoadFirstPage();

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

	@Override
	public void onLoadData(int page) {
		super.onLoadData(page);
		String url = String.format(UmiwiAPI.MINE_NOTE, page);
		GetRequest<UmiwiListResult<CourseListModel>> request = new GetRequest<UmiwiListResult<CourseListModel>>(
				url, UmiwiListParser.class, CourseListModel.class, listener);
		request.go();

	}

	private Listener<UmiwiListResult<CourseListModel>> listener = new Listener<UmiwiListResult<CourseListModel>>() {

		@Override
		public void onResult(
				AbstractRequest<UmiwiListResult<CourseListModel>> request,
				UmiwiListResult<CourseListModel> t) {
			if (t == null) {// 主要用于防止服务器数据出错
				mScrollLoader.showLoadErrorView("未知错误,请重试");
				return;
			}
			if (t.isLoadsEnd()) {// 判断是否是最后一页
				mScrollLoader.setEnd(true);
			}

			if (t.isEmptyData()) {// 当前列表没有数据
				mScrollLoader.showImageView(R.drawable.mine_empty_note, "");
				return;
			}
			mScrollLoader.setPage(t.getCurrentPage());// 用于分页请求
			mScrollLoader.setloading(false);//
			ArrayList<CourseListModel> charts = t.getItems();
			mList.addAll(charts);
			if (mAdapter == null) {
				mAdapter = new MineNoteListAdapter(getActivity(), mList);
				mListView.setAdapter(mAdapter);
			} else {
				mAdapter.notifyDataSetChanged();
			}
			mLoadingFooter.setState(LoadingFooter.State.TheEnd);
		}

		@Override
		public void onError(
				AbstractRequest<UmiwiListResult<CourseListModel>> requet,
				int statusCode, String body) {
			mScrollLoader.showLoadErrorView();
		}
	};

}
