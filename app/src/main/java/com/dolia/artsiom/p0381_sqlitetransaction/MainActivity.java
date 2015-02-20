package com.dolia.artsiom.p0381_sqlitetransaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;


public class MainActivity extends ActionBarActivity {

    final String LOG_TAG = "myLog";

    DBHelper dbh;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Boolean delDB = this.deleteDatabase("myDB");
        if(delDB){

            Log.d(LOG_TAG, "--- myDB deleted ---");
        }

        dbh = new DBHelper(this);
        myActions();
    }

    void myActions(){
        try{
            db = dbh.getWritableDatabase();
            delete(db, "mytable");

            db.beginTransaction();
            insert(db, "mytable", "val1");

            Log.d(LOG_TAG, "New DBHelper");
            DBHelper dbh2 = new DBHelper(this);
            SQLiteDatabase db2 = dbh2.getWritableDatabase();
            read(db2, "mytable");
            dbh2.close();

            db.setTransactionSuccessful();
            db.endTransaction();

            read(db, "mytable");
            dbh.close();
        }catch (Exception ex){

            Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
        }

    }

    void delete (SQLiteDatabase db, String table){
        Log.d(LOG_TAG, "Del all from " + table);
        db.delete(table, null, null);
    }

    void insert(SQLiteDatabase db, String table, String value){
        Log.d(LOG_TAG, "Insert to " + table + " value " + value);

        ContentValues cv = new ContentValues();
        cv.put("val", value);
        db.insert(table, null, cv);
    }

    void read (SQLiteDatabase db, String table){
        Log.d(LOG_TAG, "Read from " + table);

        Cursor c = db.query(table, null, null, null, null, null, null);

        if(c != null){
            Log.d(LOG_TAG, "Record count = " + c.getCount());

            if(c.moveToFirst()){

                do{

                    Log.d(LOG_TAG, c.getString(c.getColumnIndex("val")));

                }while (c.moveToNext());
            }
            c.close();
        }else{
            Log.d(LOG_TAG, "Cursor is null");
        }
    }


    class DBHelper extends SQLiteOpenHelper{

        public DBHelper (Context context){
            super(context, "myDB", null, 1);
        }

        public void onCreate(SQLiteDatabase db){

            Log.d(LOG_TAG, "--- onCreate db ---");

            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "val text"
                    + ");");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){

        }
    }
}
