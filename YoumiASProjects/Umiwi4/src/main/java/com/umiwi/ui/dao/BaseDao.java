package com.umiwi.ui.dao;

 
import android.database.sqlite.SQLiteDatabase;

public class BaseDao {
	protected DBHelper helper;
	
	public BaseDao() {
		helper = DBHelper.getInstance();
	}
	
	protected SQLiteDatabase getDb() {
		return DBHelper.getInstance().getWritableDatabase();
	}
	
	protected void close() {
		helper.close();
	}
	
	protected CursorWrapper query(String sql,String[] selectionArgs){
		CursorWrapper cw = new CursorWrapper(getDb().rawQuery(sql, selectionArgs));
		return cw;
	}
}
