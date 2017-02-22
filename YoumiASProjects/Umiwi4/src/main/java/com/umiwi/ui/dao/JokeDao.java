package com.umiwi.ui.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.database.Cursor;
import cn.youmi.framework.util.SingletonFactory;

import com.google.gson.Gson;
import com.umiwi.ui.beans.JokeBean;
import com.umiwi.ui.main.UmiwiApplication;

/**
 * 笑话dao
 * 
 * @author tjie00
 * @version 15/06/14
 */
public class JokeDao extends BaseDao {

	public static final String JOKE_TABLE = "joke_table";
	public static final String JOKE_FILE = "jokes.json";


	public JokeDao() {
		super();
	}

	public static JokeDao getInstance() {
		return SingletonFactory.getInstance(JokeDao.class);
	}

	public JokeBean getJoke() {
		JokeBean jokeBean = null;
		
		Cursor cursor = getDb().rawQuery(
				"SELECT id, content, review FROM " + JOKE_TABLE
						+ " WHERE _id=(SELECT min(_id) FROM " + JOKE_TABLE + ") AND isread=?",
				new String[]{"false"});
		if (cursor != null && cursor.moveToFirst()) {
			jokeBean = new JokeBean();
			
			jokeBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
			jokeBean.setContent(cursor.getString(cursor.getColumnIndex("content")));
			jokeBean.setReview(cursor.getString(cursor.getColumnIndex("review")));
			
		} else if(cursor != null) {
			cursor = getDb().rawQuery(
					"SELECT id, content, review FROM " + JOKE_TABLE
							+ " ORDER BY random() limit 1",
					null);
			
			if (cursor != null && cursor.moveToFirst()) {
				jokeBean = new JokeBean();
				
				jokeBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
				jokeBean.setContent(cursor.getString(cursor.getColumnIndex("content")));
				jokeBean.setReview(cursor.getString(cursor.getColumnIndex("review")));
			} else {
				return jokeBean;
			}
		}
		return jokeBean;
	}

	/**
	 * 更新数据库
	 * 
	 * @param cursor
	 * @return
	 */

	public void update(JokeBean joke) {
		getDb().execSQL(
				"UPDATE " + JOKE_TABLE + " SET isread=? WHERE id=?",
				new String[] { "true", joke.getId()+"" });
	}

	// 数据库是否需要被更新
	public boolean isJokeNeed2Update() {
		boolean needUpdate = false;
		Cursor cursor = getDb().rawQuery(
				"SELECT count(*) AS count FROM " + JOKE_TABLE
						+ " WHERE isread=?", new String[] {"false"});
		if (cursor != null && cursor.moveToFirst()) {
			int count = cursor.getInt(0);
			if (count <= 1) {
				needUpdate = true;
			}
		}

		return needUpdate;
	}

	// 保存一条笑话记录
	public void saveJoke(JokeBean joke) {
		getDb().beginTransaction(); // 开始事务
		try {
			getDb().execSQL(
					"INSERT INTO " + JOKE_TABLE
							+ "(id, content, review, isread) VALUES(?, ?, ?, ?)",
					new Object[] { joke.getId(), joke.getContent(), joke.getReview(), "false" });
			getDb().setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			getDb().endTransaction(); // 结束事务
		}
	}

	// 保存一堆笑话记录
	public void saveJokes(ArrayList<JokeBean> jokes) {
		for (JokeBean joke : jokes) {
			saveJoke(joke);
		}
	}

	/**
	 * 数据库是否被初始化
	 * 
	 * @retrun true:已经被初始化, false:数据库未被初始化
	 */
	public boolean isJokeInit() {
		boolean isInit = false;

		Cursor cursor = getDb().rawQuery("SELECT count(*) FROM " + JOKE_TABLE,
				null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int count = cursor.getInt(0);
				if (count != 0) {
					isInit = true;
				}
			}
		}

		return isInit;
	}

	// 初始化数据库信息
	public void initJoke() {
		String jokeContent = getFromAssets(JOKE_FILE);

		if (jokeContent != null) {
			JokeBean.JokeBeanRequestData jokes = new Gson().fromJson(jokeContent, JokeBean.JokeBeanRequestData.class);
			saveJokes(jokes.getRecord());
			
		}
	}

	private String getFromAssets(String jokeFile) {
		BufferedReader bufReader = null;
		try {
			InputStreamReader inputReader = new InputStreamReader(UmiwiApplication.getInstance()
					.getResources().getAssets().open(jokeFile));
			bufReader = new BufferedReader(inputReader);
			String line = "";
			StringBuffer sb = new StringBuffer();
			while ((line = bufReader.readLine()) != null)
				sb.append(line);
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(bufReader != null) {
				try {
					bufReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	// 删除已阅的过期笑话
	public void deleteOldJokes() {
		getDb().delete(JOKE_TABLE, "isread=?", new String[] { "true" });
	}

}
