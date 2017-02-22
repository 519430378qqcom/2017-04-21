package com.umiwi.ui.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.youmi.framework.util.SingletonFactory;

import com.umiwi.ui.model.SettingModel;

public class SettingDao extends BaseDao {

	public void close() {
	}
	
	public SettingDao() {
		super();
	}

	
	private static final String SETTING_TABLE = "setting_table";

	public static SettingDao getInstance() {
		return SingletonFactory.getInstance(SettingDao.class);
	}
	
	public void createTable(SQLiteDatabase db) {
 		String createSettingTableSql = 
						"CREATE TABLE IF NOT EXISTS [setting_table] (" +
						"[key] TEXT , " +
						"[value] TEXT );" +
      					"CREATE UNIQUE INDEX [key] ON [setting_table] ([key]);";
		db.execSQL(createSettingTableSql);
	}
	
	public SettingModel get(String key) {
		SettingModel setting = null;
		setting = new SettingModel();
 		CursorWrapper cursor = query(
				"SELECT * FROM ["+SETTING_TABLE+"] WHERE [key]=? ",
				new String[] { key });
		if (cursor.moveToNext()) {
			
			setting = cursorToSettingModel(cursor);
		}
		cursor.close();
  	    return setting;
	}
	
	public void save(String key, String value) {
		SQLiteDatabase db = getDb();
		db.beginTransaction();
		try {
			Cursor c = db.rawQuery(
					"SELECT [value] FROM ["+SETTING_TABLE+"] WHERE [key] = ?;",
					new String[] { key });
			if (c.moveToNext()) {
				ContentValues values = new ContentValues();
				values.put("value", value);
 				db.update(SETTING_TABLE, values,
						"key = ?", new String[] { key });
			} else {
				ContentValues values = new ContentValues();
				values.put("key", key);
				values.put("value", value);
 				db.insert(SETTING_TABLE, null, values);
			}
			c.close();
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}
	
	public void delete(String key) {
		getDb().delete(SETTING_TABLE, "key= ?  ", new String[] {key });
	}
	
	

	private SettingModel cursorToSettingModel(CursorWrapper cursor) {
		SettingModel setting = new SettingModel();
		setting.setKey(cursor.getString("key"));
		setting.setValue(cursor.getString("value"));
		return setting;
	}
}
