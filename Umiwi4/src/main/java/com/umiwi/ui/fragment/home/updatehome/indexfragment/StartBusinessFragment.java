package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
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
    private List<String> orderbyListId = new ArrayList<>();//排序:watchnum,price
    private String catid1 = "";
    private String catid = "";
    private String type = "";
    private String orderby = "ctime";
    private ArrayList<RecommendBean.RBean.TagsBean.SubTagBean> mList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_startbus_layout,null);
        ButterKnife.inject(this,view);
        mContext = getActivity();

        initRefreshLayout();


        getCatidData();
        initFlowData();
        initFlowPrice();
        initFlowOrderby();
        return view;
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
                for (int i = 0;i <tagsBeen.size(); i ++) {
                    if("创业".equals(tagsBeen.get(i).getCatname())) {
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
        for (int i =0; i < mList.size(); i++) {
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
//                    getinfos();
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
//                getinfos();
                for (int i = 0, j = catid1List.size(); i < j; i++) {
                    TextView tv = (TextView) flow_catid1.getChildAt(i);
                    tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
                }
                tv_all_catid1.setTextColor(mContext.getResources().getColor(R.color.main_color));
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
//                    getinfos();
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
//                getinfos();
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
//                    getinfos();
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
//                getinfos();
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
        priceListId.add("video");
        //priceListId.add("diamond");

        orderbyList.add("最热");
        orderbyList.add("价格");
        orderbyListId.add("watchnum");
        orderbyListId.add("price");

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
//                            getinfos();
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
//                getinfos();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
