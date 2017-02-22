package cn.youmi.account.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Pair;

import cn.youmi.account.model.UserModel;
import cn.youmi.framework.db.BaseDao;
import cn.youmi.framework.main.BaseApplication;
import cn.youmi.framework.util.SingletonFactory;

public class UserDao extends BaseDao<UserModel> {

	private static final String dbName = "youmiuser.db";
	private static final int VERSION = 2;
	
	public UserDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, dbName, factory, version);
	}

	public UserDao(){
		this(BaseApplication.getApplication(),dbName,null,VERSION);
	}
	
	public static UserDao getInstance() {
		return SingletonFactory.getInstance(UserDao.class);
	}
	
	@Override
	protected Pair<String, String> getPrimaryKeyAndValue(UserModel e) {
		return new Pair<String,String>("uid",e.getUid());
	}
	

	public UserModel getByUid(String uid) {
		UserModel user = null;
		
		Cursor cursor = getDb().query(
				"user_table",
				new String[] { "uid", "username", "mobile", "avatar",
						"balance", "identity", "grade", "identityexpire", "tutoruid", "account", "price" ,
						"usertest", "mycoin", "cookie", "loginStatus"}, "uid =?",
				new String[] { uid }, null ,null, null);

		if (cursor == null) {
			return null;
		}
		while (cursor.moveToNext()) {
			user = cursorToUserModel(cursor);
			break;
		}
		cursor.close();
		return user;
	}

//	public void save(UserModel user) {
//		getDb().beginTransaction(); // 开始事务
//		try {
//			getDb().execSQL(
//					"REPLACE INTO user_table VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
//					new Object[] { user.getUid(), user.getUsername(),
//							user.getMobile(), user.getAvatar(),
//							user.getBalance(), user.getIdentity(),
//							user.getGrade(), user.getIdentity_expire(),
//							user.getTutoruid(), user.getAccount(),
//							user.getPrice(), user.getUsertest() });
//			getDb().setTransactionSuccessful(); // 设置事务成功完成
//		} finally {
//			getDb().endTransaction(); // 结束事务
//		}
//	}

//	public void delete(UserModel user) {
//		delete(user.getUid());
//	}

	public void delete(String uid) {
		getDb().delete("user_table", "uid= ? ", new String[] { uid });
	}

	/**
	 * 更新数据库
	 * 
	 * @param cursor
	 * @return
	 */

	public String update(UserModel user) {
		ContentValues values = contatoToContentValues(user);
		getDb().update("user_table", values, " uid = " + user.getUid(), null);
		return user.getUid();
	}

	@SuppressLint("UseValueOf")
	private UserModel cursorToUserModel(Cursor cursor) {
		UserModel user = new UserModel();
		if (cursor == null) {
			return user;
		}
		user.setUid(cursor.getString(cursor.getColumnIndex("uid")));
		user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
		user.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
		user.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
		user.setBalance(cursor.getString(cursor.getColumnIndex("balance")));
		user.setIdentity(cursor.getString(cursor.getColumnIndex("identity")));
		user.setGrade(cursor.getString(cursor.getColumnIndex("grade")));
		user.setIdentity_expire(cursor.getString(cursor
				.getColumnIndex("identityexpire")));
		user.setTutoruid(cursor.getString(cursor.getColumnIndex("tutoruid")));
		user.setAccount(cursor.getInt(cursor.getColumnIndex("account")));
		user.setPrice(cursor.getInt(cursor.getColumnIndex("price")));
		user.setUsertest(cursor.getString(cursor.getColumnIndex("usertest")));
		user.setMycoin(cursor.getString(cursor.getColumnIndex("mycoin")));
		user.setCookie(cursor.getString(cursor.getColumnIndex("cookie")));
		user.setLoginStatus(UserModel.LoginStatus.ValueOf(cursor.getInt((cursor
				.getColumnIndex("loginStatus")))));
		return user;
	}
	
	@SuppressLint("UseValueOf")
	@Override
	protected UserModel cursorToModel(Cursor cursor) {
		UserModel user = new UserModel();
		user.setUid(cursor.getString(cursor.getColumnIndex("uid")));
		user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
		user.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
		user.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
		user.setBalance(cursor.getString(cursor.getColumnIndex("balance")));
		user.setIdentity(cursor.getString(cursor.getColumnIndex("identity")));
		user.setGrade(cursor.getString(cursor.getColumnIndex("grade")));
		user.setIdentity_expire(cursor.getString(cursor
				.getColumnIndex("identityexpire")));
		user.setTutoruid(cursor.getString(cursor.getColumnIndex("tutoruid")));
		user.setAccount(cursor.getInt(cursor.getColumnIndex("account")));
		user.setPrice(cursor.getInt(cursor.getColumnIndex("price")));
		user.setUsertest(cursor.getString(cursor.getColumnIndex("usertest")));
		user.setMycoin(cursor.getString(cursor.getColumnIndex("mycoin")));
		user.setCookie(cursor.getString(cursor.getColumnIndex("cookie")));
		user.setLoginStatus(UserModel.LoginStatus.ValueOf(cursor.getInt((cursor
				.getColumnIndex("loginStatus")))));
		return user;
	}
	
	
	@Override
	protected void fillContentValue(ContentValues values, UserModel user) {
		values.put("uid", user.getUid());
		values.put("username", user.getUsername());
		values.put("mobile", user.getMobile());
		values.put("avatar", user.getAvatar());
		values.put("balance", user.getBalance());
		values.put("identity", user.getIdentity());
		values.put("grade", user.getGrade());
		values.put("identityexpire", user.getIdentity_expire());
		values.put("tutoruid", user.getTutoruid());
		values.put("account", user.getAccount());
		values.put("price", user.getPrice());
		values.put("usertest", user.getUsertest());
		values.put("mycoin", user.getMycoin());
		values.put("cookie", user.getCookie());
		if (user.getLoginStatus() != null) {
			values.put("loginStatus", user.getLoginStatus().getValue());
		}
	}

	private ContentValues contatoToContentValues(UserModel user) {
		ContentValues values = new ContentValues();
		values.put("uid", user.getUid());
		values.put("username", user.getUsername());
		values.put("mobile", user.getMobile());
		values.put("avatar", user.getAvatar());
		values.put("balance", user.getBalance());
		values.put("identity", user.getIdentity());
		values.put("grade", user.getGrade());
		values.put("identityexpire", user.getIdentity_expire());
		values.put("tutoruid", user.getTutoruid());
		values.put("account", user.getAccount());
		values.put("price", user.getPrice());
		values.put("usertest", user.getUsertest());
		values.put("mycoin", user.getMycoin());
		values.put("cookie", user.getCookie());
		if (user.getLoginStatus() != null) {
			values.put("loginStatus", user.getLoginStatus().getValue());
		}
		return values;
	}
	
	
	@Override
	public String getSqlFileName() {
		return "user_table.sql";
	}
	
	@Override
	protected String getDbName() {
		return "youmiuser.db";
	}
	
	@Override
	protected String getTableName(UserModel e) {
		return "user_table";
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		super.onUpgrade(db, oldVersion, newVersion);
		if (oldVersion == 1 && newVersion == 2) {//1-->2 add audionum
			String sql = upgradeSql("mycoin");
			db.execSQL(sql);
		}
	}

}
