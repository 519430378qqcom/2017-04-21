package com.umiwi.ui.fragment.home.recommend.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.updateadapter.NewfreeAdapterV2;
import com.umiwi.ui.beans.updatebeans.NewFree;
import com.umiwi.ui.beans.updatebeans.RecommendBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：首页—推荐—最新免费
 * Created by Gpsi on 2017-02-25.
 */

public class FreeLayoutView extends LinearLayout {

    private TextView title_type_textview, title_huan;
    private ListView lv_new_free;
    private LinearLayout ll_free_root;
    private Context mContext;

    private NewfreeAdapterV2 mNewfreeAdapterV2;
    private ArrayList<RecommendBean.RBean.FreeBean.RecordBean> mList;

    public FreeLayoutView(Context context) {
        super(context);
        init(context);
    }

    public FreeLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.new_free_layout, this);
        ll_free_root = (LinearLayout) findViewById(R.id.ll_free_root);
        title_type_textview = (TextView) findViewById(R.id.title_type_textview);
        title_huan = (TextView) findViewById(R.id.title_huan);
        lv_new_free = (ListView) findViewById(R.id.lv_new_free);
        lv_new_free.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mList.get(position).getId();

            }
        });
        ll_free_root.setVisibility(GONE);
    }

    public void setData(ArrayList<RecommendBean.RBean.FreeBean.RecordBean> freeBean, String titleFree, String titleHuan) {
        title_type_textview.setText(titleFree);
        title_huan.setText(titleHuan);
        mList = freeBean;
        if (null == mList || mList.size() == 0)
            return;
        ll_free_root.setVisibility(VISIBLE);
        mNewfreeAdapterV2 = new NewfreeAdapterV2(mContext, mList);
        lv_new_free.setAdapter(mNewfreeAdapterV2);
    }
}
