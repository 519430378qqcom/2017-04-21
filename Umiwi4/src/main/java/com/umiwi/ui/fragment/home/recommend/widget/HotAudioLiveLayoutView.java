package com.umiwi.ui.fragment.home.recommend.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.updateadapter.HotAudioLiveAdapter;

/**
 * Created by Administrator on 2017/4/24 0024.
 */

public class HotAudioLiveLayoutView extends LinearLayout {

    private TextView tv_tutor_title, tv_tutor_all,tv_exp_change;
    private ListView lv_home_expert_rec;
    private LinearLayout ll_expert_root;
    private RelativeLayout rl_tutor_all;
    private Context mContext;
    private HotAudioLiveAdapter hotAudioLiveAdapter;
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
        inflater.inflate(R.layout.hot_audiolive_layout,this);
        ll_expert_root = (LinearLayout) findViewById(R.id.ll_expert_root);
        tv_tutor_title = (TextView) findViewById(R.id.tv_tutor_title);
        tv_tutor_all = (TextView) findViewById(R.id.tv_tutor_all);
        rl_tutor_all = (RelativeLayout) findViewById(R.id.rl_tutor_all);
        lv_home_expert_rec = (ListView) findViewById(R.id.lv_home_expert_rec);

        ll_expert_root.setVisibility(GONE);
        lv_home_expert_rec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        rl_tutor_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    public void setData(){

        ll_expert_root.setVisibility(VISIBLE);
        hotAudioLiveAdapter = new HotAudioLiveAdapter(mContext);
        lv_home_expert_rec.setAdapter(hotAudioLiveAdapter);

    }
}
