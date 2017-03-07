package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.umiwi.ui.beans.updatebeans.CelebrityBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.util.JsonUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

import static com.umiwi.ui.R.id.tv_all_two;
import static com.umiwi.ui.R.id.tv_cyjd;
import static com.umiwi.ui.R.id.tv_jdrj;
import static com.umiwi.ui.R.id.tv_ny;
import static com.umiwi.ui.R.id.tv_qc;
import static com.umiwi.ui.R.id.tv_wlw;
import static com.umiwi.ui.R.id.tv_xwcb;
import static com.umiwi.ui.R.id.tv_xxjs;

/**
 * Created by LvDabing on 2017/2/16.
 * Email：lvdabing@lvshandian.com
 * Detail:音频
 */

public class AudioFragment extends BaseConstantFragment {

    private TextView tv_all_one,tv_all_two,tv_all_three,tv_new,tv_host,tv_good,tv_free,
            tv_pay,tv_cy,tv_zc,tv_my,tv_ly,tv_jk,tv_qc,
            tv_xwcb,tv_xxjs,tv_cyjd,tv_jdrj,tv_wlw,tv_ny;

    private ListView listView;
    private LoadingFooter mLoadingFooter;
    private ListViewScrollLoader mScrollLoader;

    private List<AudioBean.RecordBean> mList = new ArrayList<>();

    private String price = "";//free-免费，charge-收费
    private String orderby = "ctime";//ctime-最新，watchnum-最热,usefulnum-好评

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_layout, null);

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
                orderby = "";
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
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "2222", Toast.LENGTH_SHORT).show();
            }
        });

        mLoadingFooter = new LoadingFooter(getActivity());
        listView.addFooterView(mLoadingFooter.getView());

        mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);
        listView.setOnScrollListener(mScrollLoader);

        mScrollLoader.onLoadFirstPage();
    }

    @Override
    public void onLoadData(int page) {
        super.onLoadData();

        String url = UmiwiAPI.Login_Audio+"?p="+page;
        if(price != null && price != ""){
            url += "?price="+price;
        }
        if(orderby != null && orderby != ""){
            url += "?orderby="+orderby;
        }

        Log.e("MZX",url);
        OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {
                mScrollLoader.onLoadErrorPage();
            }

            @Override
            public void onSucess(String data) {
                Log.e("MZX", "数据进行请求成功了--x-" + data);

                AudioBean audioBean = new Gson().fromJson(data, AudioBean.class);
                Log.e("MZX", "audioBean---" + audioBean.toString());

                AudioBean.PageBean pagebean = audioBean.getPage();

                if(audioBean.getPage().getCurrentpage() >= audioBean.getPage().getTotalpage()){
//                    Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
                    mScrollLoader.setEnd(true);
                    mLoadingFooter.setState(LoadingFooter.State.NoMore);
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
                    mList.addAll(audioBean.getRecord());
                }

                listView.setAdapter(new AudioAdapter(getActivity(), mList));
//                mLoadingFooter.setState(LoadingFooter.State.TheEndHint);
            }
        });
    }
}




