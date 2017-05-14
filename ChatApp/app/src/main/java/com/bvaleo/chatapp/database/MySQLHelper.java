package com.bvaleo.chatapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Valery on 16.04.2017.
 */
public class MySQLHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "chat";

    private static final String TABLE_USERS = "users";

    private static final String KEY_ID = "id";
    private static final String KEY_USER = "user";
    private static final String KEY_PASS = "pass";

    public MySQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER + " TEXT,"
                + KEY_PASS + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        onCreate(db);
    }


    public boolean isUser(String login, String pass){

        String query = "SELECT " + KEY_USER + ", " + KEY_PASS + " FROM " + TABLE_USERS + " WHERE " + KEY_USER + " = '" + login + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if(c != null && c.getCount() > 0){
            c.moveToFirst();
            String passUser = c.getString(1);
            if(passUser.equals(pass)) {
                db.close(); return true;
            }
            else {
                db.close(); return false;
            }
        } else {
            db.close();
            return false;}

    }

    public void insertNewUser(String login, String pass){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER, login);
        values.put(KEY_PASS, pass);

        db.insert(TABLE_USERS, null, values);
        db.close();
    }
}
