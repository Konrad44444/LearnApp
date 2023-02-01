package com.example.learnapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetsDb {
    private static final String DB_NAME = "SETS_DATABASE.db";
    private static int DB_VERSION = 200;
    private final Context mCtx;
    private static final String EMPTY_TABLE = "EMPTY_TABLE";

    public static String TAG = SetsDb.class.getSimpleName();

    public static final String KEY_ROW_ID = "_id";
    public static final String DEFINITION = "definition";
    public static final String DESCRIPTION = "description";

    public static final String[] tableFields = new String[] {KEY_ROW_ID, DEFINITION, DESCRIPTION};

    public static final String C_TABLE =
            "create table " + EMPTY_TABLE + "("
                    + KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DEFINITION + " not null UNIQUE, "
                    + DESCRIPTION + " text);";


    private dbHelper mDbHelper;
    public SQLiteDatabase mDb;

    public SetsDb(Context context) {
        this.mCtx = context;
    }

    public SetsDb open() throws SQLException {
        mDbHelper = new dbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if(mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public void upgrade() throws SQLException {
        mDbHelper = new dbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        mDbHelper.onUpgrade(mDb, 1, 0);
    }

    public void createTable(String name) {
        String query =
                "CREATE TABLE " + name + "("
                        + KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + DEFINITION + " not null UNIQUE, "
                        + DESCRIPTION + " text);";
        mDb.execSQL(query);
    }

    public ArrayList<String> getTables() {
        ArrayList<String> result = new ArrayList<>();
        String query = "SELECT name FROM sqlite_master WHERE type='table'";
        Cursor c = mDb.rawQuery(query, null);

        if(c.moveToFirst()) {
            while (!c.isAfterLast()) {
                result.add(c.getString(0));
                c.moveToNext();
            }
        }
        return result;
    }

    public long addToTable(String tableName, ContentValues contentValues) {
        return mDb.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public boolean updateInTable(String tableName, int id, ContentValues newValues) {
        String[] selectionArgs = {String.valueOf(id)};
        return mDb.update(tableName, newValues, KEY_ROW_ID + "=?", selectionArgs) > 0;
    }

    public boolean deleteFromTable(String tableName, int id) {
        String[] selectionArgs = {String.valueOf(id)};
        return mDb.delete(tableName, KEY_ROW_ID + "=?", selectionArgs) > 0;
    }

    public void deleteTable(String tableName) {
        String query = "DROP TABLE IF EXISTS " + tableName;
        mDb.execSQL(query);
    }

    public Cursor getDataFromTable(String tableName) {
        return mDb.query(tableName, tableFields, null, null, null, null, null);
    }

//    public ArrayList<String> getDefinitions(String tableName) {
//        Cursor c =  mDb.query(tableName, tableFields, null, null, null, null, null);
//        ArrayList<String> result = new ArrayList<>(c.getCount());
//
//        while(!c.isAfterLast()) {
//            result.add(c.getString(1));
//            c.moveToNext();
//        }
//
//        return result;
//    }


    private static class dbHelper extends SQLiteOpenHelper {
        public dbHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(C_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Aktualizacja bazy danych z wersji " + oldVersion + " na wersję "
                    + newVersion + ", co spowoduje usunięcie wszystkich danych z bazy ");
            db.execSQL("DROP TABLE IF EXISTS " + EMPTY_TABLE );
            onCreate(db);
        }
    }
}
