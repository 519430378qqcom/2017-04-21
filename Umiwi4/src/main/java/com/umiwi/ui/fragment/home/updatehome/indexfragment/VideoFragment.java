package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umiwi.ui.R;
import com.umiwi.ui.adapter.AudioAdapter;
import com.umiwi.ui.adapter.VideoAdapter;
import com.umiwi.ui.beans.AudioBean;
import com.umiwi.ui.beans.VideoBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

/**
 * Created by LvDabing on 2017/2/16.
 * Email：lvdabing@lvshandian.com
 * Detail:视频
 */

public class VideoFragment extends BaseConstantFragment {

    private TextView tv_all_one;
    private TextView tv_all_two;
    private TextView tv_all_three;
    private TextView tv_new;
    private TextView tv_host;
    private TextView tv_good;
    private TextView tv_free;
    private TextView tv_pay;

    private TextView tv_cy;
    private TextView tv_zc;
    private TextView tv_my;
    private TextView tv_ly;
    private TextView tv_jk;

    private TextView tv_qc;
    private TextView tv_xwcb;
    private TextView tv_xxjs;;
    private TextView tv_cyjd;
    private TextView tv_jdrj;
    private TextView tv_wlw;
    private TextView tv_ny;

    private ListView listView;
    private LoadingFooter mLoadingFooter;
    private ListViewScrollLoader mScrollLoader;

    private String tutoruid = "";//行家UID
    private String catid = "";//分类ID
    private String price = "";//free-免费，charge-收费
    private String orderby = "ctime";//ctime-最新，watchnum-最热,usefulnum-好评

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_layout, null);

        onLoadData(1);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tv_qc = (TextView) view.findViewById(R.id.tv_qc);
        tv_qc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_qc.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_xwcb.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_xxjs.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_cyjd.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_jdrj.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_wlw.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_ny.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_all_two.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
            }
        });
        tv_ny = (TextView) view.findViewById(R.id.tv_ny);
        tv_ny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_qc.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_xwcb.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_xxjs.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_cyjd.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_jdrj.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_wlw.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_ny.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_all_two.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
            }
        });
        tv_wlw = (TextView) view.findViewById(R.id.tv_wlw);
        tv_wlw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_qc.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_xwcb.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_xxjs.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_cyjd.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_jdrj.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_wlw.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_ny.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_all_two.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
            }
        });
        tv_jdrj = (TextView) view.findViewById(R.id.tv_jdrj);
        tv_jdrj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_qc.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_xwcb.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_xxjs.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_cyjd.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_jdrj.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_wlw.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_ny.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_all_two.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
            }
        });

        tv_xwcb = (TextView) view.findViewById(R.id.tv_xwcb);
        tv_xwcb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_qc.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_xwcb.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_xxjs.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_cyjd.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_jdrj.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_wlw.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_ny.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_all_two.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));

            }
        });
        tv_xxjs = (TextView) view.findViewById(R.id.tv_xxjs);
        tv_xxjs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_qc.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_xwcb.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_xxjs.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_cyjd.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_jdrj.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_wlw.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_ny.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_all_two.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));

            }
        });
        tv_cyjd = (TextView) view.findViewById(R.id.tv_cyjd);
        tv_cyjd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_qc.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_xwcb.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_xxjs.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_cyjd.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_jdrj.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_wlw.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_ny.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_all_two.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
            }
        });

        tv_cy = (TextView) view.findViewById(R.id.tv_cy);
        tv_cy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_cy.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_zc.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_my.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_ly.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_jk.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_all_one.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
            }
        });
        tv_zc = (TextView) view.findViewById(R.id.tv_zc);
        tv_zc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_cy.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_zc.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_my.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_ly.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_jk.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_all_one.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
            }
        });
        tv_my = (TextView) view.findViewById(R.id.tv_my);
        tv_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_cy.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_zc.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_my.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_ly.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_jk.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_all_one.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
            }
        });
        tv_ly = (TextView) view.findViewById(R.id.tv_ly);
        tv_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_cy.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_zc.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_my.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_ly.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_jk.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_all_one.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
            }
        });
        tv_jk = (TextView) view.findViewById(R.id.tv_jk);
        tv_jk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_cy.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_zc.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_my.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_ly.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_jk.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_all_one.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
            }
        });


        //最新
        tv_new = (TextView) view.findViewById(R.id.tv_new);
        tv_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_new.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_host.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_good.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                orderby = "ctime";

                onLoadData(1);
            }
        });

        //最热
        tv_host = (TextView) view.findViewById(R.id.tv_host);
        tv_host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_new.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_host.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_good.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                orderby = "watchnum";

                onLoadData(1);
            }
        });
        //好评
        tv_good = (TextView) view.findViewById(R.id.tv_good);
        tv_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_good.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_new.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_host.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                orderby = "usefulnum";

                onLoadData(1);
            }
        });

        tv_all_three = (TextView) view.findViewById(R.id.tv_all_three);
        tv_all_three.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
        tv_all_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_free.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_pay.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_all_three.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                price = "";
                onLoadData(1);
            }
        });
        //免费
        tv_free = (TextView) view.findViewById(R.id.tv_free);
        tv_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_free.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_pay.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_all_three.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));

                price = "free";
                onLoadData(1);
            }
        });
        //付费
        tv_pay = (TextView) view.findViewById(R.id.tv_pay);
        tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_pay.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_free.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_all_three.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));

                price = "charge";
                onLoadData(1);
            }
        });

        tv_all_one = (TextView) view.findViewById(R.id.tv_all_one);
        tv_all_one.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
        tv_all_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_cy.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_zc.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_my.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_ly.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_jk.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_all_one.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
            }
        });

        tv_all_two = (TextView) view.findViewById(R.id.tv_all_two);
        tv_all_two.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
        tv_all_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_all_two.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));

                tv_qc.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_xwcb.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_xxjs.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_cyjd.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_jdrj.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_wlw.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_ny.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
            }
        });

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "1111", Toast.LENGTH_SHORT).show();
            }
        });

        mLoadingFooter = new LoadingFooter(getActivity());
        listView.addFooterView(mLoadingFooter.getView());

        mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);
        listView.setOnScrollListener(mScrollLoader);

        mScrollLoader.onLoadFirstPage();
    }

    private int p = 1;
    private List<VideoBean.RecordBean> mList = new ArrayList<>();

    public void onLoadData(final int page){
        String url = UmiwiAPI.Login_Video+"?orderby="+orderby;

        if(price != null && price != ""){
            url += "?price="+price;
        }

        if(String.valueOf(p) != null && String.valueOf(p) != ""){
            url += "?p="+page;
        }

        Log.e("MZX",url);
        OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack() {
            private VideoAdapter videoAdapter;

            @Override
            public void onFaild() {
                mScrollLoader.onLoadErrorPage();
            }

            @Override
            public void onSucess(String data) {
                Log.e("MZX", "数据进行请求成功了--x-" + data);

                VideoBean audioBean = new Gson().fromJson(data, VideoBean.class);
                VideoBean.PageBean pagebean = audioBean.getPage();

                Log.e("TAG",audioBean.getPage().getCurrentpage()+"");
                Log.e("TAG",audioBean.getPage().getTotalpage()+"");

                if(audioBean.getPage().getCurrentpage() >= audioBean.getPage().getTotalpage()){
                    mLoadingFooter.setState(LoadingFooter.State.NoMore);
                    mScrollLoader.setEnd(true);
                    return;
                }

                if(pagebean.getCurrentpage() == 1){
                    mList.clear();
                }

                mScrollLoader.setPage(pagebean.getCurrentpage());
                mScrollLoader.setloading(false);

                if (mList == null) {
                      mList = audioBean.getRecord();
                } else {
//                    if(audioBean.getPage().getCurrentpage() != 1){
                        mList.addAll(audioBean.getRecord());
                    }
//                }

                videoAdapter = new VideoAdapter(getActivity(), mList);
                listView.setAdapter(videoAdapter);
            }
        });
    }
}