package com.umiwi.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.beans.AudioTmessageListBeans;
import com.umiwi.ui.beans.updatebeans.VoicePlayBean;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.ExperDetailsFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.YoumiRoomUserManager;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.framework.util.AndroidSDK;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.view.CircleImageView;


/**
 * Created by ${Gpsi} on 2017/3/11.
 */

public class VoiceDetailsAdapter extends BaseAdapter {
    public static final int ITEM_TYPE_DETAIL = 0;
    public static final int ITEM_TYPE_DIRECTORY = 1;
    public static final int ITEM_TYPE_TEACHER = 2;
    public static final int ITEM_TYPE_COMMENT_WRITE = 3;
    public static final int ITEM_TYPE_COMMENT = 4;

    private List<VoicePlayBean.RAnserVoicePlay.RDudiao> audioFileList = null;
    private FragmentActivity mActivity;
    private Context mContext;
    private String totalnum = "0";
    private VoicePlayBean.RAnserVoicePlay experDetailsVoiceBean;
    private VoicePlayBean.RAnserVoicePlay infos;
    private VoiceDetailsFragment voiceDetailsFragment;
    private List<AudioTmessageListBeans.RecordX.Record> recordList;


    public VoiceDetailsAdapter(Context context, List<VoicePlayBean.RAnserVoicePlay.RDudiao> audioFileList, VoicePlayBean.RAnserVoicePlay experDetailsVoiceBean, List<AudioTmessageListBeans.RecordX.Record> recordList, VoiceDetailsFragment voiceDetailsFragment) {
        this.mActivity = (FragmentActivity) context;
        this.mContext = context;
        this.audioFileList = audioFileList;
        this.experDetailsVoiceBean = experDetailsVoiceBean;
        this.voiceDetailsFragment = voiceDetailsFragment;
        Log.e("recordList", "recordList=" + recordList.toString());
        notifyDataSetChanged();
    }

//    public VoiceDetailsAdapter(Context context, List<AudioTmessageListBeans.RecordX.Record> recordList, VoicePlayBean.RAnserVoicePlay infos, VoiceDetailsFragment voiceDetailsFragment) {
//        this.mActivity = (FragmentActivity) context;
//        this.mContext = context;
//        this.voiceDetailsFragment = voiceDetailsFragment;
//        this.infos = infos;
//    }
    public void setData(VoicePlayBean.RAnserVoicePlay infos) {
        this.infos = infos;
        notifyDataSetChanged();
    }

    public void setRecordList(List<AudioTmessageListBeans.RecordX.Record> recordList, String totalnum) {
        //ToastU.showShort(mActivity,"刷新评论。。。");
        this.recordList = recordList;
        this.totalnum = totalnum;
        this.notifyDataSetChanged();
    }

    //    public VoiceDetailsAdapter(Context context, List<ExperDetailsVoiceBean.AudiofileBean> audioFileList, ExperDetailsVoiceBean experDetailsVoiceBean, VoiceDetailsFragment voiceDetailsFragment) {
//        this.mActivity = (FragmentActivity) context;
//        this.mContext = context;
//        this.audioFileList = audioFileList;
//        this.experDetailsVoiceBean = experDetailsVoiceBean;
//        this.voiceDetailsFragment = voiceDetailsFragment;
//    }


    @Override
    public int getCount() {
        if (audioFileList == null) {
            return 4;
        }
        if(recordList == null) {
            return 4;
        }
        if (experDetailsVoiceBean == null) {
            return 0;
        }
        return 4 + audioFileList.size() + recordList.size();

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_DETAIL;
        }
        if (position >= 1 && position < (1 + audioFileList.size())) {
            return ITEM_TYPE_DIRECTORY;
        }
        if (position == (1 + audioFileList.size())) {
            return ITEM_TYPE_TEACHER;
        }
        if (position == (2 + audioFileList.size())) {
            return ITEM_TYPE_COMMENT_WRITE;
        } else {
            return ITEM_TYPE_COMMENT;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == ITEM_TYPE_DETAIL) {
            if (convertView == null) {
                convertView = LayoutInflater.from(UmiwiApplication.getApplication()).
                        inflate(R.layout.item_audio_detail_layout, null);
            }
            configureDetailItem(convertView, position);
        }
        if (getItemViewType(position) == ITEM_TYPE_DIRECTORY) {
            if (convertView == null) {
                convertView = LayoutInflater.from(UmiwiApplication.getApplication())
                        .inflate(R.layout.voice_directory_item, null);
            }
            configureDirectoryItem(convertView, position);
        }
        if (getItemViewType(position) == ITEM_TYPE_TEACHER) {
            if (convertView == null) {
                convertView = LayoutInflater.from(UmiwiApplication.getApplication())
                        .inflate(R.layout.item_audio_teacher_layout, null);
            }
            configuretTeacherItem(convertView, position);
        }
        if (getItemViewType(position) == ITEM_TYPE_COMMENT_WRITE) {
            if (convertView == null) {
                convertView = LayoutInflater.from(UmiwiApplication.getApplication()).inflate(R.layout.item_course_comment_write_layout, null);
            }
            configureCommentWriteItme(convertView, position);
        }
        if (getItemViewType(position) == ITEM_TYPE_COMMENT) {
            if (convertView == null) {
                convertView = LayoutInflater.from(UmiwiApplication.getApplication()).inflate(R.layout.item_comment, null);
            }
            configureCommentItme(convertView, position);
        }
        return convertView;
    }

    //评论列表
    private void configureCommentItme(View view, int position) {
        int index = position - (3 + audioFileList.size());
        if (index < 0) {
            return;
        }

        if (position + 1 >= getCount()) {
            return;
        }

        CircleImageView iconImageView = (CircleImageView) view.findViewById(R.id.userhead_imageview);

        TextView nameTextView = (TextView) view.findViewById(R.id.username_textview);
        TextView timeTextView = (TextView) view.findViewById(R.id.time_textview);
        TextView contentTextView = (TextView) view.findViewById(R.id.content_textview);
        AudioTmessageListBeans.RecordX.Record record = recordList.get(index);
        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        mImageLoader.loadImage(record.getAvatar(), iconImageView);
        nameTextView.setText(record.getName());
        timeTextView.setText(record.getTime());
        contentTextView.setText(record.getContent());


    }

    View.OnClickListener writeCommentViewOnClickListener = null;

    public void setWriteCommenntViewOnClickListener(View.OnClickListener l) {
        this.writeCommentViewOnClickListener = l;
    }

    //评论
    private void configureCommentWriteItme(View view, final int position) {

        TextView commentNum = (TextView) view.findViewById(R.id.comment_num);
        CircleImageView header = (CircleImageView) view.findViewById(R.id.header);
        TextView writeComment = (TextView) view.findViewById(R.id.write_button);


        commentNum.setText("评论 (" + totalnum + ")");
        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        mImageLoader.loadImage(YoumiRoomUserManager.getInstance().getUser().getAvatar(), header, R.drawable.fragment_mine_photo);
        writeComment.setOnClickListener(writeCommentViewOnClickListener);
    }

    //讲师
    private void configuretTeacherItem(View view, int position) {
        TextView authorNameTextView = (TextView) view.findViewById(R.id.author_name_text_view);
        authorNameTextView.setText(experDetailsVoiceBean.getName());
        Log.e("TAG", "experDetailsVoiceBean.getName()=" + experDetailsVoiceBean.getName());
        TextView authorTitle = (TextView) view.findViewById(R.id.author_short_description_text_view);
//        if (!TextUtils.isEmpty(experDetailsVoiceBean.getTutortitle())) {
//            if (experDetailsVoiceBean.getTutortitle().length() >= 16) {
//                authorTitle.setText(experDetailsVoiceBean.getTutortitle().subSequence(0, 14) + "...");
//            } else {
//            }
//        }
                authorTitle.setText(experDetailsVoiceBean.getTutortitle());
        CircleImageView autorAvatarImageView = (CircleImageView) view.findViewById(R.id.author_avatar_image_view);
        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        mImageLoader.loadImage(experDetailsVoiceBean.getTutorimage(), autorAvatarImageView);
        RelativeLayout lectureContainer = (RelativeLayout) view.findViewById(R.id.lecture_container);

        lectureContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mActivity, "点击进入详细页面", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ExperDetailsFragment.class);
                intent.putExtra(ExperDetailsFragment.KEY_DEFAULT_TUTORUID, experDetailsVoiceBean.getUid());
                mActivity.startActivity(intent);

            }
        });
    }

    //目录
    private void configureDirectoryItem(View view, int position) {
        int index = position - 1;

        if (index < 0) {
            return;
        }
        final VoicePlayBean.RAnserVoicePlay.RDudiao bean = audioFileList.get(index);
        TextView tv_dir_title = (TextView) view.findViewById(R.id.tv_dir_title);
        TextView tv_dir_totaltime = (TextView) view.findViewById(R.id.tv_dir_totaltime);
        TextView try_play = (TextView) view.findViewById(R.id.try_play);
        if (bean.isTry1()){
            try_play.setVisibility(View.VISIBLE);
        }else {
            try_play.setVisibility(View.INVISIBLE);
        }
        tv_dir_title.setText(bean.getAtitle());
        tv_dir_totaltime.setText(bean.getAplaytime());

        view.setTag(bean);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mActivity, "点击播放试听", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "experDetailsVoiceBean.isIspay()=" + experDetailsVoiceBean.getIspay());
//                voiceDetailsFragment.bindVoiceSerive();
                if(experDetailsVoiceBean.getIspay()) {
                    voiceDetailsFragment.bindVoiceSerive();
                }
                if (bean.isTry1()) {
                    //试听
                    voiceDetailsFragment.startPlayer.setClickable(true);
                    voiceDetailsFragment.sb_seekbar.setVisibility(View.VISIBLE);
                    voiceDetailsFragment.getData(bean.getSource());
//                    UmiwiApplication.mainActivity.url = bean.getSource();
                } else {
                    voiceDetailsFragment.startPlayer.setClickable(false);
                }
            }
        });
    }

    //详情
    private void configureDetailItem(View view, int position) {
        final View tempView = view;

        TextView courseTitle = (TextView) view.findViewById(R.id.course_title);
        courseTitle.setText(experDetailsVoiceBean.getTitle());
        TextView videoWatches = (TextView) view.findViewById(R.id.video_watches);
        if (TextUtils.isEmpty(experDetailsVoiceBean.getPlaytime())) {
            videoWatches.setText("播放 : " + experDetailsVoiceBean.getWatchnum());
        } else {
            videoWatches.setText(experDetailsVoiceBean.getOnlinetime() + "更新" + "\t 播放 : " + experDetailsVoiceBean.getWatchnum());
        }

        final TextView courseDescription = (TextView) view.findViewById(R.id.course_description);
        TextView course_description1 = (TextView) view.findViewById(R.id.course_description1);
        ImageView iv_voice_detail1 = (ImageView) view.findViewById(R.id.iv_voice_detail1);
        ImageView iv_voice_detail2 = (ImageView) view.findViewById(R.id.iv_voice_detail2);
        TextView course_description2 = (TextView) view.findViewById(R.id.course_description2);



        if (experDetailsVoiceBean.getContent() == null
                || experDetailsVoiceBean.getContent().size() == 0) {
            course_description1.setVisibility(View.GONE);
            iv_voice_detail1.setVisibility(View.GONE);
            iv_voice_detail2.setVisibility(View.GONE);
            course_description2.setVisibility(View.GONE);
        } else {
            //显示简介数据
            ArrayList<VoicePlayBean.RAnserVoicePlay.Rcontent> content =
                    experDetailsVoiceBean.getContent();
            if(content.size()==1) {
                course_description1.setVisibility(View.VISIBLE);
                course_description1.setText(content.get(0).getWord());
            }
            if(content.size()==2) {
                course_description1.setVisibility(View.VISIBLE);
                course_description1.setText(content.get(0).getWord());
                iv_voice_detail1.setVisibility(View.VISIBLE);
                ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
                mImageLoader.loadImage(content.get(1).getImage(), iv_voice_detail1);
            }
            if(content.size() == 3) {
                course_description1.setVisibility(View.VISIBLE);
                course_description1.setText(content.get(0).getWord());
                iv_voice_detail1.setVisibility(View.VISIBLE);
                ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
                mImageLoader.loadImage(content.get(1).getImage(), iv_voice_detail1);
                course_description2.setVisibility(View.VISIBLE);
                course_description2.setText(content.get(2).getWord());
            }
            if(content.size() == 4) {
                course_description1.setVisibility(View.VISIBLE);
                course_description1.setText(content.get(0).getWord());
                iv_voice_detail1.setVisibility(View.VISIBLE);
                ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
                mImageLoader.loadImage(content.get(1).getImage(), iv_voice_detail1);
                course_description2.setVisibility(View.VISIBLE);
                course_description2.setText(content.get(2).getWord());
                iv_voice_detail2.setVisibility(View.VISIBLE);
                mImageLoader.loadImage(content.get(3).getImage(), iv_voice_detail2);
            }

        }


        final ImageView moreBar = (ImageView) view.findViewById(R.id.more_detail_imageview);

        courseDescription.setMaxLines(8);
        /*if (courseDescription.getLineCount() > 6) {
            moreBar.setVisibility(View.VISIBLE);
		}
		courseDescription.setMaxLines(6);*/

        courseDescription.getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            int lines = 0;

                            @SuppressWarnings("deprecation")
                            @Override
                            public void onGlobalLayout() {
                                lines = courseDescription.getLineCount();
                                if (AndroidSDK.isJELLY_BEAN()) {
                                    courseDescription
                                            .getViewTreeObserver()
                                            .removeOnGlobalLayoutListener(
                                                    this);
                                } else {
                                    courseDescription.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                }

                                if (lines != 0 && lines > 6) {

                                    courseDescription.setMaxLines(6);
                                    courseDescription
                                            .setEllipsize(TextUtils.TruncateAt.END);

                                    moreBar.setVisibility(View.VISIBLE);
                                    moreBar.setBackgroundDrawable(UmiwiApplication.getApplication().getResources().getDrawable(R.drawable.description_show_all_bg));

                                    tempView.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            if (courseDescription.getMaxLines() <= 6) {
                                                courseDescription.setLines(Integer.MAX_VALUE);
                                                courseDescription.setMinLines(0);
                                                moreBar.setBackground(UmiwiApplication.getApplication().getResources().getDrawable(
                                                        R.drawable.description_show_all_bg));
                                            } else {
                                                courseDescription.setMaxLines(6);
                                                moreBar.setBackground(UmiwiApplication.getApplication().getResources().getDrawable(
                                                        R.drawable.description_hide_up_bg));
                                            }
                                        }
                                    });
                                } else {
                                    courseDescription.setMaxLines(6);
                                    moreBar
                                            .setVisibility(View.GONE);
                                }
                            }
                        });

    }


}
