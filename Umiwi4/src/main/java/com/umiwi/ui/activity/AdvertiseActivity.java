package com.umiwi.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.AdvertisementBean;
import com.umiwi.ui.fragment.AudioSpecialDetailFragment;
import com.umiwi.ui.fragment.VideoSpecialDetailFragment;
import com.umiwi.ui.fragment.WebFragment;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.ColumnDetailsFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ImageLoader;

/**
 * Created by Administrator on 2017/4/8 0008.
 */

public class AdvertiseActivity extends AppCompatActivity {

    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; //需要自己定义标志
    private static final int PROGRESS = 1;
    TextView tv_advert;
    ViewPager vp_ad_images;
    private AdvertiseAdapter adapter;
    private ArrayList<AdvertisementBean.RAdvertBean> mList = new ArrayList<>();
    private int page = 3;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case PROGRESS :
                    page --;
                    tv_advert.setText("跳过广告" + page + "");
                    if(page == 0) {
                        Intent intent = new Intent(AdvertiseActivity.this,HomeMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS,1000);

                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//关键代码
        setContentView(R.layout.fragment_advertise_layout);

         vp_ad_images = (ViewPager)findViewById(R.id.vp_ad_images);
        tv_advert = (TextView)findViewById(R.id.tv_advert);
        adapter = new AdvertiseAdapter(this);
        adapter.setData(mList);
        vp_ad_images.setAdapter(adapter);

        vp_ad_images.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != (mList.size() - 1)) {
                    page = 3;
                    handler.removeMessages(PROGRESS);
                    tv_advert.setVisibility(View.GONE);
                } else {
                    page = 3;
                    handler.removeMessages(PROGRESS);
                    tv_advert.setText("跳过广告" + page + "");
                    tv_advert.setVisibility(View.VISIBLE);

                    handler.sendEmptyMessageDelayed(PROGRESS,1000);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tv_advert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                Intent intent = new Intent(AdvertiseActivity.this,HomeMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        getInfo();
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_down_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getInfo() {
        GetRequest<AdvertisementBean> request = new GetRequest<AdvertisementBean>(UmiwiAPI.UMIWI_ADVERTISE, GsonParser.class, AdvertisementBean.class, new AbstractRequest.Listener<AdvertisementBean>() {
            @Override
            public void onResult(AbstractRequest<AdvertisementBean> request, AdvertisementBean advertisementBean) {
                ArrayList<AdvertisementBean.RAdvertBean> advertisementBeanR = advertisementBean.getR();
                mList.clear();
                mList.addAll(advertisementBeanR);
                adapter.setData(mList);
            }

            @Override
            public void onError(AbstractRequest<AdvertisementBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }
    class AdvertiseAdapter extends PagerAdapter {
        private Activity activity;
        private  ArrayList<AdvertisementBean.RAdvertBean> mList;
        public AdvertiseAdapter(FragmentActivity activity) {
            this.activity = activity;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView imageView = new ImageView(activity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            final AdvertisementBean.RAdvertBean rAdvertBean = mList.get(position);
            ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
            mImageLoader.loadImage(rAdvertBean.getImage(), imageView);
            imageView.setAdjustViewBounds(true);
            imageView.setMaxHeight(container.getMeasuredHeight());
            imageView.setMaxWidth(container.getMeasuredWidth());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(para);
            container.addView(imageView, 0);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdvertiseActivity.this,HomeMainActivity.class);
                    startActivity(intent);
                    if("zhuanti".equals(rAdvertBean.getType())) {
                        String detailurl = rAdvertBean.getUrl();
                        Intent intent1 = new Intent(activity, UmiwiContainerActivity.class);
                        intent1.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VideoSpecialDetailFragment.class);
                        intent1.putExtra("detailurl", detailurl);
                        activity.startActivity(intent1);
                    }else if("audiozhuanti".equals(rAdvertBean.getType())) {
                        String typeId = rAdvertBean.getAlbumid();
                        Intent intent2 = new Intent(activity, UmiwiContainerActivity.class);
                        intent2.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AudioSpecialDetailFragment.class);
                        intent2.putExtra("typeId", typeId);
                        activity.startActivity(intent2);
                    }else if("article".equals(rAdvertBean.getType())) {
                        Intent intent3 = new Intent(activity, UmiwiContainerActivity.class);
                        intent3.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
                        intent3.putExtra(WebFragment.WEB_URL, rAdvertBean.getUrl());
                        activity.startActivity(intent3);
                    }else if("audio".equals(rAdvertBean.getType())) {
                        Intent intent4 = new Intent(activity, UmiwiContainerActivity.class);
                        intent4.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
                        intent4.putExtra(VoiceDetailsFragment.KEY_DETAILURL, rAdvertBean.getUrl());
                        activity.startActivity(intent4);
                    }else if("video".equals(rAdvertBean.getType())) {
                        Intent intent5 = new Intent(activity, UmiwiContainerActivity.class);
                        intent5.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                        intent5.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, rAdvertBean.getUrl());
                        activity.startActivity(intent5);
                    }else if("column".equals(rAdvertBean.getType())) {
                        Intent intent6 = new Intent(activity, UmiwiContainerActivity.class);
                        intent6.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ColumnDetailsFragment.class);//详情页
                        intent6.putExtra("columnurl", rAdvertBean.getUrl());
                        activity.startActivity(intent6);
                    }
                    finish();
                    handler.removeCallbacksAndMessages(null);
                }
            });


            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void setData(ArrayList<AdvertisementBean.RAdvertBean> mList) {
            this.mList = mList;
            notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
