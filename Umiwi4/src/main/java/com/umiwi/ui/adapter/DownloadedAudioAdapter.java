package com.umiwi.ui.adapter;

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
import com.umiwi.ui.model.AudioModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DownloadedAudioAdapter extends BaseAdapter {
    private ArrayList<AudioModel> audios;
    private Boolean trashStatus = false;
    private SparseBooleanArray checkedVideoIds = new SparseBooleanArray();

    public void setVideos(ArrayList<AudioModel> videos) {
        this.audios = videos;
        this.notifyDataSetChanged();
    }

    public ArrayList<AudioModel> getAudios() {
        return this.audios;
    }

    public Boolean getTrashStatus() {
        return trashStatus;
    }

    public void toggleTrashStatus() {
        initCheckedVideoIds();
        trashStatus = trashStatus ? false : true;
    }

    public DownloadedAudioAdapter(Context context, ArrayList<AudioModel> audios) {
        super();
        this.audios = audios;
    }


    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View v, ViewGroup arg2) {
        AudioModel audio = (AudioModel) getItem(position);

        final String audiofilesize = audio.getFileSize() + "";
        final String audiovideoid = audio.getVideoId();
        LayoutInflater layoutInflater = LayoutInflater.from(UmiwiApplication
                .getInstance());
        if (audiovideoid == null) {
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
            titleTextView.setText(audio.getTitle().trim());
            filesizeTextView.setText(df.format((Long.parseLong(audiofilesize))
                    / (1024 * 1024))
                    + "MB");
            CheckBox videoCheckBox = (CheckBox) v
                    .findViewById(R.id.video_checkbox);
            ImageView playImageView = (ImageView) v
                    .findViewById(R.id.play_imageview);

            videoCheckBox.setTag(audiovideoid);
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
        v.setTag(R.id.videotitle_textview,audio.getVideoId());
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
//        if (audios != null && audios.size() > 0) {
//            for (int i = 0; i < audios.size(); i++) {
//                int albumId = Integer.parseInt((videos.get(i)).getAlbumId());
//                checkedVideoIds.put(albumId, false);
//            }
//        }
        if(audios != null && audios.size() > 0) {
            for (int i = 0;i <audios.size(); i ++){
                int albumId = Integer.parseInt(audios.get(i).getAlbumId());
                checkedVideoIds.put(albumId,false);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return audios.size();
    }

    @Override
    public Object getItem(int position) {
        return audios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    DecimalFormat df = new DecimalFormat("0.00");

}
