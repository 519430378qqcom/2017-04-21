package com.umiwi.ui.main;

import android.annotation.SuppressLint;

//import com.squareup.leakcanary.RefWatcher;

import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.util.ListViewScrollLoader.OnScrollLoader;

/**
 * Fragment基本类
 * 
 * @author tangxiyong 2013-11-11下午4:14:48
 * 
 */
@SuppressLint("ValidFragment")
public class BaseConstantFragment extends BaseFragment implements OnScrollLoader{

	@Override
	public void onLoadData(int page) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void customScrollInChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
//		RefWatcher refWatcher = UmiwiApplication.getRefWatcher(getActivity());
//		refWatcher.watch(this);
	}
}
