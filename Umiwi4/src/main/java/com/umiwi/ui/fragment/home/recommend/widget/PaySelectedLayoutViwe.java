package com.umiwi.ui.fragment.home.recommend.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.updateadapter.PaySelectedAdapter;
import com.umiwi.ui.beans.updatebeans.RecommendBean;

import java.util.ArrayList;

/**
 * 首页-推荐-付费精选
 * Created by ${Gpsi} on 2017/2/27.
 */
public class PaySelectedLayoutViwe extends LinearLayout {

    private Context mContext;
    private LinearLayout ll_pay_selected_root;
    private TextView tv_title_type, tv_title_tag;
    private ListView lv_home_pay_selected;
    private PaySelectedAdapter mPaySelectedAdapter;
    private ArrayList<RecommendBean.RBean.ChargeBean.RecordBeanX> mList;

    public PaySelectedLayoutViwe(Context context) {
        super(context);
        init(context);
    }

    public PaySelectedLayoutViwe(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.home_pay_selected_layout, this);
        ll_pay_selected_root = (LinearLayout) findViewById(R.id.ll_pay_selected_root);
        tv_title_type = (TextView) findViewById(R.id.tv_title_type);
        tv_title_tag = (TextView) findViewById(R.id.tv_title_tag);
        lv_home_pay_selected = (ListView) findViewById(R.id.lv_home_pay_selected);
        ll_pay_selected_root.setVisibility(GONE);
    }

    public void setData(ArrayList<RecommendBean.RBean.ChargeBean.RecordBeanX> recordBeanXes, String chargeTitle,String chargehuan) {

        tv_title_type.setText(chargeTitle);
        tv_title_tag.setText(chargehuan);
        mList = recordBeanXes;
        if (null == mList || mList.size() == 0)
            return;
        ll_pay_selected_root.setVisibility(VISIBLE);
        mPaySelectedAdapter = new PaySelectedAdapter(mContext, mList);
        lv_home_pay_selected.setAdapter(mPaySelectedAdapter);

    }
}
