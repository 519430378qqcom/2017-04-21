package com.umiwi.ui.fragment.home.recommend.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.updateadapter.BigShotAdapter;
import com.umiwi.ui.beans.updatebeans.RecommendBean;

import java.util.ArrayList;

/**
 * 类描述：
 * Created by Gpsi on 2017-02-27.
 */

public class BigShotLayoutView extends LinearLayout {
    private LinearLayout youmi_big_shot_root;
    private TextView title_type_textview;
    private HorizontalListView hlv_big_shot;
    private BigShotAdapter mBigShotAdapter;

    private Context mContext;
    private ArrayList<RecommendBean.RBean.DalaoBean> mList;


    public BigShotLayoutView(Context context) {
        super(context);
        init(context);
    }

    public BigShotLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.youmi_big_shot_item_layout, this);
        youmi_big_shot_root = (LinearLayout) findViewById(R.id.youmi_big_shot_root);
        title_type_textview = (TextView) findViewById(R.id.title_type_textview);
        hlv_big_shot = (HorizontalListView) findViewById(R.id.hlv_big_shot);
        youmi_big_shot_root.setVisibility(GONE);

    }

    public void setData(ViewPager rootViewPager, ScrollView rootScroll, ArrayList<RecommendBean.RBean.DalaoBean> dalaoBeen, String answerTitle) {

        title_type_textview.setText(answerTitle);
        mList = dalaoBeen;
        if (null == mList || mList.size() == 0)
            return;
        youmi_big_shot_root.setVisibility(VISIBLE);
        hlv_big_shot.setRootParent(rootViewPager, rootScroll);
        mBigShotAdapter = new BigShotAdapter(mContext, mList);
        hlv_big_shot.setAdapter(mBigShotAdapter);
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBigShotAdapter.notifyDataSetChanged();
            super.handleMessage(msg);
        }
    };
}
