package cn.youmi.framework.util;

import android.animation.ObjectAnimator;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnViewType;
import com.etiennelawlor.quickreturn.library.utils.QuickReturnUtils;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.framework.view.LoadingFooter;
import cn.youmi.framework.view.LoadingFooter.State;

/**
 * @author tangxiyong
 * @version 2014年7月2日 上午10:25:22
 */
public class ListViewQuickReturnScrollLoader implements AbsListView.OnScrollListener {

    // region Member Variables
    private int mMinFooterTranslation;
    private int mMinHeaderTranslation;
    private int mPrevScrollY = 0;
    private int mHeaderDiffTotal = 0;
    private int mFooterDiffTotal = 0;
    private View mHeader;
    private View mFooter;
    private QuickReturnViewType mQuickReturnType;
    private boolean mCanSlideInIdleScrollState = false;

    private List<AbsListView.OnScrollListener> mExtraOnScrollListenerList = new ArrayList<AbsListView.OnScrollListener>();
    
    protected LoadingFooter mLoadingFooter;
   	private boolean isloading = false;
   	private boolean mIsEnd = false;
   	private int mPage = 0;
       
    public QuickReturnOnScrollLoader mScrollListener;
    
    // endregion

    // region Constructors
    public ListViewQuickReturnScrollLoader(QuickReturnViewType quickReturnType, View headerView, int headerTranslation, View footerView, int footerTranslation){
        mQuickReturnType = quickReturnType;
        mHeader =  headerView;
        mMinHeaderTranslation = headerTranslation;
        mFooter =  footerView;
        mMinFooterTranslation = footerTranslation;
    }
    // endregion
    public ListViewQuickReturnScrollLoader(QuickReturnViewType quickReturnType, View headerView, int headerTranslation, View footerView, int footerTranslation, QuickReturnOnScrollLoader l){
        mQuickReturnType = quickReturnType;
        mHeader =  headerView;
        mMinHeaderTranslation = headerTranslation;
        mFooter =  footerView;
        mMinFooterTranslation = footerTranslation;
        mScrollListener = l;
    }
    
    public ListViewQuickReturnScrollLoader(QuickReturnViewType quickReturnType, View headerView, int headerTranslation, View footerView, int footerTranslation, QuickReturnOnScrollLoader l, LoadingFooter loadingFooter){
        mQuickReturnType = quickReturnType;
        mHeader =  headerView;
        mMinHeaderTranslation = headerTranslation;
        mFooter =  footerView;
        mMinFooterTranslation = footerTranslation;
        mScrollListener = l;
		mLoadingFooter = loadingFooter;
    }
    
    
    /** 分页*/
	public interface QuickReturnOnScrollLoader {
		void onLoadData(final int page);
		void onLoadData();
		/** 在onScroll里处理一些逻辑 */
        void customScrollInChange();
		/** 在onScrollStateChanged里处理一些逻辑 */
        void customScrollStateChanged(AbsListView view, int scrollState);
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
	
	private void errorFooterListener() {

		mLoadingFooter.getView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoadErrorPage();
			}
		});
	
	}

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    	if (isEnd() && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			if (mLoadingFooter != null) {
				mLoadingFooter.setState(LoadingFooter.State.NoMore);
			}
		}
    	
        // apply another list' s on scroll listener
        for (AbsListView.OnScrollListener listener : mExtraOnScrollListenerList) {
          listener.onScrollStateChanged(view, scrollState);
        }
        if(scrollState == SCROLL_STATE_IDLE && mCanSlideInIdleScrollState){

            int midHeader = -mMinHeaderTranslation/2;
            int midFooter = mMinFooterTranslation/2;

            switch (mQuickReturnType) {
                case HEADER:
                    if (-mHeaderDiffTotal > 0 && -mHeaderDiffTotal < midHeader) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), 0);
                        anim.setDuration(100);
                        anim.start();
                        mHeaderDiffTotal = 0;
                    } else if (-mHeaderDiffTotal < -mMinHeaderTranslation && -mHeaderDiffTotal >= midHeader) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), mMinHeaderTranslation);
                        anim.setDuration(100);
                        anim.start();
                        mHeaderDiffTotal = mMinHeaderTranslation;
                    }
                    break;
                case FOOTER:
                    if (-mFooterDiffTotal > 0 && -mFooterDiffTotal < midFooter) { // slide up
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), 0);
                        anim.setDuration(100);
                        anim.start();
                        mFooterDiffTotal = 0;
                    } else if (-mFooterDiffTotal < mMinFooterTranslation && -mFooterDiffTotal >= midFooter) { // slide down
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), mMinFooterTranslation);
                        anim.setDuration(100);
                        anim.start();
                        mFooterDiffTotal = -mMinFooterTranslation;
                    }
                    break;
                case BOTH:
                    if (-mHeaderDiffTotal > 0 && -mHeaderDiffTotal < midHeader) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), 0);
                        anim.setDuration(100);
                        anim.start();
                        mHeaderDiffTotal = 0;
                    } else if (-mHeaderDiffTotal < -mMinHeaderTranslation && -mHeaderDiffTotal >= midHeader) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), mMinHeaderTranslation);
                        anim.setDuration(100);
                        anim.start();
                        mHeaderDiffTotal = mMinHeaderTranslation;
                    }

                    if (-mFooterDiffTotal > 0 && -mFooterDiffTotal < midFooter) { // slide up
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), 0);
                        anim.setDuration(100);
                        anim.start();
                        mFooterDiffTotal = 0;
                    } else if (-mFooterDiffTotal < mMinFooterTranslation && -mFooterDiffTotal >= midFooter) { // slide down
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), mMinFooterTranslation);
                        anim.setDuration(100);
                        anim.start();
                        mFooterDiffTotal = -mMinFooterTranslation;
                    }
                    break;
                case TWITTER:
                    if (-mHeaderDiffTotal > 0 && -mHeaderDiffTotal < midHeader) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), 0);
                        anim.setDuration(100);
                        anim.start();
                        mHeaderDiffTotal = 0;
                    } else if (-mHeaderDiffTotal < -mMinHeaderTranslation && -mHeaderDiffTotal >= midHeader) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), mMinHeaderTranslation);
                        anim.setDuration(100);
                        anim.start();
                        mHeaderDiffTotal = mMinHeaderTranslation;
                    }

                    if (-mFooterDiffTotal > 0 && -mFooterDiffTotal < midFooter) { // slide up
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), 0);
                        anim.setDuration(100);
                        anim.start();
                        mFooterDiffTotal = 0;
                    } else if (-mFooterDiffTotal < mMinFooterTranslation && -mFooterDiffTotal >= midFooter) { // slide down
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), mMinFooterTranslation);
                        anim.setDuration(100);
                        anim.start();
                        mFooterDiffTotal = -mMinFooterTranslation;
                    }
                    break;
            }

        }
        if (mScrollListener != null) {
			mScrollListener.customScrollStateChanged(view, scrollState);
		}
    }

    @Override
    public void onScroll(AbsListView listview, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    	if (isLoading()){
			return;
    	}
    	//isLoading() == false && totalItemCount != 0 && firstVisibleItem + visibleItemCount >= totalItemCount && !isEnd()
		if (isLoading() == false && totalItemCount-2 != 0 && firstVisibleItem + visibleItemCount >= totalItemCount-2 && !isEnd()) {// Less headerview and footerview
			setloading(true);
			startLoadNextPage();
		}
        // apply extra on scroll listener
        for (AbsListView.OnScrollListener listener : mExtraOnScrollListenerList) {
          listener.onScroll(listview, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        int scrollY = QuickReturnUtils.getScrollY(listview);
        int diff = mPrevScrollY - scrollY;


        if(diff != 0){
            switch (mQuickReturnType){
                case HEADER:
                    if(diff < 0){ // scrolling down
                        mHeaderDiffTotal = Math.max(mHeaderDiffTotal + diff, mMinHeaderTranslation);
                    } else { // scrolling up
                        mHeaderDiffTotal = Math.min(Math.max(mHeaderDiffTotal + diff, mMinHeaderTranslation), 0);
                    }

                    mHeader.setTranslationY(mHeaderDiffTotal);
                    break;
                case FOOTER:
                    if(diff < 0){ // scrolling down
                        mFooterDiffTotal = Math.max(mFooterDiffTotal + diff, -mMinFooterTranslation);
                    } else { // scrolling up
                        mFooterDiffTotal = Math.min(Math.max(mFooterDiffTotal + diff, -mMinFooterTranslation), 0);
                    }

                    mFooter.setTranslationY(-mFooterDiffTotal);
                    break;
                case BOTH:
                    if(diff < 0){ // scrolling down
                        mHeaderDiffTotal = Math.max(mHeaderDiffTotal + diff, mMinHeaderTranslation);
                        mFooterDiffTotal = Math.max(mFooterDiffTotal + diff, -mMinFooterTranslation);
                    } else { // scrolling up
                        mHeaderDiffTotal = Math.min(Math.max(mHeaderDiffTotal + diff, mMinHeaderTranslation), 0);
                        mFooterDiffTotal = Math.min(Math.max(mFooterDiffTotal + diff, -mMinFooterTranslation), 0);
                    }

                    mHeader.setTranslationY(mHeaderDiffTotal);
                    mFooter.setTranslationY(-mFooterDiffTotal);
                    break;
                case TWITTER:
                    if(diff < 0){ // scrolling down
                        if(scrollY > -mMinHeaderTranslation)
                            mHeaderDiffTotal = Math.max(mHeaderDiffTotal + diff, mMinHeaderTranslation);

                        if(scrollY > mMinFooterTranslation)
                            mFooterDiffTotal = Math.max(mFooterDiffTotal + diff, -mMinFooterTranslation);
                    } else { // scrolling up
                        mHeaderDiffTotal = Math.min(Math.max(mHeaderDiffTotal + diff, mMinHeaderTranslation), 0);
                        mFooterDiffTotal = Math.min(Math.max(mFooterDiffTotal + diff, -mMinFooterTranslation), 0);
                    }

                    mHeader.setTranslationY(mHeaderDiffTotal);
                    mFooter.setTranslationY(-mFooterDiffTotal);
                default:
                    break;
            }
        }

        mPrevScrollY = scrollY;
        
        if (mScrollListener != null) {
			mScrollListener.customScrollInChange();
		}
    }

    public void setCanSlideInIdleScrollState(boolean canSlideInIdleScrollState){
        mCanSlideInIdleScrollState = canSlideInIdleScrollState;
    }

    public void registerExtraOnScrollListener(AbsListView.OnScrollListener listener) {
        mExtraOnScrollListenerList.add(listener);
    }}
