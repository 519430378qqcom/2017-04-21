package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.beans.ExperDetailsBean;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.util.JsonUtil;
import com.umiwi.ui.view.TopFloatScrollView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;

/**
 * Created by lws on 2017/2/28.
 */

public class ExperDetailsFragment extends BaseConstantFragment {
    public static final String KEY_DEFAULT_TUTORUID = "key.defaulttutoruid";
    private ImageView head;
    private ImageView iv_back;
    private ImageView iv_shared;
    private TextView tv_name;
    private TextView tv_describe;
    private TextView tv_content;
    private TextView tv_unfold;
    public static TextView subscriber;
    public static TextView free_read;
    private boolean isUnfold = true;
    private boolean isFirstOnMeasure;
    private ArrayList<String> mTitleList = new ArrayList<String>();
    private TabLayout tabsOrder;
    private FrameLayout fl_content;
    private Fragment detailsColumnFragment, experDetailsVoiceFragment, experDetailsVideoFragment, experDetailsWendaFragment, experDetailsCommentFragment;
    public static TopFloatScrollView scroll_view;
    public int CurrentPosition;
    private String uid;
    private String experName;
    private String tutorimage;
    private String description;
    private String tutortitle;
    public static String albumurl;
    public static String audioalbumurl;
    public static String questionurl;
    public static String tcolumnurl;
    public static String threadurl;
    public static LinearLayout tv_more;
    public static OnScrollListener mListener;
    public static LinearLayout yuedu;
    private TextView question;
    private String uid1;
    private ExperDetailsBean.ShareBean share;

    public static void setOnScrollListener(OnScrollListener listener) {
        mListener = listener;
    }

    public interface OnScrollListener {
        void IsColumnbottom();

    }

    public static OnScrollListenerVoice mListenerVoice;

    public static void setOnScrollListenerVoice(OnScrollListenerVoice listener) {
        mListenerVoice = listener;
    }

    public interface OnScrollListenerVoice {
        void IsvoiceBottom();
    }

    public static OnScrollListenerVideo mListenerVideo;

    public static void setOnScrollListenerVideo(OnScrollListenerVideo listener) {
        mListenerVideo = listener;
    }

    public interface OnScrollListenerVideo {
        void IsVideoBottom();
    }


    public static OnScrollListenerWenda mListenerWenda;

    public static void setOnScrollListenerWenda(OnScrollListenerWenda listener) {
        mListenerWenda = listener;
    }

    public interface OnScrollListenerWenda {
        void IswendaBottom();
    }

    public static OnScrollListenerComment mListenerComment;

    public static void setOnScrollListenerComment(OnScrollListenerComment listener) {
        mListenerComment = listener;
    }

    public interface OnScrollListenerComment {
        void IsCommentBottom();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expert_details_layout, null);
        uid = getActivity().getIntent().getStringExtra(ExperDetailsFragment.this.KEY_DEFAULT_TUTORUID);
        getInfo();
        initView(view);
        return view;
    }

    private void getInfo() {
        OkHttpUtils.get().url(UmiwiAPI.CELEBRTYY_DETAILS + uid).build().execute(new CustomStringCallBack() {

            @Override
            public void onFaild() {
                Log.e("data", "名人详情请求数据失败");
            }

            @Override
            public void onSucess(String data) {
                Log.e("data", "名人详情请求数据成功" + data);
                Log.e("TAG", "UmiwiAPI.CELEBRTYY_DETAILS + uid=" + UmiwiAPI.CELEBRTYY_DETAILS + uid);
                ExperDetailsBean experDetailsBean = JsonUtil.json2Bean(data, ExperDetailsBean.class);
                String id = experDetailsBean.getResult().getTcolumnurl();
                experName = experDetailsBean.getName();
                tutorimage = experDetailsBean.getTutorimage();
                description = experDetailsBean.getDescription();
                tutortitle = experDetailsBean.getTutortitle();
                share = experDetailsBean.getShare();
                String isopenask = experDetailsBean.getIsopenask();
                uid1 = experDetailsBean.getUid();
                if (isopenask.equals("1")) {
                    question.setVisibility(View.GONE);
                } else if (isopenask.equals("2")) {
                    question.setVisibility(View.VISIBLE);
                }
                tv_name.setText(experName);
                tv_describe.setText(tutortitle);
                tv_content.setText(description);

                if (description.length()>50){
                    tv_unfold.setVisibility(View.VISIBLE);
                }else {
                    tv_unfold.setVisibility(View.GONE);
                }
//                tv_content.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Layout l = tv_content.getLayout();
//                        if (l != null) {
//                            int lines = l.getLineCount();
//                            if (lines > 0) {
//                                if (l.getEllipsisCount(lines - 1) > 0) {
//                                    Log.e("111111111", "省略");
//                                }
//                            }
//                        } else {
//                            Log.e("111111111",  "省略no");
//                        }
//                    }
//                });

                ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
                mImageLoader.loadImage(tutorimage, head);
                ExperDetailsBean.ResultBean resultUrl = experDetailsBean.getResult();
                if (resultUrl != null) {
//            mTitleList.add("视频");
//            mTitleList.add("问答");
//            mTitleList.add("评论");
                    //专栏
                    albumurl = resultUrl.getAlbumurl();
                    Log.e("TAG", "albumurl=" + albumurl);
                    //音频
                    audioalbumurl = resultUrl.getAudioalbumurl();
                    Log.e("TAG", "audioalbumurl=" + audioalbumurl);
                    //视频
                    tcolumnurl = resultUrl.getTcolumnurl();
                    Log.e("TAG", "tcolumnurl=" + tcolumnurl);
                    //问答数据
                    questionurl = resultUrl.getQuestionurl();
                    Log.e("TAG", "questionurl=" + questionurl);
                    //评论
                    threadurl = resultUrl.getThreadurl();
                    Log.e("TAG", "threadurl=" + threadurl);
//                    if (!TextUtils.isEmpty(albumurl)){
//                        mTitleList.add("专栏");
//                    }
//                    if (!TextUtils.isEmpty(audioalbumurl)){
//                        mTitleList.add("音频");
//                    }
//                    if (!TextUtils.isEmpty(tcolumnurl)){
//                        mTitleList.add("视频");
//                    }
//                    if (!TextUtils.isEmpty(questionurl)){
//                        mTitleList.add("问答");
//                    }
//                    if (!TextUtils.isEmpty(tcolumnurl)){
//                        mTitleList.add("评论");
//                    }
                    cutTheme(0);

                }

            }
        });
    }

    private void initView(View view) {
        isFirstOnMeasure = true;
        head = (ImageView) view.findViewById(R.id.head);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        iv_shared = (ImageView) view.findViewById(R.id.iv_shared);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_describe = (TextView) view.findViewById(R.id.tv_describe);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_unfold = (TextView) view.findViewById(R.id.tv_unfold);
        tabsOrder = (TabLayout) view.findViewById(R.id.tabs_order);
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
        tv_more = (LinearLayout) view.findViewById(R.id.more);
        question = (TextView) view.findViewById(R.id.question);
        subscriber = (TextView) view.findViewById(R.id.subscriber);
        free_read = (TextView) view.findViewById(R.id.free_read);
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AskQuestionFragment.class);
                intent.putExtra("uid", uid1);
                startActivity(intent);
            }
        });
        iv_shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (share.getSharecontent() != null && share.getShareimg() != null && share.getSharetitle() != null && share.getShareurl() != null) {
                    ShareDialog.getInstance().showDialog(getActivity(), share.getSharetitle(),
                            share.getSharecontent(), share.getShareurl(), share.getShareimg());

                }

            }
        });

        yuedu = (LinearLayout) view.findViewById(R.id.yuedu);
        scroll_view = (TopFloatScrollView) view.findViewById(R.id.scroll_view);

        tv_content.setEllipsize(TextUtils.TruncateAt.END);

        tv_unfold.setOnClickListener(new UnfoldOnClickListener());
        iv_back.setOnClickListener(new BackOnClickListener());
        scroll_view.setFocusable(true);
        scroll_view.setFocusableInTouchMode(true);
        scroll_view.requestFocus();

        scroll_view.registerOnScrollViewScrollToBottom(new TopFloatScrollView.OnScrollBottomListener() {
            @Override
            public void onLoading() {
                int position = (int) scroll_view.getTag();
                if (position == 0) {
                    Log.e("TAG", "专栏");
                    mListener.IsColumnbottom();
                } else if (position == 1) {
                    Log.e("TAG", "音频");
                    mListenerVoice.IsvoiceBottom();
                } else if (position == 2) {
                    Log.e("onload", "视频");
                    mListenerVideo.IsVideoBottom();
                } else if (position == 3) {
                    Log.e("onload", "问答");

                    mListenerWenda.IswendaBottom();
                } else if (position == 4) {
                    Log.e("onload", "评论");
                    mListenerComment.IsCommentBottom();
                }
            }
        });

        initTabLayout();

    }

    //初始化 TabLayout
    private void initTabLayout() {
//
            mTitleList.add("专栏");
            mTitleList.add("音频");
            mTitleList.add("视频");
            mTitleList.add("问答");
            mTitleList.add("评论");
        //设置tab模式，当前为系统默认模式
        tabsOrder.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabsOrder.addTab(tabsOrder.newTab().setText(mTitleList.get(0)));
        tabsOrder.addTab(tabsOrder.newTab().setText(mTitleList.get(1)));
        tabsOrder.addTab(tabsOrder.newTab().setText(mTitleList.get(2)));
        tabsOrder.addTab(tabsOrder.newTab().setText(mTitleList.get(3)));
        tabsOrder.addTab(tabsOrder.newTab().setText(mTitleList.get(4)));
        tabsOrder.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                cutTheme(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void cutTheme(int position) {
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragments(transaction);
        switch (position) {
            case 0:
                CurrentPosition = 0;
                scroll_view.setTag(0);
                if (tcolumnurl.equals("")){
                    yuedu.setVisibility(View.GONE);
                }else{
                    yuedu.setVisibility(View.VISIBLE);
                }
                if (detailsColumnFragment == null) {
                    detailsColumnFragment = new DetailsColumnFragment();
                    transaction.add(R.id.fl_content, detailsColumnFragment);
                } else {
                    transaction.show(detailsColumnFragment);
                }

                break;
            case 1:
                scroll_view.setTag(1);
                yuedu.setVisibility(View.GONE);
                CurrentPosition = 1;
                if (experDetailsVoiceFragment == null) {
                    experDetailsVoiceFragment = new ExperDetailsVoiceFragment();
                    transaction.add(R.id.fl_content, experDetailsVoiceFragment);
                } else {
                    transaction.show(experDetailsVoiceFragment);
                }

                break;
            case 2:
                CurrentPosition = 2;
                scroll_view.setTag(2);

                yuedu.setVisibility(View.GONE);

                if (experDetailsVideoFragment == null) {
                    experDetailsVideoFragment = new ExperDetailsVideoFragment();
                    transaction.add(R.id.fl_content, experDetailsVideoFragment);
                } else {
                    transaction.show(experDetailsVideoFragment);
                }
                break;
            case 3:
                scroll_view.setTag(3);

                CurrentPosition = 3;
                yuedu.setVisibility(View.GONE);

                if (experDetailsWendaFragment == null) {
                    experDetailsWendaFragment = new ExperDetailsWendaFragment();
                    transaction.add(R.id.fl_content, experDetailsWendaFragment);
                } else {
                    transaction.show(experDetailsWendaFragment);
                }
                break;
            case 4:
                scroll_view.setTag(4);

                CurrentPosition = 4;
                yuedu.setVisibility(View.GONE);

                if (experDetailsCommentFragment == null) {
                    experDetailsCommentFragment = new ExperDetailsCommentFragment();
                    transaction.add(R.id.fl_content, experDetailsCommentFragment);
                } else {
                    transaction.show(experDetailsCommentFragment);
                }
                break;

        }
        transaction.commitAllowingStateLoss();

        Handler handler = new Handler();
        handler.postDelayed(runnable, 200);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            scroll_view.scrollTo(0, scroll_view.getScrollY());// 改变滚动条的位置
        }
    };

    public void hideFragments(FragmentTransaction ft) {
        if (detailsColumnFragment != null)
            ft.hide(detailsColumnFragment);
        if (experDetailsVoiceFragment != null)
            ft.hide(experDetailsVoiceFragment);
        if (experDetailsVideoFragment != null)
            ft.hide(experDetailsVideoFragment);
        if (experDetailsWendaFragment != null)
            ft.hide(experDetailsWendaFragment);
        if (experDetailsCommentFragment != null) {
            ft.hide(experDetailsCommentFragment);
        }
    }

    class UnfoldOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (isUnfold) { //开
                isUnfold = false;
                tv_content.setMaxLines(Integer.MAX_VALUE);
                Drawable drawable = getResources().getDrawable(R.drawable.fold);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_unfold.setCompoundDrawables(null, null, drawable, null);
                tv_unfold.setText("收起");
            } else {  //关
                isUnfold = true;
                tv_content.setMaxLines(3);
                Drawable drawable = getResources().getDrawable(R.drawable.unfold);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_unfold.setCompoundDrawables(null, null, drawable, null);
                tv_unfold.setText("展开");
            }
        }
    }

    class BackOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            getActivity().finish();
        }
    }


}
