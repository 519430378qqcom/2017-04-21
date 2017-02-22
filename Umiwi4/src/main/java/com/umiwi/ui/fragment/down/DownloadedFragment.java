package com.umiwi.ui.fragment.down;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.PlayerActivity;
import com.umiwi.ui.adapter.DownloadedAdapter;
import com.umiwi.ui.dialog.DownloadListDialog;
import com.umiwi.ui.fragment.PlayerFragment;
import com.umiwi.ui.managers.VideoDownloadManager;
import com.umiwi.ui.managers.VideoManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.VideoModel;
import com.umiwi.ui.util.LoginUtil;

import java.util.ArrayList;

import cn.youmi.framework.fragment.BaseFragment;

/**
 * @Author xiaobo
 * @Version 2014年6月13日上午10:18:36
 */
public class DownloadedFragment extends BaseFragment {
    public static final String KEY_ALBUM_ID = "key.albumid";
    public static final String KEY_ALBUM_TITLE = "key.albumtitle";

    private ListView mListView;
    private DownloadedAdapter mAdapter;
    private ArrayList<VideoModel> videos = new ArrayList<VideoModel>();
    private String albumId;
    private String albumTitle;

    public static DownloadedFragment newInstance(String albumId, String albumTitle) {
        DownloadedFragment f = new DownloadedFragment();
        f.setAlbumId(albumId);
        f.setAlbumTitle(albumTitle);
        return f;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    ActionMode mMode;
    Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        inflater.inflate(R.menu.toolbar_delete, menu);
//		menu.findItem(R.id.delete).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                trashClick();
                if (!mAdapter.isEmpty()) {
                    mMode = mActionBarToolbar.startActionMode(new DeleteCallback());
                }

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(AppCompatActivity a) {
        super.onActivityCreated(a);
        albumId = a.getIntent().getStringExtra(KEY_ALBUM_ID);
        albumTitle = a.getIntent().getStringExtra(KEY_ALBUM_TITLE);
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frame_listview_layout, null);
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBarAndToolbarTitle(mActionBarToolbar, albumTitle);

        mListView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new DownloadedAdapter(getActivity(), this.videos);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(downItemClick);

        return view;
    }


    private class DeleteCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.setTitle("删除");
            MenuItem deleteItem = menu.add(0, 1, 2, "删除").setIcon(R.drawable.ic_delete);
            MenuItemCompat.setShowAsAction(deleteItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case 1:
                    SparseBooleanArray checkedVideoids = mAdapter.getCheckedVideoIds();
                    ArrayList<Integer> videoids = new ArrayList<Integer>();
                    for (int i = 0; i < checkedVideoids.size(); i++) {
                        int VideoId = checkedVideoids.keyAt(i);
                        if (checkedVideoids.get(VideoId)) {
                            videoids.add(VideoId);
                        }
                    }

                    if (!videoids.isEmpty()) {
                        for (int videoId : videoids) {
                            VideoDownloadManager.getInstance().deleteDownloadedByVideoId("" + videoId);
                        }
                        update();
                        mAdapter.initCheckedVideoIds();
                    }
                    trashClick();

                    mode.finish();
                    break;

                default:
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode arg0) {
            trashClick();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        update();
        MobclickAgent.onPageStart(fragmentName);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(fragmentName);
    }

    private void update() {

        videos = VideoManager.getInstance().getDownloadedListByAlbumId(albumId);// 已经下载

        for (VideoModel video : videos) {

            if (!video.isWatched()) {
                VideoManager.getInstance().setWatchedByVideoId(video.getVideoId());
            }
        }

        ArrayList<VideoModel> albumVideos = VideoManager.getInstance().getVideosByAlbumId(albumId);
        if (albumVideos.size() > videos.size()) {
            VideoModel videoFake = new VideoModel();
            videoFake.setAlbumId(albumId);
            videos.add(videoFake);
        }

        mAdapter.setVideos(videos);

    }

    OnItemClickListener downItemClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (mAdapter.getTrashStatus()) {
                VideoModel video = (VideoModel) mAdapter.getItem(position);
                if (video.getVideoId() == null) {
                    return;
                }
                CheckBox videoCheckBox = (CheckBox) view.findViewById(R.id.video_checkbox);
                videoCheckBox.performClick();
            } else {
                VideoModel video = (VideoModel) mAdapter.getItem(position);
                if (video.getVideoId() == null) {
                    if (YoumiRoomUserManager.getInstance().isLogin()) {
                        ArrayList<VideoModel> videos = VideoManager.getInstance().getVideosByAlbumId(video.getAlbumId());
                        DownloadListDialog.getInstance().showDialog(getActivity(), videos);
                    } else {
                        LoginUtil.getInstance().showLoginView(getActivity());
                    }
                    return;
                }

                if (video.isLocalFileValid()) {

                    ArrayList<VideoModel> videos = mAdapter.getVideos();
                    ArrayList<VideoModel> playVideos = new ArrayList<VideoModel>();

                    for (VideoModel myvideo : videos) {
                        if (myvideo.getVideoId() != null) {
                            playVideos.add(myvideo);
                        }
                    }

                    Intent i = new Intent(getActivity(), PlayerActivity.class);
                    i.putExtra(PlayerFragment.KEY_VIDEO, video);
                    startActivity(i);
                } else {
                    Toast.makeText(getActivity(), "文件不存在！", Toast.LENGTH_SHORT).show();//TODO
                }
                return;
            }
        }
    };

    public void trashClick() {
        mAdapter.toggleTrashStatus();
        mAdapter.notifyDataSetChanged();
    }

}