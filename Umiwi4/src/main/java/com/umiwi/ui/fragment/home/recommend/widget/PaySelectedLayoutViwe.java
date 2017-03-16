package com.umiwi.ui.fragment.home.recommend.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.beans.updatebeans.ChargeBean;
import com.umiwi.ui.beans.updatebeans.FreeRecordBean;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ImageLoader;

/**
 * 首页-推荐-付费精选
 * Created by ${Gpsi} on 2017/2/27.
 */
public class PaySelectedLayoutViwe extends LinearLayout {

    private Context mContext;
    private LinearLayout ll_pay_selected_root, ll_pay_right_video, ll_pay_selected_audio;
    private TextView tv_title_type, tv_title_tag, tv_pay_video_tag_right, tv_pay_video_tag_left, tv_pay_video_tag_right_1, tv_pay_video_tag_left_1,
            tv_audio_pay_time, tv_audio_pay_context, tv_audio_pay_price;
    private View v_pay_selected_interval;
    private ImageView iv_pay_video, iv_pay_video_1;
    private ImageLoader mImageLoader;
    private int currentpage = 1;
    private int totalpage = 1;
    private String mHuanUrl;
    private String mChargeTitle;
    private String mChargehuan;
    private String mChargehuanUrl;

    public PaySelectedLayoutViwe(Context context) {
        super(context);
        init(context);
    }

    public PaySelectedLayoutViwe(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.home_pay_selected_layout, this);
        ll_pay_selected_root = (LinearLayout) findViewById(R.id.ll_pay_selected_root);
        tv_title_type = (TextView) findViewById(R.id.tv_title_type);
        tv_title_tag = (TextView) findViewById(R.id.tv_title_tag);
        tv_pay_video_tag_right = (TextView) findViewById(R.id.tv_pay_video_tag_right);
        tv_pay_video_tag_left = (TextView) findViewById(R.id.tv_pay_video_tag_left);
        tv_pay_video_tag_right_1 = (TextView) findViewById(R.id.tv_pay_video_tag_right_1);
        tv_pay_video_tag_left_1 = (TextView) findViewById(R.id.tv_pay_video_tag_left_1);
        tv_audio_pay_time = (TextView) findViewById(R.id.tv_audio_pay_time);
        tv_audio_pay_context = (TextView) findViewById(R.id.tv_audio_pay_context);
        tv_audio_pay_price = (TextView) findViewById(R.id.tv_audio_pay_price);

        ll_pay_right_video = (LinearLayout) findViewById(R.id.ll_pay_right_video);
        ll_pay_selected_audio = (LinearLayout) findViewById(R.id.ll_pay_selected_audio);

        iv_pay_video = (ImageView) findViewById(R.id.iv_pay_video);
        iv_pay_video_1 = (ImageView) findViewById(R.id.iv_pay_video_1);


        ll_pay_selected_root.setVisibility(GONE);

        mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
    }

    public void setData(ArrayList<RecommendBean.RBean.ChargeBean.RecordBeanX> recordBeanXes, String chargeTitle, String chargehuan, String chargehuanUrl ,int totalPage) {

        if (null == recordBeanXes || recordBeanXes.size() == 0)
            return;
        mHuanUrl = chargehuanUrl;
        mChargehuan = chargehuan;
        mChargeTitle = chargeTitle;
        mChargehuanUrl = chargehuanUrl;
        totalpage = totalPage;

        tv_title_type.setText(chargeTitle);
        tv_title_tag.setText(chargehuan);
        tv_title_tag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        ll_pay_selected_root.setVisibility(VISIBLE);
        initView(recordBeanXes);

    }

    private void getData() {
        if (currentpage >= totalpage) {
            currentpage = 1;
        } else {
            currentpage = currentpage + 1;
        }
        GetRequest<ChargeBean> request = new GetRequest<>(
                String.format(mHuanUrl + "?p=%s", currentpage), GsonParser.class, ChargeBean.class, huanListener);
        request.go();
    }

    private AbstractRequest.Listener<ChargeBean> huanListener = new AbstractRequest.Listener<ChargeBean>() {

        @Override
        public void onResult(AbstractRequest<ChargeBean> request, ChargeBean t) {
            if (null != t && null != t.getR()) {
                totalpage = t.getR().getPage().getTotalpage();
                setData(t.getR().getRecord(), mChargeTitle, mChargehuan, mChargehuanUrl,totalpage);
            }

        }

        @Override
        public void onError(AbstractRequest<ChargeBean> requet, int statusCode, String body) {

        }

    };


    /**
     * 初始化音频，视频布局
     */
    private void initView(final ArrayList<RecommendBean.RBean.ChargeBean.RecordBeanX> datas) {

        RecommendBean.RBean.ChargeBean.RecordBeanX recordBeanX;

        /**视频**/
        int videoSize = 0;
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getType().equals("video"))
                videoSize = videoSize + 1;
        }
        if (videoSize == 1) {
            recordBeanX = datas.get(0);
            mImageLoader.loadImage(recordBeanX.getImage(), iv_pay_video, R.drawable.ic_launcher);
            tv_pay_video_tag_right.setText(recordBeanX.getPricetag());
            tv_pay_video_tag_left.setText(recordBeanX.getPlaytime());
            ll_pay_right_video.setVisibility(View.GONE);

            //点击跳转
            final RecommendBean.RBean.ChargeBean.RecordBeanX finalRecordBeanX = recordBeanX;
            iv_pay_video.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                    intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, finalRecordBeanX.getUrl());
                    mContext.startActivity(intent);
                }
            });
        } else if (videoSize == 2) {
            /**左侧**/
            recordBeanX = datas.get(0);
            mImageLoader.loadImage(recordBeanX.getImage(), iv_pay_video, R.drawable.ic_launcher);
            tv_pay_video_tag_right.setText(recordBeanX.getPricetag());
            tv_pay_video_tag_left.setText(recordBeanX.getPlaytime());

            /**右侧**/
            ll_pay_right_video.setVisibility(View.VISIBLE);
            recordBeanX = datas.get(1);
            mImageLoader.loadImage(recordBeanX.getImage(), iv_pay_video_1, R.drawable.ic_launcher);
            tv_pay_video_tag_right_1.setText(recordBeanX.getPricetag());
            tv_pay_video_tag_left_1.setText(recordBeanX.getPlaytime());
            //点击跳转
            iv_pay_video.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                    intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, datas.get(0).getUrl());
                    mContext.startActivity(intent);
                }
            });
            iv_pay_video_1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                    intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, datas.get(1).getUrl());
                    mContext.startActivity(intent);
                }
            });

        }

        ll_pay_selected_audio.removeAllViews();
        /**音频**/
        for (int i = videoSize; i < datas.size(); i++) {

            recordBeanX = datas.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.pay_selected_item, null);
            tv_audio_pay_time = (TextView) view.findViewById(R.id.tv_audio_pay_time);
            tv_audio_pay_context = (TextView) view.findViewById(R.id.tv_audio_pay_context);
            tv_audio_pay_price = (TextView) view.findViewById(R.id.tv_audio_pay_price);
            v_pay_selected_interval = view.findViewById(R.id.v_pay_selected_interval);

            tv_audio_pay_time.setText(recordBeanX.getPlaytime());
            tv_audio_pay_context.setText(recordBeanX.getTitle());
            tv_audio_pay_price.setText(recordBeanX.getPricetag());

            if (i == datas.size() - 1) {
                v_pay_selected_interval.setVisibility(GONE);
            } else {
                v_pay_selected_interval.setVisibility(VISIBLE);
            }
            final RecommendBean.RBean.ChargeBean.RecordBeanX finalRecordBeanX1 = recordBeanX;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("付费音频url=", finalRecordBeanX1.getUrl());
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
                    intent.putExtra(VoiceDetailsFragment.KEY_DETAILURL, finalRecordBeanX1.getUrl());
                    mContext.startActivity(intent);
                }
            });

            ll_pay_selected_audio.addView(view);
        }

    }
}
