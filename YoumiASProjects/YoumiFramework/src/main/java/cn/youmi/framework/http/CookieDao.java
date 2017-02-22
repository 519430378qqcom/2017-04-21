package cn.youmi.framework.http;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import org.apache.http.cookie.Cookie;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.framework.db.BaseDao;
import cn.youmi.framework.main.BaseApplication;
import cn.youmi.framework.util.SingletonFactory;

public class CookieDao extends BaseDao<CookieModel> {

	private static final int VERSION = 1;

	private CookieDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public static CookieDao getInstance(Context context) {
		return SingletonFactory.getInstance(CookieDao.class);
	}

	public CookieDao(){
		this(BaseApplication.getApplication(),"cookies",null,VERSION);
	}
	
	public void close() {
	}
	
	/** 获取cookie中的uid */
	public String getUid() {
		String uid = null;
		CookieModel cookie = getByName("U");

		if (cookie == null) {
			return uid;
		}
		String u = cookie.getValue();
		String s[] = u.split("%26");
		for (int i = 0; i < s.length; i++) {
			if (s[i].contains("ID%3D")) {
				uid = s[i].replace("ID%3D", "");
			}
		}
		return uid;
	}

	/**
	 * 根据名称获取cookie
	 */
	public CookieModel getByName(String name) {
		CookieModel cookie = null;
		Cursor cursor = getReadableDatabase().query("cookie_table",
				new String[] { "id", "name", "value", "expires", "domain"}, "name =?",
				new String[] { name }, null, null, null,null);
		if (cursor == null) {
			return null;
		}
		while (cursor.moveToNext()) {
			cookie = cursorToCookieModel(cursor);
			break;
		}
		cursor.close();
		return cookie;

	}

	public void insert(List<CookieModel> cookiemodels) {
//		getDb().beginTransaction(); // 开始事务
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		try {
			for (CookieModel cookiemodel : cookiemodels) {
				Cursor c = db.rawQuery("SELECT * FROM [cookie_table] WHERE name = ?", new String[]{cookiemodel.getName() });
				if(c.moveToNext()){
					ContentValues cv = new ContentValues();
					fillContentValue(cv, cookiemodel);
					db.update("cookie_table", cv, " [name] = ? ", new String[]{cookiemodel.getName() });
				}else{
					db.execSQL(
							"REPLACE INTO cookie_table VALUES(null, ?, ?, ?,?)",
							new Object[] { cookiemodel.getName(),
									cookiemodel.getValue(),
									cookiemodel.getExpires(),
									cookiemodel.getDomain()});
				}
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}
	
	@Override
	protected void fillContentValue(ContentValues cv, CookieModel e) {
		super.fillContentValue(cv, e);
		cv.put("name", e.getName());
		cv.put("value", e.getValue());
		cv.put("expires", e.getExpires());
	}
	
	/**
	 * 更新cookie
	 */
	public String update(CookieModel cookie) {
		ContentValues values = contatoToContentValues(new ContentValues(),
				cookie);
		getDb().update("cookie_table", values, "name = " + cookie.getName(),
				null);
		return cookie.getName();
	}

	// 删除cookie
	public void delete(CookieModel cookie) {
		String name = cookie.getName();
		delete(name);
	}

	public void delete(String name) {
		getDb().delete("cookie_table", "name= ?", new String[] { name });
	}

	/** 清空 */
	public void clear() {
		getDb().delete("cookie_table", "1=?", new String[] { "1" });
	}
	
	public void deleteUser() {
		delete("username");
		delete("E");
		delete("U");
	}

	/**
	 * 列出没有过期的cookie
	 * 
	 * @return
	 */
	public ArrayList<CookieModel> listAvailable() {
		ArrayList<CookieModel> cookies = new ArrayList<CookieModel>();

//		String clientTime = "" + (new Date()).getTime();
		String clientTime = "" + 0;
		Cursor cursor = getDb().query("cookie_table",
				new String[] { "id", "name", "value", "expires","domain" },
				null, null, null, null, null,null);
//		Cursor cursor = getDb().query("cookie_table",
//				new String[] { "id", "name", "value", "expires","domain" },
//				"expires>=?", new String[] { clientTime }, null, null, null,null);
		if (cursor == null) {
			return cookies;
		}
		while (cursor.moveToNext()) {
			CookieModel cookie = cursorToCookieModel(cursor);
			cookies.add(cookie);
		}

		cursor.close();

		return cookies;
	}
	
	
	/** 未过期cookie 字符串 */
	public String getAsString() {
		String cookiestr = "";
		ArrayList<CookieModel> cookiemodels = listAvailable();
		if (cookiemodels == null) {

			return null;
		}
		for (CookieModel cookiemodel : cookiemodels) {
			String name = cookiemodel.getName();
			String value = cookiemodel.getValue();
			cookiestr += name + "=" + value + ";";
		}
		return cookiestr;
	}

	public ArrayList<CookieModel> listar() {
		ArrayList<CookieModel> cookies = new ArrayList<CookieModel>();

		Cursor cursor = getDb().query("cookie_table",
				new String[] { "id", "name", "value", "expires","domain" }, null, null,
				null, null, null,null);

		if (cursor == null) {
			return cookies;
		}

		while (cursor.moveToNext()) {
			CookieModel cookie = cursorToCookieModel(cursor);
			cookies.add(cookie);
		}

		cursor.close();

		return cookies;
	}

	private CookieModel cursorToCookieModel(Cursor cursor) {
		CookieModel cookie = new CookieModel();
		if (cursor == null) {
			return cookie;
		}
		cookie.setId(cursor.getLong(cursor.getColumnIndex("id")));
		cookie.setName(cursor.getString(cursor.getColumnIndex("name")));
		cookie.setValue(cursor.getString(cursor.getColumnIndex("value")));
		cookie.setExpires(cursor.getString(cursor.getColumnIndex("expires")));
		cookie.setDomain(cursor.getString(cursor.getColumnIndex("domain")));
		return cookie;
	}

	private ContentValues contatoToContentValues(ContentValues values,
			CookieModel cookie) {
		values.put("value", cookie.getValue());
		values.put("expires", cookie.getExpires());
		values.put("domain", cookie.getDomain());
		return values;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS [cookie_table] " +
				"([id] INTEGER primary key autoincrement, " + "[name] TEXT," + // cookie名
				"[value] TEXT," + // cookie值
				"[expires] TEXT, " +
				"[domain] TEXT);"// 过期时间
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void saveCookies(ArrayList<Cookie> cookies) {
		// TODO save
		ArrayList<CookieModel> cookiemodels = new ArrayList<CookieModel>();
		String expiryTime = String.valueOf(20 * 60 * 1000L);//默认20分钟过期
		for (Cookie cookie : cookies) {
			if (null != cookie.getExpiryDate() && 0 != cookie.getExpiryDate().getTime()) {
				expiryTime = String.valueOf(cookie.getExpiryDate().getTime());
			}
			CookieModel cookiemodel = new CookieModel(cookie.getName(), cookie.getValue(), expiryTime);
			cookiemodels.add(cookiemodel);
			cookiemodel.setDomain(cookie.getDomain());
		}
		//TODO
		insert(cookiemodels);

	}
}
