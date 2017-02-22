package com.umiwi.ui.dao;

import java.util.ArrayList;
import java.util.Date;

import android.database.Cursor;
import cn.youmi.framework.util.SingletonFactory;

import com.umiwi.ui.beans.LecturerBean;
import com.umiwi.ui.beans.SearchCloudBean;

/**
 * 云标签dao
 * 
 * @author tjie00
 * @version 15/06/14
 */
public class SearchCloudDao extends BaseDao {

	public static final String SEARCHCLOUD_TABLE = "searchcloud_table";

	public SearchCloudDao() {
		super();
	}

	public static SearchCloudDao getInstance() {
		return SingletonFactory.getInstance(SearchCloudDao.class);
	}

	public int getPageCount() {
		Cursor cursor = getDb().rawQuery(
				"SELECT page FROM " + SEARCHCLOUD_TABLE
						+ " ORDER BY page DESC LIMIT 1",
				null);
		
		if(cursor.moveToFirst()) {
			return cursor.getInt(0);
		}
		return -1;
	}
	
	public ArrayList<SearchCloudBean> getCloudByPage(int page) {
		ArrayList<SearchCloudBean> beans = new ArrayList<SearchCloudBean>();
		
		Cursor cursor = getDb().rawQuery(
				"SELECT [page], [title], [detailurl], [type], [color], [modifytime], [lecturer_name], [lecturer_title], [lecturer_image], [lecturer_courseurl] FROM " + SEARCHCLOUD_TABLE
						+ " WHERE page=?",
				new String[]{page+""});
		while (cursor != null && cursor.moveToNext()) {
			SearchCloudBean bean = new SearchCloudBean();
			
			bean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			bean.setDetailurl(cursor.getString(cursor.getColumnIndex("detailurl")));
			bean.setType(cursor.getString(cursor.getColumnIndex("type")));
			bean.setColor(cursor.getString(cursor.getColumnIndex("color")));
			
			LecturerBean info = new LecturerBean();
			info.setName(cursor.getString(cursor.getColumnIndex("lecturer_name")));
			info.setTitle(cursor.getString(cursor.getColumnIndex("lecturer_title")));
			info.setImage(cursor.getString(cursor.getColumnIndex("lecturer_image")));
			info.setCourseurl(cursor.getString(cursor.getColumnIndex("lecturer_courseurl")));
			
			bean.setInfo(info);
			
			beans.add(bean);
		} 
		return beans;
	}


	// 清空表中内容
	public void deleteClouds() {
		getDb().delete(SEARCHCLOUD_TABLE, null, null);
	}

	// 数据库是否被创建,其中是否有数据
	public boolean isInit() {
		boolean isInit = false;

		Cursor cursor = getDb().rawQuery("SELECT count(*) FROM " + SEARCHCLOUD_TABLE,
				null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int count = cursor.getInt(0);
				if (count != 0 || count != -1) {
					isInit = true;
				}
			}
		}

		return isInit;
	}

	// 数据库中的数据是否已经过期
	public boolean ifDataOutTime() {
		boolean isOutTime = false;
		isOutTime = isInit();
		
		
		if(!isOutTime) {
			return true;
		}
		
		if(isOutTime) {
			Cursor cursor = getDb().rawQuery("SELECT modifytime FROM " + SEARCHCLOUD_TABLE + " limit 1", null);
			if(cursor.moveToFirst()) {
				long modifytime = cursor.getLong(0);
				if(modifytime != 0 && modifytime != -1) {
					Date date = new Date(modifytime);
	
					Date now = new Date();
					
					
					return (now.getTime() / 86400000 - date.getTime() / 86400000) > 1;
					
				}
			}
		}
		
		return isOutTime;
	}

	// 保存
	public void saveSearchClouds(ArrayList<ArrayList<SearchCloudBean>> results) {
		if(results != null && results.size()>0) {
			
			deleteClouds();
			
			for(int i=0; i <results.size(); i++) {
				ArrayList<SearchCloudBean> wrapper = results.get(i);
				if(wrapper != null && wrapper.size() > 0) {
					
					for(int j=0; j<wrapper.size(); j++) {
						saveCloud(wrapper.get(j), i+1);
					}
				}
				
			}
		}
	}

	private void saveCloud(SearchCloudBean searchCloudBean, int page) {
		
		LecturerBean info = searchCloudBean.getInfo();
		if(info == null) {
			info = new LecturerBean();
			info.setTitle("");
			info.setName("");
			info.setImage("");
			info.setCourseurl("");
		}
		
		getDb().beginTransaction(); // 开始事务
		try {
			getDb().execSQL(
					"INSERT INTO " + SEARCHCLOUD_TABLE
							+ "([page], [title], [detailurl], [type], [color], "
							+ "[lecturer_name], [lecturer_title], [lecturer_image], [lecturer_courseurl], [modifytime]) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					new Object[] { page, searchCloudBean.getTitle(), 
							searchCloudBean.getDetailurl(), 
							searchCloudBean.getType(), 
							searchCloudBean.getColor(),
							info.getName(),
							info.getTitle(),
							info.getImage(),
							info.getCourseurl(),
							System.currentTimeMillis()});
			getDb().setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			getDb().endTransaction(); // 结束事务
		}
		
	}

}
