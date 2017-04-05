package com.umiwi.ui.fragment.home.recommend.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.updateadapter.LbumListAdapter;
import com.umiwi.ui.beans.updatebeans.RecommendBean;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/4/1 0001.
 */

public class lbumListLayoutview extends LinearLayout {

    private TextView tv_tutor_title, tv_tutor_all,title_huan;
    private ListView lv_home_expert_rec;
    private LinearLayout ll_expert_root;
    private RelativeLayout rl_tutor_all;
    private ArrayList<RecommendBean.RBean.AlbumListBean.AlbumListRecord> mList;
    private Context mContext;
    private int currentpage = 1;
    private int totalpage = 1;
    private LbumListAdapter lbumListAdapter;
    public lbumListLayoutview(Context context) {
        super(context);
        initView(context);
    }

    public lbumListLayoutview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.lbumlist_layout,null);
        ll_expert_root = (LinearLayout) findViewById(R.id.ll_expert_root);
        tv_tutor_title = (TextView) findViewById(R.id.tv_tutor_title);
        tv_tutor_all = (TextView) findViewById(R.id.tv_tutor_all);
        rl_tutor_all = (RelativeLayout) findViewById(R.id.rl_tutor_all);
        lv_home_expert_rec = (ListView) findViewById(R.id.lv_home_expert_rec);
        title_huan = (TextView)findViewById(R.id.title_huan);

        ll_expert_root.setVisibility(GONE);


    }


    public void setData(RecommendBean.RBean.AlbumListBean albumlist) {
        totalpage = albumlist.getPage().getTotalpage();
        mList = albumlist.getRecord();


    }
}
