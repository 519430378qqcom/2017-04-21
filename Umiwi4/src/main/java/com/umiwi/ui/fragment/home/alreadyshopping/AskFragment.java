package com.umiwi.ui.fragment.home.alreadyshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.AskHearAdapter;
import com.umiwi.ui.beans.updatebeans.AlreadyAskBean;
import com.umiwi.ui.fragment.WebFragment;
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
 * Created by Administrator on 2017/3/12.
 */

public class AskFragment extends BaseConstantFragment {

    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private int askTotalpage;
    private int askPage = 1;
    private ArrayList<AlreadyAskBean.RAlreadyAnser.Question> askInfos = new ArrayList<>();
    private AskHearAdapter askHearAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View inflate = inflater.inflate(R.layout.fragment_ask_layout, null);
        ButterKnife.inject(this, inflate);
        initRefreshLayout();
        askHearAdapter = new AskHearAdapter(getActivity());
        askHearAdapter.setData(askInfos);
        listview.setAdapter(askHearAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlreadyAskBean.RAlreadyAnser.Question question = askInfos.get(i);
                String title = question.getTitle();
                String buttontag = question.getButtontag();
                String tavatar = question.getTavatar();
                String playtime = question.getPlaytime();
                String answertime = question.getAnswertime();
                String listennum = question.getListennum();
                String goodnum = question.getGoodnum();
                String id = question.getId();
                String tuid = question.getTuid();
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AnswerDetailsFragment.class);
                intent.putExtra("title",title);
                intent.putExtra("buttontag",buttontag);
                intent.putExtra("tavatar",tavatar);
                intent.putExtra("playtime",playtime);
                intent.putExtra("answertime",answertime);
                intent.putExtra("listennum",listennum);
                intent.putExtra("goodnum",goodnum);
                intent.putExtra("uid",tuid);
                intent.putExtra("id",id);
                startActivity(intent);

            }
        });
        getAskInfos();

        return inflate;
    }


    private void getAskInfos() {
        String url = UmiwiAPI.Ask_Hear + "?p=" + askPage;
        GetRequest<AlreadyAskBean> request = new GetRequest<AlreadyAskBean>(
                url, GsonParser.class,
                AlreadyAskBean.class,
                AnswerListener);
        request.go();
    }


    private AbstractRequest.Listener<AlreadyAskBean> AnswerListener = new AbstractRequest.Listener<AlreadyAskBean>() {

        @Override
        public void onResult(AbstractRequest<AlreadyAskBean> request, AlreadyAskBean umiAnwebeans) {
            ArrayList<AlreadyAskBean.RAlreadyAnser.Question> questions = umiAnwebeans.getR().getQuestions();
            AlreadyAskBean.RAlreadyAnser.PageBean page = umiAnwebeans.getR().getPage();
            askTotalpage = page.getTotalpage();
            askInfos.addAll(questions);
            askHearAdapter.setData(askInfos);
            refreshLayout.setRefreshing(false);
            listview.setEnabled(true);
        }

        @Override
        public void onError(AbstractRequest<AlreadyAskBean> requet, int statusCode, String body) {


        }
    };


    private void initRefreshLayout() {
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.main_color));
        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {

                askPage++;
                if (askPage <= askTotalpage) {
                    refreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getAskInfos();
                            refreshLayout.setLoading(false);
                        }
                    }, 1000);

                } else {
                    refreshLayout.setLoading(false);

                }


            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listview.setEnabled(false);
                askPage = 1;
                askInfos.clear();
                getAskInfos();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
