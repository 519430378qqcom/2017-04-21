package com.umiwi.ui.fragment.mine;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.MyRecordAdapter;
import com.umiwi.ui.adapter.MyRecordAdapter.OnSelectionNumberChangeListener;
import com.umiwi.ui.beans.UmiwiMyRecordBeans;
import com.umiwi.ui.beans.UmiwiMyRecordBeans.MyCouponListRequestData;
import com.umiwi.ui.beans.UmiwiResultBeans;
import com.umiwi.ui.beans.UmiwiResultBeans.ResultBeansRequestData;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;

import java.util.ArrayList;

import cn.youmi.framework.dialog.MsgDialog;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.PostRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;
import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;
import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView.OnItemClickListener;

/**
 * 播放记录
 * 
 * @author tangxiyong 2013-12-9上午10:52:34
 * 
 */
public class RecordFragment extends BaseConstantFragment {
	private MyRecordAdapter mAdapter;

	private PinnedHeaderListView mListView;
	private ProgressDialog mLoadingDialog = null;

	private ListViewScrollLoader mScrollLoader;
	private LoadingFooter mLoadingFooter;

	ActionMode mMode;
	Menu menu;
	/** 要删除的课程位置 */
	private UmiwiMyRecordBeans gvideo;
	private OnSelectionNumberChangeListener msetOnSelectionNumberChangeListener = new OnSelectionNumberChangeListener() {
		
		@Override
		public void onSelectionNumberChange(int num) {
			if(num > 0){
				mMode.setTitle("删除(" +num+")");
			}else{
				mMode.setTitle("删除");
			}
		}
	};

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		this.menu = menu;
		inflater.inflate(R.menu.toolbar_delete, menu);
		menu.findItem(R.id.delete).setVisible(false);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.delete:
			boolean isEditMode = mAdapter.isEditMode();
			mAdapter.setEditModel(!isEditMode);
			if(!mAdapter.isEmpty()){
				mMode = mActionBarToolbar.startActionMode(new DeleteCallback());
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressLint("InflateParams") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_frame_pinnedheaderlistview_layout, null);
        
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "播放记录");
		
		mLoadingDialog = new ProgressDialog(getActivity());
		mLoadingDialog.setMessage("加载中,请稍候...");
		mLoadingDialog.setIndeterminate(true);
		mLoadingDialog.setCancelable(true);
		mLoadingDialog.setCanceledOnTouchOutside(false);

		mListView = (PinnedHeaderListView) view.findViewById(R.id.listView);
		mAdapter = new MyRecordAdapter();
		mAdapter.setOnSelectionNumberChangeListener(msetOnSelectionNumberChangeListener);
		
		mLoadingFooter = new LoadingFooter(getActivity());
		
		mListView.addFooterView(mLoadingFooter.getView());
		mListView.setAdapter(mAdapter);
		
		mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);
		mListView.setOnScrollListener(mScrollLoader);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int section, int position, long id) {
				
				mListView.clearChoices();
				int headerViewsCount = mListView.getHeaderViewsCount();
				if (isHeaderViewPosition(position)) {
					int indexInDataSource = position - headerViewsCount;
					UmiwiMyRecordBeans mListBeans = mAdapter.getItem(section, indexInDataSource);
					if (!isNullFlag(mListBeans)) {
						Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
						intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
						intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, mListBeans.getDetailurl());
						startActivity(intent);
					}
				}
			}

			@Override
			public void onSectionClick(AdapterView<?> adapterView, View view,
					int section, long id) {

			}
		});


		mScrollLoader.onLoadFirstPage();
		return view;
	}
	
	boolean isNullFlag(UmiwiMyRecordBeans mListBeans) {
        return mListBeans == null;
    }

	boolean isHeaderViewPosition(int position) {
		return position - mListView.getHeaderViewsCount() < mAdapter.size()
				&& position - mListView.getHeaderViewsCount() >= 0;
	}
	
	private class DeleteCallback implements ActionMode.Callback {
		@Override
		public boolean onCreateActionMode(ActionMode arg0, Menu menu) {
			arg0.setTitle("删除");
			
			MenuItem clearItem = menu.add(0, 2, 3, "清空");
			MenuItemCompat.setShowAsAction(clearItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
			
			MenuItem deleteItem = menu.add(0, 1, 2, "删除").setIcon( R.drawable.ic_delete);
			MenuItemCompat.setShowAsAction(deleteItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);

			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

			switch (item.getItemId()) {
			case 1:
				deleteRecord();
				mode.finish();
				break;

			case 2:
				showClearDialog();
				mode.finish();
				break;

			default:
				break;
			}
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode arg0) {
			boolean isEditMode = mAdapter.isEditMode();
			mAdapter.setEditModel(!isEditMode);
		}
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

	private void showClearDialog(){
		final MsgDialog dialog = new MsgDialog();
		dialog.setTitle("提示");
		dialog.setMessage("你确定要清空播放记录？");
		dialog.setPositiveButtonText("清空");
		dialog.setPositiveButtonListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismissAllowingStateLoss();
				clear_record();
				
			}
		});
		dialog.show(getChildFragmentManager(), "dialog");
	}

	//清空
	private Listener<ResultBeansRequestData> clearListener = new Listener<UmiwiResultBeans.ResultBeansRequestData>() {
		@Override
		public void onResult(AbstractRequest<ResultBeansRequestData> request,
				ResultBeansRequestData t) {
			if ("9999".equals(t.getE())) {
				mAdapter.clear();
				showMsg("播放记录删除成功");
			} else {
				showMsg(t.getM());
			}
		}

		@Override
		public void onError(AbstractRequest<ResultBeansRequestData> requet,
				int statusCode, String body) {
			showMsg("网络异常，请重试");
		}
	};

	/** 清空播放记录 */
	private void clear_record() {
		GetRequest<UmiwiResultBeans.ResultBeansRequestData> request = new GetRequest<UmiwiResultBeans.ResultBeansRequestData>(
				UmiwiAPI.UMIWI_CLEAR_MYRECORD, GsonParser.class,
				UmiwiResultBeans.ResultBeansRequestData.class, clearListener);
		request.go();
	}

	private Listener<MyCouponListRequestData> listener = new Listener<UmiwiMyRecordBeans.MyCouponListRequestData>() {
		@Override
		public void onResult(AbstractRequest<MyCouponListRequestData> request,
				MyCouponListRequestData t) {
			
			if (t.getTotal() != 10) {
				mScrollLoader.setEnd(true);
			}
			if (t.getTotal() == 0) {
				mScrollLoader.showImageView(R.drawable.mine_empty_record, "");
				return;
			}
			ArrayList<UmiwiMyRecordBeans> charts = t.getRecord();
			mScrollLoader.setPage(t.getCurr_page());
			mScrollLoader.setloading(false);
			mAdapter.append(charts);
			if (!charts.isEmpty()) {
				menu.findItem(R.id.delete).setVisible(true);
			}
			mLoadingFooter.setState(LoadingFooter.State.TheEnd);
		}

		@Override
		public void onError(AbstractRequest<MyCouponListRequestData> requet,
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
		String url = String.format(UmiwiAPI.UMIWI_MY_RECORD, page);
		Log.e("TAG", "播放记录url=" + url);
		GetRequest<UmiwiMyRecordBeans.MyCouponListRequestData> request = new GetRequest<UmiwiMyRecordBeans.MyCouponListRequestData>(
				url, GsonParser.class,
				UmiwiMyRecordBeans.MyCouponListRequestData.class, listener);
		request.go();
	}


	/** 验证观看权限 */
	private void getVipPermission(UmiwiMyRecordBeans video) {

	}

	public void showNetworkAlertDialog(UmiwiMyRecordBeans video) {
		gvideo = video;

		final MsgDialog dialog = new MsgDialog();
		
		dialog.setTitle("提示");
		dialog.setMessage("您正使用2G/3G网络观看视频，运营商将会收取较高费用。是否继续？");
		dialog.setPositiveButtonText("继续观看");
		dialog.setPositiveButtonListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismissAllowingStateLoss();
				getVipPermission(gvideo);
			}
		});
		dialog.setNegativeButtonListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mLoadingDialog.isShowing()) {
					mLoadingDialog.dismiss();
				}
			}
		});
		dialog.show(getChildFragmentManager(), "dialog");
	}

	
	
	
	private Listener<ResultBeansRequestData> mDeleteListener = new Listener<UmiwiResultBeans.ResultBeansRequestData>() {

		@Override
		public void onResult(AbstractRequest<ResultBeansRequestData> request,
				ResultBeansRequestData t) {
			
			if(t.getE().equals("9999")){
				@SuppressWarnings("unchecked")
				ArrayList<UmiwiMyRecordBeans> selected = (ArrayList<UmiwiMyRecordBeans>) request.getTag();
				mAdapter.remove(selected);
				showMsg("播放记录删除成功");
			} else {
				showMsg(t.getM());
			}
		}

		@Override
		public void onError(AbstractRequest<ResultBeansRequestData> requet,
				int statusCode, String body) {
			showMsg(body);
		}
	};
	
	
	private void deleteRecord() {

		ArrayList<UmiwiMyRecordBeans> selected = mAdapter.getSelected();
		int size = selected.size();
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i < size;i++){
			sb.append(selected.get(i).getId());
			if(i != size - 1){
				sb.append(",");
			}
		}
		
		String ids = sb.toString();
		
		if (TextUtils.isEmpty(ids)) {
			return;
		}
		
		String url = UmiwiAPI.MY_RECORD_DELETE_BY_IDS;
		
		PostRequest<ResultBeansRequestData> request = new PostRequest<UmiwiResultBeans.ResultBeansRequestData>(url, GsonParser.class, ResultBeansRequestData.class,mDeleteListener);
		request.addParam("albumids", ids);
		request.setTag(selected);
		request.go();
		
	}
}
