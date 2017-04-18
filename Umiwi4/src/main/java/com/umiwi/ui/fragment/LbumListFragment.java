package com.umiwi.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.LbumlistFragmentAdapter;
import com.umiwi.ui.beans.updatebeans.LbumListBean;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
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
 * Created by Administrator on 2017/4/5 0005.
 */

public class LbumListFragment extends BaseConstantFragment implements View.OnClickListener {
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
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.record)
    ImageView record;
    @InjectView(R.id.pb_loading)
    ProgressBar pb_loading;
    private int page = 1;
    private boolean isRefresh = true;
    private int totalpage = 1;
    private Context mContext;
    private String catid = "";
    private String type = "";
    private String orderby = "new";
    private String pagenum = "";
    private List<String> catidList = new ArrayList<>();//分类
    private List<String> catidListId = new ArrayList<>();//分类id
    private List<String> typeList = new ArrayList<>();//音频 视频
    private List<String> typeListId = new ArrayList<>();//audio vedio
    private List<String> orderbyList = new ArrayList<>();//最热 价格
    private List<String> orderbyListId = new ArrayList<>();//hot price
    private ArrayList<RecommendBean.RBean.TagsBean> mList = new ArrayList<>();
    private ArrayList<LbumListBean.RLbumlist.LbumlistRecord> lbumlists = new ArrayList<>();
    private LbumlistFragmentAdapter lbAdapter;
    private AnimationDrawable background;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lbumlist_layout, null);
        ButterKnife.inject(this, view);
        mContext = getActivity();
        initRefreshLayout();
        lbAdapter = new LbumlistFragmentAdapter(getActivity());
        lbAdapter.setData(lbumlists);
        listview.setAdapter(lbAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String type = lbumlists.get(position).getType();
                String typeId = lbumlists.get(position).getId();
                if ("音频".equals(type)) {
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AudioSpecialDetailFragment.class);
                    intent.putExtra("typeId", typeId);
                    String toURI = intent.toURI();
                    Log.e("TAG", "音频详情toURI=" + toURI);
                    mContext.startActivity(intent);
                } else {
                    String detailurl = lbumlists.get(position).getDetailurl();
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VideoSpecialDetailFragment.class);
                    intent.putExtra("detailurl", detailurl);
                    mContext.startActivity(intent);
                }
            }
        });
        getCatIdData();
        initFlowData();
        initTypeData();
        initFlowOrderby();
        back.setOnClickListener(this);
        initMediaPlay();
        getinfos();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(UmiwiApplication.mainActivity != null) {
            if(UmiwiApplication.mainActivity.service != null) {
                background = (AnimationDrawable) record.getBackground();
                try {
                    if (UmiwiApplication.mainActivity.service.isPlaying()) {
                        background.start();
                    } else {
                        background.stop();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * "http://v.youmi.cn/ClientApi/getNewZhuanti2List?p=%s&type=%s&order=%s&pagenum=%s&catid=%s";
     */
    private void getinfos() {
        String url = String.format(UmiwiAPI.UMIWI_LBUMLIST, page, type, orderby, catid);
//        Log.e("TAG", "rLbumlistsurl=" + url);
        GetRequest<LbumListBean> request = new GetRequest<LbumListBean>(url, GsonParser.class, LbumListBean.class, new AbstractRequest.Listener<LbumListBean>() {
            @Override
            public void onResult(AbstractRequest<LbumListBean> request, LbumListBean lbumListBean) {

                ArrayList<LbumListBean.RLbumlist.LbumlistRecord> record = lbumListBean.getR().getRecord();
                if (record != null) {

                    totalpage = lbumListBean.getR().getPage().getTotalpage();
//                    Log.e("TAG", "rLbumlists=" + record);
                    if (isRefresh) {
                        refreshLayout.setRefreshing(false);
                        lbumlists.clear();
                    } else {
                        refreshLayout.setLoading(false);
                    }
                    lbumlists.addAll(record);
                    lbAdapter.setData(lbumlists);
                }
            }

            @Override
            public void onError(AbstractRequest<LbumListBean> requet, int statusCode, String body) {
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
     * 播放按钮
     */
    private void initMediaPlay() {
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UmiwiApplication.mainActivity.service != null) {
                    try {
                        if (UmiwiApplication.mainActivity.service.isPlaying() || UmiwiApplication.mainActivity.isPause) {
                            if (UmiwiApplication.mainActivity.herfUrl != null) {
                                Log.e("TAG", "UmiwiApplication.mainActivity.herfUrl=" + UmiwiApplication.mainActivity.herfUrl);
                                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
                                intent.putExtra(VoiceDetailsFragment.KEY_DETAILURL, UmiwiApplication.mainActivity.herfUrl);
                                getActivity().startActivity(intent);
                            }
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast toast = Toast.makeText(getActivity(), "没有正在播放的音频", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }

    //获取一级列表
    private void getCatIdData() {
        GetRequest<RecommendBean> request = new GetRequest<>(
                UmiwiAPI.VIDEO_TUIJIAN, GsonParser.class, RecommendBean.class, new AbstractRequest.Listener<RecommendBean>() {
            @Override
            public void onResult(AbstractRequest<RecommendBean> request, RecommendBean recommendBean) {
                ArrayList<RecommendBean.RBean.TagsBean> tagsBeen = recommendBean.getR().getTags();
                mList.addAll(tagsBeen);
                getCatid1Data();
                pb_loading.setVisibility(View.GONE);
            }

            @Override
            public void onError(AbstractRequest<RecommendBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    private void getCatid1Data() {
        catidList.clear();
        catidList.clear();
        for (int i = 0; i < mList.size(); i++) {
            catidList.add(mList.get(i).getCatname());
            catidListId.add(mList.get(i).getCatid());
        }
        initCatid1Flow();
    }

    private void initCatid1Flow() {
        flow_catid1.removeAllViews();
        for (int i = 0, j = catidList.size(); i < j; i++) {
            final TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R
                    .layout.flow_text, flow_catid1, false);
            tv.setText(catidList.get(i));
            tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
            tv_all_catid1.setTextColor(mContext.getResources().getColor(R.color.main_color));
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    catid = catidListId.get(finalI);
                    isRefresh = true;
                    getinfos();
                    for (int i = 0, j = catidList.size(); i < j; i++) {
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
                isRefresh = true;
                getinfos();
                for (int i = 0, j = catidList.size(); i < j; i++) {
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
                orderby = "new";
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

    //初始化type流布局
    private void initTypeData() {
        flow_price.removeAllViews();
        for (int i = 0; i < typeList.size(); i++) {
            final TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R
                    .layout.flow_text, flow_price, false);
            tv.setText(typeList.get(i));
            tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
            tv_all_price.setTextColor(mContext.getResources().getColor(R.color.main_color));
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    type = typeListId.get(finalI);
                    isRefresh = true;
                    getinfos();
                    for (int i = 0, j = typeList.size(); i < j; i++) {
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
                for (int i = 0, j = typeList.size(); i < j; i++) {
                    TextView tv = (TextView) flow_price.getChildAt(i);
                    tv.setTextColor(mContext.getResources().getColor(R.color.gray_a));
                }
                tv_all_price.setTextColor(mContext.getResources().getColor(R.color.main_color));
            }
        });
    }

    /**
     * 初始type和orderby数据
     */
    private void initFlowData() {

        typeList.add("音频专题");
        typeList.add("视频专题");
        typeListId.add("audio");
        typeListId.add("video");

        orderbyList.add("最热");
        orderbyList.add("价格");
        orderbyListId.add("hot");
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
    public void onClick(View v) {
        getActivity().finish();
    }
}
