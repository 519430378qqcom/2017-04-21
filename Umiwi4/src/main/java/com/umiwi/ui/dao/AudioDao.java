package com.umiwi.ui.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;

import com.umiwi.ui.model.AudioModel;
import com.umiwi.ui.model.AudioModel.DownloadStatus;

import java.util.ArrayList;

import cn.youmi.framework.util.SingletonFactory;

public class AudioDao extends BaseDao {

	public AudioDao() {
		super();
	}

	private static final String AUDIO_TABLE = "audio_table";

	public static AudioDao getInstance() {
		return SingletonFactory.getInstance(AudioDao.class);
	}

	/**
	 *获取所有专辑的ID
	 *
	 * @param 
	 * @return
	 */
	public ArrayList<String> getAlbumIds() {
		ArrayList<String> albumIds = new ArrayList<String>();
		CursorWrapper cursor = query(
				"SELECT  albumID FROM [audio_table] ", null);

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
	public ArrayList<AudioModel> getByAlbumid(String albumid) {
		ArrayList<AudioModel> audios = new ArrayList<AudioModel>();
		CursorWrapper cursor = query(
				"SELECT DISTINCT * FROM [audio_table] WHERE [albumID] = ? AND [istry] != 1;",
				new String[] { albumid });
		while (cursor.moveToNext()) {
			AudioModel audio = cursorToVideoModel(cursor);
			audios.add(audio);
		}
		cursor.close();
		return audios;
	}

	/**
	 * 根据视频id 获取视频信息
	 *
	 * @param videoId
	 * @return
	 */
	public AudioModel getByVideoid(String videoId) {
		AudioModel audio = new AudioModel();
		CursorWrapper cursor = query(
				"SELECT DISTINCT * FROM [audio_table] WHERE [videoID] = ? AND [istry] = 0;",
				new String[] { videoId });
		if (cursor.moveToNext()) {
			audio = cursorToVideoModel(cursor);
		}else{
			CursorWrapper c = query(
					"SELECT DISTINCT * FROM [audio_table] WHERE [videoID] = ? AND [istry] = 1;",
					new String[] { videoId });
			if(c.moveToNext()){
				audio = cursorToVideoModel(c);
			}
			c.close();
		}
		cursor.close();
		return audio;
	}

	/**
	 * 根据下载状态查找视频列表
	 *
	 * @param 
	 * @return
	 */
	public ArrayList<AudioModel> getByDownloadStatus(
			DownloadStatus downloadStatus) {
		ArrayList<AudioModel> audios = new ArrayList<AudioModel>();
		CursorWrapper cursor = query(
				"SELECT DISTINCT * FROM [audio_table] WHERE [downloadStatus] = ?;",
				new String[] { String.valueOf(downloadStatus.getValue()) });
		while (cursor.moveToNext()) {
			AudioModel audio = cursorToVideoModel(cursor);
			audios.add(audio);
		}

		cursor.close();

		return audios;
	}

	/**
	 * 获取正在下载和暂停下载的视频列表
	 */
	public ArrayList<AudioModel> getDownloadingList() {
		ArrayList<AudioModel> audios = new ArrayList<AudioModel>();
		return audios;

	}

	public ArrayList<AudioModel> getDownloadingListByAlbumId(String albumId) {
		ArrayList<AudioModel> audios = new ArrayList<AudioModel>();
		CursorWrapper cursor = query(
				"SELECT * FROM [audio_table] WHERE [albumId]=? and  [downloadStatus] != ? and  [downloadStatus] != ? ",
				new String[] { albumId, String.valueOf(DownloadStatus.DOWNLOAD_COMPLETE.getValue()), String.valueOf(DownloadStatus.NOTIN.getValue())  });
		while (cursor.moveToNext()) {
			AudioModel audio = cursorToVideoModel(cursor);
			audios.add(audio);
		}

		cursor.close();

		return audios;

	}

	public ArrayList<AudioModel> getDownloadingListByAudioId(String videoId) {
		ArrayList<AudioModel> audios = new ArrayList<AudioModel>();
		CursorWrapper cursor = query(
				"SELECT * FROM [audio_table] WHERE [videoId]=? ",
				new String[] { videoId  });
		while (cursor.moveToNext()) {
			AudioModel audio = cursorToVideoModel(cursor);
			audios.add(audio);
		}

		cursor.close();

		return audios;
	}



	/**
	 * 获取已经下载的列表
	 */
	public ArrayList<AudioModel> getDownloadedList() {
		return  getByDownloadStatus(DownloadStatus.DOWNLOAD_COMPLETE);
	}

	public ArrayList<AudioModel> getDownloadedListByAlbumId(String albumId) {
//		return getListByAlbumIdDownloadStatus(albumId, DownloadStatus.DOWNLOAD_COMPLETE);
		if (null == albumId) {
			return new ArrayList<AudioModel>();
		}
		return getListByAlbumIdDownloadStatus(albumId, "4");
	}

	public ArrayList<AudioModel> getListByAlbumIdDownloadStatus(String albumId, String downloadStatus) {
		ArrayList<AudioModel> audios = new ArrayList<AudioModel>();
		CursorWrapper cursor = query(
				"SELECT DISTINCT * FROM [audio_table] WHERE [albumId]=? and  [downloadStatus] = ? ",
				new String[] {albumId,downloadStatus});
		while (cursor.moveToNext()) {
			AudioModel audio = cursorToVideoModel(cursor);
			audios.add(audio);
		}

		cursor.close();

		return audios;
	}
	/**
	 * 插入数据库
	 */
	public void save(AudioModel audio) {
		save(audio,getDb());
	}

	public void save(AudioModel audio,SQLiteDatabase db){
		db.beginTransaction();
		try {
			int istry = audio.isTry() ? 1 : 0;
			Cursor c = db.rawQuery(
					"SELECT DISTINCT [videoID] FROM [audio_table] WHERE [videoID] = ? AND [istry] = ?;",
					new String[] { audio.getVideoId() ,String.valueOf(istry)});
			if (c.moveToNext()) {
				db.update(AUDIO_TABLE, contatoToContentValues(audio),
						"videoID = ?", new String[] { audio.getVideoId() });
			} else {
				db.insert(AUDIO_TABLE, null, contatoToContentValues(audio));
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
	 * @param audio
	 */

	public void delete(AudioModel audio) {
		getDb().delete("audio_table", "albumID= ? and videoID=? ",
				new String[] { audio.getAlbumId(), audio.getVideoId() });
	}

	/**
	 * 删除指定视频 重载
	 *
	 * @param 
	 */

	// TODO duplicate?
	public void delete(String albumid, String videoId) {
		getDb().delete("audio_table", "albumID= ? and videoID=? ",
				new String[] { albumid, videoId });
	}

	public void deleteAllVide() {
		getDb().delete("audio_table", null, null);
	}

	public void setDownloadStatusByVideoId(String VideoId, int downloadStatus) {
		ContentValues values = new ContentValues();
		values.put("downloadStatus", downloadStatus);
		getDb().update(AUDIO_TABLE, values, "videoID = ?",
				new String[] { VideoId });
	}


	public void setStatusByVideoId(String videoId, DownloadStatus downloadStatus) {
		ContentValues values = new ContentValues();
		values.put("downloadStatus",
				downloadStatus.getStringValue());
		getDb().update("audio_table", values, " videoID=" + videoId, null);
	}


	/**
	 * 视频暂停下载
	 */
	public void setPause(String videoId) {
		ContentValues values = new ContentValues();
		values.put("pause",
				DownloadStatus.DOWNLOAD_PAUSE.getStringValue());
		getDb().update("audio_table", values, " videoID=" + videoId, null);
	}
	

	/**
	 * 设置已经观看
	 */
	public void setWatchedByVideoId(String videoId) {
		ContentValues values = new ContentValues();
		values.put("watched", 1);
		getDb().update("audio_table", values, " videoID=" + videoId, null);
	}
	
	
	
	public void updateExpiretimeUidByAlbumId(String albumId, String exipretime, String uid, String albumTitle) {
		ContentValues values = new ContentValues();
		values.put("expiretime", exipretime);
		values.put("uid", uid);
		values.put("albumTitle", albumTitle);
 		getDb().update("audio_table", values, " albumID=" + albumId, null);
	}
	
	private AudioModel cursorToVideoModel(CursorWrapper cursor) {
		AudioModel audio = new AudioModel();
		audio.setVideoId(cursor.getString("videoID"));
		audio.setAlbumId(cursor.getString("albumID"));
		audio.setAlbumTitle(cursor.getString("albumTitle"));
//		audio.setAlbumImageurl(cursor.getString("albumImageurl"));
		
		String decodedUrl = cursor.getString("videoUrl");
		if(decodedUrl != null){
			String url = new String(Base64.decode(decodedUrl, Base64.URL_SAFE));
			audio.setVideoUrl(url);
		}
		audio.setFileSize(cursor.getInt("fileSize"));
		audio.setFilePath(cursor.getString("filePath"));
		audio.setFileName(cursor.getString("fileName"));
		audio.setExt(cursor.getString("extention"));
//		audio.setImageURL(cursor.getString("imageURL"));
		audio.setTitle(cursor.getString("title"));
		audio.setExpiretime(cursor.getString("expireTime"));
		audio.setUid(cursor.getString("uid"));
		audio.setDownloadStatus(DownloadStatus.ValueOf(cursor.getInt("downloadStatus")));
		audio.setClasses(cursor.getString("classes"));
//		audio.setLastwatchposition(cursor.getInt("lastPosition"));
//		audio.setFileheader(cursor.getBlob("fileHeader"));
		
		if(cursor.getInt("watched")>0) {
			audio.setListened(true);
		} else {
			audio.setListened(false);
		}
		
		if(cursor.getInt("istry") == 1) {
			audio.setTry(true);
		} else {
			audio.setTry(false);
		}
		return audio;
	}

	private ContentValues contatoToContentValues(AudioModel audio) {
		ContentValues values = new ContentValues();
		if (audio.getVideoId() != null) {
			values.put("videoID", audio.getVideoId());
		}
		if (audio.getAlbumId() != null) {
			values.put("albumID", audio.getAlbumId());
		}
		
		if (audio.getAlbumTitle() != null) {
			values.put("albumTitle", audio.getAlbumTitle());
		}
		
//		if (audio.getAlbumImageurl() != null) {
//			values.put("albumImageurl", audio.getAlbumImageurl());
//		}
		
		if (audio.getVideoUrl() != null) {
			String url = Base64.encodeToString(audio.getVideoUrl().getBytes(), Base64.URL_SAFE);
			values.put("videoUrl", url);
		}
		if (audio.getFilePath() != null) {
			values.put("filePath", audio.getFilePath());
		}

		if (audio.getFileName() != null) {
			values.put("fileName", audio.getFileName());
		}
		

		if (audio.getFileSize() >0) {
			values.put("fileSize", audio.getFileSize());
		}

		if (audio.getExt() != null) {
			values.put("extention", audio.getExt());
		}
//		if (audio.getImageURL() != null) {
//			values.put("imageURL", audio.getImageURL());
//		}

		if (audio.getTitle() != null) {
			values.put("title", audio.getTitle());
		}

		if (audio.getExpiretime() != null) {
			values.put("expireTime", audio.getExpiretime());
		}

		if (audio.getUid() != null) {
			values.put("uid", audio.getUid());
		}

		if (audio.getDownloadStatus() != null) {
			values.put("downloadStatus", audio.getDownloadStatus().getValue());
		}

		if (audio.getFileName() != null) {
			values.put("classes", audio.getClasses());
		}

//		if (audio.getLastwatchposition() != 0) {
//			values.put("lastPosition", audio.getLastwatchposition());
//		}

		if (audio.isTry()) {
			values.put("istry", 1);
		} else {
			values.put("istry", 0);
		}
		
//		if (audio.getFileheader() != null) {
//			values.put("fileHeader", audio.getFileheader());
//		}
		

		if (audio.isListened()) {
			values.put("watched", 1);
		} else {
			values.put("watched", 0);
		}
		
		return values;
	}

	public ArrayList<AudioModel> getDownloadingAudios() {
		ArrayList<AudioModel> audios = new ArrayList<AudioModel>();
		String[] args = { String.valueOf(DownloadStatus.NOTIN.getValue()),
				String.valueOf(DownloadStatus.DOWNLOAD_COMPLETE.getValue()), };
		String sql = "SELECT * FROM [audio_table] WHERE [downloadStatus] IN (?,?,?,?)";
		sql = "SELECT * FROM [audio_table] WHERE ([downloadStatus] != ? AND [downloadStatus] != ?)";
		CursorWrapper c = query(sql, args);
		while (c.moveToNext()) {
			AudioModel audio = cursorToVideoModel(c);
			audios.add(audio);
		}
		c.close();
		return audios;
	}
}
