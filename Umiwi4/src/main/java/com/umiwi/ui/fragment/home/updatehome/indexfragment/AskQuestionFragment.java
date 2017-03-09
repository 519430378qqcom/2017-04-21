package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.updateadapter.AskQuestionAdapter;
import com.umiwi.ui.beans.updatebeans.NamedQuestionBean;
import com.umiwi.ui.beans.updatebeans.QuestionListBean;
import com.umiwi.ui.beans.updatebeans.VideoBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.util.JsonUtil;
import com.umiwi.ui.view.CircleImageView;
import com.umiwi.ui.view.MonitorScrollView;
import com.umiwi.ui.view.NoScrollListview;
import com.umiwi.ui.view.TopFloatScrollView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.util.ImageLoader;

/**
 * Created by Administrator on 2017/3/8.
 */

public class AskQuestionFragment extends BaseConstantFragment {

    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.header)
    CircleImageView header;
    @InjectView(R.id.name)
    TextView name;
    @InjectView(R.id.describe)
    TextView describe;
    @InjectView(R.id.obligation)
    TextView obligation;
    @InjectView(R.id.notice)
    TextView notice;
    @InjectView(R.id.et_question)
    EditText etQuestion;
    @InjectView(R.id.tv_number)
    TextView tvNumber;
    @InjectView(R.id.question1)
    TextView question1;
    @InjectView(R.id.answer_num)
    TextView answerNum;
    @InjectView(R.id.hear_num)
    TextView hearNum;
    @InjectView(R.id.noscroll_listview)
    NoScrollListview noscrollListview;
    @InjectView(R.id.more)
    LinearLayout more;
    @InjectView(R.id.mon_scrollview)
    MonitorScrollView monScrollview;
    private int page = 1;
    private String uid;
    private ArrayList<QuestionListBean.RecordBean> questionList = new ArrayList<>();
    private int currentpage;
    private int totalpage;
    private AskQuestionAdapter askQuestionAdapter;
    private boolean isBottom = false;
    private Runnable runnable;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ask_question_layout, null);
        ButterKnife.inject(this, view);
        etQuestion.addTextChangedListener(textWatcher);
        etQuestion.setFilters(new InputFilter[]{new InputFilter.LengthFilter(80)});
        uid = getActivity().getIntent().getStringExtra("uid");
        askQuestionAdapter = new AskQuestionAdapter(getActivity());
        askQuestionAdapter.setData(questionList);
        noscrollListview.setAdapter(askQuestionAdapter);
        if (!TextUtils.isEmpty(uid)) {
            getInfos(uid);
        }

        getQuestionlist();
        monScrollview.setOnScrollListener(new MonitorScrollView.OnScrollListener() {
            @Override
            public void onScroll(int y) {

            }

            @Override
            public void isBotton(boolean isBotton) {
                page++;
                isBottom = true;
                if (page <= totalpage) {
                    more.setVisibility(View.VISIBLE);
                    getQuestionlist();
                }
            }
        });
        return view;
    }

    private void getQuestionlist() {
        OkHttpUtils.get().url(UmiwiAPI.QUESTION_LIST).addParams("p", page + "").addParams("tuid", uid).build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {

            }

            @Override
            public void onSucess(final String data) {
                if (!TextUtils.isEmpty(data)) {
                    if (isBottom == true) {
                        if (runnable == null) {
                            runnable = new Runnable() {
                                @Override
                                public void run() {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.e("data", "行家音频列表请求成功了" + data);
                                            more.setVisibility(View.GONE);
                                            QuestionListBean questionListBean = JsonUtil.json2Bean(data, QuestionListBean.class);
                                            currentpage = questionListBean.getPage().getCurrentpage();
                                            totalpage = questionListBean.getPage().getTotalpage();
                                            List<QuestionListBean.RecordBean> record = questionListBean.getRecord();
                                            questionList.addAll(record);
                                            askQuestionAdapter.setData(questionList);
                                        }
                                    });
                                }
                            };
                        }
                    } else {
                        Log.e("data", "行家音频列表请求成功了" + data);
                        QuestionListBean questionListBean = JsonUtil.json2Bean(data, QuestionListBean.class);
                        currentpage = questionListBean.getPage().getCurrentpage();
                        totalpage = questionListBean.getPage().getTotalpage();
                        List<QuestionListBean.RecordBean> record = questionListBean.getRecord();
                        questionList.addAll(record);
                        askQuestionAdapter.setData(questionList);
                    }

                }
            }
        });

    }

    private void getInfos(String uid) {
        OkHttpUtils.get().url(UmiwiAPI.NAMED_QUESTIONA).addParams("tutoruid", uid).build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {

            }

            @Override
            public void onSucess(String data) {
                if (!TextUtils.isEmpty(data)) {
                    NamedQuestionBean namedQuestionBean = JsonUtil.json2Bean(data, NamedQuestionBean.class);
                    name.setText(namedQuestionBean.getName());
                    describe.setText(namedQuestionBean.getDescription());
                    ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
                    mImageLoader.loadImage(namedQuestionBean.getImage(), header);
                    obligation.setText(namedQuestionBean.getTutor_ask_desc());
                    etQuestion.setHint(namedQuestionBean.getAsk_desc());
                    question1.setText(namedQuestionBean.getAskpriceinfo());
                    answerNum.setText(namedQuestionBean.getQuestion());
                    hearNum.setText(namedQuestionBean.getTotallistennum());
                }
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        private String temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            int length = text.length();
            tvNumber.setText("" + length);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
