package com.umiwi.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.beans.CommentListBeans;
import com.umiwi.ui.beans.UmiwiListBeans;
import com.umiwi.ui.beans.UmiwiListDetailBeans;
import com.umiwi.ui.beans.UmiwiListDetailBeans.ListDetailRequestData;
import com.umiwi.ui.fragment.LecturerDetailFragment;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.fragment.pay.PayOrderDetailFragment;
import com.umiwi.ui.fragment.pay.PayTypeEvent;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.StatisticsManager;
import com.umiwi.ui.managers.StatisticsUrl;
import com.umiwi.ui.managers.VideoManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.VideoModel;
import com.umiwi.ui.util.LoginUtil;
import com.umiwi.video.control.PlayerController;
import com.umiwi.video.control.PlayerController.PlayerItem;

import java.util.ArrayList;

import cn.youmi.framework.util.AndroidSDK;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.view.CircleImageView;

public class CourseDetailsAdapter extends BaseAdapter {

    public static final int ITEM_TYPE_DETAIL = 0;
    public static final int ITEM_TYPE_VIDEO = 1;
    public static final int ITEM_TYPE_TEACHER = 2;
    public static final int ITEM_TYPE_RELATE = 3;
    public static final int ITEM_TYPE_COMMENT_WRITE = 4;
    public static final int ITEM_TYPE_COMMENT = 5;

    private FragmentActivity mActivity;
    private Context mContext;
    private CourseDetailPlayFragment courseDetailLayoutFragments;

    private String commentNumStr = "0";

    private ArrayList<CommentListBeans> mList = null;

    public ArrayList<UmiwiListBeans> mRelatedList = null;

    private ListDetailRequestData mRequestData = null;

    public ArrayList<VideoModel> playList = null;

    private VideoModel updateVideo = null;

    LinearLayout mCourseContainer;

    private View containner;

    public CourseDetailsAdapter(Context context) {
        this.mActivity = (FragmentActivity) context;
        this.mContext = context;
    }

    public CourseDetailsAdapter(Context context, ArrayList<CommentListBeans> mList, CourseDetailPlayFragment courseDetailLayoutFragments) {
        this.mActivity = (FragmentActivity) context;
        this.mContext = context;
        this.mList = mList;

        this.courseDetailLayoutFragments = courseDetailLayoutFragments;
        notifyDataSetChanged();
    }

    public void setDetail(ListDetailRequestData detailBean) {
        this.mRequestData = detailBean;
        notifyDataSetChanged();

    }

    public void setRelatedVideos(ArrayList<UmiwiListBeans> mRelatedList) {
        this.mRelatedList = mRelatedList;
        notifyDataSetChanged();

    }

    public void setCommentNum(String commentNum) {
        this.commentNumStr = commentNum;
        notifyDataSetChanged();

    }

    public UmiwiListBeans getRelateVideos(int position) {
        int index = position - 3;
        UmiwiListBeans relate = mRelatedList.get(index);
        return relate;
    }

    public CommentListBeans getCommentList(int position) {
        int index = position - (3 + mRelatedList.size());
        CommentListBeans comment = mList.get(index);
        return comment;
    }


    @Override
    public int getCount() {
        if (mRelatedList == null) {
            return 0;
        }
        if (mRequestData == null) {
            return 0;
        }
        if (mList == null) {
            return 10;
        }
        return 10 + mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

//	@Override
//	public boolean isEnabled(int position) {
//		int itemType = getItemViewType(position);
//		return itemType == 2 || itemType == 3;
//	}

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_DETAIL;
        }
        if (position == 1) {
            return ITEM_TYPE_VIDEO;
        }
        if (position == 2) {
            return ITEM_TYPE_TEACHER;
        }
        if (position >= 3 && position < (3 + mRelatedList.size())) {
            return ITEM_TYPE_RELATE;
        }
        if (position == (3 + mRelatedList.size())) {
            return ITEM_TYPE_COMMENT_WRITE;
        }
        return ITEM_TYPE_COMMENT;
    }

    @Override
    public int getViewTypeCount() {
        return 6;
    }

    private View video1Item;

    private void configureDetailItem(View view, int position) {
        final View tempView = view;

        TextView courseTitle = (TextView) view.findViewById(R.id.course_title);
        courseTitle.setText(mRequestData.getTitle());
        TextView videoWatches = (TextView) view.findViewById(R.id.video_watches);
        if (TextUtils.isEmpty(mRequestData.getRecordTime())) {
            videoWatches.setText("播放 : " + mRequestData.getWatchNum());
        } else {
            videoWatches.setText("播放 : " + mRequestData.getWatchNum() + "\t 录制时间 : " + mRequestData.getRecordTime());
        }

        final TextView courseDescription = (TextView) view.findViewById(R.id.course_description);
        String description = "";
        if (mRequestData.getSummary() == null
                || mRequestData.getSummary().length() == 0) {
            description = mRequestData.getIntroduce();
        } else {
            description = mRequestData.getSummary();
        }
        courseDescription.setText(description);

        final ImageView moreBar = (ImageView) view.findViewById(R.id.more_detail_imageview);

        courseDescription.setMaxLines(8);
        /*if (courseDescription.getLineCount() > 6) {
            moreBar.setVisibility(View.VISIBLE);
		}
		courseDescription.setMaxLines(6);*/

        courseDescription.getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        new OnGlobalLayoutListener() {
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

                                    tempView.setOnClickListener(new OnClickListener() {

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

    @SuppressWarnings("deprecation")
    private void configureVideoItem(View view, int position) {

        mCourseContainer = (LinearLayout) view.findViewById(R.id.courses_container);
        if (mCourseContainer.getChildCount() >= 1) {
            mCourseContainer.removeAllViews();
        }

        int duration = 0;
        int currentVideoIndex = -1;
        PlayerItem playItem = PlayerController.getInstance().getCurrentPlayerItem();

        if (!mRequestData.getCourse().isEmpty() && mRequestData.getCourse().size() > 1) {
            View contentsTextView = LayoutInflater.from(UmiwiApplication.getInstance())
                    .inflate(R.layout.item_course_video_contents, null);
            mCourseContainer.addView(contentsTextView);
        }

        // PlayerController.getInstance().getPlayerView().setDisplay();

        if (playItem != null) {
            VideoModel currentVideo = playItem.getDataSource();
            for (int i = 0; i < mRequestData.getCourse().size(); i++) {
                UmiwiListDetailBeans c = mRequestData.getCourse().get(i);
                if ((c.getVid()+"").equalsIgnoreCase(currentVideo.getVideoId())) {
                    currentVideoIndex = i;
                    break;
                }
            }
            if (mRequestData.getTryvideo() != null) {
                for (int i = 0; i < mRequestData.getTryvideo().size(); i++) {
                    UmiwiListDetailBeans c = mRequestData.getTryvideo().get(i);
                    if ((c.getVid()+"").equalsIgnoreCase(currentVideo.getVideoId())) {
                        currentVideoIndex = i;
                        break;
                    }
                }
            }
        }

        mCourseContainer.setTag(R.id.key_play_list_count, mRequestData.getCourse().size());

        for (int i = 0; i < mRequestData.getCourse().size(); i++) {
            UmiwiListDetailBeans c = mRequestData.getCourse().get(i);
            View item = LayoutInflater.from(UmiwiApplication.getInstance()).inflate(R.layout.item_course_video, null);
            /*CirclePercentageView cpv = (CirclePercentageView) item
                    .findViewById(R.id.watched_percentage_view);
			cpv.drawLine = true;
			cpv.setPercentage(c.getWatchProgress());*/

            ImageView videoStutsImageView = (ImageView) item.findViewById(R.id.video_stuts_image_view);

            videoStutsImageView.setBackgroundDrawable(UmiwiApplication.getApplication().getResources().getDrawable(R.drawable.video_item_pause_bg));

            TextView titleTextView = (TextView) item.findViewById(R.id.title_text_view);
            titleTextView.setText(c.getTitle());
            TextView subtitleTextView = (TextView) item.findViewById(R.id.subtitle_text_view);
            subtitleTextView.setText("时长:" + c.getDuration() / 60 + "分钟");// TODO

            item.setTag(R.id.key_video_item_position, i);
            item.setOnClickListener(videoItemOnClickListener);

            mCourseContainer.addView(item);

            duration += c.getDuration() / 60;

            //if (i != mRequestData.getCourse().size() - 1) {
            View seperatorLine = LayoutInflater.from(
                    UmiwiApplication.getInstance()).inflate(
                    R.layout.item_line, null);
            mCourseContainer.addView(seperatorLine);
            //}

            if (currentVideoIndex == i) {
                int color = Color.rgb(255, 102, 00);
                videoStutsImageView.setBackgroundDrawable(UmiwiApplication.getApplication().getResources().getDrawable(R.drawable.video_item_play_bg));
                titleTextView.setTextColor(color);
                subtitleTextView.setTextColor(color);
            }

            if (i == 0) {
                video1Item = item;
            }
        }

        if (mRequestData.getCourse().size() == 1) {
            mCourseContainer.removeAllViews();
        }

    }

    private void configureTeacherItem(View view, int position) {
        TextView authorNameTextView = (TextView) view.findViewById(R.id.author_name_text_view);
        authorNameTextView.setText(mRequestData.getAuthorNameRaw());
        TextView authorTitle = (TextView) view.findViewById(R.id.author_short_description_text_view);
        if (!TextUtils.isEmpty(mRequestData.getAuthorTitle())) {
            if (mRequestData.getAuthorTitle().length() >= 16) {
                authorTitle.setText(mRequestData.getAuthorTitle().subSequence(0, 14) + "...");
            } else {
                authorTitle.setText(mRequestData.getAuthorTitle());
            }
        }
        CircleImageView autorAvatarImageView = (CircleImageView) view.findViewById(R.id.author_avatar_image_view);
        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        mImageLoader.loadImage(mRequestData.getAuthorAvatar(), autorAvatarImageView);
        RelativeLayout lectureContainer = (RelativeLayout) view.findViewById(R.id.lecture_container);
//        lectureContainer.setOnClickListener(mLectureOnClickListener);

    }

    private void configureRelateCourseItme(View view, int position) {
        int index = position - 3;

        if (index < 0) {
            return;
        }
        UmiwiListBeans video = mRelatedList.get(index);

        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        TextView authorNameTextView = (TextView) view.findViewById(R.id.authorname);

        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        mImageLoader.loadImage(video.getImage(), imageView);

        titleTextView.setText(video.getTitle());
        authorNameTextView.setText(video.getAuthorname());

        view.setTag(video);
        view.setOnClickListener(relateCourseClickListener);
    }

    private void configureCommentWriteItme(View view, int position) {
        TextView commentNum = (TextView) view.findViewById(R.id.comment_num);
        CircleImageView header = (CircleImageView) view.findViewById(R.id.header);
        TextView writeComment = (TextView) view.findViewById(R.id.write_button);
        writeComment.setOnClickListener(writeCommentViewOnClickListener);

        commentNum.setText("评论 (" + commentNumStr + ")");
        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        mImageLoader.loadImage(YoumiRoomUserManager.getInstance().getUser().getAvatar(), header, R.drawable.fragment_mine_photo);
    }

    private void configureCommentItme(View view, int position) {
        int index = position - (4 + mRelatedList.size());
        if (index < 0) {
            return;
        }

        if (position + 1 >= getCount()) {
            return;
        }

        CommentListBeans commentBean = mList.get(index);

//		View commentLayout = view.findViewById(R.id.comment_layout);
//		commentLayout.setOnClickListener(commentViewOnClickListener);
//		commentLayout.setTag(commentBean);

        ImageView iconImageView = (ImageView) view.findViewById(R.id.userhead_imageview);

        TextView nameTextView = (TextView) view.findViewById(R.id.username_textview);
        TextView timeTextView = (TextView) view.findViewById(R.id.time_textview);
        TextView contentTextView = (TextView) view.findViewById(R.id.content_textview);

        nameTextView.setText(commentBean.getUsername());
        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        mImageLoader.loadImage(commentBean.getHeadimg(), iconImageView);

        timeTextView.setText(commentBean.getCtime().split(" ")[0]);
        contentTextView.setText(commentBean.getQuestion());
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        if (getItemViewType(position) == ITEM_TYPE_DETAIL) {
            if (convertView == null) {
                convertView = LayoutInflater.from(UmiwiApplication.getApplication()).inflate(R.layout.item_course_detail_layout, null);
            }
            configureDetailItem(convertView, position);
        }

        if (getItemViewType(position) == ITEM_TYPE_VIDEO) {
            if (convertView == null) {
                convertView = LayoutInflater.from(UmiwiApplication.getApplication()).inflate(R.layout.item_course_video_layout, null);
            }

            containner = convertView;

            configureVideoItem(convertView, position);
        }

        if (getItemViewType(position) == ITEM_TYPE_TEACHER) {
            if (convertView == null) {
                convertView = LayoutInflater.from(UmiwiApplication.getApplication()).inflate(R.layout.item_course_teacher_layout, null);
            }
            configureTeacherItem(convertView, position);
        }

        if (getItemViewType(position) == ITEM_TYPE_RELATE) {
            if (convertView == null) {
                convertView = LayoutInflater.from(UmiwiApplication.getApplication()).inflate(R.layout.item_course, null);
            }
            configureRelateCourseItme(convertView, position);
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

    public interface VideoClickListener {
        void setVideoClickListener(Pair<VideoModel, Boolean> result);
    }

    VideoClickListener mVideoItemClickListener;

    public VideoClickListener getmVideoItemClickListener() {
        return mVideoItemClickListener;
    }

    public void setmVideoItemClickListener(VideoClickListener mVideoItemClickListener) {
        this.mVideoItemClickListener = mVideoItemClickListener;
    }

    OnClickListener videoItemOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {

            int index = (Integer) view.getTag(R.id.key_video_item_position);
            /*Log.e("Umiwi", "onClick index = " + index);

			int count = (Integer) mCourseContainer.getTag(R.id.key_play_list_count);*/

            if (mRequestData.isIsbuy()) {
                /*if (mRequestData.isDobindmobile()) {
                    showBindPhone();
					return;
				}*/
                MobclickAgent.onEvent(mContext, "详情大纲区V", "播放");
            } else {
                if (YoumiRoomUserManager.getInstance().getUser().isLogout()) {
                    LoginUtil.getInstance().showLoginView(mContext);
                    MobclickAgent.onEvent(mContext, "详情大纲区V", "登录");
                    return;
                } else {
                    showJoinVipDialog("2");
                    MobclickAgent.onEvent(mContext, "详情大纲区V", "购买");
                    return;
                }
            }

			/*for(int i = 0; i < count; i++) {
                View itemView = mCourseContainer.findViewWithTag(i+"");
				ImageView videoStutsImageView = (ImageView) itemView
						.findViewById(R.id.video_stuts_image_view);
				TextView titleTextView = (TextView) itemView
						.findViewById(R.id.title_text_view);
				TextView subtitleTextView = (TextView) itemView
						.findViewById(R.id.subtitle_text_view);

				if(i != index) {
					videoStutsImageView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.video_item_pause_bg));
					titleTextView.setTextColor(Color.BLACK);
					subtitleTextView.setTextColor(Color.BLACK);
				} else {
					int color = Color.rgb(255, 102, 00);

					videoStutsImageView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.video_item_play_bg));
					titleTextView.setTextColor(color);
					subtitleTextView.setTextColor(color);
				}

			}*/

            //mRequestData.isIsbuy();
            Pair<VideoModel, Boolean> result = selectCourseAt(index, true);
            if (mVideoItemClickListener != null) {
                mVideoItemClickListener.setVideoClickListener(selectCourseAt(index, true));
            }
//            VideoModel video = null;
//            if (result != null) {
//                video = result.first;
//            }
//            if (video != null && !result.second) {
//                PlayerItem currentItem = PlayerController.getInstance()
//                        .getCurrentPlayerItem();
//                if (currentItem != null
//                        && video.equals(currentItem.getDataSource())) {
//                    PlayStatus ps = PlayerController.getInstance()
//                            .getPlayStatus();
//                    if (ps == PlayStatus.PLAYING || ps == PlayStatus.PAUSED
//                            || ps == PlayStatus.PREPARING) {
//                        return;
//                    }
//
//                }
//                PlayerController.getInstance().setDataSource(video);
//                if (!result.second) {
//                    PlayerController.getInstance().setPlayList(playList);
//                } else {
//                    PlayerController.getInstance().setPlayList(null);
//                }
//                if (PlayerController.supportMultipleScreen()) {
//                    PlayerController.getInstance().prepareAndStart();
//                } else {
//                    Intent i = new Intent(mContext, PlayerActivity.class);
//                    mContext.startActivity(i);
//                }
//            }

            notifyDataSetChanged();
        }
    };

    public Pair<VideoModel, Boolean> selectCourseAt(int index,
                                                    boolean showDialog) {
        long vid = 0;
        boolean tryVideo = false;

        playList = new ArrayList<VideoModel>();
        if (mRequestData.isIsbuy()) {
            vid = mRequestData.getCourse().get(index).getVid();
            for (UmiwiListDetailBeans db : mRequestData.getCourse()) {
                VideoModel dm = VideoManager.getInstance().getVideoById(
                        db.getVid() + "");
                playList.add(dm);
            }
            Log.e("playList", "playList=" + playList.get(0).toString());
        } else {
            tryVideo = true;
            if (index >= -1) {

            }

            if (mRequestData.getTryvideo().isEmpty()) {
                return null;
            }
            vid = mRequestData.getTryvideo().get(index).getVid();

            for (UmiwiListDetailBeans db : mRequestData.getTryvideo()) {
                VideoModel dm = VideoManager.getInstance().getVideoById(
                        db.getVid() + "");
                playList.add(dm);
            }
        }
        updateVideo = VideoManager.getInstance().getVideoById(
                String.valueOf(vid));
        return new Pair<VideoModel, Boolean>(updateVideo, tryVideo);
    }

    public void showJoinVipDialog(String spm_location) {
        String vPrice = mRequestData.getPrice();
        int vAlubmId = mRequestData.getId();

        if (!mRequestData.isCanbuy()) {

            Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
            intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayOrderDetailFragment.class);
            intent.putExtra(PayOrderDetailFragment.KEY_ORDER_ID, "23");
            intent.putExtra(PayOrderDetailFragment.KEY_ORDER_TYPE, PayTypeEvent.VIP);
            intent.putExtra(PayOrderDetailFragment.KEY_SPM, String.format(StatisticsUrl.ORDER_VIP_DETAIL, spm_location, mRequestData.getId()));
            mContext.startActivity(intent);

            return;
        }
        if (Float.valueOf(vPrice).intValue() < 100f) {

            Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
            intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayOrderDetailFragment.class);
            intent.putExtra(PayOrderDetailFragment.KEY_ORDER_ID, vAlubmId + "");
            intent.putExtra(PayOrderDetailFragment.KEY_ORDER_TYPE, PayTypeEvent.ALBUM);
            intent.putExtra(PayOrderDetailFragment.KEY_SPM, String.format(StatisticsUrl.ORDER_COURSE_DETAIL, spm_location, mRequestData.getId(), vPrice));
            mContext.startActivity(intent);

        } else {
//			BuyCourseDialog d = new BuyCourseDialog();
//			if (mRequestData.isCanbuy()) {
//				d.price = vPrice;
//				d.alubmId = vAlubmId;
//			}
//			mContext.showDialog(d);
        }

    }


    private OnClickListener mLectureOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            setPlayerPauseIfPlaying();

            Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
            intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LecturerDetailFragment.class);
            intent.putExtra(LecturerDetailFragment.KEY_DEFAULT_DETAILURL, mRequestData.getCourseurl());
            mContext.startActivity(intent);

            MobclickAgent.onEvent(mContext, "详情页面V", "讲师点击");
        }
    };

    OnClickListener commentViewOnClickListener = null;

    public void setCommentViewOnClickListener(OnClickListener l) {
        this.commentViewOnClickListener = l;
    }

    OnClickListener writeCommentViewOnClickListener = null;

    public void setWriteCommenntViewOnClickListener(OnClickListener l) {
        this.writeCommentViewOnClickListener = l;
    }

    private void setPlayerPauseIfPlaying() {
        PlayerController.getInstance().pause();
    }

    OnClickListener relateCourseClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            UmiwiListBeans video = (UmiwiListBeans) v.getTag();
            courseDetailLayoutFragments.loadingVideo = true;
            PlayerController.getInstance().pause();
            PlayerController.getInstance().stop();
            PlayerController.getInstance().onComplete();
            PlayerController.getInstance().showPrepareLoading();
            courseDetailLayoutFragments.detaiURL = video.getDetailurl();
            courseDetailLayoutFragments.onLoadDetailData();

            MobclickAgent.onEvent(mActivity, "详情页面V", "相关课程");
            StatisticsManager.getInstance().getResultInfo(video.getSpmurl());// 统计
        }
    };

    public void notifayVideoItem() {//TODO
        configureVideoItem(containner, 1);
        containner.invalidate();
    }
}
