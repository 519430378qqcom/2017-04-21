package com.umiwi.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;
import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.JoinerAdapter;
import com.umiwi.ui.adapter.TutorAdapter;
import com.umiwi.ui.beans.ActivityBean;
import com.umiwi.ui.beans.ActivityBean.Info;
import com.umiwi.ui.beans.ActivityBean.TutorInfo;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.LoginUtil;
import com.umiwi.ui.view.ExpandableTextView;
import com.umiwi.ui.view.LoginScrollView;
import com.umiwi.ui.view.LoginScrollView.OnScrollViewListener;

import java.util.ArrayList;

import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.ImageLoader;

public class ActivityDetailFragment extends BaseFragment {

    protected static final String KEY_DETAIL_URL = "keyDetailURL";
    public static final String KEY_IS_OVER = "keyIsOver";
    public static final String KEY_ACTIVITY_TITLE = "keyActivityTitle";
    private String url;
    private String activityTitle;
    private boolean isOver;
    private String actionBarTitle;
    private LoginScrollView scrollView;
    private Toolbar mActionBarToolbar;

    private TextView stutsTextView;
    private TextView countTimeTextView;

    private int headImageViewHeight;

    private ActivityBean activityBean;
    private Listener<ActivityBean> listener = new Listener<ActivityBean>() {
        @Override
        public void onResult(AbstractRequest<ActivityBean> request,
                             ActivityBean t) {

//			dismissDialog();
            activityBean = t;
            update(t);
            menu.setGroupVisible(0, true);
        }

        @Override
        public void onError(AbstractRequest<ActivityBean> requet,
                            int statusCode, String body) {
//			dismissDialog();
//			showError();
        }
    };

    Menu menu;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        inflater.inflate(R.menu.toolbar_share, menu);
        menu.setGroupVisible(0, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                if (TextUtils.isEmpty(activityBean.getInfo().shareUrl) && getActivity() == null) {
                    return true;
                }
                ShareDialog.getInstance().showDialog(getActivity(),
                        activityBean.getInfo().title,
                        activityBean.getInfo().shareContent,
                        activityBean.getInfo().shareUrl,
                        activityBean.getInfo().imageURL);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity_detail, null);
        url = getActivity().getIntent().getStringExtra(KEY_DETAIL_URL);

        isOver = getActivity().getIntent().getBooleanExtra(KEY_IS_OVER, false);

        activityTitle = getActivity().getIntent().getStringExtra(KEY_ACTIVITY_TITLE);

        ListView tutorsListView = (ListView) view.findViewById(R.id.tutors_list_view);
        tutorsListView.setAdapter(mAdapter);

        HorizontalListView joinersListView = (HorizontalListView) view.findViewById(R.id.joiners_list_view);
        joinersListView.setAdapter(mJoinerAdapter);

        signupButton = (TextView) view.findViewById(R.id.signup_submit_text_view);

        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBarAndToolbarTitle(mActionBarToolbar, "");

        scrollView = (LoginScrollView) view.findViewById(R.id.content_scroll_view);

        scrollView.setOnScrollViewListener(scrollChangeListener);

        stutsTextView = (TextView) view.findViewById(R.id.stuts_text_view);
        countTimeTextView = (TextView) view.findViewById(R.id.count_down_text_view);
        imageView = (ImageView) view.findViewById(R.id.image_view);

        offlineTitle = (TextView) view.findViewById(R.id.offline_activity_title_text_view);
        tutorName = (TextView) view.findViewById(R.id.tutor_name_text_view);
        timeText = (TextView) view.findViewById(R.id.time_text_view);
        addressText = (TextView) view.findViewById(R.id.address_text_view);
        numberText = (TextView) view.findViewById(R.id.number_text_view);

        vLoadData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(fragmentName);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(fragmentName);
    }

    TutorAdapter mAdapter = new TutorAdapter();
    private OnClickListener mSignupListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (YoumiRoomUserManager.getInstance().isLogin()) {
                Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
                i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ActivitySignupFragment.class);
                i.putExtra(ActivitySignupFragment.KEY_ACTIVITY_ID, activityBean.getInfo().id);
                startActivity(i);
            } else {
                LoginUtil.getInstance().showLoginView(getActivity());
            }
        }
    };

    private TextView signupButton;


    private void vLoadData() {
//		hideContentView();
//		showLoading();
        GetRequest<ActivityBean> req = new GetRequest<ActivityBean>(url, GsonParser.class, ActivityBean.class, listener);
        req.go();
    }

    JoinerAdapter mJoinerAdapter = new JoinerAdapter();

    private void update(ActivityBean t) {
        String imageURL = t.getInfo().imageURL;
//		showContentView();
        mJoinerAdapter.setData(t.getJoiners());


        ImageLoader mImageLoader = new ImageLoader(getActivity());
        mImageLoader.loadImage(imageURL, imageView);
        LinearLayout.LayoutParams para = (android.widget.LinearLayout.LayoutParams) imageView.getLayoutParams();
        para.width = DimensionUtil.getScreenWidth(getActivity());
        para.height = (para.width * 55) / 64;

        headImageViewHeight = para.height;

        imageView.setLayoutParams(para);

        if (t.getInfo() != null && !TextUtils.isEmpty(t.getInfo().title)) {
            offlineTitle.setText(t.getInfo().title);
        } else {
            offlineTitle.setVisibility(View.GONE);
        }

        TutorInfo tutor = null;
        if (t.getTutors() != null && !t.getTutors().isEmpty()) {
            tutor = t.getTutors().get(0);
        }
        if (tutor != null) {
            String ss = tutor.name + "   ";
            Spannable wordtoSpan = new SpannableString(ss + tutor.title);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.GRAY), ss.length(), wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tutorName.setText(wordtoSpan);
        } else {
            view.findViewById(R.id.name_container).setVisibility(View.GONE);
            view.findViewById(R.id.tutor_intro_text).setVisibility(View.GONE);
            view.findViewById(R.id.tutors_list_view).setVisibility(View.GONE);
            view.findViewById(R.id.tutor_diliver_view).setVisibility(View.GONE);
        }

        timeText.setText(t.getInfo().startTime);

        addressText.setText(t.getInfo().address);

        numberText.setText(Html.fromHtml("<font color='#000000'> " + t.getInfo().maxPersion + "人  " + "</font>"
                        + "<font color='#999999'> " + "已报名" + "</font>" + "<font color='#ff6600'> " + t.getInfo().total + "</font>"
                        + "<font color='#999999'> " + "人 ,通过" + "</font>" + "<font color='#50b539'> " + t.getInfo().chekedNum + "</font>"
                        + "<font color='#999999'> " + "人" + "</font>"
        ));
        /*}else{
            findViewById(R.id.tutor_info_container).setVisibility(View.GONE);
			findViewById(R.id.signup_button_outer).setVisibility(View.VISIBLE);
			signupButton = (TextView) findViewById(R.id.signup_button_outer);
			findViewById(R.id.signup_button_outer).setOnClickListener(mSignupListener);
			findViewById(R.id.tutor_intro_text).setVisibility(View.GONE);
			findViewById(R.id.tutor_diliver_view).setVisibility(View.GONE);
		}*/

        etv = (ExpandableTextView) view.findViewById(R.id.description_text_view);
        int lineCount = etv.setText(t.getInfo().description);
        mAdapter.setData(t.getTutors());
        etv.setOnClickListener(mToggleButtonListener);

        if (lineCount <= 5) {
            view.findViewById(R.id.toggle_expand_button).setVisibility(View.GONE);
        }

        toggleButton = (ImageView) view.findViewById(R.id.toggle_expand_button);
        toggleButton.setOnClickListener(mToggleButtonListener);


        ViewGroup preceduresContainer = (ViewGroup) view.findViewById(R.id.precedures_container);
        ArrayList<String> precedures = t.getInfo().getPrecedures();
        if (precedures != null && precedures.size() > 1) {

            int counter = 0;
            for (String s : precedures) {

                if (!TextUtils.isEmpty(s)) {
                    View v = getActivity().getLayoutInflater().inflate(R.layout.item_precedure, null);
                    preceduresContainer.addView(v);
                    TextView content = (TextView) v.findViewById(R.id.content_text_view);
                    content.setText(s);
                    ImageView dotImageView = (ImageView) v.findViewById(R.id.dot_image_view);
                    dotImageView.setImageResource(R.drawable.circle_gray);

                }
                counter++;
            }
        } else if (precedures.size() == 1 && TextUtils.isEmpty(precedures.get(0))) {
            view.findViewById(R.id.diliver_view).setVisibility(View.GONE);
            view.findViewById(R.id.flow_path_text_view).setVisibility(View.GONE);
            preceduresContainer.setVisibility(View.GONE);
        } else if (precedures.size() == 1 && !TextUtils.isEmpty(precedures.get(0))) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.item_precedure, null);
            preceduresContainer.addView(v);
            TextView content = (TextView) v.findViewById(R.id.content_text_view);
            content.setText(precedures.get(0));
            ImageView dotImageView = (ImageView) v.findViewById(R.id.dot_image_view);
            dotImageView.setImageResource(R.drawable.circle_gray);
        } else {
            view.findViewById(R.id.diliver_view).setVisibility(View.GONE);
            view.findViewById(R.id.flow_path_text_view).setVisibility(View.GONE);
            preceduresContainer.setVisibility(View.GONE);
        }
        TextView joinersTextView = (TextView) view.findViewById(R.id.joiners_text_view);

        joinersTextView.setText("参与者 ( " + mJoinerAdapter.getCount() + " )");

        scrollToTop();

        if (isOver) {
            signupButton.setEnabled(false);
            signupButton.setText("活动已结束");

            stutsTextView.setText("活动已结束");
            countTimeTextView.setText("请期待下次活动");
        } else {
            handleInTime(t);
        }
        signupButton.setOnClickListener(mSignupListener);

    }

    // 处理未过时
    private void handleInTime(ActivityBean t) {
        stutsTextView.setText("剩余时间");

        if (t.getInfo() != null) {
            Info info = t.getInfo();

            if (!TextUtils.isEmpty(info.servertime) && !TextUtils.isEmpty(info.enter_endtime_timestamp)) {
                try {
                    long currentTime = Long.parseLong(info.servertime);
                    long endTime = Long.parseLong(info.enter_endtime_timestamp);

                    if (endTime - currentTime > 24 * 60 * 60) {
                        countTimeTextView.setText((endTime - currentTime) / (24 * 60 * 60) + "天");
                    } else {
                        startCountingDown((endTime - currentTime) * 1000);
                    }
                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    // 倒计时处理
    private void startCountingDown(long time) {
        CountDown countDown = new CountDown(time, 1000);
        countDown.start();
    }

    private Handler mHandler = new Handler();

    private void scrollToTop() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        }, 100);
    }


    private OnClickListener mToggleButtonListener = new OnClickListener() {
        @SuppressWarnings("deprecation")
        @Override
        public void onClick(View v) {
            etv.toggleExpand();
            if (etv.isExpand()) {
                toggleButton.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.description_hide_up_bg));
            } else {
                toggleButton.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.description_show_all_bg));
            }

        }
    };
    private ExpandableTextView etv;
    private ImageView toggleButton;

    // 监听滑动事件
    public OnScrollViewListener scrollChangeListener = new OnScrollViewListener() {
        @Override
        public void onScrollChanged(LoginScrollView loginScrollView,
                                    int x, int y, int oldx, int oldy) {

            if (headImageViewHeight > 0) {
                changeToolBarBackGround(y, oldy, headImageViewHeight);
            }
        }

    };
    private ImageView imageView;
    private TextView offlineTitle;
    private TextView tutorName;
    private View view;
    private TextView timeText;
    private TextView addressText;
    private TextView numberText;

    //  根据滑动设置toolbar的透明度
    protected void changeToolBarBackGround(int y, int oldy, int opaqueTotalHeight) {
        int transparent = 0;

        if (y < opaqueTotalHeight) {
            transparent = y * 255 / opaqueTotalHeight;
        } else {
            transparent = 255;
        }

        mActionBarToolbar.setBackgroundColor(Color.parseColor("#ff6600"));
        mActionBarToolbar.getBackground().setAlpha(transparent);

        if (y == 0) {
            mActionBarToolbar.setNavigationIcon(R.drawable.ic_action_bar_activity_return);
            mActionBarToolbar.setTitle("");
        } else {
            mActionBarToolbar.setTitle(activityTitle);
            mActionBarToolbar.setNavigationIcon(R.drawable.ic_action_bar_return);
        }
    }

    class CountDown extends CountDownTimer {

        private StringBuilder timeStringBuilder;

        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            timeStringBuilder = new StringBuilder();
        }

        @Override
        public void onFinish() {
            stutsTextView.setText("活动已结束");
            countTimeTextView.setText("请期待下次活动");

            signupButton.setEnabled(false);
            signupButton.setText("活动已结束");
        }

        @Override
        public void onTick(long millisUntilFinished) {

            timeStringBuilder.delete(0, timeStringBuilder.length());

            String hour = millisUntilFinished / 3600000 + "";
            timeStringBuilder.append(hour).append("时");
            String min = (millisUntilFinished / 1000) / 60 % 60 + "";
            timeStringBuilder.append(min).append("分");
            String second = (millisUntilFinished / 1000) % 60 + "";
            timeStringBuilder.append(second).append("秒");

            Spannable spannable = new SpannableString(timeStringBuilder.toString());
            spannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(Color.GRAY), 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(Color.BLACK), 3, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(Color.GRAY), 5, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(Color.BLACK), 6, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(Color.GRAY), 6, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            countTimeTextView.setText(spannable);
        }
    }

}



