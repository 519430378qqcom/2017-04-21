package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.updateadapter.MyAlreadyAnswerAdapter;
import com.umiwi.ui.beans.updatebeans.AnswerBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.view.RefreshLayout;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * Created by Administrator on 2017/3/3.
 */

//已回答页面
public class AnsweredFragment extends BaseConstantFragment {

    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    private int page = 1;
    private ArrayList<AnswerBean.RAnser.Question> questionsInfos = new ArrayList<>();
    private boolean isonRefresh = false;
    private int totalpage;
    private MyAlreadyAnswerAdapter myAlreadyAnswerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.answered_fragment, null);

        ButterKnife.inject(this, view);
        myAlreadyAnswerAdapter = new MyAlreadyAnswerAdapter(getActivity());
        myAlreadyAnswerAdapter.setData(questionsInfos);
        listview.setAdapter(myAlreadyAnswerAdapter);
        getInfos();
        initrefreshLayout();
        return view;
    }

    private void getInfos() {
        String url = String.format(UmiwiAPI.Mine_Answer, page, 2);
        GetRequest<AnswerBean> request = new GetRequest<AnswerBean>(
                url, GsonParser.class,
                AnswerBean.class,
                AnswerListener);
        request.go();
    }

    private void initrefreshLayout() {
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.main_color));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isonRefresh = true;
                getInfos();
            }
        });

        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                page++;
                if (page<=totalpage){
                    refreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getInfos();
                            refreshLayout.setLoading(false);

                        }
                    },1000);
                }else {
                    refreshLayout.setLoading(false);
                }
            }
        });
    }

    private AbstractRequest.Listener<AnswerBean> AnswerListener = new AbstractRequest.Listener<AnswerBean>() {

        @Override
        public void onResult(AbstractRequest<AnswerBean> request, AnswerBean umiAnwebeans) {
            Log.e("result","成功");
            if (isonRefresh){
                refreshLayout.setRefreshing(false);
                questionsInfos.clear();
            }
            AnswerBean.RAnser infos = umiAnwebeans.getR();
            AnswerBean.RAnser.PageBean page = infos.getPage();
            totalpage = page.getTotalpage();
            ArrayList<AnswerBean.RAnser.Question> questions = infos.getQuestions();
            questionsInfos.addAll(questions);
            myAlreadyAnswerAdapter.setData(questionsInfos);
            isonRefresh = false;
        }

        @Override
        public void onError(AbstractRequest<AnswerBean> requet, int statusCode, String body) {

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
