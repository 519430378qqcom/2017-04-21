package com.umiwi.ui.fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.PostRequest;
import cn.youmi.framework.util.KeyboardUtils;
import cn.youmi.framework.view.CircleImageView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.FeedbackResultBean;
import com.umiwi.ui.http.parsers.SearchFeedbackParser;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.util.CommonHelper;

/***
 * 用户搜索不满意
 * @author tjie00	
 * 2014-06-13 
 *
 */
public class SearchFeedbackFragment extends BaseConstantFragment {

    public static final String EXTRA_CATEGORY = "CHARTS_POSITION";
    
	/**指定布局xml*/
	private View view;
	
	private TextView tvCommit;
	private EditText etContent;
	private TextView tvName;
	private CircleImageView ivIcon;
	
	private String searchKeyword;
	
	private CheckBox ckNoresult; 
	private CheckBox ckResultWrong;
	private CheckBox ckSlow;
	private CheckBox ckNocomfortable;
	private CheckBox[] cbs;
	
	private Map<Integer, String> ptos;
	
	private SparseArray<String> questions;
	
	private ProgressBar mProgressBar;
	
	{
		ptos = new HashMap<Integer, String>();
		ptos.put(0, "feedback_icon_image1" + "," + "Lucy Miao");
		ptos.put(1, "feedback_icon_image2" + "," + "Secret Feng");
		ptos.put(2, "feedback_icon_image3" + "," + "sue");
		
	}
	@Override
	public void onActivityCreated(AppCompatActivity a) {
		super.onActivityCreated(a);
		searchKeyword = a.getIntent().getStringExtra(EXTRA_CATEGORY);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_search_feedback, null);
		
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		mActionBarToolbar.setTitle("意见反馈");
		
		initView(view);
		setListener();
		
		return view;

	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(fragmentName);
	}

	@Override
	public void onResume() {
		super.onResume();
		for(CheckBox cb : cbs) {
			cb.setChecked(false);
		}
		
		etContent.setText("");
		
		showPTO();
		MobclickAgent.onPageStart(fragmentName);
	}
	
	// 设置监听器
	private void setListener() {
		FeedbackCommitListener listener = new FeedbackCommitListener();
		tvCommit.setOnClickListener(listener);
	}


	/**
	 * 初始化
	 */
	private void initView(View view) {
		tvCommit = (TextView) view.findViewById(R.id.tv_commit);
		tvName = (TextView) view.findViewById(R.id.name_textview);

		ivIcon = (CircleImageView) view.findViewById(R.id.icon);
		
		/*LinearLayout.LayoutParams para_r = (LinearLayout.LayoutParams) ivIcon
				.getLayoutParams();
		para_r.width = 240;
		para_r.height = para_r.width;
		ivIcon.setLayoutParams(para_r);*/
		
		//showPTO();
		
		ckNoresult = (CheckBox) view.findViewById(R.id.cb_noresult);
		ckResultWrong = (CheckBox) view.findViewById(R.id.cb_result_wrong);
		ckSlow = (CheckBox) view.findViewById(R.id.cb_too_slow);
		ckNocomfortable = (CheckBox) view.findViewById(R.id.cb_nocomfortable);

		cbs = new CheckBox[] {ckNoresult, 
							  ckResultWrong, 
							  ckSlow,
							  ckNocomfortable
		};
		
		questions = new SparseArray<String>();
		questions.put(R.id.cb_noresult, "搜不到想要的");
		questions.put(R.id.cb_result_wrong, "搜索结果不准");
		questions.put(R.id.cb_too_slow, "搜索太慢");
		questions.put(R.id.cb_nocomfortable, "显示搜索错误");
		
		etContent = (EditText) view.findViewById(R.id.et_reason);
		mProgressBar = (ProgressBar) view.findViewById(R.id.loading);

	}
	
	// 显示名称和头像
	@SuppressWarnings("deprecation")
	private void showPTO() {
		
		int id = choosePTOID();
		
		String pto = ptos.get(id);
		String[] ptoInfo = pto.split(",");
		
		Drawable icon = getResources().getDrawable(getResources().getIdentifier(ptoInfo[0], "drawable", getActivity().getPackageName()));
		ivIcon.setBackgroundDrawable(icon);
		tvName.setText("我是产品部: " + ptoInfo[1] );
	}

	// 随机选择pto
	private int choosePTOID() {
		Random random = new Random();
		int ptoid = random.nextInt(3);
		return ptoid;
	}

	private class FeedbackCommitListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			// 提交反馈
			case R.id.tv_commit:
				
				mProgressBar.setVisibility(View.VISIBLE);
				
				String strContent = etContent.getText().toString().trim();
				KeyboardUtils.hideKeyboard(getActivity());
				commitFeedback(strContent);
				
				break;
				
				
			default:
				break;
			}
			
		}
	}


	// 提交意见反馈
	public void commitFeedback(String strContent) {
		PostRequest<FeedbackResultBean.FeedbackResultRequestData> request = new PostRequest<FeedbackResultBean.FeedbackResultRequestData>(UmiwiAPI.FEEDBACK_URL, SearchFeedbackParser.class, listener);
		request.addParam("content", strContent);
		request.addParam("option", getQuestion());
		request.addParam("app", "android");
		request.addParam("system", Build.VERSION.RELEASE);
		request.addParam("deviceid", CommonHelper.getMacMD5());//TODO
		request.addParam("model", Build.MODEL);
		try {
			request.addParam("version", CommonHelper.getVersionName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.go();
	}
	
	/**
	 * 获得用户选择的问题提交
	 * 
	 * @return
	 */
	public String getQuestion() {
		StringBuilder builder = new StringBuilder("搜索：");
		builder.append(searchKeyword).append("; ");
		for (int i = 0; i < cbs.length; i++) {
			if (cbs[i].isChecked()) {
				if (i == 0) {
					builder .append(questions.get(cbs[i].getId()));
				} else {
					builder .append("," + questions.get(cbs[i].getId()));
				}
			}
		}
		return builder.toString();
	}

	private Listener<FeedbackResultBean.FeedbackResultRequestData> listener = new Listener<FeedbackResultBean.FeedbackResultRequestData>() {
		@Override
		public void onResult(AbstractRequest<FeedbackResultBean.FeedbackResultRequestData> request, FeedbackResultBean.FeedbackResultRequestData t) {
			dismiss();
			
			if(t != null && t.getCode().equals("succ")){
				Toast.makeText(UmiwiApplication.getContext(), "提交成功!感谢您的反馈,我们一定会及时处理!", Toast.LENGTH_LONG).show();
				getActivity().finish();
			}else{
				Toast.makeText(UmiwiApplication.getContext(), "提交失败,请重新提交!", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onError(AbstractRequest<FeedbackResultBean.FeedbackResultRequestData> requet, int statusCode,
				String body) {
			dismiss();
			
			Toast.makeText(UmiwiApplication.getContext(), "网络连接异常", Toast.LENGTH_LONG).show();
		}
	};
	
	public void dismiss() {
		if(mProgressBar.isShown()) {
			mProgressBar.setVisibility(View.GONE);
		}
	}
	
}
