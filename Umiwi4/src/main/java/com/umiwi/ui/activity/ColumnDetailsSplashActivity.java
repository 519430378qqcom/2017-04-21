package com.umiwi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.adapter.ColumnAttentionAdapter;
import com.umiwi.ui.adapter.ColumnDetailsAdapter;
import com.umiwi.ui.adapter.ColumnRecordAdapter;
import com.umiwi.ui.beans.ColumnDetailsBean;
import com.umiwi.ui.beans.UmiwiBuyCreateOrderBeans;
import com.umiwi.ui.beans.updatebeans.AudioSpecialDetailsBean;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.fragment.home.alreadyshopping.LogicalThinkingFragment;
import com.umiwi.ui.fragment.pay.PayingFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.LoginUtil;
import com.umiwi.ui.view.NoScrollListview;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.PreferenceUtils;


public class ColumnDetailsSplashActivity extends AppCompatActivity {
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
    private ColumnDetailsBean columnDetailsBean;
    private AudioSpecialDetailsBean.RAudioSpecialDetails details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column_details_splash);
        columnurl = "http://i.v.youmi.cn/tutorcolumn/detailApi?id=28";
        initView();
        getData();
    }

    private void getData() {
        GetRequest<AudioSpecialDetailsBean> request = new GetRequest<AudioSpecialDetailsBean>(columnurl, GsonParser.class, AudioSpecialDetailsBean.class, new AbstractRequest.Listener<AudioSpecialDetailsBean>() {
            @Override
            public void onResult(AbstractRequest<AudioSpecialDetailsBean> request, AudioSpecialDetailsBean audioSpecialDetailsBean) {
                details = audioSpecialDetailsBean.getR();
                ArrayList<AudioSpecialDetailsBean.RAudioSpecialDetails.ContentWord> content = details.getContent();//详情简介
                description.setAdapter(new ColumnDetailsAdapter(ColumnDetailsSplashActivity.this,content));
                targetuser.setText(details.getTargetuser());
                ArrayList<AudioSpecialDetailsBean.RAudioSpecialDetails.AttentionWord> attention = details.getAttention();
                attention_listview.setAdapter(new ColumnAttentionAdapter(ColumnDetailsSplashActivity.this, details.getAttention()));
                ArrayList<AudioSpecialDetailsBean.RAudioSpecialDetails.LastRecord> last_record = details.getLast_record();
                ColumnDetailsSplashActivity.this.last_record.setAdapter(new ColumnRecordAdapter(ColumnDetailsSplashActivity.this, details.getLast_record()));
                title.setText(details.getTitle());
                priceinfo.setText(details.getPriceinfo());
                shortcontent.setText(details.getShortcontent());

                salenum.setText(details.getSalenum());

                Glide.with(getApplicationContext()).load(details.getImage()).into(iv_image);
                tv_name.setText(details.getTutor_name());
                tv_title.setText(details.getTutor_title());

                Log.e("TAG", "columnDetailsBean.isIsbuy=" + details.isbuy());
                if (details.isbuy()){
                    tv_free_read.setText("进入专栏");
                    tv_prize.setText("已订阅");
                    tv_prize.setEnabled(false);
                }else {
                    tv_free_read.setText("免费阅读");
                    tv_prize.setEnabled(true);
                    tv_prize.setText(String.format("订阅:  %s元/年", details.getPrice()));
                }
                tv_prize.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!YoumiRoomUserManager.getInstance().isLogin()) {
                            LoginUtil.getInstance().showLoginView(getApplication());
                            return;
                        }
                        getSubscriber(details.getId());
                    }
                });

            }

            @Override
            public void onError(AbstractRequest<AudioSpecialDetailsBean> requet, int statusCode, String body) {

            }
        });
        request.go();
//        OkHttpUtils.get().url(columnurl).build().execute(new CustomStringCallBack() {
//            @Override
//            public void onFaild() {
//                Log.e("data","失败");
//            }
//
//            @Override
//            public void onSucess(String data) {
//                Log.e("data","请求数据成功"+data);
//                if (!TextUtils.isEmpty(data)){
//                    columnDetailsBean = JsonUtil.json2Bean(data, ColumnDetailsBean.class);
//                    final List<ColumnDetailsBean.ContentBean> content = columnDetailsBean.getContent();//详情简介
//
//                    description.setAdapter(new ColumnDetailsAdapter(ColumnDetailsSplashActivity.this,content));
//                    targetuser.setText(columnDetailsBean.getTargetuser());
//                    attention_listview.setAdapter(new ColumnAttentionAdapter(ColumnDetailsSplashActivity.this, columnDetailsBean.getAttention()));
//                    last_record.setAdapter(new ColumnRecordAdapter(ColumnDetailsSplashActivity.this, columnDetailsBean.getLast_record()));
//
//                    title.setText(columnDetailsBean.getTitle());
//                    priceinfo.setText(columnDetailsBean.getPriceinfo());
//                    shortcontent.setText(columnDetailsBean.getShortcontent());
//
//                    salenum.setText(columnDetailsBean.getSalenum());
//
//                    Glide.with(getApplicationContext()).load(columnDetailsBean.getImage()).into(iv_image);
//                    tv_name.setText(columnDetailsBean.getTutor_name());
//                    tv_title.setText(columnDetailsBean.getTutor_title());
//
//                    Log.e("TAG", "columnDetailsBean.isIsbuy=" + columnDetailsBean.isIsbuy());
//                    if (columnDetailsBean.isIsbuy()){
//                        tv_free_read.setText("进入专栏");
//                        tv_prize.setText("已订阅");
//                        tv_prize.setEnabled(false);
//                    }else {
//                        tv_free_read.setText("免费阅读");
//                        tv_prize.setEnabled(true);
//                        tv_prize.setText(String.format("订阅:  %s元/年", columnDetailsBean.getPrice()));
//                    }
//                    tv_prize.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (!YoumiRoomUserManager.getInstance().isLogin()) {
//                                LoginUtil.getInstance().showLoginView(getApplication());
//                                return;
//                            }
//                            getSubscriber(columnDetailsBean.getId());
//                        }
//                    });
//                }
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
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
            Log.e("TAG", "payurl==" + payurl);
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
        Intent i = new Intent(getApplicationContext(), UmiwiContainerActivity.class);
        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayingFragment.class);
        i.putExtra(PayingFragment.KEY_PAY_URL, payurl);
        startActivity(i);
//        finish();
    }

    private void initView() {
        targetuser = (TextView)findViewById(R.id.targetuser);
        title = (TextView)findViewById(R.id.title);
        priceinfo = (TextView)findViewById(R.id.priceinfo);
        shortcontent = (TextView)findViewById(R.id.shortcontent);
        salenum = (TextView)findViewById(R.id.salenum);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_prize = (TextView)findViewById(R.id.tv_prize);
        tv_free_read = (TextView)findViewById(R.id.tv_free_read);
        iv_image = (ImageView)findViewById(R.id.iv_image);
        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_shared = (ImageView)findViewById(R.id.iv_shared);
        description = (NoScrollListview)findViewById(R.id.description);
        attention_listview = (NoScrollListview)findViewById(R.id.attention_listview);
        last_record = (NoScrollListview)findViewById(R.id.last_record);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceUtils.setPrefBoolean(ColumnDetailsSplashActivity.this, "isCanShowGift", true);
//                Intent intent = new Intent(ColumnDetailsSplashActivity.this, HomeMainActivity.class);
//                startActivity(intent);
                ColumnDetailsSplashActivity.this.finish();
            }
        });
        tv_free_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), "免费试读", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LogicalThinkingFragment.class);
                intent.putExtra("id",details.getId());
                intent.putExtra("title",details.getTitle());
                startActivity(intent);
            }
        });
        iv_shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareDialog.getInstance().showDialog(ColumnDetailsSplashActivity.this, details.getSharetitle(),
                        details.getSharecontent(),details.getShareurl(), details.getShareimg());
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            PreferenceUtils.setPrefBoolean(this, "isCanShowGift", true);
//            Intent intent = new Intent(ColumnDetailsSplashActivity.this, HomeMainActivity.class);
//            startActivity(intent);
            this.finish();

        }
        return super.onKeyDown(keyCode, event);
    }
}
