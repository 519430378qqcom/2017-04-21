package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.AttentionAdapter;
import com.umiwi.ui.adapter.DescriptionAdapter;
import com.umiwi.ui.adapter.LastRecordAdapter;
import com.umiwi.ui.beans.UmiwiBuyCreateOrderBeans;
import com.umiwi.ui.beans.updatebeans.DetailsCoumnBean;
import com.umiwi.ui.fragment.home.alreadyshopping.LogicalThinkingFragment;
import com.umiwi.ui.fragment.pay.PayingFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.LoginUtil;
import com.umiwi.ui.view.NoScrollListview;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ToastU;

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
    @InjectView(R.id.ll_addview)
    LinearLayout llAddview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exper_details_column_layout, null);
        ButterKnife.inject(this, view);

        String albumurl = ExperDetailsFragment.tcolumnurl;
        ExperDetailsFragment.tv_more.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(albumurl)) {
            ExperDetailsFragment.yuedu.setVisibility(View.VISIBLE);
            llAddview.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
            getInfos(albumurl);
        } else {
            ExperDetailsFragment.yuedu.setVisibility(View.GONE);
            llAddview.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }
        ExperDetailsFragment.setOnScrollListener(new ExperDetailsFragment.OnScrollListener() {
            @Override
            public void IsColumnbottom() {

            }
        });
        attentionListview.setFocusable(false);
        return view;
    }

    @Override
    public void onResume() {
        Log.e("onResume","onreseum");
        super.onResume();
    }

    private void getInfos(String albumurl) {
        GetRequest<DetailsCoumnBean> request = new GetRequest<DetailsCoumnBean>(
                albumurl, GsonParser.class,
                DetailsCoumnBean.class, new AbstractRequest.Listener<DetailsCoumnBean>() {
            @Override
            public void onResult(AbstractRequest<DetailsCoumnBean> request, DetailsCoumnBean detailsCoumnBean) {
                final DetailsCoumnBean.RDetailsInfo r = detailsCoumnBean.getR();
                if (r!=null){
                    fillData(r);

                    final String id = r.getId();
                    if (r.getIsbuy()) {
                        ExperDetailsFragment.subscriber.setText("已订阅");
                        ExperDetailsFragment.free_read.setText("进入专栏");
                        ExperDetailsFragment.subscriber.setEnabled(false);
                    } else {
                        ExperDetailsFragment.free_read.setText("免费阅读");
                        ExperDetailsFragment.subscriber.setEnabled(true);
                        ExperDetailsFragment.subscriber.setText(String.format("订阅:  %s元/年", r.getPrice()));
                    }


                    ExperDetailsFragment.subscriber.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(r.getId())) {
                                ToastU.showShort(getActivity(), "行家Id不存在");
                                return;
                            }
                            if (!YoumiRoomUserManager.getInstance().isLogin()) {
                                LoginUtil.getInstance().showLoginView(getActivity());
                                return;
                            }
                            getSubscriber(id);
                        }
                    });

                    ExperDetailsFragment.free_read.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(r.getId())) {
                                ToastU.showShort(getActivity(), "行家Id不存在");
                                return;
                            }
                            Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                            intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LogicalThinkingFragment.class);
                            intent.putExtra("id", r.getId());
                            intent.putExtra("title", r.getTitle());
                            startActivity(intent);
                        }
                    });
                }

            }

            @Override
            public void onError(AbstractRequest<DetailsCoumnBean> requet, int statusCode, String body) {
                Log.e("info","flase"+body);

            }
        });
        request.go();
//        OkHttpUtils.get().url(albumurl).build().execute(new CustomStringCallBack() {
//            @Override
//            public void onFaild() {
//                Log.e("data", "详情专栏请求数据失败");
//            }
//
//            @Override
//            public void onSucess(String data) {
//                Log.e("data", "详情专栏请求数据成功 :" + data);
//                if (data != null) {
//
//                    final ExperDetailsAlbumbean experDetailsAlbumbean = JsonUtil.json2Bean(data, ExperDetailsAlbumbean.class);
//                    if (experDetailsAlbumbean != null) {
//                        fillData(experDetailsAlbumbean);
//
//                        final String id = experDetailsAlbumbean.getId();
//                        if (experDetailsAlbumbean.isIsbuy()) {
//                            ExperDetailsFragment.subscriber.setText("已订阅");
//                            ExperDetailsFragment.subscriber.setEnabled(false);
//                        } else {
//                            ExperDetailsFragment.subscriber.setEnabled(true);
//                            ExperDetailsFragment.subscriber.setText(String.format("订阅:  %s元/年", experDetailsAlbumbean.getPrice()));
//                        }
//
//
//                        ExperDetailsFragment.subscriber.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (TextUtils.isEmpty(experDetailsAlbumbean.getId())) {
//                                    ToastU.showShort(getActivity(), "行家Id不存在");
//                                    return;
//                                }
//                                if (!YoumiRoomUserManager.getInstance().isLogin()) {
//                                    LoginUtil.getInstance().showLoginView(getActivity());
//                                    return;
//                                }
//                                getSubscriber(id);
//                            }
//                        });
//
//                        ExperDetailsFragment.free_read.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (TextUtils.isEmpty(experDetailsAlbumbean.getId())) {
//                                    ToastU.showShort(getActivity(), "行家Id不存在");
//                                    return;
//                                }
//                                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
//                                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LogicalThinkingFragment.class);
//                                intent.putExtra("id", experDetailsAlbumbean.getId());
//                                intent.putExtra("title", experDetailsAlbumbean.getTitle());
//                                startActivity(intent);
//                            }
//                        });
//                    }
//                } else {
//                }
//            }
//        });
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


    private void fillData(DetailsCoumnBean.RDetailsInfo experDetailsAlbumbean) {
        String title = experDetailsAlbumbean.getTitle();
        String shortcontent = experDetailsAlbumbean.getShortcontent();
        ArrayList<DetailsCoumnBean.RDetailsInfo.ContentBean> content = experDetailsAlbumbean.getContent();
        ArrayList<DetailsCoumnBean.RDetailsInfo.Attentioninfo> attention = experDetailsAlbumbean.getAttention();
        String updatedescription = experDetailsAlbumbean.getUpdatedescription();
        String salenum = experDetailsAlbumbean.getSalenum();
        ArrayList<DetailsCoumnBean.RDetailsInfo.LastBean> last_record = experDetailsAlbumbean.getLast_record();
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
