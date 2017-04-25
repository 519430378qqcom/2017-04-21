package com.umiwi.ui.fragment.alreadyboughtfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.adapter.ColumnAttentionAdapter;
import com.umiwi.ui.adapter.ColumnDetailsAdapter;
import com.umiwi.ui.adapter.LogicalThinkingAdapter;
import com.umiwi.ui.beans.updatebeans.AttemptBean;
import com.umiwi.ui.beans.updatebeans.AudioSpecialDetailsBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.view.NoScrollListview;
import com.umiwi.ui.view.ScrollChangeScrollView;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * Created by Administrator on 2017/4/25 0025.
 * 已购专栏详情页面
 */

public class BuyColumnDetailsFragment extends BaseConstantFragment {
    ScrollChangeScrollView scrollview;
    ImageView iv_image;
    ImageView iv_fold_down;
    TextView tv_name;
    TextView tv_title;
    LinearLayout ll_orderby;
    ImageView iv_sort;
    TextView orderby;
    TextView update_count;
    TextView tv_buynumber;
//    @InjectView(R.id.lv_buycolumn)
//    NoScrollListview lv_buycolumn;
    LinearLayout ll_listvisable;
    LinearLayout ll_column_details;
    NoScrollListview description;
    TextView targetuser;
    NoScrollListview attention_listview;
    View rl_background;
    ImageView iv_back;
    TextView tab_title;
    ImageView iv_shared;
    ImageView record;
    RelativeLayout rl_bottom_up;
    ImageView iv_up;

    private String orderbyId = "new";
    private String id;//
    private ArrayList<AttemptBean.RAttenmpInfo.RecordsBean> recordList = new ArrayList<>();
    private LogicalThinkingAdapter logicalThinkingAdapter;
    private AudioSpecialDetailsBean.RAudioSpecialDetails details;
    private String columnurl;
    private NoScrollListview lv_buycolumn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_column_details,null);
//        ButterKnife.inject(this,view);

        id = getActivity().getIntent().getStringExtra("id");
        columnurl = getActivity().getIntent().getStringExtra("columnurl");

        initView(view);
        getDetailsData();
        getListData();
        return view;
    }

    private void initView(View view) {
        scrollview = (ScrollChangeScrollView) view.findViewById(R.id.scrollview);
        scrollview.scrollTo(0,0);
        lv_buycolumn = (NoScrollListview) view.findViewById(R.id.lv_buycolumn);
        iv_image = (ImageView) view.findViewById(R.id.iv_image);
        iv_fold_down = (ImageView) view.findViewById(R.id.iv_fold_down);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        ll_orderby = (LinearLayout) view.findViewById(R.id.ll_orderby);
        iv_sort = (ImageView) view.findViewById(R.id.iv_sort);
        orderby = (TextView) view.findViewById(R.id.orderby);
        update_count = (TextView) view.findViewById(R.id.update_count);
        tv_buynumber = (TextView) view.findViewById(R.id.tv_buynumber);
        ll_listvisable = (LinearLayout) view.findViewById(R.id.ll_listvisable);
        ll_column_details = (LinearLayout) view.findViewById(R.id.ll_column_details);
        description = (NoScrollListview) view.findViewById(R.id.description);
        targetuser = (TextView) view.findViewById(R.id.targetuser);
        attention_listview = (NoScrollListview) view.findViewById(R.id.attention_listview);
        rl_background = view.findViewById(R.id.rl_background);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tab_title = (TextView) view.findViewById(R.id.tab_title);
        iv_shared = (ImageView) view.findViewById(R.id.iv_shared);
        record = (ImageView) view.findViewById(R.id.record);
        iv_up = (ImageView) view.findViewById(R.id.iv_up);
        rl_bottom_up = (RelativeLayout) view.findViewById(R.id.rl_bottom_up);
        lv_buycolumn.setFocusable(false);
    }

    //获取专栏简介数据
    private void getDetailsData() {
        String url = String.format(UmiwiAPI.No_buy_column,25);
        Log.e("TAG", "已购买的专栏url="  + url);
        GetRequest<AudioSpecialDetailsBean> request = new GetRequest<AudioSpecialDetailsBean>(url, GsonParser.class, AudioSpecialDetailsBean.class, new AbstractRequest.Listener<AudioSpecialDetailsBean>() {
            @Override
            public void onResult(AbstractRequest<AudioSpecialDetailsBean> request, AudioSpecialDetailsBean audioSpecialDetailsBean) {
                details = audioSpecialDetailsBean.getR();
                ArrayList<AudioSpecialDetailsBean.RAudioSpecialDetails.ContentWord> content = details.getContent();//详情简介
                description.setAdapter(new ColumnDetailsAdapter(getActivity(), content));
                targetuser.setText(details.getTargetuser());

                attention_listview.setAdapter(new ColumnAttentionAdapter(getActivity(), details.getAttention()));
                tab_title.setText(details.getTitle());
                Glide.with(getActivity()).load(details.getImage()).into(iv_image);
                tv_name.setText(details.getTitle());
                tv_title.setText(details.getShortcontent());


            }

            @Override
            public void onError(AbstractRequest<AudioSpecialDetailsBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }
    //获取列表数据
    private void getListData() {
        String url = String.format(UmiwiAPI.Logincal_thinking, id, orderbyId);
        Log.e("TAG", "已购买的专栏url=" + url);
        GetRequest<AttemptBean> request = new GetRequest<AttemptBean>(url, GsonParser.class, AttemptBean.class, new AbstractRequest.Listener<AttemptBean>() {
            @Override
            public void onResult(AbstractRequest<AttemptBean> request, AttemptBean attemptBean) {
                ArrayList<AttemptBean.RAttenmpInfo.RecordsBean> recordsBeen = attemptBean.getR().getRecord();
                if(recordsBeen != null) {
//                    Log.e("TAG", "recordsBean=" + recordsBeen.toString());
                    recordList.clear();
                    update_count.setText(String.format("已更新%s条", recordsBeen.size()));
//                    Log.e("TAG", "recordsBeen.size()=" + recordsBeen.size());
                    recordList.addAll(recordsBeen);
                    logicalThinkingAdapter = new LogicalThinkingAdapter(getActivity(), recordList);
                    lv_buycolumn.setAdapter(logicalThinkingAdapter);

                }

            }

            @Override
            public void onError(AbstractRequest<AttemptBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        ButterKnife.reset(this);
    }
}
