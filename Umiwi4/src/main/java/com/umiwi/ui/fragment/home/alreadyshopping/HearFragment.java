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
import com.umiwi.ui.adapter.updateadapter.HearAdapter;
import com.umiwi.ui.beans.updatebeans.AlreadyAskBean;
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

public class HearFragment extends BaseConstantFragment {

    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private ArrayList<AlreadyAskBean.RAlreadyAnser.Question> hearInfos = new ArrayList<>();

    private int hearPage = 1;
    private int hearTotalpage;
    private HearAdapter hearAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View inflate = inflater.inflate(R.layout.fragment_hear_layout, null);
        ButterKnife.inject(this, inflate);
        initRefreshLayout();
        hearAdapter = new HearAdapter(getActivity());
        hearAdapter.setData(hearInfos);
        listview.setAdapter(hearAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlreadyAskBean.RAlreadyAnser.Question question = hearInfos.get(i);
                String title = question.getTitle();
                String buttontag = question.getButtontag();
                String tavatar = question.getTavatar();
                String playtime = question.getPlaytime();
                String answertime = question.getAnswertime();
                String listennum = question.getListennum();
                String goodnum = question.getGoodnum();
                String tuid = question.getTuid();
                String id = question.getId();
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
        getHearInfos();
        return inflate;
    }
    private void getHearInfos() {
        String url = UmiwiAPI.Hear_url + "?p=" + hearPage;
        GetRequest<AlreadyAskBean> request = new GetRequest<AlreadyAskBean>(
                url, GsonParser.class,
                AlreadyAskBean.class,
                hearListener);
        request.go();
    }

    private AbstractRequest.Listener<AlreadyAskBean> hearListener = new AbstractRequest.Listener<AlreadyAskBean>() {

        @Override
        public void onResult(AbstractRequest<AlreadyAskBean> request, AlreadyAskBean umiAnwebeans) {
            ArrayList<AlreadyAskBean.RAlreadyAnser.Question> questions = umiAnwebeans.getR().getQuestions();
            AlreadyAskBean.RAlreadyAnser.PageBean page = umiAnwebeans.getR().getPage();
            hearTotalpage = page.getTotalpage();
            hearInfos.addAll(questions);
            hearAdapter.setData(hearInfos);
            refreshLayout.setRefreshing(false);
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

                 hearPage++;
                 if (hearPage <= hearTotalpage) {
                     refreshLayout.postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             getHearInfos();
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
                hearPage = 1;
                hearInfos.clear();
                getHearInfos();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
