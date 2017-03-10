package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnViewType;
import com.umiwi.ui.IVoiceService;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.AudioResourceBean;
import com.umiwi.ui.beans.updatebeans.ExperDetailsVoiceBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.util.JsonUtil;
import com.umiwi.ui.util.Utils;
import com.umiwi.video.services.VoiceService;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.ListViewQuickReturnScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

/**
 * Created by Administrator on 2017/3/7.
 */

public class VoiceDetailsFragment extends BaseConstantFragment implements View.OnClickListener, ListViewQuickReturnScrollLoader.QuickReturnOnScrollLoader {

    public static final String KEY_DETAILURL = "key.detaiurl";
    /**
     * 音频进度的更新
     */
    private static final int PROGRESS = 1;
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
    private String CurrentvoiceId;
    //    private Handler handler = new Handler();
//    public Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            seekbar.setProgress(mBinder.getCurrentDuration());
//            changeTimes.setText(DateUtils.formatmmss(mBinder.getCurrentDuration()));
//
//            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                    if (b) {
//                        mBinder.getMediaplayer().seekTo(seekBar.getProgress());
//                        changeTimes.setText(DateUtils.formatmmss(seekBar.getProgress()));
//
//                    }
//                }
//
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//
//                }
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//
//                }
//            });
//            handler.postDelayed(runnable, (int) ((float) mBinder.getCurrentDuration() / (float) mBinder.getDuration() * 100));
//        }
//    };
    private String source;
    private String voices;
    //播放音频的链接地址
    private String url;

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
                    try {
                        int currentPosition = service.getCurrentPosition();
                        changeTimes.setText(utils.stringForTime(currentPosition));
                        totalTime.setText(utils.stringForTime(service.getDuration()));
//                        Log.e("TAG", "service.getDuration()=" + service.getDuration());
//                        Log.e("TAG", "service.getCurrentPosition()=" + service.getCurrentPosition());
                        seekbar.setProgress(currentPosition);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    removeMessages(PROGRESS);
                    try {
                        sendEmptyMessageDelayed(PROGRESS, (long) ((int) (float) service.getCurrentPosition() / (float) service.getDuration() * 100));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }
    };
    private List<ExperDetailsVoiceBean.AudiofileBean> audiofile;
    private ExperDetailsVoiceBean experDetailsVoiceBean;
    private ExperDetailsVoiceBean.AudiofileBean audiofileBean;
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

    //    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "onCreate()");
        herfurl = getActivity().getIntent().getStringExtra(KEY_DETAILURL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_voice_details_layout, null);
        ButterKnife.inject(this, rootView);
        utils = new Utils();
        imageLoader = new ImageLoader(getActivity());
        //获取的链接
        herfurl = getActivity().getIntent().getStringExtra(KEY_DETAILURL);

        Log.e("TAG", "herfurl=" + herfurl);
//        seekbar = (SeekBar) view.findViewById(seekbar);

        //得到音频播放地址
        if (!TextUtils.isEmpty(herfurl)) {
            getInfos(herfurl);
        }
        initListener();
        //注册广播
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(VoiceService.OPEN_COMPLETION);
        getActivity().registerReceiver(myReceiver, filter);

        Log.e("TAG", "onCreateView()");
        initViews();
        return rootView;
    }

    //初始化播放器底下视图
    private void initViews() {
        mListView = (ListView) rootView.findViewById(R.id.listView);
        mListView.setSelector(R.color.transparent);
        View mPlaceHolderView = getActivity().getLayoutInflater().inflate(
                R.layout.view_header_placeholder, mListView, false);
        mListView.addHeaderView(mPlaceHolderView);

        mLoadingFooter = new LoadingFooter(getActivity());// 加载更多的view
        int bottomHeight = getResources().getDimensionPixelSize(R.dimen.bottom_bar_height);
        mScrollLoader = new ListViewQuickReturnScrollLoader(QuickReturnViewType.FOOTER, null, 0, bottomBarLayout, bottomHeight,this, mLoadingFooter);


        tab_layout = (LinearLayout) rootView.findViewById(R.id.tab_layout);
        bottomBarLayout = (LinearLayout) rootView.findViewById(R.id.bottom_bar_container);

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

        commentButton = (RadioButton) rootView.findViewById(R.id.comment_button);
        commentButton.setOnClickListener(writeCommentListener);

        saveButton = (RadioButton) rootView.findViewById(R.id.fav_button);
        saveButton.setOnClickListener(favClickListener);

        rootView.findViewById(R.id.share_radiobutton).setOnClickListener(shareCourseClickListener);

    }
    //点击标题
    private View.OnClickListener scrollListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.scroll_detail :

                    break;
                case R.id.scroll_teacher :

                    break;
                case R.id.scroll_relate :

                    break;
                case R.id.scroll_comment :

                    break;
            }
        }
    };
    //收藏
    private View.OnClickListener favClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    //评论
    private View.OnClickListener writeCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    //下载
    private View.OnClickListener downloadListClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    //分享
    private View.OnClickListener shareCourseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    private ImageLoader imageLoader;

    private void getInfos(String herfurl) {
        OkHttpUtils.get().url(herfurl).build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {
                Log.e("data", "音频url获取失败");

            }

            @Override
            public void onSucess(String data) {
                Log.e("TAG", "音频url获取成功=" + data);
                if (!TextUtils.isEmpty(data)) {
                    experDetailsVoiceBean = JsonUtil.json2Bean(data, ExperDetailsVoiceBean.class);
                    String imageurl = experDetailsVoiceBean.getImage();
                    Log.e("TAG", "imageurl=" + imageurl);

                    imageLoader.loadImage(imageurl,ivHeader,R.drawable.icon_umiwi);

                    audiofile = experDetailsVoiceBean.getAudiofile();
                    if (audiofile != null && audiofile.size() > 0) {
                        audiofileBean = audiofile.get(0);
//                        source = audiofileBean.getSource();
                        url = audiofileBean.getSource();
                        Log.e("TAG", "url =" + url);
//                        if (url!=null){
//                            UmiwiApplication.getInstance().getBinder().searchInfo(source);
//                        }
                        if (url != null) {
                            getData(url);
                        }
                    }

                }
            }
        });
    }

    private void getData(String url) {
        OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {

            }

            @Override
            public void onSucess(String data) {
                AudioResourceBean audioResourceBean = JsonUtil.json2Bean(data, AudioResourceBean.class);
                source = audioResourceBean.getSource();
                if (source != null) {
                    bindVoiceSerive();
                }
            }
        });
    }

    private void initListener() {
        startPlayer.setOnClickListener(this);
        lastVoice.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        nextVoice.setOnClickListener(this);
        seekbar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());


    }
    //Listview滑动监听
    @Override
    public void customScrollStateChanged(AbsListView view, int scrollState) {

    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                seekbar.setMax(service.getDuration());
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
                    service.seekTo(progress);
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

    /**
     * 绑定服务
     */
    private void bindVoiceSerive() {

        Intent intent = new Intent(getActivity(), VoiceService.class);
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
                    service = IVoiceService.Stub.asInterface(iBinder);

                    if (service != null) {
                        try {
                            service.openAudio(source);
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
        getActivity().bindService(intent, conn, getActivity().BIND_AUTO_CREATE);
//        getActivity().startService(intent);
//        intent.putExtra("url", url);

    }

    private ServiceConnection conn = null;


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
                    if (service.isPlaying()) {
                        //暂停
                        service.pause();
                        startPlayer.setBackgroundResource(R.drawable.pause_player);
                    } else {
                        service.play();
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
        Log.e("TAG", "onDestory()");
        handler.removeCallbacksAndMessages(null);

//        if (conn != null) {
//            getActivity().unbindService(conn);
//            conn = null;
//        }

        super.onDestroy();
    }


}
