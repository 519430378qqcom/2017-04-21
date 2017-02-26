package com.umiwi.ui.fragment.home.recommend.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.updateadapter.ExpertRecAdapter;
import com.umiwi.ui.beans.updatebeans.RecommendBean;

import java.util.ArrayList;

/**
 * 类描述：首页—推荐—行家推荐
 * Created by Gpsi on 2017-02-25.
 */

public class ExpertRecLayoutView extends LinearLayout {

    private Context mContext;
    private TextView tv_tutor_title, tv_tutor_all;
    private ListView lv_home_expert_rec;
    private ExpertRecAdapter mExpertRecAdapter;

    private ArrayList<RecommendBean.RBean.TutorBean> mList;

    public ExpertRecLayoutView(Context context) {
        super(context);
        init(context);
    }

    public ExpertRecLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.home_expert_recommend_layout, this);
        tv_tutor_title = (TextView) findViewById(R.id.tv_tutor_title);
        tv_tutor_all = (TextView) findViewById(R.id.tv_tutor_all);
        lv_home_expert_rec = (ListView) findViewById(R.id.lv_home_expert_rec);

    }

    public void setData(ArrayList<RecommendBean.RBean.TutorBean> tutorBeen, String tutorTitle, String tutorAll) {

        tv_tutor_title.setText(tutorTitle);
        tv_tutor_all.setText(tutorAll);
        mList = tutorBeen;
        if (null == mList || mList.size() == 0)
            return;
        mExpertRecAdapter = new ExpertRecAdapter(mContext,mList);
        lv_home_expert_rec.setAdapter(mExpertRecAdapter);

    }
}
