package com.umiwi.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.UmiwiResultBeans;
import com.umiwi.ui.http.parsers.ResponseParser;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.VideoModel;
import com.umiwi.video.control.PlayerController;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.PostRequest;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.SingletonFactory;
import cn.youmi.framework.util.ToastU;


public class EditNoteFragment {
    private ImageView screenshotImageView;
    private EditText etNote;
    private PlayerController controller;
    private VideoModel video;
    private String time;

    private ProgressBar pb;

    private ProgressDialog pd;
    private Context mContext;

    private ImageLoader mImageLoader;
    private DialogPlus dialogPlus;

    private String tutoruid;


    public static EditNoteFragment getInstance() {
        return SingletonFactory.getInstance(EditNoteFragment.class);
    }

    public void showDialog(final Context mContext, String tutoruid) {
        this.tutoruid = tutoruid;
        this.mContext = mContext;
        ViewHolder holder = new ViewHolder(R.layout.fragment_edit_note);
        dialogPlus = new DialogPlus.Builder(mContext)
                .setContentHolder(holder)
                .setGravity(Gravity.CENTER)
                .setBackgroundColorResourceId(R.color.black)
                .setOnDismissListener(new com.orhanobut.dialogplus.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogPlus dialog) {
                        PlayerController.getInstance().resume();
                    }
                })
                .setMargins(0, 0, 0, 0).create();
        View view = dialogPlus.getHolderView();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        toolbar.setTitle("编辑笔记");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
            }
        });

        mImageLoader = new ImageLoader(UmiwiApplication.getApplication());

        PlayerController.getInstance().pause();

        pd = new ProgressDialog(mContext);// 创建ProgressDialog对象
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置进度条风格，风格为圆形，旋转的
        pd.setTitle("提示");// 设置ProgressDialog标题
        pd.setMessage("笔记正在上传中请稍候...");// 设置ProgressDialog提示信息
        pd.setIndeterminate(true);// 设置ProgressDialog 的进度条不明确
        pd.setCancelable(true);

        pb = (ProgressBar) view.findViewById(R.id.loading);

        etNote = (EditText) view.findViewById(R.id.content_edit_text);
        etNote.setOnKeyListener(new doneListener());

        view.findViewById(R.id.save_button).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        saveNote();
                    }
                });

        screenshotImageView = (ImageView) view.findViewById(R.id.screen_shot_image_viwe);

        initData();

        loadScreenShot();

        dialogPlus.show();
    }

    private class doneListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                return true;
            }
            return false;
        }
    }


    private void initData() {
        controller = PlayerController.getInstance();

        video = controller.getCurrentPlayerItem().getDataSource();

        int position = controller.getCurrentPosition();
        DecimalFormat formater = new DecimalFormat("#######.000");
        time = formater.format((double) position / 1000.0);
    }

    // 提交笔记
    private void saveNote() {
        String strNote = etNote.getText().toString();
        if (TextUtils.isEmpty(strNote)) {
            ToastU.showShort(UmiwiApplication.getApplication(), "笔记内容不能为空.");

            return;
        }

        // KeyboardUtils.

        try {
            uploadNote(strNote);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 上传笔记
    private void uploadNote(String strNote) throws UnsupportedEncodingException {

        pd.show();

        PostRequest<UmiwiResultBeans.ResultBeansRequestData> request = new PostRequest<UmiwiResultBeans.ResultBeansRequestData>(UmiwiAPI.SAVE_NOTE, ResponseParser.class, new NoteListner());
        //HashMap<String, String> params = new HashMap<String, String>();

		/*uid 记笔记的用户编号（必填参数）
	       categoryid 专辑的编号（必填参数）
	       videoid 视频编号（必填参数）
	       content 笔记内容（必填参数）
	       title   专辑名称（必填参数）
	       tutorid 导师编号（必填参数）
	       img     笔记配图文件的文件流
	       videotype 视频收费类型 1=>免费 2=>收费 3=> 试看*/
		
		
		/*String userid = UserManager.getInstance().getUid();
		params.put("uid", userid);
		params.put("categoryid", video.getAlbumId());
		params.put("videoid", video.getVideoId());
		params.put("content", strNote);
		params.put("title", video.getAlbumTitle());
		//params.put("tutorid", );
		params.put("img", time);
		//params.put("videotype", );
		 */

        String userid = YoumiRoomUserManager.getInstance().getUid();
        request.addParam("uid", userid);
        request.addParam("albumid", video.getAlbumId());
        request.addParam("videoid", video.getVideoId());
        request.addParam("content", strNote);
        request.addParam("tutorid", tutoruid); // 讲师id 做死
        request.addParam("title", video.getTitle());
        request.addParam("time", time);

        HttpDispatcher.getInstance().go(request);
    }

    private void loadScreenShot() {
        String shotScreenStr = String.format(UmiwiAPI.NOTE_VIDEO_SHOT,
                video.getVideoId(), time, video.getAlbumId());
        mImageLoader.loadImage(shotScreenStr, screenshotImageView);
        pb.setVisibility(View.GONE);
    }

    private class NoteListner implements
            Listener<UmiwiResultBeans.ResultBeansRequestData> {
        @Override
        public void onResult(
                AbstractRequest<UmiwiResultBeans.ResultBeansRequestData> request,
                UmiwiResultBeans.ResultBeansRequestData t) {
            if (pd.isShowing()) {
                pd.dismiss();
            }

            if (t != null && t.isSucc()) {
                ToastU.showShort(UmiwiApplication.getApplication(), t.getMsg());
                dialogPlus.dismiss();
            } else {
                ToastU.showShort(UmiwiApplication.getApplication(), t.getMsg());
            }

        }

        @Override
        public void onError(
                AbstractRequest<UmiwiResultBeans.ResultBeansRequestData> requet,
                int statusCode, String body) {
            if (pd.isShowing()) {
                pd.dismiss();
            }

            ToastU.showShort(UmiwiApplication.getApplication(), "笔记保存失败,请稍后重试.");
        }
    }

}