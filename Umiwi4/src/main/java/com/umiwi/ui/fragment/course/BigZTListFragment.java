package com.umiwi.ui.fragment.course;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.BigZTListAdapter;
import com.umiwi.ui.fragment.pay.PayOrderDetailFragment;
import com.umiwi.ui.fragment.pay.PayTypeEvent;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.BigZTList;
import com.umiwi.ui.parsers.BigZTLIstParser;
import com.umiwi.ui.parsers.BigZTListResult;
import com.umiwi.ui.util.LoginUtil;

import java.util.List;

import cn.youmi.account.event.UserEvent;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

/**
 * @author tangxiyong
 * @version 2015-6-11 下午3:39:07
 */
public class BigZTListFragment extends BaseConstantFragment {
	public static final String KEY_URL = "key.url";
	public static final String KEY_ACTION_TITLE = "key.actiontitle";

	private String url;
	private String actionTitle;
	
	private ExpandableListView stageSectionListView;

	private BigZTListAdapter stageSectionAdapter;

	private LoadingFooter mLoadingFooter;
	private ListViewScrollLoader mScrollLoader;
	
	private ImageView image;
	private TextView introduce;
	
	private TextView price;
	private TextView discountPrice;
	private RelativeLayout buyLayout;
	
	private String buyId;
	
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
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_big_zt_layout, null);
		
		View header = inflater.inflate(R.layout.fragment_big_zt_list_header, null);
		
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(mActionBarToolbar);
		
		price = (TextView) view.findViewById(R.id.price);
		price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		discountPrice = (TextView) view.findViewById(R.id.discount_price);
		TextView buyzhuanti = (TextView) view.findViewById(R.id.buyzhuanti);
		buyLayout = (RelativeLayout) view.findViewById(R.id.buyzhuanti_layout);
		buyzhuanti.setOnClickListener(buyZTClickListener);
		
		stageSectionListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
		
		image = (ImageView) header.findViewById(R.id.jpzt_iv_image);
		introduce = (TextView) header.findViewById(R.id.jpzt_tv_introduce);
		
		LayoutParams para = image.getLayoutParams();
		para.width = DimensionUtil.getScreenWidth(getActivity());
		para.height = (DimensionUtil.getScreenWidth(getActivity()) * 15) / 32;
		image.setLayoutParams(para);

		mLoadingFooter = new LoadingFooter(getActivity());
		stageSectionListView.addHeaderView(header, null, false);
		stageSectionListView.addFooterView(mLoadingFooter.getView());

		// don't allowed contract
		stageSectionListView.setOnGroupClickListener(new OnGroupClickListener() {
					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						return true;
					}
				});

		mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);

		stageSectionAdapter = new BigZTListAdapter(getActivity());
		stageSectionListView.setAdapter(stageSectionAdapter);
		
		mScrollLoader.onLoadErrorPage();
		
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		YoumiRoomUserManager.getInstance().registerListener(userListener);
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
	public void onDestroyView() {
		super.onDestroyView();
		YoumiRoomUserManager.getInstance().unregisterListener(userListener);
	}
	
	private OnClickListener buyZTClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (YoumiRoomUserManager.getInstance().isLogin()) {
				Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayOrderDetailFragment.class);
				intent.putExtra(PayOrderDetailFragment.KEY_ORDER_ID, buyId);
				intent.putExtra(PayOrderDetailFragment.KEY_ORDER_TYPE, PayTypeEvent.ZHUANTI);
//				intent.putExtra(PayOrderDetailFragment.KEY_SPM, String.format(StatisticsUrl.ORDER_JPZT_DETAIL, buyId, buyprice));
				startActivity(intent);
			} else {
				LoginUtil.getInstance().showLoginView(getActivity());
			}
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
	public void onLoadData() {//http://i.v.youmi.cn/clientApi/zhuanti3detail?id=4
		super.onLoadData();
		if (TextUtils.isEmpty(url)) {
			mScrollLoader.showContentView("链接错误,请重新打开");
			onClickFooterFinishView();
			return;
		}
		GetRequest<BigZTListResult<BigZTList>> request = new GetRequest<BigZTListResult<BigZTList>>(
				url, BigZTLIstParser.class, BigZTList.class,
				stageSectionListner);
		request.go();
	}

	private Listener<BigZTListResult<BigZTList>> stageSectionListner = new Listener<BigZTListResult<BigZTList>>() {

		@Override
		public void onResult(
				AbstractRequest<BigZTListResult<BigZTList>> request,
				BigZTListResult<BigZTList> t) {

			if (t == null) {
				mLoadingFooter.setState(LoadingFooter.State.TheEnd);
				return;
			}

			ImageLoader mImageLoader = new ImageLoader(getActivity());
			mImageLoader.loadImage(t.getImageTwo(), image, R.drawable.image_loader_big);
			
			if (t.isBuy()) {//true 需要购买
				buyLayout.setVisibility(View.VISIBLE);
				buyId = t.getSectionId() + "";
				price.setText("原价：" + t.getPrice() + "元");
				discountPrice.setText("优惠价：" + t.getDiscountPrice() + "元");
			}

			introduce.setText(t.getIntroduce());
			introduce.setVisibility(View.VISIBLE);
			actionTitle = t.getTitle();
			mActionBarToolbar.setTitle(actionTitle);

			stageSectionAdapter.setData(t.getItems());
			int groupCount = t.getItems().size();
			for (int i = 0; i < groupCount; i++) {
				stageSectionListView.expandGroup(i);
			}
			mLoadingFooter.setState(LoadingFooter.State.TheEnd);

		}

		@Override
		public void onError(AbstractRequest<BigZTListResult<BigZTList>> requet,
				int statusCode, String body) {

		}
	};

	ModelStatusListener<UserEvent, UserModel> userListener = new ModelStatusListener<UserEvent, UserModel>() {

		@Override
		public void onModelGet(UserEvent key, UserModel models) {}

		@Override
		public void onModelUpdate(UserEvent key, UserModel model) {
			switch (key) {
			case PAY_SUCC:
				mScrollLoader.onLoadFirstPage();
				break;
			default:
				break;
			}
		
		}

		@Override
		public void onModelsGet(UserEvent key, List<UserModel> models) {}

	};
	
}
