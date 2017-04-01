package com.umiwi.ui.fragment.home.recommend.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.HotVideoAdapter;
import com.umiwi.ui.beans.updatebeans.HotVideoChangeBean;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.main.UmiwiAPI;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * Created by Administrator on 2017/3/23.
 */

public class HotVideoLayout extends LinearLayout {
    private LinearLayout ll_hotvideo_root;
    private TextView title_type_textview, title_huan;
    private Context mContext;
    private ListViewInScroll lv_hot_video;
    private HotVideoAdapter hotVideoAdapter;
    private int currentpage = 1;
    private int totalpage = 1;
    private ArrayList<RecommendBean.RBean.HotVideoBean.HotVideoRecordBean> mList;

    public HotVideoLayout(Context context) {
        super(context);
        initLayout(context);
    }

    public HotVideoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    private void initLayout(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.hot_video_layout, this);
        ll_hotvideo_root = (LinearLayout) findViewById(R.id.ll_hotvideo_root);
        title_type_textview = (TextView) findViewById(R.id.title_type_textview);
        title_huan = (TextView) findViewById(R.id.title_huan);
        lv_hot_video = (ListViewInScroll) findViewById(R.id.lv_hot_video);

        ll_hotvideo_root.setVisibility(GONE);
    }

    public void setData(RecommendBean.RBean.HotVideoBean hotvideo) {
        totalpage = hotvideo.getPage().getTotalpage();
        mList = hotvideo.getRecord();
        if (null == mList || mList.size() == 0)
            return;
        ll_hotvideo_root.setVisibility(VISIBLE);
        hotVideoAdapter = new HotVideoAdapter(mContext, mList);
        lv_hot_video.setAdapter(hotVideoAdapter);
        lv_hot_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, mList.get(i).getDetailurl());
                mContext.startActivity(intent);
            }
        });
        //换一批
        title_huan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getChangeData();
            }
        });
    }

    private void getChangeData() {
        if (currentpage >= totalpage) {
            currentpage = 1;
        } else {
            currentpage = currentpage + 1;
        }
        String url = String.format(UmiwiAPI.UMIWI_HOTVIDEO_HUAN,currentpage);
        GetRequest<HotVideoChangeBean> request = new GetRequest<HotVideoChangeBean>(url, GsonParser.class, HotVideoChangeBean.class, new AbstractRequest.Listener<HotVideoChangeBean>() {
            @Override
            public void onResult(AbstractRequest<HotVideoChangeBean> request, HotVideoChangeBean hotVideoChangeBean) {
                totalpage = hotVideoChangeBean.getR().getPage().getTotalpage();
                mList.clear();
                mList.addAll(hotVideoChangeBean.getR().getRecord());
                hotVideoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(AbstractRequest<HotVideoChangeBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }
}
