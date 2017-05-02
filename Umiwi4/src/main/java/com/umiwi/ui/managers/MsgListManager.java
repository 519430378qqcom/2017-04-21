package com.umiwi.ui.managers;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.umiwi.ui.adapter.MessageListAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 * 聊天室的消息列表控制类
 */

public class MsgListManager {
    /**
     * 消息扩展字段，中的昵称字段
     */
    public static final String USER_NAME = "name";
    /**
     * 消息集合最大容量
     */
    private static final int MESSAGE_CAPACITY = 500;
    private Container container;
    private RecyclerView msgView;
    private Context context;
    /**
     * 聊天室消息集合
     */
    public LinkedList<ChatRoomMessage> chatRoomMessages;
    private MessageListAdapter messageListAdapter;
    public MsgListManager(Container container, RecyclerView msgView) {
        this.container = container;
        this.msgView = msgView;
        context = msgView.getContext();
        init();
    }

    /**
     * 初始化数据
     */
    private void init(){
        chatRoomMessages = new LinkedList<>();
        msgView.setLayoutManager(new LinearLayoutManager(context));
        messageListAdapter = new MessageListAdapter(context, chatRoomMessages);
        msgView.setAdapter(messageListAdapter);
    }

    /**
     *当收到消息之后
     */
    public void onImcomingMessage(List<ChatRoomMessage> messages) {
        boolean needScrollToBottom = isLastMessageVisible();
        boolean needRefresh = false;
        for (ChatRoomMessage message : messages) {
            // 保证显示到界面上的消息，来自同一个聊天室
            if (isMyMessage(message)) {
                //只接收3种消息类型
                if(message.getMsgType()!=MsgTypeEnum.image&&message.getMsgType()!=MsgTypeEnum.audio&&message.getMsgType()!=MsgTypeEnum.text) {
                    return;
                }
                saveMessage(message);
                needRefresh = true;
            }
        }
        if(needRefresh) {
            messageListAdapter.notifyDataSetChanged();
        }
        if(needScrollToBottom) {
            msgView.scrollToPosition(messageListAdapter.getBottomDataPosition());
        }
    }

    /**
     * 判断底部数据是否可见
     * @return
     */
    private boolean isLastMessageVisible() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) msgView.getLayoutManager();
        int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
        return lastVisiblePosition >= messageListAdapter.getBottomDataPosition();
    }
    /**
     * 保存消息到消息集合，并保证不超过消息数量的最大值
     * @param message
     */
    public void saveMessage(final ChatRoomMessage message) {
        if (message == null) {
            return;
        }

        if (chatRoomMessages.size() >= MESSAGE_CAPACITY) {
            chatRoomMessages.poll();
        }

        chatRoomMessages.add(message);
    }
    /**
     * 判断是否是当前直播间的消息
     * @param message
     * @return
     */
    public boolean isMyMessage(ChatRoomMessage message) {
        return message.getSessionType() == container.sessionType
                && message.getSessionId() != null
                && message.getSessionId().equals(container.account);
    }
}
