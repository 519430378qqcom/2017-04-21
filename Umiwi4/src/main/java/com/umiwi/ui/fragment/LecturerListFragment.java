package com.umiwi.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.LecturerListAdapter;
import com.umiwi.ui.beans.LecturerBean;
import com.umiwi.ui.beans.LecturerBean.LecturerBeanRequestData;
import com.umiwi.ui.beans.LecturerBean.LecturerBeanWapper;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.view.MyLetterListView;
import com.umiwi.ui.view.MyLetterListView.OnTouchingLetterChangedListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;
import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;

/**
 * 讲师列表页面
 * 
 * @author tjie00
 * @since v4.3
 * @version v4.3 2014\04\09
 * 
 */
public class LecturerListFragment extends BaseConstantFragment {

	private PopupWindow popWindow;

	private View popViewBack;

	private TextView lastnameTextView;

	private MyLetterListView letterListView;

	private PinnedHeaderListView lecturerListView;
	private LecturerListAdapter mListAdapter;

	private Runnable dissmiss;
	private ArrayList<LecturerBeanWapper> lecturerWapper;

	private LastnameHandler mHandler;

	public static final int POP_SHOW = 1;

	public static final int POP_DISMISS = 2;

	public static final int POP_MOVE = 3;

	private LoadingFooter mLoadingFooter;
	private ListViewScrollLoader mScrollLoader;
	
	public static String SIGN="key.sign";
	public static class LastnameHandler extends Handler {

		WeakReference<LecturerListFragment> lecturerListFragment;

		public LastnameHandler(LecturerListFragment lecturerListFragment) {
			this.lecturerListFragment = new WeakReference<LecturerListFragment>(
					lecturerListFragment);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case POP_SHOW:
				String lastname = (String) msg.obj;
				Integer locationY = msg.arg1;

				lecturerListFragment.get().showLastname(lastname, locationY);
				break;

			case POP_DISMISS:
				lecturerListFragment.get().dissmissPop();
				break;

			case POP_MOVE:
				String lastnameStr = (String) msg.obj;
				Integer toLocationY = msg.arg1;

				lecturerListFragment.get().move(lastnameStr, toLocationY);
				break;

			default:
				break;

			}
		}

	}

	@SuppressLint("InflateParams") 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View contentView = inflater.inflate(R.layout.lecturer_list_fragment, null);
		
//		mActionBarToolbar = (Toolbar) contentView.findViewById(R.id.toolbar_actionbar);
//		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "名人大佬");
		
		popViewBack = inflater.inflate(R.layout.lastname_popview, null);
		lecturerListView = (PinnedHeaderListView) contentView.findViewById(R.id.lecturer_listview);
		letterListView = (MyLetterListView) contentView
				.findViewById(R.id.letter_listview);
		lastnameTextView = (TextView) popViewBack
				.findViewById(R.id.lastname_textview);
		
		mHandler = new LastnameHandler(this);

		popWindow = new PopupWindow(popViewBack, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popWindow.setAnimationStyle(R.style.popwindow_style);
		popWindow.dismiss();

		mLoadingFooter = new LoadingFooter(getActivity());
		lecturerListView.addFooterView(mLoadingFooter.getView());

		mListAdapter = new LecturerListAdapter(getActivity());

		lecturerListView.setAdapter(mListAdapter);

		mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);
		mScrollLoader.onLoadFirstPage();
		lecturerListView.setOnScrollListener(mScrollLoader);

		lecturerListView
				.setOnItemClickListener(new PinnedHeaderListView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int section, int position, long id) {
						if (lecturerWapper != null && lecturerWapper.size() > 0) {
							LecturerBeanWapper wapper = lecturerWapper
									.get(section);
							ArrayList<LecturerBean> lecturers = wapper
									.getLecturers();
							if (lecturers != null && lecturers.size() > 0) {
								LecturerBean lecturer = lecturers.get(position);
								
								Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
								intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LecturerDetailFragment.class);
								intent.putExtra(LecturerDetailFragment.KEY_DEFAULT_DETAILURL,lecturer.getCourseurl());
								startActivity(intent);
								
							}
						}
					}

					@Override
					public void onSectionClick(AdapterView<?> adapterView,
							View view, int section, long id) {
						// TODO Auto-generated method stub
					}
				});

		dissmiss = new Runnable() {
			@Override
			public void run() {
				if (popWindow != null && popWindow.isShowing()) {
					popWindow.dismiss();
				}
			}
		};

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
		if(popWindow != null && popWindow.isShowing()) {
			popWindow.dismiss();
		}
		MobclickAgent.onPageEnd(fragmentName);
	}
	
	public void move(String lastnameStr, Integer toLocationY) {
		lastnameTextView.setText(lastnameStr);
		popWindow.showAtLocation(lecturerListView, Gravity.CENTER, 0, 0);

	}

	public void dissmissPop() {
		mHandler.postDelayed(dissmiss, 1000);
	}

	// 显示姓名
	public void showLastname(String lastname, Integer locationY) {
		lastnameTextView.setText(lastname);
		popWindow.showAtLocation(letterListView, Gravity.CENTER, 0, 0);
	}

	private Listener<LecturerBeanRequestData> listener = new Listener<LecturerBean.LecturerBeanRequestData>() {

		@Override
		public void onResult(AbstractRequest<LecturerBeanRequestData> request,
				LecturerBeanRequestData t) {
			if (t != null) {
				lecturerWapper = t.getRecord();

				mListAdapter.setLecturers(lecturerWapper);

				initLetterView(lecturerWapper);
				mLoadingFooter.setState(LoadingFooter.State.TheEnd);
			}
		}

		@Override
		public void onError(AbstractRequest<LecturerBeanRequestData> requet,
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
	public void onLoadData() {
		GetRequest<LecturerBean.LecturerBeanRequestData> request = new GetRequest<LecturerBean.LecturerBeanRequestData>(
				UmiwiAPI.LECTURER_LIST, GsonParser.class,
				LecturerBean.LecturerBeanRequestData.class, listener);
		HttpDispatcher.getInstance().go(request);
	}

	// 显示字母view
	private void initLetterView(ArrayList<LecturerBeanWapper> lecturerWapper) {
		letterListView.setData(lecturerWapper);
		letterListView.setVisibility(View.VISIBLE);

		OnTouchetterChangedListener listener = new OnTouchetterChangedListener();

		letterListView.setOnTouchingLetterChangedListener(listener);
		letterListView.setHandler(mHandler);
	}

	class OnTouchetterChangedListener implements
			OnTouchingLetterChangedListener {
		@Override
		public void onTouchingLetterChanged(String s, String order) {
			if (lecturerListView != null) {

				int groundPosition = 0;
				try {
					groundPosition = Integer.parseInt(order);
				} catch (NumberFormatException e) {
					groundPosition = 0;
				}

				int position = 0;
				for (int i = 0; i < groundPosition; i++) {
					position += mListAdapter.getCountForSection(i);
				}

				position += groundPosition;

				lastnameTextView.setText(s);
				lecturerListView.setSelection(position);
			}
		}
	}

}
