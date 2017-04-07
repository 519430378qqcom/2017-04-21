package com.umiwi.ui.fragment.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.HomeMainActivity;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.beans.updatebeans.AdvertisementBean;
import com.umiwi.ui.fragment.AudioSpecialDetailFragment;
import com.umiwi.ui.fragment.VideoSpecialDetailFragment;
import com.umiwi.ui.fragment.WebFragment;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.ColumnDetailsFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ImageLoader;

/**
 * Created by Administrator on 2017/4/7 0007.
 */

public class AdvertiseFragment extends BaseConstantFragment {

    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; //需要自己定义标志

    private static final int PROGRESS = 1;
    @InjectView(R.id.tv_advert)
    TextView tv_advert;
    @InjectView(R.id.vp_ad_images)
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
                        Intent intent = new Intent(getActivity(),HomeMainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS,1000);

                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//关键代码
        View view = inflater.inflate(R.layout.fragment_advertise_layout, null);

        ButterKnife.inject(this,view);
        adapter = new AdvertiseAdapter(getActivity());
        adapter.setData(mList);
        vp_ad_images.setAdapter(adapter);

        vp_ad_images.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != (mList.size() - 1)) {
                    tv_advert.setVisibility(View.GONE);
                } else {
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
                Intent intent = new Intent(getActivity(),HomeMainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        getInfo();
        return view;
    }

    @Override
    public boolean onClickBack(AppCompatActivity a) {

        return super.onClickBack(a);
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);

    }

    class AdvertiseAdapter extends PagerAdapter {
        private FragmentActivity activity;
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
            ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ImageView imageView = new ImageView(activity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setAdjustViewBounds(true);
            
            final AdvertisementBean.RAdvertBean rAdvertBean = mList.get(position);
            ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
            mImageLoader.loadImage(rAdvertBean.getImage(), imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(para);
            container.addView(imageView, 0);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if("zhuanti".equals(rAdvertBean.getType())) {
                        String detailurl = rAdvertBean.getUrl();
                        Intent intent = new Intent(activity, UmiwiContainerActivity.class);
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VideoSpecialDetailFragment.class);
                        intent.putExtra("detailurl", detailurl);
                        activity.startActivity(intent);
                    }else if("audiozhuanti".equals(rAdvertBean.getType())) {
                        String typeId = rAdvertBean.getAlbumid();
                        Intent intent = new Intent(activity, UmiwiContainerActivity.class);
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AudioSpecialDetailFragment.class);
                        intent.putExtra("typeId", typeId);
                        activity.startActivity(intent);
                    }else if("article".equals(rAdvertBean.getType())) {
                        Intent intent = new Intent(activity, UmiwiContainerActivity.class);
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
                        intent.putExtra(WebFragment.WEB_URL, rAdvertBean.getUrl());
                        activity.startActivity(intent);
                    }else if("audio".equals(rAdvertBean.getType())) {
                        Intent intent = new Intent(activity, UmiwiContainerActivity.class);
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
                        intent.putExtra(VoiceDetailsFragment.KEY_DETAILURL, rAdvertBean.getUrl());
                        activity.startActivity(intent);
                    }else if("video".equals(rAdvertBean.getType())) {
                        Intent intent = new Intent(activity, UmiwiContainerActivity.class);
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                        intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, rAdvertBean.getUrl());
                        activity.startActivity(intent);
                    }else if("column".equals(rAdvertBean.getType())) {
                        Intent intent = new Intent(activity, UmiwiContainerActivity.class);
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ColumnDetailsFragment.class);//详情页
                        intent.putExtra("columnurl", rAdvertBean.getUrl());
                        activity.startActivity(intent);
                    }
                    getActivity().finish();
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
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}
