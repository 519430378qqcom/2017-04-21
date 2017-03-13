package com.umiwi.ui.fragment.down;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.DownloadedListAdapter;
import com.umiwi.ui.adapter.DownloadedListAdapter.HolderFirst;
import com.umiwi.ui.fragment.course.CourseSequenceListFragment;
import com.umiwi.ui.managers.AudioDownloadManager;
import com.umiwi.ui.managers.AudioManager;
import com.umiwi.ui.managers.VideoDownloadManager;
import com.umiwi.ui.managers.VideoDownloadManager.DownloadStatusListener;
import com.umiwi.ui.managers.VideoManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.AlbumModel;
import com.umiwi.ui.model.AudioModel;
import com.umiwi.ui.model.VideoModel;
import com.umiwi.ui.model.VideoModel.DownloadStatus;
import com.umiwi.ui.util.SDCardManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.util.NetworkManager;

public class DownloadedListFragment extends BaseFragment implements DownloadStatusListener,AudioDownloadManager.DownloadStatusListener1 {
    private ArrayList<VideoModel> DownloadedList;
    private ArrayList<VideoModel> DownloadingList;
    private ArrayList<AudioModel> downLoadingAudioList;
    private ArrayList<AudioModel> downLoadedAudioList;
    private ArrayList<AlbumModel> albums = new ArrayList<AlbumModel>();
    private DownloadedListAdapter mAdapter;

    private ListView downloadedListView;
    private ImageView nodownloadedView;

    private TextView diskinfoTextView;
    private String DownloadedSize = "";
    //	private String AvaliableSize = "";
    private SharedPreferences mSharedPreferences;

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
        menu.findItem(R.id.delete).setVisible(false);
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

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloaded_list, null);
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBarAndToolbarTitle(mActionBarToolbar, "下载");
        onPostInflateView(view);
        return view;
    }

    protected void onPostInflateView(View view) {

        diskinfoTextView = (TextView) view.findViewById(R.id.diskinfo_textview);
        nodownloadedView = (ImageView) view.findViewById(R.id.nodownloaded);
        downloadedListView = (ListView) view.findViewById(R.id.listView);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        nodownloadedView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(CourseSequenceListFragment.KEY_URL, "http://i.v.youmi.cn/apireader/fromCategoryOrTutorGetAlbumList?ctype=clientnew");
                intent.putExtra(CourseSequenceListFragment.KEY_ACTION_TITLE, "最新课程");
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseSequenceListFragment.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        mAdapter = new DownloadedListAdapter(getActivity(), this.albums);
        downloadedListView.setAdapter(mAdapter);

        downloadedListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AlbumModel album = (AlbumModel) mAdapter.getItem(position);
                if (mAdapter.getTrashStatus()) {
                    CheckBox albumCheckBox = (CheckBox) view.findViewById(R.id.album_checkbox);
                    albumCheckBox.performClick();
                } else {

                    Intent i_downloaded = new Intent(getActivity(), UmiwiContainerActivity.class);

                    if (album.getAlbumId() != null) {
                        if (album.getUrl().contains("mp3")) {
                            i_downloaded.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, DownloadedAudioFragment.class);
                            i_downloaded.putExtra(DownloadedAudioFragment.KEY_ALBUM_TITLE, album.getTitle());
                            i_downloaded.putExtra(DownloadedAudioFragment.KEY_ALBUM_ID, album.getAlbumId());
                        } else {

                            i_downloaded.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, DownloadedFragment.class);
                            i_downloaded.putExtra(DownloadedFragment.KEY_ALBUM_TITLE, album.getTitle());
                            i_downloaded.putExtra(DownloadedFragment.KEY_ALBUM_ID, album.getAlbumId());
                            Log.e("TAG", "album.getTitle() + album.getAlbumId() = " +album.getTitle() );
                            Log.e("TAG", " album.getAlbumId() = " +album.getAlbumId() );
                            Log.e("TAG", "album的URL=" + album.getUrl());
                        }

                    } else {
                        i_downloaded.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, DownloadingFragment.class);
                    }
                    startActivity(i_downloaded);
                }


            }
        });

        downloadedListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                trashClick();
                mMode = mActionBarToolbar.startActionMode(new DeleteCallback());
                return false;
            }
        });

        VideoDownloadManager.getInstance().registerDownloadStatusListener(this);
        AudioDownloadManager.getInstance().registerDownloadStatusListener(this);
    }

    @Override
    public void onDownloadStatusChange(AudioModel audio, AudioModel.DownloadStatus1 ds, String msg) {
        switch (ds) {
            case DOWNLOAD_COMPLETE:
                update();
                break;
            default:
                break;
        }
    }

    @Override
    public void onAudioProgressChange(AudioModel audio, int current, int total, int speed) {
        audio.setDownloadedSize(current);
        audio.setFileSize(total);
        audio.setSpeed(speed);
        updateDownloading();
    }


    private class DeleteCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode arg0, Menu menu) {
            arg0.setTitle("删除");
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
                    SparseBooleanArray checkedAlbumids = mAdapter.getCheckedAlbumIds();
                    for (int i = 0; i < checkedAlbumids.size(); i++) {
                        int albumId = checkedAlbumids.keyAt(i);
                        if (checkedAlbumids.get(albumId)) {
                            VideoDownloadManager.getInstance().deleteDownloadedByAlbumId("" + albumId);
                            AudioDownloadManager.getInstance().deleteDownloadedByAlbumId("" + albumId);
                        }
                    }

                    update();
                    mAdapter.initCheckedAlbumIds();
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
    public void onStart() {
        super.onStart();
        if (YoumiRoomUserManager.getInstance().isLogin() && NetworkManager.getInstance().isWifi()) {
            ArrayList<VideoModel> videos = VideoDownloadManager.getInstance().getDownloadingList();
            if (!videos.isEmpty()) {
                for (VideoModel video : videos) {
                    VideoDownloadManager.getInstance().addDownload(video);
                }
//				ToastU.showLong(getActivity(), "连接到WIFI,开始下载未完全视频");
            }
            ArrayList<AudioModel> audios = AudioDownloadManager.getInstance().getDownloadingList();
            if(!audios.isEmpty()) {
                for (AudioModel audio: audios){
                    AudioDownloadManager.getInstance().addDownload(audio);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
        updateDownloading();
        MobclickAgent.onPageStart(fragmentName);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(fragmentName);
    }

    private void update() {

        DownloadedList = VideoManager.getInstance().getDownloadedList();//已经下载
        downLoadedAudioList = AudioManager.getInstance().getDownloadedList();
        DownloadingList = VideoDownloadManager.getInstance().getDownloadingList();//正在下载
        downLoadingAudioList = AudioDownloadManager.getInstance().getDownloadingList() ;


        long size = 0;
        for (VideoModel video : DownloadedList) {
            //	System.out.println("TMD===="+video.getFilePath()+" name "+video.getFileName()+"  size"+video.getFileSize());
            size = size + video.getFileSize();
        }
        for (AudioModel audio : downLoadedAudioList) {
            size = size + audio.getFileSize();
        }

        DownloadedSize = SDCardManager.convertStorage(size);
//		AvaliableSize =  VideoDownloadManager.getInstance().getSotrageAvailableSize();
        //	System.out.println("TMD==="+StorageUtils.getStoragePath());
        diskinfoTextView.setText("已下载" + DownloadedSize
                + " \t" + mSharedPreferences.getString("downname", "") + "可用空间"
                + SDCardManager.getStorageFree(SDCardManager.getDefauleSDCardPath()));

        initListData();
        mAdapter.setAlbums(this.albums);
    }

    public void initListData() {
        if (albums != null) {
            albums.clear();
        }

        if (DownloadedList == null && DownloadingList == null &&
                downLoadedAudioList == null && downLoadingAudioList == null) {
            downloadedListView.setVisibility(View.GONE);
            nodownloadedView.setVisibility(View.VISIBLE);

            return;
        }


        albums = new ArrayList<AlbumModel>();

        AlbumModel albumFake = new AlbumModel();
        if (DownloadingList != null && DownloadingList.size() > 0 ) {
            albumFake.setTitle("正在下载");
            albumFake.setDownloadFilesize(12);
            albumFake.setDownloadVideoCount(1);
            albums.add(albumFake);
            //System.out.println("TMD====添加后看文件个数："+albums.size() );
        }
        if(downLoadingAudioList != null && downLoadingAudioList.size() > 0) {
            albumFake.setTitle("正在下载");
            albumFake.setDownloadFilesize(12);
            albumFake.setDownLoadAudioCount(1);
            albums.add(albumFake);
        }

        Map<String, Boolean> watcheds = new HashMap<String, Boolean>();
        if (DownloadedList != null && DownloadedList.size() > 0) {
            for (int i = 0; i < DownloadedList.size(); i++) {
                VideoModel video = DownloadedList.get(i);
                String albumid = video.getAlbumId();

                //	System.out.println("TMD====ALBUMID "+albumid);
                if (albumid == null) {
                    albumid = "";
                }

                if (watcheds.get(albumid) == null) {
                    watcheds.put(albumid, true);
                }

                if (!video.isWatched()) {
                    watcheds.put(albumid, false);
                }

                Boolean hasin = false;
                for (AlbumModel mAlbum : albums) {
                    if (mAlbum.getAlbumId() != null && mAlbum.getAlbumId().equals(video.getAlbumId())) {
                        //System.out.println("TMD=="+mAlbum.getDownloadFilesize()+"   "+video.getFileSize());

                        mAlbum.setDownloadFilesize(mAlbum.getDownloadFilesize() + video.getFileSize());
                        mAlbum.setDownloadVideoCount(mAlbum.getDownloadVideoCount() + 1);
                        //	System.out.println("TMD====SIZE  "+mAlbum.getDownloadVideoCount());
                        hasin = true;
                    }
                }

                if (hasin == false) {
                    AlbumModel album = new AlbumModel();
                    album.setAlbumId(video.getAlbumId());
                    album.setImageURL(video.getAlbumImageurl());
                    if (video.getAlbumImageurl() == null) {
                        album.setImageURL(video.getVideoUrl());
                    }
                    album.setTitle(video.getAlbumTitle());
                    album.setDownloadFilesize(video.getFileSize());
                    album.setDownloadVideoCount(1);
                    album.setWatched(watcheds.get(albumid));
                    album.setUrl(video.getVideoUrl());
                    albums.add(album);
                    //System.out.println("TMD====SIZE"+albums.size());
                } else {

                }


            }
        }
        if (downLoadedAudioList != null && downLoadedAudioList.size() > 0) {
            for (int i = 0; i < downLoadedAudioList.size(); i++) {
                AudioModel audio = downLoadedAudioList.get(i);
                String albumid = audio.getAlbumId();

                //	System.out.println("TMD====ALBUMID "+albumid);
                if (albumid == null) {
                    albumid = "";
                }

                if (watcheds.get(albumid) == null) {
                    watcheds.put(albumid, true);
                }

                if (!audio.isWatched()) {
                    watcheds.put(albumid, false);
                }

                Boolean hasin = false;
                for (AlbumModel mAlbum : albums) {
                    if (mAlbum.getAlbumId() != null && mAlbum.getAlbumId().equals(audio.getAlbumId())) {
                        //System.out.println("TMD=="+mAlbum.getDownloadFilesize()+"   "+video.getFileSize());

                        mAlbum.setDownloadFilesize(mAlbum.getDownloadFilesize() + audio.getFileSize());
                        mAlbum.setDownloadVideoCount(mAlbum.getDownLoadAudioCount() + 1);
                        //	System.out.println("TMD====SIZE  "+mAlbum.getDownloadVideoCount());
                        hasin = true;
                    }
                }

                if (hasin == false) {
                    AlbumModel album = new AlbumModel();
                    album.setAlbumId(audio.getAlbumId());
                    album.setImageURL(audio.getImageURL());
                    if (audio.getImageURL() == null) {
                        album.setImageURL(audio.getVideoUrl());
                    }
                    album.setTitle(audio.getAlbumTitle());
                    album.setDownloadFilesize(audio.getFileSize());
                    album.setDownLoadAudioCount(1);
                    album.setWatched(watcheds.get(albumid));
                    album.setUrl(audio.getVideoUrl());
                    albums.add(album);
                    //System.out.println("TMD====SIZE"+albums.size());
                } else {

                }


            }
        }
        //System.out.println("TMD====SIZE"+albums.size());
        if (albums.size() <= 0) {
            downloadedListView.setVisibility(View.GONE);
            nodownloadedView.setVisibility(View.VISIBLE);
        } else {
            downloadedListView.setVisibility(View.VISIBLE);
            nodownloadedView.setVisibility(View.GONE);
        }
    }

    public void updateDownloading() {

        if (DownloadingList == null && downLoadingAudioList == null) {
            return;
        }


        float speed = 0;
        boolean pauseAll = true;

        for (VideoModel video : DownloadingList) {
            if (video.getDownloadStatus() != DownloadStatus.DOWNLOAD_PAUSE) {
                pauseAll = false;
            }
//            if (video.getDownloadStatus() == DownloadStatus1.DOWNLOAD_IN) {
//                speed = speed + video.getSpeed();
//            }
        }
        for (AudioModel audio : downLoadingAudioList) {
            if(audio.getDownloadStatus() != AudioModel.DownloadStatus1.DOWNLOAD_PAUSE) {
                pauseAll = false;
            }
        }

        for (AudioModel audio : downLoadingAudioList) {
            if(audio.getDownloadStatus() == AudioModel.DownloadStatus1.DOWNLOAD_IN) {
                speed = speed + audio.getSpeed();
            }
        }
        for (VideoModel video : DownloadingList) {
            if (video.getDownloadStatus() == DownloadStatus.DOWNLOAD_IN) {
                speed = speed + video.getSpeed();
            }
        }

        mAdapter.setPauseAll(pauseAll);
        int downloadNum = DownloadingList.size() + downLoadingAudioList.size();
        mAdapter.setSpeed(speed);
        mAdapter.setDownloadNum(downloadNum);
        View convertView = downloadedListView.getChildAt(0);
        if (convertView != null && convertView.getId() == R.id.itemview_relativelayout_first) {
            HolderFirst holder = (HolderFirst) convertView.getTag();
            DecimalFormat df = new DecimalFormat("0.00");
            String speedtext = "";
            if (speed > (1024 * 1024)) {
                speedtext = df.format(speed / (1024 * 1024)) + " M/s";
            } else {
                speedtext = df.format(speed / (1024)) + " KB/s";
            }

            if (pauseAll == true) {
                holder.speedTextView.setText("已暂停");
            } else {
                holder.speedTextView.setText(speedtext);
            }

            holder.videocountTextView.setText("正在下载(" + downloadNum + ")");
        }

    }

    @Override
    public void onDownloadStatusChange(VideoModel video, DownloadStatus ds,
                                       String msg) {
        switch (ds) {
            case DOWNLOAD_COMPLETE:
                update();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChange(VideoModel video, int current, int total,
                                 int speed) {
        video.setDownloadedSize(current);
        video.setFileSize(total);
        video.setSpeed(speed);
        updateDownloading();
    }



    public void trashClick() {
        mAdapter.toggleTrashStatus();
        mAdapter.notifyDataSetChanged();
    }

}