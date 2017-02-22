package com.umiwi.ui.fragment.down;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.managers.VideoManager;
import com.umiwi.ui.model.VideoModel;
import com.umiwi.ui.util.SDCardManager;
import com.umiwi.ui.util.SDCardManager.SDCardInfo;
import com.umiwi.video.utils.PreferenceUtil;

import java.util.ArrayList;

import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.view.NumberProgressBar;

/**
 * 下载路径选择
 *
 * @author tangxiong
 * @version 2014年9月29日 下午2:45:24 TODO
 */
public class SelectDownloadPathFragment extends BaseFragment {

    private ListView mListView;
    private ArrayList<SDCardInfo> mList;
    private PathAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_frame_listview_layout, null);
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBarAndToolbarTitle(mActionBarToolbar, "选择下载路径");

        mListView = (ListView) view.findViewById(R.id.listView);
        mList = SDCardManager.getExternalStorageDirectory();

        if (mList.isEmpty()) {
            Toast.makeText(getActivity(), "没有找到可用的存储卡", Toast.LENGTH_SHORT).show();
        }

        mAdapter = new PathAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);

        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).path.equals(PreferenceUtil.getPreference(getActivity(), SDCardManager.DOWNLOAD_FILE_PATH))) {
                mAdapter.setSelected(i);
                mAdapter.notifyDataSetChanged();
                break;
            } else {
                mAdapter.setSelected(0);
                mAdapter.notifyDataSetChanged();
            }
        }

        mListView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                return false;
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


    private static int selectId = -1;

    public class PathAdapter extends BaseAdapter {

        private LayoutInflater mLayoutInflater;

        private ArrayList<SDCardInfo> mList;

        public PathAdapter(Context context, ArrayList<SDCardInfo> mList) {
            mLayoutInflater = ((Activity) context).getLayoutInflater();
            this.mList = mList;
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

        public void setSelected(int str) {
            selectId = str;
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            view = mLayoutInflater.inflate(R.layout.setting_down_path_item,
                    null, false);

            Holder holder = getHolder(view);

            final SDCardInfo listBeans = mList.get(position);
            holder.title.setText("存储卡" + (position + 1));
            ArrayList<VideoModel> DownloadedList = VideoManager.getInstance().getDownloadedList();//已经下载
            long size = 0;
            for (VideoModel video : DownloadedList) {
                size = size + video.getFileSize();
            }
            holder.totalStorage.setText(SDCardManager.getStorageTotal(listBeans.path));
            holder.videoStorage.setText("视频缓存：" + SDCardManager.convertStorage(size));
            holder.freeStorage.setText("可用空间 " + SDCardManager.getStorageFree(listBeans.path));
            holder.mProgressbar.setMax(1000);
            holder.mProgressbar.setProgress(SDCardManager.getStoragePerce(listBeans.path));

            holder.selectionButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SDCardManager sdCardManager = new SDCardManager(mList.get(position).path);
                    if (sdCardManager.exist()) {
                        PreferenceUtil.savePreference(getContext(), SDCardManager.DOWNLOAD_FILE_PATH, mList.get(position).path);
                        setSelected(position);
                    } else {
                        buttonView.setChecked(false);
                        Toast.makeText(getActivity(), "当前存储路径不可用", Toast.LENGTH_LONG).show();
                    }
                }
            });

            if (selectId == position) {
                holder.selectionButton.setChecked(true);
            }
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

            public TextView title;
            public RadioButton selectionButton;
            public NumberProgressBar mProgressbar;

            public TextView totalStorage;
            public TextView videoStorage;
            public TextView freeStorage;

            public Holder(View view) {
                title = (TextView) view.findViewById(R.id.down_path_textview);
                selectionButton = (RadioButton) view
                        .findViewById(R.id.down_path_radiobutton);
                totalStorage = (TextView) view
                        .findViewById(R.id.total_storage_size);
                videoStorage = (TextView) view
                        .findViewById(R.id.video_storage_size);
                freeStorage = (TextView) view
                        .findViewById(R.id.free_storage_size);
                mProgressbar = (NumberProgressBar) view
                        .findViewById(R.id.progressbar);
                mProgressbar.setProgress(0);
            }
        }
    }
}
