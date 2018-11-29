package com.cis2323.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "TASKS";
    private static final String DB_COLUMN = "TASKNAME";
    private static int DB_VER = 1;

    public DBHelper(Context context) {
        super(context,DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String q = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT NOT NULL);", DB_NAME, DB_COLUMN);
        db.execSQL(q);

        //db.execSQL("CREATE TABLE TASKS (ID INTEGER PRIMARY KEY AUTOINCREMENT, TASKNAME TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String q = String.format("DELETE TABLE IF EXISTS %s;", DB_NAME);
        db.execSQL(q);

        //db.execSQL("DELETE TABLE IF EXISTS TASKS;");
        onCreate(db);
    }

    public void insertTask(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN,task);
        db.insertWithOnConflict(DB_NAME,null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ArrayList<String> getTaskList(){
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(DB_NAME, new String[]{DB_COLUMN}, null, null, null, null, null);

        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex(DB_COLUMN);
            taskList.add(cursor.getString(index));
        }

        cursor.close();
        db.close();

        return taskList;
    }

    public void deleteTask(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_NAME, DB_COLUMN + " =d ?",new String[]{task});
        db.close();
    }
}
