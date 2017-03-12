package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.CommitVoiceBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.view.CircleImageView;
import com.umiwi.video.recorder.AudioManager;
import com.umiwi.video.recorder.MediaManager;

import java.io.File;
import java.io.FileInputStream;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.PostRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ImageLoader;

/**
 * Created by Administrator on 2017/3/9.
 */

public class RecordVoiceFragment extends BaseConstantFragment implements View.OnClickListener {

    @InjectView(R.id.header)
    CircleImageView header;
    @InjectView(R.id.name)
    TextView name;
    @InjectView(R.id.times)
    TextView times;
    @InjectView(R.id.miaoshu)
    TextView miaoshu;
    @InjectView(R.id.perice)
    TextView perice;
    @InjectView(R.id.record_icon)
    ImageView recordIcon;
    @InjectView(R.id.paly_puase)
    ImageView palyPuase;
    @InjectView(R.id.anew)
    TextView anew;
    @InjectView(R.id.commit)
    TextView commit;
    @InjectView(R.id.record_lenght)
    TextView recordLenght;
    @InjectView(R.id.tv_luyin)
    TextView tvLuyin;
    @InjectView(R.id.back)
    ImageView back;
    private boolean isRecord = true;
    private AudioManager audioManager;
    private Timer timer;
    private int recLen = 0;
    private String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_voice, null);
        ButterKnife.inject(this, view);
        initOnClickListener();
        initAudioManager();
        getIntentInfos();
        return view;
    }

    private void getIntentInfos() {
        String price = getActivity().getIntent().getStringExtra("price");
        String tname = getActivity().getIntent().getStringExtra("tname");
        String tavatar = getActivity().getIntent().getStringExtra("tavatar");
        String title = getActivity().getIntent().getStringExtra("title");
        String answertime = getActivity().getIntent().getStringExtra("answertime");
        id = getActivity().getIntent().getStringExtra("id");
        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        mImageLoader.loadImage(tavatar, header);
        name.setText(tname);
        times.setText(answertime);
        miaoshu.setText(title);
        perice.setText(price);
    }


    private void initAudioManager() {
        boolean state = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (state) {
            String dri = Environment.getExternalStorageDirectory() + "/Umi_recorder_audios";
            audioManager = AudioManager.getmInstance(dri);
            audioManager.setOnAudioStateListener(new AudioManager.AudioStateListener() {
                @Override
                public void wellPrepared() {
                    tvLuyin.setText("录音中，点击停止录音");
                    timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {

                            getActivity().runOnUiThread(new Runnable() {      // UI thread
                                @Override
                                public void run() {
                                    recordLenght.setTextColor(getResources().getColor(R.color.main_color));

                                    recLen++;
//                    recordLenght.setText(""+recLen);
                                    if (recLen < 180) {
                                        recordLenght.setText((recLen / 60 < 10 ? "0" + recLen / 60 : recLen / 60) + ":" + (recLen % 60 < 10 ? "0" + recLen % 60 : recLen % 60));
                                    } else {
                                        recordLenght.setText("03:00");
                                        timer.cancel();
                                        timer.purge();
                                        timer = null;
                                        audioManager.release();
                                    }
                                }
                            });
                        }
                    };

                    timer.schedule(task, 1000, 1000);
                }
            });
        }
    }

    private void initOnClickListener() {
        recordIcon.setOnClickListener(this);
        palyPuase.setOnClickListener(this);
        anew.setOnClickListener(this);
        commit.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_icon: // 录音按钮
                isRecord = true;
                recordIcon.setVisibility(View.GONE);
                palyPuase.setImageResource(R.drawable.start_player);
                palyPuase.setVisibility(View.VISIBLE);  //开始录音并且计时
                audioManager.prepareAudio();
                break;
            case R.id.paly_puase:  //开始或者暂停
                if (isRecord == true) { //录音完成
                    timer.cancel();
                    timer.purge();
                    timer = null;
                    anew.setVisibility(View.VISIBLE);
                    commit.setVisibility(View.VISIBLE);
                    palyPuase.setImageResource(R.drawable.pause_player);
                    isRecord = false;
                    tvLuyin.setText("录音完成");
                    boolean isRelease = audioManager.getIsRelease();
                    if (isRelease == false) {
                        if (recLen < 180) {
                            audioManager.release();
                        }
                    } else {
                        MediaManager.pause();
                    }
                } else {
                    palyPuase.setImageResource(R.drawable.start_player);
                    isRecord = true;  //开始放音
                    Log.e("audioManager", audioManager.getCurrentFilePath());
                    if (MediaManager.isPause == true) {
                        MediaManager.resume();
                        return;
                    }
                    MediaManager.playSound(audioManager.getCurrentFilePath(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {

                        }
                    });
                }
                break;
            case R.id.anew:   //重新录制
                anew.setVisibility(View.GONE);
                commit.setVisibility(View.GONE);
                palyPuase.setVisibility(View.GONE);
                recordIcon.setVisibility(View.VISIBLE);
                audioManager.deleteFile();
                recLen = 0;

                MediaManager.relese();
                tvLuyin.setText("点击按钮开始录音");
                recordLenght.setText("录音时长限制为3分钟以内");
                recordLenght.setTextColor(getResources().getColor(R.color.gray_a));
                break;
            case R.id.commit:  // 提交
                commitVoice();
                break;
            case R.id.back:
                getActivity().finish();
                break;
        }
    }

    private void commitVoice() {
        try {
            String path = encodeBase64File(audioManager.getCurrentFilePath());
            PostRequest<CommitVoiceBean> prequest = new PostRequest<CommitVoiceBean>(UmiwiAPI.COMMIT_VOICE, GsonParser.class, CommitVoiceBean.class, pushListener);
            prequest.addParam("qid", id);
            prequest.addParam("content", path);
            prequest.addParam("playtime", recLen + "");
            prequest.go();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private AbstractRequest.Listener<CommitVoiceBean> pushListener = new AbstractRequest.Listener<CommitVoiceBean>() {

        @Override
        public void onResult(AbstractRequest<CommitVoiceBean> request, CommitVoiceBean t) {
            Toast.makeText(getActivity(), "........", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(AbstractRequest<CommitVoiceBean> requet, int statusCode, String body) {
            Log.e("request", requet.toString());
            Log.e("request", body);


        }
    };

    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }
}
