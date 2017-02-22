package com.umiwi.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.SearchBean;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;

public class SearchAdapter extends BaseAdapter {

	private boolean isHistory;
	private ArrayList<SearchBean> historySearch;
	private ArrayList<SearchBean> resultSearch;

	private FragmentManager fragmentManager;

	public SearchAdapter(FragmentManager fm) {
		this.fragmentManager = fm;
	}
	
	public SearchAdapter(Context context, ArrayList<SearchBean> resultSearch) {
		this.resultSearch = resultSearch;
		this.historySearch = null;
		notifyDataSetChanged();
	}

	public boolean isResultSearch() {
		return resultSearch != null;
	}

	public boolean isHistory() {
		return historySearch != null;
	}

	public ArrayList<SearchBean> getResultSearch() {
		return resultSearch;
	}

	public ArrayList<SearchBean> getSearchHistory() {
		return historySearch;
	}

	public void setHistorySearch(ArrayList<SearchBean> history, boolean isHistory) {
		historySearch = history;
		resultSearch = null;
		this.isHistory = isHistory;
		notifyDataSetChanged();
	}

	public void setResultSearch(ArrayList<SearchBean> resultSearch) {
		this.resultSearch = resultSearch;
		this.historySearch = null;
		notifyDataSetChanged();
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		if (resultSearch != null) {
			return 0;
		}
		if (historySearch != null) {
			return 2;
		}
		return super.getItemViewType(position);
	}

	@Override
	public int getCount() {
		if (historySearch != null) {
			if (historySearch.isEmpty()) {
				return 0;
			}
			if (isHistory) {
				return historySearch.size() + 1;
			}
			return historySearch.size();
		}
		if (resultSearch != null) {
			return resultSearch.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getContext());

		if (resultSearch != null) {
			if (view == null) {
				view = LayoutInflater.from(UmiwiApplication.getApplication()).inflate(R.layout.search_result_listitem, null);
			}
			ProductViewHolder holder = getHolder(view);
			SearchBean searchBean = resultSearch.get(position);

			mImageLoader.loadImage(searchBean.getImage() + "", holder.image);
			holder.title.setText(searchBean.getTitle() + "");
			holder.authorname.setText(searchBean.getAuthorname() + "");

			holder.type.setVisibility(View.GONE);

		}

		if (historySearch != null) {
			if (view == null) {
				view = LayoutInflater.from(UmiwiApplication.getApplication()).inflate(R.layout.item_history, null);
			}
			HistoryViewHolder historyHolder = getHistoryHolder(view);

			if (position == historySearch.size()) {
				historyHolder.historyTextView.setText("清空历史记录");
				historyHolder.historycheckImageView.setVisibility(View.GONE);
				historyHolder.historyTextView.setGravity(Gravity.CENTER);
				historyHolder.historyTextView.setTextColor(Color.BLACK);
			} else {
				historyHolder.historyTextView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				historyHolder.historyTextView.setText(historySearch.get(position).getTitle());
			}
		}

		return view;
	}

	private ProductViewHolder getHolder(final View view) {
		ProductViewHolder holder = (ProductViewHolder) view.getTag();
		if (holder == null) {
			holder = new ProductViewHolder(view);
			view.setTag(holder);
		}
		return holder;
	}

	private class ProductViewHolder {

		public ImageView image;
		public ImageView type;

		public TextView title;

		public TextView authorname;

		public ProductViewHolder(View view) {
			type = (ImageView) view.findViewById(R.id.coupon_type);
			image = (ImageView) view.findViewById(R.id.image);
			title = (TextView) view.findViewById(R.id.title);
			authorname = (TextView) view.findViewById(R.id.authorname);
		}
	}
	private HistoryViewHolder getHistoryHolder(final View view) {
		HistoryViewHolder holder = (HistoryViewHolder) view.getTag();
		if (holder == null) {
			holder = new HistoryViewHolder(view);
			view.setTag(holder);
		}
		return holder;
	}
	
	private class HistoryViewHolder {
		
		public TextView historyTextView;
		public ImageView historycheckImageView;
		
		public HistoryViewHolder(View view) {
			historyTextView = (TextView) view.findViewById(R.id.history_textview);
			historycheckImageView = (ImageView) view.findViewById(R.id.history_check_imageview);
		}
	}

}
