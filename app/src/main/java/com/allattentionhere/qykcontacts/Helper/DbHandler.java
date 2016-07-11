package com.allattentionhere.qykcontacts.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

import com.allattentionhere.qykcontacts.Model.Contacts;
import com.google.gson.Gson;

import java.util.ArrayList;


public class DbHandler {

    public static final String S_TABLE_NAME = "Contacts";
    public static final String S_COL_1 = "contact_id";
    public static final String S_COL_2 = "contact_string";
    private static final int DATABASE_VERSION = 1;
    private static DbHelper dbInstance;

    public static SQLiteDatabase getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = new DbHelper(context.getApplicationContext());
        }
        return dbInstance.getWritableDatabase();
    }

    public static void addContactToDb(String id, String string_contacts) {
        SQLiteDatabase db = getInstance(MyApplication.context);
        ContentValues contentValues = new ContentValues();
        contentValues.put(S_COL_1, id);
        contentValues.put(S_COL_2, string_contacts);
        db.insert(S_TABLE_NAME, null, contentValues);
    }

    public static void updateContact(String id, String string_contacts) {
        SQLiteDatabase db = getInstance(MyApplication.context);
        ContentValues contentValues = new ContentValues();
        contentValues.put(S_COL_1, id);
        contentValues.put(S_COL_2, string_contacts);
        String where = S_COL_1 + "=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        db.update(S_TABLE_NAME, contentValues, where, whereArgs);
    }

    public static Contacts[] fetchContactsList() {
        SQLiteDatabase db = getInstance(MyApplication.context);
        Cursor cursor = db.rawQuery("select * from " + S_TABLE_NAME, null);
        ArrayList<Contacts> mArr = new ArrayList<>();

        while (cursor.moveToNext()) {
//            Log.d("K9_db", "in db| id=" + cursor.getString(0) +"| str="+ cursor.getString(1));
            Contacts mContact = new Gson().fromJson(cursor.getString(1), Contacts.class);
            mArr.add(mContact);
        }

        if (cursor != null) cursor.close();
        Contacts[] mC = mArr.toArray(new Contacts[mArr.size()]);

        return mC;
    }

    public static String fetchContact(String id) {
        SQLiteDatabase db = getInstance(MyApplication.context);
        Cursor cursor = db.rawQuery("select * from " + S_TABLE_NAME + " where " + S_COL_1 + " = " + id, null);
        String pickupSlotList = null;

        if (cursor != null && cursor.moveToFirst()) {
            Log.d("K9_db", "in db : " + cursor.getString(0) + cursor.getString(1));
            pickupSlotList = cursor.getString(1);
        }
        if (cursor != null) cursor.close();
        return pickupSlotList;
    }

    public static Contacts[] fetchFavoriteContacts() {
        SQLiteDatabase db = getInstance(MyApplication.context);
        Cursor cursor = db.rawQuery("select * from " + S_TABLE_NAME, null);
        ArrayList<Contacts> mArr = new ArrayList<>();

        while (cursor.moveToNext()) {
//            Log.d("K9_db", "in db| id=" + cursor.getString(0) +"| str="+ cursor.getString(1));
            Contacts mContact = new Gson().fromJson(cursor.getString(1), Contacts.class);
            if (mContact.getIsFavorite().equalsIgnoreCase("true")) {
                mArr.add(mContact);
            }
        }

        if (cursor != null) cursor.close();
        Contacts[] mC = mArr.toArray(new Contacts[mArr.size()]);

        return mC;
    }


    public static class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context context) {
            super(context, context.getPackageName(), new LoggingCursorFactory(), DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + S_TABLE_NAME + " (contact_id TEXT, contact_string TEXT, is_favorite TEXT, is_tobereminded TEXT )");
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + S_TABLE_NAME);

        }

        private static class LoggingCursorFactory implements SQLiteDatabase.CursorFactory {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
                Log.d(this.getClass().getSimpleName(), query.toString());
                return new SQLiteCursor(masterQuery, editTable, query);
            }
        }
    }
}