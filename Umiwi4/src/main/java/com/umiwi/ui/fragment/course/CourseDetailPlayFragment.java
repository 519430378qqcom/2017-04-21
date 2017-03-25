package com.umiwi.ui.fragment.course;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnViewType;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.CourseDetailsAdapter;
import com.umiwi.ui.adapter.CourseDetailsAdapter.VideoClickListener;
import com.umiwi.ui.beans.AddFavBeans;
import com.umiwi.ui.beans.AddFavBeans.AddFavBeansRequestData;
import com.umiwi.ui.beans.CommentListBeans;
import com.umiwi.ui.beans.CommentResultBean;
import com.umiwi.ui.beans.UmiwiListBeans;
import com.umiwi.ui.beans.UmiwiListDetailBeans;
import com.umiwi.ui.beans.UmiwiListDetailBeans.ListDetailRequestData;
import com.umiwi.ui.dao.CollectionDao;
import com.umiwi.ui.dialog.DownloadListDialog;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.fragment.EditNoteFragment;
import com.umiwi.ui.fragment.pay.PayOrderDetailFragment;
import com.umiwi.ui.fragment.pay.PayTypeEvent;
import com.umiwi.ui.http.parsers.CourseDetailParser;
import com.umiwi.ui.http.parsers.RelatedVideosParser;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.StatisticsUrl;
import com.umiwi.ui.managers.VideoManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.UserClass;
import com.umiwi.ui.model.VideoModel;
import com.umiwi.ui.util.LoginUtil;
import com.umiwi.ui.util.PermissionUtil;
import com.umiwi.ui.view.ResizeRelativeLayout;
import com.umiwi.video.control.IMediaPlayerController;
import com.umiwi.video.control.PlayerController;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.account.event.UserEvent;
import cn.youmi.account.manager.UserManager;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.debug.LogUtils;
import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.PostRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.util.AndroidSDK;
import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.ListViewQuickReturnScrollLoader;
import cn.youmi.framework.util.ListViewQuickReturnScrollLoader.QuickReturnOnScrollLoader;
import cn.youmi.framework.util.NetworkManager;
import cn.youmi.framework.util.NetworkManager.OnNetworkChangeListener;
import cn.youmi.framework.util.SharePreferenceUtil;
import cn.youmi.framework.util.ToastU;
import cn.youmi.framework.view.LoadingFooter;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by youmi on 15/10/10. new playview and coursedetail
 */
public class CourseDetailPlayFragment extends BaseFragment implements QuickReturnOnScrollLoader, OnDismissListener, ActivityCompat.OnRequestPermissionsResultCallback {

    public static String TAG = "play_fragment";

    public static final String KEY_DETAIURL = "key.detaiurl";

    private RadioButton saveButton;

    private RadioButton scrollComment;
    private RadioButton scrollRelate;
    private RadioButton scrollTeacher;
    private RadioButton scrollDetail;

    private RadioButton downLoadButton;
    private RadioButton commentButton;

    private ListView mListView;
    private CourseDetailsAdapter mAdapter;
    private ArrayList<CommentListBeans> mList;
    private ListDetailRequestData mRequestData;

    private LoadingFooter mLoadingFooter;
    private ListViewQuickReturnScrollLoader mScrollLoader;
    private LinearLayout bottomBarLayout;

    private View courseCompetencePanelLayout;
    private RelativeLayout bottomcompetencePanel;
    private TextView bottomCompetenceContent;
    private TextView bottomCoursePrice;
    private TextView bottomCourseOldPrice;
    private TextView bottomCourseBuyButton;
    private TextView bottomCompetenceButton;

    private LinearLayout competenceMiddleContainer;
    private TextView middleCompetenceButton;
    private TextView middleCompetenceContent;
    private ImageView middlePlayButton;
    private ImageView competenceReturnButton;

    private TextView tryCourseContent;

    public String detaiURL;
    private String albumID;

    public boolean loadingVideo;

    private boolean isRefresh;

    private ProgressBar mProgressBar;

    private CollectionDao collectionDao;

    private View rightPanelLayout;
    private ListView playListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (TextUtils.isEmpty(getActivity().getIntent().getStringExtra(KEY_DETAIURL)))
            return;

        detaiURL = getActivity().getIntent().getStringExtra(KEY_DETAIURL);
        Log.e("TAG", "detailRUL==" + detaiURL);
        PlayerController.getInstance().initPlayerController(getActivity());
        PlayerController.getInstance().initNativeMediaPlayer();
        collectionDao = new CollectionDao();
        NetworkManager.getInstance().addListener(networkChangeListener);

        if (AndroidSDK.isLOLLIPOP()) {
            Window window = getActivity().getWindow();
            window.setStatusBarColor(Color.parseColor("#ff000000"));
        }
    }

    @SuppressLint("InflateParams")
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_course_detail_play_layout, null);

        mSpUtil = UmiwiApplication.getInstance().getSpUtil();

        mListView = (ListView) rootView.findViewById(R.id.listView);
        mListView.setSelector(R.color.transparent);

        scrollDetail = (RadioButton) rootView.findViewById(R.id.scroll_detail);
        scrollTeacher = (RadioButton) rootView.findViewById(R.id.scroll_teacher);
        scrollRelate = (RadioButton) rootView.findViewById(R.id.scroll_relate);
        scrollComment = (RadioButton) rootView.findViewById(R.id.scroll_comment);

        scrollDetail.setOnClickListener(scrollListener);
        scrollTeacher.setOnClickListener(scrollListener);
        scrollRelate.setOnClickListener(scrollListener);
        scrollComment.setOnClickListener(scrollListener);

        downLoadButton = (RadioButton) rootView.findViewById(R.id.download_button);
        downLoadButton.setOnClickListener(downloadListClickListener);

        rootView.findViewById(R.id.share_radiobutton).setOnClickListener(shareCourseClickListener);

        commentButton = (RadioButton) rootView.findViewById(R.id.comment_button);
        commentButton.setOnClickListener(writeCommentListener);


        saveButton = (RadioButton) rootView.findViewById(R.id.fav_button);
        saveButton.setOnClickListener(favClickListener);

        bottomBarLayout = (LinearLayout) rootView.findViewById(R.id.bottom_bar_container);

        FrameLayout player_container = (FrameLayout) rootView.findViewById(R.id.player_container);

        mList = new ArrayList<>();
        mAdapter = new CourseDetailsAdapter(getActivity(), mList, this);


        int headerHeight = getResources().getDimensionPixelSize(R.dimen.header_height);

        int bottomHeight = getResources().getDimensionPixelSize(R.dimen.bottom_bar_height);

        View mPlaceHolderView = getActivity().getLayoutInflater().inflate(
                R.layout.view_header_placeholder, mListView, false);
        mListView.addHeaderView(mPlaceHolderView);

        View tabLayout = rootView.findViewById(R.id.tab_layout);

        mLoadingFooter = new LoadingFooter(getActivity());// 加载更多的view
        mScrollLoader = new ListViewQuickReturnScrollLoader(QuickReturnViewType.FOOTER, null, 0, bottomBarLayout, bottomHeight, this, mLoadingFooter);

        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(mScrollLoader);

//		mAdapter.setCommentViewOnClickListener(commentViewListener);
        mAdapter.setWriteCommenntViewOnClickListener(writeCommentListener);

        initCommentEditLayout(inflater);
        initButtomPanelLayout(rootView);
        initRightPanelLayout(inflater);
        initCourseCompetencePanelLayout(inflater);

        PlayerController.getInstance().addPlayerViewToWindows(player_container);
        PlayerController.getInstance().setCustomPanelContainer(courseCompetencePanelLayout);
        PlayerController.getInstance().setRightPanelLayout(rightPanelLayout);
        PlayerController.getInstance().setOnCompletionListener(mCompletionListener);
        PlayerController.getInstance().setScreenAndControllerHide(new IMediaPlayerController.screenAndControllerHide() {
            @Override
            public void hide() {
                if (playListView != null) {
                    playListView.setVisibility(View.GONE);
                }
            }
        });

        mAdapter.setmVideoItemClickListener(videoItemClickListener);

        onLoadDetailData();

        return rootView;
    }

    /**
     * 底部登录与权限控制面板
     */
    private void initButtomPanelLayout(View view) {
        bottomcompetencePanel = (RelativeLayout) view.findViewById(R.id.competence_bottom_container);
        bottomCompetenceContent = (TextView) view.findViewById(R.id.bottom_competence_content);
        bottomCoursePrice = (TextView) view.findViewById(R.id.price);
        bottomCourseOldPrice = (TextView) view.findViewById(R.id.oldprice);
        bottomCourseBuyButton = (TextView) view.findViewById(R.id.course_buy_button);
        bottomCompetenceButton = (TextView) view.findViewById(R.id.bottom_competence_button);
        tryCourseContent = (TextView) view.findViewById(R.id.try_course_content);
    }

    /**
     * 自定义右侧控制面板
     */
    private void initRightPanelLayout(LayoutInflater inflater) {
        rightPanelLayout = inflater.inflate(R.layout.youmi_player_right_panel_layout, null);
        ImageView playerEditButton = (ImageView) rightPanelLayout.findViewById(R.id.player_edit_button);
        ImageView playerSelectCourseButton = (ImageView) rightPanelLayout.findViewById(R.id.player_select_course_button);
        playList = new ArrayList<>();
        playListAdapter = new PlayListAdapter();
        playListView = (ListView) rightPanelLayout.findViewById(R.id.play_list_view);
        playListView.setOnItemClickListener(playListItemClickListener);
        playerEditButton.setOnClickListener(playerEditClickListener);
        playerSelectCourseButton.setOnClickListener(playerSelectCourseClickListener);
        playListView.setAdapter(playListAdapter);
        if (YoumiRoomUserManager.getInstance().isLogin()) {
            playerEditButton.setEnabled(true);
            playerEditButton.setBackgroundResource(R.drawable.ic_video_note_hilight);
            playerSelectCourseButton.setEnabled(true);
            playerSelectCourseButton.setBackgroundResource(R.drawable.ic_video_course_hilight);
        } else {
            playerEditButton.setEnabled(false);
            playerEditButton.setBackgroundResource(R.drawable.ic_video_note_disabled);
            playerSelectCourseButton.setEnabled(true);
            playerSelectCourseButton.setBackgroundResource(R.drawable.ic_video_course_disabled);
        }
    }

    /**
     * 自定义权限控制面板
     */
    private void initCourseCompetencePanelLayout(LayoutInflater inflater) {
        courseCompetencePanelLayout = inflater.inflate(R.layout.youmi_player_course_competence_panel_layout, null);

        competenceMiddleContainer = (LinearLayout) courseCompetencePanelLayout.findViewById(R.id.competence_middle_container);
        middleCompetenceButton = (TextView) courseCompetencePanelLayout.findViewById(R.id.middle_competence_button);
        middleCompetenceContent = (TextView) courseCompetencePanelLayout.findViewById(R.id.middle_competence_content);

        middlePlayButton = (ImageView) courseCompetencePanelLayout.findViewById(R.id.middle_play_button);
        competenceReturnButton = (ImageView) courseCompetencePanelLayout.findViewById(R.id.competence_return_button);
        competenceReturnButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        courseCompetencePanelLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;//屏蔽播放器下所以的事件处理
            }
        });
    }

    private View.OnClickListener playerEditClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PlayerController.getInstance().pause();
            EditNoteFragment.getInstance().showDialog(getActivity(), mRequestData.getTutoruid());
        }
    };
    private View.OnClickListener playerSelectCourseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (playListView.isShown()) {
                playListView.setVisibility(View.GONE);
            } else {
                playListView.setVisibility(View.VISIBLE);
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        YoumiRoomUserManager.getInstance().registerListener(userListener);
        if (isRefresh) {
            onDetailDataUpdate();
            this.isRefresh = false;
        }
        PlayerController.getInstance().registerAllListener();
        PlayerController.getInstance().initConfigrationChange(null);
//        PlayerController.getInstance().resume();
        MobclickAgent.onPageStart(fragmentName);
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(fragmentName);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!isRefresh) {
            YoumiRoomUserManager.getInstance().unregisterListener(userListener);
        }
        PlayerController.getInstance().unregisterAllListener();
        PlayerController.getInstance().cancelOkHttpClien();
        PlayerController.getInstance().stop();
        PlayerController.getInstance().releaseAndStop();
        PlayerController.getInstance().releaseNativeMediaPlayer();
        NetworkManager.getInstance().removeListener(networkChangeListener);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        PlayerController.getInstance().initConfigrationChange(newConfig);
    }


    /***
     * 加载专辑详情
     *
     * @author tangxiyong
     * @version 2015-4-7 上午11:29:06
     */
    public void onLoadDetailData() {
        if (TextUtils.isEmpty(detaiURL)) {
            return;
        }
        showLoading();
        GetRequest<ListDetailRequestData> req = new GetRequest<ListDetailRequestData>(
                detaiURL, CourseDetailParser.class, detailListener);
        HttpDispatcher.getInstance().go(req);

    }

    @Override
    public void onLoadData() {

    }

    @Override
    public void onLoadData(int page) {
        GetRequest<CommentListBeans.CommentListRequestData> request = new GetRequest<CommentListBeans.CommentListRequestData>(
                String.format(UmiwiAPI.COMMENT_REPLY_LIST, albumID, page), GsonParser.class,
                CommentListBeans.CommentListRequestData.class, commentListener);
        request.go();
    }

    @Override
    public void customScrollInChange() {

    }


    @Override
    public void customScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {//0,3,4,10
            if (mListView.getFirstVisiblePosition() == 0) {
                scrollDetail.setChecked(true);
            } else if (mListView.getFirstVisiblePosition() >= 1 && mListView.getFirstVisiblePosition() < 3) {
                scrollTeacher.setChecked(true);
            } else if (mListView.getFirstVisiblePosition() >= 3 && mListView.getFirstVisiblePosition() < (3 + mAdapter.mRelatedList.size())) {
                scrollRelate.setChecked(true);
            } else if (mListView.getFirstVisiblePosition() >= (3 + mAdapter.mRelatedList.size())) {
                scrollComment.setChecked(true);
            }

        }

    }

    protected void loadRelatedVideos(String albumId) {
        if (TextUtils.isEmpty(albumId)) {
            return;
        }
        GetRequest<ArrayList<UmiwiListBeans>> request = new GetRequest<ArrayList<UmiwiListBeans>>(
                String.format(UmiwiAPI.VIDEO_VIP_RELATE, albumId), RelatedVideosParser.class, relatedVideosListener);
        request.setTag(albumId);
        HttpDispatcher.getInstance().go(request);
    }


    /**
     * 刷新页面
     */
    private void onDetailDataUpdate() {
        setPlayList();

        if (null != mRequestData && !mRequestData.isIsbuy()) {// new price
            bottomcompetencePanel.setVisibility(View.VISIBLE);
            bottomCoursePrice.setVisibility(View.GONE);
            bottomCourseOldPrice.setVisibility(View.GONE);
            bottomCourseBuyButton.setVisibility(View.GONE);
            bottomCompetenceButton.setVisibility(View.GONE);
            bottomCompetenceContent.setVisibility(View.GONE);

            if (mRequestData.isCanbuy()) {
                bottomCoursePrice.setText("￥" + mRequestData.getPrice());
                bottomCompetenceButton.setText(getActivity().getString(R.string.competence_vip_button));
                bottomCoursePrice.setVisibility(View.VISIBLE);
                //原价
                if (!mRequestData.getOldprice().equals(mRequestData.getPrice())) {
                    bottomCourseOldPrice.setVisibility(View.VISIBLE);
                } else {
                    bottomCourseOldPrice.setVisibility(View.GONE);
                }
                bottomCourseOldPrice.setText("￥"+ mRequestData.getOldprice());
                bottomCourseOldPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
                bottomCourseBuyButton.setVisibility(View.VISIBLE);
                bottomCompetenceButton.setVisibility(View.VISIBLE);
                bottomCourseBuyButton.setOnClickListener(courseBuyButtonListener);
                bottomCompetenceButton.setOnClickListener(vipButtonListener);

                middleCompetenceContent.setText(getActivity().getString(R.string.competence_course_subtitle));
                middleCompetenceButton.setText(getActivity().getString(R.string.competence_vip_button));
                middleCompetenceButton.setOnClickListener(vipButtonListener);
                middleCompetenceButton.setVisibility(View.VISIBLE);
                middleCompetenceContent.setVisibility(View.VISIBLE);
                middlePlayButton.setVisibility(View.GONE);
            } else {
                UserClass userClass = UserClass.fromString(mRequestData.getClasses());
                switch (userClass) {
                    case DIAMOND:
                    case SILVER:
                    case GOLDEN:
                        bottomCompetenceButton.setText(getActivity().getString(R.string.competence_vip_button));
                        bottomCompetenceContent.setText(getActivity().getString(R.string.competence_vip_show_content));
                        bottomCompetenceButton.setVisibility(View.VISIBLE);
                        bottomCompetenceContent.setVisibility(View.VISIBLE);
                        bottomCompetenceButton.setOnClickListener(vipButtonListener);

                        middleCompetenceContent.setText(getActivity().getString(R.string.competence_vip_subtitle));
                        middleCompetenceButton.setText(getActivity().getString(R.string.competence_vip_button));
                        middleCompetenceButton.setOnClickListener(vipButtonListener);
                        middleCompetenceButton.setVisibility(View.VISIBLE);
                        middleCompetenceContent.setVisibility(View.VISIBLE);
                        middlePlayButton.setVisibility(View.GONE);
                        break;
                    case REGISTERED:
                    default:
                        bottomCompetenceButton.setText(getActivity().getString(R.string.competence_login_button));
                        bottomCompetenceContent.setText(getActivity().getString(R.string.competence_login_show_content));
                        bottomCompetenceButton.setVisibility(View.VISIBLE);
                        bottomCompetenceContent.setVisibility(View.VISIBLE);
                        bottomCompetenceButton.setOnClickListener(loginButtonListener);

                        middleCompetenceContent.setText(getActivity().getString(R.string.competence_login_subtitle));
                        middleCompetenceButton.setText(getActivity().getString(R.string.competence_login_button));
                        middleCompetenceButton.setOnClickListener(loginButtonListener);
                        middleCompetenceButton.setVisibility(View.VISIBLE);
                        middleCompetenceContent.setVisibility(View.VISIBLE);
                        middlePlayButton.setVisibility(View.GONE);
                        break;
                }
            }
        } else {
            bottomcompetencePanel.setVisibility(View.GONE);
            tryCourseContent.setVisibility(View.GONE);

            middleCompetenceContent.setText(getActivity().getString(R.string.competence_play_content));
            middleCompetenceButton.setVisibility(View.GONE);
            middleCompetenceContent.setVisibility(View.VISIBLE);
            middlePlayButton.setVisibility(View.VISIBLE);
            middlePlayButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        dismissLoading();
    }

    private void showLoading() {
        FrameLayout rootFrameLayout = (FrameLayout) getActivity().findViewById(android.R.id.content);
        rootFrameLayout.setBackgroundColor(UmiwiApplication.getContext().getResources().getColor(R.color.black_a8));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mProgressBar = new ProgressBar(getActivity());
        mProgressBar.setLayoutParams(layoutParams);
        mProgressBar.setVisibility(View.VISIBLE);
        rootFrameLayout.addView(mProgressBar);
    }

    private void dismissLoading() {
        mProgressBar.setVisibility(View.GONE);
        try {
            //让正在播放的音频暂停
            if (UmiwiApplication.mainActivity.service != null && UmiwiApplication.mainActivity.service.isPlaying()) {
                UmiwiApplication.mainActivity.service.pause();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void setPlayList() {

        Pair<VideoModel, Boolean> result = null;

        mAdapter.notifyDataSetChanged();
        if (mAdapter.selectCourseAt(0, false) != null) {
            result = mAdapter.selectCourseAt(0, false);
        }

        VideoModel video = null;
        if (result != null) {
            video = result.first;
        }
        if (video != null) {
            PlayerController.getInstance().setDataSource(video);
            if (video.getAlbumId() != null) {
                if (!result.second) {
                    playList.addAll(mAdapter.playList);
                    PlayerController.getInstance().setPlayList(mAdapter.playList);
                } else {
//                    playList.addAll(null);
                    PlayerController.getInstance().setPlayList(null);
                }
                playListAdapter.notifyDataSetChanged();

            }
            if (NetworkManager.getInstance().isWifi()) {
                PlayerController.getInstance().prepareAndStart();
                return;
            }

            if (NetworkManager.getInstance().isWapNetwork()) {
                if (mSpUtil.getShow3GDialog()) {
                    isFirstLoad = true;
                    showNetorkDialog();
                }
                if (mSpUtil.getPlayWith3G()) {
                    PlayerController.getInstance().prepareAndStart();
                    ToastU.showShort(getActivity(), getActivity().getApplicationContext().getString(R.string.show_3g_toast));
                    return;
                } else {
                    ToastU.showShort(getActivity(), "当前在3G网络下，请到设置里设置");
                }
            } else {
                ToastU.showShort(getActivity(), "当前网络不可用");
            }
        }

    }

    VideoClickListener videoItemClickListener = new VideoClickListener() {
        @Override
        public void setVideoClickListener(Pair<VideoModel, Boolean> result) {
            VideoModel video = result.first;
            PlayerController.getInstance().setDataSource(video);
            playListAdapter.notifyDataSetChanged();

            PlayerController.getInstance().pause();
//            PlayerController.getInstance().stop();
            PlayerController.getInstance().onComplete();
            PlayerController.getInstance().showPrepareLoading();
            PlayerController.getInstance().prepareAndStart();
            mAdapter.notifyDataSetChanged();
        }
    };

    private AdapterView.OnItemClickListener playListItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //TODO 控制面板隐藏
            PlayListAdapter adapter = (PlayListAdapter) parent.getAdapter();
            VideoModel video = playListAdapter.getItem(position);
            if (video.equals(PlayerController.getInstance().getCurrentPlayerItem().getDataSource())) {
                return;
            }
            PlayerController.getInstance().setDataSource(video);
            playListAdapter.notifyDataSetChanged();

            PlayerController.getInstance().pause();
//            PlayerController.getInstance().stop();
            PlayerController.getInstance().onComplete();
            PlayerController.getInstance().showPrepareLoading();
            PlayerController.getInstance().prepareAndStart();
            mAdapter.notifyDataSetChanged();
        }
    };


    IMediaPlayer.OnCompletionListener mCompletionListener = new IMediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(IMediaPlayer mp) {
            if (null != mRequestData && !mRequestData.isIsbuy()) {
                PlayerController.getInstance().goSmallScreen();
                PlayerController.getInstance().showCustomPanel();
            } else {
                if (PlayerController.getInstance().isPlayNext()) {
                    PlayerController.getInstance().stop();
                    PlayerController.getInstance().onComplete();
                    PlayerController.getInstance().showPrepareLoading();
                    PlayerController.getInstance().prepareAndStart();
                    mAdapter.notifyDataSetChanged();
                } else {
                    PlayerController.getInstance().goSmallScreen();
                }
            }
        }
    };

    IMediaPlayer.OnErrorListener mErrorListener = new IMediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(IMediaPlayer mp, int what, int extra) {
            return false;
        }
    };

    private PlayListAdapter playListAdapter = null;
    private ArrayList<VideoModel> playList = null;

    private class PlayListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return playList == null ? 0 : playList.size();
        }

        @Override
        public VideoModel getItem(int position) {
            return playList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_play_list, null);
            }
            VideoModel video = getItem(position);
            TextView titleTextView = (TextView) convertView.findViewById(R.id.title_text_view);
            titleTextView.setText(video.getTitle());
            ImageView videoCamerImageView = (ImageView) convertView.findViewById(R.id.video_camera_image_view);

            if (video.equals(PlayerController.getInstance().getCurrentPlayerItem().getDataSource())) {
                titleTextView.setTextColor(Color.rgb(82, 180, 59));// TODO 正在播放 条目 处理
                videoCamerImageView.setImageResource(R.drawable.ic_video_camera_hilight);
            } else {
                titleTextView.setTextColor(Color.rgb(187, 187, 187));
                videoCamerImageView.setImageResource(R.drawable.ic_video_camera);
            }
            return convertView;
        }
    }

    private Listener<ListDetailRequestData> detailListener = new Listener<ListDetailRequestData>() {

        @Override
        public void onResult(AbstractRequest<ListDetailRequestData> request,
                             ListDetailRequestData t) {
            if (t != null) {
                mRequestData = t;
                mAdapter.setDetail(mRequestData);
                mListView.smoothScrollToPosition(0);
                loadingVideo = false;

                albumID = String.valueOf(t.getId());
                Log.e("TAG", "t.getId()=" + t.getId());
                Log.e("TAG", "albumID=" + albumID);
                mList.clear();
                mScrollLoader.onLoadFirstPage();
                loadRelatedVideos(t.getId() + "");

                if (UserManager.getInstance().isLogin() && collectionDao.isSaved(albumID)) {
                    saveButton.setChecked(true);
                } else {
                    saveButton.setChecked(false);
                }

                if (!isRefresh) {
                    onDetailDataUpdate();
                }
            }
        }

        @Override
        public void onError(AbstractRequest<ListDetailRequestData> requet,
                            int statusCode, String body) {
            ToastU.showShort(getActivity(), "当前网络不可用");
        }

    };

    private Listener<CommentListBeans.CommentListRequestData> commentListener = new Listener<CommentListBeans.CommentListRequestData>() {

        @Override
        public void onResult(
                AbstractRequest<CommentListBeans.CommentListRequestData> request,
                CommentListBeans.CommentListRequestData t) {

            if (t != null) {

                if (t.getCurr_page() == t.getPages()) {
                    mScrollLoader.setEnd(true);
                }

                mScrollLoader.setPage(t.getCurr_page());// 用于分页请求
                mScrollLoader.setloading(false);

                // 数据加载
                ArrayList<CommentListBeans> charts = t.getRecord();
                mList.addAll(charts);
                mAdapter.setCommentNum(t.getTotals() + "");
                if (mAdapter == null) {
                    mAdapter = new CourseDetailsAdapter(getActivity(), mList, CourseDetailPlayFragment.this);
                    mListView.setAdapter(mAdapter);// 解析成功 播放列表
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onError(
                AbstractRequest<CommentListBeans.CommentListRequestData> requet,
                int statusCode, String body) {
            mLoadingFooter.setState(LoadingFooter.State.Error);
            mLoadingFooter.getView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mScrollLoader.onLoadErrorPage();
                }
            });
        }
    };


    private Listener<ArrayList<UmiwiListBeans>> relatedVideosListener = new Listener<ArrayList<UmiwiListBeans>>() {
        @Override
        public void onResult(
                AbstractRequest<ArrayList<UmiwiListBeans>> request,
                ArrayList<UmiwiListBeans> t) {
            if (t != null) {
                mAdapter.setRelatedVideos(t);
            }
        }

        @Override
        public void onError(AbstractRequest<ArrayList<UmiwiListBeans>> requet,
                            int statusCode, String body) {
        }
    };


    OnClickListener scrollListener = new OnClickListener() {
        //0,3,4,10

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.scroll_detail:
                    //mListView.smoothScrollToPositionFromTop(0, 0);
                    mListView.setSelection(0);
                    //mListView.scrollBy(0, 0);

                    break;
                case R.id.scroll_teacher:
//                    if (mRequestData != null && mRequestData.getCourse() != null && mRequestData.getCourse().size() > 1) {
//                        mListView.setSelection(3);
//                    } else {
//                        mListView.setSelection(2);
//                    }
                    //mListView.scrollBy(0, DimensionUtil.dip2px(-20));

                    break;
                case R.id.scroll_relate:
                    mListView.smoothScrollToPositionFromTop(3, DimensionUtil.dip2px(-90));
                    break;
                case R.id.scroll_comment:
                    if (mAdapter.isEmpty()) {
                        return;
                    }
                    mListView.smoothScrollToPositionFromTop(3 + mAdapter.mRelatedList.size(), DimensionUtil.dip2px(-70));
                    break;
                default:
                    break;
            }
            mListView.invalidate();
        }
    };


    private OnClickListener mEditButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            EditNoteFragment.getInstance().showDialog(getActivity(), mRequestData.getTutoruid());
        }
    };

    private OnClickListener loginButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            PlayerController.getInstance().pause();
            showLogin();
        }
    };

    private OnClickListener vipButtonListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            PlayerController.getInstance().pause();
            if (YoumiRoomUserManager.getInstance().isLogin()) {
                showJoinVipDialog();
                MobclickAgent.onEvent(getActivity(), "详情购买V", "钻石购买");
            } else {
                showLogin();
            }
        }

    };

    private OnClickListener courseBuyButtonListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            PlayerController.getInstance().pause();
            if (!YoumiRoomUserManager.getInstance().isLogin()) {
                showLogin();
                return;
            }
            showCourseBuyDialog();
            MobclickAgent.onEvent(getActivity(), "详情购买V", "单课购买");
        }

    };

    //分享
    private OnClickListener shareCourseClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mRequestData == null) {
                return;
            }

            PlayerController.getInstance().pause();
            ShareDialog.getInstance().showDialog(getActivity(),
                    mRequestData.getTitle(), mRequestData.getSharecontent(),
                    mRequestData.getShareurl(), mRequestData.getImage());
        }
    };

    private OnClickListener writeCommentListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            if (!YoumiRoomUserManager.getInstance().isLogin()) {
                PlayerController.getInstance().pause();
                showLogin();
                return;
            }

            if (null == mRequestData) {
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

    private String[] REQUEST_STORAGE_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_PERMISSION_CODE_TAKE_STORAGE = 7;

    private OnClickListener downloadListClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            if (!YoumiRoomUserManager.getInstance().isLogin()) {
                PlayerController.getInstance().pause();
                showLogin();
                return;
            }
            if (null == mRequestData) {
                return;
            }

            if (!mRequestData.isIsbuy()) {
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

            DownloadListDialog.getInstance().showDialog(getActivity(), getVideos());
            MobclickAgent.onEvent(getActivity(), "详情页面V", "下载");
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE_TAKE_STORAGE:
                if (PermissionUtil.verifyPermissions(grantResults)) {
                    DownloadListDialog.getInstance().showDialog(getActivity(), getVideos());
                    MobclickAgent.onEvent(getActivity(), "详情页面V", "下载");
                } else {
                    ToastU.showShort(getActivity(), "无法读取存储卡，请先授权");
                }
                break;
        }
    }

    private OnClickListener favClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            if (TextUtils.isEmpty(albumID)) {
                return;
            }

            if (!UserManager.getInstance().isLogin()) {
                saveButton.setChecked(false);
                PlayerController.getInstance().pause();
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


    public ArrayList<VideoModel> getVideos() {
        if (mRequestData == null) {
            return null;
        }
        ArrayList<VideoModel> videos = new ArrayList<VideoModel>(mRequestData
                .getCourse().size());

        for (int i = 0; i < mRequestData.getCourse().size(); i++) {
            UmiwiListDetailBeans c = mRequestData.getCourse().get(i);
            VideoModel video = VideoManager.getInstance().getVideoById(
                    c.getVid() + "");
            videos.add(video);
//            Log.e("TAG", "video =" + video.getFileName());
//            Log.e("TAG", "video =" + video.getTitle());
//            Log.e("TAG", "video =" + video.getVideoUrl());
//            Log.e("TAG", "video =" + video.getVideoId());
//            Log.e("TAG", "video =" + video.getAlbumTitle());
        }
        return videos;
    }


    /**
     * 单课购买
     *
     * @author tangxiyong
     * @version 2015-4-9 下午5:25:58
     */
    public void showCourseBuyDialog() {
        Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayOrderDetailFragment.class);
        intent.putExtra(PayOrderDetailFragment.KEY_ORDER_ID, mRequestData.getId() + "");
        intent.putExtra(PayOrderDetailFragment.KEY_ORDER_TYPE, PayTypeEvent.ALBUM);
        intent.putExtra(PayOrderDetailFragment.KEY_SPM, String.format(StatisticsUrl.ORDER_COURSE_DETAIL, "7", albumID, mRequestData.getPrice()));
        startActivity(intent);
    }

    /**
     * 钻石购买
     *
     * @author tangxiyong
     * @version 2015-4-9 下午5:26:16
     */
    public void showJoinVipDialog() {
        Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayOrderDetailFragment.class);
        intent.putExtra(PayOrderDetailFragment.KEY_ORDER_ID, "23");
        intent.putExtra(PayOrderDetailFragment.KEY_ORDER_TYPE, PayTypeEvent.VIP);
        intent.putExtra(PayOrderDetailFragment.KEY_SPM, String.format(StatisticsUrl.ORDER_VIP_DETAIL, "7", albumID));
        startActivity(intent);
    }

    private void showLogin() {
        LoginUtil.getInstance().showLoginView(getActivity());
    }


    // 收藏
    private void addFav() {
        albumID = String.valueOf(mRequestData.getId());

        if (TextUtils.isEmpty(albumID)) {
            return;
        }

        collectionDao.saveCollection(albumID);

        String favStr = String.format(UmiwiAPI.UMIWI_FAV_ADD_VIDEO_ALBUMID, albumID);
        Log.e("TAG", "favStr=" + favStr);
        GetRequest<AddFavBeans.AddFavBeansRequestData> req = new GetRequest<AddFavBeans.AddFavBeansRequestData>(
                favStr, GsonParser.class,
                AddFavBeans.AddFavBeansRequestData.class, favListener);
        HttpDispatcher.getInstance().go(req);

    }

    private void changeSaveButton() {
        if (collectionDao.isSaved(albumID)) {
            saveButton.setChecked(true);
        } else {
            saveButton.setChecked(false);
        }
    }

    private Listener<AddFavBeans.AddFavBeansRequestData> favListener = new Listener<AddFavBeans.AddFavBeansRequestData>() {

        @Override
        public void onResult(AbstractRequest<AddFavBeansRequestData> request,
                             AddFavBeansRequestData t) {
            collectionDao.updateCollection(albumID);
            changeSaveButton();
            ToastU.showShort(getActivity(), "收藏成功");
        }

        @Override
        public void onError(AbstractRequest<AddFavBeansRequestData> requet,
                            int statusCode, String body) {
            ToastU.showShort(getActivity(), body);
            changeSaveButton();
        }
    };

    private void removeFav() {
        albumID = String.valueOf(mRequestData.getId());

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

    private Listener<AddFavBeans.AddFavBeansRequestData> removeListener = new Listener<AddFavBeans.AddFavBeansRequestData>() {

        @Override
        public void onResult(AbstractRequest<AddFavBeansRequestData> request,
                             AddFavBeansRequestData t) {
            collectionDao.updateCollection(albumID);
            changeSaveButton();
            ToastU.showShort(getActivity(), "取消收藏");
        }

        @Override
        public void onError(AbstractRequest<AddFavBeansRequestData> requet,
                            int statusCode, String body) {
            ToastU.showShort(getActivity(), body);
            changeSaveButton();
        }
    };


    private ModelStatusListener<UserEvent, UserModel> userListener = new ModelStatusListener<UserEvent, UserModel>() {

        @Override
        public void onModelGet(UserEvent key, UserModel models) {
        }

        @Override
        public void onModelUpdate(UserEvent key, UserModel model) {
            switch (key) {
                case HOME_LOGIN:
                    isRefresh = true;
                    PlayerController.getInstance().pause();
                    PlayerController.getInstance().onComplete();
                    PlayerController.getInstance().hideCustomPanel();
                    PlayerController.getInstance().hideEndPanel();
                    PlayerController.getInstance().showPrepareLoading();
                    onLoadDetailData();
                    LogUtils.e("detaillayout", "===login");
                    break;
                case PAY_SUCC:
                    isRefresh = true;
                    PlayerController.getInstance().pause();
                    PlayerController.getInstance().onComplete();
                    PlayerController.getInstance().hideCustomPanel();
                    PlayerController.getInstance().hideEndPanel();
                    PlayerController.getInstance().showPrepareLoading();
                    onLoadDetailData();
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onModelsGet(UserEvent key, List<UserModel> models) {
        }
    };


    /**
     * 评论控制面板
     */
    private void initCommentEditLayout(LayoutInflater inflater) {

        // 编辑窗口
        View menuLayout = inflater.inflate(R.layout.dailog_comment_write_layout, null);

        mEditMenuWindow = new PopupWindow(menuLayout,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
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
        btn_send.setOnClickListener(new OnClickListener() {

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
            mEditMenuWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
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

    // 点击底部发送按钮，关闭编辑窗口
    private void closeButtomSend() {
        mEditMenuWindow.dismiss();
    }

    // 编辑窗口关闭时，隐藏输入法软键盘
    @Override
    public void onDismiss() {
        // 如果软键盘打开,隐藏输入法软键盘
        if (mIsKeyboardOpened) {
            hideKeyBoard();
        }
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    private PopupWindow mEditMenuWindow;
    private EditText mEt_menu;
    private boolean mIsKeyboardOpened;// 软键盘是否显示
    private int mMenuOpenedHeight;// 编辑菜单打开时的高度

    private View rootView;

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

    private void showComment() {
        PostRequest<CommentResultBean> request = new PostRequest<CommentResultBean>(
                String.format(UmiwiAPI.COMMENT_SEND + StatisticsUrl.DETAIL_COMMENT_WRITE_BUTTON, albumID),
                GsonParser.class, CommentResultBean.class, writeListener);
        request.addParam("albumid", albumID);
        request.addParam("question", mEt_menu.getText().toString().trim());
        request.go();
        closeButtomSend();

    }

    private Listener<CommentResultBean> writeListener = new Listener<CommentResultBean>() {

        @Override
        public void onResult(AbstractRequest<CommentResultBean> request, CommentResultBean t) {

            if (t.isSucc()) {
                mEt_menu.setText("");
                if (mList != null) {
                    mList.add(0, t.getR());
                    mAdapter.notifyDataSetChanged();
                }
                Toast.makeText(UmiwiApplication.getApplication(), "评论成功！", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(AbstractRequest<CommentResultBean> requet,
                            int statusCode, String body) {
            Toast.makeText(UmiwiApplication.getApplication(), "评论失败，请试重...", Toast.LENGTH_SHORT).show();
        }
    };

    private SharePreferenceUtil mSpUtil;

    private boolean isFirstLoad;

    private OnNetworkChangeListener networkChangeListener = new OnNetworkChangeListener() {
        @Override
        public void onNetworkChange() {
            if (!NetworkManager.getInstance().isWifi()) {
                PlayerController.getInstance().pause();
                if (mSpUtil.getPlayWith3G()) {
                    ToastU.showShort(getActivity(), getActivity().getApplicationContext().getString(R.string.show_3g_toast));
                } else {
                    if (mSpUtil.getShow3GDialog()) {
                        ToastU.showShort(getActivity(), "当前网络不可用");
                        return;
                    } else {
                        showNetorkDialog();
                        return;
                    }
                }
            }
        }
    };

    private void showNetorkDialog() {
        ViewHolder holder = new ViewHolder(R.layout.dialog_non_wifi_network);
        final DialogPlus dialogPlus = new DialogPlus.Builder(getActivity())
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(Gravity.CENTER)
                .create();
        dialogPlus.show();

        View view = dialogPlus.getHolderView();
        view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSpUtil.setPalyWith3G(false);
                mSpUtil.setShow3GDialog(true);
                dialogPlus.dismiss();
                getActivity().finish();
            }
        });
        view.findViewById(R.id.go_on_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSpUtil.setPalyWith3G(true);
                if (isFirstLoad) {
                    onLoadDetailData();
                    isFirstLoad = false;
                } else {
                    PlayerController.getInstance().resume();
                }
                dialogPlus.dismiss();
            }
        });

    }

}
