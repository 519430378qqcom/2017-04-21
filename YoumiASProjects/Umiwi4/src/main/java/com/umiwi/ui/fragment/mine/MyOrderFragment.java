
package com.umiwi.ui.fragment.mine;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import cn.youmi.account.event.UserEvent;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.UmiwiMyOrderAdapter;
import com.umiwi.ui.beans.UmiwiMyOrderBeans;
import com.umiwi.ui.beans.UmiwiMyOrderBeans.MyOrderListRequestData;
import com.umiwi.ui.beans.UmiwiResultBeans;
import com.umiwi.ui.beans.UmiwiResultBeans.ResultBeansRequestData;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.fragment.course.JPZTDetailFragment;
import com.umiwi.ui.fragment.pay.PayingFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.StatisticsUrl;
import com.umiwi.ui.managers.YoumiRoomUserManager;


/**
 * 我的订单 
 * @author tangxiyong
 * 2013-12-6下午4:14:27
 *
 */
public class MyOrderFragment extends BaseConstantFragment implements ModelStatusListener<UserEvent, UserModel> {
	
    private UmiwiMyOrderAdapter mAdapter;

    private ListView mListView;
    private LoadingFooter mLoadingFooter;
    private ListViewScrollLoader mScrollLoader;

	private ArrayList<UmiwiMyOrderBeans> mList;

	/** 关闭的订单位置*/
	private int close_position;
	
    public static MyOrderFragment newInstance() {
    	MyOrderFragment f = new MyOrderFragment();
		return f;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frame_listview_layout, null);
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBarAndToolbarTitle(mActionBarToolbar, "我的订单");
        mList = new ArrayList<UmiwiMyOrderBeans>();
        mListView = (ListView) view.findViewById(R.id.listView);
        
        mLoadingFooter = new LoadingFooter(getActivity());
        mListView.addFooterView(mLoadingFooter.getView());
        
        mAdapter = new UmiwiMyOrderAdapter(getActivity(), mList);
        
        mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);
        mListView.setAdapter(mAdapter);
        
        mListView.setOnScrollListener(mScrollLoader);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	UmiwiMyOrderBeans mListBeans = (UmiwiMyOrderBeans) mAdapter.getItem(position - mListView.getHeaderViewsCount());
				// 1 => '未付款'
				// 2 => '付款成功'
				// 3 => '已关闭'
				// 4 => '已退款'
            	
            	Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
            	
            	if ("PRODUCT_ALBUM".equals(mListBeans.getDetail().get(0).getProduct_type())) {
                	if ("1".equals(mListBeans.getStatusnum())) {
						
	    				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayingFragment.class);
	    				intent.putExtra(PayingFragment.KEY_PAY_URL, String.format(UmiwiAPI.UMIWI_PAY_FOR_ORDER + StatisticsUrl.ORDER_LIST, mListBeans.getCode(), 1));
	    				startActivity(intent);
                		
					} else if("2".equals(mListBeans.getStatusnum())){
	    				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
	    				intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, mListBeans.getDetail().get(0).getDetailurl());
	    				startActivity(intent);
					} else {
						showMsg("订单"+mListBeans.getStatus());
					}
				} else if ("PRODUCT_VIP".equals(mListBeans.getDetail().get(0).getProduct_type())) {
					if ("1".equals(mListBeans.getStatusnum())) {
						
	    				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayingFragment.class);
	    				intent.putExtra(PayingFragment.KEY_PAY_URL, String.format(UmiwiAPI.UMIWI_PAY_FOR_ORDER + StatisticsUrl.ORDER_LIST, mListBeans.getCode(), 2));
	    				startActivity(intent);
						
					} else {
						showMsg("订单"+mListBeans.getStatus());
					}
				} else if ("PRODUCT_SECTION".equals(mListBeans.getDetail().get(0).getProduct_type())) {//专题
					if ("1".equals(mListBeans.getStatusnum())) {
						
	    				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayingFragment.class);
	    				intent.putExtra(PayingFragment.KEY_PAY_URL, String.format(UmiwiAPI.UMIWI_PAY_FOR_ORDER + StatisticsUrl.ORDER_LIST, mListBeans.getCode(), 3));
	    				startActivity(intent);
						
					} else if("2".equals(mListBeans.getStatusnum())){//TODO
//	    				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailLayoutFragments.class);
//	    				intent.putExtra(CourseDetailLayoutFragments.KEY_DETAIURL, mListBeans.getDetail().get(0).getDetailurl());
						intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, JPZTDetailFragment.class);
						intent.putExtra(JPZTDetailFragment.KEY_URL, mListBeans.getDetail().get(0).getDetailurl());
						startActivity(intent);
					} else {
						showMsg("订单"+mListBeans.getStatus());
					}
				} else if ("PRODUCT_TUTOR".equals(mListBeans.getDetail().get(0).getProduct_type())) {//讲师资询
					if ("1".equals(mListBeans.getStatusnum())) {
						
	    				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayingFragment.class);
	    				intent.putExtra(PayingFragment.KEY_PAY_URL, String.format(UmiwiAPI.UMIWI_PAY_FOR_ORDER + StatisticsUrl.ORDER_LIST, mListBeans.getCode(), 4));
	    				startActivity(intent);
						
					} else {
						showMsg("订单"+mListBeans.getStatus());
					}
				}
            }
        });
        
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				UmiwiMyOrderBeans mListBeans = (UmiwiMyOrderBeans) mAdapter.getItem(position - mListView.getHeaderViewsCount());
				if ("1".equals(mListBeans.getStatusnum())) {
					closeOrderDialog(mListBeans.getCloseurl(), position - mListView.getHeaderViewsCount());
				} else {
					showMsg("订单"+mListBeans.getStatus());
				}
				return false;
			}
		});
        mScrollLoader.onLoadFirstPage();
        YoumiRoomUserManager.getInstance().registerListener(this);
        
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
	
	/** 关闭一个订单*/
    private void closeOrderDialog(final String closeurl, final int position_) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("关闭订单");
		builder.setMessage("你确定要关闭订单？");
		// 设置点击按钮 “删除”
		builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				closeOrder(closeurl);
				close_position = position_;
			}

		});
		// 设置点击按钮“取消”
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}

		});
		AlertDialog alt = builder.create();
		alt.show();
	}
    

	private Listener<ResultBeansRequestData> listener = new Listener<UmiwiResultBeans.ResultBeansRequestData>() {
		@Override
		public void onResult(AbstractRequest<ResultBeansRequestData> request,
				ResultBeansRequestData t) {
			if ("succ".equals(t.getCode())) {
				mList.remove(close_position);//刷新界面
				mAdapter.notifyDataSetChanged();
				mListView.invalidate();
				showMsg("订单关闭成功");
			} else {
				showMsg(t.getMsg());
			}
		}

		@Override
		public void onError(AbstractRequest<ResultBeansRequestData> requet,
				int statusCode, String body) {
			showMsg("网络异常,请重试");
		}
	};
    
    /** 关闭订单*/
    private void  closeOrder(String closeurl) {
    	GetRequest<UmiwiResultBeans.ResultBeansRequestData> request = 
    			new GetRequest<UmiwiResultBeans.ResultBeansRequestData>(closeurl, GsonParser.class,UmiwiResultBeans.ResultBeansRequestData.class, listener);
    	HttpDispatcher.getInstance().go(request);
	}
    
	@Override
	public void onDestroy() {
		super.onDestroy();
		YoumiRoomUserManager.getInstance().unregisterListener(this);
	}

    private Listener<UmiwiMyOrderBeans.MyOrderListRequestData> orderListener = new Listener<UmiwiMyOrderBeans.MyOrderListRequestData>() {

		@Override
		public void onResult(AbstractRequest<MyOrderListRequestData> request,
				MyOrderListRequestData t) {
			if (t != null) {
				if (t.getTotal() != 10) {
					mScrollLoader.setEnd(true);
				}
				if (t.getTotal() == 0) {
					mScrollLoader.showImageView(R.drawable.mine_empty_order, "");
					return;
				}
				ArrayList<UmiwiMyOrderBeans> charts = t.getRecord();
				mList.addAll(charts);
				mScrollLoader.setPage(t.getCurr_page());
				mScrollLoader.setloading(false);
				if (mAdapter == null) {
					mAdapter = new UmiwiMyOrderAdapter(getActivity(), mList);
					mListView.setAdapter(mAdapter);// 解析成功播放列表
				} else {
					mAdapter.notifyDataSetChanged();
				}
				mLoadingFooter.setState(LoadingFooter.State.TheEnd);
			}
		}

		@Override
		public void onError(AbstractRequest<MyOrderListRequestData> requet,
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
	public void onLoadData(int page) {

		GetRequest<UmiwiMyOrderBeans.MyOrderListRequestData> request = new GetRequest<UmiwiMyOrderBeans.MyOrderListRequestData>(
				String.format(UmiwiAPI.UMIWI_MY_ORDER, page), GsonParser.class,
				UmiwiMyOrderBeans.MyOrderListRequestData.class, orderListener);
		HttpDispatcher.getInstance().go(request);
	
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case 10:
			switch (resultCode) {
			case 11:
				mList.clear();
				mAdapter.notifyDataSetChanged();
				mScrollLoader.onLoadFirstPage();
				break;

			default:
				break;
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onModelGet(UserEvent key, UserModel models) {
		
	}

	@Override
	public void onModelUpdate(UserEvent key, UserModel model) {
		
		switch (key) {
//		case PAY_HOME_ORDER:
//			mList.clear();
//			mAdapter.notifyDataSetChanged();
//			mScrollLoader.onLoadFirstPage();
//			break;
		case PAY_SUCC:
			mList.clear();
			mAdapter.notifyDataSetChanged();
			mScrollLoader.onLoadFirstPage();
			break;

		default:
			break;
		}
			
	}

	@Override
	public void onModelsGet(UserEvent key, List<UserModel> models) {
		
	}

}
