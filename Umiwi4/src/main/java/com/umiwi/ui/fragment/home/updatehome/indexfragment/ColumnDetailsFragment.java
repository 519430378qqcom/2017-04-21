package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.adapter.ColumnAdapter;
import com.umiwi.ui.adapter.ColumnAttentionAdapter;
import com.umiwi.ui.adapter.ColumnDetailsAdapter;
import com.umiwi.ui.adapter.ColumnRecordAdapter;
import com.umiwi.ui.beans.ColumnDetailsBean;
import com.umiwi.ui.beans.updatebeans.HomeCoumnBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.util.JsonUtil;
import com.umiwi.ui.view.NoScrollListview;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

import cn.youmi.framework.view.LoadingFooter;

import static android.view.View.inflate;
import static com.umiwi.ui.R.id.listView;
import static com.umiwi.ui.R.id.targetuser;
import static com.umiwi.ui.main.UmiwiAPI.No_buy_column;
import static u.aly.x.U;

/**
 * Created by Administrator on 2017/3/6.
 */

//专栏详情
public class ColumnDetailsFragment extends BaseConstantFragment {

    private String columnurl;
    private TextView targetuser;
    private TextView title;
    private TextView priceinfo;
    private TextView shortcontent;
    private TextView salenum;
    private TextView tv_name;
    private TextView tv_title;
    private TextView tv_prize;
    private TextView tv_free_read;

    private ImageView iv_image;
    private ImageView iv_back;
    private ImageView iv_shared;

    private NoScrollListview description;
    private NoScrollListview attention_listview;
    private NoScrollListview last_record;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_column_layout, null);

        columnurl = getActivity().getIntent().getStringExtra("columnurl");
        Log.e("data", columnurl);

        initView(view);
        getData();

        return view;
    }

    private void initView(View view) {

        targetuser = (TextView) view.findViewById(R.id.targetuser);
        title = (TextView) view.findViewById(R.id.title);
        priceinfo = (TextView) view.findViewById(R.id.priceinfo);
        shortcontent = (TextView) view.findViewById(R.id.shortcontent);
        salenum = (TextView) view.findViewById(R.id.salenum);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_prize = (TextView) view.findViewById(R.id.tv_prize);
        tv_free_read = (TextView) view.findViewById(R.id.tv_free_read);
        tv_free_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "免费试读", Toast.LENGTH_SHORT).show();
            }
        });

        iv_image = (ImageView) view.findViewById(R.id.iv_image);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        iv_shared = (ImageView) view.findViewById(R.id.iv_shared);
        iv_shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "分享", Toast.LENGTH_SHORT).show();
            }
        });
        tv_title = (TextView) view.findViewById(R.id.tv_title);

        description = (NoScrollListview) view. findViewById(R.id.description);
        attention_listview = (NoScrollListview) view. findViewById(R.id.attention_listview);
        last_record = (NoScrollListview) view. findViewById(R.id.last_record);
    }

    private void getData() {
        OkHttpUtils.get().url(columnurl).build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {
                Log.e("data","失败");
            }

            @Override
            public void onSucess(String data) {
                Log.e("data","请求数据成功"+data);
                if (!TextUtils.isEmpty(data)){
                    ColumnDetailsBean columnDetailsBean = JsonUtil.json2Bean(data, ColumnDetailsBean.class);
                    List<ColumnDetailsBean.ContentBean> content = columnDetailsBean.getContent();//详情简介

                    description.setAdapter(new ColumnDetailsAdapter(getActivity(),content));
                    targetuser.setText(columnDetailsBean.getTargetuser());
                    attention_listview.setAdapter(new ColumnAttentionAdapter(getActivity(),columnDetailsBean.getAttention()));
                    last_record.setAdapter(new ColumnRecordAdapter(getActivity(),columnDetailsBean.getLast_record()));

                    title.setText(columnDetailsBean.getSharetitle());
                    priceinfo.setText(columnDetailsBean.getPriceinfo());
                    shortcontent.setText(columnDetailsBean.getSharecontent());
                    salenum.setText(columnDetailsBean.getSalenum());

                    Glide.with(getActivity()).load(columnDetailsBean.getImage()).into(iv_image);
                    tv_name.setText(columnDetailsBean.getTutor_name());
                    tv_title.setText(columnDetailsBean.getTutor_title());
                    tv_prize.setText("订阅："+columnDetailsBean.getPrice()+"元一年");
                }
            }
        });

    }
}
