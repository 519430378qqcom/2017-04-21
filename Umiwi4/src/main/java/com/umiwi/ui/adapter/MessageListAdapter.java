package com.umiwi.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.umiwi.ui.R;
import com.umiwi.ui.managers.MsgListManager;
import com.umiwi.ui.util.DateUtils;
import com.umiwi.ui.view.CircleImageView;

import java.util.LinkedList;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.account.manager.UserManager;


/**
 * Created by Administrator on 2017/4/28.
 */

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * 显示观众发送的消息
     */
    private final int WATCHER_MSG = 1;
    /**
     * 显示主播发送的消息
     */
    private final int AUTHOR_MSG = 2;
    private Context context;
    public LinkedList<IMMessage> messages;

    public MessageListAdapter(Context context, LinkedList<IMMessage> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == WATCHER_MSG) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chatroom_mesage_watcher, parent, false);
            return new WatcherViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chatroom_mesage_author, parent, false);
            return new AuthorViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (Boolean) messages.get(position).getRemoteExtension().get(MsgListManager.IS_AUTHOR) ? AUTHOR_MSG : WATCHER_MSG;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (messages.size() < 1) {
            return;
        }
        IMMessage chatRoomMessage = messages.get(position);
        Map<String, Object> map = chatRoomMessage.getRemoteExtension();
        WatcherViewHolder watcherViewHolder;
        AuthorViewHolder authorViewHolder;
        if (holder instanceof WatcherViewHolder) {
            watcherViewHolder = (WatcherViewHolder) holder;
            if (map.size() > 1) {
                setformatTime(watcherViewHolder.tv_send_time, position);
                String userName = chatRoomMessage.getFromAccount();
                watcherViewHolder.tvId.setText(chatRoomMessage.getFromAccount());
                String url = (String) map.get(MsgListManager.HEAD_PHOTO_URL);
                Glide.with(context).load(url).placeholder(R.drawable.fragment_mine_login_no).into(watcherViewHolder.civHead);
                if (UserManager.getInstance().getUser().getUsername().equals(userName)) {//自己发的消息
                    watcherViewHolder.rl_text.setBackgroundResource(R.drawable.blue_rectangle);
                }
            }
            watcherViewHolder.tv_content.setText(chatRoomMessage.getContent());
        } else if (holder instanceof AuthorViewHolder) {
            authorViewHolder = (AuthorViewHolder) holder;
            if (map.size() > 1) {
                setformatTime(authorViewHolder.tv_send_time, position);
                String userName = chatRoomMessage.getFromAccount();
                authorViewHolder.tvId.setText(userName);
                String url = (String) map.get(MsgListManager.HEAD_PHOTO_URL);
                Glide.with(context).load(url).placeholder(R.drawable.fragment_mine_login_no).into(authorViewHolder.civHead);
                authorViewHolder.rl_text.setBackgroundResource(R.drawable.maincolor_rectangle);
            }
            if (chatRoomMessage.getMsgType() == MsgTypeEnum.text) {//显示文本消息
                authorViewHolder.rl_text.setVisibility(View.VISIBLE);
                authorViewHolder.rl_audio.setVisibility(View.GONE);
                authorViewHolder.rl_picture.setVisibility(View.GONE);
                authorViewHolder.tv_content.setText(chatRoomMessage.getContent());
            }else if(chatRoomMessage.getMsgType() == MsgTypeEnum.image) {//显示图片消息
                authorViewHolder.rl_text.setVisibility(View.GONE);
                authorViewHolder.rl_audio.setVisibility(View.GONE);
                authorViewHolder.rl_picture.setVisibility(View.VISIBLE);
                ImageAttachment attachment = (ImageAttachment) chatRoomMessage.getAttachment();
                Glide.with(context).load(attachment.getUrl()).placeholder(R.drawable.web_refresh).into(authorViewHolder.iv_receive);
            }else if(chatRoomMessage.getMsgType() == MsgTypeEnum.image) {//显示录音
                authorViewHolder.rl_text.setVisibility(View.GONE);
                authorViewHolder.rl_audio.setVisibility(View.VISIBLE);
                authorViewHolder.rl_picture.setVisibility(View.GONE);
                AudioAttachment attachment = (AudioAttachment) chatRoomMessage.getAttachment();
            }
        }
    }

    /**
     * 时间显示格式化字符串
     *
     * @param position
     * @return
     */
    private void setformatTime(TextView tv_time, int position) {
        Long time = messages.get(position).getTime();
        if (position == 0) {
            tv_time.setVisibility(View.VISIBLE);
            tv_time.setText(DateUtils.MdHm(time));
        } else {
            Long lastTime = messages.get(position - 1).getTime();
            if (time - lastTime > 120000) {
                tv_time.setVisibility(View.VISIBLE);
                tv_time.setText(DateUtils.MdHm(time));
            } else {
                tv_time.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    /**
     * 显示观众消息的viewholder
     */
    class WatcherViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_send_time)
        TextView tv_send_time;
        @InjectView(R.id.tv_content)
        TextView tv_content;
        @InjectView(R.id.civ_head)
        CircleImageView civHead;
        @InjectView(R.id.tv_id)
        TextView tvId;
        @InjectView(R.id.rl_text)
        RelativeLayout rl_text;

        public WatcherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    /**
     * 显示主播消息的viewholder
     */
    class AuthorViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_send_time)
        TextView tv_send_time;
        @InjectView(R.id.tv_content)
        TextView tv_content;
        @InjectView(R.id.civ_head)
        CircleImageView civHead;
        @InjectView(R.id.tv_id)
        TextView tvId;
        @InjectView(R.id.rl_text)
        RelativeLayout rl_text;
        @InjectView(R.id.rl_picture)
        RelativeLayout rl_picture;
        @InjectView(R.id.rl_audio)
        RelativeLayout rl_audio;
        @InjectView(R.id.iv_receive)
        ImageView iv_receive;

        public AuthorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public int getBottomDataPosition() {
        return messages.size() - 1;
    }
}
