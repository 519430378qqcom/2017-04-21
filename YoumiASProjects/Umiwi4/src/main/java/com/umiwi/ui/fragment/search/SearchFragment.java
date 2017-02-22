package com.umiwi.ui.fragment.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.SearchAdapter;
import com.umiwi.ui.beans.JokeBean;
import com.umiwi.ui.beans.SearchBean;
import com.umiwi.ui.beans.SearchBean.SearchBeanRequestData;
import com.umiwi.ui.beans.SearchCloudBean;
import com.umiwi.ui.beans.SearchCloudBean.SearchCloudBeanRequestData;
import com.umiwi.ui.beans.SuggestBeans;
import com.umiwi.ui.dao.JokeDao;
import com.umiwi.ui.dao.SearchCloudDao;
import com.umiwi.ui.dao.SearchDao;
import com.umiwi.ui.dialog.JokeDialog;
import com.umiwi.ui.fragment.LecturerDetailFragment;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.CommonHelper;
import com.umiwi.ui.util.LoginUtil;
import com.umiwi.ui.view.KeywordsFlow;

import java.util.ArrayList;
import java.util.Calendar;

import cn.youmi.framework.debug.LogUtils;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.util.ToastU;
import cn.youmi.framework.util.URLCoderUtils;
import cn.youmi.framework.view.LoadingFooter;

public class SearchFragment extends BaseConstantFragment {
	
	private ArrayList<SearchBean> mResultList;
	private JokeDao jokeDao = new JokeDao();
	private SearchDao searchDao = new SearchDao();
	
	private View clearButton;
	private KeywordsFlow hotwordsFlowLayout;
	private View resultContainer;
	private View hotwordsContainer;
	private SearchAdapter searchAdapter;
	private String keyword;
	private EditText searchEditText;
	private LoadingFooter mLoadingFooter;
	private ListViewScrollLoader mScrollLoader;
	private View emptyView;
	private ListView mListView;
	
	private TextView tvCount;
	private ImageView ivNoResult;
	private LinearLayout spinnerContainer;
	private Spinner conditionSpinner;
	private Spinner timeSpinner;
	private ArrayAdapter<String> conditionAdapter;
	private ArrayAdapter<String> timeAdapter;
	private String[] conditionContent;
	private String[] timeContent;
	private String filter = "all";
	private String order = "new";

	@SuppressLint("InflateParams") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search, null);
		View header = inflater.inflate(R.layout.search_result_header_view, null);
		isStartActivity = true;
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		searchEditText = (EditText) view.findViewById(R.id.search_edit_text);
		searchEditText.setOnFocusChangeListener(focusChangeListener);
		searchEditText.setOnKeyListener(keyListener);
		searchEditText.addTextChangedListener(watcher);
		view.findViewById(R.id.cancel_button).setOnClickListener(cancelButtonListener);
		clearButton = view.findViewById(R.id.clear_button);
		clearButton.setOnClickListener(clearButtonListener);
		
		resultContainer = view.findViewById(R.id.result_container);
		hotwordsContainer = view.findViewById(R.id.hot_words_container);

		emptyView = view.findViewById(R.id.empty_view);
		hotwordsFlowLayout = (KeywordsFlow) view
				.findViewById(R.id.hot_words_flow_layout);

		
		tvCount = (TextView) header.findViewById(R.id.count_tv);
		ivNoResult = (ImageView) header.findViewById(R.id.nocourse);
		mListView = (ListView) view.findViewById(R.id.listView);
		mResultList = new ArrayList<SearchBean>();
		mLoadingFooter = new LoadingFooter(getActivity());
		searchAdapter = new SearchAdapter(getActivity(), mResultList);
		mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);
		
		mListView.addHeaderView(header, null, false);
		mListView.addFooterView(mLoadingFooter.getView());
		mListView.setOnScrollListener(mScrollLoader);
		mListView.setOnItemClickListener(itemClickListener);
		mListView.setAdapter(searchAdapter);
		
		
		initResult(view);
		
		initCloud(view);
		return view;
	}

	private void initResult(View view) {
		
		spinnerContainer = (LinearLayout) view.findViewById(R.id.spinner_container);
		
		conditionContent = getResources().getStringArray(R.array.search_condition_array);
		timeContent = getResources().getStringArray(R.array.search_time_condition);
		
		conditionSpinner = (Spinner) view.findViewById(R.id.condition_spinner);
		timeSpinner = (Spinner) view.findViewById(R.id.time_spinner);
		
		conditionAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, conditionContent);
		conditionAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
		conditionSpinner.setAdapter(conditionAdapter);
		
		timeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, timeContent);
		timeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
		timeSpinner.setAdapter(timeAdapter);

		conditionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				TextView tempView = (TextView) view;
				String content = conditionContent[position];
				content = content.concat(" ▼");
				tempView.setGravity(Gravity.CENTER);
				tempView.setText(content);
				tempView.setTextColor(UmiwiApplication.getContext().getResources().getColor(R.color.umiwi_black));

				if (!YoumiRoomUserManager.getInstance().isLogin() && 1 == position) {
					LoginUtil.getInstance().showLoginView(getActivity());
					ToastU.showShort(getActivity(), "当前操作需要登录");
					conditionSpinner.setSelection(0);
					return;
				}

				if (position == 0) {
					filter = "all";
				} else {
					filter = "mycourse";
				}
				if (!isStartActivity) {
					mResultList.clear();
					mScrollLoader.onLoadFirstPage();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		timeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				TextView tempView = (TextView) view;
				String content = timeContent[position];
				content = content.concat(" ▼");
				tempView.setGravity(Gravity.CENTER);
				tempView.setText(content);
				tempView.setTextColor(UmiwiApplication.getContext().getResources().getColor(R.color.umiwi_black));

				switch (position) {
					case 0:
						order = "new";
						break;
					case 1:
						order = "new";
						break;
					case 2:
						order = "hot";
						break;

					default:
						order = "new";
						break;
				}
				if (!isStartActivity) {
					mResultList.clear();
					mScrollLoader.onLoadFirstPage();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	
	@Override
	public void onStart() {
		super.onStart();
		checkJokesState();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		scheduleLoadNextDelayed();
		MobclickAgent.onPageStart(fragmentName);
	}

	@Override
	public void onPause() {
		super.onPause();
		cancelLoad();
		MobclickAgent.onPageEnd(fragmentName);
	}

	private void showEmptyView(){
		emptyView.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
	}
	
	private void hideEmptyView(){
		emptyView.setVisibility(View.GONE);
		mListView.setVisibility(View.VISIBLE);
	}
	
	private boolean isCloudKeyword;

	public static final int MIN_CLICK_DELAY_TIME = 1000;
	private long lastClickTime = 0;
	private boolean isStartActivity = false;

	private View.OnClickListener cloudClickLisner = new View.OnClickListener() {

		@Override
		public void onClick(View view) {

			SearchCloudBean bean = (SearchCloudBean) view.getTag(R.id.key_search_cloud_key);
			String type = bean.getType();
			long currentTime = Calendar.getInstance().getTimeInMillis();
			if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
				lastClickTime = currentTime;
				if (type.equals("word")) {
					isCloudKeyword = true;
					resultContainer.setVisibility(View.VISIBLE);
					hotwordsContainer.setVisibility(View.GONE);
					hideEmptyView();
					keyword = bean.getTitle();
					mResultList.clear();
					mScrollLoader.onLoadFirstPage();
				} else if (type.equals("course")) {

					Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
					intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
					intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, bean.getDetailurl());
					startActivity(intent);
					searchDao.save(new SearchBean(bean.getTitle(), bean.getDetailurl()));

				} else if (type.equals("tutor")) {
					Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
					intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LecturerDetailFragment.class);
					intent.putExtra(LecturerDetailFragment.KEY_DEFAULT_DETAILURL, bean.getDetailurl());
					startActivity(intent);

					searchDao.save(new SearchBean(bean.getTitle(), bean.getDetailurl()));

				}
			}

			MobclickAgent.onEvent(getActivity(), "搜索VI", "云标签选择");
		}
	};
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			if (searchAdapter.isResultSearch()) {
				SearchBean sb = searchAdapter.getResultSearch().get(position - mListView.getHeaderViewsCount());
				Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
				intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, sb.getDetailurl());
				startActivity(intent);
//				searchDao.save(new SearchBean(bean.getTitle(), bean.getDetailurl()));
				
			} else {
				if(position - mListView.getHeaderViewsCount() == searchAdapter.getSearchHistory().size()){
					searchDao.clearHistory();
					searchAdapter.setHistorySearch(new ArrayList<SearchBean>(),false);
				}else{
					if (position - mListView.getHeaderViewsCount() <= -1){
						return;
					}
					SearchBean sb = searchAdapter.getSearchHistory().get(position - mListView.getHeaderViewsCount());
					keyword = sb.getTitle();
					isCloudKeyword = true;
					searchEditText.clearFocus();
					mResultList.clear();
					mScrollLoader.onLoadFirstPage();
				}

			}
		}
	};

	
	private OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				tvCount.setVisibility(View.GONE);
				ivNoResult.setVisibility(View.GONE);
				resultContainer.setVisibility(View.VISIBLE);
				spinnerContainer.setVisibility(View.GONE);
				hotwordsContainer.setVisibility(View.GONE);
				searchAdapter.setHistorySearch(searchDao.getAll(), true);
				mScrollLoader.setloading(false);
				mScrollLoader.setEnd(true);
				hideEmptyView();
			}
		}
	};
	
	private OnClickListener clearButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			searchEditText.setText("");
		}
	};
	
	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

		@Override
		public void afterTextChanged(Editable s) {
			tvCount.setVisibility(View.GONE);
			ivNoResult.setVisibility(View.GONE);
			hotwordsContainer.setVisibility(View.GONE);
			if (searchEditText.length() > 0) {
				clearButton.setVisibility(View.VISIBLE);
				if (isCloudKeyword) {
					isCloudKeyword = false;
				} else {
					spinnerContainer.setVisibility(View.GONE);
					suggestKeyword();
				}
				
				cancelLoad();
			} else {
				clearButton.setVisibility(View.INVISIBLE);
				resultContainer.setVisibility(View.VISIBLE);
				spinnerContainer.setVisibility(View.GONE);
				searchAdapter.setHistorySearch(searchDao.getAll(), true);
				mScrollLoader.setloading(false);
				mScrollLoader.setEnd(true);
				hideEmptyView();
			}
		}
	};

	private OnKeyListener keyListener = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_ENTER) {
				if (!TextUtils.isEmpty(searchEditText.getText())) {
					keyword = searchEditText.getText().toString();
					mResultList.clear();
					mScrollLoader.onLoadFirstPage();
					searchEditText.clearFocus();
				}
				return true;
			}
			return false;
		}
	};
	
	OnClickListener cancelButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (hotwordsContainer.getVisibility() == View.VISIBLE) {
				getActionActivity().finish();
				return;
			}
			searchEditText.setText("");
			searchEditText.clearFocus();
			resultContainer.setVisibility(View.GONE);
			hotwordsContainer.setVisibility(View.VISIBLE);
			scheduleLoadNextDelayed();
		}
	};
	
	private Listener<SearchBean.SearchBeanRequestData> searchResultListener = new Listener<SearchBean.SearchBeanRequestData>() {

		@Override
		public void onResult(AbstractRequest<SearchBeanRequestData> request,
				SearchBeanRequestData t) {
			if (null != t) {
				LogUtils.e("search", "listener111"+isStartActivity);
				spinnerContainer.setVisibility(View.VISIBLE);
				if (t.getIssearch() == 1 && t.getTotals() != 0 ) {
					tvCount.setVisibility(View.VISIBLE);
					ivNoResult.setVisibility(View.GONE);
					tvCount.setText("共有" + t.getTotals() + "个相关课程");
				} else {
					tvCount.setVisibility(View.VISIBLE);
					tvCount.setText("向你推荐下列课程");
					ivNoResult.setVisibility(View.VISIBLE);
					ImageLoader mImageLoader = new ImageLoader(getActivity());
					mImageLoader.loadImage(R.drawable.no_search_result, ivNoResult);
				}
				
				if (t.getCurr_page() == t.getPages()) {
					mScrollLoader.setEnd(true);
				} else {
					mScrollLoader.setEnd(false);
				}
				mScrollLoader.setPage(t.getCurr_page());
				mScrollLoader.setloading(false);
				
				isCloudKeyword = true;
				ArrayList<SearchBean> charts = t.getRecord();
				mResultList.addAll(charts);

				searchAdapter.setResultSearch(mResultList);
//				searchAdapter = new SearchAdapter(getActivity(), mResultList);
//				mListView.setAdapter(searchAdapter);// 解析成功 播放列表
				isStartActivity = false;
				LogUtils.e("search", "listener000"+isStartActivity);
			}
			
		}

		@Override
		public void onError(AbstractRequest<SearchBeanRequestData> requet,
				int statusCode, String body) {
			mScrollLoader.showLoadErrorView();
			JokeDialog d = new JokeDialog();
			d.showDialog(getActivity());
		}
	};
	
	ArrayList<SearchBean> history = new ArrayList<SearchBean>();
	
	private Listener<SuggestBeans> suggestListener = new Listener<SuggestBeans>() {

		@Override
		public void onResult(AbstractRequest<SuggestBeans> request,
				SuggestBeans t) {
			if (null != t && t.getRecord().size() > 0) {
				history.clear();
				for (int i = 0; i < t.getRecord().size(); i++) {
					history.add(new SearchBean(t.getRecord().get(i), String.valueOf(i)));
				}
				searchAdapter.setHistorySearch(history, false);
			}
			mScrollLoader.setloading(false);
			mScrollLoader.setEnd(true);
		}

		@Override
		public void onError(AbstractRequest<SuggestBeans> requet,
				int statusCode, String body) {}
	};
	
	private Listener<JokeBean.JokeBeanRequestData> jokeListener = new Listener<JokeBean.JokeBeanRequestData>() {
		@Override
		public void onResult(
				AbstractRequest<JokeBean.JokeBeanRequestData> request,
				JokeBean.JokeBeanRequestData t) {
			if (t != null && "9999".equals(t.getErrorcode())) {
				jokeDao.saveJokes(t.getRecord());
				jokeDao.deleteOldJokes();
			}

		}

		@Override
		public void onError(
				AbstractRequest<JokeBean.JokeBeanRequestData> requet,
				int statusCode, String body) {

		}

	};
	
	private void checkJokesState() {
		if (!jokeDao.isJokeInit()) {
			jokeDao.initJoke();
		} else {
			if (jokeDao.isJokeNeed2Update()) {
				if (CommonHelper.checkNetWifi(getActivity())) {
					GetRequest<JokeBean.JokeBeanRequestData> request = new GetRequest<JokeBean.JokeBeanRequestData>(
							UmiwiAPI.JOKE_URL, GsonParser.class,
							JokeBean.JokeBeanRequestData.class, jokeListener);
					HttpDispatcher.getInstance().go(request);
				}
			}
		}
	}
	
	private void suggestKeyword(){
		GetRequest<SuggestBeans> req = new GetRequest<SuggestBeans>(
				String.format(UmiwiAPI.SUGGEST_SEARCH, URLCoderUtils.URLEncoder(searchEditText.getText().toString())),
				GsonParser.class, SuggestBeans.class, suggestListener);
		HttpDispatcher.getInstance().go(req);
	}

	@Override
	public void onLoadData(int page) {//http://i.v.youmi.cn/ClientApi/search?pagenum=8&q=%s&filter=%s&order=%s&p=%s
		super.onLoadData(page);
		//		mScrollLoader.setEnd(false);
		searchEditText.setText(keyword);
		searchEditText.setSelection(keyword.length());
		searchDao.save(new SearchBean(keyword, keyword));
		LogUtils.e("search", "loaddate"+isStartActivity);

		String url = String.format(UmiwiAPI.SEARCH, URLCoderUtils.URLEncoder(keyword), filter, order, page);

		GetRequest<SearchBean.SearchBeanRequestData> request = new GetRequest<SearchBean.SearchBeanRequestData>(
				url, GsonParser.class,
				SearchBean.SearchBeanRequestData.class, searchResultListener);
		request.go();


	}

	//TODO Cloud
	private GestureDetector mGestureDetector;

	// 显示下一页内容
	public static final int SHOW_NEXT = 1;
	// 显示上一页内容
	public static final int SHOW_PRE = 2;

	public int showType = SHOW_NEXT;

	public static SearchCloudDao cloudDao;

	private boolean shouldLoadData = false;

	private int page = 1;
	private int pages = 1;

	private Runnable mDowloadPrePageRun = new Runnable() {
		@Override
		public void run() {
			downloadPre();
		}
	};

	private Runnable mDowloadNextPageRun = new Runnable() {
		@Override
		public void run() {
			downloadNext();
			mHandler.postDelayed(mDowloadNextPageRun, 5000);
		}
	};

	private void cancelLoad() {
		mHandler.removeCallbacks(mDowloadPrePageRun);
		mHandler.removeCallbacks(mDowloadNextPageRun);
	}

	private void scheduleLoadNextDelayed() {
		mHandler.removeCallbacks(mDowloadNextPageRun);
		mHandler.postDelayed(mDowloadNextPageRun, 5000);
	}

	OnGestureListener onGestureListener = new OnGestureListener() {
		@Override
		public boolean onDown(MotionEvent e) {
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				cancelLoad();
			}
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			scheduleLoadNextDelayed();
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			if (e1.getX() - e2.getX() > 10 || e1.getY() - e2.getY() > 10) {
				downloadNext();
			} else if (e2.getX() - e1.getX() > 10 || e2.getY() - e1.getY() > 10) {
				downloadPre();
			}
			return true;
		}

	};
	
	private void initCloud(View view) {
		cloudDao = new SearchCloudDao();
		mGestureDetector = new GestureDetector(getActivity(), onGestureListener);
		shouldLoadData = checkLoadDataStatue();

		if (shouldLoadData) {
			downloadSearchCloudBean();
		} else {
			loadData();
		}

		hotwordsFlowLayout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					scheduleLoadNextDelayed();
				}

				return mGestureDetector.onTouchEvent(event);
			}
		});
	}
	
	/**
	 * 检测是否需要从服务器下载数据
	 * 
	 * @return true: download data from net false: get data from db
	 */
	private boolean checkLoadDataStatue() {
		boolean needDownloadData = false;
		needDownloadData = cloudDao.ifDataOutTime();
		return needDownloadData;
	}

	public void downloadPre() {
		showType = SHOW_PRE;
		if (page > 1) {
			loadDataByPage(--page);
		} else if (page == 1) {
			page = pages;
			loadDataByPage(page);
		}
	}
	
	public void downloadNext() {
		showType = SHOW_NEXT;
		if (page < pages) {
			loadDataByPage(++page);
		} else if (page == pages) {
			page = 1;
			loadDataByPage(page);
		}
	}

	private void downloadSearchCloudBean() {
		GetRequest<SearchCloudBean.SearchCloudBeanRequestData> request = new GetRequest<SearchCloudBean.SearchCloudBeanRequestData>(
				UmiwiAPI.SEARCH_CLOUD, GsonParser.class,
				SearchCloudBean.SearchCloudBeanRequestData.class, cloudListener);
		HttpDispatcher.getInstance().go(request);
	}

	Listener<SearchCloudBean.SearchCloudBeanRequestData> cloudListener = new Listener<SearchCloudBean.SearchCloudBeanRequestData>() {

		@Override
		public void onResult(
				AbstractRequest<SearchCloudBeanRequestData> request,
				SearchCloudBeanRequestData t) {
			ArrayList<ArrayList<SearchCloudBean>> results = t.getRecord();
			cloudDao.saveSearchClouds(results);
			loadData();
		}

		@Override
		public void onError(AbstractRequest<SearchCloudBeanRequestData> requet,
				int statusCode, String body) {
		}

	};

	private static void feedKeywordsFlow(KeywordsFlow tagView,
			ArrayList<SearchCloudBean> results) {
//		Random random = new Random();
		for (int i = 0; i < results.size(); i++) {
//			int ran = random.nextInt(results.size());
			SearchCloudBean tmp = results.get(i);
			tagView.feedKeyword(tmp);
		}
	}

	// 加载数据
	public void loadData() {
		if (pages == 1 || pages == -1) {
			pages = cloudDao.getPageCount();
		}
		loadDataByPage(page);
	}

	private void loadDataByPage(int page) {
		ArrayList<SearchCloudBean> beans = cloudDao.getCloudByPage(page);
		if (beans != null && beans.size() > 0) {

			hotwordsFlowLayout.rubKeywords();
			feedKeywordsFlow(hotwordsFlowLayout, beans);
			hotwordsFlowLayout.setOnItemClickListener(cloudClickLisner);

			if (showType == SHOW_NEXT) {
				hotwordsFlowLayout.go2Show(KeywordsFlow.ANIMATION_IN);
			} else {
				hotwordsFlowLayout.go2Show(KeywordsFlow.ANIMATION_OUT);
			}
			scheduleLoadNextDelayed();
		}
	}

}
