package com.umiwi.ui.managers;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.umiwi.ui.adapter.MessageListAdapter;

import java.util.LinkedList;

/**
 * Created by Administrator on 2017/5/2.
 * 聊天室的消息列表控制类
 */

public class MsgListManager {
    /**
     * 是否主播
     */
    public static final String IS_AUTHOR = "isauthor";
    /**
     * 消息扩展字段，头像的图片链接
     */
    public static final String HEAD_PHOTO_URL = "avatar";
    /**
     * 消息扩展字段，头像的图片链接
     */
    public static final String USER_NAME = "username";
    /**
     * 消息的本地扩展字段，记录每个音频文件的播放进度
     */
    public static final String AUDIO_PLAYED_DURATION = "currenPosition";
    /**
     * 消息集合最大容量
     */
    public static final int MESSAGE_CAPACITY = 500;
    private Container container;
    private RecyclerView msgView;
    private Context context;
    /**
     * 聊天室消息集合
     */
    public LinkedList<IMMessage> chatRoomMessages;
    public MessageListAdapter messageListAdapter;
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
    public void onImcomingMessage(IMMessage message) {
        boolean isLastMessageVisible = isLastMessageVisible();
        boolean needRefresh = false;
            // 保证显示到界面上的消息，来自同一个聊天室
            if (isMyMessage(message)) {
                //只接收3种消息类型
                if(message.getMsgType()!=MsgTypeEnum.image&&message.getMsgType()!=MsgTypeEnum.audio&&message.getMsgType()!=MsgTypeEnum.text) {
                    return;
                }
                saveMessage(message);
                needRefresh = true;
            }
        if(needRefresh) {
            messageListAdapter.notifyItemChanged(chatRoomMessages.size());
        }
        if(isLastMessageVisible) {
            msgView.scrollToPosition(messageListAdapter.getBottomDataPosition());
        }
    }
    /**
     *头部加数据
     */
    public void addHeadMessage(IMMessage message) {
        boolean needRefresh = false;
        // 保证显示到界面上的消息，来自同一个聊天室
        if (isMyMessage(message)) {
            //只接收3种消息类型
            if(message.getMsgType()!=MsgTypeEnum.image&&message.getMsgType()!=MsgTypeEnum.audio&&message.getMsgType()!=MsgTypeEnum.text) {
                return;
            }
            //屏蔽没有扩展字段的消息
            if (message.getRemoteExtension().size()<2) {
                return;
            }
            if (chatRoomMessages.size() >= MESSAGE_CAPACITY) {
                chatRoomMessages.removeLast();
            }
            chatRoomMessages.addFirst(message);
            needRefresh = true;
        }
        if(needRefresh) {
            messageListAdapter.notifyDataSetChanged();
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
    public void saveMessage(final IMMessage message) {
        if (message == null) {
            return;
        }
        //屏蔽没有扩展字段的消息
        if (message.getRemoteExtension().size()<2) {
            return;
        }
        if (chatRoomMessages.size() >= MESSAGE_CAPACITY) {
            chatRoomMessages.poll();
        }
//        if(message.getMsgType()==MsgTypeEnum.audio) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put(MsgListManager.AUDIO_PLAYED_DURATION,0);
//            message.setLocalExtension(map);
//        }
        chatRoomMessages.add(message);
    }
    /**
     * 判断是否是当前直播间的消息
     * @param message
     * @return
     */
    public boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == container.sessionType
                && message.getSessionId() != null
                && message.getSessionId().equals(container.account);
    }

}
