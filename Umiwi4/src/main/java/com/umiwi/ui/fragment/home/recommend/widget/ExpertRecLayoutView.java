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
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.ExpertRecAdapter;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.fragment.AudioSpecialFragment;
import com.umiwi.ui.fragment.home.alreadyshopping.LogicalThinkingFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.ColumnDetailsFragment;
import com.umiwi.ui.main.UmiwiAPI;

import java.util.ArrayList;

/**
 * 类描述：首页—推荐—行家推荐
 * Created by Gpsi on 2017-02-25.
 */

public class ExpertRecLayoutView extends LinearLayout {

    private Context mContext;
    private TextView tv_tutor_title, tv_tutor_all,tv_audio_more;
    private ListView lv_home_expert_rec;
    private LinearLayout ll_expert_root;
    private RelativeLayout rl_tutor_all;
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

    private void init(final Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.home_expert_recommend_layout, this);
        ll_expert_root = (LinearLayout) findViewById(R.id.ll_expert_root);
        tv_tutor_title = (TextView) findViewById(R.id.tv_tutor_title);
        tv_tutor_all = (TextView) findViewById(R.id.tv_tutor_all);
        rl_tutor_all = (RelativeLayout) findViewById(R.id.rl_tutor_all);
        lv_home_expert_rec = (ListView) findViewById(R.id.lv_home_expert_rec);
//        tv_audio_more = (TextView)findViewById(R.id.tv_audio_more);
        ll_expert_root.setVisibility(GONE);
        lv_home_expert_rec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                boolean isbuy = mList.get(position).getIsbuy();
                String tutoruid = mList.get(position).getUid();
                String columnid = mList.get(position).getColumnid();
//                Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
//                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ExperDetailsFragment.class);
//                intent.putExtra(ExperDetailsFragment.KEY_DEFAULT_TUTORUID, tutoruid);
//                Log.e("TAG", "首页音频跳转url=" + mList.get(position).getUrl());
//                if (isbuy == true){
//                    Log.e("isbuy","isbuy");
//                    intent.putExtra("isbuy","isbuy");
//                }
//                mContext.startActivity(intent);
//                Log.e("TAG", "id1=" + UmiwiAPI.No_buy_column + columnid);
                if (isbuy) {
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LogicalThinkingFragment.class);
                    intent.putExtra("id", mList.get(position).getColumnid());
                    intent.putExtra("title", mList.get(position).getTitle());
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ColumnDetailsFragment.class);//详情页
                    intent.putExtra("columnurl", UmiwiAPI.No_buy_column + columnid);
                    mContext.startActivity(intent);
                }
            }
        });
        rl_tutor_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                NewHomeRecommendFragment.getRootViewpager().setCurrentItem(1);
                Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AudioSpecialFragment.class);
                mContext.startActivity(intent);
            }
        });
//        tv_audio_more.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
//                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AudioSpecialFragment.class);
//                mContext.startActivity(intent);
//            }
//        });
    }

    public void setData(ArrayList<RecommendBean.RBean.TutorBean> tutorBeen, String tutorTitle, String tutorAll) {

//        tv_tutor_title.setText(tutorTitle);
//        tv_tutor_all.setText(tutorAll);
        mList = tutorBeen;
        if (null == mList || mList.size() == 0)
            return;
        ll_expert_root.setVisibility(VISIBLE);
        mExpertRecAdapter = new ExpertRecAdapter(mContext, mList);
        lv_home_expert_rec.setAdapter(mExpertRecAdapter);

    }
}
