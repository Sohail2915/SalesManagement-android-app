package com.kk.kamranqadeer.salemanager.Date_Base;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by kamran qadeer on 7/31/2018.
 */

public class DbHelper extends SQLiteOpenHelper{
    //int oldVersion=3;
    //int newVersion=4;
    private static final String DataBaseName="SALE_MANAGER1_0.1";//data base name is define
    private static final int DATABASE_VERSION=1;
    public DbHelper(Context context) {
        super(context, DataBaseName, null, DATABASE_VERSION);
        //SQLiteDatabase db=this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CreateTable1="create table "+ Contracts.Sale_Table.TABLE_NAME + "(" +
                Contracts.Sale_Table._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                Contracts.Sale_Table.COLUMN_1 +" TEXT NOT NULL,"+
                Contracts.Sale_Table.COLUMN_2 +" TEXT NOT NULL,"+
                Contracts.Sale_Table.COLUMN_3 +" TEXT NOT NULL);";
        final String SQL_CreateTable2="create table "+Contracts.Sale_ProduDeteil_Table.TABLE_NAME +"("+
                Contracts.Sale_ProduDeteil_Table._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                Contracts.Sale_ProduDeteil_Table.COLUMN_1+" TEXT NOT NULL,"+
                Contracts.Sale_ProduDeteil_Table.COLUMN_2+" TEXT NOT NULL,"+
                Contracts.Sale_ProduDeteil_Table.COLUMN_3+" TEXT NOT NULL,"+
                Contracts.Sale_ProduDeteil_Table.COLUMN_4+" TEXT NOT NULL,"+
                Contracts.Sale_ProduDeteil_Table.COLUMN_5+" TEXT NOT NULL,"+
                Contracts.Sale_ProduDeteil_Table.COLUMN_6+" INTEGER,"+
                " FOREIGN KEY ("+Contracts.Sale_ProduDeteil_Table.COLUMN_6+") REFERENCES "+Contracts.Sale_ProduDeteil_Table.TABLE_NAME+"("+Contracts.Sale_Table._ID+"));";
        final String SQL_CreateTable3="create table "+ Contracts.Purch_Table.TABLE_NAME + "(" +
                Contracts.Sale_Table._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                Contracts.Purch_Table.COLUMN_1 +" TEXT NOT NULL,"+
                Contracts.Purch_Table.COLUMN_2 +" TEXT NOT NULL,"+
                Contracts.Purch_Table.COLUMN_3 +" TEXT NOT NULL);";
        final String SQL_CreateTable4="create table "+Contracts.Purch_Detail_Table.TABLE_NAME +"("+
                Contracts.Purch_Detail_Table._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                Contracts.Purch_Detail_Table.COLUMN_1+" TEXT NOT NULL,"+
                Contracts.Purch_Detail_Table.COLUMN_2+" TEXT NOT NULL,"+
                Contracts.Purch_Detail_Table.COLUMN_3+" TEXT NOT NULL,"+
                Contracts.Purch_Detail_Table.COLUMN_4+" TEXT NOT NULL,"+
                Contracts.Purch_Detail_Table.COLUMN_6+" TEXT NOT NULL,"+
                Contracts.Purch_Detail_Table.COLUMN_7+" TEXT NOT NULL,"+
                Contracts.Purch_Detail_Table.COLUMN_5+" INTEGER,"+
                " FOREIGN KEY ("+Contracts.Purch_Detail_Table.COLUMN_5+") REFERENCES "+Contracts.Purch_Detail_Table.TABLE_NAME+"("+Contracts.Purch_Table._ID+"));";
        final String SQL_CreateTable5="create table "+Contracts.Purch_Amount_Table.TABLE_NAME +"("+
                Contracts.Purch_Amount_Table._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                Contracts.Purch_Amount_Table.COLUMN_1+" TEXT NOT NULL,"+
                Contracts.Purch_Amount_Table.COLUMN_2+" TEXT NOT NULL,"+
                Contracts.Purch_Amount_Table.COLUMN_3+" TEXT NOT NULL,"+
                Contracts.Purch_Amount_Table.COLUMN_4+" TEXT NOT NULL,"+
                Contracts.Purch_Amount_Table.COLUMN_5+" INTEGER,"+
                " FOREIGN KEY ("+Contracts.Purch_Amount_Table.COLUMN_5+") REFERENCES "+Contracts.Purch_Amount_Table.TABLE_NAME+"("+Contracts.Purch_Table._ID+"));";
        final String SQL_CreateTable6="create table "+ Contracts.Production.TABLE_NAME + "(" +
                Contracts.Production._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                Contracts.Production.COLUMN_1 +" TEXT NOT NULL,"+
                Contracts.Production.COLUMN_2 +" TEXT NOT NULL,"+
                Contracts.Production.COLUMN_3 +" TEXT NOT NULL,"+
                Contracts.Production.COLUMN_4 +" TEXT NOT NULL,"+
                Contracts.Production.COLUMN_5 +" TEXT NOT NULL);";
        final String SQL_CreateTable7="create table "+Contracts.Production_Today.TABLE_NAME +"("+
                Contracts.Production_Today._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                Contracts.Production_Today.COLUMN_1+" TEXT NOT NULL,"+
                Contracts.Production_Today.COLUMN_2+" TEXT NOT NULL,"+
                Contracts.Production_Today.COLUMN_3+" TEXT NOT NULL,"+
                Contracts.Production_Today.COLUMN_4+" TEXT NOT NULL,"+
                Contracts.Production_Today.COLUMN_5+" TEXT NOT NULL,"+
                Contracts.Production_Today.COLUMN_6+" TEXT NOT NULL,"+
                Contracts.Production_Today.COLUMN_7+" INTEGER,"+
                Contracts.Production_Today.COLUMN_8+" TEXT NOT NULL,"+
                " FOREIGN KEY ("+Contracts.Production_Today.COLUMN_7+") REFERENCES "+Contracts.Production_Today.TABLE_NAME+"("+Contracts.Production._ID+"));";
        final String SQL_CreateTable8="create table "+Contracts.Production_Meterials.TABLE_NAME +"("+
                Contracts.Production_Meterials._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                Contracts.Production_Meterials.COLUMN_1+" TEXT NOT NULL,"+
                Contracts.Production_Meterials.COLUMN_2+" TEXT NOT NULL,"+
                Contracts.Production_Meterials.COLUMN_3+" TEXT NOT NULL,"+
                Contracts.Production_Meterials.COLUMN_4+" TEXT NOT NULL,"+
                Contracts.Production_Meterials.COLUMN_5+" TEXT NOT NULL,"+
                Contracts.Production_Meterials.COLUMN_7+" TEXT NOT NULL,"+
                Contracts.Production_Meterials.COLUMN_8+" TEXT NOT NULL,"+
                Contracts.Production_Meterials.COLUMN_9+" TEXT NOT NULL,"+
                Contracts.Production_Meterials.COLUMN_6+" INTEGER,"+
                " FOREIGN KEY ("+Contracts.Production_Meterials.COLUMN_6+") REFERENCES "+Contracts.Production_Meterials.TABLE_NAME+"("+Contracts.Production._ID+"));";
        final String SQL_CreateTable9="create table "+ Contracts.Stock.TABLE_NAME + "(" +
                Contracts.Stock._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                Contracts.Stock.COLUMN_1 +" TEXT NOT NULL,"+
                Contracts.Stock.COLUMN_2 +" TEXT NOT NULL,"+
                Contracts.Stock.COLUMN_3 +" TEXT NOT NULL,"+
                Contracts.Stock.COLUMN_4 +" TEXT NOT NULL,"+
                Contracts.Stock.COLUMN_5 +" TEXT NOT NULL,"+
                Contracts.Stock.COLUMN_6 +" INTEGER,"+
        " FOREIGN KEY ("+Contracts.Stock.COLUMN_6+") REFERENCES "+Contracts.Stock.TABLE_NAME+"("+Contracts.Production._ID+"));";
        final String SQL_CreateTable10="create table "+ Contracts.Record_Date.TABLE_NAME + "(" +
                Contracts.Record_Date._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                Contracts.Record_Date.COLUMN_1 +" TEXT NOT NULL,"+
                Contracts.Record_Date.COLUMN_2 +" INTEGER,"+
                " FOREIGN KEY ("+Contracts.Record_Date.COLUMN_2+") REFERENCES "+Contracts.Record_Date.TABLE_NAME+"("+Contracts.Production_Today._ID+"));";
        final String SQL_CreateTable11="create table "+ Contracts.Material_record.TABLE_NAME + "(" +
                Contracts.Material_record._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                Contracts.Material_record.COLUMN_1 +" TEXT NOT NULL,"+
                Contracts.Material_record.COLUMN_2 +" TEXT NOT NULL,"+
                Contracts.Material_record.COLUMN_3 +" TEXT NOT NULL,"+
                Contracts.Material_record.COLUMN_4 +" TEXT NOT NULL,"+
                Contracts.Material_record.COLUMN_5 +" TEXT NOT NULL,"+
                Contracts.Material_record.COLUMN_6 +" INTEGER,"+
                " FOREIGN KEY ("+Contracts.Material_record.COLUMN_6+") REFERENCES "+Contracts.Material_record.TABLE_NAME+"("+Contracts.Record_Date._ID+"));";
        final String SQL_CreateTable12="create table "+ Contracts.Labour_Record.TABLE_NAME + "(" +
                Contracts.Labour_Record._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                Contracts.Labour_Record.COLUMN_1 +" TEXT NOT NULL,"+
                Contracts.Labour_Record.COLUMN_2 +" TEXT NOT NULL,"+
                Contracts.Labour_Record.COLUMN_3 +" TEXT NOT NULL,"+
                Contracts.Labour_Record.COLUMN_4 +" TEXT NOT NULL,"+
                Contracts.Labour_Record.COLUMN_5 +" TEXT NOT NULL,"+
                Contracts.Labour_Record.COLUMN_6 +" INTEGER,"+
                " FOREIGN KEY ("+Contracts.Labour_Record.COLUMN_6+") REFERENCES "+Contracts.Labour_Record.TABLE_NAME+"("+Contracts.Record_Date._ID+"));";
        final String SQL_CreateTable13="create table "+ Contracts.Production_Record.TABLE_NAME + "(" +
                Contracts.Production_Record._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                Contracts.Production_Record.COLUMN_1 +" TEXT NOT NULL,"+
                Contracts.Production_Record.COLUMN_2 +" TEXT NOT NULL,"+
                Contracts.Production_Record.COLUMN_3 +" TEXT NOT NULL,"+
                Contracts.Production_Record.COLUMN_4 +" TEXT NOT NULL,"+
                Contracts.Production_Record.COLUMN_5 +" TEXT NOT NULL,"+
                Contracts.Production_Record.COLUMN_6 +" INTEGER,"+
                " FOREIGN KEY ("+Contracts.Production_Record.COLUMN_6+") REFERENCES "+Contracts.Production_Record.TABLE_NAME+"("+Contracts.Record_Date._ID+"));";
        final String SQL_CreateTable14="create table "+ Contracts.Account_Record.TABLE_NAME + "(" +
                Contracts.Account_Record._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                Contracts.Account_Record.COLUMN_1 +" TEXT NOT NULL,"+
                Contracts.Account_Record.COLUMN_2 +" TEXT NOT NULL,"+
                Contracts.Account_Record.COLUMN_3 +" TEXT NOT NULL,"+
                Contracts.Account_Record.COLUMN_4 +" TEXT NOT NULL,"+
                Contracts.Account_Record.COLUMN_5 +" TEXT NOT NULL,"+
                Contracts.Account_Record.COLUMN_6 +" INTEGER,"+
                " FOREIGN KEY ("+Contracts.Account_Record.COLUMN_6+") REFERENCES "+Contracts.Account_Record.TABLE_NAME+"("+Contracts.Record_Date._ID+"));";
        final String SQL_CreateTable15="create table "+ Contracts.Labour_Record_Recycle.TABLE_NAME + "(" +
                Contracts.Labour_Record_Recycle._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                Contracts.Labour_Record_Recycle.COLUMN_1 +" TEXT NOT NULL,"+
                Contracts.Labour_Record_Recycle.COLUMN_2 +" TEXT NOT NULL,"+
                Contracts.Labour_Record_Recycle.COLUMN_3 +" TEXT NOT NULL,"+
                Contracts.Labour_Record_Recycle.COLUMN_4 +" TEXT NOT NULL,"+
                Contracts.Labour_Record_Recycle.COLUMN_5 +" TEXT NOT NULL,"+
                Contracts.Labour_Record_Recycle.COLUMN_6 +" INTEGER,"+
                " FOREIGN KEY ("+Contracts.Labour_Record_Recycle.COLUMN_6+") REFERENCES "+Contracts.Labour_Record_Recycle.TABLE_NAME+"("+Contracts.Labour_Record_Recycle._ID+"));";
        db.execSQL(SQL_CreateTable1);
        db.execSQL(SQL_CreateTable2);
        db.execSQL(SQL_CreateTable3);
        db.execSQL(SQL_CreateTable4);
        db.execSQL(SQL_CreateTable5);
        db.execSQL(SQL_CreateTable6);
        db.execSQL(SQL_CreateTable7);
        db.execSQL(SQL_CreateTable8);
        db.execSQL(SQL_CreateTable9);
        db.execSQL(SQL_CreateTable10);
        db.execSQL(SQL_CreateTable11);
        db.execSQL(SQL_CreateTable12);
        db.execSQL(SQL_CreateTable13);
        db.execSQL(SQL_CreateTable14);
        db.execSQL(SQL_CreateTable15);
        Log.d("Create","Successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.Sale_Table.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.Sale_ProduDeteil_Table.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.Purch_Table.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.Purch_Detail_Table.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.Purch_Amount_Table.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.Production.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.Production_Today.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.Production_Meterials.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.Stock.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.Record_Date.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.Material_record.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.Labour_Record.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.Production_Record.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.Account_Record.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.Labour_Record_Recycle.TABLE_NAME);
      // if (newVersion > oldVersion) {
        //        db.execSQL("ALTER TABLE "+Contracts.Record_Date.TABLE_NAME+" ADD COLUMN Production_IDFK DEFAULT 0");
          //  }
       //if (newVersion > oldVersion) {
         //   db.execSQL("ALTER TABLE "+Contracts.Production_Meterials.TABLE_NAME+" ADD COLUMN EXTRA_AMOUNT DEFAULT 0");
        //}
        //data base is created now
        onCreate(db);
    }
}
