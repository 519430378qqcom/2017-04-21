package com.umiwi.ui.fragment.mine;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.KeyboardUtils;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.UmiwiMyCardAdapter;
import com.umiwi.ui.beans.UmiwiMyCardBeans;
import com.umiwi.ui.beans.UmiwiMyCardBeans.MyCardListRequestData;
import com.umiwi.ui.beans.UmiwiResultBeans;
import com.umiwi.ui.beans.UmiwiResultBeans.ResultBeansRequestData;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.fragment.course.CourseListFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;

/**
 * 我的兑换卡 我的兑换码
 * 
 * @author xiaobo
 * 
 */
public class MyCardFragment extends BaseConstantFragment {
	
	private ArrayList<UmiwiMyCardBeans> mList;
	
	private UmiwiMyCardAdapter mAdapter;
	private ListView mListView;
	private LoadingFooter mLoadingFooter;
	private ListViewScrollLoader mScrollLoader;
	private ProgressDialog mLoadingDialog = null;
	private TextView mycard_btn;
	private EditText mycard_code;


	@SuppressLint("InflateParams") 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_mycard, null);
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "开卡/兑换课程");

		// 表单部分
		mycard_code = (EditText) view.findViewById(R.id.mycard_code);
		mycard_btn = (TextView) view.findViewById(R.id.mycard_btn);

		// 提交点击事件
		mycard_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				KeyboardUtils.hideKeyboard(getActivity());
				String code = mycard_code.getText().toString().trim();
				if (TextUtils.isEmpty(code)) {
					showMsg("兑换码不能为空");
				} else {
					mLoadingDialog = ProgressDialog.show(getActivity(), // context
							"", // title
							"请稍候...", // message
							true);
					mycard_code.setText(null);
					cardExchange(code);
				}

			}
		});


		// 列表部分ß
		mList = new ArrayList<UmiwiMyCardBeans>();
		mListView = (ListView) view.findViewById(R.id.f_mycard_listview);
		mLoadingFooter = new LoadingFooter(getActivity());
		mListView.addFooterView(mLoadingFooter.getView());
		mAdapter = new UmiwiMyCardAdapter(getActivity(), mList);
		mListView.setAdapter(mAdapter);
		
		mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);

		mListView.setOnScrollListener(mScrollLoader);
			// 设置页面显示用户名
		String username = YoumiRoomUserManager.getInstance().getUser().getUsername();
		TextView v_username = (TextView) view.findViewById(R.id.mycard_username);
		v_username.setText("兑换用户：" + username);
		mScrollLoader.onLoadFirstPage();
		
		
		mListView.setOnItemClickListener(new OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = null;
				UmiwiMyCardBeans mListBeans = (UmiwiMyCardBeans) mAdapter.getItem(position - mListView.getHeaderViewsCount());
				if ("album".equals(mListBeans.getType())) {
					intent = new Intent(getActivity(), UmiwiContainerActivity.class);
					intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
					intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, mListBeans.getProducts().getDetailurl());
				} else if ("section".equals(mListBeans.getType())) {
					intent = new Intent(getActivity(), UmiwiContainerActivity.class);
					intent.putExtra(CourseListFragment.KEY_URL, mListBeans.getProducts().getDetailurl());
					intent.putExtra(CourseListFragment.KEY_ACTION_TITLE, mListBeans.getProducts().getTitle());
					intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseListFragment.class);

				} else if("member".equals(mListBeans.getType())) {
					intent = new Intent(getActivity(), UmiwiContainerActivity.class);
					intent.putExtra(CourseListFragment.KEY_URL, UmiwiAPI.DIAMOND_FEAST);
					intent.putExtra(CourseListFragment.KEY_ACTION_TITLE, "钻石会员");
					intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseListFragment.class);
				} else {
					
				}
				startActivity(intent);
				
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

	private Listener<MyCardListRequestData> listener = new Listener<UmiwiMyCardBeans.MyCardListRequestData>() {

		@Override
		public void onResult(AbstractRequest<MyCardListRequestData> request,
				MyCardListRequestData t) {
			if (t != null) {
				if (t.getCurr_page() == t.getPages()) {
					mScrollLoader.setEnd(true);
				}
				if (t.getTotal() == 0) {
					mLoadingFooter.setState(LoadingFooter.State.TheEnd);
				}
				ArrayList<UmiwiMyCardBeans> charts = t.getRecord();
				if (null != charts) {
					mList.addAll(charts);
					if (mAdapter == null) {
						mAdapter = new UmiwiMyCardAdapter(getActivity(), mList);
						mListView.setAdapter(mAdapter);
					} else {
						mAdapter.notifyDataSetChanged();
					}
				}
				
				mScrollLoader.setPage(t.getCurr_page());
				mScrollLoader.setloading(false);
				
				mLoadingFooter.setState(LoadingFooter.State.TheEnd);
			}
		}

		@Override
		public void onError(AbstractRequest<MyCardListRequestData> requet,
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
		String url = String.format(UmiwiAPI.UMIWI_MY_CARD, page);
		GetRequest<UmiwiMyCardBeans.MyCardListRequestData> request = new GetRequest<UmiwiMyCardBeans.MyCardListRequestData>(
				url, GsonParser.class,
				UmiwiMyCardBeans.MyCardListRequestData.class, listener);
		HttpDispatcher.getInstance().go(request);
	}



	private Listener<ResultBeansRequestData> exchaneListener = new Listener<UmiwiResultBeans.ResultBeansRequestData>() {

		@Override
		public void onResult(AbstractRequest<ResultBeansRequestData> request,
				ResultBeansRequestData t) {
			if (mLoadingDialog.isShowing()) {
				mLoadingDialog.dismiss();
			}
			if (t.isSucc()) {
				// 成功
				mList.clear();
				mAdapter.notifyDataSetChanged();
				mScrollLoader.onLoadFirstPage();
				showMsg(t.getM());
			} else {
				// 失败
				showMsg(t.getM());
			}
		}

		@Override
		public void onError(AbstractRequest<ResultBeansRequestData> requet,
				int statusCode, String body) {
			if (mLoadingDialog.isShowing()) {
				mLoadingDialog.dismiss();
			}
		}
	};

	/**
	 * 兑换
	 */
	public void cardExchange(String code) {
		String url = null;
		if(code.length() == 14) { 
			url = String.format(UmiwiAPI.UMIWI_TCODE_CHANGE_URL_14, code);
		} else {
			url = String.format(UmiwiAPI.UMIWI_TCODE_CHANGE_URL_10, code);
		}
		
		GetRequest<UmiwiResultBeans.ResultBeansRequestData> request = new GetRequest<UmiwiResultBeans.ResultBeansRequestData>(
				url, GsonParser.class,
				UmiwiResultBeans.ResultBeansRequestData.class, exchaneListener);
		HttpDispatcher.getInstance().go(request);
	}
}
