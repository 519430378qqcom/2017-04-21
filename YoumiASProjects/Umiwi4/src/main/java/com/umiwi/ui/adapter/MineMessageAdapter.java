package com.umiwi.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.model.MineMessageModel;

import java.util.ArrayList;

import cn.youmi.framework.main.ContextProvider;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.view.CircleImageView;

/**
 * @author tangixyong
 * @version 2015-5-8 下午2:29:21
 */
public class MineMessageAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;

    private ArrayList<MineMessageModel> mList;

    public MineMessageAdapter(Context context) {
        super();
        mLayoutInflater = LayoutInflater.from(UmiwiApplication.getInstance());
    }

    public MineMessageAdapter(Context context, ArrayList<MineMessageModel> mList) {
        mLayoutInflater = LayoutInflater.from(UmiwiApplication.getInstance());
        this.mList = mList;
    }

    public void setData(ArrayList<MineMessageModel> mList) {
        this.mList = mList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mList != null && position < mList.size() - 1) {
            return mList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "ViewHolder"})
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = mLayoutInflater.inflate(R.layout.item_mine_message, null);

        Holder holder = getHolder(view);

        final MineMessageModel listBeans = mList.get(position);

        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        mImageLoader.loadImage(listBeans.getAvatar(), holder.image);
        holder.title.setText(htmlString(listBeans.getUsername(), listBeans.getCtime()));
        holder.authorname.setText(listBeans.getContent());

        return view;
    }

    private Holder getHolder(final View view) {
        Holder holder = (Holder) view.getTag();
        if (holder == null) {
            holder = new Holder(view);
            view.setTag(holder);
        }
        return holder;
    }

    private class Holder {

        public CircleImageView image;

        public TextView title;

        public TextView authorname;

        public Holder(View view) {
            image = (CircleImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
            authorname = (TextView) view.findViewById(R.id.authorname);
        }
    }

    private Spanned htmlString(String s1, String s2) {
        return (Html.fromHtml("<font color='#000000'>" + s1 + "  " + "</font>" + "  " + "<font color='#999999'>" + s2 + "</font>"));
    }
}
