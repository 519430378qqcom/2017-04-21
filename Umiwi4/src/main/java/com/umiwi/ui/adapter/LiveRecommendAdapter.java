package com.umiwi.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.LiveDetailsBean;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/4/27.
 */

public class LiveRecommendAdapter extends RecyclerView.Adapter<LiveRecommendAdapter.LiveRecommendViewHolder>{
    private Context context;
    private List<LiveDetailsBean.RBean.RecordBean.DescriptionBean> list;
    public LiveRecommendAdapter(Context context, List<LiveDetailsBean.RBean.RecordBean.DescriptionBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public LiveRecommendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_live_recommend, parent, false);
        return new LiveRecommendViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(LiveRecommendViewHolder holder, int position) {
        holder.tv_content.setText(list.get(position).getWord());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class LiveRecommendViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.tv_content)
        TextView tv_content;
        public LiveRecommendViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }
}
