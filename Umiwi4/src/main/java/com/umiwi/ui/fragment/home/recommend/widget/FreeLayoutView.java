package com.umiwi.ui.fragment.home.recommend.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.NewfreeAdapterV2;
import com.umiwi.ui.beans.updatebeans.FreeRecordBean;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

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

    private String mHuanUrl;
    private int currentpage = 1;
    private int totalpage = 1;

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
//        lv_new_free.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mList.get(position).getId();
//                // TODO 分享测试
//                NewShareDialog.getInstance().showDialog(mContext, "gps", mList.get(position).getTitle()
//                        , mList.get(position).getUrl(), "www.gpsi_img.com");
//            }
//        });
        ll_free_root.setVisibility(GONE);
    }

    public void setData(final RecommendBean.RBean.FreeBean freeBean, String titleFree, String titleHuan, String huanUrl) {
        totalpage = freeBean.getPage().getTotalpage();
//        title_type_textview.setText(titleFree);
        title_huan.setText(titleHuan);
        mList = freeBean.getRecord();
        mHuanUrl = huanUrl;
        if (null == mList || mList.size() == 0)
            return;
        ll_free_root.setVisibility(VISIBLE);
        mNewfreeAdapterV2 = new NewfreeAdapterV2(mContext, mList);
        lv_new_free.setAdapter(mNewfreeAdapterV2);

        lv_new_free.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mList.get(position).getType().equals("video")) {
//                    Log.e("TAG", "这是免费视频可以观看");
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                    intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, mList.get(position).getUrl());
                    mContext.startActivity(intent);
                } else if (mList.get(position).getType().equals("audio")) {
                    //TODO
//                    Toast.makeText(mContext, "敬请期待", Toast.LENGTH_SHORT).show();
//                    Log.e("TAG", "freeBean.get(po)=" + mList.get(position).getUrl());
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
                    intent.putExtra(VoiceDetailsFragment.KEY_DETAILURL, mList.get(position).getUrl());
                    mContext.startActivity(intent);
//                    Toast.makeText(mContext, "敬请期待", Toast.LENGTH_SHORT).show();

                }
            }
        });
        title_huan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private void getData() {
        if (currentpage >= totalpage) {
            currentpage = 1;
        } else {
            currentpage = currentpage + 1;
        }
        GetRequest<FreeRecordBean> request = new GetRequest<>(
                String.format(mHuanUrl + "?p=%s", currentpage), GsonParser.class, FreeRecordBean.class, huanListener);
        request.go();
    }

    private AbstractRequest.Listener<FreeRecordBean> huanListener = new AbstractRequest.Listener<FreeRecordBean>() {

        @Override
        public void onResult(AbstractRequest<FreeRecordBean> request, FreeRecordBean t) {
            if (null != t && null != t.getR()) {
//                currentpage = t.getR().getPage().getCurrentpage();
                totalpage = t.getR().getPage().getTotalpage();
                mList.clear();
                mList.addAll(t.getR().getRecord());
                mNewfreeAdapterV2.notifyDataSetChanged();
            }

        }

        @SuppressWarnings("deprecation")
        @Override
        public void onError(AbstractRequest<FreeRecordBean> requet, int statusCode, String body) {

        }
    };

}
