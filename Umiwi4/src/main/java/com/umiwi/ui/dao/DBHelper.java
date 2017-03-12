package com.umiwi.ui.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.model.VideoModel;
import com.umiwi.ui.model.VideoModel.DownloadStatus;
import com.umiwi.ui.util.CommonHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cn.youmi.framework.util.SingletonFactory;

/**
 * umiwi数据库
 * 
 * @author tangxiyong 2013-11-20下午4:50:21
 * 
 */
public class DBHelper extends SQLiteOpenHelper {

	public static DBHelper getInstance() {
		return SingletonFactory.getInstance(DBHelper.class);
	}

	public DBHelper() {
		this(UmiwiApplication.getInstance());
	}

	// 数据库名
	private static final String DB_NAME = "umiwi.db";

	// 数据库版本 4.0版定为20 4.1.0为22 4.2为24 4.3=43, 4.3.6=44, 4.5.1=50, 4.8.0=80
	//4.6.1=61 加入游戏        4.6.5=70 加入搜索历史 4.8.2=82
	private static final int VERSION = 82;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		context.getPackageManager();
	}

	public void onCreate(SQLiteDatabase db) {
		// 4.0 创建本地播放记录的表
		db.execSQL("CREATE TABLE IF NOT EXISTS newrecords "
				+ "(mid integer primary key autoincrement, "
				+ "albumId varchar(100)," + // 视频专辑id
				"videoVid varchar(100)," + // 视频vid
				"image varchar(100)," + // 视频头图像地址
				"videoUrl varchar(100)," + // 视频下载地址
				"path varchar(100)," + // 视频保存路径
				"title varchar(100)," + // 视频标题
				"ext varchar(100)," + // 扩展名 用于判断mp4&mp3
				"secretString varchar(200)," + // 加密
				"classes varchar(200)," + // 视频类型
				"historypoint varchar(100))"// 保存在本地的播放时间点
		);

		// 销毁热词
		db.execSQL("DROP TABLE IF EXISTS searchhotwords");


		// 创建视频表
		// 4.4
		createVideoTable(db);
		createAudioTable(db);
		SettingDao.getInstance().createTable(db);

		// 创建笑话
		db.execSQL("CREATE TABLE IF NOT EXISTS joke_table("
				+ "[_id] integer primary key autoincrement, "
				+ "[id] integer, " 
				+ "[content] TEXT, "
				+ "[review] TEXT, "
				+ "[isread] TEXT)");
		
		// 创建标签云
		db.execSQL("CREATE TABLE IF NOT EXISTS searchcloud_table("
				+ "[_id] integer primary key autoincrement, "
				+ "[page] integer, " 
				+ "[title] varchar(1000), "
				+ "[detailurl] varchar(1000), "
				+ "[type] varchar(20), "
				+ "[color] varchar(30), "
				+ "[lecturer_name] varchar(100), "
				+ "[lecturer_title] varchar(1000), "
				+ "[lecturer_image] varchar(1000), "
				+ "[lecturer_courseurl] varchar(1000), "
				+ "[modifytime] date)");
		
		// 创建本地收藏
		db.execSQL("CREATE TABLE IF NOT EXISTS collection_table("
				+ "[albumid] varchar(1000), "
				+ "[favid] varchar(1000), "
				+ "[userid] varchar(100), "
				+ "[is_save_upload] varchar(10), "
				+ "[is_delete_upload] varchar(10))");
		
		createGameRecordTable(db);
		
		createSearchHistoryTable(db);
		
	}
	
	// 搜索历史记录
	private void createSearchHistoryTable(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS [search_history]("
				+"[_id] integer primary key autoincrement,"
				+"[history] TEXT);"
				);
	}

	private void createGameRecordTable(SQLiteDatabase db){
		db.execSQL("CREATE TABLE IF NOT EXISTS [game_record]("
				+"[gameId] TEXT,"
				+"[level] INT);"
				);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String strSource = "strSource";
		String loaded = "loaded";
		checkColumnExist(db, loaded, strSource);

		if (oldVersion <= 10) {// 针对3.4.01以前是10;
			if (!checkColumnExist(db, loaded, strSource)) {

				db.execSQL("ALTER TABLE loaded ADD strSource varchar(200)");
				db.execSQL("ALTER TABLE loaded ADD vip_id varchar(100)");
				db.execSQL("ALTER TABLE loaded ADD video_classes varchar(200)");

				db.execSQL("DROP TABLE IF EXISTS loaded");

				onCreate(db);
			}
		}

		db.execSQL("DROP TABLE IF EXISTS favorites");
		db.execSQL("DROP TABLE IF EXISTS records");
		db.execSQL("DROP TABLE IF EXISTS hotwords");
		db.execSQL("DROP TABLE IF EXISTS hotwords1");
		db.execSQL("DROP TABLE IF EXISTS quotation");
		db.execSQL("DROP TABLE IF EXISTS paynotify");
		
		db.execSQL("DROP TABLE IF EXISTS user_table");//4.8.2delete
		db.execSQL("DROP TABLE IF EXISTS cookies");//4.8.2delete

		// 4.0 创建本地播放记录的表
		db.execSQL("CREATE TABLE IF NOT EXISTS newrecords "
				+ "(mid integer primary key autoincrement, "
				+ "albumId varchar(100)," + // 视频专辑id
				"videoVid varchar(100)," + // 视频vid
				"image varchar(100)," + // 视频头图像地址
				"videoUrl varchar(100)," + // 视频下载地址
				"path varchar(100)," + // 视频保存路径
				"title varchar(100)," + // 视频标题
				"ext varchar(100)," + // 扩展名 用于判断mp4&mp3
				"secretString varchar(200)," + // 加密
				"classes varchar(200)," + // 视频类型
				"historypoint varchar(100))"// 保存在本地的播放时间点
		);


		if (oldVersion <= 24) {
			createVideoTable(db);
			createAudioTable(db);
			SettingDao.getInstance().createTable(db);
			transferNewLoadedToVideoTable(db);
		}
		
		if(oldVersion <=44) {
			Cursor c = db.rawQuery("SELECT * FROM [video_table] limit 1", null);
			if(c.getColumnIndex("watched") == -1){
				db.execSQL("alter table [video_table] add column watched int");
			}
			c.close();

			Cursor c1 = db.rawQuery("SELECT * FROM [audio_table] limit 1", null);
			if(c1.getColumnIndex("watched") == -1){
				db.execSQL("alter table [video_table] add column watched int");
			}
			c1.close();
		}

		// 销毁热词
		db.execSQL("DROP TABLE IF EXISTS searchhotwords");

		// 创建笑话
		db.execSQL("CREATE TABLE IF NOT EXISTS joke_table("
				+ "[_id] integer primary key autoincrement, "
				+ "[id] integer, " 
				+ "[content] TEXT, "
				+ "[review] TEXT, "
				+ "[isread] TEXT)");
		
		// 创建标签云
		db.execSQL("CREATE TABLE IF NOT EXISTS searchcloud_table("
				+ "[_id] integer primary key autoincrement, "
				+ "[page] integer, " 
				+ "[title] varchar(1000), "
				+ "[detailurl] varchar(1000), "
				+ "[type] varchar(20), "
				+ "[color] varchar(30), "
				+ "[lecturer_name] varchar(100), "
				+ "[lecturer_title] varchar(1000), "
				+ "[lecturer_image] varchar(1000), "
				+ "[lecturer_courseurl] varchar(1000), "
				+ "[modifytime] varchar(50))");
		
		// 创建本地收藏
		db.execSQL("CREATE TABLE IF NOT EXISTS collection_table("
				+ "[albumid] varchar(1000), "
				+ "[favid] varchar(1000), "
				+ "[userid] varchar(100), "
				+ "[is_save_upload] varchar(10), "
				+ "[is_delete_upload] varchar(10))");
		
		if(oldVersion <= 51){
			createGameRecordTable(db);
		}
		
		createSearchHistoryTable(db);
	}
	private void createAudioTable(SQLiteDatabase db) {
		// 4.3 创建音频表
		String createAudioTableSql = "CREATE TABLE IF NOT EXISTS [audio_table] ("
				+ "[videoID] TEXT NOT NULL ON CONFLICT REPLACE, "
				+ "[albumID] TEXT, "
				+ "[albumTitle] TEXT, "
				+ "[videoUrl] TEXT, "
				+ "[filePath] TEXT, "
				+ "[fileName] TEXT, "
				+ "[fileSize] TEXT, "
				+ "[extention] TEXT, "
				+ "[title] TEXT, "
				+ "[expireTime] TEXT, "
				+ "[uid] TEXT, "
				+ "[downloadStatus] INT, "
				+ "[classes] TEXT, "
				+ "[istry] INT, "
				+ "[watched] INT);"
				+ "CREATE UNIQUE INDEX [index_videoID] ON [audio_table] ([videoID]);";
		db.execSQL(createAudioTableSql);
		// 4.3 创建视频索引
		// db.execSQL("CREATE  UNIQUE INDEX  IF NOT EXISTS albumvideo on videos(albumid, videoid) ");


	}
	private void createVideoTable(SQLiteDatabase db) {
		// 4.3 创建视频表
		String createVideoTableSql = "CREATE TABLE IF NOT EXISTS [video_table] ("
				+ "[videoID] TEXT NOT NULL ON CONFLICT REPLACE, "
				+ "[albumID] TEXT, "
				+ "[albumTitle] TEXT, "
				+ "[albumImageurl] TEXT, "
				+ "[videoUrl] TEXT, "
				+ "[filePath] TEXT, "
				+ "[fileName] TEXT, "
				+ "[fileSize] TEXT, "
				+ "[extention] TEXT, "
				+ "[imageURL] TEXT, "
				+ "[title] TEXT, "
				+ "[expireTime] TEXT, "
				+ "[uid] TEXT, "
				+ "[downloadStatus] INT, "
				+ "[classes] TEXT, "
				+ "[lastPosition] INT, "
				+ "[istry] INT, "
				+ "[fileHeader] BLOB, "
				+ "[watched] INT);"
				+ "CREATE UNIQUE INDEX [index_videoID] ON [video_table] ([videoID]);";
		db.execSQL(createVideoTableSql);
		// 4.3 创建视频索引
		// db.execSQL("CREATE  UNIQUE INDEX  IF NOT EXISTS albumvideo on videos(albumid, videoid) ");
		
		
	}


	private void transferNewLoadedToVideoTable(SQLiteDatabase db) {
		// newloaded
		Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='newloaded';", null);
		boolean newLoadedTableExist = cursor.moveToNext();
		cursor.close();
		if(!newLoadedTableExist){
			return;
		}
		
		Cursor c = db.rawQuery("SELECT * FROM [newloaded]", null);
		CursorWrapper cw = new CursorWrapper(c);

		HashMap<String, DownloadStatus> downloadStatusMap = new HashMap<String, DownloadStatus>();
		downloadStatusMap.put("complete", DownloadStatus.DOWNLOAD_COMPLETE);
		downloadStatusMap.put("pause", DownloadStatus.DOWNLOAD_PAUSE);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);// 设置日期格式
		while (c.moveToNext()) {
			VideoModel vm = new VideoModel();
			vm.setAlbumId(cw.getString("albumId"));
			vm.setVideoId(cw.getString("videoVid"));
			vm.setImageURL(cw.getString("image"));

			// TODO path
			String path = cw.getString("path");
			String url = cw.getString("videoUrl");
			if (url.length() == 32) {
				// md5 encoded;
			} else {
				vm.setVideoUrl(url);
				url = CommonHelper.encodeMD5(url);
			}

			String state = cw.getString("state");
			vm.setDownloadStatus(downloadStatusMap.get(state));
			path = path + url + cw.getString("ext");

			long time = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30;
			String timeStr = df.format(new Date(time));
			vm.setExpiretime(timeStr);

			vm.setFilePath(path);
			vm.setTitle(cw.getString("title"));
			VideoDao.getInstance().save(vm, db);
		}
		cw.close();
		db.execSQL("DROP TABLE [newloaded]");
	}

	/**
	 * 检查某表列是否存在
	 * 
	 * @param db
	 * @param tableName
	 *            表名
	 * @param columnName
	 *            列名
	 * @return
	 */
	private boolean checkColumnExist(SQLiteDatabase db, String tableName,
			String columnName) {
		boolean result = false;
		Cursor cursor = null;
		try {
			// 查询一行
			cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0",
					null);
			result = cursor != null && cursor.getColumnIndex(columnName) != -1;
		} catch (Exception e) {
		} finally {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return result;
	}
}
