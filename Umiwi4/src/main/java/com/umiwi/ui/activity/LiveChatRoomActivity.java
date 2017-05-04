package com.umiwi.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.ChatRoomDetailsBean;
import com.umiwi.ui.beans.NIMAccountBean;
import com.umiwi.ui.fragment.audiolive.LiveDetailsFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.Container;
import com.umiwi.ui.managers.ModuleProxy;
import com.umiwi.ui.managers.MsgListManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.youmi.account.manager.UserManager;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

public class LiveChatRoomActivity extends AppCompatActivity implements ModuleProxy {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_live_chat_room);
        ButterKnife.inject(this);
        loginNIM();
        initData();
        initMessageList();
        setListener();
    }

    /**
     * 监听设置
     */
    private void setListener() {
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
                    Log.e("TAG",loginInfo.getAccount()+"----"+loginInfo.getToken());
                    //请求网易云信登录
                    RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
                        @Override
                        public void onSuccess(LoginInfo param) {
                            Toast.makeText(LiveChatRoomActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
                            accessChatRoom(roomId);
                            registerObservers(true);
                        }

                        @Override
                        public void onFailed(int code) {
                            Log.e("TAG",code+"");
                            Toast.makeText(LiveChatRoomActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onException(Throwable exception) {
                            Log.e("TAG",exception.toString());
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

    private void initData() {
        String id = getIntent().getStringExtra(LiveDetailsFragment.DETAILS_ID);
        roomId = getIntent().getStringExtra(ROOM_ID);
        GetRequest<ChatRoomDetailsBean> request = new GetRequest<>(
                UmiwiAPI.CHAT_DETAILS + id, GsonParser.class, ChatRoomDetailsBean.class, new AbstractRequest.Listener<ChatRoomDetailsBean>() {
            @Override
            public void onResult(AbstractRequest<ChatRoomDetailsBean> request, ChatRoomDetailsBean chatRoomDetails) {
                if (chatRoomDetails != null) {
                    chatRoomDetailsBean = chatRoomDetails;
                    ChatRoomDetailsBean.RBean.RecordBean record = chatRoomDetails.getR().getRecord();
                    tvTitle.setText(record.getTitle());
                    String partNum = "("+record.getPartakenum()+"人)";
                    String status = record.getStatus();
                    switch (status){
                        case "1":
                            tvStatus.setText("未开始"+partNum);
                            break;
                        case "2":
                            tvStatus.setText("直播中"+partNum);
                            break;
                        case "3":
                            tvStatus.setText("已结束"+partNum);
                            break;
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
     * @param roomId
     */
    private void accessChatRoom(String roomId){
        EnterChatRoomData data = new EnterChatRoomData(roomId);
        NIMClient.getService(ChatRoomService.class).enterChatRoom(data);
    }
    /**
     * 注册消息接收器
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
            msgListManager.onImcomingMessage(messages);
        }
    };

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        registerObservers(false);
        super.onDestroy();
    }

    @OnClick({R.id.iv_back, R.id.iv_more, R.id.btn_comfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_more:
                break;
            case R.id.btn_comfirm:
                String content = etInput.getText().toString().trim();
                // 创建文本消息
                final ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomTextMessage(roomId,content);
                //添加扩展字段userName
                HashMap<String, Object> map = new HashMap<>();
                map.put(MsgListManager.USER_NAME, UserManager.getInstance().getUser().getUsername());
                message.setRemoteExtension(map);
                NIMClient.getService(ChatRoomService.class).sendMessage(message,true).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        //添加自己发送的消息到集合
                        ArrayList<ChatRoomMessage> chatRoomMessages = new ArrayList<>();
                        chatRoomMessages.add(message);
                        msgListManager.onImcomingMessage(chatRoomMessages);
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

}
