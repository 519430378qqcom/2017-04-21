package cn.youmi.framework.util;

import java.util.ArrayList;

import android.widget.ListView;

/***
 * 用于ListView setOnItemClickListener
 * 
 * @author tangxiyong
 * 
 */
public class ListViewPositionUtils {
	private static <E> boolean isNullFlag(E mListBeans) {
		return mListBeans == null;
	}

	private static <E> boolean isHeaderViewPosition(int position, ListView mListView, ArrayList<E> mList) {
		return position - mListView.getHeaderViewsCount() < mList.size() && position - mListView.getHeaderViewsCount() >= 0;
	}

	public static <E> boolean isPositionCanClick(E mListBeans, int position, ListView mListView, ArrayList<E> mList) {

		return position >= 0 && isHeaderViewPosition(position, mListView, mList) && !isNullFlag(mListBeans) && mList.size() != 0;
	}
	/** position - mListView.getHeaderViewsCount() */
	public static <E> int indexInDataSource(int position, ListView mListView) {
		if (position <= -1) {
			return 0;
		}
		return position - mListView.getHeaderViewsCount();
	}
}
