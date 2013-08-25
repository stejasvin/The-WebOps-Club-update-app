package com.example.testfile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdatesDbHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "updatesManager";


    private static final String TABLE_UPDATES = "updates";

    public static final String KEY_LOCAL_ID = "l_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "l_timestamp";
    public static final String KEY_SERVER_ID = "s_id";
    private static final String TAG = "UpdatesDatabaseHandler";

    public UpdatesDbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_UPDATES_TABLE = "CREATE TABLE " + TABLE_UPDATES + "("
                + KEY_LOCAL_ID + " INTEGER PRIMARY KEY,"
                + KEY_SERVER_ID + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_MESSAGE + " TEXT,"
                + KEY_TIMESTAMP + " TEXT" + ")";
        db.execSQL(CREATE_UPDATES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UPDATES);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new update
    public void addUpdate(Update update) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // Annotation primary key

//        values.put(KEY_LOCAL_ID, update.getLocalId());
        values.put(KEY_SERVER_ID, update.getServerId());
        values.put(KEY_TITLE, update.getTitle());
        values.put(KEY_MESSAGE, update.getMessage());
        values.put(KEY_TIMESTAMP, update.getLocalTime());

        // Inserting Row
        if (db.insert(TABLE_UPDATES, null, values) == -1)
            Log.e(TAG, "error in inserting");
        db.close(); // Closing database connection
    }

    // Getting single update
    public Update getUpdate(int l_id) {
        Update update = new Update();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_UPDATES, new String[]{
                KEY_LOCAL_ID,
                KEY_SERVER_ID,
                KEY_TITLE,
                KEY_MESSAGE,
                KEY_TIMESTAMP}, KEY_LOCAL_ID + "=?",
                new String[]{String.valueOf(l_id)}, null, null, null, null);

        if (cursor != null)
            if (cursor.moveToFirst()) {

                update.setLocalId(cursor.getInt(0));
                update.setServerId(cursor.getString(1));
                update.setTitle(cursor.getString(2));
                update.setMessage(cursor.getString(3));
                update.setLocalTime(cursor.getString(4));

                db.close();
                return update;
            }
        db.close();
        return null;

    }

    // Getting All Updates
    public ArrayList<Update> getAllUpdates() {
        ArrayList<Update> updateList = new ArrayList<Update>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_UPDATES + " ORDER BY " + KEY_LOCAL_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Update update = new Update();
                update.setLocalId(cursor.getInt(0));
                update.setServerId(cursor.getString(1));
                update.setTitle(cursor.getString(2));
                update.setMessage(cursor.getString(3));
                update.setLocalTime(cursor.getString(4));

                // Adding annotation to list
                updateList.add(update);
            } while (cursor.moveToNext());
        }
        db.close();

        // return annotation list
        return updateList;
    }

    public void deleteUpdate(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_UPDATES, KEY_LOCAL_ID + " = ?",
                new String[]{id});
        db.close();
    }
//    // Getting All Annotations
//    public ArrayList<HashMap<String,String>> getAllAnnotationsArrayList(String ecgId) {
//        ArrayList<HashMap<String,String>> annotationList = new ArrayList<HashMap<String,String>>();
//        // Select All Query
////        String selectQuery = "SELECT  * FROM " + TABLE_UPDATES + "WHERE "+ KEY_ECG_ID +"= "+ s_id;
//
//        SQLiteDatabase db = this.getReadableDatabase();
////        Cursor cursor = db.rawQuery(selectQuery, null);
//        Cursor cursor = db.query(TABLE_UPDATES, new String[] {
//                KEY_LOCAL_ID,
//                KEY_SERVER_ID,
//                KEY_LOCAL_ECG_ID,
//                KEY_NOTE,
//                KEY_TIMESTAMP}, KEY_LOCAL_ECG_ID + "=?",
//                new String[] { ecgId }, null, null, null, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                HashMap<String,String> hashMap = new HashMap<String, String>();
//                hashMap.put(KEY_LOCAL_ID,cursor.getString(0));
//                hashMap.put(KEY_SERVER_ID, cursor.getString(1));
//                hashMap.put(KEY_LOCAL_ECG_ID,cursor.getString(2));
//                hashMap.put(KEY_NOTE, cursor.getString(3));
//                hashMap.put(KEY_TIMESTAMP, cursor.getString(4));
//                // Adding annotation to list
//                annotationList.add(hashMap);
//            } while (cursor.moveToNext());
//        }
//        db.close();
//        // return annotation list
//        return annotationList;
//    }

//	// Updating single annotation
//	public int updateAnnotation(Annotation annotation) {
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		ContentValues values = new ContentValues();
//		values.put(KEY_NAME, annotation.getName());
//		values.put(KEY_PH_NO, annotation.getPhoneNumber());
//
//		// updating row
//		return db.update(TABLE_UPDATES, values, KEY_ID + " = ?",
//				new String[] { String.valueOf(annotation.getID()) });
//	}
//
//	// Deleting single annotation
//	public void deleteAnnotation(Annotation annotation) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		db.delete(TABLE_UPDATES, KEY_ID + " = ?",
//				new String[] { String.valueOf(annotation.getID()) });
//		db.close();
//	}


//	// Getting annotations Count
//	public int getAnnotationsCount() {
//		String countQuery = "SELECT  * FROM " + TABLE_UPDATES;
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cursor = db.rawQuery(countQuery, null);
//		cursor.close();
//
//		// return count
//		return cursor.getCount();
//	}

}
