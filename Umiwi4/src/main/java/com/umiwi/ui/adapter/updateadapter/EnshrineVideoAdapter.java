package com.umiwi.ui.adapter.updateadapter;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.EnshrineVideoBean;

import java.util.List;

import static com.umiwi.ui.R.id.cat;
import static com.umiwi.ui.R.id.tv_name;
import static com.umiwi.ui.R.id.tv_tutortitle;

/**
 * Created by Administrator on 2017/3/23.
 */

public class EnshrineVideoAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private List<EnshrineVideoBean.REnshrineVideo.EnshrineVideoInfo> mList;
    public EnshrineVideoAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder hoder = null;
        if (view == null) {
            hoder = new ViewHolder();
//            convertView = View.inflate(activity, R.layout.recentpalyvideo_item, null);
//            viewHolder.iv_author = (ImageView) convertView.findViewById(R.id.iv_author);
//            viewHolder.tv_video_time = (TextView) convertView.findViewById(R.id.tv_video_time);
//            viewHolder.tv_video_name = (TextView) convertView.findViewById(R.id.tv_video_name);
//            viewHolder.tv_video_detail = (TextView) convertView.findViewById(R.id.tv_video_detail);
//            viewHolder.tv_playnum = (TextView) convertView.findViewById(R.id.tv_playnum);
//            viewHolder.tv_playname = (TextView) convertView.findViewById(R.id.tv_playname);
            view = View.inflate(activity, R.layout.item_buy_video_new, null);
            hoder.cat = (TextView) view.findViewById(cat);
            hoder.title = (TextView) view.findViewById(R.id.title);
            hoder.price = (TextView) view.findViewById(R.id.price);
            hoder.playtime = (TextView) view.findViewById(R.id.playtime);
            hoder.watchnum = (TextView) view.findViewById(R.id.watchnum);
//            hoder.process = (TextView) view.findViewById(R.id.process);
            hoder.view_firstvisable = view.findViewById(R.id.view_firstvisable);
            hoder.tv_tutortitle = (TextView) view.findViewById(tv_tutortitle);
            hoder.tv_name = (TextView) view.findViewById(tv_name);

            view.setTag(hoder);

        } else {
            hoder = (ViewHolder) view.getTag();
        }
//        Glide.with(context).load(mList.get(position).getLimage()).into(viewHolder.iv_author);
//        viewHolder.tv_video_time.setText(mList.get(position).getPlaytime());
//        viewHolder.tv_video_name.setText(mList.get(position).getShorttitle());
//        viewHolder.tv_video_detail.setText(mList.get(position).getName() + " " + mList.get(position).getTutortitle());
//        viewHolder.tv_playnum.setText("播放 "+mList.get(position).getWatchnum());
//        if (!TextUtils.isEmpty(mList.get(position).getTime())) {
//            viewHolder.tv_playname.setText(mList.get(position).getTime());
//        } else {
//            viewHolder.tv_playname.setText("null");
//        }
        EnshrineVideoBean.REnshrineVideo.EnshrineVideoInfo enshrineVideoInfo = mList.get(position);
        String cat = enshrineVideoInfo.getTagname();
        if (!TextUtils.isEmpty(cat)) {
            hoder.cat.setText(cat);
            hoder.cat.setVisibility(View.VISIBLE);
        } else {
            hoder.cat.setVisibility(View.GONE);
        }
        if (position == 0) {
            hoder.view_firstvisable.setVisibility(View.GONE);
        } else {
            hoder.view_firstvisable.setVisibility(View.VISIBLE);
        }
        hoder.title.setText(enshrineVideoInfo.getShorttitle());
        hoder.price.setText(enshrineVideoInfo.getPrice());
        hoder.tv_name.setText(enshrineVideoInfo.getName());
        hoder.tv_tutortitle.setText(enshrineVideoInfo.getTutortitle());
        hoder.playtime.setText(enshrineVideoInfo.getPlaytime());
        hoder.watchnum.setText("播放 "+enshrineVideoInfo.getWatchnum());

        return view;
    }
    class ViewHolder{
//        private ImageView iv_author;
//        private TextView tv_video_time;
//        private TextView tv_video_name;
//        private TextView tv_video_detail;
//        private TextView tv_playnum;
//        private TextView tv_playname;

        TextView title;
        TextView cat;
        TextView tv_name,tv_tutortitle;
        TextView playtime;
        TextView watchnum;
        TextView price;
        View view_firstvisable;
    }
    public void setData(List<EnshrineVideoBean.REnshrineVideo.EnshrineVideoInfo> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }
}
