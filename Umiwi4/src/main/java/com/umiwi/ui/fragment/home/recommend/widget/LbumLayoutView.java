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
import com.umiwi.ui.adapter.updateadapter.LbumListAdapter;
import com.umiwi.ui.beans.updatebeans.LbumListChangeBean;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.fragment.AudioSpecialDetailFragment;
import com.umiwi.ui.fragment.LbumListFragment;
import com.umiwi.ui.fragment.VideoSpecialDetailFragment;
import com.umiwi.ui.main.UmiwiAPI;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class LbumLayoutView extends LinearLayout {
    private TextView tv_tutor_title, tv_tutor_all,title_huan;
    private ListView lv_home_expert_rec;
    private LinearLayout ll_expert_root;
    private RelativeLayout rl_tutor_all;
    private ArrayList<RecommendBean.RBean.AlbumListBean.AlbumListRecord> mList;
    private int currentpage = 1;
    private int totalpage = 1;

    private LbumListAdapter lbumListAdapter;
    private Context mContext;


    public LbumLayoutView(Context context) {
        super(context);
        initView(context);
    }

    public LbumLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.lbumlist_layout,this);
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
        if (null == mList || mList.size() == 0) {
            return;
        }
        ll_expert_root.setVisibility(VISIBLE);
        lbumListAdapter = new LbumListAdapter(mContext, mList);
        lv_home_expert_rec.setAdapter(lbumListAdapter);

        lv_home_expert_rec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String type = mList.get(position).getType();
                String typeId = mList.get(position).getId();
                if ("音频".equals(type)) {
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AudioSpecialDetailFragment.class);
                    intent.putExtra("typeId", typeId);
                    mContext.startActivity(intent);
                } else {
                    String detailurl = mList.get(position).getDetailurl();
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VideoSpecialDetailFragment.class);
                    intent.putExtra("detailurl", detailurl);
                    mContext.startActivity(intent);
                }

            }
        });
        title_huan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getChangeData();
//                Toast.makeText(mContext, "换一批", Toast.LENGTH_SHORT).show();
            }
        });
        //查看全部
        tv_tutor_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LbumListFragment.class);
                mContext.startActivity(intent);
            }
        });

    }
    //换一批
    private void getChangeData() {
        if (currentpage >= totalpage) {
            currentpage = 1;
        } else {
            currentpage = currentpage + 1;
        }
        String url = String.format(UmiwiAPI.UMIWI_CHOICENESS,currentpage);
        GetRequest<LbumListChangeBean> request = new GetRequest<LbumListChangeBean>(url, GsonParser.class,LbumListChangeBean.class,lbumlistchange);
        request.go();
    }
    private AbstractRequest.Listener<LbumListChangeBean> lbumlistchange = new AbstractRequest.Listener<LbumListChangeBean>() {
        @Override
        public void onResult(AbstractRequest<LbumListChangeBean> request, LbumListChangeBean lbumListChangeBean) {
            totalpage = lbumListChangeBean.getR().getPage().getTotalpage();
            mList.clear();
            mList.addAll(lbumListChangeBean.getR().getRecord());
            lbumListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onError(AbstractRequest<LbumListChangeBean> requet, int statusCode, String body) {

        }
    };
}
