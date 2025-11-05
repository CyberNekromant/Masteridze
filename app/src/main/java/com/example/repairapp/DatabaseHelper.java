package com.example.repairapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "repair_requests.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE = "requests";
    private static final String COL_ID = "id";
    private static final String COL_OWNER = "owner_name";
    private static final String COL_PHONE = "phone";
    private static final String COL_MODEL = "model";
    private static final String COL_DESC = "description";
    private static final String COL_DATE = "date_created";
    private static final String COL_TIME = "time_created";
    private static final String COL_STATUS = "status";
    private static final String COL_TYPE = "type";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_OWNER + " TEXT, "
                + COL_PHONE + " TEXT, "
                + COL_MODEL + " TEXT, "
                + COL_DESC + " TEXT, "
                + COL_DATE + " TEXT, "
                + COL_TIME + " TEXT, "
                + COL_STATUS + " TEXT DEFAULT 'новая', "
                + COL_TYPE + " TEXT);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public long insertRequest(Request request) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_OWNER, request.getOwnerName());
        cv.put(COL_PHONE, request.getPhone());
        cv.put(COL_MODEL, request.getModel());
        cv.put(COL_DESC, request.getDescription());
        cv.put(COL_DATE, request.getDateCreated());
        cv.put(COL_TIME, request.getTimeCreated());
        cv.put(COL_STATUS, request.getStatus());
        cv.put(COL_TYPE, request.getType());
        return db.insert(TABLE, null, cv);
    }

    public List<Request> getRequestsByType(String type) {
        List<Request> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE, null, COL_TYPE + "=?", new String[]{type}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Request r = new Request(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_OWNER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MODEL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DESC)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS)),
                        type
                );
                list.add(r);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<Request> getAllRequests() {
        List<Request> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE));
                Request r = new Request(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_OWNER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MODEL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DESC)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS)),
                        type
                );
                list.add(r);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public void updateStatus(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_STATUS, status);
        db.update(TABLE, cv, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteRequest(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void updateRequestFull(int id, String ownerName, String phone, String model,
                                  String description, String dateCreated, String timeCreated,
                                  String status, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_OWNER, ownerName);
        cv.put(COL_PHONE, phone);
        cv.put(COL_MODEL, model);
        cv.put(COL_DESC, description);
        cv.put(COL_DATE, dateCreated);
        cv.put(COL_TIME, timeCreated);
        cv.put(COL_STATUS, status);
        cv.put(COL_TYPE, type);
        db.update(TABLE, cv, COL_ID + "=?", new String[]{String.valueOf(id)});
    }
}