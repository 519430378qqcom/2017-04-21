package com.umiwi.ui.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.model.VideoModel;

public class DownloadedAdapter extends BaseAdapter {
    private ArrayList<VideoModel> videos;
    private Boolean trashStatus = false;
    private SparseBooleanArray checkedVideoIds = new SparseBooleanArray();

    public void setVideos(ArrayList<VideoModel> videos) {
        this.videos = videos;
        this.notifyDataSetChanged();
    }

    public ArrayList<VideoModel> getVideos() {
        return this.videos;
    }

    public Boolean getTrashStatus() {
        return trashStatus;
    }

    public void toggleTrashStatus() {
        initCheckedVideoIds();
        trashStatus = trashStatus ? false : true;
    }

    public DownloadedAdapter(Context context, ArrayList<VideoModel> videos) {
        super();
        this.videos = videos;
    }


    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View v, ViewGroup arg2) {
        VideoModel video = (VideoModel) getItem(position);
        final String filesize = video.getFileSize() + "";
        final String videoid = video.getVideoId();
        LayoutInflater layoutInflater = LayoutInflater.from(UmiwiApplication
                .getInstance());
        if (videoid == null) {
            v = layoutInflater.inflate(R.layout.image_layout, null);
            ImageView iv = (ImageView) v.findViewById(R.id.image_sc);
            iv.setImageResource(R.drawable.add_continue1);
            iv.setVisibility(View.VISIBLE);
        } else {
            v = layoutInflater.inflate(R.layout.fragment_downloaded_item, null);
            TextView titleTextView = (TextView) v
                    .findViewById(R.id.videotitle_textview);
            TextView filesizeTextView = (TextView) v
                    .findViewById(R.id.filesize_textview);
            titleTextView.setText(video.getTitle().trim());
            filesizeTextView.setText(df.format((Long.parseLong(filesize))
                    / (1024 * 1024))
                    + "MB");
            CheckBox videoCheckBox = (CheckBox) v
                    .findViewById(R.id.video_checkbox);
            ImageView playImageView = (ImageView) v
                    .findViewById(R.id.play_imageview);

            videoCheckBox.setTag(videoid);
            if (getTrashStatus()) {
                videoCheckBox.setVisibility(View.VISIBLE);
                videoCheckBox
                        .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton v,
                                                         boolean isChecked) {
                                String videoId = (String) v.getTag();
                                if (videoId == null) {
                                    return;
                                }

                                setClicked(videoId);
                            }
                        });
                playImageView.setVisibility(View.GONE);

            } else {
                videoCheckBox.setChecked(false);
                videoCheckBox.setVisibility(View.GONE);
                playImageView.setVisibility(View.VISIBLE);
            }
        }

        v.setTag(R.id.videotitle_textview, video.getVideoId());
        return v;
    }

    public void setClicked(String videoid) {
        int videoId = Integer.parseInt(videoid);
        boolean oldState = checkedVideoIds.get(videoId);

        // if in the group there was not any child checked
        if (oldState == true) {
            checkedVideoIds.put(videoId, false);
        } else {
            checkedVideoIds.put(videoId, true);
        }

    }

    public SparseBooleanArray getCheckedVideoIds() {
        return checkedVideoIds;
    }

    public void initCheckedVideoIds() {
        checkedVideoIds.clear();
        if (videos != null && videos.size() > 0) {
            for (int i = 0; i < videos.size(); i++) {
                int albumId = Integer.parseInt((videos.get(i)).getAlbumId());
                checkedVideoIds.put(albumId, false);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int position) {
        return videos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    DecimalFormat df = new DecimalFormat("0.00");

}
