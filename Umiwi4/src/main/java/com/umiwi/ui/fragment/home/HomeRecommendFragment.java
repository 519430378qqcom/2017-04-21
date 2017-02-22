package com.umiwi.ui.fragment.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.HomeMainActivity;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.HomeRecommendAdapter;
import com.umiwi.ui.adapter.LunboAdapter;
import com.umiwi.ui.beans.GiftBeans;
import com.umiwi.ui.beans.GiftBeans.GiftRequestData;
import com.umiwi.ui.beans.HomeADBeans;
import com.umiwi.ui.beans.HomeADBeans.HomeADBeansRequestData;
import com.umiwi.ui.beans.UmiwiListBeans;
import com.umiwi.ui.beans.UmiwiListBeans.ChartsListRequestData;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.fragment.GiftFragment;
import com.umiwi.ui.fragment.UserTestInfoFragment;
import com.umiwi.ui.fragment.down.DownloadedListFragment;
import com.umiwi.ui.fragment.mine.RecordFragment;
import com.umiwi.ui.fragment.search.SearchFragment;
import com.umiwi.ui.http.parsers.ADParser;
import com.umiwi.ui.http.parsers.CourseListParser;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.NoticeManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.HomeRecommend;
import com.umiwi.ui.parsers.HomeRecommendParser;
import com.umiwi.ui.parsers.HomeRecommendResult;
import com.umiwi.ui.util.CommonHelper;
import com.umiwi.ui.util.LoginUtil;
import com.umiwi.ui.util.ManifestUtils;
import com.umiwi.ui.view.AutoViewPager;
import com.umiwi.ui.view.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.account.event.UserEvent;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

/**
 * @author tangxiyong
 * @version 2015-6-9 下午6:34:12
 */
public class HomeRecommendFragment extends BaseConstantFragment {

    private ListView mListView;
    private HomeRecommendAdapter mAdapter;
    private ArrayList<HomeRecommend> mList;

    private LoadingFooter mLoadingFooter;
    private ListViewScrollLoader mScrollLoader;

    private View header;

    private ImageView error_empty;
    private AutoViewPager mAutoViewPager;
    private LunboAdapter mLunboAdapter;
    private CirclePageIndicator mIndicator;
    private ArrayList<UmiwiListBeans> mLunboList;

    private SharedPreferences mSharedPreferences;

    private RelativeLayout topic_rl;

    private boolean isLunboShow;
    private boolean isRecommendShow;
    private boolean isTopic;

    private SwipeRefreshLayout refreshLayout;

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mLunboList.clear();
            mList.clear();
            mAdapter.notifyDataSetChanged();
            loadLunboData();
            mScrollLoader.onLoadFirstPage();
        }
    };

    @SuppressLint("InflateParams")
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_recommend, null);

        header = inflater.inflate(R.layout.fragment_home_recommend_gallery, null);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        mActionBarToolbar.setTitle(null);
//		mActionBarToolbar.inflateMenu(R.menu.toolbar_home);
        view.findViewById(R.id.search).setOnClickListener(menuItemClickListener);
        view.findViewById(R.id.download).setOnClickListener(menuItemClickListener);
        view.findViewById(R.id.record).setOnClickListener(menuItemClickListener);

        mLunboList = new ArrayList<UmiwiListBeans>();

        error_empty = (ImageView) header.findViewById(R.id.image_sc);
        error_empty.setVisibility(View.GONE);
        mAutoViewPager = (AutoViewPager) header.findViewById(R.id.view_pager);

        LayoutParams para = mAutoViewPager.getLayoutParams();
        para.width = DimensionUtil.getScreenWidth(getActivity());
        para.height = (para.width * 300) / 640;

        mAutoViewPager.setLayoutParams(para);

        mIndicator = (CirclePageIndicator) header.findViewById(R.id.indicator);

        mAutoViewPager.setAdapter(new LunboAdapter(getActivity(), mLunboList));
        mIndicator.setViewPager(mAutoViewPager);
        mAutoViewPager.setStopScrollWhenTouch(false);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh_layout);//如果需要下拉刷新
        refreshLayout.setOnRefreshListener(mOnRefreshListener);
        int color = getResources().getColor(R.color.umiwi_orange);
        refreshLayout.setColorSchemeColors(color, color, Color.YELLOW, Color.WHITE);

        mList = new ArrayList<HomeRecommend>();
        mListView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new HomeRecommendAdapter(getActivity(), mList);

        mLoadingFooter = new LoadingFooter(getActivity());// 加载更多的view
        mListView.addHeaderView(header, null, false);
        mListView.addFooterView(mLoadingFooter.getView());
        mListView.setSelector(R.color.transparent);

        mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);// 初始化加载更多监听

        mListView.setAdapter(mAdapter);

        mListView.setOnScrollListener(mScrollLoader);// 添加加载更多到listveiw

        loadLunboData();
        mScrollLoader.onLoadFirstPage();// 初始化接口，加载第一页

        if (YoumiRoomUserManager.getInstance().isLogin() && !"yes".equals(YoumiRoomUserManager.getInstance().getUser().getUsertest())) {
            topic_rl = (RelativeLayout) header.findViewById(R.id.topic_rl);
            topic_rl.setVisibility(View.GONE);
            topic_rl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(),
                            UmiwiContainerActivity.class);
                    i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS,
                            UserTestInfoFragment.class);
                    i.putExtra(UserTestInfoFragment.URL_CATEGORY,
                            UmiwiAPI.USER_TEST_COURSE);
                    startActivity(i);
                    MobclickAgent.onEvent(getActivity(), "首页VI", "测试题");
                }
            });
        }

        if (YoumiRoomUserManager.getInstance().isLogin()) {
            NoticeManager.getInstance().loadNotice();
        }

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
        if (mSharedPreferences.getBoolean("isCanShowGift", false) && !mSharedPreferences.getBoolean("isShowGiftOnceAndNoToShowAgain", false)) {
            getRegisterDevice();
        }
        mAutoViewPager.startAutoScroll(5000);
        MobclickAgent.onPageStart(fragmentName);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAutoViewPager.stopAutoScroll();
        MobclickAgent.onPageEnd(fragmentName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        YoumiRoomUserManager.getInstance().unregisterListener(userListener);
    }


    View.OnClickListener menuItemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
            switch (v.getId()) {
                case R.id.search:
                    i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, SearchFragment.class);
                    break;
                case R.id.download:
                    i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, DownloadedListFragment.class);
                    break;
                case R.id.record:
                    if (YoumiRoomUserManager.getInstance().isLogin()) {
                        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, RecordFragment.class);
                    } else {
                        LoginUtil.getInstance().showLoginView(getActivity());
                        return;
                    }
                    break;

                default:
                    break;
            }
            startActivity(i);
        }
    };

    /**
     * 轮播数据
     */
    private void loadLunboData() {

        GetRequest<UmiwiListBeans.ChartsListRequestData> request = new GetRequest<UmiwiListBeans.ChartsListRequestData>(
                UmiwiAPI.VIDEO_LUNBO + CommonHelper.getChannelModelViesion(),
                CourseListParser.class, lunboListener);
        HttpDispatcher.getInstance().go(request);
    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        GetRequest<HomeRecommendResult<HomeRecommend>> request = new GetRequest<HomeRecommendResult<HomeRecommend>>(
                UmiwiAPI.HOME_INDEX, HomeRecommendParser.class,
                HomeRecommend.class, listener);
        request.go();

    }

    /**
     * 注册设备
     */
    private void getRegisterDevice() {
        String ad_str;
        try {
            ad_str = String.format(UmiwiAPI.ADANDSILVERCOOKIE,
                    CommonHelper.getMacMD5(), ManifestUtils.getChannelString(getActivity()), CommonHelper.getModel(), CommonHelper.getVersionName());
            GetRequest<HomeADBeans.HomeADBeansRequestData> request = new GetRequest<HomeADBeans.HomeADBeansRequestData>(
                    ad_str, ADParser.class, adListener);
            HttpDispatcher.getInstance().go(request);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 欢乐大礼包
     */
    private void getGiftData() {
        String ad_str;
        try {
            ad_str = String
                    .format(UmiwiAPI.APP_GIFT,
                            CommonHelper.getVersionName(), CommonHelper.getMacMD5(), ManifestUtils.getChannelString(getActivity()));
            GetRequest<GiftBeans.GiftRequestData> request = new GetRequest<GiftBeans.GiftRequestData>(
                    ad_str, GsonParser.class, GiftBeans.GiftRequestData.class, giftListener);
            request.go();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loginupate() {
        mScrollLoader.onLoadFirstPageAndScrollToTop(mListView);
    }

    private Listener<HomeADBeans.HomeADBeansRequestData> adListener = new Listener<HomeADBeans.HomeADBeansRequestData>() {

        @Override
        public void onResult(AbstractRequest<HomeADBeansRequestData> request,
                             HomeADBeansRequestData t) {
            if (null != t) {
                getGiftData();
            }
        }

        @Override
        public void onError(AbstractRequest<HomeADBeansRequestData> requet,
                            int statusCode, String body) {

        }
    };

    private Listener<GiftBeans.GiftRequestData> giftListener = new Listener<GiftBeans.GiftRequestData>() {

        @Override
        public void onResult(AbstractRequest<GiftRequestData> request,
                             GiftRequestData t) {
            if (t != null && t.isStatus()) {
                mSharedPreferences.getBoolean("isCanShowGift", false);
                mSharedPreferences.getBoolean("isShowGiftOnceAndNoToShowAgain", false);
                Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
                i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, GiftFragment.class);
                startActivity(i);
            }

        }

        @Override
        public void onError(AbstractRequest<GiftRequestData> requet,
                            int statusCode, String body) {

        }
    };

    private Listener<ChartsListRequestData> lunboListener = new Listener<UmiwiListBeans.ChartsListRequestData>() {

        @Override
        public void onResult(AbstractRequest<ChartsListRequestData> request, ChartsListRequestData t) {
            if (null != t && null != t.getRecord()) {
                mLunboAdapter = new LunboAdapter(getActivity(), t.getRecord());
                mAutoViewPager.setAdapter(mLunboAdapter);
                mIndicator.setViewPager(mAutoViewPager);
                mAutoViewPager.setInterval(5000);
                mAutoViewPager.setSlideBorderMode(AutoViewPager.SLIDE_BORDER_MODE_CYCLE);// 循环。
                mAutoViewPager.setAnimation(new AlphaAnimation(1, (float) 0.2));
                mAutoViewPager.startAutoScroll(5000);
                isLunboShow = true;
                refreshLayout.setRefreshing(false);
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onError(AbstractRequest<ChartsListRequestData> requet, int statusCode, String body) {
            isLunboShow = false;
            error_empty.setVisibility(View.VISIBLE);
            error_empty.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.refresh_network));
            error_empty.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    loadLunboData();
                    error_empty.setVisibility(View.GONE);
                    if (!isRecommendShow) {
                        mScrollLoader.onLoadErrorPage();
                    }
                }
            });

            refreshLayout.setRefreshing(false);
        }
    };

    private Listener<HomeRecommendResult<HomeRecommend>> listener = new Listener<HomeRecommendResult<HomeRecommend>>() {

        @Override
        public void onResult(
                AbstractRequest<HomeRecommendResult<HomeRecommend>> request,
                HomeRecommendResult<HomeRecommend> t) {
            if (t == null) {// 主要用于防止服务器数据出错
                mScrollLoader.showLoadErrorView("未知错误,请重试");
                return;
            }
            mList.clear();
            // 数据加载
            ArrayList<HomeRecommend> charts = t.getItems();
            mList.addAll(charts);

            if (mAdapter == null) {
                mAdapter = new HomeRecommendAdapter(getActivity(), mList);
                mListView.setAdapter(mAdapter);// 解析成功 播放列表
            } else {
                mAdapter.notifyDataSetChanged();
            }
            if (isTopic) {
                isTopic = false;
                mListView.setSelection(2);
            }
            isRecommendShow = true;
            mLoadingFooter.setState(LoadingFooter.State.TheEndHint);
            refreshLayout.setRefreshing(false);
        }

        @Override
        public void onError(
                AbstractRequest<HomeRecommendResult<HomeRecommend>> requet,
                int statusCode, String body) {
            isRecommendShow = false;
            mLoadingFooter.setState(LoadingFooter.State.Error);
            mLoadingFooter.getView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mScrollLoader.onLoadErrorPage();
                    if (!isLunboShow) {
                        loadLunboData();
                    }
                }
            });
            refreshLayout.setRefreshing(false);
        }
    };

    ModelStatusListener<UserEvent, UserModel> userListener = new ModelStatusListener<UserEvent, UserModel>() {

        @Override
        public void onModelGet(UserEvent key, UserModel models) {
        }

        @Override
        public void onModelUpdate(UserEvent key, UserModel model) {
            HomeMainActivity instance = (HomeMainActivity) getActivity();
            switch (key) {
                case LOGGED_KICK_OUT:
                    loginupate();
                    break;
                case HOME_LOGIN:
                    loginupate();
                    instance.PushString();
                    break;
                case HOME_TESTINFO:

                    isTopic = true;
                    mScrollLoader.onLoadFirstPage();
                    break;
                case HOME_CHANGE_PASSWORD:
                    loginupate();
                    instance.PushString();
                    break;
                case HOME_RESET_PASSWORD:
                    loginupate();
                    instance.PushString();
                    break;
                case HOME_CHANGE_NAME_AND_PASSWORD:
                    loginupate();
                    instance.PushString();
                    break;
                case HOME_LOGIN_OUT:
                    loginupate();
                    break;
                case HOME_USER_REGISTE_SUCC_AND_COMMIT_INFO:
                    loginupate();
                    instance.PushString();
                    break;
                case HOME_WEBVIEW:
                    loginupate();
                    instance.PushString();
                    break;
                case USER_MOBILE_REGISTE_SUCC_NO_INFO:
                    loginupate();
                    instance.PushString();
                    break;
                case PAY_SUCC:
                    loginupate();
                    instance.PushString();
                    break;

                default:
                    break;
            }

        }

        @Override
        public void onModelsGet(UserEvent key, List<UserModel> models) {
        }

    };

}
