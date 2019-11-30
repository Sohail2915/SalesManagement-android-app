package com.kk.kamranqadeer.salemanager.Date_Base;

import android.provider.BaseColumns;

/**
 * Created by kamran qadeer on 7/31/2018.
 */

public class Contracts {
    public static final class Sale_Table implements BaseColumns
    {
        public static final String TABLE_NAME = "Sale_Product";
        public static final String COLUMN_1 = "Product_Name";
        public static final String COLUMN_2 = "Product_Code";
        public static final String COLUMN_3 = "Product_Quntity";
    }
    public static final class Sale_ProduDeteil_Table implements BaseColumns
    {
        public static final String TABLE_NAME = "Sale_Quntity";
        public static final String COLUMN_1 = "Quntity";
        public static final String COLUMN_2 = "Shope_Name";
        public static final String COLUMN_3 = "Current_Date_Time";
        public static final String COLUMN_4 = "Return_Date_Time";
        public static final String COLUMN_5 = "Pic_ID";
        public static final String COLUMN_6 = "Sale_Table_IDFK";
    }
    public static final class Purch_Table implements BaseColumns
    {
        public static final String TABLE_NAME = "Purchasing";
        public static final String COLUMN_1 = "Purch_Name";
        public static final String COLUMN_2 = "Purch_Amount";
        public static final String COLUMN_3 = "Purch_Date_Time";
    }
    public static final class Purch_Detail_Table implements BaseColumns
    {
        public static final String TABLE_NAME = "Purch_Detail";
        public static final String COLUMN_1 = "Product_Name";
        public static final String COLUMN_2 = "One_Pice_Price";
        public static final String COLUMN_3 = "Total_Quntity";
        public static final String COLUMN_4 = "Total_Price";
        public static final String COLUMN_5 = "Purch_Table_IDFK";
        public static final String COLUMN_6 = "QUNTITY";
        public static final String COLUMN_7 = "SCALE";
    }
    public static final class Purch_Amount_Table implements BaseColumns
    {
        public static final String TABLE_NAME = "Purch_Amount";
        public static final String COLUMN_1 = "Total_Cost";
        public static final String COLUMN_2 = "Total_Item";
        public static final String COLUMN_3 = "Traval_Ext";
        public static final String COLUMN_4 = "Final_Amount";
        public static final String COLUMN_5 = "Purch_Table_IDFK";
    }
    public static final class Production implements BaseColumns
    {
        public static final String TABLE_NAME = "Production";
        public static final String COLUMN_1 = "NAME";
        public static final String COLUMN_2 = "QUNTITY";
        public static final String COLUMN_3 = "GRADE";
        public static final String COLUMN_4 = "CODE";
        public static final String COLUMN_5 = "DATE_TIME";
    }
    public static final class Production_Meterials implements BaseColumns
    {
        public static final String TABLE_NAME = "Production_Meterials";
        public static final String COLUMN_1 = "NAME";
        public static final String COLUMN_2 = "USED";
        public static final String COLUMN_3 = "SCALE";
        public static final String COLUMN_4 = "COST";
        public static final String COLUMN_5 = "ON_DOZ_LABORWORK";
        public static final String COLUMN_6 = "Production_Table_IDFK";
        public static final String COLUMN_7 = "HOW_MANY_DOZEN";
        public static final String COLUMN_8 = "ONE_DOZE_AMOUNT";
        public static final String COLUMN_9 = "EXTRA_AMOUNT";
    }
    public static final class Production_Today implements BaseColumns
    {
        public static final String TABLE_NAME = "Production_Today";
        public static final String COLUMN_1 = "PRODUCTION";
        public static final String COLUMN_2 = "TODAY_COST";
        public static final String COLUMN_3 = "TODAY_LABOR_WORK";
        public static final String COLUMN_4 = "TOTAL_AMOUNT";
        public static final String COLUMN_5 = "DATE";
        public static final String COLUMN_6 = "DAY";
        public static final String COLUMN_7 = "Production_Meterials_IDFK";
        public static final String COLUMN_8 = "LABOUR_NAME";
    }
    public static final class Stock implements BaseColumns
    {
        public static final String TABLE_NAME = "Stock";
        public static final String COLUMN_1 = "NAME";
        public static final String COLUMN_2 = "Total_Stock";
        public static final String COLUMN_3 = "Used";
        public static final String COLUMN_4 = "Left";
        public static final String COLUMN_5 = "Storage_1";
        public static final String COLUMN_6 = "Production_IDFK";
    }
    public static final class Record_Date implements BaseColumns
    {
        public static final String TABLE_NAME = "Record_Date";
        public static final String COLUMN_1 = "DATE";
        public static final String COLUMN_2 = "Production_IDFK";
    }
    public static final class Material_record implements BaseColumns
    {
        public static final String TABLE_NAME = "Material_record";
        public static final String COLUMN_1 = "Date";
        public static final String COLUMN_2 = "NAME";
        public static final String COLUMN_3 = "Purchasing";
        public static final String COLUMN_4 = "Used";
        public static final String COLUMN_5 = "Left";
        public static final String COLUMN_6 = "IDFK";
    }
    public static final class Labour_Record implements BaseColumns
    {
        public static final String TABLE_NAME = "Labour_Record";
        public static final String COLUMN_1 = "Date";
        public static final String COLUMN_2 = "NAME";
        public static final String COLUMN_3 = "Work";
        public static final String COLUMN_4 = "Amount";
        public static final String COLUMN_5 = "Pay";
        public static final String COLUMN_6 = "IDFK";
    }
    public static final class Production_Record implements BaseColumns
    {
        public static final String TABLE_NAME = "Production_Record";
        public static final String COLUMN_1 = "Date";
        public static final String COLUMN_2 = "NAME";
        public static final String COLUMN_3 = "Production";
        public static final String COLUMN_4 = "Sale";
        public static final String COLUMN_5 = "Left";
        public static final String COLUMN_6 = "IDFK";
    }
    public static final class Account_Record implements BaseColumns
    {
        public static final String TABLE_NAME = "Account_Record";
        public static final String COLUMN_1 = "Date";
        public static final String COLUMN_2 = "Sale_Amount";
        public static final String COLUMN_3 = "Prod_Amount";
        public static final String COLUMN_4 = "Profit_Amount";
        public static final String COLUMN_5 = "Loss_Amount";
        public static final String COLUMN_6 = "IDFK";
    }
    public static final class Labour_Record_Recycle implements BaseColumns
    {
        public static final String TABLE_NAME = "Labour_Record_Recycle";
        public static final String COLUMN_1 = "Date";
        public static final String COLUMN_2 = "NAME";
        public static final String COLUMN_3 = "Product_Name";
        public static final String COLUMN_4 = "Work";
        public static final String COLUMN_5 = "Amount";
        public static final String COLUMN_6 = "IDFK";
    }
}
