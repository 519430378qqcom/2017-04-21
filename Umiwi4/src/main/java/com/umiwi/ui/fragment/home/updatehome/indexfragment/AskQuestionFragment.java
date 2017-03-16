package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.AskQuestionAdapter;
import com.umiwi.ui.beans.UmiwiAddQuestionBeans;
import com.umiwi.ui.beans.UmiwiBuyCreateOrderBeans;
import com.umiwi.ui.beans.updatebeans.HomeAskBean;
import com.umiwi.ui.beans.updatebeans.NamedQuestionBean;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.fragment.pay.PayingFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.JsonUtil;
import com.umiwi.ui.util.LoginUtil;
import com.umiwi.ui.util.SoftKeyBoardListener;
import com.umiwi.ui.view.CircleImageView;
import com.umiwi.ui.view.MonitorScrollView;
import com.umiwi.ui.view.NoScrollListview;
import com.umiwi.video.control.PlayerController;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
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
    @InjectView(R.id.share)
    ImageView share;
    @InjectView(R.id.header)
    CircleImageView header;
    @InjectView(R.id.name)
    TextView name;
    @InjectView(R.id.describe)
    TextView describe;
    @InjectView(R.id.obligation)
    TextView obligation;
    //    @InjectView(R.id.notice)
//    TextView notice;
    @InjectView(R.id.et_question)
    EditText etQuestion;
    @InjectView(R.id.tv_number)
    TextView tvNumber;
    @InjectView(R.id.price)
    TextView price;
    @InjectView(R.id.question1)
    TextView question1;
    @InjectView(R.id.answer_num)
    TextView answerNum;
    @InjectView(R.id.hear_num)
    TextView hearNum;
    @InjectView(R.id.tv_unfold)
    TextView tv_unfold;
    @InjectView(R.id.noscroll_listview)
    NoScrollListview noscrollListview;
    @InjectView(R.id.more)
    LinearLayout more;
    @InjectView(R.id.mon_scrollview)
    MonitorScrollView monScrollview;
    @InjectView(R.id.scrollView)
    View scrollView;
    private int page = 1;
    private String uid;
    private ArrayList<HomeAskBean.RAlHomeAnser.Record> questionList = new ArrayList<>();
    private int currentpage;
    private int totalpage;
    private AskQuestionAdapter askQuestionAdapter;
    private boolean isBottom = false;
    private Runnable runnable;
    private NamedQuestionBean namedQuestionBean;
    private boolean isUnfold = true;
    private Handler handler = new Handler();
private int mHeight;
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
        share.setOnClickListener(this);
        tv_unfold.setOnClickListener(new UnfoldOnClickListener());

        SoftKeyBoardListener.setListener(getActivity(),
                new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
                    @Override
                    public void keyBoardShow(int height) {
                        mHeight = height;
//                        ViewGroup.LayoutParams layoutParams = scrollView.getLayoutParams();
//                        layoutParams.height =height;
//                        scrollView.setLayoutParams(layoutParams);
//
//                        scrollView.setVisibility(View.VISIBLE);
//                        handler.postDelayed(runnable1,200);
//                                monScrollview.scrollTo(0, 0);
//                        monScrollview.smoothScrollTo(0, height);
                    }

                    @Override
                    public void keyBoardHide(int height) {
                        scrollView.setVisibility(View.GONE);
                        monScrollview.scrollTo(0, 0);
                        monScrollview.smoothScrollTo(0, 0);
                    }
                });
        return view;
    }

    private Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            monScrollview.scrollTo(0, 0);
            monScrollview.smoothScrollTo(0, mHeight);
        }
    };

    @Override
    public void onResume() {
        getQuestionlist();

        super.onResume();
    }

    private void getQuestionlist() {

        String url = String.format(UmiwiAPI.QUESTION_LIST, page, uid);
//        String url = "http://i.v.youmi.cn/api8/questionlist?tuid="+uid;
        GetRequest<HomeAskBean> request = new GetRequest<HomeAskBean>(
                url, GsonParser.class,
                HomeAskBean.class,
                new Listener<HomeAskBean>() {
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
                                                more.setVisibility(View.GONE);
                                                HomeAskBean.RAlHomeAnser r = homeAskBean.getR();
                                                totalpage = r.getPage().getTotalpage();
                                                currentpage = r.getPage().getCurrentpage();
                                                ArrayList<HomeAskBean.RAlHomeAnser.Record> record = r.getRecord();
                                                questionList.addAll(record);
                                                askQuestionAdapter.setData(questionList);

//
                                            }
                                        });
                                    }
                                };
                            }
                        } else {
                            HomeAskBean.RAlHomeAnser r = homeAskBean.getR();
                            totalpage = r.getPage().getTotalpage();
                            currentpage = r.getPage().getCurrentpage();
                            ArrayList<HomeAskBean.RAlHomeAnser.Record> record = r.getRecord();
                            questionList.addAll(record);
                            askQuestionAdapter.setData(questionList);

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

    private void getInfos(String uid) {
        OkHttpUtils.get().url(UmiwiAPI.NAMED_QUESTIONA).addParams("tutoruid", uid).build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {

            }

            @Override
            public void onSucess(String data) {
                Log.e("TAG", "data=" + data.toString());
                if (!TextUtils.isEmpty(data)) {
                    namedQuestionBean = JsonUtil.json2Bean(data, NamedQuestionBean.class);
                    name.setText(namedQuestionBean.getName());
                    describe.setText(namedQuestionBean.getDescription());
                    describe.setEllipsize(TextUtils.TruncateAt.END);
                    if (namedQuestionBean.getDescription().length() > 50) {
                        tv_unfold.setVisibility(View.VISIBLE);
                    } else {
                        tv_unfold.setVisibility(View.GONE);
                    }

                    ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
                    mImageLoader.loadImage(namedQuestionBean.getImage(), header);
                    obligation.setText(namedQuestionBean.getTutor_ask_desc());
                    etQuestion.setHint(namedQuestionBean.getAsk_desc());
                    price.setText(namedQuestionBean.getAskpriceinfo());
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
            }

        }
    };

    private void showLogin() {
        LoginUtil.getInstance().showLoginView(getActivity());
    }


    /**
     * 提交问题
     */
    private void addQuestion() {
        String url = null;

        url = String.format(UmiwiAPI.ADD_QUESTIONA, uid, etQuestion.getText().toString().trim());
        GetRequest<UmiwiAddQuestionBeans> request = new GetRequest<UmiwiAddQuestionBeans>(
                url, GsonParser.class,
                UmiwiAddQuestionBeans.class,
                addQuestionListener);
        request.go();
    }


    private Listener<UmiwiAddQuestionBeans> addQuestionListener = new Listener<UmiwiAddQuestionBeans>() {
        @Override
        public void onResult(AbstractRequest<UmiwiAddQuestionBeans> request, UmiwiAddQuestionBeans umiwiAddQuestionBeans) {
            String questionId = umiwiAddQuestionBeans.getR().getQid();

            getOrderId(questionId);


        }

        @Override
        public void onError(AbstractRequest<UmiwiAddQuestionBeans> requet, int statusCode, String body) {
            ToastU.showShort(getActivity(), "行家不存在");
        }
    };

    /**
     * 获取提问的payurl
     *
     * @param questionId 问题id
     */
    private void getOrderId(String questionId) {
        String url = null;
        url = String.format(UmiwiAPI.CREATE_QUESTIN_ORDERID, "json", questionId);
        GetRequest<UmiwiBuyCreateOrderBeans> request = new GetRequest<UmiwiBuyCreateOrderBeans>(
                url, GsonParser.class,
                UmiwiBuyCreateOrderBeans.class,
                addQuestionOrderListener);
        request.go();
    }

    private Listener<UmiwiBuyCreateOrderBeans> addQuestionOrderListener = new Listener<UmiwiBuyCreateOrderBeans>() {
        @Override
        public void onResult(AbstractRequest<UmiwiBuyCreateOrderBeans> request, UmiwiBuyCreateOrderBeans umiwiBuyCreateOrderBeans) {
            String payurl = umiwiBuyCreateOrderBeans.getR().getPayurl();
            questionBuyDialog(payurl);
        }

        @Override
        public void onError(AbstractRequest<UmiwiBuyCreateOrderBeans> requet, int statusCode, String body) {

        }
    };


    /**
     * 跳转到购买界面
     *
     * @param payurl
     */
    public void questionBuyDialog(String payurl) {
        Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayingFragment.class);
        i.putExtra(PayingFragment.KEY_PAY_URL, payurl);
        startActivity(i);
        getActivity().finish();

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
            case R.id.back:
                getActivity().finish();
            case R.id.share:
                if (namedQuestionBean.getSharecontent() != null && namedQuestionBean.getShareimg() != null && namedQuestionBean.getSharetitle() != null && namedQuestionBean.getShareurl() != null) {
                    ShareDialog.getInstance().showDialog(getActivity(),
                            namedQuestionBean.getSharetitle(), namedQuestionBean.getSharecontent(),
                            namedQuestionBean.getShareurl(), namedQuestionBean.getShareimg());
                }
                break;
        }
    }

    class UnfoldOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (isUnfold) { //开
                isUnfold = false;
                describe.setMaxLines(Integer.MAX_VALUE);
                Drawable drawable = getResources().getDrawable(R.drawable.fold);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_unfold.setCompoundDrawables(null, null, drawable, null);
                tv_unfold.setText("收起");
            } else {  //关
                isUnfold = true;
                describe.setMaxLines(3);
                Drawable drawable = getResources().getDrawable(R.drawable.unfold);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_unfold.setCompoundDrawables(null, null, drawable, null);
                tv_unfold.setText("展开");
            }
        }
    }
}
