package com.creativeclan.canteenpaynfc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by root on 4/9/17.
 */

public class Dbhelper extends SQLiteOpenHelper {

    public  static  final  String db_name="Cantpay.db";
    public  static  final  String table_name="cantpay";
    public  static  final  String col_1="id";
    public  static  final  String col_2="roll";
    public  static  final  String col_3="name";
    public  static  final  String col_4="credit";

    public Dbhelper(Context context) {
        super(context, db_name, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+table_name+"(id VARCHAR(50) PRIMARY KEY,roll INTEGER,name TEXT,credit INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getdata( String nid) {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+table_name+" where id = "+nid,null);
        return res;
    }



    public  void up(String nid,int totv){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("update "+table_name+" set credit = "+totv+" where id = "+nid);


    }
}
