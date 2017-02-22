package cn.youmi.framework.db;

import java.util.HashMap;

import android.database.Cursor;

public class CursorWrapper {
	private Cursor mCursor;
	private HashMap<String,Integer> nameToIndexMap = new HashMap<String, Integer>();
	public CursorWrapper(Cursor c){
		mCursor = c;
	}
	
	private int getIndex(String columnName){
		Integer index = nameToIndexMap.get(columnName);
		if(index == null){
			index = mCursor.getColumnIndex(columnName);
			nameToIndexMap.put(columnName, index);
		}
		return index;
	}
	
	public int getInt(String columnName){
		return mCursor.getInt(getIndex(columnName));
	}
	
	public void close(){
		mCursor.close();
		mCursor = null;
		nameToIndexMap.clear();
	}
	
	public boolean moveToNext(){
		return mCursor.moveToNext();
	}

	public String getString(String columnName) {
		return mCursor.getString(getIndex(columnName));
	}
	
	public long getLong(String columnName){
		return mCursor.getLong(getIndex(columnName));
	}

	public byte[] getBlob(String columnName) {
		return mCursor.getBlob(getIndex(columnName));
	}
	
	public boolean getBoolean(String columnName){
		int i = mCursor.getInt(getIndex(columnName));
		return mCursor.getInt(getIndex(columnName)) != 0;
	}
}
