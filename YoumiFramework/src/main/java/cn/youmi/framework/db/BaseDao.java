package cn.youmi.framework.db;

 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import cn.youmi.framework.main.BaseApplication;
import cn.youmi.framework.main.ContextProvider;

public class BaseDao <E> extends SQLiteOpenHelper{
	
	public BaseDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	protected SQLiteDatabase getDb() {
		return getReadableDatabase();
	}
	
	protected CursorWrapper query(SQLiteDatabase db,String sql,String[] selectionArgs){
		CursorWrapper cw = new CursorWrapper(db.rawQuery(sql, selectionArgs));
		return cw;
	}
	
	protected boolean contains(E e){
		Pair<String,String> rs = getPrimaryKeyAndValue(e);
		if(rs == null){
			return false;
		}
		boolean contains = false;
		SQLiteDatabase db = getWritableDatabase();
//		db.delete(getTableName(e), rs.first + " = ?", new String[] { rs.second });
		Cursor c = db.rawQuery("SELECT * FROM [" + getTableName(e) + "] WHERE [" + rs.first + "] = " + rs.second + ";" , null);
		if(c.moveToFirst()){
			contains = true;
		}
		db.close();
		return contains;
	}
	
	public synchronized String save(E e){
		return save(e,true);
	}
	
	public synchronized String save(E e,boolean force){
		if(contains(e) ){
			if(force){
				return update(e);
			}
		}else{
			return insert(e);
		}
		return null;
	}
	
	public synchronized void save(ArrayList<E> es){
		for(E e:es){
			save(e);
		}
	}
	
	public void save(ArrayList<E> es,boolean force){
		for(E e:es){
			save(e,force);
		}
	}
	
	public synchronized E getById(String id){
		return null;
	}
	
	public synchronized void delete(E e){
		Pair<String,String> rs = getPrimaryKeyAndValue(e);
		SQLiteDatabase db = getWritableDatabase();
		db.delete(getTableName(e), rs.first + " = ?", new String[] { rs.second });
		db.close();
	}
	
	protected synchronized String update(E e){
		ContentValues values = new ContentValues();
		fillContentValue(values,e);
		Pair<String,String> pk = getPrimaryKeyAndValue(e);
		SQLiteDatabase db = getWritableDatabase();
		db.update(getTableName(e), values, pk.first + " = " + pk.second,null);
		db.close();
		return pk.second;
	}
	
	protected synchronized String insert(E e){
		ContentValues values = new ContentValues();
		fillContentValue(values,e);
		SQLiteDatabase db = getWritableDatabase();
		long id = db.insert(getTableName(e), null, values);
		db.close();
		return String.valueOf(id);//TODO
	}
	
	protected String getTableName(E e){
//		return "xxx";//TODO
		DbTable dbTable = e.getClass().getAnnotation(DbTable.class);
		return dbTable.tableName();
	}
	
	protected void fillContentValue(ContentValues cv,E e){
		
	}
	
	protected E cursorToModel(Cursor c){
		return null;
	}
	
	protected Pair<String,String> getPrimaryKeyAndValue(E e){
		Field[] fields = e.getClass().getDeclaredFields();
		for(Field f:fields){
			DbTable dbTable = f.getAnnotation(DbTable.class);
			
			
			if(dbTable != null && dbTable.primaryKey()){
				String name = f.getName();
				try {
					boolean isAccessable = f.isAccessible();
					f.setAccessible(true);
					String value = (String) f.get(e);
					f.setAccessible(isAccessable);//TODO
//					String value = e.getClass().getMethod("get" + , parameterTypes)
					return new Pair<String,String>(name,value);
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
				return null;
			}
		}
		return null;
	}
	
	public String extractSql(String file,String name){
		InputStream is = null;
		try {
			is = BaseApplication.getContext().getAssets().open(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			StringBuilder sb = new StringBuilder();
			
			boolean started = false;
			
			while((line = br.readLine()) != null){
				if(line.startsWith(name)){
					started = true;
					continue;
				}else{
					if(started && line.startsWith("---")){
						break;
					}
					if(started){
						sb.append(line);
					}
				}
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception ie){
			ie.printStackTrace();
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = extractSql(getSqlFileName(), "create");
		db.execSQL(sql);
	}
	
	public String upgradeSql(String name) {
		return "alter table [" + getTableName() + "] add [" + name + "] TEXT（300）";
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	protected String getTableName() {
		return null;
	}
	
	protected String getSqlFileName(){
		return null;
	}
	
	protected String getDbName(){
		return null;
	}
}
