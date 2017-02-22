package cn.youmi.framework.util;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import cn.youmi.framework.view.LoadingFooter;
import cn.youmi.framework.view.LoadingFooter.State;

/**
 * @author tangxiyong
 * @version 2014年7月2日 上午10:25:22
 */
public class ListViewScrollLoader implements AbsListView.OnScrollListener {

	protected LoadingFooter mLoadingFooter;
	private boolean isloading = true;
	private boolean mIsEnd = false;
	private int mPage = 0;
	
	public OnScrollLoader mScrollListener;
	
	public ListViewScrollLoader(OnScrollLoader l){
		mScrollListener = l;
	}
	
	public ListViewScrollLoader(OnScrollLoader l, LoadingFooter loadingFooter){
		mScrollListener = l;
		mLoadingFooter = loadingFooter;
	}
	/** 分页*/
	public interface OnScrollLoader {
		void onLoadData(final int page);
		void onLoadData();
		/** 在onScroll里处理一些逻辑 */
		void customScrollInChange();
		
	}
	
	private void startLoadData(int page) {
		if (mLoadingFooter != null) {
			mLoadingFooter.setState(LoadingFooter.State.Loading);
			setloading(true);
			if (mScrollListener != null) {
				mScrollListener.onLoadData(page);
				mScrollListener.onLoadData();
			}
		}
	}
	
	private void startLoadNextPage() {
		startLoadData(getPage() + 1);
	}
	
	public void onLoadFirstPage() {
		if (mLoadingFooter != null) {
			mLoadingFooter.setState(LoadingFooter.State.Idle);
			setEnd(false);
			startLoadData(1);
		}
	}
	
//	public void onLoadNextPage() {
//		startLoadNextPage();
//	}
	
	public void onLoadErrorPage() {
		if (mLoadingFooter != null) {
			mLoadingFooter.setState(LoadingFooter.State.Loading);
		}
		startLoadData(getPage() + 1);
	}
	/**
	 * 在onLoadFirstPageAndScrollToTop(listView)前加上，
	 * mScrollLoader.setPage(0);防止网络错误时调用onLoadErrorPage()时page=2
	 * @param listView
	 */
	public void onLoadFirstPageAndScrollToTop(ListView listView) {
        ListViewUtils.smoothScrollListViewToTop(listView);
        onLoadFirstPage();
    }

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (isEnd() && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			if (mLoadingFooter != null) {
				mLoadingFooter.setState(LoadingFooter.State.NoMore);
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (isLoading()){
			return;
		}
		if (isLoading() == false && totalItemCount != 0 && firstVisibleItem + visibleItemCount >= totalItemCount && !isEnd()) {
			setloading(true);
			startLoadNextPage();
		}
		if (mScrollListener != null) {
			mScrollListener.customScrollInChange();
		}
		
		
	}

	
	
	public boolean isLoading() {
		return isloading;
	}

	public void setloading(boolean isloading) {
		this.isloading = isloading;
		if(isloading){
			mLoadingFooter.setState(State.Loading);
		}else{
			mLoadingFooter.setState(State.Idle);
		}
		
	}

	public boolean isEnd() {
		return mIsEnd;
	}

	public void setEnd(boolean mIsEnd) {
		this.mIsEnd = mIsEnd;
		if(mIsEnd){
			mLoadingFooter.setState(State.TheEnd);
		}
	}

	public int getPage() {
		return mPage;
	}

	public void setPage(int mPage) {
		this.mPage = mPage;
	}
	
	public void showContentView(CharSequence contentString) {
		mLoadingFooter.setContentStr(contentString.toString());
		mLoadingFooter.setState(State.ShowContent);
	}
	
	public void showImageView(int imageId, CharSequence contentString) {
		mLoadingFooter.setContentImageId(imageId);
		mLoadingFooter.setContentStr(contentString.toString());
		mLoadingFooter.setState(State.ShowImage);
	}
	
	public void showLoadErrorView(String errorContent) {
		if (TextUtils.isEmpty(errorContent)) {
			showLoadErrorView();
		} else {
			showContentView(errorContent);
			errorFooterListener();
		}
	}
	
	public void showLoadErrorView() {
		mLoadingFooter.setState(LoadingFooter.State.Error);
		errorFooterListener();
	}
	
	private void emptyFooterListener() {
		mLoadingFooter.getView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
	}
	
	private void errorFooterListener() {
		mLoadingFooter.getView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoadErrorPage();
			}
		});
	}

}
