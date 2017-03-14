package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnViewType;
import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.IVoiceService;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.AudioTmessageAdapter;
import com.umiwi.ui.adapter.VoiceDetailsAdapter;
import com.umiwi.ui.beans.AddFavBeans;
import com.umiwi.ui.beans.AudioTmessageBeans;
import com.umiwi.ui.beans.AudioTmessageListBeans;
import com.umiwi.ui.beans.UmiwiBuyCreateOrderBeans;
import com.umiwi.ui.beans.updatebeans.AudioResourceBean;
import com.umiwi.ui.beans.updatebeans.ExperDetailsVoiceBean;
import com.umiwi.ui.beans.updatebeans.VoicePlayBean;
import com.umiwi.ui.dao.CollectionDao;
import com.umiwi.ui.dialog.DownloadAudioListDialog1;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.fragment.pay.PayingFragment;
import com.umiwi.ui.http.parsers.AudioDetailParser;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.AudioManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.AudioModel;
import com.umiwi.ui.util.LoginUtil;
import com.umiwi.ui.util.PermissionUtil;
import com.umiwi.ui.util.Utils;
import com.umiwi.ui.view.RefreshLayout;
import com.umiwi.ui.view.ResizeRelativeLayout;
import com.umiwi.video.control.PlayerController;
import com.umiwi.video.services.VoiceService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.account.manager.UserManager;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.ListViewQuickReturnScrollLoader;
import cn.youmi.framework.util.NetworkManager;
import cn.youmi.framework.util.SharePreferenceUtil;
import cn.youmi.framework.util.ToastU;
import cn.youmi.framework.view.LoadingFooter;

/**
 * Created by Administrator on 2017/3/7.
 */

public class VoiceDetailsFragment extends BaseConstantFragment implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback, ListViewQuickReturnScrollLoader.QuickReturnOnScrollLoader, PopupWindow.OnDismissListener {

    public static final String KEY_DETAILURL = "key.detaiurl";
    /**
     * 音频进度的更新
     */
    private static final int PROGRESS = 1;
    private static final int QUEST_SUCCESS = 2;
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @InjectView(R.id.iv_header)
    ImageView ivHeader;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.last_voice)
    ImageView lastVoice;
    @InjectView(R.id.start_player)
    ImageView startPlayer;
    @InjectView(R.id.next_voice)
    ImageView nextVoice;
    @InjectView(R.id.seekbar)
    SeekBar seekbar;
    //    VoiceService.VoiceBinder mBinder;
//    public static SeekBar seekbar;
    @InjectView(R.id.change_times)
    TextView changeTimes;
    @InjectView(R.id.total_time)
    TextView totalTime;
    private String source;
    private String voices;
    //播放音频的链接地址
    private String url;

    private int page = 1;
    private int totalpage = 1;
    private Context mContext;
    private boolean isRefresh = true;
    private AudioTmessageAdapter tmessageAdapter;
    private List<AudioTmessageListBeans.RecordX.Record> recordList = new ArrayList<>();

    /**
     * VoiceService的代理类
     */
    private IVoiceService service;

    /**
     * 广播
     */
    private MyReceiver myReceiver;
    private Utils utils;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESS:
                    if (UmiwiApplication.mainActivity.service != null) {
                        try {
                            seekbar.setMax(UmiwiApplication.mainActivity.service.getDuration());
                            int currentPosition = UmiwiApplication.mainActivity.service.getCurrentPosition();
                            changeTimes.setText(utils.stringForTime(currentPosition));
                            totalTime.setText(utils.stringForTime(UmiwiApplication.mainActivity.service.getDuration()));
//                        Log.e("TAG", "service.getDuration()=" + service.getDuration());
//                        Log.e("TAG", "service.getCurrentPosition()=" + service.getCurrentPosition());
                            seekbar.setProgress(currentPosition);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;

            }
        }
    };
    private List<VoicePlayBean.RAnserVoicePlay.RDudiao> audioFileList;
    private ExperDetailsVoiceBean experDetailsVoiceBean;
    private VoicePlayBean.RAnserVoicePlay.RDudiao audiofileBean;
    private View rootView;
    private ListView mListView;
    private LinearLayout tab_layout;
    private RadioButton scrollDetail;
    private RadioButton scrollTeacher;
    private RadioButton scrollRelate;
    private RadioButton scrollComment;
    private RadioButton downLoadButton;
    private RadioButton commentButton;
    private LinearLayout bottomBarLayout;
    private RadioButton saveButton;
    private ListViewQuickReturnScrollLoader mScrollLoader;
    private LoadingFooter mLoadingFooter;
    private String herfurl;
    private VoiceDetailsAdapter mAdapter;
    private String albumID;
    private LinearLayout ll_voice_needpay;
    private TextView tv_needpay;
    private RelativeLayout rl_voice_ispay;
    private VoicePlayBean.RAnserVoicePlay infos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        //获取的链接
        herfurl = getActivity().getIntent().getStringExtra(KEY_DETAILURL);
//        herfurl = getActivity().getIntent().getStringExtra(KEY_DETAILURL);
//        herfurl = "http://i.v.youmi.cn/audioalbum/getApi?id=103";
        Log.e("TAG", "herfurl=" + herfurl);
        UmiwiApplication.mainActivity.herfUrl = herfurl;
        //得到音频播放地址
        if (!TextUtils.isEmpty(herfurl)) {
            getInfos(herfurl);
        }
        collectionDao = new CollectionDao();
//        onLoadDetailData();
    }

    private void onLoadDetailData() {
        if (TextUtils.isEmpty(herfurl)) {
            return;
        }
//        showLoading();

        GetRequest<ExperDetailsVoiceBean.AudiofileBean> req = new GetRequest<ExperDetailsVoiceBean.AudiofileBean>(
                herfurl, AudioDetailParser.class, detailListener);
        HttpDispatcher.getInstance().go(req);
    }

    private AbstractRequest.Listener<ExperDetailsVoiceBean.AudiofileBean> detailListener = new AbstractRequest.Listener<ExperDetailsVoiceBean.AudiofileBean>() {
        @Override
        public void onResult(AbstractRequest<ExperDetailsVoiceBean.AudiofileBean> request,
                             ExperDetailsVoiceBean.AudiofileBean audiofileBean) {
//            if (UserManager.getInstance().isLogin() && collectionDao.isSaved(albumID)) {
//                saveButton.setChecked(true);
//            } else {
//                saveButton.setChecked(false);
//            }
            Log.e("TAG", "数据请求成功");
        }

        @Override
        public void onError(AbstractRequest<ExperDetailsVoiceBean.AudiofileBean> requet,
                            int statusCode, String body) {

        }
    };

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_voice_details_layout, null);
        ButterKnife.inject(this, rootView);
        utils = new Utils();
        mSpUtil = UmiwiApplication.getInstance().getSpUtil();
        imageLoader = new ImageLoader(getActivity());
        initRefreshLayout();
        initListener();
        //注册广播
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(VoiceService.OPEN_COMPLETION);
        getActivity().registerReceiver(myReceiver, filter);

        Log.e("TAG", "onCreateView()");
        initViews();
        initCommentEditLayout(inflater);

        return rootView;
    }

    @Override
    public void customScrollInChange() {

    }

    //Listview滑动监听
    @Override
    public void customScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {//0,3,4,10

            if (mListView.getFirstVisiblePosition() == 0) {
                scrollDetail.setChecked(true);
            } else if (mListView.getFirstVisiblePosition() >= 1 && mListView.getFirstVisiblePosition() < 2) {
                scrollRelate.setChecked(true);
            } else if (mListView.getFirstVisiblePosition() >= 2 && mListView.getFirstVisiblePosition() < (2 + audioFileList.size())) {
                scrollTeacher.setChecked(true);
            } else if (mListView.getFirstVisiblePosition() >= (2 + audioFileList.size())) {
                scrollComment.setChecked(true);
            }

        }

    }


    /**
     * 评论控制面板
     */
    private void initCommentEditLayout(LayoutInflater inflater) {

        // 编辑窗口
        View menuLayout = inflater.inflate(R.layout.dailog_comment_write_layout, null);

        mEditMenuWindow = new PopupWindow(menuLayout,
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);
        mEditMenuWindow.setBackgroundDrawable(getActivity().getResources().getDrawable(
                R.color.white));
        mEditMenuWindow.setTouchable(true);
        mEditMenuWindow.setFocusable(true);
//		mEditMenuWindow.setAnimationStyle(R.style.MenuAnimation);
        mEditMenuWindow.setOutsideTouchable(false);
        mEditMenuWindow.update();
        // 监听菜单消失
        mEditMenuWindow.setOnDismissListener(this);

        // 菜单控件
        mEt_menu = (EditText) menuLayout.findViewById(R.id.write_comment_edittext);
        TextView btn_send = (TextView) menuLayout.findViewById(R.id.send_comment);
        btn_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mEt_menu.getText().toString().trim())) {
                    ToastU.showShort(getActivity(), "说点什么吧！");
                    return;
                }
                showComment();
            }
        });
        listenerKeyBoardState(menuLayout);
    }

    private void listenerKeyBoardState(View menuLayout) {
        ResizeRelativeLayout mMenuLayout = (ResizeRelativeLayout) menuLayout
                .findViewById(R.id.menu_layout);
        mMenuLayout
                .setOnResizeRelativeListener(new ResizeRelativeLayout.OnResizeRelativeListener() {
                    @Override
                    public void OnResizeRelative(int w, int h, int oldw,
                                                 int oldh) {
                        mIsKeyboardOpened = false;
                        // 记录第一次打开输入法时的布局高度
                        if (h < oldh && oldh > 0 && mMenuOpenedHeight == 0) {
                            mMenuOpenedHeight = h;
                        }

                        // 布局的高度小于之前的高度
                        if (h < oldh) {
                            mIsKeyboardOpened = true;
                        }
                        // 或者输入法打开情况下,
                        // 输入字符后再清除(三星输入法软键盘在输入后，软键盘高度增加一行，清除输入后，高度变小，但是软键盘仍是打开状态)
                        else if ((h <= mMenuOpenedHeight)
                                && (mMenuOpenedHeight != 0)) {
                            mIsKeyboardOpened = true;
                        }

                    }
                });
    }

    /**
     * 提交评论
     */
    private void showComment() {
        String url = String.format(UmiwiAPI.audio_tmessage, experDetailsVoiceBean.getId(), mEt_menu.getText().toString().trim());
        GetRequest<AudioTmessageBeans> request = new GetRequest<AudioTmessageBeans>(
                url, GsonParser.class,
                AudioTmessageBeans.class,
                AudioListener);
        request.go();
//        closeButtomSend();
    }

    private AbstractRequest.Listener<AudioTmessageBeans> AudioListener = new AbstractRequest.Listener<AudioTmessageBeans>() {

        @Override
        public void onResult(AbstractRequest<AudioTmessageBeans> request, AudioTmessageBeans tmessageBeans) {
            if (tmessageBeans.getE().equals("9999")) {
                ToastU.showShort(getActivity(), "评论提交成功!");
                if (mEditMenuWindow.isShowing()) {
                    mEditMenuWindow.dismiss();
                }
                isRefresh = true;
                showCommentList();
            }
        }

        @Override
        public void onError(AbstractRequest<AudioTmessageBeans> requet, int statusCode, String body) {
        }
    };

    /**
     * 获取评论列表
     */
    private void showCommentList() {
        String url = String.format(UmiwiAPI.audio_tmessage_list, page, experDetailsVoiceBean.getUid(), "audioalbum", experDetailsVoiceBean.getId());
        GetRequest<AudioTmessageListBeans> request = new GetRequest<AudioTmessageListBeans>(
                url, GsonParser.class,
                AudioTmessageListBeans.class,
                AudioListenerList);
        request.go();
    }

    private AbstractRequest.Listener<AudioTmessageListBeans> AudioListenerList = new AbstractRequest.Listener<AudioTmessageListBeans>() {

        @Override
        public void onResult(AbstractRequest<AudioTmessageListBeans> request, AudioTmessageListBeans tmessageBeans) {
            AudioTmessageListBeans.RecordX.PageBean page = tmessageBeans.getR().getPage();
            Log.e("评论列表", tmessageBeans.getR().getRecord().toString());
            totalpage = page.getTotalpage();
            if (isRefresh) {
                refreshLayout.setRefreshing(false);
                recordList.clear();
            } else {
                refreshLayout.setLoading(false);
            }
            recordList.addAll(tmessageBeans.getR().getRecord());
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onError(AbstractRequest<AudioTmessageListBeans> requet, int statusCode, String body) {
            if (isRefresh) {
                refreshLayout.setRefreshing(false);
            } else {
                refreshLayout.setLoading(false);
            }
        }
    };


    //初始化播放器底下视图
    private void initViews() {
        mListView = (ListView) rootView.findViewById(R.id.listView);
        mListView.setSelector(R.color.transparent);

        View mPlaceHolderView = getActivity().getLayoutInflater().inflate(
                R.layout.view_header_placeholder, mListView, false);
        mListView.addHeaderView(mPlaceHolderView);
        ll_voice_needpay = (LinearLayout) rootView.findViewById(R.id.ll_voice_needpay);
        ll_voice_needpay.setVisibility(View.GONE);
        rl_voice_ispay = (RelativeLayout) rootView.findViewById(R.id.rl_voice_ispay);
        rl_voice_ispay.setVisibility(View.VISIBLE);
        tv_needpay = (TextView) rootView.findViewById(R.id.tv_needpay);

        mLoadingFooter = new LoadingFooter(getActivity());// 加载更多的view
        int bottomHeight = getResources().getDimensionPixelSize(R.dimen.bottom_bar_height);


        tab_layout = (LinearLayout) rootView.findViewById(R.id.tab_layout);
        bottomBarLayout = (LinearLayout) rootView.findViewById(R.id.bottom_bar_container);
        mScrollLoader = new ListViewQuickReturnScrollLoader(QuickReturnViewType.FOOTER, null, 0, bottomBarLayout, bottomHeight, this, mLoadingFooter);

        scrollDetail = (RadioButton) rootView.findViewById(R.id.scroll_detail);
        scrollTeacher = (RadioButton) rootView.findViewById(R.id.scroll_teacher);
        scrollRelate = (RadioButton) rootView.findViewById(R.id.scroll_relate);
        scrollComment = (RadioButton) rootView.findViewById(R.id.scroll_comment);

        scrollDetail.setOnClickListener(scrollListener);
        scrollTeacher.setOnClickListener(scrollListener);
        scrollRelate.setOnClickListener(scrollListener);
        scrollComment.setOnClickListener(scrollListener);

        //下载
        downLoadButton = (RadioButton) rootView.findViewById(R.id.download_button);


        //评论
        commentButton = (RadioButton) rootView.findViewById(R.id.comment_button);
        commentButton.setOnClickListener(writeCommentListener);

        //收藏
        saveButton = (RadioButton) rootView.findViewById(R.id.fav_button);
        saveButton.setOnClickListener(favClickListener);
        //分享
        rootView.findViewById(R.id.share_radiobutton).setOnClickListener(shareCourseClickListener);

        //listview滑动监听
        mListView.setOnScrollListener(mScrollLoader);


    }

    //点击标题
    private View.OnClickListener scrollListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //详情
                case R.id.scroll_detail:
                    mListView.setSelection(0);
                    break;
                //目录
                case R.id.scroll_relate:
//                    if (audioFileList != null && audioFileList.size() > 1) {
//                        mListView.setSelection(2);
//                    } else {
//                        mListView.setSelection(1);
//                    }
                    mListView.smoothScrollToPositionFromTop(1, DimensionUtil.dip2px(-90));
                    break;
                //讲师
                case R.id.scroll_teacher:
                    mListView.smoothScrollToPositionFromTop(2, DimensionUtil.dip2px(-90));
                    break;
                //评论
                case R.id.scroll_comment:
                    if (mAdapter.isEmpty()) {
                        return;
                    }
                    mListView.smoothScrollToPositionFromTop(2 + audioFileList.size(), DimensionUtil.dip2px(-70));
                    break;
                default:
                    break;
            }
            mListView.invalidate();
        }
    };
    private CollectionDao collectionDao;
    //收藏
    private View.OnClickListener favClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(albumID)) {
                return;
            }

            if (!UserManager.getInstance().isLogin()) {
                saveButton.setChecked(false);
                showLogin();
                return;
            }
            if (!collectionDao.isSaved(albumID)) {
                // 收藏
                addFav();
                MobclickAgent.onEvent(getActivity(), "详情页面V", "收藏");
            } else {
                removeFav();
            }
        }
    };

    //收藏
    private void addFav() {
        albumID = String.valueOf(infos.getId());

        if (TextUtils.isEmpty(albumID)) {
            return;
        }

        collectionDao.saveCollection(albumID);

        String favStr = String.format(UmiwiAPI.UMIWI_FAV_ADD_VIDEO_ALBUMID, albumID);

        GetRequest<AddFavBeans.AddFavBeansRequestData> req = new GetRequest<AddFavBeans.AddFavBeansRequestData>(
                favStr, GsonParser.class,
                AddFavBeans.AddFavBeansRequestData.class, favListener);
        HttpDispatcher.getInstance().go(req);
    }

    //移除收藏
    private void removeFav() {
        albumID = String.valueOf(infos.getId());

        if (TextUtils.isEmpty(albumID)) {
            return;
        }

        collectionDao.deleteCollectionCompelete(albumID);

        String favStr = String.format(UmiwiAPI.FAV_REMOVE_VIDEO_ALBUMID + "&isalbum=y", albumID);

        GetRequest<AddFavBeans.AddFavBeansRequestData> req = new GetRequest<AddFavBeans.AddFavBeansRequestData>(
                favStr, GsonParser.class,
                AddFavBeans.AddFavBeansRequestData.class, removeListener);
        HttpDispatcher.getInstance().go(req);
    }

    private AbstractRequest.Listener<AddFavBeans.AddFavBeansRequestData> removeListener = new AbstractRequest.Listener<AddFavBeans.AddFavBeansRequestData>() {

        @Override
        public void onResult(AbstractRequest<AddFavBeans.AddFavBeansRequestData> request,
                             AddFavBeans.AddFavBeansRequestData t) {
            collectionDao.updateCollection(albumID);
            changeSaveButton();
            ToastU.showShort(getActivity(), "取消收藏");
        }

        @Override
        public void onError(AbstractRequest<AddFavBeans.AddFavBeansRequestData> requet,
                            int statusCode, String body) {
            ToastU.showShort(getActivity(), body);
            changeSaveButton();
        }
    };

    private AbstractRequest.Listener<AddFavBeans.AddFavBeansRequestData> favListener = new AbstractRequest.Listener<AddFavBeans.AddFavBeansRequestData>() {

        @Override
        public void onResult(AbstractRequest<AddFavBeans.AddFavBeansRequestData> request,
                             AddFavBeans.AddFavBeansRequestData t) {
            collectionDao.updateCollection(albumID);
            changeSaveButton();
            ToastU.showShort(getActivity(), "收藏成功");
        }

        @Override
        public void onError(AbstractRequest<AddFavBeans.AddFavBeansRequestData> requet,
                            int statusCode, String body) {
            ToastU.showShort(getActivity(), body);
            changeSaveButton();
        }
    };

    //改变收藏按钮状态
    private void changeSaveButton() {
        if (collectionDao.isSaved(albumID)) {
            saveButton.setChecked(true);
        } else {
            saveButton.setChecked(false);
        }
    }

    private void showLogin() {
        LoginUtil.getInstance().showLoginView(getActivity());
    }

    //评论
    private View.OnClickListener writeCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!YoumiRoomUserManager.getInstance().isLogin()) {
//                PlayerController.getInstance().pause();
                showLogin();
                return;
            }

            if (null == experDetailsVoiceBean) {
                return;
            }

            if (YoumiRoomUserManager.getInstance().isLogin()) {
//				WriteCommentDialog.getInstance().showDialog(getActivity(), albumID);
//				StatisticsManager.getInstance().getResultInfo(StatisticsUrl.DETAIL_COMMENT_WRITE);
                getActivity().setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                clickTopSend();
            } else {
                ToastU.showShort(getActivity(), "您没有权限,请先购买该课程后再评论.");
            }

        }
    };
    private PopupWindow mEditMenuWindow;
    private EditText mEt_menu;
    private boolean mIsKeyboardOpened;// 软键盘是否显示
    private int mMenuOpenedHeight;// 编辑菜单打开时的高度
    // 点击底部发送按钮，关闭编辑窗口
    private void closeButtomSend() {
        mEditMenuWindow.dismiss();
    }

    // 点击顶部发送按钮,打开/关闭编辑窗口
    private void clickTopSend() {
        if (mEditMenuWindow.isShowing()) {
            // 先关闭窗口再隐藏软键盘
            mEditMenuWindow.dismiss();

            // 隐藏输入法软键盘
            // hideKeyBoard();
        } else {
            // 窗口显示前显示输入法软键盘
            showKeyBoard();
            // 显示输入窗口
            mEditMenuWindow.showAtLocation(rootView, Gravity.CENTER,0 ,0 );
        }
    }

    // 窗口显示前显示输入法软键盘
    private void showKeyBoard() {
        InputMethodManager inputMgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);// 调用此方法才能自动打开输入法软键盘
        mEditMenuWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mEditMenuWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED); // 在显示popupwindow之后调用，否则输入法会在窗口底层
    }

    // 隐藏输入法软键盘
    private void hideKeyBoard() {
        InputMethodManager inputMgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);// 输入法软键盘打开时关闭,关闭时打开
        mEditMenuWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mEditMenuWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED); // 在显示popupwindow之后调用，否则输入法会在窗口底层

    }

    private String[] REQUEST_STORAGE_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_PERMISSION_CODE_TAKE_STORAGE = 7;
    //下载
    private View.OnClickListener downloadListClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!YoumiRoomUserManager.getInstance().isLogin()) {
                PlayerController.getInstance().pause();
                showLogin();
                return;
            }
            if (null == infos) {
                return;
            }

            if (!infos.getIspay()) {
                ToastU.showShort(getActivity(), "您没有权限,请先购买该课程后再下载.");
                return;
            }

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Snackbar.make(mListView, "需要授权读取存储卡", Snackbar.LENGTH_INDEFINITE)
                            .setAction("授权", new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(getActivity(), REQUEST_STORAGE_PERMISSIONS, REQUEST_PERMISSION_CODE_TAKE_STORAGE);
                                }
                            })
                            .show();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), REQUEST_STORAGE_PERMISSIONS, REQUEST_PERMISSION_CODE_TAKE_STORAGE);
                }
            }

            //下载的dialog
            DownloadAudioListDialog1.getInstance().showDialog(getActivity(), getAudios());
            MobclickAgent.onEvent(getActivity(), "详情页面V", "下载");
        }

    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE_TAKE_STORAGE:
                if (PermissionUtil.verifyPermissions(grantResults)) {
                    DownloadAudioListDialog1.getInstance().showDialog(getActivity(), getAudios());
                    MobclickAgent.onEvent(getActivity(), "详情页面V", "下载");
                } else {
                    ToastU.showShort(getActivity(), "无法读取存储卡，请先授权");
                }
                break;
        }
    }

    public ArrayList<AudioModel> getAudios() {
        if (infos == null) {
            return null;
        }
        Log.e("TAG", "experDetailsVoiceBean==" + infos.getAudiofile().size());
        ArrayList<AudioModel> videos = new ArrayList<AudioModel>(infos
                .getAudiofile().size());

        for (int i = 0; i < infos.getAudiofile().size(); i++) {
            VoicePlayBean.RAnserVoicePlay.RDudiao audiofileBean = infos.getAudiofile().get(i);
            Log.e("TAG", "audiofileBean=" + audiofileBean.getAid());
            AudioModel video = AudioManager.getInstance().getVideoById(
                    audiofileBean.getAid() + "");
            video.setVideoUrl(source);
            Log.e("TAG", "设置audio的URL=" + source);
            videos.add(video);
            Log.e("TAG", "video=" + video.getAlbumTitle());//怎么对公司进行调研
            Log.e("TAG", "video=" + video.getVideoId());//57
            Log.e("TAG", "video=" + video.getAlbumId());//55
            Log.e("TAG", "video=" + video.getFileName());//null
            Log.e("TAG", "video=" + video.getTitle());//怎样对公司进行调研
            Log.e("TAG", "video=" + video.getVideoUrl());//http://v.youmi.cn/audioalbum/playsourceapi?audioalbumid=55&audiofileid=57
        }
        return videos;
    }

    //分享
    private View.OnClickListener shareCourseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (infos == null) {
                return;
            }

            PlayerController.getInstance().pause();
            ShareDialog.getInstance().showDialog(getActivity(),
                    infos.getShare().getSharetitle(), infos.getShare().getSharecontent(),
                    infos.getShare().getShareurl(), infos.getShare().getShareimg());
        }
    };
    private ImageLoader imageLoader;

    private void getInfos(String herfurl) {

        GetRequest<VoicePlayBean> request = new GetRequest<VoicePlayBean>(
                herfurl, GsonParser.class,
                VoicePlayBean.class, new AbstractRequest.Listener<VoicePlayBean>() {
            @Override
            public void onResult(AbstractRequest<VoicePlayBean> request, VoicePlayBean voicePlayBean) {
                Log.e("ssb", "onResult: "+voicePlayBean.getR().getImage() );



                infos = voicePlayBean.getR();
                String image = infos.getImage();
                albumID = infos.getId();
                imageLoader.loadImage(image, ivHeader, R.drawable.icon_umiwi);
                audioFileList = infos.getAudiofile();
                if (audioFileList != null && audioFileList.size() > 0) {
                    if (audioFileList != null && audioFileList.size() > 0) {
                        audiofileBean = audioFileList.get(0);
//                        source = audiofileBean.getSource();
                        url = audiofileBean.getSource();
                        Log.e("url", "url =" + url);
                        if (url != null) {
                            //判断是否显示需要支付的view
                            //如果是免费或是已经支付
                            if (infos.getIspay()) {
                                ll_voice_needpay.setVisibility(View.GONE);
                                rl_voice_ispay.setVisibility(View.VISIBLE);
                                Log.e("aaa",url);

                                getData(url);
                            } else {
                                //没有支付
                                startPlayer.setClickable(false);
                                ll_voice_needpay.setVisibility(View.VISIBLE);
                                rl_voice_ispay.setVisibility(View.GONE);
                                seekbar.setVisibility(View.INVISIBLE);
                                tv_needpay.setText("支付 ￥" + infos.getPrice() + "元");
                                tv_needpay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getOrderId(infos.getId());

                                    }
                                });
                            }

                        }

                        //请求音频数据，保存数据库中去
                        for (VoicePlayBean.RAnserVoicePlay.RDudiao db : infos.getAudiofile()) {
                            AudioModel vm = AudioManager.getInstance().getVideoById(db.getAid() + "");
                            if (vm == null) {
                                vm = new AudioModel();
                            }

                            String albumTitle = infos.getTitle();
                            if (albumTitle == null) {
                                albumTitle = infos.getTitle();
                            }

                            vm.setTitle(db.getAtitle());
                            vm.setVideoUrl(db.getSource());
                            vm.setVideoId(db.getAid() + "");
                            vm.setAlbumId(infos.getId() + "");
                            vm.setAlbumTitle(albumTitle);
                            vm.setExpiretime(db.getAplaytime());
                            vm.setUid(YoumiRoomUserManager.getInstance().getUid());
                            vm.setImageURL(infos.getTutorimage());
//                            vm.setAlbumImageurl(experDetailsVoiceBean.getTutorimage());
                            vm.setTry(false);
//				vm.setLastwatchposition((int) (db.getDuration() * db.getWatchProgress() / 100.0f * 1000));

                            AudioManager.getInstance().saveVideo(vm);
                        }
                    }
                }

                mAdapter = new VoiceDetailsAdapter(getActivity(), audioFileList, infos,recordList, VoiceDetailsFragment.this);
                mListView.setAdapter(mAdapter);
                Log.e("TAG", "11111audioFileList=" + audioFileList);
                Log.e("TAG", "1111111mAdapter=" + mAdapter);

                mAdapter.setWriteCommenntViewOnClickListener(writeCommentListener);
//                    收藏按钮
                if (UserManager.getInstance().isLogin() && collectionDao.isSaved(albumID)) {
                    saveButton.setChecked(true);
                } else {
                    saveButton.setChecked(false);
                }
                //下载
                downLoadButton.setOnClickListener(downloadListClickListener);
                showCommentList();


            }

            @Override
            public void onError(AbstractRequest<VoicePlayBean> requet, int statusCode, String body) {

            }
        });
        request.go();

//
//        OkHttpUtils.get().url(herfurl).build().execute(new CustomStringCallBack() {
//            @Override
//            public void onFaild() {
//                Log.e("data", "音频url获取失败");
//
//            }
//
//            @Override
//            public void onSucess(String data) {
//                Log.e("TAG", "音频url获取成功=" + data);
//                if (!TextUtils.isEmpty(data)) {
//                    experDetailsVoiceBean = JsonUtil.json2Bean(data, ExperDetailsVoiceBean.class);
//                    String imageurl = experDetailsVoiceBean.getImage();
//                    Log.e("TAG", "imageurl=" + imageurl);
//                    albumID = experDetailsVoiceBean.getId();
//                    imageLoader.loadImage(imageurl, ivHeader, R.drawable.icon_umiwi);
//
//
//                    audioFileList = experDetailsVoiceBean.getAudiofile();
//                    if (audioFileList != null && audioFileList.size() > 0) {
//                        audiofileBean = audioFileList.get(0);
////                        source = audiofileBean.getSource();
//                        url = audiofileBean.getSource();
//                        Log.e("TAG", "url =" + url);
////                        if (url!=null){
//                            UmiwiApplication.getInstance().getBinder().searchInfo(source);
////                        }
//                        if (url != null) {
//                            //判断是否显示需要支付的view
//                            //如果是免费或是已经支付
//                            if(experDetailsVoiceBean.isIspay()) {
//                                ll_voice_needpay.setVisibility(View.GONE);
//                                rl_voice_ispay.setVisibility(View.VISIBLE);
//                                getData(url);
//                            }else {
//                                //没有支付
//                                startPlayer.setClickable(false);
//                                ll_voice_needpay.setVisibility(View.VISIBLE);
//                                rl_voice_ispay.setVisibility(View.GONE);
//                                seekbar.setVisibility(View.INVISIBLE);
//                                tv_needpay.setText("支付 ￥" + experDetailsVoiceBean.getPrice() + "元");
//                                tv_needpay.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        getOrderId(experDetailsVoiceBean.getId());
//                                    }
//                                });
//                            }
//
//                        }
//                        //请求音频数据，保存数据库中去
//                        for (ExperDetailsVoiceBean.AudiofileBean db : experDetailsVoiceBean.getAudiofile()) {
//                            AudioModel vm = AudioManager.getInstance().getVideoById(db.getAid() + "");
//                            if (vm == null) {
//                                vm = new AudioModel();
//                            }
//
//                            String albumTitle = experDetailsVoiceBean.getTitle();
//                            if (albumTitle == null) {
//                                albumTitle = experDetailsVoiceBean.getTitle();
//                            }
//
//                            vm.setTitle(db.getAtitle());
//                            vm.setVideoUrl(db.getSource());
//                            vm.setVideoId(db.getAid() + "");
//                            vm.setAlbumId(experDetailsVoiceBean.getId() + "");
//                            vm.setAlbumTitle(albumTitle);
//                            vm.setExpiretime(db.getAplaytime());
//                            vm.setUid(YoumiRoomUserManager.getInstance().getUid());
//                            vm.setImageURL(experDetailsVoiceBean.getTutorimage());
////                            vm.setAlbumImageurl(experDetailsVoiceBean.getTutorimage());
//                            vm.setTry(false);
////				vm.setLastwatchposition((int) (db.getDuration() * db.getWatchProgress() / 100.0f * 1000));
//
//                            AudioManager.getInstance().saveVideo(vm);
//                        }
//
//                    }
//
//
//
//                    mAdapter = new VoiceDetailsAdapter(getActivity(), audioFileList, experDetailsVoiceBean,recordList, VoiceDetailsFragment.this);
//                    mListView.setAdapter(mAdapter);
//                    Log.e("TAG", "11111audioFileList=" + audioFileList);
//                    Log.e("TAG", "1111111mAdapter=" + mAdapter);
//
//                    mAdapter.setWriteCommenntViewOnClickListener(writeCommentListener);
////                    收藏按钮
//                    if (UserManager.getInstance().isLogin() && collectionDao.isSaved(albumID)) {
//                        saveButton.setChecked(true);
//                    } else {
//                        saveButton.setChecked(false);
//                    }
//                    //下载
//                    downLoadButton.setOnClickListener(downloadListClickListener);
//                    showCommentList();
//
//
//
//                }
//            }
//        });
    }

    private void getData(String url) {
        GetRequest<AudioResourceBean> request = new GetRequest<AudioResourceBean>(
                url, GsonParser.class,
                AudioResourceBean.class, new AbstractRequest.Listener<AudioResourceBean>() {
            @Override
            public void onResult(AbstractRequest<AudioResourceBean> request, AudioResourceBean audioResourceBean) {

                AudioResourceBean.RAudioRes r = audioResourceBean.getR();
                 source = r.getSource();
                if (source!=null){
                    bindVoiceSerive();
                }

            }

            @Override
            public void onError(AbstractRequest<AudioResourceBean> requet, int statusCode, String body) {

            }
        });
        request.go();

    }

    private void initListener() {
        startPlayer.setOnClickListener(this);
        lastVoice.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        nextVoice.setOnClickListener(this);
        seekbar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());


    }


    @Override
    public void onDismiss() {
        // 如果软键盘打开,隐藏输入法软键盘
        if (mIsKeyboardOpened) {
            hideKeyBoard();
        }
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                seekbar.setMax(UmiwiApplication.mainActivity.service.getDuration());
                //发消息更新音频播放的进度
                handler.sendEmptyMessage(PROGRESS);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            Log.e("TAG", "progress=" + progress);
            try {
                if (fromUser) {
                    UmiwiApplication.mainActivity.service.seekTo(progress);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private ServiceConnection conn = null;

    /**
     * 绑定服务
     */
    public void bindVoiceSerive() {
        //发消息，更新进度
        handler.sendEmptyMessage(PROGRESS);
        if (source.equals(UmiwiApplication.mainActivity.musicUrl)) {
            //如果再次进来当前音乐暂停，就继续播放
            try {
                if (!UmiwiApplication.mainActivity.service.isPlaying()) {
                    UmiwiApplication.mainActivity.service.play();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return;
        }
        //保存当前音频链接地址
        UmiwiApplication.mainActivity.musicUrl = source;
        Intent intent = new Intent(UmiwiApplication.mainActivity, VoiceService.class);
//        intent.setAction("com.umiwi.video.action.BIND_SERVICE");
        if (conn == null) {

            conn = new ServiceConnection() {
                /**
                 * 当绑定服务成功的时候回调这个方法
                 * @param name
                 * @param iBinder
                 */
                @Override
                public void onServiceConnected(ComponentName name, IBinder iBinder) {
                    Log.e("TAG", "ibinder=" + iBinder);
                    UmiwiApplication.mainActivity.service = IVoiceService.Stub.asInterface(iBinder);

                    if (UmiwiApplication.mainActivity.service != null) {
                        try {
                            UmiwiApplication.mainActivity.service.openAudio(source);
                            Log.e("TAG", "source=in" + source);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                }

                /**
                 * 当和服务断掉的时候回调这个方法
                 * @param name
                 */
                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.e("TAG", "name=" + name);

                }
            };

        }
        intent.putExtra("source", source);
        UmiwiApplication.mainActivity.bindService(intent, conn, getActivity().BIND_AUTO_CREATE);
//        getActivity().startService(intent);
//        intent.putExtra("url", url);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().finish();

                break;
            case R.id.last_voice:
                Toast.makeText(getActivity(), "已是最前", Toast.LENGTH_SHORT)
                        .show();

                break;
            case R.id.start_player:
                try {
                    if (UmiwiApplication.mainActivity.service.isPlaying()) {
                        //暂停
                        UmiwiApplication.mainActivity.service.pause();
                        startPlayer.setBackgroundResource(R.drawable.pause_player);
                    } else {
                        UmiwiApplication.mainActivity.service.play();
                        startPlayer.setBackgroundResource(R.drawable.start_player);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.next_voice:
                Toast.makeText(getActivity(), "已是最后", Toast.LENGTH_SHORT)
                        .show();
                break;

        }
    }


    private class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            mBinder = (VoiceService.VoiceBinder) iBinder;
            Log.e("mbinder", "拿到代理人对象");
         /*   mBinder.playVoice(voices);
            seekbar.setMax(mBinder.getDuration());
            handler.post(runnable);*/
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
//        handler.removeCallbacks(runnable);
        getActivity().unregisterReceiver(myReceiver);
        Log.e("TAG", "onDestory()");
        handler.removeCallbacksAndMessages(null);

//        if (conn != null) {
//            getActivity().unbindService(conn);
//            conn = null;
//        }

        super.onDestroy();
    }

    private void initRefreshLayout() {
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.main_color));
        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                page++;
                isRefresh = false;
                if (page <= totalpage) {
                    refreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showCommentList();
                        }
                    }, 1000);

                } else {
                    ToastU.showLong(mContext, "没有更多了!");
                    refreshLayout.setLoading(false);

                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                page = 1;
                showCommentList();
            }
        });

    }

    private SharePreferenceUtil mSpUtil;
    private NetworkManager.OnNetworkChangeListener networkChangeListener = new NetworkManager.OnNetworkChangeListener() {
        @Override
        public void onNetworkChange() {
            if (!NetworkManager.getInstance().isWifi()) {
//                PlayerController.getInstance().pause();
                if (mSpUtil.getPlayWith3G()) {
                    ToastU.showShort(getActivity(), getActivity().getApplicationContext().getString(R.string.show_3g_toast));
                } else {
                    if (mSpUtil.getShow3GDialog()) {
                        ToastU.showShort(getActivity(), "当前网络不可用");
                        return;
                    } else {
//                        showNetorkDialog();
                        return;
                    }
                }
            }
        }
    };


    /**
     * 获取提问的payurl
     *
     * @param questionId 问题id
     */
    private void getOrderId(String questionId) {
        String url = null;
        url = String.format(UmiwiAPI.CREATE_BUYAUDIO, "json", questionId, "");
        GetRequest<UmiwiBuyCreateOrderBeans> request = new GetRequest<UmiwiBuyCreateOrderBeans>(
                url, GsonParser.class,
                UmiwiBuyCreateOrderBeans.class,
                addQuestionOrderListener);
        request.go();
    }

    private AbstractRequest.Listener<UmiwiBuyCreateOrderBeans> addQuestionOrderListener = new AbstractRequest.Listener<UmiwiBuyCreateOrderBeans>() {
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
}
