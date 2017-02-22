package com.umiwi.ui.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Pair;

import com.umiwi.ui.beans.SearchBean;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

import cn.youmi.framework.db.BaseDao;

public class SearchDao extends BaseDao<SearchBean> {

	public SearchDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public SearchDao() {
		this(UmiwiApplication.getApplication(), "search.db", null, 1);
	}
	
	@Override
	protected void fillContentValue(ContentValues cv, SearchBean e) {
		cv.put("title",e.getTitle());
		cv.put("type", e.getDetailurl());
		cv.put("time", System.currentTimeMillis());
		super.fillContentValue(cv, e);
	}
	
	public ArrayList<SearchBean> getAll(){
		ArrayList<SearchBean> beans = new ArrayList<SearchBean>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT [title] ,[type] FROM [search_table] ORDER BY [time] desc LIMIT 20;" , null);
		while(cursor.moveToNext()){
			beans.add(cursorToModel(cursor));
		}
		db.close();
		return beans;
	}
	
	@Override
	protected SearchBean cursorToModel(Cursor c) {
		SearchBean sb = new SearchBean();
		sb.setTitle(c.getString(c.getColumnIndex("title")));
		sb.setDetailurl(c.getString(c.getColumnIndex("type")));
		return sb;
	}
	
	protected Pair<String, String> getPrimaryKeyAndValue(SearchBean e) {
		return new Pair<String,String>("title","'" + e.getTitle() + "'");
	}
	
	@Override
	protected String getDbName() {
		return "search.db";
	}
	
	@Override
	protected String getTableName(SearchBean e) {
		return "search_table";
	}
	
	@Override
	protected String getSqlFileName() {
		return "search_table.sql";
	}
	
	@Override
	protected boolean contains(SearchBean e) {
		boolean contains = false;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT [title] FROM [search_table] WHERE [title]=?;", new String[]{e.getTitle()});
		if(cursor.moveToFirst()){
			contains = true;
		}
		cursor.close();
		db.close();
		return contains;
	}

	public void clearHistory() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("search_table", null, null);
		db.close();
	}
}
