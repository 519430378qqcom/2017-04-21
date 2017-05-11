package com.umiwi.ui.managers;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.umiwi.ui.adapter.MessageListAdapter;
import com.umiwi.ui.view.RefreshLayout;

import java.util.LinkedList;
import java.util.List;

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
     * 拉取聊天记录的时间撮
     */
    private long chatRecordLastTime = 0;
    /**
     * 是否第一次获取聊天记录
     */
    private Boolean firstGetRecord = true;
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
    /**
     * 聊天记录的缓存集合
     */
    public LinkedList<IMMessage> chatRecordMessages;
    /**
     * 每次刷新加载聊天记录的数量
     */
    private final int loadRecordSum = 8;
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
    private void init() {
        chatRoomMessages = new LinkedList<>();
        msgView.setLayoutManager(new LinearLayoutManager(context));
        messageListAdapter = new MessageListAdapter(context, chatRoomMessages);
        msgView.setAdapter(messageListAdapter);
    }

    /**
     * 当收到消息之后
     */
    public void onImcomingMessage(IMMessage message) {
        boolean isLastMessageVisible = isLastMessageVisible();
        boolean needRefresh = false;
        // 保证显示到界面上的消息，来自同一个聊天室
        if (isMyMessage(message)) {
            //只接收3种消息类型
            if (message.getMsgType() != MsgTypeEnum.image && message.getMsgType() != MsgTypeEnum.audio && message.getMsgType() != MsgTypeEnum.text) {
                return;
            }
            saveMessage(message);
            needRefresh = true;
        }
        if (needRefresh) {
            messageListAdapter.notifyItemChanged(chatRoomMessages.size());
        }
        if (isLastMessageVisible) {
            scrollBottom();
        }
    }

    /**
     * 使消息列表滚动到底部
     */
    public void scrollBottom() {
        msgView.scrollToPosition(messageListAdapter.getBottomDataPosition());
    }

    /**
     * 消息集合头部加数据，用于加载聊天记录
     */
    public void addHeadMessage(IMMessage message) {
        boolean needRefresh = false;
        // 保证显示到界面上的消息，来自同一个聊天室
        if (isMyMessage(message) && msgFilter(message)) {
            if (chatRoomMessages.size() >= MESSAGE_CAPACITY) {
                chatRoomMessages.removeLast();
            }
            chatRoomMessages.addFirst(message);
            needRefresh = true;
        }
        if (needRefresh) {
            messageListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 消息过滤返回true的消息符合要求
     *
     * @param message
     */
    private boolean msgFilter(IMMessage message) {
        //只接收3种消息类型
        if (message.getMsgType() != MsgTypeEnum.image && message.getMsgType() != MsgTypeEnum.audio && message.getMsgType() != MsgTypeEnum.text) {
            return false;
        }
        //屏蔽没有扩展字段的消息
        if (message.getRemoteExtension().size() < 3) {
            return false;
        }
        return true;
    }

    /**
     * 判断底部数据是否可见
     *
     * @return
     */
    private boolean isLastMessageVisible() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) msgView.getLayoutManager();
        int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
        return lastVisiblePosition >= messageListAdapter.getBottomDataPosition();
    }

    /**
     * 保存消息到消息集合，并保证不超过消息数量的最大值
     *
     * @param message
     */
    public void saveMessage(final IMMessage message) {
        if (message == null) {
            return;
        }
        //屏蔽没有扩展字段的消息
        if (message.getRemoteExtension().size() < 3) {
            return;
        }
        if (chatRoomMessages.size() >= MESSAGE_CAPACITY) {
            chatRoomMessages.poll();
        }
        chatRoomMessages.add(message);
    }

    /**
     * 判断是否是当前直播间的消息
     *
     * @param message
     * @return
     */
    public boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == container.sessionType
                && message.getSessionId() != null
                && message.getSessionId().equals(container.account);
    }

    /**
     * 加载聊天记录
     */
    public void loadChatRecord(final String roomId, final RefreshLayout refreshLayout) {
        if (chatRecordMessages == null) {
            chatRecordMessages = new LinkedList<>();
        }
        if (chatRecordMessages.size() > loadRecordSum) {
            getChatRecordFromCache(roomId,refreshLayout);
            refreshLayout.setRefreshing(false);
        } else {
            if(chatRecordLastTime<=0) {
                chatRecordLastTime = System.currentTimeMillis();
            }
            NIMClient.getService(ChatRoomService.class).pullMessageHistory(roomId, chatRecordLastTime, 100).setCallback(new RequestCallback<List<ChatRoomMessage>>() {
                @Override
                public void onSuccess(List<ChatRoomMessage> param) {
                    if (param != null && param.size() > 0) {
                        chatRecordLastTime = param.get(param.size() - 1).getTime();
                        for (IMMessage imMessage : param) {
                            if (msgFilter(imMessage)) {
                                chatRecordMessages.add(imMessage);
                            }
                        }
                        getChatRecordFromCache(roomId,refreshLayout);
                        refreshLayout.setRefreshing(false);
                        //第一次加载聊天记录滚到底部
                        if (firstGetRecord) {
                            scrollBottom();
                            firstGetRecord = false;
                        }
                    } else {
                        Toast.makeText(context, "没有更多消息了", Toast.LENGTH_SHORT).show();
                        refreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailed(int code) {
                    Log.e("TAG",code+"");
                }

                @Override
                public void onException(Throwable exception) {
                    Log.e("TAG",exception.toString());
                }
            });
        }
    }

    /**
     * 从缓存集合中获取数据
     */
    private void getChatRecordFromCache(String roomId, final RefreshLayout refreshLayout) {
        if (chatRecordMessages.size() > loadRecordSum) {
            for (int i = 0; i < loadRecordSum; i++) {
                addHeadMessage(chatRecordMessages.get(0));
                chatRecordMessages.removeFirst();
            }
        }else {
            loadChatRecord(roomId,refreshLayout);
        }
    }
}
