package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.AttentionAdapter;
import com.umiwi.ui.adapter.DescriptionAdapter;
import com.umiwi.ui.adapter.LastRecordAdapter;
import com.umiwi.ui.beans.ExperDetailsAlbumbean;
import com.umiwi.ui.beans.UmiwiBuyCreateOrderBeans;
import com.umiwi.ui.fragment.home.alreadyshopping.LogicalThinkingFragment;
import com.umiwi.ui.fragment.pay.PayingFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.util.JsonUtil;
import com.umiwi.ui.view.NoScrollListview;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * 详情专栏
 * Created by Administrator on 2017/2/28.
 */

public class DetailsColumnFragment extends BaseConstantFragment {

    @InjectView(R.id.title)
    TextView tv_title;
    @InjectView(R.id.priceinfo)
    TextView priceinfo;
    @InjectView(R.id.shortcontent)
    TextView tv_shortcontent;
    @InjectView(R.id.salenum)
    TextView tv_salenum;
    @InjectView(R.id.description)
    NoScrollListview description;
    @InjectView(R.id.targetuser)
    TextView tv_targetuser;
    @InjectView(R.id.attention_listview)
    NoScrollListview attentionListview;
    @InjectView(R.id.last_record)
    NoScrollListview lastRecord;
    @InjectView(R.id.no_data)
    TextView noData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exper_details_column_layout, null);
        String albumurl = ExperDetailsFragment.tcolumnurl;
        ExperDetailsFragment.tv_more.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(albumurl)) {
            getInfos(albumurl);
        }
        ButterKnife.inject(this, view);
        ExperDetailsFragment.setOnScrollListener(new ExperDetailsFragment.OnScrollListener() {
            @Override
            public void IsColumnbottom() {

            }
        });

        return view;
    }

    private void getInfos(String albumurl) {
        OkHttpUtils.get().url(albumurl).build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {
                Log.e("data", "详情专栏请求数据失败");
            }

            @Override
            public void onSucess(String data) {
                Log.e("data", "详情专栏请求数据成功 :" + data);
                if (data != null) {
                    noData.setVisibility(View.GONE);

                    final ExperDetailsAlbumbean experDetailsAlbumbean = JsonUtil.json2Bean(data, ExperDetailsAlbumbean.class);
                    if (experDetailsAlbumbean != null) {
                        fillData(experDetailsAlbumbean);

                        final String id = experDetailsAlbumbean.getId();
                        if (experDetailsAlbumbean.isIsbuy()) {
                            ExperDetailsFragment.subscriber.setText("已订阅");
                            ExperDetailsFragment.subscriber.setEnabled(false);
                        } else {
                            ExperDetailsFragment.subscriber.setEnabled(true);
                            ExperDetailsFragment.subscriber.setText(String.format("订阅:  %s元/年", experDetailsAlbumbean.getPrice()));
                        }
                        ExperDetailsFragment.subscriber.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getSubscriber(id);
                            }
                        });

                        ExperDetailsFragment.free_read.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LogicalThinkingFragment.class);
                                intent.putExtra("id", experDetailsAlbumbean.getId());
                                startActivity(intent);
                            }
                        });
                    }
                }else {
                    noData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 获取订阅payurl
     */
    private void getSubscriber(String id) {
        String url = null;
        url = String.format(UmiwiAPI.CREATE_SUBSCRIBER_ORDERID, "json", id);
        GetRequest<UmiwiBuyCreateOrderBeans> request = new GetRequest<UmiwiBuyCreateOrderBeans>(
                url, GsonParser.class,
                UmiwiBuyCreateOrderBeans.class,
                subscriberListener);
        request.go();
    }

    private AbstractRequest.Listener<UmiwiBuyCreateOrderBeans> subscriberListener = new AbstractRequest.Listener<UmiwiBuyCreateOrderBeans>() {
        @Override
        public void onResult(AbstractRequest<UmiwiBuyCreateOrderBeans> request, UmiwiBuyCreateOrderBeans umiwiBuyCreateOrderBeans) {
            String payurl = umiwiBuyCreateOrderBeans.getR().getPayurl();
            subscriberBuyDialog(payurl);
        }

        @Override
        public void onError(AbstractRequest<UmiwiBuyCreateOrderBeans> requet, int statusCode, String body) {

        }
    };

    /**
     * 跳转到购买界面
     *
     * @param payurl
     */
    public void subscriberBuyDialog(String payurl) {
        Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayingFragment.class);
        i.putExtra(PayingFragment.KEY_PAY_URL, payurl);
        startActivity(i);
        getActivity().finish();
    }


    private void fillData(ExperDetailsAlbumbean experDetailsAlbumbean) {
        String title = experDetailsAlbumbean.getTitle();
        String shortcontent = experDetailsAlbumbean.getShortcontent();
        List<ExperDetailsAlbumbean.ContentBean> content = experDetailsAlbumbean.getContent();
        List<ExperDetailsAlbumbean.AttentionBean> attention = experDetailsAlbumbean.getAttention();
        String updatedescription = experDetailsAlbumbean.getUpdatedescription();
        String salenum = experDetailsAlbumbean.getSalenum();
        List<ExperDetailsAlbumbean.LastRecordBean> last_record = experDetailsAlbumbean.getLast_record();
        String targetuser = experDetailsAlbumbean.getTargetuser();
        priceinfo.setText(experDetailsAlbumbean.getPriceinfo());
        if (title != null) {
            tv_title.setText(title);
        }
        if (shortcontent != null) {
            tv_shortcontent.setText(shortcontent);
        }
        if (content != null && content.size() > 0) {
            description.setAdapter(new DescriptionAdapter(getActivity(), content));
        }
        if (targetuser != null) {
            tv_targetuser.setText(targetuser);
        }
        if (salenum != null) {
            tv_salenum.setText(salenum);
        }
        if (attention != null && attention.size() > 0) {
            attentionListview.setAdapter(new AttentionAdapter(getActivity(), attention));
        }

        if (last_record != null && last_record.size() > 0) {
            lastRecord.setAdapter(new LastRecordAdapter(getActivity(), last_record));
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
