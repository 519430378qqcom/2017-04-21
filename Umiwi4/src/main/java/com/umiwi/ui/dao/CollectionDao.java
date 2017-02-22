package com.umiwi.ui.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.CourseListModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.youmi.framework.util.SingletonFactory;

/**
 * 收藏dao
 *
 * @author tjie00
 * @version 15/06/19
 */
public class CollectionDao extends BaseDao {

    public static final String COLLECTION_TABLE = "collection_table";

    public CollectionDao() {
        super();
    }


    public static CollectionDao getInstance() {
        return SingletonFactory.getInstance(CollectionDao.class);
    }

    // 获取需要上传的收藏ids
    public List<String> getUnuploadSaveCollections() {
        LinkedList<String> collections = new LinkedList<String>();

        Cursor cursor = getDb().rawQuery(
                "SELECT albumid FROM " + COLLECTION_TABLE
                        + " WHERE is_save_upload=? AND userid=?",
                new String[]{"false", YoumiRoomUserManager.getInstance().getUid()});
        if (cursor != null && cursor.moveToNext()) {
            String collection = cursor.getString(cursor.getColumnIndex("albumid"));

            collections.add(collection);

        }

        cursor.close();

        return collections;
    }

    // 获取需要上传的删除ids
    public List<String> getUnuploadDeleteCollections() {
        LinkedList<String> collections = new LinkedList<String>();

        Cursor cursor = getDb().rawQuery(
                "SELECT albumid FROM " + COLLECTION_TABLE
                        + " WHERE is_delete_upload=? AND userid=?",
                new String[]{"false", YoumiRoomUserManager.getInstance().getUid()});
        if (cursor != null && cursor.moveToNext()) {
            String collection = cursor.getString(cursor.getColumnIndex("albumid"));

            collections.add(collection);

        }

        cursor.close();

        return collections;
    }

    // 数据库中是否含有未上传的收藏数据
    public boolean isCollectionNeed2Update() {
        boolean needUpdate = false;
        Cursor cursor = getDb().rawQuery(
                "SELECT count(*) AS upcount FROM " + COLLECTION_TABLE
                        + " WHERE is_save_upload=? OR is_delete_upload=? AND userid=?", new String[]{"false", "false", YoumiRoomUserManager.getInstance().getUid()});
        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndex("upcount"));
            if (count > 0) {
                needUpdate = true;
            }
        }

        cursor.close();

        return needUpdate;
    }

    // 同步数据
    public void syncCollections(ArrayList<CourseListModel> collections) {
        if (collections != null && collections.size() > 0) {
            for (CourseListModel collection : collections) {
                if (isSaved(collection.id + "")) {
                    updateCollection(collection);
                } else {
                    saveCollection(collection);
                }
            }
        }
    }

    // 同步数据库中未收藏的数据
    private void saveCollection(CourseListModel collection) {
        SQLiteDatabase sqldb = getDb();

        sqldb.beginTransaction(); // 开始事务
        try {
            sqldb.execSQL(
                    "INSERT INTO  " + COLLECTION_TABLE + " ([albumid], [favid], [is_save_upload] , [is_delete_upload], [userid]) VALUES(?, ?, ?, ?, ?)",
                    new String[]{collection.id + "", collection.favid, "true", "", YoumiRoomUserManager.getInstance().getUid()});
            sqldb.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            sqldb.endTransaction(); // 结束事务
        }
    }

    // 同步数据库中的已收藏的状态
    private void updateCollection(CourseListModel collection) {
        SQLiteDatabase sqldb = getDb();

        sqldb.beginTransaction(); // 开始事务
        try {
            sqldb.execSQL(
                    "UPDATE " + COLLECTION_TABLE + " SET is_save_upload=?, favid=?, is_delete_upload=? WHERE albumid=? AND userid=?",
                    new String[]{"true", collection.favid + "", "", collection.id + "", YoumiRoomUserManager.getInstance().getUid()});
            sqldb.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            sqldb.endTransaction(); // 结束事务
        }
    }

    // 更新上传成功状态
    public void updateCollection(String albumid) {
        SQLiteDatabase sqldb = getDb();

        sqldb.beginTransaction(); // 开始事务
        try {
            sqldb.execSQL(
                    "UPDATE " + COLLECTION_TABLE + " SET is_save_upload=? WHERE albumid=? AND userid=?",
                    new String[]{"true", albumid, YoumiRoomUserManager.getInstance().getUid()});
            sqldb.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            sqldb.endTransaction(); // 结束事务
        }
    }


    // 保存一条收藏记录
    public void saveCollection(String albumid) {
        SQLiteDatabase sqldb = getDb();

        sqldb.beginTransaction(); // 开始事务
        try {
            sqldb.execSQL(
                    "INSERT INTO " + COLLECTION_TABLE
                            + "([albumid], [favid], [is_save_upload] , [is_delete_upload], [userid]) VALUES(?, ?, ?, ?, ?)",
                    new Object[]{albumid, "", "false", "", YoumiRoomUserManager.getInstance().getUid()});
            sqldb.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            sqldb.endTransaction(); // 结束事务
        }
    }

    // 在数据库中彻底删除一条收藏记录
    public void deleteCollectionCompelete(String albumid) {
        SQLiteDatabase sqldb = getDb();

        sqldb.beginTransaction(); // 开始事务

        try {
            sqldb.delete(COLLECTION_TABLE, "[albumid]=? AND userid=?", new String[]{albumid, YoumiRoomUserManager.getInstance().getUid()});
            sqldb.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            sqldb.endTransaction(); // 结束事务
        }
    }

    // 更新收藏记录的删除状态
    public void deleteCollectionInDB(String albumid) {

        SQLiteDatabase sqldb = getDb();

        sqldb.beginTransaction(); // 开始事务
        try {
            sqldb.execSQL(
                    "UPDATE " + COLLECTION_TABLE + " SET is_delete_upload=? WHERE albumid=? AND userid=?",
                    new String[]{"false", albumid, YoumiRoomUserManager.getInstance().getUid()});
            sqldb.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            sqldb.endTransaction(); // 结束事务
        }
    }

    // 根据视频id判断视频是否被收藏
    public boolean isSaved(String albumid) {
        boolean isSaved = false;
        if (new String[]{albumid, YoumiRoomUserManager.getInstance().getUid()} == null) {//http://stackoverflow.com/questions/29619718/android-the-bind-value-at-index-1-is-null-error
            return false;
        }
        Cursor cursor = getDb().rawQuery(
                "SELECT [albumid], [favid], [is_save_upload], [is_delete_upload], [userid] FROM " + COLLECTION_TABLE
                        + " WHERE albumid=? AND userid=?", new String[]{albumid, YoumiRoomUserManager.getInstance().getUid()});
        if (cursor != null && cursor.moveToFirst()) {//http://stackoverflow.com/questions/24504213/illegalargumentexception-the-bind-value-index-1-is-null-with-sqlite-database-in
            if (cursor.getCount() > 0) {
                isSaved = true;
            }
        }
        cursor.close();

        return isSaved;
    }

}
