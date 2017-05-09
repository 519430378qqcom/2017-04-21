package com.umiwi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.umiwi.ui.R;
import com.umiwi.ui.adapter.ChatRecordAdapter;
import com.umiwi.ui.beans.ChatRecordBean;
import com.umiwi.ui.beans.ChatRoomDetailsBean;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.fragment.audiolive.AudioLiveDetailsFragment;
import com.umiwi.ui.fragment.audiolive.LiveDetailsFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.ModuleProxy;
import com.umiwi.ui.managers.MsgListManager;
import com.umiwi.ui.view.RefreshLayout;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

import static com.umiwi.ui.fragment.audiolive.AudioLiveDetailsFragment.LIVEID;

public class ChatRecordActivity extends AppCompatActivity implements ModuleProxy, View.OnClickListener {
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
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
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
    private PopupWindow popupWindow;
    private String id;
    private int page;
    private ChatRecordAdapter chatRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_record_chat);
        ButterKnife.inject(this);
        initData();
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.main_color));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getChatRecord();
            }
        });
    }
    /**
     * 获取聊天记录
     */
    private void getChatRecord() {
        String url = String.format(UmiwiAPI.CHAT_RECORD,id,page);
        GetRequest<ChatRecordBean> request = new GetRequest<>(
                url, GsonParser.class, ChatRecordBean.class, new AbstractRequest.Listener<ChatRecordBean>() {
            @Override
            public void onResult(AbstractRequest<ChatRecordBean> request, ChatRecordBean chatRecordBean) {
                if(chatRecordBean!=null) {
                    page++;
                    List<ChatRecordBean.RBean.RecordBean> records = chatRecordBean.getR().getRecord();
                    if(chatRecordAdapter == null) {
                        chatRecordAdapter = new ChatRecordAdapter(ChatRecordActivity.this);
                        for (ChatRecordBean.RBean.RecordBean recordBean:records){
                            chatRecordAdapter.addChatRecord(recordBean);
                        }
                        chatRecordAdapter.notifyDataSetChanged();
                    }else {
                        for (ChatRecordBean.RBean.RecordBean recordBean:records){
                            chatRecordAdapter.addChatRecord(recordBean);
                        }
                        chatRecordAdapter.notifyDataSetChanged();
                    }
                }else {
                    refreshLayout.setRefreshing(false);
                    Toast.makeText(ChatRecordActivity.this, "没用更多消息了", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(AbstractRequest<ChatRecordBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    private void initData() {
        id = getIntent().getStringExtra(LiveDetailsFragment.DETAILS_ID);
        roomId = getIntent().getStringExtra(ROOM_ID);
        GetRequest<ChatRoomDetailsBean> request = new GetRequest<>(
                UmiwiAPI.CHAT_DETAILS + id, GsonParser.class, ChatRoomDetailsBean.class, new AbstractRequest.Listener<ChatRoomDetailsBean>() {
            @Override
            public void onResult(AbstractRequest<ChatRoomDetailsBean> request, ChatRoomDetailsBean chatRoomDetails) {
                if (chatRoomDetails != null) {
                    chatRoomDetailsBean = chatRoomDetails;
                    ChatRoomDetailsBean.RBean.RecordBean record = chatRoomDetails.getR().getRecord();
                    tvTitle.setText(record.getTitle());
                    String partNum = "(" + record.getPartakenum() + "人)";
                    String status = record.getStatus();
                    switch (status) {
                        case "1":
                            tvStatus.setText("未开始" + partNum);
                            break;
                        case "2":
                            tvStatus.setText("直播中" + partNum);
                            break;
                        case "3":
                            tvStatus.setText("已结束" + partNum);
                            break;
                    }
                    getChatRecord();
                }
            }

            @Override
            public void onError(AbstractRequest<ChatRoomDetailsBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        if (chatRecordAdapter.handler != null) {
            chatRecordAdapter.handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    @OnClick({R.id.iv_back, R.id.iv_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_more:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    initmPopupWindow();
                }
                break;
        }
    }

    public void initmPopupWindow() {
        View cusView = getLayoutInflater().inflate(R.layout.popupview_item, null);
        popupWindow = new PopupWindow(cusView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAsDropDown(ivMore, -350, 50);

        //触摸事件
        cusView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                return false;
            }
        });

        TextView tv_share = (TextView) cusView.findViewById(R.id.tv_share);
        TextView tv_details = (TextView) cusView.findViewById(R.id.tv_details);
        TextView tv_stop = (TextView) cusView.findViewById(R.id.tv_stop);
        View line = cusView.findViewById(R.id.view_line);
        line.setVisibility(View.GONE);
        tv_stop.setVisibility(View.GONE);
        tv_share.setOnClickListener(this);
        tv_details.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_share:
                popupWindow.dismiss();
                popupWindow = null;
                ChatRoomDetailsBean.RBean.ShareBean share = chatRoomDetailsBean.getR().getShare();
                ShareDialog.getInstance().showDialog(this, share.getSharetitle(), share.getSharecontent(), share.getShareurl(), share.getShareimg());
                break;
            case R.id.tv_details:
                popupWindow.dismiss();
                popupWindow = null;
                if (AudioLiveDetailsFragment.isPause) {
                    finish();
                } else {
                    Intent intent = new Intent(this, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AudioLiveDetailsFragment.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra(LIVEID, id);
                    startActivity(intent);
                }
                break;

        }
    }

}
