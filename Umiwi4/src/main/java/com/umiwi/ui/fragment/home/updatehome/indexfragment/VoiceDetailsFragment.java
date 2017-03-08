package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.ExperDetailsVoiceBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.util.DateUtils;
import com.umiwi.ui.util.JsonUtil;
import com.umiwi.video.services.VoiceService;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/3/7.
 */

public class VoiceDetailsFragment extends BaseConstantFragment implements View.OnClickListener {

    @InjectView(R.id.iv_header)
    ImageView ivHeader;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.last_voice)
    ImageView lastVoice;
    @InjectView(R.id.start_player)
    ImageView startPlayer;
    @InjectView(R.id.next_voice)
    ImageView nextVoice;
    VoiceService.VoiceBinder mBinder;
    public static SeekBar seekbar;
    @InjectView(R.id.change_times)
    TextView changeTimes;
    @InjectView(R.id.total_time)
    TextView totalTime;
    private String CurrentvoiceId;
    private Handler handler = new Handler();
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekbar.setProgress(mBinder.getCurrentDuration());
            changeTimes.setText(DateUtils.formatmmss(mBinder.getCurrentDuration()));

            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (b) {
                        mBinder.getMediaplayer().seekTo(seekBar.getProgress());
                        changeTimes.setText(DateUtils.formatmmss(seekBar.getProgress()));

                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            handler.postDelayed(runnable, (int) ((float) mBinder.getCurrentDuration() / (float) mBinder.getDuration() * 100));
        }
    };
    private String source;
    private String voices;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voice_details_layout, null);
        ButterKnife.inject(this, view);
        String herfurl = getActivity().getIntent().getStringExtra("herfurl");
        if (!TextUtils.isEmpty(herfurl)) {
            getInfos(herfurl);
        }
        seekbar = (SeekBar) view.findViewById(R.id.seekbar);
        initListener();
        return view;
    }

    private void getInfos(String herfurl) {
        OkHttpUtils.get().url(herfurl).build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {
                Log.e("data", "音频url获取失败");
            }

            @Override
            public void onSucess(String data) {
                Log.e("data", "音频url获取成功");
                if (!TextUtils.isEmpty(data)) {
                    ExperDetailsVoiceBean experDetailsVoiceBean = JsonUtil.json2Bean(data, ExperDetailsVoiceBean.class);
                    List<ExperDetailsVoiceBean.AudiofileBean> audiofile = experDetailsVoiceBean.getAudiofile();
                    if (audiofile != null && audiofile.size() > 0) {
                        ExperDetailsVoiceBean.AudiofileBean audiofileBean = audiofile.get(0);
                        source = audiofileBean.getSource();
                        if (source!=null){
                            UmiwiApplication.getInstance().getBinder().searchInfo(source);
                        }
                    }

                }
            }

        });

    }

    private void initListener() {
        startPlayer.setOnClickListener(this);
        lastVoice.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        nextVoice.setOnClickListener(this);
    }

    private void bindVoiceSerive() {

        Intent intent = new Intent(getActivity(), VoiceService.class);
        getActivity().bindService(intent, new MyConn(), getActivity().BIND_AUTO_CREATE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.last_voice:
                break;
            case R.id.start_player:
//                if (mBinder.getMediaplayer().isPlaying()) {
//                    startPlayer.setImageResource(R.drawable.pause_player);
//                } else {
//                    startPlayer.setImageResource(R.drawable.start_player);
//                }
//                mBinder.playOrPause();
//                break;
            case R.id.next_voice:
                break;

        }
    }

    private class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinder = (VoiceService.VoiceBinder) iBinder;
            Log.e("mbinder", "拿到代理人对象");
         /*   mBinder.playVoice(voices);
            seekbar.setMax(mBinder.getDuration());
            handler.post(runnable);*/
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}
