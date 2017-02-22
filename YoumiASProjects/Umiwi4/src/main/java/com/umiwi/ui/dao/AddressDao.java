package com.umiwi.ui.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Pair;

import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.model.AddressModel;

import cn.youmi.framework.db.BaseDao;
import cn.youmi.framework.util.SingletonFactory;

public class AddressDao extends BaseDao<AddressModel> {

    private static final String dbName = "address.db";
    private static final int VERSION = 1;

    public AddressDao(Context context, String name, CursorFactory factory,
                      int version) {
        super(context, dbName, factory, version);
    }

    public AddressDao() {
        this(UmiwiApplication.getApplication(), dbName, null, VERSION);
    }

    public static AddressDao getInstance() {
        return SingletonFactory.getInstance(AddressDao.class);
    }

    @Override
    protected Pair<String, String> getPrimaryKeyAndValue(AddressModel e) {
        return new Pair<String, String>("username", e.getUserName());
    }


    public AddressModel getAddress() {
        AddressModel beans = null;
        Cursor cursor = getDb().query(
                "address_table",
                new String[]{"username", "mobile", "address"}, "",
                new String[]{}, null, null, null);

        if (cursor == null) {
            return null;
        }
        while (cursor.moveToNext()) {
            beans = cursorToModel(cursor);
            break;
        }
        cursor.close();
        return beans;
    }

    @Override
    protected AddressModel cursorToModel(Cursor cursor) {
        AddressModel user = new AddressModel();
        user.setUserName(cursor.getString(cursor.getColumnIndex("username")));
        user.setMoblie(cursor.getString(cursor.getColumnIndex("mobile")));
        user.setAddress(cursor.getString(cursor.getColumnIndex("address")));
        return user;
    }

    @Override
    protected void fillContentValue(ContentValues cv, AddressModel e) {
        cv.put("username", e.getUserName());
        cv.put("mobile", e.getMoblie());
        cv.put("address", e.getAddress());
        super.fillContentValue(cv, e);
    }


    @Override
    public String getSqlFileName() {
        return "address_table.sql";
    }

    @Override
    protected String getDbName() {
        return "address.db";
    }

    @Override
    protected String getTableName(AddressModel e) {
        return "address_table";
    }

    public void clear() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("address_table", null, null);
        db.close();
    }

}
