package com.umiwi.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.umiwi.ui.R;
import com.umiwi.ui.managers.MsgListManager;
import com.umiwi.ui.view.CircleImageView;

import java.util.LinkedList;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.account.manager.UserManager;
import cn.youmi.account.model.UserModel;

/**
 * Created by Administrator on 2017/4/28.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder>{
    private Context context;
    public LinkedList<ChatRoomMessage> messages;
    public MessageListAdapter(Context context,LinkedList<ChatRoomMessage> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chatroom_mesage, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        if(messages.size()<1) {
            return;
        }
        ChatRoomMessage chatRoomMessage = messages.get(position);
        //userName和文字底色
        Map<String, Object> map = chatRoomMessage.getRemoteExtension();
        if(map!=null&&map.size()>0) {
            String userName = (String) map.get(MsgListManager.USER_NAME);
            if(!TextUtils.isEmpty(userName)) {
                holder.tvId.setText(userName);
                UserModel user = UserManager.getInstance().getUser();
                //如果是自己的消息
                if(userName.equals(user.getUsername())) {
                    holder.rlBackground.setBackgroundResource(R.drawable.blue_rectangle);
                    Glide.with(context).load(user.getAvatar()).crossFade().placeholder(R.drawable.ic_launcher).into(holder.civHead);
                }else {
                    holder.rlBackground.setBackgroundResource(R.drawable.white_rectangle);
                    holder.civHead.setImageResource(R.drawable.ic_launcher);
                }
            }
        }else {
            holder.tvId.setText(R.string.visitor);
            holder.rlBackground.setBackgroundResource(R.drawable.white_rectangle);
            holder.civHead.setImageResource(R.drawable.ic_launcher);
        }
        //message content
        holder.tv_content.setText(chatRoomMessage.getContent());
    }

    @Override
    public int getItemCount() {
        Log.e("TAG",messages.toString());
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.tv_content)
        TextView tv_content;
        @InjectView(R.id.civ_head)
        CircleImageView civHead;
        @InjectView(R.id.tv_id)
        TextView tvId;
        @InjectView(R.id.rl_background)
        RelativeLayout rlBackground;
        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }
    public int getBottomDataPosition() {
        return messages.size() - 1;
    }
}
