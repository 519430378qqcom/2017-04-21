package com.umiwi.ui.fragment.down;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.adapter.DownloadedAudioAdapter;
import com.umiwi.ui.beans.DownLoadVoiceBean;
import com.umiwi.ui.dialog.DownloadAudioListDialog1;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.AudioDownloadManager;
import com.umiwi.ui.managers.AudioManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.AudioModel;
import com.umiwi.ui.util.JsonUtil;
import com.umiwi.ui.util.LoginUtil;
import com.umiwi.ui.util.Utils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;
import java.util.ArrayList;

import cn.youmi.framework.fragment.BaseFragment;

/**
 * @Author xiaobo
 * @Version 2014年6月13日上午10:18:36
 */
public class DownloadedAudioFragment extends BaseFragment {
    public static final String KEY_ALBUM_ID = "key.albumid";
    public static final String KEY_ALBUM_TITLE = "key.albumtitle";
    private static final int PROGRESS = 1;

    private ListView mListView;
    private DownloadedAudioAdapter mAdapter;
//    private ArrayList<VideoModel> videos = new ArrayList<VideoModel>();
    private ArrayList<AudioModel> audios = new ArrayList<AudioModel>();
    private String albumId;
    private String albumTitle;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case PROGRESS :
                    if(mediaPlayer != null) {
                        seekbar.setMax(mediaPlayer.getDuration());
                        total_time.setText(utils.stringForTime(mediaPlayer.getDuration()));
                        change_times.setText(utils.stringForTime(mediaPlayer.getCurrentPosition()));
                        seekbar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;
            }
        }
    };
    private ImageView iv_music_last;
    private ImageView iv_music_pauseplay;
    private ImageView iv_music_next;
    private TextView tv_download_voicetitle;
    private TextView change_times;
    private TextView total_time;
    private SeekBar seekbar;
    private PopupWindow pop;
    private Utils utils;
    private AudioModel audio;

    public static DownloadedAudioFragment newInstance(String albumId, String albumTitle) {
        DownloadedAudioFragment f = new DownloadedAudioFragment();
        f.setAlbumId(albumId);
        f.setAlbumTitle(albumTitle);
        return f;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    ActionMode mMode;
    Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        utils = new Utils();
        Log.e("TAG", "onCreate()");
    }

    @Override
    public void onPaused(AppCompatActivity a) {
        super.onPaused(a);
        Log.e("TAG", "onPaused()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("TAG", "onStop()");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        inflater.inflate(R.menu.toolbar_delete, menu);
//		menu.findItem(R.id.delete).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                trashClick();
                if (!mAdapter.isEmpty()) {
                    mMode = mActionBarToolbar.startActionMode(new DeleteCallback());
                }

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(AppCompatActivity a) {
        super.onActivityCreated(a);
        albumId = a.getIntent().getStringExtra(KEY_ALBUM_ID);
        albumTitle = a.getIntent().getStringExtra(KEY_ALBUM_TITLE);
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frame_listview_layout, null);
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBarAndToolbarTitle(mActionBarToolbar, albumTitle);

        mListView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new DownloadedAudioAdapter(getActivity(), this.audios);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(downItemClick);

        return view;
    }


    private class DeleteCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.setTitle("删除");
            MenuItem deleteItem = menu.add(0, 1, 2, "删除").setIcon(R.drawable.ic_delete);
            MenuItemCompat.setShowAsAction(deleteItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case 1:
                    SparseBooleanArray checkedVideoids = mAdapter.getCheckedVideoIds();
                    ArrayList<Integer> videoids = new ArrayList<Integer>();
                    for (int i = 0; i < checkedVideoids.size(); i++) {
                        int VideoId = checkedVideoids.keyAt(i);
                        if (checkedVideoids.get(VideoId)) {
                            videoids.add(VideoId);
                        }
                    }

                    if (!videoids.isEmpty()) {
                        for (int videoId : videoids) {
                            AudioDownloadManager.getInstance().deleteDownloadedByVideoId("" + videoId);
                        }
                        update();
                        mAdapter.initCheckedVideoIds();
                    }
                    trashClick();

                    mode.finish();
                    break;

                default:
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode arg0) {
            trashClick();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        update();
        MobclickAgent.onPageStart(fragmentName);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(fragmentName);
    }

    private void update() {

        audios = AudioManager.getInstance().getDownloadedListByAlbumId(albumId);
        for (AudioModel audio: audios){
            if(!audio.isWatched()) {
                AudioManager.getInstance().setWatchedByVideoId(audio.getVideoId());
            }
        }
        ArrayList<AudioModel> albumAudios = AudioManager.getInstance().getVideosByAlbumId(albumId);
        if(albumAudios.size() > audios.size()) {
            AudioModel audioFake = new AudioModel();
            audioFake.setAlbumId(albumId);
            audios.add(audioFake);
        }

        mAdapter.setVideos(audios);

    }

    //初始化音乐播放器
    private MediaPlayer mediaPlayer ;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_music_last :

                    break;
                case R.id.iv_music_pauseplay :
                    if (mediaPlayer!=null && mediaPlayer.isPlaying()) {

                        pause();
                        iv_music_pauseplay.setImageResource(R.drawable.music_pause);
                    } else {
                        play();
                        iv_music_pauseplay.setImageResource(R.drawable.music_play_p);
                    }
                    break;
                case R.id.iv_music_next :

                    break;
            }
        }
    };
    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser) {
//                mediaPlayer.seekTo(progress);
                seekTo(progress);
                Log.e("TAG", "进度更新");
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    OnItemClickListener downItemClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (mAdapter.getTrashStatus()) {
                AudioModel audio = (AudioModel) mAdapter.getItem(position);
                if(audio.getVideoId() == null) {
                    return;
                }

                CheckBox videoCheckBox = (CheckBox) view.findViewById(R.id.video_checkbox);
                videoCheckBox.performClick();

            } else {
                audio = (AudioModel) mAdapter.getItem(position);
                Log.e("TAG", "audio");
                if (audio.getVideoId() == null) {
                    if (YoumiRoomUserManager.getInstance().isLogin()) {
                        ArrayList<AudioModel> videos = AudioManager.getInstance().getVideosByAlbumId(audio.getAlbumId());
                        DownloadAudioListDialog1.getInstance().showDialog(getActivity(), videos);
                    } else {
                        LoginUtil.getInstance().showLoginView(getActivity());
                    }
                    return;
                }

                if (audio.isLocalFileValid()) {
//                    Toast.makeText(getActivity(), "点击播放试听", Toast.LENGTH_SHORT).show();
                    View view1 = View.inflate(getActivity(),R.layout.voice_down_populayout,null);
                    iv_music_last = (ImageView) view1.findViewById(R.id.iv_music_last);
                    iv_music_pauseplay = (ImageView) view1.findViewById(R.id.iv_music_pauseplay);
                    iv_music_next = (ImageView) view1.findViewById(R.id.iv_music_next);
                    tv_download_voicetitle = (TextView) view1.findViewById(R.id.tv_download_voicetitle);
                    tv_download_voicetitle.setText(audio.getAlbumTitle());

                    change_times = (TextView) view1.findViewById(R.id.change_times);
                    total_time = (TextView) view1.findViewById(R.id.total_time);
                    seekbar = (SeekBar) view1.findViewById(R.id.seekbar);
                    iv_music_last.setOnClickListener(mOnClickListener);
                    iv_music_pauseplay.setOnClickListener(mOnClickListener);
                    iv_music_next.setOnClickListener(mOnClickListener);


                    pop = new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
                    pop.setFocusable(true);
                    pop.setBackgroundDrawable(new BitmapDrawable());
                    pop.setOutsideTouchable(true);

//                    pop.setAnimationStyle(R.style.mypopuwindow_anim_style);
                    pop.update();
                    pop.showAtLocation(view, Gravity.CENTER,0,0);
                    pop.setOnDismissListener(mOnDismissListener);
//                    handler.sendEmptyMessage(PROGRESS);
//                    if(audio.getVideoUrl().equals(UmiwiApplication.mainActivity.musicUrl)) {
//                        if (mediaPlayer == null) {
//                            openAudio(audio.getVideoUrl());
//                            Log.e("TAG", "audio.getVideoUrl==" + audio.getVideoUrl());
//                        } else {
//                            if(!mediaPlayer.isPlaying()) {
//                                play();
//                            }
//                        }
//                        return;
//                    }



                    Log.e("TAG", "audio.getVideoUrl1==" + audio.getVideoUrl());
                    if (audio.getVideoUrl().contains("mp3")) {
//                        if(audio.getVideoUrl().equals(UmiwiApplication.mainActivity.musicUrl)) {
//                            if (mediaPlayer == null) {
//                                openAudio(audio.getVideoUrl());
//                            } else {
//                                if(!mediaPlayer.isPlaying()) {
//                                    play();
//                                }
//                            }
//                            return;
//                        }
                        if (mediaPlayer == null) {
                            try {
                                if(UmiwiApplication.mainActivity.service != null &&UmiwiApplication.mainActivity.service.isPlaying()) {
                                    UmiwiApplication.mainActivity.service.pause();
                                }
                                openAudio(audio.getVideoUrl());
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }

                        } else {
                            if(!mediaPlayer.isPlaying()) {
                                play();
                            }
                        }
                        UmiwiApplication.mainActivity.musicUrl = audio.getVideoUrl();
                    } else {
                        OkHttpUtils.get().url(audio.getVideoUrl()).build().execute(new CustomStringCallBack() {
                            @Override
                            public void onFaild() {

                            }

                            @Override
                            public void onSucess(String data) {
                                DownLoadVoiceBean downLoadVoiceBean = JsonUtil.json2Bean(data, DownLoadVoiceBean.class);
                                String source = downLoadVoiceBean.getSource();
                                try {
                                    if(UmiwiApplication.mainActivity.service.isPlaying()) {
                                        UmiwiApplication.mainActivity.service.pause();
                                    }
                                    if(mediaPlayer == null) {
                                        openAudio(source);
                                    }
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }

                                UmiwiApplication.mainActivity.musicUrl = source;
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), "文件不存在！", Toast.LENGTH_SHORT).show();//TODO
                }
                return;
            }
        }
    };

    //开始播放音乐
    private void openAudio(String url) {
        handler.sendEmptyMessage(PROGRESS);
        if (mediaPlayer != null) {
            //把上一个音频资源释放
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        //设置准备，错误，播放完成的监听
        mediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mediaPlayer.setOnErrorListener(mOnErrorListener);
        mediaPlayer.setOnCompletionListener(mOnCompletionListener);
        //设置播放地址
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        handler.sendEmptyMessage(PROGRESS);
    }

    private MediaPlayer.OnPreparedListener mOnPreparedListener= new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            play();
            seekbar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
            handler.sendEmptyMessage(PROGRESS);
        }
    };
    private MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
    };
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {

        }
    };
    private PopupWindow.OnDismissListener mOnDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {

        }
    };

    public void trashClick() {
        mAdapter.toggleTrashStatus();
        mAdapter.notifyDataSetChanged();
    }
    private void play() {

//        try {
//            mediaPlayer.setDataSource(audio.getVideoUrl());
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if(mediaPlayer != null) {
            mediaPlayer.start();
        }
    }


    private void pause() {
        if(mediaPlayer != null) {
            mediaPlayer.pause();
        }

    }
    private int getDuration() {
        if(mediaPlayer != null) {
            return  mediaPlayer.getDuration();
        }
        return 0;
    }
    private int getCurrentPosition() {
        if(mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }
    private boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }
    private void seekTo(int position){
        if(mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }
    @Override
    public void onDestroy() {
        if(mediaPlayer !=null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}