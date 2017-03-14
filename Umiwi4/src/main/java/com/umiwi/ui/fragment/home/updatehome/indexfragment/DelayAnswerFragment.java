package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.AnswerAdapter;
import com.umiwi.ui.adapter.updateadapter.MyAnswerAdapter;
import com.umiwi.ui.beans.UmiwiMyRecordBeans;
import com.umiwi.ui.beans.updatebeans.AnswerBean;
import com.umiwi.ui.fragment.setting.FeedbackFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.view.RefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * Created by Administrator on 2017/3/3.
 */

//待回答页面
public class DelayAnswerFragment extends BaseFragment {


    @InjectView(R.id.lv_answer_answered)
    ListView lvAnswerAnswered;
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private int page = 1;
    private ArrayList<AnswerBean.RAnser.Question> questionsInfos = new ArrayList<>();
    private MyAnswerAdapter myAnswerAdapter;
    private boolean isonRefresh = false;
    private int totalpage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delay_answer_fragment, null);
        ButterKnife.inject(this, view);

        myAnswerAdapter = new MyAnswerAdapter(getActivity());
        myAnswerAdapter.setData(questionsInfos);
        lvAnswerAnswered.setAdapter(myAnswerAdapter);
        lvAnswerAnswered.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                AnswerBean.RAnser.Question question = questionsInfos.get(i);
//                String price = question.getPrice();
//                String tname = question.getTname();
//                String tavatar = question.getTavatar();
//                String title = question.getTitle();
//                String answertime = question.getAnswertime();
//                String id = question.getId();
//                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
//                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, RecordVoiceFragment.class);
//                intent.putExtra("price",price);
//                intent.putExtra("tname",tname);
//                intent.putExtra("tavatar",tavatar);
//                intent.putExtra("title",title);
//                intent.putExtra("answertime",answertime);
//                intent.putExtra("id",id);
//                startActivity(intent);
            }
        });
        initrefreshLayout();
        getInfos();
        return view;
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

    //    YoumiRoomUserManager.getInstance().isLogin()
    private void getInfos() {
        String url = String.format(UmiwiAPI.Mine_Answer, page, 1);
        GetRequest<AnswerBean> request = new GetRequest<AnswerBean>(
                url, GsonParser.class,
                AnswerBean.class,
                AnswerListener);
        request.go();
    }


    private AbstractRequest.Listener<AnswerBean> AnswerListener = new AbstractRequest.Listener<AnswerBean>() {

        @Override
        public void onResult(AbstractRequest<AnswerBean> request, AnswerBean umiAnwebeans) {
            if (isonRefresh){
                refreshLayout.setRefreshing(false);
                questionsInfos.clear();
            }
            AnswerBean.RAnser infos = umiAnwebeans.getR();
            AnswerBean.RAnser.PageBean page = infos.getPage();
            totalpage = page.getTotalpage();
            ArrayList<AnswerBean.RAnser.Question> questions = infos.getQuestions();
            questionsInfos.addAll(questions);
            myAnswerAdapter.setData(questionsInfos);
            isonRefresh = false;
        }

        @Override
        public void onError(AbstractRequest<AnswerBean> requet, int statusCode, String body) {
            Toast.makeText(getActivity(), "失败", Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
