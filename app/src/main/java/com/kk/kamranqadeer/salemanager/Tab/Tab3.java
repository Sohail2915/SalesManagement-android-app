package com.kk.kamranqadeer.salemanager.Tab;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kk.kamranqadeer.salemanager.AllAdapters.Recor_Adapter_2;
import com.kk.kamranqadeer.salemanager.AllAdapters.Recor_Adapter_3;
import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;
import com.kk.kamranqadeer.salemanager.R;

import java.util.ArrayList;

/**
 * Created by kamran qadeer on 8/30/2018.
 */

@SuppressLint("ValidFragment")
public class Tab3 extends Fragment {
    DbHelper db_helper;
    RecyclerView recyclerView;
    Recor_Adapter_3 adapter;
    long id;
    @SuppressLint("ValidFragment")
    public Tab3(long id) {
        this.id=id;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        db_helper = new DbHelper(getContext());
        view=inflater.inflate(R.layout.record_production,container,false);
        UpdateProductionRecord();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Recor_Adapter_3(getContext(),getAllProductionRecord());
        recyclerView.setAdapter(adapter);
        return view;
    }
    public Cursor getAllProductionRecord() {
        db_helper=new DbHelper(getContext());
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Production_Record._ID, Contracts.Production_Record.COLUMN_1, Contracts.Production_Record.COLUMN_2,Contracts.Production_Record.COLUMN_3,Contracts.Production_Record.COLUMN_4,Contracts.Production_Record.COLUMN_5,Contracts.Production_Record.COLUMN_6};
        Cursor cr = mDb.query(Contracts.Production_Record.TABLE_NAME, columns, Contracts.Production_Record.COLUMN_6 + " = '" + id + "'", null, null, null, null);
        return cr;
    }
    private Cursor getAllSale() {
        SQLiteDatabase mDb=db_helper.getReadableDatabase();
        String[] columns=new String[]{Contracts.Sale_ProduDeteil_Table._ID,Contracts.Sale_ProduDeteil_Table.COLUMN_1,Contracts.Sale_ProduDeteil_Table.COLUMN_2,Contracts.Sale_ProduDeteil_Table.COLUMN_3,Contracts.Sale_ProduDeteil_Table.COLUMN_4,Contracts.Sale_ProduDeteil_Table.COLUMN_5,Contracts.Sale_ProduDeteil_Table.COLUMN_6};
        Cursor cr=mDb.query(Contracts.Sale_ProduDeteil_Table.TABLE_NAME,columns,null,null,null,null,null);
        return cr;

    }
    public void UpdateProductionRecord() {
       Cursor c=getAllProductionRecord();
        String Sale = "0",Left="0";
       while (c.moveToNext()) {
           long id = c.getInt(c.getColumnIndex(Contracts.Production_Record._ID));
           String Date = c.getString(c.getColumnIndex(Contracts.Production_Record.COLUMN_1));
           String Name = c.getString(c.getColumnIndex(Contracts.Production_Record.COLUMN_2));
           String pro = c.getString(c.getColumnIndex(Contracts.Production_Record.COLUMN_3));
            Sale=getSale(Date,Name);
            Left=Double.toString(Double.parseDouble(pro)-Double.parseDouble(Sale));
           SQLiteDatabase mDb = db_helper.getWritableDatabase();
           ContentValues args = new ContentValues();
            args.put(Contracts.Production_Record.COLUMN_4, Sale);
            args.put(Contracts.Production_Record.COLUMN_5, Left);
           mDb.update(Contracts.Production_Record.TABLE_NAME, args, Contracts.Production_Record._ID + " = " + id, null);
       }
       c.close();
    }
    public String getSale(String d,String n){
        String Sale = "0";
        Cursor c1=getAllSale();
        while (c1.moveToNext()) {
            String name = c1.getString(c1.getColumnIndex(Contracts.Sale_ProduDeteil_Table.COLUMN_3));
            String da = c1.getString(c1.getColumnIndex(Contracts.Sale_ProduDeteil_Table.COLUMN_4));
            String spilt[] = da.split("-");
            String m = spilt[0], y = spilt[2], date = m + "-" + y;
            if (d.equals(date) && n.equals(name)) {
                String sale = c1.getString(c1.getColumnIndex(Contracts.Sale_ProduDeteil_Table.COLUMN_1));
                Sale = Double.toString(Double.parseDouble(sale) + Double.parseDouble(Sale));
                // System.out.println("Sale  "+sale);
            }
        }
        c1.close();
        return Sale;
    }
}
