package com.umiwi.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.umiwi.ui.R;
import com.umiwi.ui.adapter.DownloadListDialogAdapter;
import com.umiwi.ui.managers.VideoDownloadManager;
import com.umiwi.ui.managers.VideoDownloadManager.DownloadStatusListener;
import com.umiwi.ui.model.VideoModel;
import com.umiwi.ui.model.VideoModel.DownloadStatus;
import com.umiwi.video.control.PlayerController;

import java.util.ArrayList;

import cn.youmi.framework.util.SingletonFactory;

public class DownloadListDialog implements DownloadStatusListener {

    private DownloadListDialogAdapter mDownloadListDialogAdapter;
    private ArrayList<VideoModel> videoList = null;

    public static DownloadListDialog getInstance() {
        return SingletonFactory.getInstance(DownloadListDialog.class);
    }

    private void setVideos(ArrayList<VideoModel> videos) {
        if (videos.size() > 0) {
            ArrayList<VideoModel> myVideos = new ArrayList<>();
            for (VideoModel mV : videos) {
                if (mV.isTry() || TextUtils.isEmpty(mV.getVideoUrl())) {

                } else {
                    myVideos.add(mV);
                }
            }
            this.videoList = myVideos;
        }
    }

    public void showDialog(final Context mContext, ArrayList<VideoModel> videos) {
        setVideos(videos);

        ListHolder holder = new ListHolder();
        mDownloadListDialogAdapter = new DownloadListDialogAdapter();
        mDownloadListDialogAdapter.setVideos(videos);

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
                            VideoModel video = mDownloadListDialogAdapter.getItem(position - 1);
                            if (DownloadStatus.NOTIN == video.getDownloadStatus()) {
                                VideoDownloadManager.getInstance().addDownload(video);
                            }
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
                                        VideoModel video = videoList.get(i);
                                        if (video != null) {
                                            if (DownloadStatus.NOTIN == video.getDownloadStatus()) {
                                                VideoDownloadManager.getInstance().addDownload(video);
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
                        PlayerController.getInstance().resume();
                    }
                })
                .create();
        View footerView = dialogPlus.getHeaderView();
        TextView title = (TextView) footerView.findViewById(R.id.title);
        title.setText("视频(" + videos.size() + ")");

        dialogPlus.show();
    }


    @Override
    public void onDownloadStatusChange(VideoModel video, DownloadStatus ds, String msg) {

    }

    @Override
    public void onProgressChange(VideoModel video, int current, int total, int speed) {

    }


}
