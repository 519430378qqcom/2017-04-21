package com.umiwi.ui.fragment.audiolive;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.LiveChatRoomActivity;
import com.umiwi.ui.adapter.LiveRecommendAdapter;
import com.umiwi.ui.beans.LiveDetailsBean;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * Created by Administrator on 2017/4/28.
 */

public class LiveDetailsFragment extends BaseConstantFragment{
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
    public static final String DETAILS_ID = "detailsId";
    private Activity activity;
    private LiveDetailsBean liveDetails;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_details, null);
        ButterKnife.inject(this,view);
        activity = getActivity();
        record.setVisibility(View.GONE);
        initData();
        return view;
    }
    private void initData() {
        String id = activity.getIntent().getStringExtra(DETAILS_ID);
        Log.e("TAG", "liveDetailsBean=" + UmiwiAPI.LIVE_DETAILS+id);
        GetRequest<LiveDetailsBean> request = new GetRequest<LiveDetailsBean>(
                UmiwiAPI.LIVE_DETAILS+id, GsonParser.class, LiveDetailsBean.class, new AbstractRequest.Listener<LiveDetailsBean>() {
            @Override
            public void onResult(AbstractRequest<LiveDetailsBean> request, LiveDetailsBean liveDetailsBean) {

                Log.e("TAG", "liveDetailsBean=" + liveDetailsBean.getR().getRecord().getImage());
                if(liveDetailsBean != null) {
                    liveDetails = liveDetailsBean;
                    LiveDetailsBean.RBean.RecordBean record = liveDetailsBean.getR().getRecord();
                    Glide.with(activity.getApplicationContext()).load(record.getImage()).into(ivLiveImge);
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
                    LiveRecommendAdapter liveRecommendAdapter = new LiveRecommendAdapter(activity, record.getDescription());
                    rcy_liverecommend.setLayoutManager(new LinearLayoutManager(activity));
                    rcy_liverecommend.setAdapter(liveRecommendAdapter);
                    if(record.isIsbuy()) {
                        btnComfirm.setText(R.string.immediately_join);
                        btnComfirm.setBackgroundColor(Color.parseColor("#50b539"));
                    }else {
                        btnComfirm.setText(getString(R.string.immediately_join)+"("+record.getPrice()+")");
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
        activity.finish();
    }

    @OnClick(R.id.iv_shared)
    public void onIvSharedClicked() {
        LiveDetailsBean.RBean.ShareBean share = liveDetails.getR().getShare();
        ShareDialog.getInstance().showDialog(activity, share.getSharetitle(),
                share.getSharecontent().get(0).getWord(), share.getShareurl(), share.getShareimg());
    }
    @OnClick(R.id.btn_comfirm)
    public void onbtnComfirmClicked(){
        if(getString(R.string.immediately_join).equals(btnComfirm.getText().toString())) {
            //进入直播间
        }else {
            //支付
        }
        Intent intent = new Intent(getActivity(), LiveChatRoomActivity.class);
        intent.putExtra(LiveDetailsFragment.DETAILS_ID,liveDetails.getR().getRecord().getId());
        intent.putExtra(LiveChatRoomActivity.ROOM_ID,liveDetails.getR().getRecord().getRoomid());
        getActivity().startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }
}
