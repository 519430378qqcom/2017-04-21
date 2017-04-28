package com.umiwi.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import com.umiwi.ui.R;
import com.umiwi.ui.adapter.MessageListAdapter;
import com.umiwi.ui.beans.LiveDetailsBean;
import com.umiwi.ui.beans.NIMAccountBean;
import com.umiwi.ui.fragment.audiolive.LiveDetailsFragment;
import com.umiwi.ui.main.UmiwiAPI;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

public class LiveChatRoomActivity extends AppCompatActivity {
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
     * 聊天室消息集合
     */
    public LinkedList<ChatRoomMessage> chatRoomMessages;
    private MessageListAdapter messageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chat_room);
        ButterKnife.inject(this);
        chatRoomMessages = new LinkedList<>();
        loginNIM();
        initData();
        initMessageList();
    }

    /**
     * 登录网易云信服务端
     */
    private void loginNIM() {
        GetRequest<NIMAccountBean> request = new GetRequest<NIMAccountBean>(UmiwiAPI.NIM_ACCOUNT, GsonParser.class, NIMAccountBean.class, new AbstractRequest.Listener<NIMAccountBean>() {
            @Override
            public void onResult(AbstractRequest<NIMAccountBean> request, NIMAccountBean nimAccountBean) {
                if (nimAccountBean != null) {
                    final LoginInfo loginInfo = new LoginInfo(nimAccountBean.getR().getAccid(), nimAccountBean.getR().getToken());
                    RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
                        @Override
                        public void onSuccess(LoginInfo param) {
                            Toast.makeText(LiveChatRoomActivity.this, loginInfo.getAccount() + "登录成功", Toast.LENGTH_SHORT).show();
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
        GetRequest<LiveDetailsBean> request = new GetRequest<>(
                UmiwiAPI.LIVE_DETAILS + id, GsonParser.class, LiveDetailsBean.class, new AbstractRequest.Listener<LiveDetailsBean>() {
            @Override
            public void onResult(AbstractRequest<LiveDetailsBean> request, LiveDetailsBean liveDetailsBean) {
                if (liveDetailsBean != null) {
                    LiveDetailsBean.RBean.RecordBean record = liveDetailsBean.getR().getRecord();
                    tvTitle.setText(record.getTitle());
                    tvStatus.setText(record.getStatus()+"("+record.getPartakenum()+"人)");
                }
            }

            @Override
            public void onError(AbstractRequest<LiveDetailsBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    /**
     * 初始化
     */
    private void initMessageList() {
        rcy_mesagelist.setLayoutManager(new LinearLayoutManager(this));
        messageListAdapter = new MessageListAdapter(this, chatRoomMessages);
        rcy_mesagelist.setAdapter(messageListAdapter);
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
            Toast.makeText(LiveChatRoomActivity.this, messages.get(0).getContent(), Toast.LENGTH_SHORT).show();
            onImcomingMessage(messages);
        }
    };

    /**
     *
     */
    private void onImcomingMessage(List<ChatRoomMessage> messages) {
        chatRoomMessages.addAll(messages);
        messageListAdapter.messages = chatRoomMessages;
        messageListAdapter.notifyDataSetChanged();
//        messageListAdapter.notifyItemRangeInserted(chatRoomMessages.size() - messages.size(),chatRoomMessages.size());
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
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
                ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomTextMessage(roomId,content);
                NIMClient.getService(ChatRoomService.class).sendMessage(message,true).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        Toast.makeText(LiveChatRoomActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
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
}
