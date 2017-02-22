package com.umiwi.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.beans.UmiwiShakeBean;
import com.umiwi.ui.fragment.ShakeCoupondFragment;
import com.umiwi.ui.fragment.ShakeFragment;
import com.umiwi.ui.fragment.UmiwiEntranceTicketFragment;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.main.UmiwiApplication;

import cn.youmi.framework.dialog.MsgDialog;
import cn.youmi.framework.util.ToastU;

public class ShakeResultDialog extends DialogFragment {

    public OnDismissListener dismissListener;

    private ImageView ivCancel;
    private ImageView ivShakeResult;

    private UmiwiShakeBean bean;
    public static final String TAG = "ShakeResultDailog";

    public ShakeFragment shakeFragment;
    private ProgressBar loadingProgressBar;

    public void setData(UmiwiShakeBean bean) {
        this.bean = bean;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME,
                android.R.style.Theme_Translucent_NoTitleBar);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pupwindow_shake_result, null);

        ivCancel = (ImageView) rootView.findViewById(R.id.view_close);
        ivShakeResult = (ImageView) rootView.findViewById(R.id.iv_shake_result);
        loadingProgressBar = (ProgressBar) rootView
                .findViewById(R.id.loading_progress);

        final LinearLayout contentContainer = (LinearLayout) rootView.findViewById(R.id.content_container);


        Glide.with(UmiwiApplication.getContext())
                .load(bean.getLotteryurl())
                .listener(new RequestListener<String, GlideDrawable>() {

                    @Override
                    public boolean onException(Exception arg0, String arg1,
                                               Target<GlideDrawable> arg2, boolean arg3) {

                        dismiss();

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable arg0, String arg1,
                                                   Target<GlideDrawable> arg2, boolean arg3,
                                                   boolean arg4) {
                        FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) contentContainer.getLayoutParams();
                        params.width = arg0.getIntrinsicWidth();
                        params.height = arg0.getIntrinsicHeight();

                        LinearLayout.LayoutParams resultParams = (android.widget.LinearLayout.LayoutParams) ivShakeResult.getLayoutParams();
                        resultParams.width = arg0.getIntrinsicWidth();
                        resultParams.height = arg0.getIntrinsicHeight() - ivCancel.getHeight();
                        ivShakeResult.setLayoutParams(resultParams);
                        ivShakeResult.invalidate();

                        contentContainer.setLayoutParams(params);
                        contentContainer.invalidate();
                        loadingProgressBar.setVisibility(View.INVISIBLE);
                        contentContainer.setVisibility(View.VISIBLE);

                        return false;
                    }
                })
                .into(ivShakeResult);


        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ownerFragment.isShaking = false;
//                dismiss();

                if ("menpiao".equalsIgnoreCase(bean.getResulttype())) {
                    final MsgDialog dialog = new MsgDialog();
                    dialog.setTitle("领取提示");// TODO move into R.string
                    dialog.setMessage("您确定要放弃领取?");
                    dialog.setPositiveButtonText("确定");
                    dialog.setNegativeButtonText("领取");
                    dialog.setPositiveButtonListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            dialog.dismiss();
                        }
                    });
                    dialog.setNegativeButtonListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent menpiaoIntent = new Intent(getActivity(), UmiwiContainerActivity.class);
                            menpiaoIntent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, UmiwiEntranceTicketFragment.class);
                            menpiaoIntent.putExtra(UmiwiEntranceTicketFragment.KEY_LOTTERY_USER_ID, bean.getLotteryUserId());
                            startActivity(menpiaoIntent);
                            dismiss();
                            dialog.dismiss();
                        }
                    });
                    dialog.show(getActivity().getSupportFragmentManager(), "dialog");
                    return;
                }
                dismiss();

            }
        });

        ivShakeResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ownerFragment.isShaking = false;

                dismiss();

                if (bean.getResulttype() != null
                        && !TextUtils.isEmpty(bean.getResulttype())) {
                    if ("coupon".equalsIgnoreCase(bean.getResulttype())) {
                        Intent couponIntent = new Intent(getActivity(), UmiwiContainerActivity.class);
                        couponIntent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ShakeCoupondFragment.class);
                        startActivity(couponIntent);
                    } else if ("album".equalsIgnoreCase(bean.getResulttype())) {
                        Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                        intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, bean.getResulturl());
                        startActivity(intent);
                    } else if ("menpiao".equalsIgnoreCase(bean.getResulttype())) {
                        //TODO 活动门票
                        Intent menpiaoIntent = new Intent(getActivity(), UmiwiContainerActivity.class);
                        menpiaoIntent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, UmiwiEntranceTicketFragment.class);
                        menpiaoIntent.putExtra(UmiwiEntranceTicketFragment.KEY_LOTTERY_USER_ID, bean.getLotteryUserId());
                        startActivity(menpiaoIntent);
                    } else {
                        ToastU.showLong(getActivity(), "当前版本过低，请升级最新版本");
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void dismiss() {
        if (dismissListener != null) {
            dismissListener.onDismiss();
        }
        super.dismiss();
    }

}
