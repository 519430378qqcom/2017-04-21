package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.ExperDetailsWendaAdapter;
import com.umiwi.ui.beans.updatebeans.HomeAskBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.view.NoScrollListview;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * 行家详情问答
 * Created by Administrator on 2017/2/28.
 */

public class ExperDetailsWendaFragment extends BaseConstantFragment {

    @InjectView(R.id.noscroll_listview)
    NoScrollListview noscrollListview;
    @InjectView(R.id.no_data)
    TextView noData;
    private int page;
    private int totalpage;
    private boolean isBottom = false;
    private List<HomeAskBean.RAlHomeAnser.Record> wendaInfos = new ArrayList<>();
    private ExperDetailsWendaAdapter experDetailsWendaAdapter;
    private Handler handler;
    private Runnable runnable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exper_details_wenda_layout, null);
        ButterKnife.inject(this, view);
        experDetailsWendaAdapter = new ExperDetailsWendaAdapter(getActivity());
        experDetailsWendaAdapter.setData(wendaInfos);
        noscrollListview.setAdapter(experDetailsWendaAdapter);
        ExperDetailsFragment.setOnScrollListenerWenda(new ExperDetailsFragment.OnScrollListenerWenda() {
            @Override
            public void IswendaBottom() {
                page++;
                isBottom = true;
                if (page <= totalpage) {
                    ExperDetailsFragment.tv_more.setVisibility(View.VISIBLE);
                    getInfos();
                }
            }
        });
        noscrollListview.setFocusable(false);
        handler = new Handler();
        return view;
    }

    @Override
    public void onResume() {
        getInfos();

        super.onResume();
    }

    private void getInfos() {
        String questionurl = ExperDetailsFragment.questionurl;
        if (!TextUtils.isEmpty(questionurl)) {
            String url = questionurl + "/?p=" + page;
            GetRequest<HomeAskBean> request = new GetRequest<HomeAskBean>(
                    url, GsonParser.class,
                    HomeAskBean.class,
                    new AbstractRequest.Listener<HomeAskBean>() {
                        @Override
                        public void onResult(AbstractRequest<HomeAskBean> request, final HomeAskBean homeAskBean) {
                            if (isBottom == true) {
                                if (runnable == null) {
                                    runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ExperDetailsFragment.tv_more.setVisibility(View.GONE);
                                                    HomeAskBean.RAlHomeAnser r = homeAskBean.getR();
                                                    totalpage = r.getPage().getTotalpage();
                                                    ArrayList<HomeAskBean.RAlHomeAnser.Record> record = r.getRecord();
                                                    if (record != null && record.size() > 0) {
                                                        noData.setVisibility(View.GONE);
                                                    } else {
                                                        noData.setVisibility(View.VISIBLE);
                                                    }
                                                    wendaInfos.addAll(record);
                                                    experDetailsWendaAdapter.setData(wendaInfos);


                                                }
                                            });
                                        }
                                    };
                                }
                                handler.postDelayed(runnable, 1000);
                            } else {
                                HomeAskBean.RAlHomeAnser r = homeAskBean.getR();
                                totalpage = r.getPage().getTotalpage();
                                ArrayList<HomeAskBean.RAlHomeAnser.Record> record = r.getRecord();
                                if (record != null && record.size() > 0) {
                                    noData.setVisibility(View.GONE);
                                } else {
                                    noData.setVisibility(View.VISIBLE);
                                }
                                wendaInfos.addAll(record);
                                experDetailsWendaAdapter.setData(wendaInfos);

                            }
                            Log.e("questions", "成功");

                        }

                        @Override
                        public void onError(AbstractRequest<HomeAskBean> requet, int statusCode, String body) {
                            Log.e("questions", "失败");

                        }
                    });
            request.go();

        }

    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
