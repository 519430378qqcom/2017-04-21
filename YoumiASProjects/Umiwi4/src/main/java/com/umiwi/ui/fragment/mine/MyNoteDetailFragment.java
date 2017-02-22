package com.umiwi.ui.fragment.mine;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.adapter.MineNoteDetailAdapter;
import com.umiwi.ui.beans.NoteDetailBeans;
import com.umiwi.ui.beans.NoteDetailBeans.NoteDetailRequestData;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.model.CourseListModel;

/**
 * 我的笔记详情
 * 
 * @author tangxiong
 * @version 2014年6月16日 下午6:08:42
 */
public class MyNoteDetailFragment extends BaseConstantFragment {

	private MineNoteDetailAdapter mAdapter;

	private ListView mListView;

	private LoadingFooter mLoadingFooter;
	private ListViewScrollLoader mScrollLoader;
	public CourseListModel noteList;

	private ArrayList<NoteDetailBeans> mList;

	MyNoteDetailFragment fragment;

	private Listener<NoteDetailRequestData> listener = new Listener<NoteDetailBeans.NoteDetailRequestData>() {

		@Override
		public void onResult(AbstractRequest<NoteDetailRequestData> request,
				NoteDetailRequestData t) {
			if (t != null) {
				ArrayList<NoteDetailBeans> charts = t.getRecord();
				mList.addAll(charts);

				if (mAdapter == null) {
					mAdapter = new MineNoteDetailAdapter(fragment,
							getActivity(), mList, noteList);
					mListView.setAdapter(mAdapter);// 解析成功 播放列表
				} else {
					mAdapter.notifyDataSetChanged();
				}
				mLoadingFooter.setState(LoadingFooter.State.TheEnd);
			}
		}

		@Override
		public void onError(AbstractRequest<NoteDetailRequestData> requet,
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_frame_listview_layout,
				null);

		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, noteList.title);

		fragment = this;
		mList = new ArrayList<NoteDetailBeans>();
		mListView = (ListView) view.findViewById(R.id.listView);
		mAdapter = new MineNoteDetailAdapter(fragment, getActivity(), mList,
				noteList);
		mLoadingFooter = new LoadingFooter(getActivity());
		mListView.addFooterView(mLoadingFooter.getView());

		mListView.setAdapter(mAdapter);

		mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);
		mListView.setOnScrollListener(mScrollLoader);
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
		GetRequest<NoteDetailBeans.NoteDetailRequestData> request = new GetRequest<NoteDetailBeans.NoteDetailRequestData>(
				noteList.notelist, GsonParser.class,
				NoteDetailRequestData.class, listener);
		HttpDispatcher.getInstance().go(request);

	}

}
