package com.example.saki99.retrofit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saki99 on 8.2.2018..
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Retrofit.db";

    public static class TableNames implements BaseColumns {
        public static final String TABLE_NAME = "datas_table";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_BODY = "body";
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TableNames.TABLE_NAME + " (" +
                        TableNames._ID + " INTEGER PRIMARY KEY," +
                        TableNames.COLUMN_NAME_TITLE + " TEXT," +
                        TableNames.COLUMN_NAME_BODY + " TEXT)";

        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TableNames.TABLE_NAME;

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void addData(Podatak podatak) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TableNames.COLUMN_NAME_TITLE, podatak.getTitle());
        values.put(TableNames.COLUMN_NAME_BODY, podatak.getBody());

        db.insert(TableNames.TABLE_NAME, null, values);
        db.close();
    }

    public Podatak getData(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TableNames.TABLE_NAME,
                                    new String[] { TableNames._ID, TableNames.COLUMN_NAME_TITLE, TableNames.COLUMN_NAME_BODY },
                                    TableNames._ID + "=?",
                                    new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        return new Podatak(cursor.getString(1), cursor.getString(2));
    }

    public List<Podatak> getAllData() {

        List<Podatak> podaci = new ArrayList<>();

        String query = "SELECT * FROM " + TableNames.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Podatak contact = new Podatak();
                contact.setTitle(cursor.getString(1));
                contact.setBody(cursor.getString(2));
                podaci.add(contact);
            } while (cursor.moveToNext());
        }

        return podaci;
    }
}
