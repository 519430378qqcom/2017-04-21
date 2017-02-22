package com.umiwi.ui.fragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import cn.youmi.account.event.UserEvent;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.util.ToastU;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.LogoUserInfoGridViewAdapter;
import com.umiwi.ui.adapter.ViewHolder;
import com.umiwi.ui.beans.LogoUserInfoBeans;
import com.umiwi.ui.beans.LogoUserInfoResultBeans;
import com.umiwi.ui.beans.LogoUserInfoResultBeans.LogoUserInfoRequestData;
import com.umiwi.ui.fragment.user.BindingPhoneFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;

public class UserTestInfoFragment extends BaseConstantFragment {
	private ProgressDialog mLoadingDialog;
	private LogoUserInfoGridViewAdapter mAdapter;
	private ArrayList<LogoUserInfoBeans> mList;
	private GridView mGridView;
	private TextView title;
	public String url;
	private String nexturl = "";
	private String strrecord = "";
	private String requests = "";
	private TextView prev;
	private TextView user_submit;
	
	private int checkNum; // 记录选中的条目数量   
	
	private boolean goHome;

	public static final String URL_CATEGORY = "URL_CATEGORY";
	private TextView title_o;
	private TextView compter_iv;
	
	public static UserTestInfoFragment newInstance(String url) {
		UserTestInfoFragment fragment = new UserTestInfoFragment();
		Bundle bundle = new Bundle();
		bundle.putString(URL_CATEGORY, url);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.user_info_testing, null);
		
		mLoadingDialog = new ProgressDialog(getActivity());
		mLoadingDialog.setMessage("加载中,请稍候...");
		mLoadingDialog.setIndeterminate(true);
		mLoadingDialog.setCancelable(true);
		
		YoumiRoomUserManager.getInstance().registerListener(mUserListener);
		
		compter_iv = (TextView) view.findViewById(R.id.user_compter_iv);
		title = (TextView) view.findViewById(R.id.user_title);
		title_o = (TextView) view.findViewById(R.id.user_title_o); 
		prev = (TextView) view.findViewById(R.id.user_prev);
		user_submit = (TextView) view.findViewById(R.id.user_submit);
		mGridView = (GridView) view.findViewById(R.id.girdview);
		mList = new ArrayList<LogoUserInfoBeans>();
		mAdapter = new LogoUserInfoGridViewAdapter(getActivity(), mList, nexturl);
		mGridView.setAdapter(mAdapter);
		
		Bundle bundle = getArguments();
		if (null !=bundle &&!TextUtils.isEmpty(bundle.getString(URL_CATEGORY))) {
			
			url = bundle.getString(URL_CATEGORY);
		}
        if (url.contains("step=4")) {
        	UserTestingResult(url);
		} else {
			UserTesting(url);
		}
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				LogoUserInfoBeans mListBeans = (LogoUserInfoBeans) mAdapter.getItem(position);
				if (null!= nexturl && !nexturl.contains("tagids")) {
					UserTestInfoFragment courseDetailFragment = new UserTestInfoFragment();
					courseDetailFragment.url = String.format(nexturl+mListBeans.getValue());
					UmiwiContainerActivity ca = (UmiwiContainerActivity) getActivity();
					ca.getNavigationController().pushFragment(courseDetailFragment);
					return;
				}
				
				ViewHolder holder = (ViewHolder) view.getTag();   
                // 改变CheckBox的状态   
                holder.content.toggle();   
                // 将CheckBox的选中状况记录下来   
                LogoUserInfoGridViewAdapter.getIsSelected().put(position, holder.content.isChecked());    
                // 调整选定条目   
                if (holder.content.isChecked() == true) {
                	if (checkNum == 3) {
						ToastU.showShort(getActivity(), "最多可选3个哒~！");
    					holder.content.setChecked(false);
    					return;
    				}
                	Resources res = getResources();  
                    Drawable drawable = res.getDrawable(R.drawable.ic_tick); 
                    drawable.mutate().setAlpha(55);
        			holder.image.setImageDrawable(drawable);
//        			float alpha = 0.5f;
//        			holder.image.setImageAlpha((int) alpha * 255);
                    checkNum++;   
                } else {   
                	holder.image.setImageDrawable(null);
                	LogoUserInfoGridViewAdapter.getIsSelected().remove(position);
                    checkNum--;   
                }   
			}
		});
		
		user_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (goHome) {
					UmiwiContainerActivity ca = (UmiwiContainerActivity) getActivity();
					ca.slideToFinishActivity();
					return;
				}
				
				if (null != strrecord && !"".equals(strrecord)) {//提交多选
					YoumiRoomUserManager.getInstance().getUserInfoSave(UserEvent.HOME_TESTINFO);
					return;
				}
				Map map = LogoUserInfoGridViewAdapter.getIsSelected(); 
				Iterator iter = map.entrySet().iterator(); 
				while (iter.hasNext()) { 
				    Map.Entry entry = (Map.Entry) iter.next(); 
				    int key = (Integer) entry.getKey(); 
				    final LogoUserInfoBeans listBeans = mList.get(key);
					requests += listBeans.getValue() +",";
				}
				
				if ("".equals(requests)) {
					ToastU.showShort(getActivity(), "最少选择1个哒~！");
					return;
				}
				
				String submit_str = nexturl + requests;//下一步
				UserTestingResult(submit_str);
			}
		});
		
		prev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UmiwiContainerActivity ca = (UmiwiContainerActivity) getActivity();
				if (null != strrecord && !"".equals(strrecord)) {
					UserTestInfoFragment courseDetailFragment = new UserTestInfoFragment();
					courseDetailFragment.url = UmiwiAPI.USER_TEST_COURSE;
					ca.getNavigationController().pushFragment(courseDetailFragment);
					return;
				}
				ca.getNavigationController().popFragment();
			}
		});
		
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
	
	private Listener<com.umiwi.ui.beans.LogoUserInfoBeans.LogoUserInfoRequestData> lis = new Listener<LogoUserInfoBeans.LogoUserInfoRequestData>() {

		@Override
		public void onResult(
				AbstractRequest<com.umiwi.ui.beans.LogoUserInfoBeans.LogoUserInfoRequestData> request,
				com.umiwi.ui.beans.LogoUserInfoBeans.LogoUserInfoRequestData t) {
			nexturl = t.getNexturl();
			if (null != nexturl && !"".equals(nexturl)) {
				ArrayList<LogoUserInfoBeans> charts = t.getRecord();
				mList.addAll(charts);
			}
			title.setVisibility(View.VISIBLE);
			title_o.setVisibility(View.VISIBLE);
			mGridView.setVisibility(View.VISIBLE);
			if (mList.size() == 3) {
				mGridView.setNumColumns(1);
			} else {
				mGridView.setNumColumns(2);
				mGridView.setHorizontalSpacing(10);
			}
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//				if (null != nexturl && !"".equals(nexturl) && !nexturl.contains("tagids")) {
//					mGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
//				} else {
//					mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
//				}
//			}
			
			if (nexturl.contains("step=3")) {
				title.setText("我想要");
//				title_o.setText("请选择想要学习的方向");
				prev.setVisibility(View.VISIBLE);
			} else if(nexturl.contains("step=4")) {
				title.setText("我喜欢");
				title_o.setText("最多可选3项");
				prev.setVisibility(View.VISIBLE);
				user_submit.setVisibility(View.VISIBLE);
//				user_submit.setText("下一步");
				user_submit.setText("马上学习");
			} else {
				title.setText("我是");
//				title_o.setText("请选择符合您的头像");
				user_submit.setVisibility(View.VISIBLE);
				user_submit.setBackgroundResource(0);
				user_submit.setGravity(Gravity.RIGHT);
				user_submit.setTextSize(16);
				user_submit.setText(R.string.jump_go_home);
				goHome = true;
			}
			if (mAdapter == null) {
				mAdapter = new LogoUserInfoGridViewAdapter(getActivity(), mList, nexturl);
				mGridView.setAdapter(mAdapter);
			} else {
				mAdapter.notifyDataSetChanged();
			}
			
			progressDissmiss();
		}

		@Override
		public void onError(
				AbstractRequest<com.umiwi.ui.beans.LogoUserInfoBeans.LogoUserInfoRequestData> requet,
				int statusCode, String body) {
			// TODO Auto-generated method stub
			
		}
	};

	private void UserTesting(String url) {
		progressShow();
		
		GetRequest<LogoUserInfoBeans.LogoUserInfoRequestData> request = 
				new GetRequest<LogoUserInfoBeans.LogoUserInfoRequestData>(url,GsonParser.class,LogoUserInfoBeans.LogoUserInfoRequestData.class,lis);
		request.go();
	}
	
	private Listener<LogoUserInfoRequestData> listener = new Listener<LogoUserInfoResultBeans.LogoUserInfoRequestData>() {

		@Override
		public void onResult(AbstractRequest<LogoUserInfoRequestData> request,
				LogoUserInfoRequestData t) {
//			title.setVisibility(View.VISIBLE);
//			title_o.setVisibility(View.VISIBLE);
//			if (null == nexturl || "".equals(nexturl)) {
//				strrecord = t.getStrrecord();//服务器把<br>改成<br/>
//				title.setText("马上学习");
//				title_o.setVisibility(View.GONE);
//				prev.setVisibility(View.VISIBLE);
//				prev.setText("重新测试");
//				user_submit.setVisibility(View.VISIBLE);
//				user_submit.setText("马上学习");
//				compter_iv.setText(Html.fromHtml(strrecord));
//				compter_iv.setVisibility(View.VISIBLE);
//				progressDissmiss();
//				return;
//			}
			
			YoumiRoomUserManager.getInstance().getUserInfoSave(UserEvent.HOME_TESTINFO);
			progressDissmiss();
		}

		@Override
		public void onError(AbstractRequest<LogoUserInfoRequestData> requet,
				int statusCode, String body) {
			// TODO Auto-generated method stub
			
		}
	};
	private void UserTestingResult(String url) {
		progressShow();
		
		GetRequest<LogoUserInfoResultBeans.LogoUserInfoRequestData> request = 
				new GetRequest<LogoUserInfoResultBeans.LogoUserInfoRequestData>(url, GsonParser.class,LogoUserInfoResultBeans.LogoUserInfoRequestData.class,listener);
		request.go();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		progressDissmiss();
		YoumiRoomUserManager.getInstance().unregisterListener(mUserListener);
	}
	
	private ModelStatusListener<UserEvent, UserModel> mUserListener = new ModelStatusListener<UserEvent, UserModel>() {

		@Override
		public void onModelGet(UserEvent key, UserModel models) {
		}

		@Override
		public void onModelUpdate(UserEvent key, UserModel model) {
			switch (key) {
			case HOME_TESTINFO:
				if (model.isDobindmobile()) {
					Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
					i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, BindingPhoneFragment.class);
					startActivity(i);
				}

				UmiwiContainerActivity ca = (UmiwiContainerActivity) getActivity();
				ca.slideToFinishActivity();
				break;

			default:
				break;
			}
			
		
		}

		@Override
		public void onModelsGet(UserEvent key, List<UserModel> models) {
		}};
	
	private void progressShow() {
		if (mLoadingDialog != null)
			mLoadingDialog.show();
	}
	
	private void progressDissmiss() {
		if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}
	}
	
}
