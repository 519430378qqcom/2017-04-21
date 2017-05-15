package com.umiwi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.ChatRoomDetailsBean;
import com.umiwi.ui.beans.NIMAccountBean;
import com.umiwi.ui.beans.OnlineSumBean;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.fragment.audiolive.AudioLiveDetailsFragment;
import com.umiwi.ui.fragment.audiolive.LiveDetailsFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.Container;
import com.umiwi.ui.managers.ModuleProxy;
import com.umiwi.ui.managers.MsgListManager;
import com.umiwi.ui.view.RefreshLayout;

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


public class LiveChatRoomActivity extends AppCompatActivity implements ModuleProxy, View.OnClickListener {
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
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    public static final String ROOM_ID = "roomId";
    /**
     * 聊天室ID
     */
    private String roomId;
    /**
     * 消息列表控制器
     */
    private MsgListManager msgListManager;
    /**
     * 聊天室消息
     */
    private ChatRoomDetailsBean chatRoomDetailsBean;
    private PopupWindow popupWindow;
    private String id;
    /**
     * 判断是否已经加载过聊天记录
     */
    private boolean alreadyloadRecord;
    private final int TIME = 1;
    private final int UPDATE_SUM = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_SUM:
                    updateSum();
                    Message message = Message.obtain();
                    message.what = UPDATE_SUM;
                    handler.sendMessageDelayed(message,30000);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_live_chat_room);
        ButterKnife.inject(this);
        loginNIM();
        initData();
        initMessageList();
        initRefreshLayout();
        Message message = Message.obtain();
        message.what = UPDATE_SUM;
        handler.sendMessageDelayed(message,30000);
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
     * 获取聊天记录
     */
    private void getChatRecord() {
        msgListManager.loadChatRecord(roomId, refreshLayout);
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
                            if (!alreadyloadRecord) {
                                getChatRecord();
                                alreadyloadRecord = true;
                            }
                        }

                        @Override
                        public void onFailed(int code) {
                            Toast.makeText(LiveChatRoomActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onException(Throwable exception) {
                            Toast.makeText(LiveChatRoomActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();
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
    /**
     * 更新在线人数
     */
    private void updateSum(){
        String url = String.format(UmiwiAPI.UMIWI_ONLINE_NUM,id);
        GetRequest<OnlineSumBean> request = new GetRequest<>(
                url, GsonParser.class, OnlineSumBean.class, new AbstractRequest.Listener<OnlineSumBean>() {

            @Override
            public void onResult(AbstractRequest<OnlineSumBean> request, OnlineSumBean onlineSumBean) {
                String partakenum = onlineSumBean.getR().getPartakenum();
                if(partakenum!=null&&!"".equals(partakenum)) {
                    String partNum = "(" + partakenum + "人)";
                    String status = chatRoomDetailsBean.getR().getRecord().getStatus();
                    String content;
                    if(status !=null&&!"".equals(status)) {
                        switch (status) {
                            case "1":
                                status = "未开始";
                                break;
                            case "2":
                                status = "直播中";
                                break;
                            case "3":
                                status = "已结束";
                                break;
                        }
                        content = status + partNum;
                    }else {
                        content = partNum;
                    }
                    tvStatus.setText(content);
                }
            }

            @Override
            public void onError(AbstractRequest<OnlineSumBean> requet, int statusCode, String body) {

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
                    String istutor = record.getIstutor();
                    if("1".equals(istutor)) {
                        Toast.makeText(LiveChatRoomActivity.this, "您是播主，您现在进的是普通直播间", Toast.LENGTH_LONG).show();
                    }
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
                    if (StatusCode.LOGINED == NIMClient.getStatus()&&!alreadyloadRecord) {
                        getChatRecord();
                        alreadyloadRecord = true;
                    }
                }
            }

            @Override
            public void onError(AbstractRequest<ChatRoomDetailsBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    /**
     * 初始化
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
        NIMClient.getService(ChatRoomService.class).enterChatRoom(data).setCallback(new RequestCallback() {
            @Override
            public void onSuccess(Object param) {

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
     * 离开聊天室
     * @param roomId
     */
    private void exitChatRoom(String roomId){
        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
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
            for (ChatRoomMessage chatRoomMessage : messages) {
                msgListManager.onImcomingMessage(chatRoomMessage);
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
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
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
        if (UmiwiApplication.mainActivity.service != null) {
            try {
                if (UmiwiApplication.mainActivity.service.isPlaying()) {
                    UmiwiApplication.mainActivity.service.pause();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        exitChatRoom(roomId);
        super.onDestroy();
    }

    @OnClick({R.id.iv_back, R.id.iv_more, R.id.btn_comfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_more:
                if(chatRoomDetailsBean == null) {
                    Toast.makeText(LiveChatRoomActivity.this, "未获取直播间信息，请稍候再试", Toast.LENGTH_SHORT).show();
                }
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    initmPopupWindow();
                }
                break;
            case R.id.btn_comfirm:
                String content = etInput.getText().toString().trim();
                if (content == null || content.equals("")) {
                    Toast.makeText(LiveChatRoomActivity.this, "发送空消息不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 创建文本消息
                final ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomTextMessage(roomId, content);
                HashMap<String, Object> map = new HashMap<>();
                map.put(MsgListManager.IS_AUTHOR, false);
                map.put(MsgListManager.HEAD_PHOTO_URL, UserManager.getInstance().getUser().getAvatar());
                map.put(MsgListManager.USER_NAME, UserManager.getInstance().getUser().getUsername());
                message.setRemoteExtension(map);
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
        View line = cusView.findViewById(R.id.view_line);
        line.setVisibility(View.GONE);
        tv_stop.setVisibility(View.GONE);
        tv_share.setOnClickListener(this);
        tv_details.setOnClickListener(this);
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
                if (AudioLiveDetailsFragment.isPause) {
                    finish();
                } else {
                    Intent intent = new Intent(this, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AudioLiveDetailsFragment.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra(LIVEID, id);
                    startActivity(intent);
                }
                break;
        }
    }

}
