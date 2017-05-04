package com.umiwi.ui.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

/**
 * 主播聊天室界面
 */
public class AuthorChatRoomActivity extends AppCompatActivity implements ModuleProxy {
    public static final String ROOM_ID = "roomId";
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
    RelativeLayout rlInputText;
    @InjectView(R.id.rl_input)
    RelativeLayout rlInput;
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
        setContentView(R.layout.activity_author_chat_room);
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
                    Log.e("TAG", loginInfo.getAccount() + "----" + loginInfo.getToken());
                    //请求网易云信登录
                    RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
                        @Override
                        public void onSuccess(LoginInfo param) {
                            Toast.makeText(AuthorChatRoomActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
                            accessChatRoom(roomId);
                            registerObservers(true);
                        }

                        @Override
                        public void onFailed(int code) {
                            Log.e("TAG", code + "");
                            Toast.makeText(AuthorChatRoomActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onException(Throwable exception) {
                            Log.e("TAG", exception.toString());
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
            Toast.makeText(AuthorChatRoomActivity.this, messages.get(0).getContent(), Toast.LENGTH_SHORT).show();
            msgListManager.onImcomingMessage(messages);
        }
    };

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        registerObservers(false);
        super.onDestroy();
    }

    @OnClick({R.id.iv_back, R.id.iv_more, R.id.btn_comfirm,R.id.tv_record_hint_top, R.id.tv_audio, R.id.tv_text, R.id.tv_picture,
            R.id.btn_cancle, R.id.iv_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
               finish();
                break;
            case R.id.iv_more:
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
                if(getResources().getString(R.string.pack_up).equals(tvText.getText().toString())) {//点击收起
                    switchStatus(R.id.tv_picture);
                    switchSoftInput(false);
                }else {
                    switchStatus(R.id.tv_text);
                }
                break;
            case R.id.tv_picture://点击图片功能
                switchStatus(R.id.tv_picture);
                switchSoftInput(false);
                break;
            case R.id.btn_cancle://取消录制
                break;
            case R.id.iv_record://开始||停止录音
                if(getResources().getString(R.string.click_start_record).equals(tvRecordHint.getText().toString())) {
                    rlInputOptions.setVisibility(View.GONE);
                    tvRecordHintTop.setVisibility(View.VISIBLE);
                    tvRecordHintTop.setText(R.string.record_60_hint);
                    btnCancle.setVisibility(View.VISIBLE);
                    ivRecord.setImageResource(R.drawable.record_pause);
                    tvRecordHint.setText(R.string.click_stop_record);

                }else {}
                break;
        }
    }

    /**
     * 发送文本消息
     */
    private void sendTextMsg(String content) {
        // 创建文本消息
        final ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomTextMessage(roomId, content);
        //添加扩展字段userName
        HashMap<String, Object> map = new HashMap<>();
        map.put(MsgListManager.USER_NAME, UserManager.getInstance().getUser().getUsername());
        message.setRemoteExtension(map);
        NIMClient.getService(ChatRoomService.class).sendMessage(message, true).setCallback(new RequestCallback<Void>() {
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
    }

    /**
     * 切换软件盘显示
     * @param isShow
     */
    private void switchSoftInput(Boolean isShow){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(isShow) {
            imm.showSoftInput(etInput,InputMethodManager.SHOW_FORCED);
        }else {
            imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0); //强制隐藏键盘
        }
    }
    /**
     * 切换输入显示状态
     * @param id
     */
    private void switchStatus(int id){
        switch (id){
            case R.id.tv_audio:
                tvAudio.setTextColor(getResources().getColor(R.color.main_color));
                Drawable icon_audio_checked = getResources().getDrawable(R.drawable.icon_audio_checked);
                icon_audio_checked.setBounds(0, 0, icon_audio_checked.getMinimumWidth(), icon_audio_checked.getMinimumHeight());
                tvAudio.setCompoundDrawables(icon_audio_checked,null,null,null);
                tvText.setTextColor(getResources().getColor(R.color.black_a8));
                tvText.setText(R.string.text);
                Drawable icon_text_normal = getResources().getDrawable(R.drawable.icon_text_normal);
                icon_text_normal.setBounds(0, 0, icon_text_normal.getMinimumWidth(), icon_text_normal.getMinimumHeight());
                tvText.setCompoundDrawables(icon_text_normal,null,null,null);
                rlInputText.setVisibility(View.GONE);
                rlInputAudio.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_text:
                tvText.setTextColor(getResources().getColor(R.color.main_color));
                tvText.setText(R.string.pack_up);
                Drawable icon_text_checked = getResources().getDrawable(R.drawable.icon_text_checked);
                icon_text_checked.setBounds(0, 0, icon_text_checked.getMinimumWidth(), icon_text_checked.getMinimumHeight());
                tvText.setCompoundDrawables(icon_text_checked,null,null,null);
                tvAudio.setTextColor(getResources().getColor(R.color.black_a8));
                Drawable icon_audio_normal = getResources().getDrawable(R.drawable.icon_audio_normal);
                icon_audio_normal.setBounds(0, 0, icon_audio_normal.getMinimumWidth(), icon_audio_normal.getMinimumHeight());
                tvAudio.setCompoundDrawables(icon_audio_normal,null,null,null);
                rlInputText.setVisibility(View.VISIBLE);
                rlInputAudio.setVisibility(View.GONE);
                break;
            case R.id.tv_picture:
                tvAudio.setTextColor(getResources().getColor(R.color.black_a8));
                Drawable icon_audio_normal1 = getResources().getDrawable(R.drawable.icon_audio_normal);
                icon_audio_normal1.setBounds(0, 0, icon_audio_normal1.getMinimumWidth(), icon_audio_normal1.getMinimumHeight());
                tvAudio.setCompoundDrawables(icon_audio_normal1,null,null,null);
                tvText.setTextColor(getResources().getColor(R.color.black_a8));
                tvText.setText(R.string.text);
                Drawable icon_text_normal1 = getResources().getDrawable(R.drawable.icon_text_normal);
                icon_text_normal1.setBounds(0, 0, icon_text_normal1.getMinimumWidth(), icon_text_normal1.getMinimumHeight());
                tvText.setCompoundDrawables(icon_text_normal1,null,null,null);
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

//    @OnClick({R.id.ll_input_hint, R.id.rl_input_hint, , R.id.tv_record_hint, R.id.rl_input_audio, R.id.rl_input_text, R.id.rl_input})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.ll_input_hint:
//                break;
//            case R.id.rl_input_hint:
//                break;
//                break;
//            case R.id.tv_record_hint:
//                break;
//            case R.id.rl_input_audio:
//                break;
//            case R.id.rl_input_text:
//                break;
//            case R.id.rl_input:
//                break;
//        }
//    }
}
