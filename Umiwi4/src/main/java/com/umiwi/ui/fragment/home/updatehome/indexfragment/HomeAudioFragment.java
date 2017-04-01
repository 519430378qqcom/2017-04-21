package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.AlreadyVoiceAdapter;
import com.umiwi.ui.beans.VideoHeadBean;
import com.umiwi.ui.beans.updatebeans.AlreadyShopVoiceBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.util.JsonUtil;
import com.umiwi.ui.view.FlowLayout;
import com.umiwi.ui.view.RefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ListViewQuickReturnScrollLoader;
import cn.youmi.framework.util.ToastU;

/**
 * Created by shang on 2017/3/13.
 * Detail:音频
 */
public class HomeAudioFragment extends BaseConstantFragment implements ListViewQuickReturnScrollLoader.QuickReturnOnScrollLoader {
    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @InjectView(R.id.ll_catid2)
    LinearLayout ll_catid2;
    @InjectView(R.id.tv_all_catid1)
    TextView tv_all_catid1;
    @InjectView(R.id.tv_all_catid2)
    TextView tv_all_catid2;
    @InjectView(R.id.tv_all_price)
    TextView tv_all_price;
    @InjectView(R.id.tv_all_orderby)
    TextView tv_all_orderby;
    @InjectView(R.id.flow_catid1)
    FlowLayout flow_catid1;
    @InjectView(R.id.flow_catid2)
    FlowLayout flow_catid2;
    @InjectView(R.id.flow_price)
    FlowLayout flow_price;
    @InjectView(R.id.flow_orderby)
    FlowLayout flow_orderby;

//    @InjectView(R.id.ll_visiable_or)
//    LinearLayout ll_visiable_or;

    private int page = 1;
    private int totalpage = 1;
    private ArrayList<AlreadyShopVoiceBean.RAlreadyVoice.Record> recordList = new ArrayList<>();
    private AlreadyVoiceAdapter alreadyVoiceAdapter;
    private Context mContext;
    private boolean isRefresh = true;

    private List<String> catid1List = new ArrayList<>();//一级分类
    private List<String> catid2List = new ArrayList<>();//二级分类
    private List<String> priceList = new ArrayList<>();//免费，收费，钻石专享
    private List<String> orderbyList = new ArrayList<>();//排序:最热,好评
    private List<String> catid1ListId = new ArrayList<>();//一级分类ID
    private List<String> catid2ListId = new ArrayList<>();//二级分类ID
    private List<String> priceListId = new ArrayList<>();//free,charge,diamond
    private List<String> orderbyListId = new ArrayList<>();//排序:watchnum,usefulnum

    private String catid1 = "";
    private String catid = "";
    private String price = "";
    private String orderby = "ctime";

    private List<VideoHeadBean> videoHeadBeanList = new ArrayList<>();
    private int touchSlop;
    private float mFirstY;
    private float mCurrentY;
    private int direction;
    private ObjectAnimator animator;
    private int height;
    private int width;
    private ListViewQuickReturnScrollLoader mScrollLoader;
    private LinearLayout ll_visiable_or;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_audio_layout, null);
        ButterKnife.inject(this, view);
        ll_visiable_or = (LinearLayout) view.findViewById(R.id.ll_visiable_or);
        mContext = getActivity();
        initRefreshLayout();
        alreadyVoiceAdapter = new AlreadyVoiceAdapter(getActivity());
        alreadyVoiceAdapter.setData(recordList);

        listview.setAdapter(alreadyVoiceAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlreadyShopVoiceBean.RAlreadyVoice.Record record = recordList.get(i);
                String hrefurl = record.getHrefurl();
                Log.e("hrefurl", hrefurl);
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
                intent.putExtra(VoiceDetailsFragment.KEY_DETAILURL, hrefurl);
                startActivity(intent);

            }
        });
        getCatidData();
        getinfos();
        initFlowData();
        initFlowPrice();
        initFlowOrderby();
        initScrollView();

        return view;
    }

    private void initScrollView() {
        //最小滑动距离
        touchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        listview.setOnTouchListener(mOnTouchListener);
    }

    private boolean mShow = false;
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN :
                    mFirstY = event.getY();

                    break;
                case MotionEvent.ACTION_MOVE:
                    mCurrentY = event.getY();
                    if(mCurrentY - mFirstY > touchSlop) {
                       direction = 0;//down

                    }else if(mFirstY - mCurrentY > touchSlop) {
                        direction = 1;//up

                    }
                    if(direction == 1) {
                        if(mShow) {
                            showAnim(1);
                            mShow = !mShow;
                        }
                    }else if(direction == 0) {
                        if(!mShow) {
                            showAnim(0);
                            mShow = !mShow;
                        }
                    }
                    break;
            }
            return false;
        }
    };
    private void showAnim(int flag) {

        if(animator != null && animator.isRunning()) {
            animator.cancel();
        }
        if (flag == 0) {
            animator = ObjectAnimator.ofFloat(ll_visiable_or, "translationY", ll_visiable_or.getTranslationY(), 0);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ll_visiable_or.setVisibility(View.VISIBLE);
                }
            });
        } else {
            animator = ObjectAnimator.ofFloat(ll_visiable_or,"translationY",ll_visiable_or.getTranslationY(),-ll_visiable_or.getHeight());
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ll_visiable_or.setVisibility(View.GONE);
                }
            });
        }
        animator.setDuration(200);
        animator.start();
    }

    /**
     * 初始price和orderby数据
     */
    private void initFlowData() {

        priceList.add("免费");
        priceList.add("收费");
        // priceList.add("钻石专享");
        priceListId.add("free");
        priceListId.add("charge");
        //priceListId.add("diamond");

        orderbyList.add("最热");
        orderbyList.add("好评");
        orderbyListId.add("watchnum");
        orderbyListId.add("usefulnum");

    }

    /**
     * 得到一级分类和二级分类的数据
     */
    private void getCatidData() {
        String URL = UmiwiAPI.audio_head;
        OkHttpUtils.get().url(URL).build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {
            }

            @Override
            public void onSucess(String data) {
                videoHeadBeanList.addAll(JsonUtil.json2BeanList(data, VideoHeadBean.class));
                getCatid1Data();
            }
        });
    }

    /**
     * 得到一级分类流部局数据
     */
    private void getCatid1Data() {
        catid1List.clear();
        catid1ListId.clear();
        for (int i = 0, j = videoHeadBeanList.size(); i < j; i++) {
            catid1List.add(videoHeadBeanList.get(i).getInfo().getName());
            catid1ListId.add(videoHeadBeanList.get(i).getInfo().getId());
        }

        initCatid1Flow();
    }

    /**
     * 得到化二级分类流部局数据
     */
    private void getCatid2Data(int position) {
        catid2List.clear();
        catid2ListId.clear();
        for (int i = 0, j = videoHeadBeanList.get(position).getSubtree().size(); i < j; i++) {
            catid2List.add(videoHeadBeanList.get(position).getSubtree().get(i).getName());
            catid2ListId.add(videoHeadBeanList.get(position).getSubtree().get(i).getId());
        }
        initCatid2Flow();
    }

    /**
     * 初始化一级分类流部局
     */
    private void initCatid1Flow() {
        flow_catid1.removeAllViews();
        for (int i = 0, j = catid1List.size(); i < j; i++) {
            final TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R
                    .layout.flow_text, flow_catid1, false);
            tv.setText(catid1List.get(i));
            tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
            tv_all_catid1.setTextColor(mContext.getResources().getColor(R.color.main_color));
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    catid1 = catid1ListId.get(finalI);
                    catid = catid1ListId.get(finalI);
                    ll_catid2.setVisibility(View.VISIBLE);
                    //获得二级分类数据
                    getCatid2Data(finalI);
                    isRefresh = true;
                    getinfos();
                    for (int i = 0, j = catid1List.size(); i < j; i++) {
                        TextView tv = (TextView) flow_catid1.getChildAt(i);
                        tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
                    }
                    tv.setTextColor(mContext.getResources().getColor(R.color.main_color));
                    tv_all_catid1.setTextColor(mContext.getResources().getColor(R.color.gray_a));
                }
            });
            flow_catid1.addView(tv);
        }

        tv_all_catid1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catid = "";
                catid1 = "";
                ll_catid2.setVisibility(View.GONE);
                isRefresh = true;
                getinfos();
                for (int i = 0, j = catid1List.size(); i < j; i++) {
                    TextView tv = (TextView) flow_catid1.getChildAt(i);
                    tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
                }
                tv_all_catid1.setTextColor(mContext.getResources().getColor(R.color.main_color));
            }
        });

    }


    /**
     * 初始化二级分类流部局
     */
    private void initCatid2Flow() {
        flow_catid2.removeAllViews();
        for (int i = 0, j = catid2List.size(); i < j; i++) {
            final TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R
                    .layout.flow_text, flow_catid2, false);
            tv.setText(catid2List.get(i));
            tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
            tv_all_catid2.setTextColor(mContext.getResources().getColor(R.color.main_color));
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    catid = catid2ListId.get(finalI);
                    isRefresh = true;
                    getinfos();
                    for (int i = 0, j = catid2List.size(); i < j; i++) {
                        TextView tv = (TextView) flow_catid2.getChildAt(i);
                        tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
                    }
                    tv.setTextColor(mContext.getResources().getColor(R.color.main_color));
                    tv_all_catid2.setTextColor(mContext.getResources().getColor(R.color.gray_a));
                }
            });
            flow_catid2.addView(tv);
        }

        tv_all_catid2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catid = catid1;
                isRefresh = true;
                getinfos();
                for (int i = 0, j = catid2List.size(); i < j; i++) {
                    TextView tv = (TextView) flow_catid2.getChildAt(i);
                    tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
                }
                tv_all_catid2.setTextColor(mContext.getResources().getColor(R.color.main_color));
            }
        });

    }

    /**
     * 初始化price流部局
     */
    private void initFlowPrice() {
        flow_price.removeAllViews();
        for (int i = 0, j = priceList.size(); i < j; i++) {
            final TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R
                    .layout.flow_text, flow_price, false);
            tv.setText(priceList.get(i));
            tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
            tv_all_price.setTextColor(mContext.getResources().getColor(R.color.main_color));
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    price = priceListId.get(finalI);
                    isRefresh = true;
                    getinfos();
                    for (int i = 0, j = priceList.size(); i < j; i++) {
                        TextView tv = (TextView) flow_price.getChildAt(i);
                        tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
                    }
                    tv.setTextColor(mContext.getResources().getColor(R.color.main_color));
                    tv_all_price.setTextColor(mContext.getResources().getColor(R.color.gray_a));
                }
            });
            flow_price.addView(tv);
        }

        tv_all_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = "";
                isRefresh = true;
                getinfos();
                for (int i = 0, j = priceList.size(); i < j; i++) {
                    TextView tv = (TextView) flow_price.getChildAt(i);
                    tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
                }
                tv_all_price.setTextColor(mContext.getResources().getColor(R.color.main_color));
            }
        });

    }

    /**
     * 初始化orderby流部局
     */
    private void initFlowOrderby() {
        flow_orderby.removeAllViews();
        for (int i = 0, j = orderbyList.size(); i < j; i++) {
            final TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R
                    .layout.flow_text, flow_orderby, false);
            tv.setText(orderbyList.get(i));
            tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
            tv_all_orderby.setTextColor(mContext.getResources().getColor(R.color.main_color));
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderby = orderbyListId.get(finalI);
                    isRefresh = true;
                    getinfos();
                    for (int i = 0, j = orderbyList.size(); i < j; i++) {
                        TextView tv = (TextView) flow_orderby.getChildAt(i);
                        tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
                    }
                    tv.setTextColor(mContext.getResources().getColor(R.color.main_color));
                    tv_all_orderby.setTextColor(mContext.getResources().getColor(R.color.gray_a));
                }
            });
            flow_orderby.addView(tv);
        }

        tv_all_orderby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderby = "ctime";
                isRefresh = true;
                getinfos();
                for (int i = 0, j = orderbyList.size(); i < j; i++) {
                    TextView tv = (TextView) flow_orderby.getChildAt(i);
                    tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
                }
                tv_all_orderby.setTextColor(mContext.getResources().getColor(R.color.main_color));
            }
        });
    }

    /**
     * 请求数据
     */
    private void getinfos() {
        String url = String.format(UmiwiAPI.Login_Audio, page, catid, price, orderby);
        Log.e("TAGurl", "url="+ url);
        GetRequest<AlreadyShopVoiceBean> request = new GetRequest<AlreadyShopVoiceBean>(
                url, GsonParser.class,
                AlreadyShopVoiceBean.class,
                AnswerListener);
        request.go();
    }

    private AbstractRequest.Listener<AlreadyShopVoiceBean> AnswerListener = new AbstractRequest.Listener<AlreadyShopVoiceBean>() {

        @Override
        public void onResult(AbstractRequest<AlreadyShopVoiceBean> request, AlreadyShopVoiceBean umiAnwebeans) {
            AlreadyShopVoiceBean.RAlreadyVoice.PageBean page = umiAnwebeans.getR().getPage();
            totalpage = page.getTotalpage();
            ArrayList<AlreadyShopVoiceBean.RAlreadyVoice.Record> record = umiAnwebeans.getR().getRecord();
            if (isRefresh) {
                refreshLayout.setRefreshing(false);
                recordList.clear();
            } else {
                refreshLayout.setLoading(false);
            }
            recordList.addAll(record);
            alreadyVoiceAdapter.setData(recordList);
        }

        @Override
        public void onError(AbstractRequest<AlreadyShopVoiceBean> requet, int statusCode, String body) {
            if (isRefresh) {
                refreshLayout.setRefreshing(false);
            } else {
                refreshLayout.setLoading(false);
            }
        }
    };

    private void initRefreshLayout() {
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.main_color));
        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                page++;
                isRefresh = false;
                if (page <= totalpage) {
                    refreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getinfos();
                        }
                    }, 1000);

                } else {
                    ToastU.showLong(mContext, "没有更多了!");
                    refreshLayout.setLoading(false);

                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                page = 1;
                getinfos();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void customScrollStateChanged(AbsListView view, int scrollState) {

    }
}




