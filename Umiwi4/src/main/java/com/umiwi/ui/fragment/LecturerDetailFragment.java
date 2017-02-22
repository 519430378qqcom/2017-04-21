package com.umiwi.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.LecturerCourseListAdapter;
import com.umiwi.ui.beans.CourseBean;
import com.umiwi.ui.beans.CourseBean.CourseBeanRequestData;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.AndroidSDK;
import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.ListViewPositionUtils;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.CircleImageView;
import cn.youmi.framework.view.LoadingFooter;

/**
 * 讲师详情页面
 * 
 * @author tjie00
 * @since v4.3
 * @version v4.3 2014\04\11
 * 
 */
public class LecturerDetailFragment extends BaseConstantFragment {
	public static final String KEY_DEFAULT_DETAILURL = "key.defaultdetailurl";

	private String desc;

	private TextView titleView;
	private TextView nameView;
	private TextView countView;
	private TextView resolveTextView;

	private View header;

	private ListView mListView;
	private TextView descTextVeiw;
	private LinearLayout descLinearLayout;
	private LinearLayout showAllLinearLayout;
	private TextView moreTextView;
	private ImageView moreImageView;
	private FrameLayout backGround;
	private LecturerCourseListAdapter mListAdapter;
	private ImageView iconBack;
	private CircleImageView icon;

	private TextView tvRelatedClassTextView;

	private LoadingFooter mLoadingFooter;
	private ListViewScrollLoader mScrollLoader;

	private ArrayList<CourseBean> mList;
	public String lecturerDetailUrl;

	private ViewGroup.LayoutParams layoutParams;
	private ViewGroup.LayoutParams defaultParams;

	private MoreClickListener moreClickListener;
	private int totalPages = 1;
	private int page = 1;

	public static final String EXTRA_STRURL_ = "EXTRA_STRURL";

	// 接受前面传递过来的讲师信息
	public void setLecturerDetailUrl(String lecturerDetailUrl) {
		this.lecturerDetailUrl = lecturerDetailUrl;
	}

	@Override
	public void onActivityCreated(AppCompatActivity a) {
		lecturerDetailUrl = a.getIntent().getStringExtra(KEY_DEFAULT_DETAILURL);
		super.onActivityCreated(a);
	}

	@SuppressLint("InflateParams") 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_frame_listview_layout, null);
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(mActionBarToolbar);
		
		header = inflater.inflate(R.layout.lecturer_detail_fragment_headview,null);

		mListView = (ListView) view.findViewById(R.id.listView);

		backGround = (FrameLayout) header
				.findViewById(R.id.lecturer_linearlayout);

		layoutParams = backGround.getLayoutParams();
		layoutParams.width = LayoutParams.MATCH_PARENT;
		layoutParams.height = DimensionUtil.getScreenHeight(getActivity()) / 3 + 30;
		backGround.setLayoutParams(layoutParams);

		defaultParams = new LinearLayout.LayoutParams(-1, -2);

		iconBack = (ImageView) header.findViewById(R.id.icon_back);
		icon = (CircleImageView) header.findViewById(R.id.icon);

		// 根据屏幕适配圆形头像大小
		ViewGroup.LayoutParams para = icon.getLayoutParams();
		para.width = (DimensionUtil.getScreenWidth(getActivity()) * 2) / 9;
		para.height = para.width;
		icon.setLayoutParams(para);

		descTextVeiw = (TextView) header.findViewById(R.id.desc_textview);
		descLinearLayout = (LinearLayout) header
				.findViewById(R.id.desc_linearlayout);
		showAllLinearLayout = (LinearLayout) header
				.findViewById(R.id.more_linearlayout);
		moreTextView = (TextView) header.findViewById(R.id.more_textview);
		moreImageView = (ImageView) header.findViewById(R.id.more_imageview);
		nameView = (TextView) header.findViewById(R.id.name_textview);
		titleView = (TextView) header.findViewById(R.id.title_textview);
		countView = (TextView) header.findViewById(R.id.count_textview);
		countView.setVisibility(View.INVISIBLE);
		resolveTextView = (TextView) header.findViewById(R.id.resolve_textview);

		tvRelatedClassTextView = (TextView) header.findViewById(R.id.related_classes_textview);
		
		mListView.addHeaderView(header, null, false);

		mLoadingFooter = new LoadingFooter(getActivity());
		mListView.addFooterView(mLoadingFooter.getView());

		mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);
		mListView.setOnScrollListener(mScrollLoader);

		mListAdapter = new LecturerCourseListAdapter(getActivity());
		mListView.setAdapter(mListAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				mListView.clearChoices();
				CourseBean item = mList.get(ListViewPositionUtils.indexInDataSource(position, mListView));
				if (ListViewPositionUtils.isPositionCanClick(item, position, mListView, mList)) {

					Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
					intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
					intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, item.getDetailurl());
					startActivity(intent);
				}
			}
		});

		moreClickListener = new MoreClickListener();
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
	public void onDestroyView() {
		super.onDestroyView();
	}

	public void loadRoundImage(String imageUrl) {
		
		ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
		mImageLoader.loadImage(imageUrl, icon);
		mImageLoader.loadImage(imageUrl, iconBack);
		
	}


	public void onLoadData(final int page) {
		String lecturersStr = lecturerDetailUrl;

		if (page <= totalPages) {
			lecturersStr = lecturersStr + ("&p=" + page);

			GetRequest<CourseBean.CourseBeanRequestData> request = new GetRequest<CourseBean.CourseBeanRequestData>(
					lecturerDetailUrl+ ("&p=" + page), GsonParser.class,
					CourseBean.CourseBeanRequestData.class, listener);
			HttpDispatcher.getInstance().go(request);
		}
	}
	private String lecturerTitle;
	private Listener<CourseBeanRequestData> listener = new Listener<CourseBean.CourseBeanRequestData>() {
		@Override
		public void onResult(AbstractRequest<CourseBeanRequestData> request,
				CourseBeanRequestData t) {
			
			if (t != null) {
				lecturerTitle = t.getName();
				
				mActionBarToolbar.setTitle(lecturerTitle);
				mActionBarToolbar.setNavigationIcon(R.drawable.ic_action_bar_return);
				
				loadRoundImage(t.getImage());
				nameView.setText(t.getName());
				titleView.setText(t.getTitle());

				mScrollLoader.setPage(t.getCurr_page());
				mScrollLoader.setloading(false);

				if (t.getCurr_page() == t.getPages()) {
					mScrollLoader.setEnd(true);
					mLoadingFooter.setState(LoadingFooter.State.TheEnd);
				}

				if (page == 1) {
					totalPages = t.getPages();

					// mActionBar.setTitle(t.getName());
					int count = t.getTotals();
					countView.setText("课程 ( " + count + " )");
					countView.setVisibility(View.VISIBLE);

					resolveTextView.setVisibility(View.GONE);

					if (TextUtils.isEmpty(desc)) {
						desc = t
								.getDescription();
					}

					if (!TextUtils.isEmpty(desc)) {
						descLinearLayout.setVisibility(View.VISIBLE);
						descTextVeiw.setText(desc);
						moreClickListener.setContentView(descTextVeiw);

						descTextVeiw.getViewTreeObserver()
								.addOnGlobalLayoutListener(
										new OnGlobalLayoutListener() {
											int lines = 0;

											@SuppressWarnings("deprecation")
											@Override
											public void onGlobalLayout() {
												lines = descTextVeiw
														.getLineCount();
												if (AndroidSDK.isJELLY_BEAN()) {
													descTextVeiw
															.getViewTreeObserver()
															.removeOnGlobalLayoutListener(
																	this);
												} else {
													descTextVeiw.getViewTreeObserver().removeGlobalOnLayoutListener(this);
												}

												if (lines != 0 && lines > 3) {

													descTextVeiw.setMaxLines(3);
													descTextVeiw
															.setEllipsize(TextUtils.TruncateAt.END);

													showAllLinearLayout
															.setVisibility(View.VISIBLE);
													moreTextView.setText("全文");
													moreImageView.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.show_all_content_bg));

													descLinearLayout
															.setOnClickListener(moreClickListener);
												} else {
													showAllLinearLayout
															.setVisibility(View.GONE);
												}
											}
										});

					} else {
						descLinearLayout.setVisibility(View.GONE);
					}
					
				}
				
				tvRelatedClassTextView.setVisibility(View.VISIBLE);
				
				page = t.getCurr_page();

				if (mList == null) {
					mList = t.getRecord();
				} else {
					if (page <= totalPages) {
						mList.addAll(t.getRecord());
					}
				}
				
				mListAdapter.setCourses(mList);
				mLoadingFooter.setState(LoadingFooter.State.TheEnd);
			}
		}

		@Override
		public void onError(AbstractRequest<CourseBeanRequestData> requet,
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
	

	private class MoreClickListener implements View.OnClickListener {

		private TextView contentView;

		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			if (contentView.getLineCount() == 3) {
				contentView.setLines(Integer.MAX_VALUE);
				contentView.setMinLines(0);
				contentView.setEllipsize(TextUtils.TruncateAt.END);

				moreTextView.setText("收起");
				moreImageView.setBackgroundDrawable(getActivity().getResources().getDrawable(
						R.drawable.pack_up_button_background));

				backGround.setLayoutParams(defaultParams);
			} else {
				contentView.setMaxLines(3);

				moreTextView.setText("全文");
				moreImageView.setBackgroundDrawable(getActivity().getResources().getDrawable(
						R.drawable.show_all_content_bg));

				backGround.setLayoutParams(layoutParams);

			}
			descLinearLayout.requestLayout();

			backGround.requestLayout();
			header.requestLayout();
		}

		public void setContentView(TextView contentTextView) {
			this.contentView = contentTextView;
		}
	}

}
