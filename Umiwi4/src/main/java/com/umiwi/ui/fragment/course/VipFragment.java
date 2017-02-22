package com.umiwi.ui.fragment.course;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.CourseListAdapter;
import com.umiwi.ui.fragment.pay.PayOrderDetailFragment;
import com.umiwi.ui.fragment.pay.PayTypeEvent;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.CourseListModel;
import com.umiwi.ui.model.VipIntroModel;
import com.umiwi.ui.parsers.UmiwiListParser;
import com.umiwi.ui.parsers.UmiwiListResult;
import com.umiwi.ui.util.LoginUtil;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.account.event.UserEvent;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.ListViewPositionUtils;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.util.ToastU;
import cn.youmi.framework.view.LoadingFooter;

/**
 * @author tangxiyong
 * @version 2015-5-7 下午5:02:21
 */
public class VipFragment extends BaseConstantFragment {
	public static final String KEY_ACTION_TITLE = "key.actiontitle";
	private String actionTitle;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_ACTION_TITLE, KEY_ACTION_TITLE);
	}

	@Override
	public void onActivityCreated(AppCompatActivity a) {
		super.onActivityCreated(a);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			actionTitle = savedInstanceState.getString(KEY_ACTION_TITLE);
		} else {
			Bundle bundle = getArguments();
			if (bundle != null) {
				actionTitle = bundle.getString(KEY_ACTION_TITLE);
			}
		}
	}

	private ListView mListView;
	private CourseListAdapter mAdapter;
	private ArrayList<CourseListModel> mList;

	private LoadingFooter mLoadingFooter;
	private ListViewScrollLoader mScrollLoader;
	
	private ImageView introImage;
	private TextView buyVip;
	
	private String orderby = "ctime";
	private String subjectType = "&type";
	
	private boolean isClear = false;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_frame_listview_layout, null);
		
		View header = inflater.inflate(R.layout.fragment_vip_header, null);
		
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, actionTitle);

		initHeaderView(header);
		
		mListView = (ListView) view.findViewById(R.id.listView);
		mList = new ArrayList<CourseListModel>();
		mAdapter = new CourseListAdapter(getActivity(), mList);

		mLoadingFooter = new LoadingFooter(getActivity());// 加载更多的view
		mListView.addHeaderView(header);
		mListView.addFooterView(mLoadingFooter.getView());

		mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);// 初始化加载更多监听

		mListView.setAdapter(mAdapter);

		mListView.setOnScrollListener(mScrollLoader);// 添加加载更多到listveiw

		mListView.setOnItemClickListener(mTimeLineOnClickListener);
		
		onLoadVipIntro();

		mScrollLoader.onLoadFirstPage();// 初始化接口，加载第一页

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

	private void initHeaderView(View header) {
		introImage = (ImageView) header.findViewById(R.id.intro_image);
//		TextView price = (TextView) header.findViewById(R.id.price);//3988元/年
//		price.setText(Html.fromHtml("<font size='18' color='#ff6600'>" +"<b>"+ "3988" + "</b>"+"</font>" + 
//				"<font size='14' color='#666666'>" + "  元/年" + "</font>" ));
		buyVip = (TextView) header.findViewById(R.id.join_vip);
		if ("23".equals(YoumiRoomUserManager.getInstance().getUser().getIdentity())) {
			buyVip.setText("续费会员");
		}
		buyVip.setOnClickListener(buyVipClickListener);
		
		RadioButton newest = (RadioButton) header.findViewById(R.id.newest);
		RadioButton hot = (RadioButton) header.findViewById(R.id.hot);
		
		RadioButton allCourse = (RadioButton) header.findViewById(R.id.all_course);
		RadioButton freeCourse = (RadioButton) header.findViewById(R.id.free_course);
		RadioButton vipCourse = (RadioButton) header.findViewById(R.id.vip_course);
		
		newest.setOnCheckedChangeListener(newestCheckedChangeListener);
		hot.setOnCheckedChangeListener(hotCheckedChangeListener);
		allCourse.setOnCheckedChangeListener(allCourseCheckedChangeListener);
		freeCourse.setOnCheckedChangeListener(freeCourseCheckedChangeListener);
		vipCourse.setOnCheckedChangeListener(vipCourseCheckedChangeListener);
	}
	
	private OnCheckedChangeListener newestCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				orderby = "ctime";
				isClear = true;
				mScrollLoader.onLoadFirstPage();
			}			
		}
	};
	private OnCheckedChangeListener hotCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				orderby = "watchnum";
				isClear = true;
				mScrollLoader.onLoadFirstPage();
			}			
		}
	};
	private OnCheckedChangeListener allCourseCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				subjectType = "";
				isClear = true;
				mScrollLoader.onLoadFirstPage();
			}			
		}
	};
	private OnCheckedChangeListener freeCourseCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				subjectType = "&price=free";
				isClear = true;
				mScrollLoader.onLoadFirstPage();
			}			
		}
	};
	private OnCheckedChangeListener vipCourseCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				subjectType = "&subject=5&catid=0";
				isClear = true;
				mScrollLoader.onLoadFirstPage();
			}			
		}
	};
	
	private OnClickListener buyVipClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (YoumiRoomUserManager.getInstance().isLogin()) {
				Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayOrderDetailFragment.class);
				intent.putExtra(PayOrderDetailFragment.KEY_ORDER_ID, "23");
				intent.putExtra(PayOrderDetailFragment.KEY_ORDER_TYPE, PayTypeEvent.VIP);
				startActivity(intent);
			} else {
				LoginUtil.getInstance().showLoginView(getActivity());
			}
		}
	};

	private OnItemClickListener mTimeLineOnClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mListView.clearChoices();
			// listview item的点击容错处理
			CourseListModel item = mList.get(ListViewPositionUtils.indexInDataSource(position, mListView));
			if (ListViewPositionUtils.isPositionCanClick(item, position, mListView, mList)) {
//				Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
//				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailLayoutFragments.class);
//				intent.putExtra(CourseDetailLayoutFragments.KEY_DETAIURL, item.detailurl);
//				startActivity(intent);

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

			if (isClear) {
				mList.clear();
			}
			// 数据加载
			ArrayList<CourseListModel> charts = t.getItems();
			mList.addAll(charts);

			if (mAdapter == null) {
				mAdapter = new CourseListAdapter(getActivity(), mList);
				mListView.setAdapter(mAdapter);// 解析成功 播放列表
			} else {
				mAdapter.notifyDataSetChanged();
			}
			
			isClear = false;
		}

		@Override
		public void onError(AbstractRequest<UmiwiListResult<CourseListModel>> requet,
				int statusCode, String body) {
			mScrollLoader.showLoadErrorView();
		}

	};
	
	private Listener<VipIntroModel> vipIntroListener = new Listener<VipIntroModel>() {

		@Override
		public void onResult(AbstractRequest<VipIntroModel> request,
				VipIntroModel t) {
			if (null != t) {
				LayoutParams para = introImage.getLayoutParams();
				para.width = DimensionUtil.getScreenWidth(getActivity());
				para.height = (para.width * t.height) / t.width;
				introImage.setLayoutParams(para);
				
				ImageLoader mImageLoader = new ImageLoader(getActivity());
				mImageLoader.loadBitmap(t.image, introImage);
			}
		}

		@Override
		public void onError(AbstractRequest<VipIntroModel> requet,
				int statusCode, String body) {
			onLoadVipIntro();
		}
	};
	
	private void onLoadVipIntro() {
		GetRequest<VipIntroModel> request = new GetRequest<VipIntroModel>(
				UmiwiAPI.VIP_MEMBERINTRO, GsonParser.class,
				VipIntroModel.class, vipIntroListener);
		request.go();
	}

	@Override
	public void onLoadData(int page) {
		String url = String.format(UmiwiAPI.COURSE_LIST, orderby, subjectType, page);
		GetRequest<UmiwiListResult<CourseListModel>> request = new GetRequest<UmiwiListResult<CourseListModel>>(
				url, UmiwiListParser.class,
				CourseListModel.class, listener);
		request.go();
	}
	
	ModelStatusListener<UserEvent, UserModel> userListener = new ModelStatusListener<UserEvent, UserModel>() {

		@Override
		public void onModelGet(UserEvent key, UserModel models) {}

		@Override
		public void onModelUpdate(UserEvent key, UserModel model) {
			switch (key) {
			case PAY_SUCC:
				buyVip.setText("续费会员");
				ToastU.showShort(getActivity(), "购买成功！");
				break;
			default:
				break;
			}
		
		}

		@Override
		public void onModelsGet(UserEvent key, List<UserModel> models) {}

	};

}
