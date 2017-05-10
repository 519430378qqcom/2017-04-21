package com.umiwi.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.umiwi.ui.R;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.MsgListManager;
import com.umiwi.ui.util.DateUtils;
import com.umiwi.ui.view.CircleImageView;

import java.util.LinkedList;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.account.manager.UserManager;

import static com.umiwi.ui.main.UmiwiApplication.mainActivity;


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
    private final String URL = "url";
    /**
     * 当前音频的viewHolder
     */
    private AuthorViewHolder viewHolder;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                String audioPath = UmiwiApplication.mainActivity.service.getAudioPath();
                String url = msg.getData().getString(URL);
                if (audioPath.equals(url)) {
                    if (viewHolder.sb_audio_progress.getProgress() < viewHolder.sb_audio_progress.getMax()) {
                        viewHolder.sb_audio_progress.setProgress(viewHolder.sb_audio_progress.getProgress() + 1000);
                        sendHandler(url, viewHolder);
                    } else {
                        viewHolder.sb_audio_progress.setProgress(0);
                        viewHolder.iv_audio_controll.setImageResource(android.R.drawable.ic_media_play);
                    }
                }
            } catch (RemoteException e) {


            }
        }
    };

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
            View view = LayoutInflater.from(context).inflate(R.layout.item_chatroom_mesage_author1, parent, false);
            return new AuthorViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object isAuthor = messages.get(position).getRemoteExtension().get(MsgListManager.IS_AUTHOR);
        if (isAuthor != null) {
            Boolean isauthor = (Boolean) isAuthor;
            if (isauthor) {
                return AUTHOR_MSG;
            }  else {
                return WATCHER_MSG;
            }
        } else {
            return AUTHOR_MSG;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (messages.size() < 1) {
            return;
        }
        final IMMessage chatRoomMessage = messages.get(position);
        Map<String, Object> map = chatRoomMessage.getRemoteExtension();
        WatcherViewHolder watcherViewHolder;
        final AuthorViewHolder authorViewHolder;
        if (holder instanceof WatcherViewHolder) {
            watcherViewHolder = (WatcherViewHolder) holder;
            if (map.size() > 1) {
                setformatTime(watcherViewHolder.tv_send_time, position);
                String userName = (String) chatRoomMessage.getRemoteExtension().get(MsgListManager.USER_NAME);
                watcherViewHolder.tvId.setText(userName);
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
                String userName = (String) chatRoomMessage.getRemoteExtension().get(MsgListManager.USER_NAME);
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
            } else if (chatRoomMessage.getMsgType() == MsgTypeEnum.image) {//显示图片消息
                authorViewHolder.rl_text.setVisibility(View.GONE);
                authorViewHolder.rl_audio.setVisibility(View.GONE);
                authorViewHolder.rl_picture.setVisibility(View.VISIBLE);
                ImageAttachment attachment = (ImageAttachment) chatRoomMessage.getAttachment();
                Glide.with(context).load(attachment.getUrl()).into(authorViewHolder.iv_receive);
            } else if (chatRoomMessage.getMsgType() == MsgTypeEnum.audio) {//显示录音
                authorViewHolder.rl_text.setVisibility(View.GONE);
                authorViewHolder.rl_audio.setVisibility(View.VISIBLE);
                authorViewHolder.rl_picture.setVisibility(View.GONE);
                AudioAttachment attachment = (AudioAttachment) chatRoomMessage.getAttachment();
                final String audioUrl = attachment.getUrl();
                long duration = attachment.getDuration();
                try {
                    if (isPlayUrl(audioUrl)) {
                        authorViewHolder.iv_audio_controll.setImageResource(android.R.drawable.ic_media_pause);
                    } else {
                        authorViewHolder.iv_audio_controll.setImageResource(android.R.drawable.ic_media_play);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                authorViewHolder.tv_audio_time.setText(DateUtils.formatmmss(duration));
                authorViewHolder.sb_audio_progress.setMax((int) duration);
                authorViewHolder.sb_audio_progress.setProgress(0);
                //判断当前列表的音频文件是否正在播放
                if (UmiwiApplication.mainActivity.service != null) {
                    String playingUrl = null;
                    try {
                        playingUrl = UmiwiApplication.mainActivity.service.getAudioPath();
                        //播放的是当前列表的音频
                        if (isPlayUrl(playingUrl)) {
                            try {
                                //判断播放状态
                                if (mainActivity.service.isPlaying()) {
                                    authorViewHolder.iv_audio_controll.setImageResource(android.R.drawable.ic_media_pause);
                                    authorViewHolder.sb_audio_progress.setProgress(mainActivity.service.getCurrentPosition());
                                } else {
                                    authorViewHolder.iv_audio_controll.setImageResource(android.R.drawable.ic_media_play);
                                    if (mainActivity.service.getCurrentPosition() >= duration) {
                                        authorViewHolder.sb_audio_progress.setProgress(0);
                                    }
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        } else {
                            authorViewHolder.iv_audio_controll.setImageResource(android.R.drawable.ic_media_play);
                            authorViewHolder.sb_audio_progress.setProgress(0);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    authorViewHolder.iv_audio_controll.setImageResource(android.R.drawable.ic_media_play);
                    authorViewHolder.sb_audio_progress.setProgress(0);
                }
                authorViewHolder.iv_audio_controll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UmiwiApplication.mainActivity.service != null) {
                            try {
                                //正在播放，并且播放的是此item
                                if (UmiwiApplication.mainActivity.service.isPlaying() && isPlayUrl(audioUrl)) {
                                    UmiwiApplication.mainActivity.service.pause();
                                    authorViewHolder.iv_audio_controll.setImageResource(android.R.drawable.ic_media_play);
                                    handler.removeCallbacksAndMessages(null);
                                    //正在播放，此前播放的不是此item
                                } else if (UmiwiApplication.mainActivity.service.isPlaying() && !isPlayUrl(audioUrl)) {
                                    UmiwiApplication.mainActivity.service.pause();
                                    UmiwiApplication.mainActivity.service.openAudio(audioUrl);
                                    authorViewHolder.iv_audio_controll.setImageResource(android.R.drawable.ic_media_pause);
                                    viewHolder.iv_audio_controll.setImageResource(android.R.drawable.ic_media_play);
                                    viewHolder.sb_audio_progress.setProgress(0);
                                    sendHandler(audioUrl, authorViewHolder);
                                    //播放暂停状态,之前播放的是此item
                                } else if (!UmiwiApplication.mainActivity.service.isPlaying() && isPlayUrl(audioUrl)) {
                                    UmiwiApplication.mainActivity.service.play();
                                    UmiwiApplication.mainActivity.service.seekTo(authorViewHolder.sb_audio_progress.getProgress());
                                    authorViewHolder.iv_audio_controll.setImageResource(android.R.drawable.ic_media_pause);
                                    sendHandler(audioUrl, authorViewHolder);
                                    //播放暂停状态,之前播放的也不是此item
                                } else if (!UmiwiApplication.mainActivity.service.isPlaying() && !isPlayUrl(audioUrl)) {
                                    UmiwiApplication.mainActivity.service.openAudio(audioUrl);
                                    UmiwiApplication.mainActivity.service.seekTo(authorViewHolder.sb_audio_progress.getProgress());
                                    authorViewHolder.iv_audio_controll.setImageResource(android.R.drawable.ic_media_pause);
                                    sendHandler(audioUrl, authorViewHolder);
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        } else {
                            VoiceDetailsFragment.bind(audioUrl);
                            authorViewHolder.iv_audio_controll.setImageResource(android.R.drawable.ic_media_pause);
                            sendHandler(audioUrl, authorViewHolder);
                        }
                    }
                });
                authorViewHolder.sb_audio_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                        chatRoomMessage.getLocalExtension().put(MsgListManager.AUDIO_PLAYED_DURATION,progress);
                        if (fromUser && UmiwiApplication.mainActivity.service != null) {
                            try {
                                //播放的是此item
                                if (isPlayUrl(audioUrl)) {
                                    UmiwiApplication.mainActivity.service.seekTo(progress);
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }
        }
    }

    /**
     * 服务中播放的当前音频地址和传入的地址是否一样
     *
     * @param audioUrl
     * @return
     * @throws RemoteException
     */
    private boolean isPlayUrl(String audioUrl) throws RemoteException {
        if (UmiwiApplication.mainActivity.service == null) {
            return false;
        }
        return UmiwiApplication.mainActivity.service.getAudioPath().equals(audioUrl);
    }

    /**
     * 发送消息开始计时
     *
     * @param audioUrl
     */
    private void sendHandler(String audioUrl, AuthorViewHolder viewHolder) {
        this.viewHolder = viewHolder;
        handler.removeCallbacksAndMessages(null);
        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString(URL, audioUrl);
        message.setData(bundle);
        handler.sendMessageDelayed(message, 1000);
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
        @InjectView(R.id.iv_receive)
        ImageView iv_receive;
        @InjectView(R.id.rl_audio)
        RelativeLayout rl_audio;
        @InjectView(R.id.iv_audio_controll)
        ImageView iv_audio_controll;
        @InjectView(R.id.sb_audio_progress)
        SeekBar sb_audio_progress;
        @InjectView(R.id.tv_audio_time)
        TextView tv_audio_time;

        public AuthorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public int getBottomDataPosition() {
        return messages.size() - 1;
    }
}
