package com.umiwi.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.CommentListBeans;
import com.umiwi.ui.beans.CommentResultBean;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.StatisticsUrl;
import com.umiwi.ui.view.ResizeRelativeLayout;
import com.umiwi.video.control.PlayerController;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.PostRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.SingletonFactory;
import cn.youmi.framework.util.ToastU;


/**
 * @author tangxiyong
 * @version 2015-5-18 下午2:23:37
 */
public class WriteCommentDialog {

    private EditText writeComment;
    private String albumId;

    public static WriteCommentDialog getInstance() {
        return SingletonFactory.getInstance(WriteCommentDialog.class);
    }

    public void showDialog(final Context mContext, String albumId) {
        this.albumId = albumId;
        ViewHolder holder = new ViewHolder(R.layout.dailog_comment_write_layout);

        DialogPlus dialogPlus = new DialogPlus.Builder(mContext)
                .setContentHolder(holder)
                .setGravity(Gravity.BOTTOM)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.send_comment:
                                if (TextUtils.isEmpty(writeComment.getText().toString())) {
                                    ToastU.showShort(mContext, "写点什么吧！");
                                    return;
                                }
                                showComment();
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
        View view = dialogPlus.getHolderView();
        writeComment = (EditText) view.findViewById(R.id.write_comment_edittext);
        dialogPlus.show();
//		KeyboardUtils.showKeyboard(writeComment);
        listenerKeyBoardState(view);
        writeComment.setFocusable(true);
        writeComment.setFocusableInTouchMode(true);
        writeComment.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) writeComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(writeComment, InputMethodManager.RESULT_SHOWN);
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private boolean mIsKeyboardOpened;// 软键盘是否显示
    private int mMenuOpenedHeight;// 编辑菜单打开时的高度

    private void listenerKeyBoardState(View menuLayout) {
        ResizeRelativeLayout mMenuLayout = (ResizeRelativeLayout) menuLayout
                .findViewById(R.id.menu_layout);
        mMenuLayout
                .setOnResizeRelativeListener(new ResizeRelativeLayout.OnResizeRelativeListener() {
                    @Override
                    public void OnResizeRelative(int w, int h, int oldw,
                                                 int oldh) {
                        mIsKeyboardOpened = false;
//                        Log.e("菜单高度", "h = " + h + ",oldh = " + oldh);

                        // 记录第一次打开输入法时的布局高度
                        if (h < oldh && oldh > 0 && mMenuOpenedHeight == 0) {
                            mMenuOpenedHeight = h;
                        }

                        // 布局的高度小于之前的高度
                        if (h < oldh) {
                            mIsKeyboardOpened = true;
                        }
                        // 或者输入法打开情况下,
                        // 输入字符后再清除(三星输入法软键盘在输入后，软键盘高度增加一行，清除输入后，高度变小，但是软键盘仍是打开状态)
                        else if ((h <= mMenuOpenedHeight)
                                && (mMenuOpenedHeight != 0)) {
                            mIsKeyboardOpened = true;
                        }

//                        Log.e("是否打开", "软键盘  = " + mIsKeyboardOpened);
                    }
                });
    }

    private void showComment() {
        PostRequest<CommentResultBean> request = new PostRequest<CommentResultBean>(
                String.format(UmiwiAPI.COMMENT_SEND + StatisticsUrl.DETAIL_COMMENT_WRITE_BUTTON, albumId),
                GsonParser.class, CommentResultBean.class, writeListener);
        request.addParam("albumid", albumId);
        request.addParam("question", writeComment.getText().toString().trim());
        request.go();

    }

    private Listener<CommentResultBean> writeListener = new Listener<CommentResultBean>() {

        @Override
        public void onResult(AbstractRequest<CommentResultBean> request, CommentResultBean t) {

            if (t.isSucc()) {
                if (writesuccListener != null) {
                    writesuccListener.onWriteSuccCallback(t.getR());
                }
                Toast.makeText(UmiwiApplication.getApplication(), "评论成功！", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(AbstractRequest<CommentResultBean> requet,
                            int statusCode, String body) {
            Toast.makeText(UmiwiApplication.getApplication(), "评论失败，请试重...", Toast.LENGTH_SHORT).show();
        }
    };

    public interface WriteSuccListener {
        void onWriteSuccCallback(CommentListBeans commentBean);
    }

    private WriteSuccListener writesuccListener;

    public void setCouponChooseListener(WriteSuccListener l) {
        this.writesuccListener = l;
    }
}
