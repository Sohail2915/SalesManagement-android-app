package com.kk.kamranqadeer.salemanager.Tab;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.MessageQueue;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kk.kamranqadeer.salemanager.AllAdapters.Recor_Adapter_1;
import com.kk.kamranqadeer.salemanager.Date_Base.Contracts;
import com.kk.kamranqadeer.salemanager.Date_Base.DbHelper;
import com.kk.kamranqadeer.salemanager.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;
import java.util.function.DoublePredicate;

/**
 * Created by kamran qadeer on 8/30/2018.
 */

@SuppressLint("ValidFragment")
public class Tab1 extends Fragment {
    DbHelper db_helper;
    Recor_Adapter_1 adapter;
    RecyclerView recyclerView;
    String Date;
    long id;
    public Tab1(long id,String Date){
        this.Date=Date;
        this.id=id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        db_helper = new DbHelper(getContext());
        view=inflater.inflate(R.layout.record_materil,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Recor_Adapter_1(getContext(), getAllMaterialName());
        recyclerView.setAdapter(adapter);
        Material_Record_update();
        return view;
    }
    public Cursor getAllMaterialName() {
            db_helper=new DbHelper(getContext());
            SQLiteDatabase mDb = db_helper.getReadableDatabase();
            String[] columns = new String[]{Contracts.Material_record._ID, Contracts.Material_record.COLUMN_1, Contracts.Material_record.COLUMN_2,Contracts.Material_record.COLUMN_3, Contracts.Material_record.COLUMN_4, Contracts.Material_record.COLUMN_5, Contracts.Material_record.COLUMN_6};
            Cursor cr = mDb.query(Contracts.Material_record.TABLE_NAME, columns, Contracts.Material_record.COLUMN_6 + " = '" + id + "'", null, null, null, null);
            return cr;
    }
    private Cursor getAllPurch() {
        SQLiteDatabase  mDb=db_helper.getReadableDatabase();
        Cursor cr=mDb.rawQuery("select * from "+ Contracts.Purch_Table.TABLE_NAME,null);
        return cr;
    }
    public Cursor getAllpurchMaterial(long id) {
        SQLiteDatabase mDb = db_helper.getReadableDatabase();
        String[] columns = new String[]{Contracts.Purch_Detail_Table._ID, Contracts.Purch_Detail_Table.COLUMN_1, Contracts.Purch_Detail_Table.COLUMN_6};
        Cursor cr = mDb.query(Contracts.Purch_Detail_Table.TABLE_NAME, columns, Contracts.Purch_Detail_Table.COLUMN_5 + " = '" + id + "'", null, null, null, null);
        return cr;
    }
    public void Material_Record_update(){
        SQLiteDatabase mdb=db_helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        //first getting purchasing table id
        DecimalFormat f=new DecimalFormat("#0.00");
        ArrayList<Long> ID=new ArrayList<Long>();
        Cursor c1=getAllPurch(),c2=getAllMaterial(),c3=getAllMaterialName();
        while (c1.moveToNext()){
            String date=c1.getString(c1.getColumnIndex(Contracts.Purch_Table.COLUMN_3));
            String[] items1 = date.split("-");
            String m=items1[0],y=items1[2],D=m+"-"+y;
            if(D.equals(Date)){
               long id=c1.getInt(c1.getColumnIndex(Contracts.Purch_Table._ID));
               ID.add(id);
            }
            System.out.println("id="+id);
        }

        while (c3.moveToNext()){
            String name=c3.getString(c3.getColumnIndex(Contracts.Material_record.COLUMN_2));
            String Used=c3.getString(c3.getColumnIndex(Contracts.Material_record.COLUMN_4));
            long id=c3.getInt(c3.getColumnIndex(Contracts.Material_record._ID));
            String split[]=Used.split(" ");
            String M_Used=PurchMaterialQuntity(name,ID);
            String Left=Double.toString( Double.parseDouble(M_Used) - Double.parseDouble(split[0]));
            values.put(Contracts.Material_record.COLUMN_3, M_Used+" "+split[1]);
            values.put(Contracts.Material_record.COLUMN_5, Left+" "+split[1]);
            mdb.update(Contracts.Material_record.TABLE_NAME, values,Contracts.Material_record._ID+" = "+id,null);
        }
        c3.close();
    }
    public String PurchMaterialQuntity(String M_Name,ArrayList<Long> id){
        // geting how much material is purchased
        String M_Quntity="0.0";
        for (int i=0;i<id.size();i++){
        Cursor c1=getAllpurchMaterial(id.get(i));
        DecimalFormat format=new DecimalFormat("#0.00");
        if(c1.getCount()!=0) {
            while (c1.moveToNext()) {
                String Name = c1.getString(c1.getColumnIndex(Contracts.Purch_Detail_Table.COLUMN_1));
                if (Name.equals(M_Name)) {
                    String Quntity = c1.getString(c1.getColumnIndex(Contracts.Purch_Detail_Table.COLUMN_6));
                    M_Quntity = format.format(Double.parseDouble(Quntity) + Double.parseDouble(M_Quntity));
                }
            }
        }
        c1.close();
        }
       return M_Quntity;
    }
    private Cursor getAllMaterial() {
        SQLiteDatabase mDb=db_helper.getReadableDatabase();
        Cursor cr=mDb.rawQuery("select * from "+ Contracts.Sale_Table.TABLE_NAME,null);
        //  cr = db.query(Contract.TableDetail.TABLE_NAME, null, null, null, null,null, null);
        return cr;
    }
}
