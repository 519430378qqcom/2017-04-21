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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.AudioVideoAdapter;
import com.umiwi.ui.beans.updatebeans.AudioVideoBean;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.view.FlowLayout;
import com.umiwi.ui.view.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ToastU;


/**
 * Created by Administrator on 2017/3/30 0030.
 */

public class StartBusinessFragment extends BaseConstantFragment {

    @InjectView(R.id.tv_all_catid1)
    TextView tv_all_catid1;
    @InjectView(R.id.flow_catid1)
    FlowLayout flow_catid1;
    @InjectView(R.id.tv_all_price)
    TextView tv_all_price;
    @InjectView(R.id.flow_price)
    FlowLayout flow_price;
    @InjectView(R.id.tv_all_orderby)
    TextView tv_all_orderby;
    @InjectView(R.id.flow_orderby)
    FlowLayout flow_orderby;
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @InjectView(R.id.listview)
    ListView listview;
    private int page = 1;
    private boolean isRefresh = true;
    private int totalpage = 1;
    private Context mContext;

    private List<String> catid1List = new ArrayList<>();//一级分类
    private List<String> priceList = new ArrayList<>();//音频，视频，
    private List<String> orderbyList = new ArrayList<>();//排序:最热,价格
    private List<String> catid1ListId = new ArrayList<>();//一级分类ID
    private List<String> priceListId = new ArrayList<>();//audio,video
    private List<String> orderbyListId = new ArrayList<>();//排序:ctime,watchnum,price
    private String catid1 = "";
    private String catid = "";
    private String type = "";
    private String price = "";
    private String orderby = "ctime";
    private ArrayList<RecommendBean.RBean.TagsBean.SubTagBean> mList = new ArrayList<>();
    private ArrayList<AudioVideoBean.RAUdioVideo.AudioVideoList> audioVideoList = new ArrayList<>();
    private AudioVideoAdapter audioVideoAdapter;
    private LinearLayout ll_visiable_or;
    private int touchSlop;
    private float mFirstY;
    private float mCurrentY;
    private int direction;
    private ObjectAnimator animator;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_startbus_layout, null);
        ButterKnife.inject(this, view);
        mContext = getActivity();
        ll_visiable_or = (LinearLayout) view.findViewById(R.id.ll_visiable_or);
        initRefreshLayout();
        audioVideoAdapter = new AudioVideoAdapter(getActivity());
        audioVideoAdapter.setData(audioVideoList);
        listview.setAdapter(audioVideoAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AudioVideoBean.RAUdioVideo.AudioVideoList audioVideoList = StartBusinessFragment.this.audioVideoList.get(position);
                String type = audioVideoList.getType();
                if ("视频".equals(type)) {
                    String hrefurl = audioVideoList.getUrl();
                    Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                    intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, hrefurl);
                    startActivity(intent);
                } else {
                    String hrefurl = audioVideoList.getUrl();
                    Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
                    intent.putExtra(VoiceDetailsFragment.KEY_DETAILURL, hrefurl);
                    startActivity(intent);
                }
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
    private boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition;// 标记上次滑动位置
    private void initScrollView() {
        //最小滑动距离
        touchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        listview.setOnTouchListener(mOnTouchListener);

    }


    private boolean mShow = true;
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN :
                    mFirstY = event.getY();
                    Log.e("TAG", "mFirstY=" + mFirstY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mCurrentY = event.getY();
                    Log.e("TAG", "mCurrentY=" + mCurrentY);
                    if(mCurrentY - mFirstY > touchSlop) {
                        direction = 0;//down
                    }else if(mFirstY - mCurrentY > touchSlop) {
                        direction = 1;//up
                    }
                    break;
                case MotionEvent.ACTION_UP:
//                    mCurrentY = event.getY();
//                    Log.e("TAG", "mCurrentY=" + mCurrentY);
//                    if(mCurrentY - mFirstY > 0) {
//                        direction = 0;//down
//                    }else if(mFirstY - mCurrentY > 0) {
//                        direction = 1;//up
//                    }
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
            Log.e("TAG", "下滑");
            animator = ObjectAnimator.ofFloat(ll_visiable_or, "translationY", ll_visiable_or.getTranslationY(), 0);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ll_visiable_or.setVisibility(View.VISIBLE);
                }
            });
        }
        if(flag == 1) {
            Log.e("TAG", "上滑");
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

    //请求列表数据
    private void getinfos() {
        String url = String.format(UmiwiAPI.UMIWI_BUS_WORK_TEND, page, catid, type, price, orderby);
//        Log.e("TAG", "url12121=" + url);
        GetRequest<AudioVideoBean> request = new GetRequest<AudioVideoBean>(url, GsonParser.class, AudioVideoBean.class, new AbstractRequest.Listener<AudioVideoBean>() {
            @Override
            public void onResult(AbstractRequest<AudioVideoBean> request, AudioVideoBean audioVideoBean) {
                ArrayList<AudioVideoBean.RAUdioVideo.AudioVideoList> audioVideoLists = audioVideoBean.getR().getRecord();
                if(audioVideoLists != null) {
                    totalpage= audioVideoBean.getR().getPage().getTotalpage();
                    if (isRefresh) {
                        refreshLayout.setRefreshing(false);
                        audioVideoList.clear();
                    } else {
                        refreshLayout.setLoading(false);
                    }
                    audioVideoList.addAll(audioVideoLists);
                    audioVideoAdapter.setData(audioVideoList);
//                    Log.e("TAG", "audioVideoBeanR=" + audioVideoLists.toString());
                }
            }

            @Override
            public void onError(AbstractRequest<AudioVideoBean> requet, int statusCode, String body) {
                if (isRefresh) {
                    refreshLayout.setRefreshing(false);
                } else {
                    refreshLayout.setLoading(false);
                }
            }
        });
        request.go();
    }

    /**
     * 得到一级分类数据
     */
    private void getCatidData() {
        GetRequest<RecommendBean> request = new GetRequest<>(
                UmiwiAPI.VIDEO_TUIJIAN, GsonParser.class, RecommendBean.class, new AbstractRequest.Listener<RecommendBean>() {
            @Override
            public void onResult(AbstractRequest<RecommendBean> request, RecommendBean recommendBean) {
                ArrayList<RecommendBean.RBean.TagsBean> tagsBeen = recommendBean.getR().getTags();
                for (int i = 0; i < tagsBeen.size(); i++) {
                    if ("创业".equals(tagsBeen.get(i).getCatname())) {
                        catid = tagsBeen.get(i).getCatid();
                        Log.e("TAG", "catid=" + catid);
                        ArrayList<RecommendBean.RBean.TagsBean.SubTagBean> subtag = tagsBeen.get(i).getSubtag();
                        mList.addAll(subtag);
                        getCatid1Data();
                    }
                }
            }

            @Override
            public void onError(AbstractRequest<RecommendBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    private void getCatid1Data() {
        catid1List.clear();
        catid1ListId.clear();
        for (int i = 0; i < mList.size(); i++) {
            catid1List.add(mList.get(i).getCatname());
            catid1ListId.add(mList.get(i).getCatid());
        }
        initCatid1Flow();
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
                isRefresh = true;
                getinfos();
                for (int i = 0, j = catid1List.size(); i < j; i++) {
                    TextView tv = (TextView) flow_catid1.getChildAt(i);
                    tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
                }
                tv_all_catid1.setTextColor(mContext.getResources().getColor(R.color.main_color));
            }});

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
                    type = priceListId.get(finalI);
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
                type = "";
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
     * 初始price和orderby数据
     */
    private void initFlowData() {

        priceList.add("音频");
        priceList.add("视频");
        // priceList.add("钻石专享");
        priceListId.add("audio");
        priceListId.add("album");
        //priceListId.add("diamond");

        orderbyList.add("最热");
        orderbyList.add("免费");
        orderbyList.add("价格");
        orderbyListId.add("watchnum");
        orderbyListId.add("free");
        orderbyListId.add("charge");

    }

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
}
