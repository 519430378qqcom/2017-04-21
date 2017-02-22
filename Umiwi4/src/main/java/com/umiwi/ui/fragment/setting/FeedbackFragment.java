package com.umiwi.ui.fragment.setting;

import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.youmi.framework.dialog.MsgDialog;
import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.PostRequest;
import cn.youmi.framework.view.CircleImageView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.http.parsers.FeedbackParser;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.util.CommonHelper;

public class FeedbackFragment extends BaseFragment {
	private EditText contentEditText;
	private EditText contactEditText;
	
	private int[] imageIds = new int[]{R.drawable.feedback_icon_image1,R.drawable.feedback_icon_image2,R.drawable.feedback_icon_image3};
	private String[] names = new String[]{"Lucy Miao","Secret Feng","Wade"};
	
	@SuppressLint("InflateParams") 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_feedback, null);
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "意见反馈");
		
		Button commit = (Button) view.findViewById(R.id.commit_button);
		commit.setOnClickListener(mCommitButtonOnClickListener);
		TextView phone = (TextView) view.findViewById(R.id.phone_text_view);
		phone.setText(Html.fromHtml(
				"<font color='#ff000000'>" + "客服电话:    " + "</font>" + 
				"<font color='#ff33b5e5'>" +"<u>"+ "010-85801860" + "</u>"+"</font>"));
		TextView qq = (TextView) view.findViewById(R.id.qq);
		qq.setText(Html.fromHtml(
				"<font color='#ff000000'>" + "在线 Q        Q:    " + "</font>" + 
				"<font color='#ff33b5e5'>" +"<u>"+ "800021784" + "</u>"+"</font>"));
		TextView service_tel = (TextView) view.findViewById(R.id.service_tel);
		service_tel.setText(Html.fromHtml(
				"<font color='#ff000000'>" + "微信服务号:    " + "</font>" + 
				"<font color='#ff33b5e5'>" +"<u>"+ "youmihuiyuan" + "</u>"+"</font>"));
		TextView phone_text_view = (TextView) view.findViewById(R.id.phone_text_view);
		phone_text_view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showCallDialog();
			}
		});
		contentEditText = (EditText) view.findViewById(R.id.content_edit_text);
		contactEditText = (EditText) view.findViewById(R.id.contact_edit_text);
		
		int index = new Random().nextInt(3);
		CircleImageView iconImage = (CircleImageView) view.findViewById(R.id.icon_image_view);
		iconImage.setImageResource(imageIds[index]);
		
		TextView nameText = (TextView) view.findViewById(R.id.name_text_view);
		nameText.setText("我是产品部: " + names[index]);
	
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(fragmentName);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(fragmentName);
	}
	
	private void showCallDialog(){
		final MsgDialog dialog = new MsgDialog();
		dialog.setTitle("呼叫");
		dialog.setMessage("010-85801860");
		dialog.setPositiveButtonText("呼叫");
		dialog.setPositiveButtonListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismissAllowingStateLoss();
				Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:010-85801860"));//拔打电话
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
                startActivity(intent);
			}
		});
		dialog.show(getChildFragmentManager(), "dialog");
	}

	private Listener<String> listener = new Listener<String>() {
		@Override
		public void onResult(AbstractRequest<String> request, String t) {
			if(t != null){
				Toast.makeText(UmiwiApplication.getContext(), "提交成功!感谢您的反馈,我们一定会及时处理!", Toast.LENGTH_LONG).show();
				if (null != getActivity()) {
					getActivity().finish();
				}
				
			}else{
				Toast.makeText(UmiwiApplication.getContext(), "提交失败,请重新提交!", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onError(AbstractRequest<String> requet, int statusCode,
				String body) {
			Toast.makeText(UmiwiApplication.getContext(), "网络连接异常", Toast.LENGTH_LONG).show();
		}
	};
	
	private OnClickListener mCommitButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			commitFeedback();
		}
	};
	
	private void commitFeedback(){
		if(TextUtils.isEmpty(contentEditText.getText())){
			showMsg("~您还没有留下宝贵的意见或者建议哟~");
			return;
		}
		String url = String.format(UmiwiAPI.FEEDBACK_URL);
		PostRequest<String> request = new PostRequest<String>(url, FeedbackParser.class, listener);
		request.addParam("content", contentEditText.getText().toString());
//		request.addParam("option", getQuestion());
		request.addParam("app", "android");
		request.addParam("system", Build.VERSION.RELEASE);
		request.addParam("deviceid", CommonHelper.getMacMD5());
		request.addParam("model", Build.MODEL);
		request.addParam("email", contactEditText.getText().toString());
		try {
			request.addParam("version", CommonHelper.getVersionName());
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		request.go();
	}
	
}
