package com.umiwi.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.umiwi.ui.R;
import com.umiwi.ui.adapter.DownloadAudioListDialogAdapter;
import com.umiwi.ui.managers.AudioDownloadManager;
import com.umiwi.ui.managers.AudioDownloadManager.DownloadStatusListener;
import com.umiwi.ui.model.AudioModel;
import com.umiwi.ui.model.AudioModel.DownloadStatus;

import java.util.ArrayList;

import cn.youmi.framework.util.SingletonFactory;

public class DownloadAudioListDialog implements DownloadStatusListener {

    private DownloadAudioListDialogAdapter mDownloadListDialogAdapter;
    private ArrayList<AudioModel> videoList = null;

    public static DownloadAudioListDialog getInstance() {
        return SingletonFactory.getInstance(DownloadAudioListDialog.class);
    }

    private void setVideos(ArrayList<AudioModel> audios) {
        if (audios.size() > 0) {
            ArrayList<AudioModel> myVideos = new ArrayList<>();
            for (AudioModel mV : audios) {
                if (mV.isTry() || TextUtils.isEmpty(mV.getVideoUrl())) {

                } else {
                    myVideos.add(mV);
                }
            }
            this.videoList = myVideos;
        }
    }

    public void showDialog(final Context mContext, ArrayList<AudioModel> audios) {
        setVideos(audios);

        ListHolder holder = new ListHolder();
        mDownloadListDialogAdapter = new DownloadAudioListDialogAdapter();
        mDownloadListDialogAdapter.setVideos(audios);

        DialogPlus dialogPlus = new DialogPlus.Builder(mContext)
                .setContentHolder(holder)
                .setHeader(R.layout.dialog_download_list_header)
                .setFooter(R.layout.dialog_download_list_footer)
                .setGravity(Gravity.BOTTOM)
                .setAdapter(mDownloadListDialogAdapter)
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view,
                                            int position) {

                        if (position - 1 <= -1) {
                            return;
                        }
                        if (position - 1 == videoList.size()) {
                            return;
                        }
                        if (videoList.size() > 0) {
                            AudioModel video = mDownloadListDialogAdapter.getItem(position - 1);
                            if (DownloadStatus.NOTIN == video.getDownloadStatus()) {
                                AudioDownloadManager.getInstance().addDownload(video);
                            }
                            Log.e("TAG", "点击下载时的video=" + video.getTitle());
                        }
                    }
                })
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.downloadmanager_textview:
                                if (videoList.size() > 0) {
                                    for (int i = 0; i < videoList.size(); i++) {
                                        AudioModel video = videoList.get(i);
                                        if (video != null) {
                                            if (DownloadStatus.NOTIN == video.getDownloadStatus()) {
                                                AudioDownloadManager.getInstance().addDownload(video);
                                            }
                                        }
                                    }
                                }
                                dialog.dismiss();
                                break;

                            default:
                                break;
                        }
                    }
                })
                .setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss(DialogPlus dialog) {
//                        PlayerController.getInstance().resume();
                    }
                })
                .create();
        View footerView = dialogPlus.getHeaderView();
        TextView title = (TextView) footerView.findViewById(R.id.title);
        title.setText("音频(" + audios.size() + ")");

        dialogPlus.show();
    }


    @Override
    public void onDownloadStatusChange(AudioModel video, DownloadStatus ds, String msg) {

    }

    @Override
    public void onProgressChange(AudioModel video, int current, int total, int speed) {

    }


}
