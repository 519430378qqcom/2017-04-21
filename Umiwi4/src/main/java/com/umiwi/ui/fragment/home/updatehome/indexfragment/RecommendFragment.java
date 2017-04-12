package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.fragment.GiftFragment;
import com.umiwi.ui.fragment.UserTestInfoFragment;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.fragment.home.recommend.widget.ExpertRecLayoutView;
import com.umiwi.ui.fragment.home.recommend.widget.FreeLayoutView;
import com.umiwi.ui.fragment.home.recommend.widget.HotVideoLayout;
import com.umiwi.ui.fragment.home.recommend.widget.LbumLayoutView;
import com.umiwi.ui.fragment.home.recommend.widget.PaySelectedLayoutViwe;
import com.umiwi.ui.http.parsers.ADParser;
import com.umiwi.ui.http.parsers.CourseListParser;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.NoticeManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.parsers.newparsers.NewFreeResult;
import com.umiwi.ui.util.CommonHelper;
import com.umiwi.ui.util.ManifestUtils;
import com.umiwi.ui.view.MyViewpager;
import com.umiwi.ui.view.VpSwipeRefreshLayout;

import java.lang.ref.WeakReference;
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

import static com.umiwi.ui.R.id.view_pager;
import static com.umiwi.ui.fragment.search.SearchFragment.SHOW_NEXT;


/**
 * Created by LvDabing on 2017/2/16.
 * Email：lvdabing@lvshandian.com
 * Detail: 推荐
 */

public class RecommendFragment extends BaseConstantFragment {
    @InjectView(R.id.listView)
    ListView mListView;
    @InjectView(R.id.pull_to_refresh_layout)
    VpSwipeRefreshLayout refreshLayout;
    @InjectView(R.id.sc_recomment_root)
    ScrollView sc_recomment_root;

    private View header;
    private ArrayList<UmiwiListBeans> mLunboList;
    private ImageView error_empty;
    private static MyViewpager mAutoViewPager;
//    private CirclePageIndicator mIndicator;
    private LoadingFooter mLoadingFooter;
    private LunboAdapter mLunboAdapter;
    private boolean isLunboShow;
    private boolean isRecommendShow;
    private boolean isTopic;
    private ListViewScrollLoader mScrollLoader;
    private FreeLayoutView flv_new_free;
    private HotVideoLayout hot_video_layout;
    private ExpertRecLayoutView erl_expert_rec;
//    private LineActionLayoutViwe lalv_action_line;
//    private ExpertAnswerLayoutViwe ealv_expert_answer;
//    private BigShotLayoutView bslv_big_shot;
//    private ExpertAnswerDwonLayoutViwe eadlv_expert_answer;
    private PaySelectedLayoutViwe pslv_pay_selected;
//    private RecommentBottomLayoutView rblv_bottom;
    private LbumLayoutView lbum_layout;
    private ArrayList<NewFree> mList;
    private LinearLayout ll_point;

    private NewfreeAdapter mAdapter;

    private SharedPreferences mSharedPreferences;

    private RelativeLayout topic_rl;









//    private android.os.Handler handler = new android.os.Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            int currentItem = mAutoViewPager.getCurrentItem();
//            currentItem++;
//            mAutoViewPager.setCurrentItem(currentItem);
//        }
//    };
    MyHandler handler=new  MyHandler(this);


    static  class  MyHandler extends Handler {
        WeakReference<Fragment> mActivityReference;

        MyHandler(Fragment fragment) {
            mActivityReference= new WeakReference<Fragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            int currentItem = mAutoViewPager.getCurrentItem();
            currentItem++;
            mAutoViewPager.setCurrentItem(currentItem);
        }
    }


    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
//            mLunboList.clear();
            mList.clear();
            mAdapter.notifyDataSetChanged();
            loadLunboData();
            getIndexAction();
//            mScrollLoader.onLoadFirstPage();
        }
    };

    /***
     * 行家推荐布局： expert_recommend_layout
     * listview  最新免费的布局：new_free_item_layout
     * 热门视频    hot_video_layout
     * 行家回答： expert_answer_layout
     * 付费精选：pay_selected_item_layout
     * 优米大咖：youmi_big_shot_item_layout
     * 线下活动：line_action_item_layout
     * 精选专题：lbum_list_view
    */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_layout, null);
        ButterKnife.inject(this, view);
        initView(view);
        initheader(inflater);
        loadRecommend();

        if (mLunboList != null && mLunboList.size() > 1) {
            mHandler.sendEmptyMessageDelayed(SHOW_NEXT, 5000);
        }
        return view;
    }

    /**
     * 初始化View
     */
    private void initView(View v) {
        flv_new_free = (FreeLayoutView) v.findViewById(R.id.flv_new_free);
        hot_video_layout = (HotVideoLayout) v.findViewById(R.id.hot_video_layout);
        //现在改为音频专题
        erl_expert_rec = (ExpertRecLayoutView) v.findViewById(R.id.erl_expert_rec);

//        lalv_action_line = (LineActionLayoutViwe) v.findViewById(lalv_action_line);
//        ealv_expert_answer = (ExpertAnswerLayoutViwe) v.findViewById(R.id.eav_expert_answer);
//        bslv_big_shot = (BigShotLayoutView) v.findViewById(R.id.bslv_big_shot);
//        eadlv_expert_answer = (ExpertAnswerDwonLayoutViwe) v.findViewById(eadlv_expert_answer);
        pslv_pay_selected = (PaySelectedLayoutViwe) v.findViewById(R.id.pslv_pay_selected);
//        rblv_bottom = (RecommentBottomLayoutView) v.findViewById(R.id.rblv_bottom);
        lbum_layout = (LbumLayoutView) v.findViewById(R.id.lbum_layout);
    }

    /**
     * 加载推荐页数据
     */
    private void loadRecommend() {
        getIndexAction();
    }

    /**
     * indexAction : 首页（6.8.0ok）
     */
    private void getIndexAction() {
        GetRequest<RecommendBean> request = new GetRequest<>(
                UmiwiAPI.VIDEO_TUIJIAN, GsonParser.class, RecommendBean.class, indexActionListener);
        request.go();

    }

    private AbstractRequest.Listener<RecommendBean> indexActionListener = new AbstractRequest.Listener<RecommendBean>() {

        @Override
        public void onResult(AbstractRequest<RecommendBean> request, RecommendBean t) {
//            Log.e("TAG", "t为" + t);
//            Log.e("TAG", "t=" + t.getR().getSec_free_title().toString());
            if (null != t) {

                flv_new_free.setData(t.getR().getFree(), t.getR().getSec_free_title(), t.getR().getSec_free_huan(),t.getR().getSec_free_huanurl());
                hot_video_layout.setData(t.getR().getHotvideo());
                erl_expert_rec.setData(t.getR().getTutor(), t.getR().getSec_tutor_title(), t.getR().getSec_tutor_more());
//                lalv_action_line.setData(t.getR().getHuodong(), t.getR().getSec_huodong_title());
//                ealv_expert_answer.setData(t.getR().getAsktutor(), t.getR().getSec_ask_title(), t.getR().getSec_ask_more());
//                bslv_big_shot.setData(NewHomeRecommendFragment.getRootViewpager(), sc_recomment_root, t.getR().getDalao(), t.getR().getSec_dalao_title());
//                eadlv_expert_answer.setData(t.getR().getQuestion(), t.getR().getSec_ask_quick());
                pslv_pay_selected.setData(t.getR().getCharge().getRecord(), t.getR().getSec_charge_title(), t.getR().getSec_charge_huan(),t.getR().getSec_charge_huanurl(),t.getR().getCharge().getPage().getTotalpage());
//                rblv_bottom.setData(getActivity(), t.getR().getBottom());
                lbum_layout.setData(t.getR().getAlbumlist());

            }
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onError(AbstractRequest<RecommendBean> requet, int statusCode, String body) {

        }
    };

    private int preSelectPosition;
    void initheader(LayoutInflater inflater) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        header = inflater.inflate(R.layout.fragment_home_recommend_gallery, null);
        mLunboList = new ArrayList<UmiwiListBeans>();
        error_empty = (ImageView) header.findViewById(R.id.image_sc);
        error_empty.setVisibility(View.GONE);
        mAutoViewPager = (MyViewpager) header.findViewById(view_pager);
        ll_point = (LinearLayout) header.findViewById(R.id.ll_point);

        ViewGroup.LayoutParams para = mAutoViewPager.getLayoutParams();
        para.width = DimensionUtil.getScreenWidth(getActivity());
        para.height = (para.width * 200) / 640;
        mAutoViewPager.setLayoutParams(para);

//        mIndicator = (CirclePageIndicator) header.findViewById(R.id.indicator);
//        mIndicator.setViewPager(mAutoViewPager);

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

        mAutoViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if(mLunboList != null && mLunboList.size() >0){
                    int currentPosition = position%mLunboList.size();
                    ll_point.getChildAt(preSelectPosition).setEnabled(false);
                    ll_point.getChildAt(currentPosition).setEnabled(true);
                    preSelectPosition = currentPosition;
                }

                position = position % mLunboList.size();
                for (int i = 0; i < ll_point.getChildCount(); i++) {
                    ImageView image = (ImageView) ll_point.getChildAt(i);
                    if (i == position) {
                        image.setImageResource(R.drawable.point_pressed);
                    } else {
                        image.setImageResource(R.drawable.point_normal);
                    }
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state ==ViewPager.SCROLL_STATE_DRAGGING){
                    handler.removeCallbacksAndMessages(null);
                }else if(state ==ViewPager.SCROLL_STATE_IDLE){
                    handler.removeCallbacksAndMessages(null);
                    handler.sendEmptyMessageDelayed(0, 5000);
                }else if(state ==ViewPager.SCROLL_STATE_SETTLING){
                    handler.removeCallbacksAndMessages(null);
                }
            }
        });

        mAutoViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        handler.removeCallbacksAndMessages(null);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        handler.removeCallbacksAndMessages(null);
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacksAndMessages(null);
                        handler.sendEmptyMessageDelayed(0, 5000);
                        break;
                }
                return false;
            }
        });

//        Log.e("TAG",mLunboList.size()+"-----------");
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
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
        //刷新行家数据状态 登录购买与否
        getIndexAction();
        if (mSharedPreferences.getBoolean("isCanShowGift", false) && !mSharedPreferences.getBoolean("isShowGiftOnceAndNoToShowAgain", false)) {
            getRegisterDevice();
        }
//        mAutoViewPager.startAutoScroll(5000);
        MobclickAgent.onPageStart(fragmentName);
    }

    @Override
    public void onPause() {
        super.onPause();
//        mAutoViewPager.stopAutoScroll();
        MobclickAgent.onPageEnd(fragmentName);
    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        //最新免费
     /*   GetRequest<NewFreeResult<NewFree>> request = new GetRequest<NewFreeResult<NewFree>>(
                UmiwiAPI.NEW_FREE, NewFreeParser.class, NewFree.class, listener);
        request.go();*/

    }

    private AbstractRequest.Listener<NewFreeResult<NewFree>> listener = new AbstractRequest.Listener<NewFreeResult<NewFree>>() {

        @Override
        public void onResult(
                AbstractRequest<NewFreeResult<NewFree>> request,
                NewFreeResult<NewFree> t) {

            if (t == null) {// 主要用于防止服务器数据出错
//                Log.i("youmilog", "返回值为空");
                mScrollLoader.showLoadErrorView("未知错误,请重试");
                return;
            }
            mList.clear();
            // 数据加载
            ArrayList<NewFree> charts = t.getItems();


            mList.addAll(charts);

//            Log.i("youmilog", "最新免费数据长度：" + mList.size());
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
            Log.e("TAG", "CommonHelper.getMacMD5()=" + CommonHelper.getMacMD5());
            Log.e("TAG", "ad_str=" + ad_str);
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
            Log.e("TAG", "HomeADBeans=" + t);
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
                mLunboList.clear();
                mLunboList = t.getRecord();
                ll_point.removeAllViews();
                for (int i = 0; i <mLunboList.size(); i++) {
                    ImageView point = new ImageView(getActivity());
                    if(i == 0){
                        point.setImageResource(R.drawable.point_pressed);
                    }else{
                        point.setImageResource(R.drawable.point_normal);
                    }

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,-2);
                    if(i != 0){
                        params.leftMargin = 10;//设置间距为10个像数
                    }
                    point.setLayoutParams(params);

                    //添加到线性布局
                    ll_point.addView(point);
                }

//                mAutoViewPager.setAdapter(mLunboAdapter);
                mAutoViewPager.setAdapter(new LunboAdapter(getActivity(), mLunboList));
//                mIndicator.setViewPager(mAutoViewPager);
//                mAutoViewPager.setInterval(5000);
//                mAutoViewPager.setSlideBorderMode(AutoViewPager.SLIDE_BORDER_MODE_CYCLE);// 循环。
                mAutoViewPager.setAnimation(new AlphaAnimation(1, (float) 0.2));
//                mAutoViewPager.startAutoScroll(5000);
                isLunboShow = true;
                refreshLayout.setRefreshing(false);
                handler.sendEmptyMessageDelayed(0, 5000);

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
//                    loadLunboData();
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
}
