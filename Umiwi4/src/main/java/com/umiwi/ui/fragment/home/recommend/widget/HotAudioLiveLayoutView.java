package com.umiwi.ui.fragment.home.recommend.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.LiveChatRoomActivity;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.HotAudioLiveAdapter;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.fragment.audiolive.AudioLiveDetailsFragment;
import com.umiwi.ui.fragment.audiolive.LiveDetailsFragment;
import com.umiwi.ui.fragment.home.ContainerFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/24 0024.
 */

public class HotAudioLiveLayoutView extends LinearLayout {

    private TextView tv_tutor_title, tv_tutor_all, tv_exp_change;
    private ListView lv_home_expert_rec;
    private LinearLayout ll_expert_root;
    private RelativeLayout rl_tutor_all;
    private Context mContext;
    private HotAudioLiveAdapter hotAudioLiveAdapter;
    private ArrayList<RecommendBean.RBean.HotLiveBean.HotLiveRecord> record;
    String sec_live_moreurl;

    public HotAudioLiveLayoutView(Context context) {
        super(context);
        initView(context);
    }


    public HotAudioLiveLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.hot_audiolive_layout, this);
        ll_expert_root = (LinearLayout) findViewById(R.id.ll_expert_root);
        tv_tutor_title = (TextView) findViewById(R.id.tv_tutor_title);
        tv_tutor_all = (TextView) findViewById(R.id.tv_tutor_all);
        rl_tutor_all = (RelativeLayout) findViewById(R.id.rl_tutor_all);
        lv_home_expert_rec = (ListView) findViewById(R.id.lv_home_expert_rec);

        ll_expert_root.setVisibility(GONE);
        lv_home_expert_rec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecommendBean.RBean.HotLiveBean.HotLiveRecord hotLiveRecord = record.get(position);
                boolean isbuy = hotLiveRecord.isbuy();
                String liveId = hotLiveRecord.getId();
                String roomid = hotLiveRecord.getRoomid();
                if (isbuy) {
                    Intent intent = new Intent(mContext, LiveChatRoomActivity.class);
                    intent.putExtra(LiveDetailsFragment.DETAILS_ID, liveId);
                    intent.putExtra(LiveChatRoomActivity.ROOM_ID, roomid);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AudioLiveDetailsFragment.class);
                    intent.putExtra(AudioLiveDetailsFragment.LIVEID, liveId);
                    mContext.startActivity(intent);
                }

            }
        });
        rl_tutor_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ContainerFragment.getViewPagerItem().setCurrentItem(2);
            }
        });

    }

    public void setData(RecommendBean.RBean.HotLiveBean live, String sec_live_moreurl) {
        record = live.getRecord();
        this.sec_live_moreurl = sec_live_moreurl;

        ll_expert_root.setVisibility(VISIBLE);
        hotAudioLiveAdapter = new HotAudioLiveAdapter(mContext, record);
        lv_home_expert_rec.setAdapter(hotAudioLiveAdapter);

    }
}
