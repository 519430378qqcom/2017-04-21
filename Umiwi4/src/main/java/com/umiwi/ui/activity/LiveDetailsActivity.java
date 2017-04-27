package com.umiwi.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.adapter.LiveRecommendAdapter;
import com.umiwi.ui.beans.LiveDetailsBean;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.main.UmiwiAPI;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

public class LiveDetailsActivity extends Activity {

    @InjectView(R.id.iv_live_imge)
    ImageView ivLiveImge;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tab_title)
    TextView tabTitle;
    @InjectView(R.id.iv_shared)
    ImageView ivShared;
    @InjectView(R.id.record)
    ImageView record;
    @InjectView(R.id.tv_live_title)
    TextView tvLiveTitle;
    @InjectView(R.id.iv_live_status)
    ImageView ivLiveStatus;
    @InjectView(R.id.tv_live_time)
    TextView tvLiveTime;
    @InjectView(R.id.tv_live_parkenum)
    TextView tvLiveParkenum;
    @InjectView(R.id.rcy_liverecommend)
    RecyclerView rcy_liverecommend;
    @InjectView(R.id.btn_comfirm)
    Button btnComfirm;
    private Context mContext;
    private Activity activity;
    private LiveDetailsBean liveDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_details);
        ButterKnife.inject(this);
        mContext = getApplicationContext();
        this.activity = this;
        record.setVisibility(View.GONE);
        initData();
    }
    public static void start(Context context, String id) {
        Intent intent = new Intent(context, LiveDetailsActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }
    private void initData() {
        String id = getIntent().getStringExtra("id");
        GetRequest<LiveDetailsBean> request = new GetRequest<>(
                UmiwiAPI.LIVE_DETAILS+id, GsonParser.class, LiveDetailsBean.class, new AbstractRequest.Listener<LiveDetailsBean>() {
            @Override
            public void onResult(AbstractRequest<LiveDetailsBean> request, LiveDetailsBean liveDetailsBean) {
                if(liveDetailsBean !=null) {
                    liveDetails = liveDetailsBean;
                    LiveDetailsBean.RBean.RecordBean record = liveDetailsBean.getR().getRecord();
                    Glide.with(mContext).load(record.getImage()).into(ivLiveImge);
                    tvLiveTitle.setText(record.getTitle());
                    String status = record.getStatus();
                    switch (status){
                        case "直播中":
                            ivLiveStatus.setVisibility(View.VISIBLE);
                            ivLiveStatus.setImageResource(R.drawable.living);
                            break;
                        case "已结束":
                            ivLiveStatus.setVisibility(View.VISIBLE);
                            ivLiveStatus.setImageResource(R.drawable.already_over);
                            break;
                        default:
                            ivLiveStatus.setVisibility(View.GONE);
                            break;
                    }
                    tvLiveTime.setText(record.getLive_time());
                    tvLiveParkenum.setText(record.getPartakenum()+getString(R.string.join));
                    LiveRecommendAdapter liveRecommendAdapter = new LiveRecommendAdapter(mContext, record.getDescription());
                    rcy_liverecommend.setLayoutManager(new LinearLayoutManager(mContext));
                    rcy_liverecommend.setAdapter(liveRecommendAdapter);
                    if(record.isIsbuy()) {
                        btnComfirm.setText("立即参与");
                        btnComfirm.setBackgroundColor(Color.parseColor("#50b539"));
                    }else {
                        btnComfirm.setText("立即参与"+"("+record.getPrice()+")");
                        btnComfirm.setBackgroundColor(Color.parseColor("#ff7000"));
                    }
                }
            }

            @Override
            public void onError(AbstractRequest<LiveDetailsBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    @OnClick(R.id.iv_back)
    public void onIvBackClicked() {
        finish();
    }

    @OnClick(R.id.iv_shared)
    public void onIvSharedClicked() {
        LiveDetailsBean.RBean.ShareBean share = liveDetails.getR().getShare();
        ShareDialog.getInstance().showDialog(this, share.getSharetitle(),
                share.getSharecontent().get(0).getWord(), share.getShareurl(), share.getShareimg());
    }
    @OnClick(R.id.btn_comfirm)
    public void onbtnComfirmClicked(){
        if("立即参与".equals(btnComfirm.getText().toString())) {
            //进入直播间
        }else {
            //支付
        }
    }
    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }
}
