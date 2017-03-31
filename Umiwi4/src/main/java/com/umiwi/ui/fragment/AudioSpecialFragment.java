package com.umiwi.ui.fragment;

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
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.ColumnAdapter;
import com.umiwi.ui.beans.updatebeans.HomeColumnBean;
import com.umiwi.ui.fragment.home.alreadyshopping.LogicalThinkingFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.ColumnDetailsFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.view.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * Created by Administrator on 2017/3/24.
 */

public class AudioSpecialFragment extends BaseConstantFragment implements View.OnClickListener {
    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.record)
    ImageView record;
    private List<HomeColumnBean.RhomeCoulum.HomeColumnInfo> mList;
    private ColumnAdapter columnAdapter;
    private int page = 1;
    private boolean isla = false;
    private boolean isload = false;
    private int totalpage;
    private AnimationDrawable background;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.audio_special_layout,null);
        mList = new ArrayList<>();
        ButterKnife.inject(this,view);

        columnAdapter = new ColumnAdapter(getActivity());
        columnAdapter.setData(mList);
        listview.setAdapter(columnAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                boolean isbuy = mList.get(i).getIsbuy();
                if (isbuy) {
                    Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LogicalThinkingFragment.class);
                    intent.putExtra("id", mList.get(i).getId());
                    intent.putExtra("title", mList.get(i).getTitle());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ColumnDetailsFragment.class);//详情页
                    intent.putExtra("columnurl", mList.get(i).getColumnurl());
                    Log.e("TAG", "mList.get(i).getColumnurl()=" + mList.get(i).getColumnurl());
                    startActivity(intent);
                }
            }
        });
        initrefreshLayout();
        getInfos();
        back.setOnClickListener(this);
        initMediaPlay();
        return view;
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
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        });
    }

    private void getInfos() {
        String url = UmiwiAPI.TUTORCOLUMN + page;
        Log.e("TAG", "homecoulmUrl=" + url);
        GetRequest<HomeColumnBean> request = new GetRequest<HomeColumnBean>(url, GsonParser.class, HomeColumnBean.class, new AbstractRequest.Listener<HomeColumnBean>() {
            @Override
            public void onResult(AbstractRequest<HomeColumnBean> request, HomeColumnBean homeColumnBean) {
                HomeColumnBean.RhomeCoulum.PageBean page = homeColumnBean.getR().getPage();
                totalpage = page.getTotalpage();
                ArrayList<HomeColumnBean.RhomeCoulum.HomeColumnInfo> record = homeColumnBean.getR().getRecord();
                mList.clear();
                mList.addAll(record);
                columnAdapter.setData(mList);

                if (isla) {
                    listview.setEnabled(true);
                    isla = false;
                    refreshLayout.setRefreshing(false);
                } else if (isload) {
                    isload = false;
                    refreshLayout.setLoading(false);
                }
                Log.e("homecoulm", "size" + record.size() + "page" + totalpage);
            }

            @Override
            public void onError(AbstractRequest<HomeColumnBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }
    private void initrefreshLayout() {
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.main_color));
        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                isload = true;
                page++;
                if (page <= totalpage) {
                    getInfos();
                } else {
                    refreshLayout.setLoading(false);
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listview.setEnabled(false);
                isla = true;
                page = 1;
//                mList.clear();

                getInfos();
            }
        });
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        String url = UmiwiAPI.TUTORCOLUMN + 1;
//
//        GetRequest<HomeColumnBean> request = new GetRequest<HomeColumnBean>(url,GsonParser.class,HomeColumnBean.class,onResumeListener);
//        request.go();
//        MobclickAgent.onPageStart(fragmentName);
//    }
//    private AbstractRequest.Listener<HomeColumnBean> onResumeListener = new AbstractRequest.Listener<HomeColumnBean>() {
//        @Override
//        public void onResult(AbstractRequest<HomeColumnBean> request, HomeColumnBean homeColumnBean) {
//            ArrayList<HomeColumnBean.RhomeCoulum.HomeColumnInfo> record = homeColumnBean.getR().getRecord();
//            mList.clear();
//            mList.addAll(record);
//            columnAdapter.setData(mList);
//        }
//
//        @Override
//        public void onError(AbstractRequest<HomeColumnBean> requet, int statusCode, String body) {
//
//        }
//    };

    @Override
    public void onResume() {
        super.onResume();
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

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageStart(fragmentName);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View view) {
        getActivity().finish();
    }
}
