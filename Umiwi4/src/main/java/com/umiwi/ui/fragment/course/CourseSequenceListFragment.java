package com.umiwi.ui.fragment.course;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.CourseListAdapter;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.model.CourseListModel;
import com.umiwi.ui.parsers.UmiwiListParser;
import com.umiwi.ui.parsers.UmiwiListResult;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.util.ListViewPositionUtils;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

/**
 * @author tangxiyong
 * @version 2015-5-7 下午5:02:21
 */
public class CourseSequenceListFragment extends BaseConstantFragment {
    /**
     * 无需拼&p=1 这个参数
     */
    public static final String KEY_URL = "key.url";
    public static final String KEY_ACTION_TITLE = "key.actiontitle";

    private String url;
    private String actionTitle;

    private String orderby = "ctime";
    private String subjectType = "&type";

    private boolean isClear = false;

    public static CourseSequenceListFragment newInstance() {
        CourseSequenceListFragment courseListFragment = new CourseSequenceListFragment();
        return courseListFragment;
    }

    public static CourseSequenceListFragment newInstance(String url) {
        CourseSequenceListFragment courseListFragment = new CourseSequenceListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_URL, url);
        courseListFragment.setArguments(bundle);
        return courseListFragment;
    }

    public static CourseSequenceListFragment newInstance(String url, String actionTitle) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_URL, url);
        bundle.putString(KEY_ACTION_TITLE, actionTitle);
        CourseSequenceListFragment courseListFragment = new CourseSequenceListFragment();
        courseListFragment.setArguments(bundle);
        return courseListFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_URL, url);
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


    private ListView mListView;
    private CourseListAdapter mAdapter;
    private ArrayList<CourseListModel> mList;

    private LoadingFooter mLoadingFooter;
    private ListViewScrollLoader mScrollLoader;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frame_listview_layout, null);

        View header = inflater.inflate(R.layout.fragment_course_list_header, null);

        initHeaderView(header);

        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBarAndToolbarTitle(mActionBarToolbar, actionTitle);

        mList = new ArrayList<CourseListModel>();
        mListView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new CourseListAdapter(getActivity().getApplication(), mList);

        mLoadingFooter = new LoadingFooter(getActivity().getApplication());// 加载更多的view
        mListView.addHeaderView(header);
        mListView.addFooterView(mLoadingFooter.getView());

        mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);// 初始化加载更多监听

        mListView.setAdapter(mAdapter);

        mListView.setOnScrollListener(mScrollLoader);// 添加加载更多到listveiw

        mListView.setOnItemClickListener(mTimeLineOnClickListener);

        mScrollLoader.onLoadFirstPage();// 初始化接口，加载第一页

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

    private void initHeaderView(View header) {

        RadioButton newest = (RadioButton) header.findViewById(R.id.newest);
        RadioButton hot = (RadioButton) header.findViewById(R.id.hot);
        RadioButton highOpinion = (RadioButton) header.findViewById(R.id.high_opinion);

        RadioButton allCourse = (RadioButton) header.findViewById(R.id.all_course);
        RadioButton freeCourse = (RadioButton) header.findViewById(R.id.free_course);
        RadioButton giftCourse = (RadioButton) header.findViewById(R.id.gift_course);
        RadioButton vipCourse = (RadioButton) header.findViewById(R.id.vip_course);

        LinearLayout typeLayout = (LinearLayout) header.findViewById(R.id.type_layout);

        if ("精品课程".equals(actionTitle) || "免费课程".equals(actionTitle)) {
            typeLayout.setVisibility(View.GONE);
        }

        newest.setOnCheckedChangeListener(newestCheckedChangeListener);
        hot.setOnCheckedChangeListener(hotCheckedChangeListener);
        highOpinion.setOnCheckedChangeListener(highOpinionCheckedChangeListener);
        allCourse.setOnCheckedChangeListener(allCourseCheckedChangeListener);
        freeCourse.setOnCheckedChangeListener(freeCourseCheckedChangeListener);
        giftCourse.setOnCheckedChangeListener(giftCourseCheckedChangeListener);
        vipCourse.setOnCheckedChangeListener(vipCourseCheckedChangeListener);
    }

    private CompoundButton.OnCheckedChangeListener newestCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                orderby = "ctime";
                isClear = true;
                mScrollLoader.onLoadFirstPage();
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener hotCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                orderby = "watchnum";
                isClear = true;
                mScrollLoader.onLoadFirstPage();
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener highOpinionCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                orderby = "usefulnum";
                isClear = true;
                mScrollLoader.onLoadFirstPage();
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener allCourseCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                subjectType = "";
                isClear = true;
                mScrollLoader.onLoadFirstPage();
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener freeCourseCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                subjectType = "&price=free";
                isClear = true;
                mScrollLoader.onLoadFirstPage();
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener giftCourseCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                subjectType = "&price=charge";
                isClear = true;
                mScrollLoader.onLoadFirstPage();
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener vipCourseCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                subjectType = "&subject=5&catid=0";
                isClear = true;
                mScrollLoader.onLoadFirstPage();
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
                mAdapter = new CourseListAdapter(getActivity().getApplication(), mList);
                mListView.setAdapter(mAdapter);// 解析成功 播放列表
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onError(AbstractRequest<UmiwiListResult<CourseListModel>> requet,
                            int statusCode, String body) {
            mScrollLoader.showLoadErrorView();
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
    public void onLoadData(int page) {
        if (TextUtils.isEmpty(url)) {
            mScrollLoader.showContentView("链接错误,请重新打开");
            onClickFooterFinishView();
            return;
        }
        String urlstr = String.format(url + "&orderby=%s%s&p=%s", orderby, subjectType, page);
        GetRequest<UmiwiListResult<CourseListModel>> request = new GetRequest<UmiwiListResult<CourseListModel>>(
                urlstr, UmiwiListParser.class,
                CourseListModel.class, listener);
        request.go();
    }


    @Override
    public void onLoadData() {
    }

    @Override
    public void customScrollInChange() {
    }

}
