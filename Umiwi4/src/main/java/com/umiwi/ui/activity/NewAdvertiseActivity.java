package com.umiwi.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.AdvertisementBean;
import com.umiwi.ui.fragment.AudioSpecialDetailFragment;
import com.umiwi.ui.fragment.VideoSpecialDetailFragment;
import com.umiwi.ui.fragment.WebFragment;
import com.umiwi.ui.fragment.audiolive.AudioLiveDetailsFragment;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.ColumnDetailsFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.fragment.splash.SplashFragment;
import com.umiwi.ui.util.CacheUtil;
import com.umiwi.ui.util.DateUtils;

import java.io.File;
import java.util.ArrayList;

public class NewAdvertiseActivity extends Activity {
    private ImageView iv_advertise;
    private TextView tv_advert;
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; //需要自己定义标志
    private String yMdHm;
    private int page = 3;
    private static final int PROGRESS = 1;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case PROGRESS:
                    page--;
                    tv_advert.setText("跳过广告" + page + "");
                    if (page == 0) {
                        CacheUtil.putString(NewAdvertiseActivity.this, SplashFragment.START_MIAN, yMdHm);
                        Intent intent = new Intent(NewAdvertiseActivity.this, HomeMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS, 1000);

                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//关键代码
         /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_new_advertise);
        iv_advertise = (ImageView)findViewById(R.id.iv_advertise);
        tv_advert = (TextView)findViewById(R.id.tv_advert);
        getInfo();
        //获取当前时间
        long millis = System.currentTimeMillis();
        yMdHm = DateUtils.yMdHms(millis);
        tv_advert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);

//                CacheUtil.putBoolean(AdvertiseActivity.this, SplashFragment.START_MIAN,true);

                CacheUtil.putString(NewAdvertiseActivity.this, SplashFragment.START_MIAN, yMdHm);
                Intent intent = new Intent(NewAdvertiseActivity.this, HomeMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void getInfo() {
        //如果没有广告图
        if (!new File(SplashActivity.photoPath).exists()||SplashActivity.advertisementBean1 == null){
            Intent intent = new Intent(NewAdvertiseActivity.this, HomeMainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        iv_advertise.setImageBitmap(BitmapFactory.decodeFile(SplashActivity.photoPath));
//        AdvertisementBean advertisementBean = (AdvertisementBean) getIntent().getSerializableExtra(SplashFragment.ADVERTISEMENT_BEAN);
        final ArrayList<AdvertisementBean.RAdvertBean> advertisementBeanR = SplashActivity.advertisementBean1.getR();
        tv_advert.setText("跳过广告" + page + "");
                tv_advert.setClickable(true);
                handler.sendEmptyMessageDelayed(PROGRESS, 1000);
                iv_advertise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CacheUtil.putString(NewAdvertiseActivity.this, SplashFragment.START_MIAN, yMdHm);
                        Intent intent = new Intent(NewAdvertiseActivity.this, HomeMainActivity.class);
                        startActivity(intent);
                        if ("zhuanti".equals(advertisementBeanR.get(0).getType())) {
                            String detailurl = advertisementBeanR.get(0).getUrl();
                            Intent intent1 = new Intent(NewAdvertiseActivity.this, UmiwiContainerActivity.class);
                            intent1.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VideoSpecialDetailFragment.class);
                            intent1.putExtra("detailurl", detailurl);
                            startActivity(intent1);
                        } else if ("audiozhuanti".equals(advertisementBeanR.get(0).getType())) {
                            String typeId = advertisementBeanR.get(0).getAlbumid();
                            Intent intent2 = new Intent(NewAdvertiseActivity.this, UmiwiContainerActivity.class);
                            intent2.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AudioSpecialDetailFragment.class);
                            intent2.putExtra("typeId", typeId);
                            startActivity(intent2);
                        } else if ("article".equals(advertisementBeanR.get(0).getType())) {
                            Intent intent3 = new Intent(NewAdvertiseActivity.this, UmiwiContainerActivity.class);
                            intent3.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
                            intent3.putExtra(WebFragment.WEB_URL, advertisementBeanR.get(0).getUrl());
                            startActivity(intent3);
                        } else if ("audio".equals(advertisementBeanR.get(0).getType())) {
                            Intent intent4 = new Intent(NewAdvertiseActivity.this, UmiwiContainerActivity.class);
                            intent4.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
                            intent4.putExtra(VoiceDetailsFragment.KEY_DETAILURL, advertisementBeanR.get(0).getUrl());
                            startActivity(intent4);
                        } else if ("video".equals(advertisementBeanR.get(0).getType())) {
                            Intent intent5 = new Intent(NewAdvertiseActivity.this, UmiwiContainerActivity.class);
                            intent5.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                            intent5.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, advertisementBeanR.get(0).getUrl());
                            startActivity(intent5);
                        } else if ("column".equals(advertisementBeanR.get(0).getType())) {

                            Intent intent6 = new Intent(NewAdvertiseActivity.this, UmiwiContainerActivity.class);
                            intent6.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ColumnDetailsFragment.class);//详情页
                            intent6.putExtra("columnurl", advertisementBeanR.get(0).getUrl());
                            startActivity(intent6);
                        } else if("telecast".equals(advertisementBeanR.get(0).getType())) {
                            Intent intent7 = new Intent(NewAdvertiseActivity.this, UmiwiContainerActivity.class);
                            intent7.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS,AudioLiveDetailsFragment.class);
                            intent7.putExtra(AudioLiveDetailsFragment.LIVEID,advertisementBeanR.get(0).getAlbumid());
                            startActivity(intent7);
                        }
                        finish();
                        handler.removeCallbacksAndMessages(null);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
