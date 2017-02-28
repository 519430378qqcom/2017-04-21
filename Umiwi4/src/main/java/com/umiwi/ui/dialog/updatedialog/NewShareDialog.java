package com.umiwi.ui.dialog.updatedialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.umiwi.ui.R;
import com.umiwi.video.control.PlayerController;

import cn.youmi.framework.util.SingletonFactory;
import cn.youmi.share.ShareUtils;
import cn.youmi.share.ShareUtils.ShareModle;

/**
 * @author tangxiyong
 * @version 2015-5-22 下午2:33:33
 */
public class NewShareDialog {

    private Context mContext;
    private String title;
    private String content;
    private String shareUrl;
    private String imageUrl;
    private String appName;


    public static NewShareDialog getInstance() {
        return SingletonFactory.getInstance(NewShareDialog.class);
    }

    public void showDialog(Context mContext, String title, String content,
                           String shareUrl, String imageUrl) {

        this.mContext = mContext;
        this.title = title;
        this.content = content;
        this.shareUrl = shareUrl;
        this.imageUrl = imageUrl;
        appName = mContext.getResources().getString(R.string.app_name);


        ViewHolder holder = new ViewHolder(R.layout.new_dialog_share);
        final DialogPlus dialogPlus = new DialogPlus.Builder(mContext)
                .setContentHolder(holder)
                .setGravity(Gravity.BOTTOM)
                .setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss(DialogPlus dialog) {
                        if (PlayerController.getInstance() != null) {
                            PlayerController.getInstance().resume();
                        }
                    }
                })
                .create();

        View view = dialogPlus.getHolderView();
        view.findViewById(R.id.sinaweibo).setOnClickListener(SinaWeiboClick);
        view.findViewById(R.id.wechatfriends).setOnClickListener(WechatFriendsClick);
        view.findViewById(R.id.qq).setOnClickListener(QQClick);
        view.findViewById(R.id.new_share_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
            }
        });

        dialogPlus.show();
    }

    OnClickListener SinaWeiboClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            ShareUtils.Share(mContext, appName, title, content, shareUrl, imageUrl, ShareModle.SinaWeibo);
        }
    };
    OnClickListener WechatFriendsClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            ShareUtils.Share(mContext, appName, title, content, shareUrl, imageUrl, ShareModle.WechatFriends);
        }
    };
    OnClickListener QQClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            ShareUtils.Share(mContext, appName, title, content, shareUrl, imageUrl, ShareModle.QQ);
        }
    };


}
