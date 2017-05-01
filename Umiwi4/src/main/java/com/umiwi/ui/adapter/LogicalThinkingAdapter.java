package com.umiwi.ui.adapter;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.AttemptBean;
import com.umiwi.ui.fragment.home.alreadyshopping.LogicalThinkingFragment;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.CacheUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/2/24.
 */
public class LogicalThinkingAdapter extends BaseAdapter {
    private FragmentActivity activity;
    //    private List<LogincalThinkingBean.RecordBean> mList;
    private List<AttemptBean.RAttenmpInfo.RecordsBean> mList;

    public LogicalThinkingAdapter(FragmentActivity activity, List<AttemptBean.RAttenmpInfo.RecordsBean> mList) {
        this.activity = activity;
        this.mList = mList;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
//        if (convertView == null) {
//            viewHolder = new ViewHolder();
//            convertView = View.inflate(activity, R.layout.logical_thinking_item, null);
//            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
//            viewHolder.tv_attempt = (TextView) convertView.findViewById(R.id.tv_attempt);
//            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
//            viewHolder.tv_watchnum = (TextView) convertView.findViewById(R.id.tv_watchnum);
//            viewHolder.tv_imageview = (ImageView) convertView.findViewById(R.id.tv_imageview);
//            viewHolder.tv_description = (TextView) convertView.findViewById(R.id.tv_description);
//
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        Log.e("是否显示试读", "是否显示试读=" + mList.get(position).istry());
//        if (mList.get(position).istry()) {
//            viewHolder.tv_attempt.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.tv_attempt.setVisibility(View.GONE);
//        }
//        viewHolder.tv_title.setText(mList.get(position).getTitle());
//        viewHolder.tv_time.setText(mList.get(position).getOnlinetime());
//        viewHolder.tv_watchnum.setText(mList.get(position).getWatchnum());
//        if (!TextUtils.isEmpty(mList.get(position).getDescription())) {
//            viewHolder.tv_description.setText(mList.get(position).getDescription());
//        } else {
//            viewHolder.tv_description.setVisibility(View.GONE);
//        }
//        Glide.with(activity).load(mList.get(position).getImage()).into(viewHolder.tv_imageview);
        //2017年4月19日新版
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(activity, R.layout.logical_thinking_new_item, null);
            viewHolder.iv_logical_point = (ImageView) convertView.findViewById(R.id.iv_logical_point);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_watchnum = (TextView) convertView.findViewById(R.id.tv_watchnum);
            viewHolder.tv_attempt = (TextView) convertView.findViewById(R.id.tv_attempt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_title.setText(mList.get(position).getTitle());
        viewHolder.tv_time.setText(mList.get(position).getOnlinetime());
        viewHolder.tv_watchnum.setText(mList.get(position).getWatchnum());

//        Log.e("是否显示试读", "是否显示试读=" + mList.get(position).istry());
        if (mList.get(position).istry()) {
            viewHolder.tv_attempt.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_attempt.setVisibility(View.GONE);
        }
        String uid = YoumiRoomUserManager.getInstance().getUid();
        //点击每个item记录变色
        String idArray = CacheUtil.getStringFile(activity, LogicalThinkingFragment.READ_ARRAY_ID);
        if (idArray.contains(mList.get(position).getId() + uid + LogicalThinkingFragment.NO_BUY)) {
            viewHolder.tv_title.setTextColor(Color.GRAY);
            viewHolder.iv_logical_point.setBackgroundResource(R.drawable.logical_point_gray);
        } else {
            viewHolder.tv_title.setTextColor(Color.BLACK);
            viewHolder.iv_logical_point.setBackgroundResource(R.drawable.logical_point_orange);
        }

        return convertView;
    }

    class ViewHolder {
        private TextView tv_title, tv_time, tv_watchnum, tv_description, tv_attempt;
        private ImageView iv_logical_point, tv_imageview;
    }
}
