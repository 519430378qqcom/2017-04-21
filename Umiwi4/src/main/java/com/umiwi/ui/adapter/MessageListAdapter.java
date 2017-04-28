package com.umiwi.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.umiwi.ui.R;

import java.util.LinkedList;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }
}
