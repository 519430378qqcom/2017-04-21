package com.umiwi.ui.fragment.home.recommend.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.updateadapter.LineActionadapter;
import com.umiwi.ui.beans.updatebeans.RecommendBean;

import java.util.ArrayList;

/**
 * Created by ${Gpsi} on 2017/2/27.
 */
public class LineActionLayoutViwe extends LinearLayout {

    private Context mContext;
    private LinearLayout line_action_root;
    private TextView title_type_textview;
    private ListView lv_home_action;
    private LineActionadapter mLineActionadapter;
    private ArrayList<RecommendBean.RBean.HuodongBean> mList;

    public LineActionLayoutViwe(Context context) {
        super(context);
        init(context);
    }

    public LineActionLayoutViwe(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.line_action_item_layout, this);
        line_action_root = (LinearLayout) findViewById(R.id.line_action_root);
        title_type_textview = (TextView) findViewById(R.id.title_type_textview);
//        title_tag = (TextView) findViewById(R.id.title_tag);
        lv_home_action = (ListView) findViewById(R.id.lv_home_action);
        line_action_root.setVisibility(GONE);
    }

    public void setData(ArrayList<RecommendBean.RBean.HuodongBean> huodongBeen, String huodongTitle) {

        title_type_textview.setText(huodongTitle);
//        title_tag.setText(huodongAll);
        mList = huodongBeen;
        if (null == mList || mList.size() == 0)
            return;
        line_action_root.setVisibility(VISIBLE);
        mLineActionadapter = new LineActionadapter(mContext, mList);
        lv_home_action.setAdapter(mLineActionadapter);

    }
}
