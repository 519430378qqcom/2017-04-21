package com.umiwi.ui.fragment.mine;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import cn.youmi.framework.dialog.MsgDialog;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.http.parsers.ResultParser;
import cn.youmi.framework.model.ResultModel;
import cn.youmi.framework.util.ListViewPositionUtils;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.CourseListAdapter;
import com.umiwi.ui.beans.AddFavBeans.AddFavBeansRequestData;
import com.umiwi.ui.dao.CollectionDao;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.CourseListModel;
import com.umiwi.ui.parsers.UmiwiListParser;
import com.umiwi.ui.parsers.UmiwiListResult;
import com.umiwi.ui.util.CommonHelper;

/**
 * 收藏列表
 * 
 * @author tangxiyong 2014-2-15下午8:38:28
 * 
 */
public class MyFavListFragment extends BaseConstantFragment {

	public static final String EXTRA_CATEGORY = "CHARTS_POSITION";

	private CourseListAdapter mAdapter;
	private ListView mListView;
	private LoadingFooter mLoadingFooter;
	private ArrayList<CourseListModel> mList;
	private ListViewScrollLoader mScrollLoader;

	private CollectionDao collectionDao;

	/** 要删除的课程位置 */
	private int fav_position;

	public static MyFavListFragment newInstance() {
		MyFavListFragment f = new MyFavListFragment();
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		collectionDao = new CollectionDao();
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_frame_listview_layout, null);
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "我的收藏");
		if (neeUpload()) {
			uploadData();
		}

		mList = new ArrayList<CourseListModel>();
		mListView = (ListView) view.findViewById(R.id.listView);

		mAdapter = new CourseListAdapter(getActivity(), mList);
		mLoadingFooter = new LoadingFooter(getActivity());
		mListView.addFooterView(mLoadingFooter.getView());

		mListView.setAdapter(mAdapter);
		
		mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);
		mListView.setOnScrollListener(mScrollLoader);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				mListView.clearChoices();
				CourseListModel mListBeans = mList.get(ListViewPositionUtils.indexInDataSource(position, mListView));
				if (ListViewPositionUtils.isPositionCanClick(mListBeans, position, mListView, mList)) {
					Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
					intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
					intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, mListBeans.detailurl);
					startActivity(intent);
				}
			}
		});

		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				CourseListModel mListBeans = mList.get(ListViewPositionUtils.indexInDataSource(position, mListView));
				if (ListViewPositionUtils.isPositionCanClick(mListBeans, position, mListView, mList)) {
					deleteOneFavVideo(mListBeans, position - mListView.getHeaderViewsCount());
				}
				return true;
			}
		});

		mScrollLoader.onLoadFirstPage();
		return view;
	}

	
	// 上传本地数据库中的数据到服务器
	private void uploadData() {
		List<String> saveCollections =  collectionDao.getUnuploadSaveCollections();
		
		StringBuilder sb = new StringBuilder();
		for(String saveCol : saveCollections) {
			sb.append(saveCol).append(",");
		}
		if(sb.length() > 0) {
			sb.delete(sb.length()-1, sb.length());
			
			String saveStrings = sb.toString();
			String favStr = String.format(UmiwiAPI.UMIWI_FAV_ADD_VIDEO_ALBUMID,
					saveStrings);
			GetRequest<AddFavBeansRequestData> req = new GetRequest<AddFavBeansRequestData>(
					favStr, GsonParser.class,AddFavBeansRequestData.class, saveListener);
			req.setTag(saveStrings);
			HttpDispatcher.getInstance().go(req);
		}
		
		List<String> deleteCollections = collectionDao.getUnuploadDeleteCollections();
		sb.delete(0, sb.length());
		for(String delCol : deleteCollections) {
			sb.append(delCol).append(",");
		}
		
		if(sb.length() > 0) {
			sb.delete(sb.length()-1, sb.length());
			String deleteStrings = sb.toString();
			
			String delStr = String.format(UmiwiAPI.UMIWI_FAV_DELETE_VIDEO_FAVID, deleteStrings);
			
			GetRequest<ResultModel> request = new GetRequest<ResultModel>(delStr, ResultParser.class, deleteListener);
			request.setTag(deleteStrings);
	    	HttpDispatcher.getInstance().go(request);
		}
	}

	// 是否需要上传数据
	private boolean neeUpload() {
		return (CommonHelper.checkNetWifi(UmiwiApplication.getInstance())
				&& YoumiRoomUserManager.getInstance().isLogin() && collectionDao
					.isCollectionNeed2Update());
	}

	private Listener<UmiwiListResult<CourseListModel>> favListener = new Listener<UmiwiListResult<CourseListModel>>() {

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
				mScrollLoader.showContentView("你还没有收藏");
				return;
			}
			mScrollLoader.setPage(t.getCurrentPage());// 用于分页请求
			mScrollLoader.setloading(false);//
			ArrayList<CourseListModel> charts = t.getItems();

			mList.addAll(charts);
			collectionDao.syncCollections(charts);

			if (mAdapter == null) {
				mAdapter = new CourseListAdapter(getActivity(), mList);
				mListView.setAdapter(mAdapter);
			} else {
				mAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void onError(
				AbstractRequest<UmiwiListResult<CourseListModel>> requet,
				int statusCode, String body) {
			mScrollLoader.showLoadErrorView();
		}
	};

	private Listener<AddFavBeansRequestData> saveListener = new Listener<AddFavBeansRequestData>() {
		@Override
		public void onResult(AbstractRequest<AddFavBeansRequestData> request,
				AddFavBeansRequestData t) {
			String albumIDStrings = (String) request.getTag();
			String[] albumIDs = albumIDStrings.split(",");
			if(albumIDs != null) {
				for(String albumID : albumIDs) {
					collectionDao.updateCollection(albumID);
				}
			}
		}

		@Override
		public void onError(AbstractRequest<AddFavBeansRequestData> requet,
				int statusCode, String body) {
		}
	};
	
	private Listener<ResultModel> deleteListener = new Listener<ResultModel>() {
		@Override
		public void onResult(AbstractRequest<ResultModel> request, ResultModel t) {
			if (t.isSucc()) {
				String delFavidString = (String) request.getTag();
				String[] delFavids = delFavidString.split(",");
				
				if(delFavids != null) {
					for(String delFavid : delFavids) {
						collectionDao.deleteCollectionCompelete(delFavid);
					}
				}
			} 
		}

		@Override
		public void onError(AbstractRequest<ResultModel> requet,
				int statusCode, String body) {
		}
	};
	
	public void onLoadData(final int page) {
		GetRequest<UmiwiListResult<CourseListModel>> request = new GetRequest<UmiwiListResult<CourseListModel>>(
				String.format(UmiwiAPI.UMIWI_MY_FAV, page),
				UmiwiListParser.class,
				CourseListModel.class, favListener);
		request.go();
	}


	/** 删除一个收藏视频 */
	private void deleteOneFavVideo(final CourseListModel beans, final int position_) {
		final String favID = beans.favid;
		final MsgDialog dialog = new MsgDialog();
		dialog.setTitle("提示");
		dialog.setMessage("你确定删除此课程？");
		dialog.setPositiveButtonText(R.string.delete);
		dialog.setPositiveButtonListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismissAllowingStateLoss();
				delete_fav(beans);
				fav_position = position_;
			}
		});
		dialog.show(getChildFragmentManager(), "dialog");
	}

	private Listener<ResultModel> deleteResultListener = new Listener<ResultModel>() {
		@Override
		public void onResult(AbstractRequest<ResultModel> request, ResultModel t) {
			if (t.isSucc()) {
				CourseListModel bean = (CourseListModel) request.getTag();
				mList.remove(fav_position);// 刷新界面
				mAdapter.notifyDataSetChanged();
				mListView.invalidate();
				showMsg("收藏删除成功");
				collectionDao.deleteCollectionCompelete(bean.id + "");

			} else {
				showMsg(t.getM());
			}
		}

		@Override
		public void onError(AbstractRequest<ResultModel> request,
				int statusCode, String body) {
			CourseListModel bean = (CourseListModel) request.getTag();
			mList.remove(fav_position);// 刷新界面
			mAdapter.notifyDataSetChanged();
			mListView.invalidate();
			showMsg("收藏删除成功");
			
			//getID is album ID
		}
	};

	/** 删除收藏视频 */
	private void delete_fav(CourseListModel bean) {
		String favId = bean.favid;
		GetRequest<ResultModel> request = new GetRequest<ResultModel>(
				String.format(UmiwiAPI.UMIWI_FAV_DELETE_VIDEO_FAVID, favId),
				ResultParser.class, deleteResultListener);
		request.setTag(bean);
		HttpDispatcher.getInstance().go(request);
		
	}
}
