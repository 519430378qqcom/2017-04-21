package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.Intent;
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

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.AskQuestionAdapter;
import com.umiwi.ui.beans.UmiwiAddQuestionBeans;
import com.umiwi.ui.beans.UmiwiBuyQuestionBeans;
import com.umiwi.ui.beans.UmiwiPayOrderBeans;
import com.umiwi.ui.beans.updatebeans.NamedQuestionBean;
import com.umiwi.ui.beans.updatebeans.QuestionListBean;
import com.umiwi.ui.fragment.pay.PayOrderDetailFragment;
import com.umiwi.ui.fragment.pay.PayTypeEvent;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.StatisticsUrl;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.JsonUtil;
import com.umiwi.ui.util.LoginUtil;
import com.umiwi.ui.view.CircleImageView;
import com.umiwi.ui.view.MonitorScrollView;
import com.umiwi.ui.view.NoScrollListview;
import com.umiwi.video.control.PlayerController;
import com.zhy.http.okhttp.OkHttpUtils;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.ToastU;

/**
 * Created by Administrator on 2017/3/8.
 */

public class AskQuestionFragment extends BaseConstantFragment implements View.OnClickListener {

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
    private NamedQuestionBean namedQuestionBean;

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
        back.setOnClickListener(this);
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
                    namedQuestionBean = JsonUtil.json2Bean(data, NamedQuestionBean.class);
                    name.setText(namedQuestionBean.getName());
                    describe.setText(namedQuestionBean.getDescription());
                    ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
                    mImageLoader.loadImage(namedQuestionBean.getImage(), header);
                    obligation.setText(namedQuestionBean.getTutor_ask_desc());
                    etQuestion.setHint(namedQuestionBean.getAsk_desc());
                    question1.setText(namedQuestionBean.getAskpriceinfo());
                    question1.setOnClickListener(askBuyButtonListener);
                    answerNum.setText(namedQuestionBean.getQuestion());
                    hearNum.setText(namedQuestionBean.getTotallistennum());
                }
            }
        });
    }

    private View.OnClickListener askBuyButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(etQuestion.getText().toString().trim())) {
                ToastU.showShort(getActivity(), "请输入提问内容");
            } else {
                PlayerController.getInstance().pause();
                if (!YoumiRoomUserManager.getInstance().isLogin()) {
                    showLogin();
                    return;
                }
                addQuestion();
                MobclickAgent.onEvent(getActivity(), "购买提问", "单次购买");
            }

        }
    };

    private void showLogin() {
        LoginUtil.getInstance().showLoginView(getActivity());
    }

    private void addQuestion() {
        String url = null;
        url = String.format(UmiwiAPI.ADD_QUESTIONA, uid, etQuestion.getText().toString().trim());
        GetRequest<UmiwiAddQuestionBeans> request = new GetRequest<UmiwiAddQuestionBeans>(
                url, GsonParser.class,
                UmiwiAddQuestionBeans.class,
                addQuestionListener);
        request.go();
    }

    private String price;
    private Listener<UmiwiAddQuestionBeans> addQuestionListener = new Listener<UmiwiAddQuestionBeans>() {
        @Override
        public void onResult(AbstractRequest<UmiwiAddQuestionBeans> request, UmiwiAddQuestionBeans umiwiAddQuestionBeans) {
            String questionId = umiwiAddQuestionBeans.getR().getQid();
            price = umiwiAddQuestionBeans.getR().getPrice();
            getOrderId(questionId);
        }

        @Override
        public void onError(AbstractRequest<UmiwiAddQuestionBeans> requet, int statusCode, String body) {

        }
    };

    private void getOrderId(String questionId) {
        String url = null;
        url = String.format(UmiwiAPI.CREATE_QUESTIN_ORDERID, "json", questionId);
        GetRequest<UmiwiBuyQuestionBeans> request = new GetRequest<UmiwiBuyQuestionBeans>(
                url, GsonParser.class,
                UmiwiBuyQuestionBeans.class,
                addQuestionOrderListener);
        request.go();
    }

    private Listener<UmiwiBuyQuestionBeans> addQuestionOrderListener = new Listener<UmiwiBuyQuestionBeans>() {
        @Override
        public void onResult(AbstractRequest<UmiwiBuyQuestionBeans> request, UmiwiBuyQuestionBeans umiwiBuyQuestionBeans) {
            String orderId = umiwiBuyQuestionBeans.getR().getOrder_id();
            showCourseBuyDialog(orderId);
        }

        @Override
        public void onError(AbstractRequest<UmiwiBuyQuestionBeans> requet, int statusCode, String body) {

        }
    };


    /**
     * 提问
     *
     * @author
     * @version
     */
    public void showCourseBuyDialog(String orderId) {
        Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayOrderDetailFragment.class);
        intent.putExtra(PayOrderDetailFragment.KEY_ORDER_ID, orderId);
        intent.putExtra(PayOrderDetailFragment.KEY_ORDER_TYPE, PayTypeEvent.LECTURER);
        intent.putExtra(PayOrderDetailFragment.KEY_SPM, String.format(StatisticsUrl.ORDER_LECTURER, "7", orderId, price));
        startActivity(intent);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back :
                getActivity().finish();
                break;
        }
    }
}
