package com.umiwi.ui.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.media.record.AudioRecorder;
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback;
import com.netease.nimlib.sdk.media.record.RecordType;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.ChatRoomDetailsBean;
import com.umiwi.ui.beans.NIMAccountBean;
import com.umiwi.ui.beans.updatebeans.AudioLiveStopBean;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.fragment.audiolive.AudioLiveDetailsFragment;
import com.umiwi.ui.fragment.audiolive.LiveDetailsFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.Container;
import com.umiwi.ui.managers.ModuleProxy;
import com.umiwi.ui.managers.MsgListManager;
import com.umiwi.ui.util.DateUtils;
import com.umiwi.ui.view.RefreshLayout;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.youmi.account.manager.UserManager;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

import static com.umiwi.ui.fragment.audiolive.AudioLiveDetailsFragment.LIVEID;
import static com.umiwi.ui.main.YoumiConfiguration.context;

/**
 * 主播聊天室界面
 */
public class AuthorChatRoomActivity extends AppCompatActivity implements ModuleProxy, View.OnClickListener {
    public static final String ROOM_ID = "roomId";
    /**
     * 相册图片回调的请求码
     */
    private static final int PICTURE_REQUEST = 123;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_status)
    TextView tvStatus;
    @InjectView(R.id.iv_more)
    ImageView ivMore;
    @InjectView(R.id.rcy_mesagelist)
    RecyclerView rcy_mesagelist;
    @InjectView(R.id.et_input)
    EditText etInput;
    @InjectView(R.id.btn_comfirm)
    Button btnComfirm;
    @InjectView(R.id.tv_record_hint_top)
    TextView tvRecordHintTop;
    @InjectView(R.id.tv_audio)
    TextView tvAudio;
    @InjectView(R.id.tv_text)
    TextView tvText;
    @InjectView(R.id.tv_picture)
    TextView tvPicture;
    @InjectView(R.id.ll_input_hint)
    LinearLayout llInputHint;
    @InjectView(R.id.rl_input_options)
    RelativeLayout rlInputOptions;
    @InjectView(R.id.tv_record_time)
    TextView tvRecordTime;
    @InjectView(R.id.btn_cancle)
    Button btnCancle;
    @InjectView(R.id.iv_record)
    ImageView ivRecord;
    @InjectView(R.id.tv_record_hint)
    TextView tvRecordHint;
    @InjectView(R.id.rl_input_audio)
    RelativeLayout rlInputAudio;
    @InjectView(R.id.rl_input_text)
    LinearLayout rlInputText;
    @InjectView(R.id.rl_input)
    RelativeLayout rlInput;
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    /**
     * 聊天室ID
     */
    private String roomId;
    /**
     * 消息列表控制器
     */
    private MsgListManager msgListManager;
    /**
     * 聊天室信息
     */
    private ChatRoomDetailsBean chatRoomDetailsBean;
    private Handler handler;
    private Long time;
    /**
     * 录音的对象
     */
    private AudioRecorder recorder;
    /**
     * 录制的音频文件
     */
    private File myAudiofile;
    private long myAudioLength;
    /**
     * 用户选择的图片路径
     */
    private String userSelectPath;

    private PopupWindow popupWindow;
    private String id;
    private boolean isAthor = true;
    /**
     * 拉取聊天记录的时间撮
     */
    private long chatRecordLastTime = 0;
    /**
     * 是否第一次获取聊天记录
     */
    private Boolean firstGetRecord = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_author_chat_room);
        ButterKnife.inject(this);
        loginNIM();
        initData();
        initMessageList();
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.main_color));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getChatRecord();
            }
        });
    }

    /**
     * 登录网易云信服务端
     */
    private void loginNIM() {
        //请求服务器接口获得网易云信通行证
        GetRequest<NIMAccountBean> request = new GetRequest<NIMAccountBean>(UmiwiAPI.NIM_ACCOUNT, GsonParser.class, NIMAccountBean.class, new AbstractRequest.Listener<NIMAccountBean>() {
            @Override
            public void onResult(AbstractRequest<NIMAccountBean> request, NIMAccountBean nimAccountBean) {
                if (nimAccountBean != null) {
                    final LoginInfo loginInfo = new LoginInfo(nimAccountBean.getR().getAccid(), nimAccountBean.getR().getToken());
                    //请求网易云信登录
                    RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
                        @Override
                        public void onSuccess(LoginInfo param) {
                            accessChatRoom(roomId);
                            registerObservers(true);
                            registerMultimediaObserver(true);
                        }

                        @Override
                        public void onFailed(int code) {
                            Toast.makeText(AuthorChatRoomActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onException(Throwable exception) {
                            Toast.makeText(AuthorChatRoomActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();
                        }
                    };
                    NIMClient.getService(AuthService.class).login(loginInfo).setCallback(callback);
                }
            }

            @Override
            public void onError(AbstractRequest<NIMAccountBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    private void initData() {
        id = getIntent().getStringExtra(LiveDetailsFragment.DETAILS_ID);
        roomId = getIntent().getStringExtra(ROOM_ID);
        GetRequest<ChatRoomDetailsBean> request = new GetRequest<>(
                UmiwiAPI.CHAT_DETAILS + id, GsonParser.class, ChatRoomDetailsBean.class, new AbstractRequest.Listener<ChatRoomDetailsBean>() {
            @Override
            public void onResult(AbstractRequest<ChatRoomDetailsBean> request, ChatRoomDetailsBean chatRoomDetails) {
                if (chatRoomDetails != null) {
                    chatRoomDetailsBean = chatRoomDetails;
                    ChatRoomDetailsBean.RBean.RecordBean record = chatRoomDetails.getR().getRecord();
                    tvTitle.setText(record.getTitle());
                    String partNum = "(" + record.getPartakenum() + "人)";
                    String status = record.getStatus();
                    switch (status) {
                        case "1":
                            tvStatus.setText("未开始" + partNum);
                            break;
                        case "2":
                            tvStatus.setText("直播中" + partNum);
                            break;
                        case "3":
                            tvStatus.setText("已结束" + partNum);
                            break;
                    }
                    getChatRecord();
                }
            }

            @Override
            public void onError(AbstractRequest<ChatRoomDetailsBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    /**
     * 获取聊天记录
     */
    private void getChatRecord() {
        if ("已结束".equals(tvStatus.getText().toString())) {
        } else {
            NIMClient.getService(ChatRoomService.class).pullMessageHistory(roomId, chatRecordLastTime, 30).setCallback(new RequestCallback<List<ChatRoomMessage>>() {
                @Override
                public void onSuccess(List<ChatRoomMessage> param) {
                    if (param != null && param.size() > 0) {
                        chatRecordLastTime = param.get(param.size() - 1).getTime();
                        for (IMMessage imMessage : param) {
                            msgListManager.addHeadMessage(imMessage);
                        }
                        refreshLayout.setRefreshing(false);
                        //第一次加载聊天记录滚到底部
                        if(firstGetRecord) {
                            msgListManager.scrollBottom();
                            firstGetRecord = false;
                        }
                    } else {
                        Toast.makeText(AuthorChatRoomActivity.this, "没用更多消息了", Toast.LENGTH_SHORT).show();
                        refreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailed(int code) {

                }

                @Override
                public void onException(Throwable exception) {

                }
            });
        }
    }

    /**
     * 初始化消息控制器
     */
    private void initMessageList() {
        Container container = new Container(this, roomId, SessionTypeEnum.ChatRoom, this);
        msgListManager = new MsgListManager(container, rcy_mesagelist);
    }

    /**
     * 进入聊天室
     *
     * @param roomId
     */
    private void accessChatRoom(String roomId) {
        EnterChatRoomData data = new EnterChatRoomData(roomId);
        NIMClient.getService(ChatRoomService.class).enterChatRoom(data);
    }

    /**
     * 注册消息接收器
     *
     * @param register
     */
    private void registerObservers(boolean register) {
        NIMClient.getService(ChatRoomServiceObserver.class).observeReceiveMessage(incomingChatRoomMsg, register);
    }

    Observer<List<ChatRoomMessage>> incomingChatRoomMsg = new Observer<List<ChatRoomMessage>>() {
        @Override
        public void onEvent(List<ChatRoomMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }
            for (ChatRoomMessage messages1 : messages) {
                msgListManager.onImcomingMessage(messages1);
            }
        }
    };
    Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage msg) {
            msgListManager.onImcomingMessage(msg);
        }
    };

    /**
     * 注册多媒体接收器
     *
     * @param register
     */
    private void registerMultimediaObserver(Boolean register) {
        // 监听消息状态变化
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(statusObserver, register);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        registerObservers(false);
        registerMultimediaObserver(false);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (msgListManager.messageListAdapter.handler != null) {
            msgListManager.messageListAdapter.handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICTURE_REQUEST & resultCode == RESULT_OK) {
            Uri uri = data.getData();
            userSelectPath = getAbsolutePath(context, uri);
            if (new File(userSelectPath).exists()) {
                sendPictureMsg();
            }
        }
    }

    /**
     * 通过相册返回的uri获取图片绝对路径
     *
     * @param context
     * @param uri
     * @return
     */
    public String getAbsolutePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @OnClick({R.id.iv_back, R.id.iv_more, R.id.btn_comfirm, R.id.tv_record_hint_top, R.id.tv_audio, R.id.tv_text, R.id.tv_picture,
            R.id.btn_cancle, R.id.iv_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_more:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    initmPopupWindow();
                }
                break;
            case R.id.btn_comfirm://发送文本消息
                String content = etInput.getText().toString().trim();
                sendTextMsg(content);
                break;
            case R.id.tv_record_hint_top://上面的录音提示
                break;
            case R.id.tv_audio://点击录音功能
                switchStatus(R.id.tv_audio);
                switchSoftInput(false);
                break;
            case R.id.tv_text://点击文本功能
                if (getResources().getString(R.string.pack_up).equals(tvText.getText().toString())) {//点击收起
                    switchStatus(R.id.tv_picture);
                    switchSoftInput(false);
                } else {
                    switchStatus(R.id.tv_text);
                }
                break;
            case R.id.tv_picture://点击图片功能
                switchStatus(R.id.tv_picture);
                switchSoftInput(false);
                //调起系统相册
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICTURE_REQUEST);
                break;
            case R.id.btn_cancle://取消录制
                new AlertDialog.Builder(this)
                        .setTitle("取消当前语音录制")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                recorder.completeRecord(true);
                                initRecordStatus();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.iv_record://开始||停止录音
                record();
                break;
        }
    }

    public void initmPopupWindow() {
        View cusView = getLayoutInflater().inflate(R.layout.popupview_item, null);
        popupWindow = new PopupWindow(cusView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAsDropDown(ivMore, -350, 50);

        //触摸事件
        cusView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                return false;
            }
        });

        TextView tv_share = (TextView) cusView.findViewById(R.id.tv_share);
        TextView tv_details = (TextView) cusView.findViewById(R.id.tv_details);
        TextView tv_stop = (TextView) cusView.findViewById(R.id.tv_stop);
        tv_share.setOnClickListener(this);
        tv_details.setOnClickListener(this);
        tv_stop.setOnClickListener(this);
    }

    /**
     * 开始||停止录音
     */
    private void record() {
        //开始录音
        if (getResources().getString(R.string.click_start_record).equals(tvRecordHint.getText().toString())) {
            startRecord();
        } else if (getResources().getString(R.string.click_stop_record).equals(tvRecordHint.getText().toString())) {//停止录音
            tvRecordHintTop.setText(R.string.send_hint);
            handler.removeCallbacksAndMessages(null);
            ivRecord.setImageResource(R.drawable.record_send);
            tvRecordHint.setText(R.string.click_send_record);
            recorder.completeRecord(false);
        } else if (getResources().getString(R.string.click_send_record).equals(tvRecordHint.getText().toString())) {//发送录音
            sendAudioMsg();
            initRecordStatus();
        }
    }

    /**
     * 初始化录音状态
     */
    private void initRecordStatus() {
        handler.removeCallbacksAndMessages(null);
        rlInputOptions.setVisibility(View.VISIBLE);
        tvRecordHintTop.setVisibility(View.GONE);
        tvRecordTime.setText("00:00");
        btnCancle.setVisibility(View.GONE);
        ivRecord.setImageResource(R.drawable.record);
        tvRecordHint.setText(R.string.click_start_record);
    }

    /**
     * 开启录音
     */
    private void startRecord() {
        // 初始化recorder
        if (recorder == null) {
            recorder = new AudioRecorder(
                    context,
                    RecordType.AAC, // 录制音频类型（aac/amr)
                    66, // 最长录音时长，到该长度后，会自动停止录音, 默认120s
                    callback // 录音过程回调
            );
        }
        // 开始录音
        if (recorder.startRecord()) {
            //显示录制状态
            rlInputOptions.setVisibility(View.GONE);
            tvRecordHintTop.setVisibility(View.VISIBLE);
            tvRecordHintTop.setText(R.string.record_60_hint);
            btnCancle.setVisibility(View.VISIBLE);
            ivRecord.setImageResource(R.drawable.record_pause);
            tvRecordHint.setText(R.string.click_stop_record);
            //录音计时
            if (handler == null) {
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        time += 1000;
                        tvRecordTime.setText(DateUtils.formatmmss(time));
                        if (time >= 60 * 1000) {
                            recorder.completeRecord(false);
                            sendAudioMsg();
                            startRecord();
                        } else {
                            handler.sendMessageDelayed(Message.obtain(), 1000);
                        }
                    }
                };
            }
            time = 0L;
            handler.removeCallbacksAndMessages(null);
            handler.sendMessageDelayed(Message.obtain(), 1000);
        } else {
            Toast.makeText(AuthorChatRoomActivity.this, "启动录音失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 是否自动到达最大录音时间
     */
    private Boolean isAutoMax;
    // 定义录音过程回调对象
    IAudioRecordCallback callback = new IAudioRecordCallback() {

        public void onRecordReady() {
            // 初始化完成回调，提供此接口用于在录音前关闭本地音视频播放（可选）
            if (UmiwiApplication.mainActivity.service != null) {
                try {
                    if (UmiwiApplication.mainActivity.service.isPlaying()) {
                        UmiwiApplication.mainActivity.service.pause();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        public void onRecordStart(File audioFile, RecordType recordType) {
            Log.e("TAG", "onRecordStart");
            // 开始录音回调
        }

        public void onRecordSuccess(File audioFile, long audioLength, RecordType recordType) {
            Log.e("TAG", "onRecordSuccess");
            // 录音结束，成功
            myAudiofile = audioFile;
            myAudioLength = audioLength;
        }

        public void onRecordFail() {
            // 录音结束，出错
        }

        public void onRecordCancel() {
            Log.e("TAG", "onRecordCancel");
            // 录音结束， 用户主动取消录音
        }

        public void onRecordReachedMaxTime(int maxTime) {
            // 到达指定的最长录音时间
            Log.e("TAG", "onRecordReachedMaxTime");
        }
    };

    /**
     * 给发送的消息添加共同的数据
     *
     * @param message
     */
    private void putCommonInfo(ChatRoomMessage message) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(MsgListManager.IS_AUTHOR, true);
        map.put(MsgListManager.HEAD_PHOTO_URL, UserManager.getInstance().getUser().getAvatar());
        map.put(MsgListManager.USER_NAME, UserManager.getInstance().getUser().getUsername());
        message.setRemoteExtension(map);
    }

    /**
     * 发送图片消息
     */
    private void sendPictureMsg() {
        // 创建图片消息
        String displayName = userSelectPath.substring(userSelectPath.lastIndexOf("/") + 1, userSelectPath.lastIndexOf("."));
        final ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomImageMessage(roomId, new File(userSelectPath), displayName);
        putCommonInfo(message);
        // 发送消息。如果需要关心发送结果，可设置回调函数。发送完成时，会收到回调。如果失败，会有具体的错误码。
        NIMClient.getService(ChatRoomService.class).sendMessage(message, true).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                msgListManager.onImcomingMessage(message);
                msgListManager.scrollBottom();
            }

            @Override
            public void onFailed(int code) {

            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }

    /**
     * 发送语音消息
     */
    private void sendAudioMsg() {
        if (myAudiofile != null) {
            // 创建音频消息
            final ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomAudioMessage(roomId, myAudiofile, myAudioLength);
            putCommonInfo(message);
            // 发送消息。如果需要关心发送结果，可设置回调函数。发送完成时，会收到回调。如果失败，会有具体的错误码。
            NIMClient.getService(ChatRoomService.class).sendMessage(message, true).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    msgListManager.onImcomingMessage(message);
                    msgListManager.scrollBottom();
                }

                @Override
                public void onFailed(int code) {

                }

                @Override
                public void onException(Throwable exception) {

                }
            });
        }
    }

    /**
     * 发送文本消息
     */
    private void sendTextMsg(String content) {
        if (content == null || content != null && content.trim().equals("")) {
            Toast.makeText(AuthorChatRoomActivity.this, "文字输入不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        // 创建文本消息
        final ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomTextMessage(roomId, content);
        putCommonInfo(message);
        NIMClient.getService(ChatRoomService.class).sendMessage(message, true).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                etInput.setText("");
                //添加自己发送的消息到集合
                msgListManager.onImcomingMessage(message);
                msgListManager.scrollBottom();
            }

            @Override
            public void onFailed(int code) {

            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }

    /**
     * 切换软件盘显示
     *
     * @param isShow
     */
    private void switchSoftInput(Boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            imm.showSoftInput(etInput, InputMethodManager.SHOW_FORCED);
        } else {
            imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    /**
     * 切换输入显示状态
     *
     * @param id
     */
    private void switchStatus(int id) {
        switch (id) {
            case R.id.tv_audio:
                tvAudio.setTextColor(getResources().getColor(R.color.main_color));
                Drawable icon_audio_checked = getResources().getDrawable(R.drawable.icon_audio_checked);
                icon_audio_checked.setBounds(0, 0, icon_audio_checked.getMinimumWidth(), icon_audio_checked.getMinimumHeight());
                tvAudio.setCompoundDrawables(icon_audio_checked, null, null, null);
                tvText.setTextColor(getResources().getColor(R.color.black_a8));
                tvText.setText(R.string.text);
                Drawable icon_text_normal = getResources().getDrawable(R.drawable.icon_text_normal);
                icon_text_normal.setBounds(0, 0, icon_text_normal.getMinimumWidth(), icon_text_normal.getMinimumHeight());
                tvText.setCompoundDrawables(icon_text_normal, null, null, null);
                rlInputText.setVisibility(View.GONE);
                rlInputAudio.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_text:
                tvText.setTextColor(getResources().getColor(R.color.main_color));
                tvText.setText(R.string.pack_up);
                Drawable icon_text_checked = getResources().getDrawable(R.drawable.icon_text_checked);
                icon_text_checked.setBounds(0, 0, icon_text_checked.getMinimumWidth(), icon_text_checked.getMinimumHeight());
                tvText.setCompoundDrawables(icon_text_checked, null, null, null);
                tvAudio.setTextColor(getResources().getColor(R.color.black_a8));
                Drawable icon_audio_normal = getResources().getDrawable(R.drawable.icon_audio_normal);
                icon_audio_normal.setBounds(0, 0, icon_audio_normal.getMinimumWidth(), icon_audio_normal.getMinimumHeight());
                tvAudio.setCompoundDrawables(icon_audio_normal, null, null, null);
                rlInputText.setVisibility(View.VISIBLE);
                rlInputAudio.setVisibility(View.GONE);
                break;
            case R.id.tv_picture:
                tvAudio.setTextColor(getResources().getColor(R.color.black_a8));
                Drawable icon_audio_normal1 = getResources().getDrawable(R.drawable.icon_audio_normal);
                icon_audio_normal1.setBounds(0, 0, icon_audio_normal1.getMinimumWidth(), icon_audio_normal1.getMinimumHeight());
                tvAudio.setCompoundDrawables(icon_audio_normal1, null, null, null);
                tvText.setTextColor(getResources().getColor(R.color.black_a8));
                tvText.setText(R.string.text);
                Drawable icon_text_normal1 = getResources().getDrawable(R.drawable.icon_text_normal);
                icon_text_normal1.setBounds(0, 0, icon_text_normal1.getMinimumWidth(), icon_text_normal1.getMinimumHeight());
                tvText.setCompoundDrawables(icon_text_normal1, null, null, null);
                rlInputText.setVisibility(View.GONE);
                rlInputAudio.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean sendMessage(IMMessage msg) {
        return false;
    }

    @Override
    public void onInputPanelExpand() {

    }

    @Override
    public void shouldCollapseInputPanel() {

    }

    @Override
    public boolean isLongClickEnabled() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_share:
                popupWindow.dismiss();
                popupWindow = null;
                ChatRoomDetailsBean.RBean.ShareBean share = chatRoomDetailsBean.getR().getShare();
                ShareDialog.getInstance().showDialog(this, share.getSharetitle(), share.getSharecontent(), share.getShareurl(), share.getShareimg());
                break;
            case R.id.tv_details:
                popupWindow.dismiss();
                popupWindow = null;
                Intent intent = new Intent(this, UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AudioLiveDetailsFragment.class);
                intent.putExtra("isAuthor", isAthor);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(LIVEID, id);
                startActivity(intent);
                break;
            case R.id.tv_stop:
                popupWindow.dismiss();
                popupWindow = null;
                showdialog();
                break;
        }
    }

    //结束直播
    private void showdialog() {
        View view = View.inflate(this, R.layout.stop_audiolive_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .show();
        TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
        TextView tv_commit = (TextView) view.findViewById(R.id.tv_commit);
        dialog.show();
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAudioLive(dialog);
            }
        });
    }

    private void stopAudioLive(final AlertDialog dialog) {
        String url = String.format(UmiwiAPI.UMIWI_STOP_AUDIOLIVE, id);
        GetRequest<AudioLiveStopBean> request = new GetRequest<AudioLiveStopBean>(url, GsonParser.class, AudioLiveStopBean.class, new AbstractRequest.Listener<AudioLiveStopBean>() {
            @Override
            public void onResult(AbstractRequest<AudioLiveStopBean> request, AudioLiveStopBean audioLiveStopBean) {
                boolean audioLiveStopBeanR = audioLiveStopBean.isR();
                if (audioLiveStopBeanR) {
                    finish();
                } else {
                    Toast.makeText(AuthorChatRoomActivity.this, audioLiveStopBean.getM().trim().toString(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(AbstractRequest<AudioLiveStopBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }
}
