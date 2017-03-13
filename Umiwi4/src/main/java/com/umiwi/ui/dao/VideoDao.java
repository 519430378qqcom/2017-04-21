package com.umiwi.ui.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import cn.youmi.framework.util.SingletonFactory;

import com.umiwi.ui.model.VideoModel;
import com.umiwi.ui.model.VideoModel.DownloadStatus;

public class VideoDao extends BaseDao {

	public VideoDao() {
		super();
	}

	private static final String VIDEO_TABLE = "video_table";

	public static VideoDao getInstance() {
		return SingletonFactory.getInstance(VideoDao.class);
	}

	/**
	 *获取所有专辑的ID
	 * 
	 * @param albumid
	 * @return
	 */
	public ArrayList<String> getAlbumIds() {
		ArrayList<String> albumIds = new ArrayList<String>();
		CursorWrapper cursor = query(
				"SELECT  albumID FROM [video_table] ", null);
		
		while (cursor.moveToNext()) {
			String albumid = cursor.getString("albumID");
 			if(!albumIds.contains(albumid)) {
				albumIds.add(albumid);
			}
			
		}
		cursor.close();
		return albumIds;
	}

	
	/**
	 * 根据专辑ID 获取视频列表
	 * 
	 * @param albumid
	 * @return
	 */
	public ArrayList<VideoModel> getByAlbumid(String albumid) {
		ArrayList<VideoModel> videos = new ArrayList<VideoModel>();
		CursorWrapper cursor = query(
				"SELECT DISTINCT * FROM [video_table] WHERE [albumID] = ? AND [istry] != 1;",
				new String[] { albumid });
		while (cursor.moveToNext()) {
			VideoModel video = cursorToVideoModel(cursor);
			videos.add(video);
		}
		cursor.close();
		return videos;
	}

	/**
	 * 根据视频id 获取视频信息
	 * 
	 * @param videoid
	 * @return
	 */
	public VideoModel getByVideoid(String videoid) {
		VideoModel video = new VideoModel();
		CursorWrapper cursor = query(
				"SELECT DISTINCT * FROM [video_table] WHERE [videoID] = ? AND [istry] = 0;",
				new String[] { videoid });
		if (cursor.moveToNext()) {
			video = cursorToVideoModel(cursor);
		}else{
			CursorWrapper c = query(
					"SELECT DISTINCT * FROM [video_table] WHERE [videoID] = ? AND [istry] = 1;",
					new String[] { videoid });
			if(c.moveToNext()){
				video = cursorToVideoModel(c);
			}
			c.close();
		}
		cursor.close();
		return video;
	}

	/**
	 * 根据下载状态查找视频列表
	 * 
	 * @param downloadstatus
	 * @return
	 */
	public ArrayList<VideoModel> getByDownloadStatus(
			DownloadStatus downloadStatus) {
		ArrayList<VideoModel> videos = new ArrayList<VideoModel>();
		CursorWrapper cursor = query(
				"SELECT DISTINCT * FROM [video_table] WHERE [downloadStatus] = ?;",
				new String[] { String.valueOf(downloadStatus.getValue()) });
		while (cursor.moveToNext()) {
			VideoModel video = cursorToVideoModel(cursor);
			videos.add(video);
		}

		cursor.close();

		return videos;
	}

	/**
	 * 获取正在下载和暂停下载的视频列表
	 */
	public ArrayList<VideoModel> getDownloadingList() {
		ArrayList<VideoModel> videos = new ArrayList<VideoModel>();
		return videos;
		
	}
	
	public ArrayList<VideoModel> getDownloadingListByAlbumId(String albumId) {
		ArrayList<VideoModel> videos = new ArrayList<VideoModel>();
		CursorWrapper cursor = query(
				"SELECT * FROM [video_table] WHERE [albumId]=? and  [downloadStatus] != ? and  [downloadStatus] != ? ",
				new String[] { albumId, String.valueOf(DownloadStatus.DOWNLOAD_COMPLETE.getValue()), String.valueOf(DownloadStatus.NOTIN.getValue())  });
		while (cursor.moveToNext()) {
			VideoModel video = cursorToVideoModel(cursor);
			videos.add(video);
		}

		cursor.close();

		return videos;

	}
	
	public ArrayList<VideoModel> getDownloadingListByVideoId(String videoId) {
		ArrayList<VideoModel> videos = new ArrayList<VideoModel>();
		CursorWrapper cursor = query(
				"SELECT * FROM [video_table] WHERE [videoId]=? ",
				new String[] { videoId  });
		while (cursor.moveToNext()) {
			VideoModel video = cursorToVideoModel(cursor);
			videos.add(video);
		}

		cursor.close();

		return videos;
	}
	
	

	/**
	 * 获取已经下载的列表
	 */
	public ArrayList<VideoModel> getDownloadedList() {
		return  getByDownloadStatus(VideoModel.DownloadStatus.DOWNLOAD_COMPLETE);
	}
	
	public ArrayList<VideoModel> getDownloadedListByAlbumId(String albumId) {
//		return getListByAlbumIdDownloadStatus(albumId, DownloadStatus1.DOWNLOAD_COMPLETE);
		if (null == albumId) {
			return new ArrayList<VideoModel>();
		}
		return getListByAlbumIdDownloadStatus(albumId, "4");
	}
	
	public ArrayList<VideoModel> getListByAlbumIdDownloadStatus(String albumId, String downloadStatus) {
		ArrayList<VideoModel> videos = new ArrayList<VideoModel>();
		CursorWrapper cursor = query(
				"SELECT DISTINCT * FROM [video_table] WHERE [albumId]=? and  [downloadStatus] = ? ",
				new String[] {albumId,downloadStatus});
		while (cursor.moveToNext()) {
			VideoModel video = cursorToVideoModel(cursor);
			videos.add(video);
		}

		cursor.close();

		return videos;
	}
	/**
	 * 插入数据库
	 */
	public void save(VideoModel video) {
		save(video,getDb());
	}
	
	public void save(VideoModel video,SQLiteDatabase db){
		db.beginTransaction();
		try {
			int istry = video.isTry() ? 1 : 0;
			Cursor c = db.rawQuery(
					"SELECT DISTINCT [videoID] FROM [video_table] WHERE [videoID] = ? AND [istry] = ?;",
					new String[] { video.getVideoId() ,String.valueOf(istry)});
			if (c.moveToNext()) {
				db.update(VIDEO_TABLE, contatoToContentValues(video),
						"videoID = ?", new String[] { video.getVideoId() });
			} else {
				db.insert(VIDEO_TABLE, null, contatoToContentValues(video));
			}
			c.close();
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	/**
	 * 删除指定视频
	 * 
	 * @param video
	 */

	public void delete(VideoModel video) {
		getDb().delete("video_table", "albumID= ? and videoID=? ",
				new String[] { video.getAlbumId(), video.getVideoId() });
	}

	/**
	 * 删除指定视频 重载
	 * 
	 * @param video
	 */

	// TODO duplicate?
	public void delete(String albumid, String videoid) {
		getDb().delete("video_table", "albumID= ? and videoID=? ",
				new String[] { albumid, videoid });
	}
	
	public void deleteAllVide() {
		getDb().delete("video_table", null, null);
	}

	public void setDownloadStatusByVideoId(String VideoId, int downloadStatus) {
		ContentValues values = new ContentValues();
		values.put("downloadStatus", downloadStatus);
		getDb().update(VIDEO_TABLE, values, "videoID = ?",
				new String[] { VideoId });
	}
	
 
	public void setStatusByVideoId(String videoId, DownloadStatus downloadStatus) {
		ContentValues values = new ContentValues();
		values.put("downloadStatus",
				downloadStatus.getStringValue());
		getDb().update("video_table", values, " videoID=" + videoId, null);
	}

	
	/**
	 * 视频暂停下载
	 */
	public void setPause(String videoId) {
		ContentValues values = new ContentValues();
		values.put("pause",
				VideoModel.DownloadStatus.DOWNLOAD_PAUSE.getStringValue());
		getDb().update("video_table", values, " videoID=" + videoId, null);
	}
	

	/**
	 * 设置已经观看
	 */
	public void setWatchedByVideoId(String videoId) {
		ContentValues values = new ContentValues();
		values.put("watched", 1);
		getDb().update("video_table", values, " videoID=" + videoId, null);
	}
	
	
	
	public void updateExpiretimeUidByAlbumId(String albumId, String exipretime, String uid, String albumTitle) {
		ContentValues values = new ContentValues();
		values.put("expiretime", exipretime);
		values.put("uid", uid);
		values.put("albumTitle", albumTitle);
 		getDb().update("video_table", values, " albumID=" + albumId, null);
	}
	
	private VideoModel cursorToVideoModel(CursorWrapper cursor) {
		VideoModel video = new VideoModel();
		video.setVideoId(cursor.getString("videoID"));
		video.setAlbumId(cursor.getString("albumID"));
		video.setAlbumTitle(cursor.getString("albumTitle"));
		video.setAlbumImageurl(cursor.getString("albumImageurl"));
		
		String decodedUrl = cursor.getString("videoUrl");
		if(decodedUrl != null){
			String url = new String(Base64.decode(decodedUrl, Base64.URL_SAFE));
			video.setVideoUrl(url);
		}
		video.setFileSize(cursor.getInt("fileSize"));
		video.setFilePath(cursor.getString("filePath"));
		video.setFileName(cursor.getString("fileName"));
		video.setExt(cursor.getString("extention"));
		video.setImageURL(cursor.getString("imageURL"));
		video.setTitle(cursor.getString("title"));
		video.setExpiretime(cursor.getString("expireTime"));
		video.setUid(cursor.getString("uid"));
		video.setDownloadStatus(DownloadStatus.ValueOf(cursor.getInt("downloadStatus")));
		video.setClasses(cursor.getString("classes"));
		video.setLastwatchposition(cursor.getInt("lastPosition"));
		video.setFileheader(cursor.getBlob("fileHeader"));
		
		if(cursor.getInt("watched")>0) {
			video.setWatched(true);
		} else {
			video.setWatched(false);
		}
		
		if(cursor.getInt("istry") == 1) {
			video.setTry(true);
		} else {
			video.setTry(false);
		}
		return video;
	}

	private ContentValues contatoToContentValues(VideoModel video) {
		ContentValues values = new ContentValues();
		if (video.getVideoId() != null) {
			values.put("videoID", video.getVideoId());
		}
		if (video.getAlbumId() != null) {
			values.put("albumID", video.getAlbumId());
		}
		
		if (video.getAlbumTitle() != null) {
			values.put("albumTitle", video.getAlbumTitle());
		}
		
		if (video.getAlbumImageurl() != null) {
			values.put("albumImageurl", video.getAlbumImageurl());
		}
		
		if (video.getVideoUrl() != null) {
			String url = Base64.encodeToString(video.getVideoUrl().getBytes(), Base64.URL_SAFE);
			values.put("videoUrl", url);
		}
		if (video.getFilePath() != null) {
			values.put("filePath", video.getFilePath());
		}

		if (video.getFileName() != null) {
			values.put("fileName", video.getFileName());
		}
		

		if (video.getFileSize() >0) {
			values.put("fileSize", video.getFileSize());
		}

		if (video.getExt() != null) {
			values.put("extention", video.getExt());
		}
		if (video.getImageURL() != null) {
			values.put("imageURL", video.getImageURL());
		}

		if (video.getTitle() != null) {
			values.put("title", video.getTitle());
		}

		if (video.getExpiretime() != null) {
			values.put("expireTime", video.getExpiretime());
		}

		if (video.getUid() != null) {
			values.put("uid", video.getUid());
		}

		if (video.getDownloadStatus() != null) {
			values.put("downloadStatus", video.getDownloadStatus().getValue());
		}

		if (video.getFileName() != null) {
			values.put("classes", video.getClasses());
		}

		if (video.getLastwatchposition() != 0) {
			values.put("lastPosition", video.getLastwatchposition());
		}

		if (video.isTry()) {
			values.put("istry", 1);
		} else {
			values.put("istry", 0);
		}
		
		if (video.getFileheader() != null) {
			values.put("fileHeader", video.getFileheader());
		}
		

		if (video.isWatched()) {
			values.put("watched", 1);
		} else {
			values.put("watched", 0);
		}
		
		return values;
	}

	public ArrayList<VideoModel> getDownloadingVideos() {
		ArrayList<VideoModel> videos = new ArrayList<VideoModel>();
		String[] args = { String.valueOf(DownloadStatus.NOTIN.getValue()),
				String.valueOf(DownloadStatus.DOWNLOAD_COMPLETE.getValue()), };
		String sql = "SELECT * FROM [video_table] WHERE [downloadStatus] IN (?,?,?,?)";
		sql = "SELECT * FROM [video_table] WHERE ([downloadStatus] != ? AND [downloadStatus] != ?)";
		CursorWrapper c = query(sql, args);
		while (c.moveToNext()) {
			VideoModel video = cursorToVideoModel(c);
			videos.add(video);
		}
		c.close();
		return videos;
	}
}
