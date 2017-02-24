package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.HomeMainActivity;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.LunboAdapter;
import com.umiwi.ui.adapter.updateadapter.NewfreeAdapter;
import com.umiwi.ui.beans.GiftBeans;
import com.umiwi.ui.beans.HomeADBeans;
import com.umiwi.ui.beans.UmiwiListBeans;
import com.umiwi.ui.beans.updatebeans.NewFree;
import com.umiwi.ui.fragment.GiftFragment;
import com.umiwi.ui.fragment.UserTestInfoFragment;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.fragment.setting.FeedbackFragment;
import com.umiwi.ui.http.parsers.ADParser;
import com.umiwi.ui.http.parsers.CourseListParser;
import com.umiwi.ui.http.parsers.newhttpparsers.NewFreeParser;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.NoticeManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.parsers.newparsers.NewFreeResult;
import com.umiwi.ui.util.CommonHelper;
import com.umiwi.ui.util.ManifestUtils;
import com.umiwi.ui.view.AutoViewPager;
import com.umiwi.ui.view.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.account.event.UserEvent;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.manager.ModelManager;
import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

/**
 * Created by LvDabing on 2017/2/16.
 * Email：lvdabing@lvshandian.com
 * Detail: 推荐
 */

public class RecommendFragment extends BaseConstantFragment {
    @InjectView(R.id.listView)
    ListView mListView;
    @InjectView(R.id.pull_to_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private View header;
    private ArrayList<UmiwiListBeans> mLunboList;
    private ImageView error_empty;
    private AutoViewPager mAutoViewPager;
    private CirclePageIndicator mIndicator;
    private LoadingFooter mLoadingFooter;
    private LunboAdapter mLunboAdapter;
    private boolean isLunboShow;
    private boolean isRecommendShow;
    private boolean isTopic;
    private ListViewScrollLoader mScrollLoader;

    private ArrayList<NewFree> mList;

    private NewfreeAdapter mAdapter;

    private SharedPreferences mSharedPreferences;

    private RelativeLayout topic_rl;

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

    /***
     *             行家推荐布局： expert_recommend_layout
     *   listview  最新免费的布局：new_free_item_layout
     *             行家回答： expert_answer_layout
     *             付费精选：pay_selected_item_layout
     *             优米大咖：youmi_big_shot_item_layout
     *             线下活动：line_action_item_layout
     *
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_layout, null);
        ButterKnife.inject(this, view);
        initheader(inflater);
        return view;
    }



    void initheader(LayoutInflater inflater) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        header = inflater.inflate(R.layout.fragment_home_recommend_gallery, null);
        mLunboList = new ArrayList<UmiwiListBeans>();
        error_empty = (ImageView) header.findViewById(R.id.image_sc);
        error_empty.setVisibility(View.GONE);
        mAutoViewPager = (AutoViewPager) header.findViewById(R.id.view_pager);
        ViewGroup.LayoutParams para = mAutoViewPager.getLayoutParams();
        para.width = DimensionUtil.getScreenWidth(getActivity());
        para.height = (para.width * 300) / 640;
        mAutoViewPager.setLayoutParams(para);

        mIndicator = (CirclePageIndicator) header.findViewById(R.id.indicator);

        mAutoViewPager.setAdapter(new LunboAdapter(getActivity(), mLunboList));
        mIndicator.setViewPager(mAutoViewPager);
        //TODO   删除了一些东西，出错原因可能在这
        mAutoViewPager.setStopScrollWhenTouch(false);


        refreshLayout.setOnRefreshListener(mOnRefreshListener);
        int color = getResources().getColor(R.color.umiwi_orange);
        refreshLayout.setColorSchemeColors(color, color, Color.YELLOW, Color.WHITE);

        mList = new ArrayList<NewFree>();
        mAdapter = new NewfreeAdapter(getActivity(), mList);


        mLoadingFooter = new LoadingFooter(getActivity());// 加载更多的view
        mListView.addHeaderView(header, null, false);
      //  mListView.addFooterView(mLoadingFooter.getView());
        mListView.setSelector(R.color.transparent);

        mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);// 初始化加载更多监听

        mListView.setAdapter(mAdapter);

        mListView.setOnScrollListener(mScrollLoader);// 添加加载更多到listveiw

        loadLunboData();


        mScrollLoader.onLoadFirstPage();// 初始化接口，加载第一页

        if (YoumiRoomUserManager.getInstance().isLogin() && !"yes".equals(YoumiRoomUserManager.getInstance().getUser().getUsertest())) {
            topic_rl = (RelativeLayout) header.findViewById(R.id.topic_rl);
            topic_rl.setVisibility(View.GONE);
            topic_rl.setOnClickListener(new View.OnClickListener() {
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
        mListView.setOnItemClickListener(itemClickListener);

    }

    AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
//					intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailLayoutFragments.class);
//					intent.putExtra(CourseDetailLayoutFragments.KEY_DETAIURL, listBeans.getDetailurl());
            intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
           // intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, mList.get(position).);
            //getActivity().startActivity(intent);
            Toast.makeText(getActivity(), "缺少跳转接口", Toast.LENGTH_SHORT).show();
        }
    };

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
    public void onLoadData() {
        super.onLoadData();
        //最新免费
        GetRequest<NewFreeResult<NewFree>> request = new GetRequest<NewFreeResult<NewFree>>(
                UmiwiAPI.NEW_FREE, NewFreeParser.class, NewFree.class, listener);
        request.go();

    }

    private AbstractRequest.Listener<NewFreeResult<NewFree>> listener = new AbstractRequest.Listener<NewFreeResult<NewFree>>() {

        @Override
        public void onResult(
                AbstractRequest<NewFreeResult<NewFree>> request,
                NewFreeResult<NewFree> t) {

            if (t == null) {// 主要用于防止服务器数据出错
                Log.i("youmilog", "返回值为空");
                mScrollLoader.showLoadErrorView("未知错误,请重试");
                return;
            }
            mList.clear();
            // 数据加载
            ArrayList<NewFree> charts = t.getItems();


            mList.addAll(charts);

            Log.i("youmilog", "最新免费数据长度：" + mList.size());
            if (mAdapter == null) {
                 mAdapter = new NewfreeAdapter(getActivity(), mList);
                 mListView.setAdapter(mAdapter);// 解析成功 播放列表
            } else {
                mAdapter.notifyDataSetChanged();
            }
            if (isTopic) {
                isTopic = false;
                // mListView.setSelection(2);
            }
            isRecommendShow = true;
            mLoadingFooter.setState(LoadingFooter.State.TheEndHint);
            refreshLayout.setRefreshing(false);
        }

        @Override
        public void onError(
                AbstractRequest<NewFreeResult<NewFree>> requet,
                int statusCode, String body) {
            isRecommendShow = false;
            mLoadingFooter.setState(LoadingFooter.State.Error);

            mLoadingFooter.getView().setOnClickListener(new View.OnClickListener() {
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
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AbstractRequest.Listener<HomeADBeans.HomeADBeansRequestData> adListener = new AbstractRequest.Listener<HomeADBeans.HomeADBeansRequestData>() {

        @Override
        public void onResult(AbstractRequest<HomeADBeans.HomeADBeansRequestData> request,
                             HomeADBeans.HomeADBeansRequestData t) {
            if (null != t) {
                getGiftData();
            }
        }

        @Override
        public void onError(AbstractRequest<HomeADBeans.HomeADBeansRequestData> requet,
                            int statusCode, String body) {

        }
    };

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
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AbstractRequest.Listener<GiftBeans.GiftRequestData> giftListener = new AbstractRequest.Listener<GiftBeans.GiftRequestData>() {

        @Override
        public void onResult(AbstractRequest<GiftBeans.GiftRequestData> request,
                             GiftBeans.GiftRequestData t) {
            if (t != null && t.isStatus()) {
                mSharedPreferences.getBoolean("isCanShowGift", false);
                mSharedPreferences.getBoolean("isShowGiftOnceAndNoToShowAgain", false);
                Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
                i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, GiftFragment.class);
                startActivity(i);
            }

        }

        @Override
        public void onError(AbstractRequest<GiftBeans.GiftRequestData> requet,
                            int statusCode, String body) {

        }
    };

    /**
     * 轮播数据
     */
    private void loadLunboData() {
        GetRequest<UmiwiListBeans.ChartsListRequestData> request = new GetRequest<UmiwiListBeans.ChartsListRequestData>(
                UmiwiAPI.VIDEO_LUNBO_NEW,
                CourseListParser.class, lunboListener);
        //+ CommonHelper.getChannelModelViesion()
        HttpDispatcher.getInstance().go(request);
        //request.go();
    }

    private AbstractRequest.Listener<UmiwiListBeans.ChartsListRequestData> lunboListener = new AbstractRequest.Listener<UmiwiListBeans.ChartsListRequestData>() {

        @Override
        public void onResult(AbstractRequest<UmiwiListBeans.ChartsListRequestData> request, UmiwiListBeans.ChartsListRequestData t) {
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
        public void onError(AbstractRequest<UmiwiListBeans.ChartsListRequestData> requet, int statusCode, String body) {
            isLunboShow = false;
            error_empty.setVisibility(View.VISIBLE);
            error_empty.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.refresh_network));
            error_empty.setOnClickListener(new View.OnClickListener() {

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


    private void loginupate() {
        mScrollLoader.onLoadFirstPageAndScrollToTop(mListView);
    }


    ModelManager.ModelStatusListener<UserEvent, UserModel> userListener = new ModelManager.ModelStatusListener<UserEvent, UserModel>() {

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        YoumiRoomUserManager.getInstance().unregisterListener(userListener);
    }

    //TODO  解决推荐页添加轮播图崩溃尝试



}
